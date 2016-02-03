package network.core.source;

import java.io.IOException;
import java.io.ObjectInputStream;
import network.core.users.NetworkClient;

public class PacketReceiveHandler extends Thread{
	private ObjectInputStream input;
	private NetworkStorage sk=NetworkStorage.getInstance();
	private String nick;
	private NetworkClient client;
	@Override
	public void run() {
		try{
        	MessagePacket packet;
        	//input.readObject();
        	
        	while (!interrupted()) {
        		Object o = input.readObject();
        		if(o instanceof MessagePacket){
	        		packet = (MessagePacket) o;
					if (packet != null) {
						sk.callReceiveEvent(packet);
//						if (client == null) {
//							sk.callReceiveEvent(new MessagePacket(packet.getNick(),
//									"clientCheck", null));
//						} else {
//							sk.callReceiveEvent(new MessagePacket(packet.getNick(),
//									"serverCheck", null));
//						}
					}
        		}
        	}
        	//disconnect(new IOException("EOS"));
        }
        catch(Exception e){
        	e.printStackTrace();
        	disconnect(e);
        }	
	}
	public PacketReceiveHandler(ObjectInputStream input,NetworkClient s){
		super("PacketReceiveHandler - Client");
		this.input=input;
		this.client=s;
		super.start();
	}
	public PacketReceiveHandler(ObjectInputStream input,String nick){
		super("PacketReceiveHandler - "+nick);
		this.input=input;
		this.nick=nick;
		super.start();
	}
	public void disconnect(Exception e){
   		if(client!=null){
   			try {
				client.disconnect();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Tohle by se stávat an serveru nemělo");
				e1.printStackTrace();
			}
   			sk.callDisconnectEvent(client.getSocket(),e);
   		}
   		else{
   			System.out.println("Removing client "+nick);
   			sk.disconnectClient(nick, e);
   		}
	}
}
