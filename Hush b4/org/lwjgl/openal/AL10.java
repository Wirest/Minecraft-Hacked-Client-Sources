// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

public final class AL10
{
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
    @Deprecated
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
    
    private AL10() {
    }
    
    static native void initNativeStubs() throws LWJGLException;
    
    public static void alEnable(final int capability) {
        nalEnable(capability);
    }
    
    static native void nalEnable(final int p0);
    
    public static void alDisable(final int capability) {
        nalDisable(capability);
    }
    
    static native void nalDisable(final int p0);
    
    public static boolean alIsEnabled(final int capability) {
        final boolean __result = nalIsEnabled(capability);
        return __result;
    }
    
    static native boolean nalIsEnabled(final int p0);
    
    public static boolean alGetBoolean(final int pname) {
        final boolean __result = nalGetBoolean(pname);
        return __result;
    }
    
    static native boolean nalGetBoolean(final int p0);
    
    public static int alGetInteger(final int pname) {
        final int __result = nalGetInteger(pname);
        return __result;
    }
    
    static native int nalGetInteger(final int p0);
    
    public static float alGetFloat(final int pname) {
        final float __result = nalGetFloat(pname);
        return __result;
    }
    
    static native float nalGetFloat(final int p0);
    
    public static double alGetDouble(final int pname) {
        final double __result = nalGetDouble(pname);
        return __result;
    }
    
    static native double nalGetDouble(final int p0);
    
    public static void alGetInteger(final int pname, final IntBuffer data) {
        BufferChecks.checkBuffer(data, 1);
        nalGetIntegerv(pname, MemoryUtil.getAddress(data));
    }
    
    static native void nalGetIntegerv(final int p0, final long p1);
    
    public static void alGetFloat(final int pname, final FloatBuffer data) {
        BufferChecks.checkBuffer(data, 1);
        nalGetFloatv(pname, MemoryUtil.getAddress(data));
    }
    
    static native void nalGetFloatv(final int p0, final long p1);
    
    public static void alGetDouble(final int pname, final DoubleBuffer data) {
        BufferChecks.checkBuffer(data, 1);
        nalGetDoublev(pname, MemoryUtil.getAddress(data));
    }
    
    static native void nalGetDoublev(final int p0, final long p1);
    
    public static String alGetString(final int pname) {
        final String __result = nalGetString(pname);
        return __result;
    }
    
    static native String nalGetString(final int p0);
    
    public static int alGetError() {
        final int __result = nalGetError();
        return __result;
    }
    
    static native int nalGetError();
    
    public static boolean alIsExtensionPresent(final String fname) {
        BufferChecks.checkNotNull(fname);
        final boolean __result = nalIsExtensionPresent(fname);
        return __result;
    }
    
    static native boolean nalIsExtensionPresent(final String p0);
    
    public static int alGetEnumValue(final String ename) {
        BufferChecks.checkNotNull(ename);
        final int __result = nalGetEnumValue(ename);
        return __result;
    }
    
    static native int nalGetEnumValue(final String p0);
    
    public static void alListeneri(final int pname, final int value) {
        nalListeneri(pname, value);
    }
    
    static native void nalListeneri(final int p0, final int p1);
    
    public static void alListenerf(final int pname, final float value) {
        nalListenerf(pname, value);
    }
    
    static native void nalListenerf(final int p0, final float p1);
    
    public static void alListener(final int pname, final FloatBuffer value) {
        BufferChecks.checkBuffer(value, 1);
        nalListenerfv(pname, MemoryUtil.getAddress(value));
    }
    
    static native void nalListenerfv(final int p0, final long p1);
    
    public static void alListener3f(final int pname, final float v1, final float v2, final float v3) {
        nalListener3f(pname, v1, v2, v3);
    }
    
    static native void nalListener3f(final int p0, final float p1, final float p2, final float p3);
    
    public static int alGetListeneri(final int pname) {
        final int __result = nalGetListeneri(pname);
        return __result;
    }
    
    static native int nalGetListeneri(final int p0);
    
    public static float alGetListenerf(final int pname) {
        final float __result = nalGetListenerf(pname);
        return __result;
    }
    
    static native float nalGetListenerf(final int p0);
    
