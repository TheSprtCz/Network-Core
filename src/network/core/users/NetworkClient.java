package network.core.users;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import network.core.annotations.AnnotationChecker;
import network.core.interfaces.ConnectListener;
import network.core.interfaces.DisconnectListener;
import network.core.interfaces.PacketReceiveListener;
import network.core.source.DefaultClientListeners;
import network.core.source.MessagePacket;
import network.core.source.PacketReceiveHandler;
import network.core.source.SendThread;
import network.core.source.ClientSyncThread;

public class NetworkClient extends AbstractNetworkUser{
    private Socket socket=null;
    private String nick;
    private PacketReceiveHandler PacketReceiveThread;
    private ClientSyncThread ClientSyncThread;
    private SendThread send;
    private LinkedList<MessagePacket> queue = new LinkedList<MessagePacket>();
    //private ObjectOutputStream o;
    private ObjectInputStream i;

    public void addDisconnectListener(DisconnectListener l){
    	sk.disconnectListeners.add(l);
    }
    public void addConnectListener(ConnectListener l){
    	sk.connectListeners.add(l);
    }
    public void addKickListener(PacketReceiveListener l){
    	addReceiveListener(l,"corekick");
    }
    public NetworkClient(){
    	socket = new Socket();
    	sk.reset();
    	super.start();
    }
    public ObjectInputStream getInputStream(){
    	return i;
    }
    public void connect(int timeout, String host, int port, String nick) throws IOException,UnknownHostException{
    	socket.connect(new InetSocketAddress(host, port), 1000);
    	socket.setSoTimeout(timeout*2);
    	this.nick=nick;
    	i=new ObjectInputStream(socket.getInputStream());
    	System.out.println("Před vlákenm");
    	send = new SendThread(new ObjectOutputStream(socket.getOutputStream()),queue);
    	PacketReceiveThread = new PacketReceiveHandler(i,this);
    	System.out.println("Za vláknem");
    	//o.writeObject(null);
    	send(0,"connect");
    	//o.writeObject(null);
    	//o.flush();
    	new DefaultClientListeners(this);
    	ClientSyncThread = new ClientSyncThread(timeout/2,this);
    	sk.callConnectEvent(socket);
    }
    public void connect(String host, int port, String nick) throws UnknownHostException, IOException{
    	connect(sk.defaultclientTimeout,host,port,nick);
    }
    public void close() throws IOException{
    	socket.close();
    }
    public Socket getSocket(){
    	return socket;
    }
    public void send(Serializable ob,String header){
    	if(!socket.isOutputShutdown()){
    		synchronized ( queue ) {
    			queue.add(new MessagePacket(nick,header, ob));
    			queue.notify();
    		}	
    		//o.flush();
    	}
    }
    public void disconnect() throws IOException{
    	if(socket.isConnected()){
    		send.interrupt();
    		PacketReceiveThread.interrupt();
    		ClientSyncThread.interrupt();
    		if(!socket.isClosed()){
    			socket.close();
    		}
    	}
    	this.interrupt();
    	//socket.close();    	    	
    }
    public void disconnect(String reason,boolean kicked) throws IOException{
    	send(reason,"coredsc");
    	sk.reason = reason;
    	sk.kicked = kicked;
    	disconnect();
    }
    public void disconnect(String reason) throws IOException{
    	disconnect(reason,false);
    }
    public void registerClass(Object obj){
    	try {
			new AnnotationChecker(obj,this).processClass();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}


