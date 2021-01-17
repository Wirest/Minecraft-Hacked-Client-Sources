// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class WrongUsageException extends SyntaxErrorException
{
    public WrongUsageException(final String message, final Object... replacements) {
        super(message, replacements);
    }
}
