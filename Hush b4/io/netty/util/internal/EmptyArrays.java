// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.security.cert.X509Certificate;
import java.nio.ByteBuffer;

public final class EmptyArrays
{
    public static final byte[] EMPTY_BYTES;
    public static final boolean[] EMPTY_BOOLEANS;
    public static final double[] EMPTY_DOUBLES;
    public static final float[] EMPTY_FLOATS;
    public static final int[] EMPTY_INTS;
    public static final short[] EMPTY_SHORTS;
    public static final long[] EMPTY_LONGS;
    public static final Object[] EMPTY_OBJECTS;
    public static final Class<?>[] EMPTY_CLASSES;
    public static final String[] EMPTY_STRINGS;
    public static final StackTraceElement[] EMPTY_STACK_TRACE;
    public static final ByteBuffer[] EMPTY_BYTE_BUFFERS;
    public static final X509Certificate[] EMPTY_X509_CERTIFICATES;
    
    private EmptyArrays() {
    }
    
    static {
        EMPTY_BYTES = new byte[0];
        EMPTY_BOOLEANS = new boolean[0];
        EMPTY_DOUBLES = new double[0];
        EMPTY_FLOATS = new float[0];
        EMPTY_INTS = new int[0];
        EMPTY_SHORTS = new short[0];
        EMPTY_LONGS = new long[0];
        EMPTY_OBJECTS = new Object[0];
        EMPTY_CLASSES = new Class[0];
        EMPTY_STRINGS = new String[0];
        EMPTY_STACK_TRACE = new StackTraceElement[0];
        EMPTY_BYTE_BUFFERS = new ByteBuffer[0];
        EMPTY_X509_CERTIFICATES = new X509Certificate[0];
    }
}
