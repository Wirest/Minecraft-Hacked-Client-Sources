// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collection;

class MultipleArgumentsForOptionException extends OptionException
{
    private static final long serialVersionUID = -1L;
    
    MultipleArgumentsForOptionException(final Collection<String> options) {
        super(options);
    }
    
    @Override
    public String getMessage() {
        return "Found multiple arguments for option " + this.multipleOptionMessage() + ", but you asked for only one";
    }
}