    public static void alGetListener(final int pname, final FloatBuffer floatdata) {
        BufferChecks.checkBuffer(floatdata, 1);
        nalGetListenerfv(pname, MemoryUtil.getAddress(floatdata));
    }
    
    static native void nalGetListenerfv(final int p0, final long p1);
    
    public static void alGenSources(final IntBuffer sources) {
        BufferChecks.checkDirect(sources);
        nalGenSources(sources.remaining(), MemoryUtil.getAddress(sources));
    }
    
    static native void nalGenSources(final int p0, final long p1);
    
    public static int alGenSources() {
        final int __result = nalGenSources2(1);
        return __result;
    }
    
    static native int nalGenSources2(final int p0);
    
    public static void alDeleteSources(final IntBuffer sources) {
        BufferChecks.checkDirect(sources);
        nalDeleteSources(sources.remaining(), MemoryUtil.getAddress(sources));
    }
    
    static native void nalDeleteSources(final int p0, final long p1);
    
    public static void alDeleteSources(final int source) {
        nalDeleteSources2(1, source);
    }
    
    static native void nalDeleteSources2(final int p0, final int p1);
    
    public static boolean alIsSource(final int id) {
        final boolean __result = nalIsSource(id);
        return __result;
    }
    
    static native boolean nalIsSource(final int p0);
    
    public static void alSourcei(final int source, final int pname, final int value) {
        nalSourcei(source, pname, value);
    }
    
    static native void nalSourcei(final int p0, final int p1, final int p2);
    
    public static void alSourcef(final int source, final int pname, final float value) {
        nalSourcef(source, pname, value);
    }
    
    static native void nalSourcef(final int p0, final int p1, final float p2);
    
    public static void alSource(final int source, final int pname, final FloatBuffer value) {
        BufferChecks.checkBuffer(value, 1);
        nalSourcefv(source, pname, MemoryUtil.getAddress(value));
    }
    
    static native void nalSourcefv(final int p0, final int p1, final long p2);
    
    public static void alSource3f(final int source, final int pname, final float v1, final float v2, final float v3) {
        nalSource3f(source, pname, v1, v2, v3);
    }
    
    static native void nalSource3f(final int p0, final int p1, final float p2, final float p3, final float p4);
    
    public static int alGetSourcei(final int source, final int pname) {
        final int __result = nalGetSourcei(source, pname);
        return __result;
    }
    
    static native int nalGetSourcei(final int p0, final int p1);
    
    public static float alGetSourcef(final int source, final int pname) {
        final float __result = nalGetSourcef(source, pname);
        return __result;
    }
    
    static native float nalGetSourcef(final int p0, final int p1);
    
    public static void alGetSource(final int source, final int pname, final FloatBuffer floatdata) {
        BufferChecks.checkBuffer(floatdata, 1);
        nalGetSourcefv(source, pname, MemoryUtil.getAddress(floatdata));
    }
    
    static native void nalGetSourcefv(final int p0, final int p1, final long p2);
    
    public static void alSourcePlay(final IntBuffer sources) {
        BufferChecks.checkDirect(sources);
        nalSourcePlayv(sources.remaining(), MemoryUtil.getAddress(sources));
    }
    
    static native void nalSourcePlayv(final int p0, final long p1);
    
    public static void alSourcePause(final IntBuffer sources) {
        BufferChecks.checkDirect(sources);
        nalSourcePausev(sources.remaining(), MemoryUtil.getAddress(sources));
    }
    
    static native void nalSourcePausev(final int p0, final long p1);
    
    public static void alSourceStop(final IntBuffer sources) {
        BufferChecks.checkDirect(sources);
        nalSourceStopv(sources.remaining(), MemoryUtil.getAddress(sources));
    }
    
    static native void nalSourceStopv(final int p0, final long p1);
    
    public static void alSourceRewind(final IntBuffer sources) {
        BufferChecks.checkDirect(sources);
        nalSourceRewindv(sources.remaining(), MemoryUtil.getAddress(sources));
    }
    
    static native void nalSourceRewindv(final int p0, final long p1);
    
    public static void alSourcePlay(final int source) {
        nalSourcePlay(source);
    }
    
    static native void nalSourcePlay(final int p0);
    
    public static void alSourcePause(final int source) {
        nalSourcePause(source);
    }
    
