// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.List;
import org.apache.commons.io.IOCase;
import java.io.Serializable;

public class NameFileFilter extends AbstractFileFilter implements Serializable
{
    private final String[] names;
    private final IOCase caseSensitivity;
    
    public NameFileFilter(final String name) {
        this(name, null);
    }
    
    public NameFileFilter(final String name, final IOCase caseSensitivity) {
        if (name == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        }
        this.names = new String[] { name };
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    public NameFileFilter(final String[] names) {
        this(names, null);
    }
    
    public NameFileFilter(final String[] names, final IOCase caseSensitivity) {
        if (names == null) {
            throw new IllegalArgumentException("The array of names must not be null");
        }
        System.arraycopy(names, 0, this.names = new String[names.length], 0, names.length);
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    public NameFileFilter(final List<String> names) {
        this(names, null);
    }
    
    public NameFileFilter(final List<String> names, final IOCase caseSensitivity) {
        if (names == null) {
            throw new IllegalArgumentException("The list of names must not be null");
        }
        this.names = names.toArray(new String[names.size()]);
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    @Override
    public boolean accept(final File file) {
        final String name = file.getName();
        for (final String name2 : this.names) {
            if (this.caseSensitivity.checkEquals(name, name2)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean accept(final File dir, final String name) {
        for (final String name2 : this.names) {
            if (this.caseSensitivity.checkEquals(name, name2)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        if (this.names != null) {
            for (int i = 0; i < this.names.length; ++i) {
                if (i > 0) {
                    buffer.append(",");
                }
                buffer.append(this.names[i]);
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
