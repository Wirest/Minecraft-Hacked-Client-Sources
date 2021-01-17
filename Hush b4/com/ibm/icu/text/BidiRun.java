// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public class BidiRun
{
    int start;
    int limit;
    int insertRemove;
    byte level;
    
    BidiRun() {
        this(0, 0, (byte)0);
    }
    
    BidiRun(final int start, final int limit, final byte embeddingLevel) {
        this.start = start;
        this.limit = limit;
        this.level = embeddingLevel;
    }
    
    void copyFrom(final BidiRun run) {
        this.start = run.start;
        this.limit = run.limit;
        this.level = run.level;
        this.insertRemove = run.insertRemove;
    }
    
    public int getStart() {
        return this.start;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public int getLength() {
        return this.limit - this.start;
    }
    
    public byte getEmbeddingLevel() {
        return this.level;
    }
    
    public boolean isOddRun() {
        return (this.level & 0x1) == 0x1;
    }
    
    public boolean isEvenRun() {
        return (this.level & 0x1) == 0x0;
    }
    
    public byte getDirection() {
        return (byte)(this.level & 0x1);
    }
    
    @Override
    public String toString() {
        return "BidiRun " + this.start + " - " + this.limit + " @ " + this.level;
    }
}
