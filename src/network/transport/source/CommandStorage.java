package network.transport.source;

import java.util.ArrayList;
import java.util.List;

public class CommandStorage {
	private static CommandStorage instance;
	public List<CommandInfo> cmdlisteners=new ArrayList<>();
	
	public static CommandStorage getInstance(){
		if(instance==null){
			instance=new CommandStorage();
		}
		return instance;
	}
}
