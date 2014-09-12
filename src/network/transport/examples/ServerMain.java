package network.transport.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import network.core.interfaces.ClientConnectListener;
import network.core.interfaces.ClientDisconnectListener;
import network.core.interfaces.PacketReceiveListener;
import network.core.source.ClientInfo;
import network.core.source.MessagePacket;
import network.core.users.NetworkServer;
import network.transport.interfaces.CommandListener;
import network.transport.users.CommandServer;


public class ServerMain {

	public static void main(String[] args) throws IOException {
		final CommandServer s=new CommandServer();
		ClientConnectListener t=new ClientConnectListener(){
			@Override
			public void clientConnect(ClientInfo c) {
				System.out.println("Client připojen "+(String) c.getNick());			
			}			
		};
		ClientDisconnectListener d=new ClientDisconnectListener(){

			@Override
			public void clientDisconnect(ClientInfo c) {
				System.out.println("Client odpojen: "+c.getNick());
				
			}
			
		};
		PacketReceiveListener p=new PacketReceiveListener(){

			@Override
			public void packetReceive(MessagePacket p) {
				System.out.println(p.getNick()+":"+(String) p.getObject());
				ClientInfo c=s.sk.getClientByName(p.getNick());
				try {
					s.broadcast(p.getNick(),p.getObject());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		s.addClientConnectListener(t);
		s.addClientDisconnectListener(d);
		s.create(args[0],1055);
		s.addReceiveListener(p);
		s.registerCommand("broadcast",-1,"Broadcast <Message>", new CommandListener(){

				@Override
				public void CommandExecuted(List<String> args) {
					try {
						String b = "";
						for(String x:args){
							b+=" "+x;
						}
						s.broadcast(b);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
		});
		s.registerCommand("send",2,"Send <Message> <ID>", new CommandListener(){

			@Override
			public void CommandExecuted(List<String> args) {
				try {					
					s.sk.clients.get(Integer.valueOf(args.get(1))).send(args.get(0));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
	});

	}
}
