// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Map;
import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.util.ULocale;

public abstract class CurrencyDisplayNames
{
    public static CurrencyDisplayNames getInstance(final ULocale locale) {
        return CurrencyData.provider.getInstance(locale, true);
    }
    
    public static CurrencyDisplayNames getInstance(final ULocale locale, final boolean noSubstitute) {
        return CurrencyData.provider.getInstance(locale, !noSubstitute);
    }
    
    @Deprecated
    public static boolean hasData() {
        return CurrencyData.provider.hasData();
    }
    
    public abstract ULocale getULocale();
    
    public abstract String getSymbol(final String p0);
    
    public abstract String getName(final String p0);
    
    public abstract String getPluralName(final String p0, final String p1);
    
    public abstract Map<String, String> symbolMap();
    
    public abstract Map<String, String> nameMap();
    
    @Deprecated
    protected CurrencyDisplayNames() {
    }
}
