package org.lwjgl.openal;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

import java.nio.*;

public final class AL10 {
    public static final int AL_INVALID = -1;
    public static final int AL_NONE = 0;
    public static final int AL_FALSE = 0;
    public static final int AL_TRUE = 1;
    public static final int AL_SOURCE_TYPE = 4135;
    public static final int AL_SOURCE_ABSOLUTE = 513;
    public static final int AL_SOURCE_RELATIVE = 514;
    public static final int AL_CONE_INNER_ANGLE = 4097;
    public static final int AL_CONE_OUTER_ANGLE = 4098;
    public static final int AL_PITCH = 4099;
    public static final int AL_POSITION = 4100;
    public static final int AL_DIRECTION = 4101;
    public static final int AL_VELOCITY = 4102;
    public static final int AL_LOOPING = 4103;
    public static final int AL_BUFFER = 4105;
    public static final int AL_GAIN = 4106;
    public static final int AL_MIN_GAIN = 4109;
    public static final int AL_MAX_GAIN = 4110;
    public static final int AL_ORIENTATION = 4111;
    public static final int AL_REFERENCE_DISTANCE = 4128;
    public static final int AL_ROLLOFF_FACTOR = 4129;
    public static final int AL_CONE_OUTER_GAIN = 4130;
    public static final int AL_MAX_DISTANCE = 4131;
    public static final int AL_CHANNEL_MASK = 12288;
    public static final int AL_SOURCE_STATE = 4112;
    public static final int AL_INITIAL = 4113;
    public static final int AL_PLAYING = 4114;
    public static final int AL_PAUSED = 4115;
    public static final int AL_STOPPED = 4116;
    public static final int AL_BUFFERS_QUEUED = 4117;
    public static final int AL_BUFFERS_PROCESSED = 4118;
    public static final int AL_FORMAT_MONO8 = 4352;
    public static final int AL_FORMAT_MONO16 = 4353;
    public static final int AL_FORMAT_STEREO8 = 4354;
    public static final int AL_FORMAT_STEREO16 = 4355;
    public static final int AL_FORMAT_VORBIS_EXT = 65539;
    public static final int AL_FREQUENCY = 8193;
    public static final int AL_BITS = 8194;
    public static final int AL_CHANNELS = 8195;
    public static final int AL_SIZE = 8196;
  
   *
    @deprecated
    public static final int AL_DATA = 8197;
    public static final int AL_UNUSED = 8208;
    public static final int AL_PENDING = 8209;
    public static final int AL_PROCESSED = 8210;
    public static final int AL_NO_ERROR = 0;
    public static final int AL_INVALID_NAME = 40961;
    public static final int AL_INVALID_ENUM = 40962;
    public static final int AL_INVALID_VALUE = 40963;
    public static final int AL_INVALID_OPERATION = 40964;
    public static final int AL_OUT_OF_MEMORY = 40965;
    public static final int AL_VENDOR = 45057;
    public static final int AL_VERSION = 45058;
    public static final int AL_RENDERER = 45059;
    public static final int AL_EXTENSIONS = 45060;
    public static final int AL_DOPPLER_FACTOR = 49152;
    public static final int AL_DOPPLER_VELOCITY = 49153;
    public static final int AL_DISTANCE_MODEL = 53248;
    public static final int AL_INVERSE_DISTANCE = 53249;
    public static final int AL_INVERSE_DISTANCE_CLAMPED = 53250;

    static native void initNativeStubs()
            throws LWJGLException;

    public static void alEnable(int paramInt) {
        nalEnable(paramInt);
    }

    static native void nalEnable(int paramInt);

    public static void alDisable(int paramInt) {
        nalDisable(paramInt);
    }

    static native void nalDisable(int paramInt);

    public static boolean alIsEnabled(int paramInt) {
        boolean bool = nalIsEnabled(paramInt);
        return bool;
    }

    static native boolean nalIsEnabled(int paramInt);

    public static boolean alGetBoolean(int paramInt) {
        boolean bool = nalGetBoolean(paramInt);
        return bool;
    }

    static native boolean nalGetBoolean(int paramInt);

    public static int alGetInteger(int paramInt) {
        int i = nalGetInteger(paramInt);
        return i;
    }

    static native int nalGetInteger(int paramInt);

    public static float alGetFloat(int paramInt) {
        float f = nalGetFloat(paramInt);
        return f;
    }

    static native float nalGetFloat(int paramInt);

    public static double alGetDouble(int paramInt) {
        double d = nalGetDouble(paramInt);
        return d;
    }

    static native double nalGetDouble(int paramInt);

