package splash.client.commands;

import splash.Splash;
import splash.api.command.Command;
import splash.api.config.ClientConfig;
import splash.utilities.clipboard.ClipboardUtils;
import splash.utilities.hastebin.Hastebin;
import splash.utilities.system.ClientLogger;
import splash.utilities.web.InterwebsUtils;

import java.io.IOException;

public class WebConfigCommand extends Command {

    public WebConfigCommand() {
        super("webconfig");
    }

    @Override
    public String usage() {
        return "webconfig save <config>, load <code> <config>";
    }

    @Override
    public void executeCommand(String[] commandArguments) {
        if(commandArguments.length < 2 || commandArguments.length > 3) {
            printUsage();
        }
    if(commandArguments.length == 2) {
        if(commandArguments[0].equalsIgnoreCase("save")) {
            if(new ClientConfig(commandArguments[1]).getConfigFile().exists()) {
                String code = Splash.getInstance().getConfigManager().saveConfigToWeb(new ClientConfig(commandArguments[1]));
                ClientLogger.printToMinecraft("Saved " + commandArguments[1] + " to " + code);
                ClipboardUtils.clip(code);
            } else {
                ClientLogger.printToMinecraft(commandArguments[1] + " does not exist!");
            }
        }
    }
    if(commandArguments.length == 3) {
        if(commandArguments[0].equalsIgnoreCase("load")) {
            try {
                if(InterwebsUtils.getContent("https://hastebin.com/raw/" + commandArguments[1]) != null) {
                    Splash.getInstance().getConfigManager().loadConfigFromWeb(commandArguments[1], commandArguments[2]);
                    ClientLogger.printToMinecraft("Loaded config " + commandArguments[2] + " from " + commandArguments[1]);
                } else {
                    ClientLogger.printToMinecraft("Invalid config code!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }
}
