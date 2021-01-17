// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;

public class MipMap extends Util
{
    public static int gluBuild2DMipmaps(final int target, final int components, final int width, final int height, final int format, final int type, final ByteBuffer data) {
        if (width < 1 || height < 1) {
            return 100901;
        }
        final int bpp = Util.bytesPerPixel(format, type);
        if (bpp == 0) {
            return 100900;
        }
        final int maxSize = Util.glGetIntegerv(3379);
        int w = Util.nearestPower(width);
        if (w > maxSize) {
            w = maxSize;
        }
        int h = Util.nearestPower(height);
        if (h > maxSize) {
            h = maxSize;
        }
        final PixelStoreState pss = new PixelStoreState();
        GL11.glPixelStorei(3330, 0);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3331, 0);
        GL11.glPixelStorei(3332, 0);
        int retVal = 0;
        boolean done = false;
        ByteBuffer image;
        if (w != width || h != height) {
            image = BufferUtils.createByteBuffer((w + 4) * h * bpp);
            final int error = gluScaleImage(format, width, height, type, data, w, h, type, image);
            if (error != 0) {
                retVal = error;
                done = true;
            }
            GL11.glPixelStorei(3314, 0);
            GL11.glPixelStorei(3317, 1);
            GL11.glPixelStorei(3315, 0);
            GL11.glPixelStorei(3316, 0);
        }
        else {
            image = data;
        }
        ByteBuffer bufferA = null;
        ByteBuffer bufferB = null;
        int level = 0;
        while (!done) {
            if (image != data) {
                GL11.glPixelStorei(3314, 0);
                GL11.glPixelStorei(3317, 1);
                GL11.glPixelStorei(3315, 0);
                GL11.glPixelStorei(3316, 0);
            }
            GL11.glTexImage2D(target, level, components, w, h, 0, format, type, image);
            if (w == 1 && h == 1) {
                break;
            }
            final int newW = (w < 2) ? 1 : (w >> 1);
            final int newH = (h < 2) ? 1 : (h >> 1);
            ByteBuffer newImage;
            if (bufferA == null) {
                bufferA = (newImage = BufferUtils.createByteBuffer((newW + 4) * newH * bpp));
            }
            else if (bufferB == null) {
                bufferB = (newImage = BufferUtils.createByteBuffer((newW + 4) * newH * bpp));
            }
            else {
                newImage = bufferB;
            }
            final int error2 = gluScaleImage(format, w, h, type, image, newW, newH, type, newImage);
            if (error2 != 0) {
                retVal = error2;
                done = true;
            }
            image = newImage;
            if (bufferB != null) {
                bufferB = bufferA;
            }
            w = newW;
            h = newH;
            ++level;
        }
        pss.save();
        return retVal;
    }
    
    public static int gluScaleImage(final int format, final int widthIn, final int heightIn, final int typein, final ByteBuffer dataIn, final int widthOut, final int heightOut, final int typeOut, final ByteBuffer dataOut) {
        final int components = Util.compPerPix(format);
        if (components == -1) {
            return 100900;
        }
        final float[] tempIn = new float[widthIn * heightIn * components];
        final float[] tempOut = new float[widthOut * heightOut * components];
        int sizein = 0;
        switch (typein) {
            case 5121: {
                sizein = 1;
                break;
            }
            case 5126: {
                sizein = 4;
                break;
            }
            default: {
                return 1280;
            }
        }
        int sizeout = 0;
        switch (typeOut) {
            case 5121: {
                sizeout = 1;
                break;
            }
            case 5126: {
                sizeout = 4;
                break;
            }
            default: {
                return 1280;
            }
        }
        final PixelStoreState pss = new PixelStoreState();
        int rowlen;
        if (pss.unpackRowLength > 0) {
            rowlen = pss.unpackRowLength;
        }
        else {
            rowlen = widthIn;
        }
        int rowstride;
        if (sizein >= pss.unpackAlignment) {
            rowstride = components * rowlen;
        }
        else {
            rowstride = pss.unpackAlignment / sizein * Util.ceil(components * rowlen * sizein, pss.unpackAlignment);
        }
        switch (typein) {
            case 5121: {
                int k = 0;
                dataIn.rewind();
                for (int i = 0; i < heightIn; ++i) {
                    int ubptr = i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components;
                    for (int j = 0; j < widthIn * components; ++j) {
                        tempIn[k++] = (float)(dataIn.get(ubptr++) & 0xFF);
                    }
                }
                break;
            }
            case 5126: {
                int k = 0;
                dataIn.rewind();
                for (int i = 0; i < heightIn; ++i) {
                    int fptr = 4 * (i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components);
                    for (int j = 0; j < widthIn * components; ++j) {
                        tempIn[k++] = dataIn.getFloat(fptr);
                        fptr += 4;
                    }
                }
                break;
            }
            default: {
                return 100900;
            }
        }
        final float sx = widthIn / (float)widthOut;
        final float sy = heightIn / (float)heightOut;
        final float[] c = new float[components];
        for (int iy = 0; iy < heightOut; ++iy) {
            for (int ix = 0; ix < widthOut; ++ix) {
                final int x0 = (int)(ix * sx);
                final int x2 = (int)((ix + 1) * sx);
                final int y0 = (int)(iy * sy);
                final int y2 = (int)((iy + 1) * sy);
                int readPix = 0;
                for (int ic = 0; ic < components; ++ic) {
                    c[ic] = 0.0f;
                }
                for (int ix2 = x0; ix2 < x2; ++ix2) {
                    for (int iy2 = y0; iy2 < y2; ++iy2) {
                        final int src = (iy2 * widthIn + ix2) * components;
                        for (int ic2 = 0; ic2 < components; ++ic2) {
                            final float[] array = c;
                            final int n = ic2;
                            array[n] += tempIn[src + ic2];
                        }
                        ++readPix;
                    }
                }
                int dst = (iy * widthOut + ix) * components;
                if (readPix == 0) {
                    final int src = (y0 * widthIn + x0) * components;
                    for (int ic = 0; ic < components; ++ic) {
                        tempOut[dst++] = tempIn[src + ic];
                    }
                }
                else {
                    for (int k = 0; k < components; ++k) {
                        tempOut[dst++] = c[k] / readPix;
                    }
                }
            }
        }
        if (pss.packRowLength > 0) {
            rowlen = pss.packRowLength;
        }
        else {
            rowlen = widthOut;
        }
        if (sizeout >= pss.packAlignment) {
            rowstride = components * rowlen;
        }
        else {
            rowstride = pss.packAlignment / sizeout * Util.ceil(components * rowlen * sizeout, pss.packAlignment);
        }
        switch (typeOut) {
            case 5121: {
                int k = 0;
                for (int i = 0; i < heightOut; ++i) {
                    int ubptr2 = i * rowstride + pss.packSkipRows * rowstride + pss.packSkipPixels * components;
                    for (int j = 0; j < widthOut * components; ++j) {
                        dataOut.put(ubptr2++, (byte)tempOut[k++]);
                    }
                }
                break;
            }
            case 5126: {
                int k = 0;
                for (int i = 0; i < heightOut; ++i) {
                    int fptr2 = 4 * (i * rowstride + pss.unpackSkipRows * rowstride + pss.unpackSkipPixels * components);
                    for (int j = 0; j < widthOut * components; ++j) {
                        dataOut.putFloat(fptr2, tempOut[k++]);
                        fptr2 += 4;
                    }
                }
                break;
            }
            default: {
                return 100900;
            }
        }
        return 0;
    }
}
