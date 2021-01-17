// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public class CurrencyAmount extends Measure
{
    public CurrencyAmount(final Number number, final Currency currency) {
        super(number, currency);
    }
    
    public CurrencyAmount(final double number, final Currency currency) {
        super(new Double(number), currency);
    }
    
    public Currency getCurrency() {
        return (Currency)this.getUnit();
    }
}
