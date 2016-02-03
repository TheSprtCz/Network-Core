package network.core.source;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

public class SendThread extends Thread {

	public SendThread(ObjectOutputStream stream,LinkedList<MessagePacket> queue) {
		this.output = stream;
		this.queue = queue;
		super.start();
		super.setName("SendThread");
	}
	public SendThread(ObjectOutputStream stream,LinkedList<MessagePacket> queue, String id) {
		this(stream, queue);
		super.setName("SendThread - "+id);
	}
    private Queue<MessagePacket> queue;
    private ObjectOutputStream output;
     
    @Override
    public void run() {
        while ( true ) {
            try {
            	MessagePacket obj;
 
                synchronized ( queue ) {
                    while ( queue.isEmpty() )
                        queue.wait();
                    //System.out.println("zpracovávám"); 
                    // Get the next work item off of the queue
                    obj = queue.remove();
                }
 
                // Process the work item
                output.writeObject(obj);
                //output.flush();
            }
            catch ( InterruptedException ie ) {
                break;  // Terminate
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }   

}
