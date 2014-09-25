package network.command.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import network.command.interfaces.CommandListener;
import network.command.source.CommandStorage;
import network.command.users.CommandServer;
import network.core.interfaces.ClientConnectListener;
import network.core.interfaces.ClientDisconnectListener;
import network.core.interfaces.PacketReceiveListener;
import network.core.source.ClientInfo;
import network.core.source.MessagePacket;

public class ServerListeners {
	private CommandServer server;
	ClientConnectListener connect=new ClientConnectListener(){
		@Override
		public void clientConnect(ClientInfo c) {
			System.out.println("Client připojen "+(String) c.getNick());
			try {
				server.rebroadcast(c.getNick(), c.getNick(),"connect");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
	};
	ClientDisconnectListener disconnect=new ClientDisconnectListener(){

		@Override
		public void clientDisconnect(ClientInfo c) {
			System.out.println("Client odpojen: "+c.getNick());
			try {
				server.rebroadcast(c.getNick(), c.getNick(),"disconnect");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	};
	PacketReceiveListener rebroadcast=new PacketReceiveListener(){

		@Override
		public void packetReceive(MessagePacket p) {
			System.out.println(p.getNick()+":"+(String) p.getObject());
			ClientInfo c=server.getNetworkStorage().getClientByName(p.getNick());
			try {
				server.rebroadcast(c.getNick(), p.getObject(),"broadcast");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	CommandListener broadcast= new CommandListener(){
		@Override
		public void CommandExecuted(List<String> args) {
			try {
				server.broadcast(server.getCommandStorage().ArgsString(0, args));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	};
	CommandListener defaultsend=new CommandListener(){
		@Override
		public void CommandExecuted(List<String> args) {
			try {
				server.broadcast(server.getCommandStorage().ArgsString(0, args), "broadcast");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
		
	};
	CommandListener kick=new CommandListener(){
		@Override
		public void CommandExecuted(List<String> args) {
			try {					
				ClientInfo ci = server.getNetworkStorage().getClientByName(args.get(0));
				ci.kick();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	};
	CommandListener list=new CommandListener(){
		@Override
		public void CommandExecuted(List<String> args) {	
			for(ClientInfo c:server.getNetworkStorage().clients){
				System.out.println(c.getNick()+":"+c.getHostName());
			}			
		}			
	};
	CommandListener send=new CommandListener(){
		@Override
		public void CommandExecuted(List<String> args) {
			try {					
				ClientInfo client=server.getNetworkStorage().getClientByName(args.get(0));;
				client.send(server.getCommandStorage().ArgsString(1, args));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	};
	PacketReceiveListener listreceive=new PacketReceiveListener(){
		@Override
		public void packetReceive(MessagePacket p) {
			ClientInfo asker=server.getNetworkStorage().getClientByName(p.getNick());
			List<String> names=new ArrayList<>();
			for(ClientInfo client:server.getNetworkStorage().clients){
				names.add(client.getNick());
			}	
			try {
				asker.send(names, "list");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	};
	public ServerListeners(CommandServer server){
		this.server=server;
		server.addReceiveListener(rebroadcast,"send");
		server.addReceiveListener(listreceive, "list");
		server.addClientConnectListener(connect);
		server.addClientDisconnectListener(disconnect);
		server.registerCommand("broadcast",CommandStorage.UNLIMITED,"Broadcast <Message>",broadcast);
		server.registerCommand("kick",1,"Send <Message> <ID>",kick);
		server.registerCommand("list",0,"List",list);
		server.registerCommand("send",2,CommandStorage.UNLIMITED,"Send <Message> <ID>", send);
		server.setDefaultCommand(defaultsend);
	}
}
