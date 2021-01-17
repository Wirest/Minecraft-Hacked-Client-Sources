package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.player.*;

public class CommandSpeedmine extends Command
{
    public CommandSpeedmine() {
        super("speedmine", new String[0]);
    }
    
    @Argument
    protected String speedmineHelp() {
        final String help = "Mode <0 - 3>, MineSpeed <Float>";
        return help;
    }
    
    @Argument(handles = { "minespeed", "ms" })
    protected String mineSpeed(final float value) {
        SpeedMine.speed.setFloatValue(value);
        return "Speedmine §bSpeed §fset to §6" + value + "§f";
    }
    
    @Argument(handles = { "mode", "m" })
    protected String mode(final int mode) {
        if (mode > 3 || mode < 0) {
            return "Error, value can only be 0 - 3";
        }
        String type = "";
        if (mode == 0) {
            type = "Block Damage";
        }
        if (mode == 1) {
            type = "Packet";
        }
        if (mode == 2) {
            type = "Instant";
        }
        if (mode == 3) {
            type = "New";
        }
        SpeedMine.mode.setSelectedOption(type);
        return "Speedmine §fmode set to §6" + type + "§f";
    }
}
