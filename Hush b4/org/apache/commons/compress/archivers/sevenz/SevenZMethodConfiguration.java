// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.sevenz;

public class SevenZMethodConfiguration
{
    private final SevenZMethod method;
    private final Object options;
    
    public SevenZMethodConfiguration(final SevenZMethod method) {
        this(method, null);
    }
    
    public SevenZMethodConfiguration(final SevenZMethod method, final Object options) {
        this.method = method;
        this.options = options;
        if (options != null && !Coders.findByMethod(method).canAcceptOptions(options)) {
            throw new IllegalArgumentException("The " + method + " method doesn't support options of type " + options.getClass());
        }
    }
    
    public SevenZMethod getMethod() {
        return this.method;
    }
    
    public Object getOptions() {
        return this.options;
    }
}
