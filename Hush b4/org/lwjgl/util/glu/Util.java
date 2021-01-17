// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import java.nio.IntBuffer;

public class Util
{
    private static IntBuffer scratch;
    
    protected static int ceil(final int a, final int b) {
        return (a % b == 0) ? (a / b) : (a / b + 1);
    }
    
    protected static float[] normalize(final float[] v) {
        float r = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        if (r == 0.0) {
            return v;
        }
        r = 1.0f / r;
        final int n = 0;
        v[n] *= r;
        final int n2 = 1;
        v[n2] *= r;
        final int n3 = 2;
        v[n3] *= r;
        return v;
    }
    
    protected static void cross(final float[] v1, final float[] v2, final float[] result) {
        result[0] = v1[1] * v2[2] - v1[2] * v2[1];
        result[1] = v1[2] * v2[0] - v1[0] * v2[2];
        result[2] = v1[0] * v2[1] - v1[1] * v2[0];
    }
    
    protected static int compPerPix(final int format) {
        switch (format) {
            case 6400:
            case 6401:
            case 6402:
            case 6403:
            case 6404:
            case 6405:
            case 6406:
            case 6409: {
                return 1;
            }
            case 6410: {
                return 2;
            }
            case 6407:
            case 32992: {
                return 3;
            }
            case 6408:
            case 32993: {
                return 4;
            }
            default: {
                return -1;
            }
        }
    }
    
    protected static int nearestPower(int value) {
        int i = 1;
        if (value == 0) {
            return -1;
        }
        while (value != 1) {
            if (value == 3) {
                return i << 2;
            }
            value >>= 1;
            i <<= 1;
        }
        return i;
    }
    
    protected static int bytesPerPixel(final int format, final int type) {
        int n = 0;
        switch (format) {
            case 6400:
            case 6401:
            case 6402:
            case 6403:
            case 6404:
            case 6405:
            case 6406:
            case 6409: {
                n = 1;
                break;
            }
            case 6410: {
                n = 2;
                break;
            }
            case 6407:
            case 32992: {
                n = 3;
                break;
            }
            case 6408:
            case 32993: {
                n = 4;
                break;
            }
            default: {
                n = 0;
                break;
            }
        }
        int m = 0;
        switch (type) {
            case 5121: {
                m = 1;
                break;
            }
            case 5120: {
                m = 1;
                break;
            }
            case 6656: {
                m = 1;
                break;
            }
            case 5123: {
                m = 2;
                break;
            }
            case 5122: {
                m = 2;
                break;
            }
            case 5125: {
                m = 4;
                break;
            }
            case 5124: {
                m = 4;
                break;
            }
            case 5126: {
                m = 4;
                break;
            }
            default: {
                m = 0;
                break;
            }
        }
        return n * m;
    }
    
    protected static int glGetIntegerv(final int what) {
        Util.scratch.rewind();
        GL11.glGetInteger(what, Util.scratch);
        return Util.scratch.get();
    }
    
    static {
        Util.scratch = BufferUtils.createIntBuffer(16);
    }
}
