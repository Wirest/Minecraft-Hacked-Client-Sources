// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.ICUDebug;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;
import java.text.ParsePosition;
import com.ibm.icu.text.CurrencyDisplayNames;
import java.util.MissingResourceException;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Iterator;
import java.util.HashSet;
import com.ibm.icu.text.CurrencyMetaInfo;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.lang.ref.SoftReference;
import com.ibm.icu.impl.TextTrieMap;
import java.util.List;
import com.ibm.icu.impl.ICUCache;
import java.io.Serializable;

public class Currency extends MeasureUnit implements Serializable
{
    private static final long serialVersionUID = -5839973855554750484L;
    private static final boolean DEBUG;
    private static ICUCache<ULocale, List<TextTrieMap<CurrencyStringInfo>>> CURRENCY_NAME_CACHE;
    private String isoCode;
    public static final int SYMBOL_NAME = 0;
    public static final int LONG_NAME = 1;
    public static final int PLURAL_LONG_NAME = 2;
    private static ServiceShim shim;
    private static final String EUR_STR = "EUR";
    private static final ICUCache<ULocale, String> currencyCodeCache;
    private static final ULocale UND;
    private static final String[] EMPTY_STRING_ARRAY;
    private static final int[] POW10;
    private static SoftReference<List<String>> ALL_TENDER_CODES;
    private static SoftReference<Set<String>> ALL_CODES_AS_SET;
    
    private static ServiceShim getShim() {
        if (Currency.shim == null) {
            try {
                final Class<?> cls = Class.forName("com.ibm.icu.util.CurrencyServiceShim");
                Currency.shim = (ServiceShim)cls.newInstance();
            }
            catch (Exception e) {
                if (Currency.DEBUG) {
                    e.printStackTrace();
                }
                throw new RuntimeException(e.getMessage());
            }
        }
        return Currency.shim;
    }
    
    public static Currency getInstance(final Locale locale) {
        return getInstance(ULocale.forLocale(locale));
    }
    
    public static Currency getInstance(final ULocale locale) {
        final String currency = locale.getKeywordValue("currency");
        if (currency != null) {
            return getInstance(currency);
        }
        if (Currency.shim == null) {
            return createCurrency(locale);
        }
        return Currency.shim.createInstance(locale);
    }
    
