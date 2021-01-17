// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.math.BigDecimal;
import java.math.BigInteger;

final class DigitList
{
    public static final int MAX_LONG_DIGITS = 19;
    public static final int DBL_DIG = 17;
    public int decimalAt;
    public int count;
    public byte[] digits;
    private static byte[] LONG_MIN_REP;
    
    DigitList() {
        this.decimalAt = 0;
        this.count = 0;
        this.digits = new byte[19];
    }
    
    private final void ensureCapacity(final int digitCapacity, final int digitsToCopy) {
        if (digitCapacity > this.digits.length) {
            final byte[] newDigits = new byte[digitCapacity * 2];
            System.arraycopy(this.digits, 0, newDigits, 0, digitsToCopy);
            this.digits = newDigits;
        }
    }
    
    boolean isZero() {
        for (int i = 0; i < this.count; ++i) {
            if (this.digits[i] != 48) {
                return false;
            }
        }
        return true;
    }
    
    public void append(final int digit) {
        this.ensureCapacity(this.count + 1, this.count);
        this.digits[this.count++] = (byte)digit;
    }
    
    public byte getDigitValue(final int i) {
        return (byte)(this.digits[i] - 48);
    }
    
    public final double getDouble() {
        if (this.count == 0) {
            return 0.0;
        }
        final StringBuilder temp = new StringBuilder(this.count);
        temp.append('.');
        for (int i = 0; i < this.count; ++i) {
            temp.append((char)this.digits[i]);
        }
        temp.append('E');
        temp.append(Integer.toString(this.decimalAt));
        return Double.valueOf(temp.toString());
    }
    
    public final long getLong() {
        if (this.count == 0) {
            return 0L;
        }
        if (this.isLongMIN_VALUE()) {
            return Long.MIN_VALUE;
        }
        final StringBuilder temp = new StringBuilder(this.count);
        for (int i = 0; i < this.decimalAt; ++i) {
            temp.append((i < this.count) ? ((char)this.digits[i]) : '0');
        }
        return Long.parseLong(temp.toString());
    }
    
    public BigInteger getBigInteger(final boolean isPositive) {
        if (this.isZero()) {
            return BigInteger.valueOf(0L);
        }
        int len = (this.decimalAt > this.count) ? this.decimalAt : this.count;
        if (!isPositive) {
            ++len;
        }
        final char[] text = new char[len];
        int n = 0;
        if (!isPositive) {
            text[0] = '-';
            for (int i = 0; i < this.count; ++i) {
                text[i + 1] = (char)this.digits[i];
            }
            n = this.count + 1;
        }
        else {
            for (int i = 0; i < this.count; ++i) {
                text[i] = (char)this.digits[i];
            }
            n = this.count;
        }
        for (int i = n; i < text.length; ++i) {
            text[i] = '0';
        }
        return new BigInteger(new String(text));
    }
    
    private String getStringRep(final boolean isPositive) {
        if (this.isZero()) {
            return "0";
        }
        final StringBuilder stringRep = new StringBuilder(this.count + 1);
        if (!isPositive) {
            stringRep.append('-');
        }
        int d = this.decimalAt;
        if (d < 0) {
            stringRep.append('.');
            while (d < 0) {
                stringRep.append('0');
                ++d;
            }
            d = -1;
        }
        for (int i = 0; i < this.count; ++i) {
            if (d == i) {
                stringRep.append('.');
            }
            stringRep.append((char)this.digits[i]);
        }
        while (d-- > this.count) {
            stringRep.append('0');
        }
        return stringRep.toString();
    }
    
    public BigDecimal getBigDecimal(final boolean isPositive) {
        if (this.isZero()) {
            return BigDecimal.valueOf(0L);
        }
        final long scale = this.count - (long)this.decimalAt;
        if (scale > 0L) {
            int numDigits = this.count;
            if (scale > 2147483647L) {
                final long numShift = scale - 2147483647L;
                if (numShift >= this.count) {
                    return new BigDecimal(0);
                }
                numDigits -= (int)numShift;
            }
            final StringBuilder significantDigits = new StringBuilder(numDigits + 1);
            if (!isPositive) {
                significantDigits.append('-');
            }
            for (int i = 0; i < numDigits; ++i) {
                significantDigits.append((char)this.digits[i]);
            }
            final BigInteger unscaledVal = new BigInteger(significantDigits.toString());
            return new BigDecimal(unscaledVal, (int)scale);
        }
        return new BigDecimal(this.getStringRep(isPositive));
    }
    
