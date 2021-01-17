// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public class BidiClassifier
{
    protected Object context;
    
    public BidiClassifier(final Object context) {
        this.context = context;
    }
    
    public void setContext(final Object context) {
        this.context = context;
    }
    
    public Object getContext() {
        return this.context;
    }
    
    public int classify(final int c) {
        return 19;
    }
}
