package network.command.source;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class BufferOutputStream extends OutputStream {
	private StringBuffer str = new StringBuffer();
	private PrintStream old;
    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException
    {
        final String text = new String (buffer, offset, length);     	
        str.append(text);
    }

    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
    public void resetBuffer(){
    	this.str = str.delete(0, str.length());
    }
    public String getString(){
    	return str.toString();
    }
    public void start(){
    	old = System.out;
    	System.setOut(new PrintStream(this));
    }
    public String finish(){
    	System.setOut(old);
    	String result = getString();
    	resetBuffer();
    	return result;
    }
}
