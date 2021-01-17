// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.math;

import com.ibm.icu.lang.UCharacter;
import java.math.BigInteger;
import java.io.Serializable;

public class BigDecimal extends Number implements Serializable, Comparable<BigDecimal>
{
    public static final BigDecimal ZERO;
    public static final BigDecimal ONE;
    public static final BigDecimal TEN;
    public static final int ROUND_CEILING = 2;
    public static final int ROUND_DOWN = 1;
    public static final int ROUND_FLOOR = 3;
    public static final int ROUND_HALF_DOWN = 5;
    public static final int ROUND_HALF_EVEN = 6;
    public static final int ROUND_HALF_UP = 4;
    public static final int ROUND_UNNECESSARY = 7;
    public static final int ROUND_UP = 0;
    private static final byte ispos = 1;
    private static final byte iszero = 0;
    private static final byte isneg = -1;
    private static final int MinExp = -999999999;
    private static final int MaxExp = 999999999;
    private static final int MinArg = -999999999;
    private static final int MaxArg = 999999999;
    private static final MathContext plainMC;
    private static final long serialVersionUID = 8245355804974198832L;
    private static byte[] bytecar;
    private static byte[] bytedig;
    private byte ind;
    private byte form;
    private byte[] mant;
    private int exp;
    
    public BigDecimal(final java.math.BigDecimal bd) {
        this(bd.toString());
    }
    
    public BigDecimal(final BigInteger bi) {
        this(bi.toString(10));
    }
    
    public BigDecimal(final BigInteger bi, final int scale) {
        this(bi.toString(10));
        if (scale < 0) {
            throw new NumberFormatException("Negative scale: " + scale);
        }
        this.exp = -scale;
    }
    
    public BigDecimal(final char[] inchars) {
        this(inchars, 0, inchars.length);
    }
    
    public BigDecimal(final char[] inchars, int offset, int length) {
        this.form = 0;
        int i = 0;
        char si = '\0';
        boolean eneg = false;
        int k = 0;
        int elen = 0;
        int j = 0;
        char sj = '\0';
        int dvalue = 0;
        int mag = 0;
        if (length <= 0) {
            this.bad(inchars);
        }
        this.ind = 1;
        if (inchars[offset] == '-') {
            if (--length == 0) {
                this.bad(inchars);
            }
            this.ind = -1;
            ++offset;
        }
        else if (inchars[offset] == '+') {
            if (--length == 0) {
                this.bad(inchars);
            }
            ++offset;
        }
        boolean exotic = false;
        boolean hadexp = false;
        int d = 0;
        int dotoff = -1;
        int last = -1;
        int $1;
        for ($1 = length, i = offset; $1 > 0; --$1, ++i) {
            si = inchars[i];
            if (si >= '0' && si <= '9') {
                last = i;
                ++d;
            }
            else if (si == '.') {
                if (dotoff >= 0) {
                    this.bad(inchars);
                }
                dotoff = i - offset;
            }
            else {
                if (si == 'e' || si == 'E') {
                    if (i - offset > length - 2) {
                        this.bad(inchars);
                    }
                    eneg = false;
                    if (inchars[i + 1] == '-') {
                        eneg = true;
                        k = i + 2;
                    }
                    else if (inchars[i + 1] == '+') {
                        k = i + 2;
                    }
                    else {
                        k = i + 1;
                    }
                    elen = length - (k - offset);
                    if (elen == 0 | elen > 9) {
                        this.bad(inchars);
                    }
                    for (int $2 = elen, j = k; $2 > 0; --$2, ++j) {
                        sj = inchars[j];
                        if (sj < '0') {
                            this.bad(inchars);
                        }
                        if (sj > '9') {
                            if (!UCharacter.isDigit(sj)) {
                                this.bad(inchars);
                            }
                            dvalue = UCharacter.digit(sj, 10);
                            if (dvalue < 0) {
                                this.bad(inchars);
                            }
                        }
                        else {
                            dvalue = sj - '0';
                        }
                        this.exp = this.exp * 10 + dvalue;
                    }
                    if (eneg) {
                        this.exp = -this.exp;
                    }
                    hadexp = true;
                    break;
                }
                if (!UCharacter.isDigit(si)) {
                    this.bad(inchars);
                }
                exotic = true;
                last = i;
                ++d;
            }
        }
        if (d == 0) {
            this.bad(inchars);
        }
        if (dotoff >= 0) {
            this.exp = this.exp + dotoff - d;
        }
        int $3;
        for ($3 = last - 1, i = offset; i <= $3; ++i) {
            si = inchars[i];
            if (si == '0') {
                ++offset;
                --dotoff;
                --d;
            }
            else if (si == '.') {
                ++offset;
                --dotoff;
            }
            else {
                if (si <= '9') {
                    break;
                }
                if (UCharacter.digit(si, 10) != 0) {
                    break;
                }
                ++offset;
                --dotoff;
                --d;
            }
        }
        this.mant = new byte[d];
        j = offset;
        if (exotic) {
            for (int $4 = d, i = 0; $4 > 0; --$4, ++i) {
                if (i == dotoff) {
                    ++j;
                }
                sj = inchars[j];
                if (sj <= '9') {
                    this.mant[i] = (byte)(sj - '0');
                }
                else {
                    dvalue = UCharacter.digit(sj, 10);
                    if (dvalue < 0) {
                        this.bad(inchars);
                    }
                    this.mant[i] = (byte)dvalue;
                }
                ++j;
            }
        }
        else {
            for (int $5 = d, i = 0; $5 > 0; --$5, ++i) {
                if (i == dotoff) {
                    ++j;
                }
                this.mant[i] = (byte)(inchars[j] - '0');
                ++j;
            }
        }
        if (this.mant[0] == 0) {
            this.ind = 0;
            if (this.exp > 0) {
                this.exp = 0;
            }
            if (hadexp) {
                this.mant = BigDecimal.ZERO.mant;
                this.exp = 0;
            }
        }
        else if (hadexp) {
            this.form = 1;
            mag = this.exp + this.mant.length - 1;
            if (mag < -999999999 | mag > 999999999) {
                this.bad(inchars);
            }
        }
    }
    
    public BigDecimal(final double num) {
        this(new java.math.BigDecimal(num).toString());
    }
    
