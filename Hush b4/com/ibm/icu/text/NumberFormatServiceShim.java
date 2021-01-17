// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Set;
import com.ibm.icu.util.Currency;
import java.util.MissingResourceException;
import com.ibm.icu.impl.ICUService;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Locale;
import com.ibm.icu.impl.ICULocaleService;

class NumberFormatServiceShim extends NumberFormat.NumberFormatShim
{
    private static ICULocaleService service;
    
    @Override
    Locale[] getAvailableLocales() {
        if (NumberFormatServiceShim.service.isDefault()) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return NumberFormatServiceShim.service.getAvailableLocales();
    }
    
    @Override
    ULocale[] getAvailableULocales() {
        if (NumberFormatServiceShim.service.isDefault()) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return NumberFormatServiceShim.service.getAvailableULocales();
    }
    
    @Override
    Object registerFactory(final NumberFormat.NumberFormatFactory factory) {
        return NumberFormatServiceShim.service.registerFactory(new NFFactory(factory));
    }
    
    @Override
    boolean unregister(final Object registryKey) {
        return NumberFormatServiceShim.service.unregisterFactory((ICUService.Factory)registryKey);
    }
    
    @Override
    NumberFormat createInstance(final ULocale desiredLocale, final int choice) {
        final ULocale[] actualLoc = { null };
        NumberFormat fmt = (NumberFormat)NumberFormatServiceShim.service.get(desiredLocale, choice, actualLoc);
        if (fmt == null) {
            throw new MissingResourceException("Unable to construct NumberFormat", "", "");
        }
        fmt = (NumberFormat)fmt.clone();
        if (choice == 1 || choice == 5 || choice == 6) {
            fmt.setCurrency(Currency.getInstance(desiredLocale));
        }
        final ULocale uloc = actualLoc[0];
        fmt.setLocale(uloc, uloc);
        return fmt;
    }
    
    static {
        NumberFormatServiceShim.service = new NFService();
    }
    
    private static final class NFFactory extends ICULocaleService.LocaleKeyFactory
    {
        private NumberFormat.NumberFormatFactory delegate;
        
        NFFactory(final NumberFormat.NumberFormatFactory delegate) {
            super(delegate.visible());
            this.delegate = delegate;
        }
        
        @Override
        public Object create(final ICUService.Key key, final ICUService srvc) {
            if (!this.handlesKey(key) || !(key instanceof ICULocaleService.LocaleKey)) {
                return null;
            }
            final ICULocaleService.LocaleKey lkey = (ICULocaleService.LocaleKey)key;
            Object result = this.delegate.createFormat(lkey.canonicalLocale(), lkey.kind());
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
    
    private static class NFService extends ICULocaleService
    {
        NFService() {
            super("NumberFormat");
            this.registerFactory(new RBNumberFormatFactory());
            this.markDefault();
        }
    }
}
