// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.utils;

import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;

@Immutable
public class Rfc3492Idn implements Idn
{
    private static final int base = 36;
    private static final int tmin = 1;
    private static final int tmax = 26;
    private static final int skew = 38;
    private static final int damp = 700;
    private static final int initial_bias = 72;
    private static final int initial_n = 128;
    private static final char delimiter = '-';
    private static final String ACE_PREFIX = "xn--";
    
    private int adapt(final int delta, final int numpoints, final boolean firsttime) {
        int d = delta;
        if (firsttime) {
            d /= 700;
        }
        else {
            d /= 2;
        }
        int k;
        for (d += d / numpoints, k = 0; d > 455; d /= 35, k += 36) {}
        return k + 36 * d / (d + 38);
    }
    
    private int digit(final char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 'A';
        }
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }
        if (c >= '0' && c <= '9') {
            return c - '0' + 26;
        }
        throw new IllegalArgumentException("illegal digit: " + c);
    }
    
    public String toUnicode(final String punycode) {
        final StringBuilder unicode = new StringBuilder(punycode.length());
        final StringTokenizer tok = new StringTokenizer(punycode, ".");
        while (tok.hasMoreTokens()) {
            String t = tok.nextToken();
            if (unicode.length() > 0) {
                unicode.append('.');
            }
            if (t.startsWith("xn--")) {
                t = this.decode(t.substring(4));
            }
            unicode.append(t);
        }
        return unicode.toString();
    }
    
    protected String decode(final String s) {
        String input = s;
        int n = 128;
        int i = 0;
        int bias = 72;
        final StringBuilder output = new StringBuilder(input.length());
        final int lastdelim = input.lastIndexOf(45);
        if (lastdelim != -1) {
            output.append(input.subSequence(0, lastdelim));
            input = input.substring(lastdelim + 1);
        }
    Label_0062:
        while (input.length() > 0) {
            final int oldi = i;
            int w = 1;
            int k = 36;
            while (true) {
                while (input.length() != 0) {
                    final char c = input.charAt(0);
                    input = input.substring(1);
                    final int digit = this.digit(c);
                    i += digit * w;
                    int t;
                    if (k <= bias + 1) {
                        t = 1;
                    }
                    else if (k >= bias + 26) {
                        t = 26;
                    }
                    else {
                        t = k - bias;
                    }
                    if (digit < t) {
                        bias = this.adapt(i - oldi, output.length() + 1, oldi == 0);
                        n += i / (output.length() + 1);
                        i %= output.length() + 1;
                        output.insert(i, (char)n);
                        ++i;
                        continue Label_0062;
                    }
                    w *= 36 - t;
                    k += 36;
                }
                continue;
            }
        }
        return output.toString();
    }
}
