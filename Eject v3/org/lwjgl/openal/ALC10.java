package org.lwjgl.openal;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public final class ALC10 {
    public static final int ALC_INVALID = 0;
    public static final int ALC_FALSE = 0;
    public static final int ALC_TRUE = 1;
    public static final int ALC_NO_ERROR = 0;
    public static final int ALC_MAJOR_VERSION = 4096;
    public static final int ALC_MINOR_VERSION = 4097;
    public static final int ALC_ATTRIBUTES_SIZE = 4098;
    public static final int ALC_ALL_ATTRIBUTES = 4099;
    public static final int ALC_DEFAULT_DEVICE_SPECIFIER = 4100;
    public static final int ALC_DEVICE_SPECIFIER = 4101;
    public static final int ALC_EXTENSIONS = 4102;
    public static final int ALC_FREQUENCY = 4103;
    public static final int ALC_REFRESH = 4104;
    public static final int ALC_SYNC = 4105;
    public static final int ALC_INVALID_DEVICE = 40961;
    public static final int ALC_INVALID_CONTEXT = 40962;
    public static final int ALC_INVALID_ENUM = 40963;
    public static final int ALC_INVALID_VALUE = 40964;
    public static final int ALC_OUT_OF_MEMORY = 40965;
    static final HashMap<Long, ALCcontext> contexts = new HashMap();
    static final HashMap<Long, ALCdevice> devices = new HashMap();

    static native void initNativeStubs()
            throws LWJGLException;

    public static String alcGetString(ALCdevice paramALCdevice, int paramInt) {
        ByteBuffer localByteBuffer = nalcGetString(getDevice(paramALCdevice), paramInt);
        Util.checkALCError(paramALCdevice);
        return MemoryUtil.decodeUTF8(localByteBuffer);
    }

    static native ByteBuffer nalcGetString(long paramLong, int paramInt);

    public static void alcGetInteger(ALCdevice paramALCdevice, int paramInt, IntBuffer paramIntBuffer) {
        BufferChecks.checkDirect(paramIntBuffer);
        nalcGetIntegerv(getDevice(paramALCdevice), paramInt, paramIntBuffer.remaining(), MemoryUtil.getAddress(paramIntBuffer));
        Util.checkALCError(paramALCdevice);
    }

    static native void nalcGetIntegerv(long paramLong1, int paramInt1, int paramInt2, long paramLong2);

    public static ALCdevice alcOpenDevice(String paramString) {
        ByteBuffer localByteBuffer = MemoryUtil.encodeUTF8(paramString);
        long l = nalcOpenDevice(MemoryUtil.getAddressSafe(localByteBuffer));
        if (l != 0L) {
            ALCdevice localALCdevice = new ALCdevice(l);
            synchronized (devices) {
                devices.put(Long.valueOf(l), localALCdevice);
            }
            return localALCdevice;
        }
        return null;
    }

    static native long nalcOpenDevice(long paramLong);

    public static boolean alcCloseDevice(ALCdevice paramALCdevice) {
        boolean bool = nalcCloseDevice(getDevice(paramALCdevice));
        synchronized (devices) {
            paramALCdevice.setInvalid();
            devices.remove(new Long(paramALCdevice.device));
        }
        return bool;
    }

    static native boolean nalcCloseDevice(long paramLong);

    public static ALCcontext alcCreateContext(ALCdevice paramALCdevice, IntBuffer paramIntBuffer) {
        long l = nalcCreateContext(getDevice(paramALCdevice), MemoryUtil.getAddressSafe(paramIntBuffer));
        Util.checkALCError(paramALCdevice);
        if (l != 0L) {
            ALCcontext localALCcontext = new ALCcontext(l);
            synchronized (contexts) {
                contexts.put(Long.valueOf(l), localALCcontext);
                paramALCdevice.addContext(localALCcontext);
            }
            return localALCcontext;
        }
        return null;
    }

    static native long nalcCreateContext(long paramLong1, long paramLong2);

    public static int alcMakeContextCurrent(ALCcontext paramALCcontext) {
        return nalcMakeContextCurrent(getContext(paramALCcontext));
    }

    static native int nalcMakeContextCurrent(long paramLong);

    public static void alcProcessContext(ALCcontext paramALCcontext) {
        nalcProcessContext(getContext(paramALCcontext));
    }

    static native void nalcProcessContext(long paramLong);

    public static ALCcontext alcGetCurrentContext() {
        ALCcontext localALCcontext = null;
        long l = nalcGetCurrentContext();
        if (l != 0L) {
            synchronized (contexts) {
                localALCcontext = (ALCcontext) contexts.get(Long.valueOf(l));
            }
        }
        return localALCcontext;
    }

    static native long nalcGetCurrentContext();

    public static ALCdevice alcGetContextsDevice(ALCcontext paramALCcontext) {
        ALCdevice localALCdevice = null;
        long l = nalcGetContextsDevice(getContext(paramALCcontext));
        if (l != 0L) {
            synchronized (devices) {
                localALCdevice = (ALCdevice) devices.get(Long.valueOf(l));
            }
        }
        return localALCdevice;
    }

    static native long nalcGetContextsDevice(long paramLong);

    public static void alcSuspendContext(ALCcontext paramALCcontext) {
        nalcSuspendContext(getContext(paramALCcontext));
    }

    static native void nalcSuspendContext(long paramLong);

    public static void alcDestroyContext(ALCcontext paramALCcontext) {
        synchronized (contexts) {
            ALCdevice localALCdevice = alcGetContextsDevice(paramALCcontext);
            nalcDestroyContext(getContext(paramALCcontext));
            localALCdevice.removeContext(paramALCcontext);
            paramALCcontext.setInvalid();
        }
    }

    static native void nalcDestroyContext(long paramLong);

    public static int alcGetError(ALCdevice paramALCdevice) {
        return nalcGetError(getDevice(paramALCdevice));
    }

    static native int nalcGetError(long paramLong);

    public static boolean alcIsExtensionPresent(ALCdevice paramALCdevice, String paramString) {
        ByteBuffer localByteBuffer = MemoryUtil.encodeASCII(paramString);
        boolean bool = nalcIsExtensionPresent(getDevice(paramALCdevice), MemoryUtil.getAddress(localByteBuffer));
        Util.checkALCError(paramALCdevice);
        return bool;
    }

    private static native boolean nalcIsExtensionPresent(long paramLong1, long paramLong2);

    public static int alcGetEnumValue(ALCdevice paramALCdevice, String paramString) {
        ByteBuffer localByteBuffer = MemoryUtil.encodeASCII(paramString);
        int i = nalcGetEnumValue(getDevice(paramALCdevice), MemoryUtil.getAddress(localByteBuffer));
        Util.checkALCError(paramALCdevice);
        return i;
    }

    private static native int nalcGetEnumValue(long paramLong1, long paramLong2);

    static long getDevice(ALCdevice paramALCdevice) {
        if (paramALCdevice != null) {
            Util.checkALCValidDevice(paramALCdevice);
            return paramALCdevice.device;
        }
        return 0L;
    }

    static long getContext(ALCcontext paramALCcontext) {
        if (paramALCcontext != null) {
            Util.checkALCValidContext(paramALCcontext);
            return paramALCcontext.context;
        }
        return 0L;
    }
}




