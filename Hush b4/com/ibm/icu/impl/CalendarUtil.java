// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.ULocale;

public class CalendarUtil
{
    private static ICUCache<String, String> CALTYPE_CACHE;
    private static final String CALKEY = "calendar";
    private static final String DEFCAL = "gregorian";
    
    public static String getCalendarType(final ULocale loc) {
        String calType = null;
        calType = loc.getKeywordValue("calendar");
        if (calType != null) {
            return calType;
        }
        final String baseLoc = loc.getBaseName();
        calType = CalendarUtil.CALTYPE_CACHE.get(baseLoc);
        if (calType != null) {
            return calType;
        }
        final ULocale canonical = ULocale.createCanonical(loc.toString());
        calType = canonical.getKeywordValue("calendar");
        if (calType == null) {
            String region = canonical.getCountry();
            if (region.length() == 0) {
                final ULocale fullLoc = ULocale.addLikelySubtags(canonical);
                region = fullLoc.getCountry();
            }
            try {
                final UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                final UResourceBundle calPref = rb.get("calendarPreferenceData");
                UResourceBundle order = null;
                try {
                    order = calPref.get(region);
                }
                catch (MissingResourceException mre) {
                    order = calPref.get("001");
                }
                calType = order.getString(0);
            }
            catch (MissingResourceException ex) {}
            if (calType == null) {
                calType = "gregorian";
            }
        }
        CalendarUtil.CALTYPE_CACHE.put(baseLoc, calType);
        return calType;
    }
    
    static {
        CalendarUtil.CALTYPE_CACHE = new SimpleCache<String, String>();
    }
}
