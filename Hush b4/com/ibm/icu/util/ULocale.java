// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.InvocationTargetException;
import java.util.TreeSet;
import java.lang.reflect.Method;
import com.ibm.icu.impl.locale.Extension;
import java.util.Map;
import com.ibm.icu.impl.locale.UnicodeLocaleExtension;
import com.ibm.icu.impl.locale.ParseStatus;
import java.util.List;
import com.ibm.icu.impl.locale.LanguageTag;
import com.ibm.icu.impl.locale.LocaleSyntaxException;
import com.ibm.icu.impl.locale.InternalLocaleBuilder;
import java.util.Set;
import java.util.MissingResourceException;
import java.util.TreeMap;
import com.ibm.icu.impl.LocaleUtility;
import java.text.ParseException;
import com.ibm.icu.impl.ICUResourceTableAccess;
import com.ibm.icu.impl.locale.AsciiUtil;
import com.ibm.icu.text.LocaleDisplayNames;
import java.util.Iterator;
import com.ibm.icu.impl.LocaleIDParser;
import com.ibm.icu.impl.LocaleIDs;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.locale.LocaleExtensions;
import com.ibm.icu.impl.locale.BaseLocale;
import com.ibm.icu.impl.SimpleCache;
import java.util.Locale;
import java.io.Serializable;

public final class ULocale implements Serializable
{
    private static final long serialVersionUID = 3715177670352309217L;
    public static final ULocale ENGLISH;
    public static final ULocale FRENCH;
    public static final ULocale GERMAN;
    public static final ULocale ITALIAN;
    public static final ULocale JAPANESE;
    public static final ULocale KOREAN;
    public static final ULocale CHINESE;
    public static final ULocale SIMPLIFIED_CHINESE;
    public static final ULocale TRADITIONAL_CHINESE;
    public static final ULocale FRANCE;
    public static final ULocale GERMANY;
    public static final ULocale ITALY;
    public static final ULocale JAPAN;
    public static final ULocale KOREA;
    public static final ULocale CHINA;
    public static final ULocale PRC;
    public static final ULocale TAIWAN;
    public static final ULocale UK;
    public static final ULocale US;
    public static final ULocale CANADA;
    public static final ULocale CANADA_FRENCH;
    private static final String EMPTY_STRING = "";
    private static final char UNDERSCORE = '_';
    private static final Locale EMPTY_LOCALE;
    private static final String LOCALE_ATTRIBUTE_KEY = "attribute";
    public static final ULocale ROOT;
    private static final SimpleCache<Locale, ULocale> CACHE;
    private transient volatile Locale locale;
    private String localeID;
    private transient volatile BaseLocale baseLocale;
    private transient volatile LocaleExtensions extensions;
    private static String[][] CANONICALIZE_MAP;
    private static String[][] variantsToKeywords;
    private static ICUCache<String, String> nameCache;
    private static Locale defaultLocale;
    private static ULocale defaultULocale;
    private static Locale[] defaultCategoryLocales;
    private static ULocale[] defaultCategoryULocales;
    public static Type ACTUAL_LOCALE;
    public static Type VALID_LOCALE;
    private static final String UNDEFINED_LANGUAGE = "und";
    private static final String UNDEFINED_SCRIPT = "Zzzz";
    private static final String UNDEFINED_REGION = "ZZ";
    public static final char PRIVATE_USE_EXTENSION = 'x';
    public static final char UNICODE_LOCALE_EXTENSION = 'u';
    
    private static void initCANONICALIZE_MAP() {
        if (ULocale.CANONICALIZE_MAP == null) {
            final String[][] tempCANONICALIZE_MAP = { { "C", "en_US_POSIX", null, null }, { "art_LOJBAN", "jbo", null, null }, { "az_AZ_CYRL", "az_Cyrl_AZ", null, null }, { "az_AZ_LATN", "az_Latn_AZ", null, null }, { "ca_ES_PREEURO", "ca_ES", "currency", "ESP" }, { "cel_GAULISH", "cel__GAULISH", null, null }, { "de_1901", "de__1901", null, null }, { "de_1906", "de__1906", null, null }, { "de__PHONEBOOK", "de", "collation", "phonebook" }, { "de_AT_PREEURO", "de_AT", "currency", "ATS" }, { "de_DE_PREEURO", "de_DE", "currency", "DEM" }, { "de_LU_PREEURO", "de_LU", "currency", "EUR" }, { "el_GR_PREEURO", "el_GR", "currency", "GRD" }, { "en_BOONT", "en__BOONT", null, null }, { "en_SCOUSE", "en__SCOUSE", null, null }, { "en_BE_PREEURO", "en_BE", "currency", "BEF" }, { "en_IE_PREEURO", "en_IE", "currency", "IEP" }, { "es__TRADITIONAL", "es", "collation", "traditional" }, { "es_ES_PREEURO", "es_ES", "currency", "ESP" }, { "eu_ES_PREEURO", "eu_ES", "currency", "ESP" }, { "fi_FI_PREEURO", "fi_FI", "currency", "FIM" }, { "fr_BE_PREEURO", "fr_BE", "currency", "BEF" }, { "fr_FR_PREEURO", "fr_FR", "currency", "FRF" }, { "fr_LU_PREEURO", "fr_LU", "currency", "LUF" }, { "ga_IE_PREEURO", "ga_IE", "currency", "IEP" }, { "gl_ES_PREEURO", "gl_ES", "currency", "ESP" }, { "hi__DIRECT", "hi", "collation", "direct" }, { "it_IT_PREEURO", "it_IT", "currency", "ITL" }, { "ja_JP_TRADITIONAL", "ja_JP", "calendar", "japanese" }, { "nl_BE_PREEURO", "nl_BE", "currency", "BEF" }, { "nl_NL_PREEURO", "nl_NL", "currency", "NLG" }, { "pt_PT_PREEURO", "pt_PT", "currency", "PTE" }, { "sl_ROZAJ", "sl__ROZAJ", null, null }, { "sr_SP_CYRL", "sr_Cyrl_RS", null, null }, { "sr_SP_LATN", "sr_Latn_RS", null, null }, { "sr_YU_CYRILLIC", "sr_Cyrl_RS", null, null }, { "th_TH_TRADITIONAL", "th_TH", "calendar", "buddhist" }, { "uz_UZ_CYRILLIC", "uz_Cyrl_UZ", null, null }, { "uz_UZ_CYRL", "uz_Cyrl_UZ", null, null }, { "uz_UZ_LATN", "uz_Latn_UZ", null, null }, { "zh_CHS", "zh_Hans", null, null }, { "zh_CHT", "zh_Hant", null, null }, { "zh_GAN", "zh__GAN", null, null }, { "zh_GUOYU", "zh", null, null }, { "zh_HAKKA", "zh__HAKKA", null, null }, { "zh_MIN", "zh__MIN", null, null }, { "zh_MIN_NAN", "zh__MINNAN", null, null }, { "zh_WUU", "zh__WUU", null, null }, { "zh_XIANG", "zh__XIANG", null, null }, { "zh_YUE", "zh__YUE", null, null } };
            synchronized (ULocale.class) {
                if (ULocale.CANONICALIZE_MAP == null) {
                    ULocale.CANONICALIZE_MAP = tempCANONICALIZE_MAP;
                }
            }
        }
        if (ULocale.variantsToKeywords == null) {
            final String[][] tempVariantsToKeywords = { { "EURO", "currency", "EUR" }, { "PINYIN", "collation", "pinyin" }, { "STROKE", "collation", "stroke" } };
            synchronized (ULocale.class) {
                if (ULocale.variantsToKeywords == null) {
                    ULocale.variantsToKeywords = tempVariantsToKeywords;
                }
            }
        }
    }
    
    private ULocale(final String localeID, final Locale locale) {
        this.localeID = localeID;
        this.locale = locale;
    }
    
    private ULocale(final Locale loc) {
        this.localeID = getName(forLocale(loc).toString());
        this.locale = loc;
    }
    
    public static ULocale forLocale(final Locale loc) {
        if (loc == null) {
            return null;
        }
        ULocale result = ULocale.CACHE.get(loc);
        if (result == null) {
            result = JDKLocaleHelper.toULocale(loc);
            ULocale.CACHE.put(loc, result);
        }
        return result;
    }
    
    public ULocale(final String localeID) {
        this.localeID = getName(localeID);
    }
    
    public ULocale(final String a, final String b) {
        this(a, b, null);
    }
    
    public ULocale(final String a, final String b, final String c) {
        this.localeID = getName(lscvToID(a, b, c, ""));
    }
    
    public static ULocale createCanonical(final String nonCanonicalID) {
        return new ULocale(canonicalize(nonCanonicalID), (Locale)null);
    }
    
