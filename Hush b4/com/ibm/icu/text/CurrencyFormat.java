// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;
import com.ibm.icu.util.CurrencyAmount;
import java.text.FieldPosition;
import com.ibm.icu.util.ULocale;

class CurrencyFormat extends MeasureFormat
{
    static final long serialVersionUID = -931679363692504634L;
    private NumberFormat fmt;
    
    public CurrencyFormat(final ULocale locale) {
        this.fmt = NumberFormat.getCurrencyInstance(locale.toLocale());
    }
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        try {
            final CurrencyAmount currency = (CurrencyAmount)obj;
            this.fmt.setCurrency(currency.getCurrency());
            return this.fmt.format(currency.getNumber(), toAppendTo, pos);
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException("Invalid type: " + obj.getClass().getName());
        }
    }
    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return this.fmt.parseCurrency(source, pos);
    }
}
