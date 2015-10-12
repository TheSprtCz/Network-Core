package network.core.source;

import java.io.IOException;

import network.core.users.NetworkClient;

public class ClientSyncThread extends Thread {
//	private AtomicBoolean checked = new AtomicBoolean(true); 
	private int timeout;
	private NetworkClient client;
//    @PacketReceiveAnnotation(header = "serverCheck")
//    private PacketReceiveListener serverCheck = new PacketReceiveListener(){
//
//		@Override
//		public void packetReceive(MessagePacket p) {
//			checked.lazySet(true);
//		}
//    	
//    };
    @Override
    public void run(){
    	while(!interrupted()){
    		try{
    			client.send(0,"serverCheck");
//				if (!checked.get()) {
//					System.out.println("dis");
//					client.getNetworkStorage().reason = "ClientTimeout";
//					client.disconnect();
//					client.getNetworkStorage().callDisconnectEvent(client.getSocket(),new IOException("Timeout"));
//				}
//				checked.lazySet(false);
				Thread.sleep(timeout);
			} catch (InterruptedException | IOException e) {
				this.interrupt();
			}
    	}
    }
    public ClientSyncThread(int timeout,NetworkClient cln){
    	super("ClientSyncThread");
    	this.client = cln;
    	this.timeout = timeout;
    	super.start();
    }
}
