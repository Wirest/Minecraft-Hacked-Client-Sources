// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.lang.reflect.Field;
import java.util.Date;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.GregorianCalendar;
import java.util.Collections;
import java.util.List;

public class CurrencyMetaInfo
{
    private static final CurrencyMetaInfo impl;
    private static final boolean hasData;
    @Deprecated
    protected static final CurrencyDigits defaultDigits;
    
    public static CurrencyMetaInfo getInstance() {
        return CurrencyMetaInfo.impl;
    }
    
    public static CurrencyMetaInfo getInstance(final boolean noSubstitute) {
        return CurrencyMetaInfo.hasData ? CurrencyMetaInfo.impl : null;
    }
    
    @Deprecated
    public static boolean hasData() {
        return CurrencyMetaInfo.hasData;
    }
    
    @Deprecated
    protected CurrencyMetaInfo() {
    }
    
    public List<CurrencyInfo> currencyInfo(final CurrencyFilter filter) {
        return Collections.emptyList();
    }
    
    public List<String> currencies(final CurrencyFilter filter) {
        return Collections.emptyList();
    }
    
    public List<String> regions(final CurrencyFilter filter) {
        return Collections.emptyList();
    }
    
    public CurrencyDigits currencyDigits(final String isoCode) {
        return CurrencyMetaInfo.defaultDigits;
    }
    
    private static String dateString(final long date) {
        if (date == Long.MAX_VALUE || date == Long.MIN_VALUE) {
            return null;
        }
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(TimeZone.getTimeZone("GMT"));
        gc.setTimeInMillis(date);
        return "" + gc.get(1) + '-' + (gc.get(2) + 1) + '-' + gc.get(5);
    }
    
    private static String debugString(final Object o) {
        final StringBuilder sb = new StringBuilder();
        try {
            for (final Field f : o.getClass().getFields()) {
                final Object v = f.get(o);
                if (v != null) {
                    String s;
                    if (v instanceof Date) {
                        s = dateString(((Date)v).getTime());
                    }
                    else if (v instanceof Long) {
                        s = dateString((long)v);
                    }
                    else {
                        s = String.valueOf(v);
                    }
                    if (s != null) {
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        sb.append(f.getName()).append("='").append(s).append("'");
                    }
                }
            }
        }
        catch (Throwable t) {}
        sb.insert(0, o.getClass().getSimpleName() + "(");
        sb.append(")");
        return sb.toString();
    }
    
    static {
        defaultDigits = new CurrencyDigits(2, 0);
        CurrencyMetaInfo temp = null;
        boolean tempHasData = false;
        try {
            final Class<?> clzz = Class.forName("com.ibm.icu.impl.ICUCurrencyMetaInfo");
            temp = (CurrencyMetaInfo)clzz.newInstance();
            tempHasData = true;
        }
        catch (Throwable t) {
            temp = new CurrencyMetaInfo();
        }
        impl = temp;
        hasData = tempHasData;
    }
    
    public static final class CurrencyFilter
    {
        public final String region;
        public final String currency;
        public final long from;
        public final long to;
        @Deprecated
        public final boolean tenderOnly;
        private static final CurrencyFilter ALL;
        
        private CurrencyFilter(final String region, final String currency, final long from, final long to, final boolean tenderOnly) {
            this.region = region;
            this.currency = currency;
            this.from = from;
            this.to = to;
            this.tenderOnly = tenderOnly;
        }
        
        public static CurrencyFilter all() {
            return CurrencyFilter.ALL;
        }
        
        public static CurrencyFilter now() {
            return CurrencyFilter.ALL.withDate(new Date());
        }
        
        public static CurrencyFilter onRegion(final String region) {
            return CurrencyFilter.ALL.withRegion(region);
        }
        
        public static CurrencyFilter onCurrency(final String currency) {
            return CurrencyFilter.ALL.withCurrency(currency);
        }
        
        public static CurrencyFilter onDate(final Date date) {
            return CurrencyFilter.ALL.withDate(date);
        }
        
        public static CurrencyFilter onDateRange(final Date from, final Date to) {
            return CurrencyFilter.ALL.withDateRange(from, to);
        }
        
