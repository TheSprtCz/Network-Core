package network.command.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import network.command.interfaces.CommandListener;

public class CommandStorage {
	private static CommandStorage instance;
	private BufferOutputStream buffer = new BufferOutputStream();
	
	public CommandListener help=new CommandListener(){
		@Override
		public void CommandExecuted(List<String> args) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(Language.helpText+System.lineSeparator());
			for(CommandInfo cmd:cmdlisteners){
				buffer.append(cmd.getName()+": "+cmd.getHelp()+System.lineSeparator());
			}
			System.out.print(buffer.toString());
		}		
	};
	public static final int UNLIMITED=Integer.MAX_VALUE;
	public List<CommandInfo> cmdlisteners=new ArrayList<>();
	public CommandInfo defaultCommand=null;
	
	public static CommandStorage getInstance(){
		if(instance==null){
			instance = new CommandStorage();
		}
		return instance;
	}
	public String checkCommand(String userInput,boolean record) {
		if(record){
			buffer.start();
		}
		String[] str=userInput.split("\\s");
		String name=str[0];
		ArrayList<String> list=new ArrayList<String>(Arrays.asList(str));
		list.remove(name);
		for(CommandInfo c:cmdlisteners){
			if(c.getName().equals(name)){
				if(checkSize(c.getMin(),c.getMax(),list.size())){
					c.execute(list);
				}
				else{
					System.out.println(Language.usage+c.getUsage());
				}
				if(record){
					return buffer.finish();
				}
				return null;
			}
		}
		unknownCommand(userInput);
		if(record){
			return buffer.finish();
		}
		return null;
	}
	public void checkCommand(String input){
		checkCommand(input,false);
	}
	private void unknownCommand(String input) {
		if(defaultCommand!=null){
			defaultCommand.getListener().CommandExecuted(cutString(input));
			return;
		}
		System.out.println(Language.unknownCommand);
	}
	public boolean checkSize(int min,int max, int size) {
		if(size>=min&&size<=max){
			return true;
		}
		if(min==max&&min==UNLIMITED){
			return true;
		}
		if(size<min){
			System.out.println(Language.tooFewArguments);
		}
		if(size>max){
			System.out.println(Language.tooManyArguments);
		}					
		return false;
	}
	public String ArgsString(int pos,List<String> args){
		String builder="";
		for(int i=pos++;i<args.size();i++){
			builder+=" "+args.get(i);
		}
		return builder;
	}
	public CommandInfo getCommand(String name){
		for(CommandInfo c:cmdlisteners){
			if(c.getName().equals(name)){
				return c;
			}
		}
		return null;
	}
	public void setDefaultCommand(CommandInfo c){
		defaultCommand=c;
	}
	public void removeDefaultCommand(){
		defaultCommand=null;
	}
	public ArrayList<String> cutString(String userInput){
		String[] str=userInput.split("\\s");
		return new ArrayList<String>(Arrays.asList(str));
	}
	public void reset(){
		cmdlisteners=new ArrayList<>();
		defaultCommand=null;
	}
	public boolean executeCommand(String userInput){
		ArrayList<String> array=cutString(userInput);
		CommandInfo c=getCommand(array.get(0));
		array.remove(0);
		if(c!=null){
			c.execute(array);
			return true;
		}
		return false;
	}
	
}
