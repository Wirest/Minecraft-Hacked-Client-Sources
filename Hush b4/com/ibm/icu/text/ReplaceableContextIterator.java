// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.UCaseProps;

class ReplaceableContextIterator implements UCaseProps.ContextIterator
{
    protected Replaceable rep;
    protected int index;
    protected int limit;
    protected int cpStart;
    protected int cpLimit;
    protected int contextStart;
    protected int contextLimit;
    protected int dir;
    protected boolean reachedLimit;
    
    ReplaceableContextIterator() {
        this.rep = null;
        final int n = 0;
        this.contextLimit = n;
        this.contextStart = n;
        this.index = n;
        this.cpLimit = n;
        this.cpStart = n;
        this.limit = n;
        this.dir = 0;
        this.reachedLimit = false;
    }
    
    public void setText(final Replaceable rep) {
        this.rep = rep;
        final int length = rep.length();
        this.contextLimit = length;
        this.limit = length;
        final int n = 0;
        this.contextStart = n;
        this.index = n;
        this.cpLimit = n;
        this.cpStart = n;
        this.dir = 0;
        this.reachedLimit = false;
    }
    
    public void setIndex(final int index) {
        this.cpLimit = index;
        this.cpStart = index;
        this.index = 0;
        this.dir = 0;
        this.reachedLimit = false;
    }
    
    public int getCaseMapCPStart() {
        return this.cpStart;
    }
    
    public void setLimit(final int lim) {
        if (0 <= lim && lim <= this.rep.length()) {
            this.limit = lim;
        }
        else {
            this.limit = this.rep.length();
        }
        this.reachedLimit = false;
    }
    
    public void setContextLimits(final int contextStart, final int contextLimit) {
        if (contextStart < 0) {
            this.contextStart = 0;
        }
        else if (contextStart <= this.rep.length()) {
            this.contextStart = contextStart;
        }
        else {
            this.contextStart = this.rep.length();
        }
        if (contextLimit < this.contextStart) {
            this.contextLimit = this.contextStart;
        }
        else if (contextLimit <= this.rep.length()) {
            this.contextLimit = contextLimit;
        }
        else {
            this.contextLimit = this.rep.length();
        }
        this.reachedLimit = false;
    }
    
    public int nextCaseMapCP() {
        if (this.cpLimit < this.limit) {
            this.cpStart = this.cpLimit;
            final int c = this.rep.char32At(this.cpLimit);
            this.cpLimit += UTF16.getCharCount(c);
            return c;
        }
        return -1;
    }
    
    public int replace(final String text) {
        final int delta = text.length() - (this.cpLimit - this.cpStart);
        this.rep.replace(this.cpStart, this.cpLimit, text);
        this.cpLimit += delta;
        this.limit += delta;
        this.contextLimit += delta;
        return delta;
    }
    
    public boolean didReachLimit() {
        return this.reachedLimit;
    }
    
    public void reset(final int direction) {
        if (direction > 0) {
            this.dir = 1;
            this.index = this.cpLimit;
        }
        else if (direction < 0) {
            this.dir = -1;
            this.index = this.cpStart;
        }
        else {
            this.dir = 0;
            this.index = 0;
        }
        this.reachedLimit = false;
    }
    
    public int next() {
        if (this.dir > 0) {
            if (this.index < this.contextLimit) {
                final int c = this.rep.char32At(this.index);
                this.index += UTF16.getCharCount(c);
                return c;
            }
            this.reachedLimit = true;
        }
        else if (this.dir < 0 && this.index > this.contextStart) {
            final int c = this.rep.char32At(this.index - 1);
            this.index -= UTF16.getCharCount(c);
            return c;
        }
        return -1;
    }
}
