// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;

public class DangiCalendar extends ChineseCalendar
{
    private static final long serialVersionUID = 8156297445349501985L;
    private static final int DANGI_EPOCH_YEAR = -2332;
    private static final TimeZone KOREA_ZONE;
    
    @Deprecated
    public DangiCalendar() {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    @Deprecated
    public DangiCalendar(final Date date) {
        this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
        this.setTime(date);
    }
    
    @Deprecated
    public DangiCalendar(final TimeZone zone, final ULocale locale) {
        super(zone, locale, -2332, DangiCalendar.KOREA_ZONE);
    }
    
    @Override
    @Deprecated
    public String getType() {
        return "dangi";
    }
    
    static {
        final InitialTimeZoneRule initialTimeZone = new InitialTimeZoneRule("GMT+8", 28800000, 0);
        final long[] millis1897 = { -2302128000000L };
        final long[] millis1898 = { -2270592000000L };
        final long[] millis1899 = { -1829088000000L };
        final TimeZoneRule rule1897 = new TimeArrayTimeZoneRule("Korean 1897", 25200000, 0, millis1897, 1);
        final TimeZoneRule rule1898to1911 = new TimeArrayTimeZoneRule("Korean 1898-1911", 28800000, 0, millis1898, 1);
        final TimeZoneRule ruleFrom1912 = new TimeArrayTimeZoneRule("Korean 1912-", 32400000, 0, millis1899, 1);
        final RuleBasedTimeZone tz = new RuleBasedTimeZone("KOREA_ZONE", initialTimeZone);
        tz.addTransitionRule(rule1897);
        tz.addTransitionRule(rule1898to1911);
        tz.addTransitionRule(ruleFrom1912);
        tz.freeze();
        KOREA_ZONE = tz;
    }
}
