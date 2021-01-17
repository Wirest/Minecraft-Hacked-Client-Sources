package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;

public class CommandHelp extends Command
{
    public CommandHelp() {
        super("help", new String[0]);
    }
    
    @Argument
    protected String help() {
        final String cmds = "§fFlight, Killaura, Friend, Speed, AutoFarm, Xray, Toggle, Bind, Vclip, Teleport, Phase, Regen, Enchant, Speedmine, Criticals, ChestStealer, Chams, Jesus, ArrayList, ESP, Suicide";
        return cmds;
    }
}