    public BigDecimal(int num) {
        this.form = 0;
        int i = 0;
        if (num <= 9 && num >= -9) {
            if (num == 0) {
                this.mant = BigDecimal.ZERO.mant;
                this.ind = 0;
            }
            else if (num == 1) {
                this.mant = BigDecimal.ONE.mant;
                this.ind = 1;
            }
            else if (num == -1) {
                this.mant = BigDecimal.ONE.mant;
                this.ind = -1;
            }
            else {
                this.mant = new byte[1];
                if (num > 0) {
                    this.mant[0] = (byte)num;
                    this.ind = 1;
                }
                else {
                    this.mant[0] = (byte)(-num);
                    this.ind = -1;
                }
            }
            return;
        }
        if (num > 0) {
            this.ind = 1;
            num = -num;
        }
        else {
            this.ind = -1;
        }
        int mun = num;
        i = 9;
        while (true) {
            mun /= 10;
            if (mun == 0) {
                break;
            }
            --i;
        }
        this.mant = new byte[10 - i];
        i = 10 - i - 1;
        while (true) {
            this.mant[i] = (byte)(-(byte)(num % 10));
            num /= 10;
            if (num == 0) {
                break;
            }
            --i;
        }
    }
    
    public BigDecimal(long num) {
        this.form = 0;
        int i = 0;
        if (num > 0L) {
            this.ind = 1;
            num = -num;
        }
        else if (num == 0L) {
            this.ind = 0;
        }
        else {
            this.ind = -1;
        }
        long mun = num;
        i = 18;
        while (true) {
            mun /= 10L;
            if (mun == 0L) {
                break;
            }
            --i;
        }
        this.mant = new byte[19 - i];
        i = 19 - i - 1;
        while (true) {
            this.mant[i] = (byte)(-(byte)(num % 10L));
            num /= 10L;
            if (num == 0L) {
                break;
            }
            --i;
        }
    }
    
    public BigDecimal(final String string) {
        this(string.toCharArray(), 0, string.length());
    }
    
    private BigDecimal() {
        this.form = 0;
    }
    
    public BigDecimal abs() {
        return this.abs(BigDecimal.plainMC);
    }
    
    public BigDecimal abs(final MathContext set) {
        if (this.ind == -1) {
            return this.negate(set);
        }
        return this.plus(set);
    }
    
    public BigDecimal add(final BigDecimal rhs) {
        return this.add(rhs, BigDecimal.plainMC);
    }
    
    public BigDecimal add(BigDecimal rhs, final MathContext set) {
        int newlen = 0;
        int tlen = 0;
        int mult = 0;
        byte[] t = null;
        int ia = 0;
        int ib = 0;
        int ea = 0;
        int eb = 0;
        byte ca = 0;
        byte cb = 0;
        if (set.lostDigits) {
            this.checkdigits(rhs, set.digits);
        }
        BigDecimal lhs = this;
        if (lhs.ind == 0 && set.form != 0) {
            return rhs.plus(set);
        }
        if (rhs.ind == 0 && set.form != 0) {
            return lhs.plus(set);
        }
        final int reqdig = set.digits;
        if (reqdig > 0) {
            if (lhs.mant.length > reqdig) {
                lhs = clone(lhs).round(set);
            }
            if (rhs.mant.length > reqdig) {
                rhs = clone(rhs).round(set);
            }
        }
        final BigDecimal res = new BigDecimal();
        byte[] usel = lhs.mant;
        int usellen = lhs.mant.length;
        byte[] user = rhs.mant;
        int userlen = rhs.mant.length;
        if (lhs.exp == rhs.exp) {
            res.exp = lhs.exp;
        }
        else if (lhs.exp > rhs.exp) {
            newlen = usellen + lhs.exp - rhs.exp;
            if (newlen >= userlen + reqdig + 1 && reqdig > 0) {
                res.mant = usel;
                res.exp = lhs.exp;
                res.ind = lhs.ind;
                if (usellen < reqdig) {
                    res.mant = extend(lhs.mant, reqdig);
                    res.exp -= reqdig - usellen;
                }
                return res.finish(set, false);
            }
            res.exp = rhs.exp;
            if (newlen > reqdig + 1 && reqdig > 0) {
                tlen = newlen - reqdig - 1;
                userlen -= tlen;
                res.exp += tlen;
                newlen = reqdig + 1;
            }
            if (newlen > usellen) {
                usellen = newlen;
            }
        }
        else {
            newlen = userlen + rhs.exp - lhs.exp;
            if (newlen >= usellen + reqdig + 1 && reqdig > 0) {
                res.mant = user;
                res.exp = rhs.exp;
                res.ind = rhs.ind;
                if (userlen < reqdig) {
                    res.mant = extend(rhs.mant, reqdig);
                    res.exp -= reqdig - userlen;
                }
                return res.finish(set, false);
            }
            res.exp = lhs.exp;
            if (newlen > reqdig + 1 && reqdig > 0) {
                tlen = newlen - reqdig - 1;
                usellen -= tlen;
                res.exp += tlen;
                newlen = reqdig + 1;
            }
            if (newlen > userlen) {
                userlen = newlen;
            }
        }
        if (lhs.ind == 0) {
            res.ind = 1;
        }
        else {
            res.ind = lhs.ind;
        }
        if (lhs.ind == -1 == (rhs.ind == -1)) {
            mult = 1;
        }
        else {
            mult = -1;
            if (rhs.ind != 0) {
                if (usellen < userlen | lhs.ind == 0) {
                    t = usel;
                    usel = user;
                    user = t;
                    tlen = usellen;
                    usellen = userlen;
                    userlen = tlen;
                    res.ind = (byte)(-res.ind);
                }
                else if (usellen <= userlen) {
                    ia = 0;
                    ib = 0;
                    ea = usel.length - 1;
                    eb = user.length - 1;
                    while (true) {
                        if (ia <= ea) {
                            ca = usel[ia];
                        }
                        else if (ib > eb) {
                            if (set.form != 0) {
                                return BigDecimal.ZERO;
                            }
                            break;
                        }
                        else {
                            ca = 0;
                        }
                        if (ib <= eb) {
                            cb = user[ib];
                        }
                        else {
                            cb = 0;
                        }
                        if (ca != cb) {
                            if (ca < cb) {
                                t = usel;
                                usel = user;
                                user = t;
                                tlen = usellen;
                                usellen = userlen;
                                userlen = tlen;
                                res.ind = (byte)(-res.ind);
                                break;
                            }
                            break;
                        }
                        else {
                            ++ia;
                            ++ib;
                        }
                    }
                }
            }
        }
        res.mant = byteaddsub(usel, usellen, user, userlen, mult, false);
        return res.finish(set, false);
    }
    
    public int compareTo(final BigDecimal rhs) {
        return this.compareTo(rhs, BigDecimal.plainMC);
    }
    
