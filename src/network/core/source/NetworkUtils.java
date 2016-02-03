package network.core.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class NetworkUtils {

	public static byte[] serialize(Object object) throws IOException {
	    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
	         ObjectOutput out = new ObjectOutputStream(bos)) {
	        out.writeObject(object);
	        return bos.toByteArray();
	    } 
	}
	
	public static <T> T deserialize(byte[] bytes,Class<T> Class) throws IOException, ClassNotFoundException {
	    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	         ObjectInput in = new ObjectInputStream(bis)) {
	        return (T) in.readObject();
	    } 
	}
	public static MessagePacket readNext(DataInputStream input) throws IOException, ClassNotFoundException {
		byte[] bytes = new byte[input.readInt()];
		input.readFully(bytes);
		return NetworkUtils.deserialize(bytes, MessagePacket.class);
	}
}