    public static void alGetInteger(int paramInt, IntBuffer paramIntBuffer) {
        BufferChecks.checkBuffer(paramIntBuffer, 1);
        nalGetIntegerv(paramInt, MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalGetIntegerv(int paramInt, long paramLong);

    public static void alGetFloat(int paramInt, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalGetFloatv(paramInt, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalGetFloatv(int paramInt, long paramLong);

    public static void alGetDouble(int paramInt, DoubleBuffer paramDoubleBuffer) {
        BufferChecks.checkBuffer(paramDoubleBuffer, 1);
        nalGetDoublev(paramInt, MemoryUtil.getAddress(paramDoubleBuffer));
    }

    static native void nalGetDoublev(int paramInt, long paramLong);

    public static String alGetString(int paramInt) {
        String str = nalGetString(paramInt);
        return str;
    }

    static native String nalGetString(int paramInt);

    public static int alGetError() {
        int i = nalGetError();
        return i;
    }

    static native int nalGetError();

    public static boolean alIsExtensionPresent(String paramString) {
        BufferChecks.checkNotNull(paramString);
        boolean bool = nalIsExtensionPresent(paramString);
        return bool;
    }

    static native boolean nalIsExtensionPresent(String paramString);

    public static int alGetEnumValue(String paramString) {
        BufferChecks.checkNotNull(paramString);
        int i = nalGetEnumValue(paramString);
        return i;
    }

    static native int nalGetEnumValue(String paramString);

    public static void alListeneri(int paramInt1, int paramInt2) {
        nalListeneri(paramInt1, paramInt2);
    }

    static native void nalListeneri(int paramInt1, int paramInt2);

    public static void alListenerf(int paramInt, float paramFloat) {
        nalListenerf(paramInt, paramFloat);
    }

    static native void nalListenerf(int paramInt, float paramFloat);

    public static void alListener(int paramInt, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalListenerfv(paramInt, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalListenerfv(int paramInt, long paramLong);

    public static void alListener3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
        nalListener3f(paramInt, paramFloat1, paramFloat2, paramFloat3);
    }

    static native void nalListener3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3);

    public static int alGetListeneri(int paramInt) {
        int i = nalGetListeneri(paramInt);
        return i;
    }

    static native int nalGetListeneri(int paramInt);

    public static float alGetListenerf(int paramInt) {
        float f = nalGetListenerf(paramInt);
        return f;
    }

    static native float nalGetListenerf(int paramInt);

    public static void alGetListener(int paramInt, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalGetListenerfv(paramInt, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalGetListenerfv(int paramInt, long paramLong);

    public static void alGenSources(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalGenSources(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalGenSources(int paramInt, long paramLong);

    public static int alGenSources() {
        int i = nalGenSources2(1);
        return i;
    }

    static native int nalGenSources2(int paramInt);

    public static void alDeleteSources(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalDeleteSources(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalDeleteSources(int paramInt, long paramLong);

    public static void alDeleteSources(int paramInt) {
        nalDeleteSources2(1, paramInt);
    }

    static native void nalDeleteSources2(int paramInt1, int paramInt2);

    public static boolean alIsSource(int paramInt) {
        boolean bool = nalIsSource(paramInt);
        return bool;
    }

    static native boolean nalIsSource(int paramInt);

    public static void alSourcei(int paramInt1, int paramInt2, int paramInt3) {
        nalSourcei(paramInt1, paramInt2, paramInt3);
    }

    static native void nalSourcei(int paramInt1, int paramInt2, int paramInt3);

    public static void alSourcef(int paramInt1, int paramInt2, float paramFloat) {
        nalSourcef(paramInt1, paramInt2, paramFloat);
    }

    static native void nalSourcef(int paramInt1, int paramInt2, float paramFloat);

    public static void alSource(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalSourcefv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalSourcefv(int paramInt1, int paramInt2, long paramLong);

    public static void alSource3f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3) {
        nalSource3f(paramInt1, paramInt2, paramFloat1, paramFloat2, paramFloat3);
    }

    static native void nalSource3f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3);

    public static int alGetSourcei(int paramInt1, int paramInt2) {
        int i = nalGetSourcei(paramInt1, paramInt2);
        return i;
    }

    static native int nalGetSourcei(int paramInt1, int paramInt2);

    public static float alGetSourcef(int paramInt1, int paramInt2) {
        float f = nalGetSourcef(paramInt1, paramInt2);
        return f;
    }

    static native float nalGetSourcef(int paramInt1, int paramInt2);

    public static void alGetSource(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer) {
        BufferChecks.checkBuffer(paramFloatBuffer, 1);
        nalGetSourcefv(paramInt1, paramInt2, MemoryUtil.getAddress(paramFloatBuffer));
    }

    static native void nalGetSourcefv(int paramInt1, int paramInt2, long paramLong);

    public static void alSourcePlay(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalSourcePlayv(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalSourcePlayv(int paramInt, long paramLong);

    public static void alSourcePause(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalSourcePausev(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalSourcePausev(int paramInt, long paramLong);

    public static void alSourceStop(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalSourceStopv(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalSourceStopv(int paramInt, long paramLong);

    public static void alSourceRewind(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalSourceRewindv(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalSourceRewindv(int paramInt, long paramLong);

    public static void alSourcePlay(int paramInt) {
        nalSourcePlay(paramInt);
    }

    static native void nalSourcePlay(int paramInt);

    public static void alSourcePause(int paramInt) {
        nalSourcePause(paramInt);
    }

    static native void nalSourcePause(int paramInt);

    public static void alSourceStop(int paramInt) {
        nalSourceStop(paramInt);
    }

    static native void nalSourceStop(int paramInt);

    public static void alSourceRewind(int paramInt) {
        nalSourceRewind(paramInt);
    }

    static native void nalSourceRewind(int paramInt);

    public static void alGenBuffers(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalGenBuffers(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalGenBuffers(int paramInt, long paramLong);

    public static int alGenBuffers() {
        int i = nalGenBuffers2(1);
        return i;
    }

    static native int nalGenBuffers2(int paramInt);

    public static void alDeleteBuffers(IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalDeleteBuffers(paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalDeleteBuffers(int paramInt, long paramLong);

    public static void alDeleteBuffers(int paramInt) {
        nalDeleteBuffers2(1, paramInt);
    }

    static native void nalDeleteBuffers2(int paramInt1, int paramInt2);

    public static boolean alIsBuffer(int paramInt) {
        boolean bool = nalIsBuffer(paramInt);
        return bool;
    }

    static native boolean nalIsBuffer(int paramInt);

    public static void alBufferData(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer, int paramInt3) {
        BufferChecks.checkDirect(paramByteBuffer);
        nalBufferData(paramInt1, paramInt2, MemoryUtil.getAddress(paramByteBuffer), paramByteBuffer.remaining(), paramInt3);
    }

    public static void alBufferData(int paramInt1, int paramInt2, IntBuffer paramIntBuffer, int paramInt3) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalBufferData(paramInt1, paramInt2, MemoryUtil.getAddress(paramIntBuffer), paramIntBuffer.remaining() >>> 2, paramInt3);
    }

    public static void alBufferData(int paramInt1, int paramInt2, ShortBuffer paramShortBuffer, int paramInt3) {
        BufferChecks.checkDirect(paramShortBuffer);
        nalBufferData(paramInt1, paramInt2, MemoryUtil.getAddress(paramShortBuffer), paramShortBuffer.remaining() >>> 1, paramInt3);
    }

    static native void nalBufferData(int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4);

    public static int alGetBufferi(int paramInt1, int paramInt2) {
        int i = nalGetBufferi(paramInt1, paramInt2);
        return i;
    }

    static native int nalGetBufferi(int paramInt1, int paramInt2);

    public static float alGetBufferf(int paramInt1, int paramInt2) {
        float f = nalGetBufferf(paramInt1, paramInt2);
        return f;
    }

    static native float nalGetBufferf(int paramInt1, int paramInt2);

    public static void alSourceQueueBuffers(int paramInt, IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalSourceQueueBuffers(paramInt, paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalSourceQueueBuffers(int paramInt1, int paramInt2, long paramLong);

    public static void alSourceQueueBuffers(int paramInt1, int paramInt2) {
        nalSourceQueueBuffers2(paramInt1, 1, paramInt2);
    }

    static native void nalSourceQueueBuffers2(int paramInt1, int paramInt2, int paramInt3);

    public static void alSourceUnqueueBuffers(int paramInt, IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalSourceUnqueueBuffers(paramInt, paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
    }

    static native void nalSourceUnqueueBuffers(int paramInt1, int paramInt2, long paramLong);

    public static int alSourceUnqueueBuffers(int paramInt) {
        int i = nalSourceUnqueueBuffers2(paramInt, 1);
        return i;
    }

    static native int nalSourceUnqueueBuffers2(int paramInt1, int paramInt2);

    public static void alDistanceModel(int paramInt) {
        nalDistanceModel(paramInt);
    }

    static native void nalDistanceModel(int paramInt);

    public static void alDopplerFactor(float paramFloat) {
        nalDopplerFactor(paramFloat);
    }

    static native void nalDopplerFactor(float paramFloat);

    public static void alDopplerVelocity(float paramFloat) {
        nalDopplerVelocity(paramFloat);
    }

    static native void nalDopplerVelocity(float paramFloat);
}




