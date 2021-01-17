package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;

public class CommandTabUI extends Command
{
    public CommandTabUI() {
        super("tabUI", new String[0]);
    }
    
    @Argument
    protected String tabUIHelp() {
        final String help = "load";
        return help;
    }
    
    @Argument(handles = { "load" })
    protected String load(final String name) {
        return "Loaded §b" + name + "§f";
    }
}