    public static String[] getAvailableCurrencyCodes(final ULocale loc, final Date d) {
        final CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.onDate(d).withRegion(loc.getCountry());
        final List<String> list = getTenderCurrencies(filter);
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static Set<Currency> getAvailableCurrencies() {
        final CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        final List<String> list = info.currencies(CurrencyMetaInfo.CurrencyFilter.all());
        final HashSet<Currency> resultSet = new HashSet<Currency>(list.size());
        for (final String code : list) {
            resultSet.add(new Currency(code));
        }
        return resultSet;
    }
    
    static Currency createCurrency(final ULocale loc) {
        final String variant = loc.getVariant();
        if ("EURO".equals(variant)) {
            return new Currency("EUR");
        }
        String code = Currency.currencyCodeCache.get(loc);
        if (code == null) {
            final String country = loc.getCountry();
            final CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
            final List<String> list = info.currencies(CurrencyMetaInfo.CurrencyFilter.onRegion(country));
            if (list.size() <= 0) {
                return null;
            }
            code = list.get(0);
            final boolean isPreEuro = "PREEURO".equals(variant);
            if (isPreEuro && "EUR".equals(code)) {
                if (list.size() < 2) {
                    return null;
                }
                code = list.get(1);
            }
            Currency.currencyCodeCache.put(loc, code);
        }
        return new Currency(code);
    }
    
    public static Currency getInstance(final String theISOCode) {
        if (theISOCode == null) {
            throw new NullPointerException("The input currency code is null.");
        }
        if (!isAlpha3Code(theISOCode)) {
            throw new IllegalArgumentException("The input currency code is not 3-letter alphabetic code.");
        }
        return new Currency(theISOCode.toUpperCase(Locale.ENGLISH));
    }
    
    private static boolean isAlpha3Code(final String code) {
        if (code.length() != 3) {
            return false;
        }
        for (int i = 0; i < 3; ++i) {
            final char ch = code.charAt(i);
            if (ch < 'A' || (ch > 'Z' && ch < 'a') || ch > 'z') {
                return false;
            }
        }
        return true;
    }
    
    public static Object registerInstance(final Currency currency, final ULocale locale) {
        return getShim().registerInstance(currency, locale);
    }
    
    public static boolean unregister(final Object registryKey) {
        if (registryKey == null) {
            throw new IllegalArgumentException("registryKey must not be null");
        }
        return Currency.shim != null && Currency.shim.unregister(registryKey);
    }
    
    public static Locale[] getAvailableLocales() {
        if (Currency.shim == null) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return Currency.shim.getAvailableLocales();
    }
    
    public static ULocale[] getAvailableULocales() {
        if (Currency.shim == null) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return Currency.shim.getAvailableULocales();
    }
    
    public static final String[] getKeywordValuesForLocale(final String key, final ULocale locale, final boolean commonlyUsed) {
        if (!"currency".equals(key)) {
            return Currency.EMPTY_STRING_ARRAY;
        }
        if (!commonlyUsed) {
            return getAllTenderCurrencies().toArray(new String[0]);
        }
        String prefRegion = locale.getCountry();
        if (prefRegion.length() == 0) {
            if (Currency.UND.equals(locale)) {
                return Currency.EMPTY_STRING_ARRAY;
            }
            final ULocale loc = ULocale.addLikelySubtags(locale);
            prefRegion = loc.getCountry();
        }
        final CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.now().withRegion(prefRegion);
        final List<String> result = getTenderCurrencies(filter);
        if (result.size() == 0) {
            return Currency.EMPTY_STRING_ARRAY;
        }
        return result.toArray(new String[result.size()]);
    }
    
    @Override
    public int hashCode() {
        return this.isoCode.hashCode();
    }
    
    @Override
    public boolean equals(final Object rhs) {
        if (rhs == null) {
            return false;
        }
        if (rhs == this) {
            return true;
        }
        try {
            final Currency c = (Currency)rhs;
            return this.isoCode.equals(c.isoCode);
        }
        catch (ClassCastException e) {
            return false;
        }
    }
    
    public String getCurrencyCode() {
        return this.isoCode;
    }
    
    public int getNumericCode() {
        int code = 0;
        try {
            final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "currencyNumericCodes", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            final UResourceBundle codeMap = bundle.get("codeMap");
            final UResourceBundle numCode = codeMap.get(this.isoCode);
            code = numCode.getInt();
        }
        catch (MissingResourceException ex) {}
        return code;
    }
    
    public String getSymbol() {
        return this.getSymbol(ULocale.getDefault(ULocale.Category.DISPLAY));
    }
    
    public String getSymbol(final Locale loc) {
        return this.getSymbol(ULocale.forLocale(loc));
    }
    
    public String getSymbol(final ULocale uloc) {
        return this.getName(uloc, 0, new boolean[1]);
    }
    
    public String getName(final Locale locale, final int nameStyle, final boolean[] isChoiceFormat) {
        return this.getName(ULocale.forLocale(locale), nameStyle, isChoiceFormat);
    }
    
    public String getName(final ULocale locale, final int nameStyle, final boolean[] isChoiceFormat) {
        if (nameStyle != 0 && nameStyle != 1) {
            throw new IllegalArgumentException("bad name style: " + nameStyle);
        }
        if (isChoiceFormat != null) {
            isChoiceFormat[0] = false;
        }
        final CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
        return (nameStyle == 0) ? names.getSymbol(this.isoCode) : names.getName(this.isoCode);
    }
    
    public String getName(final Locale locale, final int nameStyle, final String pluralCount, final boolean[] isChoiceFormat) {
        return this.getName(ULocale.forLocale(locale), nameStyle, pluralCount, isChoiceFormat);
    }
    
    public String getName(final ULocale locale, final int nameStyle, final String pluralCount, final boolean[] isChoiceFormat) {
        if (nameStyle != 2) {
            return this.getName(locale, nameStyle, isChoiceFormat);
        }
        if (isChoiceFormat != null) {
            isChoiceFormat[0] = false;
        }
        final CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
        return names.getPluralName(this.isoCode, pluralCount);
    }
    
    public String getDisplayName() {
        return this.getName(Locale.getDefault(), 1, null);
    }
    
    public String getDisplayName(final Locale locale) {
        return this.getName(locale, 1, null);
    }
    
    @Deprecated
    public static String parse(final ULocale locale, final String text, final int type, final ParsePosition pos) {
        List<TextTrieMap<CurrencyStringInfo>> currencyTrieVec = Currency.CURRENCY_NAME_CACHE.get(locale);
        if (currencyTrieVec == null) {
            final TextTrieMap<CurrencyStringInfo> currencyNameTrie = new TextTrieMap<CurrencyStringInfo>(true);
            final TextTrieMap<CurrencyStringInfo> currencySymbolTrie = new TextTrieMap<CurrencyStringInfo>(false);
            currencyTrieVec = new ArrayList<TextTrieMap<CurrencyStringInfo>>();
            currencyTrieVec.add(currencySymbolTrie);
            currencyTrieVec.add(currencyNameTrie);
            setupCurrencyTrieVec(locale, currencyTrieVec);
            Currency.CURRENCY_NAME_CACHE.put(locale, currencyTrieVec);
        }
        int maxLength = 0;
        String isoResult = null;
        final TextTrieMap<CurrencyStringInfo> currencyNameTrie2 = currencyTrieVec.get(1);
        CurrencyNameResultHandler handler = new CurrencyNameResultHandler();
        currencyNameTrie2.find(text, pos.getIndex(), handler);
        List<CurrencyStringInfo> list = handler.getMatchedCurrencyNames();
        if (list != null && list.size() != 0) {
            for (final CurrencyStringInfo info : list) {
                final String isoCode = info.getISOCode();
                final String currencyString = info.getCurrencyString();
                if (currencyString.length() > maxLength) {
                    maxLength = currencyString.length();
                    isoResult = isoCode;
                }
            }
        }
        if (type != 1) {
            final TextTrieMap<CurrencyStringInfo> currencySymbolTrie2 = currencyTrieVec.get(0);
            handler = new CurrencyNameResultHandler();
            currencySymbolTrie2.find(text, pos.getIndex(), handler);
            list = handler.getMatchedCurrencyNames();
            if (list != null && list.size() != 0) {
                for (final CurrencyStringInfo info2 : list) {
                    final String isoCode2 = info2.getISOCode();
                    final String currencyString2 = info2.getCurrencyString();
                    if (currencyString2.length() > maxLength) {
                        maxLength = currencyString2.length();
                        isoResult = isoCode2;
                    }
                }
            }
        }
        final int start = pos.getIndex();
        pos.setIndex(start + maxLength);
        return isoResult;
    }
    
    private static void setupCurrencyTrieVec(final ULocale locale, final List<TextTrieMap<CurrencyStringInfo>> trieVec) {
        final TextTrieMap<CurrencyStringInfo> symTrie = trieVec.get(0);
        final TextTrieMap<CurrencyStringInfo> trie = trieVec.get(1);
        final CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
        for (final Map.Entry<String, String> e : names.symbolMap().entrySet()) {
            final String symbol = e.getKey();
            final String isoCode = e.getValue();
            symTrie.put(symbol, new CurrencyStringInfo(isoCode, symbol));
        }
        for (final Map.Entry<String, String> e : names.nameMap().entrySet()) {
            final String name = e.getKey();
            final String isoCode = e.getValue();
            trie.put(name, new CurrencyStringInfo(isoCode, name));
        }
    }
    
    public int getDefaultFractionDigits() {
        final CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        final CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.isoCode);
        return digits.fractionDigits;
    }
    
