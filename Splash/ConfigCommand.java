package splash.client.commands;

import splash.api.config.ClientConfig;
import splash.Splash;
import splash.api.command.Command;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice
 * Created: 17:00, 06-Jun-20
 * Project: Client
 */
public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config");
    }

    @Override
    public String usage() {
        return "config save <name>, config load <name>";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
        if(commandArguments.length > 3) {
            this.printUsage();
        }
        if(commandArguments.length == 1) {
            if(commandArguments[0].equalsIgnoreCase("list")) {
                ClientLogger.printToMinecraft("Configs:");
                Splash.getInstance().getConfigManager().getContents().forEach(clientConfig -> ClientLogger.printToMinecraft(clientConfig.getConfigName()));
            }
        }

       if(commandArguments.length == 2) {
           String arg0 = commandArguments[0];
           String configName = commandArguments[1];

               if(arg0.equalsIgnoreCase("save")) {
                   ClientLogger.printToMinecraft("Saved config " + configName);
                   Splash.getInstance().getConfigManager().saveConfig(new ClientConfig(configName));
               } else if(arg0.equalsIgnoreCase("load")) {
                   ClientLogger.printToMinecraft("Loaded config " + configName);
                       Splash.getInstance().getConfigManager().loadConfig(new ClientConfig(configName));
               }
               if(arg0.equalsIgnoreCase("delete")) {
                   System.out.println("fuck");
                   Splash.getInstance().getConfigManager().deleteConfig(new ClientConfig(configName));
               }
           }
       }
}


