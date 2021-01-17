// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.lang.UScript;
import java.util.Iterator;
import java.util.Locale;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import java.util.HashMap;
import java.util.Map;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.text.LocaleDisplayNames;

public class LocaleDisplayNamesImpl extends LocaleDisplayNames
{
    private final ULocale locale;
    private final DialectHandling dialectHandling;
    private final DisplayContext capitalization;
    private final DataTable langData;
    private final DataTable regionData;
    private final Appender appender;
    private final MessageFormat format;
    private final MessageFormat keyTypeFormat;
    private static final Cache cache;
    private Map<CapitalizationContextUsage, boolean[]> capitalizationUsage;
    private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap;
    
    public static LocaleDisplayNames getInstance(final ULocale locale, final DialectHandling dialectHandling) {
        synchronized (LocaleDisplayNamesImpl.cache) {
            return LocaleDisplayNamesImpl.cache.get(locale, dialectHandling);
        }
    }
    
    public static LocaleDisplayNames getInstance(final ULocale locale, final DisplayContext... contexts) {
        synchronized (LocaleDisplayNamesImpl.cache) {
            return LocaleDisplayNamesImpl.cache.get(locale, contexts);
        }
    }
    
    public LocaleDisplayNamesImpl(final ULocale locale, final DialectHandling dialectHandling) {
        this(locale, new DisplayContext[] { (dialectHandling == DialectHandling.STANDARD_NAMES) ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES, DisplayContext.CAPITALIZATION_NONE });
    }
    
    public LocaleDisplayNamesImpl(final ULocale locale, final DisplayContext... contexts) {
        this.capitalizationUsage = null;
        DialectHandling dialectHandling = DialectHandling.STANDARD_NAMES;
        DisplayContext capitalization = DisplayContext.CAPITALIZATION_NONE;
        for (final DisplayContext contextItem : contexts) {
            switch (contextItem.type()) {
                case DIALECT_HANDLING: {
                    dialectHandling = ((contextItem.value() == DisplayContext.STANDARD_NAMES.value()) ? DialectHandling.STANDARD_NAMES : DialectHandling.DIALECT_NAMES);
                    break;
                }
                case CAPITALIZATION: {
                    capitalization = contextItem;
                    break;
                }
            }
        }
        this.dialectHandling = dialectHandling;
        this.capitalization = capitalization;
        this.langData = LangDataTables.impl.get(locale);
        this.regionData = RegionDataTables.impl.get(locale);
        this.locale = (ULocale.ROOT.equals(this.langData.getLocale()) ? this.regionData.getLocale() : this.langData.getLocale());
        String sep = this.langData.get("localeDisplayPattern", "separator");
        if ("separator".equals(sep)) {
            sep = ", ";
        }
        this.appender = new Appender(sep);
        String pattern = this.langData.get("localeDisplayPattern", "pattern");
        if ("pattern".equals(pattern)) {
            pattern = "{0} ({1})";
        }
        this.format = new MessageFormat(pattern);
        String keyTypePattern = this.langData.get("localeDisplayPattern", "keyTypePattern");
        if ("keyTypePattern".equals(keyTypePattern)) {
            keyTypePattern = "{0}={1}";
        }
        this.keyTypeFormat = new MessageFormat(keyTypePattern);
        if (capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU || capitalization == DisplayContext.CAPITALIZATION_FOR_STANDALONE) {
            this.capitalizationUsage = new HashMap<CapitalizationContextUsage, boolean[]>();
            final boolean[] noTransforms = { false, false };
            final CapitalizationContextUsage[] arr$2;
            final CapitalizationContextUsage[] allUsages = arr$2 = CapitalizationContextUsage.values();
            for (final CapitalizationContextUsage usage : arr$2) {
                this.capitalizationUsage.put(usage, noTransforms);
            }
            final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
            UResourceBundle contextTransformsBundle = null;
            try {
                contextTransformsBundle = rb.getWithFallback("contextTransforms");
            }
            catch (MissingResourceException e) {
                contextTransformsBundle = null;
            }
            if (contextTransformsBundle != null) {
                final UResourceBundleIterator ctIterator = contextTransformsBundle.getIterator();
                while (ctIterator.hasNext()) {
                    final UResourceBundle contextTransformUsage = ctIterator.next();
                    final int[] intVector = contextTransformUsage.getIntVector();
                    if (intVector.length >= 2) {
                        final String usageKey = contextTransformUsage.getKey();
                        final CapitalizationContextUsage usage2 = LocaleDisplayNamesImpl.contextUsageTypeMap.get(usageKey);
                        if (usage2 == null) {
                            continue;
                        }
                        final boolean[] transforms = { intVector[0] != 0, intVector[1] != 0 };
                        this.capitalizationUsage.put(usage2, transforms);
                    }
                }
            }
        }
    }
    
