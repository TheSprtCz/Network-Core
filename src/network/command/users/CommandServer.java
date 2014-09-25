package network.command.users;

import java.io.IOException;

import network.command.interfaces.CommandListener;
import network.command.source.CommandHandler;
import network.command.source.CommandInfo;
import network.command.source.CommandStorage;
import network.core.source.ClientInfo;
import network.core.users.NetworkServer;

public class CommandServer extends NetworkServer {
	CommandStorage cmd=CommandStorage.getInstance();
	public void registerCommand(String name,int arguments,String usage, CommandListener listener){
		cmd.cmdlisteners.add(new CommandInfo(name, arguments,usage,listener));
	}
	public void setDefaultCommand(CommandListener commandListener){
		cmd.setDefaultCommand(new CommandInfo("default",CommandStorage.UNLIMITED,"",commandListener));
	}
	public void registerCommand(String name,int min,int max,String usage, CommandListener listener){
		cmd.cmdlisteners.add(new CommandInfo(name, min,max,usage,listener));
	}
	public void create(String hostname, int port) throws IOException{
		super.create(hostname,port);
		new Thread(new CommandHandler()).start();
	}
	public void create(int port) throws IOException{
		super.create(port);
		new Thread(new CommandHandler()).start();
	}
	public CommandStorage getCommandStorage(){
		return cmd;
	}
	public void rebroadcast(String sender, Object o) throws IOException{
		for(ClientInfo c:getNetworkStorage().clients){
			if(!c.getNick().equals(sender)){
				c.send(sender,o);
			}
		}
	}
	public void rebroadcast(String sender, Object o,String header) throws IOException{
		for(ClientInfo c:getNetworkStorage().clients){
			if(!c.getNick().equals(sender)){
				c.send(sender,o,header);
			}
		}
	}
}
