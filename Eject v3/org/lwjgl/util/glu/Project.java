package org.lwjgl.util.glu;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Project
        extends Util {
    private static final float[] IDENTITY_MATRIX = {1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F};
    private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer finalMatrix = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer tempMatrix = BufferUtils.createFloatBuffer(16);
    private static final float[] in = new float[4];
    private static final float[] out = new float[4];
    private static final float[] forward = new float[3];
    private static final float[] side = new float[3];
    private static final float[] up = new float[3];

    private static void __gluMakeIdentityf(FloatBuffer paramFloatBuffer) {
        int i = paramFloatBuffer.position();
        paramFloatBuffer.put(IDENTITY_MATRIX);
        paramFloatBuffer.position(i);
    }

    private static void __gluMultMatrixVecf(FloatBuffer paramFloatBuffer, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2) {
        for (int i = 0; i < 4; i++) {
            paramArrayOfFloat2[i] = (paramArrayOfFloat1[0] * paramFloatBuffer.get(paramFloatBuffer.position() | 0x0 | i) + paramArrayOfFloat1[1] * paramFloatBuffer.get(paramFloatBuffer.position() | 0x4 | i) + paramArrayOfFloat1[2] * paramFloatBuffer.get(paramFloatBuffer.position() | 0x8 | i) + paramArrayOfFloat1[3] * paramFloatBuffer.get(paramFloatBuffer.position() | 0xC | i));
        }
    }

    private static boolean __gluInvertMatrixf(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2) {
        FloatBuffer localFloatBuffer = tempMatrix;
        for (int i = 0; i < 16; i++) {
            localFloatBuffer.put(i, paramFloatBuffer1.get(i | paramFloatBuffer1.position()));
        }
        __gluMakeIdentityf(paramFloatBuffer2);
        for (i = 0; i < 4; i++) {
            int m = i;
            for (int j = i | 0x1; j < 4; j++) {
                if (Math.abs(localFloatBuffer.get(j * 4 | i)) > Math.abs(localFloatBuffer.get(i * 4 | i))) {
                    m = j;
                }
            }
            if (m != i) {
                for (k = 0; k < 4; k++) {
                    f = localFloatBuffer.get(i * 4 | k);
                    localFloatBuffer.put(i * 4 | k, localFloatBuffer.get(m * 4 | k));
                    localFloatBuffer.put(m * 4 | k, f);
                    f = paramFloatBuffer2.get(i * 4 | k);
                    paramFloatBuffer2.put(i * 4 | k, paramFloatBuffer2.get(m * 4 | k));
                    paramFloatBuffer2.put(m * 4 | k, f);
                }
            }
            if (localFloatBuffer.get(i * 4 | i) == 0.0F) {
                return false;
            }
            float f = localFloatBuffer.get(i * 4 | i);
            for (int k = 0; k < 4; k++) {
                localFloatBuffer.put(i * 4 | k, localFloatBuffer.get(i * 4 | k) / f);
                paramFloatBuffer2.put(i * 4 | k, paramFloatBuffer2.get(i * 4 | k) / f);
            }
            for (j = 0; j < 4; j++) {
                if (j != i) {
                    f = localFloatBuffer.get(j * 4 | i);
                    for (k = 0; k < 4; k++) {
                        localFloatBuffer.put(j * 4 | k, localFloatBuffer.get(j * 4 | k) - localFloatBuffer.get(i * 4 | k) * f);
                        paramFloatBuffer2.put(j * 4 | k, paramFloatBuffer2.get(j * 4 | k) - paramFloatBuffer2.get(i * 4 | k) * f);
                    }
                }
            }
        }
        return true;
    }

    private static void __gluMultMatricesf(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2, FloatBuffer paramFloatBuffer3) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                paramFloatBuffer3.put(paramFloatBuffer3.position() | i * 4 | j, paramFloatBuffer1.get(paramFloatBuffer1.position() | i * 4 | 0x0) * paramFloatBuffer2.get(paramFloatBuffer2.position() | 0x0 | j) + paramFloatBuffer1.get(paramFloatBuffer1.position() | i * 4 | 0x1) * paramFloatBuffer2.get(paramFloatBuffer2.position() | 0x4 | j) + paramFloatBuffer1.get(paramFloatBuffer1.position() | i * 4 | 0x2) * paramFloatBuffer2.get(paramFloatBuffer2.position() | 0x8 | j) + paramFloatBuffer1.get(paramFloatBuffer1.position() | i * 4 | 0x3) * paramFloatBuffer2.get(paramFloatBuffer2.position() | 0xC | j));
            }
        }
    }

    public static void gluPerspective(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        float f4 = paramFloat1 / 2.0F * 3.1415927F / 180.0F;
        float f3 = paramFloat4 - paramFloat3;
        float f1 = (float) Math.sin(f4);
        if ((f3 == 0.0F) || (f1 == 0.0F) || (paramFloat2 == 0.0F)) {
            return;
        }
        float f2 = (float) Math.cos(f4) / f1;
        __gluMakeIdentityf(matrix);
        matrix.put(0, f2 / paramFloat2);
        matrix.put(5, f2);
        matrix.put(10, -(paramFloat4 + paramFloat3) / f3);
        matrix.put(11, -1.0F);
        matrix.put(14, -2.0F * paramFloat3 * paramFloat4 / f3);
        matrix.put(15, 0.0F);
        GL11.glMultMatrix(matrix);
    }

    public static void gluLookAt(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9) {
        float[] arrayOfFloat1 = forward;
        float[] arrayOfFloat2 = side;
        float[] arrayOfFloat3 = up;
        arrayOfFloat1[0] = (paramFloat4 - paramFloat1);
        arrayOfFloat1[1] = (paramFloat5 - paramFloat2);
        arrayOfFloat1[2] = (paramFloat6 - paramFloat3);
        arrayOfFloat3[0] = paramFloat7;
        arrayOfFloat3[1] = paramFloat8;
        arrayOfFloat3[2] = paramFloat9;
        normalize(arrayOfFloat1);
        cross(arrayOfFloat1, arrayOfFloat3, arrayOfFloat2);
        normalize(arrayOfFloat2);
        cross(arrayOfFloat2, arrayOfFloat1, arrayOfFloat3);
        __gluMakeIdentityf(matrix);
        matrix.put(0, arrayOfFloat2[0]);
        matrix.put(4, arrayOfFloat2[1]);
        matrix.put(8, arrayOfFloat2[2]);
        matrix.put(1, arrayOfFloat3[0]);
        matrix.put(5, arrayOfFloat3[1]);
        matrix.put(9, arrayOfFloat3[2]);
        matrix.put(2, -arrayOfFloat1[0]);
        matrix.put(6, -arrayOfFloat1[1]);
        matrix.put(10, -arrayOfFloat1[2]);
        GL11.glMultMatrix(matrix);
        GL11.glTranslatef(-paramFloat1, -paramFloat2, -paramFloat3);
    }

    public static boolean gluProject(float paramFloat1, float paramFloat2, float paramFloat3, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2, IntBuffer paramIntBuffer, FloatBuffer paramFloatBuffer3) {
        float[] arrayOfFloat1 = in;
        float[] arrayOfFloat2 = out;
        arrayOfFloat1[0] = paramFloat1;
        arrayOfFloat1[1] = paramFloat2;
        arrayOfFloat1[2] = paramFloat3;
        arrayOfFloat1[3] = 1.0F;
        __gluMultMatrixVecf(paramFloatBuffer1, arrayOfFloat1, arrayOfFloat2);
        __gluMultMatrixVecf(paramFloatBuffer2, arrayOfFloat2, arrayOfFloat1);
        if (arrayOfFloat1[3] == 0.0D) {
            return false;
        }
        arrayOfFloat1[3] = (1.0F / arrayOfFloat1[3] * 0.5F);
        arrayOfFloat1[0] = (arrayOfFloat1[0] * arrayOfFloat1[3] + 0.5F);
        arrayOfFloat1[1] = (arrayOfFloat1[1] * arrayOfFloat1[3] + 0.5F);
        arrayOfFloat1[2] = (arrayOfFloat1[2] * arrayOfFloat1[3] + 0.5F);
        paramFloatBuffer3.put(0, arrayOfFloat1[0] * paramIntBuffer.get(paramIntBuffer.position() | 0x2) + paramIntBuffer.get(paramIntBuffer.position() | 0x0));
        paramFloatBuffer3.put(1, arrayOfFloat1[1] * paramIntBuffer.get(paramIntBuffer.position() | 0x3) + paramIntBuffer.get(paramIntBuffer.position() | 0x1));
        paramFloatBuffer3.put(2, arrayOfFloat1[2]);
        return true;
    }

    public static boolean gluUnProject(float paramFloat1, float paramFloat2, float paramFloat3, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2, IntBuffer paramIntBuffer, FloatBuffer paramFloatBuffer3) {
        float[] arrayOfFloat1 = in;
        float[] arrayOfFloat2 = out;
        __gluMultMatricesf(paramFloatBuffer1, paramFloatBuffer2, finalMatrix);
        if (!__gluInvertMatrixf(finalMatrix, finalMatrix)) {
            return false;
        }
        arrayOfFloat1[0] = paramFloat1;
        arrayOfFloat1[1] = paramFloat2;
        arrayOfFloat1[2] = paramFloat3;
        arrayOfFloat1[3] = 1.0F;
        arrayOfFloat1[0] = ((arrayOfFloat1[0] - paramIntBuffer.get(paramIntBuffer.position() | 0x0)) / paramIntBuffer.get(paramIntBuffer.position() | 0x2));
        arrayOfFloat1[1] = ((arrayOfFloat1[1] - paramIntBuffer.get(paramIntBuffer.position() | 0x1)) / paramIntBuffer.get(paramIntBuffer.position() | 0x3));
        arrayOfFloat1[0] = (arrayOfFloat1[0] * 2.0F - 1.0F);
        arrayOfFloat1[1] = (arrayOfFloat1[1] * 2.0F - 1.0F);
        arrayOfFloat1[2] = (arrayOfFloat1[2] * 2.0F - 1.0F);
        __gluMultMatrixVecf(finalMatrix, arrayOfFloat1, arrayOfFloat2);
        if (arrayOfFloat2[3] == 0.0D) {
            return false;
        }
        arrayOfFloat2[3] = (1.0F / arrayOfFloat2[3]);
        paramFloatBuffer3.put(paramFloatBuffer3.position() | 0x0, arrayOfFloat2[0] * arrayOfFloat2[3]);
        paramFloatBuffer3.put(paramFloatBuffer3.position() | 0x1, arrayOfFloat2[1] * arrayOfFloat2[3]);
        paramFloatBuffer3.put(paramFloatBuffer3.position() | 0x2, arrayOfFloat2[2] * arrayOfFloat2[3]);
        return true;
    }

    public static void gluPickMatrix(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, IntBuffer paramIntBuffer) {
        if ((paramFloat3 <= 0.0F) || (paramFloat4 <= 0.0F)) {
            return;
        }
        GL11.glTranslatef((paramIntBuffer.get(paramIntBuffer.position() | 0x2) - 2.0F * (paramFloat1 - paramIntBuffer.get(paramIntBuffer.position() | 0x0))) / paramFloat3, (paramIntBuffer.get(paramIntBuffer.position() | 0x3) - 2.0F * (paramFloat2 - paramIntBuffer.get(paramIntBuffer.position() | 0x1))) / paramFloat4, 0.0F);
        GL11.glScalef(paramIntBuffer.get(paramIntBuffer.position() | 0x2) / paramFloat3, paramIntBuffer.get(paramIntBuffer.position() | 0x3) / paramFloat4, 1.0F);
    }
}




