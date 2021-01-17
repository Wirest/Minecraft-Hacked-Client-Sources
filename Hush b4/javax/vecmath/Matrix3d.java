// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Matrix3d implements Serializable, Cloneable
{
    static final long serialVersionUID = 6837536777072402710L;
    public double m00;
    public double m01;
    public double m02;
    public double m10;
    public double m11;
    public double m12;
    public double m20;
    public double m21;
    public double m22;
    private static final double EPS = 1.110223024E-16;
    private static final double ERR_EPS = 1.0E-8;
    private static double xin;
    private static double yin;
    private static double zin;
    private static double xout;
    private static double yout;
    private static double zout;
    
    public Matrix3d(final double m00, final double m2, final double m3, final double m4, final double m5, final double m6, final double m7, final double m8, final double m9) {
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
    
    public Matrix3d(final double[] array) {
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
    
    public Matrix3d(final Matrix3d matrix3d) {
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
    
    public Matrix3d(final Matrix3f matrix3f) {
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
    
    public Matrix3d() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
    }
    
    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + "\n";
    }
    
    public final void setIdentity() {
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
    
    public final void setElement(final int n, final int n2, final double m22) {
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
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d0"));
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
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d0"));
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
                            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d0"));
                        }
                    }
                    break;
                }
                default: {
                    throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d0"));
                }
            }
        }
    }
    
    public final double getElement(final int n, final int n2) {
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
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d1"));
    }
    
    public final void getRow(final int n, final Vector3d vector3d) {
        if (n == 0) {
            vector3d.x = this.m00;
            vector3d.y = this.m01;
            vector3d.z = this.m02;
        }
        else if (n == 1) {
            vector3d.x = this.m10;
            vector3d.y = this.m11;
            vector3d.z = this.m12;
        }
        else {
            if (n != 2) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d2"));
            }
            vector3d.x = this.m20;
            vector3d.y = this.m21;
            vector3d.z = this.m22;
        }
    }
    
    public final void getRow(final int n, final double[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d2"));
            }
            array[0] = this.m20;
            array[1] = this.m21;
            array[2] = this.m22;
        }
    }
    
    public final void getColumn(final int n, final Vector3d vector3d) {
        if (n == 0) {
            vector3d.x = this.m00;
            vector3d.y = this.m10;
            vector3d.z = this.m20;
        }
        else if (n == 1) {
            vector3d.x = this.m01;
            vector3d.y = this.m11;
            vector3d.z = this.m21;
        }
        else {
            if (n != 2) {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d4"));
            }
            vector3d.x = this.m02;
            vector3d.y = this.m12;
            vector3d.z = this.m22;
        }
    }
    
    public final void getColumn(final int n, final double[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d4"));
            }
            array[0] = this.m02;
            array[1] = this.m12;
            array[2] = this.m22;
        }
    }
    
    public final void setRow(final int n, final double m20, final double m21, final double m22) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d6"));
            }
        }
    }
    
    public final void setRow(final int n, final Vector3d vector3d) {
        switch (n) {
            case 0: {
                this.m00 = vector3d.x;
                this.m01 = vector3d.y;
                this.m02 = vector3d.z;
                break;
            }
            case 1: {
                this.m10 = vector3d.x;
                this.m11 = vector3d.y;
                this.m12 = vector3d.z;
                break;
            }
            case 2: {
                this.m20 = vector3d.x;
                this.m21 = vector3d.y;
                this.m22 = vector3d.z;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d6"));
            }
        }
    }
    
    public final void setRow(final int n, final double[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d6"));
            }
        }
    }
    
    public final void setColumn(final int n, final double m02, final double m3, final double m4) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d9"));
            }
        }
    }
    
    public final void setColumn(final int n, final Vector3d vector3d) {
        switch (n) {
            case 0: {
                this.m00 = vector3d.x;
                this.m10 = vector3d.y;
                this.m20 = vector3d.z;
                break;
            }
            case 1: {
                this.m01 = vector3d.x;
                this.m11 = vector3d.y;
                this.m21 = vector3d.z;
                break;
            }
            case 2: {
                this.m02 = vector3d.x;
                this.m12 = vector3d.y;
                this.m22 = vector3d.z;
                break;
            }
            default: {
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d9"));
            }
        }
    }
    
    public final void setColumn(final int n, final double[] array) {
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
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix3d9"));
            }
        }
    }
    
    public final double getScale() {
        final double[] array = new double[3];
        this.getScaleRotate(array, new double[9]);
        return max3(array);
    }
    
    public final void add(final double n) {
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
    
    public final void add(final double n, final Matrix3d matrix3d) {
        this.m00 = matrix3d.m00 + n;
        this.m01 = matrix3d.m01 + n;
        this.m02 = matrix3d.m02 + n;
        this.m10 = matrix3d.m10 + n;
        this.m11 = matrix3d.m11 + n;
        this.m12 = matrix3d.m12 + n;
        this.m20 = matrix3d.m20 + n;
        this.m21 = matrix3d.m21 + n;
        this.m22 = matrix3d.m22 + n;
    }
    
    public final void add(final Matrix3d matrix3d, final Matrix3d matrix3d2) {
        this.m00 = matrix3d.m00 + matrix3d2.m00;
        this.m01 = matrix3d.m01 + matrix3d2.m01;
        this.m02 = matrix3d.m02 + matrix3d2.m02;
        this.m10 = matrix3d.m10 + matrix3d2.m10;
        this.m11 = matrix3d.m11 + matrix3d2.m11;
        this.m12 = matrix3d.m12 + matrix3d2.m12;
        this.m20 = matrix3d.m20 + matrix3d2.m20;
        this.m21 = matrix3d.m21 + matrix3d2.m21;
        this.m22 = matrix3d.m22 + matrix3d2.m22;
    }
    
    public final void add(final Matrix3d matrix3d) {
        this.m00 += matrix3d.m00;
        this.m01 += matrix3d.m01;
        this.m02 += matrix3d.m02;
        this.m10 += matrix3d.m10;
        this.m11 += matrix3d.m11;
        this.m12 += matrix3d.m12;
        this.m20 += matrix3d.m20;
        this.m21 += matrix3d.m21;
        this.m22 += matrix3d.m22;
    }
    
    public final void sub(final Matrix3d matrix3d, final Matrix3d matrix3d2) {
        this.m00 = matrix3d.m00 - matrix3d2.m00;
        this.m01 = matrix3d.m01 - matrix3d2.m01;
        this.m02 = matrix3d.m02 - matrix3d2.m02;
        this.m10 = matrix3d.m10 - matrix3d2.m10;
        this.m11 = matrix3d.m11 - matrix3d2.m11;
        this.m12 = matrix3d.m12 - matrix3d2.m12;
        this.m20 = matrix3d.m20 - matrix3d2.m20;
        this.m21 = matrix3d.m21 - matrix3d2.m21;
        this.m22 = matrix3d.m22 - matrix3d2.m22;
    }
    
    public final void sub(final Matrix3d matrix3d) {
        this.m00 -= matrix3d.m00;
        this.m01 -= matrix3d.m01;
        this.m02 -= matrix3d.m02;
        this.m10 -= matrix3d.m10;
        this.m11 -= matrix3d.m11;
        this.m12 -= matrix3d.m12;
        this.m20 -= matrix3d.m20;
        this.m21 -= matrix3d.m21;
        this.m22 -= matrix3d.m22;
    }
    
    public final void transpose() {
        final double m10 = this.m10;
        this.m10 = this.m01;
        this.m01 = m10;
        final double m11 = this.m20;
        this.m20 = this.m02;
        this.m02 = m11;
        final double m12 = this.m21;
        this.m21 = this.m12;
        this.m12 = m12;
    }
    
    public final void transpose(final Matrix3d matrix3d) {
        if (this != matrix3d) {
            this.m00 = matrix3d.m00;
            this.m01 = matrix3d.m10;
            this.m02 = matrix3d.m20;
            this.m10 = matrix3d.m01;
            this.m11 = matrix3d.m11;
            this.m12 = matrix3d.m21;
            this.m20 = matrix3d.m02;
            this.m21 = matrix3d.m12;
            this.m22 = matrix3d.m22;
        }
        else {
            this.transpose();
        }
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
    }
    
    public final void set(final AxisAngle4d axisAngle4d) {
        final double sqrt = Math.sqrt(axisAngle4d.x * axisAngle4d.x + axisAngle4d.y * axisAngle4d.y + axisAngle4d.z * axisAngle4d.z);
        if (sqrt < 1.110223024E-16) {
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
            final double n6 = axisAngle4d.x * axisAngle4d.z;
            final double n7 = axisAngle4d.x * axisAngle4d.y;
            final double n8 = axisAngle4d.y * axisAngle4d.z;
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
    }
    
    public final void set(final AxisAngle4f axisAngle4f) {
        final double sqrt = Math.sqrt(axisAngle4f.x * axisAngle4f.x + axisAngle4f.y * axisAngle4f.y + axisAngle4f.z * axisAngle4f.z);
        if (sqrt < 1.110223024E-16) {
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
    
    public final void set(final double[] array) {
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
    
    public final void invert(final Matrix3d matrix3d) {
        this.invertGeneral(matrix3d);
    }
    
    public final void invert() {
        this.invertGeneral(this);
    }
    
    private final void invertGeneral(final Matrix3d matrix3d) {
        final double[] array = new double[9];
        final int[] array2 = new int[3];
        final double[] array3 = { matrix3d.m00, matrix3d.m01, matrix3d.m02, matrix3d.m10, matrix3d.m11, matrix3d.m12, matrix3d.m20, matrix3d.m21, matrix3d.m22 };
        if (!luDecomposition(array3, array2)) {
            throw new SingularMatrixException(VecMathI18N.getString("Matrix3d12"));
        }
        for (int i = 0; i < 9; ++i) {
            array[i] = 0.0;
        }
        array[0] = 1.0;
        array[8] = (array[4] = 1.0);
        luBacksubstitution(array3, array2, array);
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
                throw new RuntimeException(VecMathI18N.getString("Matrix3d13"));
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
    
    public final double determinant() {
        return this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
    }
    
    public final void set(final double m22) {
        this.m00 = m22;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = m22;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = m22;
    }
    
    public final void rotX(final double n) {
        final double sin = Math.sin(n);
        final double cos = Math.cos(n);
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = cos;
        this.m12 = -sin;
        this.m20 = 0.0;
        this.m21 = sin;
        this.m22 = cos;
    }
    
    public final void rotY(final double n) {
        final double sin = Math.sin(n);
        final double cos = Math.cos(n);
        this.m00 = cos;
        this.m01 = 0.0;
        this.m02 = sin;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m20 = -sin;
        this.m21 = 0.0;
        this.m22 = cos;
    }
    
    public final void rotZ(final double n) {
        final double sin = Math.sin(n);
        final double cos = Math.cos(n);
        this.m00 = cos;
        this.m01 = -sin;
        this.m02 = 0.0;
        this.m10 = sin;
        this.m11 = cos;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
    }
    
    public final void mul(final double n) {
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
    
    public final void mul(final double n, final Matrix3d matrix3d) {
        this.m00 = n * matrix3d.m00;
        this.m01 = n * matrix3d.m01;
        this.m02 = n * matrix3d.m02;
        this.m10 = n * matrix3d.m10;
        this.m11 = n * matrix3d.m11;
        this.m12 = n * matrix3d.m12;
        this.m20 = n * matrix3d.m20;
        this.m21 = n * matrix3d.m21;
        this.m22 = n * matrix3d.m22;
    }
    
    public final void mul(final Matrix3d matrix3d) {
        final double m00 = this.m00 * matrix3d.m00 + this.m01 * matrix3d.m10 + this.m02 * matrix3d.m20;
        final double m2 = this.m00 * matrix3d.m01 + this.m01 * matrix3d.m11 + this.m02 * matrix3d.m21;
        final double m3 = this.m00 * matrix3d.m02 + this.m01 * matrix3d.m12 + this.m02 * matrix3d.m22;
        final double m4 = this.m10 * matrix3d.m00 + this.m11 * matrix3d.m10 + this.m12 * matrix3d.m20;
        final double m5 = this.m10 * matrix3d.m01 + this.m11 * matrix3d.m11 + this.m12 * matrix3d.m21;
        final double m6 = this.m10 * matrix3d.m02 + this.m11 * matrix3d.m12 + this.m12 * matrix3d.m22;
        final double m7 = this.m20 * matrix3d.m00 + this.m21 * matrix3d.m10 + this.m22 * matrix3d.m20;
        final double m8 = this.m20 * matrix3d.m01 + this.m21 * matrix3d.m11 + this.m22 * matrix3d.m21;
        final double m9 = this.m20 * matrix3d.m02 + this.m21 * matrix3d.m12 + this.m22 * matrix3d.m22;
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
    
    public final void mul(final Matrix3d matrix3d, final Matrix3d matrix3d2) {
        if (this != matrix3d && this != matrix3d2) {
            this.m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m01 * matrix3d2.m10 + matrix3d.m02 * matrix3d2.m20;
            this.m01 = matrix3d.m00 * matrix3d2.m01 + matrix3d.m01 * matrix3d2.m11 + matrix3d.m02 * matrix3d2.m21;
            this.m02 = matrix3d.m00 * matrix3d2.m02 + matrix3d.m01 * matrix3d2.m12 + matrix3d.m02 * matrix3d2.m22;
            this.m10 = matrix3d.m10 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m10 + matrix3d.m12 * matrix3d2.m20;
            this.m11 = matrix3d.m10 * matrix3d2.m01 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m12 * matrix3d2.m21;
            this.m12 = matrix3d.m10 * matrix3d2.m02 + matrix3d.m11 * matrix3d2.m12 + matrix3d.m12 * matrix3d2.m22;
            this.m20 = matrix3d.m20 * matrix3d2.m00 + matrix3d.m21 * matrix3d2.m10 + matrix3d.m22 * matrix3d2.m20;
            this.m21 = matrix3d.m20 * matrix3d2.m01 + matrix3d.m21 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m21;
            this.m22 = matrix3d.m20 * matrix3d2.m02 + matrix3d.m21 * matrix3d2.m12 + matrix3d.m22 * matrix3d2.m22;
        }
        else {
            final double m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m01 * matrix3d2.m10 + matrix3d.m02 * matrix3d2.m20;
            final double m2 = matrix3d.m00 * matrix3d2.m01 + matrix3d.m01 * matrix3d2.m11 + matrix3d.m02 * matrix3d2.m21;
            final double m3 = matrix3d.m00 * matrix3d2.m02 + matrix3d.m01 * matrix3d2.m12 + matrix3d.m02 * matrix3d2.m22;
            final double m4 = matrix3d.m10 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m10 + matrix3d.m12 * matrix3d2.m20;
            final double m5 = matrix3d.m10 * matrix3d2.m01 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m12 * matrix3d2.m21;
            final double m6 = matrix3d.m10 * matrix3d2.m02 + matrix3d.m11 * matrix3d2.m12 + matrix3d.m12 * matrix3d2.m22;
            final double m7 = matrix3d.m20 * matrix3d2.m00 + matrix3d.m21 * matrix3d2.m10 + matrix3d.m22 * matrix3d2.m20;
            final double m8 = matrix3d.m20 * matrix3d2.m01 + matrix3d.m21 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m21;
            final double m9 = matrix3d.m20 * matrix3d2.m02 + matrix3d.m21 * matrix3d2.m12 + matrix3d.m22 * matrix3d2.m22;
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
    
    public final void mulNormalize(final Matrix3d matrix3d) {
        final double[] array = new double[9];
        final double[] array2 = new double[9];
        final double[] array3 = new double[3];
        array[0] = this.m00 * matrix3d.m00 + this.m01 * matrix3d.m10 + this.m02 * matrix3d.m20;
        array[1] = this.m00 * matrix3d.m01 + this.m01 * matrix3d.m11 + this.m02 * matrix3d.m21;
        array[2] = this.m00 * matrix3d.m02 + this.m01 * matrix3d.m12 + this.m02 * matrix3d.m22;
        array[3] = this.m10 * matrix3d.m00 + this.m11 * matrix3d.m10 + this.m12 * matrix3d.m20;
        array[4] = this.m10 * matrix3d.m01 + this.m11 * matrix3d.m11 + this.m12 * matrix3d.m21;
        array[5] = this.m10 * matrix3d.m02 + this.m11 * matrix3d.m12 + this.m12 * matrix3d.m22;
        array[6] = this.m20 * matrix3d.m00 + this.m21 * matrix3d.m10 + this.m22 * matrix3d.m20;
        array[7] = this.m20 * matrix3d.m01 + this.m21 * matrix3d.m11 + this.m22 * matrix3d.m21;
        array[8] = this.m20 * matrix3d.m02 + this.m21 * matrix3d.m12 + this.m22 * matrix3d.m22;
        compute_svd(array, array3, array2);
        this.m00 = array2[0];
        this.m01 = array2[1];
        this.m02 = array2[2];
        this.m10 = array2[3];
        this.m11 = array2[4];
        this.m12 = array2[5];
        this.m20 = array2[6];
        this.m21 = array2[7];
        this.m22 = array2[8];
    }
    
    public final void mulNormalize(final Matrix3d matrix3d, final Matrix3d matrix3d2) {
        final double[] array = new double[9];
        final double[] array2 = new double[9];
        final double[] array3 = new double[3];
        array[0] = matrix3d.m00 * matrix3d2.m00 + matrix3d.m01 * matrix3d2.m10 + matrix3d.m02 * matrix3d2.m20;
        array[1] = matrix3d.m00 * matrix3d2.m01 + matrix3d.m01 * matrix3d2.m11 + matrix3d.m02 * matrix3d2.m21;
        array[2] = matrix3d.m00 * matrix3d2.m02 + matrix3d.m01 * matrix3d2.m12 + matrix3d.m02 * matrix3d2.m22;
        array[3] = matrix3d.m10 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m10 + matrix3d.m12 * matrix3d2.m20;
        array[4] = matrix3d.m10 * matrix3d2.m01 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m12 * matrix3d2.m21;
        array[5] = matrix3d.m10 * matrix3d2.m02 + matrix3d.m11 * matrix3d2.m12 + matrix3d.m12 * matrix3d2.m22;
        array[6] = matrix3d.m20 * matrix3d2.m00 + matrix3d.m21 * matrix3d2.m10 + matrix3d.m22 * matrix3d2.m20;
        array[7] = matrix3d.m20 * matrix3d2.m01 + matrix3d.m21 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m21;
        array[8] = matrix3d.m20 * matrix3d2.m02 + matrix3d.m21 * matrix3d2.m12 + matrix3d.m22 * matrix3d2.m22;
        compute_svd(array, array3, array2);
        this.m00 = array2[0];
        this.m01 = array2[1];
        this.m02 = array2[2];
        this.m10 = array2[3];
        this.m11 = array2[4];
        this.m12 = array2[5];
        this.m20 = array2[6];
        this.m21 = array2[7];
        this.m22 = array2[8];
    }
    
    public final void mulTransposeBoth(final Matrix3d matrix3d, final Matrix3d matrix3d2) {
        if (this != matrix3d && this != matrix3d2) {
            this.m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m10 * matrix3d2.m01 + matrix3d.m20 * matrix3d2.m02;
            this.m01 = matrix3d.m00 * matrix3d2.m10 + matrix3d.m10 * matrix3d2.m11 + matrix3d.m20 * matrix3d2.m12;
            this.m02 = matrix3d.m00 * matrix3d2.m20 + matrix3d.m10 * matrix3d2.m21 + matrix3d.m20 * matrix3d2.m22;
            this.m10 = matrix3d.m01 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m01 + matrix3d.m21 * matrix3d2.m02;
            this.m11 = matrix3d.m01 * matrix3d2.m10 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m21 * matrix3d2.m12;
            this.m12 = matrix3d.m01 * matrix3d2.m20 + matrix3d.m11 * matrix3d2.m21 + matrix3d.m21 * matrix3d2.m22;
            this.m20 = matrix3d.m02 * matrix3d2.m00 + matrix3d.m12 * matrix3d2.m01 + matrix3d.m22 * matrix3d2.m02;
            this.m21 = matrix3d.m02 * matrix3d2.m10 + matrix3d.m12 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m12;
            this.m22 = matrix3d.m02 * matrix3d2.m20 + matrix3d.m12 * matrix3d2.m21 + matrix3d.m22 * matrix3d2.m22;
        }
        else {
            final double m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m10 * matrix3d2.m01 + matrix3d.m20 * matrix3d2.m02;
            final double m2 = matrix3d.m00 * matrix3d2.m10 + matrix3d.m10 * matrix3d2.m11 + matrix3d.m20 * matrix3d2.m12;
            final double m3 = matrix3d.m00 * matrix3d2.m20 + matrix3d.m10 * matrix3d2.m21 + matrix3d.m20 * matrix3d2.m22;
            final double m4 = matrix3d.m01 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m01 + matrix3d.m21 * matrix3d2.m02;
            final double m5 = matrix3d.m01 * matrix3d2.m10 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m21 * matrix3d2.m12;
            final double m6 = matrix3d.m01 * matrix3d2.m20 + matrix3d.m11 * matrix3d2.m21 + matrix3d.m21 * matrix3d2.m22;
            final double m7 = matrix3d.m02 * matrix3d2.m00 + matrix3d.m12 * matrix3d2.m01 + matrix3d.m22 * matrix3d2.m02;
            final double m8 = matrix3d.m02 * matrix3d2.m10 + matrix3d.m12 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m12;
            final double m9 = matrix3d.m02 * matrix3d2.m20 + matrix3d.m12 * matrix3d2.m21 + matrix3d.m22 * matrix3d2.m22;
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
    
    public final void mulTransposeRight(final Matrix3d matrix3d, final Matrix3d matrix3d2) {
        if (this != matrix3d && this != matrix3d2) {
            this.m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m01 * matrix3d2.m01 + matrix3d.m02 * matrix3d2.m02;
            this.m01 = matrix3d.m00 * matrix3d2.m10 + matrix3d.m01 * matrix3d2.m11 + matrix3d.m02 * matrix3d2.m12;
            this.m02 = matrix3d.m00 * matrix3d2.m20 + matrix3d.m01 * matrix3d2.m21 + matrix3d.m02 * matrix3d2.m22;
            this.m10 = matrix3d.m10 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m01 + matrix3d.m12 * matrix3d2.m02;
            this.m11 = matrix3d.m10 * matrix3d2.m10 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m12 * matrix3d2.m12;
            this.m12 = matrix3d.m10 * matrix3d2.m20 + matrix3d.m11 * matrix3d2.m21 + matrix3d.m12 * matrix3d2.m22;
            this.m20 = matrix3d.m20 * matrix3d2.m00 + matrix3d.m21 * matrix3d2.m01 + matrix3d.m22 * matrix3d2.m02;
            this.m21 = matrix3d.m20 * matrix3d2.m10 + matrix3d.m21 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m12;
            this.m22 = matrix3d.m20 * matrix3d2.m20 + matrix3d.m21 * matrix3d2.m21 + matrix3d.m22 * matrix3d2.m22;
        }
        else {
            final double m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m01 * matrix3d2.m01 + matrix3d.m02 * matrix3d2.m02;
            final double m2 = matrix3d.m00 * matrix3d2.m10 + matrix3d.m01 * matrix3d2.m11 + matrix3d.m02 * matrix3d2.m12;
            final double m3 = matrix3d.m00 * matrix3d2.m20 + matrix3d.m01 * matrix3d2.m21 + matrix3d.m02 * matrix3d2.m22;
            final double m4 = matrix3d.m10 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m01 + matrix3d.m12 * matrix3d2.m02;
            final double m5 = matrix3d.m10 * matrix3d2.m10 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m12 * matrix3d2.m12;
            final double m6 = matrix3d.m10 * matrix3d2.m20 + matrix3d.m11 * matrix3d2.m21 + matrix3d.m12 * matrix3d2.m22;
            final double m7 = matrix3d.m20 * matrix3d2.m00 + matrix3d.m21 * matrix3d2.m01 + matrix3d.m22 * matrix3d2.m02;
            final double m8 = matrix3d.m20 * matrix3d2.m10 + matrix3d.m21 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m12;
            final double m9 = matrix3d.m20 * matrix3d2.m20 + matrix3d.m21 * matrix3d2.m21 + matrix3d.m22 * matrix3d2.m22;
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
    
    public final void mulTransposeLeft(final Matrix3d matrix3d, final Matrix3d matrix3d2) {
        if (this != matrix3d && this != matrix3d2) {
            this.m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m10 * matrix3d2.m10 + matrix3d.m20 * matrix3d2.m20;
            this.m01 = matrix3d.m00 * matrix3d2.m01 + matrix3d.m10 * matrix3d2.m11 + matrix3d.m20 * matrix3d2.m21;
            this.m02 = matrix3d.m00 * matrix3d2.m02 + matrix3d.m10 * matrix3d2.m12 + matrix3d.m20 * matrix3d2.m22;
            this.m10 = matrix3d.m01 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m10 + matrix3d.m21 * matrix3d2.m20;
            this.m11 = matrix3d.m01 * matrix3d2.m01 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m21 * matrix3d2.m21;
            this.m12 = matrix3d.m01 * matrix3d2.m02 + matrix3d.m11 * matrix3d2.m12 + matrix3d.m21 * matrix3d2.m22;
            this.m20 = matrix3d.m02 * matrix3d2.m00 + matrix3d.m12 * matrix3d2.m10 + matrix3d.m22 * matrix3d2.m20;
            this.m21 = matrix3d.m02 * matrix3d2.m01 + matrix3d.m12 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m21;
            this.m22 = matrix3d.m02 * matrix3d2.m02 + matrix3d.m12 * matrix3d2.m12 + matrix3d.m22 * matrix3d2.m22;
        }
        else {
            final double m00 = matrix3d.m00 * matrix3d2.m00 + matrix3d.m10 * matrix3d2.m10 + matrix3d.m20 * matrix3d2.m20;
            final double m2 = matrix3d.m00 * matrix3d2.m01 + matrix3d.m10 * matrix3d2.m11 + matrix3d.m20 * matrix3d2.m21;
            final double m3 = matrix3d.m00 * matrix3d2.m02 + matrix3d.m10 * matrix3d2.m12 + matrix3d.m20 * matrix3d2.m22;
            final double m4 = matrix3d.m01 * matrix3d2.m00 + matrix3d.m11 * matrix3d2.m10 + matrix3d.m21 * matrix3d2.m20;
            final double m5 = matrix3d.m01 * matrix3d2.m01 + matrix3d.m11 * matrix3d2.m11 + matrix3d.m21 * matrix3d2.m21;
            final double m6 = matrix3d.m01 * matrix3d2.m02 + matrix3d.m11 * matrix3d2.m12 + matrix3d.m21 * matrix3d2.m22;
            final double m7 = matrix3d.m02 * matrix3d2.m00 + matrix3d.m12 * matrix3d2.m10 + matrix3d.m22 * matrix3d2.m20;
            final double m8 = matrix3d.m02 * matrix3d2.m01 + matrix3d.m12 * matrix3d2.m11 + matrix3d.m22 * matrix3d2.m21;
            final double m9 = matrix3d.m02 * matrix3d2.m02 + matrix3d.m12 * matrix3d2.m12 + matrix3d.m22 * matrix3d2.m22;
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
    
    public final void normalize(final Matrix3d matrix3d) {
        final double[] array = new double[9];
        final double[] array2 = new double[9];
        final double[] array3 = new double[3];
        array[0] = matrix3d.m00;
        array[1] = matrix3d.m01;
        array[2] = matrix3d.m02;
        array[3] = matrix3d.m10;
        array[4] = matrix3d.m11;
        array[5] = matrix3d.m12;
        array[6] = matrix3d.m20;
        array[7] = matrix3d.m21;
        array[8] = matrix3d.m22;
        compute_svd(array, array3, array2);
        this.m00 = array2[0];
        this.m01 = array2[1];
        this.m02 = array2[2];
        this.m10 = array2[3];
        this.m11 = array2[4];
        this.m12 = array2[5];
        this.m20 = array2[6];
        this.m21 = array2[7];
        this.m22 = array2[8];
    }
    
    public final void normalizeCP() {
        final double n = 1.0 / Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20);
        this.m00 *= n;
        this.m10 *= n;
        this.m20 *= n;
        final double n2 = 1.0 / Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
        this.m01 *= n2;
        this.m11 *= n2;
        this.m21 *= n2;
        this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
        this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
        this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
    }
    
    public final void normalizeCP(final Matrix3d matrix3d) {
        final double n = 1.0 / Math.sqrt(matrix3d.m00 * matrix3d.m00 + matrix3d.m10 * matrix3d.m10 + matrix3d.m20 * matrix3d.m20);
        this.m00 = matrix3d.m00 * n;
        this.m10 = matrix3d.m10 * n;
        this.m20 = matrix3d.m20 * n;
        final double n2 = 1.0 / Math.sqrt(matrix3d.m01 * matrix3d.m01 + matrix3d.m11 * matrix3d.m11 + matrix3d.m21 * matrix3d.m21);
        this.m01 = matrix3d.m01 * n2;
        this.m11 = matrix3d.m11 * n2;
        this.m21 = matrix3d.m21 * n2;
        this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
        this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
        this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
    }
    
    public boolean equals(final Matrix3d matrix3d) {
        try {
            return this.m00 == matrix3d.m00 && this.m01 == matrix3d.m01 && this.m02 == matrix3d.m02 && this.m10 == matrix3d.m10 && this.m11 == matrix3d.m11 && this.m12 == matrix3d.m12 && this.m20 == matrix3d.m20 && this.m21 == matrix3d.m21 && this.m22 == matrix3d.m22;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final Matrix3d matrix3d = (Matrix3d)o;
            return this.m00 == matrix3d.m00 && this.m01 == matrix3d.m01 && this.m02 == matrix3d.m02 && this.m10 == matrix3d.m10 && this.m11 == matrix3d.m11 && this.m12 == matrix3d.m12 && this.m20 == matrix3d.m20 && this.m21 == matrix3d.m21 && this.m22 == matrix3d.m22;
        }
        catch (ClassCastException ex) {
            return false;
        }
        catch (NullPointerException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final Matrix3d matrix3d, final double n) {
        final double n2 = this.m00 - matrix3d.m00;
        if (((n2 < 0.0) ? (-n2) : n2) > n) {
            return false;
        }
        final double n3 = this.m01 - matrix3d.m01;
        if (((n3 < 0.0) ? (-n3) : n3) > n) {
            return false;
        }
        final double n4 = this.m02 - matrix3d.m02;
        if (((n4 < 0.0) ? (-n4) : n4) > n) {
            return false;
        }
        final double n5 = this.m10 - matrix3d.m10;
        if (((n5 < 0.0) ? (-n5) : n5) > n) {
            return false;
        }
        final double n6 = this.m11 - matrix3d.m11;
        if (((n6 < 0.0) ? (-n6) : n6) > n) {
            return false;
        }
        final double n7 = this.m12 - matrix3d.m12;
        if (((n7 < 0.0) ? (-n7) : n7) > n) {
            return false;
        }
        final double n8 = this.m20 - matrix3d.m20;
        if (((n8 < 0.0) ? (-n8) : n8) > n) {
            return false;
        }
        final double n9 = this.m21 - matrix3d.m21;
        if (((n9 < 0.0) ? (-n9) : n9) > n) {
            return false;
        }
        final double n10 = this.m22 - matrix3d.m22;
        return ((n10 < 0.0) ? (-n10) : n10) <= n;
    }
    
    public int hashCode() {
        final long n = 31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * (31L * 1L + VecMathUtil.doubleToLongBits(this.m00)) + VecMathUtil.doubleToLongBits(this.m01)) + VecMathUtil.doubleToLongBits(this.m02)) + VecMathUtil.doubleToLongBits(this.m10)) + VecMathUtil.doubleToLongBits(this.m11)) + VecMathUtil.doubleToLongBits(this.m12)) + VecMathUtil.doubleToLongBits(this.m20)) + VecMathUtil.doubleToLongBits(this.m21)) + VecMathUtil.doubleToLongBits(this.m22);
        return (int)(n ^ n >> 32);
    }
    
    public final void setZero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
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
    
    public final void negate(final Matrix3d matrix3d) {
        this.m00 = -matrix3d.m00;
        this.m01 = -matrix3d.m01;
        this.m02 = -matrix3d.m02;
        this.m10 = -matrix3d.m10;
        this.m11 = -matrix3d.m11;
        this.m12 = -matrix3d.m12;
        this.m20 = -matrix3d.m20;
        this.m21 = -matrix3d.m21;
        this.m22 = -matrix3d.m22;
    }
    
    public final void transform(final Tuple3d tuple3d) {
        tuple3d.set(this.m00 * tuple3d.x + this.m01 * tuple3d.y + this.m02 * tuple3d.z, this.m10 * tuple3d.x + this.m11 * tuple3d.y + this.m12 * tuple3d.z, this.m20 * tuple3d.x + this.m21 * tuple3d.y + this.m22 * tuple3d.z);
    }
    
    public final void transform(final Tuple3d tuple3d, final Tuple3d tuple3d2) {
        final double x = this.m00 * tuple3d.x + this.m01 * tuple3d.y + this.m02 * tuple3d.z;
        final double y = this.m10 * tuple3d.x + this.m11 * tuple3d.y + this.m12 * tuple3d.z;
        tuple3d2.z = this.m20 * tuple3d.x + this.m21 * tuple3d.y + this.m22 * tuple3d.z;
        tuple3d2.x = x;
        tuple3d2.y = y;
    }
    
    final void getScaleRotate(final double[] array, final double[] array2) {
        compute_svd(new double[] { this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22 }, array, array2);
    }
    
    static void compute_svd(final double[] array, final double[] array2, final double[] array3) {
        final double[] array4 = new double[9];
        final double[] array5 = new double[9];
        final double[] array6 = new double[9];
        final double[] array7 = new double[9];
        final double[] array8 = array6;
        final double[] array9 = array7;
        final double[] array10 = new double[9];
        final double[] array11 = new double[3];
        final double[] array12 = new double[3];
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            array10[i] = array[i];
        }
        if (array[3] * array[3] < 1.110223024E-16) {
            array4[0] = 1.0;
            array4[1] = 0.0;
            array4[3] = (array4[2] = 0.0);
            array4[4] = 1.0;
            array4[5] = 0.0;
            array4[7] = (array4[6] = 0.0);
            array4[8] = 1.0;
        }
        else if (array[0] * array[0] < 1.110223024E-16) {
            array8[0] = array[0];
            array8[1] = array[1];
            array8[2] = array[2];
            array[0] = array[3];
            array[1] = array[4];
            array[2] = array[5];
            array[3] = -array8[0];
            array[4] = -array8[1];
            array[5] = -array8[2];
            array4[0] = 0.0;
            array4[1] = 1.0;
            array4[2] = 0.0;
            array4[3] = -1.0;
            array4[5] = (array4[4] = 0.0);
            array4[7] = (array4[6] = 0.0);
            array4[8] = 1.0;
        }
        else {
            final double n2 = 1.0 / Math.sqrt(array[0] * array[0] + array[3] * array[3]);
            final double n3 = array[0] * n2;
            final double n4 = array[3] * n2;
            array8[0] = n3 * array[0] + n4 * array[3];
            array8[1] = n3 * array[1] + n4 * array[4];
            array8[2] = n3 * array[2] + n4 * array[5];
            array[3] = -n4 * array[0] + n3 * array[3];
            array[4] = -n4 * array[1] + n3 * array[4];
            array[5] = -n4 * array[2] + n3 * array[5];
            array[0] = array8[0];
            array[1] = array8[1];
            array[2] = array8[2];
            array4[0] = n3;
            array4[1] = n4;
            array4[2] = 0.0;
            array4[3] = -n4;
            array4[4] = n3;
            array4[5] = 0.0;
            array4[7] = (array4[6] = 0.0);
            array4[8] = 1.0;
        }
        if (array[6] * array[6] >= 1.110223024E-16) {
            if (array[0] * array[0] < 1.110223024E-16) {
                array8[0] = array[0];
                array8[1] = array[1];
                array8[2] = array[2];
                array[0] = array[6];
                array[1] = array[7];
                array[2] = array[8];
                array[6] = -array8[0];
                array[7] = -array8[1];
                array[8] = -array8[2];
                array8[0] = array4[0];
                array8[1] = array4[1];
                array8[2] = array4[2];
                array4[0] = array4[6];
                array4[1] = array4[7];
                array4[2] = array4[8];
                array4[6] = -array8[0];
                array4[7] = -array8[1];
                array4[8] = -array8[2];
            }
            else {
                final double n5 = 1.0 / Math.sqrt(array[0] * array[0] + array[6] * array[6]);
                final double n6 = array[0] * n5;
                final double n7 = array[6] * n5;
                array8[0] = n6 * array[0] + n7 * array[6];
                array8[1] = n6 * array[1] + n7 * array[7];
                array8[2] = n6 * array[2] + n7 * array[8];
                array[6] = -n7 * array[0] + n6 * array[6];
                array[7] = -n7 * array[1] + n6 * array[7];
                array[8] = -n7 * array[2] + n6 * array[8];
                array[0] = array8[0];
                array[1] = array8[1];
                array[2] = array8[2];
                array8[0] = n6 * array4[0];
                array8[1] = n6 * array4[1];
                array4[2] = n7;
                array8[6] = -array4[0] * n7;
                array8[7] = -array4[1] * n7;
                array4[8] = n6;
                array4[0] = array8[0];
                array4[1] = array8[1];
                array4[6] = array8[6];
                array4[7] = array8[7];
            }
        }
        if (array[2] * array[2] < 1.110223024E-16) {
            array5[0] = 1.0;
            array5[1] = 0.0;
            array5[3] = (array5[2] = 0.0);
            array5[4] = 1.0;
            array5[5] = 0.0;
            array5[7] = (array5[6] = 0.0);
            array5[8] = 1.0;
        }
        else if (array[1] * array[1] < 1.110223024E-16) {
            array8[2] = array[2];
            array8[5] = array[5];
            array8[8] = array[8];
            array[2] = -array[1];
            array[5] = -array[4];
            array[8] = -array[7];
            array[1] = array8[2];
            array[4] = array8[5];
            array[7] = array8[8];
            array5[0] = 1.0;
            array5[2] = (array5[1] = 0.0);
            array5[4] = (array5[3] = 0.0);
            array5[5] = -1.0;
            array5[6] = 0.0;
            array5[7] = 1.0;
            array5[8] = 0.0;
        }
        else {
            final double n8 = 1.0 / Math.sqrt(array[1] * array[1] + array[2] * array[2]);
            final double n9 = array[1] * n8;
            final double n10 = array[2] * n8;
            array8[1] = n9 * array[1] + n10 * array[2];
            array[2] = -n10 * array[1] + n9 * array[2];
            array[1] = array8[1];
            array8[4] = n9 * array[4] + n10 * array[5];
            array[5] = -n10 * array[4] + n9 * array[5];
            array[4] = array8[4];
            array8[7] = n9 * array[7] + n10 * array[8];
            array[8] = -n10 * array[7] + n9 * array[8];
            array[7] = array8[7];
            array5[0] = 1.0;
            array5[1] = 0.0;
            array5[3] = (array5[2] = 0.0);
            array5[4] = n9;
            array5[5] = -n10;
            array5[6] = 0.0;
            array5[7] = n10;
            array5[8] = n9;
        }
        if (array[7] * array[7] >= 1.110223024E-16) {
            if (array[4] * array[4] < 1.110223024E-16) {
                array8[3] = array[3];
                array8[4] = array[4];
                array8[5] = array[5];
                array[3] = array[6];
                array[4] = array[7];
                array[5] = array[8];
                array[6] = -array8[3];
                array[7] = -array8[4];
                array[8] = -array8[5];
                array8[3] = array4[3];
                array8[4] = array4[4];
                array8[5] = array4[5];
                array4[3] = array4[6];
                array4[4] = array4[7];
                array4[5] = array4[8];
                array4[6] = -array8[3];
                array4[7] = -array8[4];
                array4[8] = -array8[5];
            }
            else {
                final double n11 = 1.0 / Math.sqrt(array[4] * array[4] + array[7] * array[7]);
                final double n12 = array[4] * n11;
                final double n13 = array[7] * n11;
                array8[3] = n12 * array[3] + n13 * array[6];
                array[6] = -n13 * array[3] + n12 * array[6];
                array[3] = array8[3];
                array8[4] = n12 * array[4] + n13 * array[7];
                array[7] = -n13 * array[4] + n12 * array[7];
                array[4] = array8[4];
                array8[5] = n12 * array[5] + n13 * array[8];
                array[8] = -n13 * array[5] + n12 * array[8];
                array[5] = array8[5];
                array8[3] = n12 * array4[3] + n13 * array4[6];
                array4[6] = -n13 * array4[3] + n12 * array4[6];
                array4[3] = array8[3];
                array8[4] = n12 * array4[4] + n13 * array4[7];
                array4[7] = -n13 * array4[4] + n12 * array4[7];
                array4[4] = array8[4];
                array8[5] = n12 * array4[5] + n13 * array4[8];
                array4[8] = -n13 * array4[5] + n12 * array4[8];
                array4[5] = array8[5];
            }
        }
        array9[0] = array[0];
        array9[1] = array[4];
        array9[2] = array[8];
        array11[0] = array[1];
        array11[1] = array[5];
        if (array11[0] * array11[0] >= 1.110223024E-16 || array11[1] * array11[1] >= 1.110223024E-16) {
            compute_qr(array9, array11, array4, array5);
        }
        array12[0] = array9[0];
        array12[1] = array9[1];
        array12[2] = array9[2];
        if (almostEqual(Math.abs(array12[0]), 1.0) && almostEqual(Math.abs(array12[1]), 1.0) && almostEqual(Math.abs(array12[2]), 1.0)) {
            for (int j = 0; j < 3; ++j) {
                if (array12[j] < 0.0) {
                    ++n;
                }
            }
            if (n == 0 || n == 2) {
                final int n14 = 0;
                final int n15 = 1;
                final int n16 = 2;
                final double n17 = 1.0;
                array2[n16] = n17;
                array2[n14] = (array2[n15] = n17);
                for (int k = 0; k < 9; ++k) {
                    array3[k] = array10[k];
                }
                return;
            }
        }
        transpose_mat(array4, array6);
        transpose_mat(array5, array7);
        svdReorder(array, array6, array7, array12, array3, array2);
    }
    
    static void svdReorder(final double[] array, final double[] array2, final double[] array3, final double[] array4, final double[] array5, final double[] array6) {
        final int[] array7 = new int[3];
        final int[] array8 = new int[3];
        final double[] array9 = new double[3];
        final double[] array10 = new double[9];
        if (array4[0] < 0.0) {
            array4[0] = -array4[0];
            array3[0] = -array3[0];
            array3[1] = -array3[1];
            array3[2] = -array3[2];
        }
        if (array4[1] < 0.0) {
            array4[1] = -array4[1];
            array3[3] = -array3[3];
            array3[4] = -array3[4];
            array3[5] = -array3[5];
        }
        if (array4[2] < 0.0) {
            array4[2] = -array4[2];
            array3[6] = -array3[6];
            array3[7] = -array3[7];
            array3[8] = -array3[8];
        }
        mat_mul(array2, array3, array10);
        if (almostEqual(Math.abs(array4[0]), Math.abs(array4[1])) && almostEqual(Math.abs(array4[1]), Math.abs(array4[2]))) {
            for (int i = 0; i < 9; ++i) {
                array5[i] = array10[i];
            }
            for (int j = 0; j < 3; ++j) {
                array6[j] = array4[j];
            }
        }
        else {
            if (array4[0] > array4[1]) {
                if (array4[0] > array4[2]) {
                    if (array4[2] > array4[1]) {
                        array7[0] = 0;
                        array7[array7[1] = 2] = 1;
                    }
                    else {
                        array7[0] = 0;
                        array7[1] = 1;
                        array7[2] = 2;
                    }
                }
                else {
                    array7[0] = 2;
                    array7[1] = 0;
                    array7[2] = 1;
                }
            }
            else if (array4[1] > array4[2]) {
                if (array4[2] > array4[0]) {
                    array7[0] = 1;
                    array7[array7[1] = 2] = 0;
                }
                else {
                    array7[array7[0] = 1] = 0;
                    array7[2] = 2;
                }
            }
            else {
                array7[0] = 2;
                array7[1] = 1;
                array7[2] = 0;
            }
            array9[0] = array[0] * array[0] + array[1] * array[1] + array[2] * array[2];
            array9[1] = array[3] * array[3] + array[4] * array[4] + array[5] * array[5];
            array9[2] = array[6] * array[6] + array[7] * array[7] + array[8] * array[8];
            int n;
            int n2;
            int n3;
            if (array9[0] > array9[1]) {
                if (array9[0] > array9[2]) {
                    if (array9[2] > array9[1]) {
                        n = 0;
                        n2 = 1;
                        n3 = 2;
                    }
                    else {
                        n = 0;
                        n3 = 1;
                        n2 = 2;
                    }
                }
                else {
                    n2 = 0;
                    n = 1;
                    n3 = 2;
                }
            }
            else if (array9[1] > array9[2]) {
                if (array9[2] > array9[0]) {
                    n3 = 0;
                    n2 = 1;
                    n = 2;
                }
                else {
                    n3 = 0;
                    n = 1;
                    n2 = 2;
                }
            }
            else {
                n2 = 0;
                n3 = 1;
                n = 2;
            }
            array6[0] = array4[array7[n]];
            array6[1] = array4[array7[n3]];
            array6[2] = array4[array7[n2]];
            array5[0] = array10[array7[n]];
            array5[3] = array10[array7[n] + 3];
            array5[6] = array10[array7[n] + 6];
            array5[1] = array10[array7[n3]];
            array5[4] = array10[array7[n3] + 3];
            array5[7] = array10[array7[n3] + 6];
            array5[2] = array10[array7[n2]];
            array5[5] = array10[array7[n2] + 3];
            array5[8] = array10[array7[n2] + 6];
        }
    }
    
    static int compute_qr(final double[] array, final double[] array2, final double[] array3, final double[] array4) {
        final double[] array5 = new double[2];
        final double[] array6 = new double[2];
        final double[] array7 = new double[2];
        final double[] array8 = new double[2];
        final double[] array9 = new double[9];
        final double n = 1.0;
        int n2 = 0;
        int n3 = 1;
        if (Math.abs(array2[1]) < 4.89E-15 || Math.abs(array2[0]) < 4.89E-15) {
            n2 = 1;
        }
        for (int n4 = 0; n4 < 10 && n2 == 0; ++n4) {
            final double compute_shift = compute_shift(array[1], array2[1], array[2]);
            compute_rot((Math.abs(array[0]) - compute_shift) * (d_sign(n, array[0]) + compute_shift / array[0]), array2[0], array8, array6, 0, n3);
            final double n5 = array6[0] * array[0] + array8[0] * array2[0];
            array2[0] = array6[0] * array2[0] - array8[0] * array[0];
            final double n6 = array8[0] * array[1];
            array[1] *= array6[0];
            final double compute_rot = compute_rot(n5, n6, array7, array5, 0, n3);
            n3 = 0;
            array[0] = compute_rot;
            final double n7 = array5[0] * array2[0] + array7[0] * array[1];
            array[1] = array5[0] * array[1] - array7[0] * array2[0];
            final double n8 = array7[0] * array2[1];
            array2[1] *= array5[0];
            array2[0] = compute_rot(n7, n8, array8, array6, 1, n3);
            final double n9 = array6[1] * array[1] + array8[1] * array2[1];
            array2[1] = array6[1] * array2[1] - array8[1] * array[1];
            final double n10 = array8[1] * array[2];
            array[2] *= array6[1];
            array[1] = compute_rot(n9, n10, array7, array5, 1, n3);
            final double n11 = array5[1] * array2[1] + array7[1] * array[2];
            array[2] = array5[1] * array[2] - array7[1] * array2[1];
            array2[1] = n11;
            final double n12 = array3[0];
            array3[0] = array5[0] * n12 + array7[0] * array3[3];
            array3[3] = -array7[0] * n12 + array5[0] * array3[3];
            final double n13 = array3[1];
            array3[1] = array5[0] * n13 + array7[0] * array3[4];
            array3[4] = -array7[0] * n13 + array5[0] * array3[4];
            final double n14 = array3[2];
            array3[2] = array5[0] * n14 + array7[0] * array3[5];
            array3[5] = -array7[0] * n14 + array5[0] * array3[5];
            final double n15 = array3[3];
            array3[3] = array5[1] * n15 + array7[1] * array3[6];
            array3[6] = -array7[1] * n15 + array5[1] * array3[6];
            final double n16 = array3[4];
            array3[4] = array5[1] * n16 + array7[1] * array3[7];
            array3[7] = -array7[1] * n16 + array5[1] * array3[7];
            final double n17 = array3[5];
            array3[5] = array5[1] * n17 + array7[1] * array3[8];
            array3[8] = -array7[1] * n17 + array5[1] * array3[8];
            final double n18 = array4[0];
            array4[0] = array6[0] * n18 + array8[0] * array4[1];
            array4[1] = -array8[0] * n18 + array6[0] * array4[1];
            final double n19 = array4[3];
            array4[3] = array6[0] * n19 + array8[0] * array4[4];
            array4[4] = -array8[0] * n19 + array6[0] * array4[4];
            final double n20 = array4[6];
            array4[6] = array6[0] * n20 + array8[0] * array4[7];
            array4[7] = -array8[0] * n20 + array6[0] * array4[7];
            final double n21 = array4[1];
            array4[1] = array6[1] * n21 + array8[1] * array4[2];
            array4[2] = -array8[1] * n21 + array6[1] * array4[2];
            final double n22 = array4[4];
            array4[4] = array6[1] * n22 + array8[1] * array4[5];
            array4[5] = -array8[1] * n22 + array6[1] * array4[5];
            final double n23 = array4[7];
            array4[7] = array6[1] * n23 + array8[1] * array4[8];
            array4[8] = -array8[1] * n23 + array6[1] * array4[8];
            array9[0] = array[0];
            array9[1] = array2[0];
            array9[3] = (array9[2] = 0.0);
            array9[4] = array[1];
            array9[5] = array2[1];
            array9[7] = (array9[6] = 0.0);
            array9[8] = array[2];
            if (Math.abs(array2[1]) < 4.89E-15 || Math.abs(array2[0]) < 4.89E-15) {
                n2 = 1;
            }
        }
        if (Math.abs(array2[1]) < 4.89E-15) {
            compute_2X2(array[0], array2[0], array[1], array, array7, array5, array8, array6, 0);
            final double n24 = array3[0];
            array3[0] = array5[0] * n24 + array7[0] * array3[3];
            array3[3] = -array7[0] * n24 + array5[0] * array3[3];
            final double n25 = array3[1];
            array3[1] = array5[0] * n25 + array7[0] * array3[4];
            array3[4] = -array7[0] * n25 + array5[0] * array3[4];
            final double n26 = array3[2];
            array3[2] = array5[0] * n26 + array7[0] * array3[5];
            array3[5] = -array7[0] * n26 + array5[0] * array3[5];
            final double n27 = array4[0];
            array4[0] = array6[0] * n27 + array8[0] * array4[1];
            array4[1] = -array8[0] * n27 + array6[0] * array4[1];
            final double n28 = array4[3];
            array4[3] = array6[0] * n28 + array8[0] * array4[4];
            array4[4] = -array8[0] * n28 + array6[0] * array4[4];
            final double n29 = array4[6];
            array4[6] = array6[0] * n29 + array8[0] * array4[7];
            array4[7] = -array8[0] * n29 + array6[0] * array4[7];
        }
        else {
            compute_2X2(array[1], array2[1], array[2], array, array7, array5, array8, array6, 1);
            final double n30 = array3[3];
            array3[3] = array5[0] * n30 + array7[0] * array3[6];
            array3[6] = -array7[0] * n30 + array5[0] * array3[6];
            final double n31 = array3[4];
            array3[4] = array5[0] * n31 + array7[0] * array3[7];
            array3[7] = -array7[0] * n31 + array5[0] * array3[7];
            final double n32 = array3[5];
            array3[5] = array5[0] * n32 + array7[0] * array3[8];
            array3[8] = -array7[0] * n32 + array5[0] * array3[8];
            final double n33 = array4[1];
            array4[1] = array6[0] * n33 + array8[0] * array4[2];
            array4[2] = -array8[0] * n33 + array6[0] * array4[2];
            final double n34 = array4[4];
            array4[4] = array6[0] * n34 + array8[0] * array4[5];
            array4[5] = -array8[0] * n34 + array6[0] * array4[5];
            final double n35 = array4[7];
            array4[7] = array6[0] * n35 + array8[0] * array4[8];
            array4[8] = -array8[0] * n35 + array6[0] * array4[8];
        }
        return 0;
    }
    
    static double max(final double n, final double n2) {
        if (n > n2) {
            return n;
        }
        return n2;
    }
    
    static double min(final double n, final double n2) {
        if (n < n2) {
            return n;
        }
        return n2;
    }
    
    static double d_sign(final double n, final double n2) {
        final double n3 = (n >= 0.0) ? n : (-n);
        return (n2 >= 0.0) ? n3 : (-n3);
    }
    
    static double compute_shift(final double a, final double a2, final double a3) {
        final double abs = Math.abs(a);
        final double abs2 = Math.abs(a2);
        final double abs3 = Math.abs(a3);
        final double min = min(abs, abs3);
        final double max = max(abs, abs3);
        double n;
        if (min == 0.0) {
            n = 0.0;
            if (max != 0.0) {
                final double n2 = min(max, abs2) / max(max, abs2);
            }
        }
        else if (abs2 < max) {
            final double n3 = min / max + 1.0;
            final double n4 = (max - min) / max;
            final double n5 = abs2 / max;
            final double n6 = n5 * n5;
            n = min * (2.0 / (Math.sqrt(n3 * n3 + n6) + Math.sqrt(n4 * n4 + n6)));
        }
        else {
            final double n7 = max / abs2;
            if (n7 == 0.0) {
                n = min * max / abs2;
            }
            else {
                final double n8 = min / max + 1.0;
                final double n9 = (max - min) / max;
                final double n10 = n8 * n7;
                final double n11 = n9 * n7;
                final double n12 = min * (1.0 / (Math.sqrt(n10 * n10 + 1.0) + Math.sqrt(n11 * n11 + 1.0))) * n7;
                n = n12 + n12;
            }
        }
        return n;
    }
    
    static int compute_2X2(final double n, final double a, final double a2, final double[] array, final double[] array2, final double[] array3, final double[] array4, final double[] array5, final int n2) {
        final double n3 = 2.0;
        final double n4 = 1.0;
        double n5 = array[0];
        double n6 = array[1];
        double n7 = 0.0;
        double n8 = 0.0;
        double n9 = 0.0;
        double n10 = 0.0;
        double n11 = 0.0;
        double a3 = n;
        double abs = Math.abs(a3);
        double n12 = a2;
        double abs2 = Math.abs(a2);
        int n13 = 1;
        final boolean b = abs2 > abs;
        if (b) {
            n13 = 3;
            final double n14 = a3;
            a3 = n12;
            n12 = n14;
            final double n15 = abs;
            abs = abs2;
            abs2 = n15;
        }
        final double abs3 = Math.abs(a);
        if (abs3 == 0.0) {
            array[1] = abs2;
            array[0] = abs;
        }
        else {
            int n16 = 1;
            if (abs3 > abs) {
                n13 = 2;
                if (abs / abs3 < 1.110223024E-16) {
                    n16 = 0;
                    n5 = abs3;
                    if (abs2 > 1.0) {
                        n6 = abs / (abs3 / abs2);
                    }
                    else {
                        n6 = abs / abs3 * abs2;
                    }
                    n7 = 1.0;
                    n9 = n12 / a;
                    n10 = 1.0;
                    n8 = a3 / a;
                }
            }
            if (n16 != 0) {
                final double n17 = abs - abs2;
                double n18;
                if (n17 == abs) {
                    n18 = 1.0;
                }
                else {
                    n18 = n17 / abs;
                }
                final double a4 = a / a3;
                final double n19 = 2.0 - n18;
                final double n20 = a4 * a4;
                Math.sqrt(n19 * n19 + n20);
                if (n18 == 0.0) {
                    Math.abs(a4);
                }
                else {
                    Math.sqrt(n18 * n18 + n20);
                }
                if (abs3 > abs) {
                    n13 = 2;
                    if (abs / abs3 < 1.110223024E-16) {
                        n16 = 0;
                        n5 = abs3;
                        if (abs2 > 1.0) {
                            n6 = abs / (abs3 / abs2);
                        }
                        else {
                            n6 = abs / abs3 * abs2;
                        }
                        n7 = 1.0;
                        n9 = n12 / a;
                        n10 = 1.0;
                        n8 = a3 / a;
                    }
                }
                if (n16 != 0) {
                    final double n21 = abs - abs2;
                    double n22;
                    if (n21 == abs) {
                        n22 = 1.0;
                    }
                    else {
                        n22 = n21 / abs;
                    }
                    final double a5 = a / a3;
                    final double n23 = 2.0 - n22;
                    final double n24 = a5 * a5;
                    final double sqrt = Math.sqrt(n23 * n23 + n24);
                    double n25;
                    if (n22 == 0.0) {
                        n25 = Math.abs(a5);
                    }
                    else {
                        n25 = Math.sqrt(n22 * n22 + n24);
                    }
                    final double n26 = (sqrt + n25) * 0.5;
                    n6 = abs2 / n26;
                    n5 = abs * n26;
                    double n27;
                    if (n24 == 0.0) {
                        if (n22 == 0.0) {
                            n27 = d_sign(n3, a3) * d_sign(n4, a);
                        }
                        else {
                            n27 = a / d_sign(n21, a3) + a5 / n23;
                        }
                    }
                    else {
                        n27 = (a5 / (sqrt + n23) + a5 / (n25 + n22)) * (n26 + 1.0);
                    }
                    final double sqrt2 = Math.sqrt(n27 * n27 + 4.0);
                    n8 = 2.0 / sqrt2;
                    n10 = n27 / sqrt2;
                    n7 = (n8 + n10 * a5) / n26;
                    n9 = n12 / a3 * n10 / n26;
                }
            }
            if (b) {
                array3[0] = n10;
                array2[0] = n8;
                array5[0] = n9;
                array4[0] = n7;
            }
            else {
                array3[0] = n7;
                array2[0] = n9;
                array5[0] = n8;
                array4[0] = n10;
            }
            if (n13 == 1) {
                n11 = d_sign(n4, array5[0]) * d_sign(n4, array3[0]) * d_sign(n4, n);
            }
            if (n13 == 2) {
                n11 = d_sign(n4, array4[0]) * d_sign(n4, array3[0]) * d_sign(n4, a);
            }
            if (n13 == 3) {
                n11 = d_sign(n4, array4[0]) * d_sign(n4, array2[0]) * d_sign(n4, a2);
            }
            array[n2] = d_sign(n5, n11);
            array[n2 + 1] = d_sign(n6, n11 * d_sign(n4, n) * d_sign(n4, a2));
        }
        return 0;
    }
    
    static double compute_rot(final double a, final double a2, final double[] array, final double[] array2, final int n, final int n2) {
        double n3;
        double n4;
        double n5;
        if (a2 == 0.0) {
            n3 = 1.0;
            n4 = 0.0;
            n5 = a;
        }
        else if (a == 0.0) {
            n3 = 0.0;
            n4 = 1.0;
            n5 = a2;
        }
        else {
            double a3 = a;
            double a4 = a2;
            double n6 = max(Math.abs(a3), Math.abs(a4));
            if (n6 >= 4.9947976805055876E145) {
                int n7 = 0;
                while (n6 >= 4.9947976805055876E145) {
                    ++n7;
                    a3 *= 2.002083095183101E-146;
                    a4 *= 2.002083095183101E-146;
                    n6 = max(Math.abs(a3), Math.abs(a4));
                }
                n5 = Math.sqrt(a3 * a3 + a4 * a4);
                n3 = a3 / n5;
                n4 = a4 / n5;
                for (int i = 1; i <= n7; ++i) {
                    n5 *= 4.9947976805055876E145;
                }
            }
            else if (n6 <= 2.002083095183101E-146) {
                int n8 = 0;
                while (n6 <= 2.002083095183101E-146) {
                    ++n8;
                    a3 *= 4.9947976805055876E145;
                    a4 *= 4.9947976805055876E145;
                    n6 = max(Math.abs(a3), Math.abs(a4));
                }
                n5 = Math.sqrt(a3 * a3 + a4 * a4);
                n3 = a3 / n5;
                n4 = a4 / n5;
                for (int j = 1; j <= n8; ++j) {
                    n5 *= 2.002083095183101E-146;
                }
            }
            else {
                n5 = Math.sqrt(a3 * a3 + a4 * a4);
                n3 = a3 / n5;
                n4 = a4 / n5;
            }
            if (Math.abs(a) > Math.abs(a2) && n3 < 0.0) {
                n3 = -n3;
                n4 = -n4;
                n5 = -n5;
            }
        }
        array[n] = n4;
        array2[n] = n3;
        return n5;
    }
    
    static void print_mat(final double[] array) {
        for (int i = 0; i < 3; ++i) {
            System.out.println(array[i * 3 + 0] + " " + array[i * 3 + 1] + " " + array[i * 3 + 2] + "\n");
        }
    }
    
    static void print_det(final double[] array) {
        System.out.println("det= " + (array[0] * array[4] * array[8] + array[1] * array[5] * array[6] + array[2] * array[3] * array[7] - array[2] * array[4] * array[6] - array[0] * array[5] * array[7] - array[1] * array[3] * array[8]));
    }
    
    static void mat_mul(final double[] array, final double[] array2, final double[] array3) {
        final double[] array4 = { array[0] * array2[0] + array[1] * array2[3] + array[2] * array2[6], array[0] * array2[1] + array[1] * array2[4] + array[2] * array2[7], array[0] * array2[2] + array[1] * array2[5] + array[2] * array2[8], array[3] * array2[0] + array[4] * array2[3] + array[5] * array2[6], array[3] * array2[1] + array[4] * array2[4] + array[5] * array2[7], array[3] * array2[2] + array[4] * array2[5] + array[5] * array2[8], array[6] * array2[0] + array[7] * array2[3] + array[8] * array2[6], array[6] * array2[1] + array[7] * array2[4] + array[8] * array2[7], array[6] * array2[2] + array[7] * array2[5] + array[8] * array2[8] };
        for (int i = 0; i < 9; ++i) {
            array3[i] = array4[i];
        }
    }
    
    static void transpose_mat(final double[] array, final double[] array2) {
        array2[0] = array[0];
        array2[1] = array[3];
        array2[2] = array[6];
        array2[3] = array[1];
        array2[4] = array[4];
        array2[5] = array[7];
        array2[6] = array[2];
        array2[7] = array[5];
        array2[8] = array[8];
    }
    
    static double max3(final double[] array) {
        if (array[0] > array[1]) {
            if (array[0] > array[2]) {
                return array[0];
            }
            return array[2];
        }
        else {
            if (array[1] > array[2]) {
                return array[1];
            }
            return array[2];
        }
    }
    
    private static final boolean almostEqual(final double a, final double a2) {
        if (a == a2) {
            return true;
        }
        final double abs = Math.abs(a - a2);
        final double abs2 = Math.abs(a);
        final double abs3 = Math.abs(a2);
        final double n = (abs2 >= abs3) ? abs2 : abs3;
        return abs < 1.0E-6 || abs / n < 1.0E-4;
    }
    
    public Object clone() {
        Matrix3d matrix3d;
        try {
            matrix3d = (Matrix3d)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        return matrix3d;
    }
}
