package cedo.command.commands;

import cedo.command.Command;
import cedo.util.Logger;

public class NameProtectCommand extends Command {

    public static String name;

    public NameProtectCommand() {
        super("NameProtect", "name protect");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Logger.ingameError(".nameprotect <name> | .nameprotect reset"); //Change to using client messages
            return;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            name = null;
            Logger.ingameInfo("NameProtect name has been reset");
        } else {
            name = args[0];
            name = name.replace("&", "\247");
            name = name.replace("\\247", "\247");
            Logger.ingameInfo("NameProtect name has been set to \247e" + name);
        }
    }
}
