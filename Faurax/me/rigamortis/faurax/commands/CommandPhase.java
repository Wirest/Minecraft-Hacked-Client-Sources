package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.player.*;
import me.rigamortis.faurax.*;

public class CommandPhase extends Command
{
    public CommandPhase() {
        super("phase", new String[0]);
    }
    
    @Argument
    protected String phaseHelp() {
        final String help = "Mode §a<§f0 - 4§a>§f";
        return help;
    }
    
    @Argument(handles = { "mode", "m" })
    protected String mode(final int mode) {
        if (mode > 5 || mode < 0) {
            return "Error, value can only be 0 - 5";
        }
        String type = "";
        if (mode == 0) {
            type = "Sand";
        }
        if (mode == 1) {
            type = "Vanilla";
        }
        if (mode == 2) {
            type = "Skip";
        }
        if (mode == 3) {
            type = "Current";
        }
        if (mode == 4) {
            type = "Vanilla Old";
        }
        if (mode == 5) {
            type = "Old";
        }
        NoClip.mode.setSelectedOption(type);
        Client.getConfig().saveConfig();
        return "Phase mode set to §b" + type;
    }
}