    public double getRoundingIncrement() {
        final CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        final CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.isoCode);
        final int data1 = digits.roundingIncrement;
        if (data1 == 0) {
            return 0.0;
        }
        final int data2 = digits.fractionDigits;
        if (data2 < 0 || data2 >= Currency.POW10.length) {
            return 0.0;
        }
        return data1 / (double)Currency.POW10[data2];
    }
    
    @Override
    public String toString() {
        return this.isoCode;
    }
    
    protected Currency(final String theISOCode) {
        this.isoCode = theISOCode;
    }
    
    private static synchronized List<String> getAllTenderCurrencies() {
        List<String> all = (Currency.ALL_TENDER_CODES == null) ? null : Currency.ALL_TENDER_CODES.get();
        if (all == null) {
            final CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.all();
            all = Collections.unmodifiableList((List<? extends String>)getTenderCurrencies(filter));
            Currency.ALL_TENDER_CODES = new SoftReference<List<String>>(all);
        }
        return all;
    }
    
    private static synchronized Set<String> getAllCurrenciesAsSet() {
        Set<String> all = (Currency.ALL_CODES_AS_SET == null) ? null : Currency.ALL_CODES_AS_SET.get();
        if (all == null) {
            final CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
            all = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(info.currencies(CurrencyMetaInfo.CurrencyFilter.all())));
            Currency.ALL_CODES_AS_SET = new SoftReference<Set<String>>(all);
        }
        return all;
    }
    
    public static boolean isAvailable(String code, final Date from, final Date to) {
        if (!isAlpha3Code(code)) {
            return false;
        }
        if (from != null && to != null && from.after(to)) {
            throw new IllegalArgumentException("To is before from");
        }
        code = code.toUpperCase(Locale.ENGLISH);
        final boolean isKnown = getAllCurrenciesAsSet().contains(code);
        if (!isKnown) {
            return false;
        }
        if (from == null && to == null) {
            return true;
        }
        final CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        final List<String> allActive = info.currencies(CurrencyMetaInfo.CurrencyFilter.onDateRange(from, to).withCurrency(code));
        return allActive.contains(code);
    }
    
    private static List<String> getTenderCurrencies(final CurrencyMetaInfo.CurrencyFilter filter) {
        final CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
        return info.currencies(filter.withTender());
    }
    
    static {
        DEBUG = ICUDebug.enabled("currency");
        Currency.CURRENCY_NAME_CACHE = new SimpleCache<ULocale, List<TextTrieMap<CurrencyStringInfo>>>();
        currencyCodeCache = new SimpleCache<ULocale, String>();
        UND = new ULocale("und");
        EMPTY_STRING_ARRAY = new String[0];
        POW10 = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
    }
    
    abstract static class ServiceShim
    {
        abstract ULocale[] getAvailableULocales();
        
        abstract Locale[] getAvailableLocales();
        
        abstract Currency createInstance(final ULocale p0);
        
        abstract Object registerInstance(final Currency p0, final ULocale p1);
        
        abstract boolean unregister(final Object p0);
    }
    
    private static final class CurrencyStringInfo
    {
        private String isoCode;
        private String currencyString;
        
        public CurrencyStringInfo(final String isoCode, final String currencyString) {
            this.isoCode = isoCode;
            this.currencyString = currencyString;
        }
        
        private String getISOCode() {
            return this.isoCode;
        }
        
        private String getCurrencyString() {
            return this.currencyString;
        }
    }
    
    private static class CurrencyNameResultHandler implements TextTrieMap.ResultHandler<CurrencyStringInfo>
    {
        private ArrayList<CurrencyStringInfo> resultList;
        
        public boolean handlePrefixMatch(final int matchLength, final Iterator<CurrencyStringInfo> values) {
            if (this.resultList == null) {
                this.resultList = new ArrayList<CurrencyStringInfo>();
            }
            while (values.hasNext()) {
                final CurrencyStringInfo item = values.next();
                if (item == null) {
                    break;
                }
                int i = 0;
                while (i < this.resultList.size()) {
                    final CurrencyStringInfo tmp = this.resultList.get(i);
                    if (item.getISOCode().equals(tmp.getISOCode())) {
                        if (matchLength > tmp.getCurrencyString().length()) {
                            this.resultList.set(i, item);
                            break;
                        }
                        break;
                    }
                    else {
                        ++i;
                    }
                }
                if (i != this.resultList.size()) {
                    continue;
                }
                this.resultList.add(item);
            }
            return true;
        }
        
        List<CurrencyStringInfo> getMatchedCurrencyNames() {
            if (this.resultList == null || this.resultList.size() == 0) {
                return null;
            }
            return this.resultList;
        }
    }
}
