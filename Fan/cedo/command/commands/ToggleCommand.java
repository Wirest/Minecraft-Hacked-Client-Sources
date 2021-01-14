package cedo.command.commands;

import cedo.Fan;
import cedo.command.Command;
import cedo.modules.Module;
import cedo.util.Logger;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("Toggle", "toggle");
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            Logger.ingameError(".toggle <module>"); //Change to using client messages
            return;
        }
        Module module = Fan.getModuleByName(args[0]);
        if (module == null) {
            Logger.ingameError("Couldn't find the module \"" + args[0] + "\"");
            return;
        }

        module.toggle();

        if (module.isEnabled()) {
            Logger.ingameInfo(module.getName() + " has been enabled");
        } else {
            Logger.ingameInfo(module.getName() + " has been disabled");
        }
    }
}
