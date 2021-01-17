// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import java.util.Map;
import java.io.Serializable;

public class CurrencyPluralInfo implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final char[] tripleCurrencySign;
    private static final String tripleCurrencyStr;
    private static final char[] defaultCurrencyPluralPatternChar;
    private static final String defaultCurrencyPluralPattern;
    private Map<String, String> pluralCountToCurrencyUnitPattern;
    private PluralRules pluralRules;
    private ULocale ulocale;
    
    public CurrencyPluralInfo() {
        this.pluralCountToCurrencyUnitPattern = null;
        this.pluralRules = null;
        this.ulocale = null;
        this.initialize(ULocale.getDefault(ULocale.Category.FORMAT));
    }
    
    public CurrencyPluralInfo(final Locale locale) {
        this.pluralCountToCurrencyUnitPattern = null;
        this.pluralRules = null;
        this.ulocale = null;
        this.initialize(ULocale.forLocale(locale));
    }
    
    public CurrencyPluralInfo(final ULocale locale) {
        this.pluralCountToCurrencyUnitPattern = null;
        this.pluralRules = null;
        this.ulocale = null;
        this.initialize(locale);
    }
    
    public static CurrencyPluralInfo getInstance() {
        return new CurrencyPluralInfo();
    }
    
    public static CurrencyPluralInfo getInstance(final Locale locale) {
        return new CurrencyPluralInfo(locale);
    }
    
    public static CurrencyPluralInfo getInstance(final ULocale locale) {
        return new CurrencyPluralInfo(locale);
    }
    
    public PluralRules getPluralRules() {
        return this.pluralRules;
    }
    
    public String getCurrencyPluralPattern(final String pluralCount) {
        String currencyPluralPattern = this.pluralCountToCurrencyUnitPattern.get(pluralCount);
        if (currencyPluralPattern == null) {
            if (!pluralCount.equals("other")) {
                currencyPluralPattern = this.pluralCountToCurrencyUnitPattern.get("other");
            }
            if (currencyPluralPattern == null) {
                currencyPluralPattern = CurrencyPluralInfo.defaultCurrencyPluralPattern;
            }
        }
        return currencyPluralPattern;
    }
    
    public ULocale getLocale() {
        return this.ulocale;
    }
    
    public void setPluralRules(final String ruleDescription) {
        this.pluralRules = PluralRules.createRules(ruleDescription);
    }
    
    public void setCurrencyPluralPattern(final String pluralCount, final String pattern) {
        this.pluralCountToCurrencyUnitPattern.put(pluralCount, pattern);
    }
    
    public void setLocale(final ULocale loc) {
        this.initialize(this.ulocale = loc);
    }
    
    public Object clone() {
        try {
            final CurrencyPluralInfo other = (CurrencyPluralInfo)super.clone();
            other.ulocale = (ULocale)this.ulocale.clone();
            other.pluralCountToCurrencyUnitPattern = new HashMap<String, String>();
            for (final String pluralCount : this.pluralCountToCurrencyUnitPattern.keySet()) {
                final String currencyPattern = this.pluralCountToCurrencyUnitPattern.get(pluralCount);
                other.pluralCountToCurrencyUnitPattern.put(pluralCount, currencyPattern);
            }
            return other;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
    
    @Override
    public boolean equals(final Object a) {
        if (a instanceof CurrencyPluralInfo) {
            final CurrencyPluralInfo other = (CurrencyPluralInfo)a;
            return this.pluralRules.equals(other.pluralRules) && this.pluralCountToCurrencyUnitPattern.equals(other.pluralCountToCurrencyUnitPattern);
        }
        return false;
    }
    
    @Override
    @Deprecated
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    String select(final double number) {
        return this.pluralRules.select(number);
    }
    
    Iterator<String> pluralPatternIterator() {
        return this.pluralCountToCurrencyUnitPattern.keySet().iterator();
    }
    
    private void initialize(final ULocale uloc) {
        this.ulocale = uloc;
        this.pluralRules = PluralRules.forLocale(uloc);
        this.setupCurrencyPluralPattern(uloc);
    }
    
    private void setupCurrencyPluralPattern(final ULocale uloc) {
        this.pluralCountToCurrencyUnitPattern = new HashMap<String, String>();
        String numberStylePattern = NumberFormat.getPattern(uloc, 0);
        final int separatorIndex = numberStylePattern.indexOf(";");
        String negNumberPattern = null;
        if (separatorIndex != -1) {
            negNumberPattern = numberStylePattern.substring(separatorIndex + 1);
            numberStylePattern = numberStylePattern.substring(0, separatorIndex);
        }
        final Map<String, String> map = CurrencyData.provider.getInstance(uloc, true).getUnitPatterns();
        for (final Map.Entry<String, String> e : map.entrySet()) {
            final String pluralCount = e.getKey();
            final String pattern = e.getValue();
            final String patternWithNumber = pattern.replace("{0}", numberStylePattern);
            String patternWithCurrencySign = patternWithNumber.replace("{1}", CurrencyPluralInfo.tripleCurrencyStr);
            if (separatorIndex != -1) {
                final String negPattern = pattern;
                final String negWithNumber = negPattern.replace("{0}", negNumberPattern);
                final String negWithCurrSign = negWithNumber.replace("{1}", CurrencyPluralInfo.tripleCurrencyStr);
                final StringBuilder posNegPatterns = new StringBuilder(patternWithCurrencySign);
                posNegPatterns.append(";");
                posNegPatterns.append(negWithCurrSign);
                patternWithCurrencySign = posNegPatterns.toString();
            }
            this.pluralCountToCurrencyUnitPattern.put(pluralCount, patternWithCurrencySign);
        }
    }
    
    static {
        tripleCurrencySign = new char[] { '¤', '¤', '¤' };
        tripleCurrencyStr = new String(CurrencyPluralInfo.tripleCurrencySign);
        defaultCurrencyPluralPatternChar = new char[] { '\0', '.', '#', '#', ' ', '¤', '¤', '¤' };
        defaultCurrencyPluralPattern = new String(CurrencyPluralInfo.defaultCurrencyPluralPatternChar);
    }
}
