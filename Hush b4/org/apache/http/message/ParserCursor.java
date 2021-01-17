// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class ParserCursor
{
    private final int lowerBound;
    private final int upperBound;
    private int pos;
    
    public ParserCursor(final int lowerBound, final int upperBound) {
        if (lowerBound < 0) {
            throw new IndexOutOfBoundsException("Lower bound cannot be negative");
        }
        if (lowerBound > upperBound) {
            throw new IndexOutOfBoundsException("Lower bound cannot be greater then upper bound");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.pos = lowerBound;
    }
    
    public int getLowerBound() {
        return this.lowerBound;
    }
    
    public int getUpperBound() {
        return this.upperBound;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public void updatePos(final int pos) {
        if (pos < this.lowerBound) {
            throw new IndexOutOfBoundsException("pos: " + pos + " < lowerBound: " + this.lowerBound);
        }
        if (pos > this.upperBound) {
            throw new IndexOutOfBoundsException("pos: " + pos + " > upperBound: " + this.upperBound);
        }
        this.pos = pos;
    }
    
    public boolean atEnd() {
        return this.pos >= this.upperBound;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append('[');
        buffer.append(Integer.toString(this.lowerBound));
        buffer.append('>');
        buffer.append(Integer.toString(this.pos));
        buffer.append('>');
        buffer.append(Integer.toString(this.upperBound));
        buffer.append(']');
        return buffer.toString();
    }
}
