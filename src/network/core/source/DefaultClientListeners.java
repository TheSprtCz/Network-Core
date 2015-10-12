package network.core.source;

import java.io.IOException;

import network.core.annotations.Annotations.PacketReceiveAnnotation;
import network.core.interfaces.PacketReceiveListener;
import network.core.users.NetworkClient;

public class DefaultClientListeners {
	private NetworkClient c;
	private NetworkStorage storage = NetworkStorage.getInstance();
	@PacketReceiveAnnotation(header = "clientCheck")
	private PacketReceiveListener check = new PacketReceiveListener() {
		@Override
		public void packetReceive(MessagePacket p) {
			try {
				c.send("test", "clientCheck");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	@PacketReceiveAnnotation(header = "reason")
	private PacketReceiveListener rsn = new PacketReceiveListener(){
		@Override
		public void packetReceive(MessagePacket p) {
			storage.reason = (String) p.getObject();
		}		
	};
	@PacketReceiveAnnotation(header = "corekick")
	private PacketReceiveListener kick = new PacketReceiveListener() {
		@Override
		public void packetReceive(MessagePacket p) {
			storage.reason = (String) p.getObject();
			storage.kicked = true;
		}
	};

	public DefaultClientListeners(NetworkClient c) {
		this.c = c;
		c.registerClass(this);
	}
}
