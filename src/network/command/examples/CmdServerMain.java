package network.command.examples;

import java.io.IOException;
import network.command.users.CommandServer;


public class CmdServerMain {

	public static void main(String[] args) throws IOException {
		final CommandServer s=new CommandServer();
		s.create(args[0],1055);
		new ServerListeners(s);
	}
}
