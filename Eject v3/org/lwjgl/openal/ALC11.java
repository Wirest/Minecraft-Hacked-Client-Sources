package org.lwjgl.openal;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ALC11 {
    public static final int ALC_DEFAULT_ALL_DEVICES_SPECIFIER = 4114;
    public static final int ALC_ALL_DEVICES_SPECIFIER = 4115;
    public static final int ALC_CAPTURE_DEVICE_SPECIFIER = 784;
    public static final int ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER = 785;
    public static final int ALC_CAPTURE_SAMPLES = 786;
    public static final int ALC_MONO_SOURCES = 4112;
    public static final int ALC_STEREO_SOURCES = 4113;

    public static ALCdevice alcCaptureOpenDevice(String paramString, int paramInt1, int paramInt2, int paramInt3) {
        ByteBuffer localByteBuffer = MemoryUtil.encodeASCII(paramString);
        long l = nalcCaptureOpenDevice(MemoryUtil.getAddressSafe(localByteBuffer), paramInt1, paramInt2, paramInt3);
        if (l != 0L) {
            ALCdevice localALCdevice = new ALCdevice(l);
            synchronized (ALC10.devices) {
                ALC10.devices.put(Long.valueOf(l), localALCdevice);
            }
            return localALCdevice;
        }
        return null;
    }

    private static native long nalcCaptureOpenDevice(long paramLong, int paramInt1, int paramInt2, int paramInt3);

    public static boolean alcCaptureCloseDevice(ALCdevice paramALCdevice) {
        boolean bool = nalcCaptureCloseDevice(ALC10.getDevice(paramALCdevice));
        synchronized (ALC10.devices) {
            paramALCdevice.setInvalid();
            ALC10.devices.remove(new Long(paramALCdevice.device));
        }
        return bool;
    }

    static native boolean nalcCaptureCloseDevice(long paramLong);

    public static void alcCaptureStart(ALCdevice paramALCdevice) {
        nalcCaptureStart(ALC10.getDevice(paramALCdevice));
    }

    static native void nalcCaptureStart(long paramLong);

    public static void alcCaptureStop(ALCdevice paramALCdevice) {
        nalcCaptureStop(ALC10.getDevice(paramALCdevice));
    }

    static native void nalcCaptureStop(long paramLong);

    public static void alcCaptureSamples(ALCdevice paramALCdevice, ByteBuffer paramByteBuffer, int paramInt) {
        nalcCaptureSamples(ALC10.getDevice(paramALCdevice), MemoryUtil.getAddress(paramByteBuffer), paramInt);
    }

    static native void nalcCaptureSamples(long paramLong1, long paramLong2, int paramInt);

    static native void initNativeStubs()
            throws LWJGLException;

    static boolean initialize() {
        try {
            IntBuffer localIntBuffer = BufferUtils.createIntBuffer(2);
            ALC10.alcGetInteger(AL.getDevice(), 4096, localIntBuffer);
            localIntBuffer.position(1);
            ALC10.alcGetInteger(AL.getDevice(), 4097, localIntBuffer);
            int i = localIntBuffer.get(0);
            int j = localIntBuffer.get(1);
            if ((i >= 1) && ((i > 1) || (j >= 1))) {
                initNativeStubs();
                AL11.initNativeStubs();
            }
        } catch (LWJGLException localLWJGLException) {
            LWJGLUtil.log("failed to initialize ALC11: " + localLWJGLException);
            return false;
        }
        return true;
    }
}




