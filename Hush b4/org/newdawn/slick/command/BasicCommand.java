// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.command;

public class BasicCommand implements Command
{
    private String name;
    
    public BasicCommand(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof BasicCommand && ((BasicCommand)other).name.equals(this.name);
    }
    
    @Override
    public String toString() {
        return "[Command=" + this.name + "]";
    }
}
