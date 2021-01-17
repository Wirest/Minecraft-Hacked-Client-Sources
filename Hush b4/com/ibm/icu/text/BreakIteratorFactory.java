// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.InputStream;
import java.io.IOException;
import com.ibm.icu.impl.Assert;
import java.util.MissingResourceException;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Locale;
import com.ibm.icu.impl.ICUService;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.impl.ICULocaleService;

final class BreakIteratorFactory extends BreakIterator.BreakIteratorServiceShim
{
    static final ICULocaleService service;
    private static final String[] KIND_NAMES;
    
    @Override
    public Object registerInstance(final BreakIterator iter, final ULocale locale, final int kind) {
        iter.setText(new StringCharacterIterator(""));
        return BreakIteratorFactory.service.registerObject(iter, locale, kind);
    }
    
    @Override
    public boolean unregister(final Object key) {
        return !BreakIteratorFactory.service.isDefault() && BreakIteratorFactory.service.unregisterFactory((ICUService.Factory)key);
    }
    
    @Override
    public Locale[] getAvailableLocales() {
        if (BreakIteratorFactory.service == null) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return BreakIteratorFactory.service.getAvailableLocales();
    }
    
    @Override
    public ULocale[] getAvailableULocales() {
        if (BreakIteratorFactory.service == null) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return BreakIteratorFactory.service.getAvailableULocales();
    }
    
    @Override
    public BreakIterator createBreakIterator(final ULocale locale, final int kind) {
        if (BreakIteratorFactory.service.isDefault()) {
            return createBreakInstance(locale, kind);
        }
        final ULocale[] actualLoc = { null };
        final BreakIterator iter = (BreakIterator)BreakIteratorFactory.service.get(locale, kind, actualLoc);
        iter.setLocale(actualLoc[0], actualLoc[0]);
        return iter;
    }
    
    private static BreakIterator createBreakInstance(final ULocale locale, final int kind) {
        RuleBasedBreakIterator iter = null;
        final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/brkitr", locale);
        InputStream ruleStream = null;
        try {
            final String typeKey = BreakIteratorFactory.KIND_NAMES[kind];
            final String brkfname = rb.getStringWithFallback("boundaries/" + typeKey);
            final String rulesFileName = "data/icudt51b/brkitr/" + brkfname;
            ruleStream = ICUData.getStream(rulesFileName);
        }
        catch (Exception e) {
            throw new MissingResourceException(e.toString(), "", "");
        }
        try {
            iter = RuleBasedBreakIterator.getInstanceFromCompiledRules(ruleStream);
        }
        catch (IOException e2) {
            Assert.fail(e2);
        }
        final ULocale uloc = ULocale.forLocale(rb.getLocale());
        iter.setLocale(uloc, uloc);
        iter.setBreakType(kind);
        return iter;
    }
    
    static {
        service = new BFService();
        KIND_NAMES = new String[] { "grapheme", "word", "line", "sentence", "title" };
    }
    
    private static class BFService extends ICULocaleService
    {
        BFService() {
            super("BreakIterator");
            this.registerFactory(new RBBreakIteratorFactory());
            this.markDefault();
        }
    }
}
