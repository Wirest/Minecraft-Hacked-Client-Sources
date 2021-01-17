// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.List;
import org.apache.commons.io.IOCase;
import java.io.Serializable;

public class SuffixFileFilter extends AbstractFileFilter implements Serializable
{
    private final String[] suffixes;
    private final IOCase caseSensitivity;
    
    public SuffixFileFilter(final String suffix) {
        this(suffix, IOCase.SENSITIVE);
    }
    
    public SuffixFileFilter(final String suffix, final IOCase caseSensitivity) {
        if (suffix == null) {
            throw new IllegalArgumentException("The suffix must not be null");
        }
        this.suffixes = new String[] { suffix };
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    public SuffixFileFilter(final String[] suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }
    
    public SuffixFileFilter(final String[] suffixes, final IOCase caseSensitivity) {
        if (suffixes == null) {
            throw new IllegalArgumentException("The array of suffixes must not be null");
        }
        System.arraycopy(suffixes, 0, this.suffixes = new String[suffixes.length], 0, suffixes.length);
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    public SuffixFileFilter(final List<String> suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }
    
    public SuffixFileFilter(final List<String> suffixes, final IOCase caseSensitivity) {
        if (suffixes == null) {
            throw new IllegalArgumentException("The list of suffixes must not be null");
        }
        this.suffixes = suffixes.toArray(new String[suffixes.size()]);
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    @Override
    public boolean accept(final File file) {
        final String name = file.getName();
        for (final String suffix : this.suffixes) {
            if (this.caseSensitivity.checkEndsWith(name, suffix)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean accept(final File file, final String name) {
        for (final String suffix : this.suffixes) {
            if (this.caseSensitivity.checkEndsWith(name, suffix)) {
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
        if (this.suffixes != null) {
            for (int i = 0; i < this.suffixes.length; ++i) {
                if (i > 0) {
                    buffer.append(",");
                }
                buffer.append(this.suffixes[i]);
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
