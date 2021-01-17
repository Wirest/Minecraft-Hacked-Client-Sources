// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Matrix4d implements Serializable, Cloneable
{
    static final long serialVersionUID = 8223903484171633710L;
    public double m00;
    public double m01;
    public double m02;
    public double m03;
    public double m10;
    public double m11;
    public double m12;
    public double m13;
    public double m20;
    public double m21;
    public double m22;
    public double m23;
    public double m30;
    public double m31;
    public double m32;
    public double m33;
    private static final double EPS = 1.0E-10;
    
    public Matrix4d(final double m00, final double m2, final double m3, final double m4, final double m5, final double m6, final double m7, final double m8, final double m9, final double m10, final double m11, final double m12, final double m13, final double m14, final double m15, final double m16) {
        this.m00 = m00;
        this.m01 = m2;
        this.m02 = m3;
        this.m03 = m4;
        this.m10 = m5;
        this.m11 = m6;
        this.m12 = m7;
        this.m13 = m8;
        this.m20 = m9;
        this.m21 = m10;
        this.m22 = m11;
        this.m23 = m12;
        this.m30 = m13;
        this.m31 = m14;
        this.m32 = m15;
        this.m33 = m16;
    }
    
    public Matrix4d(final double[] array) {
        this.m00 = array[0];
        this.m01 = array[1];
        this.m02 = array[2];
        this.m03 = array[3];
        this.m10 = array[4];
        this.m11 = array[5];
        this.m12 = array[6];
        this.m13 = array[7];
        this.m20 = array[8];
        this.m21 = array[9];
        this.m22 = array[10];
        this.m23 = array[11];
        this.m30 = array[12];
        this.m31 = array[13];
        this.m32 = array[14];
        this.m33 = array[15];
    }
    
    public Matrix4d(final Quat4d quat4d, final Vector3d vector3d, final double n) {
        this.m00 = n * (1.0 - 2.0 * quat4d.y * quat4d.y - 2.0 * quat4d.z * quat4d.z);
        this.m10 = n * (2.0 * (quat4d.x * quat4d.y + quat4d.w * quat4d.z));
        this.m20 = n * (2.0 * (quat4d.x * quat4d.z - quat4d.w * quat4d.y));
        this.m01 = n * (2.0 * (quat4d.x * quat4d.y - quat4d.w * quat4d.z));
        this.m11 = n * (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.z * quat4d.z);
        this.m21 = n * (2.0 * (quat4d.y * quat4d.z + quat4d.w * quat4d.x));
        this.m02 = n * (2.0 * (quat4d.x * quat4d.z + quat4d.w * quat4d.y));
        this.m12 = n * (2.0 * (quat4d.y * quat4d.z - quat4d.w * quat4d.x));
        this.m22 = n * (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.y * quat4d.y);
        this.m03 = vector3d.x;
        this.m13 = vector3d.y;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public Matrix4d(final Quat4f quat4f, final Vector3d vector3d, final double n) {
        this.m00 = n * (1.0 - 2.0 * quat4f.y * quat4f.y - 2.0 * quat4f.z * quat4f.z);
        this.m10 = n * (2.0 * (quat4f.x * quat4f.y + quat4f.w * quat4f.z));
        this.m20 = n * (2.0 * (quat4f.x * quat4f.z - quat4f.w * quat4f.y));
        this.m01 = n * (2.0 * (quat4f.x * quat4f.y - quat4f.w * quat4f.z));
        this.m11 = n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.z * quat4f.z);
        this.m21 = n * (2.0 * (quat4f.y * quat4f.z + quat4f.w * quat4f.x));
        this.m02 = n * (2.0 * (quat4f.x * quat4f.z + quat4f.w * quat4f.y));
        this.m12 = n * (2.0 * (quat4f.y * quat4f.z - quat4f.w * quat4f.x));
        this.m22 = n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.y * quat4f.y);
        this.m03 = vector3d.x;
        this.m13 = vector3d.y;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public Matrix4d(final Matrix4d matrix4d) {
        this.m00 = matrix4d.m00;
        this.m01 = matrix4d.m01;
        this.m02 = matrix4d.m02;
        this.m03 = matrix4d.m03;
        this.m10 = matrix4d.m10;
        this.m11 = matrix4d.m11;
        this.m12 = matrix4d.m12;
        this.m13 = matrix4d.m13;
        this.m20 = matrix4d.m20;
        this.m21 = matrix4d.m21;
        this.m22 = matrix4d.m22;
        this.m23 = matrix4d.m23;
        this.m30 = matrix4d.m30;
        this.m31 = matrix4d.m31;
        this.m32 = matrix4d.m32;
        this.m33 = matrix4d.m33;
    }
    
    public Matrix4d(final Matrix4f matrix4f) {
        this.m00 = matrix4f.m00;
        this.m01 = matrix4f.m01;
        this.m02 = matrix4f.m02;
        this.m03 = matrix4f.m03;
        this.m10 = matrix4f.m10;
        this.m11 = matrix4f.m11;
        this.m12 = matrix4f.m12;
        this.m13 = matrix4f.m13;
        this.m20 = matrix4f.m20;
        this.m21 = matrix4f.m21;
        this.m22 = matrix4f.m22;
        this.m23 = matrix4f.m23;
        this.m30 = matrix4f.m30;
        this.m31 = matrix4f.m31;
        this.m32 = matrix4f.m32;
        this.m33 = matrix4f.m33;
    }
    
    public Matrix4d(final Matrix3f matrix3f, final Vector3d vector3d, final double n) {
        this.m00 = matrix3f.m00 * n;
        this.m01 = matrix3f.m01 * n;
        this.m02 = matrix3f.m02 * n;
        this.m03 = vector3d.x;
        this.m10 = matrix3f.m10 * n;
        this.m11 = matrix3f.m11 * n;
        this.m12 = matrix3f.m12 * n;
        this.m13 = vector3d.y;
        this.m20 = matrix3f.m20 * n;
        this.m21 = matrix3f.m21 * n;
        this.m22 = matrix3f.m22 * n;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public Matrix4d(final Matrix3d matrix3d, final Vector3d vector3d, final double n) {
        this.m00 = matrix3d.m00 * n;
        this.m01 = matrix3d.m01 * n;
        this.m02 = matrix3d.m02 * n;
        this.m03 = vector3d.x;
        this.m10 = matrix3d.m10 * n;
        this.m11 = matrix3d.m11 * n;
        this.m12 = matrix3d.m12 * n;
        this.m13 = vector3d.y;
        this.m20 = matrix3d.m20 * n;
        this.m21 = matrix3d.m21 * n;
        this.m22 = matrix3d.m22 * n;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public Matrix4d() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 0.0;
    }
    
    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" + this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
    }
    
    public final void setIdentity() {
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void setElement(final int n, final int n2, final double n3) {
        Label_0350: {
            switch (n) {
                case 0: {
                    switch (n2) {
                        case 0: {
                            this.m00 = n3;
                            break Label_0350;
                        }
                        case 1: {
                            this.m01 = n3;
                            break Label_0350;
                        }
                        case 2: {
                            this.m02 = n3;
                            break Label_0350;
                        }
                        case 3: {
                            this.m03 = n3;
                            break Label_0350;
                        }
                        default: {
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
                        }
                    }
                    break;
                }
                case 1: {
                    switch (n2) {
                        case 0: {
                            this.m10 = n3;
                            break Label_0350;
                        }
                        case 1: {
                            this.m11 = n3;
                            break Label_0350;
                        }
                        case 2: {
                            this.m12 = n3;
                            break Label_0350;
                        }
                        case 3: {
                            this.m13 = n3;
                            break Label_0350;
                        }
                        default: {
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
                        }
                    }
                    break;
                }
                case 2: {
                    switch (n2) {
                        case 0: {
                            this.m20 = n3;
                            break Label_0350;
                        }
                        case 1: {
                            this.m21 = n3;
                            break Label_0350;
                        }
                        case 2: {
                            this.m22 = n3;
                            break Label_0350;
                        }
                        case 3: {
                            this.m23 = n3;
                            break Label_0350;
                        }
                        default: {
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
                        }
                    }
                    break;
                }
                case 3: {
                    switch (n2) {
                        case 0: {
                            this.m30 = n3;
                            break Label_0350;
                        }
                        case 1: {
                            this.m31 = n3;
                            break Label_0350;
                        }
                        case 2: {
                            this.m32 = n3;
                            break Label_0350;
                        }
                        case 3: {
                            this.m33 = n3;
                            break Label_0350;
                        }
                        default: {
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
                        }
                    }
                    break;
                }
                default: {
                    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d0"));
                }
            }
        }
    }
    
    public final double getElement(final int n, final int n2) {
        Label_0255: {
            switch (n) {
                case 0: {
                    switch (n2) {
                        case 0: {
                            return this.m00;
                        }
                        case 1: {
                            return this.m01;
                        }
                        case 2: {
                            return this.m02;
                        }
                        case 3: {
                            return this.m03;
                        }
                        default: {
                            break Label_0255;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (n2) {
                        case 0: {
                            return this.m10;
                        }
                        case 1: {
                            return this.m11;
                        }
                        case 2: {
                            return this.m12;
                        }
                        case 3: {
                            return this.m13;
                        }
                        default: {
                            break Label_0255;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (n2) {
                        case 0: {
                            return this.m20;
                        }
                        case 1: {
                            return this.m21;
                        }
                        case 2: {
                            return this.m22;
                        }
                        case 3: {
                            return this.m23;
                        }
                        default: {
                            break Label_0255;
                        }
                    }
                    break;
                }
                case 3: {
                    switch (n2) {
                        case 0: {
                            return this.m30;
                        }
                        case 1: {
                            return this.m31;
                        }
                        case 2: {
                            return this.m32;
                        }
                        case 3: {
                            return this.m33;
                        }
                        default: {
                            break Label_0255;
                        }
                    }
                    break;
                }
            }
        }
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d1"));
    }
    
    public final void getRow(final int n, final Vector4d vector4d) {
        if (n == 0) {
            vector4d.x = this.m00;
            vector4d.y = this.m01;
            vector4d.z = this.m02;
            vector4d.w = this.m03;
        }
        else if (n == 1) {
            vector4d.x = this.m10;
            vector4d.y = this.m11;
            vector4d.z = this.m12;
            vector4d.w = this.m13;
        }
        else if (n == 2) {
            vector4d.x = this.m20;
            vector4d.y = this.m21;
            vector4d.z = this.m22;
            vector4d.w = this.m23;
        }
        else {
            if (n != 3) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d2"));
            }
            vector4d.x = this.m30;
            vector4d.y = this.m31;
            vector4d.z = this.m32;
            vector4d.w = this.m33;
        }
    }
    
    public final void getRow(final int n, final double[] array) {
        if (n == 0) {
            array[0] = this.m00;
            array[1] = this.m01;
            array[2] = this.m02;
            array[3] = this.m03;
        }
        else if (n == 1) {
            array[0] = this.m10;
            array[1] = this.m11;
            array[2] = this.m12;
            array[3] = this.m13;
        }
        else if (n == 2) {
            array[0] = this.m20;
            array[1] = this.m21;
            array[2] = this.m22;
            array[3] = this.m23;
        }
        else {
            if (n != 3) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d2"));
            }
            array[0] = this.m30;
            array[1] = this.m31;
            array[2] = this.m32;
            array[3] = this.m33;
        }
    }
    
    public final void getColumn(final int n, final Vector4d vector4d) {
        if (n == 0) {
            vector4d.x = this.m00;
            vector4d.y = this.m10;
            vector4d.z = this.m20;
            vector4d.w = this.m30;
        }
        else if (n == 1) {
            vector4d.x = this.m01;
            vector4d.y = this.m11;
            vector4d.z = this.m21;
            vector4d.w = this.m31;
        }
        else if (n == 2) {
            vector4d.x = this.m02;
            vector4d.y = this.m12;
            vector4d.z = this.m22;
            vector4d.w = this.m32;
        }
        else {
            if (n != 3) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d3"));
            }
            vector4d.x = this.m03;
            vector4d.y = this.m13;
            vector4d.z = this.m23;
            vector4d.w = this.m33;
        }
    }
    
    public final void getColumn(final int n, final double[] array) {
        if (n == 0) {
            array[0] = this.m00;
            array[1] = this.m10;
            array[2] = this.m20;
            array[3] = this.m30;
        }
        else if (n == 1) {
            array[0] = this.m01;
            array[1] = this.m11;
            array[2] = this.m21;
            array[3] = this.m31;
        }
        else if (n == 2) {
            array[0] = this.m02;
            array[1] = this.m12;
            array[2] = this.m22;
            array[3] = this.m32;
        }
        else {
            if (n != 3) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d3"));
            }
            array[0] = this.m03;
            array[1] = this.m13;
            array[2] = this.m23;
            array[3] = this.m33;
        }
    }
    
    public final void get(final Matrix3d matrix3d) {
        final double[] array = new double[9];
        this.getScaleRotate(new double[3], array);
        matrix3d.m00 = array[0];
        matrix3d.m01 = array[1];
        matrix3d.m02 = array[2];
        matrix3d.m10 = array[3];
        matrix3d.m11 = array[4];
        matrix3d.m12 = array[5];
        matrix3d.m20 = array[6];
        matrix3d.m21 = array[7];
        matrix3d.m22 = array[8];
    }
    
    public final void get(final Matrix3f matrix3f) {
        final double[] array = new double[9];
        this.getScaleRotate(new double[3], array);
        matrix3f.m00 = (float)array[0];
        matrix3f.m01 = (float)array[1];
        matrix3f.m02 = (float)array[2];
        matrix3f.m10 = (float)array[3];
        matrix3f.m11 = (float)array[4];
        matrix3f.m12 = (float)array[5];
        matrix3f.m20 = (float)array[6];
        matrix3f.m21 = (float)array[7];
        matrix3f.m22 = (float)array[8];
    }
    
    public final double get(final Matrix3d matrix3d, final Vector3d vector3d) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        matrix3d.m00 = array[0];
        matrix3d.m01 = array[1];
        matrix3d.m02 = array[2];
        matrix3d.m10 = array[3];
        matrix3d.m11 = array[4];
        matrix3d.m12 = array[5];
        matrix3d.m20 = array[6];
        matrix3d.m21 = array[7];
        matrix3d.m22 = array[8];
        vector3d.x = this.m03;
        vector3d.y = this.m13;
        vector3d.z = this.m23;
        return Matrix3d.max3(array2);
    }
    
    public final double get(final Matrix3f matrix3f, final Vector3d vector3d) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        matrix3f.m00 = (float)array[0];
        matrix3f.m01 = (float)array[1];
        matrix3f.m02 = (float)array[2];
        matrix3f.m10 = (float)array[3];
        matrix3f.m11 = (float)array[4];
        matrix3f.m12 = (float)array[5];
        matrix3f.m20 = (float)array[6];
        matrix3f.m21 = (float)array[7];
        matrix3f.m22 = (float)array[8];
        vector3d.x = this.m03;
        vector3d.y = this.m13;
        vector3d.z = this.m23;
        return Matrix3d.max3(array2);
    }
    
    public final void get(final Quat4f quat4f) {
        final double[] array = new double[9];
        this.getScaleRotate(new double[3], array);
        final double a = 0.25 * (1.0 + array[0] + array[4] + array[8]);
        if (((a < 0.0) ? (-a) : a) >= 1.0E-30) {
            quat4f.w = (float)Math.sqrt(a);
            final double n = 0.25 / quat4f.w;
            quat4f.x = (float)((array[7] - array[5]) * n);
            quat4f.y = (float)((array[2] - array[6]) * n);
            quat4f.z = (float)((array[3] - array[1]) * n);
            return;
        }
        quat4f.w = 0.0f;
        final double a2 = -0.5 * (array[4] + array[8]);
        if (((a2 < 0.0) ? (-a2) : a2) >= 1.0E-30) {
            quat4f.x = (float)Math.sqrt(a2);
            final double n2 = 0.5 / quat4f.x;
            quat4f.y = (float)(array[3] * n2);
            quat4f.z = (float)(array[6] * n2);
            return;
        }
        quat4f.x = 0.0f;
        final double a3 = 0.5 * (1.0 - array[8]);
        if (((a3 < 0.0) ? (-a3) : a3) >= 1.0E-30) {
            quat4f.y = (float)Math.sqrt(a3);
            quat4f.z = (float)(array[7] / (2.0 * quat4f.y));
            return;
        }
        quat4f.y = 0.0f;
        quat4f.z = 1.0f;
    }
    
    public final void get(final Quat4d quat4d) {
        final double[] array = new double[9];
        this.getScaleRotate(new double[3], array);
        final double a = 0.25 * (1.0 + array[0] + array[4] + array[8]);
        if (((a < 0.0) ? (-a) : a) >= 1.0E-30) {
            quat4d.w = Math.sqrt(a);
            final double n = 0.25 / quat4d.w;
            quat4d.x = (array[7] - array[5]) * n;
            quat4d.y = (array[2] - array[6]) * n;
            quat4d.z = (array[3] - array[1]) * n;
            return;
        }
        quat4d.w = 0.0;
        final double a2 = -0.5 * (array[4] + array[8]);
        if (((a2 < 0.0) ? (-a2) : a2) >= 1.0E-30) {
            quat4d.x = Math.sqrt(a2);
            final double n2 = 0.5 / quat4d.x;
            quat4d.y = array[3] * n2;
            quat4d.z = array[6] * n2;
            return;
        }
        quat4d.x = 0.0;
        final double a3 = 0.5 * (1.0 - array[8]);
        if (((a3 < 0.0) ? (-a3) : a3) >= 1.0E-30) {
            quat4d.y = Math.sqrt(a3);
            quat4d.z = array[7] / (2.0 * quat4d.y);
            return;
        }
        quat4d.y = 0.0;
        quat4d.z = 1.0;
    }
    
    public final void get(final Vector3d vector3d) {
        vector3d.x = this.m03;
        vector3d.y = this.m13;
        vector3d.z = this.m23;
    }
    
    public final void getRotationScale(final Matrix3f matrix3f) {
        matrix3f.m00 = (float)this.m00;
        matrix3f.m01 = (float)this.m01;
        matrix3f.m02 = (float)this.m02;
        matrix3f.m10 = (float)this.m10;
        matrix3f.m11 = (float)this.m11;
        matrix3f.m12 = (float)this.m12;
        matrix3f.m20 = (float)this.m20;
        matrix3f.m21 = (float)this.m21;
        matrix3f.m22 = (float)this.m22;
    }
    
    public final void getRotationScale(final Matrix3d matrix3d) {
        matrix3d.m00 = this.m00;
        matrix3d.m01 = this.m01;
        matrix3d.m02 = this.m02;
        matrix3d.m10 = this.m10;
        matrix3d.m11 = this.m11;
        matrix3d.m12 = this.m12;
        matrix3d.m20 = this.m20;
        matrix3d.m21 = this.m21;
        matrix3d.m22 = this.m22;
    }
    
    public final double getScale() {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        return Matrix3d.max3(array2);
    }
    
    public final void setRotationScale(final Matrix3d matrix3d) {
        this.m00 = matrix3d.m00;
        this.m01 = matrix3d.m01;
        this.m02 = matrix3d.m02;
        this.m10 = matrix3d.m10;
        this.m11 = matrix3d.m11;
        this.m12 = matrix3d.m12;
        this.m20 = matrix3d.m20;
        this.m21 = matrix3d.m21;
        this.m22 = matrix3d.m22;
    }
    
    public final void setRotationScale(final Matrix3f matrix3f) {
        this.m00 = matrix3f.m00;
        this.m01 = matrix3f.m01;
        this.m02 = matrix3f.m02;
        this.m10 = matrix3f.m10;
        this.m11 = matrix3f.m11;
        this.m12 = matrix3f.m12;
        this.m20 = matrix3f.m20;
        this.m21 = matrix3f.m21;
        this.m22 = matrix3f.m22;
    }
    
    public final void setScale(final double n) {
        final double[] array = new double[9];
        this.getScaleRotate(new double[3], array);
        this.m00 = array[0] * n;
        this.m01 = array[1] * n;
        this.m02 = array[2] * n;
        this.m10 = array[3] * n;
        this.m11 = array[4] * n;
        this.m12 = array[5] * n;
        this.m20 = array[6] * n;
        this.m21 = array[7] * n;
        this.m22 = array[8] * n;
    }
    
    public final void setRow(final int n, final double n2, final double n3, final double n4, final double n5) {
        switch (n) {
            case 0: {
                this.m00 = n2;
                this.m01 = n3;
                this.m02 = n4;
                this.m03 = n5;
                break;
            }
            case 1: {
                this.m10 = n2;
                this.m11 = n3;
                this.m12 = n4;
                this.m13 = n5;
                break;
            }
            case 2: {
                this.m20 = n2;
                this.m21 = n3;
                this.m22 = n4;
                this.m23 = n5;
                break;
            }
            case 3: {
                this.m30 = n2;
                this.m31 = n3;
                this.m32 = n4;
                this.m33 = n5;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d4"));
            }
        }
    }
    
    public final void setRow(final int n, final Vector4d vector4d) {
        switch (n) {
            case 0: {
                this.m00 = vector4d.x;
                this.m01 = vector4d.y;
                this.m02 = vector4d.z;
                this.m03 = vector4d.w;
                break;
            }
            case 1: {
                this.m10 = vector4d.x;
                this.m11 = vector4d.y;
                this.m12 = vector4d.z;
                this.m13 = vector4d.w;
                break;
            }
            case 2: {
                this.m20 = vector4d.x;
                this.m21 = vector4d.y;
                this.m22 = vector4d.z;
                this.m23 = vector4d.w;
                break;
            }
            case 3: {
                this.m30 = vector4d.x;
                this.m31 = vector4d.y;
                this.m32 = vector4d.z;
                this.m33 = vector4d.w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d4"));
            }
        }
    }
    
    public final void setRow(final int n, final double[] array) {
        switch (n) {
            case 0: {
                this.m00 = array[0];
                this.m01 = array[1];
                this.m02 = array[2];
                this.m03 = array[3];
                break;
            }
            case 1: {
                this.m10 = array[0];
                this.m11 = array[1];
                this.m12 = array[2];
                this.m13 = array[3];
                break;
            }
            case 2: {
                this.m20 = array[0];
                this.m21 = array[1];
                this.m22 = array[2];
                this.m23 = array[3];
                break;
            }
            case 3: {
                this.m30 = array[0];
                this.m31 = array[1];
                this.m32 = array[2];
                this.m33 = array[3];
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d4"));
            }
        }
    }
    
    public final void setColumn(final int n, final double n2, final double n3, final double n4, final double n5) {
        switch (n) {
            case 0: {
                this.m00 = n2;
                this.m10 = n3;
                this.m20 = n4;
                this.m30 = n5;
                break;
            }
            case 1: {
                this.m01 = n2;
                this.m11 = n3;
                this.m21 = n4;
                this.m31 = n5;
                break;
            }
            case 2: {
                this.m02 = n2;
                this.m12 = n3;
                this.m22 = n4;
                this.m32 = n5;
                break;
            }
            case 3: {
                this.m03 = n2;
                this.m13 = n3;
                this.m23 = n4;
                this.m33 = n5;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d7"));
            }
        }
    }
    
    public final void setColumn(final int n, final Vector4d vector4d) {
        switch (n) {
            case 0: {
                this.m00 = vector4d.x;
                this.m10 = vector4d.y;
                this.m20 = vector4d.z;
                this.m30 = vector4d.w;
                break;
            }
            case 1: {
                this.m01 = vector4d.x;
                this.m11 = vector4d.y;
                this.m21 = vector4d.z;
                this.m31 = vector4d.w;
                break;
            }
            case 2: {
                this.m02 = vector4d.x;
                this.m12 = vector4d.y;
                this.m22 = vector4d.z;
                this.m32 = vector4d.w;
                break;
            }
            case 3: {
                this.m03 = vector4d.x;
                this.m13 = vector4d.y;
                this.m23 = vector4d.z;
                this.m33 = vector4d.w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d7"));
            }
        }
    }
    
    public final void setColumn(final int n, final double[] array) {
        switch (n) {
            case 0: {
                this.m00 = array[0];
                this.m10 = array[1];
                this.m20 = array[2];
                this.m30 = array[3];
                break;
            }
            case 1: {
                this.m01 = array[0];
                this.m11 = array[1];
                this.m21 = array[2];
                this.m31 = array[3];
                break;
            }
            case 2: {
                this.m02 = array[0];
                this.m12 = array[1];
                this.m22 = array[2];
                this.m32 = array[3];
                break;
            }
            case 3: {
                this.m03 = array[0];
                this.m13 = array[1];
                this.m23 = array[2];
                this.m33 = array[3];
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4d7"));
            }
        }
    }
    
    public final void add(final double n) {
        this.m00 += n;
        this.m01 += n;
        this.m02 += n;
        this.m03 += n;
        this.m10 += n;
        this.m11 += n;
        this.m12 += n;
        this.m13 += n;
        this.m20 += n;
        this.m21 += n;
        this.m22 += n;
        this.m23 += n;
        this.m30 += n;
        this.m31 += n;
        this.m32 += n;
        this.m33 += n;
    }
    
    public final void add(final double n, final Matrix4d matrix4d) {
        this.m00 = matrix4d.m00 + n;
        this.m01 = matrix4d.m01 + n;
        this.m02 = matrix4d.m02 + n;
        this.m03 = matrix4d.m03 + n;
        this.m10 = matrix4d.m10 + n;
        this.m11 = matrix4d.m11 + n;
        this.m12 = matrix4d.m12 + n;
        this.m13 = matrix4d.m13 + n;
        this.m20 = matrix4d.m20 + n;
        this.m21 = matrix4d.m21 + n;
        this.m22 = matrix4d.m22 + n;
        this.m23 = matrix4d.m23 + n;
        this.m30 = matrix4d.m30 + n;
        this.m31 = matrix4d.m31 + n;
        this.m32 = matrix4d.m32 + n;
        this.m33 = matrix4d.m33 + n;
    }
    
    public final void add(final Matrix4d matrix4d, final Matrix4d matrix4d2) {
        this.m00 = matrix4d.m00 + matrix4d2.m00;
        this.m01 = matrix4d.m01 + matrix4d2.m01;
        this.m02 = matrix4d.m02 + matrix4d2.m02;
        this.m03 = matrix4d.m03 + matrix4d2.m03;
        this.m10 = matrix4d.m10 + matrix4d2.m10;
        this.m11 = matrix4d.m11 + matrix4d2.m11;
        this.m12 = matrix4d.m12 + matrix4d2.m12;
        this.m13 = matrix4d.m13 + matrix4d2.m13;
        this.m20 = matrix4d.m20 + matrix4d2.m20;
        this.m21 = matrix4d.m21 + matrix4d2.m21;
        this.m22 = matrix4d.m22 + matrix4d2.m22;
        this.m23 = matrix4d.m23 + matrix4d2.m23;
        this.m30 = matrix4d.m30 + matrix4d2.m30;
        this.m31 = matrix4d.m31 + matrix4d2.m31;
        this.m32 = matrix4d.m32 + matrix4d2.m32;
        this.m33 = matrix4d.m33 + matrix4d2.m33;
    }
    
    public final void add(final Matrix4d matrix4d) {
        this.m00 += matrix4d.m00;
        this.m01 += matrix4d.m01;
        this.m02 += matrix4d.m02;
        this.m03 += matrix4d.m03;
        this.m10 += matrix4d.m10;
        this.m11 += matrix4d.m11;
        this.m12 += matrix4d.m12;
        this.m13 += matrix4d.m13;
        this.m20 += matrix4d.m20;
        this.m21 += matrix4d.m21;
        this.m22 += matrix4d.m22;
        this.m23 += matrix4d.m23;
        this.m30 += matrix4d.m30;
        this.m31 += matrix4d.m31;
        this.m32 += matrix4d.m32;
        this.m33 += matrix4d.m33;
    }
    
    public final void sub(final Matrix4d matrix4d, final Matrix4d matrix4d2) {
        this.m00 = matrix4d.m00 - matrix4d2.m00;
        this.m01 = matrix4d.m01 - matrix4d2.m01;
        this.m02 = matrix4d.m02 - matrix4d2.m02;
        this.m03 = matrix4d.m03 - matrix4d2.m03;
        this.m10 = matrix4d.m10 - matrix4d2.m10;
        this.m11 = matrix4d.m11 - matrix4d2.m11;
        this.m12 = matrix4d.m12 - matrix4d2.m12;
        this.m13 = matrix4d.m13 - matrix4d2.m13;
        this.m20 = matrix4d.m20 - matrix4d2.m20;
        this.m21 = matrix4d.m21 - matrix4d2.m21;
        this.m22 = matrix4d.m22 - matrix4d2.m22;
        this.m23 = matrix4d.m23 - matrix4d2.m23;
        this.m30 = matrix4d.m30 - matrix4d2.m30;
        this.m31 = matrix4d.m31 - matrix4d2.m31;
        this.m32 = matrix4d.m32 - matrix4d2.m32;
        this.m33 = matrix4d.m33 - matrix4d2.m33;
    }
    
    public final void sub(final Matrix4d matrix4d) {
        this.m00 -= matrix4d.m00;
        this.m01 -= matrix4d.m01;
        this.m02 -= matrix4d.m02;
        this.m03 -= matrix4d.m03;
        this.m10 -= matrix4d.m10;
        this.m11 -= matrix4d.m11;
        this.m12 -= matrix4d.m12;
        this.m13 -= matrix4d.m13;
        this.m20 -= matrix4d.m20;
        this.m21 -= matrix4d.m21;
        this.m22 -= matrix4d.m22;
        this.m23 -= matrix4d.m23;
        this.m30 -= matrix4d.m30;
        this.m31 -= matrix4d.m31;
        this.m32 -= matrix4d.m32;
        this.m33 -= matrix4d.m33;
    }
    
    public final void transpose() {
        final double m10 = this.m10;
        this.m10 = this.m01;
        this.m01 = m10;
        final double m11 = this.m20;
        this.m20 = this.m02;
        this.m02 = m11;
        final double m12 = this.m30;
        this.m30 = this.m03;
        this.m03 = m12;
        final double m13 = this.m21;
        this.m21 = this.m12;
        this.m12 = m13;
        final double m14 = this.m31;
        this.m31 = this.m13;
        this.m13 = m14;
        final double m15 = this.m32;
        this.m32 = this.m23;
        this.m23 = m15;
    }
    
    public final void transpose(final Matrix4d matrix4d) {
        if (this != matrix4d) {
            this.m00 = matrix4d.m00;
            this.m01 = matrix4d.m10;
            this.m02 = matrix4d.m20;
            this.m03 = matrix4d.m30;
            this.m10 = matrix4d.m01;
            this.m11 = matrix4d.m11;
            this.m12 = matrix4d.m21;
            this.m13 = matrix4d.m31;
            this.m20 = matrix4d.m02;
            this.m21 = matrix4d.m12;
            this.m22 = matrix4d.m22;
            this.m23 = matrix4d.m32;
            this.m30 = matrix4d.m03;
            this.m31 = matrix4d.m13;
            this.m32 = matrix4d.m23;
            this.m33 = matrix4d.m33;
        }
        else {
            this.transpose();
        }
    }
    
    public final void set(final double[] array) {
        this.m00 = array[0];
        this.m01 = array[1];
        this.m02 = array[2];
        this.m03 = array[3];
        this.m10 = array[4];
        this.m11 = array[5];
        this.m12 = array[6];
        this.m13 = array[7];
        this.m20 = array[8];
        this.m21 = array[9];
        this.m22 = array[10];
        this.m23 = array[11];
        this.m30 = array[12];
        this.m31 = array[13];
        this.m32 = array[14];
        this.m33 = array[15];
    }
    
    public final void set(final Matrix3f matrix3f) {
        this.m00 = matrix3f.m00;
        this.m01 = matrix3f.m01;
        this.m02 = matrix3f.m02;
        this.m03 = 0.0;
        this.m10 = matrix3f.m10;
        this.m11 = matrix3f.m11;
        this.m12 = matrix3f.m12;
        this.m13 = 0.0;
        this.m20 = matrix3f.m20;
        this.m21 = matrix3f.m21;
        this.m22 = matrix3f.m22;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Matrix3d matrix3d) {
        this.m00 = matrix3d.m00;
        this.m01 = matrix3d.m01;
        this.m02 = matrix3d.m02;
        this.m03 = 0.0;
        this.m10 = matrix3d.m10;
        this.m11 = matrix3d.m11;
        this.m12 = matrix3d.m12;
        this.m13 = 0.0;
        this.m20 = matrix3d.m20;
        this.m21 = matrix3d.m21;
        this.m22 = matrix3d.m22;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Quat4d quat4d) {
        this.m00 = 1.0 - 2.0 * quat4d.y * quat4d.y - 2.0 * quat4d.z * quat4d.z;
        this.m10 = 2.0 * (quat4d.x * quat4d.y + quat4d.w * quat4d.z);
        this.m20 = 2.0 * (quat4d.x * quat4d.z - quat4d.w * quat4d.y);
        this.m01 = 2.0 * (quat4d.x * quat4d.y - quat4d.w * quat4d.z);
        this.m11 = 1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.z * quat4d.z;
        this.m21 = 2.0 * (quat4d.y * quat4d.z + quat4d.w * quat4d.x);
        this.m02 = 2.0 * (quat4d.x * quat4d.z + quat4d.w * quat4d.y);
        this.m12 = 2.0 * (quat4d.y * quat4d.z - quat4d.w * quat4d.x);
        this.m22 = 1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.y * quat4d.y;
        this.m03 = 0.0;
        this.m13 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final AxisAngle4d axisAngle4d) {
        final double sqrt = Math.sqrt(axisAngle4d.x * axisAngle4d.x + axisAngle4d.y * axisAngle4d.y + axisAngle4d.z * axisAngle4d.z);
        if (sqrt < 1.0E-10) {
            this.m00 = 1.0;
            this.m01 = 0.0;
            this.m02 = 0.0;
            this.m10 = 0.0;
            this.m11 = 1.0;
            this.m12 = 0.0;
            this.m20 = 0.0;
            this.m21 = 0.0;
            this.m22 = 1.0;
        }
        else {
            final double n = 1.0 / sqrt;
            final double n2 = axisAngle4d.x * n;
            final double n3 = axisAngle4d.y * n;
            final double n4 = axisAngle4d.z * n;
            final double sin = Math.sin(axisAngle4d.angle);
            final double cos = Math.cos(axisAngle4d.angle);
            final double n5 = 1.0 - cos;
            final double n6 = n2 * n4;
            final double n7 = n2 * n3;
            final double n8 = n3 * n4;
            this.m00 = n5 * n2 * n2 + cos;
            this.m01 = n5 * n7 - sin * n4;
            this.m02 = n5 * n6 + sin * n3;
            this.m10 = n5 * n7 + sin * n4;
            this.m11 = n5 * n3 * n3 + cos;
            this.m12 = n5 * n8 - sin * n2;
            this.m20 = n5 * n6 - sin * n3;
            this.m21 = n5 * n8 + sin * n2;
            this.m22 = n5 * n4 * n4 + cos;
        }
        this.m03 = 0.0;
        this.m13 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Quat4f quat4f) {
        this.m00 = 1.0 - 2.0 * quat4f.y * quat4f.y - 2.0 * quat4f.z * quat4f.z;
        this.m10 = 2.0 * (quat4f.x * quat4f.y + quat4f.w * quat4f.z);
        this.m20 = 2.0 * (quat4f.x * quat4f.z - quat4f.w * quat4f.y);
        this.m01 = 2.0 * (quat4f.x * quat4f.y - quat4f.w * quat4f.z);
        this.m11 = 1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.z * quat4f.z;
        this.m21 = 2.0 * (quat4f.y * quat4f.z + quat4f.w * quat4f.x);
        this.m02 = 2.0 * (quat4f.x * quat4f.z + quat4f.w * quat4f.y);
        this.m12 = 2.0 * (quat4f.y * quat4f.z - quat4f.w * quat4f.x);
        this.m22 = 1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.y * quat4f.y;
        this.m03 = 0.0;
        this.m13 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final AxisAngle4f axisAngle4f) {
        final double sqrt = Math.sqrt(axisAngle4f.x * axisAngle4f.x + axisAngle4f.y * axisAngle4f.y + axisAngle4f.z * axisAngle4f.z);
        if (sqrt < 1.0E-10) {
            this.m00 = 1.0;
            this.m01 = 0.0;
            this.m02 = 0.0;
            this.m10 = 0.0;
            this.m11 = 1.0;
            this.m12 = 0.0;
            this.m20 = 0.0;
            this.m21 = 0.0;
            this.m22 = 1.0;
        }
        else {
            final double n = 1.0 / sqrt;
            final double n2 = axisAngle4f.x * n;
            final double n3 = axisAngle4f.y * n;
            final double n4 = axisAngle4f.z * n;
            final double sin = Math.sin(axisAngle4f.angle);
            final double cos = Math.cos(axisAngle4f.angle);
            final double n5 = 1.0 - cos;
            final double n6 = n2 * n4;
            final double n7 = n2 * n3;
            final double n8 = n3 * n4;
            this.m00 = n5 * n2 * n2 + cos;
            this.m01 = n5 * n7 - sin * n4;
            this.m02 = n5 * n6 + sin * n3;
            this.m10 = n5 * n7 + sin * n4;
            this.m11 = n5 * n3 * n3 + cos;
            this.m12 = n5 * n8 - sin * n2;
            this.m20 = n5 * n6 - sin * n3;
            this.m21 = n5 * n8 + sin * n2;
            this.m22 = n5 * n4 * n4 + cos;
        }
        this.m03 = 0.0;
        this.m13 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Quat4d quat4d, final Vector3d vector3d, final double n) {
        this.m00 = n * (1.0 - 2.0 * quat4d.y * quat4d.y - 2.0 * quat4d.z * quat4d.z);
        this.m10 = n * (2.0 * (quat4d.x * quat4d.y + quat4d.w * quat4d.z));
        this.m20 = n * (2.0 * (quat4d.x * quat4d.z - quat4d.w * quat4d.y));
        this.m01 = n * (2.0 * (quat4d.x * quat4d.y - quat4d.w * quat4d.z));
        this.m11 = n * (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.z * quat4d.z);
        this.m21 = n * (2.0 * (quat4d.y * quat4d.z + quat4d.w * quat4d.x));
        this.m02 = n * (2.0 * (quat4d.x * quat4d.z + quat4d.w * quat4d.y));
        this.m12 = n * (2.0 * (quat4d.y * quat4d.z - quat4d.w * quat4d.x));
        this.m22 = n * (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.y * quat4d.y);
        this.m03 = vector3d.x;
        this.m13 = vector3d.y;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Quat4f quat4f, final Vector3d vector3d, final double n) {
        this.m00 = n * (1.0 - 2.0 * quat4f.y * quat4f.y - 2.0 * quat4f.z * quat4f.z);
        this.m10 = n * (2.0 * (quat4f.x * quat4f.y + quat4f.w * quat4f.z));
        this.m20 = n * (2.0 * (quat4f.x * quat4f.z - quat4f.w * quat4f.y));
        this.m01 = n * (2.0 * (quat4f.x * quat4f.y - quat4f.w * quat4f.z));
        this.m11 = n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.z * quat4f.z);
        this.m21 = n * (2.0 * (quat4f.y * quat4f.z + quat4f.w * quat4f.x));
        this.m02 = n * (2.0 * (quat4f.x * quat4f.z + quat4f.w * quat4f.y));
        this.m12 = n * (2.0 * (quat4f.y * quat4f.z - quat4f.w * quat4f.x));
        this.m22 = n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.y * quat4f.y);
        this.m03 = vector3d.x;
        this.m13 = vector3d.y;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Quat4f quat4f, final Vector3f vector3f, final float n) {
        this.m00 = n * (1.0 - 2.0 * quat4f.y * quat4f.y - 2.0 * quat4f.z * quat4f.z);
        this.m10 = n * (2.0 * (quat4f.x * quat4f.y + quat4f.w * quat4f.z));
        this.m20 = n * (2.0 * (quat4f.x * quat4f.z - quat4f.w * quat4f.y));
        this.m01 = n * (2.0 * (quat4f.x * quat4f.y - quat4f.w * quat4f.z));
        this.m11 = n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.z * quat4f.z);
        this.m21 = n * (2.0 * (quat4f.y * quat4f.z + quat4f.w * quat4f.x));
        this.m02 = n * (2.0 * (quat4f.x * quat4f.z + quat4f.w * quat4f.y));
        this.m12 = n * (2.0 * (quat4f.y * quat4f.z - quat4f.w * quat4f.x));
        this.m22 = n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.y * quat4f.y);
        this.m03 = vector3f.x;
        this.m13 = vector3f.y;
        this.m23 = vector3f.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Matrix4f matrix4f) {
        this.m00 = matrix4f.m00;
        this.m01 = matrix4f.m01;
        this.m02 = matrix4f.m02;
        this.m03 = matrix4f.m03;
        this.m10 = matrix4f.m10;
        this.m11 = matrix4f.m11;
        this.m12 = matrix4f.m12;
        this.m13 = matrix4f.m13;
        this.m20 = matrix4f.m20;
        this.m21 = matrix4f.m21;
        this.m22 = matrix4f.m22;
        this.m23 = matrix4f.m23;
        this.m30 = matrix4f.m30;
        this.m31 = matrix4f.m31;
        this.m32 = matrix4f.m32;
        this.m33 = matrix4f.m33;
    }
    
    public final void set(final Matrix4d matrix4d) {
        this.m00 = matrix4d.m00;
        this.m01 = matrix4d.m01;
        this.m02 = matrix4d.m02;
        this.m03 = matrix4d.m03;
        this.m10 = matrix4d.m10;
        this.m11 = matrix4d.m11;
        this.m12 = matrix4d.m12;
        this.m13 = matrix4d.m13;
        this.m20 = matrix4d.m20;
        this.m21 = matrix4d.m21;
        this.m22 = matrix4d.m22;
        this.m23 = matrix4d.m23;
        this.m30 = matrix4d.m30;
        this.m31 = matrix4d.m31;
        this.m32 = matrix4d.m32;
        this.m33 = matrix4d.m33;
    }
    
    public final void invert(final Matrix4d matrix4d) {
        this.invertGeneral(matrix4d);
    }
    
    public final void invert() {
        this.invertGeneral(this);
    }
    
    final void invertGeneral(final Matrix4d matrix4d) {
        final double[] array = new double[16];
        final int[] array2 = new int[4];
        final double[] array3 = { matrix4d.m00, matrix4d.m01, matrix4d.m02, matrix4d.m03, matrix4d.m10, matrix4d.m11, matrix4d.m12, matrix4d.m13, matrix4d.m20, matrix4d.m21, matrix4d.m22, matrix4d.m23, matrix4d.m30, matrix4d.m31, matrix4d.m32, matrix4d.m33 };
        if (!luDecomposition(array3, array2)) {
            throw new SingularMatrixException(VecMathI18N.getString("Matrix4d10"));
        }
        for (int i = 0; i < 16; ++i) {
            array[i] = 0.0;
        }
        array[5] = (array[0] = 1.0);
        array[15] = (array[10] = 1.0);
        luBacksubstitution(array3, array2, array);
        this.m00 = array[0];
        this.m01 = array[1];
        this.m02 = array[2];
        this.m03 = array[3];
        this.m10 = array[4];
        this.m11 = array[5];
        this.m12 = array[6];
        this.m13 = array[7];
        this.m20 = array[8];
        this.m21 = array[9];
        this.m22 = array[10];
        this.m23 = array[11];
        this.m30 = array[12];
        this.m31 = array[13];
        this.m32 = array[14];
        this.m33 = array[15];
    }
    
    static boolean luDecomposition(final double[] array, final int[] array2) {
        final double[] array3 = new double[4];
        int n = 0;
        int n2 = 0;
        int n3 = 4;
        while (n3-- != 0) {
            double n4 = 0.0;
            int n5 = 4;
            while (n5-- != 0) {
                final double abs = Math.abs(array[n++]);
                if (abs > n4) {
                    n4 = abs;
                }
            }
            if (n4 == 0.0) {
                return false;
            }
            array3[n2++] = 1.0 / n4;
        }
        final int n6 = 0;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < i; ++j) {
                final int n7 = n6 + 4 * j + i;
                double n8 = array[n7];
                int n9 = j;
                int n10 = n6 + 4 * j;
                int n11 = n6 + i;
                while (n9-- != 0) {
                    n8 -= array[n10] * array[n11];
                    ++n10;
                    n11 += 4;
                }
                array[n7] = n8;
            }
            double n12 = 0.0;
            int n13 = -1;
            for (int k = i; k < 4; ++k) {
                final int n14 = n6 + 4 * k + i;
                double a = array[n14];
                int n15 = i;
                int n16 = n6 + 4 * k;
                int n17 = n6 + i;
                while (n15-- != 0) {
                    a -= array[n16] * array[n17];
                    ++n16;
                    n17 += 4;
                }
                array[n14] = a;
                final double n18;
                if ((n18 = array3[k] * Math.abs(a)) >= n12) {
                    n12 = n18;
                    n13 = k;
                }
            }
            if (n13 < 0) {
                throw new RuntimeException(VecMathI18N.getString("Matrix4d11"));
            }
            if (i != n13) {
                int n19 = 4;
                int n20 = n6 + 4 * n13;
                int n21 = n6 + 4 * i;
                while (n19-- != 0) {
                    final double n22 = array[n20];
                    array[n20++] = array[n21];
                    array[n21++] = n22;
                }
                array3[n13] = array3[i];
            }
            array2[i] = n13;
            if (array[n6 + 4 * i + i] == 0.0) {
                return false;
            }
            if (i != 3) {
                final double n23 = 1.0 / array[n6 + 4 * i + i];
                int n24 = n6 + 4 * (i + 1) + i;
                int n25 = 3 - i;
                while (n25-- != 0) {
                    final int n26 = n24;
                    array[n26] *= n23;
                    n24 += 4;
                }
            }
        }
        return true;
    }
    
    static void luBacksubstitution(final double[] array, final int[] array2, final double[] array3) {
        final int n = 0;
        for (int i = 0; i < 4; ++i) {
            final int n2 = i;
            int n3 = -1;
            for (int j = 0; j < 4; ++j) {
                final int n4 = array2[n + j];
                double n5 = array3[n2 + 4 * n4];
                array3[n2 + 4 * n4] = array3[n2 + 4 * j];
                if (n3 >= 0) {
                    final int n6 = j * 4;
                    for (int k = n3; k <= j - 1; ++k) {
                        n5 -= array[n6 + k] * array3[n2 + 4 * k];
                    }
                }
                else if (n5 != 0.0) {
                    n3 = j;
                }
                array3[n2 + 4 * j] = n5;
            }
            int n7 = 12;
            final int n8 = n2 + 12;
            array3[n8] /= array[n7 + 3];
            n7 -= 4;
            array3[n2 + 8] = (array3[n2 + 8] - array[n7 + 3] * array3[n2 + 12]) / array[n7 + 2];
            n7 -= 4;
            array3[n2 + 4] = (array3[n2 + 4] - array[n7 + 2] * array3[n2 + 8] - array[n7 + 3] * array3[n2 + 12]) / array[n7 + 1];
            n7 -= 4;
            array3[n2 + 0] = (array3[n2 + 0] - array[n7 + 1] * array3[n2 + 4] - array[n7 + 2] * array3[n2 + 8] - array[n7 + 3] * array3[n2 + 12]) / array[n7 + 0];
        }
    }
    
    public final double determinant() {
        return this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33) - this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33) + this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33) - this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
    }
    
    public final void set(final double m22) {
        this.m00 = m22;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = m22;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = m22;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Vector3d vector3d) {
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = vector3d.x;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = vector3d.y;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final double m22, final Vector3d vector3d) {
        this.m00 = m22;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = vector3d.x;
        this.m10 = 0.0;
        this.m11 = m22;
        this.m12 = 0.0;
        this.m13 = vector3d.y;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = m22;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Vector3d vector3d, final double m22) {
        this.m00 = m22;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = m22 * vector3d.x;
        this.m10 = 0.0;
        this.m11 = m22;
        this.m12 = 0.0;
        this.m13 = m22 * vector3d.y;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = m22;
        this.m23 = m22 * vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Matrix3f matrix3f, final Vector3f vector3f, final float n) {
        this.m00 = matrix3f.m00 * n;
        this.m01 = matrix3f.m01 * n;
        this.m02 = matrix3f.m02 * n;
        this.m03 = vector3f.x;
        this.m10 = matrix3f.m10 * n;
        this.m11 = matrix3f.m11 * n;
        this.m12 = matrix3f.m12 * n;
        this.m13 = vector3f.y;
        this.m20 = matrix3f.m20 * n;
        this.m21 = matrix3f.m21 * n;
        this.m22 = matrix3f.m22 * n;
        this.m23 = vector3f.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void set(final Matrix3d matrix3d, final Vector3d vector3d, final double n) {
        this.m00 = matrix3d.m00 * n;
        this.m01 = matrix3d.m01 * n;
        this.m02 = matrix3d.m02 * n;
        this.m03 = vector3d.x;
        this.m10 = matrix3d.m10 * n;
        this.m11 = matrix3d.m11 * n;
        this.m12 = matrix3d.m12 * n;
        this.m13 = vector3d.y;
        this.m20 = matrix3d.m20 * n;
        this.m21 = matrix3d.m21 * n;
        this.m22 = matrix3d.m22 * n;
        this.m23 = vector3d.z;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void setTranslation(final Vector3d vector3d) {
        this.m03 = vector3d.x;
        this.m13 = vector3d.y;
        this.m23 = vector3d.z;
    }
    
    public final void rotX(final double n) {
        final double sin = Math.sin(n);
        final double cos = Math.cos(n);
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = cos;
        this.m12 = -sin;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = sin;
        this.m22 = cos;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void rotY(final double n) {
        final double sin = Math.sin(n);
        final double cos = Math.cos(n);
        this.m00 = cos;
        this.m01 = 0.0;
        this.m02 = sin;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = -sin;
        this.m21 = 0.0;
        this.m22 = cos;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void rotZ(final double n) {
        final double sin = Math.sin(n);
        final double cos = Math.cos(n);
        this.m00 = cos;
        this.m01 = -sin;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = sin;
        this.m11 = cos;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }
    
    public final void mul(final double n) {
        this.m00 *= n;
        this.m01 *= n;
        this.m02 *= n;
        this.m03 *= n;
        this.m10 *= n;
        this.m11 *= n;
        this.m12 *= n;
        this.m13 *= n;
        this.m20 *= n;
        this.m21 *= n;
        this.m22 *= n;
        this.m23 *= n;
        this.m30 *= n;
        this.m31 *= n;
        this.m32 *= n;
        this.m33 *= n;
    }
    
    public final void mul(final double n, final Matrix4d matrix4d) {
        this.m00 = matrix4d.m00 * n;
        this.m01 = matrix4d.m01 * n;
        this.m02 = matrix4d.m02 * n;
        this.m03 = matrix4d.m03 * n;
        this.m10 = matrix4d.m10 * n;
        this.m11 = matrix4d.m11 * n;
        this.m12 = matrix4d.m12 * n;
        this.m13 = matrix4d.m13 * n;
        this.m20 = matrix4d.m20 * n;
        this.m21 = matrix4d.m21 * n;
        this.m22 = matrix4d.m22 * n;
        this.m23 = matrix4d.m23 * n;
        this.m30 = matrix4d.m30 * n;
        this.m31 = matrix4d.m31 * n;
        this.m32 = matrix4d.m32 * n;
        this.m33 = matrix4d.m33 * n;
    }
    
    public final void mul(final Matrix4d matrix4d) {
        final double m00 = this.m00 * matrix4d.m00 + this.m01 * matrix4d.m10 + this.m02 * matrix4d.m20 + this.m03 * matrix4d.m30;
        final double m2 = this.m00 * matrix4d.m01 + this.m01 * matrix4d.m11 + this.m02 * matrix4d.m21 + this.m03 * matrix4d.m31;
        final double m3 = this.m00 * matrix4d.m02 + this.m01 * matrix4d.m12 + this.m02 * matrix4d.m22 + this.m03 * matrix4d.m32;
        final double m4 = this.m00 * matrix4d.m03 + this.m01 * matrix4d.m13 + this.m02 * matrix4d.m23 + this.m03 * matrix4d.m33;
        final double m5 = this.m10 * matrix4d.m00 + this.m11 * matrix4d.m10 + this.m12 * matrix4d.m20 + this.m13 * matrix4d.m30;
        final double m6 = this.m10 * matrix4d.m01 + this.m11 * matrix4d.m11 + this.m12 * matrix4d.m21 + this.m13 * matrix4d.m31;
        final double m7 = this.m10 * matrix4d.m02 + this.m11 * matrix4d.m12 + this.m12 * matrix4d.m22 + this.m13 * matrix4d.m32;
        final double m8 = this.m10 * matrix4d.m03 + this.m11 * matrix4d.m13 + this.m12 * matrix4d.m23 + this.m13 * matrix4d.m33;
        final double m9 = this.m20 * matrix4d.m00 + this.m21 * matrix4d.m10 + this.m22 * matrix4d.m20 + this.m23 * matrix4d.m30;
        final double m10 = this.m20 * matrix4d.m01 + this.m21 * matrix4d.m11 + this.m22 * matrix4d.m21 + this.m23 * matrix4d.m31;
        final double m11 = this.m20 * matrix4d.m02 + this.m21 * matrix4d.m12 + this.m22 * matrix4d.m22 + this.m23 * matrix4d.m32;
        final double m12 = this.m20 * matrix4d.m03 + this.m21 * matrix4d.m13 + this.m22 * matrix4d.m23 + this.m23 * matrix4d.m33;
        final double m13 = this.m30 * matrix4d.m00 + this.m31 * matrix4d.m10 + this.m32 * matrix4d.m20 + this.m33 * matrix4d.m30;
        final double m14 = this.m30 * matrix4d.m01 + this.m31 * matrix4d.m11 + this.m32 * matrix4d.m21 + this.m33 * matrix4d.m31;
        final double m15 = this.m30 * matrix4d.m02 + this.m31 * matrix4d.m12 + this.m32 * matrix4d.m22 + this.m33 * matrix4d.m32;
        final double m16 = this.m30 * matrix4d.m03 + this.m31 * matrix4d.m13 + this.m32 * matrix4d.m23 + this.m33 * matrix4d.m33;
        this.m00 = m00;
        this.m01 = m2;
        this.m02 = m3;
        this.m03 = m4;
        this.m10 = m5;
        this.m11 = m6;
        this.m12 = m7;
        this.m13 = m8;
        this.m20 = m9;
        this.m21 = m10;
        this.m22 = m11;
        this.m23 = m12;
        this.m30 = m13;
        this.m31 = m14;
        this.m32 = m15;
        this.m33 = m16;
    }
    
    public final void mul(final Matrix4d matrix4d, final Matrix4d matrix4d2) {
        if (this != matrix4d && this != matrix4d2) {
            this.m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m01 * matrix4d2.m10 + matrix4d.m02 * matrix4d2.m20 + matrix4d.m03 * matrix4d2.m30;
            this.m01 = matrix4d.m00 * matrix4d2.m01 + matrix4d.m01 * matrix4d2.m11 + matrix4d.m02 * matrix4d2.m21 + matrix4d.m03 * matrix4d2.m31;
            this.m02 = matrix4d.m00 * matrix4d2.m02 + matrix4d.m01 * matrix4d2.m12 + matrix4d.m02 * matrix4d2.m22 + matrix4d.m03 * matrix4d2.m32;
            this.m03 = matrix4d.m00 * matrix4d2.m03 + matrix4d.m01 * matrix4d2.m13 + matrix4d.m02 * matrix4d2.m23 + matrix4d.m03 * matrix4d2.m33;
            this.m10 = matrix4d.m10 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m10 + matrix4d.m12 * matrix4d2.m20 + matrix4d.m13 * matrix4d2.m30;
            this.m11 = matrix4d.m10 * matrix4d2.m01 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m12 * matrix4d2.m21 + matrix4d.m13 * matrix4d2.m31;
            this.m12 = matrix4d.m10 * matrix4d2.m02 + matrix4d.m11 * matrix4d2.m12 + matrix4d.m12 * matrix4d2.m22 + matrix4d.m13 * matrix4d2.m32;
            this.m13 = matrix4d.m10 * matrix4d2.m03 + matrix4d.m11 * matrix4d2.m13 + matrix4d.m12 * matrix4d2.m23 + matrix4d.m13 * matrix4d2.m33;
            this.m20 = matrix4d.m20 * matrix4d2.m00 + matrix4d.m21 * matrix4d2.m10 + matrix4d.m22 * matrix4d2.m20 + matrix4d.m23 * matrix4d2.m30;
            this.m21 = matrix4d.m20 * matrix4d2.m01 + matrix4d.m21 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m21 + matrix4d.m23 * matrix4d2.m31;
            this.m22 = matrix4d.m20 * matrix4d2.m02 + matrix4d.m21 * matrix4d2.m12 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m23 * matrix4d2.m32;
            this.m23 = matrix4d.m20 * matrix4d2.m03 + matrix4d.m21 * matrix4d2.m13 + matrix4d.m22 * matrix4d2.m23 + matrix4d.m23 * matrix4d2.m33;
            this.m30 = matrix4d.m30 * matrix4d2.m00 + matrix4d.m31 * matrix4d2.m10 + matrix4d.m32 * matrix4d2.m20 + matrix4d.m33 * matrix4d2.m30;
            this.m31 = matrix4d.m30 * matrix4d2.m01 + matrix4d.m31 * matrix4d2.m11 + matrix4d.m32 * matrix4d2.m21 + matrix4d.m33 * matrix4d2.m31;
            this.m32 = matrix4d.m30 * matrix4d2.m02 + matrix4d.m31 * matrix4d2.m12 + matrix4d.m32 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m32;
            this.m33 = matrix4d.m30 * matrix4d2.m03 + matrix4d.m31 * matrix4d2.m13 + matrix4d.m32 * matrix4d2.m23 + matrix4d.m33 * matrix4d2.m33;
        }
        else {
            final double m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m01 * matrix4d2.m10 + matrix4d.m02 * matrix4d2.m20 + matrix4d.m03 * matrix4d2.m30;
            final double m2 = matrix4d.m00 * matrix4d2.m01 + matrix4d.m01 * matrix4d2.m11 + matrix4d.m02 * matrix4d2.m21 + matrix4d.m03 * matrix4d2.m31;
            final double m3 = matrix4d.m00 * matrix4d2.m02 + matrix4d.m01 * matrix4d2.m12 + matrix4d.m02 * matrix4d2.m22 + matrix4d.m03 * matrix4d2.m32;
            final double m4 = matrix4d.m00 * matrix4d2.m03 + matrix4d.m01 * matrix4d2.m13 + matrix4d.m02 * matrix4d2.m23 + matrix4d.m03 * matrix4d2.m33;
            final double m5 = matrix4d.m10 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m10 + matrix4d.m12 * matrix4d2.m20 + matrix4d.m13 * matrix4d2.m30;
            final double m6 = matrix4d.m10 * matrix4d2.m01 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m12 * matrix4d2.m21 + matrix4d.m13 * matrix4d2.m31;
            final double m7 = matrix4d.m10 * matrix4d2.m02 + matrix4d.m11 * matrix4d2.m12 + matrix4d.m12 * matrix4d2.m22 + matrix4d.m13 * matrix4d2.m32;
            final double m8 = matrix4d.m10 * matrix4d2.m03 + matrix4d.m11 * matrix4d2.m13 + matrix4d.m12 * matrix4d2.m23 + matrix4d.m13 * matrix4d2.m33;
            final double m9 = matrix4d.m20 * matrix4d2.m00 + matrix4d.m21 * matrix4d2.m10 + matrix4d.m22 * matrix4d2.m20 + matrix4d.m23 * matrix4d2.m30;
            final double m10 = matrix4d.m20 * matrix4d2.m01 + matrix4d.m21 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m21 + matrix4d.m23 * matrix4d2.m31;
            final double m11 = matrix4d.m20 * matrix4d2.m02 + matrix4d.m21 * matrix4d2.m12 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m23 * matrix4d2.m32;
            final double m12 = matrix4d.m20 * matrix4d2.m03 + matrix4d.m21 * matrix4d2.m13 + matrix4d.m22 * matrix4d2.m23 + matrix4d.m23 * matrix4d2.m33;
            final double m13 = matrix4d.m30 * matrix4d2.m00 + matrix4d.m31 * matrix4d2.m10 + matrix4d.m32 * matrix4d2.m20 + matrix4d.m33 * matrix4d2.m30;
            final double m14 = matrix4d.m30 * matrix4d2.m01 + matrix4d.m31 * matrix4d2.m11 + matrix4d.m32 * matrix4d2.m21 + matrix4d.m33 * matrix4d2.m31;
            final double m15 = matrix4d.m30 * matrix4d2.m02 + matrix4d.m31 * matrix4d2.m12 + matrix4d.m32 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m32;
            final double m16 = matrix4d.m30 * matrix4d2.m03 + matrix4d.m31 * matrix4d2.m13 + matrix4d.m32 * matrix4d2.m23 + matrix4d.m33 * matrix4d2.m33;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m03 = m4;
            this.m10 = m5;
            this.m11 = m6;
            this.m12 = m7;
            this.m13 = m8;
            this.m20 = m9;
            this.m21 = m10;
            this.m22 = m11;
            this.m23 = m12;
            this.m30 = m13;
            this.m31 = m14;
            this.m32 = m15;
            this.m33 = m16;
        }
    }
    
    public final void mulTransposeBoth(final Matrix4d matrix4d, final Matrix4d matrix4d2) {
        if (this != matrix4d && this != matrix4d2) {
            this.m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m10 * matrix4d2.m01 + matrix4d.m20 * matrix4d2.m02 + matrix4d.m30 * matrix4d2.m03;
            this.m01 = matrix4d.m00 * matrix4d2.m10 + matrix4d.m10 * matrix4d2.m11 + matrix4d.m20 * matrix4d2.m12 + matrix4d.m30 * matrix4d2.m13;
            this.m02 = matrix4d.m00 * matrix4d2.m20 + matrix4d.m10 * matrix4d2.m21 + matrix4d.m20 * matrix4d2.m22 + matrix4d.m30 * matrix4d2.m23;
            this.m03 = matrix4d.m00 * matrix4d2.m30 + matrix4d.m10 * matrix4d2.m31 + matrix4d.m20 * matrix4d2.m32 + matrix4d.m30 * matrix4d2.m33;
            this.m10 = matrix4d.m01 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m01 + matrix4d.m21 * matrix4d2.m02 + matrix4d.m31 * matrix4d2.m03;
            this.m11 = matrix4d.m01 * matrix4d2.m10 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m21 * matrix4d2.m12 + matrix4d.m31 * matrix4d2.m13;
            this.m12 = matrix4d.m01 * matrix4d2.m20 + matrix4d.m11 * matrix4d2.m21 + matrix4d.m21 * matrix4d2.m22 + matrix4d.m31 * matrix4d2.m23;
            this.m13 = matrix4d.m01 * matrix4d2.m30 + matrix4d.m11 * matrix4d2.m31 + matrix4d.m21 * matrix4d2.m32 + matrix4d.m31 * matrix4d2.m33;
            this.m20 = matrix4d.m02 * matrix4d2.m00 + matrix4d.m12 * matrix4d2.m01 + matrix4d.m22 * matrix4d2.m02 + matrix4d.m32 * matrix4d2.m03;
            this.m21 = matrix4d.m02 * matrix4d2.m10 + matrix4d.m12 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m12 + matrix4d.m32 * matrix4d2.m13;
            this.m22 = matrix4d.m02 * matrix4d2.m20 + matrix4d.m12 * matrix4d2.m21 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m32 * matrix4d2.m23;
            this.m23 = matrix4d.m02 * matrix4d2.m30 + matrix4d.m12 * matrix4d2.m31 + matrix4d.m22 * matrix4d2.m32 + matrix4d.m32 * matrix4d2.m33;
            this.m30 = matrix4d.m03 * matrix4d2.m00 + matrix4d.m13 * matrix4d2.m01 + matrix4d.m23 * matrix4d2.m02 + matrix4d.m33 * matrix4d2.m03;
            this.m31 = matrix4d.m03 * matrix4d2.m10 + matrix4d.m13 * matrix4d2.m11 + matrix4d.m23 * matrix4d2.m12 + matrix4d.m33 * matrix4d2.m13;
            this.m32 = matrix4d.m03 * matrix4d2.m20 + matrix4d.m13 * matrix4d2.m21 + matrix4d.m23 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m23;
            this.m33 = matrix4d.m03 * matrix4d2.m30 + matrix4d.m13 * matrix4d2.m31 + matrix4d.m23 * matrix4d2.m32 + matrix4d.m33 * matrix4d2.m33;
        }
        else {
            final double m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m10 * matrix4d2.m01 + matrix4d.m20 * matrix4d2.m02 + matrix4d.m30 * matrix4d2.m03;
            final double m2 = matrix4d.m00 * matrix4d2.m10 + matrix4d.m10 * matrix4d2.m11 + matrix4d.m20 * matrix4d2.m12 + matrix4d.m30 * matrix4d2.m13;
            final double m3 = matrix4d.m00 * matrix4d2.m20 + matrix4d.m10 * matrix4d2.m21 + matrix4d.m20 * matrix4d2.m22 + matrix4d.m30 * matrix4d2.m23;
            final double m4 = matrix4d.m00 * matrix4d2.m30 + matrix4d.m10 * matrix4d2.m31 + matrix4d.m20 * matrix4d2.m32 + matrix4d.m30 * matrix4d2.m33;
            final double m5 = matrix4d.m01 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m01 + matrix4d.m21 * matrix4d2.m02 + matrix4d.m31 * matrix4d2.m03;
            final double m6 = matrix4d.m01 * matrix4d2.m10 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m21 * matrix4d2.m12 + matrix4d.m31 * matrix4d2.m13;
            final double m7 = matrix4d.m01 * matrix4d2.m20 + matrix4d.m11 * matrix4d2.m21 + matrix4d.m21 * matrix4d2.m22 + matrix4d.m31 * matrix4d2.m23;
            final double m8 = matrix4d.m01 * matrix4d2.m30 + matrix4d.m11 * matrix4d2.m31 + matrix4d.m21 * matrix4d2.m32 + matrix4d.m31 * matrix4d2.m33;
            final double m9 = matrix4d.m02 * matrix4d2.m00 + matrix4d.m12 * matrix4d2.m01 + matrix4d.m22 * matrix4d2.m02 + matrix4d.m32 * matrix4d2.m03;
            final double m10 = matrix4d.m02 * matrix4d2.m10 + matrix4d.m12 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m12 + matrix4d.m32 * matrix4d2.m13;
            final double m11 = matrix4d.m02 * matrix4d2.m20 + matrix4d.m12 * matrix4d2.m21 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m32 * matrix4d2.m23;
            final double m12 = matrix4d.m02 * matrix4d2.m30 + matrix4d.m12 * matrix4d2.m31 + matrix4d.m22 * matrix4d2.m32 + matrix4d.m32 * matrix4d2.m33;
            final double m13 = matrix4d.m03 * matrix4d2.m00 + matrix4d.m13 * matrix4d2.m01 + matrix4d.m23 * matrix4d2.m02 + matrix4d.m33 * matrix4d2.m03;
            final double m14 = matrix4d.m03 * matrix4d2.m10 + matrix4d.m13 * matrix4d2.m11 + matrix4d.m23 * matrix4d2.m12 + matrix4d.m33 * matrix4d2.m13;
            final double m15 = matrix4d.m03 * matrix4d2.m20 + matrix4d.m13 * matrix4d2.m21 + matrix4d.m23 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m23;
            final double m16 = matrix4d.m03 * matrix4d2.m30 + matrix4d.m13 * matrix4d2.m31 + matrix4d.m23 * matrix4d2.m32 + matrix4d.m33 * matrix4d2.m33;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m03 = m4;
            this.m10 = m5;
            this.m11 = m6;
            this.m12 = m7;
            this.m13 = m8;
            this.m20 = m9;
            this.m21 = m10;
            this.m22 = m11;
            this.m23 = m12;
            this.m30 = m13;
            this.m31 = m14;
            this.m32 = m15;
            this.m33 = m16;
        }
    }
    
    public final void mulTransposeRight(final Matrix4d matrix4d, final Matrix4d matrix4d2) {
        if (this != matrix4d && this != matrix4d2) {
            this.m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m01 * matrix4d2.m01 + matrix4d.m02 * matrix4d2.m02 + matrix4d.m03 * matrix4d2.m03;
            this.m01 = matrix4d.m00 * matrix4d2.m10 + matrix4d.m01 * matrix4d2.m11 + matrix4d.m02 * matrix4d2.m12 + matrix4d.m03 * matrix4d2.m13;
            this.m02 = matrix4d.m00 * matrix4d2.m20 + matrix4d.m01 * matrix4d2.m21 + matrix4d.m02 * matrix4d2.m22 + matrix4d.m03 * matrix4d2.m23;
            this.m03 = matrix4d.m00 * matrix4d2.m30 + matrix4d.m01 * matrix4d2.m31 + matrix4d.m02 * matrix4d2.m32 + matrix4d.m03 * matrix4d2.m33;
            this.m10 = matrix4d.m10 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m01 + matrix4d.m12 * matrix4d2.m02 + matrix4d.m13 * matrix4d2.m03;
            this.m11 = matrix4d.m10 * matrix4d2.m10 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m12 * matrix4d2.m12 + matrix4d.m13 * matrix4d2.m13;
            this.m12 = matrix4d.m10 * matrix4d2.m20 + matrix4d.m11 * matrix4d2.m21 + matrix4d.m12 * matrix4d2.m22 + matrix4d.m13 * matrix4d2.m23;
            this.m13 = matrix4d.m10 * matrix4d2.m30 + matrix4d.m11 * matrix4d2.m31 + matrix4d.m12 * matrix4d2.m32 + matrix4d.m13 * matrix4d2.m33;
            this.m20 = matrix4d.m20 * matrix4d2.m00 + matrix4d.m21 * matrix4d2.m01 + matrix4d.m22 * matrix4d2.m02 + matrix4d.m23 * matrix4d2.m03;
            this.m21 = matrix4d.m20 * matrix4d2.m10 + matrix4d.m21 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m12 + matrix4d.m23 * matrix4d2.m13;
            this.m22 = matrix4d.m20 * matrix4d2.m20 + matrix4d.m21 * matrix4d2.m21 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m23 * matrix4d2.m23;
            this.m23 = matrix4d.m20 * matrix4d2.m30 + matrix4d.m21 * matrix4d2.m31 + matrix4d.m22 * matrix4d2.m32 + matrix4d.m23 * matrix4d2.m33;
            this.m30 = matrix4d.m30 * matrix4d2.m00 + matrix4d.m31 * matrix4d2.m01 + matrix4d.m32 * matrix4d2.m02 + matrix4d.m33 * matrix4d2.m03;
            this.m31 = matrix4d.m30 * matrix4d2.m10 + matrix4d.m31 * matrix4d2.m11 + matrix4d.m32 * matrix4d2.m12 + matrix4d.m33 * matrix4d2.m13;
            this.m32 = matrix4d.m30 * matrix4d2.m20 + matrix4d.m31 * matrix4d2.m21 + matrix4d.m32 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m23;
            this.m33 = matrix4d.m30 * matrix4d2.m30 + matrix4d.m31 * matrix4d2.m31 + matrix4d.m32 * matrix4d2.m32 + matrix4d.m33 * matrix4d2.m33;
        }
        else {
            final double m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m01 * matrix4d2.m01 + matrix4d.m02 * matrix4d2.m02 + matrix4d.m03 * matrix4d2.m03;
            final double m2 = matrix4d.m00 * matrix4d2.m10 + matrix4d.m01 * matrix4d2.m11 + matrix4d.m02 * matrix4d2.m12 + matrix4d.m03 * matrix4d2.m13;
            final double m3 = matrix4d.m00 * matrix4d2.m20 + matrix4d.m01 * matrix4d2.m21 + matrix4d.m02 * matrix4d2.m22 + matrix4d.m03 * matrix4d2.m23;
            final double m4 = matrix4d.m00 * matrix4d2.m30 + matrix4d.m01 * matrix4d2.m31 + matrix4d.m02 * matrix4d2.m32 + matrix4d.m03 * matrix4d2.m33;
            final double m5 = matrix4d.m10 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m01 + matrix4d.m12 * matrix4d2.m02 + matrix4d.m13 * matrix4d2.m03;
            final double m6 = matrix4d.m10 * matrix4d2.m10 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m12 * matrix4d2.m12 + matrix4d.m13 * matrix4d2.m13;
            final double m7 = matrix4d.m10 * matrix4d2.m20 + matrix4d.m11 * matrix4d2.m21 + matrix4d.m12 * matrix4d2.m22 + matrix4d.m13 * matrix4d2.m23;
            final double m8 = matrix4d.m10 * matrix4d2.m30 + matrix4d.m11 * matrix4d2.m31 + matrix4d.m12 * matrix4d2.m32 + matrix4d.m13 * matrix4d2.m33;
            final double m9 = matrix4d.m20 * matrix4d2.m00 + matrix4d.m21 * matrix4d2.m01 + matrix4d.m22 * matrix4d2.m02 + matrix4d.m23 * matrix4d2.m03;
            final double m10 = matrix4d.m20 * matrix4d2.m10 + matrix4d.m21 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m12 + matrix4d.m23 * matrix4d2.m13;
            final double m11 = matrix4d.m20 * matrix4d2.m20 + matrix4d.m21 * matrix4d2.m21 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m23 * matrix4d2.m23;
            final double m12 = matrix4d.m20 * matrix4d2.m30 + matrix4d.m21 * matrix4d2.m31 + matrix4d.m22 * matrix4d2.m32 + matrix4d.m23 * matrix4d2.m33;
            final double m13 = matrix4d.m30 * matrix4d2.m00 + matrix4d.m31 * matrix4d2.m01 + matrix4d.m32 * matrix4d2.m02 + matrix4d.m33 * matrix4d2.m03;
            final double m14 = matrix4d.m30 * matrix4d2.m10 + matrix4d.m31 * matrix4d2.m11 + matrix4d.m32 * matrix4d2.m12 + matrix4d.m33 * matrix4d2.m13;
            final double m15 = matrix4d.m30 * matrix4d2.m20 + matrix4d.m31 * matrix4d2.m21 + matrix4d.m32 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m23;
            final double m16 = matrix4d.m30 * matrix4d2.m30 + matrix4d.m31 * matrix4d2.m31 + matrix4d.m32 * matrix4d2.m32 + matrix4d.m33 * matrix4d2.m33;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m03 = m4;
            this.m10 = m5;
            this.m11 = m6;
            this.m12 = m7;
            this.m13 = m8;
            this.m20 = m9;
            this.m21 = m10;
            this.m22 = m11;
            this.m23 = m12;
            this.m30 = m13;
            this.m31 = m14;
            this.m32 = m15;
            this.m33 = m16;
        }
    }
    
    public final void mulTransposeLeft(final Matrix4d matrix4d, final Matrix4d matrix4d2) {
        if (this != matrix4d && this != matrix4d2) {
            this.m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m10 * matrix4d2.m10 + matrix4d.m20 * matrix4d2.m20 + matrix4d.m30 * matrix4d2.m30;
            this.m01 = matrix4d.m00 * matrix4d2.m01 + matrix4d.m10 * matrix4d2.m11 + matrix4d.m20 * matrix4d2.m21 + matrix4d.m30 * matrix4d2.m31;
            this.m02 = matrix4d.m00 * matrix4d2.m02 + matrix4d.m10 * matrix4d2.m12 + matrix4d.m20 * matrix4d2.m22 + matrix4d.m30 * matrix4d2.m32;
            this.m03 = matrix4d.m00 * matrix4d2.m03 + matrix4d.m10 * matrix4d2.m13 + matrix4d.m20 * matrix4d2.m23 + matrix4d.m30 * matrix4d2.m33;
            this.m10 = matrix4d.m01 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m10 + matrix4d.m21 * matrix4d2.m20 + matrix4d.m31 * matrix4d2.m30;
            this.m11 = matrix4d.m01 * matrix4d2.m01 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m21 * matrix4d2.m21 + matrix4d.m31 * matrix4d2.m31;
            this.m12 = matrix4d.m01 * matrix4d2.m02 + matrix4d.m11 * matrix4d2.m12 + matrix4d.m21 * matrix4d2.m22 + matrix4d.m31 * matrix4d2.m32;
            this.m13 = matrix4d.m01 * matrix4d2.m03 + matrix4d.m11 * matrix4d2.m13 + matrix4d.m21 * matrix4d2.m23 + matrix4d.m31 * matrix4d2.m33;
            this.m20 = matrix4d.m02 * matrix4d2.m00 + matrix4d.m12 * matrix4d2.m10 + matrix4d.m22 * matrix4d2.m20 + matrix4d.m32 * matrix4d2.m30;
            this.m21 = matrix4d.m02 * matrix4d2.m01 + matrix4d.m12 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m21 + matrix4d.m32 * matrix4d2.m31;
            this.m22 = matrix4d.m02 * matrix4d2.m02 + matrix4d.m12 * matrix4d2.m12 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m32 * matrix4d2.m32;
            this.m23 = matrix4d.m02 * matrix4d2.m03 + matrix4d.m12 * matrix4d2.m13 + matrix4d.m22 * matrix4d2.m23 + matrix4d.m32 * matrix4d2.m33;
            this.m30 = matrix4d.m03 * matrix4d2.m00 + matrix4d.m13 * matrix4d2.m10 + matrix4d.m23 * matrix4d2.m20 + matrix4d.m33 * matrix4d2.m30;
            this.m31 = matrix4d.m03 * matrix4d2.m01 + matrix4d.m13 * matrix4d2.m11 + matrix4d.m23 * matrix4d2.m21 + matrix4d.m33 * matrix4d2.m31;
            this.m32 = matrix4d.m03 * matrix4d2.m02 + matrix4d.m13 * matrix4d2.m12 + matrix4d.m23 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m32;
            this.m33 = matrix4d.m03 * matrix4d2.m03 + matrix4d.m13 * matrix4d2.m13 + matrix4d.m23 * matrix4d2.m23 + matrix4d.m33 * matrix4d2.m33;
        }
        else {
            final double m00 = matrix4d.m00 * matrix4d2.m00 + matrix4d.m10 * matrix4d2.m10 + matrix4d.m20 * matrix4d2.m20 + matrix4d.m30 * matrix4d2.m30;
            final double m2 = matrix4d.m00 * matrix4d2.m01 + matrix4d.m10 * matrix4d2.m11 + matrix4d.m20 * matrix4d2.m21 + matrix4d.m30 * matrix4d2.m31;
            final double m3 = matrix4d.m00 * matrix4d2.m02 + matrix4d.m10 * matrix4d2.m12 + matrix4d.m20 * matrix4d2.m22 + matrix4d.m30 * matrix4d2.m32;
            final double m4 = matrix4d.m00 * matrix4d2.m03 + matrix4d.m10 * matrix4d2.m13 + matrix4d.m20 * matrix4d2.m23 + matrix4d.m30 * matrix4d2.m33;
            final double m5 = matrix4d.m01 * matrix4d2.m00 + matrix4d.m11 * matrix4d2.m10 + matrix4d.m21 * matrix4d2.m20 + matrix4d.m31 * matrix4d2.m30;
            final double m6 = matrix4d.m01 * matrix4d2.m01 + matrix4d.m11 * matrix4d2.m11 + matrix4d.m21 * matrix4d2.m21 + matrix4d.m31 * matrix4d2.m31;
            final double m7 = matrix4d.m01 * matrix4d2.m02 + matrix4d.m11 * matrix4d2.m12 + matrix4d.m21 * matrix4d2.m22 + matrix4d.m31 * matrix4d2.m32;
            final double m8 = matrix4d.m01 * matrix4d2.m03 + matrix4d.m11 * matrix4d2.m13 + matrix4d.m21 * matrix4d2.m23 + matrix4d.m31 * matrix4d2.m33;
            final double m9 = matrix4d.m02 * matrix4d2.m00 + matrix4d.m12 * matrix4d2.m10 + matrix4d.m22 * matrix4d2.m20 + matrix4d.m32 * matrix4d2.m30;
            final double m10 = matrix4d.m02 * matrix4d2.m01 + matrix4d.m12 * matrix4d2.m11 + matrix4d.m22 * matrix4d2.m21 + matrix4d.m32 * matrix4d2.m31;
            final double m11 = matrix4d.m02 * matrix4d2.m02 + matrix4d.m12 * matrix4d2.m12 + matrix4d.m22 * matrix4d2.m22 + matrix4d.m32 * matrix4d2.m32;
            final double m12 = matrix4d.m02 * matrix4d2.m03 + matrix4d.m12 * matrix4d2.m13 + matrix4d.m22 * matrix4d2.m23 + matrix4d.m32 * matrix4d2.m33;
            final double m13 = matrix4d.m03 * matrix4d2.m00 + matrix4d.m13 * matrix4d2.m10 + matrix4d.m23 * matrix4d2.m20 + matrix4d.m33 * matrix4d2.m30;
            final double m14 = matrix4d.m03 * matrix4d2.m01 + matrix4d.m13 * matrix4d2.m11 + matrix4d.m23 * matrix4d2.m21 + matrix4d.m33 * matrix4d2.m31;
            final double m15 = matrix4d.m03 * matrix4d2.m02 + matrix4d.m13 * matrix4d2.m12 + matrix4d.m23 * matrix4d2.m22 + matrix4d.m33 * matrix4d2.m32;
            final double m16 = matrix4d.m03 * matrix4d2.m03 + matrix4d.m13 * matrix4d2.m13 + matrix4d.m23 * matrix4d2.m23 + matrix4d.m33 * matrix4d2.m33;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m03 = m4;
            this.m10 = m5;
            this.m11 = m6;
            this.m12 = m7;
            this.m13 = m8;
            this.m20 = m9;
            this.m21 = m10;
            this.m22 = m11;
            this.m23 = m12;
            this.m30 = m13;
            this.m31 = m14;
            this.m32 = m15;
            this.m33 = m16;
        }
    }
    
    public boolean equals(final Matrix4d matrix4d) {
        try {
            return this.m00 == matrix4d.m00 && this.m01 == matrix4d.m01 && this.m02 == matrix4d.m02 && this.m03 == matrix4d.m03 && this.m10 == matrix4d.m10 && this.m11 == matrix4d.m11 && this.m12 == matrix4d.m12 && this.m13 == matrix4d.m13 && this.m20 == matrix4d.m20 && this.m21 == matrix4d.m21 && this.m22 == matrix4d.m22 && this.m23 == matrix4d.m23 && this.m30 == matrix4d.m30 && this.m31 == matrix4d.m31 && this.m32 == matrix4d.m32 && this.m33 == matrix4d.m33;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Matrix4d matrix4d = (Matrix4d)o;
            return this.m00 == matrix4d.m00 && this.m01 == matrix4d.m01 && this.m02 == matrix4d.m02 && this.m03 == matrix4d.m03 && this.m10 == matrix4d.m10 && this.m11 == matrix4d.m11 && this.m12 == matrix4d.m12 && this.m13 == matrix4d.m13 && this.m20 == matrix4d.m20 && this.m21 == matrix4d.m21 && this.m22 == matrix4d.m22 && this.m23 == matrix4d.m23 && this.m30 == matrix4d.m30 && this.m31 == matrix4d.m31 && this.m32 == matrix4d.m32 && this.m33 == matrix4d.m33;
        }
        catch (ClassCastException ex) {
            return false;
        }
        catch (NullPointerException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Matrix4d matrix4d, final float n) {
        return this.epsilonEquals(matrix4d, (double)n);
    }
    
    public boolean epsilonEquals(final Matrix4d matrix4d, final double n) {
        final double n2 = this.m00 - matrix4d.m00;
        if (((n2 < 0.0) ? (-n2) : n2) > n) {
            return false;
        }
        final double n3 = this.m01 - matrix4d.m01;
        if (((n3 < 0.0) ? (-n3) : n3) > n) {
            return false;
        }
        final double n4 = this.m02 - matrix4d.m02;
        if (((n4 < 0.0) ? (-n4) : n4) > n) {
            return false;
        }
        final double n5 = this.m03 - matrix4d.m03;
        if (((n5 < 0.0) ? (-n5) : n5) > n) {
            return false;
        }
        final double n6 = this.m10 - matrix4d.m10;
        if (((n6 < 0.0) ? (-n6) : n6) > n) {
            return false;
        }
        final double n7 = this.m11 - matrix4d.m11;
        if (((n7 < 0.0) ? (-n7) : n7) > n) {
            return false;
        }
        final double n8 = this.m12 - matrix4d.m12;
        if (((n8 < 0.0) ? (-n8) : n8) > n) {
            return false;
        }
        final double n9 = this.m13 - matrix4d.m13;
        if (((n9 < 0.0) ? (-n9) : n9) > n) {
            return false;
        }
        final double n10 = this.m20 - matrix4d.m20;
        if (((n10 < 0.0) ? (-n10) : n10) > n) {
            return false;
        }
        final double n11 = this.m21 - matrix4d.m21;
        if (((n11 < 0.0) ? (-n11) : n11) > n) {
            return false;
        }
        final double n12 = this.m22 - matrix4d.m22;
        if (((n12 < 0.0) ? (-n12) : n12) > n) {
            return false;
        }
        final double n13 = this.m23 - matrix4d.m23;
        if (((n13 < 0.0) ? (-n13) : n13) > n) {
            return false;
        }
        final double n14 = this.m30 - matrix4d.m30;
        if (((n14 < 0.0) ? (-n14) : n14) > n) {
            return false;
        }
        final double n15 = this.m31 - matrix4d.m31;
        if (((n15 < 0.0) ? (-n15) : n15) > n) {
            return false;
        }
        final double n16 = this.m32 - matrix4d.m32;
        if (((n16 < 0.0) ? (-n16) : n16) > n) {
            return false;
        }
        final double n17 = this.m33 - matrix4d.m33;
        return ((n17 < 0.0) ? (-n17) : n17) <= n;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * 1L + VecMathUtil.doubleToLongBits(this.m00)) + VecMathUtil.doubleToLongBits(this.m01)) + VecMathUtil.doubleToLongBits(this.m02)) + VecMathUtil.doubleToLongBits(this.m03)) + VecMathUtil.doubleToLongBits(this.m10)) + VecMathUtil.doubleToLongBits(this.m11)) + VecMathUtil.doubleToLongBits(this.m12)) + VecMathUtil.doubleToLongBits(this.m13)) + VecMathUtil.doubleToLongBits(this.m20)) + VecMathUtil.doubleToLongBits(this.m21)) + VecMathUtil.doubleToLongBits(this.m22)) + VecMathUtil.doubleToLongBits(this.m23)) + VecMathUtil.doubleToLongBits(this.m30)) + VecMathUtil.doubleToLongBits(this.m31)) + VecMathUtil.doubleToLongBits(this.m32)) + VecMathUtil.doubleToLongBits(this.m33);
        return (int)(n ^ n >> 32);
    }
    
    public final void transform(final Tuple4d tuple4d, final Tuple4d tuple4d2) {
        final double x = this.m00 * tuple4d.x + this.m01 * tuple4d.y + this.m02 * tuple4d.z + this.m03 * tuple4d.w;
        final double y = this.m10 * tuple4d.x + this.m11 * tuple4d.y + this.m12 * tuple4d.z + this.m13 * tuple4d.w;
        final double z = this.m20 * tuple4d.x + this.m21 * tuple4d.y + this.m22 * tuple4d.z + this.m23 * tuple4d.w;
        tuple4d2.w = this.m30 * tuple4d.x + this.m31 * tuple4d.y + this.m32 * tuple4d.z + this.m33 * tuple4d.w;
        tuple4d2.x = x;
        tuple4d2.y = y;
        tuple4d2.z = z;
    }
    
    public final void transform(final Tuple4d tuple4d) {
        final double x = this.m00 * tuple4d.x + this.m01 * tuple4d.y + this.m02 * tuple4d.z + this.m03 * tuple4d.w;
        final double y = this.m10 * tuple4d.x + this.m11 * tuple4d.y + this.m12 * tuple4d.z + this.m13 * tuple4d.w;
        final double z = this.m20 * tuple4d.x + this.m21 * tuple4d.y + this.m22 * tuple4d.z + this.m23 * tuple4d.w;
        tuple4d.w = this.m30 * tuple4d.x + this.m31 * tuple4d.y + this.m32 * tuple4d.z + this.m33 * tuple4d.w;
        tuple4d.x = x;
        tuple4d.y = y;
        tuple4d.z = z;
    }
    
    public final void transform(final Tuple4f tuple4f, final Tuple4f tuple4f2) {
        final float x = (float)(this.m00 * tuple4f.x + this.m01 * tuple4f.y + this.m02 * tuple4f.z + this.m03 * tuple4f.w);
        final float y = (float)(this.m10 * tuple4f.x + this.m11 * tuple4f.y + this.m12 * tuple4f.z + this.m13 * tuple4f.w);
        final float z = (float)(this.m20 * tuple4f.x + this.m21 * tuple4f.y + this.m22 * tuple4f.z + this.m23 * tuple4f.w);
        tuple4f2.w = (float)(this.m30 * tuple4f.x + this.m31 * tuple4f.y + this.m32 * tuple4f.z + this.m33 * tuple4f.w);
        tuple4f2.x = x;
        tuple4f2.y = y;
        tuple4f2.z = z;
    }
    
    public final void transform(final Tuple4f tuple4f) {
        final float x = (float)(this.m00 * tuple4f.x + this.m01 * tuple4f.y + this.m02 * tuple4f.z + this.m03 * tuple4f.w);
        final float y = (float)(this.m10 * tuple4f.x + this.m11 * tuple4f.y + this.m12 * tuple4f.z + this.m13 * tuple4f.w);
        final float z = (float)(this.m20 * tuple4f.x + this.m21 * tuple4f.y + this.m22 * tuple4f.z + this.m23 * tuple4f.w);
        tuple4f.w = (float)(this.m30 * tuple4f.x + this.m31 * tuple4f.y + this.m32 * tuple4f.z + this.m33 * tuple4f.w);
        tuple4f.x = x;
        tuple4f.y = y;
        tuple4f.z = z;
    }
    
    public final void transform(final Point3d point3d, final Point3d point3d2) {
        final double x = this.m00 * point3d.x + this.m01 * point3d.y + this.m02 * point3d.z + this.m03;
        final double y = this.m10 * point3d.x + this.m11 * point3d.y + this.m12 * point3d.z + this.m13;
        point3d2.z = this.m20 * point3d.x + this.m21 * point3d.y + this.m22 * point3d.z + this.m23;
        point3d2.x = x;
        point3d2.y = y;
    }
    
    public final void transform(final Point3d point3d) {
        final double x = this.m00 * point3d.x + this.m01 * point3d.y + this.m02 * point3d.z + this.m03;
        final double y = this.m10 * point3d.x + this.m11 * point3d.y + this.m12 * point3d.z + this.m13;
        point3d.z = this.m20 * point3d.x + this.m21 * point3d.y + this.m22 * point3d.z + this.m23;
        point3d.x = x;
        point3d.y = y;
    }
    
    public final void transform(final Point3f point3f, final Point3f point3f2) {
        final float x = (float)(this.m00 * point3f.x + this.m01 * point3f.y + this.m02 * point3f.z + this.m03);
        final float y = (float)(this.m10 * point3f.x + this.m11 * point3f.y + this.m12 * point3f.z + this.m13);
        point3f2.z = (float)(this.m20 * point3f.x + this.m21 * point3f.y + this.m22 * point3f.z + this.m23);
        point3f2.x = x;
        point3f2.y = y;
    }
    
    public final void transform(final Point3f point3f) {
        final float x = (float)(this.m00 * point3f.x + this.m01 * point3f.y + this.m02 * point3f.z + this.m03);
        final float y = (float)(this.m10 * point3f.x + this.m11 * point3f.y + this.m12 * point3f.z + this.m13);
        point3f.z = (float)(this.m20 * point3f.x + this.m21 * point3f.y + this.m22 * point3f.z + this.m23);
        point3f.x = x;
        point3f.y = y;
    }
    
    public final void transform(final Vector3d vector3d, final Vector3d vector3d2) {
        final double x = this.m00 * vector3d.x + this.m01 * vector3d.y + this.m02 * vector3d.z;
        final double y = this.m10 * vector3d.x + this.m11 * vector3d.y + this.m12 * vector3d.z;
        vector3d2.z = this.m20 * vector3d.x + this.m21 * vector3d.y + this.m22 * vector3d.z;
        vector3d2.x = x;
        vector3d2.y = y;
    }
    
    public final void transform(final Vector3d vector3d) {
        final double x = this.m00 * vector3d.x + this.m01 * vector3d.y + this.m02 * vector3d.z;
        final double y = this.m10 * vector3d.x + this.m11 * vector3d.y + this.m12 * vector3d.z;
        vector3d.z = this.m20 * vector3d.x + this.m21 * vector3d.y + this.m22 * vector3d.z;
        vector3d.x = x;
        vector3d.y = y;
    }
    
    public final void transform(final Vector3f vector3f, final Vector3f vector3f2) {
        final float x = (float)(this.m00 * vector3f.x + this.m01 * vector3f.y + this.m02 * vector3f.z);
        final float y = (float)(this.m10 * vector3f.x + this.m11 * vector3f.y + this.m12 * vector3f.z);
        vector3f2.z = (float)(this.m20 * vector3f.x + this.m21 * vector3f.y + this.m22 * vector3f.z);
        vector3f2.x = x;
        vector3f2.y = y;
    }
    
    public final void transform(final Vector3f vector3f) {
        final float x = (float)(this.m00 * vector3f.x + this.m01 * vector3f.y + this.m02 * vector3f.z);
        final float y = (float)(this.m10 * vector3f.x + this.m11 * vector3f.y + this.m12 * vector3f.z);
        vector3f.z = (float)(this.m20 * vector3f.x + this.m21 * vector3f.y + this.m22 * vector3f.z);
        vector3f.x = x;
        vector3f.y = y;
    }
    
    public final void setRotation(final Matrix3d matrix3d) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = matrix3d.m00 * array2[0];
        this.m01 = matrix3d.m01 * array2[1];
        this.m02 = matrix3d.m02 * array2[2];
        this.m10 = matrix3d.m10 * array2[0];
        this.m11 = matrix3d.m11 * array2[1];
        this.m12 = matrix3d.m12 * array2[2];
        this.m20 = matrix3d.m20 * array2[0];
        this.m21 = matrix3d.m21 * array2[1];
        this.m22 = matrix3d.m22 * array2[2];
    }
    
    public final void setRotation(final Matrix3f matrix3f) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = matrix3f.m00 * array2[0];
        this.m01 = matrix3f.m01 * array2[1];
        this.m02 = matrix3f.m02 * array2[2];
        this.m10 = matrix3f.m10 * array2[0];
        this.m11 = matrix3f.m11 * array2[1];
        this.m12 = matrix3f.m12 * array2[2];
        this.m20 = matrix3f.m20 * array2[0];
        this.m21 = matrix3f.m21 * array2[1];
        this.m22 = matrix3f.m22 * array2[2];
    }
    
    public final void setRotation(final Quat4f quat4f) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = (1.0 - 2.0f * quat4f.y * quat4f.y - 2.0f * quat4f.z * quat4f.z) * array2[0];
        this.m10 = 2.0 * (quat4f.x * quat4f.y + quat4f.w * quat4f.z) * array2[0];
        this.m20 = 2.0 * (quat4f.x * quat4f.z - quat4f.w * quat4f.y) * array2[0];
        this.m01 = 2.0 * (quat4f.x * quat4f.y - quat4f.w * quat4f.z) * array2[1];
        this.m11 = (1.0 - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.z * quat4f.z) * array2[1];
        this.m21 = 2.0 * (quat4f.y * quat4f.z + quat4f.w * quat4f.x) * array2[1];
        this.m02 = 2.0 * (quat4f.x * quat4f.z + quat4f.w * quat4f.y) * array2[2];
        this.m12 = 2.0 * (quat4f.y * quat4f.z - quat4f.w * quat4f.x) * array2[2];
        this.m22 = (1.0 - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.y * quat4f.y) * array2[2];
    }
    
    public final void setRotation(final Quat4d quat4d) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = (1.0 - 2.0 * quat4d.y * quat4d.y - 2.0 * quat4d.z * quat4d.z) * array2[0];
        this.m10 = 2.0 * (quat4d.x * quat4d.y + quat4d.w * quat4d.z) * array2[0];
        this.m20 = 2.0 * (quat4d.x * quat4d.z - quat4d.w * quat4d.y) * array2[0];
        this.m01 = 2.0 * (quat4d.x * quat4d.y - quat4d.w * quat4d.z) * array2[1];
        this.m11 = (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.z * quat4d.z) * array2[1];
        this.m21 = 2.0 * (quat4d.y * quat4d.z + quat4d.w * quat4d.x) * array2[1];
        this.m02 = 2.0 * (quat4d.x * quat4d.z + quat4d.w * quat4d.y) * array2[2];
        this.m12 = 2.0 * (quat4d.y * quat4d.z - quat4d.w * quat4d.x) * array2[2];
        this.m22 = (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.y * quat4d.y) * array2[2];
    }
    
    public final void setRotation(final AxisAngle4d axisAngle4d) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        final double n = 1.0 / Math.sqrt(axisAngle4d.x * axisAngle4d.x + axisAngle4d.y * axisAngle4d.y + axisAngle4d.z * axisAngle4d.z);
        final double n2 = axisAngle4d.x * n;
        final double n3 = axisAngle4d.y * n;
        final double n4 = axisAngle4d.z * n;
        final double sin = Math.sin(axisAngle4d.angle);
        final double cos = Math.cos(axisAngle4d.angle);
        final double n5 = 1.0 - cos;
        final double n6 = axisAngle4d.x * axisAngle4d.z;
        final double n7 = axisAngle4d.x * axisAngle4d.y;
        final double n8 = axisAngle4d.y * axisAngle4d.z;
        this.m00 = (n5 * n2 * n2 + cos) * array2[0];
        this.m01 = (n5 * n7 - sin * n4) * array2[1];
        this.m02 = (n5 * n6 + sin * n3) * array2[2];
        this.m10 = (n5 * n7 + sin * n4) * array2[0];
        this.m11 = (n5 * n3 * n3 + cos) * array2[1];
        this.m12 = (n5 * n8 - sin * n2) * array2[2];
        this.m20 = (n5 * n6 - sin * n3) * array2[0];
        this.m21 = (n5 * n8 + sin * n2) * array2[1];
        this.m22 = (n5 * n4 * n4 + cos) * array2[2];
    }
    
    public final void setZero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 0.0;
    }
    
    public final void negate() {
        this.m00 = -this.m00;
        this.m01 = -this.m01;
        this.m02 = -this.m02;
        this.m03 = -this.m03;
        this.m10 = -this.m10;
        this.m11 = -this.m11;
        this.m12 = -this.m12;
        this.m13 = -this.m13;
        this.m20 = -this.m20;
        this.m21 = -this.m21;
        this.m22 = -this.m22;
        this.m23 = -this.m23;
        this.m30 = -this.m30;
        this.m31 = -this.m31;
        this.m32 = -this.m32;
        this.m33 = -this.m33;
    }
    
    public final void negate(final Matrix4d matrix4d) {
        this.m00 = -matrix4d.m00;
        this.m01 = -matrix4d.m01;
        this.m02 = -matrix4d.m02;
        this.m03 = -matrix4d.m03;
        this.m10 = -matrix4d.m10;
        this.m11 = -matrix4d.m11;
        this.m12 = -matrix4d.m12;
        this.m13 = -matrix4d.m13;
        this.m20 = -matrix4d.m20;
        this.m21 = -matrix4d.m21;
        this.m22 = -matrix4d.m22;
        this.m23 = -matrix4d.m23;
        this.m30 = -matrix4d.m30;
        this.m31 = -matrix4d.m31;
        this.m32 = -matrix4d.m32;
        this.m33 = -matrix4d.m33;
    }
    
    private final void getScaleRotate(final double[] array, final double[] array2) {
        Matrix3d.compute_svd(new double[] { this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22 }, array, array2);
    }
    
    public Object clone() {
        Matrix4d matrix4d;
        try {
            matrix4d = (Matrix4d)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        return matrix4d;
    }
}