    private static String lscvToID(final String lang, final String script, final String country, final String variant) {
        final StringBuilder buf = new StringBuilder();
        if (lang != null && lang.length() > 0) {
            buf.append(lang);
        }
        if (script != null && script.length() > 0) {
            buf.append('_');
            buf.append(script);
        }
        if (country != null && country.length() > 0) {
            buf.append('_');
            buf.append(country);
        }
        if (variant != null && variant.length() > 0) {
            if (country == null || country.length() == 0) {
                buf.append('_');
            }
            buf.append('_');
            buf.append(variant);
        }
        return buf.toString();
    }
    
    public Locale toLocale() {
        if (this.locale == null) {
            this.locale = JDKLocaleHelper.toLocale(this);
        }
        return this.locale;
    }
    
    public static ULocale getDefault() {
        synchronized (ULocale.class) {
            if (ULocale.defaultULocale == null) {
                return ULocale.ROOT;
            }
            final Locale currentDefault = Locale.getDefault();
            if (!ULocale.defaultLocale.equals(currentDefault)) {
                ULocale.defaultLocale = currentDefault;
                ULocale.defaultULocale = forLocale(currentDefault);
                if (!JDKLocaleHelper.isJava7orNewer()) {
                    for (final Category cat : Category.values()) {
                        final int idx = cat.ordinal();
                        ULocale.defaultCategoryLocales[idx] = currentDefault;
                        ULocale.defaultCategoryULocales[idx] = forLocale(currentDefault);
                    }
                }
            }
            return ULocale.defaultULocale;
        }
    }
    
    public static synchronized void setDefault(final ULocale newLocale) {
        Locale.setDefault(ULocale.defaultLocale = newLocale.toLocale());
        ULocale.defaultULocale = newLocale;
        for (final Category cat : Category.values()) {
            setDefault(cat, newLocale);
        }
    }
    
    public static ULocale getDefault(final Category category) {
        synchronized (ULocale.class) {
            final int idx = category.ordinal();
            if (ULocale.defaultCategoryULocales[idx] == null) {
                return ULocale.ROOT;
            }
            if (JDKLocaleHelper.isJava7orNewer()) {
                final Locale currentCategoryDefault = JDKLocaleHelper.getDefault(category);
                if (!ULocale.defaultCategoryLocales[idx].equals(currentCategoryDefault)) {
                    ULocale.defaultCategoryLocales[idx] = currentCategoryDefault;
                    ULocale.defaultCategoryULocales[idx] = forLocale(currentCategoryDefault);
                }
            }
            else {
                final Locale currentDefault = Locale.getDefault();
                if (!ULocale.defaultLocale.equals(currentDefault)) {
                    ULocale.defaultLocale = currentDefault;
                    ULocale.defaultULocale = forLocale(currentDefault);
                    for (final Category cat : Category.values()) {
                        final int tmpIdx = cat.ordinal();
                        ULocale.defaultCategoryLocales[tmpIdx] = currentDefault;
                        ULocale.defaultCategoryULocales[tmpIdx] = forLocale(currentDefault);
                    }
                }
            }
            return ULocale.defaultCategoryULocales[idx];
        }
    }
    
    public static synchronized void setDefault(final Category category, final ULocale newLocale) {
        final Locale newJavaDefault = newLocale.toLocale();
        final int idx = category.ordinal();
        ULocale.defaultCategoryULocales[idx] = newLocale;
        JDKLocaleHelper.setDefault(category, ULocale.defaultCategoryLocales[idx] = newJavaDefault);
    }
    
    public Object clone() {
        return this;
    }
    
