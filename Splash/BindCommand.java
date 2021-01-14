package splash.client.commands;

import org.lwjgl.input.Keyboard;
import splash.Splash;
import splash.api.command.Command;
import splash.api.module.Module;
import splash.client.modules.movement.Flight;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice
 * Created: 17:00, 06-Jun-20
 * Project: Client
 */
public class BindCommand extends Command {

    public BindCommand() {
        super("bind");
    }

    @Override
    public String usage() {
        return "bind <module> <macro>";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
        Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).setModuleMacro(Keyboard.KEY_H);
        if(commandArguments.length != 3) {
            printUsage();
        }
        if(commandArguments.length == 2) {
            String arg1 = commandArguments[0];
            String arg2 = commandArguments[1];

                if(Splash.getInstance().getModuleManager().getModuleByDisplayName(arg1) != null) {
                    Module module = Splash.getInstance().getModuleManager().getModuleByDisplayName(arg1);
                    int keyIndex = Keyboard.getKeyIndex(arg2.toUpperCase());
                    module.setModuleMacro(keyIndex);
                    ClientLogger.printToMinecraft("Bound " + module.getModuleDisplayName() + " to " + arg2.toUpperCase());
                } else {
                    System.out.println("invalid");
                }
            }
    }
}
