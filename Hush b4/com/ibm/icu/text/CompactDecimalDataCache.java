// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.impl.ICUCache;

class CompactDecimalDataCache
{
    private static final String SHORT_STYLE = "short";
    private static final String LONG_STYLE = "long";
    private static final String NUMBER_ELEMENTS = "NumberElements";
    private static final String PATTERN_LONG_PATH = "patternsLong/decimalFormat";
    private static final String PATTERNS_SHORT_PATH = "patternsShort/decimalFormat";
    static final String OTHER = "other";
    static final int MAX_DIGITS = 15;
    private static final String LATIN_NUMBERING_SYSTEM = "latn";
    private final ICUCache<ULocale, DataBundle> cache;
    
    CompactDecimalDataCache() {
        this.cache = new SimpleCache<ULocale, DataBundle>();
    }
    
    DataBundle get(final ULocale locale) {
        DataBundle result = this.cache.get(locale);
        if (result == null) {
            result = load(locale);
            this.cache.put(locale, result);
        }
        return result;
    }
    
    private static DataBundle load(final ULocale ulocale) {
        final NumberingSystem ns = NumberingSystem.getInstance(ulocale);
        ICUResourceBundle r = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", ulocale);
        r = r.getWithFallback("NumberElements");
        final String numberingSystemName = ns.getName();
        ICUResourceBundle shortDataBundle = null;
        ICUResourceBundle longDataBundle = null;
        if (!"latn".equals(numberingSystemName)) {
            final ICUResourceBundle bundle = findWithFallback(r, numberingSystemName, UResFlags.NOT_ROOT);
            shortDataBundle = findWithFallback(bundle, "patternsShort/decimalFormat", UResFlags.NOT_ROOT);
            longDataBundle = findWithFallback(bundle, "patternsLong/decimalFormat", UResFlags.NOT_ROOT);
        }
        if (shortDataBundle == null) {
            final ICUResourceBundle bundle = getWithFallback(r, "latn", UResFlags.ANY);
            shortDataBundle = getWithFallback(bundle, "patternsShort/decimalFormat", UResFlags.ANY);
            if (longDataBundle == null) {
                longDataBundle = findWithFallback(bundle, "patternsLong/decimalFormat", UResFlags.ANY);
                if (longDataBundle != null && isRoot(longDataBundle) && !isRoot(shortDataBundle)) {
                    longDataBundle = null;
                }
            }
        }
        final Data shortData = loadStyle(shortDataBundle, ulocale, "short");
        Data longData;
        if (longDataBundle == null) {
            longData = shortData;
        }
        else {
            longData = loadStyle(longDataBundle, ulocale, "long");
        }
        return new DataBundle(shortData, longData);
    }
    
