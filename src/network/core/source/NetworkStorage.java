package network.core.source;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import network.core.interfaces.ClientConnectListener;
import network.core.interfaces.ClientDisconnectListener;
import network.core.interfaces.ConnectListener;
import network.core.interfaces.DisconnectListener;
import network.core.interfaces.PacketReceiveListener;

public class NetworkStorage {
	private static NetworkStorage instance;
	public String reason;
	public int defaultserverTimeout = 5000;
	public int defaultclientTimeout = 7000;
	public static String defaultReason = "Server closed";
	public boolean kicked = false;
	public static String version = "0.4c";
	//public ConcurrentMap<String,ClientInfo> clients=new ConcurrentHashMap<String,ClientInfo>();
	public CopyOnWriteArrayList<ClientInfo> clients=new CopyOnWriteArrayList<>();
	public ConcurrentMap<String,CopyOnWriteArrayList<PacketReceiveListener>> receiveListeners=new ConcurrentHashMap<String,CopyOnWriteArrayList<PacketReceiveListener>>();
	//public ConcurrentMap<PacketReceiveListener,String> receiveListeners=new ConcurrentHashMap<PacketReceiveListener,String>();
	public CopyOnWriteArrayList<ClientConnectListener> clientconnectListeners=new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<ConnectListener> connectListeners=new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<DisconnectListener> disconnectListeners=new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<ClientDisconnectListener> clientdisconnectListeners=new CopyOnWriteArrayList<>();
	public int port;

	public static NetworkStorage getInstance() {
		if(instance==null){
			instance = new NetworkStorage();
		}
		return instance;
	}
	public void callReceiveEvent(MessagePacket packet) {
		CopyOnWriteArrayList<PacketReceiveListener> listeners = receiveListeners.get(packet.getHeader());
		if(listeners!=null){
			for(PacketReceiveListener listener:listeners){
				listener.packetReceive(packet);
			}
		}
	}
	public void callClientConnectEvent(ClientInfo c){
		for(ClientConnectListener l:clientconnectListeners){
			l.clientConnect(c);
		}
		c.setInitialized(true);
		c.send(0,"serverCheck");
	}
	public void callDisconnectEvent(Socket s,Exception e){
		String reason = defaultReason;
		if(this.reason!=null){
			reason = this.reason;
		}
		for(DisconnectListener l:disconnectListeners){
			l.Disconnect(s,e,reason,kicked);
		}
	}
	public void callClientDisconnectEvent(ClientInfo c,Exception e){
		if(clients.contains(c)){
			for(ClientDisconnectListener l:clientdisconnectListeners){
				l.clientDisconnect(c,e,c.getReason(),c.isKicked());
			}
			clients.remove(c);
		}
	}
	public void callConnectEvent(Socket s){
		for(ConnectListener l:connectListeners){
			l.Connect(s);
		}
	}
	public ClientInfo getClientByName(String nick){
		for(ClientInfo ci:clients){
			String n = ci.getNick().replaceAll("[^\\x20-\\x7e]", "");
			if(n.equals(nick.replaceAll("[^\\x20-\\x7e]", ""))){
				return ci;
			}
		}
		return null;
	}
	public boolean isConnected(String nick){
		return getClientByName(nick)!=null;
	}
	public void disconnectClient(String nick,Exception e){
		if(getClientByName(nick)!=null){
			ClientInfo c = getClientByName(nick);
			callClientDisconnectEvent(c,e);
			c.remove();
		}	
	}
	public void disconnectClient(ClientInfo ci,Exception e){
		callClientDisconnectEvent(ci,e);
		ci.remove();
		//System.out.println("Disconnected");
	}
	public void kick(String nick, String reason){
		ClientInfo cln = getClientByName(nick);
		if(cln!=null){
			cln.kick(reason);
			//callClientDisconnectEvent(cln,new IOException("Kicked"));
		}
	}
	public void reset() {
		receiveListeners=new ConcurrentHashMap<String,CopyOnWriteArrayList<PacketReceiveListener>>();
		clientconnectListeners=new CopyOnWriteArrayList<>();
		connectListeners=new CopyOnWriteArrayList<>();
		disconnectListeners=new CopyOnWriteArrayList<>();
		clientdisconnectListeners=new CopyOnWriteArrayList<>();
		clients=new CopyOnWriteArrayList<>();
	}
}