    @Override
    public ULocale getLocale() {
        return this.locale;
    }
    
    @Override
    public DialectHandling getDialectHandling() {
        return this.dialectHandling;
    }
    
    @Override
    public DisplayContext getContext(final DisplayContext.Type type) {
        DisplayContext result = null;
        switch (type) {
            case DIALECT_HANDLING: {
                result = ((this.dialectHandling == DialectHandling.STANDARD_NAMES) ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES);
                break;
            }
            case CAPITALIZATION: {
                result = this.capitalization;
                break;
            }
            default: {
                result = DisplayContext.STANDARD_NAMES;
                break;
            }
        }
        return result;
    }
    
    private String adjustForUsageAndContext(final CapitalizationContextUsage usage, final String name) {
        String result = name;
        boolean titlecase = false;
        switch (this.capitalization) {
            case CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE: {
                titlecase = true;
                break;
            }
            case CAPITALIZATION_FOR_UI_LIST_OR_MENU:
            case CAPITALIZATION_FOR_STANDALONE: {
                if (this.capitalizationUsage != null) {
                    final boolean[] transforms = this.capitalizationUsage.get(usage);
                    titlecase = ((this.capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU) ? transforms[0] : transforms[1]);
                    break;
                }
                break;
            }
        }
        if (titlecase) {
            int stopPosLimit = 8;
            final int len = name.length();
            if (stopPosLimit > len) {
                stopPosLimit = len;
            }
            int stopPos;
            for (stopPos = 0; stopPos < stopPosLimit; ++stopPos) {
                final int ch = name.codePointAt(stopPos);
                if (ch < 65 || (ch > 90 && ch < 97)) {
                    break;
                }
                if (ch > 122 && ch < 192) {
                    break;
                }
                if (ch >= 65536) {
                    ++stopPos;
                }
            }
            if (stopPos > 0 && stopPos < len) {
                final String firstWord = name.substring(0, stopPos);
                final String firstWordTitleCase = UCharacter.toTitleCase(this.locale, firstWord, null, 768);
                result = firstWordTitleCase.concat(name.substring(stopPos));
            }
            else {
                result = UCharacter.toTitleCase(this.locale, name, null, 768);
            }
        }
        return result;
    }
    
    @Override
    public String localeDisplayName(final ULocale locale) {
        return this.localeDisplayNameInternal(locale);
    }
    
    @Override
    public String localeDisplayName(final Locale locale) {
        return this.localeDisplayNameInternal(ULocale.forLocale(locale));
    }
    
    @Override
    public String localeDisplayName(final String localeId) {
        return this.localeDisplayNameInternal(new ULocale(localeId));
    }
    
