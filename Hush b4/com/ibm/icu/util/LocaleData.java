// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.MissingResourceException;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.impl.ICUResourceBundle;

public final class LocaleData
{
    private static final String MEASUREMENT_SYSTEM = "MeasurementSystem";
    private static final String PAPER_SIZE = "PaperSize";
    private static final String LOCALE_DISPLAY_PATTERN = "localeDisplayPattern";
    private static final String PATTERN = "pattern";
    private static final String SEPARATOR = "separator";
    private boolean noSubstitute;
    private ICUResourceBundle bundle;
    private ICUResourceBundle langBundle;
    public static final int ES_STANDARD = 0;
    public static final int ES_AUXILIARY = 1;
    public static final int ES_INDEX = 2;
    @Deprecated
    public static final int ES_CURRENCY = 3;
    public static final int ES_PUNCTUATION = 4;
    public static final int ES_COUNT = 5;
    public static final int QUOTATION_START = 0;
    public static final int QUOTATION_END = 1;
    public static final int ALT_QUOTATION_START = 2;
    public static final int ALT_QUOTATION_END = 3;
    public static final int DELIMITER_COUNT = 4;
    private static final String[] DELIMITER_TYPES;
    private static VersionInfo gCLDRVersion;
    
    private LocaleData() {
    }
    
    public static UnicodeSet getExemplarSet(final ULocale locale, final int options) {
        return getInstance(locale).getExemplarSet(options, 0);
    }
    
    public static UnicodeSet getExemplarSet(final ULocale locale, final int options, final int extype) {
        return getInstance(locale).getExemplarSet(options, extype);
    }
    
    public UnicodeSet getExemplarSet(final int options, final int extype) {
        final String[] exemplarSetTypes = { "ExemplarCharacters", "AuxExemplarCharacters", "ExemplarCharactersIndex", "ExemplarCharactersCurrency", "ExemplarCharactersPunctuation" };
        if (extype == 3) {
            return new UnicodeSet();
        }
        try {
            final ICUResourceBundle stringBundle = (ICUResourceBundle)this.bundle.get(exemplarSetTypes[extype]);
            if (this.noSubstitute && stringBundle.getLoadingStatus() == 2) {
                return null;
            }
            final String unicodeSetPattern = stringBundle.getString();
            if (extype == 4) {
                try {
                    return new UnicodeSet(unicodeSetPattern, 0x1 | options);
                }
                catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Can't create exemplars for " + exemplarSetTypes[extype] + " in " + this.bundle.getLocale(), e);
                }
            }
            return new UnicodeSet(unicodeSetPattern, 0x1 | options);
        }
        catch (MissingResourceException ex) {
            if (extype == 1) {
                return new UnicodeSet();
            }
            if (extype == 2) {
                return null;
            }
            throw ex;
        }
    }
    
    public static final LocaleData getInstance(final ULocale locale) {
        final LocaleData ld = new LocaleData();
        ld.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
        ld.langBundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/lang", locale);
        ld.noSubstitute = false;
        return ld;
    }
    
    public static final LocaleData getInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public void setNoSubstitute(final boolean setting) {
        this.noSubstitute = setting;
    }
    
    public boolean getNoSubstitute() {
        return this.noSubstitute;
    }
    
    public String getDelimiter(final int type) {
        final ICUResourceBundle delimitersBundle = (ICUResourceBundle)this.bundle.get("delimiters");
        final ICUResourceBundle stringBundle = delimitersBundle.getWithFallback(LocaleData.DELIMITER_TYPES[type]);
        if (this.noSubstitute && stringBundle.getLoadingStatus() == 2) {
            return null;
        }
        return stringBundle.getString();
    }
    
    public static final MeasurementSystem getMeasurementSystem(final ULocale locale) {
        final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
        final UResourceBundle sysBundle = bundle.get("MeasurementSystem");
        final int system = sysBundle.getInt();
        if (MeasurementSystem.US.equals(system)) {
            return MeasurementSystem.US;
        }
        if (MeasurementSystem.SI.equals(system)) {
            return MeasurementSystem.SI;
        }
        return null;
    }
    
    public static final PaperSize getPaperSize(final ULocale locale) {
        final UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
        final UResourceBundle obj = bundle.get("PaperSize");
        final int[] size = obj.getIntVector();
        return new PaperSize(size[0], size[1]);
    }
    
    public String getLocaleDisplayPattern() {
        final ICUResourceBundle locDispBundle = (ICUResourceBundle)this.langBundle.get("localeDisplayPattern");
        final String localeDisplayPattern = locDispBundle.getStringWithFallback("pattern");
        return localeDisplayPattern;
    }
    
    public String getLocaleSeparator() {
        final ICUResourceBundle locDispBundle = (ICUResourceBundle)this.langBundle.get("localeDisplayPattern");
        final String localeSeparator = locDispBundle.getStringWithFallback("separator");
        return localeSeparator;
    }
    
    public static VersionInfo getCLDRVersion() {
        if (LocaleData.gCLDRVersion == null) {
            final UResourceBundle supplementalDataBundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            final UResourceBundle cldrVersionBundle = supplementalDataBundle.get("cldrVersion");
            LocaleData.gCLDRVersion = VersionInfo.getInstance(cldrVersionBundle.getString());
        }
        return LocaleData.gCLDRVersion;
    }
    
    static {
        DELIMITER_TYPES = new String[] { "quotationStart", "quotationEnd", "alternateQuotationStart", "alternateQuotationEnd" };
        LocaleData.gCLDRVersion = null;
    }
    
    public static final class MeasurementSystem
    {
        public static final MeasurementSystem SI;
        public static final MeasurementSystem US;
        private int systemID;
        
        private MeasurementSystem(final int id) {
            this.systemID = id;
        }
        
        private boolean equals(final int id) {
            return this.systemID == id;
        }
        
        static {
            SI = new MeasurementSystem(0);
            US = new MeasurementSystem(1);
        }
    }
    
    public static final class PaperSize
    {
        private int height;
        private int width;
        
        private PaperSize(final int h, final int w) {
            this.height = h;
            this.width = w;
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public int getWidth() {
            return this.width;
        }
    }
}
