// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

import java.nio.IntBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.FloatBuffer;
import org.lwjgl.LWJGLException;

public final class AL11
{
    public static final int AL_SEC_OFFSET = 4132;
    public static final int AL_SAMPLE_OFFSET = 4133;
    public static final int AL_BYTE_OFFSET = 4134;
    public static final int AL_STATIC = 4136;
    public static final int AL_STREAMING = 4137;
    public static final int AL_UNDETERMINED = 4144;
    public static final int AL_ILLEGAL_COMMAND = 40964;
    public static final int AL_SPEED_OF_SOUND = 49155;
    public static final int AL_LINEAR_DISTANCE = 53251;
    public static final int AL_LINEAR_DISTANCE_CLAMPED = 53252;
    public static final int AL_EXPONENT_DISTANCE = 53253;
    public static final int AL_EXPONENT_DISTANCE_CLAMPED = 53254;
    
    private AL11() {
    }
    
    static native void initNativeStubs() throws LWJGLException;
    
    public static void alListener3i(final int pname, final int v1, final int v2, final int v3) {
        nalListener3i(pname, v1, v2, v3);
    }
    
    static native void nalListener3i(final int p0, final int p1, final int p2, final int p3);
    
    public static void alGetListeneri(final int pname, final FloatBuffer intdata) {
        BufferChecks.checkBuffer(intdata, 1);
        nalGetListeneriv(pname, MemoryUtil.getAddress(intdata));
    }
    
    static native void nalGetListeneriv(final int p0, final long p1);
    
    public static void alSource3i(final int source, final int pname, final int v1, final int v2, final int v3) {
        nalSource3i(source, pname, v1, v2, v3);
    }
    
    static native void nalSource3i(final int p0, final int p1, final int p2, final int p3, final int p4);
    
    public static void alSource(final int source, final int pname, final IntBuffer value) {
        BufferChecks.checkBuffer(value, 1);
        nalSourceiv(source, pname, MemoryUtil.getAddress(value));
    }
    
    static native void nalSourceiv(final int p0, final int p1, final long p2);
    
    public static void alBufferf(final int buffer, final int pname, final float value) {
        nalBufferf(buffer, pname, value);
    }
    
    static native void nalBufferf(final int p0, final int p1, final float p2);
    
    public static void alBuffer3f(final int buffer, final int pname, final float v1, final float v2, final float v3) {
        nalBuffer3f(buffer, pname, v1, v2, v3);
    }
    
    static native void nalBuffer3f(final int p0, final int p1, final float p2, final float p3, final float p4);
    
    public static void alBuffer(final int buffer, final int pname, final FloatBuffer value) {
        BufferChecks.checkBuffer(value, 1);
        nalBufferfv(buffer, pname, MemoryUtil.getAddress(value));
    }
    
    static native void nalBufferfv(final int p0, final int p1, final long p2);
    
    public static void alBufferi(final int buffer, final int pname, final int value) {
        nalBufferi(buffer, pname, value);
    }
    
    static native void nalBufferi(final int p0, final int p1, final int p2);
    
    public static void alBuffer3i(final int buffer, final int pname, final int v1, final int v2, final int v3) {
        nalBuffer3i(buffer, pname, v1, v2, v3);
    }
    
    static native void nalBuffer3i(final int p0, final int p1, final int p2, final int p3, final int p4);
    
    public static void alBuffer(final int buffer, final int pname, final IntBuffer value) {
        BufferChecks.checkBuffer(value, 1);
        nalBufferiv(buffer, pname, MemoryUtil.getAddress(value));
    }
    
    static native void nalBufferiv(final int p0, final int p1, final long p2);
    
    public static int alGetBufferi(final int buffer, final int pname) {
        final int __result = nalGetBufferi(buffer, pname);
        return __result;
    }
    
    static native int nalGetBufferi(final int p0, final int p1);
    
    public static void alGetBuffer(final int buffer, final int pname, final IntBuffer values) {
        BufferChecks.checkBuffer(values, 1);
        nalGetBufferiv(buffer, pname, MemoryUtil.getAddress(values));
    }
    
    static native void nalGetBufferiv(final int p0, final int p1, final long p2);
    
    public static float alGetBufferf(final int buffer, final int pname) {
        final float __result = nalGetBufferf(buffer, pname);
        return __result;
    }
    
    static native float nalGetBufferf(final int p0, final int p1);
    
    public static void alGetBuffer(final int buffer, final int pname, final FloatBuffer values) {
        BufferChecks.checkBuffer(values, 1);
        nalGetBufferfv(buffer, pname, MemoryUtil.getAddress(values));
    }
    
    static native void nalGetBufferfv(final int p0, final int p1, final long p2);
    
    public static void alSpeedOfSound(final float value) {
        nalSpeedOfSound(value);
    }
    
    static native void nalSpeedOfSound(final float p0);
}
