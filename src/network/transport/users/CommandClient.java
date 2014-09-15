package network.transport.users;

import java.io.IOException;
import java.net.UnknownHostException;

import network.core.users.NetworkClient;
import network.transport.interfaces.CommandListener;
import network.transport.source.CommandHandler;
import network.transport.source.CommandInfo;
import network.transport.source.CommandStorage;

public class CommandClient extends NetworkClient {
	private CommandStorage cmd=CommandStorage.getInstance();
	public void registerCommand(String name,int arguments,String usage, CommandListener listener){
		cmd.cmdlisteners.add(new CommandInfo(name, arguments,usage,listener));
	}
	public void connect(String hostName,int port, String name) throws UnknownHostException, IOException{
		super.connect(hostName, port, name);
		new Thread(new CommandHandler()).start();
	}
	
}
