package network.command.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandHandler extends Thread {
	private CommandStorage cs=CommandStorage.getInstance();
    BufferedReader stdIn =
            new BufferedReader(
                new InputStreamReader(System.in));
	
	@Override
	public void run() {
        String userInput;
		try {
			while (!interrupted()) {
				userInput = stdIn.readLine();
				if (userInput != null) {
					cs.checkCommand(userInput);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public CommandHandler(){
		super("CommandHandler");
		super.start();
	}
}