    private static ICUResourceBundle findWithFallback(final ICUResourceBundle r, final String path, final UResFlags flags) {
        if (r == null) {
            return null;
        }
        final ICUResourceBundle result = r.findWithFallback(path);
        if (result == null) {
            return null;
        }
        switch (flags) {
            case NOT_ROOT: {
                return isRoot(result) ? null : result;
            }
            case ANY: {
                return result;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    private static ICUResourceBundle getWithFallback(final ICUResourceBundle r, final String path, final UResFlags flags) {
        final ICUResourceBundle result = findWithFallback(r, path, flags);
        if (result == null) {
            throw new MissingResourceException("Cannot find " + path, ICUResourceBundle.class.getName(), path);
        }
        return result;
    }
    
    private static boolean isRoot(final ICUResourceBundle r) {
        final ULocale bundleLocale = r.getULocale();
        return bundleLocale.equals(ULocale.ROOT) || bundleLocale.toString().equals("root");
    }
    
    private static Data loadStyle(final ICUResourceBundle r, final ULocale locale, final String style) {
        final int size = r.getSize();
        final Data result = new Data(new long[15], new HashMap<String, DecimalFormat.Unit[]>());
        for (int i = 0; i < size; ++i) {
            populateData(r.get(i), locale, style, result);
        }
        fillInMissing(result);
        return result;
    }
    
    private static void populateData(final UResourceBundle divisorData, final ULocale locale, final String style, final Data result) {
        final long magnitude = Long.parseLong(divisorData.getKey());
        final int thisIndex = (int)Math.log10((double)magnitude);
        if (thisIndex >= 15) {
            return;
        }
        final int size = divisorData.getSize();
        int numZeros = 0;
        boolean otherVariantDefined = false;
        for (int i = 0; i < size; ++i) {
            final UResourceBundle pluralVariantData = divisorData.get(i);
            final String pluralVariant = pluralVariantData.getKey();
            final String template = pluralVariantData.getString();
            if (pluralVariant.equals("other")) {
                otherVariantDefined = true;
            }
            final int nz = populatePrefixSuffix(pluralVariant, thisIndex, template, locale, style, result);
            if (nz != numZeros) {
                if (numZeros != 0) {
                    throw new IllegalArgumentException("Plural variant '" + pluralVariant + "' template '" + template + "' for 10^" + thisIndex + " has wrong number of zeros in " + localeAndStyle(locale, style));
                }
                numZeros = nz;
            }
        }
        if (!otherVariantDefined) {
            throw new IllegalArgumentException("No 'other' plural variant defined for 10^" + thisIndex + "in " + localeAndStyle(locale, style));
        }
        long divisor = magnitude;
        for (int j = 1; j < numZeros; ++j) {
            divisor /= 10L;
        }
        result.divisors[thisIndex] = divisor;
    }
    
    private static int populatePrefixSuffix(final String pluralVariant, final int idx, final String template, final ULocale locale, final String style, final Data result) {
        final int firstIdx = template.indexOf("0");
        final int lastIdx = template.lastIndexOf("0");
        if (firstIdx == -1) {
            throw new IllegalArgumentException("Expect at least one zero in template '" + template + "' for variant '" + pluralVariant + "' for 10^" + idx + " in " + localeAndStyle(locale, style));
        }
        final String prefix = fixQuotes(template.substring(0, firstIdx));
        final String suffix = fixQuotes(template.substring(lastIdx + 1));
        saveUnit(new DecimalFormat.Unit(prefix, suffix), pluralVariant, idx, result.units);
        if (prefix.trim().length() == 0 && suffix.trim().length() == 0) {
            return idx + 1;
        }
        int i;
        for (i = firstIdx + 1; i <= lastIdx && template.charAt(i) == '0'; ++i) {}
        return i - firstIdx;
    }
    
    private static String fixQuotes(final String prefixOrSuffix) {
        final StringBuilder result = new StringBuilder();
        final int len = prefixOrSuffix.length();
        QuoteState state = QuoteState.OUTSIDE;
        for (int idx = 0; idx < len; ++idx) {
            final char ch = prefixOrSuffix.charAt(idx);
            if (ch == '\'') {
                if (state == QuoteState.INSIDE_EMPTY) {
                    result.append('\'');
                }
            }
            else {
                result.append(ch);
            }
            switch (state) {
                case OUTSIDE: {
                    state = ((ch == '\'') ? QuoteState.INSIDE_EMPTY : QuoteState.OUTSIDE);
                    break;
                }
                case INSIDE_EMPTY:
                case INSIDE_FULL: {
                    state = ((ch == '\'') ? QuoteState.OUTSIDE : QuoteState.INSIDE_FULL);
                    break;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
        }
        return result.toString();
    }
    
    private static String localeAndStyle(final ULocale locale, final String style) {
        return "locale '" + locale + "' style '" + style + "'";
    }
    
    private static void fillInMissing(final Data result) {
        long lastDivisor = 1L;
        for (int i = 0; i < result.divisors.length; ++i) {
            if (result.units.get("other")[i] == null) {
                result.divisors[i] = lastDivisor;
                copyFromPreviousIndex(i, result.units);
            }
            else {
                lastDivisor = result.divisors[i];
                propagateOtherToMissing(i, result.units);
            }
        }
    }
    
    private static void propagateOtherToMissing(final int idx, final Map<String, DecimalFormat.Unit[]> units) {
        final DecimalFormat.Unit otherVariantValue = units.get("other")[idx];
        for (final DecimalFormat.Unit[] byBase : units.values()) {
            if (byBase[idx] == null) {
                byBase[idx] = otherVariantValue;
            }
        }
    }
    
    private static void copyFromPreviousIndex(final int idx, final Map<String, DecimalFormat.Unit[]> units) {
        for (final DecimalFormat.Unit[] byBase : units.values()) {
            if (idx == 0) {
                byBase[idx] = DecimalFormat.NULL_UNIT;
            }
            else {
                byBase[idx] = byBase[idx - 1];
            }
        }
    }
    
    private static void saveUnit(final DecimalFormat.Unit unit, final String pluralVariant, final int idx, final Map<String, DecimalFormat.Unit[]> units) {
        DecimalFormat.Unit[] byBase = units.get(pluralVariant);
        if (byBase == null) {
            byBase = new DecimalFormat.Unit[15];
            units.put(pluralVariant, byBase);
        }
        byBase[idx] = unit;
    }
    
    static DecimalFormat.Unit getUnit(final Map<String, DecimalFormat.Unit[]> units, final String variant, final int base) {
        DecimalFormat.Unit[] byBase = units.get(variant);
        if (byBase == null) {
            byBase = units.get("other");
        }
        return byBase[base];
    }
    
    static class Data
    {
        long[] divisors;
        Map<String, DecimalFormat.Unit[]> units;
        
        Data(final long[] divisors, final Map<String, DecimalFormat.Unit[]> units) {
            this.divisors = divisors;
            this.units = units;
        }
    }
    
    static class DataBundle
    {
        Data shortData;
        Data longData;
        
        DataBundle(final Data shortData, final Data longData) {
            this.shortData = shortData;
            this.longData = longData;
        }
    }
    
    private enum QuoteState
    {
        OUTSIDE, 
        INSIDE_EMPTY, 
        INSIDE_FULL;
    }
    
    private enum UResFlags
    {
        ANY, 
        NOT_ROOT;
    }
}
