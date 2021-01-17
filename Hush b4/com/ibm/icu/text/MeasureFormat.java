// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.util.ULocale;

public abstract class MeasureFormat extends UFormat
{
    static final long serialVersionUID = -7182021401701778240L;
    
    @Deprecated
    protected MeasureFormat() {
    }
    
    public static MeasureFormat getCurrencyFormat(final ULocale locale) {
        return new CurrencyFormat(locale);
    }
    
    public static MeasureFormat getCurrencyFormat() {
        return getCurrencyFormat(ULocale.getDefault(ULocale.Category.FORMAT));
    }
}