    public com.ibm.icu.math.BigDecimal getBigDecimalICU(final boolean isPositive) {
        if (this.isZero()) {
            return com.ibm.icu.math.BigDecimal.valueOf(0L);
        }
        final long scale = this.count - (long)this.decimalAt;
        if (scale > 0L) {
            int numDigits = this.count;
            if (scale > 2147483647L) {
                final long numShift = scale - 2147483647L;
                if (numShift >= this.count) {
                    return new com.ibm.icu.math.BigDecimal(0);
                }
                numDigits -= (int)numShift;
            }
            final StringBuilder significantDigits = new StringBuilder(numDigits + 1);
            if (!isPositive) {
                significantDigits.append('-');
            }
            for (int i = 0; i < numDigits; ++i) {
                significantDigits.append((char)this.digits[i]);
            }
            final BigInteger unscaledVal = new BigInteger(significantDigits.toString());
            return new com.ibm.icu.math.BigDecimal(unscaledVal, (int)scale);
        }
        return new com.ibm.icu.math.BigDecimal(this.getStringRep(isPositive));
    }
    
    boolean isIntegral() {
        while (this.count > 0 && this.digits[this.count - 1] == 48) {
            --this.count;
        }
        return this.count == 0 || this.decimalAt >= this.count;
    }
    
    final void set(double source, final int maximumDigits, final boolean fixedPoint) {
        if (source == 0.0) {
            source = 0.0;
        }
        final String rep = Double.toString(source);
        this.set(rep, 19);
        if (fixedPoint) {
            if (-this.decimalAt > maximumDigits) {
                this.count = 0;
                return;
            }
            if (-this.decimalAt == maximumDigits) {
                if (this.shouldRoundUp(0)) {
                    this.count = 1;
                    ++this.decimalAt;
                    this.digits[0] = 49;
                }
                else {
                    this.count = 0;
                }
                return;
            }
        }
        while (this.count > 1 && this.digits[this.count - 1] == 48) {
            --this.count;
        }
        this.round(fixedPoint ? (maximumDigits + this.decimalAt) : ((maximumDigits == 0) ? -1 : maximumDigits));
    }
    
    private void set(final String rep, final int maxCount) {
        this.decimalAt = -1;
        this.count = 0;
        int exponent = 0;
        int leadingZerosAfterDecimal = 0;
        boolean nonZeroDigitSeen = false;
        int i = 0;
        if (rep.charAt(i) == '-') {
            ++i;
        }
        while (i < rep.length()) {
            final char c = rep.charAt(i);
            if (c == '.') {
                this.decimalAt = this.count;
            }
            else {
                if (c == 'e' || c == 'E') {
                    ++i;
                    if (rep.charAt(i) == '+') {
                        ++i;
                    }
                    exponent = Integer.valueOf(rep.substring(i));
                    break;
                }
                if (this.count < maxCount) {
                    if (!nonZeroDigitSeen) {
                        nonZeroDigitSeen = (c != '0');
                        if (!nonZeroDigitSeen && this.decimalAt != -1) {
                            ++leadingZerosAfterDecimal;
                        }
                    }
                    if (nonZeroDigitSeen) {
                        this.ensureCapacity(this.count + 1, this.count);
                        this.digits[this.count++] = (byte)c;
                    }
                }
            }
            ++i;
        }
        if (this.decimalAt == -1) {
            this.decimalAt = this.count;
        }
        this.decimalAt += exponent - leadingZerosAfterDecimal;
    }
    
    private boolean shouldRoundUp(final int maximumDigits) {
        if (maximumDigits < this.count) {
            if (this.digits[maximumDigits] > 53) {
                return true;
            }
            if (this.digits[maximumDigits] == 53) {
                for (int i = maximumDigits + 1; i < this.count; ++i) {
                    if (this.digits[i] != 48) {
                        return true;
                    }
                }
                return maximumDigits > 0 && this.digits[maximumDigits - 1] % 2 != 0;
            }
        }
        return false;
    }
    
