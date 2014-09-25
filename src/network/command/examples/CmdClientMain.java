package network.command.examples;

import java.io.IOException;
import java.net.UnknownHostException;
import network.command.users.CommandClient;


public class CmdClientMain {

	public static void main(String[] args) throws IOException {
		CommandClient c=new CommandClient();	
		try {
			new ClientListeners(c);
			c.connect("localhost", 1055, args[0]);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
