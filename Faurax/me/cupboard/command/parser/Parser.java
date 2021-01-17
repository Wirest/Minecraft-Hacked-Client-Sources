package me.cupboard.command.parser;

import me.cupboard.command.executor.*;

public abstract class Parser<T>
{
    protected final CommandExecutor executor;
    private int index;
    
    public Parser(final CommandExecutor executor) {
        this.executor = executor;
    }
    
    public abstract T test(final String p0);
    
    public abstract boolean handles(final Class p0);
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
}