        public static CurrencyFilter onDate(final long date) {
            return CurrencyFilter.ALL.withDate(date);
        }
        
        public static CurrencyFilter onDateRange(final long from, final long to) {
            return CurrencyFilter.ALL.withDateRange(from, to);
        }
        
        public static CurrencyFilter onTender() {
            return CurrencyFilter.ALL.withTender();
        }
        
        public CurrencyFilter withRegion(final String region) {
            return new CurrencyFilter(region, this.currency, this.from, this.to, this.tenderOnly);
        }
        
        public CurrencyFilter withCurrency(final String currency) {
            return new CurrencyFilter(this.region, currency, this.from, this.to, this.tenderOnly);
        }
        
        public CurrencyFilter withDate(final Date date) {
            return new CurrencyFilter(this.region, this.currency, date.getTime(), date.getTime(), this.tenderOnly);
        }
        
        public CurrencyFilter withDateRange(final Date from, final Date to) {
            final long fromLong = (from == null) ? Long.MIN_VALUE : from.getTime();
            final long toLong = (to == null) ? Long.MAX_VALUE : to.getTime();
            return new CurrencyFilter(this.region, this.currency, fromLong, toLong, this.tenderOnly);
        }
        
        public CurrencyFilter withDate(final long date) {
            return new CurrencyFilter(this.region, this.currency, date, date, this.tenderOnly);
        }
        
        public CurrencyFilter withDateRange(final long from, final long to) {
            return new CurrencyFilter(this.region, this.currency, from, to, this.tenderOnly);
        }
        
        public CurrencyFilter withTender() {
            return new CurrencyFilter(this.region, this.currency, this.from, this.to, true);
        }
        
        @Override
        public boolean equals(final Object rhs) {
            return rhs instanceof CurrencyFilter && this.equals((CurrencyFilter)rhs);
        }
        
        public boolean equals(final CurrencyFilter rhs) {
            return this == rhs || (rhs != null && equals(this.region, rhs.region) && equals(this.currency, rhs.currency) && this.from == rhs.from && this.to == rhs.to && this.tenderOnly == rhs.tenderOnly);
        }
        
        @Override
        public int hashCode() {
            int hc = 0;
            if (this.region != null) {
                hc = this.region.hashCode();
            }
            if (this.currency != null) {
                hc = hc * 31 + this.currency.hashCode();
            }
            hc = hc * 31 + (int)this.from;
            hc = hc * 31 + (int)(this.from >>> 32);
            hc = hc * 31 + (int)this.to;
            hc = hc * 31 + (int)(this.to >>> 32);
            hc = hc * 31 + (this.tenderOnly ? 1 : 0);
            return hc;
        }
        
        @Override
        public String toString() {
            return debugString(this);
        }
        
        private static boolean equals(final String lhs, final String rhs) {
            return lhs == rhs || (lhs != null && lhs.equals(rhs));
        }
        
        static {
            ALL = new CurrencyFilter(null, null, Long.MIN_VALUE, Long.MAX_VALUE, false);
        }
    }
    
    public static final class CurrencyDigits
    {
        public final int fractionDigits;
        public final int roundingIncrement;
        
        public CurrencyDigits(final int fractionDigits, final int roundingIncrement) {
            this.fractionDigits = fractionDigits;
            this.roundingIncrement = roundingIncrement;
        }
        
        @Override
        public String toString() {
            return debugString(this);
        }
    }
    
    public static final class CurrencyInfo
    {
        public final String region;
        public final String code;
        public final long from;
        public final long to;
        public final int priority;
        private final boolean tender;
        
        @Deprecated
        public CurrencyInfo(final String region, final String code, final long from, final long to, final int priority) {
            this(region, code, from, to, priority, true);
        }
        
        @Deprecated
        public CurrencyInfo(final String region, final String code, final long from, final long to, final int priority, final boolean tender) {
            this.region = region;
            this.code = code;
            this.from = from;
            this.to = to;
            this.priority = priority;
            this.tender = tender;
        }
        
        @Override
        public String toString() {
            return debugString(this);
        }
        
        public boolean isTender() {
            return this.tender;
        }
    }
}
