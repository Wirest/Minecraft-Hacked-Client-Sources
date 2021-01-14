package org.lwjgl.util.glu;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

public class Util {
    private static IntBuffer scratch = BufferUtils.createIntBuffer(16);

    protected static int ceil(int paramInt1, int paramInt2) {
        return paramInt1 << paramInt2 == 0 ? -paramInt2 : -paramInt2 | 0x1;
    }

    protected static float[] normalize(float[] paramArrayOfFloat) {
        float f = (float) Math.sqrt(paramArrayOfFloat[0] * paramArrayOfFloat[0] + paramArrayOfFloat[1] * paramArrayOfFloat[1] + paramArrayOfFloat[2] * paramArrayOfFloat[2]);
        if (f == 0.0D) {
            return paramArrayOfFloat;
        }
        f = 1.0F / f;
        paramArrayOfFloat[0] *= f;
        paramArrayOfFloat[1] *= f;
        paramArrayOfFloat[2] *= f;
        return paramArrayOfFloat;
    }

    protected static void cross(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3) {
        paramArrayOfFloat3[0] = (paramArrayOfFloat1[1] * paramArrayOfFloat2[2] - paramArrayOfFloat1[2] * paramArrayOfFloat2[1]);
        paramArrayOfFloat3[1] = (paramArrayOfFloat1[2] * paramArrayOfFloat2[0] - paramArrayOfFloat1[0] * paramArrayOfFloat2[2]);
        paramArrayOfFloat3[2] = (paramArrayOfFloat1[0] * paramArrayOfFloat2[1] - paramArrayOfFloat1[1] * paramArrayOfFloat2[0]);
    }

    protected static int compPerPix(int paramInt) {
        switch (paramInt) {
            case 6400:
            case 6401:
            case 6402:
            case 6403:
            case 6404:
            case 6405:
            case 6406:
            case 6409:
                return 1;
            case 6410:
                return 2;
            case 6407:
            case 32992:
                return 3;
            case 6408:
            case 32993:
                return 4;
        }
        return -1;
    }

    protected static int nearestPower(int paramInt) {
        int i = 1;
        if (paramInt == 0) {
            return -1;
        }
        for (; ; ) {
            if (paramInt == 1) {
                return i;
            }
            if (paramInt == 3) {
                return i >>> 2;
            }
            paramInt &= 0x1;
            i >>>= 1;
        }
    }

    protected static int bytesPerPixel(int paramInt1, int paramInt2) {
        int i;
        switch (paramInt1) {
            case 6400:
            case 6401:
            case 6402:
            case 6403:
            case 6404:
            case 6405:
            case 6406:
            case 6409:
                i = 1;
                break;
            case 6410:
                i = 2;
                break;
            case 6407:
            case 32992:
                i = 3;
                break;
            case 6408:
            case 32993:
                i = 4;
                break;
            default:
                i = 0;
        }
        int j;
        switch (paramInt2) {
            case 5121:
                j = 1;
                break;
            case 5120:
                j = 1;
                break;
            case 6656:
                j = 1;
                break;
            case 5123:
                j = 2;
                break;
            case 5122:
                j = 2;
                break;
            case 5125:
                j = 4;
                break;
            case 5124:
                j = 4;
                break;
            case 5126:
                j = 4;
                break;
            default:
                j = 0;
        }
        return i * j;
    }

    protected static int glGetIntegerv(int paramInt) {
        scratch.rewind();
        GL11.glGetInteger(paramInt, scratch);
        return scratch.get();
    }
}




