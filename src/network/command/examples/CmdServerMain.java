package network.command.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

import network.command.users.CommandServer;


public class CmdServerMain {

	public static void main(String[] args) throws IOException {
		CommandServer s=new CommandServer();
		String help = "Použití: server.jar <IP> <Port>";
		try{
			s.create(args[0],Integer.parseInt(args[1]));
		}
		catch(IndexOutOfBoundsException|NumberFormatException e){
			System.out.println(help);
		}
		catch(SocketException e){
			System.out.println("Nepovedlo se vytvořit server na příslušně kombinace IP a portu "+args[0]+":"+args[1]);
		}		
		new ServerListeners(s);
		BufferedReader stdIn = new BufferedReader(
			    new InputStreamReader(System.in));
		String userInput;
		while ((userInput = stdIn.readLine()) != null) {
            s.getCommandStorage().checkCommand(userInput);
         }        
	}
}
