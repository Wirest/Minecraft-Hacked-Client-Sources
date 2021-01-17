// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

import java.util.Map;
import java.util.HashMap;

final class MessageFormatter
{
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';
    
    static FormattingTuple format(final String messagePattern, final Object arg) {
        return arrayFormat(messagePattern, new Object[] { arg });
    }
    
    static FormattingTuple format(final String messagePattern, final Object argA, final Object argB) {
        return arrayFormat(messagePattern, new Object[] { argA, argB });
    }
    
    static Throwable getThrowableCandidate(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable)lastEntry;
        }
        return null;
    }
    
    static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray) {
        final Throwable throwableCandidate = getThrowableCandidate(argArray);
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwableCandidate);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i = 0;
        final StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);
        int L = 0;
        while (L < argArray.length) {
            final int j = messagePattern.indexOf("{}", i);
            if (j == -1) {
                if (i == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwableCandidate);
                }
                sbuf.append(messagePattern.substring(i, messagePattern.length()));
                return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
            }
            else {
                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        --L;
                        sbuf.append(messagePattern.substring(i, j - 1));
                        sbuf.append('{');
                        i = j + 1;
                    }
                    else {
                        sbuf.append(messagePattern.substring(i, j - 1));
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Void>());
                        i = j + 2;
                    }
                }
                else {
                    sbuf.append(messagePattern.substring(i, j));
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Void>());
                    i = j + 2;
                }
                ++L;
            }
        }
        sbuf.append(messagePattern.substring(i, messagePattern.length()));
        if (L < argArray.length - 1) {
            return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
        }
        return new FormattingTuple(sbuf.toString(), argArray, null);
    }
    
    static boolean isEscapedDelimeter(final String messagePattern, final int delimeterStartIndex) {
        return delimeterStartIndex != 0 && messagePattern.charAt(delimeterStartIndex - 1) == '\\';
    }
    
    static boolean isDoubleEscaped(final String messagePattern, final int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }
    
    private static void deeplyAppendParameter(final StringBuffer sbuf, final Object o, final Map<Object[], Void> seenMap) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        }
        else if (o instanceof boolean[]) {
            booleanArrayAppend(sbuf, (boolean[])o);
        }
        else if (o instanceof byte[]) {
            byteArrayAppend(sbuf, (byte[])o);
        }
        else if (o instanceof char[]) {
            charArrayAppend(sbuf, (char[])o);
        }
        else if (o instanceof short[]) {
            shortArrayAppend(sbuf, (short[])o);
        }
        else if (o instanceof int[]) {
            intArrayAppend(sbuf, (int[])o);
        }
        else if (o instanceof long[]) {
            longArrayAppend(sbuf, (long[])o);
        }
        else if (o instanceof float[]) {
            floatArrayAppend(sbuf, (float[])o);
        }
        else if (o instanceof double[]) {
            doubleArrayAppend(sbuf, (double[])o);
        }
        else {
            objectArrayAppend(sbuf, (Object[])o, seenMap);
        }
    }
    
    private static void safeObjectAppend(final StringBuffer sbuf, final Object o) {
        try {
            final String oAsString = o.toString();
            sbuf.append(oAsString);
        }
        catch (Throwable t) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + ']');
            t.printStackTrace();
            sbuf.append("[FAILED toString()]");
        }
    }
    
    private static void objectArrayAppend(final StringBuffer sbuf, final Object[] a, final Map<Object[], Void> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            for (int len = a.length, i = 0; i < len; ++i) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }
            seenMap.remove(a);
        }
        else {
            sbuf.append("...");
        }
        sbuf.append(']');
    }
    
    private static void booleanArrayAppend(final StringBuffer sbuf, final boolean[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void byteArrayAppend(final StringBuffer sbuf, final byte[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void charArrayAppend(final StringBuffer sbuf, final char[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void shortArrayAppend(final StringBuffer sbuf, final short[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void intArrayAppend(final StringBuffer sbuf, final int[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void longArrayAppend(final StringBuffer sbuf, final long[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void floatArrayAppend(final StringBuffer sbuf, final float[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private static void doubleArrayAppend(final StringBuffer sbuf, final double[] a) {
        sbuf.append('[');
        for (int len = a.length, i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }
        sbuf.append(']');
    }
    
    private MessageFormatter() {
    }
}
