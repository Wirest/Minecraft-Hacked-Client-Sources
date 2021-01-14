package splash.client.commands;

import splash.Splash;
import splash.api.command.Command;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice
 * Created: 15:40, 20-Jun-20
 * Project: Splash
 */
public class HelpCommand extends Command {


    public HelpCommand() {
        super("help");
    }

    @Override
    public String usage() {
        return "help";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
        Splash.getInstance().getCommandManager().getContents().forEach(command -> ClientLogger.printToMinecraft(command.getCommandName() + " - " + command.usage()));
    }
}
