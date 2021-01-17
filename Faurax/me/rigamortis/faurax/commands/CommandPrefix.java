package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.rigamortis.faurax.*;
import me.cupboard.command.argument.*;

public class CommandPrefix extends Command
{
    public CommandPrefix() {
        super("prefix", new String[0]);
    }
    
    @Argument
    protected String prefix(final String s) {
        CommandManager.prefix.setSelectedOption(s);
        Client.getConfig().saveConfig();
        return "Command prefix set to " + s;
    }
}
