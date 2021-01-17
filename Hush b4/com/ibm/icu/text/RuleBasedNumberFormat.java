// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import java.util.HashMap;
import com.ibm.icu.impl.PatternProps;
import java.text.ParsePosition;
import com.ibm.icu.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.util.Set;
import java.util.Comparator;
import java.util.Arrays;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Locale;
import java.util.Map;
import com.ibm.icu.util.ULocale;

public class RuleBasedNumberFormat extends NumberFormat
{
    static final long serialVersionUID = -7664252765575395068L;
    public static final int SPELLOUT = 1;
    public static final int ORDINAL = 2;
    public static final int DURATION = 3;
    public static final int NUMBERING_SYSTEM = 4;
    private transient NFRuleSet[] ruleSets;
    private transient String[] ruleSetDescriptions;
    private transient NFRuleSet defaultRuleSet;
    private ULocale locale;
    private transient RbnfLenientScannerProvider scannerProvider;
    private transient boolean lookedForScanner;
    private transient DecimalFormatSymbols decimalFormatSymbols;
    private transient DecimalFormat decimalFormat;
    private boolean lenientParse;
    private transient String lenientParseRules;
    private transient String postProcessRules;
    private transient RBNFPostProcessor postProcessor;
    private Map<String, String[]> ruleSetDisplayNames;
    private String[] publicRuleSetNames;
    private static final boolean DEBUG;
    private static final String[] rulenames;
    private static final String[] locnames;
    
    public RuleBasedNumberFormat(final String description) {
        this.ruleSets = null;
        this.ruleSetDescriptions = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.lenientParse = false;
        this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
        this.init(description, null);
    }
    
    public RuleBasedNumberFormat(final String description, final String[][] localizations) {
        this.ruleSets = null;
        this.ruleSetDescriptions = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.lenientParse = false;
        this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
        this.init(description, localizations);
    }
    
    public RuleBasedNumberFormat(final String description, final Locale locale) {
        this(description, ULocale.forLocale(locale));
    }
    
    public RuleBasedNumberFormat(final String description, final ULocale locale) {
        this.ruleSets = null;
        this.ruleSetDescriptions = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.lenientParse = false;
        this.locale = locale;
        this.init(description, null);
    }
    
    public RuleBasedNumberFormat(final String description, final String[][] localizations, final ULocale locale) {
        this.ruleSets = null;
        this.ruleSetDescriptions = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.lenientParse = false;
        this.locale = locale;
        this.init(description, localizations);
    }
    
    public RuleBasedNumberFormat(final Locale locale, final int format) {
        this(ULocale.forLocale(locale), format);
    }
    
    public RuleBasedNumberFormat(final ULocale locale, final int format) {
        this.ruleSets = null;
        this.ruleSetDescriptions = null;
        this.defaultRuleSet = null;
        this.locale = null;
        this.scannerProvider = null;
        this.decimalFormatSymbols = null;
        this.decimalFormat = null;
        this.lenientParse = false;
        this.locale = locale;
        final ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/rbnf", locale);
        final ULocale uloc = bundle.getULocale();
        this.setLocale(uloc, uloc);
        String description = "";
        String[][] localizations = null;
        try {
            description = bundle.getString(RuleBasedNumberFormat.rulenames[format - 1]);
        }
        catch (MissingResourceException e) {
            try {
                final ICUResourceBundle rules = bundle.getWithFallback("RBNFRules/" + RuleBasedNumberFormat.rulenames[format - 1]);
                final UResourceBundleIterator it = rules.getIterator();
                while (it.hasNext()) {
                    description = description.concat(it.nextString());
                }
            }
            catch (MissingResourceException ex) {}
        }
        try {
            final UResourceBundle locb = bundle.get(RuleBasedNumberFormat.locnames[format - 1]);
            localizations = new String[locb.getSize()][];
            for (int i = 0; i < localizations.length; ++i) {
                localizations[i] = locb.get(i).getStringArray();
            }
        }
        catch (MissingResourceException ex2) {}
        this.init(description, localizations);
    }
    
    public RuleBasedNumberFormat(final int format) {
        this(ULocale.getDefault(ULocale.Category.FORMAT), format);
    }
    