    public final void round(int maximumDigits) {
        if (maximumDigits >= 0 && maximumDigits < this.count) {
            Label_0078: {
                if (this.shouldRoundUp(maximumDigits)) {
                    while (true) {
                        while (--maximumDigits >= 0) {
                            final byte[] digits = this.digits;
                            final int n = maximumDigits;
                            ++digits[n];
                            if (this.digits[maximumDigits] <= 57) {
                                ++maximumDigits;
                                break Label_0078;
                            }
                        }
                        this.digits[0] = 49;
                        ++this.decimalAt;
                        maximumDigits = 0;
                        continue;
                    }
                }
            }
            this.count = maximumDigits;
        }
        while (this.count > 1 && this.digits[this.count - 1] == 48) {
            --this.count;
        }
    }
    
    public final void set(final long source) {
        this.set(source, 0);
    }
    
    public final void set(long source, final int maximumDigits) {
        if (source <= 0L) {
            if (source == Long.MIN_VALUE) {
                final int n = 19;
                this.count = n;
                this.decimalAt = n;
                System.arraycopy(DigitList.LONG_MIN_REP, 0, this.digits, 0, this.count);
            }
            else {
                this.count = 0;
                this.decimalAt = 0;
            }
        }
        else {
            int left = 19;
            while (source > 0L) {
                this.digits[--left] = (byte)(48L + source % 10L);
                source /= 10L;
            }
            this.decimalAt = 19 - left;
            int right;
            for (right = 18; this.digits[right] == 48; --right) {}
            this.count = right - left + 1;
            System.arraycopy(this.digits, left, this.digits, 0, this.count);
        }
        if (maximumDigits > 0) {
            this.round(maximumDigits);
        }
    }
    
    public final void set(final BigInteger source, final int maximumDigits) {
        final String stringDigits = source.toString();
        final int length = stringDigits.length();
        this.decimalAt = length;
        this.count = length;
        while (this.count > 1 && stringDigits.charAt(this.count - 1) == '0') {
            --this.count;
        }
        int offset = 0;
        if (stringDigits.charAt(0) == '-') {
            ++offset;
            --this.count;
            --this.decimalAt;
        }
        this.ensureCapacity(this.count, 0);
        for (int i = 0; i < this.count; ++i) {
            this.digits[i] = (byte)stringDigits.charAt(i + offset);
        }
        if (maximumDigits > 0) {
            this.round(maximumDigits);
        }
    }
    
    private void setBigDecimalDigits(final String stringDigits, final int maximumDigits, final boolean fixedPoint) {
        this.set(stringDigits, stringDigits.length());
        this.round(fixedPoint ? (maximumDigits + this.decimalAt) : ((maximumDigits == 0) ? -1 : maximumDigits));
    }
    
    public final void set(final BigDecimal source, final int maximumDigits, final boolean fixedPoint) {
        this.setBigDecimalDigits(source.toString(), maximumDigits, fixedPoint);
    }
    
    public final void set(final com.ibm.icu.math.BigDecimal source, final int maximumDigits, final boolean fixedPoint) {
        this.setBigDecimalDigits(source.toString(), maximumDigits, fixedPoint);
    }
    
    private boolean isLongMIN_VALUE() {
        if (this.decimalAt != this.count || this.count != 19) {
            return false;
        }
        for (int i = 0; i < this.count; ++i) {
            if (this.digits[i] != DigitList.LONG_MIN_REP[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DigitList)) {
            return false;
        }
        final DigitList other = (DigitList)obj;
        if (this.count != other.count || this.decimalAt != other.decimalAt) {
            return false;
        }
        for (int i = 0; i < this.count; ++i) {
            if (this.digits[i] != other.digits[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hashcode = this.decimalAt;
        for (int i = 0; i < this.count; ++i) {
            hashcode = hashcode * 37 + this.digits[i];
        }
        return hashcode;
    }
    
    @Override
    public String toString() {
        if (this.isZero()) {
            return "0";
        }
        final StringBuilder buf = new StringBuilder("0.");
        for (int i = 0; i < this.count; ++i) {
            buf.append((char)this.digits[i]);
        }
        buf.append("x10^");
        buf.append(this.decimalAt);
        return buf.toString();
    }
    
    static {
        final String s = Long.toString(Long.MIN_VALUE);
        DigitList.LONG_MIN_REP = new byte[19];
        for (int i = 0; i < 19; ++i) {
            DigitList.LONG_MIN_REP[i] = (byte)s.charAt(i + 1);
        }
    }
}
