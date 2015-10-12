package network.core.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import network.core.interfaces.ConnectListener;
import network.core.interfaces.DisconnectListener;
import network.core.interfaces.PacketReceiveListener;
import network.core.source.MessagePacket;
import network.core.users.NetworkClient;
import network.core.annotations.Annotations.*;

public class ClientMain {
	static NetworkClient c;
	@ConnectAnnotation
	ConnectListener t=new ConnectListener(){
		@Override
		public void Connect(Socket c) {
			System.out.println("Připojeno k serveru");
		}			
	};
	@DisconnectAnnotation
	DisconnectListener d=new DisconnectListener(){

		@Override
		public void Disconnect(Socket s, Exception e, String reason, boolean kicked) {
			System.out.println("Odpojeno od serveru: "+reason);
			System.exit(1);
			
		}
		
	};
	@PacketReceiveAnnotation(header = "msg")
	PacketReceiveListener p=new PacketReceiveListener(){

		@Override
		public void packetReceive(MessagePacket p) {
			System.out.println(p.getNick()+":"+(String) p.getObject());
							
		}
		
	};
	public static void main(String[] args) {
		c=new NetworkClient();
		BufferedReader stdIn = new BufferedReader(
		    new InputStreamReader(System.in));
		c.registerClass(new ClientMain());
		try {
			c.connect("localhost", 1055, args[0]);
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
               c.send(userInput,"msg");
            }        
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
