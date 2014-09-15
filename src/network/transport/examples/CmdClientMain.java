package network.transport.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import network.core.interfaces.ClientConnectListener;
import network.core.interfaces.ClientDisconnectListener;
import network.core.interfaces.ConnectListener;
import network.core.interfaces.DisconnectListener;
import network.core.interfaces.PacketReceiveListener;
import network.core.source.ClientInfo;
import network.core.source.MessagePacket;
import network.core.users.NetworkServer;
import network.transport.interfaces.CommandListener;
import network.transport.users.CommandClient;
import network.transport.users.CommandServer;


public class CmdClientMain {

	public static void main(String[] args) throws IOException {
		final CommandClient c=new CommandClient();
		ConnectListener t=new ConnectListener(){
			@Override
			public void Connect(Socket c) {
				System.out.println("Připojeno k serveru");
			}			
		};
		DisconnectListener d=new DisconnectListener(){

			@Override
			public void Disconnect(Socket s) {
				System.out.println("Odpojeno od serveru");
				System.exit(1);
				
			}
			
		};
		PacketReceiveListener p=new PacketReceiveListener(){

			@Override
			public void packetReceive(MessagePacket p) {
				System.out.println(p.getNick()+":"+(String) p.getObject());
								
			}
			
		};
        BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in));
		c.addConnectListener(t);
		c.addDisconnectListener(d);
		c.addReceiveListener(p);
		try {
			c.connect("localhost", 1055, args[0]);      
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.registerCommand("send",-1,"Send <Message> <ID>", new CommandListener(){

			@Override
			public void CommandExecuted(List<String> args) {
				try {					
					for(String s:args){
						c.send(s);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
	});
		c.registerCommand("quit",1,"Send <Message> <ID>", new CommandListener(){

			@Override
			public void CommandExecuted(List<String> args) {
				try {					
					c.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
	});
	}
}
