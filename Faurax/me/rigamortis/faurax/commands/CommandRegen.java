package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.player.*;

public class CommandRegen extends Command
{
    public CommandRegen() {
        super("Regen", new String[0]);
    }
    
    @Argument
    protected String regenHelp() {
        final String help = "Mode §a<§f0 - 3§a>§f, Health §a<§fFloat§a>§f";
        return help;
    }
    
    @Argument(handles = { "mode", "m" })
    protected String regen(final int mode) {
        if (mode > 3 || mode < 0) {
            return "Error, value can only be 0 - 3";
        }
        String type = "";
        if (mode == 0) {
            type = "AutoSoup";
        }
        if (mode == 1) {
            type = "AutoPot";
        }
        if (mode == 2) {
            type = "Vanilla";
        }
        if (mode == 3) {
            type = "NCP";
        }
        Regen.mode.setSelectedOption(type);
        return "Regen §bmode§f set to §6" + type + "§f";
    }
    
    @Argument(handles = { "health", "h", "hp" })
    protected String regenHealth(final float health) {
        Regen.health.setFloatValue(health);
        return "Regen §bhealth§f set to §6" + health + "§f";
    }
}
