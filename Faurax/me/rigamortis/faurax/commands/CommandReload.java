package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.rigamortis.faurax.*;
import me.cupboard.command.argument.*;

public class CommandReload extends Command
{
    public CommandReload() {
        super("reload", new String[] { "rl" });
    }
    
    @Argument
    protected String reload() {
        Client.reload();
        final String cmds = "Done.";
        return cmds;
    }
}
