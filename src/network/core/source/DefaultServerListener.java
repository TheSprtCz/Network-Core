package network.core.source;

import network.core.annotations.Annotations.PacketReceiveAnnotation;
import network.core.interfaces.PacketReceiveListener;
import network.core.users.NetworkServer;

public class DefaultServerListener {
	private NetworkServer s;
	private NetworkStorage storage = NetworkStorage.getInstance();
	@PacketReceiveAnnotation(header = "coredsc")
	private PacketReceiveListener coredsc= new PacketReceiveListener(){
		@Override
		public void packetReceive(MessagePacket p) {
			storage.getClientByName(p.getNick()).setReason((String) p.getObject());			
		}		
	};
	
	public DefaultServerListener(NetworkServer s){
		this.s = s;
		s.registerClass(this);
	}
	
}
