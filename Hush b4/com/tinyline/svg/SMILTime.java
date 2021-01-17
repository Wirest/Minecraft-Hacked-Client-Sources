// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyString;

public final class SMILTime
{
    public static final int SMIL_TIME_INDEFINITE = 0;
    public static final int SMIL_TIME_OFFSET = 1;
    public static final int SMIL_TIME_EVENT_BASED = 2;
    public int type;
    public int timeValue;
    public int offset;
    public TinyString idValue;
    
    public SMILTime() {
        this.type = 0;
        this.offset = -1;
    }
    
    public SMILTime(final int type, final int offset) {
        switch (this.type = type) {
            case 1: {
                this.offset = offset;
                break;
            }
            default: {
                this.offset = -1;
                break;
            }
        }
    }
    
    public SMILTime(final SMILTime smilTime) {
        smilTime.copyTo(this);
    }
    
    public void copyTo(final SMILTime smilTime) {
        smilTime.type = this.type;
        smilTime.timeValue = this.timeValue;
        smilTime.offset = this.offset;
        if (this.idValue != null) {
            smilTime.idValue = new TinyString(this.idValue.data);
        }
        else {
            smilTime.idValue = null;
        }
    }
    
    public int getResolvedOffset() {
        return this.timeValue + this.offset;
    }
    
    public final boolean greaterThan(final SMILTime smilTime) {
        return this.getResolvedOffset() > smilTime.getResolvedOffset();
    }
    
    public final boolean equalTo(final SMILTime smilTime) {
        return this.getResolvedOffset() == smilTime.getResolvedOffset();
    }
}
