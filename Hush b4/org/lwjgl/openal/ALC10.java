// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

import org.lwjgl.BufferChecks;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import org.lwjgl.LWJGLException;
import java.util.HashMap;

public final class ALC10
{
    static final HashMap<Long, ALCcontext> contexts;
    static final HashMap<Long, ALCdevice> devices;
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
    
    static native void initNativeStubs() throws LWJGLException;
    
    public static String alcGetString(final ALCdevice device, final int pname) {
        final ByteBuffer buffer = nalcGetString(getDevice(device), pname);
        Util.checkALCError(device);
        return MemoryUtil.decodeUTF8(buffer);
    }
    
    static native ByteBuffer nalcGetString(final long p0, final int p1);
    
    public static void alcGetInteger(final ALCdevice device, final int pname, final IntBuffer integerdata) {
        BufferChecks.checkDirect(integerdata);
        nalcGetIntegerv(getDevice(device), pname, integerdata.remaining(), MemoryUtil.getAddress(integerdata));
        Util.checkALCError(device);
    }
    
    static native void nalcGetIntegerv(final long p0, final int p1, final int p2, final long p3);
    
    public static ALCdevice alcOpenDevice(final String devicename) {
        final ByteBuffer buffer = MemoryUtil.encodeUTF8(devicename);
        final long device_address = nalcOpenDevice(MemoryUtil.getAddressSafe(buffer));
        if (device_address != 0L) {
            final ALCdevice device = new ALCdevice(device_address);
            synchronized (ALC10.devices) {
                ALC10.devices.put(device_address, device);
            }
            return device;
        }
        return null;
    }
    
    static native long nalcOpenDevice(final long p0);
    
    public static boolean alcCloseDevice(final ALCdevice device) {
        final boolean result = nalcCloseDevice(getDevice(device));
        synchronized (ALC10.devices) {
            device.setInvalid();
            ALC10.devices.remove(new Long(device.device));
        }
        return result;
    }
    
    static native boolean nalcCloseDevice(final long p0);
    
    public static ALCcontext alcCreateContext(final ALCdevice device, final IntBuffer attrList) {
        final long context_address = nalcCreateContext(getDevice(device), MemoryUtil.getAddressSafe(attrList));
        Util.checkALCError(device);
        if (context_address != 0L) {
            final ALCcontext context = new ALCcontext(context_address);
            synchronized (ALC10.contexts) {
                ALC10.contexts.put(context_address, context);
                device.addContext(context);
            }
            return context;
        }
        return null;
    }
    
    static native long nalcCreateContext(final long p0, final long p1);
    
    public static int alcMakeContextCurrent(final ALCcontext context) {
        return nalcMakeContextCurrent(getContext(context));
    }
    
    static native int nalcMakeContextCurrent(final long p0);
    
    public static void alcProcessContext(final ALCcontext context) {
        nalcProcessContext(getContext(context));
    }
    
    static native void nalcProcessContext(final long p0);
    
    public static ALCcontext alcGetCurrentContext() {
        ALCcontext context = null;
        final long context_address = nalcGetCurrentContext();
        if (context_address != 0L) {
            synchronized (ALC10.contexts) {
                context = ALC10.contexts.get(context_address);
            }
        }
        return context;
    }
    
    static native long nalcGetCurrentContext();
    
    public static ALCdevice alcGetContextsDevice(final ALCcontext context) {
        ALCdevice device = null;
        final long device_address = nalcGetContextsDevice(getContext(context));
        if (device_address != 0L) {
            synchronized (ALC10.devices) {
                device = ALC10.devices.get(device_address);
            }
        }
        return device;
    }
    
    static native long nalcGetContextsDevice(final long p0);
    
    public static void alcSuspendContext(final ALCcontext context) {
        nalcSuspendContext(getContext(context));
    }
    
    static native void nalcSuspendContext(final long p0);
    
    public static void alcDestroyContext(final ALCcontext context) {
        synchronized (ALC10.contexts) {
            final ALCdevice device = alcGetContextsDevice(context);
            nalcDestroyContext(getContext(context));
            device.removeContext(context);
            context.setInvalid();
        }
    }
    
    static native void nalcDestroyContext(final long p0);
    
    public static int alcGetError(final ALCdevice device) {
        return nalcGetError(getDevice(device));
    }
    
    static native int nalcGetError(final long p0);
    
    public static boolean alcIsExtensionPresent(final ALCdevice device, final String extName) {
        final ByteBuffer buffer = MemoryUtil.encodeASCII(extName);
        final boolean result = nalcIsExtensionPresent(getDevice(device), MemoryUtil.getAddress(buffer));
        Util.checkALCError(device);
        return result;
    }
    
    private static native boolean nalcIsExtensionPresent(final long p0, final long p1);
    
    public static int alcGetEnumValue(final ALCdevice device, final String enumName) {
        final ByteBuffer buffer = MemoryUtil.encodeASCII(enumName);
        final int result = nalcGetEnumValue(getDevice(device), MemoryUtil.getAddress(buffer));
        Util.checkALCError(device);
        return result;
    }
    
    private static native int nalcGetEnumValue(final long p0, final long p1);
    
    static long getDevice(final ALCdevice device) {
        if (device != null) {
            Util.checkALCValidDevice(device);
            return device.device;
        }
        return 0L;
    }
    
    static long getContext(final ALCcontext context) {
        if (context != null) {
            Util.checkALCValidContext(context);
            return context.context;
        }
        return 0L;
    }
    
    static {
        contexts = new HashMap<Long, ALCcontext>();
        devices = new HashMap<Long, ALCdevice>();
    }
}
