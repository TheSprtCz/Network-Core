package network.core.source;

import network.core.annotations.Annotations.PacketReceiveAnnotation;
import network.core.interfaces.PacketReceiveListener;
import network.core.users.NetworkClient;

public class DefaultListeners {
	private NetworkClient c;
	private NetworkStorage storage = NetworkStorage.getInstance();

    public DefaultListeners(NetworkClient c){
    	this.c = c;
    	c.registerClass(this);
    }
    @PacketReceiveAnnotation(header = "clientCheck")
    private PacketReceiveListener check = new PacketReceiveListener(){
		@Override
		public void packetReceive(MessagePacket p) {
			c.send("test","clientCheck");
		}    	
    };
    @PacketReceiveAnnotation(header = "corekick")
    private PacketReceiveListener kick = new PacketReceiveListener(){
		@Override
		public void packetReceive(MessagePacket p) {
			storage.reason = (String) p.getObject();
			storage.kicked = true;
		}    	
    };
}
