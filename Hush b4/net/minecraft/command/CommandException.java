// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class CommandException extends Exception
{
    private final Object[] errorObjects;
    
    public CommandException(final String message, final Object... objects) {
        super(message);
        this.errorObjects = objects;
    }
    
    public Object[] getErrorObjects() {
        return this.errorObjects;
    }
}
