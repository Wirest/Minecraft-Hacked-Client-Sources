// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collection;
import java.util.Collections;

class UnconfiguredOptionException extends OptionException
{
    private static final long serialVersionUID = -1L;
    
    UnconfiguredOptionException(final String option) {
        this(Collections.singletonList(option));
    }
    
    UnconfiguredOptionException(final Collection<String> options) {
        super(options);
    }
    
    @Override
    public String getMessage() {
        return "Option " + this.multipleOptionMessage() + " has not been configured on this parser";
    }
}
