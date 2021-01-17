// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.impl.ICUService;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Locale;
import com.ibm.icu.impl.ICULocaleService;

final class CurrencyServiceShim extends Currency.ServiceShim
{
    static final ICULocaleService service;
    
    @Override
    Locale[] getAvailableLocales() {
        if (CurrencyServiceShim.service.isDefault()) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return CurrencyServiceShim.service.getAvailableLocales();
    }
    
    @Override
    ULocale[] getAvailableULocales() {
        if (CurrencyServiceShim.service.isDefault()) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return CurrencyServiceShim.service.getAvailableULocales();
    }
    
    @Override
    Currency createInstance(final ULocale loc) {
        if (CurrencyServiceShim.service.isDefault()) {
            return Currency.createCurrency(loc);
        }
        final Currency curr = (Currency)CurrencyServiceShim.service.get(loc);
        return curr;
    }
    
    @Override
    Object registerInstance(final Currency currency, final ULocale locale) {
        return CurrencyServiceShim.service.registerObject(currency, locale);
    }
    
    @Override
    boolean unregister(final Object registryKey) {
        return CurrencyServiceShim.service.unregisterFactory((ICUService.Factory)registryKey);
    }
    
    static {
        service = new CFService();
    }
    
    private static class CFService extends ICULocaleService
    {
        CFService() {
            super("Currency");
            this.registerFactory(new CurrencyFactory());
            this.markDefault();
        }
    }
}
