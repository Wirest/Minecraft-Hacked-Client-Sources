// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

public class FunctionResultContext extends FromNativeContext
{
    private Function function;
    private Object[] args;
    
    FunctionResultContext(final Class resultClass, final Function function, final Object[] args) {
        super(resultClass);
        this.function = function;
        this.args = args;
    }
    
    public Function getFunction() {
        return this.function;
    }
    
    public Object[] getArguments() {
        return this.args;
    }
}
