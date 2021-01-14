package splash.client.commands;

import splash.Splash;
import splash.api.command.Command;
import splash.api.module.Module;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice
 * Created: 17:00, 06-Jun-20
 * Project: Client
 */
public class ToggleCommand extends Command {


    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public String usage() {
        return "toggle <module>";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
        if(commandArguments.length != 1) {
            this.printUsage();
        }
        if(commandArguments.length == 1) {
          String moduleName = commandArguments[0];

              if(Splash.getInstance().getModuleManager().getModuleByDisplayName(moduleName) != null) {
                  Module module = Splash.getInstance().getModuleManager().getModuleByDisplayName(moduleName);
                  ClientLogger.printToMinecraft("Toggled " + module.getModuleDisplayName());
                  module.activateModule();
              } else {
                  System.out.println("Invalid module!");
              }
          }
      }
}
