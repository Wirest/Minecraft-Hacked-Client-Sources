// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.text.ParsePosition;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.util.Iterator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collection;
import com.ibm.icu.util.Currency;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import java.util.Map;

public class CompactDecimalFormat extends DecimalFormat
{
    private static final long serialVersionUID = 4716293295276629682L;
    private static final int POSITIVE_PREFIX = 0;
    private static final int POSITIVE_SUFFIX = 1;
    private static final int AFFIX_SIZE = 2;
    private static final CompactDecimalDataCache cache;
    private final Map<String, Unit[]> units;
    private final long[] divisor;
    private final String[] currencyAffixes;
    private final PluralRules pluralRules;
    
    public static CompactDecimalFormat getInstance(final ULocale locale, final CompactStyle style) {
        return new CompactDecimalFormat(locale, style);
    }
    
    public static CompactDecimalFormat getInstance(final Locale locale, final CompactStyle style) {
        return new CompactDecimalFormat(ULocale.forLocale(locale), style);
    }
    
    CompactDecimalFormat(final ULocale locale, final CompactStyle style) {
        final DecimalFormat format = (DecimalFormat)NumberFormat.getInstance(locale);
        final CompactDecimalDataCache.Data data = this.getData(locale, style);
        this.units = data.units;
        this.divisor = data.divisors;
        this.applyPattern(format.toPattern());
        this.setDecimalFormatSymbols(format.getDecimalFormatSymbols());
        this.setMaximumSignificantDigits(3);
        this.setSignificantDigitsUsed(true);
        if (style == CompactStyle.SHORT) {
            this.setGroupingUsed(false);
        }
        this.pluralRules = PluralRules.forLocale(locale);
        final DecimalFormat currencyFormat = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
        (this.currencyAffixes = new String[2])[0] = currencyFormat.getPositivePrefix();
        this.currencyAffixes[1] = currencyFormat.getPositiveSuffix();
        this.setCurrency(null);
    }
    
    @Deprecated
    public CompactDecimalFormat(final String pattern, final DecimalFormatSymbols formatSymbols, final String[] prefix, final String[] suffix, final long[] divisor, final Collection<String> debugCreationErrors, final CompactStyle style, final String[] currencyAffixes) {
        if (prefix.length < 15) {
            this.recordError(debugCreationErrors, "Must have at least 15 prefix items.");
        }
        if (prefix.length != suffix.length || prefix.length != divisor.length) {
            this.recordError(debugCreationErrors, "Prefix, suffix, and divisor arrays must have the same length.");
        }
        long oldDivisor = 0L;
        final Map<String, Integer> seen = new HashMap<String, Integer>();
        for (int i = 0; i < prefix.length; ++i) {
            if (prefix[i] == null || suffix[i] == null) {
                this.recordError(debugCreationErrors, "Prefix or suffix is null for " + i);
            }
            final int log = (int)Math.log10((double)divisor[i]);
            if (log > i) {
                this.recordError(debugCreationErrors, "Divisor[" + i + "] must be less than or equal to 10^" + i + ", but is: " + divisor[i]);
            }
            final long roundTrip = (long)Math.pow(10.0, log);
            if (roundTrip != divisor[i]) {
                this.recordError(debugCreationErrors, "Divisor[" + i + "] must be a power of 10, but is: " + divisor[i]);
            }
            final String key = prefix[i] + "\uffff" + suffix[i] + "\uffff" + (i - log);
            final Integer old = seen.get(key);
            if (old != null) {
                this.recordError(debugCreationErrors, "Collision between values for " + i + " and " + old + " for [prefix/suffix/index-log(divisor)" + key.replace('\uffff', ';'));
            }
            else {
                seen.put(key, i);
            }
            if (divisor[i] < oldDivisor) {
                this.recordError(debugCreationErrors, "Bad divisor, the divisor for 10E" + i + "(" + divisor[i] + ") is less than the divisor for the divisor for 10E" + (i - 1) + "(" + oldDivisor + ")");
            }
            oldDivisor = divisor[i];
        }
        this.units = this.otherPluralVariant(prefix, suffix);
        this.divisor = divisor.clone();
        this.applyPattern(pattern);
        this.setDecimalFormatSymbols(formatSymbols);
        this.setMaximumSignificantDigits(2);
        this.setSignificantDigitsUsed(true);
        this.setGroupingUsed(false);
        this.currencyAffixes = currencyAffixes.clone();
        this.pluralRules = null;
        this.setCurrency(null);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final CompactDecimalFormat other = (CompactDecimalFormat)obj;
        return this.mapsAreEqual(this.units, other.units) && Arrays.equals(this.divisor, other.divisor) && Arrays.equals(this.currencyAffixes, other.currencyAffixes) && this.pluralRules.equals(other.pluralRules);
    }
    
