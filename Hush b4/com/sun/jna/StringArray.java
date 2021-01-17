// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class StringArray extends Memory implements Function.PostCallRead
{
    private boolean wide;
    private List natives;
    private Object[] original;
    
    public StringArray(final String[] strings) {
        this(strings, false);
    }
    
    public StringArray(final String[] strings, final boolean wide) {
        this((Object[])strings, wide);
    }
    
    public StringArray(final WString[] strings) {
        this(strings, true);
    }
    
    private StringArray(final Object[] strings, final boolean wide) {
        super((strings.length + 1) * Pointer.SIZE);
        this.natives = new ArrayList();
        this.original = strings;
        this.wide = wide;
        for (int i = 0; i < strings.length; ++i) {
            Pointer p = null;
            if (strings[i] != null) {
                final NativeString ns = new NativeString(strings[i].toString(), wide);
                this.natives.add(ns);
                p = ns.getPointer();
            }
            this.setPointer(Pointer.SIZE * i, p);
        }
        this.setPointer(Pointer.SIZE * strings.length, null);
    }
    
    public void read() {
        final boolean returnWide = this.original instanceof WString[];
        for (int si = 0; si < this.original.length; ++si) {
            final Pointer p = this.getPointer(si * Pointer.SIZE);
            Object s = null;
            if (p != null) {
                s = p.getString(0L, this.wide);
                if (returnWide) {
                    s = new WString((String)s);
                }
            }
            this.original[si] = s;
        }
    }
    
    public String toString() {
        String s = this.wide ? "const wchar_t*[]" : "const char*[]";
        s += Arrays.asList(this.original);
        return s;
    }
}
