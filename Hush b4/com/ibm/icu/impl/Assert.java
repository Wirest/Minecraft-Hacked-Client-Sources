// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public class Assert
{
    public static void fail(final Exception e) {
        fail(e.toString());
    }
    
    public static void fail(final String msg) {
        throw new IllegalStateException("failure '" + msg + "'");
    }
    
    public static void assrt(final boolean val) {
        if (!val) {
            throw new IllegalStateException("assert failed");
        }
    }
    
    public static void assrt(final String msg, final boolean val) {
        if (!val) {
            throw new IllegalStateException("assert '" + msg + "' failed");
        }
    }
}