    static native void nalSourcePause(final int p0);
    
    public static void alSourceStop(final int source) {
        nalSourceStop(source);
    }
    
    static native void nalSourceStop(final int p0);
    
    public static void alSourceRewind(final int source) {
        nalSourceRewind(source);
    }
    
    static native void nalSourceRewind(final int p0);
    
    public static void alGenBuffers(final IntBuffer buffers) {
        BufferChecks.checkDirect(buffers);
        nalGenBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers));
    }
    
    static native void nalGenBuffers(final int p0, final long p1);
    
    public static int alGenBuffers() {
        final int __result = nalGenBuffers2(1);
        return __result;
    }
    
    static native int nalGenBuffers2(final int p0);
    
    public static void alDeleteBuffers(final IntBuffer buffers) {
        BufferChecks.checkDirect(buffers);
        nalDeleteBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers));
    }
    
    static native void nalDeleteBuffers(final int p0, final long p1);
    
    public static void alDeleteBuffers(final int buffer) {
        nalDeleteBuffers2(1, buffer);
    }
    
    static native void nalDeleteBuffers2(final int p0, final int p1);
    
    public static boolean alIsBuffer(final int buffer) {
        final boolean __result = nalIsBuffer(buffer);
        return __result;
    }
    
    static native boolean nalIsBuffer(final int p0);
    
    public static void alBufferData(final int buffer, final int format, final ByteBuffer data, final int freq) {
        BufferChecks.checkDirect(data);
        nalBufferData(buffer, format, MemoryUtil.getAddress(data), data.remaining(), freq);
    }
    
    public static void alBufferData(final int buffer, final int format, final IntBuffer data, final int freq) {
        BufferChecks.checkDirect(data);
        nalBufferData(buffer, format, MemoryUtil.getAddress(data), data.remaining() << 2, freq);
    }
    
    public static void alBufferData(final int buffer, final int format, final ShortBuffer data, final int freq) {
        BufferChecks.checkDirect(data);
        nalBufferData(buffer, format, MemoryUtil.getAddress(data), data.remaining() << 1, freq);
    }
    
    static native void nalBufferData(final int p0, final int p1, final long p2, final int p3, final int p4);
    
    public static int alGetBufferi(final int buffer, final int pname) {
        final int __result = nalGetBufferi(buffer, pname);
        return __result;
    }
    
    static native int nalGetBufferi(final int p0, final int p1);
    
    public static float alGetBufferf(final int buffer, final int pname) {
        final float __result = nalGetBufferf(buffer, pname);
        return __result;
    }
    
    static native float nalGetBufferf(final int p0, final int p1);
    
    public static void alSourceQueueBuffers(final int source, final IntBuffer buffers) {
        BufferChecks.checkDirect(buffers);
        nalSourceQueueBuffers(source, buffers.remaining(), MemoryUtil.getAddress(buffers));
    }
    
    static native void nalSourceQueueBuffers(final int p0, final int p1, final long p2);
    
    public static void alSourceQueueBuffers(final int source, final int buffer) {
        nalSourceQueueBuffers2(source, 1, buffer);
    }
    
    static native void nalSourceQueueBuffers2(final int p0, final int p1, final int p2);
    
    public static void alSourceUnqueueBuffers(final int source, final IntBuffer buffers) {
        BufferChecks.checkDirect(buffers);
        nalSourceUnqueueBuffers(source, buffers.remaining(), MemoryUtil.getAddress(buffers));
    }
    
    static native void nalSourceUnqueueBuffers(final int p0, final int p1, final long p2);
    
    public static int alSourceUnqueueBuffers(final int source) {
        final int __result = nalSourceUnqueueBuffers2(source, 1);
        return __result;
    }
    
    static native int nalSourceUnqueueBuffers2(final int p0, final int p1);
    
    public static void alDistanceModel(final int value) {
        nalDistanceModel(value);
    }
    
    static native void nalDistanceModel(final int p0);
    
    public static void alDopplerFactor(final float value) {
        nalDopplerFactor(value);
    }
    
    static native void nalDopplerFactor(final float p0);
    
    public static void alDopplerVelocity(final float value) {
        nalDopplerVelocity(value);
    }
    
    static native void nalDopplerVelocity(final float p0);
}
