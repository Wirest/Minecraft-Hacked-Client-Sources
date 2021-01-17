// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collection;
import java.util.Collections;

class UnrecognizedOptionException extends OptionException
{
    private static final long serialVersionUID = -1L;
    
    UnrecognizedOptionException(final String option) {
        super(Collections.singletonList(option));
    }
    
    @Override
    public String getMessage() {
        return this.singleOptionMessage() + " is not a recognized option";
    }
}
