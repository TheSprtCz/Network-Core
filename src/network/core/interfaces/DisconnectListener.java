package network.core.interfaces;

import java.net.Socket;

public interface DisconnectListener {
	public void Disconnect(Socket s, Exception e, String reason, boolean kicked);
}
