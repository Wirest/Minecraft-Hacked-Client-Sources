// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class CommandNotFoundException extends CommandException
{
    public CommandNotFoundException() {
        this("commands.generic.notFound", new Object[0]);
    }
    
    public CommandNotFoundException(final String p_i1363_1_, final Object... p_i1363_2_) {
        super(p_i1363_1_, p_i1363_2_);
    }
}
