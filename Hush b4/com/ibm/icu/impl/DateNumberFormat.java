// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import com.ibm.icu.lang.UCharacter;
import java.text.ParsePosition;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.text.NumberFormat;

public final class DateNumberFormat extends NumberFormat
{
    private static final long serialVersionUID = -6315692826916346953L;
    private char[] digits;
    private char zeroDigit;
    private char minusSign;
    private boolean positiveOnly;
    private transient char[] decimalBuf;
    private static SimpleCache<ULocale, char[]> CACHE;
    private int maxIntDigits;
    private int minIntDigits;
    private static final long PARSE_THRESHOLD = 922337203685477579L;
    
    public DateNumberFormat(final ULocale loc, final String digitString, final String nsName) {
        this.positiveOnly = false;
        this.decimalBuf = new char[20];
        this.initialize(loc, digitString, nsName);
    }
    
    public DateNumberFormat(final ULocale loc, final char zeroDigit, final String nsName) {
        this.positiveOnly = false;
        this.decimalBuf = new char[20];
        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 10; ++i) {
            buf.append((char)(zeroDigit + i));
        }
        this.initialize(loc, buf.toString(), nsName);
    }
    
    private void initialize(final ULocale loc, final String digitString, final String nsName) {
        char[] elems = DateNumberFormat.CACHE.get(loc);
        if (elems == null) {
            final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", loc);
            String minusString;
            try {
                minusString = rb.getStringWithFallback("NumberElements/" + nsName + "/symbols/minusSign");
            }
            catch (MissingResourceException ex) {
                if (!nsName.equals("latn")) {
                    try {
                        minusString = rb.getStringWithFallback("NumberElements/latn/symbols/minusSign");
                    }
                    catch (MissingResourceException ex2) {
                        minusString = "-";
                    }
                }
                else {
                    minusString = "-";
                }
            }
            elems = new char[11];
            for (int i = 0; i < 10; ++i) {
                elems[i] = digitString.charAt(i);
            }
            elems[10] = minusString.charAt(0);
            DateNumberFormat.CACHE.put(loc, elems);
        }
        System.arraycopy(elems, 0, this.digits = new char[10], 0, 10);
        this.zeroDigit = this.digits[0];
        this.minusSign = elems[10];
    }
    
    @Override
    public void setMaximumIntegerDigits(final int newValue) {
        this.maxIntDigits = newValue;
    }
    
    @Override
    public int getMaximumIntegerDigits() {
        return this.maxIntDigits;
    }
    
    @Override
    public void setMinimumIntegerDigits(final int newValue) {
        this.minIntDigits = newValue;
    }
    
    @Override
    public int getMinimumIntegerDigits() {
        return this.minIntDigits;
    }
    
    public void setParsePositiveOnly(final boolean isPositiveOnly) {
        this.positiveOnly = isPositiveOnly;
    }
    
    public char getZeroDigit() {
        return this.zeroDigit;
    }
    
    public void setZeroDigit(final char zero) {
        this.zeroDigit = zero;
        if (this.digits == null) {
            this.digits = new char[10];
        }
        this.digits[0] = zero;
        for (int i = 1; i < 10; ++i) {
            this.digits[i] = (char)(zero + i);
        }
    }
    
    public char[] getDigits() {
        return this.digits;
    }
    
    @Override
    public StringBuffer format(final double number, final StringBuffer toAppendTo, final FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(double, StringBuffer, FieldPostion) is not implemented");
    }
    
    @Override
    public StringBuffer format(long numberL, final StringBuffer toAppendTo, final FieldPosition pos) {
        if (numberL < 0L) {
            toAppendTo.append(this.minusSign);
            numberL = -numberL;
        }
        int number = (int)numberL;
        final int limit = (this.decimalBuf.length < this.maxIntDigits) ? this.decimalBuf.length : this.maxIntDigits;
        int index = limit - 1;
        while (true) {
            this.decimalBuf[index] = this.digits[number % 10];
            number /= 10;
            if (index == 0 || number == 0) {
                break;
            }
            --index;
        }
        for (int padding = this.minIntDigits - (limit - index); padding > 0; --padding) {
            this.decimalBuf[--index] = this.digits[0];
        }
        final int length = limit - index;
        toAppendTo.append(this.decimalBuf, index, length);
        pos.setBeginIndex(0);
        if (pos.getField() == 0) {
            pos.setEndIndex(length);
        }
        else {
            pos.setEndIndex(0);
        }
        return toAppendTo;
    }
    
    @Override
    public StringBuffer format(final BigInteger number, final StringBuffer toAppendTo, final FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(BigInteger, StringBuffer, FieldPostion) is not implemented");
    }
    
    @Override
    public StringBuffer format(final BigDecimal number, final StringBuffer toAppendTo, final FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
    }
    
    @Override
    public StringBuffer format(final com.ibm.icu.math.BigDecimal number, final StringBuffer toAppendTo, final FieldPosition pos) {
        throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
    }
    
    @Override
    public Number parse(final String text, final ParsePosition parsePosition) {
        long num = 0L;
        boolean sawNumber = false;
        boolean negative = false;
        int base;
        int offset;
        for (base = parsePosition.getIndex(), offset = 0; base + offset < text.length(); ++offset) {
            final char ch = text.charAt(base + offset);
            if (offset == 0 && ch == this.minusSign) {
                if (this.positiveOnly) {
                    break;
                }
                negative = true;
            }
            else {
                int digit = ch - this.digits[0];
                if (digit < 0 || 9 < digit) {
                    digit = UCharacter.digit(ch);
                }
                if (digit < 0 || 9 < digit) {
                    for (digit = 0; digit < 10; ++digit) {
                        if (ch == this.digits[digit]) {
                            break;
                        }
                    }
                }
                if (0 > digit || digit > 9 || num >= 922337203685477579L) {
                    break;
                }
                sawNumber = true;
                num = num * 10L + digit;
            }
        }
        Number result = null;
        if (sawNumber) {
            num = (negative ? (num * -1L) : num);
            result = num;
            parsePosition.setIndex(base + offset);
        }
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !super.equals(obj) || !(obj instanceof DateNumberFormat)) {
            return false;
        }
        final DateNumberFormat other = (DateNumberFormat)obj;
        return this.maxIntDigits == other.maxIntDigits && this.minIntDigits == other.minIntDigits && this.minusSign == other.minusSign && this.positiveOnly == other.positiveOnly && Arrays.equals(this.digits, other.digits);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (this.digits == null) {
            this.setZeroDigit(this.zeroDigit);
        }
        this.decimalBuf = new char[20];
    }
    
    static {
        DateNumberFormat.CACHE = new SimpleCache<ULocale, char[]>();
    }
}
