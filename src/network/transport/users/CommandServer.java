package network.transport.users;

import java.io.IOException;

import network.core.users.NetworkServer;
import network.transport.interfaces.CommandListener;
import network.transport.source.CommandHandler;
import network.transport.source.CommandInfo;
import network.transport.source.CommandStorage;

public class CommandServer extends NetworkServer {
	private CommandStorage cmd=CommandStorage.getInstance();
	public void registerCommand(String name,int arguments,String usage, CommandListener listener){
		cmd.cmdlisteners.add(new CommandInfo(name, arguments,usage,listener));
	}
	public void create(String hostname, int port) throws IOException{
		super.create(hostname,port);
		new Thread(new CommandHandler()).start();
	}
	public void create(int port) throws IOException{
		super.create(port);
		new Thread(new CommandHandler()).start();
	}
}
