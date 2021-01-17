// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

final class CLChecks
{
    private CLChecks() {
    }
    
    static int calculateBufferRectSize(final PointerBuffer offset, final PointerBuffer region, long row_pitch, long slice_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        final long x = offset.get(0);
        final long y = offset.get(1);
        final long z = offset.get(2);
        if (LWJGLUtil.DEBUG && (x < 0L || y < 0L || z < 0L)) {
            throw new IllegalArgumentException("Invalid cl_mem host offset: " + x + ", " + y + ", " + z);
        }
        final long w = region.get(0);
        final long h = region.get(1);
        final long d = region.get(2);
        if (LWJGLUtil.DEBUG && (w < 1L || h < 1L || d < 1L)) {
            throw new IllegalArgumentException("Invalid cl_mem rectangle region dimensions: " + w + " x " + h + " x " + d);
        }
        if (row_pitch == 0L) {
            row_pitch = w;
        }
        else if (LWJGLUtil.DEBUG && row_pitch < w) {
            throw new IllegalArgumentException("Invalid host row pitch specified: " + row_pitch);
        }
        if (slice_pitch == 0L) {
            slice_pitch = row_pitch * h;
        }
        else if (LWJGLUtil.DEBUG && slice_pitch < row_pitch * h) {
            throw new IllegalArgumentException("Invalid host slice pitch specified: " + slice_pitch);
        }
        return (int)(z * slice_pitch + y * row_pitch + x + w * h * d);
    }
    
    static int calculateImageSize(final PointerBuffer region, long row_pitch, long slice_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        final long w = region.get(0);
        final long h = region.get(1);
        final long d = region.get(2);
        if (LWJGLUtil.DEBUG && (w < 1L || h < 1L || d < 1L)) {
            throw new IllegalArgumentException("Invalid cl_mem image region dimensions: " + w + " x " + h + " x " + d);
        }
        if (row_pitch == 0L) {
            row_pitch = w;
        }
        else if (LWJGLUtil.DEBUG && row_pitch < w) {
            throw new IllegalArgumentException("Invalid row pitch specified: " + row_pitch);
        }
        if (slice_pitch == 0L) {
            slice_pitch = row_pitch * h;
        }
        else if (LWJGLUtil.DEBUG && slice_pitch < row_pitch * h) {
            throw new IllegalArgumentException("Invalid slice pitch specified: " + slice_pitch);
        }
        return (int)(slice_pitch * d);
    }
    
    static int calculateImage2DSize(final ByteBuffer format, final long w, final long h, long row_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        if (LWJGLUtil.DEBUG && (w < 1L || h < 1L)) {
            throw new IllegalArgumentException("Invalid 2D image dimensions: " + w + " x " + h);
        }
        final int elementSize = getElementSize(format);
        if (row_pitch == 0L) {
            row_pitch = w * elementSize;
        }
        else if (LWJGLUtil.DEBUG && (row_pitch < w * elementSize || row_pitch % elementSize != 0L)) {
            throw new IllegalArgumentException("Invalid image_row_pitch specified: " + row_pitch);
        }
        return (int)(row_pitch * h);
    }
    
    static int calculateImage3DSize(final ByteBuffer format, final long w, final long h, final long d, long row_pitch, long slice_pitch) {
        if (!LWJGLUtil.CHECKS) {
            return 0;
        }
        if (LWJGLUtil.DEBUG && (w < 1L || h < 1L || d < 2L)) {
            throw new IllegalArgumentException("Invalid 3D image dimensions: " + w + " x " + h + " x " + d);
        }
        final int elementSize = getElementSize(format);
        if (row_pitch == 0L) {
            row_pitch = w * elementSize;
        }
        else if (LWJGLUtil.DEBUG && (row_pitch < w * elementSize || row_pitch % elementSize != 0L)) {
            throw new IllegalArgumentException("Invalid image_row_pitch specified: " + row_pitch);
        }
        if (slice_pitch == 0L) {
            slice_pitch = row_pitch * h;
        }
        else if (LWJGLUtil.DEBUG && (row_pitch < row_pitch * h || slice_pitch % row_pitch != 0L)) {
            throw new IllegalArgumentException("Invalid image_slice_pitch specified: " + row_pitch);
        }
        return (int)(slice_pitch * d);
    }
    
    private static int getElementSize(final ByteBuffer format) {
        final int channelOrder = format.getInt(format.position() + 0);
        final int channelType = format.getInt(format.position() + 4);
        return getChannelCount(channelOrder) * getChannelSize(channelType);
    }
    
    private static int getChannelCount(final int channelOrder) {
        switch (channelOrder) {
            case 4272:
            case 4273:
            case 4280:
            case 4281:
            case 4282: {
                return 1;
            }
            case 4274:
            case 4275:
            case 4283: {
                return 2;
            }
            case 4276:
            case 4284: {
                return 3;
            }
            case 4277:
            case 4278:
            case 4279: {
                return 4;
            }
            default: {
                throw new IllegalArgumentException("Invalid cl_channel_order specified: " + LWJGLUtil.toHexString(channelOrder));
            }
        }
    }
    
    private static int getChannelSize(final int channelType) {
        switch (channelType) {
            case 4304:
            case 4306:
            case 4311:
            case 4314: {
                return 1;
            }
            case 4305:
            case 4307:
            case 4308:
            case 4309:
            case 4312:
            case 4315:
            case 4317: {
                return 2;
            }
            case 4310:
            case 4313:
            case 4316:
            case 4318: {
                return 4;
            }
            default: {
                throw new IllegalArgumentException("Invalid cl_channel_type specified: " + LWJGLUtil.toHexString(channelType));
            }
        }
    }
}
