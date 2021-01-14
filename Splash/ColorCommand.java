package splash.client.commands;

import splash.Splash;
import splash.api.command.Command;
import splash.utilities.system.ClientLogger;
import splash.utilities.visual.ColorUtilities;

/**
 * Author: Ice
 * Created: 12:51, 21-Jun-20
 * Project: Splash
 */
public class ColorCommand extends Command {


    public ColorCommand() {
        super("color");
    }

    @Override
    public String usage() {
        return "colorchange";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
        Splash.getInstance().getModuleManager().getContents().forEach(module -> module.setColor(ColorUtilities.getGerman(0.9F)));
    }
}