    private boolean mapsAreEqual(final Map<String, Unit[]> lhs, final Map<String, Unit[]> rhs) {
        if (lhs.size() != rhs.size()) {
            return false;
        }
        for (final Map.Entry<String, Unit[]> entry : lhs.entrySet()) {
            final Unit[] value = rhs.get(entry.getKey());
            if (value == null || !Arrays.equals(entry.getValue(), value)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public StringBuffer format(final double number, final StringBuffer toAppendTo, final FieldPosition pos) {
        final Amount amount = this.toAmount(number);
        final Unit unit = amount.getUnit();
        unit.writePrefix(toAppendTo);
        super.format(amount.getQty(), toAppendTo, pos);
        unit.writeSuffix(toAppendTo);
        return toAppendTo;
    }
    
    @Override
    public AttributedCharacterIterator formatToCharacterIterator(final Object obj) {
        if (!(obj instanceof Number)) {
            throw new IllegalArgumentException();
        }
        final Number number = (Number)obj;
        final Amount amount = this.toAmount(number.doubleValue());
        return super.formatToCharacterIterator(amount.getQty(), amount.getUnit());
    }
    
    @Override
    public StringBuffer format(final long number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.format((double)number, toAppendTo, pos);
    }
    
    @Override
    public StringBuffer format(final BigInteger number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.format(number.doubleValue(), toAppendTo, pos);
    }
    
    @Override
    public StringBuffer format(final BigDecimal number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.format(number.doubleValue(), toAppendTo, pos);
    }
    
    @Override
    public StringBuffer format(final com.ibm.icu.math.BigDecimal number, final StringBuffer toAppendTo, final FieldPosition pos) {
        return this.format(number.doubleValue(), toAppendTo, pos);
    }
    
    @Override
    public Number parse(final String text, final ParsePosition parsePosition) {
        throw new UnsupportedOperationException();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        throw new NotSerializableException();
    }
    
    private void readObject(final ObjectInputStream in) throws IOException {
        throw new NotSerializableException();
    }
    
    private Amount toAmount(double number) {
        final boolean negative = this.isNumberNegative(number);
        number = this.adjustNumberAsInFormatting(number);
        int base = (number <= 1.0) ? 0 : ((int)Math.log10(number));
        if (base >= 15) {
            base = 14;
        }
        number /= this.divisor[base];
        final String pluralVariant = this.getPluralForm(number);
        if (negative) {
            number = -number;
        }
        return new Amount(number, CompactDecimalDataCache.getUnit(this.units, pluralVariant, base));
    }
    
    private void recordError(final Collection<String> creationErrors, final String errorMessage) {
        if (creationErrors == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        creationErrors.add(errorMessage);
    }
    
    private Map<String, Unit[]> otherPluralVariant(final String[] prefix, final String[] suffix) {
        final Map<String, Unit[]> result = new HashMap<String, Unit[]>();
        final Unit[] units = new Unit[prefix.length];
        for (int i = 0; i < units.length; ++i) {
            units[i] = new Unit(prefix[i], suffix[i]);
        }
        result.put("other", units);
        return result;
    }
    
    private String getPluralForm(final double number) {
        if (this.pluralRules == null) {
            return "other";
        }
        return this.pluralRules.select(number);
    }
    
    private CompactDecimalDataCache.Data getData(final ULocale locale, final CompactStyle style) {
        final CompactDecimalDataCache.DataBundle bundle = CompactDecimalFormat.cache.get(locale);
        switch (style) {
            case SHORT: {
                return bundle.shortData;
            }
            case LONG: {
                return bundle.longData;
            }
            default: {
                return bundle.shortData;
            }
        }
    }
    
    static {
        cache = new CompactDecimalDataCache();
    }
    
    public enum CompactStyle
    {
        SHORT, 
        LONG;
    }
    
    private static class Amount
    {
        private final double qty;
        private final Unit unit;
        
        public Amount(final double qty, final Unit unit) {
            this.qty = qty;
            this.unit = unit;
        }
        
        public double getQty() {
            return this.qty;
        }
        
        public Unit getUnit() {
            return this.unit;
        }
    }
}
