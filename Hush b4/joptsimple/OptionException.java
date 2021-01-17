// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple;

import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class OptionException extends RuntimeException
{
    private static final long serialVersionUID = -1L;
    private final List<String> options;
    
    protected OptionException(final Collection<String> options) {
        (this.options = new ArrayList<String>()).addAll(options);
    }
    
    protected OptionException(final Collection<String> options, final Throwable cause) {
        super(cause);
        (this.options = new ArrayList<String>()).addAll(options);
    }
    
    public Collection<String> options() {
        return Collections.unmodifiableCollection((Collection<? extends String>)this.options);
    }
    
    protected final String singleOptionMessage() {
        return this.singleOptionMessage(this.options.get(0));
    }
    
    protected final String singleOptionMessage(final String option) {
        return "'" + option + "'";
    }
    
    protected final String multipleOptionMessage() {
        final StringBuilder buffer = new StringBuilder("[");
        final Iterator<String> iter = this.options.iterator();
        while (iter.hasNext()) {
            buffer.append(this.singleOptionMessage(iter.next()));
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(']');
        return buffer.toString();
    }
    
    static OptionException unrecognizedOption(final String option) {
        return new UnrecognizedOptionException(option);
    }
}
