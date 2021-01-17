// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.List;
import java.util.ArrayList;

public abstract class NameAbbreviator
{
    private static final NameAbbreviator DEFAULT;
    
    public static NameAbbreviator getAbbreviator(final String pattern) {
        if (pattern.length() <= 0) {
            return NameAbbreviator.DEFAULT;
        }
        final String trimmed = pattern.trim();
        if (trimmed.isEmpty()) {
            return NameAbbreviator.DEFAULT;
        }
        int i;
        for (i = 0; i < trimmed.length() && trimmed.charAt(i) >= '0' && trimmed.charAt(i) <= '9'; ++i) {}
        if (i == trimmed.length()) {
            return new MaxElementAbbreviator(Integer.parseInt(trimmed));
        }
        final ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList<PatternAbbreviatorFragment>(5);
        for (int pos = 0; pos < trimmed.length() && pos >= 0; ++pos) {
            int ellipsisPos = pos;
            int charCount;
            if (trimmed.charAt(pos) == '*') {
                charCount = Integer.MAX_VALUE;
                ++ellipsisPos;
            }
            else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
                charCount = trimmed.charAt(pos) - '0';
                ++ellipsisPos;
            }
            else {
                charCount = 0;
            }
            char ellipsis = '\0';
            if (ellipsisPos < trimmed.length()) {
                ellipsis = trimmed.charAt(ellipsisPos);
                if (ellipsis == '.') {
                    ellipsis = '\0';
                }
            }
            fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
            pos = trimmed.indexOf(46, pos);
            if (pos == -1) {
                break;
            }
        }
        return new PatternAbbreviator(fragments);
    }
    
    public static NameAbbreviator getDefaultAbbreviator() {
        return NameAbbreviator.DEFAULT;
    }
    
    public abstract String abbreviate(final String p0);
    
    static {
        DEFAULT = new NOPAbbreviator();
    }
    
    private static class NOPAbbreviator extends NameAbbreviator
    {
        public NOPAbbreviator() {
        }
        
        @Override
        public String abbreviate(final String buf) {
            return buf;
        }
    }
    
    private static class MaxElementAbbreviator extends NameAbbreviator
    {
        private final int count;
        
        public MaxElementAbbreviator(final int count) {
            this.count = ((count < 1) ? 1 : count);
        }
        
        @Override
        public String abbreviate(final String buf) {
            int end = buf.length() - 1;
            for (int i = this.count; i > 0; --i) {
                end = buf.lastIndexOf(46, end - 1);
                if (end == -1) {
                    return buf;
                }
            }
            return buf.substring(end + 1);
        }
    }
    
    private static class PatternAbbreviatorFragment
    {
        private final int charCount;
        private final char ellipsis;
        
        public PatternAbbreviatorFragment(final int charCount, final char ellipsis) {
            this.charCount = charCount;
            this.ellipsis = ellipsis;
        }
        
        public int abbreviate(final StringBuilder buf, final int startPos) {
            int nextDot = buf.toString().indexOf(46, startPos);
            if (nextDot != -1) {
                if (nextDot - startPos > this.charCount) {
                    buf.delete(startPos + this.charCount, nextDot);
                    nextDot = startPos + this.charCount;
                    if (this.ellipsis != '\0') {
                        buf.insert(nextDot, this.ellipsis);
                        ++nextDot;
                    }
                }
                ++nextDot;
            }
            return nextDot;
        }
    }
    
    private static class PatternAbbreviator extends NameAbbreviator
    {
        private final PatternAbbreviatorFragment[] fragments;
        
        public PatternAbbreviator(final List<PatternAbbreviatorFragment> fragments) {
            if (fragments.size() == 0) {
                throw new IllegalArgumentException("fragments must have at least one element");
            }
            fragments.toArray(this.fragments = new PatternAbbreviatorFragment[fragments.size()]);
        }
        
        @Override
        public String abbreviate(final String buf) {
            int pos = 0;
            final StringBuilder sb = new StringBuilder(buf);
            for (int i = 0; i < this.fragments.length - 1 && pos < buf.length(); pos = this.fragments[i].abbreviate(sb, pos), ++i) {}
            for (PatternAbbreviatorFragment terminalFragment = this.fragments[this.fragments.length - 1]; pos < buf.length() && pos >= 0; pos = terminalFragment.abbreviate(sb, pos)) {}
            return sb.toString();
        }
    }
}
