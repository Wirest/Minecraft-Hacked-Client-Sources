// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collection;

class MissingRequiredOptionException extends OptionException
{
    private static final long serialVersionUID = -1L;
    
    protected MissingRequiredOptionException(final Collection<String> options) {
        super(options);
    }
    
    @Override
    public String getMessage() {
        return "Missing required option(s) " + this.multipleOptionMessage();
    }
}
