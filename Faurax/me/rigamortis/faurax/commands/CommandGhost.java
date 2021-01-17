package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;

public class CommandGhost extends Command
{
    public CommandGhost() {
        super("ghost", new String[0]);
    }
    
    @Argument
    protected String vclip(final int pos) {
        return "Vclipped " + pos;
    }
}
