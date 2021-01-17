// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.regex.Pattern;
import com.ibm.icu.text.UnicodeMatcher;
import com.ibm.icu.text.Replaceable;
import java.util.ArrayList;
import java.util.Locale;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.UTF16;
import java.io.IOException;

public final class Utility
{
    private static final char APOSTROPHE = '\'';
    private static final char BACKSLASH = '\\';
    private static final int MAGIC_UNSIGNED = Integer.MIN_VALUE;
    private static final char ESCAPE = '\ua5a5';
    static final byte ESCAPE_BYTE = -91;
    public static String LINE_SEPARATOR;
    static final char[] HEX_DIGIT;
    private static final char[] UNESCAPE_MAP;
    static final char[] DIGITS;
    
    public static final boolean arrayEquals(final Object[] source, final Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof Object[])) {
            return false;
        }
        final Object[] targ = (Object[])target;
        return source.length == targ.length && arrayRegionMatches(source, 0, targ, 0, source.length);
    }
    
    public static final boolean arrayEquals(final int[] source, final Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof int[])) {
            return false;
        }
        final int[] targ = (int[])target;
        return source.length == targ.length && arrayRegionMatches(source, 0, targ, 0, source.length);
    }
    
    public static final boolean arrayEquals(final double[] source, final Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof double[])) {
            return false;
        }
        final double[] targ = (double[])target;
        return source.length == targ.length && arrayRegionMatches(source, 0, targ, 0, source.length);
    }
    
    public static final boolean arrayEquals(final byte[] source, final Object target) {
        if (source == null) {
            return target == null;
        }
        if (!(target instanceof byte[])) {
            return false;
        }
        final byte[] targ = (byte[])target;
        return source.length == targ.length && arrayRegionMatches(source, 0, targ, 0, source.length);
    }
    
    public static final boolean arrayEquals(final Object source, final Object target) {
        if (source == null) {
            return target == null;
        }
        if (source instanceof Object[]) {
            return arrayEquals((Object[])source, target);
        }
        if (source instanceof int[]) {
            return arrayEquals((int[])source, target);
        }
        if (source instanceof double[]) {
            return arrayEquals((double[])source, target);
        }
        if (source instanceof byte[]) {
            return arrayEquals((byte[])source, target);
        }
        return source.equals(target);
    }
    
    public static final boolean arrayRegionMatches(final Object[] source, final int sourceStart, final Object[] target, final int targetStart, final int len) {
        final int sourceEnd = sourceStart + len;
        final int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; ++i) {
            if (!arrayEquals(source[i], target[i + delta])) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean arrayRegionMatches(final char[] source, final int sourceStart, final char[] target, final int targetStart, final int len) {
        final int sourceEnd = sourceStart + len;
        final int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; ++i) {
            if (source[i] != target[i + delta]) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean arrayRegionMatches(final int[] source, final int sourceStart, final int[] target, final int targetStart, final int len) {
        final int sourceEnd = sourceStart + len;
        final int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; ++i) {
            if (source[i] != target[i + delta]) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean arrayRegionMatches(final double[] source, final int sourceStart, final double[] target, final int targetStart, final int len) {
        final int sourceEnd = sourceStart + len;
        final int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; ++i) {
            if (source[i] != target[i + delta]) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean arrayRegionMatches(final byte[] source, final int sourceStart, final byte[] target, final int targetStart, final int len) {
        final int sourceEnd = sourceStart + len;
        final int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; ++i) {
            if (source[i] != target[i + delta]) {
                return false;
            }
        }
        return true;
    }
    
    public static final boolean objectEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : (b != null && a.equals(b));
    }
    
    public static <T extends Comparable<T>> int checkCompare(final T a, final T b) {
        return (a == null) ? ((b == null) ? 0 : -1) : ((b == null) ? 1 : a.compareTo(b));
    }
    
    public static int checkHash(final Object a) {
        return (a == null) ? 0 : a.hashCode();
    }
    
    public static final String arrayToRLEString(final int[] a) {
        final StringBuilder buffer = new StringBuilder();
        appendInt(buffer, a.length);
        int runValue = a[0];
        int runLength = 1;
        for (int i = 1; i < a.length; ++i) {
            final int s = a[i];
            if (s == runValue && runLength < 65535) {
                ++runLength;
            }
            else {
                encodeRun(buffer, runValue, runLength);
                runValue = s;
                runLength = 1;
            }
        }
        encodeRun(buffer, runValue, runLength);
        return buffer.toString();
    }
    
    public static final String arrayToRLEString(final short[] a) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append((char)(a.length >> 16));
        buffer.append((char)a.length);
        short runValue = a[0];
        int runLength = 1;
        for (int i = 1; i < a.length; ++i) {
            final short s = a[i];
            if (s == runValue && runLength < 65535) {
                ++runLength;
            }
            else {
                encodeRun(buffer, runValue, runLength);
                runValue = s;
                runLength = 1;
            }
        }
        encodeRun(buffer, runValue, runLength);
        return buffer.toString();
    }
    
    public static final String arrayToRLEString(final char[] a) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append((char)(a.length >> 16));
        buffer.append((char)a.length);
        char runValue = a[0];
        int runLength = 1;
        for (int i = 1; i < a.length; ++i) {
            final char s = a[i];
            if (s == runValue && runLength < 65535) {
                ++runLength;
            }
            else {
                encodeRun(buffer, (short)runValue, runLength);
                runValue = s;
                runLength = 1;
            }
        }
        encodeRun(buffer, (short)runValue, runLength);
        return buffer.toString();
    }
    
    public static final String arrayToRLEString(final byte[] a) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append((char)(a.length >> 16));
        buffer.append((char)a.length);
        byte runValue = a[0];
        int runLength = 1;
        final byte[] state = new byte[2];
        for (int i = 1; i < a.length; ++i) {
            final byte b = a[i];
            if (b == runValue && runLength < 255) {
                ++runLength;
            }
            else {
                encodeRun(buffer, runValue, runLength, state);
                runValue = b;
                runLength = 1;
            }
        }
        encodeRun(buffer, runValue, runLength, state);
        if (state[0] != 0) {
            appendEncodedByte(buffer, (byte)0, state);
        }
        return buffer.toString();
    }
    
    private static final <T extends Appendable> void encodeRun(final T buffer, final int value, int length) {
        if (length < 4) {
            for (int j = 0; j < length; ++j) {
                if (value == 42405) {
                    appendInt(buffer, value);
                }
                appendInt(buffer, value);
            }
        }
        else {
            if (length == 42405) {
                if (value == 42405) {
                    appendInt(buffer, 42405);
                }
                appendInt(buffer, value);
                --length;
            }
            appendInt(buffer, 42405);
            appendInt(buffer, length);
            appendInt(buffer, value);
        }
    }
    
    private static final <T extends Appendable> void appendInt(final T buffer, final int value) {
        try {
            buffer.append((char)(value >>> 16));
            buffer.append((char)(value & 0xFFFF));
        }
        catch (IOException e) {
            throw new IllegalIcuArgumentException(e);
        }
    }
    
    private static final <T extends Appendable> void encodeRun(final T buffer, final short value, int length) {
        try {
            if (length < 4) {
                for (int j = 0; j < length; ++j) {
                    if (value == 42405) {
                        buffer.append('\ua5a5');
                    }
                    buffer.append((char)value);
                }
            }
            else {
                if (length == 42405) {
                    if (value == 42405) {
                        buffer.append('\ua5a5');
                    }
                    buffer.append((char)value);
                    --length;
                }
                buffer.append('\ua5a5');
                buffer.append((char)length);
                buffer.append((char)value);
            }
        }
        catch (IOException e) {
            throw new IllegalIcuArgumentException(e);
        }
    }
    
    private static final <T extends Appendable> void encodeRun(final T buffer, final byte value, int length, final byte[] state) {
        if (length < 4) {
            for (int j = 0; j < length; ++j) {
                if (value == -91) {
                    appendEncodedByte(buffer, (byte)(-91), state);
                }
                appendEncodedByte(buffer, value, state);
            }
        }
        else {
            if (length == -91) {
                if (value == -91) {
                    appendEncodedByte(buffer, (byte)(-91), state);
                }
                appendEncodedByte(buffer, value, state);
                --length;
            }
            appendEncodedByte(buffer, (byte)(-91), state);
            appendEncodedByte(buffer, (byte)length, state);
            appendEncodedByte(buffer, value, state);
        }
    }
    
    private static final <T extends Appendable> void appendEncodedByte(final T buffer, final byte value, final byte[] state) {
        try {
            if (state[0] != 0) {
                final char c = (char)(state[1] << 8 | (value & 0xFF));
                buffer.append(c);
                state[0] = 0;
            }
            else {
                state[0] = 1;
                state[1] = value;
            }
        }
        catch (IOException e) {
            throw new IllegalIcuArgumentException(e);
        }
    }
    
    public static final int[] RLEStringToIntArray(final String s) {
        int length;
        int[] array;
        int ai;
        int i;
        int maxI;
        int c;
        int runLength;
        int runValue;
        int j;
        for (length = getInt(s, 0), array = new int[length], ai = 0, i = 1, maxI = s.length() / 2; ai < length && i < maxI; array[ai++] = c) {
            c = getInt(s, i++);
            if (c == 42405) {
                c = getInt(s, i++);
                if (c != 42405) {
                    runLength = c;
                    runValue = getInt(s, i++);
                    for (j = 0; j < runLength; ++j) {
                        array[ai++] = runValue;
                    }
                }
            }
            else {}
        }
        if (ai != length || i != maxI) {
            throw new IllegalStateException("Bad run-length encoded int array");
        }
        return array;
    }
    
    static final int getInt(final String s, final int i) {
        return s.charAt(2 * i) << 16 | s.charAt(2 * i + 1);
    }
    
    public static final short[] RLEStringToShortArray(final String s) {
        final int length = s.charAt(0) << 16 | s.charAt(1);
        final short[] array = new short[length];
        int ai = 0;
        for (int i = 2; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '\ua5a5') {
                c = s.charAt(++i);
                if (c == '\ua5a5') {
                    array[ai++] = (short)c;
                }
                else {
                    final int runLength = c;
                    final short runValue = (short)s.charAt(++i);
                    for (int j = 0; j < runLength; ++j) {
                        array[ai++] = runValue;
                    }
                }
            }
            else {
                array[ai++] = (short)c;
            }
        }
        if (ai != length) {
            throw new IllegalStateException("Bad run-length encoded short array");
        }
        return array;
    }
    
    public static final char[] RLEStringToCharArray(final String s) {
        final int length = s.charAt(0) << 16 | s.charAt(1);
        final char[] array = new char[length];
        int ai = 0;
        for (int i = 2; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '\ua5a5') {
                c = s.charAt(++i);
                if (c == '\ua5a5') {
                    array[ai++] = c;
                }
                else {
                    final int runLength = c;
                    final char runValue = s.charAt(++i);
                    for (int j = 0; j < runLength; ++j) {
                        array[ai++] = runValue;
                    }
                }
            }
            else {
                array[ai++] = c;
            }
        }
        if (ai != length) {
            throw new IllegalStateException("Bad run-length encoded short array");
        }
        return array;
    }
    
    public static final byte[] RLEStringToByteArray(final String s) {
        final int length = s.charAt(0) << 16 | s.charAt(1);
        final byte[] array = new byte[length];
        boolean nextChar = true;
        char c = '\0';
        int node = 0;
        int runLength = 0;
        int i = 2;
        int ai = 0;
        while (ai < length) {
            byte b;
            if (nextChar) {
                c = s.charAt(i++);
                b = (byte)(c >> 8);
                nextChar = false;
            }
            else {
                b = (byte)(c & '\u00ff');
                nextChar = true;
            }
            switch (node) {
                case 0: {
                    if (b == -91) {
                        node = 1;
                        continue;
                    }
                    array[ai++] = b;
                    continue;
                }
                case 1: {
                    if (b == -91) {
                        array[ai++] = -91;
                        node = 0;
                        continue;
                    }
                    runLength = b;
                    if (runLength < 0) {
                        runLength += 256;
                    }
                    node = 2;
                    continue;
                }
                case 2: {
                    for (int j = 0; j < runLength; ++j) {
                        array[ai++] = b;
                    }
                    node = 0;
                    continue;
                }
            }
        }
        if (node != 0) {
            throw new IllegalStateException("Bad run-length encoded byte array");
        }
        if (i != s.length()) {
            throw new IllegalStateException("Excess data in RLE byte array string");
        }
        return array;
    }
    
    public static final String formatForSource(final String s) {
        final StringBuilder buffer = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (i > 0) {
                buffer.append('+').append(Utility.LINE_SEPARATOR);
            }
            buffer.append("        \"");
            int count = 11;
            while (i < s.length() && count < 80) {
                final char c = s.charAt(i++);
                if (c < ' ' || c == '\"' || c == '\\') {
                    if (c == '\n') {
                        buffer.append("\\n");
                        count += 2;
                    }
                    else if (c == '\t') {
                        buffer.append("\\t");
                        count += 2;
                    }
                    else if (c == '\r') {
                        buffer.append("\\r");
                        count += 2;
                    }
                    else {
                        buffer.append('\\');
                        buffer.append(Utility.HEX_DIGIT[(c & '\u01c0') >> 6]);
                        buffer.append(Utility.HEX_DIGIT[(c & '8') >> 3]);
                        buffer.append(Utility.HEX_DIGIT[c & '\u0007']);
                        count += 4;
                    }
                }
                else if (c <= '~') {
                    buffer.append(c);
                    ++count;
                }
                else {
                    buffer.append("\\u");
                    buffer.append(Utility.HEX_DIGIT[(c & '\uf000') >> 12]);
                    buffer.append(Utility.HEX_DIGIT[(c & '\u0f00') >> 8]);
                    buffer.append(Utility.HEX_DIGIT[(c & '\u00f0') >> 4]);
                    buffer.append(Utility.HEX_DIGIT[c & '\u000f']);
                    count += 6;
                }
            }
            buffer.append('\"');
        }
        return buffer.toString();
    }
    
    public static final String format1ForSource(final String s) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("\"");
        int i = 0;
        while (i < s.length()) {
            final char c = s.charAt(i++);
            if (c < ' ' || c == '\"' || c == '\\') {
                if (c == '\n') {
                    buffer.append("\\n");
                }
                else if (c == '\t') {
                    buffer.append("\\t");
                }
                else if (c == '\r') {
                    buffer.append("\\r");
                }
                else {
                    buffer.append('\\');
                    buffer.append(Utility.HEX_DIGIT[(c & '\u01c0') >> 6]);
                    buffer.append(Utility.HEX_DIGIT[(c & '8') >> 3]);
                    buffer.append(Utility.HEX_DIGIT[c & '\u0007']);
                }
            }
            else if (c <= '~') {
                buffer.append(c);
            }
            else {
                buffer.append("\\u");
                buffer.append(Utility.HEX_DIGIT[(c & '\uf000') >> 12]);
                buffer.append(Utility.HEX_DIGIT[(c & '\u0f00') >> 8]);
                buffer.append(Utility.HEX_DIGIT[(c & '\u00f0') >> 4]);
                buffer.append(Utility.HEX_DIGIT[c & '\u000f']);
            }
        }
        buffer.append('\"');
        return buffer.toString();
    }
    
    public static final String escape(final String s) {
        final StringBuilder buf = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            final int c = Character.codePointAt(s, i);
            i += UTF16.getCharCount(c);
            if (c >= 32 && c <= 127) {
                if (c == 92) {
                    buf.append("\\\\");
                }
                else {
                    buf.append((char)c);
                }
            }
            else {
                final boolean four = c <= 65535;
                buf.append(four ? "\\u" : "\\U");
                buf.append(hex(c, four ? 4 : 8));
            }
        }
        return buf.toString();
    }
    
    public static int unescapeAt(final String s, final int[] offset16) {
        int result = 0;
        int n = 0;
        int minDig = 0;
        int maxDig = 0;
        int bitsPerDigit = 4;
        boolean braces = false;
        int offset17 = offset16[0];
        final int length = s.length();
        if (offset17 < 0 || offset17 >= length) {
            return -1;
        }
        int c = Character.codePointAt(s, offset17);
        offset17 += UTF16.getCharCount(c);
        switch (c) {
            case 117: {
                maxDig = (minDig = 4);
                break;
            }
            case 85: {
                maxDig = (minDig = 8);
                break;
            }
            case 120: {
                minDig = 1;
                if (offset17 < length && UTF16.charAt(s, offset17) == 123) {
                    ++offset17;
                    braces = true;
                    maxDig = 8;
                    break;
                }
                maxDig = 2;
                break;
            }
            default: {
                final int dig = UCharacter.digit(c, 8);
                if (dig >= 0) {
                    minDig = 1;
                    maxDig = 3;
                    n = 1;
                    bitsPerDigit = 3;
                    result = dig;
                    break;
                }
                break;
            }
        }
        if (minDig != 0) {
            while (offset17 < length && n < maxDig) {
                c = UTF16.charAt(s, offset17);
                final int dig = UCharacter.digit(c, (bitsPerDigit == 3) ? 8 : 16);
                if (dig < 0) {
                    break;
                }
                result = (result << bitsPerDigit | dig);
                offset17 += UTF16.getCharCount(c);
                ++n;
            }
            if (n < minDig) {
                return -1;
            }
            if (braces) {
                if (c != 125) {
                    return -1;
                }
                ++offset17;
            }
            if (result < 0 || result >= 1114112) {
                return -1;
            }
            if (offset17 < length && UTF16.isLeadSurrogate((char)result)) {
                int ahead = offset17 + 1;
                c = s.charAt(offset17);
                if (c == 92 && ahead < length) {
                    final int[] o = { ahead };
                    c = unescapeAt(s, o);
                    ahead = o[0];
                }
                if (UTF16.isTrailSurrogate((char)c)) {
                    offset17 = ahead;
                    result = UCharacterProperty.getRawSupplementary((char)result, (char)c);
                }
            }
            offset16[0] = offset17;
            return result;
        }
        else {
            for (int i = 0; i < Utility.UNESCAPE_MAP.length; i += 2) {
                if (c == Utility.UNESCAPE_MAP[i]) {
                    offset16[0] = offset17;
                    return Utility.UNESCAPE_MAP[i + 1];
                }
                if (c < Utility.UNESCAPE_MAP[i]) {
                    break;
                }
            }
            if (c == 99 && offset17 < length) {
                c = UTF16.charAt(s, offset17);
                offset16[0] = offset17 + UTF16.getCharCount(c);
                return 0x1F & c;
            }
            offset16[0] = offset17;
            return c;
        }
    }
    
    public static String unescape(final String s) {
        final StringBuilder buf = new StringBuilder();
        final int[] pos = { 0 };
        int i = 0;
        while (i < s.length()) {
            final char c = s.charAt(i++);
            if (c == '\\') {
                pos[0] = i;
                final int e = unescapeAt(s, pos);
                if (e < 0) {
                    throw new IllegalArgumentException("Invalid escape sequence " + s.substring(i - 1, Math.min(i + 8, s.length())));
                }
                buf.appendCodePoint(e);
                i = pos[0];
            }
            else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
    
    public static String unescapeLeniently(final String s) {
        final StringBuilder buf = new StringBuilder();
        final int[] pos = { 0 };
        int i = 0;
        while (i < s.length()) {
            final char c = s.charAt(i++);
            if (c == '\\') {
                pos[0] = i;
                final int e = unescapeAt(s, pos);
                if (e < 0) {
                    buf.append(c);
                }
                else {
                    buf.appendCodePoint(e);
                    i = pos[0];
                }
            }
            else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
    
    public static String hex(final long ch) {
        return hex(ch, 4);
    }
    
    public static String hex(long i, final int places) {
        if (i == Long.MIN_VALUE) {
            return "-8000000000000000";
        }
        final boolean negative = i < 0L;
        if (negative) {
            i = -i;
        }
        String result = Long.toString(i, 16).toUpperCase(Locale.ENGLISH);
        if (result.length() < places) {
            result = "0000000000000000".substring(result.length(), places) + result;
        }
        if (negative) {
            return '-' + result;
        }
        return result;
    }
    
    public static String hex(final CharSequence s) {
        return hex(s, 4, ",", true, new StringBuilder()).toString();
    }
    
    public static <S extends CharSequence, U extends CharSequence, T extends Appendable> T hex(final S s, final int width, final U separator, final boolean useCodePoints, final T result) {
        try {
            if (useCodePoints) {
                int cp;
                for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
                    cp = Character.codePointAt(s, i);
                    if (i != 0) {
                        result.append(separator);
                    }
                    result.append(hex(cp, width));
                }
            }
            else {
                for (int j = 0; j < s.length(); ++j) {
                    if (j != 0) {
                        result.append(separator);
                    }
                    result.append(hex(s.charAt(j), width));
                }
            }
            return result;
        }
        catch (IOException e) {
            throw new IllegalIcuArgumentException(e);
        }
    }
    
    public static String hex(final byte[] o, final int start, final int end, final String separator) {
        final StringBuilder result = new StringBuilder();
        for (int i = start; i < end; ++i) {
            if (i != 0) {
                result.append(separator);
            }
            result.append(hex(o[i]));
        }
        return result.toString();
    }
    
    public static <S extends CharSequence> String hex(final S s, final int width, final S separator) {
        return hex(s, width, separator, true, new StringBuilder()).toString();
    }
    
    public static void split(final String s, final char divider, final String[] output) {
        int last = 0;
        int current = 0;
        int i;
        for (i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == divider) {
                output[current++] = s.substring(last, i);
                last = i + 1;
            }
        }
        output[current++] = s.substring(last, i);
        while (current < output.length) {
            output[current++] = "";
        }
    }
    
    public static String[] split(final String s, final char divider) {
        int last = 0;
        final ArrayList<String> output = new ArrayList<String>();
        int i;
        for (i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == divider) {
                output.add(s.substring(last, i));
                last = i + 1;
            }
        }
        output.add(s.substring(last, i));
        return output.toArray(new String[output.size()]);
    }
    
    public static int lookup(final String source, final String[] target) {
        for (int i = 0; i < target.length; ++i) {
            if (source.equals(target[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean parseChar(final String id, final int[] pos, final char ch) {
        final int start = pos[0];
        pos[0] = PatternProps.skipWhiteSpace(id, pos[0]);
        if (pos[0] == id.length() || id.charAt(pos[0]) != ch) {
            pos[0] = start;
            return false;
        }
        final int n = 0;
        ++pos[n];
        return true;
    }
    
    public static int parsePattern(final String rule, int pos, final int limit, final String pattern, final int[] parsedInts) {
        final int[] p = { 0 };
        int intCount = 0;
        for (int i = 0; i < pattern.length(); ++i) {
            final char cpat = pattern.charAt(i);
            switch (cpat) {
                case ' ': {
                    if (pos >= limit) {
                        return -1;
                    }
                    final char c = rule.charAt(pos++);
                    if (!PatternProps.isWhiteSpace(c)) {
                        return -1;
                    }
                }
                case '~': {
                    pos = PatternProps.skipWhiteSpace(rule, pos);
                    break;
                }
                case '#': {
                    p[0] = pos;
                    parsedInts[intCount++] = parseInteger(rule, p, limit);
                    if (p[0] == pos) {
                        return -1;
                    }
                    pos = p[0];
                    break;
                }
                default: {
                    if (pos >= limit) {
                        return -1;
                    }
                    final char c = (char)UCharacter.toLowerCase(rule.charAt(pos++));
                    if (c != cpat) {
                        return -1;
                    }
                    break;
                }
            }
        }
        return pos;
    }
    
    public static int parsePattern(final String pat, final Replaceable text, int index, final int limit) {
        int ipat = 0;
        if (ipat == pat.length()) {
            return index;
        }
        int cpat = Character.codePointAt(pat, ipat);
        while (index < limit) {
            final int c = text.char32At(index);
            if (cpat == 126) {
                if (PatternProps.isWhiteSpace(c)) {
                    index += UTF16.getCharCount(c);
                    continue;
                }
                if (++ipat == pat.length()) {
                    return index;
                }
            }
            else {
                if (c != cpat) {
                    return -1;
                }
                final int n = UTF16.getCharCount(c);
                index += n;
                ipat += n;
                if (ipat == pat.length()) {
                    return index;
                }
            }
            cpat = UTF16.charAt(pat, ipat);
        }
        return -1;
    }
    
    public static int parseInteger(final String rule, final int[] pos, final int limit) {
        int count = 0;
        int value = 0;
        int p = pos[0];
        int radix = 10;
        if (rule.regionMatches(true, p, "0x", 0, 2)) {
            p += 2;
            radix = 16;
        }
        else if (p < limit && rule.charAt(p) == '0') {
            ++p;
            count = 1;
            radix = 8;
        }
        while (p < limit) {
            final int d = UCharacter.digit(rule.charAt(p++), radix);
            if (d < 0) {
                --p;
                break;
            }
            ++count;
            final int v = value * radix + d;
            if (v <= value) {
                return 0;
            }
            value = v;
        }
        if (count > 0) {
            pos[0] = p;
        }
        return value;
    }
    
    public static String parseUnicodeIdentifier(final String str, final int[] pos) {
        final StringBuilder buf = new StringBuilder();
        int p;
        int ch;
        for (p = pos[0]; p < str.length(); p += UTF16.getCharCount(ch)) {
            ch = Character.codePointAt(str, p);
            if (buf.length() == 0) {
                if (!UCharacter.isUnicodeIdentifierStart(ch)) {
                    return null;
                }
                buf.appendCodePoint(ch);
            }
            else {
                if (!UCharacter.isUnicodeIdentifierPart(ch)) {
                    break;
                }
                buf.appendCodePoint(ch);
            }
        }
        pos[0] = p;
        return buf.toString();
    }
    
    private static <T extends Appendable> void recursiveAppendNumber(final T result, final int n, final int radix, final int minDigits) {
        try {
            final int digit = n % radix;
            if (n >= radix || minDigits > 1) {
                recursiveAppendNumber((Appendable)result, n / radix, radix, minDigits - 1);
            }
            result.append(Utility.DIGITS[digit]);
        }
        catch (IOException e) {
            throw new IllegalIcuArgumentException(e);
        }
    }
    
    public static <T extends Appendable> T appendNumber(final T result, final int n, final int radix, final int minDigits) {
        try {
            if (radix < 2 || radix > 36) {
                throw new IllegalArgumentException("Illegal radix " + radix);
            }
            int abs;
            if ((abs = n) < 0) {
                abs = -n;
                result.append("-");
            }
            recursiveAppendNumber(result, abs, radix, minDigits);
            return result;
        }
        catch (IOException e) {
            throw new IllegalIcuArgumentException(e);
        }
    }
    
    public static int parseNumber(final String text, final int[] pos, final int radix) {
        int n = 0;
        int p;
        for (p = pos[0]; p < text.length(); ++p) {
            final int ch = Character.codePointAt(text, p);
            final int d = UCharacter.digit(ch, radix);
            if (d < 0) {
                break;
            }
            n = radix * n + d;
            if (n < 0) {
                return -1;
            }
        }
        if (p == pos[0]) {
            return -1;
        }
        pos[0] = p;
        return n;
    }
    
    public static boolean isUnprintable(final int c) {
        return c < 32 || c > 126;
    }
    
    public static <T extends Appendable> boolean escapeUnprintable(final T result, final int c) {
        try {
            if (isUnprintable(c)) {
                result.append('\\');
                if ((c & 0xFFFF0000) != 0x0) {
                    result.append('U');
                    result.append(Utility.DIGITS[0xF & c >> 28]);
                    result.append(Utility.DIGITS[0xF & c >> 24]);
                    result.append(Utility.DIGITS[0xF & c >> 20]);
                    result.append(Utility.DIGITS[0xF & c >> 16]);
                }
                else {
                    result.append('u');
                }
                result.append(Utility.DIGITS[0xF & c >> 12]);
                result.append(Utility.DIGITS[0xF & c >> 8]);
                result.append(Utility.DIGITS[0xF & c >> 4]);
                result.append(Utility.DIGITS[0xF & c]);
                return true;
            }
            return false;
        }
        catch (IOException e) {
            throw new IllegalIcuArgumentException(e);
        }
    }
    
    public static int quotedIndexOf(final String text, final int start, final int limit, final String setOfChars) {
        for (int i = start; i < limit; ++i) {
            final char c = text.charAt(i);
            if (c == '\\') {
                ++i;
            }
            else if (c == '\'') {
                while (++i < limit && text.charAt(i) != '\'') {}
            }
            else if (setOfChars.indexOf(c) >= 0) {
                return i;
            }
        }
        return -1;
    }
    
    public static void appendToRule(final StringBuffer rule, final int c, final boolean isLiteral, final boolean escapeUnprintable, final StringBuffer quoteBuf) {
        if (isLiteral || (escapeUnprintable && isUnprintable(c))) {
            if (quoteBuf.length() > 0) {
                while (quoteBuf.length() >= 2 && quoteBuf.charAt(0) == '\'' && quoteBuf.charAt(1) == '\'') {
                    rule.append('\\').append('\'');
                    quoteBuf.delete(0, 2);
                }
                int trailingCount = 0;
                while (quoteBuf.length() >= 2 && quoteBuf.charAt(quoteBuf.length() - 2) == '\'' && quoteBuf.charAt(quoteBuf.length() - 1) == '\'') {
                    quoteBuf.setLength(quoteBuf.length() - 2);
                    ++trailingCount;
                }
                if (quoteBuf.length() > 0) {
                    rule.append('\'');
                    rule.append(quoteBuf);
                    rule.append('\'');
                    quoteBuf.setLength(0);
                }
                while (trailingCount-- > 0) {
                    rule.append('\\').append('\'');
                }
            }
            if (c != -1) {
                if (c == 32) {
                    final int len = rule.length();
                    if (len > 0 && rule.charAt(len - 1) != ' ') {
                        rule.append(' ');
                    }
                }
                else if (!escapeUnprintable || !escapeUnprintable(rule, c)) {
                    rule.appendCodePoint(c);
                }
            }
        }
        else if (quoteBuf.length() == 0 && (c == 39 || c == 92)) {
            rule.append('\\').append((char)c);
        }
        else if (quoteBuf.length() > 0 || (c >= 33 && c <= 126 && (c < 48 || c > 57) && (c < 65 || c > 90) && (c < 97 || c > 122)) || PatternProps.isWhiteSpace(c)) {
            quoteBuf.appendCodePoint(c);
            if (c == 39) {
                quoteBuf.append((char)c);
            }
        }
        else {
            rule.appendCodePoint(c);
        }
    }
    
    public static void appendToRule(final StringBuffer rule, final String text, final boolean isLiteral, final boolean escapeUnprintable, final StringBuffer quoteBuf) {
        for (int i = 0; i < text.length(); ++i) {
            appendToRule(rule, text.charAt(i), isLiteral, escapeUnprintable, quoteBuf);
        }
    }
    
    public static void appendToRule(final StringBuffer rule, final UnicodeMatcher matcher, final boolean escapeUnprintable, final StringBuffer quoteBuf) {
        if (matcher != null) {
            appendToRule(rule, matcher.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
        }
    }
    
    public static final int compareUnsigned(int source, int target) {
        source -= Integer.MIN_VALUE;
        target -= Integer.MIN_VALUE;
        if (source < target) {
            return -1;
        }
        if (source > target) {
            return 1;
        }
        return 0;
    }
    
    public static final byte highBit(int n) {
        if (n <= 0) {
            return -1;
        }
        byte bit = 0;
        if (n >= 65536) {
            n >>= 16;
            bit += 16;
        }
        if (n >= 256) {
            n >>= 8;
            bit += 8;
        }
        if (n >= 16) {
            n >>= 4;
            bit += 4;
        }
        if (n >= 4) {
            n >>= 2;
            bit += 2;
        }
        if (n >= 2) {
            n >>= 1;
            ++bit;
        }
        return bit;
    }
    
    public static String valueOf(final int[] source) {
        final StringBuilder result = new StringBuilder(source.length);
        for (int i = 0; i < source.length; ++i) {
            result.appendCodePoint(source[i]);
        }
        return result.toString();
    }
    
    public static String repeat(final String s, final int count) {
        if (count <= 0) {
            return "";
        }
        if (count == 1) {
            return s;
        }
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            result.append(s);
        }
        return result.toString();
    }
    
    public static String[] splitString(final String src, final String target) {
        return src.split("\\Q" + target + "\\E");
    }
    
    public static String[] splitWhitespace(final String src) {
        return src.split("\\s+");
    }
    
    public static String fromHex(final String string, final int minLength, final String separator) {
        return fromHex(string, minLength, Pattern.compile((separator != null) ? separator : "\\s+"));
    }
    
    public static String fromHex(final String string, final int minLength, final Pattern separator) {
        final StringBuilder buffer = new StringBuilder();
        final String[] arr$;
        final String[] parts = arr$ = separator.split(string);
        for (final String part : arr$) {
            if (part.length() < minLength) {
                throw new IllegalArgumentException("code point too short: " + part);
            }
            final int cp = Integer.parseInt(part, 16);
            buffer.appendCodePoint(cp);
        }
        return buffer.toString();
    }
    
    public static ClassLoader getFallbackClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
            if (cl == null) {
                throw new RuntimeException("No accessible class loader is available.");
            }
        }
        return cl;
    }
    
    static {
        Utility.LINE_SEPARATOR = System.getProperty("line.separator");
        HEX_DIGIT = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        UNESCAPE_MAP = new char[] { 'a', '\u0007', 'b', '\b', 'e', '\u001b', 'f', '\f', 'n', '\n', 'r', '\r', 't', '\t', 'v', '\u000b' };
        DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    }
}
