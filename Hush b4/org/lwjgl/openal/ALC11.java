// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

import java.nio.IntBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;

public final class ALC11
{
    public static final int ALC_DEFAULT_ALL_DEVICES_SPECIFIER = 4114;
    public static final int ALC_ALL_DEVICES_SPECIFIER = 4115;
    public static final int ALC_CAPTURE_DEVICE_SPECIFIER = 784;
    public static final int ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER = 785;
    public static final int ALC_CAPTURE_SAMPLES = 786;
    public static final int ALC_MONO_SOURCES = 4112;
    public static final int ALC_STEREO_SOURCES = 4113;
    
    public static ALCdevice alcCaptureOpenDevice(final String devicename, final int frequency, final int format, final int buffersize) {
        final ByteBuffer buffer = MemoryUtil.encodeASCII(devicename);
        final long device_address = nalcCaptureOpenDevice(MemoryUtil.getAddressSafe(buffer), frequency, format, buffersize);
        if (device_address != 0L) {
            final ALCdevice device = new ALCdevice(device_address);
            synchronized (ALC10.devices) {
                ALC10.devices.put(device_address, device);
            }
            return device;
        }
        return null;
    }
    
    private static native long nalcCaptureOpenDevice(final long p0, final int p1, final int p2, final int p3);
    
    public static boolean alcCaptureCloseDevice(final ALCdevice device) {
        final boolean result = nalcCaptureCloseDevice(ALC10.getDevice(device));
        synchronized (ALC10.devices) {
            device.setInvalid();
            ALC10.devices.remove(new Long(device.device));
        }
        return result;
    }
    
    static native boolean nalcCaptureCloseDevice(final long p0);
    
    public static void alcCaptureStart(final ALCdevice device) {
        nalcCaptureStart(ALC10.getDevice(device));
    }
    
    static native void nalcCaptureStart(final long p0);
    
    public static void alcCaptureStop(final ALCdevice device) {
        nalcCaptureStop(ALC10.getDevice(device));
    }
    
    static native void nalcCaptureStop(final long p0);
    
    public static void alcCaptureSamples(final ALCdevice device, final ByteBuffer buffer, final int samples) {
        nalcCaptureSamples(ALC10.getDevice(device), MemoryUtil.getAddress(buffer), samples);
    }
    
    static native void nalcCaptureSamples(final long p0, final long p1, final int p2);
    
    static native void initNativeStubs() throws LWJGLException;
    
    static boolean initialize() {
        try {
            final IntBuffer ib = BufferUtils.createIntBuffer(2);
            ALC10.alcGetInteger(AL.getDevice(), 4096, ib);
            ib.position(1);
            ALC10.alcGetInteger(AL.getDevice(), 4097, ib);
            final int major = ib.get(0);
            final int minor = ib.get(1);
            if (major >= 1 && (major > 1 || minor >= 1)) {
                initNativeStubs();
                AL11.initNativeStubs();
            }
        }
        catch (LWJGLException le) {
            LWJGLUtil.log("failed to initialize ALC11: " + le);
            return false;
        }
        return true;
    }
}
