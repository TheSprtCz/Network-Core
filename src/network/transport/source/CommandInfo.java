package network.transport.source;

import network.transport.interfaces.CommandListener;

public class CommandInfo {
	private CommandListener listener;
	private String name;
	private int arguments=0;
	private String usage="";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CommandListener getListener() {
		return listener;
	}
	public void setListener(CommandListener listener) {
		this.listener = listener;
	}
	public CommandInfo(String name, CommandListener list){
		this.name=name;
		this.listener=list;
	}
	public CommandInfo(String name,int arguments, String usage, CommandListener list){
		this.name=name;
		this.listener=list;
		this.setArguments(arguments);
		this.setUsage(usage);
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public int getArguments() {
		return arguments;
	}
	public void setArguments(int arguments) {
		this.arguments = arguments;
	}
	
}
