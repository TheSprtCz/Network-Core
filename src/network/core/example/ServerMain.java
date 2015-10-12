package network.core.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import network.core.annotations.Annotations.ClientConnectAnnotation;
import network.core.annotations.Annotations.ClientDisconnectAnnotation;
import network.core.annotations.Annotations.PacketReceiveAnnotation;
import network.core.interfaces.ClientConnectListener;
import network.core.interfaces.ClientDisconnectListener;
import network.core.interfaces.PacketReceiveListener;
import network.core.source.ClientInfo;
import network.core.source.MessagePacket;
import network.core.source.NetworkStorage;
import network.core.users.NetworkServer;


public class ServerMain {
	final static NetworkServer s = new NetworkServer();
	final NetworkStorage sk = s.sk;
	@ClientConnectAnnotation
	ClientConnectListener t=new ClientConnectListener(){
		@Override
		public void clientConnect(ClientInfo c) {
			System.out.println("Client připojen "+(String) c.getNick());			
		}			
	};
	@ClientDisconnectAnnotation
	ClientDisconnectListener d=new ClientDisconnectListener(){

		@Override
		public void clientDisconnect(ClientInfo c,Exception e, String reason, boolean kicked) {
			System.out.println("Client odpojen: "+c.getNick());
			
		}
		
	};
	@PacketReceiveAnnotation(header = "re-broadcast")
	PacketReceiveListener p=new PacketReceiveListener(){

		@Override
		public void packetReceive(MessagePacket p) {
			System.out.println(p.getNick()+":"+(String) p.getObject());
			s.broadcast(p.getNick()+":"+(String) p.getObject(),"msg");
		}
		
	};
	public static void main(String[] args) {
        BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in));
		s.registerClass(new ServerMain());
		try {
			s.create("localhost",1055);
            String userInput;
			while ((userInput = stdIn.readLine()) != null) {
                s.broadcast(userInput,"msg");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
