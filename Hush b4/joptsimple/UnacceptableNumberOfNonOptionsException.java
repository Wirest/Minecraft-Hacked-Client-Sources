// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Collection;
import java.util.Collections;

class UnacceptableNumberOfNonOptionsException extends OptionException
{
    private static final long serialVersionUID = -1L;
    private final int minimum;
    private final int maximum;
    private final int actual;
    
    UnacceptableNumberOfNonOptionsException(final int minimum, final int maximum, final int actual) {
        super(Collections.singletonList("[arguments]"));
        this.minimum = minimum;
        this.maximum = maximum;
        this.actual = actual;
    }
    
    @Override
    public String getMessage() {
        return String.format("actual = %d, minimum = %d, maximum = %d", this.actual, this.minimum, this.maximum);
    }
}
