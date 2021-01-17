// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Set;
import com.ibm.icu.impl.ICUService;
import java.util.MissingResourceException;
import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Locale;
import com.ibm.icu.impl.ICULocaleService;

class CalendarServiceShim extends Calendar.CalendarShim
{
    private static ICULocaleService service;
    
    @Override
    Locale[] getAvailableLocales() {
        if (CalendarServiceShim.service.isDefault()) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return CalendarServiceShim.service.getAvailableLocales();
    }
    
    @Override
    ULocale[] getAvailableULocales() {
        if (CalendarServiceShim.service.isDefault()) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return CalendarServiceShim.service.getAvailableULocales();
    }
    
    @Override
    Calendar createInstance(ULocale desiredLocale) {
        final ULocale[] actualLoc = { null };
        if (desiredLocale.equals(ULocale.ROOT)) {
            desiredLocale = ULocale.ROOT;
        }
        ULocale useLocale;
        if (desiredLocale.getKeywordValue("calendar") == null) {
            final String calType = CalendarUtil.getCalendarType(desiredLocale);
            useLocale = desiredLocale.setKeywordValue("calendar", calType);
        }
        else {
            useLocale = desiredLocale;
        }
        Calendar cal = (Calendar)CalendarServiceShim.service.get(useLocale, actualLoc);
        if (cal == null) {
            throw new MissingResourceException("Unable to construct Calendar", "", "");
        }
        cal = (Calendar)cal.clone();
        return cal;
    }
    
    @Override
    Object registerFactory(final Calendar.CalendarFactory factory) {
        return CalendarServiceShim.service.registerFactory(new CalFactory(factory));
    }
    
    @Override
    boolean unregister(final Object k) {
        return CalendarServiceShim.service.unregisterFactory((ICUService.Factory)k);
    }
    
    static {
        CalendarServiceShim.service = new CalService();
    }
    
    private static final class CalFactory extends ICULocaleService.LocaleKeyFactory
    {
        private Calendar.CalendarFactory delegate;
        
        CalFactory(final Calendar.CalendarFactory delegate) {
            super(delegate.visible());
            this.delegate = delegate;
        }
        
        @Override
        public Object create(final ICUService.Key key, final ICUService srvc) {
            if (!this.handlesKey(key) || !(key instanceof ICULocaleService.LocaleKey)) {
                return null;
            }
            final ICULocaleService.LocaleKey lkey = (ICULocaleService.LocaleKey)key;
            Object result = this.delegate.createCalendar(lkey.canonicalLocale());
            if (result == null) {
                result = srvc.getKey(key, null, this);
            }
            return result;
        }
        
        @Override
        protected Set<String> getSupportedIDs() {
            return this.delegate.getSupportedLocaleNames();
        }
    }
    
    private static class CalService extends ICULocaleService
    {
        CalService() {
            super("Calendar");
            this.registerFactory(new RBCalendarFactory());
            this.markDefault();
        }
    }
}
