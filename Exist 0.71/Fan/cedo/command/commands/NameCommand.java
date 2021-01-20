package cedo.command.commands;

import cedo.command.Command;
import cedo.util.Logger;

public class NameCommand extends Command {

    public static String namenameOfWatermark;

    public NameCommand() {
        super("Name", "name");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Logger.ingameError(".name <name> | .name reset");
            return;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            namenameOfWatermark = null;
            Logger.ingameInfo("Name has been reset");
        } else {
            namenameOfWatermark = String.join(" ", args);
            namenameOfWatermark = namenameOfWatermark.replace("&", "\247");
            namenameOfWatermark = namenameOfWatermark.replace("\\247", "\247");
            Logger.ingameInfo("Name has been set to \247e" + namenameOfWatermark);
        }
    }
}
