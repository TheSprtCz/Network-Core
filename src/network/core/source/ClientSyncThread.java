package network.core.source;

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
    			client.send(0,"clientCheck");
    			//System.out.println("sentCheck");
//				if (!checked.get()) {
//					System.out.println("dis");
//					client.getNetworkStorage().reason = "ClientTimeout";
//					client.disconnect();
//					client.getNetworkStorage().callDisconnectEvent(client.getSocket(),new IOException("Timeout"));
//				}
//				checked.lazySet(false);
				ClientSyncThread.sleep(timeout);
			} catch (InterruptedException e) {
				//System.out.println("Interrupted Syncthread");
				this.interrupt();
			}
    	}
    	//System.out.println("Interrupted Syncthread (vyběhnul ze smyčky)");
    }
    public ClientSyncThread(int timeout,NetworkClient cln){
    	super("ClientSyncThread");
    	System.out.println("Vytvořen ClientSyncThread");
    	this.client = cln;
    	this.timeout = timeout;
    	this.start();
    }
}