    public int compareTo(final BigDecimal rhs, final MathContext set) {
        int thislength = 0;
        int i = 0;
        if (set.lostDigits) {
            this.checkdigits(rhs, set.digits);
        }
        if (this.ind == rhs.ind & this.exp == rhs.exp) {
            thislength = this.mant.length;
            if (thislength < rhs.mant.length) {
                return (byte)(-this.ind);
            }
            if (thislength > rhs.mant.length) {
                return this.ind;
            }
            if (thislength <= set.digits | set.digits == 0) {
                for (int $6 = thislength, i = 0; $6 > 0; --$6, ++i) {
                    if (this.mant[i] < rhs.mant[i]) {
                        return (byte)(-this.ind);
                    }
                    if (this.mant[i] > rhs.mant[i]) {
                        return this.ind;
                    }
                }
                return 0;
            }
        }
        else {
            if (this.ind < rhs.ind) {
                return -1;
            }
            if (this.ind > rhs.ind) {
                return 1;
            }
        }
        final BigDecimal newrhs = clone(rhs);
        newrhs.ind = (byte)(-newrhs.ind);
        return this.add(newrhs, set).ind;
    }
    
    public BigDecimal divide(final BigDecimal rhs) {
        return this.dodivide('D', rhs, BigDecimal.plainMC, -1);
    }
    
    public BigDecimal divide(final BigDecimal rhs, final int round) {
        final MathContext set = new MathContext(0, 0, false, round);
        return this.dodivide('D', rhs, set, -1);
    }
    
    public BigDecimal divide(final BigDecimal rhs, final int scale, final int round) {
        if (scale < 0) {
            throw new ArithmeticException("Negative scale: " + scale);
        }
        final MathContext set = new MathContext(0, 0, false, round);
        return this.dodivide('D', rhs, set, scale);
    }
    
    public BigDecimal divide(final BigDecimal rhs, final MathContext set) {
        return this.dodivide('D', rhs, set, -1);
    }
    
    public BigDecimal divideInteger(final BigDecimal rhs) {
        return this.dodivide('I', rhs, BigDecimal.plainMC, 0);
    }
    
    public BigDecimal divideInteger(final BigDecimal rhs, final MathContext set) {
        return this.dodivide('I', rhs, set, 0);
    }
    
    public BigDecimal max(final BigDecimal rhs) {
        return this.max(rhs, BigDecimal.plainMC);
    }
    
    public BigDecimal max(final BigDecimal rhs, final MathContext set) {
        if (this.compareTo(rhs, set) >= 0) {
            return this.plus(set);
        }
        return rhs.plus(set);
    }
    
    public BigDecimal min(final BigDecimal rhs) {
        return this.min(rhs, BigDecimal.plainMC);
    }
    
    public BigDecimal min(final BigDecimal rhs, final MathContext set) {
        if (this.compareTo(rhs, set) <= 0) {
            return this.plus(set);
        }
        return rhs.plus(set);
    }
    
    public BigDecimal multiply(final BigDecimal rhs) {
        return this.multiply(rhs, BigDecimal.plainMC);
    }
    
    public BigDecimal multiply(BigDecimal rhs, final MathContext set) {
        byte[] multer = null;
        byte[] multand = null;
        int acclen = 0;
        int n = 0;
        byte mult = 0;
        if (set.lostDigits) {
            this.checkdigits(rhs, set.digits);
        }
        BigDecimal lhs = this;
        int padding = 0;
        final int reqdig = set.digits;
        if (reqdig > 0) {
            if (lhs.mant.length > reqdig) {
                lhs = clone(lhs).round(set);
            }
            if (rhs.mant.length > reqdig) {
                rhs = clone(rhs).round(set);
            }
        }
        else {
            if (lhs.exp > 0) {
                padding += lhs.exp;
            }
            if (rhs.exp > 0) {
                padding += rhs.exp;
            }
        }
        if (lhs.mant.length < rhs.mant.length) {
            multer = lhs.mant;
            multand = rhs.mant;
        }
        else {
            multer = rhs.mant;
            multand = lhs.mant;
        }
        int multandlen = multer.length + multand.length - 1;
        if (multer[0] * multand[0] > 9) {
            acclen = multandlen + 1;
        }
        else {
            acclen = multandlen;
        }
        final BigDecimal res = new BigDecimal();
        byte[] acc = new byte[acclen];
        int $7;
        for ($7 = multer.length, n = 0; $7 > 0; --$7, ++n) {
            mult = multer[n];
            if (mult != 0) {
                acc = byteaddsub(acc, acc.length, multand, multandlen, mult, true);
            }
            --multandlen;
        }
        res.ind = (byte)(lhs.ind * rhs.ind);
        res.exp = lhs.exp + rhs.exp - padding;
        if (padding == 0) {
            res.mant = acc;
        }
        else {
            res.mant = extend(acc, acc.length + padding);
        }
        return res.finish(set, false);
    }
    
    public BigDecimal negate() {
        return this.negate(BigDecimal.plainMC);
    }
    
    public BigDecimal negate(final MathContext set) {
        if (set.lostDigits) {
            this.checkdigits(null, set.digits);
        }
        final BigDecimal res = clone(this);
        res.ind = (byte)(-res.ind);
        return res.finish(set, false);
    }
    
    public BigDecimal plus() {
        return this.plus(BigDecimal.plainMC);
    }
    
    public BigDecimal plus(final MathContext set) {
        if (set.lostDigits) {
            this.checkdigits(null, set.digits);
        }
        if (set.form == 0 && this.form == 0) {
            if (this.mant.length <= set.digits) {
                return this;
            }
            if (set.digits == 0) {
                return this;
            }
        }
        return clone(this).finish(set, false);
    }
    
    public BigDecimal pow(final BigDecimal rhs) {
        return this.pow(rhs, BigDecimal.plainMC);
    }
    
    public BigDecimal pow(final BigDecimal rhs, final MathContext set) {
        int workdigits = 0;
        int L = 0;
        int i = 0;
        if (set.lostDigits) {
            this.checkdigits(rhs, set.digits);
        }
        int n = rhs.intcheck(-999999999, 999999999);
        BigDecimal lhs = this;
        final int reqdig = set.digits;
        if (reqdig == 0) {
            if (rhs.ind == -1) {
                throw new ArithmeticException("Negative power: " + rhs.toString());
            }
            workdigits = 0;
        }
        else {
            if (rhs.mant.length + rhs.exp > reqdig) {
                throw new ArithmeticException("Too many digits: " + rhs.toString());
            }
            if (lhs.mant.length > reqdig) {
                lhs = clone(lhs).round(set);
            }
            L = rhs.mant.length + rhs.exp;
            workdigits = reqdig + L + 1;
        }
        final MathContext workset = new MathContext(workdigits, set.form, false, set.roundingMode);
        BigDecimal res = BigDecimal.ONE;
        if (n == 0) {
            return res;
        }
        if (n < 0) {
            n = -n;
        }
        boolean seenbit = false;
        i = 1;
        while (true) {
            n += n;
            if (n < 0) {
                seenbit = true;
                res = res.multiply(lhs, workset);
            }
            if (i == 31) {
                break;
            }
            if (seenbit) {
                res = res.multiply(res, workset);
            }
            ++i;
        }
        if (rhs.ind < 0) {
            res = BigDecimal.ONE.divide(res, workset);
        }
        return res.finish(set, true);
    }
    
