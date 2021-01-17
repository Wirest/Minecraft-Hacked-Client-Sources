// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.lang.UCharacter;

public class CaseInsensitiveString
{
    private String string;
    private int hash;
    private String folded;
    
    private static String foldCase(final String foldee) {
        return UCharacter.foldCase(foldee, true);
    }
    
    private void getFolded() {
        if (this.folded == null) {
            this.folded = foldCase(this.string);
        }
    }
    
    public CaseInsensitiveString(final String s) {
        this.hash = 0;
        this.folded = null;
        this.string = s;
    }
    
    public String getString() {
        return this.string;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        this.getFolded();
        try {
            final CaseInsensitiveString cis = (CaseInsensitiveString)o;
            cis.getFolded();
            return this.folded.equals(cis.folded);
        }
        catch (ClassCastException e) {
            try {
                final String s = (String)o;
                return this.folded.equals(foldCase(s));
            }
            catch (ClassCastException e2) {
                return false;
            }
        }
    }
    
    @Override
    public int hashCode() {
        this.getFolded();
        if (this.hash == 0) {
            this.hash = this.folded.hashCode();
        }
        return this.hash;
    }
    
    @Override
    public String toString() {
        return this.string;
    }
}
