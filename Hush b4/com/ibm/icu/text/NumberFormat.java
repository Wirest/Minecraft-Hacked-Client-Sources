// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.Format;
import java.util.Collections;
import java.util.Set;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import java.text.ParseException;
import java.text.ParsePosition;
import com.ibm.icu.util.CurrencyAmount;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import com.ibm.icu.util.Currency;

public abstract class NumberFormat extends UFormat
{
    public static final int NUMBERSTYLE = 0;
    public static final int CURRENCYSTYLE = 1;
    public static final int PERCENTSTYLE = 2;
    public static final int SCIENTIFICSTYLE = 3;
    public static final int INTEGERSTYLE = 4;
    public static final int ISOCURRENCYSTYLE = 5;
    public static final int PLURALCURRENCYSTYLE = 6;
    public static final int INTEGER_FIELD = 0;
    public static final int FRACTION_FIELD = 1;
    private static NumberFormatShim shim;
    private static final char[] doubleCurrencySign;
    private static final String doubleCurrencyStr;
    private boolean groupingUsed;
    private byte maxIntegerDigits;
    private byte minIntegerDigits;
    private byte maxFractionDigits;
    private byte minFractionDigits;
    private boolean parseIntegerOnly;
    private int maximumIntegerDigits;
    private int minimumIntegerDigits;
    private int maximumFractionDigits;
    private int minimumFractionDigits;
    private Currency currency;
    static final int currentSerialVersion = 1;
    private int serialVersionOnStream;
    private static final long serialVersionUID = -2308460125733713944L;
    private boolean parseStrict;
    
