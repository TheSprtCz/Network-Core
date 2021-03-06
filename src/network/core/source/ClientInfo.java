package network.core.source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ClientInfo {
	private int port;
	private String hostName, nick;
	private String reason = "Disconnected";
	private Socket socket;
	private boolean initialized, kicked = false;
	private ObjectInputStream inputStream;
	private PacketReceiveHandler thread;
	private LinkedList<MessagePacket> queue = new LinkedList<MessagePacket>();
	private SendThread send;
	private Map<String, Object> atributes = new HashMap<String, Object>();

	public ClientInfo(String nick, Socket socket,
			ObjectInputStream i, ObjectOutputStream o) {
		this.port = socket.getPort();
		this.hostName = socket.getRemoteSocketAddress().toString();
		this.nick = nick;
		this.setSocket(socket);
		this.inputStream = i;
		this.send = new SendThread(o, queue);
		thread = new PacketReceiveHandler(inputStream, nick);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void send(Serializable o, String header) {
		send("Server",o,header);
	}

	public void send(String nick, Serializable o, String header) {
		if(!socket.isClosed() && initialized && !socket.isOutputShutdown()){
	    	synchronized ( queue ) {
	    		queue.add(new MessagePacket(nick,header, o));
	    		queue.notify();
	    	}	
	    		//o.flush();
		}
	}
	
	public void send(MessagePacket packet){
		send(packet.getNick(),packet.getObject(),packet.getHeader());
	}
	public void remove() {
		try {
			thread.interrupt();
			send.interrupt();
			if (!socket.isClosed()) {
				socket.close();
			}
		}
		// socket.close();
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void kick(String reason) {
		send(reason, "corekick");
		setKicked(true);
		setReason(reason);
		NetworkStorage.getInstance().disconnectClient(this, new IOException("Kicked"));
	}

	public Map<String, Object> getAtributes() {
		return atributes;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isKicked() {
		return kicked;
	}

	public void setKicked(boolean kicked) {
		this.kicked = kicked;
	}
}
