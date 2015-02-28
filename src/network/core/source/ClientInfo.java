package network.core.source;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientInfo {
	private int port;
	private String hostName, nick;
	private Socket socket;
	private boolean initialized;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private PacketReceiveHandler thread;
	private Map<String, Object> atributes = new HashMap<String, Object>();

	public ClientInfo(String nick, Socket socket,
			ObjectInputStream i, ObjectOutputStream o) {
		this.port = socket.getPort();
		this.hostName = socket.getRemoteSocketAddress().toString();
		this.nick = nick;
		this.setSocket(socket);
		this.inputStream = i;
		this.outputStream = o;
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

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void send(Object o, String header) {
		try {
			outputStream.writeObject(new MessagePacket("Server", header, o));
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			remove();
		}
	}

	public void send(String nick, Object o, String header) {
		try {
			outputStream.writeObject(new MessagePacket(nick, header, o));
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			remove();
		}
	}
	
	public void send(MessagePacket packet){
		send(packet.getNick(),packet.getObject(),packet.getHeader());
	}
	public void remove() {
		try {
			if (!socket.isInputShutdown()) {
				socket.shutdownInput();
			}
			if (!socket.isOutputShutdown()) {
				socket.shutdownOutput();
			}
			if (!socket.isClosed()) {
				socket.close();
			}
		}
		// socket.close();
		catch (IOException e) {
			e.printStackTrace();
		}
		thread.interrupt();
	}

	public void kick(String reason) {
		send(reason, "corekick");
		remove();
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
}