    @Override
    public StringBuffer format(final Object number, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (number instanceof Long) {
            return this.format((long)number, toAppendTo, pos);
        }
        if (number instanceof BigInteger) {
            return this.format((BigInteger)number, toAppendTo, pos);
        }
        if (number instanceof BigDecimal) {
            return this.format((BigDecimal)number, toAppendTo, pos);
        }
        if (number instanceof com.ibm.icu.math.BigDecimal) {
            return this.format((com.ibm.icu.math.BigDecimal)number, toAppendTo, pos);
        }
        if (number instanceof CurrencyAmount) {
            return this.format((CurrencyAmount)number, toAppendTo, pos);
        }
        if (number instanceof Number) {
            return this.format(((Number)number).doubleValue(), toAppendTo, pos);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Number");
    }
    
    @Override
    public final Object parseObject(final String source, final ParsePosition parsePosition) {
        return this.parse(source, parsePosition);
    }
    
    public final String format(final double number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }
    
    public final String format(final long number) {
        final StringBuffer buf = new StringBuffer(19);
        final FieldPosition pos = new FieldPosition(0);
        this.format(number, buf, pos);
        return buf.toString();
    }
    
    public final String format(final BigInteger number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }
    
    public final String format(final BigDecimal number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }
    
    public final String format(final com.ibm.icu.math.BigDecimal number) {
        return this.format(number, new StringBuffer(), new FieldPosition(0)).toString();
    }
    
    public final String format(final CurrencyAmount currAmt) {
        return this.format(currAmt, new StringBuffer(), new FieldPosition(0)).toString();
    }
    
    public abstract StringBuffer format(final double p0, final StringBuffer p1, final FieldPosition p2);
    
    public abstract StringBuffer format(final long p0, final StringBuffer p1, final FieldPosition p2);
    
    public abstract StringBuffer format(final BigInteger p0, final StringBuffer p1, final FieldPosition p2);
    
    public abstract StringBuffer format(final BigDecimal p0, final StringBuffer p1, final FieldPosition p2);
    
    public abstract StringBuffer format(final com.ibm.icu.math.BigDecimal p0, final StringBuffer p1, final FieldPosition p2);
    
    public StringBuffer format(final CurrencyAmount currAmt, final StringBuffer toAppendTo, final FieldPosition pos) {
        final Currency save = this.getCurrency();
        final Currency curr = currAmt.getCurrency();
        final boolean same = curr.equals(save);
        if (!same) {
            this.setCurrency(curr);
        }
        this.format(currAmt.getNumber(), toAppendTo, pos);
        if (!same) {
            this.setCurrency(save);
        }
        return toAppendTo;
    }
    
    public abstract Number parse(final String p0, final ParsePosition p1);
    
    public Number parse(final String text) throws ParseException {
        final ParsePosition parsePosition = new ParsePosition(0);
        final Number result = this.parse(text, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new ParseException("Unparseable number: \"" + text + '\"', parsePosition.getErrorIndex());
        }
        return result;
    }
    
    public CurrencyAmount parseCurrency(final CharSequence text, final ParsePosition pos) {
        final Number n = this.parse(text.toString(), pos);
        return (n == null) ? null : new CurrencyAmount(n, this.getEffectiveCurrency());
    }
    
    public boolean isParseIntegerOnly() {
        return this.parseIntegerOnly;
    }
    
    public void setParseIntegerOnly(final boolean value) {
        this.parseIntegerOnly = value;
    }
    
    public void setParseStrict(final boolean value) {
        this.parseStrict = value;
    }
    
    public boolean isParseStrict() {
        return this.parseStrict;
    }
    
    public static final NumberFormat getInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 0);
    }
    
    public static NumberFormat getInstance(final Locale inLocale) {
        return getInstance(ULocale.forLocale(inLocale), 0);
    }
    
    public static NumberFormat getInstance(final ULocale inLocale) {
        return getInstance(inLocale, 0);
    }
    
    public static final NumberFormat getInstance(final int style) {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), style);
    }
    
    public static NumberFormat getInstance(final Locale inLocale, final int style) {
        return getInstance(ULocale.forLocale(inLocale), style);
    }
    
    public static final NumberFormat getNumberInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 0);
    }
    
    public static NumberFormat getNumberInstance(final Locale inLocale) {
        return getInstance(ULocale.forLocale(inLocale), 0);
    }
    
    public static NumberFormat getNumberInstance(final ULocale inLocale) {
        return getInstance(inLocale, 0);
    }
    
    public static final NumberFormat getIntegerInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 4);
    }
    
    public static NumberFormat getIntegerInstance(final Locale inLocale) {
        return getInstance(ULocale.forLocale(inLocale), 4);
    }
    
    public static NumberFormat getIntegerInstance(final ULocale inLocale) {
        return getInstance(inLocale, 4);
    }
    
    public static final NumberFormat getCurrencyInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 1);
    }
    
    public static NumberFormat getCurrencyInstance(final Locale inLocale) {
        return getInstance(ULocale.forLocale(inLocale), 1);
    }
    
    public static NumberFormat getCurrencyInstance(final ULocale inLocale) {
        return getInstance(inLocale, 1);
    }
    
    public static final NumberFormat getPercentInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 2);
    }
    
    public static NumberFormat getPercentInstance(final Locale inLocale) {
        return getInstance(ULocale.forLocale(inLocale), 2);
    }
    
    public static NumberFormat getPercentInstance(final ULocale inLocale) {
        return getInstance(inLocale, 2);
    }
    
    public static final NumberFormat getScientificInstance() {
        return getInstance(ULocale.getDefault(ULocale.Category.FORMAT), 3);
    }
    
    public static NumberFormat getScientificInstance(final Locale inLocale) {
        return getInstance(ULocale.forLocale(inLocale), 3);
    }
    
    public static NumberFormat getScientificInstance(final ULocale inLocale) {
        return getInstance(inLocale, 3);
    }
    
    private static NumberFormatShim getShim() {
        if (NumberFormat.shim == null) {
            try {
                final Class<?> cls = Class.forName("com.ibm.icu.text.NumberFormatServiceShim");
                NumberFormat.shim = (NumberFormatShim)cls.newInstance();
            }
            catch (MissingResourceException e) {
                throw e;
            }
            catch (Exception e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
        return NumberFormat.shim;
    }
    
    public static Locale[] getAvailableLocales() {
        if (NumberFormat.shim == null) {
            return ICUResourceBundle.getAvailableLocales();
        }
        return getShim().getAvailableLocales();
    }
    
    public static ULocale[] getAvailableULocales() {
        if (NumberFormat.shim == null) {
            return ICUResourceBundle.getAvailableULocales();
        }
        return getShim().getAvailableULocales();
    }
    
    public static Object registerFactory(final NumberFormatFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("factory must not be null");
        }
        return getShim().registerFactory(factory);
    }
    
    public static boolean unregister(final Object registryKey) {
        if (registryKey == null) {
            throw new IllegalArgumentException("registryKey must not be null");
        }
        return NumberFormat.shim != null && NumberFormat.shim.unregister(registryKey);
    }
    
    @Override
    public int hashCode() {
        return this.maximumIntegerDigits * 37 + this.maxFractionDigits;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final NumberFormat other = (NumberFormat)obj;
        return this.maximumIntegerDigits == other.maximumIntegerDigits && this.minimumIntegerDigits == other.minimumIntegerDigits && this.maximumFractionDigits == other.maximumFractionDigits && this.minimumFractionDigits == other.minimumFractionDigits && this.groupingUsed == other.groupingUsed && this.parseIntegerOnly == other.parseIntegerOnly && this.parseStrict == other.parseStrict;
    }
    
    @Override
    public Object clone() {
        final NumberFormat other = (NumberFormat)super.clone();
        return other;
    }
    
    public boolean isGroupingUsed() {
        return this.groupingUsed;
    }
    
    public void setGroupingUsed(final boolean newValue) {
        this.groupingUsed = newValue;
    }
    
    public int getMaximumIntegerDigits() {
        return this.maximumIntegerDigits;
    }
    
    public void setMaximumIntegerDigits(final int newValue) {
        this.maximumIntegerDigits = Math.max(0, newValue);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.minimumIntegerDigits = this.maximumIntegerDigits;
        }
    }
    
    public int getMinimumIntegerDigits() {
        return this.minimumIntegerDigits;
    }
    
    public void setMinimumIntegerDigits(final int newValue) {
        this.minimumIntegerDigits = Math.max(0, newValue);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.maximumIntegerDigits = this.minimumIntegerDigits;
        }
    }
    
    public int getMaximumFractionDigits() {
        return this.maximumFractionDigits;
    }
    
    public void setMaximumFractionDigits(final int newValue) {
        this.maximumFractionDigits = Math.max(0, newValue);
        if (this.maximumFractionDigits < this.minimumFractionDigits) {
            this.minimumFractionDigits = this.maximumFractionDigits;
        }
    }
    
    public int getMinimumFractionDigits() {
        return this.minimumFractionDigits;
    }
    
    public void setMinimumFractionDigits(final int newValue) {
        this.minimumFractionDigits = Math.max(0, newValue);
        if (this.maximumFractionDigits < this.minimumFractionDigits) {
            this.maximumFractionDigits = this.minimumFractionDigits;
        }
    }
    
    public void setCurrency(final Currency theCurrency) {
        this.currency = theCurrency;
    }
    
    public Currency getCurrency() {
        return this.currency;
    }
    
    @Deprecated
    protected Currency getEffectiveCurrency() {
        Currency c = this.getCurrency();
        if (c == null) {
            ULocale uloc = this.getLocale(ULocale.VALID_LOCALE);
            if (uloc == null) {
                uloc = ULocale.getDefault(ULocale.Category.FORMAT);
            }
            c = Currency.getInstance(uloc);
        }
        return c;
    }
    
    public int getRoundingMode() {
        throw new UnsupportedOperationException("getRoundingMode must be implemented by the subclass implementation.");
    }
    
    public void setRoundingMode(final int roundingMode) {
        throw new UnsupportedOperationException("setRoundingMode must be implemented by the subclass implementation.");
    }
    
    public static NumberFormat getInstance(final ULocale desiredLocale, final int choice) {
        if (choice < 0 || choice > 6) {
            throw new IllegalArgumentException("choice should be from NUMBERSTYLE to PLURALCURRENCYSTYLE");
        }
        return getShim().createInstance(desiredLocale, choice);
    }
    
    static NumberFormat createInstance(final ULocale desiredLocale, final int choice) {
        String pattern = getPattern(desiredLocale, choice);
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols(desiredLocale);
        if (choice == 1 || choice == 5) {
            final String temp = symbols.getCurrencyPattern();
            if (temp != null) {
                pattern = temp;
            }
        }
        if (choice == 5) {
            pattern = pattern.replace("¤", NumberFormat.doubleCurrencyStr);
        }
        final NumberingSystem ns = NumberingSystem.getInstance(desiredLocale);
        if (ns == null) {
            return null;
        }
        NumberFormat format;
        if (ns != null && ns.isAlgorithmic()) {
            int desiredRulesType = 4;
            final String nsDesc = ns.getDescription();
            final int firstSlash = nsDesc.indexOf("/");
            final int lastSlash = nsDesc.lastIndexOf("/");
            String nsRuleSetName;
            ULocale nsLoc;
            if (lastSlash > firstSlash) {
                final String nsLocID = nsDesc.substring(0, firstSlash);
                final String nsRuleSetGroup = nsDesc.substring(firstSlash + 1, lastSlash);
                nsRuleSetName = nsDesc.substring(lastSlash + 1);
                nsLoc = new ULocale(nsLocID);
                if (nsRuleSetGroup.equals("SpelloutRules")) {
                    desiredRulesType = 1;
                }
            }
            else {
                nsLoc = desiredLocale;
                nsRuleSetName = nsDesc;
            }
            final RuleBasedNumberFormat r = new RuleBasedNumberFormat(nsLoc, desiredRulesType);
            r.setDefaultRuleSet(nsRuleSetName);
            format = r;
        }
        else {
            final DecimalFormat f = new DecimalFormat(pattern, symbols, choice);
            if (choice == 4) {
                f.setMaximumFractionDigits(0);
                f.setDecimalSeparatorAlwaysShown(false);
                f.setParseIntegerOnly(true);
            }
            format = f;
        }
        final ULocale valid = symbols.getLocale(ULocale.VALID_LOCALE);
        final ULocale actual = symbols.getLocale(ULocale.ACTUAL_LOCALE);
        format.setLocale(valid, actual);
        return format;
    }
    
    @Deprecated
    protected static String getPattern(final Locale forLocale, final int choice) {
        return getPattern(ULocale.forLocale(forLocale), choice);
    }
    
    protected static String getPattern(final ULocale forLocale, final int choice) {
        final int entry = (choice == 4) ? 0 : ((choice == 5 || choice == 6) ? 1 : choice);
        final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", forLocale);
        final String[] numberPatternKeys = { "decimalFormat", "currencyFormat", "percentFormat", "scientificFormat" };
        final NumberingSystem ns = NumberingSystem.getInstance(forLocale);
        String result = null;
        try {
            result = rb.getStringWithFallback("NumberElements/" + ns.getName() + "/patterns/" + numberPatternKeys[entry]);
        }
        catch (MissingResourceException ex) {
            result = rb.getStringWithFallback("NumberElements/latn/patterns/" + numberPatternKeys[entry]);
        }
        return result;
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            this.maximumIntegerDigits = this.maxIntegerDigits;
            this.minimumIntegerDigits = this.minIntegerDigits;
            this.maximumFractionDigits = this.maxFractionDigits;
            this.minimumFractionDigits = this.minFractionDigits;
        }
        if (this.minimumIntegerDigits > this.maximumIntegerDigits || this.minimumFractionDigits > this.maximumFractionDigits || this.minimumIntegerDigits < 0 || this.minimumFractionDigits < 0) {
            throw new InvalidObjectException("Digit count range invalid");
        }
        this.serialVersionOnStream = 1;
    }
    
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        this.maxIntegerDigits = (byte)((this.maximumIntegerDigits > 127) ? 127 : ((byte)this.maximumIntegerDigits));
        this.minIntegerDigits = (byte)((this.minimumIntegerDigits > 127) ? 127 : ((byte)this.minimumIntegerDigits));
        this.maxFractionDigits = (byte)((this.maximumFractionDigits > 127) ? 127 : ((byte)this.maximumFractionDigits));
        this.minFractionDigits = (byte)((this.minimumFractionDigits > 127) ? 127 : ((byte)this.minimumFractionDigits));
        stream.defaultWriteObject();
    }
    
    public NumberFormat() {
        this.groupingUsed = true;
        this.maxIntegerDigits = 40;
        this.minIntegerDigits = 1;
        this.maxFractionDigits = 3;
        this.minFractionDigits = 0;
        this.parseIntegerOnly = false;
        this.maximumIntegerDigits = 40;
        this.minimumIntegerDigits = 1;
        this.maximumFractionDigits = 3;
        this.minimumFractionDigits = 0;
        this.serialVersionOnStream = 1;
    }
    
    static {
        doubleCurrencySign = new char[] { '¤', '¤' };
        doubleCurrencyStr = new String(NumberFormat.doubleCurrencySign);
    }
    
    public abstract static class NumberFormatFactory
    {
        public static final int FORMAT_NUMBER = 0;
        public static final int FORMAT_CURRENCY = 1;
        public static final int FORMAT_PERCENT = 2;
        public static final int FORMAT_SCIENTIFIC = 3;
        public static final int FORMAT_INTEGER = 4;
        
        public boolean visible() {
            return true;
        }
        
        public abstract Set<String> getSupportedLocaleNames();
        
        public NumberFormat createFormat(final ULocale loc, final int formatType) {
            return this.createFormat(loc.toLocale(), formatType);
        }
        
        public NumberFormat createFormat(final Locale loc, final int formatType) {
            return this.createFormat(ULocale.forLocale(loc), formatType);
        }
        
        protected NumberFormatFactory() {
        }
    }
    
    public abstract static class SimpleNumberFormatFactory extends NumberFormatFactory
    {
        final Set<String> localeNames;
        final boolean visible;
        
        public SimpleNumberFormatFactory(final Locale locale) {
            this(locale, true);
        }
        
        public SimpleNumberFormatFactory(final Locale locale, final boolean visible) {
            this.localeNames = Collections.singleton(ULocale.forLocale(locale).getBaseName());
            this.visible = visible;
        }
        
        public SimpleNumberFormatFactory(final ULocale locale) {
            this(locale, true);
        }
        
        public SimpleNumberFormatFactory(final ULocale locale, final boolean visible) {
            this.localeNames = Collections.singleton(locale.getBaseName());
            this.visible = visible;
        }
        
        @Override
        public final boolean visible() {
            return this.visible;
        }
        
        @Override
        public final Set<String> getSupportedLocaleNames() {
            return this.localeNames;
        }
    }
    
    abstract static class NumberFormatShim
    {
        abstract Locale[] getAvailableLocales();
        
        abstract ULocale[] getAvailableULocales();
        
        abstract Object registerFactory(final NumberFormatFactory p0);
        
        abstract boolean unregister(final Object p0);
        
        abstract NumberFormat createInstance(final ULocale p0, final int p1);
    }
    
    public static class Field extends Format.Field
    {
        static final long serialVersionUID = -4516273749929385842L;
        public static final Field SIGN;
        public static final Field INTEGER;
        public static final Field FRACTION;
        public static final Field EXPONENT;
        public static final Field EXPONENT_SIGN;
        public static final Field EXPONENT_SYMBOL;
        public static final Field DECIMAL_SEPARATOR;
        public static final Field GROUPING_SEPARATOR;
        public static final Field PERCENT;
        public static final Field PERMILLE;
        public static final Field CURRENCY;
        
        protected Field(final String fieldName) {
            super(fieldName);
        }
        
        @Override
        protected Object readResolve() throws InvalidObjectException {
            if (this.getName().equals(Field.INTEGER.getName())) {
                return Field.INTEGER;
            }
            if (this.getName().equals(Field.FRACTION.getName())) {
                return Field.FRACTION;
            }
            if (this.getName().equals(Field.EXPONENT.getName())) {
                return Field.EXPONENT;
            }
            if (this.getName().equals(Field.EXPONENT_SIGN.getName())) {
                return Field.EXPONENT_SIGN;
            }
            if (this.getName().equals(Field.EXPONENT_SYMBOL.getName())) {
                return Field.EXPONENT_SYMBOL;
            }
            if (this.getName().equals(Field.CURRENCY.getName())) {
                return Field.CURRENCY;
            }
            if (this.getName().equals(Field.DECIMAL_SEPARATOR.getName())) {
                return Field.DECIMAL_SEPARATOR;
            }
            if (this.getName().equals(Field.GROUPING_SEPARATOR.getName())) {
                return Field.GROUPING_SEPARATOR;
            }
            if (this.getName().equals(Field.PERCENT.getName())) {
                return Field.PERCENT;
            }
            if (this.getName().equals(Field.PERMILLE.getName())) {
                return Field.PERMILLE;
            }
            if (this.getName().equals(Field.SIGN.getName())) {
                return Field.SIGN;
            }
            throw new InvalidObjectException("An invalid object.");
        }
        
        static {
            SIGN = new Field("sign");
            INTEGER = new Field("integer");
            FRACTION = new Field("fraction");
            EXPONENT = new Field("exponent");
            EXPONENT_SIGN = new Field("exponent sign");
            EXPONENT_SYMBOL = new Field("exponent symbol");
            DECIMAL_SEPARATOR = new Field("decimal separator");
            GROUPING_SEPARATOR = new Field("grouping separator");
            PERCENT = new Field("percent");
            PERMILLE = new Field("per mille");
            CURRENCY = new Field("currency");
        }
    }
}