    @Override
    public int hashCode() {
        return this.localeID.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof ULocale && this.localeID.equals(((ULocale)obj).localeID));
    }
    
    public static ULocale[] getAvailableLocales() {
        return ICUResourceBundle.getAvailableULocales();
    }
    
    public static String[] getISOCountries() {
        return LocaleIDs.getISOCountries();
    }
    
    public static String[] getISOLanguages() {
        return LocaleIDs.getISOLanguages();
    }
    
    public String getLanguage() {
        return getLanguage(this.localeID);
    }
    
    public static String getLanguage(final String localeID) {
        return new LocaleIDParser(localeID).getLanguage();
    }
    
    public String getScript() {
        return getScript(this.localeID);
    }
    
    public static String getScript(final String localeID) {
        return new LocaleIDParser(localeID).getScript();
    }
    
    public String getCountry() {
        return getCountry(this.localeID);
    }
    
    public static String getCountry(final String localeID) {
        return new LocaleIDParser(localeID).getCountry();
    }
    
    public String getVariant() {
        return getVariant(this.localeID);
    }
    
    public static String getVariant(final String localeID) {
        return new LocaleIDParser(localeID).getVariant();
    }
    
    public static String getFallback(final String localeID) {
        return getFallbackString(getName(localeID));
    }
    
    public ULocale getFallback() {
        if (this.localeID.length() == 0 || this.localeID.charAt(0) == '@') {
            return null;
        }
        return new ULocale(getFallbackString(this.localeID), (Locale)null);
    }
    
    private static String getFallbackString(final String fallback) {
        int extStart = fallback.indexOf(64);
        if (extStart == -1) {
            extStart = fallback.length();
        }
        int last = fallback.lastIndexOf(95, extStart);
        if (last == -1) {
            last = 0;
        }
        else {
            while (last > 0) {
                if (fallback.charAt(last - 1) != '_') {
                    break;
                }
                --last;
            }
        }
        return fallback.substring(0, last) + fallback.substring(extStart);
    }
    
    public String getBaseName() {
        return getBaseName(this.localeID);
    }
    
    public static String getBaseName(final String localeID) {
        if (localeID.indexOf(64) == -1) {
            return localeID;
        }
        return new LocaleIDParser(localeID).getBaseName();
    }
    
    public String getName() {
        return this.localeID;
    }
    
    private static int getShortestSubtagLength(final String localeID) {
        int length;
        final int localeIDLength = length = localeID.length();
        boolean reset = true;
        int tmpLength = 0;
        for (int i = 0; i < localeIDLength; ++i) {
            if (localeID.charAt(i) != '_' && localeID.charAt(i) != '-') {
                if (reset) {
                    reset = false;
                    tmpLength = 0;
                }
                ++tmpLength;
            }
            else {
                if (tmpLength != 0 && tmpLength < length) {
                    length = tmpLength;
                }
                reset = true;
            }
        }
        return length;
    }
    
    public static String getName(final String localeID) {
        String tmpLocaleID;
        if (localeID != null && !localeID.contains("@") && getShortestSubtagLength(localeID) == 1) {
            tmpLocaleID = forLanguageTag(localeID).getName();
            if (tmpLocaleID.length() == 0) {
                tmpLocaleID = localeID;
            }
        }
        else {
            tmpLocaleID = localeID;
        }
        String name = ULocale.nameCache.get(tmpLocaleID);
        if (name == null) {
            name = new LocaleIDParser(tmpLocaleID).getName();
            ULocale.nameCache.put(tmpLocaleID, name);
        }
        return name;
    }
    
    @Override
    public String toString() {
        return this.localeID;
    }
    
    public Iterator<String> getKeywords() {
        return getKeywords(this.localeID);
    }
    
    public static Iterator<String> getKeywords(final String localeID) {
        return new LocaleIDParser(localeID).getKeywords();
    }
    
    public String getKeywordValue(final String keywordName) {
        return getKeywordValue(this.localeID, keywordName);
    }
    
    public static String getKeywordValue(final String localeID, final String keywordName) {
        return new LocaleIDParser(localeID).getKeywordValue(keywordName);
    }
    
    public static String canonicalize(final String localeID) {
        final LocaleIDParser parser = new LocaleIDParser(localeID, true);
        String baseName = parser.getBaseName();
        boolean foundVariant = false;
        if (localeID.equals("")) {
            return "";
        }
        initCANONICALIZE_MAP();
        for (int i = 0; i < ULocale.variantsToKeywords.length; ++i) {
            final String[] vals = ULocale.variantsToKeywords[i];
            int idx = baseName.lastIndexOf("_" + vals[0]);
            if (idx > -1) {
                foundVariant = true;
                baseName = baseName.substring(0, idx);
                if (baseName.endsWith("_")) {
                    baseName = baseName.substring(0, --idx);
                }
                parser.setBaseName(baseName);
                parser.defaultKeywordValue(vals[1], vals[2]);
                break;
            }
        }
        int i = 0;
        while (i < ULocale.CANONICALIZE_MAP.length) {
            if (ULocale.CANONICALIZE_MAP[i][0].equals(baseName)) {
                foundVariant = true;
                final String[] vals = ULocale.CANONICALIZE_MAP[i];
                parser.setBaseName(vals[1]);
                if (vals[2] != null) {
                    parser.defaultKeywordValue(vals[2], vals[3]);
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
        if (!foundVariant && parser.getLanguage().equals("nb") && parser.getVariant().equals("NY")) {
            parser.setBaseName(lscvToID("nn", parser.getScript(), parser.getCountry(), null));
        }
        return parser.getName();
    }
    
    public ULocale setKeywordValue(final String keyword, final String value) {
        return new ULocale(setKeywordValue(this.localeID, keyword, value), (Locale)null);
    }
    
    public static String setKeywordValue(final String localeID, final String keyword, final String value) {
        final LocaleIDParser parser = new LocaleIDParser(localeID);
        parser.setKeywordValue(keyword, value);
        return parser.getName();
    }
    
    public String getISO3Language() {
        return getISO3Language(this.localeID);
    }
    
    public static String getISO3Language(final String localeID) {
        return LocaleIDs.getISO3Language(getLanguage(localeID));
    }
    
    public String getISO3Country() {
        return getISO3Country(this.localeID);
    }
    
    public static String getISO3Country(final String localeID) {
        return LocaleIDs.getISO3Country(getCountry(localeID));
    }
    
    public String getDisplayLanguage() {
        return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), false);
    }
    
    public String getDisplayLanguage(final ULocale displayLocale) {
        return getDisplayLanguageInternal(this, displayLocale, false);
    }
    
    public static String getDisplayLanguage(final String localeID, final String displayLocaleID) {
        return getDisplayLanguageInternal(new ULocale(localeID), new ULocale(displayLocaleID), false);
    }
    
    public static String getDisplayLanguage(final String localeID, final ULocale displayLocale) {
        return getDisplayLanguageInternal(new ULocale(localeID), displayLocale, false);
    }
    
    public String getDisplayLanguageWithDialect() {
        return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), true);
    }
    
    public String getDisplayLanguageWithDialect(final ULocale displayLocale) {
        return getDisplayLanguageInternal(this, displayLocale, true);
    }
    
    public static String getDisplayLanguageWithDialect(final String localeID, final String displayLocaleID) {
        return getDisplayLanguageInternal(new ULocale(localeID), new ULocale(displayLocaleID), true);
    }
    
    public static String getDisplayLanguageWithDialect(final String localeID, final ULocale displayLocale) {
        return getDisplayLanguageInternal(new ULocale(localeID), displayLocale, true);
    }
    
    private static String getDisplayLanguageInternal(final ULocale locale, final ULocale displayLocale, final boolean useDialect) {
        final String lang = useDialect ? locale.getBaseName() : locale.getLanguage();
        return LocaleDisplayNames.getInstance(displayLocale).languageDisplayName(lang);
    }
    
    public String getDisplayScript() {
        return getDisplayScriptInternal(this, getDefault(Category.DISPLAY));
    }
    
    @Deprecated
    public String getDisplayScriptInContext() {
        return getDisplayScriptInContextInternal(this, getDefault(Category.DISPLAY));
    }
    
    public String getDisplayScript(final ULocale displayLocale) {
        return getDisplayScriptInternal(this, displayLocale);
    }
    
    @Deprecated
    public String getDisplayScriptInContext(final ULocale displayLocale) {
        return getDisplayScriptInContextInternal(this, displayLocale);
    }
    
    public static String getDisplayScript(final String localeID, final String displayLocaleID) {
        return getDisplayScriptInternal(new ULocale(localeID), new ULocale(displayLocaleID));
    }
    
    @Deprecated
    public static String getDisplayScriptInContext(final String localeID, final String displayLocaleID) {
        return getDisplayScriptInContextInternal(new ULocale(localeID), new ULocale(displayLocaleID));
    }
    
    public static String getDisplayScript(final String localeID, final ULocale displayLocale) {
        return getDisplayScriptInternal(new ULocale(localeID), displayLocale);
    }
    
    @Deprecated
    public static String getDisplayScriptInContext(final String localeID, final ULocale displayLocale) {
        return getDisplayScriptInContextInternal(new ULocale(localeID), displayLocale);
    }
    
    private static String getDisplayScriptInternal(final ULocale locale, final ULocale displayLocale) {
        return LocaleDisplayNames.getInstance(displayLocale).scriptDisplayName(locale.getScript());
    }
    
    private static String getDisplayScriptInContextInternal(final ULocale locale, final ULocale displayLocale) {
        return LocaleDisplayNames.getInstance(displayLocale).scriptDisplayNameInContext(locale.getScript());
    }
    
    public String getDisplayCountry() {
        return getDisplayCountryInternal(this, getDefault(Category.DISPLAY));
    }
    
    public String getDisplayCountry(final ULocale displayLocale) {
        return getDisplayCountryInternal(this, displayLocale);
    }
    
    public static String getDisplayCountry(final String localeID, final String displayLocaleID) {
        return getDisplayCountryInternal(new ULocale(localeID), new ULocale(displayLocaleID));
    }
    
    public static String getDisplayCountry(final String localeID, final ULocale displayLocale) {
        return getDisplayCountryInternal(new ULocale(localeID), displayLocale);
    }
    
    private static String getDisplayCountryInternal(final ULocale locale, final ULocale displayLocale) {
        return LocaleDisplayNames.getInstance(displayLocale).regionDisplayName(locale.getCountry());
    }
    
    public String getDisplayVariant() {
        return getDisplayVariantInternal(this, getDefault(Category.DISPLAY));
    }
    
    public String getDisplayVariant(final ULocale displayLocale) {
        return getDisplayVariantInternal(this, displayLocale);
    }
    
    public static String getDisplayVariant(final String localeID, final String displayLocaleID) {
        return getDisplayVariantInternal(new ULocale(localeID), new ULocale(displayLocaleID));
    }
    
    public static String getDisplayVariant(final String localeID, final ULocale displayLocale) {
        return getDisplayVariantInternal(new ULocale(localeID), displayLocale);
    }
    
    private static String getDisplayVariantInternal(final ULocale locale, final ULocale displayLocale) {
        return LocaleDisplayNames.getInstance(displayLocale).variantDisplayName(locale.getVariant());
    }
    
    public static String getDisplayKeyword(final String keyword) {
        return getDisplayKeywordInternal(keyword, getDefault(Category.DISPLAY));
    }
    
    public static String getDisplayKeyword(final String keyword, final String displayLocaleID) {
        return getDisplayKeywordInternal(keyword, new ULocale(displayLocaleID));
    }
    
    public static String getDisplayKeyword(final String keyword, final ULocale displayLocale) {
        return getDisplayKeywordInternal(keyword, displayLocale);
    }
    
    private static String getDisplayKeywordInternal(final String keyword, final ULocale displayLocale) {
        return LocaleDisplayNames.getInstance(displayLocale).keyDisplayName(keyword);
    }
    
    public String getDisplayKeywordValue(final String keyword) {
        return getDisplayKeywordValueInternal(this, keyword, getDefault(Category.DISPLAY));
    }
    
    public String getDisplayKeywordValue(final String keyword, final ULocale displayLocale) {
        return getDisplayKeywordValueInternal(this, keyword, displayLocale);
    }
    
    public static String getDisplayKeywordValue(final String localeID, final String keyword, final String displayLocaleID) {
        return getDisplayKeywordValueInternal(new ULocale(localeID), keyword, new ULocale(displayLocaleID));
    }
    
    public static String getDisplayKeywordValue(final String localeID, final String keyword, final ULocale displayLocale) {
        return getDisplayKeywordValueInternal(new ULocale(localeID), keyword, displayLocale);
    }
    
    private static String getDisplayKeywordValueInternal(final ULocale locale, String keyword, final ULocale displayLocale) {
        keyword = AsciiUtil.toLowerString(keyword.trim());
        final String value = locale.getKeywordValue(keyword);
        return LocaleDisplayNames.getInstance(displayLocale).keyValueDisplayName(keyword, value);
    }
    
    public String getDisplayName() {
        return getDisplayNameInternal(this, getDefault(Category.DISPLAY));
    }
    
    public String getDisplayName(final ULocale displayLocale) {
        return getDisplayNameInternal(this, displayLocale);
    }
    
    public static String getDisplayName(final String localeID, final String displayLocaleID) {
        return getDisplayNameInternal(new ULocale(localeID), new ULocale(displayLocaleID));
    }
    
    public static String getDisplayName(final String localeID, final ULocale displayLocale) {
        return getDisplayNameInternal(new ULocale(localeID), displayLocale);
    }
    
    private static String getDisplayNameInternal(final ULocale locale, final ULocale displayLocale) {
        return LocaleDisplayNames.getInstance(displayLocale).localeDisplayName(locale);
    }
    
    public String getDisplayNameWithDialect() {
        return getDisplayNameWithDialectInternal(this, getDefault(Category.DISPLAY));
    }
    
    public String getDisplayNameWithDialect(final ULocale displayLocale) {
        return getDisplayNameWithDialectInternal(this, displayLocale);
    }
    
    public static String getDisplayNameWithDialect(final String localeID, final String displayLocaleID) {
        return getDisplayNameWithDialectInternal(new ULocale(localeID), new ULocale(displayLocaleID));
    }
    
    public static String getDisplayNameWithDialect(final String localeID, final ULocale displayLocale) {
        return getDisplayNameWithDialectInternal(new ULocale(localeID), displayLocale);
    }
    
    private static String getDisplayNameWithDialectInternal(final ULocale locale, final ULocale displayLocale) {
        return LocaleDisplayNames.getInstance(displayLocale, LocaleDisplayNames.DialectHandling.DIALECT_NAMES).localeDisplayName(locale);
    }
    
    public String getCharacterOrientation() {
        return ICUResourceTableAccess.getTableString("com/ibm/icu/impl/data/icudt51b", this, "layout", "characters");
    }
    
    public String getLineOrientation() {
        return ICUResourceTableAccess.getTableString("com/ibm/icu/impl/data/icudt51b", this, "layout", "lines");
    }
    
    public static ULocale acceptLanguage(final String acceptLanguageList, final ULocale[] availableLocales, final boolean[] fallback) {
        if (acceptLanguageList == null) {
            throw new NullPointerException();
        }
        ULocale[] acceptList = null;
        try {
            acceptList = parseAcceptLanguage(acceptLanguageList, true);
        }
        catch (ParseException pe) {
            acceptList = null;
        }
        if (acceptList == null) {
            return null;
        }
        return acceptLanguage(acceptList, availableLocales, fallback);
    }
    
    public static ULocale acceptLanguage(final ULocale[] acceptLanguageList, final ULocale[] availableLocales, final boolean[] fallback) {
        if (fallback != null) {
            fallback[0] = true;
        }
        for (int i = 0; i < acceptLanguageList.length; ++i) {
            ULocale aLocale = acceptLanguageList[i];
            boolean[] setFallback = fallback;
            do {
                for (int j = 0; j < availableLocales.length; ++j) {
                    if (availableLocales[j].equals(aLocale)) {
                        if (setFallback != null) {
                            setFallback[0] = false;
                        }
                        return availableLocales[j];
                    }
                    if (aLocale.getScript().length() == 0 && availableLocales[j].getScript().length() > 0 && availableLocales[j].getLanguage().equals(aLocale.getLanguage()) && availableLocales[j].getCountry().equals(aLocale.getCountry()) && availableLocales[j].getVariant().equals(aLocale.getVariant())) {
                        final ULocale minAvail = minimizeSubtags(availableLocales[j]);
                        if (minAvail.getScript().length() == 0) {
                            if (setFallback != null) {
                                setFallback[0] = false;
                            }
                            return aLocale;
                        }
                    }
                }
                final Locale loc = aLocale.toLocale();
                final Locale parent = LocaleUtility.fallback(loc);
                if (parent != null) {
                    aLocale = new ULocale(parent);
                }
                else {
                    aLocale = null;
                }
                setFallback = null;
            } while (aLocale != null);
        }
        return null;
    }
    
    public static ULocale acceptLanguage(final String acceptLanguageList, final boolean[] fallback) {
        return acceptLanguage(acceptLanguageList, getAvailableLocales(), fallback);
    }
    
    public static ULocale acceptLanguage(final ULocale[] acceptLanguageList, final boolean[] fallback) {
        return acceptLanguage(acceptLanguageList, getAvailableLocales(), fallback);
    }
    
    static ULocale[] parseAcceptLanguage(String acceptLanguage, final boolean isLenient) throws ParseException {
        class ULocaleAcceptLanguageQ implements Comparable<ULocaleAcceptLanguageQ>
        {
            private double q = q2;
            private double serial = serial;
            
            public ULocaleAcceptLanguageQ(final double theq, final int theserial) {
            }
            
            public int compareTo(final ULocaleAcceptLanguageQ other) {
                if (this.q > other.q) {
                    return -1;
                }
                if (this.q < other.q) {
                    return 1;
                }
                if (this.serial < other.serial) {
                    return -1;
                }
                if (this.serial > other.serial) {
                    return 1;
                }
                return 0;
            }
        }
        final TreeMap<ULocaleAcceptLanguageQ, ULocale> map = new TreeMap<ULocaleAcceptLanguageQ, ULocale>();
        final StringBuilder languageRangeBuf = new StringBuilder();
        final StringBuilder qvalBuf = new StringBuilder();
        int state = 0;
        acceptLanguage += ",";
        boolean subTag = false;
        boolean q1 = false;
        int n;
        for (n = 0; n < acceptLanguage.length(); ++n) {
            boolean gotLanguageQ = false;
            final char c = acceptLanguage.charAt(n);
            switch (state) {
                case 0: {
                    if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
                        languageRangeBuf.append(c);
                        state = 1;
                        subTag = false;
                        break;
                    }
                    if (c == '*') {
                        languageRangeBuf.append(c);
                        state = 2;
                        break;
                    }
                    if (c != ' ' && c != '\t') {
                        state = -1;
                        break;
                    }
                    break;
                }
                case 1: {
                    if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
                        languageRangeBuf.append(c);
                        break;
                    }
                    if (c == '-') {
                        subTag = true;
                        languageRangeBuf.append(c);
                        break;
                    }
                    if (c == '_') {
                        if (isLenient) {
                            subTag = true;
                            languageRangeBuf.append(c);
                            break;
                        }
                        state = -1;
                        break;
                    }
                    else if ('0' <= c && c <= '9') {
                        if (subTag) {
                            languageRangeBuf.append(c);
                            break;
                        }
                        state = -1;
                        break;
                    }
                    else {
                        if (c == ',') {
                            gotLanguageQ = true;
                            break;
                        }
                        if (c == ' ' || c == '\t') {
                            state = 3;
                            break;
                        }
                        if (c == ';') {
                            state = 4;
                            break;
                        }
                        state = -1;
                        break;
                    }
                    break;
                }
                case 2: {
                    if (c == ',') {
                        gotLanguageQ = true;
                        break;
                    }
                    if (c == ' ' || c == '\t') {
                        state = 3;
                        break;
                    }
                    if (c == ';') {
                        state = 4;
                        break;
                    }
                    state = -1;
                    break;
                }
                case 3: {
                    if (c == ',') {
                        gotLanguageQ = true;
                        break;
                    }
                    if (c == ';') {
                        state = 4;
                        break;
                    }
                    if (c != ' ' && c != '\t') {
                        state = -1;
                        break;
                    }
                    break;
                }
                case 4: {
                    if (c == 'q') {
                        state = 5;
                        break;
                    }
                    if (c != ' ' && c != '\t') {
                        state = -1;
                        break;
                    }
                    break;
                }
                case 5: {
                    if (c == '=') {
                        state = 6;
                        break;
                    }
                    if (c != ' ' && c != '\t') {
                        state = -1;
                        break;
                    }
                    break;
                }
                case 6: {
                    if (c == '0') {
                        q1 = false;
                        qvalBuf.append(c);
                        state = 7;
                        break;
                    }
                    if (c == '1') {
                        qvalBuf.append(c);
                        state = 7;
                        break;
                    }
                    if (c == '.') {
                        if (isLenient) {
                            qvalBuf.append(c);
                            state = 8;
                            break;
                        }
                        state = -1;
                        break;
                    }
                    else {
                        if (c != ' ' && c != '\t') {
                            state = -1;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 7: {
                    if (c == '.') {
                        qvalBuf.append(c);
                        state = 8;
                        break;
                    }
                    if (c == ',') {
                        gotLanguageQ = true;
                        break;
                    }
                    if (c == ' ' || c == '\t') {
                        state = 10;
                        break;
                    }
                    state = -1;
                    break;
                }
                case 8: {
                    if ('0' > c && c > '9') {
                        state = -1;
                        break;
                    }
                    if (q1 && c != '0' && !isLenient) {
                        state = -1;
                        break;
                    }
                    qvalBuf.append(c);
                    state = 9;
                    break;
                }
                case 9: {
                    if ('0' <= c && c <= '9') {
                        if (q1 && c != '0') {
                            state = -1;
                            break;
                        }
                        qvalBuf.append(c);
                        break;
                    }
                    else {
                        if (c == ',') {
                            gotLanguageQ = true;
                            break;
                        }
                        if (c == ' ' || c == '\t') {
                            state = 10;
                            break;
                        }
                        state = -1;
                        break;
                    }
                    break;
                }
                case 10: {
                    if (c == ',') {
                        gotLanguageQ = true;
                        break;
                    }
                    if (c != ' ' && c != '\t') {
                        state = -1;
                        break;
                    }
                    break;
                }
            }
            if (state == -1) {
                throw new ParseException("Invalid Accept-Language", n);
            }
            if (gotLanguageQ) {
                double q2 = 1.0;
                if (qvalBuf.length() != 0) {
                    try {
                        q2 = Double.parseDouble(qvalBuf.toString());
                    }
                    catch (NumberFormatException nfe) {
                        q2 = 1.0;
                    }
                    if (q2 > 1.0) {
                        q2 = 1.0;
                    }
                }
                if (languageRangeBuf.charAt(0) != '*') {
                    final int serial = map.size();
                    final ULocaleAcceptLanguageQ entry = new ULocaleAcceptLanguageQ(serial);
                    map.put(entry, new ULocale(canonicalize(languageRangeBuf.toString())));
                }
                languageRangeBuf.setLength(0);
                qvalBuf.setLength(0);
                state = 0;
            }
        }
        if (state != 0) {
            throw new ParseException("Invalid AcceptlLanguage", n);
        }
        final ULocale[] acceptList = map.values().toArray(new ULocale[map.size()]);
        return acceptList;
    }
    
    public static ULocale addLikelySubtags(final ULocale loc) {
        final String[] tags = new String[3];
        String trailing = null;
        final int trailingIndex = parseTagString(loc.localeID, tags);
        if (trailingIndex < loc.localeID.length()) {
            trailing = loc.localeID.substring(trailingIndex);
        }
        final String newLocaleID = createLikelySubtagsString(tags[0], tags[1], tags[2], trailing);
        return (newLocaleID == null) ? loc : new ULocale(newLocaleID);
    }
    
    public static ULocale minimizeSubtags(final ULocale loc) {
        final String[] tags = new String[3];
        final int trailingIndex = parseTagString(loc.localeID, tags);
        final String originalLang = tags[0];
        final String originalScript = tags[1];
        final String originalRegion = tags[2];
        String originalTrailing = null;
        if (trailingIndex < loc.localeID.length()) {
            originalTrailing = loc.localeID.substring(trailingIndex);
        }
        final String maximizedLocaleID = createLikelySubtagsString(originalLang, originalScript, originalRegion, null);
        if (isEmptyString(maximizedLocaleID)) {
            return loc;
        }
        String tag = createLikelySubtagsString(originalLang, null, null, null);
        if (tag.equals(maximizedLocaleID)) {
            final String newLocaleID = createTagString(originalLang, null, null, originalTrailing);
            return new ULocale(newLocaleID);
        }
        if (originalRegion.length() != 0) {
            tag = createLikelySubtagsString(originalLang, null, originalRegion, null);
            if (tag.equals(maximizedLocaleID)) {
                final String newLocaleID = createTagString(originalLang, null, originalRegion, originalTrailing);
                return new ULocale(newLocaleID);
            }
        }
        if (originalRegion.length() != 0 && originalScript.length() != 0) {
            tag = createLikelySubtagsString(originalLang, originalScript, null, null);
            if (tag.equals(maximizedLocaleID)) {
                final String newLocaleID = createTagString(originalLang, originalScript, null, originalTrailing);
                return new ULocale(newLocaleID);
            }
        }
        return loc;
    }
    
    private static boolean isEmptyString(final String string) {
        return string == null || string.length() == 0;
    }
    
    private static void appendTag(final String tag, final StringBuilder buffer) {
        if (buffer.length() != 0) {
            buffer.append('_');
        }
        buffer.append(tag);
    }
    
    private static String createTagString(final String lang, final String script, final String region, final String trailing, final String alternateTags) {
        LocaleIDParser parser = null;
        boolean regionAppended = false;
        final StringBuilder tag = new StringBuilder();
        if (!isEmptyString(lang)) {
            appendTag(lang, tag);
        }
        else if (isEmptyString(alternateTags)) {
            appendTag("und", tag);
        }
        else {
            parser = new LocaleIDParser(alternateTags);
            final String alternateLang = parser.getLanguage();
            appendTag(isEmptyString(alternateLang) ? "und" : alternateLang, tag);
        }
        if (!isEmptyString(script)) {
            appendTag(script, tag);
        }
        else if (!isEmptyString(alternateTags)) {
            if (parser == null) {
                parser = new LocaleIDParser(alternateTags);
            }
            final String alternateScript = parser.getScript();
            if (!isEmptyString(alternateScript)) {
                appendTag(alternateScript, tag);
            }
        }
        if (!isEmptyString(region)) {
            appendTag(region, tag);
            regionAppended = true;
        }
        else if (!isEmptyString(alternateTags)) {
            if (parser == null) {
                parser = new LocaleIDParser(alternateTags);
            }
            final String alternateRegion = parser.getCountry();
            if (!isEmptyString(alternateRegion)) {
                appendTag(alternateRegion, tag);
                regionAppended = true;
            }
        }
        if (trailing != null && trailing.length() > 1) {
            int separators = 0;
            if (trailing.charAt(0) == '_') {
                if (trailing.charAt(1) == '_') {
                    separators = 2;
                }
            }
            else {
                separators = 1;
            }
            if (regionAppended) {
                if (separators == 2) {
                    tag.append(trailing.substring(1));
                }
                else {
                    tag.append(trailing);
                }
            }
            else {
                if (separators == 1) {
                    tag.append('_');
                }
                tag.append(trailing);
            }
        }
        return tag.toString();
    }
    
    static String createTagString(final String lang, final String script, final String region, final String trailing) {
        return createTagString(lang, script, region, trailing, null);
    }
    
    private static int parseTagString(final String localeID, final String[] tags) {
        final LocaleIDParser parser = new LocaleIDParser(localeID);
        final String lang = parser.getLanguage();
        final String script = parser.getScript();
        final String region = parser.getCountry();
        if (isEmptyString(lang)) {
            tags[0] = "und";
        }
        else {
            tags[0] = lang;
        }
        if (script.equals("Zzzz")) {
            tags[1] = "";
        }
        else {
            tags[1] = script;
        }
        if (region.equals("ZZ")) {
            tags[2] = "";
        }
        else {
            tags[2] = region;
        }
        final String variant = parser.getVariant();
        if (!isEmptyString(variant)) {
            final int index = localeID.indexOf(variant);
            return (index > 0) ? (index - 1) : index;
        }
        final int index = localeID.indexOf(64);
        return (index == -1) ? localeID.length() : index;
    }
    
    private static String lookupLikelySubtags(final String localeId) {
        final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "likelySubtags");
        try {
            return bundle.getString(localeId);
        }
        catch (MissingResourceException e) {
            return null;
        }
    }
    
    private static String createLikelySubtagsString(final String lang, final String script, final String region, final String variants) {
        if (!isEmptyString(script) && !isEmptyString(region)) {
            final String searchTag = createTagString(lang, script, region, null);
            final String likelySubtags = lookupLikelySubtags(searchTag);
            if (likelySubtags != null) {
                return createTagString(null, null, null, variants, likelySubtags);
            }
        }
        if (!isEmptyString(script)) {
            final String searchTag = createTagString(lang, script, null, null);
            final String likelySubtags = lookupLikelySubtags(searchTag);
            if (likelySubtags != null) {
                return createTagString(null, null, region, variants, likelySubtags);
            }
        }
        if (!isEmptyString(region)) {
            final String searchTag = createTagString(lang, null, region, null);
            final String likelySubtags = lookupLikelySubtags(searchTag);
            if (likelySubtags != null) {
                return createTagString(null, script, null, variants, likelySubtags);
            }
        }
        final String searchTag = createTagString(lang, null, null, null);
        final String likelySubtags = lookupLikelySubtags(searchTag);
        if (likelySubtags != null) {
            return createTagString(null, script, region, variants, likelySubtags);
        }
        return null;
    }
    
    public String getExtension(final char key) {
        if (!LocaleExtensions.isValidKey(key)) {
            throw new IllegalArgumentException("Invalid extension key: " + key);
        }
        return this.extensions().getExtensionValue(key);
    }
    
    public Set<Character> getExtensionKeys() {
        return this.extensions().getKeys();
    }
    
    public Set<String> getUnicodeLocaleAttributes() {
        return this.extensions().getUnicodeLocaleAttributes();
    }
    
    public String getUnicodeLocaleType(final String key) {
        if (!LocaleExtensions.isValidUnicodeLocaleKey(key)) {
            throw new IllegalArgumentException("Invalid Unicode locale key: " + key);
        }
        return this.extensions().getUnicodeLocaleType(key);
    }
    
    public Set<String> getUnicodeLocaleKeys() {
        return this.extensions().getUnicodeLocaleKeys();
    }
    
    public String toLanguageTag() {
        BaseLocale base = this.base();
        LocaleExtensions exts = this.extensions();
        if (base.getVariant().equalsIgnoreCase("POSIX")) {
            base = BaseLocale.getInstance(base.getLanguage(), base.getScript(), base.getRegion(), "");
            if (exts.getUnicodeLocaleType("va") == null) {
                final InternalLocaleBuilder ilocbld = new InternalLocaleBuilder();
                try {
                    ilocbld.setLocale(BaseLocale.ROOT, exts);
                    ilocbld.setUnicodeLocaleKeyword("va", "posix");
                    exts = ilocbld.getLocaleExtensions();
                }
                catch (LocaleSyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        final LanguageTag tag = LanguageTag.parseLocale(base, exts);
        final StringBuilder buf = new StringBuilder();
        String subtag = tag.getLanguage();
        if (subtag.length() > 0) {
            buf.append(LanguageTag.canonicalizeLanguage(subtag));
        }
        subtag = tag.getScript();
        if (subtag.length() > 0) {
            buf.append("-");
            buf.append(LanguageTag.canonicalizeScript(subtag));
        }
        subtag = tag.getRegion();
        if (subtag.length() > 0) {
            buf.append("-");
            buf.append(LanguageTag.canonicalizeRegion(subtag));
        }
        List<String> subtags = tag.getVariants();
        for (final String s : subtags) {
            buf.append("-");
            buf.append(LanguageTag.canonicalizeVariant(s));
        }
        subtags = tag.getExtensions();
        for (final String s : subtags) {
            buf.append("-");
            buf.append(LanguageTag.canonicalizeExtension(s));
        }
        subtag = tag.getPrivateuse();
        if (subtag.length() > 0) {
            if (buf.length() > 0) {
                buf.append("-");
            }
            buf.append("x").append("-");
            buf.append(LanguageTag.canonicalizePrivateuse(subtag));
        }
        return buf.toString();
    }
    
    public static ULocale forLanguageTag(final String languageTag) {
        final LanguageTag tag = LanguageTag.parse(languageTag, null);
        final InternalLocaleBuilder bldr = new InternalLocaleBuilder();
        bldr.setLanguageTag(tag);
        return getInstance(bldr.getBaseLocale(), bldr.getLocaleExtensions());
    }
    
    private static ULocale getInstance(final BaseLocale base, final LocaleExtensions exts) {
        String id = lscvToID(base.getLanguage(), base.getScript(), base.getRegion(), base.getVariant());
        final Set<Character> extKeys = exts.getKeys();
        if (!extKeys.isEmpty()) {
            final TreeMap<String, String> kwds = new TreeMap<String, String>();
            for (final Character key : extKeys) {
                final Extension ext = exts.getExtension(key);
                if (ext instanceof UnicodeLocaleExtension) {
                    final UnicodeLocaleExtension uext = (UnicodeLocaleExtension)ext;
                    final Set<String> ukeys = uext.getUnicodeLocaleKeys();
                    for (final String bcpKey : ukeys) {
                        final String bcpType = uext.getUnicodeLocaleType(bcpKey);
                        final String lkey = bcp47ToLDMLKey(bcpKey);
                        final String ltype = bcp47ToLDMLType(lkey, (bcpType.length() == 0) ? "yes" : bcpType);
                        if (lkey.equals("va") && ltype.equals("posix") && base.getVariant().length() == 0) {
                            id += "_POSIX";
                        }
                        else {
                            kwds.put(lkey, ltype);
                        }
                    }
                    final Set<String> uattributes = uext.getUnicodeLocaleAttributes();
                    if (uattributes.size() <= 0) {
                        continue;
                    }
                    final StringBuilder attrbuf = new StringBuilder();
                    for (final String attr : uattributes) {
                        if (attrbuf.length() > 0) {
                            attrbuf.append('-');
                        }
                        attrbuf.append(attr);
                    }
                    kwds.put("attribute", attrbuf.toString());
                }
                else {
                    kwds.put(String.valueOf(key), ext.getValue());
                }
            }
            if (!kwds.isEmpty()) {
                final StringBuilder buf = new StringBuilder(id);
                buf.append("@");
                final Set<Map.Entry<String, String>> kset = kwds.entrySet();
                boolean insertSep = false;
                for (final Map.Entry<String, String> kwd : kset) {
                    if (insertSep) {
                        buf.append(";");
                    }
                    else {
                        insertSep = true;
                    }
                    buf.append(kwd.getKey());
                    buf.append("=");
                    buf.append(kwd.getValue());
                }
                id = buf.toString();
            }
        }
        return new ULocale(id);
    }
    
    private BaseLocale base() {
        if (this.baseLocale == null) {
            String language = this.getLanguage();
            if (this.equals(ULocale.ROOT)) {
                language = "";
            }
            this.baseLocale = BaseLocale.getInstance(language, this.getScript(), this.getCountry(), this.getVariant());
        }
        return this.baseLocale;
    }
    
    private LocaleExtensions extensions() {
        if (this.extensions == null) {
            final Iterator<String> kwitr = this.getKeywords();
            if (kwitr == null) {
                this.extensions = LocaleExtensions.EMPTY_EXTENSIONS;
            }
            else {
                final InternalLocaleBuilder intbld = new InternalLocaleBuilder();
                while (kwitr.hasNext()) {
                    final String key = kwitr.next();
                    if (key.equals("attribute")) {
                        final String[] arr$;
                        final String[] uattributes = arr$ = this.getKeywordValue(key).split("[-_]");
                        for (final String uattr : arr$) {
                            try {
                                intbld.addUnicodeLocaleAttribute(uattr);
                            }
                            catch (LocaleSyntaxException ex) {}
                        }
                    }
                    else if (key.length() >= 2) {
                        final String bcpKey = ldmlKeyToBCP47(key);
                        final String bcpType = ldmlTypeToBCP47(key, this.getKeywordValue(key));
                        if (bcpKey == null || bcpType == null) {
                            continue;
                        }
                        try {
                            intbld.setUnicodeLocaleKeyword(bcpKey, bcpType);
                        }
                        catch (LocaleSyntaxException ex2) {}
                    }
                    else {
                        if (key.length() != 1 || key.charAt(0) == 'u') {
                            continue;
                        }
                        try {
                            intbld.setExtension(key.charAt(0), this.getKeywordValue(key).replace("_", "-"));
                        }
                        catch (LocaleSyntaxException ex3) {}
                    }
                }
                this.extensions = intbld.getLocaleExtensions();
            }
        }
        return this.extensions;
    }
    
    private static String ldmlKeyToBCP47(String key) {
        final UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        final UResourceBundle keyMap = keyTypeData.get("keyMap");
        key = AsciiUtil.toLowerString(key);
        String bcpKey = null;
        try {
            bcpKey = keyMap.getString(key);
        }
        catch (MissingResourceException ex) {}
        if (bcpKey != null) {
            return bcpKey;
        }
        if (key.length() == 2 && LanguageTag.isExtensionSubtag(key)) {
            return key;
        }
        return null;
    }
    
    private static String bcp47ToLDMLKey(String bcpKey) {
        final UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        final UResourceBundle keyMap = keyTypeData.get("keyMap");
        bcpKey = AsciiUtil.toLowerString(bcpKey);
        String key = null;
        for (int i = 0; i < keyMap.getSize(); ++i) {
            final UResourceBundle mapData = keyMap.get(i);
            if (bcpKey.equals(mapData.getString())) {
                key = mapData.getKey();
                break;
            }
        }
        if (key == null) {
            return bcpKey;
        }
        return key;
    }
    
    private static String ldmlTypeToBCP47(String key, final String type) {
        final UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        final UResourceBundle typeMap = keyTypeData.get("typeMap");
        key = AsciiUtil.toLowerString(key);
        UResourceBundle typeMapForKey = null;
        String bcpType = null;
        String typeResKey = key.equals("timezone") ? type.replace('/', ':') : type;
        try {
            typeMapForKey = typeMap.get(key);
            bcpType = typeMapForKey.getString(typeResKey);
        }
        catch (MissingResourceException ex) {}
        if (bcpType == null && typeMapForKey != null) {
            final UResourceBundle typeAlias = keyTypeData.get("typeAlias");
            try {
                final UResourceBundle typeAliasForKey = typeAlias.get(key);
                typeResKey = typeAliasForKey.getString(typeResKey);
                bcpType = typeMapForKey.getString(typeResKey.replace('/', ':'));
            }
            catch (MissingResourceException ex2) {}
        }
        if (bcpType != null) {
            return bcpType;
        }
        final int typeLen = type.length();
        if (typeLen >= 3 && typeLen <= 8 && LanguageTag.isExtensionSubtag(type)) {
            return type;
        }
        return null;
    }
    
    private static String bcp47ToLDMLType(String key, String bcpType) {
        final UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        final UResourceBundle typeMap = keyTypeData.get("typeMap");
        key = AsciiUtil.toLowerString(key);
        bcpType = AsciiUtil.toLowerString(bcpType);
        String type = null;
        try {
            final UResourceBundle typeMapForKey = typeMap.get(key);
            int i = 0;
            while (i < typeMapForKey.getSize()) {
                final UResourceBundle mapData = typeMapForKey.get(i);
                if (bcpType.equals(mapData.getString())) {
                    type = mapData.getKey();
                    if (key.equals("timezone")) {
                        type = type.replace(':', '/');
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
        catch (MissingResourceException ex) {}
        if (type == null) {
            return bcpType;
        }
        return type;
    }
    
    static {
        ENGLISH = new ULocale("en", Locale.ENGLISH);
        FRENCH = new ULocale("fr", Locale.FRENCH);
        GERMAN = new ULocale("de", Locale.GERMAN);
        ITALIAN = new ULocale("it", Locale.ITALIAN);
        JAPANESE = new ULocale("ja", Locale.JAPANESE);
        KOREAN = new ULocale("ko", Locale.KOREAN);
        CHINESE = new ULocale("zh", Locale.CHINESE);
        SIMPLIFIED_CHINESE = new ULocale("zh_Hans", Locale.CHINESE);
        TRADITIONAL_CHINESE = new ULocale("zh_Hant", Locale.CHINESE);
        FRANCE = new ULocale("fr_FR", Locale.FRANCE);
        GERMANY = new ULocale("de_DE", Locale.GERMANY);
        ITALY = new ULocale("it_IT", Locale.ITALY);
        JAPAN = new ULocale("ja_JP", Locale.JAPAN);
        KOREA = new ULocale("ko_KR", Locale.KOREA);
        CHINA = new ULocale("zh_Hans_CN", Locale.CHINA);
        PRC = ULocale.CHINA;
        TAIWAN = new ULocale("zh_Hant_TW", Locale.TAIWAN);
        UK = new ULocale("en_GB", Locale.UK);
        US = new ULocale("en_US", Locale.US);
        CANADA = new ULocale("en_CA", Locale.CANADA);
        CANADA_FRENCH = new ULocale("fr_CA", Locale.CANADA_FRENCH);
        EMPTY_LOCALE = new Locale("", "");
        ROOT = new ULocale("", ULocale.EMPTY_LOCALE);
        CACHE = new SimpleCache<Locale, ULocale>();
        ULocale.nameCache = new SimpleCache<String, String>();
        ULocale.defaultLocale = Locale.getDefault();
        ULocale.defaultCategoryLocales = new Locale[Category.values().length];
        ULocale.defaultCategoryULocales = new ULocale[Category.values().length];
        ULocale.defaultULocale = forLocale(ULocale.defaultLocale);
        if (JDKLocaleHelper.isJava7orNewer()) {
            for (final Category cat : Category.values()) {
                final int idx = cat.ordinal();
                ULocale.defaultCategoryLocales[idx] = JDKLocaleHelper.getDefault(cat);
                ULocale.defaultCategoryULocales[idx] = forLocale(ULocale.defaultCategoryLocales[idx]);
            }
        }
        else {
            if (JDKLocaleHelper.isOriginalDefaultLocale(ULocale.defaultLocale)) {
                final String userScript = JDKLocaleHelper.getSystemProperty("user.script");
                if (userScript != null && LanguageTag.isScript(userScript)) {
                    final BaseLocale base = ULocale.defaultULocale.base();
                    final BaseLocale newBase = BaseLocale.getInstance(base.getLanguage(), userScript, base.getRegion(), base.getVariant());
                    ULocale.defaultULocale = getInstance(newBase, ULocale.defaultULocale.extensions());
                }
            }
            for (final Category cat : Category.values()) {
                final int idx = cat.ordinal();
                ULocale.defaultCategoryLocales[idx] = ULocale.defaultLocale;
                ULocale.defaultCategoryULocales[idx] = ULocale.defaultULocale;
            }
        }
        ULocale.ACTUAL_LOCALE = new Type();
        ULocale.VALID_LOCALE = new Type();
    }
    
    public enum Category
    {
        DISPLAY, 
        FORMAT;
    }
    
    public static final class Type
    {
        private Type() {
        }
    }
    
    public static final class Builder
    {
        private final InternalLocaleBuilder _locbld;
        
        public Builder() {
            this._locbld = new InternalLocaleBuilder();
        }
        
        public Builder setLocale(final ULocale locale) {
            try {
                this._locbld.setLocale(locale.base(), locale.extensions());
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder setLanguageTag(final String languageTag) {
            final ParseStatus sts = new ParseStatus();
            final LanguageTag tag = LanguageTag.parse(languageTag, sts);
            if (sts.isError()) {
                throw new IllformedLocaleException(sts.getErrorMessage(), sts.getErrorIndex());
            }
            this._locbld.setLanguageTag(tag);
            return this;
        }
        
        public Builder setLanguage(final String language) {
            try {
                this._locbld.setLanguage(language);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder setScript(final String script) {
            try {
                this._locbld.setScript(script);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder setRegion(final String region) {
            try {
                this._locbld.setRegion(region);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder setVariant(final String variant) {
            try {
                this._locbld.setVariant(variant);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder setExtension(final char key, final String value) {
            try {
                this._locbld.setExtension(key, value);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder setUnicodeLocaleKeyword(final String key, final String type) {
            try {
                this._locbld.setUnicodeLocaleKeyword(key, type);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder addUnicodeLocaleAttribute(final String attribute) {
            try {
                this._locbld.addUnicodeLocaleAttribute(attribute);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder removeUnicodeLocaleAttribute(final String attribute) {
            try {
                this._locbld.removeUnicodeLocaleAttribute(attribute);
            }
            catch (LocaleSyntaxException e) {
                throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
            }
            return this;
        }
        
        public Builder clear() {
            this._locbld.clear();
            return this;
        }
        
        public Builder clearExtensions() {
            this._locbld.clearExtensions();
            return this;
        }
        
        public ULocale build() {
            return getInstance(this._locbld.getBaseLocale(), this._locbld.getLocaleExtensions());
        }
    }
    
    private static final class JDKLocaleHelper
    {
        private static boolean isJava7orNewer;
        private static Method mGetScript;
        private static Method mGetExtensionKeys;
        private static Method mGetExtension;
        private static Method mGetUnicodeLocaleKeys;
        private static Method mGetUnicodeLocaleAttributes;
        private static Method mGetUnicodeLocaleType;
        private static Method mForLanguageTag;
        private static Method mGetDefault;
        private static Method mSetDefault;
        private static Object eDISPLAY;
        private static Object eFORMAT;
        private static final String[][] JAVA6_MAPDATA;
        
        public static boolean isJava7orNewer() {
            return JDKLocaleHelper.isJava7orNewer;
        }
        
        public static ULocale toULocale(final Locale loc) {
            return JDKLocaleHelper.isJava7orNewer ? toULocale7(loc) : toULocale6(loc);
        }
        
        public static Locale toLocale(final ULocale uloc) {
            return JDKLocaleHelper.isJava7orNewer ? toLocale7(uloc) : toLocale6(uloc);
        }
        
        private static ULocale toULocale7(final Locale loc) {
            String language = loc.getLanguage();
            String script = "";
            final String country = loc.getCountry();
            String variant = loc.getVariant();
            Set<String> attributes = null;
            Map<String, String> keywords = null;
            try {
                script = (String)JDKLocaleHelper.mGetScript.invoke(loc, (Object[])null);
                final Set<Character> extKeys = (Set<Character>)JDKLocaleHelper.mGetExtensionKeys.invoke(loc, (Object[])null);
                if (!extKeys.isEmpty()) {
                    for (final Character extKey : extKeys) {
                        if (extKey == 'u') {
                            final Set<String> uAttributes = (Set<String>)JDKLocaleHelper.mGetUnicodeLocaleAttributes.invoke(loc, (Object[])null);
                            if (!uAttributes.isEmpty()) {
                                attributes = new TreeSet<String>();
                                for (final String attr : uAttributes) {
                                    attributes.add(attr);
                                }
                            }
                            final Set<String> uKeys = (Set<String>)JDKLocaleHelper.mGetUnicodeLocaleKeys.invoke(loc, (Object[])null);
                            for (final String kwKey : uKeys) {
                                final String kwVal = (String)JDKLocaleHelper.mGetUnicodeLocaleType.invoke(loc, kwKey);
                                if (kwVal != null) {
                                    if (kwKey.equals("va")) {
                                        variant = ((variant.length() == 0) ? kwVal : (kwVal + "_" + variant));
                                    }
                                    else {
                                        if (keywords == null) {
                                            keywords = new TreeMap<String, String>();
                                        }
                                        keywords.put(kwKey, kwVal);
                                    }
                                }
                            }
                        }
                        else {
                            final String extVal = (String)JDKLocaleHelper.mGetExtension.invoke(loc, extKey);
                            if (extVal == null) {
                                continue;
                            }
                            if (keywords == null) {
                                keywords = new TreeMap<String, String>();
                            }
                            keywords.put(String.valueOf(extKey), extVal);
                        }
                    }
                }
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e2) {
                throw new RuntimeException(e2);
            }
            if (language.equals("no") && country.equals("NO") && variant.equals("NY")) {
                language = "nn";
                variant = "";
            }
            final StringBuilder buf = new StringBuilder(language);
            if (script.length() > 0) {
                buf.append('_');
                buf.append(script);
            }
            if (country.length() > 0) {
                buf.append('_');
                buf.append(country);
            }
            if (variant.length() > 0) {
                if (country.length() == 0) {
                    buf.append('_');
                }
                buf.append('_');
                buf.append(variant);
            }
            if (attributes != null) {
                final StringBuilder attrBuf = new StringBuilder();
                for (final String attr2 : attributes) {
                    if (attrBuf.length() != 0) {
                        attrBuf.append('-');
                    }
                    attrBuf.append(attr2);
                }
                if (keywords == null) {
                    keywords = new TreeMap<String, String>();
                }
                keywords.put("attribute", attrBuf.toString());
            }
            if (keywords != null) {
                buf.append('@');
                boolean addSep = false;
                for (final Map.Entry<String, String> kwEntry : keywords.entrySet()) {
                    String kwKey2 = kwEntry.getKey();
                    String kwVal2 = kwEntry.getValue();
                    if (kwKey2.length() != 1) {
                        kwKey2 = bcp47ToLDMLKey(kwKey2);
                        kwVal2 = bcp47ToLDMLType(kwKey2, (kwVal2.length() == 0) ? "yes" : kwVal2);
                    }
                    if (addSep) {
                        buf.append(';');
                    }
                    else {
                        addSep = true;
                    }
                    buf.append(kwKey2);
                    buf.append('=');
                    buf.append(kwVal2);
                }
            }
            return new ULocale(ULocale.getName(buf.toString()), loc, null);
        }
        
        private static ULocale toULocale6(final Locale loc) {
            ULocale uloc = null;
            String locStr = loc.toString();
            if (locStr.length() == 0) {
                uloc = ULocale.ROOT;
            }
            else {
                for (int i = 0; i < JDKLocaleHelper.JAVA6_MAPDATA.length; ++i) {
                    if (JDKLocaleHelper.JAVA6_MAPDATA[i][0].equals(locStr)) {
                        final LocaleIDParser p = new LocaleIDParser(JDKLocaleHelper.JAVA6_MAPDATA[i][1]);
                        p.setKeywordValue(JDKLocaleHelper.JAVA6_MAPDATA[i][2], JDKLocaleHelper.JAVA6_MAPDATA[i][3]);
                        locStr = p.getName();
                        break;
                    }
                }
                uloc = new ULocale(ULocale.getName(locStr), loc, null);
            }
            return uloc;
        }
        
        private static Locale toLocale7(final ULocale uloc) {
            Locale loc = null;
            final String ulocStr = uloc.getName();
            if (uloc.getScript().length() > 0 || ulocStr.contains("@")) {
                String tag = uloc.toLanguageTag();
                tag = AsciiUtil.toUpperString(tag);
                try {
                    loc = (Locale)JDKLocaleHelper.mForLanguageTag.invoke(null, tag);
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                catch (InvocationTargetException e2) {
                    throw new RuntimeException(e2);
                }
            }
            if (loc == null) {
                loc = new Locale(uloc.getLanguage(), uloc.getCountry(), uloc.getVariant());
            }
            return loc;
        }
        
        private static Locale toLocale6(final ULocale uloc) {
            String locstr = uloc.getBaseName();
            for (int i = 0; i < JDKLocaleHelper.JAVA6_MAPDATA.length; ++i) {
                if (locstr.equals(JDKLocaleHelper.JAVA6_MAPDATA[i][1]) || locstr.equals(JDKLocaleHelper.JAVA6_MAPDATA[i][4])) {
                    if (JDKLocaleHelper.JAVA6_MAPDATA[i][2] == null) {
                        locstr = JDKLocaleHelper.JAVA6_MAPDATA[i][0];
                        break;
                    }
                    final String val = uloc.getKeywordValue(JDKLocaleHelper.JAVA6_MAPDATA[i][2]);
                    if (val != null && val.equals(JDKLocaleHelper.JAVA6_MAPDATA[i][3])) {
                        locstr = JDKLocaleHelper.JAVA6_MAPDATA[i][0];
                        break;
                    }
                }
            }
            final LocaleIDParser p = new LocaleIDParser(locstr);
            final String[] names = p.getLanguageScriptCountryVariant();
            return new Locale(names[0], names[2], names[3]);
        }
        
        public static Locale getDefault(final Category category) {
            Locale loc = Locale.getDefault();
            if (JDKLocaleHelper.isJava7orNewer) {
                Object cat = null;
                switch (category) {
                    case DISPLAY: {
                        cat = JDKLocaleHelper.eDISPLAY;
                        break;
                    }
                    case FORMAT: {
                        cat = JDKLocaleHelper.eFORMAT;
                        break;
                    }
                }
                if (cat != null) {
                    try {
                        loc = (Locale)JDKLocaleHelper.mGetDefault.invoke(null, cat);
                    }
                    catch (InvocationTargetException e) {}
                    catch (IllegalArgumentException e2) {}
                    catch (IllegalAccessException ex) {}
                }
            }
            return loc;
        }
        
        public static void setDefault(final Category category, final Locale newLocale) {
            if (JDKLocaleHelper.isJava7orNewer) {
                Object cat = null;
                switch (category) {
                    case DISPLAY: {
                        cat = JDKLocaleHelper.eDISPLAY;
                        break;
                    }
                    case FORMAT: {
                        cat = JDKLocaleHelper.eFORMAT;
                        break;
                    }
                }
                if (cat != null) {
                    try {
                        JDKLocaleHelper.mSetDefault.invoke(null, cat, newLocale);
                    }
                    catch (InvocationTargetException e) {}
                    catch (IllegalArgumentException e2) {}
                    catch (IllegalAccessException ex) {}
                }
            }
        }
        
        public static boolean isOriginalDefaultLocale(final Locale loc) {
            if (JDKLocaleHelper.isJava7orNewer) {
                String script = "";
                try {
                    script = (String)JDKLocaleHelper.mGetScript.invoke(loc, (Object[])null);
                }
                catch (Exception e) {
                    return false;
                }
                return loc.getLanguage().equals(getSystemProperty("user.language")) && loc.getCountry().equals(getSystemProperty("user.country")) && loc.getVariant().equals(getSystemProperty("user.variant")) && script.equals(getSystemProperty("user.script"));
            }
            return loc.getLanguage().equals(getSystemProperty("user.language")) && loc.getCountry().equals(getSystemProperty("user.country")) && loc.getVariant().equals(getSystemProperty("user.variant"));
        }
        
        public static String getSystemProperty(final String key) {
            String val = null;
            final String fkey = key;
            if (System.getSecurityManager() != null) {
                try {
                    val = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
                        public String run() {
                            return System.getProperty(fkey);
                        }
                    });
                }
                catch (AccessControlException e) {}
            }
            else {
                val = System.getProperty(fkey);
            }
            return val;
        }
        
        static {
            JDKLocaleHelper.isJava7orNewer = false;
            JAVA6_MAPDATA = new String[][] { { "ja_JP_JP", "ja_JP", "calendar", "japanese", "ja" }, { "no_NO_NY", "nn_NO", null, null, "nn" }, { "th_TH_TH", "th_TH", "numbers", "thai", "th" } };
            try {
                JDKLocaleHelper.mGetScript = Locale.class.getMethod("getScript", (Class<?>[])null);
                JDKLocaleHelper.mGetExtensionKeys = Locale.class.getMethod("getExtensionKeys", (Class<?>[])null);
                JDKLocaleHelper.mGetExtension = Locale.class.getMethod("getExtension", Character.TYPE);
                JDKLocaleHelper.mGetUnicodeLocaleKeys = Locale.class.getMethod("getUnicodeLocaleKeys", (Class<?>[])null);
                JDKLocaleHelper.mGetUnicodeLocaleAttributes = Locale.class.getMethod("getUnicodeLocaleAttributes", (Class<?>[])null);
                JDKLocaleHelper.mGetUnicodeLocaleType = Locale.class.getMethod("getUnicodeLocaleType", String.class);
                JDKLocaleHelper.mForLanguageTag = Locale.class.getMethod("forLanguageTag", String.class);
                Class<?> cCategory = null;
                final Class[] arr$;
                final Class<?>[] classes = (Class<?>[])(arr$ = Locale.class.getDeclaredClasses());
                for (final Class<?> c : arr$) {
                    if (c.getName().equals("java.util.Locale$Category")) {
                        cCategory = c;
                        break;
                    }
                }
                if (cCategory != null) {
                    JDKLocaleHelper.mGetDefault = Locale.class.getDeclaredMethod("getDefault", cCategory);
                    JDKLocaleHelper.mSetDefault = Locale.class.getDeclaredMethod("setDefault", cCategory, Locale.class);
                    final Method mName = cCategory.getMethod("name", (Class<?>[])null);
                    final Object[] arr$2;
                    final Object[] enumConstants = arr$2 = (Object[])cCategory.getEnumConstants();
                    for (final Object e : arr$2) {
                        final String catVal = (String)mName.invoke(e, (Object[])null);
                        if (catVal.equals("DISPLAY")) {
                            JDKLocaleHelper.eDISPLAY = e;
                        }
                        else if (catVal.equals("FORMAT")) {
                            JDKLocaleHelper.eFORMAT = e;
                        }
                    }
                    if (JDKLocaleHelper.eDISPLAY != null && JDKLocaleHelper.eFORMAT != null) {
                        JDKLocaleHelper.isJava7orNewer = true;
                    }
                }
            }
            catch (NoSuchMethodException e2) {}
            catch (IllegalArgumentException e3) {}
            catch (IllegalAccessException e4) {}
            catch (InvocationTargetException e5) {}
            catch (SecurityException ex) {}
        }
    }
}
