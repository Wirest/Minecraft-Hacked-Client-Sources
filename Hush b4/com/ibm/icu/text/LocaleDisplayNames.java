// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Locale;
import com.ibm.icu.impl.LocaleDisplayNamesImpl;
import com.ibm.icu.util.ULocale;

public abstract class LocaleDisplayNames
{
    public static LocaleDisplayNames getInstance(final ULocale locale) {
        return getInstance(locale, DialectHandling.STANDARD_NAMES);
    }
    
    public static LocaleDisplayNames getInstance(final ULocale locale, final DialectHandling dialectHandling) {
        return LocaleDisplayNamesImpl.getInstance(locale, dialectHandling);
    }
    
    public static LocaleDisplayNames getInstance(final ULocale locale, final DisplayContext... contexts) {
        return LocaleDisplayNamesImpl.getInstance(locale, contexts);
    }
    
    public abstract ULocale getLocale();
    
    public abstract DialectHandling getDialectHandling();
    
    public abstract DisplayContext getContext(final DisplayContext.Type p0);
    
    public abstract String localeDisplayName(final ULocale p0);
    
    public abstract String localeDisplayName(final Locale p0);
    
    public abstract String localeDisplayName(final String p0);
    
    public abstract String languageDisplayName(final String p0);
    
    public abstract String scriptDisplayName(final String p0);
    
    @Deprecated
    public String scriptDisplayNameInContext(final String script) {
        return this.scriptDisplayName(script);
    }
    
    public abstract String scriptDisplayName(final int p0);
    
    public abstract String regionDisplayName(final String p0);
    
    public abstract String variantDisplayName(final String p0);
    
    public abstract String keyDisplayName(final String p0);
    
    public abstract String keyValueDisplayName(final String p0, final String p1);
    
    @Deprecated
    protected LocaleDisplayNames() {
    }
    
    public enum DialectHandling
    {
        STANDARD_NAMES, 
        DIALECT_NAMES;
    }
}
