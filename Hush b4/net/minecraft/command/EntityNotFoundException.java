// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class EntityNotFoundException extends CommandException
{
    public EntityNotFoundException() {
        this("commands.generic.entity.notFound", new Object[0]);
    }
    
    public EntityNotFoundException(final String p_i46035_1_, final Object... p_i46035_2_) {
        super(p_i46035_1_, p_i46035_2_);
    }
}