    private String localeDisplayNameInternal(final ULocale locale) {
        String resultName = null;
        String lang = locale.getLanguage();
        if (locale.getBaseName().length() == 0) {
            lang = "root";
        }
        final String script = locale.getScript();
        final String country = locale.getCountry();
        final String variant = locale.getVariant();
        boolean hasScript = script.length() > 0;
        boolean hasCountry = country.length() > 0;
        final boolean hasVariant = variant.length() > 0;
        Label_0285: {
            if (this.dialectHandling == DialectHandling.DIALECT_NAMES) {
                if (hasScript && hasCountry) {
                    final String langScriptCountry = lang + '_' + script + '_' + country;
                    final String result = this.localeIdName(langScriptCountry);
                    if (!result.equals(langScriptCountry)) {
                        resultName = result;
                        hasScript = false;
                        hasCountry = false;
                        break Label_0285;
                    }
                }
                if (hasScript) {
                    final String langScript = lang + '_' + script;
                    final String result = this.localeIdName(langScript);
                    if (!result.equals(langScript)) {
                        resultName = result;
                        hasScript = false;
                        break Label_0285;
                    }
                }
                if (hasCountry) {
                    final String langCountry = lang + '_' + country;
                    final String result = this.localeIdName(langCountry);
                    if (!result.equals(langCountry)) {
                        resultName = result;
                        hasCountry = false;
                    }
                }
            }
        }
        if (resultName == null) {
            resultName = this.localeIdName(lang);
        }
        final StringBuilder buf = new StringBuilder();
        if (hasScript) {
            buf.append(this.scriptDisplayNameInContext(script));
        }
        if (hasCountry) {
            this.appender.append(this.regionDisplayName(country), buf);
        }
        if (hasVariant) {
            this.appender.append(this.variantDisplayName(variant), buf);
        }
        final Iterator<String> keys = locale.getKeywords();
        if (keys != null) {
            while (keys.hasNext()) {
                final String key = keys.next();
                final String value = locale.getKeywordValue(key);
                final String keyDisplayName = this.keyDisplayName(key);
                final String valueDisplayName = this.keyValueDisplayName(key, value);
                if (!valueDisplayName.equals(value)) {
                    this.appender.append(valueDisplayName, buf);
                }
                else if (!key.equals(keyDisplayName)) {
                    final String keyValue = this.keyTypeFormat.format(new String[] { keyDisplayName, valueDisplayName });
                    this.appender.append(keyValue, buf);
                }
                else {
                    this.appender.append(keyDisplayName, buf).append("=").append(valueDisplayName);
                }
            }
        }
        String resultRemainder = null;
        if (buf.length() > 0) {
            resultRemainder = buf.toString();
        }
        if (resultRemainder != null) {
            resultName = this.format.format(new Object[] { resultName, resultRemainder });
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, resultName);
    }
    
    private String localeIdName(final String localeId) {
        return this.langData.get("Languages", localeId);
    }
    
    @Override
    public String languageDisplayName(final String lang) {
        if (lang.equals("root") || lang.indexOf(95) != -1) {
            return lang;
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.LANGUAGE, this.langData.get("Languages", lang));
    }
    
