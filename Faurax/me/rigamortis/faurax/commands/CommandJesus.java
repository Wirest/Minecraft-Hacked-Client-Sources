package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.movement.*;
import me.rigamortis.faurax.*;

public class CommandJesus extends Command
{
    public CommandJesus() {
        super("Jesus", new String[] { "j" });
    }
    
    @Argument
    protected String jesusHelp() {
        final String help = "Mode §a<§f0 - 3§a>§f";
        return help;
    }
    
    @Argument(handles = { "mode", "m" })
    protected String jesus(final int mode) {
        if (mode > 3 || mode < 0) {
            return "Error, value can only be 0 - 3";
        }
        String type = "";
        if (mode == 0) {
            type = "New";
        }
        if (mode == 1) {
            type = "Old";
        }
        if (mode == 2) {
            type = "Vanilla";
        }
        if (mode == 3) {
            type = "Dolphin";
        }
        Jesus.mode.setSelectedOption(type);
        Client.getConfig().saveConfig();
        return "Jesus §bmode§f set to §6" + type + "§f";
    }
}