    @Override
    public Object clone() {
        return super.clone();
    }
    
    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof RuleBasedNumberFormat)) {
            return false;
        }
        final RuleBasedNumberFormat that2 = (RuleBasedNumberFormat)that;
        if (!this.locale.equals(that2.locale) || this.lenientParse != that2.lenientParse) {
            return false;
        }
        if (this.ruleSets.length != that2.ruleSets.length) {
            return false;
        }
        for (int i = 0; i < this.ruleSets.length; ++i) {
            if (!this.ruleSets[i].equals(that2.ruleSets[i])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @Deprecated
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.ruleSets.length; ++i) {
            result.append(this.ruleSets[i].toString());
        }
        return result.toString();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeUTF(this.toString());
        out.writeObject(this.locale);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException {
        final String description = in.readUTF();
        ULocale loc;
        try {
            loc = (ULocale)in.readObject();
        }
        catch (Exception e) {
            loc = ULocale.getDefault(ULocale.Category.FORMAT);
        }
        final RuleBasedNumberFormat temp = new RuleBasedNumberFormat(description, loc);
        this.ruleSets = temp.ruleSets;
        this.defaultRuleSet = temp.defaultRuleSet;
        this.publicRuleSetNames = temp.publicRuleSetNames;
        this.decimalFormatSymbols = temp.decimalFormatSymbols;
        this.decimalFormat = temp.decimalFormat;
        this.locale = temp.locale;
    }
    
    public String[] getRuleSetNames() {
        return this.publicRuleSetNames.clone();
    }
    
    public ULocale[] getRuleSetDisplayNameLocales() {
        if (this.ruleSetDisplayNames != null) {
            final Set<String> s = this.ruleSetDisplayNames.keySet();
            final String[] locales = s.toArray(new String[s.size()]);
            Arrays.sort(locales, String.CASE_INSENSITIVE_ORDER);
            final ULocale[] result = new ULocale[locales.length];
            for (int i = 0; i < locales.length; ++i) {
                result[i] = new ULocale(locales[i]);
            }
            return result;
        }
        return null;
    }
    
    private String[] getNameListForLocale(final ULocale loc) {
        if (loc != null && this.ruleSetDisplayNames != null) {
            final String[] localeNames = { loc.getBaseName(), ULocale.getDefault(ULocale.Category.DISPLAY).getBaseName() };
            for (int i = 0; i < localeNames.length; ++i) {
                for (String lname = localeNames[i]; lname.length() > 0; lname = ULocale.getFallback(lname)) {
                    final String[] names = this.ruleSetDisplayNames.get(lname);
                    if (names != null) {
                        return names;
                    }
                }
            }
        }
        return null;
    }
    
    public String[] getRuleSetDisplayNames(final ULocale loc) {
        String[] names = this.getNameListForLocale(loc);
        if (names != null) {
            return names.clone();
        }
        names = this.getRuleSetNames();
        for (int i = 0; i < names.length; ++i) {
            names[i] = names[i].substring(1);
        }
        return names;
    }
    
    public String[] getRuleSetDisplayNames() {
        return this.getRuleSetDisplayNames(ULocale.getDefault(ULocale.Category.DISPLAY));
    }
    
    public String getRuleSetDisplayName(final String ruleSetName, final ULocale loc) {
        final String[] rsnames = this.publicRuleSetNames;
        int ix = 0;
        while (ix < rsnames.length) {
            if (rsnames[ix].equals(ruleSetName)) {
                final String[] names = this.getNameListForLocale(loc);
                if (names != null) {
                    return names[ix];
                }
                return rsnames[ix].substring(1);
            }
            else {
                ++ix;
            }
        }
        throw new IllegalArgumentException("unrecognized rule set name: " + ruleSetName);
    }
    
    public String getRuleSetDisplayName(final String ruleSetName) {
        return this.getRuleSetDisplayName(ruleSetName, ULocale.getDefault(ULocale.Category.DISPLAY));
    }
    
    public String format(final double number, final String ruleSet) throws IllegalArgumentException {
        if (ruleSet.startsWith("%%")) {
            throw new IllegalArgumentException("Can't use internal rule set");
        }
        return this.format(number, this.findRuleSet(ruleSet));
    }
    
    public String format(final long number, final String ruleSet) throws IllegalArgumentException {
        if (ruleSet.startsWith("%%")) {
            throw new IllegalArgumentException("Can't use internal rule set");
        }
        return this.format(number, this.findRuleSet(ruleSet));
    }
    
    @Override
    public StringBuffer format(final double number, final StringBuffer toAppendTo, final FieldPosition ignore) {
        toAppendTo.append(this.format(number, this.defaultRuleSet));
        return toAppendTo;
    }
    
    @Override
    public StringBuffer format(final long number, final StringBuffer toAppendTo, final FieldPosition ignore) {
        toAppendTo.append(this.format(number, this.defaultRuleSet));
        return toAppendTo;
    }
    
    @Override
    public StringBuffer format(final BigInteger number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.format(new BigDecimal(number), toAppendTo, pos);
    }
    
    @Override
    public StringBuffer format(final java.math.BigDecimal number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.format(new BigDecimal(number), toAppendTo, pos);
    }
    
    @Override
    public StringBuffer format(final BigDecimal number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.format(number.doubleValue(), toAppendTo, pos);
    }
    
    @Override
    public Number parse(final String text, final ParsePosition parsePosition) {
        final String workingText = text.substring(parsePosition.getIndex());
        final ParsePosition workingPos = new ParsePosition(0);
        Number tempResult = null;
        Number result = 0L;
        final ParsePosition highWaterMark = new ParsePosition(workingPos.getIndex());
        for (int i = this.ruleSets.length - 1; i >= 0; --i) {
            if (this.ruleSets[i].isPublic()) {
                if (this.ruleSets[i].isParseable()) {
                    tempResult = this.ruleSets[i].parse(workingText, workingPos, Double.MAX_VALUE);
                    if (workingPos.getIndex() > highWaterMark.getIndex()) {
                        result = tempResult;
                        highWaterMark.setIndex(workingPos.getIndex());
                    }
                    if (highWaterMark.getIndex() == workingText.length()) {
                        break;
                    }
                    workingPos.setIndex(0);
                }
            }
        }
        parsePosition.setIndex(parsePosition.getIndex() + highWaterMark.getIndex());
        return result;
    }
    
    public void setLenientParseMode(final boolean enabled) {
        this.lenientParse = enabled;
    }
    
    public boolean lenientParseEnabled() {
        return this.lenientParse;
    }
    
    public void setLenientScannerProvider(final RbnfLenientScannerProvider scannerProvider) {
        this.scannerProvider = scannerProvider;
    }
    
    public RbnfLenientScannerProvider getLenientScannerProvider() {
        if (this.scannerProvider == null && this.lenientParse && !this.lookedForScanner) {
            try {
                this.lookedForScanner = true;
                final Class<?> cls = Class.forName("com.ibm.icu.text.RbnfScannerProviderImpl");
                final RbnfLenientScannerProvider provider = (RbnfLenientScannerProvider)cls.newInstance();
                this.setLenientScannerProvider(provider);
            }
            catch (Exception ex) {}
        }
        return this.scannerProvider;
    }
    
    public void setDefaultRuleSet(final String ruleSetName) {
        if (ruleSetName == null) {
            if (this.publicRuleSetNames.length > 0) {
                this.defaultRuleSet = this.findRuleSet(this.publicRuleSetNames[0]);
            }
            else {
                this.defaultRuleSet = null;
                int n = this.ruleSets.length;
                while (--n >= 0) {
                    final String currentName = this.ruleSets[n].getName();
                    if (currentName.equals("%spellout-numbering") || currentName.equals("%digits-ordinal") || currentName.equals("%duration")) {
                        this.defaultRuleSet = this.ruleSets[n];
                        return;
                    }
                }
                n = this.ruleSets.length;
                while (--n >= 0) {
                    if (this.ruleSets[n].isPublic()) {
                        this.defaultRuleSet = this.ruleSets[n];
                        break;
                    }
                }
            }
        }
        else {
            if (ruleSetName.startsWith("%%")) {
                throw new IllegalArgumentException("cannot use private rule set: " + ruleSetName);
            }
            this.defaultRuleSet = this.findRuleSet(ruleSetName);
        }
    }
    
    public String getDefaultRuleSetName() {
        if (this.defaultRuleSet != null && this.defaultRuleSet.isPublic()) {
            return this.defaultRuleSet.getName();
        }
        return "";
    }
    
    public void setDecimalFormatSymbols(final DecimalFormatSymbols newSymbols) {
        if (newSymbols != null) {
            this.decimalFormatSymbols = (DecimalFormatSymbols)newSymbols.clone();
            if (this.decimalFormat != null) {
                this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols);
            }
            for (int i = 0; i < this.ruleSets.length; ++i) {
                this.ruleSets[i].parseRules(this.ruleSetDescriptions[i], this);
            }
        }
    }
    
    NFRuleSet getDefaultRuleSet() {
        return this.defaultRuleSet;
    }
    
    RbnfLenientScanner getLenientScanner() {
        if (this.lenientParse) {
            final RbnfLenientScannerProvider provider = this.getLenientScannerProvider();
            if (provider != null) {
                return provider.get(this.locale, this.lenientParseRules);
            }
        }
        return null;
    }
    
    DecimalFormatSymbols getDecimalFormatSymbols() {
        if (this.decimalFormatSymbols == null) {
            this.decimalFormatSymbols = new DecimalFormatSymbols(this.locale);
        }
        return this.decimalFormatSymbols;
    }
    
    DecimalFormat getDecimalFormat() {
        if (this.decimalFormat == null) {
            this.decimalFormat = (DecimalFormat)NumberFormat.getInstance(this.locale);
            if (this.decimalFormatSymbols != null) {
                this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols);
            }
        }
        return this.decimalFormat;
    }
    
    private String extractSpecial(final StringBuilder description, final String specialName) {
        String result = null;
        final int lp = description.indexOf(specialName);
        if (lp != -1 && (lp == 0 || description.charAt(lp - 1) == ';')) {
            int lpEnd = description.indexOf(";%", lp);
            if (lpEnd == -1) {
                lpEnd = description.length() - 1;
            }
            int lpStart;
            for (lpStart = lp + specialName.length(); lpStart < lpEnd && PatternProps.isWhiteSpace(description.charAt(lpStart)); ++lpStart) {}
            result = description.substring(lpStart, lpEnd);
            description.delete(lp, lpEnd + 1);
        }
        return result;
    }
    
    private void init(final String description, final String[][] localizations) {
        this.initLocalizations(localizations);
        final StringBuilder descBuf = this.stripWhitespace(description);
        this.lenientParseRules = this.extractSpecial(descBuf, "%%lenient-parse:");
        this.postProcessRules = this.extractSpecial(descBuf, "%%post-process:");
        int numRuleSets = 0;
        for (int p = descBuf.indexOf(";%"); p != -1; ++p, p = descBuf.indexOf(";%", p)) {
            ++numRuleSets;
        }
        ++numRuleSets;
        this.ruleSets = new NFRuleSet[numRuleSets];
        this.ruleSetDescriptions = new String[numRuleSets];
        int curRuleSet = 0;
        int start = 0;
        for (int p2 = descBuf.indexOf(";%"); p2 != -1; p2 = descBuf.indexOf(";%", start)) {
            this.ruleSetDescriptions[curRuleSet] = descBuf.substring(start, p2 + 1);
            this.ruleSets[curRuleSet] = new NFRuleSet(this.ruleSetDescriptions, curRuleSet);
            ++curRuleSet;
            start = p2 + 1;
        }
        this.ruleSetDescriptions[curRuleSet] = descBuf.substring(start);
        this.ruleSets[curRuleSet] = new NFRuleSet(this.ruleSetDescriptions, curRuleSet);
        boolean defaultNameFound = false;
        int n = this.ruleSets.length;
        this.defaultRuleSet = this.ruleSets[this.ruleSets.length - 1];
        while (--n >= 0) {
            final String currentName = this.ruleSets[n].getName();
            if (currentName.equals("%spellout-numbering") || currentName.equals("%digits-ordinal") || currentName.equals("%duration")) {
                this.defaultRuleSet = this.ruleSets[n];
                defaultNameFound = true;
                break;
            }
        }
        if (!defaultNameFound) {
            for (int i = this.ruleSets.length - 1; i >= 0; --i) {
                if (!this.ruleSets[i].getName().startsWith("%%")) {
                    this.defaultRuleSet = this.ruleSets[i];
                    break;
                }
            }
        }
        for (int i = 0; i < this.ruleSets.length; ++i) {
            this.ruleSets[i].parseRules(this.ruleSetDescriptions[i], this);
        }
        int publicRuleSetCount = 0;
        for (int j = 0; j < this.ruleSets.length; ++j) {
            if (!this.ruleSets[j].getName().startsWith("%%")) {
                ++publicRuleSetCount;
            }
        }
        final String[] publicRuleSetTemp = new String[publicRuleSetCount];
        publicRuleSetCount = 0;
        for (int k = this.ruleSets.length - 1; k >= 0; --k) {
            if (!this.ruleSets[k].getName().startsWith("%%")) {
                publicRuleSetTemp[publicRuleSetCount++] = this.ruleSets[k].getName();
            }
        }
        if (this.publicRuleSetNames != null) {
            int k = 0;
        Label_0511:
            while (k < this.publicRuleSetNames.length) {
                final String name = this.publicRuleSetNames[k];
                for (int l = 0; l < publicRuleSetTemp.length; ++l) {
                    if (name.equals(publicRuleSetTemp[l])) {
                        ++k;
                        continue Label_0511;
                    }
                }
                throw new IllegalArgumentException("did not find public rule set: " + name);
            }
            this.defaultRuleSet = this.findRuleSet(this.publicRuleSetNames[0]);
        }
        else {
            this.publicRuleSetNames = publicRuleSetTemp;
        }
    }
    
    private void initLocalizations(final String[][] localizations) {
        if (localizations != null) {
            this.publicRuleSetNames = localizations[0].clone();
            final Map<String, String[]> m = new HashMap<String, String[]>();
            for (int i = 1; i < localizations.length; ++i) {
                final String[] data = localizations[i];
                final String loc = data[0];
                final String[] names = new String[data.length - 1];
                if (names.length != this.publicRuleSetNames.length) {
                    throw new IllegalArgumentException("public name length: " + this.publicRuleSetNames.length + " != localized names[" + i + "] length: " + names.length);
                }
                System.arraycopy(data, 1, names, 0, names.length);
                m.put(loc, names);
            }
            if (!m.isEmpty()) {
                this.ruleSetDisplayNames = m;
            }
        }
    }
    
    private StringBuilder stripWhitespace(final String description) {
        final StringBuilder result = new StringBuilder();
        int start = 0;
        while (start != -1 && start < description.length()) {
            while (start < description.length() && PatternProps.isWhiteSpace(description.charAt(start))) {
                ++start;
            }
            if (start < description.length() && description.charAt(start) == ';') {
                ++start;
            }
            else {
                final int p = description.indexOf(59, start);
                if (p == -1) {
                    result.append(description.substring(start));
                    start = -1;
                }
                else if (p < description.length()) {
                    result.append(description.substring(start, p + 1));
                    start = p + 1;
                }
                else {
                    start = -1;
                }
            }
        }
        return result;
    }
    
    private String format(final double number, final NFRuleSet ruleSet) {
        final StringBuffer result = new StringBuffer();
        ruleSet.format(number, result, 0);
        this.postProcess(result, ruleSet);
        return result.toString();
    }
    
    private String format(final long number, final NFRuleSet ruleSet) {
        final StringBuffer result = new StringBuffer();
        ruleSet.format(number, result, 0);
        this.postProcess(result, ruleSet);
        return result.toString();
    }
    
    private void postProcess(final StringBuffer result, final NFRuleSet ruleSet) {
        if (this.postProcessRules != null) {
            if (this.postProcessor == null) {
                int ix = this.postProcessRules.indexOf(";");
                if (ix == -1) {
                    ix = this.postProcessRules.length();
                }
                final String ppClassName = this.postProcessRules.substring(0, ix).trim();
                try {
                    final Class<?> cls = Class.forName(ppClassName);
                    (this.postProcessor = (RBNFPostProcessor)cls.newInstance()).init(this, this.postProcessRules);
                }
                catch (Exception e) {
                    if (RuleBasedNumberFormat.DEBUG) {
                        System.out.println("could not locate " + ppClassName + ", error " + e.getClass().getName() + ", " + e.getMessage());
                    }
                    this.postProcessor = null;
                    this.postProcessRules = null;
                    return;
                }
            }
            this.postProcessor.process(result, ruleSet);
        }
    }
    
    NFRuleSet findRuleSet(final String name) throws IllegalArgumentException {
        for (int i = 0; i < this.ruleSets.length; ++i) {
            if (this.ruleSets[i].getName().equals(name)) {
                return this.ruleSets[i];
            }
        }
        throw new IllegalArgumentException("No rule set named " + name);
    }
    
    static {
        DEBUG = ICUDebug.enabled("rbnf");
        rulenames = new String[] { "SpelloutRules", "OrdinalRules", "DurationRules", "NumberingSystemRules" };
        locnames = new String[] { "SpelloutLocalizations", "OrdinalLocalizations", "DurationLocalizations", "NumberingSystemLocalizations" };
    }
}
