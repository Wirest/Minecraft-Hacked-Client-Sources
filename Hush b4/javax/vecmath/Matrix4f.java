// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Matrix4f implements Serializable, Cloneable
{
    static final long serialVersionUID = -8405036035410109353L;
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;
    private static final double EPS = 1.0E-8;
    
    public Matrix4f(final float m00, final float m2, final float m3, final float m4, final float m5, final float m6, final float m7, final float m8, final float m9, final float m10, final float m11, final float m12, final float m13, final float m14, final float m15, final float m16) {
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
    
    public Matrix4f(final float[] array) {
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
    
    public Matrix4f(final Quat4f quat4f, final Vector3f vector3f, final float n) {
        this.m00 = (float)(n * (1.0 - 2.0 * quat4f.y * quat4f.y - 2.0 * quat4f.z * quat4f.z));
        this.m10 = (float)(n * (2.0 * (quat4f.x * quat4f.y + quat4f.w * quat4f.z)));
        this.m20 = (float)(n * (2.0 * (quat4f.x * quat4f.z - quat4f.w * quat4f.y)));
        this.m01 = (float)(n * (2.0 * (quat4f.x * quat4f.y - quat4f.w * quat4f.z)));
        this.m11 = (float)(n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.z * quat4f.z));
        this.m21 = (float)(n * (2.0 * (quat4f.y * quat4f.z + quat4f.w * quat4f.x)));
        this.m02 = (float)(n * (2.0 * (quat4f.x * quat4f.z + quat4f.w * quat4f.y)));
        this.m12 = (float)(n * (2.0 * (quat4f.y * quat4f.z - quat4f.w * quat4f.x)));
        this.m22 = (float)(n * (1.0 - 2.0 * quat4f.x * quat4f.x - 2.0 * quat4f.y * quat4f.y));
        this.m03 = vector3f.x;
        this.m13 = vector3f.y;
        this.m23 = vector3f.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public Matrix4f(final Matrix4d matrix4d) {
        this.m00 = (float)matrix4d.m00;
        this.m01 = (float)matrix4d.m01;
        this.m02 = (float)matrix4d.m02;
        this.m03 = (float)matrix4d.m03;
        this.m10 = (float)matrix4d.m10;
        this.m11 = (float)matrix4d.m11;
        this.m12 = (float)matrix4d.m12;
        this.m13 = (float)matrix4d.m13;
        this.m20 = (float)matrix4d.m20;
        this.m21 = (float)matrix4d.m21;
        this.m22 = (float)matrix4d.m22;
        this.m23 = (float)matrix4d.m23;
        this.m30 = (float)matrix4d.m30;
        this.m31 = (float)matrix4d.m31;
        this.m32 = (float)matrix4d.m32;
        this.m33 = (float)matrix4d.m33;
    }
    
    public Matrix4f(final Matrix4f matrix4f) {
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
    
    public Matrix4f(final Matrix3f matrix3f, final Vector3f vector3f, final float n) {
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public Matrix4f() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 0.0f;
    }
    
    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" + this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
    }
    
    public final void setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void setElement(final int n, final int n2, final float n3) {
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
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
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
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
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
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
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
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                        }
                    }
                    break;
                }
                default: {
                    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
            }
        }
    }
    
    public final float getElement(final int n, final int n2) {
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
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
    }
    
    public final void getRow(final int n, final Vector4f vector4f) {
        if (n == 0) {
            vector4f.x = this.m00;
            vector4f.y = this.m01;
            vector4f.z = this.m02;
            vector4f.w = this.m03;
        }
        else if (n == 1) {
            vector4f.x = this.m10;
            vector4f.y = this.m11;
            vector4f.z = this.m12;
            vector4f.w = this.m13;
        }
        else if (n == 2) {
            vector4f.x = this.m20;
            vector4f.y = this.m21;
            vector4f.z = this.m22;
            vector4f.w = this.m23;
        }
        else {
            if (n != 3) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
            }
            vector4f.x = this.m30;
            vector4f.y = this.m31;
            vector4f.z = this.m32;
            vector4f.w = this.m33;
        }
    }
    
    public final void getRow(final int n, final float[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
            }
            array[0] = this.m30;
            array[1] = this.m31;
            array[2] = this.m32;
            array[3] = this.m33;
        }
    }
    
    public final void getColumn(final int n, final Vector4f vector4f) {
        if (n == 0) {
            vector4f.x = this.m00;
            vector4f.y = this.m10;
            vector4f.z = this.m20;
            vector4f.w = this.m30;
        }
        else if (n == 1) {
            vector4f.x = this.m01;
            vector4f.y = this.m11;
            vector4f.z = this.m21;
            vector4f.w = this.m31;
        }
        else if (n == 2) {
            vector4f.x = this.m02;
            vector4f.y = this.m12;
            vector4f.z = this.m22;
            vector4f.w = this.m32;
        }
        else {
            if (n != 3) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
            }
            vector4f.x = this.m03;
            vector4f.y = this.m13;
            vector4f.z = this.m23;
            vector4f.w = this.m33;
        }
    }
    
    public final void getColumn(final int n, final float[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
            }
            array[0] = this.m03;
            array[1] = this.m13;
            array[2] = this.m23;
            array[3] = this.m33;
        }
    }
    
    public final void setScale(final float n) {
        final double[] array = new double[9];
        this.getScaleRotate(new double[3], array);
        this.m00 = (float)(array[0] * n);
        this.m01 = (float)(array[1] * n);
        this.m02 = (float)(array[2] * n);
        this.m10 = (float)(array[3] * n);
        this.m11 = (float)(array[4] * n);
        this.m12 = (float)(array[5] * n);
        this.m20 = (float)(array[6] * n);
        this.m21 = (float)(array[7] * n);
        this.m22 = (float)(array[8] * n);
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
    
    public final float get(final Matrix3f matrix3f, final Vector3f vector3f) {
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
        vector3f.x = this.m03;
        vector3f.y = this.m13;
        vector3f.z = this.m23;
        return (float)Matrix3d.max3(array2);
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
    
    public final void get(final Vector3f vector3f) {
        vector3f.x = this.m03;
        vector3f.y = this.m13;
        vector3f.z = this.m23;
    }
    
    public final void getRotationScale(final Matrix3f matrix3f) {
        matrix3f.m00 = this.m00;
        matrix3f.m01 = this.m01;
        matrix3f.m02 = this.m02;
        matrix3f.m10 = this.m10;
        matrix3f.m11 = this.m11;
        matrix3f.m12 = this.m12;
        matrix3f.m20 = this.m20;
        matrix3f.m21 = this.m21;
        matrix3f.m22 = this.m22;
    }
    
    public final float getScale() {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        return (float)Matrix3d.max3(array2);
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
    
    public final void setRow(final int n, final float n2, final float n3, final float n4, final float n5) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
            }
        }
    }
    
    public final void setRow(final int n, final Vector4f vector4f) {
        switch (n) {
            case 0: {
                this.m00 = vector4f.x;
                this.m01 = vector4f.y;
                this.m02 = vector4f.z;
                this.m03 = vector4f.w;
                break;
            }
            case 1: {
                this.m10 = vector4f.x;
                this.m11 = vector4f.y;
                this.m12 = vector4f.z;
                this.m13 = vector4f.w;
                break;
            }
            case 2: {
                this.m20 = vector4f.x;
                this.m21 = vector4f.y;
                this.m22 = vector4f.z;
                this.m23 = vector4f.w;
                break;
            }
            case 3: {
                this.m30 = vector4f.x;
                this.m31 = vector4f.y;
                this.m32 = vector4f.z;
                this.m33 = vector4f.w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
            }
        }
    }
    
    public final void setRow(final int n, final float[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
            }
        }
    }
    
    public final void setColumn(final int n, final float n2, final float n3, final float n4, final float n5) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
            }
        }
    }
    
    public final void setColumn(final int n, final Vector4f vector4f) {
        switch (n) {
            case 0: {
                this.m00 = vector4f.x;
                this.m10 = vector4f.y;
                this.m20 = vector4f.z;
                this.m30 = vector4f.w;
                break;
            }
            case 1: {
                this.m01 = vector4f.x;
                this.m11 = vector4f.y;
                this.m21 = vector4f.z;
                this.m31 = vector4f.w;
                break;
            }
            case 2: {
                this.m02 = vector4f.x;
                this.m12 = vector4f.y;
                this.m22 = vector4f.z;
                this.m32 = vector4f.w;
                break;
            }
            case 3: {
                this.m03 = vector4f.x;
                this.m13 = vector4f.y;
                this.m23 = vector4f.z;
                this.m33 = vector4f.w;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
            }
        }
    }
    
    public final void setColumn(final int n, final float[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
            }
        }
    }
    
    public final void add(final float n) {
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
    
    public final void add(final float n, final Matrix4f matrix4f) {
        this.m00 = matrix4f.m00 + n;
        this.m01 = matrix4f.m01 + n;
        this.m02 = matrix4f.m02 + n;
        this.m03 = matrix4f.m03 + n;
        this.m10 = matrix4f.m10 + n;
        this.m11 = matrix4f.m11 + n;
        this.m12 = matrix4f.m12 + n;
        this.m13 = matrix4f.m13 + n;
        this.m20 = matrix4f.m20 + n;
        this.m21 = matrix4f.m21 + n;
        this.m22 = matrix4f.m22 + n;
        this.m23 = matrix4f.m23 + n;
        this.m30 = matrix4f.m30 + n;
        this.m31 = matrix4f.m31 + n;
        this.m32 = matrix4f.m32 + n;
        this.m33 = matrix4f.m33 + n;
    }
    
    public final void add(final Matrix4f matrix4f, final Matrix4f matrix4f2) {
        this.m00 = matrix4f.m00 + matrix4f2.m00;
        this.m01 = matrix4f.m01 + matrix4f2.m01;
        this.m02 = matrix4f.m02 + matrix4f2.m02;
        this.m03 = matrix4f.m03 + matrix4f2.m03;
        this.m10 = matrix4f.m10 + matrix4f2.m10;
        this.m11 = matrix4f.m11 + matrix4f2.m11;
        this.m12 = matrix4f.m12 + matrix4f2.m12;
        this.m13 = matrix4f.m13 + matrix4f2.m13;
        this.m20 = matrix4f.m20 + matrix4f2.m20;
        this.m21 = matrix4f.m21 + matrix4f2.m21;
        this.m22 = matrix4f.m22 + matrix4f2.m22;
        this.m23 = matrix4f.m23 + matrix4f2.m23;
        this.m30 = matrix4f.m30 + matrix4f2.m30;
        this.m31 = matrix4f.m31 + matrix4f2.m31;
        this.m32 = matrix4f.m32 + matrix4f2.m32;
        this.m33 = matrix4f.m33 + matrix4f2.m33;
    }
    
    public final void add(final Matrix4f matrix4f) {
        this.m00 += matrix4f.m00;
        this.m01 += matrix4f.m01;
        this.m02 += matrix4f.m02;
        this.m03 += matrix4f.m03;
        this.m10 += matrix4f.m10;
        this.m11 += matrix4f.m11;
        this.m12 += matrix4f.m12;
        this.m13 += matrix4f.m13;
        this.m20 += matrix4f.m20;
        this.m21 += matrix4f.m21;
        this.m22 += matrix4f.m22;
        this.m23 += matrix4f.m23;
        this.m30 += matrix4f.m30;
        this.m31 += matrix4f.m31;
        this.m32 += matrix4f.m32;
        this.m33 += matrix4f.m33;
    }
    
    public final void sub(final Matrix4f matrix4f, final Matrix4f matrix4f2) {
        this.m00 = matrix4f.m00 - matrix4f2.m00;
        this.m01 = matrix4f.m01 - matrix4f2.m01;
        this.m02 = matrix4f.m02 - matrix4f2.m02;
        this.m03 = matrix4f.m03 - matrix4f2.m03;
        this.m10 = matrix4f.m10 - matrix4f2.m10;
        this.m11 = matrix4f.m11 - matrix4f2.m11;
        this.m12 = matrix4f.m12 - matrix4f2.m12;
        this.m13 = matrix4f.m13 - matrix4f2.m13;
        this.m20 = matrix4f.m20 - matrix4f2.m20;
        this.m21 = matrix4f.m21 - matrix4f2.m21;
        this.m22 = matrix4f.m22 - matrix4f2.m22;
        this.m23 = matrix4f.m23 - matrix4f2.m23;
        this.m30 = matrix4f.m30 - matrix4f2.m30;
        this.m31 = matrix4f.m31 - matrix4f2.m31;
        this.m32 = matrix4f.m32 - matrix4f2.m32;
        this.m33 = matrix4f.m33 - matrix4f2.m33;
    }
    
    public final void sub(final Matrix4f matrix4f) {
        this.m00 -= matrix4f.m00;
        this.m01 -= matrix4f.m01;
        this.m02 -= matrix4f.m02;
        this.m03 -= matrix4f.m03;
        this.m10 -= matrix4f.m10;
        this.m11 -= matrix4f.m11;
        this.m12 -= matrix4f.m12;
        this.m13 -= matrix4f.m13;
        this.m20 -= matrix4f.m20;
        this.m21 -= matrix4f.m21;
        this.m22 -= matrix4f.m22;
        this.m23 -= matrix4f.m23;
        this.m30 -= matrix4f.m30;
        this.m31 -= matrix4f.m31;
        this.m32 -= matrix4f.m32;
        this.m33 -= matrix4f.m33;
    }
    
    public final void transpose() {
        final float m10 = this.m10;
        this.m10 = this.m01;
        this.m01 = m10;
        final float m11 = this.m20;
        this.m20 = this.m02;
        this.m02 = m11;
        final float m12 = this.m30;
        this.m30 = this.m03;
        this.m03 = m12;
        final float m13 = this.m21;
        this.m21 = this.m12;
        this.m12 = m13;
        final float m14 = this.m31;
        this.m31 = this.m13;
        this.m13 = m14;
        final float m15 = this.m32;
        this.m32 = this.m23;
        this.m23 = m15;
    }
    
    public final void transpose(final Matrix4f matrix4f) {
        if (this != matrix4f) {
            this.m00 = matrix4f.m00;
            this.m01 = matrix4f.m10;
            this.m02 = matrix4f.m20;
            this.m03 = matrix4f.m30;
            this.m10 = matrix4f.m01;
            this.m11 = matrix4f.m11;
            this.m12 = matrix4f.m21;
            this.m13 = matrix4f.m31;
            this.m20 = matrix4f.m02;
            this.m21 = matrix4f.m12;
            this.m22 = matrix4f.m22;
            this.m23 = matrix4f.m32;
            this.m30 = matrix4f.m03;
            this.m31 = matrix4f.m13;
            this.m32 = matrix4f.m23;
            this.m33 = matrix4f.m33;
        }
        else {
            this.transpose();
        }
    }
    
    public final void set(final Quat4f quat4f) {
        this.m00 = 1.0f - 2.0f * quat4f.y * quat4f.y - 2.0f * quat4f.z * quat4f.z;
        this.m10 = 2.0f * (quat4f.x * quat4f.y + quat4f.w * quat4f.z);
        this.m20 = 2.0f * (quat4f.x * quat4f.z - quat4f.w * quat4f.y);
        this.m01 = 2.0f * (quat4f.x * quat4f.y - quat4f.w * quat4f.z);
        this.m11 = 1.0f - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.z * quat4f.z;
        this.m21 = 2.0f * (quat4f.y * quat4f.z + quat4f.w * quat4f.x);
        this.m02 = 2.0f * (quat4f.x * quat4f.z + quat4f.w * quat4f.y);
        this.m12 = 2.0f * (quat4f.y * quat4f.z - quat4f.w * quat4f.x);
        this.m22 = 1.0f - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.y * quat4f.y;
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final AxisAngle4f axisAngle4f) {
        final float n = (float)Math.sqrt(axisAngle4f.x * axisAngle4f.x + axisAngle4f.y * axisAngle4f.y + axisAngle4f.z * axisAngle4f.z);
        if (n < 1.0E-8) {
            this.m00 = 1.0f;
            this.m01 = 0.0f;
            this.m02 = 0.0f;
            this.m10 = 0.0f;
            this.m11 = 1.0f;
            this.m12 = 0.0f;
            this.m20 = 0.0f;
            this.m21 = 0.0f;
            this.m22 = 1.0f;
        }
        else {
            final float n2 = 1.0f / n;
            final float n3 = axisAngle4f.x * n2;
            final float n4 = axisAngle4f.y * n2;
            final float n5 = axisAngle4f.z * n2;
            final float n6 = (float)Math.sin(axisAngle4f.angle);
            final float n7 = (float)Math.cos(axisAngle4f.angle);
            final float n8 = 1.0f - n7;
            final float n9 = n3 * n5;
            final float n10 = n3 * n4;
            final float n11 = n4 * n5;
            this.m00 = n8 * n3 * n3 + n7;
            this.m01 = n8 * n10 - n6 * n5;
            this.m02 = n8 * n9 + n6 * n4;
            this.m10 = n8 * n10 + n6 * n5;
            this.m11 = n8 * n4 * n4 + n7;
            this.m12 = n8 * n11 - n6 * n3;
            this.m20 = n8 * n9 - n6 * n4;
            this.m21 = n8 * n11 + n6 * n3;
            this.m22 = n8 * n5 * n5 + n7;
        }
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final Quat4d quat4d) {
        this.m00 = (float)(1.0 - 2.0 * quat4d.y * quat4d.y - 2.0 * quat4d.z * quat4d.z);
        this.m10 = (float)(2.0 * (quat4d.x * quat4d.y + quat4d.w * quat4d.z));
        this.m20 = (float)(2.0 * (quat4d.x * quat4d.z - quat4d.w * quat4d.y));
        this.m01 = (float)(2.0 * (quat4d.x * quat4d.y - quat4d.w * quat4d.z));
        this.m11 = (float)(1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.z * quat4d.z);
        this.m21 = (float)(2.0 * (quat4d.y * quat4d.z + quat4d.w * quat4d.x));
        this.m02 = (float)(2.0 * (quat4d.x * quat4d.z + quat4d.w * quat4d.y));
        this.m12 = (float)(2.0 * (quat4d.y * quat4d.z - quat4d.w * quat4d.x));
        this.m22 = (float)(1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.y * quat4d.y);
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final AxisAngle4d axisAngle4d) {
        final double sqrt = Math.sqrt(axisAngle4d.x * axisAngle4d.x + axisAngle4d.y * axisAngle4d.y + axisAngle4d.z * axisAngle4d.z);
        if (sqrt < 1.0E-8) {
            this.m00 = 1.0f;
            this.m01 = 0.0f;
            this.m02 = 0.0f;
            this.m10 = 0.0f;
            this.m11 = 1.0f;
            this.m12 = 0.0f;
            this.m20 = 0.0f;
            this.m21 = 0.0f;
            this.m22 = 1.0f;
        }
        else {
            final double n = 1.0 / sqrt;
            final double n2 = axisAngle4d.x * n;
            final double n3 = axisAngle4d.y * n;
            final double n4 = axisAngle4d.z * n;
            final float n5 = (float)Math.sin(axisAngle4d.angle);
            final float n6 = (float)Math.cos(axisAngle4d.angle);
            final float n7 = 1.0f - n6;
            final float n8 = (float)(n2 * n4);
            final float n9 = (float)(n2 * n3);
            final float n10 = (float)(n3 * n4);
            this.m00 = n7 * (float)(n2 * n2) + n6;
            this.m01 = n7 * n9 - n5 * (float)n4;
            this.m02 = n7 * n8 + n5 * (float)n3;
            this.m10 = n7 * n9 + n5 * (float)n4;
            this.m11 = n7 * (float)(n3 * n3) + n6;
            this.m12 = n7 * n10 - n5 * (float)n2;
            this.m20 = n7 * n8 - n5 * (float)n3;
            this.m21 = n7 * n10 + n5 * (float)n2;
            this.m22 = n7 * (float)(n4 * n4) + n6;
        }
        this.m03 = 0.0f;
        this.m13 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final Quat4d quat4d, final Vector3d vector3d, final double n) {
        this.m00 = (float)(n * (1.0 - 2.0 * quat4d.y * quat4d.y - 2.0 * quat4d.z * quat4d.z));
        this.m10 = (float)(n * (2.0 * (quat4d.x * quat4d.y + quat4d.w * quat4d.z)));
        this.m20 = (float)(n * (2.0 * (quat4d.x * quat4d.z - quat4d.w * quat4d.y)));
        this.m01 = (float)(n * (2.0 * (quat4d.x * quat4d.y - quat4d.w * quat4d.z)));
        this.m11 = (float)(n * (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.z * quat4d.z));
        this.m21 = (float)(n * (2.0 * (quat4d.y * quat4d.z + quat4d.w * quat4d.x)));
        this.m02 = (float)(n * (2.0 * (quat4d.x * quat4d.z + quat4d.w * quat4d.y)));
        this.m12 = (float)(n * (2.0 * (quat4d.y * quat4d.z - quat4d.w * quat4d.x)));
        this.m22 = (float)(n * (1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.y * quat4d.y));
        this.m03 = (float)vector3d.x;
        this.m13 = (float)vector3d.y;
        this.m23 = (float)vector3d.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final Quat4f quat4f, final Vector3f vector3f, final float n) {
        this.m00 = n * (1.0f - 2.0f * quat4f.y * quat4f.y - 2.0f * quat4f.z * quat4f.z);
        this.m10 = n * (2.0f * (quat4f.x * quat4f.y + quat4f.w * quat4f.z));
        this.m20 = n * (2.0f * (quat4f.x * quat4f.z - quat4f.w * quat4f.y));
        this.m01 = n * (2.0f * (quat4f.x * quat4f.y - quat4f.w * quat4f.z));
        this.m11 = n * (1.0f - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.z * quat4f.z);
        this.m21 = n * (2.0f * (quat4f.y * quat4f.z + quat4f.w * quat4f.x));
        this.m02 = n * (2.0f * (quat4f.x * quat4f.z + quat4f.w * quat4f.y));
        this.m12 = n * (2.0f * (quat4f.y * quat4f.z - quat4f.w * quat4f.x));
        this.m22 = n * (1.0f - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.y * quat4f.y);
        this.m03 = vector3f.x;
        this.m13 = vector3f.y;
        this.m23 = vector3f.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final Matrix4d matrix4d) {
        this.m00 = (float)matrix4d.m00;
        this.m01 = (float)matrix4d.m01;
        this.m02 = (float)matrix4d.m02;
        this.m03 = (float)matrix4d.m03;
        this.m10 = (float)matrix4d.m10;
        this.m11 = (float)matrix4d.m11;
        this.m12 = (float)matrix4d.m12;
        this.m13 = (float)matrix4d.m13;
        this.m20 = (float)matrix4d.m20;
        this.m21 = (float)matrix4d.m21;
        this.m22 = (float)matrix4d.m22;
        this.m23 = (float)matrix4d.m23;
        this.m30 = (float)matrix4d.m30;
        this.m31 = (float)matrix4d.m31;
        this.m32 = (float)matrix4d.m32;
        this.m33 = (float)matrix4d.m33;
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
    
    public final void invert(final Matrix4f matrix4f) {
        this.invertGeneral(matrix4f);
    }
    
    public final void invert() {
        this.invertGeneral(this);
    }
    
    final void invertGeneral(final Matrix4f matrix4f) {
        final double[] array = new double[16];
        final double[] array2 = new double[16];
        final int[] array3 = new int[4];
        array[0] = matrix4f.m00;
        array[1] = matrix4f.m01;
        array[2] = matrix4f.m02;
        array[3] = matrix4f.m03;
        array[4] = matrix4f.m10;
        array[5] = matrix4f.m11;
        array[6] = matrix4f.m12;
        array[7] = matrix4f.m13;
        array[8] = matrix4f.m20;
        array[9] = matrix4f.m21;
        array[10] = matrix4f.m22;
        array[11] = matrix4f.m23;
        array[12] = matrix4f.m30;
        array[13] = matrix4f.m31;
        array[14] = matrix4f.m32;
        array[15] = matrix4f.m33;
        if (!luDecomposition(array, array3)) {
            throw new SingularMatrixException(VecMathI18N.getString("Matrix4f12"));
        }
        for (int i = 0; i < 16; ++i) {
            array2[i] = 0.0;
        }
        array2[5] = (array2[0] = 1.0);
        array2[15] = (array2[10] = 1.0);
        luBacksubstitution(array, array3, array2);
        this.m00 = (float)array2[0];
        this.m01 = (float)array2[1];
        this.m02 = (float)array2[2];
        this.m03 = (float)array2[3];
        this.m10 = (float)array2[4];
        this.m11 = (float)array2[5];
        this.m12 = (float)array2[6];
        this.m13 = (float)array2[7];
        this.m20 = (float)array2[8];
        this.m21 = (float)array2[9];
        this.m22 = (float)array2[10];
        this.m23 = (float)array2[11];
        this.m30 = (float)array2[12];
        this.m31 = (float)array2[13];
        this.m32 = (float)array2[14];
        this.m33 = (float)array2[15];
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
                throw new RuntimeException(VecMathI18N.getString("Matrix4f13"));
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
    
    public final float determinant() {
        return this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33) - this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33) + this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33) - this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
    }
    
    public final void set(final Matrix3f matrix3f) {
        this.m00 = matrix3f.m00;
        this.m01 = matrix3f.m01;
        this.m02 = matrix3f.m02;
        this.m03 = 0.0f;
        this.m10 = matrix3f.m10;
        this.m11 = matrix3f.m11;
        this.m12 = matrix3f.m12;
        this.m13 = 0.0f;
        this.m20 = matrix3f.m20;
        this.m21 = matrix3f.m21;
        this.m22 = matrix3f.m22;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final Matrix3d matrix3d) {
        this.m00 = (float)matrix3d.m00;
        this.m01 = (float)matrix3d.m01;
        this.m02 = (float)matrix3d.m02;
        this.m03 = 0.0f;
        this.m10 = (float)matrix3d.m10;
        this.m11 = (float)matrix3d.m11;
        this.m12 = (float)matrix3d.m12;
        this.m13 = 0.0f;
        this.m20 = (float)matrix3d.m20;
        this.m21 = (float)matrix3d.m21;
        this.m22 = (float)matrix3d.m22;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final float m22) {
        this.m00 = m22;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = m22;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = m22;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final float[] array) {
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
    
    public final void set(final Vector3f vector3f) {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = vector3f.x;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = vector3f.y;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = vector3f.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final float m22, final Vector3f vector3f) {
        this.m00 = m22;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = vector3f.x;
        this.m10 = 0.0f;
        this.m11 = m22;
        this.m12 = 0.0f;
        this.m13 = vector3f.y;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = m22;
        this.m23 = vector3f.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final Vector3f vector3f, final float m22) {
        this.m00 = m22;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = m22 * vector3f.x;
        this.m10 = 0.0f;
        this.m11 = m22;
        this.m12 = 0.0f;
        this.m13 = m22 * vector3f.y;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = m22;
        this.m23 = m22 * vector3f.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
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
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void set(final Matrix3d matrix3d, final Vector3d vector3d, final double n) {
        this.m00 = (float)(matrix3d.m00 * n);
        this.m01 = (float)(matrix3d.m01 * n);
        this.m02 = (float)(matrix3d.m02 * n);
        this.m03 = (float)vector3d.x;
        this.m10 = (float)(matrix3d.m10 * n);
        this.m11 = (float)(matrix3d.m11 * n);
        this.m12 = (float)(matrix3d.m12 * n);
        this.m13 = (float)vector3d.y;
        this.m20 = (float)(matrix3d.m20 * n);
        this.m21 = (float)(matrix3d.m21 * n);
        this.m22 = (float)(matrix3d.m22 * n);
        this.m23 = (float)vector3d.z;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void setTranslation(final Vector3f vector3f) {
        this.m03 = vector3f.x;
        this.m13 = vector3f.y;
        this.m23 = vector3f.z;
    }
    
    public final void rotX(final float n) {
        final float m21 = (float)Math.sin(n);
        final float n2 = (float)Math.cos(n);
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = n2;
        this.m12 = -m21;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = m21;
        this.m22 = n2;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void rotY(final float n) {
        final float m02 = (float)Math.sin(n);
        final float n2 = (float)Math.cos(n);
        this.m00 = n2;
        this.m01 = 0.0f;
        this.m02 = m02;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = -m02;
        this.m21 = 0.0f;
        this.m22 = n2;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void rotZ(final float n) {
        final float m10 = (float)Math.sin(n);
        final float n2 = (float)Math.cos(n);
        this.m00 = n2;
        this.m01 = -m10;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = m10;
        this.m11 = n2;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public final void mul(final float n) {
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
    
    public final void mul(final float n, final Matrix4f matrix4f) {
        this.m00 = matrix4f.m00 * n;
        this.m01 = matrix4f.m01 * n;
        this.m02 = matrix4f.m02 * n;
        this.m03 = matrix4f.m03 * n;
        this.m10 = matrix4f.m10 * n;
        this.m11 = matrix4f.m11 * n;
        this.m12 = matrix4f.m12 * n;
        this.m13 = matrix4f.m13 * n;
        this.m20 = matrix4f.m20 * n;
        this.m21 = matrix4f.m21 * n;
        this.m22 = matrix4f.m22 * n;
        this.m23 = matrix4f.m23 * n;
        this.m30 = matrix4f.m30 * n;
        this.m31 = matrix4f.m31 * n;
        this.m32 = matrix4f.m32 * n;
        this.m33 = matrix4f.m33 * n;
    }
    
    public final void mul(final Matrix4f matrix4f) {
        final float m00 = this.m00 * matrix4f.m00 + this.m01 * matrix4f.m10 + this.m02 * matrix4f.m20 + this.m03 * matrix4f.m30;
        final float m2 = this.m00 * matrix4f.m01 + this.m01 * matrix4f.m11 + this.m02 * matrix4f.m21 + this.m03 * matrix4f.m31;
        final float m3 = this.m00 * matrix4f.m02 + this.m01 * matrix4f.m12 + this.m02 * matrix4f.m22 + this.m03 * matrix4f.m32;
        final float m4 = this.m00 * matrix4f.m03 + this.m01 * matrix4f.m13 + this.m02 * matrix4f.m23 + this.m03 * matrix4f.m33;
        final float m5 = this.m10 * matrix4f.m00 + this.m11 * matrix4f.m10 + this.m12 * matrix4f.m20 + this.m13 * matrix4f.m30;
        final float m6 = this.m10 * matrix4f.m01 + this.m11 * matrix4f.m11 + this.m12 * matrix4f.m21 + this.m13 * matrix4f.m31;
        final float m7 = this.m10 * matrix4f.m02 + this.m11 * matrix4f.m12 + this.m12 * matrix4f.m22 + this.m13 * matrix4f.m32;
        final float m8 = this.m10 * matrix4f.m03 + this.m11 * matrix4f.m13 + this.m12 * matrix4f.m23 + this.m13 * matrix4f.m33;
        final float m9 = this.m20 * matrix4f.m00 + this.m21 * matrix4f.m10 + this.m22 * matrix4f.m20 + this.m23 * matrix4f.m30;
        final float m10 = this.m20 * matrix4f.m01 + this.m21 * matrix4f.m11 + this.m22 * matrix4f.m21 + this.m23 * matrix4f.m31;
        final float m11 = this.m20 * matrix4f.m02 + this.m21 * matrix4f.m12 + this.m22 * matrix4f.m22 + this.m23 * matrix4f.m32;
        final float m12 = this.m20 * matrix4f.m03 + this.m21 * matrix4f.m13 + this.m22 * matrix4f.m23 + this.m23 * matrix4f.m33;
        final float m13 = this.m30 * matrix4f.m00 + this.m31 * matrix4f.m10 + this.m32 * matrix4f.m20 + this.m33 * matrix4f.m30;
        final float m14 = this.m30 * matrix4f.m01 + this.m31 * matrix4f.m11 + this.m32 * matrix4f.m21 + this.m33 * matrix4f.m31;
        final float m15 = this.m30 * matrix4f.m02 + this.m31 * matrix4f.m12 + this.m32 * matrix4f.m22 + this.m33 * matrix4f.m32;
        final float m16 = this.m30 * matrix4f.m03 + this.m31 * matrix4f.m13 + this.m32 * matrix4f.m23 + this.m33 * matrix4f.m33;
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
    
    public final void mul(final Matrix4f matrix4f, final Matrix4f matrix4f2) {
        if (this != matrix4f && this != matrix4f2) {
            this.m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m01 * matrix4f2.m10 + matrix4f.m02 * matrix4f2.m20 + matrix4f.m03 * matrix4f2.m30;
            this.m01 = matrix4f.m00 * matrix4f2.m01 + matrix4f.m01 * matrix4f2.m11 + matrix4f.m02 * matrix4f2.m21 + matrix4f.m03 * matrix4f2.m31;
            this.m02 = matrix4f.m00 * matrix4f2.m02 + matrix4f.m01 * matrix4f2.m12 + matrix4f.m02 * matrix4f2.m22 + matrix4f.m03 * matrix4f2.m32;
            this.m03 = matrix4f.m00 * matrix4f2.m03 + matrix4f.m01 * matrix4f2.m13 + matrix4f.m02 * matrix4f2.m23 + matrix4f.m03 * matrix4f2.m33;
            this.m10 = matrix4f.m10 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m10 + matrix4f.m12 * matrix4f2.m20 + matrix4f.m13 * matrix4f2.m30;
            this.m11 = matrix4f.m10 * matrix4f2.m01 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m12 * matrix4f2.m21 + matrix4f.m13 * matrix4f2.m31;
            this.m12 = matrix4f.m10 * matrix4f2.m02 + matrix4f.m11 * matrix4f2.m12 + matrix4f.m12 * matrix4f2.m22 + matrix4f.m13 * matrix4f2.m32;
            this.m13 = matrix4f.m10 * matrix4f2.m03 + matrix4f.m11 * matrix4f2.m13 + matrix4f.m12 * matrix4f2.m23 + matrix4f.m13 * matrix4f2.m33;
            this.m20 = matrix4f.m20 * matrix4f2.m00 + matrix4f.m21 * matrix4f2.m10 + matrix4f.m22 * matrix4f2.m20 + matrix4f.m23 * matrix4f2.m30;
            this.m21 = matrix4f.m20 * matrix4f2.m01 + matrix4f.m21 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m21 + matrix4f.m23 * matrix4f2.m31;
            this.m22 = matrix4f.m20 * matrix4f2.m02 + matrix4f.m21 * matrix4f2.m12 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m23 * matrix4f2.m32;
            this.m23 = matrix4f.m20 * matrix4f2.m03 + matrix4f.m21 * matrix4f2.m13 + matrix4f.m22 * matrix4f2.m23 + matrix4f.m23 * matrix4f2.m33;
            this.m30 = matrix4f.m30 * matrix4f2.m00 + matrix4f.m31 * matrix4f2.m10 + matrix4f.m32 * matrix4f2.m20 + matrix4f.m33 * matrix4f2.m30;
            this.m31 = matrix4f.m30 * matrix4f2.m01 + matrix4f.m31 * matrix4f2.m11 + matrix4f.m32 * matrix4f2.m21 + matrix4f.m33 * matrix4f2.m31;
            this.m32 = matrix4f.m30 * matrix4f2.m02 + matrix4f.m31 * matrix4f2.m12 + matrix4f.m32 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m32;
            this.m33 = matrix4f.m30 * matrix4f2.m03 + matrix4f.m31 * matrix4f2.m13 + matrix4f.m32 * matrix4f2.m23 + matrix4f.m33 * matrix4f2.m33;
        }
        else {
            final float m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m01 * matrix4f2.m10 + matrix4f.m02 * matrix4f2.m20 + matrix4f.m03 * matrix4f2.m30;
            final float m2 = matrix4f.m00 * matrix4f2.m01 + matrix4f.m01 * matrix4f2.m11 + matrix4f.m02 * matrix4f2.m21 + matrix4f.m03 * matrix4f2.m31;
            final float m3 = matrix4f.m00 * matrix4f2.m02 + matrix4f.m01 * matrix4f2.m12 + matrix4f.m02 * matrix4f2.m22 + matrix4f.m03 * matrix4f2.m32;
            final float m4 = matrix4f.m00 * matrix4f2.m03 + matrix4f.m01 * matrix4f2.m13 + matrix4f.m02 * matrix4f2.m23 + matrix4f.m03 * matrix4f2.m33;
            final float m5 = matrix4f.m10 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m10 + matrix4f.m12 * matrix4f2.m20 + matrix4f.m13 * matrix4f2.m30;
            final float m6 = matrix4f.m10 * matrix4f2.m01 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m12 * matrix4f2.m21 + matrix4f.m13 * matrix4f2.m31;
            final float m7 = matrix4f.m10 * matrix4f2.m02 + matrix4f.m11 * matrix4f2.m12 + matrix4f.m12 * matrix4f2.m22 + matrix4f.m13 * matrix4f2.m32;
            final float m8 = matrix4f.m10 * matrix4f2.m03 + matrix4f.m11 * matrix4f2.m13 + matrix4f.m12 * matrix4f2.m23 + matrix4f.m13 * matrix4f2.m33;
            final float m9 = matrix4f.m20 * matrix4f2.m00 + matrix4f.m21 * matrix4f2.m10 + matrix4f.m22 * matrix4f2.m20 + matrix4f.m23 * matrix4f2.m30;
            final float m10 = matrix4f.m20 * matrix4f2.m01 + matrix4f.m21 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m21 + matrix4f.m23 * matrix4f2.m31;
            final float m11 = matrix4f.m20 * matrix4f2.m02 + matrix4f.m21 * matrix4f2.m12 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m23 * matrix4f2.m32;
            final float m12 = matrix4f.m20 * matrix4f2.m03 + matrix4f.m21 * matrix4f2.m13 + matrix4f.m22 * matrix4f2.m23 + matrix4f.m23 * matrix4f2.m33;
            final float m13 = matrix4f.m30 * matrix4f2.m00 + matrix4f.m31 * matrix4f2.m10 + matrix4f.m32 * matrix4f2.m20 + matrix4f.m33 * matrix4f2.m30;
            final float m14 = matrix4f.m30 * matrix4f2.m01 + matrix4f.m31 * matrix4f2.m11 + matrix4f.m32 * matrix4f2.m21 + matrix4f.m33 * matrix4f2.m31;
            final float m15 = matrix4f.m30 * matrix4f2.m02 + matrix4f.m31 * matrix4f2.m12 + matrix4f.m32 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m32;
            final float m16 = matrix4f.m30 * matrix4f2.m03 + matrix4f.m31 * matrix4f2.m13 + matrix4f.m32 * matrix4f2.m23 + matrix4f.m33 * matrix4f2.m33;
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
    
    public final void mulTransposeBoth(final Matrix4f matrix4f, final Matrix4f matrix4f2) {
        if (this != matrix4f && this != matrix4f2) {
            this.m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m10 * matrix4f2.m01 + matrix4f.m20 * matrix4f2.m02 + matrix4f.m30 * matrix4f2.m03;
            this.m01 = matrix4f.m00 * matrix4f2.m10 + matrix4f.m10 * matrix4f2.m11 + matrix4f.m20 * matrix4f2.m12 + matrix4f.m30 * matrix4f2.m13;
            this.m02 = matrix4f.m00 * matrix4f2.m20 + matrix4f.m10 * matrix4f2.m21 + matrix4f.m20 * matrix4f2.m22 + matrix4f.m30 * matrix4f2.m23;
            this.m03 = matrix4f.m00 * matrix4f2.m30 + matrix4f.m10 * matrix4f2.m31 + matrix4f.m20 * matrix4f2.m32 + matrix4f.m30 * matrix4f2.m33;
            this.m10 = matrix4f.m01 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m01 + matrix4f.m21 * matrix4f2.m02 + matrix4f.m31 * matrix4f2.m03;
            this.m11 = matrix4f.m01 * matrix4f2.m10 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m21 * matrix4f2.m12 + matrix4f.m31 * matrix4f2.m13;
            this.m12 = matrix4f.m01 * matrix4f2.m20 + matrix4f.m11 * matrix4f2.m21 + matrix4f.m21 * matrix4f2.m22 + matrix4f.m31 * matrix4f2.m23;
            this.m13 = matrix4f.m01 * matrix4f2.m30 + matrix4f.m11 * matrix4f2.m31 + matrix4f.m21 * matrix4f2.m32 + matrix4f.m31 * matrix4f2.m33;
            this.m20 = matrix4f.m02 * matrix4f2.m00 + matrix4f.m12 * matrix4f2.m01 + matrix4f.m22 * matrix4f2.m02 + matrix4f.m32 * matrix4f2.m03;
            this.m21 = matrix4f.m02 * matrix4f2.m10 + matrix4f.m12 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m12 + matrix4f.m32 * matrix4f2.m13;
            this.m22 = matrix4f.m02 * matrix4f2.m20 + matrix4f.m12 * matrix4f2.m21 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m32 * matrix4f2.m23;
            this.m23 = matrix4f.m02 * matrix4f2.m30 + matrix4f.m12 * matrix4f2.m31 + matrix4f.m22 * matrix4f2.m32 + matrix4f.m32 * matrix4f2.m33;
            this.m30 = matrix4f.m03 * matrix4f2.m00 + matrix4f.m13 * matrix4f2.m01 + matrix4f.m23 * matrix4f2.m02 + matrix4f.m33 * matrix4f2.m03;
            this.m31 = matrix4f.m03 * matrix4f2.m10 + matrix4f.m13 * matrix4f2.m11 + matrix4f.m23 * matrix4f2.m12 + matrix4f.m33 * matrix4f2.m13;
            this.m32 = matrix4f.m03 * matrix4f2.m20 + matrix4f.m13 * matrix4f2.m21 + matrix4f.m23 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m23;
            this.m33 = matrix4f.m03 * matrix4f2.m30 + matrix4f.m13 * matrix4f2.m31 + matrix4f.m23 * matrix4f2.m32 + matrix4f.m33 * matrix4f2.m33;
        }
        else {
            final float m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m10 * matrix4f2.m01 + matrix4f.m20 * matrix4f2.m02 + matrix4f.m30 * matrix4f2.m03;
            final float m2 = matrix4f.m00 * matrix4f2.m10 + matrix4f.m10 * matrix4f2.m11 + matrix4f.m20 * matrix4f2.m12 + matrix4f.m30 * matrix4f2.m13;
            final float m3 = matrix4f.m00 * matrix4f2.m20 + matrix4f.m10 * matrix4f2.m21 + matrix4f.m20 * matrix4f2.m22 + matrix4f.m30 * matrix4f2.m23;
            final float m4 = matrix4f.m00 * matrix4f2.m30 + matrix4f.m10 * matrix4f2.m31 + matrix4f.m20 * matrix4f2.m32 + matrix4f.m30 * matrix4f2.m33;
            final float m5 = matrix4f.m01 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m01 + matrix4f.m21 * matrix4f2.m02 + matrix4f.m31 * matrix4f2.m03;
            final float m6 = matrix4f.m01 * matrix4f2.m10 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m21 * matrix4f2.m12 + matrix4f.m31 * matrix4f2.m13;
            final float m7 = matrix4f.m01 * matrix4f2.m20 + matrix4f.m11 * matrix4f2.m21 + matrix4f.m21 * matrix4f2.m22 + matrix4f.m31 * matrix4f2.m23;
            final float m8 = matrix4f.m01 * matrix4f2.m30 + matrix4f.m11 * matrix4f2.m31 + matrix4f.m21 * matrix4f2.m32 + matrix4f.m31 * matrix4f2.m33;
            final float m9 = matrix4f.m02 * matrix4f2.m00 + matrix4f.m12 * matrix4f2.m01 + matrix4f.m22 * matrix4f2.m02 + matrix4f.m32 * matrix4f2.m03;
            final float m10 = matrix4f.m02 * matrix4f2.m10 + matrix4f.m12 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m12 + matrix4f.m32 * matrix4f2.m13;
            final float m11 = matrix4f.m02 * matrix4f2.m20 + matrix4f.m12 * matrix4f2.m21 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m32 * matrix4f2.m23;
            final float m12 = matrix4f.m02 * matrix4f2.m30 + matrix4f.m12 * matrix4f2.m31 + matrix4f.m22 * matrix4f2.m32 + matrix4f.m32 * matrix4f2.m33;
            final float m13 = matrix4f.m03 * matrix4f2.m00 + matrix4f.m13 * matrix4f2.m01 + matrix4f.m23 * matrix4f2.m02 + matrix4f.m33 * matrix4f2.m03;
            final float m14 = matrix4f.m03 * matrix4f2.m10 + matrix4f.m13 * matrix4f2.m11 + matrix4f.m23 * matrix4f2.m12 + matrix4f.m33 * matrix4f2.m13;
            final float m15 = matrix4f.m03 * matrix4f2.m20 + matrix4f.m13 * matrix4f2.m21 + matrix4f.m23 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m23;
            final float m16 = matrix4f.m03 * matrix4f2.m30 + matrix4f.m13 * matrix4f2.m31 + matrix4f.m23 * matrix4f2.m32 + matrix4f.m33 * matrix4f2.m33;
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
    
    public final void mulTransposeRight(final Matrix4f matrix4f, final Matrix4f matrix4f2) {
        if (this != matrix4f && this != matrix4f2) {
            this.m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m01 * matrix4f2.m01 + matrix4f.m02 * matrix4f2.m02 + matrix4f.m03 * matrix4f2.m03;
            this.m01 = matrix4f.m00 * matrix4f2.m10 + matrix4f.m01 * matrix4f2.m11 + matrix4f.m02 * matrix4f2.m12 + matrix4f.m03 * matrix4f2.m13;
            this.m02 = matrix4f.m00 * matrix4f2.m20 + matrix4f.m01 * matrix4f2.m21 + matrix4f.m02 * matrix4f2.m22 + matrix4f.m03 * matrix4f2.m23;
            this.m03 = matrix4f.m00 * matrix4f2.m30 + matrix4f.m01 * matrix4f2.m31 + matrix4f.m02 * matrix4f2.m32 + matrix4f.m03 * matrix4f2.m33;
            this.m10 = matrix4f.m10 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m01 + matrix4f.m12 * matrix4f2.m02 + matrix4f.m13 * matrix4f2.m03;
            this.m11 = matrix4f.m10 * matrix4f2.m10 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m12 * matrix4f2.m12 + matrix4f.m13 * matrix4f2.m13;
            this.m12 = matrix4f.m10 * matrix4f2.m20 + matrix4f.m11 * matrix4f2.m21 + matrix4f.m12 * matrix4f2.m22 + matrix4f.m13 * matrix4f2.m23;
            this.m13 = matrix4f.m10 * matrix4f2.m30 + matrix4f.m11 * matrix4f2.m31 + matrix4f.m12 * matrix4f2.m32 + matrix4f.m13 * matrix4f2.m33;
            this.m20 = matrix4f.m20 * matrix4f2.m00 + matrix4f.m21 * matrix4f2.m01 + matrix4f.m22 * matrix4f2.m02 + matrix4f.m23 * matrix4f2.m03;
            this.m21 = matrix4f.m20 * matrix4f2.m10 + matrix4f.m21 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m12 + matrix4f.m23 * matrix4f2.m13;
            this.m22 = matrix4f.m20 * matrix4f2.m20 + matrix4f.m21 * matrix4f2.m21 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m23 * matrix4f2.m23;
            this.m23 = matrix4f.m20 * matrix4f2.m30 + matrix4f.m21 * matrix4f2.m31 + matrix4f.m22 * matrix4f2.m32 + matrix4f.m23 * matrix4f2.m33;
            this.m30 = matrix4f.m30 * matrix4f2.m00 + matrix4f.m31 * matrix4f2.m01 + matrix4f.m32 * matrix4f2.m02 + matrix4f.m33 * matrix4f2.m03;
            this.m31 = matrix4f.m30 * matrix4f2.m10 + matrix4f.m31 * matrix4f2.m11 + matrix4f.m32 * matrix4f2.m12 + matrix4f.m33 * matrix4f2.m13;
            this.m32 = matrix4f.m30 * matrix4f2.m20 + matrix4f.m31 * matrix4f2.m21 + matrix4f.m32 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m23;
            this.m33 = matrix4f.m30 * matrix4f2.m30 + matrix4f.m31 * matrix4f2.m31 + matrix4f.m32 * matrix4f2.m32 + matrix4f.m33 * matrix4f2.m33;
        }
        else {
            final float m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m01 * matrix4f2.m01 + matrix4f.m02 * matrix4f2.m02 + matrix4f.m03 * matrix4f2.m03;
            final float m2 = matrix4f.m00 * matrix4f2.m10 + matrix4f.m01 * matrix4f2.m11 + matrix4f.m02 * matrix4f2.m12 + matrix4f.m03 * matrix4f2.m13;
            final float m3 = matrix4f.m00 * matrix4f2.m20 + matrix4f.m01 * matrix4f2.m21 + matrix4f.m02 * matrix4f2.m22 + matrix4f.m03 * matrix4f2.m23;
            final float m4 = matrix4f.m00 * matrix4f2.m30 + matrix4f.m01 * matrix4f2.m31 + matrix4f.m02 * matrix4f2.m32 + matrix4f.m03 * matrix4f2.m33;
            final float m5 = matrix4f.m10 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m01 + matrix4f.m12 * matrix4f2.m02 + matrix4f.m13 * matrix4f2.m03;
            final float m6 = matrix4f.m10 * matrix4f2.m10 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m12 * matrix4f2.m12 + matrix4f.m13 * matrix4f2.m13;
            final float m7 = matrix4f.m10 * matrix4f2.m20 + matrix4f.m11 * matrix4f2.m21 + matrix4f.m12 * matrix4f2.m22 + matrix4f.m13 * matrix4f2.m23;
            final float m8 = matrix4f.m10 * matrix4f2.m30 + matrix4f.m11 * matrix4f2.m31 + matrix4f.m12 * matrix4f2.m32 + matrix4f.m13 * matrix4f2.m33;
            final float m9 = matrix4f.m20 * matrix4f2.m00 + matrix4f.m21 * matrix4f2.m01 + matrix4f.m22 * matrix4f2.m02 + matrix4f.m23 * matrix4f2.m03;
            final float m10 = matrix4f.m20 * matrix4f2.m10 + matrix4f.m21 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m12 + matrix4f.m23 * matrix4f2.m13;
            final float m11 = matrix4f.m20 * matrix4f2.m20 + matrix4f.m21 * matrix4f2.m21 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m23 * matrix4f2.m23;
            final float m12 = matrix4f.m20 * matrix4f2.m30 + matrix4f.m21 * matrix4f2.m31 + matrix4f.m22 * matrix4f2.m32 + matrix4f.m23 * matrix4f2.m33;
            final float m13 = matrix4f.m30 * matrix4f2.m00 + matrix4f.m31 * matrix4f2.m01 + matrix4f.m32 * matrix4f2.m02 + matrix4f.m33 * matrix4f2.m03;
            final float m14 = matrix4f.m30 * matrix4f2.m10 + matrix4f.m31 * matrix4f2.m11 + matrix4f.m32 * matrix4f2.m12 + matrix4f.m33 * matrix4f2.m13;
            final float m15 = matrix4f.m30 * matrix4f2.m20 + matrix4f.m31 * matrix4f2.m21 + matrix4f.m32 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m23;
            final float m16 = matrix4f.m30 * matrix4f2.m30 + matrix4f.m31 * matrix4f2.m31 + matrix4f.m32 * matrix4f2.m32 + matrix4f.m33 * matrix4f2.m33;
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
    
    public final void mulTransposeLeft(final Matrix4f matrix4f, final Matrix4f matrix4f2) {
        if (this != matrix4f && this != matrix4f2) {
            this.m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m10 * matrix4f2.m10 + matrix4f.m20 * matrix4f2.m20 + matrix4f.m30 * matrix4f2.m30;
            this.m01 = matrix4f.m00 * matrix4f2.m01 + matrix4f.m10 * matrix4f2.m11 + matrix4f.m20 * matrix4f2.m21 + matrix4f.m30 * matrix4f2.m31;
            this.m02 = matrix4f.m00 * matrix4f2.m02 + matrix4f.m10 * matrix4f2.m12 + matrix4f.m20 * matrix4f2.m22 + matrix4f.m30 * matrix4f2.m32;
            this.m03 = matrix4f.m00 * matrix4f2.m03 + matrix4f.m10 * matrix4f2.m13 + matrix4f.m20 * matrix4f2.m23 + matrix4f.m30 * matrix4f2.m33;
            this.m10 = matrix4f.m01 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m10 + matrix4f.m21 * matrix4f2.m20 + matrix4f.m31 * matrix4f2.m30;
            this.m11 = matrix4f.m01 * matrix4f2.m01 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m21 * matrix4f2.m21 + matrix4f.m31 * matrix4f2.m31;
            this.m12 = matrix4f.m01 * matrix4f2.m02 + matrix4f.m11 * matrix4f2.m12 + matrix4f.m21 * matrix4f2.m22 + matrix4f.m31 * matrix4f2.m32;
            this.m13 = matrix4f.m01 * matrix4f2.m03 + matrix4f.m11 * matrix4f2.m13 + matrix4f.m21 * matrix4f2.m23 + matrix4f.m31 * matrix4f2.m33;
            this.m20 = matrix4f.m02 * matrix4f2.m00 + matrix4f.m12 * matrix4f2.m10 + matrix4f.m22 * matrix4f2.m20 + matrix4f.m32 * matrix4f2.m30;
            this.m21 = matrix4f.m02 * matrix4f2.m01 + matrix4f.m12 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m21 + matrix4f.m32 * matrix4f2.m31;
            this.m22 = matrix4f.m02 * matrix4f2.m02 + matrix4f.m12 * matrix4f2.m12 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m32 * matrix4f2.m32;
            this.m23 = matrix4f.m02 * matrix4f2.m03 + matrix4f.m12 * matrix4f2.m13 + matrix4f.m22 * matrix4f2.m23 + matrix4f.m32 * matrix4f2.m33;
            this.m30 = matrix4f.m03 * matrix4f2.m00 + matrix4f.m13 * matrix4f2.m10 + matrix4f.m23 * matrix4f2.m20 + matrix4f.m33 * matrix4f2.m30;
            this.m31 = matrix4f.m03 * matrix4f2.m01 + matrix4f.m13 * matrix4f2.m11 + matrix4f.m23 * matrix4f2.m21 + matrix4f.m33 * matrix4f2.m31;
            this.m32 = matrix4f.m03 * matrix4f2.m02 + matrix4f.m13 * matrix4f2.m12 + matrix4f.m23 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m32;
            this.m33 = matrix4f.m03 * matrix4f2.m03 + matrix4f.m13 * matrix4f2.m13 + matrix4f.m23 * matrix4f2.m23 + matrix4f.m33 * matrix4f2.m33;
        }
        else {
            final float m00 = matrix4f.m00 * matrix4f2.m00 + matrix4f.m10 * matrix4f2.m10 + matrix4f.m20 * matrix4f2.m20 + matrix4f.m30 * matrix4f2.m30;
            final float m2 = matrix4f.m00 * matrix4f2.m01 + matrix4f.m10 * matrix4f2.m11 + matrix4f.m20 * matrix4f2.m21 + matrix4f.m30 * matrix4f2.m31;
            final float m3 = matrix4f.m00 * matrix4f2.m02 + matrix4f.m10 * matrix4f2.m12 + matrix4f.m20 * matrix4f2.m22 + matrix4f.m30 * matrix4f2.m32;
            final float m4 = matrix4f.m00 * matrix4f2.m03 + matrix4f.m10 * matrix4f2.m13 + matrix4f.m20 * matrix4f2.m23 + matrix4f.m30 * matrix4f2.m33;
            final float m5 = matrix4f.m01 * matrix4f2.m00 + matrix4f.m11 * matrix4f2.m10 + matrix4f.m21 * matrix4f2.m20 + matrix4f.m31 * matrix4f2.m30;
            final float m6 = matrix4f.m01 * matrix4f2.m01 + matrix4f.m11 * matrix4f2.m11 + matrix4f.m21 * matrix4f2.m21 + matrix4f.m31 * matrix4f2.m31;
            final float m7 = matrix4f.m01 * matrix4f2.m02 + matrix4f.m11 * matrix4f2.m12 + matrix4f.m21 * matrix4f2.m22 + matrix4f.m31 * matrix4f2.m32;
            final float m8 = matrix4f.m01 * matrix4f2.m03 + matrix4f.m11 * matrix4f2.m13 + matrix4f.m21 * matrix4f2.m23 + matrix4f.m31 * matrix4f2.m33;
            final float m9 = matrix4f.m02 * matrix4f2.m00 + matrix4f.m12 * matrix4f2.m10 + matrix4f.m22 * matrix4f2.m20 + matrix4f.m32 * matrix4f2.m30;
            final float m10 = matrix4f.m02 * matrix4f2.m01 + matrix4f.m12 * matrix4f2.m11 + matrix4f.m22 * matrix4f2.m21 + matrix4f.m32 * matrix4f2.m31;
            final float m11 = matrix4f.m02 * matrix4f2.m02 + matrix4f.m12 * matrix4f2.m12 + matrix4f.m22 * matrix4f2.m22 + matrix4f.m32 * matrix4f2.m32;
            final float m12 = matrix4f.m02 * matrix4f2.m03 + matrix4f.m12 * matrix4f2.m13 + matrix4f.m22 * matrix4f2.m23 + matrix4f.m32 * matrix4f2.m33;
            final float m13 = matrix4f.m03 * matrix4f2.m00 + matrix4f.m13 * matrix4f2.m10 + matrix4f.m23 * matrix4f2.m20 + matrix4f.m33 * matrix4f2.m30;
            final float m14 = matrix4f.m03 * matrix4f2.m01 + matrix4f.m13 * matrix4f2.m11 + matrix4f.m23 * matrix4f2.m21 + matrix4f.m33 * matrix4f2.m31;
            final float m15 = matrix4f.m03 * matrix4f2.m02 + matrix4f.m13 * matrix4f2.m12 + matrix4f.m23 * matrix4f2.m22 + matrix4f.m33 * matrix4f2.m32;
            final float m16 = matrix4f.m03 * matrix4f2.m03 + matrix4f.m13 * matrix4f2.m13 + matrix4f.m23 * matrix4f2.m23 + matrix4f.m33 * matrix4f2.m33;
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
    
    public boolean equals(final Matrix4f matrix4f) {
        try {
            return this.m00 == matrix4f.m00 && this.m01 == matrix4f.m01 && this.m02 == matrix4f.m02 && this.m03 == matrix4f.m03 && this.m10 == matrix4f.m10 && this.m11 == matrix4f.m11 && this.m12 == matrix4f.m12 && this.m13 == matrix4f.m13 && this.m20 == matrix4f.m20 && this.m21 == matrix4f.m21 && this.m22 == matrix4f.m22 && this.m23 == matrix4f.m23 && this.m30 == matrix4f.m30 && this.m31 == matrix4f.m31 && this.m32 == matrix4f.m32 && this.m33 == matrix4f.m33;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Matrix4f matrix4f = (Matrix4f)o;
            return this.m00 == matrix4f.m00 && this.m01 == matrix4f.m01 && this.m02 == matrix4f.m02 && this.m03 == matrix4f.m03 && this.m10 == matrix4f.m10 && this.m11 == matrix4f.m11 && this.m12 == matrix4f.m12 && this.m13 == matrix4f.m13 && this.m20 == matrix4f.m20 && this.m21 == matrix4f.m21 && this.m22 == matrix4f.m22 && this.m23 == matrix4f.m23 && this.m30 == matrix4f.m30 && this.m31 == matrix4f.m31 && this.m32 == matrix4f.m32 && this.m33 == matrix4f.m33;
        }
        catch (ClassCastException ex) {
            return false;
        }
        catch (NullPointerException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Matrix4f matrix4f, final float n) {
        boolean b = true;
        if (Math.abs(this.m00 - matrix4f.m00) > n) {
            b = false;
        }
        if (Math.abs(this.m01 - matrix4f.m01) > n) {
            b = false;
        }
        if (Math.abs(this.m02 - matrix4f.m02) > n) {
            b = false;
        }
        if (Math.abs(this.m03 - matrix4f.m03) > n) {
            b = false;
        }
        if (Math.abs(this.m10 - matrix4f.m10) > n) {
            b = false;
        }
        if (Math.abs(this.m11 - matrix4f.m11) > n) {
            b = false;
        }
        if (Math.abs(this.m12 - matrix4f.m12) > n) {
            b = false;
        }
        if (Math.abs(this.m13 - matrix4f.m13) > n) {
            b = false;
        }
        if (Math.abs(this.m20 - matrix4f.m20) > n) {
            b = false;
        }
        if (Math.abs(this.m21 - matrix4f.m21) > n) {
            b = false;
        }
        if (Math.abs(this.m22 - matrix4f.m22) > n) {
            b = false;
        }
        if (Math.abs(this.m23 - matrix4f.m23) > n) {
            b = false;
        }
        if (Math.abs(this.m30 - matrix4f.m30) > n) {
            b = false;
        }
        if (Math.abs(this.m31 - matrix4f.m31) > n) {
            b = false;
        }
        if (Math.abs(this.m32 - matrix4f.m32) > n) {
            b = false;
        }
        if (Math.abs(this.m33 - matrix4f.m33) > n) {
            b = false;
        }
        return b;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * 1L + VecMathUtil.floatToIntBits(this.m00)) + VecMathUtil.floatToIntBits(this.m01)) + VecMathUtil.floatToIntBits(this.m02)) + VecMathUtil.floatToIntBits(this.m03)) + VecMathUtil.floatToIntBits(this.m10)) + VecMathUtil.floatToIntBits(this.m11)) + VecMathUtil.floatToIntBits(this.m12)) + VecMathUtil.floatToIntBits(this.m13)) + VecMathUtil.floatToIntBits(this.m20)) + VecMathUtil.floatToIntBits(this.m21)) + VecMathUtil.floatToIntBits(this.m22)) + VecMathUtil.floatToIntBits(this.m23)) + VecMathUtil.floatToIntBits(this.m30)) + VecMathUtil.floatToIntBits(this.m31)) + VecMathUtil.floatToIntBits(this.m32)) + VecMathUtil.floatToIntBits(this.m33);
        return (int)(n ^ n >> 32);
    }
    
    public final void transform(final Tuple4f tuple4f, final Tuple4f tuple4f2) {
        final float x = this.m00 * tuple4f.x + this.m01 * tuple4f.y + this.m02 * tuple4f.z + this.m03 * tuple4f.w;
        final float y = this.m10 * tuple4f.x + this.m11 * tuple4f.y + this.m12 * tuple4f.z + this.m13 * tuple4f.w;
        final float z = this.m20 * tuple4f.x + this.m21 * tuple4f.y + this.m22 * tuple4f.z + this.m23 * tuple4f.w;
        tuple4f2.w = this.m30 * tuple4f.x + this.m31 * tuple4f.y + this.m32 * tuple4f.z + this.m33 * tuple4f.w;
        tuple4f2.x = x;
        tuple4f2.y = y;
        tuple4f2.z = z;
    }
    
    public final void transform(final Tuple4f tuple4f) {
        final float x = this.m00 * tuple4f.x + this.m01 * tuple4f.y + this.m02 * tuple4f.z + this.m03 * tuple4f.w;
        final float y = this.m10 * tuple4f.x + this.m11 * tuple4f.y + this.m12 * tuple4f.z + this.m13 * tuple4f.w;
        final float z = this.m20 * tuple4f.x + this.m21 * tuple4f.y + this.m22 * tuple4f.z + this.m23 * tuple4f.w;
        tuple4f.w = this.m30 * tuple4f.x + this.m31 * tuple4f.y + this.m32 * tuple4f.z + this.m33 * tuple4f.w;
        tuple4f.x = x;
        tuple4f.y = y;
        tuple4f.z = z;
    }
    
    public final void transform(final Point3f point3f, final Point3f point3f2) {
        final float x = this.m00 * point3f.x + this.m01 * point3f.y + this.m02 * point3f.z + this.m03;
        final float y = this.m10 * point3f.x + this.m11 * point3f.y + this.m12 * point3f.z + this.m13;
        point3f2.z = this.m20 * point3f.x + this.m21 * point3f.y + this.m22 * point3f.z + this.m23;
        point3f2.x = x;
        point3f2.y = y;
    }
    
    public final void transform(final Point3f point3f) {
        final float x = this.m00 * point3f.x + this.m01 * point3f.y + this.m02 * point3f.z + this.m03;
        final float y = this.m10 * point3f.x + this.m11 * point3f.y + this.m12 * point3f.z + this.m13;
        point3f.z = this.m20 * point3f.x + this.m21 * point3f.y + this.m22 * point3f.z + this.m23;
        point3f.x = x;
        point3f.y = y;
    }
    
    public final void transform(final Vector3f vector3f, final Vector3f vector3f2) {
        final float x = this.m00 * vector3f.x + this.m01 * vector3f.y + this.m02 * vector3f.z;
        final float y = this.m10 * vector3f.x + this.m11 * vector3f.y + this.m12 * vector3f.z;
        vector3f2.z = this.m20 * vector3f.x + this.m21 * vector3f.y + this.m22 * vector3f.z;
        vector3f2.x = x;
        vector3f2.y = y;
    }
    
    public final void transform(final Vector3f vector3f) {
        final float x = this.m00 * vector3f.x + this.m01 * vector3f.y + this.m02 * vector3f.z;
        final float y = this.m10 * vector3f.x + this.m11 * vector3f.y + this.m12 * vector3f.z;
        vector3f.z = this.m20 * vector3f.x + this.m21 * vector3f.y + this.m22 * vector3f.z;
        vector3f.x = x;
        vector3f.y = y;
    }
    
    public final void setRotation(final Matrix3d matrix3d) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = (float)(matrix3d.m00 * array2[0]);
        this.m01 = (float)(matrix3d.m01 * array2[1]);
        this.m02 = (float)(matrix3d.m02 * array2[2]);
        this.m10 = (float)(matrix3d.m10 * array2[0]);
        this.m11 = (float)(matrix3d.m11 * array2[1]);
        this.m12 = (float)(matrix3d.m12 * array2[2]);
        this.m20 = (float)(matrix3d.m20 * array2[0]);
        this.m21 = (float)(matrix3d.m21 * array2[1]);
        this.m22 = (float)(matrix3d.m22 * array2[2]);
    }
    
    public final void setRotation(final Matrix3f matrix3f) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = (float)(matrix3f.m00 * array2[0]);
        this.m01 = (float)(matrix3f.m01 * array2[1]);
        this.m02 = (float)(matrix3f.m02 * array2[2]);
        this.m10 = (float)(matrix3f.m10 * array2[0]);
        this.m11 = (float)(matrix3f.m11 * array2[1]);
        this.m12 = (float)(matrix3f.m12 * array2[2]);
        this.m20 = (float)(matrix3f.m20 * array2[0]);
        this.m21 = (float)(matrix3f.m21 * array2[1]);
        this.m22 = (float)(matrix3f.m22 * array2[2]);
    }
    
    public final void setRotation(final Quat4f quat4f) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = (float)((1.0f - 2.0f * quat4f.y * quat4f.y - 2.0f * quat4f.z * quat4f.z) * array2[0]);
        this.m10 = (float)(2.0f * (quat4f.x * quat4f.y + quat4f.w * quat4f.z) * array2[0]);
        this.m20 = (float)(2.0f * (quat4f.x * quat4f.z - quat4f.w * quat4f.y) * array2[0]);
        this.m01 = (float)(2.0f * (quat4f.x * quat4f.y - quat4f.w * quat4f.z) * array2[1]);
        this.m11 = (float)((1.0f - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.z * quat4f.z) * array2[1]);
        this.m21 = (float)(2.0f * (quat4f.y * quat4f.z + quat4f.w * quat4f.x) * array2[1]);
        this.m02 = (float)(2.0f * (quat4f.x * quat4f.z + quat4f.w * quat4f.y) * array2[2]);
        this.m12 = (float)(2.0f * (quat4f.y * quat4f.z - quat4f.w * quat4f.x) * array2[2]);
        this.m22 = (float)((1.0f - 2.0f * quat4f.x * quat4f.x - 2.0f * quat4f.y * quat4f.y) * array2[2]);
    }
    
    public final void setRotation(final Quat4d quat4d) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        this.m00 = (float)((1.0 - 2.0 * quat4d.y * quat4d.y - 2.0 * quat4d.z * quat4d.z) * array2[0]);
        this.m10 = (float)(2.0 * (quat4d.x * quat4d.y + quat4d.w * quat4d.z) * array2[0]);
        this.m20 = (float)(2.0 * (quat4d.x * quat4d.z - quat4d.w * quat4d.y) * array2[0]);
        this.m01 = (float)(2.0 * (quat4d.x * quat4d.y - quat4d.w * quat4d.z) * array2[1]);
        this.m11 = (float)((1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.z * quat4d.z) * array2[1]);
        this.m21 = (float)(2.0 * (quat4d.y * quat4d.z + quat4d.w * quat4d.x) * array2[1]);
        this.m02 = (float)(2.0 * (quat4d.x * quat4d.z + quat4d.w * quat4d.y) * array2[2]);
        this.m12 = (float)(2.0 * (quat4d.y * quat4d.z - quat4d.w * quat4d.x) * array2[2]);
        this.m22 = (float)((1.0 - 2.0 * quat4d.x * quat4d.x - 2.0 * quat4d.y * quat4d.y) * array2[2]);
    }
    
    public final void setRotation(final AxisAngle4f axisAngle4f) {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        final double sqrt = Math.sqrt(axisAngle4f.x * axisAngle4f.x + axisAngle4f.y * axisAngle4f.y + axisAngle4f.z * axisAngle4f.z);
        if (sqrt < 1.0E-8) {
            this.m00 = 1.0f;
            this.m01 = 0.0f;
            this.m02 = 0.0f;
            this.m10 = 0.0f;
            this.m11 = 1.0f;
            this.m12 = 0.0f;
            this.m20 = 0.0f;
            this.m21 = 0.0f;
            this.m22 = 1.0f;
        }
        else {
            final double n = 1.0 / sqrt;
            final double n2 = axisAngle4f.x * n;
            final double n3 = axisAngle4f.y * n;
            final double n4 = axisAngle4f.z * n;
            final double sin = Math.sin(axisAngle4f.angle);
            final double cos = Math.cos(axisAngle4f.angle);
            final double n5 = 1.0 - cos;
            final double n6 = axisAngle4f.x * axisAngle4f.z;
            final double n7 = axisAngle4f.x * axisAngle4f.y;
            final double n8 = axisAngle4f.y * axisAngle4f.z;
            this.m00 = (float)((n5 * n2 * n2 + cos) * array2[0]);
            this.m01 = (float)((n5 * n7 - sin * n4) * array2[1]);
            this.m02 = (float)((n5 * n6 + sin * n3) * array2[2]);
            this.m10 = (float)((n5 * n7 + sin * n4) * array2[0]);
            this.m11 = (float)((n5 * n3 * n3 + cos) * array2[1]);
            this.m12 = (float)((n5 * n8 - sin * n2) * array2[2]);
            this.m20 = (float)((n5 * n6 - sin * n3) * array2[0]);
            this.m21 = (float)((n5 * n8 + sin * n2) * array2[1]);
            this.m22 = (float)((n5 * n4 * n4 + cos) * array2[2]);
        }
    }
    
    public final void setZero() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 0.0f;
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
    
    public final void negate(final Matrix4f matrix4f) {
        this.m00 = -matrix4f.m00;
        this.m01 = -matrix4f.m01;
        this.m02 = -matrix4f.m02;
        this.m03 = -matrix4f.m03;
        this.m10 = -matrix4f.m10;
        this.m11 = -matrix4f.m11;
        this.m12 = -matrix4f.m12;
        this.m13 = -matrix4f.m13;
        this.m20 = -matrix4f.m20;
        this.m21 = -matrix4f.m21;
        this.m22 = -matrix4f.m22;
        this.m23 = -matrix4f.m23;
        this.m30 = -matrix4f.m30;
        this.m31 = -matrix4f.m31;
        this.m32 = -matrix4f.m32;
        this.m33 = -matrix4f.m33;
    }
    
    private final void getScaleRotate(final double[] array, final double[] array2) {
        Matrix3d.compute_svd(new double[] { this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22 }, array, array2);
    }
    
    public Object clone() {
        Matrix4f matrix4f;
        try {
            matrix4f = (Matrix4f)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        return matrix4f;
    }
}