    public BigDecimal remainder(final BigDecimal rhs) {
        return this.dodivide('R', rhs, BigDecimal.plainMC, -1);
    }
    
    public BigDecimal remainder(final BigDecimal rhs, final MathContext set) {
        return this.dodivide('R', rhs, set, -1);
    }
    
    public BigDecimal subtract(final BigDecimal rhs) {
        return this.subtract(rhs, BigDecimal.plainMC);
    }
    
    public BigDecimal subtract(final BigDecimal rhs, final MathContext set) {
        if (set.lostDigits) {
            this.checkdigits(rhs, set.digits);
        }
        final BigDecimal newrhs = clone(rhs);
        newrhs.ind = (byte)(-newrhs.ind);
        return this.add(newrhs, set);
    }
    
    public byte byteValueExact() {
        final int num = this.intValueExact();
        if (num > 127 | num < -128) {
            throw new ArithmeticException("Conversion overflow: " + this.toString());
        }
        return (byte)num;
    }
    
    @Override
    public double doubleValue() {
        return Double.valueOf(this.toString());
    }
    
    @Override
    public boolean equals(final Object obj) {
        int i = 0;
        char[] lca = null;
        char[] rca = null;
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BigDecimal)) {
            return false;
        }
        final BigDecimal rhs = (BigDecimal)obj;
        if (this.ind != rhs.ind) {
            return false;
        }
        if (this.mant.length == rhs.mant.length & this.exp == rhs.exp & this.form == rhs.form) {
            for (int $8 = this.mant.length, i = 0; $8 > 0; --$8, ++i) {
                if (this.mant[i] != rhs.mant[i]) {
                    return false;
                }
            }
        }
        else {
            lca = this.layout();
            rca = rhs.layout();
            if (lca.length != rca.length) {
                return false;
            }
            for (int $9 = lca.length, i = 0; $9 > 0; --$9, ++i) {
                if (lca[i] != rca[i]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public float floatValue() {
        return Float.valueOf(this.toString());
    }
    
    public String format(final int before, final int after) {
        return this.format(before, after, -1, -1, 1, 4);
    }
    
    public String format(final int before, final int after, final int explaces, final int exdigits, int exformint, int exround) {
        int mag = 0;
        int thisafter = 0;
        int lead = 0;
        byte[] newmant = null;
        int chop = 0;
        int need = 0;
        int oldexp = 0;
        int p = 0;
        char[] newa = null;
        int i = 0;
        int places = 0;
        if (before < -1 | before == 0) {
            this.badarg("format", 1, String.valueOf(before));
        }
        if (after < -1) {
            this.badarg("format", 2, String.valueOf(after));
        }
        if (explaces < -1 | explaces == 0) {
            this.badarg("format", 3, String.valueOf(explaces));
        }
        if (exdigits < -1) {
            this.badarg("format", 4, String.valueOf(explaces));
        }
        if (exformint != 1) {
            if (exformint != 2) {
                if (exformint == -1) {
                    exformint = 1;
                }
                else {
                    this.badarg("format", 5, String.valueOf(exformint));
                }
            }
        }
        if (exround != 4) {
            try {
                if (exround == -1) {
                    exround = 4;
                }
                else {
                    new MathContext(9, 1, false, exround);
                }
            }
            catch (IllegalArgumentException $16) {
                this.badarg("format", 6, String.valueOf(exround));
            }
        }
        final BigDecimal num = clone(this);
        if (exdigits == -1) {
            num.form = 0;
        }
        else if (num.ind == 0) {
            num.form = 0;
        }
        else {
            mag = num.exp + num.mant.length;
            if (mag > exdigits) {
                num.form = (byte)exformint;
            }
            else if (mag < -5) {
                num.form = (byte)exformint;
            }
            else {
                num.form = 0;
            }
        }
        if (after >= 0) {
            while (true) {
                if (num.form == 0) {
                    thisafter = -num.exp;
                }
                else if (num.form == 1) {
                    thisafter = num.mant.length - 1;
                }
                else {
                    lead = (num.exp + num.mant.length - 1) % 3;
                    if (lead < 0) {
                        lead += 3;
                    }
                    if (++lead >= num.mant.length) {
                        thisafter = 0;
                    }
                    else {
                        thisafter = num.mant.length - lead;
                    }
                }
                if (thisafter == after) {
                    break;
                }
                if (thisafter < after) {
                    newmant = extend(num.mant, num.mant.length + after - thisafter);
                    num.mant = newmant;
                    num.exp -= after - thisafter;
                    if (num.exp < -999999999) {
                        throw new ArithmeticException("Exponent Overflow: " + num.exp);
                    }
                    break;
                }
                else {
                    chop = thisafter - after;
                    if (chop > num.mant.length) {
                        num.mant = BigDecimal.ZERO.mant;
                        num.ind = 0;
                        num.exp = 0;
                    }
                    else {
                        need = num.mant.length - chop;
                        oldexp = num.exp;
                        num.round(need, exround);
                        if (num.exp - oldexp == chop) {
                            break;
                        }
                        continue;
                    }
                }
            }
        }
        char[] a = num.layout();
        if (before > 0) {
            int $11;
            for ($11 = a.length, p = 0; $11 > 0; --$11, ++p) {
                if (a[p] == '.') {
                    break;
                }
                if (a[p] == 'E') {
                    break;
                }
            }
            if (p > before) {
                this.badarg("format", 1, String.valueOf(before));
            }
            if (p < before) {
                newa = new char[a.length + before - p];
                int $12;
                for ($12 = before - p, i = 0; $12 > 0; --$12, ++i) {
                    newa[i] = ' ';
                }
                System.arraycopy(a, 0, newa, i, a.length);
                a = newa;
            }
        }
        if (explaces > 0) {
            int $13;
            for ($13 = a.length - 1, p = a.length - 1; $13 > 0 && a[p] != 'E'; --$13, --p) {}
            if (p == 0) {
                newa = new char[a.length + explaces + 2];
                System.arraycopy(a, 0, newa, 0, a.length);
                for (int $14 = explaces + 2, i = a.length; $14 > 0; --$14, ++i) {
                    newa[i] = ' ';
                }
                a = newa;
            }
            else {
                places = a.length - p - 2;
                if (places > explaces) {
                    this.badarg("format", 3, String.valueOf(explaces));
                }
                if (places < explaces) {
                    newa = new char[a.length + explaces - places];
                    System.arraycopy(a, 0, newa, 0, p + 2);
                    int $15;
                    for ($15 = explaces - places, i = p + 2; $15 > 0; --$15, ++i) {
                        newa[i] = '0';
                    }
                    System.arraycopy(a, p + 2, newa, i, places);
                    a = newa;
                }
            }
        }
        return new String(a);
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    @Override
    public int intValue() {
        return this.toBigInteger().intValue();
    }
    
    public int intValueExact() {
        int useexp = 0;
        int i = 0;
        int topdig = 0;
        if (this.ind == 0) {
            return 0;
        }
        int lodigit = this.mant.length - 1;
        if (this.exp < 0) {
            lodigit += this.exp;
            if (!allzero(this.mant, lodigit + 1)) {
                throw new ArithmeticException("Decimal part non-zero: " + this.toString());
            }
            if (lodigit < 0) {
                return 0;
            }
            useexp = 0;
        }
        else {
            if (this.exp + lodigit > 9) {
                throw new ArithmeticException("Conversion overflow: " + this.toString());
            }
            useexp = this.exp;
        }
        int result = 0;
        int $16;
        for ($16 = lodigit + useexp, i = 0; i <= $16; ++i) {
            result *= 10;
            if (i <= lodigit) {
                result += this.mant[i];
            }
        }
        if (lodigit + useexp == 9) {
            topdig = result / 1000000000;
            if (topdig != this.mant[0]) {
                if (result == Integer.MIN_VALUE && this.ind == -1 && this.mant[0] == 2) {
                    return result;
                }
                throw new ArithmeticException("Conversion overflow: " + this.toString());
            }
        }
        if (this.ind == 1) {
            return result;
        }
        return -result;
    }
    
    @Override
    public long longValue() {
        return this.toBigInteger().longValue();
    }
    
    public long longValueExact() {
        int cstart = 0;
        int useexp = 0;
        int i = 0;
        long topdig = 0L;
        if (this.ind == 0) {
            return 0L;
        }
        int lodigit = this.mant.length - 1;
        if (this.exp < 0) {
            lodigit += this.exp;
            if (lodigit < 0) {
                cstart = 0;
            }
            else {
                cstart = lodigit + 1;
            }
            if (!allzero(this.mant, cstart)) {
                throw new ArithmeticException("Decimal part non-zero: " + this.toString());
            }
            if (lodigit < 0) {
                return 0L;
            }
            useexp = 0;
        }
        else {
            if (this.exp + this.mant.length > 18) {
                throw new ArithmeticException("Conversion overflow: " + this.toString());
            }
            useexp = this.exp;
        }
        long result = 0L;
        int $17;
        for ($17 = lodigit + useexp, i = 0; i <= $17; ++i) {
            result *= 10L;
            if (i <= lodigit) {
                result += this.mant[i];
            }
        }
        if (lodigit + useexp == 18) {
            topdig = result / 1000000000000000000L;
            if (topdig != this.mant[0]) {
                if (result == Long.MIN_VALUE && this.ind == -1 && this.mant[0] == 9) {
                    return result;
                }
                throw new ArithmeticException("Conversion overflow: " + this.toString());
            }
        }
        if (this.ind == 1) {
            return result;
        }
        return -result;
    }
    
    public BigDecimal movePointLeft(final int n) {
        final BigDecimal res = clone(this);
        res.exp -= n;
        return res.finish(BigDecimal.plainMC, false);
    }
    
    public BigDecimal movePointRight(final int n) {
        final BigDecimal res = clone(this);
        res.exp += n;
        return res.finish(BigDecimal.plainMC, false);
    }
    
    public int scale() {
        if (this.exp >= 0) {
            return 0;
        }
        return -this.exp;
    }
    
    public BigDecimal setScale(final int scale) {
        return this.setScale(scale, 7);
    }
    
    public BigDecimal setScale(final int scale, final int round) {
        int padding = 0;
        int newlen = 0;
        final int ourscale = this.scale();
        if (ourscale == scale && this.form == 0) {
            return this;
        }
        BigDecimal res = clone(this);
        if (ourscale <= scale) {
            if (ourscale == 0) {
                padding = res.exp + scale;
            }
            else {
                padding = scale - ourscale;
            }
            res.mant = extend(res.mant, res.mant.length + padding);
            res.exp = -scale;
        }
        else {
            if (scale < 0) {
                throw new ArithmeticException("Negative scale: " + scale);
            }
            newlen = res.mant.length - (ourscale - scale);
            res = res.round(newlen, round);
            if (res.exp != -scale) {
                res.mant = extend(res.mant, res.mant.length + 1);
                --res.exp;
            }
        }
        res.form = 0;
        return res;
    }
    
    public short shortValueExact() {
        final int num = this.intValueExact();
        if (num > 32767 | num < -32768) {
            throw new ArithmeticException("Conversion overflow: " + this.toString());
        }
        return (short)num;
    }
    
    public int signum() {
        return this.ind;
    }
    
    public java.math.BigDecimal toBigDecimal() {
        return new java.math.BigDecimal(this.unscaledValue(), this.scale());
    }
    
    public BigInteger toBigInteger() {
        BigDecimal res = null;
        int newlen = 0;
        byte[] newmant = null;
        if (this.exp >= 0 & this.form == 0) {
            res = this;
        }
        else if (this.exp >= 0) {
            res = clone(this);
            res.form = 0;
        }
        else if (-this.exp >= this.mant.length) {
            res = BigDecimal.ZERO;
        }
        else {
            res = clone(this);
            newlen = res.mant.length + res.exp;
            newmant = new byte[newlen];
            System.arraycopy(res.mant, 0, newmant, 0, newlen);
            res.mant = newmant;
            res.form = 0;
            res.exp = 0;
        }
        return new BigInteger(new String(res.layout()));
    }
    
    public BigInteger toBigIntegerExact() {
        if (this.exp < 0 && !allzero(this.mant, this.mant.length + this.exp)) {
            throw new ArithmeticException("Decimal part non-zero: " + this.toString());
        }
        return this.toBigInteger();
    }
    
    public char[] toCharArray() {
        return this.layout();
    }
    
    @Override
    public String toString() {
        return new String(this.layout());
    }
    
    public BigInteger unscaledValue() {
        BigDecimal res = null;
        if (this.exp >= 0) {
            res = this;
        }
        else {
            res = clone(this);
            res.exp = 0;
        }
        return res.toBigInteger();
    }
    
    public static BigDecimal valueOf(final double dub) {
        return new BigDecimal(new Double(dub).toString());
    }
    
    public static BigDecimal valueOf(final long lint) {
        return valueOf(lint, 0);
    }
    
    public static BigDecimal valueOf(final long lint, final int scale) {
        BigDecimal res = null;
        if (lint == 0L) {
            res = BigDecimal.ZERO;
        }
        else if (lint == 1L) {
            res = BigDecimal.ONE;
        }
        else if (lint == 10L) {
            res = BigDecimal.TEN;
        }
        else {
            res = new BigDecimal(lint);
        }
        if (scale == 0) {
            return res;
        }
        if (scale < 0) {
            throw new NumberFormatException("Negative scale: " + scale);
        }
        res = clone(res);
        res.exp = -scale;
        return res;
    }
    
    private char[] layout() {
        int i = 0;
        StringBuilder sb = null;
        int euse = 0;
        int sig = 0;
        char csign = '\0';
        char[] rec = null;
        int len = 0;
        final char[] cmant = new char[this.mant.length];
        int $18;
        for ($18 = this.mant.length, i = 0; $18 > 0; --$18, ++i) {
            cmant[i] = (char)(this.mant[i] + 48);
        }
        if (this.form != 0) {
            sb = new StringBuilder(cmant.length + 15);
            if (this.ind == -1) {
                sb.append('-');
            }
            euse = this.exp + cmant.length - 1;
            if (this.form == 1) {
                sb.append(cmant[0]);
                if (cmant.length > 1) {
                    sb.append('.').append(cmant, 1, cmant.length - 1);
                }
            }
            else {
                sig = euse % 3;
                if (sig < 0) {
                    sig += 3;
                }
                euse -= sig;
                if (++sig >= cmant.length) {
                    sb.append(cmant, 0, cmant.length);
                    for (int $19 = sig - cmant.length; $19 > 0; --$19) {
                        sb.append('0');
                    }
                }
                else {
                    sb.append(cmant, 0, sig).append('.').append(cmant, sig, cmant.length - sig);
                }
            }
            if (euse != 0) {
                if (euse < 0) {
                    csign = '-';
                    euse = -euse;
                }
                else {
                    csign = '+';
                }
                sb.append('E').append(csign).append(euse);
            }
            rec = new char[sb.length()];
            final int srcEnd = sb.length();
            if (0 != srcEnd) {
                sb.getChars(0, srcEnd, rec, 0);
            }
            return rec;
        }
        if (this.exp == 0) {
            if (this.ind >= 0) {
                return cmant;
            }
            rec = new char[cmant.length + 1];
            rec[0] = '-';
            System.arraycopy(cmant, 0, rec, 1, cmant.length);
            return rec;
        }
        else {
            final int needsign = (this.ind == -1) ? 1 : 0;
            final int mag = this.exp + cmant.length;
            if (mag < 1) {
                len = needsign + 2 - this.exp;
                rec = new char[len];
                if (needsign != 0) {
                    rec[0] = '-';
                }
                rec[needsign] = '0';
                rec[needsign + 1] = '.';
                for (int $20 = -mag, i = needsign + 2; $20 > 0; --$20, ++i) {
                    rec[i] = '0';
                }
                System.arraycopy(cmant, 0, rec, needsign + 2 - mag, cmant.length);
                return rec;
            }
            if (mag > cmant.length) {
                len = needsign + mag;
                rec = new char[len];
                if (needsign != 0) {
                    rec[0] = '-';
                }
                System.arraycopy(cmant, 0, rec, needsign, cmant.length);
                for (int $21 = mag - cmant.length, i = needsign + cmant.length; $21 > 0; --$21, ++i) {
                    rec[i] = '0';
                }
                return rec;
            }
            len = needsign + 1 + cmant.length;
            rec = new char[len];
            if (needsign != 0) {
                rec[0] = '-';
            }
            System.arraycopy(cmant, 0, rec, needsign, mag);
            rec[needsign + mag] = '.';
            System.arraycopy(cmant, mag, rec, needsign + mag + 1, cmant.length - mag);
            return rec;
        }
    }
    
    private int intcheck(final int min, final int max) {
        final int i = this.intValueExact();
        if (i < min | i > max) {
            throw new ArithmeticException("Conversion overflow: " + i);
        }
        return i;
    }
    
    private BigDecimal dodivide(final char code, BigDecimal rhs, final MathContext set, int scale) {
        int thisdigit = 0;
        int i = 0;
        byte v2 = 0;
        int ba = 0;
        int mult = 0;
        int start = 0;
        int padding = 0;
        int d = 0;
        byte[] newvar1 = null;
        byte lasthave = 0;
        int actdig = 0;
        byte[] newmant = null;
        if (set.lostDigits) {
            this.checkdigits(rhs, set.digits);
        }
        BigDecimal lhs = this;
        if (rhs.ind == 0) {
            throw new ArithmeticException("Divide by 0");
        }
        if (lhs.ind == 0) {
            if (set.form != 0) {
                return BigDecimal.ZERO;
            }
            if (scale == -1) {
                return lhs;
            }
            return lhs.setScale(scale);
        }
        else {
            int reqdig = set.digits;
            if (reqdig > 0) {
                if (lhs.mant.length > reqdig) {
                    lhs = clone(lhs).round(set);
                }
                if (rhs.mant.length > reqdig) {
                    rhs = clone(rhs).round(set);
                }
            }
            else {
                if (scale == -1) {
                    scale = lhs.scale();
                }
                reqdig = lhs.mant.length;
                if (scale != -lhs.exp) {
                    reqdig = reqdig + scale + lhs.exp;
                }
                reqdig = reqdig - (rhs.mant.length - 1) - rhs.exp;
                if (reqdig < lhs.mant.length) {
                    reqdig = lhs.mant.length;
                }
                if (reqdig < rhs.mant.length) {
                    reqdig = rhs.mant.length;
                }
            }
            final int newexp = lhs.exp - rhs.exp + lhs.mant.length - rhs.mant.length;
            if (newexp < 0 && code != 'D') {
                if (code == 'I') {
                    return BigDecimal.ZERO;
                }
                return clone(lhs).finish(set, false);
            }
            else {
                final BigDecimal res = new BigDecimal();
                res.ind = (byte)(lhs.ind * rhs.ind);
                res.exp = newexp;
                res.mant = new byte[reqdig + 1];
                final int newlen = reqdig + reqdig + 1;
                byte[] var1 = extend(lhs.mant, newlen);
                int var1len = newlen;
                final byte[] var2 = rhs.mant;
                int var2len = newlen;
                int b2b = var2[0] * 10 + 1;
                if (var2.length > 1) {
                    b2b += var2[1];
                }
                int have = 0;
            Label_0683_Outer:
                while (true) {
                    thisdigit = 0;
                Label_0683:
                    while (true) {
                        while (var1len >= var2len) {
                            Label_0572: {
                                if (var1len == var2len) {
                                    for (int $22 = var1len, i = 0; $22 > 0; --$22, ++i) {
                                        if (i < var2.length) {
                                            v2 = var2[i];
                                        }
                                        else {
                                            v2 = 0;
                                        }
                                        if (var1[i] < v2) {
                                            break Label_0683;
                                        }
                                        if (var1[i] > v2) {
                                            ba = var1[0];
                                            break Label_0572;
                                        }
                                    }
                                    Label_0514: {
                                        break Label_0514;
                                        if (have != 0 | thisdigit != 0) {
                                            res.mant[have] = (byte)thisdigit;
                                            if (++have == reqdig + 1) {
                                                break Label_0683_Outer;
                                            }
                                            if (var1[0] == 0) {
                                                break Label_0683_Outer;
                                            }
                                        }
                                        if (scale >= 0 && -res.exp > scale) {
                                            break Label_0683_Outer;
                                        }
                                        if (code != 'D' && res.exp <= 0) {
                                            break Label_0683_Outer;
                                        }
                                        --res.exp;
                                        --var2len;
                                        continue Label_0683_Outer;
                                    }
                                    ++thisdigit;
                                    res.mant[have] = (byte)thisdigit;
                                    ++have;
                                    var1[0] = 0;
                                    break Label_0683_Outer;
                                }
                                ba = var1[0] * 10;
                                if (var1len > 1) {
                                    ba += var1[1];
                                }
                            }
                            mult = ba * 10 / b2b;
                            if (mult == 0) {
                                mult = 1;
                            }
                            thisdigit += mult;
                            var1 = byteaddsub(var1, var1len, var2, var2len, -mult, true);
                            if (var1[0] != 0) {
                                continue Label_0683_Outer;
                            }
                            int $23;
                            for ($23 = var1len - 2, start = 0; start <= $23 && var1[start] == 0; ++start) {
                                --var1len;
                            }
                            if (start == 0) {
                                continue Label_0683_Outer;
                            }
                            System.arraycopy(var1, start, var1, 0, var1len);
                        }
                        continue Label_0683;
                    }
                }
                if (have == 0) {
                    have = 1;
                }
                if (code == 'I' | code == 'R') {
                    if (have + res.exp > reqdig) {
                        throw new ArithmeticException("Integer overflow");
                    }
                    if (code == 'R') {
                        if (res.mant[0] == 0) {
                            return clone(lhs).finish(set, false);
                        }
                        if (var1[0] == 0) {
                            return BigDecimal.ZERO;
                        }
                        res.ind = lhs.ind;
                        padding = reqdig + reqdig + 1 - lhs.mant.length;
                        res.exp = res.exp - padding + lhs.exp;
                        d = var1len;
                        for (i = d - 1; i >= 1; --i) {
                            if (!(res.exp < lhs.exp & res.exp < rhs.exp)) {
                                break;
                            }
                            if (var1[i] != 0) {
                                break;
                            }
                            --d;
                            ++res.exp;
                        }
                        if (d < var1.length) {
                            newvar1 = new byte[d];
                            System.arraycopy(var1, 0, newvar1, 0, d);
                            var1 = newvar1;
                        }
                        res.mant = var1;
                        return res.finish(set, false);
                    }
                }
                else if (var1[0] != 0) {
                    lasthave = res.mant[have - 1];
                    if (lasthave % 5 == 0) {
                        res.mant[have - 1] = (byte)(lasthave + 1);
                    }
                }
                if (scale >= 0) {
                    if (have != res.mant.length) {
                        res.exp -= res.mant.length - have;
                    }
                    actdig = res.mant.length - (-res.exp - scale);
                    res.round(actdig, set.roundingMode);
                    if (res.exp != -scale) {
                        res.mant = extend(res.mant, res.mant.length + 1);
                        --res.exp;
                    }
                    return res.finish(set, true);
                }
                if (have == res.mant.length) {
                    res.round(set);
                    have = reqdig;
                }
                else {
                    if (res.mant[0] == 0) {
                        return BigDecimal.ZERO;
                    }
                    newmant = new byte[have];
                    System.arraycopy(res.mant, 0, newmant, 0, have);
                    res.mant = newmant;
                }
                return res.finish(set, true);
            }
        }
    }
    
    private void bad(final char[] s) {
        throw new NumberFormatException("Not a number: " + String.valueOf(s));
    }
    
    private void badarg(final String name, final int pos, final String value) {
        throw new IllegalArgumentException("Bad argument " + pos + " " + "to" + " " + name + ":" + " " + value);
    }
    
    private static final byte[] extend(final byte[] inarr, final int newlen) {
        if (inarr.length == newlen) {
            return inarr;
        }
        final byte[] newarr = new byte[newlen];
        System.arraycopy(inarr, 0, newarr, 0, inarr.length);
        return newarr;
    }
    
    private static final byte[] byteaddsub(final byte[] a, final int avlen, final byte[] b, final int bvlen, final int m, final boolean reuse) {
        int op = 0;
        int dp90 = 0;
        int i = 0;
        final int alength = a.length;
        final int blength = b.length;
        int ap = avlen - 1;
        int maxarr;
        int bp = maxarr = bvlen - 1;
        if (maxarr < ap) {
            maxarr = ap;
        }
        byte[] reb = null;
        if (reuse && maxarr + 1 == alength) {
            reb = a;
        }
        if (reb == null) {
            reb = new byte[maxarr + 1];
        }
        boolean quickm = false;
        if (m == 1) {
            quickm = true;
        }
        else if (m == -1) {
            quickm = true;
        }
        int digit = 0;
        for (op = maxarr; op >= 0; --op) {
            if (ap >= 0) {
                if (ap < alength) {
                    digit += a[ap];
                }
                --ap;
            }
            if (bp >= 0) {
                if (bp < blength) {
                    if (quickm) {
                        if (m > 0) {
                            digit += b[bp];
                        }
                        else {
                            digit -= b[bp];
                        }
                    }
                    else {
                        digit += b[bp] * m;
                    }
                }
                --bp;
            }
            if (digit < 10 && digit >= 0) {
                reb[op] = (byte)digit;
                digit = 0;
            }
            else {
                dp90 = digit + 90;
                reb[op] = BigDecimal.bytedig[dp90];
                digit = BigDecimal.bytecar[dp90];
            }
        }
        if (digit == 0) {
            return reb;
        }
        byte[] newarr = null;
        if (reuse && maxarr + 2 == a.length) {
            newarr = a;
        }
        if (newarr == null) {
            newarr = new byte[maxarr + 2];
        }
        newarr[0] = (byte)digit;
        if (maxarr < 10) {
            for (int $24 = maxarr + 1, i = 0; $24 > 0; --$24, ++i) {
                newarr[i + 1] = reb[i];
            }
        }
        else {
            System.arraycopy(reb, 0, newarr, 1, maxarr + 1);
        }
        return newarr;
    }
    
    private static final byte[] diginit() {
        int op = 0;
        int digit = 0;
        final byte[] work = new byte[190];
        for (op = 0; op <= 189; ++op) {
            digit = op - 90;
            if (digit >= 0) {
                work[op] = (byte)(digit % 10);
                BigDecimal.bytecar[op] = (byte)(digit / 10);
            }
            else {
                digit += 100;
                work[op] = (byte)(digit % 10);
                BigDecimal.bytecar[op] = (byte)(digit / 10 - 10);
            }
        }
        return work;
    }
    
    private static final BigDecimal clone(final BigDecimal dec) {
        final BigDecimal copy = new BigDecimal();
        copy.ind = dec.ind;
        copy.exp = dec.exp;
        copy.form = dec.form;
        copy.mant = dec.mant;
        return copy;
    }
    
    private void checkdigits(final BigDecimal rhs, final int dig) {
        if (dig == 0) {
            return;
        }
        if (this.mant.length > dig && !allzero(this.mant, dig)) {
            throw new ArithmeticException("Too many digits: " + this.toString());
        }
        if (rhs == null) {
            return;
        }
        if (rhs.mant.length > dig && !allzero(rhs.mant, dig)) {
            throw new ArithmeticException("Too many digits: " + rhs.toString());
        }
    }
    
    private BigDecimal round(final MathContext set) {
        return this.round(set.digits, set.roundingMode);
    }
    
    private BigDecimal round(final int len, final int mode) {
        boolean reuse = false;
        byte first = 0;
        byte[] newmant = null;
        final int adjust = this.mant.length - len;
        if (adjust <= 0) {
            return this;
        }
        this.exp += adjust;
        final int sign = this.ind;
        final byte[] oldmant = this.mant;
        if (len > 0) {
            System.arraycopy(oldmant, 0, this.mant = new byte[len], 0, len);
            reuse = true;
            first = oldmant[len];
        }
        else {
            this.mant = BigDecimal.ZERO.mant;
            this.ind = 0;
            reuse = false;
            if (len == 0) {
                first = oldmant[0];
            }
            else {
                first = 0;
            }
        }
        int increment = 0;
        if (mode == 4) {
            if (first >= 5) {
                increment = sign;
            }
        }
        else if (mode == 7) {
            if (!allzero(oldmant, len)) {
                throw new ArithmeticException("Rounding necessary");
            }
        }
        else if (mode == 5) {
            if (first > 5) {
                increment = sign;
            }
            else if (first == 5 && !allzero(oldmant, len + 1)) {
                increment = sign;
            }
        }
        else if (mode == 6) {
            if (first > 5) {
                increment = sign;
            }
            else if (first == 5) {
                if (!allzero(oldmant, len + 1)) {
                    increment = sign;
                }
                else if (this.mant[this.mant.length - 1] % 2 != 0) {
                    increment = sign;
                }
            }
        }
        else if (mode != 1) {
            if (mode == 0) {
                if (!allzero(oldmant, len)) {
                    increment = sign;
                }
            }
            else if (mode == 2) {
                if (sign > 0 && !allzero(oldmant, len)) {
                    increment = sign;
                }
            }
            else {
                if (mode != 3) {
                    throw new IllegalArgumentException("Bad round value: " + mode);
                }
                if (sign < 0 && !allzero(oldmant, len)) {
                    increment = sign;
                }
            }
        }
        if (increment != 0) {
            if (this.ind == 0) {
                this.mant = BigDecimal.ONE.mant;
                this.ind = (byte)increment;
            }
            else {
                if (this.ind == -1) {
                    increment = -increment;
                }
                newmant = byteaddsub(this.mant, this.mant.length, BigDecimal.ONE.mant, 1, increment, reuse);
                if (newmant.length > this.mant.length) {
                    ++this.exp;
                    System.arraycopy(newmant, 0, this.mant, 0, this.mant.length);
                }
                else {
                    this.mant = newmant;
                }
            }
        }
        if (this.exp > 999999999) {
            throw new ArithmeticException("Exponent Overflow: " + this.exp);
        }
        return this;
    }
    
    private static final boolean allzero(final byte[] array, int start) {
        int i = 0;
        if (start < 0) {
            start = 0;
        }
        int $25;
        for ($25 = array.length - 1, i = start; i <= $25; ++i) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    private BigDecimal finish(final MathContext set, final boolean strip) {
        int d = 0;
        int i = 0;
        byte[] newmant = null;
        int mag = 0;
        int sig = 0;
        if (set.digits != 0 && this.mant.length > set.digits) {
            this.round(set);
        }
        if (strip && set.form != 0) {
            d = this.mant.length;
            for (i = d - 1; i >= 1 && this.mant[i] == 0; --i) {
                --d;
                ++this.exp;
            }
            if (d < this.mant.length) {
                newmant = new byte[d];
                System.arraycopy(this.mant, 0, newmant, 0, d);
                this.mant = newmant;
            }
        }
        this.form = 0;
        int $26;
        for ($26 = this.mant.length, i = 0; $26 > 0; --$26, ++i) {
            if (this.mant[i] != 0) {
                if (i > 0) {
                    newmant = new byte[this.mant.length - i];
                    System.arraycopy(this.mant, i, newmant, 0, this.mant.length - i);
                    this.mant = newmant;
                }
                mag = this.exp + this.mant.length;
                if (mag > 0) {
                    if (mag > set.digits && set.digits != 0) {
                        this.form = (byte)set.form;
                    }
                    if (mag - 1 <= 999999999) {
                        return this;
                    }
                }
                else if (mag < -5) {
                    this.form = (byte)set.form;
                }
                if (--mag < -999999999 | mag > 999999999) {
                    if (this.form == 2) {
                        sig = mag % 3;
                        if (sig < 0) {
                            sig += 3;
                        }
                        mag -= sig;
                        if (mag >= -999999999 && mag <= 999999999) {
                            return this;
                        }
                    }
                    throw new ArithmeticException("Exponent Overflow: " + mag);
                }
                return this;
            }
        }
        this.ind = 0;
        if (set.form != 0) {
            this.exp = 0;
        }
        else if (this.exp > 0) {
            this.exp = 0;
        }
        else if (this.exp < -999999999) {
            throw new ArithmeticException("Exponent Overflow: " + this.exp);
        }
        this.mant = BigDecimal.ZERO.mant;
        return this;
    }
    
    static {
        ZERO = new BigDecimal(0L);
        ONE = new BigDecimal(1L);
        TEN = new BigDecimal(10);
        plainMC = new MathContext(0, 0);
        BigDecimal.bytecar = new byte[190];
        BigDecimal.bytedig = diginit();
    }
}
