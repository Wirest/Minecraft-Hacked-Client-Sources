package splash.client.commands;

import splash.Splash;
import splash.api.command.Command;
import splash.utilities.system.ClientLogger;

public class NameCommand extends Command {

	public NameCommand() {
		super("name");
	}

	@Override
	public String usage() {
		return "name <name>";
	}

	@Override
	public void executeCommand(String[] commandArguments) {
		if(commandArguments.length < 1 || commandArguments.length > 1) {
			printUsage();
		}
		
		Splash.getInstance().setClientName(commandArguments[0]);
		System.out.println(commandArguments[0]);
		ClientLogger.printToMinecraft("Changed name to " + commandArguments[0]);
		
	}

}
