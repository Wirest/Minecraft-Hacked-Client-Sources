package org.lwjgl.openal;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class AL11 {
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

    static native void initNativeStubs()
            throws LWJGLException;

    public static void alListener3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        nalListener3i(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    static native void nalListener3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public static void alGetListeneri(int paramInt, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalGetListeneriv(paramInt, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalGetListeneriv(int paramInt, long paramLong);

    public static void alSource3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        nalSource3i(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    static native void nalSource3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    public static void alSource(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        BufferChecks.checkBuffer(paramIntBuffer, 1);
        nalSourceiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalSourceiv(int paramInt1, int paramInt2, long paramLong);

    public static void alBufferf(int paramInt1, int paramInt2, float paramFloat) {
        nalBufferf(paramInt1, paramInt2, paramFloat);
    }

    static native void nalBufferf(int paramInt1, int paramInt2, float paramFloat);

    public static void alBuffer3f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3) {
        nalBuffer3f(paramInt1, paramInt2, paramFloat1, paramFloat2, paramFloat3);
    }

    static native void nalBuffer3f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3);

    public static void alBuffer(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalBufferfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalBufferfv(int paramInt1, int paramInt2, long paramLong);

    public static void alBufferi(int paramInt1, int paramInt2, int paramInt3) {
        nalBufferi(paramInt1, paramInt2, paramInt3);
    }

    static native void nalBufferi(int paramInt1, int paramInt2, int paramInt3);

    public static void alBuffer3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        nalBuffer3i(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }

    static native void nalBuffer3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

    public static void alBuffer(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        BufferChecks.checkBuffer(paramIntBuffer, 1);
        nalBufferiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalBufferiv(int paramInt1, int paramInt2, long paramLong);

    public static int alGetBufferi(int paramInt1, int paramInt2) {
        int i = nalGetBufferi(paramInt1, paramInt2);
        return i;
    }

    static native int nalGetBufferi(int paramInt1, int paramInt2);

    public static void alGetBuffer(int paramInt1, int paramInt2, IntBuffer paramIntBuffer) {
        BufferChecks.checkBuffer(paramIntBuffer, 1);
        nalGetBufferiv(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalGetBufferiv(int paramInt1, int paramInt2, long paramLong);

    public static float alGetBufferf(int paramInt1, int paramInt2) {
        float f = nalGetBufferf(paramInt1, paramInt2);
        return f;
    }

    static native float nalGetBufferf(int paramInt1, int paramInt2);

    public static void alGetBuffer(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalGetBufferfv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalGetBufferfv(int paramInt1, int paramInt2, long paramLong);

    public static void alSpeedOfSound(float paramFloat) {
        nalSpeedOfSound(paramFloat);
    }

    static native void nalSpeedOfSound(float paramFloat);
}




