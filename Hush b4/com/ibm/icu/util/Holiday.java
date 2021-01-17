// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;

public abstract class Holiday implements DateRule
{
    private String name;
    private DateRule rule;
    private static Holiday[] noHolidays;
    
    public static Holiday[] getHolidays() {
        return getHolidays(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public static Holiday[] getHolidays(final Locale locale) {
        return getHolidays(ULocale.forLocale(locale));
    }
    
    public static Holiday[] getHolidays(final ULocale locale) {
        Holiday[] result = Holiday.noHolidays;
        try {
            final ResourceBundle bundle = UResourceBundle.getBundleInstance("com.ibm.icu.impl.data.HolidayBundle", locale);
            result = (Holiday[])bundle.getObject("holidays");
        }
        catch (MissingResourceException ex) {}
        return result;
    }
    
    public Date firstAfter(final Date start) {
        return this.rule.firstAfter(start);
    }
    
    public Date firstBetween(final Date start, final Date end) {
        return this.rule.firstBetween(start, end);
    }
    
    public boolean isOn(final Date date) {
        return this.rule.isOn(date);
    }
    
    public boolean isBetween(final Date start, final Date end) {
        return this.rule.isBetween(start, end);
    }
    
    protected Holiday(final String name, final DateRule rule) {
        this.name = name;
        this.rule = rule;
    }
    
    public String getDisplayName() {
        return this.getDisplayName(ULocale.getDefault(ULocale.Category.DISPLAY));
    }
    
    public String getDisplayName(final Locale locale) {
        return this.getDisplayName(ULocale.forLocale(locale));
    }
    
    public String getDisplayName(final ULocale locale) {
        String dispName = this.name;
        try {
            final ResourceBundle bundle = UResourceBundle.getBundleInstance("com.ibm.icu.impl.data.HolidayBundle", locale);
            dispName = bundle.getString(this.name);
        }
        catch (MissingResourceException ex) {}
        return dispName;
    }
    
    public DateRule getRule() {
        return this.rule;
    }
    
    public void setRule(final DateRule rule) {
        this.rule = rule;
    }
    
    static {
        Holiday.noHolidays = new Holiday[0];
    }
}
