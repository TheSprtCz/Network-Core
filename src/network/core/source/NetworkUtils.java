package network.core.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class NetworkUtils {
	
	public static byte[] serialize(Serializable ser) throws IOException{
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutputStream out = new ObjectOutputStream(bos);
	    out.writeObject(ser);
	    return bos.toByteArray();
	}
	
	public static <T> T deserialize(Class<T> cls, byte[] b) throws IOException, ClassNotFoundException{
	    ByteArrayInputStream bis = new ByteArrayInputStream(b);
	    ObjectInputStream in = new ObjectInputStream(bis);
	    return (T) in.readObject();
	}
	public static MessagePacket readNext(DataInputStream input) throws IOException, ClassNotFoundException {
		byte[] bytes = new byte[input.readInt()];
		input.readFully(bytes);
		return NetworkUtils.deserialize(MessagePacket.class, bytes);
	}
}
