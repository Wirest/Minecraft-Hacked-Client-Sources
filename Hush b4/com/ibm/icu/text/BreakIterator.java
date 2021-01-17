// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import java.util.MissingResourceException;
import java.util.Locale;
import java.text.StringCharacterIterator;
import java.text.CharacterIterator;
import com.ibm.icu.util.ULocale;
import java.lang.ref.SoftReference;

public abstract class BreakIterator implements Cloneable
{
    private static final boolean DEBUG;
    public static final int DONE = -1;
    public static final int KIND_CHARACTER = 0;
    public static final int KIND_WORD = 1;
    public static final int KIND_LINE = 2;
    public static final int KIND_SENTENCE = 3;
    public static final int KIND_TITLE = 4;
    private static final int KIND_COUNT = 5;
    private static final SoftReference<?>[] iterCache;
    private static BreakIteratorServiceShim shim;
    private ULocale validLocale;
    private ULocale actualLocale;
    
    protected BreakIterator() {
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
    
    public abstract int first();
    
    public abstract int last();
    
    public abstract int next(final int p0);
    
    public abstract int next();
    
    public abstract int previous();
    
    public abstract int following(final int p0);
    
    public int preceding(final int offset) {
        int pos;
        for (pos = this.following(offset); pos >= offset && pos != -1; pos = this.previous()) {}
        return pos;
    }
    
    public boolean isBoundary(final int offset) {
        return offset == 0 || this.following(offset - 1) == offset;
    }
    
    public abstract int current();
    
    public abstract CharacterIterator getText();
    
    public void setText(final String newText) {
        this.setText(new StringCharacterIterator(newText));
    }
    
    public abstract void setText(final CharacterIterator p0);
    
    public static BreakIterator getWordInstance() {
        return getWordInstance(ULocale.getDefault());
    }
    
    public static BreakIterator getWordInstance(final Locale where) {
        return getBreakInstance(ULocale.forLocale(where), 1);
    }
    
    public static BreakIterator getWordInstance(final ULocale where) {
        return getBreakInstance(where, 1);
    }
    
    public static BreakIterator getLineInstance() {
        return getLineInstance(ULocale.getDefault());
    }
    
    public static BreakIterator getLineInstance(final Locale where) {
        return getBreakInstance(ULocale.forLocale(where), 2);
    }
    
    public static BreakIterator getLineInstance(final ULocale where) {
        return getBreakInstance(where, 2);
    }
    
    public static BreakIterator getCharacterInstance() {
        return getCharacterInstance(ULocale.getDefault());
    }
    
    public static BreakIterator getCharacterInstance(final Locale where) {
        return getBreakInstance(ULocale.forLocale(where), 0);
    }
    
    public static BreakIterator getCharacterInstance(final ULocale where) {
        return getBreakInstance(where, 0);
    }
    
    public static BreakIterator getSentenceInstance() {
        return getSentenceInstance(ULocale.getDefault());
    }
    
    public static BreakIterator getSentenceInstance(final Locale where) {
        return getBreakInstance(ULocale.forLocale(where), 3);
    }
    
    public static BreakIterator getSentenceInstance(final ULocale where) {
        return getBreakInstance(where, 3);
    }
    
    public static BreakIterator getTitleInstance() {
        return getTitleInstance(ULocale.getDefault());
    }
    
    public static BreakIterator getTitleInstance(final Locale where) {
        return getBreakInstance(ULocale.forLocale(where), 4);
    }
    
    public static BreakIterator getTitleInstance(final ULocale where) {
        return getBreakInstance(where, 4);
    }
    
    public static Object registerInstance(final BreakIterator iter, final Locale locale, final int kind) {
        return registerInstance(iter, ULocale.forLocale(locale), kind);
    }
    
    public static Object registerInstance(final BreakIterator iter, final ULocale locale, final int kind) {
        if (BreakIterator.iterCache[kind] != null) {
            final BreakIteratorCache cache = (BreakIteratorCache)BreakIterator.iterCache[kind].get();
            if (cache != null && cache.getLocale().equals(locale)) {
                BreakIterator.iterCache[kind] = null;
            }
        }
        return getShim().registerInstance(iter, locale, kind);
    }
    
    public static boolean unregister(final Object key) {
        if (key == null) {
            throw new IllegalArgumentException("registry key must not be null");
        }
        if (BreakIterator.shim != null) {
            for (int kind = 0; kind < 5; ++kind) {
                BreakIterator.iterCache[kind] = null;
            }
            return BreakIterator.shim.unregister(key);
        }
        return false;
    }
    
    @Deprecated
    public static BreakIterator getBreakInstance(final ULocale where, final int kind) {
        if (BreakIterator.iterCache[kind] != null) {
            final BreakIteratorCache cache = (BreakIteratorCache)BreakIterator.iterCache[kind].get();
            if (cache != null && cache.getLocale().equals(where)) {
                return cache.createBreakInstance();
            }
        }
        final BreakIterator result = getShim().createBreakIterator(where, kind);
        final BreakIteratorCache cache2 = new BreakIteratorCache(where, result);
        BreakIterator.iterCache[kind] = new SoftReference<Object>(cache2);
        if (result instanceof RuleBasedBreakIterator) {
            final RuleBasedBreakIterator rbbi = (RuleBasedBreakIterator)result;
            rbbi.setBreakType(kind);
        }
        return result;
    }
    
    public static synchronized Locale[] getAvailableLocales() {
        return getShim().getAvailableLocales();
    }
    
    public static synchronized ULocale[] getAvailableULocales() {
        return getShim().getAvailableULocales();
    }
    
    private static BreakIteratorServiceShim getShim() {
        if (BreakIterator.shim == null) {
            try {
                final Class<?> cls = Class.forName("com.ibm.icu.text.BreakIteratorFactory");
                BreakIterator.shim = (BreakIteratorServiceShim)cls.newInstance();
            }
            catch (MissingResourceException e) {
                throw e;
            }
            catch (Exception e2) {
                if (BreakIterator.DEBUG) {
                    e2.printStackTrace();
                }
                throw new RuntimeException(e2.getMessage());
            }
        }
        return BreakIterator.shim;
    }
    
    public final ULocale getLocale(final ULocale.Type type) {
        return (type == ULocale.ACTUAL_LOCALE) ? this.actualLocale : this.validLocale;
    }
    
    final void setLocale(final ULocale valid, final ULocale actual) {
        if (valid == null != (actual == null)) {
            throw new IllegalArgumentException();
        }
        this.validLocale = valid;
        this.actualLocale = actual;
    }
    
    static {
        DEBUG = ICUDebug.enabled("breakiterator");
        iterCache = new SoftReference[5];
    }
    
    private static final class BreakIteratorCache
    {
        private BreakIterator iter;
        private ULocale where;
        
        BreakIteratorCache(final ULocale where, final BreakIterator iter) {
            this.where = where;
            this.iter = (BreakIterator)iter.clone();
        }
        
        ULocale getLocale() {
            return this.where;
        }
        
        BreakIterator createBreakInstance() {
            return (BreakIterator)this.iter.clone();
        }
    }
    
    abstract static class BreakIteratorServiceShim
    {
        public abstract Object registerInstance(final BreakIterator p0, final ULocale p1, final int p2);
        
        public abstract boolean unregister(final Object p0);
        
        public abstract Locale[] getAvailableLocales();
        
        public abstract ULocale[] getAvailableULocales();
        
        public abstract BreakIterator createBreakIterator(final ULocale p0, final int p1);
    }
}
