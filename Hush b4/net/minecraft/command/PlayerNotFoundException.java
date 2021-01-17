// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class PlayerNotFoundException extends CommandException
{
    public PlayerNotFoundException() {
        this("commands.generic.player.notFound", new Object[0]);
    }
    
    public PlayerNotFoundException(final String message, final Object... replacements) {
        super(message, replacements);
    }
}
