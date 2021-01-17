package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.player.*;
import me.rigamortis.faurax.*;

public class CommandGodmode extends Command
{
    public CommandGodmode() {
        super("godmode", new String[0]);
    }
    
    @Argument
    protected String godModeHelp() {
        return "Type §a<§f0 - 2§a>§f";
    }
    
    @Argument(handles = { "type", "t" })
    protected String god(final int mode) {
        if (mode > 3 || mode < 0) {
            return "Error, value can only be 0 - 3";
        }
        String type = "";
        if (mode == 0) {
            type = "Factions";
        }
        if (mode == 1) {
            type = "Slimes";
        }
        if (mode == 2) {
            type = "Sethome";
        }
        if (mode == 3) {
            type = "Teleport";
        }
        Godmode.mode.setSelectedOption(type);
        Client.getConfig().saveConfig();
        return "Godmode §bmode§f set to §6" + type + "§f";
    }
}
