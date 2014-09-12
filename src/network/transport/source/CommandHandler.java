package network.transport.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandHandler implements Runnable {
	private CommandStorage cs=CommandStorage.getInstance();
    BufferedReader stdIn =
            new BufferedReader(
                new InputStreamReader(System.in));
	
	@Override
	public void run() {
        String userInput;
		try {
			while ((userInput = stdIn.readLine()) != null) {
				if(!checkCommand(userInput)){
					unknownCommand();
				};
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void unknownCommand() {
		System.out.println(Language.unknownCommand);		
	}

	private Boolean checkCommand(String userInput) {
		String[] str=userInput.split("\\s");
		String name=str[0];
		ArrayList<String> list=new ArrayList<String>(Arrays.asList(str));
		list.remove(name);
		for(CommandInfo c:cs.cmdlisteners){
			if(c.getName().equals(name)){
				if(checkSize(c.getArguments(),list.size())){
					c.getListener().CommandExecuted(list);
					return true;
				}
				System.out.println(Language.usage+c.getUsage());
				return true;
			}
		}
		return false;
		
	}

	private boolean checkSize(int arguments, int size) {
		if(arguments==size||arguments==-1){
			return true;
		}
		if(arguments>size){
			System.out.println(Language.tooFewArguments);
		}
		if(arguments<size){
			System.out.println(Language.tooManyArguments);
		}					
		return false;
	}

	
}
