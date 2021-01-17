// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;

public class InitialTimeZoneRule extends TimeZoneRule
{
    private static final long serialVersionUID = 1876594993064051206L;
    
    public InitialTimeZoneRule(final String name, final int rawOffset, final int dstSavings) {
        super(name, rawOffset, dstSavings);
    }
    
    @Override
    public boolean isEquivalentTo(final TimeZoneRule other) {
        return other instanceof InitialTimeZoneRule && super.isEquivalentTo(other);
    }
    
    @Override
    public Date getFinalStart(final int prevRawOffset, final int prevDSTSavings) {
        return null;
    }
    
    @Override
    public Date getFirstStart(final int prevRawOffset, final int prevDSTSavings) {
        return null;
    }
    
    @Override
    public Date getNextStart(final long base, final int prevRawOffset, final int prevDSTSavings, final boolean inclusive) {
        return null;
    }
    
    @Override
    public Date getPreviousStart(final long base, final int prevRawOffset, final int prevDSTSavings, final boolean inclusive) {
        return null;
    }
    
    @Override
    public boolean isTransitionRule() {
        return false;
    }
}
