// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Matrix3f implements Serializable, Cloneable
{
    static final long serialVersionUID = 329697160112089834L;
    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
    public float m20;
    public float m21;
    public float m22;
    private static final double EPS = 1.0E-8;
    
    public Matrix3f(final float m00, final float m2, final float m3, final float m4, final float m5, final float m6, final float m7, final float m8, final float m9) {
        this.m00 = m00;
        this.m01 = m2;
        this.m02 = m3;
        this.m10 = m4;
        this.m11 = m5;
        this.m12 = m6;
        this.m20 = m7;
        this.m21 = m8;
        this.m22 = m9;
    }
    
    public Matrix3f(final float[] array) {
        this.m00 = array[0];
        this.m01 = array[1];
        this.m02 = array[2];
        this.m10 = array[3];
        this.m11 = array[4];
        this.m12 = array[5];
        this.m20 = array[6];
        this.m21 = array[7];
        this.m22 = array[8];
    }
    
    public Matrix3f(final Matrix3d matrix3d) {
        this.m00 = (float)matrix3d.m00;
        this.m01 = (float)matrix3d.m01;
        this.m02 = (float)matrix3d.m02;
        this.m10 = (float)matrix3d.m10;
        this.m11 = (float)matrix3d.m11;
        this.m12 = (float)matrix3d.m12;
        this.m20 = (float)matrix3d.m20;
        this.m21 = (float)matrix3d.m21;
        this.m22 = (float)matrix3d.m22;
    }
    
    public Matrix3f(final Matrix3f matrix3f) {
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
    
    public Matrix3f() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
    }
    
    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
    }
    
    public final void setIdentity() {
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
    
    public final void setElement(final int n, final int n2, final float m22) {
        Label_0234: {
            switch (n) {
                case 0: {
                    switch (n2) {
                        case 0: {
                            this.m00 = m22;
                            break Label_0234;
                        }
                        case 1: {
                            this.m01 = m22;
                            break Label_0234;
                        }
                        case 2: {
                            this.m02 = m22;
                            break Label_0234;
                        }
                        default: {
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
                        }
                    }
                    break;
                }
                case 1: {
                    switch (n2) {
                        case 0: {
                            this.m10 = m22;
                            break Label_0234;
                        }
                        case 1: {
                            this.m11 = m22;
                            break Label_0234;
                        }
                        case 2: {
                            this.m12 = m22;
                            break Label_0234;
                        }
                        default: {
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
                        }
                    }
                    break;
                }
                case 2: {
                    switch (n2) {
                        case 0: {
                            this.m20 = m22;
                            break Label_0234;
                        }
                        case 1: {
                            this.m21 = m22;
                            break Label_0234;
                        }
                        case 2: {
                            this.m22 = m22;
                            break Label_0234;
                        }
                        default: {
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
                        }
                    }
                    break;
                }
                default: {
                    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f0"));
                }
            }
        }
    }
    
    public final void getRow(final int n, final Vector3f vector3f) {
        if (n == 0) {
            vector3f.x = this.m00;
            vector3f.y = this.m01;
            vector3f.z = this.m02;
        }
        else if (n == 1) {
            vector3f.x = this.m10;
            vector3f.y = this.m11;
            vector3f.z = this.m12;
        }
        else {
            if (n != 2) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f1"));
            }
            vector3f.x = this.m20;
            vector3f.y = this.m21;
            vector3f.z = this.m22;
        }
    }
    
    public final void getRow(final int n, final float[] array) {
        if (n == 0) {
            array[0] = this.m00;
            array[1] = this.m01;
            array[2] = this.m02;
        }
        else if (n == 1) {
            array[0] = this.m10;
            array[1] = this.m11;
            array[2] = this.m12;
        }
        else {
            if (n != 2) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f1"));
            }
            array[0] = this.m20;
            array[1] = this.m21;
            array[2] = this.m22;
        }
    }
    
    public final void getColumn(final int n, final Vector3f vector3f) {
        if (n == 0) {
            vector3f.x = this.m00;
            vector3f.y = this.m10;
            vector3f.z = this.m20;
        }
        else if (n == 1) {
            vector3f.x = this.m01;
            vector3f.y = this.m11;
            vector3f.z = this.m21;
        }
        else {
            if (n != 2) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f3"));
            }
            vector3f.x = this.m02;
            vector3f.y = this.m12;
            vector3f.z = this.m22;
        }
    }
    
    public final void getColumn(final int n, final float[] array) {
        if (n == 0) {
            array[0] = this.m00;
            array[1] = this.m10;
            array[2] = this.m20;
        }
        else if (n == 1) {
            array[0] = this.m01;
            array[1] = this.m11;
            array[2] = this.m21;
        }
        else {
            if (n != 2) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f3"));
            }
            array[0] = this.m02;
            array[1] = this.m12;
            array[2] = this.m22;
        }
    }
    
    public final float getElement(final int n, final int n2) {
        Label_0162: {
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
                        default: {
                            break Label_0162;
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
                        default: {
                            break Label_0162;
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
                        default: {
                            break Label_0162;
                        }
                    }
                    break;
                }
            }
        }
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f5"));
    }
    
    public final void setRow(final int n, final float m20, final float m21, final float m22) {
        switch (n) {
            case 0: {
                this.m00 = m20;
                this.m01 = m21;
                this.m02 = m22;
                break;
            }
            case 1: {
                this.m10 = m20;
                this.m11 = m21;
                this.m12 = m22;
                break;
            }
            case 2: {
                this.m20 = m20;
                this.m21 = m21;
                this.m22 = m22;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
            }
        }
    }
    
    public final void setRow(final int n, final Vector3f vector3f) {
        switch (n) {
            case 0: {
                this.m00 = vector3f.x;
                this.m01 = vector3f.y;
                this.m02 = vector3f.z;
                break;
            }
            case 1: {
                this.m10 = vector3f.x;
                this.m11 = vector3f.y;
                this.m12 = vector3f.z;
                break;
            }
            case 2: {
                this.m20 = vector3f.x;
                this.m21 = vector3f.y;
                this.m22 = vector3f.z;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
            }
        }
    }
    
    public final void setRow(final int n, final float[] array) {
        switch (n) {
            case 0: {
                this.m00 = array[0];
                this.m01 = array[1];
                this.m02 = array[2];
                break;
            }
            case 1: {
                this.m10 = array[0];
                this.m11 = array[1];
                this.m12 = array[2];
                break;
            }
            case 2: {
                this.m20 = array[0];
                this.m21 = array[1];
                this.m22 = array[2];
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f6"));
            }
        }
    }
    
    public final void setColumn(final int n, final float m02, final float m3, final float m4) {
        switch (n) {
            case 0: {
                this.m00 = m02;
                this.m10 = m3;
                this.m20 = m4;
                break;
            }
            case 1: {
                this.m01 = m02;
                this.m11 = m3;
                this.m21 = m4;
                break;
            }
            case 2: {
                this.m02 = m02;
                this.m12 = m3;
                this.m22 = m4;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
            }
        }
    }
    
    public final void setColumn(final int n, final Vector3f vector3f) {
        switch (n) {
            case 0: {
                this.m00 = vector3f.x;
                this.m10 = vector3f.y;
                this.m20 = vector3f.z;
                break;
            }
            case 1: {
                this.m01 = vector3f.x;
                this.m11 = vector3f.y;
                this.m21 = vector3f.z;
                break;
            }
            case 2: {
                this.m02 = vector3f.x;
                this.m12 = vector3f.y;
                this.m22 = vector3f.z;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
            }
        }
    }
    
    public final void setColumn(final int n, final float[] array) {
        switch (n) {
            case 0: {
                this.m00 = array[0];
                this.m10 = array[1];
                this.m20 = array[2];
                break;
            }
            case 1: {
                this.m01 = array[0];
                this.m11 = array[1];
                this.m21 = array[2];
                break;
            }
            case 2: {
                this.m02 = array[0];
                this.m12 = array[1];
                this.m22 = array[2];
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3f9"));
            }
        }
    }
    
    public final float getScale() {
        final double[] array = new double[9];
        final double[] array2 = new double[3];
        this.getScaleRotate(array2, array);
        return (float)Matrix3d.max3(array2);
    }
    
    public final void add(final float n) {
        this.m00 += n;
        this.m01 += n;
        this.m02 += n;
        this.m10 += n;
        this.m11 += n;
        this.m12 += n;
        this.m20 += n;
        this.m21 += n;
        this.m22 += n;
    }
    
    public final void add(final float n, final Matrix3f matrix3f) {
        this.m00 = matrix3f.m00 + n;
        this.m01 = matrix3f.m01 + n;
        this.m02 = matrix3f.m02 + n;
        this.m10 = matrix3f.m10 + n;
        this.m11 = matrix3f.m11 + n;
        this.m12 = matrix3f.m12 + n;
        this.m20 = matrix3f.m20 + n;
        this.m21 = matrix3f.m21 + n;
        this.m22 = matrix3f.m22 + n;
    }
    
    public final void add(final Matrix3f matrix3f, final Matrix3f matrix3f2) {
        this.m00 = matrix3f.m00 + matrix3f2.m00;
        this.m01 = matrix3f.m01 + matrix3f2.m01;
        this.m02 = matrix3f.m02 + matrix3f2.m02;
        this.m10 = matrix3f.m10 + matrix3f2.m10;
        this.m11 = matrix3f.m11 + matrix3f2.m11;
        this.m12 = matrix3f.m12 + matrix3f2.m12;
        this.m20 = matrix3f.m20 + matrix3f2.m20;
        this.m21 = matrix3f.m21 + matrix3f2.m21;
        this.m22 = matrix3f.m22 + matrix3f2.m22;
    }
    
    public final void add(final Matrix3f matrix3f) {
        this.m00 += matrix3f.m00;
        this.m01 += matrix3f.m01;
        this.m02 += matrix3f.m02;
        this.m10 += matrix3f.m10;
        this.m11 += matrix3f.m11;
        this.m12 += matrix3f.m12;
        this.m20 += matrix3f.m20;
        this.m21 += matrix3f.m21;
        this.m22 += matrix3f.m22;
    }
    
    public final void sub(final Matrix3f matrix3f, final Matrix3f matrix3f2) {
        this.m00 = matrix3f.m00 - matrix3f2.m00;
        this.m01 = matrix3f.m01 - matrix3f2.m01;
        this.m02 = matrix3f.m02 - matrix3f2.m02;
        this.m10 = matrix3f.m10 - matrix3f2.m10;
        this.m11 = matrix3f.m11 - matrix3f2.m11;
        this.m12 = matrix3f.m12 - matrix3f2.m12;
        this.m20 = matrix3f.m20 - matrix3f2.m20;
        this.m21 = matrix3f.m21 - matrix3f2.m21;
        this.m22 = matrix3f.m22 - matrix3f2.m22;
    }
    
    public final void sub(final Matrix3f matrix3f) {
        this.m00 -= matrix3f.m00;
        this.m01 -= matrix3f.m01;
        this.m02 -= matrix3f.m02;
        this.m10 -= matrix3f.m10;
        this.m11 -= matrix3f.m11;
        this.m12 -= matrix3f.m12;
        this.m20 -= matrix3f.m20;
        this.m21 -= matrix3f.m21;
        this.m22 -= matrix3f.m22;
    }
    
    public final void transpose() {
        final float m10 = this.m10;
        this.m10 = this.m01;
        this.m01 = m10;
        final float m11 = this.m20;
        this.m20 = this.m02;
        this.m02 = m11;
        final float m12 = this.m21;
        this.m21 = this.m12;
        this.m12 = m12;
    }
    
    public final void transpose(final Matrix3f matrix3f) {
        if (this != matrix3f) {
            this.m00 = matrix3f.m00;
            this.m01 = matrix3f.m10;
            this.m02 = matrix3f.m20;
            this.m10 = matrix3f.m01;
            this.m11 = matrix3f.m11;
            this.m12 = matrix3f.m21;
            this.m20 = matrix3f.m02;
            this.m21 = matrix3f.m12;
            this.m22 = matrix3f.m22;
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
            final double sin = Math.sin(axisAngle4d.angle);
            final double cos = Math.cos(axisAngle4d.angle);
            final double n5 = 1.0 - cos;
            final double n6 = n2 * n4;
            final double n7 = n2 * n3;
            final double n8 = n3 * n4;
            this.m00 = (float)(n5 * n2 * n2 + cos);
            this.m01 = (float)(n5 * n7 - sin * n4);
            this.m02 = (float)(n5 * n6 + sin * n3);
            this.m10 = (float)(n5 * n7 + sin * n4);
            this.m11 = (float)(n5 * n3 * n3 + cos);
            this.m12 = (float)(n5 * n8 - sin * n2);
            this.m20 = (float)(n5 * n6 - sin * n3);
            this.m21 = (float)(n5 * n8 + sin * n2);
            this.m22 = (float)(n5 * n4 * n4 + cos);
        }
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
    }
    
    public final void set(final float[] array) {
        this.m00 = array[0];
        this.m01 = array[1];
        this.m02 = array[2];
        this.m10 = array[3];
        this.m11 = array[4];
        this.m12 = array[5];
        this.m20 = array[6];
        this.m21 = array[7];
        this.m22 = array[8];
    }
    
    public final void set(final Matrix3f matrix3f) {
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
    
    public final void set(final Matrix3d matrix3d) {
        this.m00 = (float)matrix3d.m00;
        this.m01 = (float)matrix3d.m01;
        this.m02 = (float)matrix3d.m02;
        this.m10 = (float)matrix3d.m10;
        this.m11 = (float)matrix3d.m11;
        this.m12 = (float)matrix3d.m12;
        this.m20 = (float)matrix3d.m20;
        this.m21 = (float)matrix3d.m21;
        this.m22 = (float)matrix3d.m22;
    }
    
    public final void invert(final Matrix3f matrix3f) {
        this.invertGeneral(matrix3f);
    }
    
    public final void invert() {
        this.invertGeneral(this);
    }
    
    private final void invertGeneral(final Matrix3f matrix3f) {
        final double[] array = new double[9];
        final double[] array2 = new double[9];
        final int[] array3 = new int[3];
        array[0] = matrix3f.m00;
        array[1] = matrix3f.m01;
        array[2] = matrix3f.m02;
        array[3] = matrix3f.m10;
        array[4] = matrix3f.m11;
        array[5] = matrix3f.m12;
        array[6] = matrix3f.m20;
        array[7] = matrix3f.m21;
        array[8] = matrix3f.m22;
        if (!luDecomposition(array, array3)) {
            throw new SingularMatrixException(VecMathI18N.getString("Matrix3f12"));
        }
        for (int i = 0; i < 9; ++i) {
            array2[i] = 0.0;
        }
        array2[0] = 1.0;
        array2[8] = (array2[4] = 1.0);
        luBacksubstitution(array, array3, array2);
        this.m00 = (float)array2[0];
        this.m01 = (float)array2[1];
        this.m02 = (float)array2[2];
        this.m10 = (float)array2[3];
        this.m11 = (float)array2[4];
        this.m12 = (float)array2[5];
        this.m20 = (float)array2[6];
        this.m21 = (float)array2[7];
        this.m22 = (float)array2[8];
    }
    
    static boolean luDecomposition(final double[] array, final int[] array2) {
        final double[] array3 = new double[3];
        int n = 0;
        int n2 = 0;
        int n3 = 3;
        while (n3-- != 0) {
            double n4 = 0.0;
            int n5 = 3;
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
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < i; ++j) {
                final int n7 = n6 + 3 * j + i;
                double n8 = array[n7];
                int n9 = j;
                int n10 = n6 + 3 * j;
                int n11 = n6 + i;
                while (n9-- != 0) {
                    n8 -= array[n10] * array[n11];
                    ++n10;
                    n11 += 3;
                }
                array[n7] = n8;
            }
            double n12 = 0.0;
            int n13 = -1;
            for (int k = i; k < 3; ++k) {
                final int n14 = n6 + 3 * k + i;
                double a = array[n14];
                int n15 = i;
                int n16 = n6 + 3 * k;
                int n17 = n6 + i;
                while (n15-- != 0) {
                    a -= array[n16] * array[n17];
                    ++n16;
                    n17 += 3;
                }
                array[n14] = a;
                final double n18;
                if ((n18 = array3[k] * Math.abs(a)) >= n12) {
                    n12 = n18;
                    n13 = k;
                }
            }
            if (n13 < 0) {
                throw new RuntimeException(VecMathI18N.getString("Matrix3f13"));
            }
            if (i != n13) {
                int n19 = 3;
                int n20 = n6 + 3 * n13;
                int n21 = n6 + 3 * i;
                while (n19-- != 0) {
                    final double n22 = array[n20];
                    array[n20++] = array[n21];
                    array[n21++] = n22;
                }
                array3[n13] = array3[i];
            }
            array2[i] = n13;
            if (array[n6 + 3 * i + i] == 0.0) {
                return false;
            }
            if (i != 2) {
                final double n23 = 1.0 / array[n6 + 3 * i + i];
                int n24 = n6 + 3 * (i + 1) + i;
                int n25 = 2 - i;
                while (n25-- != 0) {
                    final int n26 = n24;
                    array[n26] *= n23;
                    n24 += 3;
                }
            }
        }
        return true;
    }
    
    static void luBacksubstitution(final double[] array, final int[] array2, final double[] array3) {
        final int n = 0;
        for (int i = 0; i < 3; ++i) {
            final int n2 = i;
            int n3 = -1;
            for (int j = 0; j < 3; ++j) {
                final int n4 = array2[n + j];
                double n5 = array3[n2 + 3 * n4];
                array3[n2 + 3 * n4] = array3[n2 + 3 * j];
                if (n3 >= 0) {
                    final int n6 = j * 3;
                    for (int k = n3; k <= j - 1; ++k) {
                        n5 -= array[n6 + k] * array3[n2 + 3 * k];
                    }
                }
                else if (n5 != 0.0) {
                    n3 = j;
                }
                array3[n2 + 3 * j] = n5;
            }
            int n7 = 6;
            final int n8 = n2 + 6;
            array3[n8] /= array[n7 + 2];
            n7 -= 3;
            array3[n2 + 3] = (array3[n2 + 3] - array[n7 + 2] * array3[n2 + 6]) / array[n7 + 1];
            n7 -= 3;
            array3[n2 + 0] = (array3[n2 + 0] - array[n7 + 1] * array3[n2 + 3] - array[n7 + 2] * array3[n2 + 6]) / array[n7 + 0];
        }
    }
    
    public final float determinant() {
        return this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
    }
    
    public final void set(final float m22) {
        this.m00 = m22;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = m22;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = m22;
    }
    
    public final void rotX(final float n) {
        final float m21 = (float)Math.sin(n);
        final float n2 = (float)Math.cos(n);
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = n2;
        this.m12 = -m21;
        this.m20 = 0.0f;
        this.m21 = m21;
        this.m22 = n2;
    }
    
    public final void rotY(final float n) {
        final float m02 = (float)Math.sin(n);
        final float n2 = (float)Math.cos(n);
        this.m00 = n2;
        this.m01 = 0.0f;
        this.m02 = m02;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m20 = -m02;
        this.m21 = 0.0f;
        this.m22 = n2;
    }
    
    public final void rotZ(final float n) {
        final float m10 = (float)Math.sin(n);
        final float n2 = (float)Math.cos(n);
        this.m00 = n2;
        this.m01 = -m10;
        this.m02 = 0.0f;
        this.m10 = m10;
        this.m11 = n2;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
    }
    
    public final void mul(final float n) {
        this.m00 *= n;
        this.m01 *= n;
        this.m02 *= n;
        this.m10 *= n;
        this.m11 *= n;
        this.m12 *= n;
        this.m20 *= n;
        this.m21 *= n;
        this.m22 *= n;
    }
    
    public final void mul(final float n, final Matrix3f matrix3f) {
        this.m00 = n * matrix3f.m00;
        this.m01 = n * matrix3f.m01;
        this.m02 = n * matrix3f.m02;
        this.m10 = n * matrix3f.m10;
        this.m11 = n * matrix3f.m11;
        this.m12 = n * matrix3f.m12;
        this.m20 = n * matrix3f.m20;
        this.m21 = n * matrix3f.m21;
        this.m22 = n * matrix3f.m22;
    }
    
    public final void mul(final Matrix3f matrix3f) {
        final float m00 = this.m00 * matrix3f.m00 + this.m01 * matrix3f.m10 + this.m02 * matrix3f.m20;
        final float m2 = this.m00 * matrix3f.m01 + this.m01 * matrix3f.m11 + this.m02 * matrix3f.m21;
        final float m3 = this.m00 * matrix3f.m02 + this.m01 * matrix3f.m12 + this.m02 * matrix3f.m22;
        final float m4 = this.m10 * matrix3f.m00 + this.m11 * matrix3f.m10 + this.m12 * matrix3f.m20;
        final float m5 = this.m10 * matrix3f.m01 + this.m11 * matrix3f.m11 + this.m12 * matrix3f.m21;
        final float m6 = this.m10 * matrix3f.m02 + this.m11 * matrix3f.m12 + this.m12 * matrix3f.m22;
        final float m7 = this.m20 * matrix3f.m00 + this.m21 * matrix3f.m10 + this.m22 * matrix3f.m20;
        final float m8 = this.m20 * matrix3f.m01 + this.m21 * matrix3f.m11 + this.m22 * matrix3f.m21;
        final float m9 = this.m20 * matrix3f.m02 + this.m21 * matrix3f.m12 + this.m22 * matrix3f.m22;
        this.m00 = m00;
        this.m01 = m2;
        this.m02 = m3;
        this.m10 = m4;
        this.m11 = m5;
        this.m12 = m6;
        this.m20 = m7;
        this.m21 = m8;
        this.m22 = m9;
    }
    
    public final void mul(final Matrix3f matrix3f, final Matrix3f matrix3f2) {
        if (this != matrix3f && this != matrix3f2) {
            this.m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m01 * matrix3f2.m10 + matrix3f.m02 * matrix3f2.m20;
            this.m01 = matrix3f.m00 * matrix3f2.m01 + matrix3f.m01 * matrix3f2.m11 + matrix3f.m02 * matrix3f2.m21;
            this.m02 = matrix3f.m00 * matrix3f2.m02 + matrix3f.m01 * matrix3f2.m12 + matrix3f.m02 * matrix3f2.m22;
            this.m10 = matrix3f.m10 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m10 + matrix3f.m12 * matrix3f2.m20;
            this.m11 = matrix3f.m10 * matrix3f2.m01 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m12 * matrix3f2.m21;
            this.m12 = matrix3f.m10 * matrix3f2.m02 + matrix3f.m11 * matrix3f2.m12 + matrix3f.m12 * matrix3f2.m22;
            this.m20 = matrix3f.m20 * matrix3f2.m00 + matrix3f.m21 * matrix3f2.m10 + matrix3f.m22 * matrix3f2.m20;
            this.m21 = matrix3f.m20 * matrix3f2.m01 + matrix3f.m21 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m21;
            this.m22 = matrix3f.m20 * matrix3f2.m02 + matrix3f.m21 * matrix3f2.m12 + matrix3f.m22 * matrix3f2.m22;
        }
        else {
            final float m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m01 * matrix3f2.m10 + matrix3f.m02 * matrix3f2.m20;
            final float m2 = matrix3f.m00 * matrix3f2.m01 + matrix3f.m01 * matrix3f2.m11 + matrix3f.m02 * matrix3f2.m21;
            final float m3 = matrix3f.m00 * matrix3f2.m02 + matrix3f.m01 * matrix3f2.m12 + matrix3f.m02 * matrix3f2.m22;
            final float m4 = matrix3f.m10 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m10 + matrix3f.m12 * matrix3f2.m20;
            final float m5 = matrix3f.m10 * matrix3f2.m01 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m12 * matrix3f2.m21;
            final float m6 = matrix3f.m10 * matrix3f2.m02 + matrix3f.m11 * matrix3f2.m12 + matrix3f.m12 * matrix3f2.m22;
            final float m7 = matrix3f.m20 * matrix3f2.m00 + matrix3f.m21 * matrix3f2.m10 + matrix3f.m22 * matrix3f2.m20;
            final float m8 = matrix3f.m20 * matrix3f2.m01 + matrix3f.m21 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m21;
            final float m9 = matrix3f.m20 * matrix3f2.m02 + matrix3f.m21 * matrix3f2.m12 + matrix3f.m22 * matrix3f2.m22;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m10 = m4;
            this.m11 = m5;
            this.m12 = m6;
            this.m20 = m7;
            this.m21 = m8;
            this.m22 = m9;
        }
    }
    
    public final void mulNormalize(final Matrix3f matrix3f) {
        final double[] array = new double[9];
        final double[] array2 = new double[9];
        final double[] array3 = new double[3];
        array[0] = this.m00 * matrix3f.m00 + this.m01 * matrix3f.m10 + this.m02 * matrix3f.m20;
        array[1] = this.m00 * matrix3f.m01 + this.m01 * matrix3f.m11 + this.m02 * matrix3f.m21;
        array[2] = this.m00 * matrix3f.m02 + this.m01 * matrix3f.m12 + this.m02 * matrix3f.m22;
        array[3] = this.m10 * matrix3f.m00 + this.m11 * matrix3f.m10 + this.m12 * matrix3f.m20;
        array[4] = this.m10 * matrix3f.m01 + this.m11 * matrix3f.m11 + this.m12 * matrix3f.m21;
        array[5] = this.m10 * matrix3f.m02 + this.m11 * matrix3f.m12 + this.m12 * matrix3f.m22;
        array[6] = this.m20 * matrix3f.m00 + this.m21 * matrix3f.m10 + this.m22 * matrix3f.m20;
        array[7] = this.m20 * matrix3f.m01 + this.m21 * matrix3f.m11 + this.m22 * matrix3f.m21;
        array[8] = this.m20 * matrix3f.m02 + this.m21 * matrix3f.m12 + this.m22 * matrix3f.m22;
        Matrix3d.compute_svd(array, array3, array2);
        this.m00 = (float)array2[0];
        this.m01 = (float)array2[1];
        this.m02 = (float)array2[2];
        this.m10 = (float)array2[3];
        this.m11 = (float)array2[4];
        this.m12 = (float)array2[5];
        this.m20 = (float)array2[6];
        this.m21 = (float)array2[7];
        this.m22 = (float)array2[8];
    }
    
    public final void mulNormalize(final Matrix3f matrix3f, final Matrix3f matrix3f2) {
        final double[] array = new double[9];
        final double[] array2 = new double[9];
        final double[] array3 = new double[3];
        array[0] = matrix3f.m00 * matrix3f2.m00 + matrix3f.m01 * matrix3f2.m10 + matrix3f.m02 * matrix3f2.m20;
        array[1] = matrix3f.m00 * matrix3f2.m01 + matrix3f.m01 * matrix3f2.m11 + matrix3f.m02 * matrix3f2.m21;
        array[2] = matrix3f.m00 * matrix3f2.m02 + matrix3f.m01 * matrix3f2.m12 + matrix3f.m02 * matrix3f2.m22;
        array[3] = matrix3f.m10 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m10 + matrix3f.m12 * matrix3f2.m20;
        array[4] = matrix3f.m10 * matrix3f2.m01 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m12 * matrix3f2.m21;
        array[5] = matrix3f.m10 * matrix3f2.m02 + matrix3f.m11 * matrix3f2.m12 + matrix3f.m12 * matrix3f2.m22;
        array[6] = matrix3f.m20 * matrix3f2.m00 + matrix3f.m21 * matrix3f2.m10 + matrix3f.m22 * matrix3f2.m20;
        array[7] = matrix3f.m20 * matrix3f2.m01 + matrix3f.m21 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m21;
        array[8] = matrix3f.m20 * matrix3f2.m02 + matrix3f.m21 * matrix3f2.m12 + matrix3f.m22 * matrix3f2.m22;
        Matrix3d.compute_svd(array, array3, array2);
        this.m00 = (float)array2[0];
        this.m01 = (float)array2[1];
        this.m02 = (float)array2[2];
        this.m10 = (float)array2[3];
        this.m11 = (float)array2[4];
        this.m12 = (float)array2[5];
        this.m20 = (float)array2[6];
        this.m21 = (float)array2[7];
        this.m22 = (float)array2[8];
    }
    
    public final void mulTransposeBoth(final Matrix3f matrix3f, final Matrix3f matrix3f2) {
        if (this != matrix3f && this != matrix3f2) {
            this.m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m10 * matrix3f2.m01 + matrix3f.m20 * matrix3f2.m02;
            this.m01 = matrix3f.m00 * matrix3f2.m10 + matrix3f.m10 * matrix3f2.m11 + matrix3f.m20 * matrix3f2.m12;
            this.m02 = matrix3f.m00 * matrix3f2.m20 + matrix3f.m10 * matrix3f2.m21 + matrix3f.m20 * matrix3f2.m22;
            this.m10 = matrix3f.m01 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m01 + matrix3f.m21 * matrix3f2.m02;
            this.m11 = matrix3f.m01 * matrix3f2.m10 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m21 * matrix3f2.m12;
            this.m12 = matrix3f.m01 * matrix3f2.m20 + matrix3f.m11 * matrix3f2.m21 + matrix3f.m21 * matrix3f2.m22;
            this.m20 = matrix3f.m02 * matrix3f2.m00 + matrix3f.m12 * matrix3f2.m01 + matrix3f.m22 * matrix3f2.m02;
            this.m21 = matrix3f.m02 * matrix3f2.m10 + matrix3f.m12 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m12;
            this.m22 = matrix3f.m02 * matrix3f2.m20 + matrix3f.m12 * matrix3f2.m21 + matrix3f.m22 * matrix3f2.m22;
        }
        else {
            final float m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m10 * matrix3f2.m01 + matrix3f.m20 * matrix3f2.m02;
            final float m2 = matrix3f.m00 * matrix3f2.m10 + matrix3f.m10 * matrix3f2.m11 + matrix3f.m20 * matrix3f2.m12;
            final float m3 = matrix3f.m00 * matrix3f2.m20 + matrix3f.m10 * matrix3f2.m21 + matrix3f.m20 * matrix3f2.m22;
            final float m4 = matrix3f.m01 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m01 + matrix3f.m21 * matrix3f2.m02;
            final float m5 = matrix3f.m01 * matrix3f2.m10 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m21 * matrix3f2.m12;
            final float m6 = matrix3f.m01 * matrix3f2.m20 + matrix3f.m11 * matrix3f2.m21 + matrix3f.m21 * matrix3f2.m22;
            final float m7 = matrix3f.m02 * matrix3f2.m00 + matrix3f.m12 * matrix3f2.m01 + matrix3f.m22 * matrix3f2.m02;
            final float m8 = matrix3f.m02 * matrix3f2.m10 + matrix3f.m12 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m12;
            final float m9 = matrix3f.m02 * matrix3f2.m20 + matrix3f.m12 * matrix3f2.m21 + matrix3f.m22 * matrix3f2.m22;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m10 = m4;
            this.m11 = m5;
            this.m12 = m6;
            this.m20 = m7;
            this.m21 = m8;
            this.m22 = m9;
        }
    }
    
    public final void mulTransposeRight(final Matrix3f matrix3f, final Matrix3f matrix3f2) {
        if (this != matrix3f && this != matrix3f2) {
            this.m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m01 * matrix3f2.m01 + matrix3f.m02 * matrix3f2.m02;
            this.m01 = matrix3f.m00 * matrix3f2.m10 + matrix3f.m01 * matrix3f2.m11 + matrix3f.m02 * matrix3f2.m12;
            this.m02 = matrix3f.m00 * matrix3f2.m20 + matrix3f.m01 * matrix3f2.m21 + matrix3f.m02 * matrix3f2.m22;
            this.m10 = matrix3f.m10 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m01 + matrix3f.m12 * matrix3f2.m02;
            this.m11 = matrix3f.m10 * matrix3f2.m10 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m12 * matrix3f2.m12;
            this.m12 = matrix3f.m10 * matrix3f2.m20 + matrix3f.m11 * matrix3f2.m21 + matrix3f.m12 * matrix3f2.m22;
            this.m20 = matrix3f.m20 * matrix3f2.m00 + matrix3f.m21 * matrix3f2.m01 + matrix3f.m22 * matrix3f2.m02;
            this.m21 = matrix3f.m20 * matrix3f2.m10 + matrix3f.m21 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m12;
            this.m22 = matrix3f.m20 * matrix3f2.m20 + matrix3f.m21 * matrix3f2.m21 + matrix3f.m22 * matrix3f2.m22;
        }
        else {
            final float m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m01 * matrix3f2.m01 + matrix3f.m02 * matrix3f2.m02;
            final float m2 = matrix3f.m00 * matrix3f2.m10 + matrix3f.m01 * matrix3f2.m11 + matrix3f.m02 * matrix3f2.m12;
            final float m3 = matrix3f.m00 * matrix3f2.m20 + matrix3f.m01 * matrix3f2.m21 + matrix3f.m02 * matrix3f2.m22;
            final float m4 = matrix3f.m10 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m01 + matrix3f.m12 * matrix3f2.m02;
            final float m5 = matrix3f.m10 * matrix3f2.m10 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m12 * matrix3f2.m12;
            final float m6 = matrix3f.m10 * matrix3f2.m20 + matrix3f.m11 * matrix3f2.m21 + matrix3f.m12 * matrix3f2.m22;
            final float m7 = matrix3f.m20 * matrix3f2.m00 + matrix3f.m21 * matrix3f2.m01 + matrix3f.m22 * matrix3f2.m02;
            final float m8 = matrix3f.m20 * matrix3f2.m10 + matrix3f.m21 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m12;
            final float m9 = matrix3f.m20 * matrix3f2.m20 + matrix3f.m21 * matrix3f2.m21 + matrix3f.m22 * matrix3f2.m22;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m10 = m4;
            this.m11 = m5;
            this.m12 = m6;
            this.m20 = m7;
            this.m21 = m8;
            this.m22 = m9;
        }
    }
    
    public final void mulTransposeLeft(final Matrix3f matrix3f, final Matrix3f matrix3f2) {
        if (this != matrix3f && this != matrix3f2) {
            this.m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m10 * matrix3f2.m10 + matrix3f.m20 * matrix3f2.m20;
            this.m01 = matrix3f.m00 * matrix3f2.m01 + matrix3f.m10 * matrix3f2.m11 + matrix3f.m20 * matrix3f2.m21;
            this.m02 = matrix3f.m00 * matrix3f2.m02 + matrix3f.m10 * matrix3f2.m12 + matrix3f.m20 * matrix3f2.m22;
            this.m10 = matrix3f.m01 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m10 + matrix3f.m21 * matrix3f2.m20;
            this.m11 = matrix3f.m01 * matrix3f2.m01 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m21 * matrix3f2.m21;
            this.m12 = matrix3f.m01 * matrix3f2.m02 + matrix3f.m11 * matrix3f2.m12 + matrix3f.m21 * matrix3f2.m22;
            this.m20 = matrix3f.m02 * matrix3f2.m00 + matrix3f.m12 * matrix3f2.m10 + matrix3f.m22 * matrix3f2.m20;
            this.m21 = matrix3f.m02 * matrix3f2.m01 + matrix3f.m12 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m21;
            this.m22 = matrix3f.m02 * matrix3f2.m02 + matrix3f.m12 * matrix3f2.m12 + matrix3f.m22 * matrix3f2.m22;
        }
        else {
            final float m00 = matrix3f.m00 * matrix3f2.m00 + matrix3f.m10 * matrix3f2.m10 + matrix3f.m20 * matrix3f2.m20;
            final float m2 = matrix3f.m00 * matrix3f2.m01 + matrix3f.m10 * matrix3f2.m11 + matrix3f.m20 * matrix3f2.m21;
            final float m3 = matrix3f.m00 * matrix3f2.m02 + matrix3f.m10 * matrix3f2.m12 + matrix3f.m20 * matrix3f2.m22;
            final float m4 = matrix3f.m01 * matrix3f2.m00 + matrix3f.m11 * matrix3f2.m10 + matrix3f.m21 * matrix3f2.m20;
            final float m5 = matrix3f.m01 * matrix3f2.m01 + matrix3f.m11 * matrix3f2.m11 + matrix3f.m21 * matrix3f2.m21;
            final float m6 = matrix3f.m01 * matrix3f2.m02 + matrix3f.m11 * matrix3f2.m12 + matrix3f.m21 * matrix3f2.m22;
            final float m7 = matrix3f.m02 * matrix3f2.m00 + matrix3f.m12 * matrix3f2.m10 + matrix3f.m22 * matrix3f2.m20;
            final float m8 = matrix3f.m02 * matrix3f2.m01 + matrix3f.m12 * matrix3f2.m11 + matrix3f.m22 * matrix3f2.m21;
            final float m9 = matrix3f.m02 * matrix3f2.m02 + matrix3f.m12 * matrix3f2.m12 + matrix3f.m22 * matrix3f2.m22;
            this.m00 = m00;
            this.m01 = m2;
            this.m02 = m3;
            this.m10 = m4;
            this.m11 = m5;
            this.m12 = m6;
            this.m20 = m7;
            this.m21 = m8;
            this.m22 = m9;
        }
    }
    
    public final void normalize() {
        final double[] array = new double[9];
        this.getScaleRotate(new double[3], array);
        this.m00 = (float)array[0];
        this.m01 = (float)array[1];
        this.m02 = (float)array[2];
        this.m10 = (float)array[3];
        this.m11 = (float)array[4];
        this.m12 = (float)array[5];
        this.m20 = (float)array[6];
        this.m21 = (float)array[7];
        this.m22 = (float)array[8];
    }
    
    public final void normalize(final Matrix3f matrix3f) {
        final double[] array = new double[9];
        final double[] array2 = new double[9];
        final double[] array3 = new double[3];
        array[0] = matrix3f.m00;
        array[1] = matrix3f.m01;
        array[2] = matrix3f.m02;
        array[3] = matrix3f.m10;
        array[4] = matrix3f.m11;
        array[5] = matrix3f.m12;
        array[6] = matrix3f.m20;
        array[7] = matrix3f.m21;
        array[8] = matrix3f.m22;
        Matrix3d.compute_svd(array, array3, array2);
        this.m00 = (float)array2[0];
        this.m01 = (float)array2[1];
        this.m02 = (float)array2[2];
        this.m10 = (float)array2[3];
        this.m11 = (float)array2[4];
        this.m12 = (float)array2[5];
        this.m20 = (float)array2[6];
        this.m21 = (float)array2[7];
        this.m22 = (float)array2[8];
    }
    
    public final void normalizeCP() {
        final float n = 1.0f / (float)Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20);
        this.m00 *= n;
        this.m10 *= n;
        this.m20 *= n;
        final float n2 = 1.0f / (float)Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
        this.m01 *= n2;
        this.m11 *= n2;
        this.m21 *= n2;
        this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
        this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
        this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
    }
    
    public final void normalizeCP(final Matrix3f matrix3f) {
        final float n = 1.0f / (float)Math.sqrt(matrix3f.m00 * matrix3f.m00 + matrix3f.m10 * matrix3f.m10 + matrix3f.m20 * matrix3f.m20);
        this.m00 = matrix3f.m00 * n;
        this.m10 = matrix3f.m10 * n;
        this.m20 = matrix3f.m20 * n;
        final float n2 = 1.0f / (float)Math.sqrt(matrix3f.m01 * matrix3f.m01 + matrix3f.m11 * matrix3f.m11 + matrix3f.m21 * matrix3f.m21);
        this.m01 = matrix3f.m01 * n2;
        this.m11 = matrix3f.m11 * n2;
        this.m21 = matrix3f.m21 * n2;
        this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
        this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
        this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
    }
    
    public boolean equals(final Matrix3f matrix3f) {
        try {
            return this.m00 == matrix3f.m00 && this.m01 == matrix3f.m01 && this.m02 == matrix3f.m02 && this.m10 == matrix3f.m10 && this.m11 == matrix3f.m11 && this.m12 == matrix3f.m12 && this.m20 == matrix3f.m20 && this.m21 == matrix3f.m21 && this.m22 == matrix3f.m22;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Matrix3f matrix3f = (Matrix3f)o;
            return this.m00 == matrix3f.m00 && this.m01 == matrix3f.m01 && this.m02 == matrix3f.m02 && this.m10 == matrix3f.m10 && this.m11 == matrix3f.m11 && this.m12 == matrix3f.m12 && this.m20 == matrix3f.m20 && this.m21 == matrix3f.m21 && this.m22 == matrix3f.m22;
        }
        catch (ClassCastException ex) {
            return false;
        }
        catch (NullPointerException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Matrix3f matrix3f, final float n) {
        boolean b = true;
        if (Math.abs(this.m00 - matrix3f.m00) > n) {
            b = false;
        }
        if (Math.abs(this.m01 - matrix3f.m01) > n) {
            b = false;
        }
        if (Math.abs(this.m02 - matrix3f.m02) > n) {
            b = false;
        }
        if (Math.abs(this.m10 - matrix3f.m10) > n) {
            b = false;
        }
        if (Math.abs(this.m11 - matrix3f.m11) > n) {
            b = false;
        }
        if (Math.abs(this.m12 - matrix3f.m12) > n) {
            b = false;
        }
        if (Math.abs(this.m20 - matrix3f.m20) > n) {
            b = false;
        }
        if (Math.abs(this.m21 - matrix3f.m21) > n) {
            b = false;
        }
        if (Math.abs(this.m22 - matrix3f.m22) > n) {
            b = false;
        }
        return b;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * 1L + VecMathUtil.floatToIntBits(this.m00)) + VecMathUtil.floatToIntBits(this.m01)) + VecMathUtil.floatToIntBits(this.m02)) + VecMathUtil.floatToIntBits(this.m10)) + VecMathUtil.floatToIntBits(this.m11)) + VecMathUtil.floatToIntBits(this.m12)) + VecMathUtil.floatToIntBits(this.m20)) + VecMathUtil.floatToIntBits(this.m21)) + VecMathUtil.floatToIntBits(this.m22);
        return (int)(n ^ n >> 32);
    }
    
    public final void setZero() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
    }
    
    public final void negate() {
        this.m00 = -this.m00;
        this.m01 = -this.m01;
        this.m02 = -this.m02;
        this.m10 = -this.m10;
        this.m11 = -this.m11;
        this.m12 = -this.m12;
        this.m20 = -this.m20;
        this.m21 = -this.m21;
        this.m22 = -this.m22;
    }
    
    public final void negate(final Matrix3f matrix3f) {
        this.m00 = -matrix3f.m00;
        this.m01 = -matrix3f.m01;
        this.m02 = -matrix3f.m02;
        this.m10 = -matrix3f.m10;
        this.m11 = -matrix3f.m11;
        this.m12 = -matrix3f.m12;
        this.m20 = -matrix3f.m20;
        this.m21 = -matrix3f.m21;
        this.m22 = -matrix3f.m22;
    }
    
    public final void transform(final Tuple3f tuple3f) {
        tuple3f.set(this.m00 * tuple3f.x + this.m01 * tuple3f.y + this.m02 * tuple3f.z, this.m10 * tuple3f.x + this.m11 * tuple3f.y + this.m12 * tuple3f.z, this.m20 * tuple3f.x + this.m21 * tuple3f.y + this.m22 * tuple3f.z);
    }
    
    public final void transform(final Tuple3f tuple3f, final Tuple3f tuple3f2) {
        final float x = this.m00 * tuple3f.x + this.m01 * tuple3f.y + this.m02 * tuple3f.z;
        final float y = this.m10 * tuple3f.x + this.m11 * tuple3f.y + this.m12 * tuple3f.z;
        tuple3f2.z = this.m20 * tuple3f.x + this.m21 * tuple3f.y + this.m22 * tuple3f.z;
        tuple3f2.x = x;
        tuple3f2.y = y;
    }
    
    void getScaleRotate(final double[] array, final double[] array2) {
        Matrix3d.compute_svd(new double[] { this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22 }, array, array2);
    }
    
    public Object clone() {
        Matrix3f matrix3f;
        try {
            matrix3f = (Matrix3f)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        return matrix3f;
    }
}