    @Override
    public String scriptDisplayName(final String script) {
        String str = this.langData.get("Scripts%stand-alone", script);
        if (str.equals(script)) {
            str = this.langData.get("Scripts", script);
        }
        return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, str);
    }
    
    @Override
    public String scriptDisplayNameInContext(final String script) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, this.langData.get("Scripts", script));
    }
    
    @Override
    public String scriptDisplayName(final int scriptCode) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.SCRIPT, this.scriptDisplayName(UScript.getShortName(scriptCode)));
    }
    
    @Override
    public String regionDisplayName(final String region) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.TERRITORY, this.regionData.get("Countries", region));
    }
    
    @Override
    public String variantDisplayName(final String variant) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.VARIANT, this.langData.get("Variants", variant));
    }
    
    @Override
    public String keyDisplayName(final String key) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.KEY, this.langData.get("Keys", key));
    }
    
    @Override
    public String keyValueDisplayName(final String key, final String value) {
        return this.adjustForUsageAndContext(CapitalizationContextUsage.TYPE, this.langData.get("Types", key, value));
    }
    
    public static boolean haveData(final DataTableType type) {
        switch (type) {
            case LANG: {
                return LangDataTables.impl instanceof ICUDataTables;
            }
            case REGION: {
                return RegionDataTables.impl instanceof ICUDataTables;
            }
            default: {
                throw new IllegalArgumentException("unknown type: " + type);
            }
        }
    }
    
    static {
        cache = new Cache();
        (contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>()).put("languages", CapitalizationContextUsage.LANGUAGE);
        LocaleDisplayNamesImpl.contextUsageTypeMap.put("script", CapitalizationContextUsage.SCRIPT);
        LocaleDisplayNamesImpl.contextUsageTypeMap.put("territory", CapitalizationContextUsage.TERRITORY);
        LocaleDisplayNamesImpl.contextUsageTypeMap.put("variant", CapitalizationContextUsage.VARIANT);
        LocaleDisplayNamesImpl.contextUsageTypeMap.put("key", CapitalizationContextUsage.KEY);
        LocaleDisplayNamesImpl.contextUsageTypeMap.put("type", CapitalizationContextUsage.TYPE);
    }
    
    private enum CapitalizationContextUsage
    {
        LANGUAGE, 
        SCRIPT, 
        TERRITORY, 
        VARIANT, 
        KEY, 
        TYPE;
    }
    
    public static class DataTable
    {
        ULocale getLocale() {
            return ULocale.ROOT;
        }
        
        String get(final String tableName, final String code) {
            return this.get(tableName, null, code);
        }
        
        String get(final String tableName, final String subTableName, final String code) {
            return code;
        }
    }
    
    static class ICUDataTable extends DataTable
    {
        private final ICUResourceBundle bundle;
        
        public ICUDataTable(final String path, final ULocale locale) {
            this.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
        }
        
        public ULocale getLocale() {
            return this.bundle.getULocale();
        }
        
        public String get(final String tableName, final String subTableName, final String code) {
            return ICUResourceTableAccess.getTableString(this.bundle, tableName, subTableName, code);
        }
    }
    
    abstract static class DataTables
    {
        public abstract DataTable get(final ULocale p0);
        
        public static DataTables load(final String className) {
            try {
                return (DataTables)Class.forName(className).newInstance();
            }
            catch (Throwable t) {
                final DataTable NO_OP = new DataTable();
                return new DataTables() {
                    @Override
                    public DataTable get(final ULocale locale) {
                        return NO_OP;
                    }
                };
            }
        }
    }
    
    abstract static class ICUDataTables extends DataTables
    {
        private final String path;
        
        protected ICUDataTables(final String path) {
            this.path = path;
        }
        
        @Override
        public DataTable get(final ULocale locale) {
            return new ICUDataTable(this.path, locale);
        }
    }
    
    static class LangDataTables
    {
        static final DataTables impl;
        
        static {
            impl = DataTables.load("com.ibm.icu.impl.ICULangDataTables");
        }
    }
    
    static class RegionDataTables
    {
        static final DataTables impl;
        
        static {
            impl = DataTables.load("com.ibm.icu.impl.ICURegionDataTables");
        }
    }
    
    public enum DataTableType
    {
        LANG, 
        REGION;
    }
    
    static class Appender
    {
        private final String sep;
        
        Appender(final String sep) {
            this.sep = sep;
        }
        
        StringBuilder append(final String s, final StringBuilder b) {
            if (b.length() > 0) {
                b.append(this.sep);
            }
            b.append(s);
            return b;
        }
    }
    
    private static class Cache
    {
        private ULocale locale;
        private DialectHandling dialectHandling;
        private DisplayContext capitalization;
        private LocaleDisplayNames cache;
        
        public LocaleDisplayNames get(final ULocale locale, final DialectHandling dialectHandling) {
            if (dialectHandling != this.dialectHandling || DisplayContext.CAPITALIZATION_NONE != this.capitalization || !locale.equals(this.locale)) {
                this.locale = locale;
                this.dialectHandling = dialectHandling;
                this.capitalization = DisplayContext.CAPITALIZATION_NONE;
                this.cache = new LocaleDisplayNamesImpl(locale, dialectHandling);
            }
            return this.cache;
        }
        
        public LocaleDisplayNames get(final ULocale locale, final DisplayContext... contexts) {
            DialectHandling dialectHandlingIn = DialectHandling.STANDARD_NAMES;
            DisplayContext capitalizationIn = DisplayContext.CAPITALIZATION_NONE;
            for (final DisplayContext contextItem : contexts) {
                switch (contextItem.type()) {
                    case DIALECT_HANDLING: {
                        dialectHandlingIn = ((contextItem.value() == DisplayContext.STANDARD_NAMES.value()) ? DialectHandling.STANDARD_NAMES : DialectHandling.DIALECT_NAMES);
                        break;
                    }
                    case CAPITALIZATION: {
                        capitalizationIn = contextItem;
                        break;
                    }
                }
            }
            if (dialectHandlingIn != this.dialectHandling || capitalizationIn != this.capitalization || !locale.equals(this.locale)) {
                this.locale = locale;
                this.dialectHandling = dialectHandlingIn;
                this.capitalization = capitalizationIn;
                this.cache = new LocaleDisplayNamesImpl(locale, contexts);
            }
            return this.cache;
        }
    }
}
