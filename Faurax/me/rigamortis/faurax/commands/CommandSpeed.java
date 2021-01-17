package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.module.modules.movement.*;

public class CommandSpeed extends Command
{
    public CommandSpeed() {
        super("Speed", new String[] { "s" });
    }
    
    @Argument
    protected String speedHelp() {
        final String help = "Mode §a<§f0 - 6§a>§f, RunSpeed §a<§fFloat§a>§f";
        return help;
    }
    
    @Argument(handles = { "runspeed", "rs" })
    protected String speedValue(final float speed) {
        Speed.speedValue.setFloatValue(speed);
        return "Speed §bRun Speed§f set to §6" + speed + "§f";
    }
    
    @Argument(handles = { "mode", "m" })
    protected String speed(final int mode) {
        if (mode > 6 || mode < 0) {
            return "Error, value can only be 0 - 6";
        }
        String type = "";
        if (mode == 0) {
            type = "Bhop";
        }
        if (mode == 1) {
            type = "YPort";
        }
        if (mode == 2) {
            type = "Vanilla";
        }
        if (mode == 3) {
            type = "Boost";
        }
        if (mode == 4) {
            type = "Slow";
        }
        if (mode == 5) {
            type = "New Bhop";
        }
        if (mode == 6) {
            type = "Timer";
        }
        Speed.mode.setSelectedOption(type);
        return "Speed §bmode§f set to §6" + type + "§f";
    }
}
