// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.ICUCache;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import java.util.Map;

public final class ListFormatter
{
    private final String two;
    private final String start;
    private final String middle;
    private final String end;
    static Map<ULocale, ListFormatter> localeToData;
    static Cache cache;
    
    public ListFormatter(final String two, final String start, final String middle, final String end) {
        this.two = two;
        this.start = start;
        this.middle = middle;
        this.end = end;
    }
    
    public static ListFormatter getInstance(final ULocale locale) {
        return ListFormatter.cache.get(locale);
    }
    
    public static ListFormatter getInstance(final Locale locale) {
        return getInstance(ULocale.forLocale(locale));
    }
    
    public static ListFormatter getInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public String format(final Object... items) {
        return this.format(Arrays.asList(items));
    }
    
    public String format(final Collection<?> items) {
        final Iterator<?> it = items.iterator();
        int count = items.size();
        switch (count) {
            case 0: {
                return "";
            }
            case 1: {
                return it.next().toString();
            }
            case 2: {
                return this.format2(this.two, it.next(), it.next());
            }
            default: {
                String result = it.next().toString();
                result = this.format2(this.start, result, it.next());
                for (count -= 3; count > 0; --count) {
                    result = this.format2(this.middle, result, it.next());
                }
                return this.format2(this.end, result, it.next());
            }
        }
    }
    
    private String format2(final String pattern, final Object a, final Object b) {
        final int i0 = pattern.indexOf("{0}");
        final int i2 = pattern.indexOf("{1}");
        if (i0 < 0 || i2 < 0) {
            throw new IllegalArgumentException("Missing {0} or {1} in pattern " + pattern);
        }
        if (i0 < i2) {
            return pattern.substring(0, i0) + a + pattern.substring(i0 + 3, i2) + b + pattern.substring(i2 + 3);
        }
        return pattern.substring(0, i2) + b + pattern.substring(i2 + 3, i0) + a + pattern.substring(i0 + 3);
    }
    
    static void add(final String locale, final String... data) {
        ListFormatter.localeToData.put(new ULocale(locale), new ListFormatter(data[0], data[1], data[2], data[3]));
    }
    
    static {
        ListFormatter.localeToData = new HashMap<ULocale, ListFormatter>();
        ListFormatter.cache = new Cache();
    }
    
    private static class Cache
    {
        private final ICUCache<ULocale, ListFormatter> cache;
        
        private Cache() {
            this.cache = new SimpleCache<ULocale, ListFormatter>();
        }
        
        public ListFormatter get(final ULocale locale) {
            ListFormatter result = this.cache.get(locale);
            if (result == null) {
                result = load(locale);
                this.cache.put(locale, result);
            }
            return result;
        }
        
        private static ListFormatter load(final ULocale ulocale) {
            ICUResourceBundle r = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", ulocale);
            r = r.getWithFallback("listPattern/standard");
            return new ListFormatter(r.getWithFallback("2").getString(), r.getWithFallback("start").getString(), r.getWithFallback("middle").getString(), r.getWithFallback("end").getString());
        }
    }
}
