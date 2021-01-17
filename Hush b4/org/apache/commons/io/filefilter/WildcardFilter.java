// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.util.List;
import java.io.Serializable;

@Deprecated
public class WildcardFilter extends AbstractFileFilter implements Serializable
{
    private final String[] wildcards;
    
    public WildcardFilter(final String wildcard) {
        if (wildcard == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        }
        this.wildcards = new String[] { wildcard };
    }
    
    public WildcardFilter(final String[] wildcards) {
        if (wildcards == null) {
            throw new IllegalArgumentException("The wildcard array must not be null");
        }
        System.arraycopy(wildcards, 0, this.wildcards = new String[wildcards.length], 0, wildcards.length);
    }
    
    public WildcardFilter(final List<String> wildcards) {
        if (wildcards == null) {
            throw new IllegalArgumentException("The wildcard list must not be null");
        }
        this.wildcards = wildcards.toArray(new String[wildcards.size()]);
    }
    
    @Override
    public boolean accept(final File dir, final String name) {
        if (dir != null && new File(dir, name).isDirectory()) {
            return false;
        }
        for (final String wildcard : this.wildcards) {
            if (FilenameUtils.wildcardMatch(name, wildcard)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean accept(final File file) {
        if (file.isDirectory()) {
            return false;
        }
        for (final String wildcard : this.wildcards) {
            if (FilenameUtils.wildcardMatch(file.getName(), wildcard)) {
                return true;
            }
        }
        return false;
    }
}
