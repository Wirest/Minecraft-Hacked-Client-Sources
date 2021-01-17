package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.combat.*;
import me.rigamortis.faurax.*;

public class CommandCrits extends Command
{
    public CommandCrits() {
        super("criticals", new String[] { "crits" });
    }
    
    @Argument
    protected String critsHelp() {
        final String help = "Mode §a<§f0 - 3§a>§f";
        return help;
    }
    
    @Argument(handles = { "mode", "m" })
    protected String crits(final int mode) {
        if (mode > 3 || mode < 0) {
            return "Error, value can only be 0 - 3";
        }
        String type = "";
        if (mode == 0) {
            type = "Jump";
        }
        if (mode == 1) {
            type = "Packet";
        }
        if (mode == 2) {
            type = "Mini Jumps";
        }
        if (mode == 3) {
            type = "New";
        }
        Criticals.mode.setSelectedOption(type);
        Client.getConfig().saveConfig();
        return "Criticals §bmode§f set to §6" + type + "§f";
    }
}
