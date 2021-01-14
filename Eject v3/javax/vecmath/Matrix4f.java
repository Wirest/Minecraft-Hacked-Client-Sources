package javax.vecmath;

import java.io.Serializable;

public class Matrix4f
        implements Serializable, Cloneable {
    static final long serialVersionUID = -8405036035410109353L;
    private static final double EPS = 1.0E-8D;
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

    public Matrix4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, float paramFloat15, float paramFloat16) {
        this.m00 = paramFloat1;
        this.m01 = paramFloat2;
        this.m02 = paramFloat3;
        this.m03 = paramFloat4;
        this.m10 = paramFloat5;
        this.m11 = paramFloat6;
        this.m12 = paramFloat7;
        this.m13 = paramFloat8;
        this.m20 = paramFloat9;
        this.m21 = paramFloat10;
        this.m22 = paramFloat11;
        this.m23 = paramFloat12;
        this.m30 = paramFloat13;
        this.m31 = paramFloat14;
        this.m32 = paramFloat15;
        this.m33 = paramFloat16;
    }

    public Matrix4f(float[] paramArrayOfFloat) {
        this.m00 = paramArrayOfFloat[0];
        this.m01 = paramArrayOfFloat[1];
        this.m02 = paramArrayOfFloat[2];
        this.m03 = paramArrayOfFloat[3];
        this.m10 = paramArrayOfFloat[4];
        this.m11 = paramArrayOfFloat[5];
        this.m12 = paramArrayOfFloat[6];
        this.m13 = paramArrayOfFloat[7];
        this.m20 = paramArrayOfFloat[8];
        this.m21 = paramArrayOfFloat[9];
        this.m22 = paramArrayOfFloat[10];
        this.m23 = paramArrayOfFloat[11];
        this.m30 = paramArrayOfFloat[12];
        this.m31 = paramArrayOfFloat[13];
        this.m32 = paramArrayOfFloat[14];
        this.m33 = paramArrayOfFloat[15];
    }

    public Matrix4f(Quat4f paramQuat4f, Vector3f paramVector3f, float paramFloat) {
        this.m00 = ((float) (paramFloat * (1.0D - 2.0D * paramQuat4f.y * paramQuat4f.y - 2.0D * paramQuat4f.z * paramQuat4f.z)));
        this.m10 = ((float) (paramFloat * (2.0D * (paramQuat4f.x * paramQuat4f.y + paramQuat4f.w * paramQuat4f.z))));
        this.m20 = ((float) (paramFloat * (2.0D * (paramQuat4f.x * paramQuat4f.z - paramQuat4f.w * paramQuat4f.y))));
        this.m01 = ((float) (paramFloat * (2.0D * (paramQuat4f.x * paramQuat4f.y - paramQuat4f.w * paramQuat4f.z))));
        this.m11 = ((float) (paramFloat * (1.0D - 2.0D * paramQuat4f.x * paramQuat4f.x - 2.0D * paramQuat4f.z * paramQuat4f.z)));
        this.m21 = ((float) (paramFloat * (2.0D * (paramQuat4f.y * paramQuat4f.z + paramQuat4f.w * paramQuat4f.x))));
        this.m02 = ((float) (paramFloat * (2.0D * (paramQuat4f.x * paramQuat4f.z + paramQuat4f.w * paramQuat4f.y))));
        this.m12 = ((float) (paramFloat * (2.0D * (paramQuat4f.y * paramQuat4f.z - paramQuat4f.w * paramQuat4f.x))));
        this.m22 = ((float) (paramFloat * (1.0D - 2.0D * paramQuat4f.x * paramQuat4f.x - 2.0D * paramQuat4f.y * paramQuat4f.y)));
        this.m03 = paramVector3f.x;
        this.m13 = paramVector3f.y;
        this.m23 = paramVector3f.z;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public Matrix4f(Matrix4d paramMatrix4d) {
        this.m00 = ((float) paramMatrix4d.m00);
        this.m01 = ((float) paramMatrix4d.m01);
        this.m02 = ((float) paramMatrix4d.m02);
        this.m03 = ((float) paramMatrix4d.m03);
        this.m10 = ((float) paramMatrix4d.m10);
        this.m11 = ((float) paramMatrix4d.m11);
        this.m12 = ((float) paramMatrix4d.m12);
        this.m13 = ((float) paramMatrix4d.m13);
        this.m20 = ((float) paramMatrix4d.m20);
        this.m21 = ((float) paramMatrix4d.m21);
        this.m22 = ((float) paramMatrix4d.m22);
        this.m23 = ((float) paramMatrix4d.m23);
        this.m30 = ((float) paramMatrix4d.m30);
        this.m31 = ((float) paramMatrix4d.m31);
        this.m32 = ((float) paramMatrix4d.m32);
        this.m33 = ((float) paramMatrix4d.m33);
    }

    public Matrix4f(Matrix4f paramMatrix4f) {
        this.m00 = paramMatrix4f.m00;
        this.m01 = paramMatrix4f.m01;
        this.m02 = paramMatrix4f.m02;
        this.m03 = paramMatrix4f.m03;
        this.m10 = paramMatrix4f.m10;
        this.m11 = paramMatrix4f.m11;
        this.m12 = paramMatrix4f.m12;
        this.m13 = paramMatrix4f.m13;
        this.m20 = paramMatrix4f.m20;
        this.m21 = paramMatrix4f.m21;
        this.m22 = paramMatrix4f.m22;
        this.m23 = paramMatrix4f.m23;
        this.m30 = paramMatrix4f.m30;
        this.m31 = paramMatrix4f.m31;
        this.m32 = paramMatrix4f.m32;
        this.m33 = paramMatrix4f.m33;
    }

    public Matrix4f(Matrix3f paramMatrix3f, Vector3f paramVector3f, float paramFloat) {
        this.m00 = (paramMatrix3f.m00 * paramFloat);
        this.m01 = (paramMatrix3f.m01 * paramFloat);
        this.m02 = (paramMatrix3f.m02 * paramFloat);
        this.m03 = paramVector3f.x;
        this.m10 = (paramMatrix3f.m10 * paramFloat);
        this.m11 = (paramMatrix3f.m11 * paramFloat);
        this.m12 = (paramMatrix3f.m12 * paramFloat);
        this.m13 = paramVector3f.y;
        this.m20 = (paramMatrix3f.m20 * paramFloat);
        this.m21 = (paramMatrix3f.m21 * paramFloat);
        this.m22 = (paramMatrix3f.m22 * paramFloat);
        this.m23 = paramVector3f.z;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public Matrix4f() {
        this.m00 = 0.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = 0.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 0.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 0.0F;
    }

    static boolean luDecomposition(double[] paramArrayOfDouble, int[] paramArrayOfInt) {
        double[] arrayOfDouble = new double[4];
        double d2 = 0;
        int j = 0;
        double d1 = 4;
        double d3;
        while (d1-- != 0) {
            d3 = 0.0D;
            i = 4;
            while (i-- != 0) {
                double d4 = paramArrayOfDouble[(d2++)];
                d4 = Math.abs(d4);
                if (d4 > d3) {
                    d3 = d4;
                }
            }
            if (d3 == 0.0D) {
                return false;
            }
            arrayOfDouble[(j++)] = (1.0D / d3);
        }
        int i = 0;
        for (d1 = 0; d1 < 4; d1++) {
            int m;
            double d5;
            int n;
            int i1;
            int k;
            for (d2 = 0; d2 < d1; d2++) {
                m = i | 4 * d2 | d1;
                d5 = paramArrayOfDouble[m];
                d3 = d2;
                n = i | 4 * d2;
                for (i1 = i | d1; d3-- != 0; i1 += 4) {
                    d5 -= paramArrayOfDouble[n] * paramArrayOfDouble[i1];
                    n++;
                }
                paramArrayOfDouble[m] = d5;
            }
            double d6 = 0.0D;
            j = -1;
            double d7;
            for (d2 = d1; d2 < 4; d2++) {
                m = i | 4 * d2 | d1;
                d5 = paramArrayOfDouble[m];
                k = d1;
                n = i | 4 * d2;
                for (i1 = i | d1; k-- != 0; i1 += 4) {
                    d5 -= paramArrayOfDouble[n] * paramArrayOfDouble[i1];
                    n++;
                }
                paramArrayOfDouble[m] = d5;
                if ((d7 = arrayOfDouble[d2] * Math.abs(d5)) >= d6) {
                    d6 = d7;
                    j = d2;
                }
            }
            if (j < 0) {
                throw new RuntimeException(VecMathI18N.getString("Matrix4f13"));
            }
            if (d1 != j) {
                k = 4;
                n = i | 4 * j;
                i1 = i | 4 * d1;
                while (k-- != 0) {
                    d7 = paramArrayOfDouble[n];
                    paramArrayOfDouble[(n++)] = paramArrayOfDouble[i1];
                    paramArrayOfDouble[(i1++)] = d7;
                }
                arrayOfDouble[j] = arrayOfDouble[d1];
            }
            paramArrayOfInt[d1] = j;
            if (paramArrayOfDouble[(i | 4 * d1 | d1)] == 0.0D) {
                return false;
            }
            if (d1 != 3) {
                d7 = 1.0D / paramArrayOfDouble[(i | 4 * d1 | d1)];
                m = i | 4 * (d1 | 0x1) | d1;
                d2 = 3 - d1;
                while (d2-- != 0) {
                    paramArrayOfDouble[m] *= d7;
                    m += 4;
                }
            }
        }
        return true;
    }

    static void luBacksubstitution(double[] paramArrayOfDouble1, int[] paramArrayOfInt, double[] paramArrayOfDouble2) {
        int i1 = 0;
        for (int n = 0; n < 4; n++) {
            int i2 = n;
            int j = -1;
            for (int i = 0; i < 4; i++) {
                int k = paramArrayOfInt[(i1 | i)];
                double d = paramArrayOfDouble2[(i2 | 4 * k)];
                paramArrayOfDouble2[(i2 | 4 * k)] = paramArrayOfDouble2[(i2 | 4 * i)];
                if (j >= 0) {
                    i3 = i * 4;
                    for (int m = j; m <= i - 1; m++) {
                        d -= paramArrayOfDouble1[(i3 | m)] * paramArrayOfDouble2[(i2 | 4 * m)];
                    }
                }
                if (d != 0.0D) {
                    j = i;
                }
                paramArrayOfDouble2[(i2 | 4 * i)] = d;
            }
            int i3 = 12;
            paramArrayOfDouble2[(i2 | 0xC)] /= paramArrayOfDouble1[(i3 | 0x3)];
            i3 -= 4;
            paramArrayOfDouble2[(i2 | 0x8)] = ((paramArrayOfDouble2[(i2 | 0x8)] - paramArrayOfDouble1[(i3 | 0x3)] * paramArrayOfDouble2[(i2 | 0xC)]) / paramArrayOfDouble1[(i3 | 0x2)]);
            i3 -= 4;
            paramArrayOfDouble2[(i2 | 0x4)] = ((paramArrayOfDouble2[(i2 | 0x4)] - paramArrayOfDouble1[(i3 | 0x2)] * paramArrayOfDouble2[(i2 | 0x8)] - paramArrayOfDouble1[(i3 | 0x3)] * paramArrayOfDouble2[(i2 | 0xC)]) / paramArrayOfDouble1[(i3 | 0x1)]);
            i3 -= 4;
            paramArrayOfDouble2[(i2 | 0x0)] = ((paramArrayOfDouble2[(i2 | 0x0)] - paramArrayOfDouble1[(i3 | 0x1)] * paramArrayOfDouble2[(i2 | 0x4)] - paramArrayOfDouble1[(i3 | 0x2)] * paramArrayOfDouble2[(i2 | 0x8)] - paramArrayOfDouble1[(i3 | 0x3)] * paramArrayOfDouble2[(i2 | 0xC)]) / paramArrayOfDouble1[(i3 | 0x0)]);
        }
    }

    public String toString() {
        return this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" + this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" + this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" + this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
    }

    public final void setIdentity() {
        this.m00 = 1.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void setElement(int paramInt1, int paramInt2, float paramFloat) {
        switch (paramInt1) {
            case 0:
                switch (paramInt2) {
                    case 0:
                        this.m00 = paramFloat;
                        break;
                    case 1:
                        this.m01 = paramFloat;
                        break;
                    case 2:
                        this.m02 = paramFloat;
                        break;
                    case 3:
                        this.m03 = paramFloat;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;
            case 1:
                switch (paramInt2) {
                    case 0:
                        this.m10 = paramFloat;
                        break;
                    case 1:
                        this.m11 = paramFloat;
                        break;
                    case 2:
                        this.m12 = paramFloat;
                        break;
                    case 3:
                        this.m13 = paramFloat;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;
            case 2:
                switch (paramInt2) {
                    case 0:
                        this.m20 = paramFloat;
                        break;
                    case 1:
                        this.m21 = paramFloat;
                        break;
                    case 2:
                        this.m22 = paramFloat;
                        break;
                    case 3:
                        this.m23 = paramFloat;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;
            case 3:
                switch (paramInt2) {
                    case 0:
                        this.m30 = paramFloat;
                        break;
                    case 1:
                        this.m31 = paramFloat;
                        break;
                    case 2:
                        this.m32 = paramFloat;
                        break;
                    case 3:
                        this.m33 = paramFloat;
                        break;
                    default:
                        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
                }
                break;
            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f0"));
        }
    }

    public final float getElement(int paramInt1, int paramInt2) {
        switch (paramInt1) {
            case 0:
                switch (paramInt2) {
                    case 0:
                        return this.m00;
                    case 1:
                        return this.m01;
                    case 2:
                        return this.m02;
                    case 3:
                        return this.m03;
                }
                break;
            case 1:
                switch (paramInt2) {
                    case 0:
                        return this.m10;
                    case 1:
                        return this.m11;
                    case 2:
                        return this.m12;
                    case 3:
                        return this.m13;
                }
                break;
            case 2:
                switch (paramInt2) {
                    case 0:
                        return this.m20;
                    case 1:
                        return this.m21;
                    case 2:
                        return this.m22;
                    case 3:
                        return this.m23;
                }
                break;
            case 3:
                switch (paramInt2) {
                    case 0:
                        return this.m30;
                    case 1:
                        return this.m31;
                    case 2:
                        return this.m32;
                    case 3:
                        return this.m33;
                }
                break;
        }
        throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f1"));
    }

    public final void getRow(int paramInt, Vector4f paramVector4f) {
        if (paramInt == 0) {
            paramVector4f.x = this.m00;
            paramVector4f.y = this.m01;
            paramVector4f.z = this.m02;
            paramVector4f.w = this.m03;
        } else if (paramInt == 1) {
            paramVector4f.x = this.m10;
            paramVector4f.y = this.m11;
            paramVector4f.z = this.m12;
            paramVector4f.w = this.m13;
        } else if (paramInt == 2) {
            paramVector4f.x = this.m20;
            paramVector4f.y = this.m21;
            paramVector4f.z = this.m22;
            paramVector4f.w = this.m23;
        } else if (paramInt == 3) {
            paramVector4f.x = this.m30;
            paramVector4f.y = this.m31;
            paramVector4f.z = this.m32;
            paramVector4f.w = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
        }
    }

    public final void getRow(int paramInt, float[] paramArrayOfFloat) {
        if (paramInt == 0) {
            paramArrayOfFloat[0] = this.m00;
            paramArrayOfFloat[1] = this.m01;
            paramArrayOfFloat[2] = this.m02;
            paramArrayOfFloat[3] = this.m03;
        } else if (paramInt == 1) {
            paramArrayOfFloat[0] = this.m10;
            paramArrayOfFloat[1] = this.m11;
            paramArrayOfFloat[2] = this.m12;
            paramArrayOfFloat[3] = this.m13;
        } else if (paramInt == 2) {
            paramArrayOfFloat[0] = this.m20;
            paramArrayOfFloat[1] = this.m21;
            paramArrayOfFloat[2] = this.m22;
            paramArrayOfFloat[3] = this.m23;
        } else if (paramInt == 3) {
            paramArrayOfFloat[0] = this.m30;
            paramArrayOfFloat[1] = this.m31;
            paramArrayOfFloat[2] = this.m32;
            paramArrayOfFloat[3] = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f2"));
        }
    }

    public final void getColumn(int paramInt, Vector4f paramVector4f) {
        if (paramInt == 0) {
            paramVector4f.x = this.m00;
            paramVector4f.y = this.m10;
            paramVector4f.z = this.m20;
            paramVector4f.w = this.m30;
        } else if (paramInt == 1) {
            paramVector4f.x = this.m01;
            paramVector4f.y = this.m11;
            paramVector4f.z = this.m21;
            paramVector4f.w = this.m31;
        } else if (paramInt == 2) {
            paramVector4f.x = this.m02;
            paramVector4f.y = this.m12;
            paramVector4f.z = this.m22;
            paramVector4f.w = this.m32;
        } else if (paramInt == 3) {
            paramVector4f.x = this.m03;
            paramVector4f.y = this.m13;
            paramVector4f.z = this.m23;
            paramVector4f.w = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
        }
    }

    public final void getColumn(int paramInt, float[] paramArrayOfFloat) {
        if (paramInt == 0) {
            paramArrayOfFloat[0] = this.m00;
            paramArrayOfFloat[1] = this.m10;
            paramArrayOfFloat[2] = this.m20;
            paramArrayOfFloat[3] = this.m30;
        } else if (paramInt == 1) {
            paramArrayOfFloat[0] = this.m01;
            paramArrayOfFloat[1] = this.m11;
            paramArrayOfFloat[2] = this.m21;
            paramArrayOfFloat[3] = this.m31;
        } else if (paramInt == 2) {
            paramArrayOfFloat[0] = this.m02;
            paramArrayOfFloat[1] = this.m12;
            paramArrayOfFloat[2] = this.m22;
            paramArrayOfFloat[3] = this.m32;
        } else if (paramInt == 3) {
            paramArrayOfFloat[0] = this.m03;
            paramArrayOfFloat[1] = this.m13;
            paramArrayOfFloat[2] = this.m23;
            paramArrayOfFloat[3] = this.m33;
        } else {
            throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f4"));
        }
    }

    public final void get(Matrix3d paramMatrix3d) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        paramMatrix3d.m00 = arrayOfDouble1[0];
        paramMatrix3d.m01 = arrayOfDouble1[1];
        paramMatrix3d.m02 = arrayOfDouble1[2];
        paramMatrix3d.m10 = arrayOfDouble1[3];
        paramMatrix3d.m11 = arrayOfDouble1[4];
        paramMatrix3d.m12 = arrayOfDouble1[5];
        paramMatrix3d.m20 = arrayOfDouble1[6];
        paramMatrix3d.m21 = arrayOfDouble1[7];
        paramMatrix3d.m22 = arrayOfDouble1[8];
    }

    public final void get(Matrix3f paramMatrix3f) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        paramMatrix3f.m00 = ((float) arrayOfDouble1[0]);
        paramMatrix3f.m01 = ((float) arrayOfDouble1[1]);
        paramMatrix3f.m02 = ((float) arrayOfDouble1[2]);
        paramMatrix3f.m10 = ((float) arrayOfDouble1[3]);
        paramMatrix3f.m11 = ((float) arrayOfDouble1[4]);
        paramMatrix3f.m12 = ((float) arrayOfDouble1[5]);
        paramMatrix3f.m20 = ((float) arrayOfDouble1[6]);
        paramMatrix3f.m21 = ((float) arrayOfDouble1[7]);
        paramMatrix3f.m22 = ((float) arrayOfDouble1[8]);
    }

    public final float get(Matrix3f paramMatrix3f, Vector3f paramVector3f) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        paramMatrix3f.m00 = ((float) arrayOfDouble1[0]);
        paramMatrix3f.m01 = ((float) arrayOfDouble1[1]);
        paramMatrix3f.m02 = ((float) arrayOfDouble1[2]);
        paramMatrix3f.m10 = ((float) arrayOfDouble1[3]);
        paramMatrix3f.m11 = ((float) arrayOfDouble1[4]);
        paramMatrix3f.m12 = ((float) arrayOfDouble1[5]);
        paramMatrix3f.m20 = ((float) arrayOfDouble1[6]);
        paramMatrix3f.m21 = ((float) arrayOfDouble1[7]);
        paramMatrix3f.m22 = ((float) arrayOfDouble1[8]);
        paramVector3f.x = this.m03;
        paramVector3f.y = this.m13;
        paramVector3f.z = this.m23;
        return (float) Matrix3d.max3(arrayOfDouble2);
    }

    public final void get(Quat4f paramQuat4f) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        double d = 0.25D * (1.0D + arrayOfDouble1[0] + arrayOfDouble1[4] + arrayOfDouble1[8]);
        if ((d < 0.0D ? -d : d) >= 1.0E-30D) {
            paramQuat4f.w = ((float) Math.sqrt(d));
            d = 0.25D / paramQuat4f.w;
            paramQuat4f.x = ((float) ((arrayOfDouble1[7] - arrayOfDouble1[5]) * d));
            paramQuat4f.y = ((float) ((arrayOfDouble1[2] - arrayOfDouble1[6]) * d));
            paramQuat4f.z = ((float) ((arrayOfDouble1[3] - arrayOfDouble1[1]) * d));
            return;
        }
        paramQuat4f.w = 0.0F;
        d = -0.5D * (arrayOfDouble1[4] + arrayOfDouble1[8]);
        if ((d < 0.0D ? -d : d) >= 1.0E-30D) {
            paramQuat4f.x = ((float) Math.sqrt(d));
            d = 0.5D / paramQuat4f.x;
            paramQuat4f.y = ((float) (arrayOfDouble1[3] * d));
            paramQuat4f.z = ((float) (arrayOfDouble1[6] * d));
            return;
        }
        paramQuat4f.x = 0.0F;
        d = 0.5D * (1.0D - arrayOfDouble1[8]);
        if ((d < 0.0D ? -d : d) >= 1.0E-30D) {
            paramQuat4f.y = ((float) Math.sqrt(d));
            paramQuat4f.z = ((float) (arrayOfDouble1[7] / (2.0D * paramQuat4f.y)));
            return;
        }
        paramQuat4f.y = 0.0F;
        paramQuat4f.z = 1.0F;
    }

    public final void get(Vector3f paramVector3f) {
        paramVector3f.x = this.m03;
        paramVector3f.y = this.m13;
        paramVector3f.z = this.m23;
    }

    public final void getRotationScale(Matrix3f paramMatrix3f) {
        paramMatrix3f.m00 = this.m00;
        paramMatrix3f.m01 = this.m01;
        paramMatrix3f.m02 = this.m02;
        paramMatrix3f.m10 = this.m10;
        paramMatrix3f.m11 = this.m11;
        paramMatrix3f.m12 = this.m12;
        paramMatrix3f.m20 = this.m20;
        paramMatrix3f.m21 = this.m21;
        paramMatrix3f.m22 = this.m22;
    }

    public final float getScale() {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        return (float) Matrix3d.max3(arrayOfDouble2);
    }

    public final void setScale(float paramFloat) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        this.m00 = ((float) (arrayOfDouble1[0] * paramFloat));
        this.m01 = ((float) (arrayOfDouble1[1] * paramFloat));
        this.m02 = ((float) (arrayOfDouble1[2] * paramFloat));
        this.m10 = ((float) (arrayOfDouble1[3] * paramFloat));
        this.m11 = ((float) (arrayOfDouble1[4] * paramFloat));
        this.m12 = ((float) (arrayOfDouble1[5] * paramFloat));
        this.m20 = ((float) (arrayOfDouble1[6] * paramFloat));
        this.m21 = ((float) (arrayOfDouble1[7] * paramFloat));
        this.m22 = ((float) (arrayOfDouble1[8] * paramFloat));
    }

    public final void setRotationScale(Matrix3f paramMatrix3f) {
        this.m00 = paramMatrix3f.m00;
        this.m01 = paramMatrix3f.m01;
        this.m02 = paramMatrix3f.m02;
        this.m10 = paramMatrix3f.m10;
        this.m11 = paramMatrix3f.m11;
        this.m12 = paramMatrix3f.m12;
        this.m20 = paramMatrix3f.m20;
        this.m21 = paramMatrix3f.m21;
        this.m22 = paramMatrix3f.m22;
    }

    public final void setRow(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        switch (paramInt) {
            case 0:
                this.m00 = paramFloat1;
                this.m01 = paramFloat2;
                this.m02 = paramFloat3;
                this.m03 = paramFloat4;
                break;
            case 1:
                this.m10 = paramFloat1;
                this.m11 = paramFloat2;
                this.m12 = paramFloat3;
                this.m13 = paramFloat4;
                break;
            case 2:
                this.m20 = paramFloat1;
                this.m21 = paramFloat2;
                this.m22 = paramFloat3;
                this.m23 = paramFloat4;
                break;
            case 3:
                this.m30 = paramFloat1;
                this.m31 = paramFloat2;
                this.m32 = paramFloat3;
                this.m33 = paramFloat4;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
        }
    }

    public final void setRow(int paramInt, Vector4f paramVector4f) {
        switch (paramInt) {
            case 0:
                this.m00 = paramVector4f.x;
                this.m01 = paramVector4f.y;
                this.m02 = paramVector4f.z;
                this.m03 = paramVector4f.w;
                break;
            case 1:
                this.m10 = paramVector4f.x;
                this.m11 = paramVector4f.y;
                this.m12 = paramVector4f.z;
                this.m13 = paramVector4f.w;
                break;
            case 2:
                this.m20 = paramVector4f.x;
                this.m21 = paramVector4f.y;
                this.m22 = paramVector4f.z;
                this.m23 = paramVector4f.w;
                break;
            case 3:
                this.m30 = paramVector4f.x;
                this.m31 = paramVector4f.y;
                this.m32 = paramVector4f.z;
                this.m33 = paramVector4f.w;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
        }
    }

    public final void setRow(int paramInt, float[] paramArrayOfFloat) {
        switch (paramInt) {
            case 0:
                this.m00 = paramArrayOfFloat[0];
                this.m01 = paramArrayOfFloat[1];
                this.m02 = paramArrayOfFloat[2];
                this.m03 = paramArrayOfFloat[3];
                break;
            case 1:
                this.m10 = paramArrayOfFloat[0];
                this.m11 = paramArrayOfFloat[1];
                this.m12 = paramArrayOfFloat[2];
                this.m13 = paramArrayOfFloat[3];
                break;
            case 2:
                this.m20 = paramArrayOfFloat[0];
                this.m21 = paramArrayOfFloat[1];
                this.m22 = paramArrayOfFloat[2];
                this.m23 = paramArrayOfFloat[3];
                break;
            case 3:
                this.m30 = paramArrayOfFloat[0];
                this.m31 = paramArrayOfFloat[1];
                this.m32 = paramArrayOfFloat[2];
                this.m33 = paramArrayOfFloat[3];
                break;
            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f6"));
        }
    }

    public final void setColumn(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        switch (paramInt) {
            case 0:
                this.m00 = paramFloat1;
                this.m10 = paramFloat2;
                this.m20 = paramFloat3;
                this.m30 = paramFloat4;
                break;
            case 1:
                this.m01 = paramFloat1;
                this.m11 = paramFloat2;
                this.m21 = paramFloat3;
                this.m31 = paramFloat4;
                break;
            case 2:
                this.m02 = paramFloat1;
                this.m12 = paramFloat2;
                this.m22 = paramFloat3;
                this.m32 = paramFloat4;
                break;
            case 3:
                this.m03 = paramFloat1;
                this.m13 = paramFloat2;
                this.m23 = paramFloat3;
                this.m33 = paramFloat4;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
        }
    }

    public final void setColumn(int paramInt, Vector4f paramVector4f) {
        switch (paramInt) {
            case 0:
                this.m00 = paramVector4f.x;
                this.m10 = paramVector4f.y;
                this.m20 = paramVector4f.z;
                this.m30 = paramVector4f.w;
                break;
            case 1:
                this.m01 = paramVector4f.x;
                this.m11 = paramVector4f.y;
                this.m21 = paramVector4f.z;
                this.m31 = paramVector4f.w;
                break;
            case 2:
                this.m02 = paramVector4f.x;
                this.m12 = paramVector4f.y;
                this.m22 = paramVector4f.z;
                this.m32 = paramVector4f.w;
                break;
            case 3:
                this.m03 = paramVector4f.x;
                this.m13 = paramVector4f.y;
                this.m23 = paramVector4f.z;
                this.m33 = paramVector4f.w;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
        }
    }

    public final void setColumn(int paramInt, float[] paramArrayOfFloat) {
        switch (paramInt) {
            case 0:
                this.m00 = paramArrayOfFloat[0];
                this.m10 = paramArrayOfFloat[1];
                this.m20 = paramArrayOfFloat[2];
                this.m30 = paramArrayOfFloat[3];
                break;
            case 1:
                this.m01 = paramArrayOfFloat[0];
                this.m11 = paramArrayOfFloat[1];
                this.m21 = paramArrayOfFloat[2];
                this.m31 = paramArrayOfFloat[3];
                break;
            case 2:
                this.m02 = paramArrayOfFloat[0];
                this.m12 = paramArrayOfFloat[1];
                this.m22 = paramArrayOfFloat[2];
                this.m32 = paramArrayOfFloat[3];
                break;
            case 3:
                this.m03 = paramArrayOfFloat[0];
                this.m13 = paramArrayOfFloat[1];
                this.m23 = paramArrayOfFloat[2];
                this.m33 = paramArrayOfFloat[3];
                break;
            default:
                throw new ArrayIndexOutOfBoundsException(VecMathI18N.getString("Matrix4f9"));
        }
    }

    public final void add(float paramFloat) {
        this.m00 += paramFloat;
        this.m01 += paramFloat;
        this.m02 += paramFloat;
        this.m03 += paramFloat;
        this.m10 += paramFloat;
        this.m11 += paramFloat;
        this.m12 += paramFloat;
        this.m13 += paramFloat;
        this.m20 += paramFloat;
        this.m21 += paramFloat;
        this.m22 += paramFloat;
        this.m23 += paramFloat;
        this.m30 += paramFloat;
        this.m31 += paramFloat;
        this.m32 += paramFloat;
        this.m33 += paramFloat;
    }

    public final void add(float paramFloat, Matrix4f paramMatrix4f) {
        paramMatrix4f.m00 += paramFloat;
        paramMatrix4f.m01 += paramFloat;
        paramMatrix4f.m02 += paramFloat;
        paramMatrix4f.m03 += paramFloat;
        paramMatrix4f.m10 += paramFloat;
        paramMatrix4f.m11 += paramFloat;
        paramMatrix4f.m12 += paramFloat;
        paramMatrix4f.m13 += paramFloat;
        paramMatrix4f.m20 += paramFloat;
        paramMatrix4f.m21 += paramFloat;
        paramMatrix4f.m22 += paramFloat;
        paramMatrix4f.m23 += paramFloat;
        paramMatrix4f.m30 += paramFloat;
        paramMatrix4f.m31 += paramFloat;
        paramMatrix4f.m32 += paramFloat;
        paramMatrix4f.m33 += paramFloat;
    }

    public final void add(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2) {
        paramMatrix4f1.m00 += paramMatrix4f2.m00;
        paramMatrix4f1.m01 += paramMatrix4f2.m01;
        paramMatrix4f1.m02 += paramMatrix4f2.m02;
        paramMatrix4f1.m03 += paramMatrix4f2.m03;
        paramMatrix4f1.m10 += paramMatrix4f2.m10;
        paramMatrix4f1.m11 += paramMatrix4f2.m11;
        paramMatrix4f1.m12 += paramMatrix4f2.m12;
        paramMatrix4f1.m13 += paramMatrix4f2.m13;
        paramMatrix4f1.m20 += paramMatrix4f2.m20;
        paramMatrix4f1.m21 += paramMatrix4f2.m21;
        paramMatrix4f1.m22 += paramMatrix4f2.m22;
        paramMatrix4f1.m23 += paramMatrix4f2.m23;
        paramMatrix4f1.m30 += paramMatrix4f2.m30;
        paramMatrix4f1.m31 += paramMatrix4f2.m31;
        paramMatrix4f1.m32 += paramMatrix4f2.m32;
        paramMatrix4f1.m33 += paramMatrix4f2.m33;
    }

    public final void add(Matrix4f paramMatrix4f) {
        this.m00 += paramMatrix4f.m00;
        this.m01 += paramMatrix4f.m01;
        this.m02 += paramMatrix4f.m02;
        this.m03 += paramMatrix4f.m03;
        this.m10 += paramMatrix4f.m10;
        this.m11 += paramMatrix4f.m11;
        this.m12 += paramMatrix4f.m12;
        this.m13 += paramMatrix4f.m13;
        this.m20 += paramMatrix4f.m20;
        this.m21 += paramMatrix4f.m21;
        this.m22 += paramMatrix4f.m22;
        this.m23 += paramMatrix4f.m23;
        this.m30 += paramMatrix4f.m30;
        this.m31 += paramMatrix4f.m31;
        this.m32 += paramMatrix4f.m32;
        this.m33 += paramMatrix4f.m33;
    }

    public final void sub(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2) {
        paramMatrix4f1.m00 -= paramMatrix4f2.m00;
        paramMatrix4f1.m01 -= paramMatrix4f2.m01;
        paramMatrix4f1.m02 -= paramMatrix4f2.m02;
        paramMatrix4f1.m03 -= paramMatrix4f2.m03;
        paramMatrix4f1.m10 -= paramMatrix4f2.m10;
        paramMatrix4f1.m11 -= paramMatrix4f2.m11;
        paramMatrix4f1.m12 -= paramMatrix4f2.m12;
        paramMatrix4f1.m13 -= paramMatrix4f2.m13;
        paramMatrix4f1.m20 -= paramMatrix4f2.m20;
        paramMatrix4f1.m21 -= paramMatrix4f2.m21;
        paramMatrix4f1.m22 -= paramMatrix4f2.m22;
        paramMatrix4f1.m23 -= paramMatrix4f2.m23;
        paramMatrix4f1.m30 -= paramMatrix4f2.m30;
        paramMatrix4f1.m31 -= paramMatrix4f2.m31;
        paramMatrix4f1.m32 -= paramMatrix4f2.m32;
        paramMatrix4f1.m33 -= paramMatrix4f2.m33;
    }

    public final void sub(Matrix4f paramMatrix4f) {
        this.m00 -= paramMatrix4f.m00;
        this.m01 -= paramMatrix4f.m01;
        this.m02 -= paramMatrix4f.m02;
        this.m03 -= paramMatrix4f.m03;
        this.m10 -= paramMatrix4f.m10;
        this.m11 -= paramMatrix4f.m11;
        this.m12 -= paramMatrix4f.m12;
        this.m13 -= paramMatrix4f.m13;
        this.m20 -= paramMatrix4f.m20;
        this.m21 -= paramMatrix4f.m21;
        this.m22 -= paramMatrix4f.m22;
        this.m23 -= paramMatrix4f.m23;
        this.m30 -= paramMatrix4f.m30;
        this.m31 -= paramMatrix4f.m31;
        this.m32 -= paramMatrix4f.m32;
        this.m33 -= paramMatrix4f.m33;
    }

    public final void transpose() {
        float f = this.m10;
        this.m10 = this.m01;
        this.m01 = f;
        f = this.m20;
        this.m20 = this.m02;
        this.m02 = f;
        f = this.m30;
        this.m30 = this.m03;
        this.m03 = f;
        f = this.m21;
        this.m21 = this.m12;
        this.m12 = f;
        f = this.m31;
        this.m31 = this.m13;
        this.m13 = f;
        f = this.m32;
        this.m32 = this.m23;
        this.m23 = f;
    }

    public final void transpose(Matrix4f paramMatrix4f) {
        if (this != paramMatrix4f) {
            this.m00 = paramMatrix4f.m00;
            this.m01 = paramMatrix4f.m10;
            this.m02 = paramMatrix4f.m20;
            this.m03 = paramMatrix4f.m30;
            this.m10 = paramMatrix4f.m01;
            this.m11 = paramMatrix4f.m11;
            this.m12 = paramMatrix4f.m21;
            this.m13 = paramMatrix4f.m31;
            this.m20 = paramMatrix4f.m02;
            this.m21 = paramMatrix4f.m12;
            this.m22 = paramMatrix4f.m22;
            this.m23 = paramMatrix4f.m32;
            this.m30 = paramMatrix4f.m03;
            this.m31 = paramMatrix4f.m13;
            this.m32 = paramMatrix4f.m23;
            this.m33 = paramMatrix4f.m33;
        } else {
            transpose();
        }
    }

    public final void set(Quat4f paramQuat4f) {
        this.m00 = (1.0F - 2.0F * paramQuat4f.y * paramQuat4f.y - 2.0F * paramQuat4f.z * paramQuat4f.z);
        this.m10 = (2.0F * (paramQuat4f.x * paramQuat4f.y + paramQuat4f.w * paramQuat4f.z));
        this.m20 = (2.0F * (paramQuat4f.x * paramQuat4f.z - paramQuat4f.w * paramQuat4f.y));
        this.m01 = (2.0F * (paramQuat4f.x * paramQuat4f.y - paramQuat4f.w * paramQuat4f.z));
        this.m11 = (1.0F - 2.0F * paramQuat4f.x * paramQuat4f.x - 2.0F * paramQuat4f.z * paramQuat4f.z);
        this.m21 = (2.0F * (paramQuat4f.y * paramQuat4f.z + paramQuat4f.w * paramQuat4f.x));
        this.m02 = (2.0F * (paramQuat4f.x * paramQuat4f.z + paramQuat4f.w * paramQuat4f.y));
        this.m12 = (2.0F * (paramQuat4f.y * paramQuat4f.z - paramQuat4f.w * paramQuat4f.x));
        this.m22 = (1.0F - 2.0F * paramQuat4f.x * paramQuat4f.x - 2.0F * paramQuat4f.y * paramQuat4f.y);
        this.m03 = 0.0F;
        this.m13 = 0.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(AxisAngle4f paramAxisAngle4f) {
        float f1 = (float) Math.sqrt(paramAxisAngle4f.x * paramAxisAngle4f.x + paramAxisAngle4f.y * paramAxisAngle4f.y + paramAxisAngle4f.z * paramAxisAngle4f.z);
        if (f1 < 1.0E-8D) {
            this.m00 = 1.0F;
            this.m01 = 0.0F;
            this.m02 = 0.0F;
            this.m10 = 0.0F;
            this.m11 = 1.0F;
            this.m12 = 0.0F;
            this.m20 = 0.0F;
            this.m21 = 0.0F;
            this.m22 = 1.0F;
        } else {
            f1 = 1.0F / f1;
            float f2 = paramAxisAngle4f.x * f1;
            float f3 = paramAxisAngle4f.y * f1;
            float f4 = paramAxisAngle4f.z * f1;
            float f5 = (float) Math.sin(paramAxisAngle4f.angle);
            float f6 = (float) Math.cos(paramAxisAngle4f.angle);
            float f7 = 1.0F - f6;
            float f8 = f2 * f4;
            float f9 = f2 * f3;
            float f10 = f3 * f4;
            this.m00 = (f7 * f2 * f2 + f6);
            this.m01 = (f7 * f9 - f5 * f4);
            this.m02 = (f7 * f8 + f5 * f3);
            this.m10 = (f7 * f9 + f5 * f4);
            this.m11 = (f7 * f3 * f3 + f6);
            this.m12 = (f7 * f10 - f5 * f2);
            this.m20 = (f7 * f8 - f5 * f3);
            this.m21 = (f7 * f10 + f5 * f2);
            this.m22 = (f7 * f4 * f4 + f6);
        }
        this.m03 = 0.0F;
        this.m13 = 0.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Quat4d paramQuat4d) {
        this.m00 = ((float) (1.0D - 2.0D * paramQuat4d.y * paramQuat4d.y - 2.0D * paramQuat4d.z * paramQuat4d.z));
        this.m10 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.y + paramQuat4d.w * paramQuat4d.z)));
        this.m20 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.z - paramQuat4d.w * paramQuat4d.y)));
        this.m01 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.y - paramQuat4d.w * paramQuat4d.z)));
        this.m11 = ((float) (1.0D - 2.0D * paramQuat4d.x * paramQuat4d.x - 2.0D * paramQuat4d.z * paramQuat4d.z));
        this.m21 = ((float) (2.0D * (paramQuat4d.y * paramQuat4d.z + paramQuat4d.w * paramQuat4d.x)));
        this.m02 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.z + paramQuat4d.w * paramQuat4d.y)));
        this.m12 = ((float) (2.0D * (paramQuat4d.y * paramQuat4d.z - paramQuat4d.w * paramQuat4d.x)));
        this.m22 = ((float) (1.0D - 2.0D * paramQuat4d.x * paramQuat4d.x - 2.0D * paramQuat4d.y * paramQuat4d.y));
        this.m03 = 0.0F;
        this.m13 = 0.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(AxisAngle4d paramAxisAngle4d) {
        double d1 = Math.sqrt(paramAxisAngle4d.x * paramAxisAngle4d.x + paramAxisAngle4d.y * paramAxisAngle4d.y + paramAxisAngle4d.z * paramAxisAngle4d.z);
        if (d1 < 1.0E-8D) {
            this.m00 = 1.0F;
            this.m01 = 0.0F;
            this.m02 = 0.0F;
            this.m10 = 0.0F;
            this.m11 = 1.0F;
            this.m12 = 0.0F;
            this.m20 = 0.0F;
            this.m21 = 0.0F;
            this.m22 = 1.0F;
        } else {
            d1 = 1.0D / d1;
            double d2 = paramAxisAngle4d.x * d1;
            double d3 = paramAxisAngle4d.y * d1;
            double d4 = paramAxisAngle4d.z * d1;
            float f1 = (float) Math.sin(paramAxisAngle4d.angle);
            float f2 = (float) Math.cos(paramAxisAngle4d.angle);
            float f3 = 1.0F - f2;
            float f4 = (float) (d2 * d4);
            float f5 = (float) (d2 * d3);
            float f6 = (float) (d3 * d4);
            this.m00 = (f3 * (float) (d2 * d2) + f2);
            this.m01 = (f3 * f5 - f1 * (float) d4);
            this.m02 = (f3 * f4 + f1 * (float) d3);
            this.m10 = (f3 * f5 + f1 * (float) d4);
            this.m11 = (f3 * (float) (d3 * d3) + f2);
            this.m12 = (f3 * f6 - f1 * (float) d2);
            this.m20 = (f3 * f4 - f1 * (float) d3);
            this.m21 = (f3 * f6 + f1 * (float) d2);
            this.m22 = (f3 * (float) (d4 * d4) + f2);
        }
        this.m03 = 0.0F;
        this.m13 = 0.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Quat4d paramQuat4d, Vector3d paramVector3d, double paramDouble) {
        this.m00 = ((float) (paramDouble * (1.0D - 2.0D * paramQuat4d.y * paramQuat4d.y - 2.0D * paramQuat4d.z * paramQuat4d.z)));
        this.m10 = ((float) (paramDouble * (2.0D * (paramQuat4d.x * paramQuat4d.y + paramQuat4d.w * paramQuat4d.z))));
        this.m20 = ((float) (paramDouble * (2.0D * (paramQuat4d.x * paramQuat4d.z - paramQuat4d.w * paramQuat4d.y))));
        this.m01 = ((float) (paramDouble * (2.0D * (paramQuat4d.x * paramQuat4d.y - paramQuat4d.w * paramQuat4d.z))));
        this.m11 = ((float) (paramDouble * (1.0D - 2.0D * paramQuat4d.x * paramQuat4d.x - 2.0D * paramQuat4d.z * paramQuat4d.z)));
        this.m21 = ((float) (paramDouble * (2.0D * (paramQuat4d.y * paramQuat4d.z + paramQuat4d.w * paramQuat4d.x))));
        this.m02 = ((float) (paramDouble * (2.0D * (paramQuat4d.x * paramQuat4d.z + paramQuat4d.w * paramQuat4d.y))));
        this.m12 = ((float) (paramDouble * (2.0D * (paramQuat4d.y * paramQuat4d.z - paramQuat4d.w * paramQuat4d.x))));
        this.m22 = ((float) (paramDouble * (1.0D - 2.0D * paramQuat4d.x * paramQuat4d.x - 2.0D * paramQuat4d.y * paramQuat4d.y)));
        this.m03 = ((float) paramVector3d.x);
        this.m13 = ((float) paramVector3d.y);
        this.m23 = ((float) paramVector3d.z);
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Quat4f paramQuat4f, Vector3f paramVector3f, float paramFloat) {
        this.m00 = (paramFloat * (1.0F - 2.0F * paramQuat4f.y * paramQuat4f.y - 2.0F * paramQuat4f.z * paramQuat4f.z));
        this.m10 = (paramFloat * (2.0F * (paramQuat4f.x * paramQuat4f.y + paramQuat4f.w * paramQuat4f.z)));
        this.m20 = (paramFloat * (2.0F * (paramQuat4f.x * paramQuat4f.z - paramQuat4f.w * paramQuat4f.y)));
        this.m01 = (paramFloat * (2.0F * (paramQuat4f.x * paramQuat4f.y - paramQuat4f.w * paramQuat4f.z)));
        this.m11 = (paramFloat * (1.0F - 2.0F * paramQuat4f.x * paramQuat4f.x - 2.0F * paramQuat4f.z * paramQuat4f.z));
        this.m21 = (paramFloat * (2.0F * (paramQuat4f.y * paramQuat4f.z + paramQuat4f.w * paramQuat4f.x)));
        this.m02 = (paramFloat * (2.0F * (paramQuat4f.x * paramQuat4f.z + paramQuat4f.w * paramQuat4f.y)));
        this.m12 = (paramFloat * (2.0F * (paramQuat4f.y * paramQuat4f.z - paramQuat4f.w * paramQuat4f.x)));
        this.m22 = (paramFloat * (1.0F - 2.0F * paramQuat4f.x * paramQuat4f.x - 2.0F * paramQuat4f.y * paramQuat4f.y));
        this.m03 = paramVector3f.x;
        this.m13 = paramVector3f.y;
        this.m23 = paramVector3f.z;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Matrix4d paramMatrix4d) {
        this.m00 = ((float) paramMatrix4d.m00);
        this.m01 = ((float) paramMatrix4d.m01);
        this.m02 = ((float) paramMatrix4d.m02);
        this.m03 = ((float) paramMatrix4d.m03);
        this.m10 = ((float) paramMatrix4d.m10);
        this.m11 = ((float) paramMatrix4d.m11);
        this.m12 = ((float) paramMatrix4d.m12);
        this.m13 = ((float) paramMatrix4d.m13);
        this.m20 = ((float) paramMatrix4d.m20);
        this.m21 = ((float) paramMatrix4d.m21);
        this.m22 = ((float) paramMatrix4d.m22);
        this.m23 = ((float) paramMatrix4d.m23);
        this.m30 = ((float) paramMatrix4d.m30);
        this.m31 = ((float) paramMatrix4d.m31);
        this.m32 = ((float) paramMatrix4d.m32);
        this.m33 = ((float) paramMatrix4d.m33);
    }

    public final void set(Matrix4f paramMatrix4f) {
        this.m00 = paramMatrix4f.m00;
        this.m01 = paramMatrix4f.m01;
        this.m02 = paramMatrix4f.m02;
        this.m03 = paramMatrix4f.m03;
        this.m10 = paramMatrix4f.m10;
        this.m11 = paramMatrix4f.m11;
        this.m12 = paramMatrix4f.m12;
        this.m13 = paramMatrix4f.m13;
        this.m20 = paramMatrix4f.m20;
        this.m21 = paramMatrix4f.m21;
        this.m22 = paramMatrix4f.m22;
        this.m23 = paramMatrix4f.m23;
        this.m30 = paramMatrix4f.m30;
        this.m31 = paramMatrix4f.m31;
        this.m32 = paramMatrix4f.m32;
        this.m33 = paramMatrix4f.m33;
    }

    public final void invert(Matrix4f paramMatrix4f) {
        invertGeneral(paramMatrix4f);
    }

    public final void invert() {
        invertGeneral(this);
    }

    final void invertGeneral(Matrix4f paramMatrix4f) {
        double[] arrayOfDouble1 = new double[16];
        double[] arrayOfDouble2 = new double[16];
        int[] arrayOfInt = new int[4];
        arrayOfDouble1[0] = paramMatrix4f.m00;
        arrayOfDouble1[1] = paramMatrix4f.m01;
        arrayOfDouble1[2] = paramMatrix4f.m02;
        arrayOfDouble1[3] = paramMatrix4f.m03;
        arrayOfDouble1[4] = paramMatrix4f.m10;
        arrayOfDouble1[5] = paramMatrix4f.m11;
        arrayOfDouble1[6] = paramMatrix4f.m12;
        arrayOfDouble1[7] = paramMatrix4f.m13;
        arrayOfDouble1[8] = paramMatrix4f.m20;
        arrayOfDouble1[9] = paramMatrix4f.m21;
        arrayOfDouble1[10] = paramMatrix4f.m22;
        arrayOfDouble1[11] = paramMatrix4f.m23;
        arrayOfDouble1[12] = paramMatrix4f.m30;
        arrayOfDouble1[13] = paramMatrix4f.m31;
        arrayOfDouble1[14] = paramMatrix4f.m32;
        arrayOfDouble1[15] = paramMatrix4f.m33;
        if (!luDecomposition(arrayOfDouble1, arrayOfInt)) {
            throw new SingularMatrixException(VecMathI18N.getString("Matrix4f12"));
        }
        for (int i = 0; i < 16; i++) {
            arrayOfDouble2[i] = 0.0D;
        }
        arrayOfDouble2[0] = 1.0D;
        arrayOfDouble2[5] = 1.0D;
        arrayOfDouble2[10] = 1.0D;
        arrayOfDouble2[15] = 1.0D;
        luBacksubstitution(arrayOfDouble1, arrayOfInt, arrayOfDouble2);
        this.m00 = ((float) arrayOfDouble2[0]);
        this.m01 = ((float) arrayOfDouble2[1]);
        this.m02 = ((float) arrayOfDouble2[2]);
        this.m03 = ((float) arrayOfDouble2[3]);
        this.m10 = ((float) arrayOfDouble2[4]);
        this.m11 = ((float) arrayOfDouble2[5]);
        this.m12 = ((float) arrayOfDouble2[6]);
        this.m13 = ((float) arrayOfDouble2[7]);
        this.m20 = ((float) arrayOfDouble2[8]);
        this.m21 = ((float) arrayOfDouble2[9]);
        this.m22 = ((float) arrayOfDouble2[10]);
        this.m23 = ((float) arrayOfDouble2[11]);
        this.m30 = ((float) arrayOfDouble2[12]);
        this.m31 = ((float) arrayOfDouble2[13]);
        this.m32 = ((float) arrayOfDouble2[14]);
        this.m33 = ((float) arrayOfDouble2[15]);
    }

    public final float determinant() {
        float f = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
        f -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
        f += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
        f -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
        return f;
    }

    public final void set(Matrix3f paramMatrix3f) {
        this.m00 = paramMatrix3f.m00;
        this.m01 = paramMatrix3f.m01;
        this.m02 = paramMatrix3f.m02;
        this.m03 = 0.0F;
        this.m10 = paramMatrix3f.m10;
        this.m11 = paramMatrix3f.m11;
        this.m12 = paramMatrix3f.m12;
        this.m13 = 0.0F;
        this.m20 = paramMatrix3f.m20;
        this.m21 = paramMatrix3f.m21;
        this.m22 = paramMatrix3f.m22;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Matrix3d paramMatrix3d) {
        this.m00 = ((float) paramMatrix3d.m00);
        this.m01 = ((float) paramMatrix3d.m01);
        this.m02 = ((float) paramMatrix3d.m02);
        this.m03 = 0.0F;
        this.m10 = ((float) paramMatrix3d.m10);
        this.m11 = ((float) paramMatrix3d.m11);
        this.m12 = ((float) paramMatrix3d.m12);
        this.m13 = 0.0F;
        this.m20 = ((float) paramMatrix3d.m20);
        this.m21 = ((float) paramMatrix3d.m21);
        this.m22 = ((float) paramMatrix3d.m22);
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(float paramFloat) {
        this.m00 = paramFloat;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = paramFloat;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = paramFloat;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(float[] paramArrayOfFloat) {
        this.m00 = paramArrayOfFloat[0];
        this.m01 = paramArrayOfFloat[1];
        this.m02 = paramArrayOfFloat[2];
        this.m03 = paramArrayOfFloat[3];
        this.m10 = paramArrayOfFloat[4];
        this.m11 = paramArrayOfFloat[5];
        this.m12 = paramArrayOfFloat[6];
        this.m13 = paramArrayOfFloat[7];
        this.m20 = paramArrayOfFloat[8];
        this.m21 = paramArrayOfFloat[9];
        this.m22 = paramArrayOfFloat[10];
        this.m23 = paramArrayOfFloat[11];
        this.m30 = paramArrayOfFloat[12];
        this.m31 = paramArrayOfFloat[13];
        this.m32 = paramArrayOfFloat[14];
        this.m33 = paramArrayOfFloat[15];
    }

    public final void set(Vector3f paramVector3f) {
        this.m00 = 1.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = paramVector3f.x;
        this.m10 = 0.0F;
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m13 = paramVector3f.y;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m23 = paramVector3f.z;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(float paramFloat, Vector3f paramVector3f) {
        this.m00 = paramFloat;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = paramVector3f.x;
        this.m10 = 0.0F;
        this.m11 = paramFloat;
        this.m12 = 0.0F;
        this.m13 = paramVector3f.y;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = paramFloat;
        this.m23 = paramVector3f.z;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Vector3f paramVector3f, float paramFloat) {
        this.m00 = paramFloat;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = (paramFloat * paramVector3f.x);
        this.m10 = 0.0F;
        this.m11 = paramFloat;
        this.m12 = 0.0F;
        this.m13 = (paramFloat * paramVector3f.y);
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = paramFloat;
        this.m23 = (paramFloat * paramVector3f.z);
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Matrix3f paramMatrix3f, Vector3f paramVector3f, float paramFloat) {
        this.m00 = (paramMatrix3f.m00 * paramFloat);
        this.m01 = (paramMatrix3f.m01 * paramFloat);
        this.m02 = (paramMatrix3f.m02 * paramFloat);
        this.m03 = paramVector3f.x;
        this.m10 = (paramMatrix3f.m10 * paramFloat);
        this.m11 = (paramMatrix3f.m11 * paramFloat);
        this.m12 = (paramMatrix3f.m12 * paramFloat);
        this.m13 = paramVector3f.y;
        this.m20 = (paramMatrix3f.m20 * paramFloat);
        this.m21 = (paramMatrix3f.m21 * paramFloat);
        this.m22 = (paramMatrix3f.m22 * paramFloat);
        this.m23 = paramVector3f.z;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void set(Matrix3d paramMatrix3d, Vector3d paramVector3d, double paramDouble) {
        this.m00 = ((float) (paramMatrix3d.m00 * paramDouble));
        this.m01 = ((float) (paramMatrix3d.m01 * paramDouble));
        this.m02 = ((float) (paramMatrix3d.m02 * paramDouble));
        this.m03 = ((float) paramVector3d.x);
        this.m10 = ((float) (paramMatrix3d.m10 * paramDouble));
        this.m11 = ((float) (paramMatrix3d.m11 * paramDouble));
        this.m12 = ((float) (paramMatrix3d.m12 * paramDouble));
        this.m13 = ((float) paramVector3d.y);
        this.m20 = ((float) (paramMatrix3d.m20 * paramDouble));
        this.m21 = ((float) (paramMatrix3d.m21 * paramDouble));
        this.m22 = ((float) (paramMatrix3d.m22 * paramDouble));
        this.m23 = ((float) paramVector3d.z);
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void setTranslation(Vector3f paramVector3f) {
        this.m03 = paramVector3f.x;
        this.m13 = paramVector3f.y;
        this.m23 = paramVector3f.z;
    }

    public final void rotX(float paramFloat) {
        float f1 = (float) Math.sin(paramFloat);
        float f2 = (float) Math.cos(paramFloat);
        this.m00 = 1.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = f2;
        this.m12 = (-f1);
        this.m13 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = f1;
        this.m22 = f2;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void rotY(float paramFloat) {
        float f1 = (float) Math.sin(paramFloat);
        float f2 = (float) Math.cos(paramFloat);
        this.m00 = f2;
        this.m01 = 0.0F;
        this.m02 = f1;
        this.m03 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = 1.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m20 = (-f1);
        this.m21 = 0.0F;
        this.m22 = f2;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void rotZ(float paramFloat) {
        float f1 = (float) Math.sin(paramFloat);
        float f2 = (float) Math.cos(paramFloat);
        this.m00 = f2;
        this.m01 = (-f1);
        this.m02 = 0.0F;
        this.m03 = 0.0F;
        this.m10 = f1;
        this.m11 = f2;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 1.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 1.0F;
    }

    public final void mul(float paramFloat) {
        this.m00 *= paramFloat;
        this.m01 *= paramFloat;
        this.m02 *= paramFloat;
        this.m03 *= paramFloat;
        this.m10 *= paramFloat;
        this.m11 *= paramFloat;
        this.m12 *= paramFloat;
        this.m13 *= paramFloat;
        this.m20 *= paramFloat;
        this.m21 *= paramFloat;
        this.m22 *= paramFloat;
        this.m23 *= paramFloat;
        this.m30 *= paramFloat;
        this.m31 *= paramFloat;
        this.m32 *= paramFloat;
        this.m33 *= paramFloat;
    }

    public final void mul(float paramFloat, Matrix4f paramMatrix4f) {
        paramMatrix4f.m00 *= paramFloat;
        paramMatrix4f.m01 *= paramFloat;
        paramMatrix4f.m02 *= paramFloat;
        paramMatrix4f.m03 *= paramFloat;
        paramMatrix4f.m10 *= paramFloat;
        paramMatrix4f.m11 *= paramFloat;
        paramMatrix4f.m12 *= paramFloat;
        paramMatrix4f.m13 *= paramFloat;
        paramMatrix4f.m20 *= paramFloat;
        paramMatrix4f.m21 *= paramFloat;
        paramMatrix4f.m22 *= paramFloat;
        paramMatrix4f.m23 *= paramFloat;
        paramMatrix4f.m30 *= paramFloat;
        paramMatrix4f.m31 *= paramFloat;
        paramMatrix4f.m32 *= paramFloat;
        paramMatrix4f.m33 *= paramFloat;
    }

    public final void mul(Matrix4f paramMatrix4f) {
        float f1 = this.m00 * paramMatrix4f.m00 + this.m01 * paramMatrix4f.m10 + this.m02 * paramMatrix4f.m20 + this.m03 * paramMatrix4f.m30;
        float f2 = this.m00 * paramMatrix4f.m01 + this.m01 * paramMatrix4f.m11 + this.m02 * paramMatrix4f.m21 + this.m03 * paramMatrix4f.m31;
        float f3 = this.m00 * paramMatrix4f.m02 + this.m01 * paramMatrix4f.m12 + this.m02 * paramMatrix4f.m22 + this.m03 * paramMatrix4f.m32;
        float f4 = this.m00 * paramMatrix4f.m03 + this.m01 * paramMatrix4f.m13 + this.m02 * paramMatrix4f.m23 + this.m03 * paramMatrix4f.m33;
        float f5 = this.m10 * paramMatrix4f.m00 + this.m11 * paramMatrix4f.m10 + this.m12 * paramMatrix4f.m20 + this.m13 * paramMatrix4f.m30;
        float f6 = this.m10 * paramMatrix4f.m01 + this.m11 * paramMatrix4f.m11 + this.m12 * paramMatrix4f.m21 + this.m13 * paramMatrix4f.m31;
        float f7 = this.m10 * paramMatrix4f.m02 + this.m11 * paramMatrix4f.m12 + this.m12 * paramMatrix4f.m22 + this.m13 * paramMatrix4f.m32;
        float f8 = this.m10 * paramMatrix4f.m03 + this.m11 * paramMatrix4f.m13 + this.m12 * paramMatrix4f.m23 + this.m13 * paramMatrix4f.m33;
        float f9 = this.m20 * paramMatrix4f.m00 + this.m21 * paramMatrix4f.m10 + this.m22 * paramMatrix4f.m20 + this.m23 * paramMatrix4f.m30;
        float f10 = this.m20 * paramMatrix4f.m01 + this.m21 * paramMatrix4f.m11 + this.m22 * paramMatrix4f.m21 + this.m23 * paramMatrix4f.m31;
        float f11 = this.m20 * paramMatrix4f.m02 + this.m21 * paramMatrix4f.m12 + this.m22 * paramMatrix4f.m22 + this.m23 * paramMatrix4f.m32;
        float f12 = this.m20 * paramMatrix4f.m03 + this.m21 * paramMatrix4f.m13 + this.m22 * paramMatrix4f.m23 + this.m23 * paramMatrix4f.m33;
        float f13 = this.m30 * paramMatrix4f.m00 + this.m31 * paramMatrix4f.m10 + this.m32 * paramMatrix4f.m20 + this.m33 * paramMatrix4f.m30;
        float f14 = this.m30 * paramMatrix4f.m01 + this.m31 * paramMatrix4f.m11 + this.m32 * paramMatrix4f.m21 + this.m33 * paramMatrix4f.m31;
        float f15 = this.m30 * paramMatrix4f.m02 + this.m31 * paramMatrix4f.m12 + this.m32 * paramMatrix4f.m22 + this.m33 * paramMatrix4f.m32;
        float f16 = this.m30 * paramMatrix4f.m03 + this.m31 * paramMatrix4f.m13 + this.m32 * paramMatrix4f.m23 + this.m33 * paramMatrix4f.m33;
        this.m00 = f1;
        this.m01 = f2;
        this.m02 = f3;
        this.m03 = f4;
        this.m10 = f5;
        this.m11 = f6;
        this.m12 = f7;
        this.m13 = f8;
        this.m20 = f9;
        this.m21 = f10;
        this.m22 = f11;
        this.m23 = f12;
        this.m30 = f13;
        this.m31 = f14;
        this.m32 = f15;
        this.m33 = f16;
    }

    public final void mul(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2) {
        if ((this != paramMatrix4f1) && (this != paramMatrix4f2)) {
            this.m00 = (paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m01 * paramMatrix4f2.m10 + paramMatrix4f1.m02 * paramMatrix4f2.m20 + paramMatrix4f1.m03 * paramMatrix4f2.m30);
            this.m01 = (paramMatrix4f1.m00 * paramMatrix4f2.m01 + paramMatrix4f1.m01 * paramMatrix4f2.m11 + paramMatrix4f1.m02 * paramMatrix4f2.m21 + paramMatrix4f1.m03 * paramMatrix4f2.m31);
            this.m02 = (paramMatrix4f1.m00 * paramMatrix4f2.m02 + paramMatrix4f1.m01 * paramMatrix4f2.m12 + paramMatrix4f1.m02 * paramMatrix4f2.m22 + paramMatrix4f1.m03 * paramMatrix4f2.m32);
            this.m03 = (paramMatrix4f1.m00 * paramMatrix4f2.m03 + paramMatrix4f1.m01 * paramMatrix4f2.m13 + paramMatrix4f1.m02 * paramMatrix4f2.m23 + paramMatrix4f1.m03 * paramMatrix4f2.m33);
            this.m10 = (paramMatrix4f1.m10 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m10 + paramMatrix4f1.m12 * paramMatrix4f2.m20 + paramMatrix4f1.m13 * paramMatrix4f2.m30);
            this.m11 = (paramMatrix4f1.m10 * paramMatrix4f2.m01 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m12 * paramMatrix4f2.m21 + paramMatrix4f1.m13 * paramMatrix4f2.m31);
            this.m12 = (paramMatrix4f1.m10 * paramMatrix4f2.m02 + paramMatrix4f1.m11 * paramMatrix4f2.m12 + paramMatrix4f1.m12 * paramMatrix4f2.m22 + paramMatrix4f1.m13 * paramMatrix4f2.m32);
            this.m13 = (paramMatrix4f1.m10 * paramMatrix4f2.m03 + paramMatrix4f1.m11 * paramMatrix4f2.m13 + paramMatrix4f1.m12 * paramMatrix4f2.m23 + paramMatrix4f1.m13 * paramMatrix4f2.m33);
            this.m20 = (paramMatrix4f1.m20 * paramMatrix4f2.m00 + paramMatrix4f1.m21 * paramMatrix4f2.m10 + paramMatrix4f1.m22 * paramMatrix4f2.m20 + paramMatrix4f1.m23 * paramMatrix4f2.m30);
            this.m21 = (paramMatrix4f1.m20 * paramMatrix4f2.m01 + paramMatrix4f1.m21 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m21 + paramMatrix4f1.m23 * paramMatrix4f2.m31);
            this.m22 = (paramMatrix4f1.m20 * paramMatrix4f2.m02 + paramMatrix4f1.m21 * paramMatrix4f2.m12 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m23 * paramMatrix4f2.m32);
            this.m23 = (paramMatrix4f1.m20 * paramMatrix4f2.m03 + paramMatrix4f1.m21 * paramMatrix4f2.m13 + paramMatrix4f1.m22 * paramMatrix4f2.m23 + paramMatrix4f1.m23 * paramMatrix4f2.m33);
            this.m30 = (paramMatrix4f1.m30 * paramMatrix4f2.m00 + paramMatrix4f1.m31 * paramMatrix4f2.m10 + paramMatrix4f1.m32 * paramMatrix4f2.m20 + paramMatrix4f1.m33 * paramMatrix4f2.m30);
            this.m31 = (paramMatrix4f1.m30 * paramMatrix4f2.m01 + paramMatrix4f1.m31 * paramMatrix4f2.m11 + paramMatrix4f1.m32 * paramMatrix4f2.m21 + paramMatrix4f1.m33 * paramMatrix4f2.m31);
            this.m32 = (paramMatrix4f1.m30 * paramMatrix4f2.m02 + paramMatrix4f1.m31 * paramMatrix4f2.m12 + paramMatrix4f1.m32 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m32);
            this.m33 = (paramMatrix4f1.m30 * paramMatrix4f2.m03 + paramMatrix4f1.m31 * paramMatrix4f2.m13 + paramMatrix4f1.m32 * paramMatrix4f2.m23 + paramMatrix4f1.m33 * paramMatrix4f2.m33);
        } else {
            float f1 = paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m01 * paramMatrix4f2.m10 + paramMatrix4f1.m02 * paramMatrix4f2.m20 + paramMatrix4f1.m03 * paramMatrix4f2.m30;
            float f2 = paramMatrix4f1.m00 * paramMatrix4f2.m01 + paramMatrix4f1.m01 * paramMatrix4f2.m11 + paramMatrix4f1.m02 * paramMatrix4f2.m21 + paramMatrix4f1.m03 * paramMatrix4f2.m31;
            float f3 = paramMatrix4f1.m00 * paramMatrix4f2.m02 + paramMatrix4f1.m01 * paramMatrix4f2.m12 + paramMatrix4f1.m02 * paramMatrix4f2.m22 + paramMatrix4f1.m03 * paramMatrix4f2.m32;
            float f4 = paramMatrix4f1.m00 * paramMatrix4f2.m03 + paramMatrix4f1.m01 * paramMatrix4f2.m13 + paramMatrix4f1.m02 * paramMatrix4f2.m23 + paramMatrix4f1.m03 * paramMatrix4f2.m33;
            float f5 = paramMatrix4f1.m10 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m10 + paramMatrix4f1.m12 * paramMatrix4f2.m20 + paramMatrix4f1.m13 * paramMatrix4f2.m30;
            float f6 = paramMatrix4f1.m10 * paramMatrix4f2.m01 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m12 * paramMatrix4f2.m21 + paramMatrix4f1.m13 * paramMatrix4f2.m31;
            float f7 = paramMatrix4f1.m10 * paramMatrix4f2.m02 + paramMatrix4f1.m11 * paramMatrix4f2.m12 + paramMatrix4f1.m12 * paramMatrix4f2.m22 + paramMatrix4f1.m13 * paramMatrix4f2.m32;
            float f8 = paramMatrix4f1.m10 * paramMatrix4f2.m03 + paramMatrix4f1.m11 * paramMatrix4f2.m13 + paramMatrix4f1.m12 * paramMatrix4f2.m23 + paramMatrix4f1.m13 * paramMatrix4f2.m33;
            float f9 = paramMatrix4f1.m20 * paramMatrix4f2.m00 + paramMatrix4f1.m21 * paramMatrix4f2.m10 + paramMatrix4f1.m22 * paramMatrix4f2.m20 + paramMatrix4f1.m23 * paramMatrix4f2.m30;
            float f10 = paramMatrix4f1.m20 * paramMatrix4f2.m01 + paramMatrix4f1.m21 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m21 + paramMatrix4f1.m23 * paramMatrix4f2.m31;
            float f11 = paramMatrix4f1.m20 * paramMatrix4f2.m02 + paramMatrix4f1.m21 * paramMatrix4f2.m12 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m23 * paramMatrix4f2.m32;
            float f12 = paramMatrix4f1.m20 * paramMatrix4f2.m03 + paramMatrix4f1.m21 * paramMatrix4f2.m13 + paramMatrix4f1.m22 * paramMatrix4f2.m23 + paramMatrix4f1.m23 * paramMatrix4f2.m33;
            float f13 = paramMatrix4f1.m30 * paramMatrix4f2.m00 + paramMatrix4f1.m31 * paramMatrix4f2.m10 + paramMatrix4f1.m32 * paramMatrix4f2.m20 + paramMatrix4f1.m33 * paramMatrix4f2.m30;
            float f14 = paramMatrix4f1.m30 * paramMatrix4f2.m01 + paramMatrix4f1.m31 * paramMatrix4f2.m11 + paramMatrix4f1.m32 * paramMatrix4f2.m21 + paramMatrix4f1.m33 * paramMatrix4f2.m31;
            float f15 = paramMatrix4f1.m30 * paramMatrix4f2.m02 + paramMatrix4f1.m31 * paramMatrix4f2.m12 + paramMatrix4f1.m32 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m32;
            float f16 = paramMatrix4f1.m30 * paramMatrix4f2.m03 + paramMatrix4f1.m31 * paramMatrix4f2.m13 + paramMatrix4f1.m32 * paramMatrix4f2.m23 + paramMatrix4f1.m33 * paramMatrix4f2.m33;
            this.m00 = f1;
            this.m01 = f2;
            this.m02 = f3;
            this.m03 = f4;
            this.m10 = f5;
            this.m11 = f6;
            this.m12 = f7;
            this.m13 = f8;
            this.m20 = f9;
            this.m21 = f10;
            this.m22 = f11;
            this.m23 = f12;
            this.m30 = f13;
            this.m31 = f14;
            this.m32 = f15;
            this.m33 = f16;
        }
    }

    public final void mulTransposeBoth(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2) {
        if ((this != paramMatrix4f1) && (this != paramMatrix4f2)) {
            this.m00 = (paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m10 * paramMatrix4f2.m01 + paramMatrix4f1.m20 * paramMatrix4f2.m02 + paramMatrix4f1.m30 * paramMatrix4f2.m03);
            this.m01 = (paramMatrix4f1.m00 * paramMatrix4f2.m10 + paramMatrix4f1.m10 * paramMatrix4f2.m11 + paramMatrix4f1.m20 * paramMatrix4f2.m12 + paramMatrix4f1.m30 * paramMatrix4f2.m13);
            this.m02 = (paramMatrix4f1.m00 * paramMatrix4f2.m20 + paramMatrix4f1.m10 * paramMatrix4f2.m21 + paramMatrix4f1.m20 * paramMatrix4f2.m22 + paramMatrix4f1.m30 * paramMatrix4f2.m23);
            this.m03 = (paramMatrix4f1.m00 * paramMatrix4f2.m30 + paramMatrix4f1.m10 * paramMatrix4f2.m31 + paramMatrix4f1.m20 * paramMatrix4f2.m32 + paramMatrix4f1.m30 * paramMatrix4f2.m33);
            this.m10 = (paramMatrix4f1.m01 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m01 + paramMatrix4f1.m21 * paramMatrix4f2.m02 + paramMatrix4f1.m31 * paramMatrix4f2.m03);
            this.m11 = (paramMatrix4f1.m01 * paramMatrix4f2.m10 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m21 * paramMatrix4f2.m12 + paramMatrix4f1.m31 * paramMatrix4f2.m13);
            this.m12 = (paramMatrix4f1.m01 * paramMatrix4f2.m20 + paramMatrix4f1.m11 * paramMatrix4f2.m21 + paramMatrix4f1.m21 * paramMatrix4f2.m22 + paramMatrix4f1.m31 * paramMatrix4f2.m23);
            this.m13 = (paramMatrix4f1.m01 * paramMatrix4f2.m30 + paramMatrix4f1.m11 * paramMatrix4f2.m31 + paramMatrix4f1.m21 * paramMatrix4f2.m32 + paramMatrix4f1.m31 * paramMatrix4f2.m33);
            this.m20 = (paramMatrix4f1.m02 * paramMatrix4f2.m00 + paramMatrix4f1.m12 * paramMatrix4f2.m01 + paramMatrix4f1.m22 * paramMatrix4f2.m02 + paramMatrix4f1.m32 * paramMatrix4f2.m03);
            this.m21 = (paramMatrix4f1.m02 * paramMatrix4f2.m10 + paramMatrix4f1.m12 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m12 + paramMatrix4f1.m32 * paramMatrix4f2.m13);
            this.m22 = (paramMatrix4f1.m02 * paramMatrix4f2.m20 + paramMatrix4f1.m12 * paramMatrix4f2.m21 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m32 * paramMatrix4f2.m23);
            this.m23 = (paramMatrix4f1.m02 * paramMatrix4f2.m30 + paramMatrix4f1.m12 * paramMatrix4f2.m31 + paramMatrix4f1.m22 * paramMatrix4f2.m32 + paramMatrix4f1.m32 * paramMatrix4f2.m33);
            this.m30 = (paramMatrix4f1.m03 * paramMatrix4f2.m00 + paramMatrix4f1.m13 * paramMatrix4f2.m01 + paramMatrix4f1.m23 * paramMatrix4f2.m02 + paramMatrix4f1.m33 * paramMatrix4f2.m03);
            this.m31 = (paramMatrix4f1.m03 * paramMatrix4f2.m10 + paramMatrix4f1.m13 * paramMatrix4f2.m11 + paramMatrix4f1.m23 * paramMatrix4f2.m12 + paramMatrix4f1.m33 * paramMatrix4f2.m13);
            this.m32 = (paramMatrix4f1.m03 * paramMatrix4f2.m20 + paramMatrix4f1.m13 * paramMatrix4f2.m21 + paramMatrix4f1.m23 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m23);
            this.m33 = (paramMatrix4f1.m03 * paramMatrix4f2.m30 + paramMatrix4f1.m13 * paramMatrix4f2.m31 + paramMatrix4f1.m23 * paramMatrix4f2.m32 + paramMatrix4f1.m33 * paramMatrix4f2.m33);
        } else {
            float f1 = paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m10 * paramMatrix4f2.m01 + paramMatrix4f1.m20 * paramMatrix4f2.m02 + paramMatrix4f1.m30 * paramMatrix4f2.m03;
            float f2 = paramMatrix4f1.m00 * paramMatrix4f2.m10 + paramMatrix4f1.m10 * paramMatrix4f2.m11 + paramMatrix4f1.m20 * paramMatrix4f2.m12 + paramMatrix4f1.m30 * paramMatrix4f2.m13;
            float f3 = paramMatrix4f1.m00 * paramMatrix4f2.m20 + paramMatrix4f1.m10 * paramMatrix4f2.m21 + paramMatrix4f1.m20 * paramMatrix4f2.m22 + paramMatrix4f1.m30 * paramMatrix4f2.m23;
            float f4 = paramMatrix4f1.m00 * paramMatrix4f2.m30 + paramMatrix4f1.m10 * paramMatrix4f2.m31 + paramMatrix4f1.m20 * paramMatrix4f2.m32 + paramMatrix4f1.m30 * paramMatrix4f2.m33;
            float f5 = paramMatrix4f1.m01 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m01 + paramMatrix4f1.m21 * paramMatrix4f2.m02 + paramMatrix4f1.m31 * paramMatrix4f2.m03;
            float f6 = paramMatrix4f1.m01 * paramMatrix4f2.m10 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m21 * paramMatrix4f2.m12 + paramMatrix4f1.m31 * paramMatrix4f2.m13;
            float f7 = paramMatrix4f1.m01 * paramMatrix4f2.m20 + paramMatrix4f1.m11 * paramMatrix4f2.m21 + paramMatrix4f1.m21 * paramMatrix4f2.m22 + paramMatrix4f1.m31 * paramMatrix4f2.m23;
            float f8 = paramMatrix4f1.m01 * paramMatrix4f2.m30 + paramMatrix4f1.m11 * paramMatrix4f2.m31 + paramMatrix4f1.m21 * paramMatrix4f2.m32 + paramMatrix4f1.m31 * paramMatrix4f2.m33;
            float f9 = paramMatrix4f1.m02 * paramMatrix4f2.m00 + paramMatrix4f1.m12 * paramMatrix4f2.m01 + paramMatrix4f1.m22 * paramMatrix4f2.m02 + paramMatrix4f1.m32 * paramMatrix4f2.m03;
            float f10 = paramMatrix4f1.m02 * paramMatrix4f2.m10 + paramMatrix4f1.m12 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m12 + paramMatrix4f1.m32 * paramMatrix4f2.m13;
            float f11 = paramMatrix4f1.m02 * paramMatrix4f2.m20 + paramMatrix4f1.m12 * paramMatrix4f2.m21 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m32 * paramMatrix4f2.m23;
            float f12 = paramMatrix4f1.m02 * paramMatrix4f2.m30 + paramMatrix4f1.m12 * paramMatrix4f2.m31 + paramMatrix4f1.m22 * paramMatrix4f2.m32 + paramMatrix4f1.m32 * paramMatrix4f2.m33;
            float f13 = paramMatrix4f1.m03 * paramMatrix4f2.m00 + paramMatrix4f1.m13 * paramMatrix4f2.m01 + paramMatrix4f1.m23 * paramMatrix4f2.m02 + paramMatrix4f1.m33 * paramMatrix4f2.m03;
            float f14 = paramMatrix4f1.m03 * paramMatrix4f2.m10 + paramMatrix4f1.m13 * paramMatrix4f2.m11 + paramMatrix4f1.m23 * paramMatrix4f2.m12 + paramMatrix4f1.m33 * paramMatrix4f2.m13;
            float f15 = paramMatrix4f1.m03 * paramMatrix4f2.m20 + paramMatrix4f1.m13 * paramMatrix4f2.m21 + paramMatrix4f1.m23 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m23;
            float f16 = paramMatrix4f1.m03 * paramMatrix4f2.m30 + paramMatrix4f1.m13 * paramMatrix4f2.m31 + paramMatrix4f1.m23 * paramMatrix4f2.m32 + paramMatrix4f1.m33 * paramMatrix4f2.m33;
            this.m00 = f1;
            this.m01 = f2;
            this.m02 = f3;
            this.m03 = f4;
            this.m10 = f5;
            this.m11 = f6;
            this.m12 = f7;
            this.m13 = f8;
            this.m20 = f9;
            this.m21 = f10;
            this.m22 = f11;
            this.m23 = f12;
            this.m30 = f13;
            this.m31 = f14;
            this.m32 = f15;
            this.m33 = f16;
        }
    }

    public final void mulTransposeRight(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2) {
        if ((this != paramMatrix4f1) && (this != paramMatrix4f2)) {
            this.m00 = (paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m01 * paramMatrix4f2.m01 + paramMatrix4f1.m02 * paramMatrix4f2.m02 + paramMatrix4f1.m03 * paramMatrix4f2.m03);
            this.m01 = (paramMatrix4f1.m00 * paramMatrix4f2.m10 + paramMatrix4f1.m01 * paramMatrix4f2.m11 + paramMatrix4f1.m02 * paramMatrix4f2.m12 + paramMatrix4f1.m03 * paramMatrix4f2.m13);
            this.m02 = (paramMatrix4f1.m00 * paramMatrix4f2.m20 + paramMatrix4f1.m01 * paramMatrix4f2.m21 + paramMatrix4f1.m02 * paramMatrix4f2.m22 + paramMatrix4f1.m03 * paramMatrix4f2.m23);
            this.m03 = (paramMatrix4f1.m00 * paramMatrix4f2.m30 + paramMatrix4f1.m01 * paramMatrix4f2.m31 + paramMatrix4f1.m02 * paramMatrix4f2.m32 + paramMatrix4f1.m03 * paramMatrix4f2.m33);
            this.m10 = (paramMatrix4f1.m10 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m01 + paramMatrix4f1.m12 * paramMatrix4f2.m02 + paramMatrix4f1.m13 * paramMatrix4f2.m03);
            this.m11 = (paramMatrix4f1.m10 * paramMatrix4f2.m10 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m12 * paramMatrix4f2.m12 + paramMatrix4f1.m13 * paramMatrix4f2.m13);
            this.m12 = (paramMatrix4f1.m10 * paramMatrix4f2.m20 + paramMatrix4f1.m11 * paramMatrix4f2.m21 + paramMatrix4f1.m12 * paramMatrix4f2.m22 + paramMatrix4f1.m13 * paramMatrix4f2.m23);
            this.m13 = (paramMatrix4f1.m10 * paramMatrix4f2.m30 + paramMatrix4f1.m11 * paramMatrix4f2.m31 + paramMatrix4f1.m12 * paramMatrix4f2.m32 + paramMatrix4f1.m13 * paramMatrix4f2.m33);
            this.m20 = (paramMatrix4f1.m20 * paramMatrix4f2.m00 + paramMatrix4f1.m21 * paramMatrix4f2.m01 + paramMatrix4f1.m22 * paramMatrix4f2.m02 + paramMatrix4f1.m23 * paramMatrix4f2.m03);
            this.m21 = (paramMatrix4f1.m20 * paramMatrix4f2.m10 + paramMatrix4f1.m21 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m12 + paramMatrix4f1.m23 * paramMatrix4f2.m13);
            this.m22 = (paramMatrix4f1.m20 * paramMatrix4f2.m20 + paramMatrix4f1.m21 * paramMatrix4f2.m21 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m23 * paramMatrix4f2.m23);
            this.m23 = (paramMatrix4f1.m20 * paramMatrix4f2.m30 + paramMatrix4f1.m21 * paramMatrix4f2.m31 + paramMatrix4f1.m22 * paramMatrix4f2.m32 + paramMatrix4f1.m23 * paramMatrix4f2.m33);
            this.m30 = (paramMatrix4f1.m30 * paramMatrix4f2.m00 + paramMatrix4f1.m31 * paramMatrix4f2.m01 + paramMatrix4f1.m32 * paramMatrix4f2.m02 + paramMatrix4f1.m33 * paramMatrix4f2.m03);
            this.m31 = (paramMatrix4f1.m30 * paramMatrix4f2.m10 + paramMatrix4f1.m31 * paramMatrix4f2.m11 + paramMatrix4f1.m32 * paramMatrix4f2.m12 + paramMatrix4f1.m33 * paramMatrix4f2.m13);
            this.m32 = (paramMatrix4f1.m30 * paramMatrix4f2.m20 + paramMatrix4f1.m31 * paramMatrix4f2.m21 + paramMatrix4f1.m32 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m23);
            this.m33 = (paramMatrix4f1.m30 * paramMatrix4f2.m30 + paramMatrix4f1.m31 * paramMatrix4f2.m31 + paramMatrix4f1.m32 * paramMatrix4f2.m32 + paramMatrix4f1.m33 * paramMatrix4f2.m33);
        } else {
            float f1 = paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m01 * paramMatrix4f2.m01 + paramMatrix4f1.m02 * paramMatrix4f2.m02 + paramMatrix4f1.m03 * paramMatrix4f2.m03;
            float f2 = paramMatrix4f1.m00 * paramMatrix4f2.m10 + paramMatrix4f1.m01 * paramMatrix4f2.m11 + paramMatrix4f1.m02 * paramMatrix4f2.m12 + paramMatrix4f1.m03 * paramMatrix4f2.m13;
            float f3 = paramMatrix4f1.m00 * paramMatrix4f2.m20 + paramMatrix4f1.m01 * paramMatrix4f2.m21 + paramMatrix4f1.m02 * paramMatrix4f2.m22 + paramMatrix4f1.m03 * paramMatrix4f2.m23;
            float f4 = paramMatrix4f1.m00 * paramMatrix4f2.m30 + paramMatrix4f1.m01 * paramMatrix4f2.m31 + paramMatrix4f1.m02 * paramMatrix4f2.m32 + paramMatrix4f1.m03 * paramMatrix4f2.m33;
            float f5 = paramMatrix4f1.m10 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m01 + paramMatrix4f1.m12 * paramMatrix4f2.m02 + paramMatrix4f1.m13 * paramMatrix4f2.m03;
            float f6 = paramMatrix4f1.m10 * paramMatrix4f2.m10 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m12 * paramMatrix4f2.m12 + paramMatrix4f1.m13 * paramMatrix4f2.m13;
            float f7 = paramMatrix4f1.m10 * paramMatrix4f2.m20 + paramMatrix4f1.m11 * paramMatrix4f2.m21 + paramMatrix4f1.m12 * paramMatrix4f2.m22 + paramMatrix4f1.m13 * paramMatrix4f2.m23;
            float f8 = paramMatrix4f1.m10 * paramMatrix4f2.m30 + paramMatrix4f1.m11 * paramMatrix4f2.m31 + paramMatrix4f1.m12 * paramMatrix4f2.m32 + paramMatrix4f1.m13 * paramMatrix4f2.m33;
            float f9 = paramMatrix4f1.m20 * paramMatrix4f2.m00 + paramMatrix4f1.m21 * paramMatrix4f2.m01 + paramMatrix4f1.m22 * paramMatrix4f2.m02 + paramMatrix4f1.m23 * paramMatrix4f2.m03;
            float f10 = paramMatrix4f1.m20 * paramMatrix4f2.m10 + paramMatrix4f1.m21 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m12 + paramMatrix4f1.m23 * paramMatrix4f2.m13;
            float f11 = paramMatrix4f1.m20 * paramMatrix4f2.m20 + paramMatrix4f1.m21 * paramMatrix4f2.m21 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m23 * paramMatrix4f2.m23;
            float f12 = paramMatrix4f1.m20 * paramMatrix4f2.m30 + paramMatrix4f1.m21 * paramMatrix4f2.m31 + paramMatrix4f1.m22 * paramMatrix4f2.m32 + paramMatrix4f1.m23 * paramMatrix4f2.m33;
            float f13 = paramMatrix4f1.m30 * paramMatrix4f2.m00 + paramMatrix4f1.m31 * paramMatrix4f2.m01 + paramMatrix4f1.m32 * paramMatrix4f2.m02 + paramMatrix4f1.m33 * paramMatrix4f2.m03;
            float f14 = paramMatrix4f1.m30 * paramMatrix4f2.m10 + paramMatrix4f1.m31 * paramMatrix4f2.m11 + paramMatrix4f1.m32 * paramMatrix4f2.m12 + paramMatrix4f1.m33 * paramMatrix4f2.m13;
            float f15 = paramMatrix4f1.m30 * paramMatrix4f2.m20 + paramMatrix4f1.m31 * paramMatrix4f2.m21 + paramMatrix4f1.m32 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m23;
            float f16 = paramMatrix4f1.m30 * paramMatrix4f2.m30 + paramMatrix4f1.m31 * paramMatrix4f2.m31 + paramMatrix4f1.m32 * paramMatrix4f2.m32 + paramMatrix4f1.m33 * paramMatrix4f2.m33;
            this.m00 = f1;
            this.m01 = f2;
            this.m02 = f3;
            this.m03 = f4;
            this.m10 = f5;
            this.m11 = f6;
            this.m12 = f7;
            this.m13 = f8;
            this.m20 = f9;
            this.m21 = f10;
            this.m22 = f11;
            this.m23 = f12;
            this.m30 = f13;
            this.m31 = f14;
            this.m32 = f15;
            this.m33 = f16;
        }
    }

    public final void mulTransposeLeft(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2) {
        if ((this != paramMatrix4f1) && (this != paramMatrix4f2)) {
            this.m00 = (paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m10 * paramMatrix4f2.m10 + paramMatrix4f1.m20 * paramMatrix4f2.m20 + paramMatrix4f1.m30 * paramMatrix4f2.m30);
            this.m01 = (paramMatrix4f1.m00 * paramMatrix4f2.m01 + paramMatrix4f1.m10 * paramMatrix4f2.m11 + paramMatrix4f1.m20 * paramMatrix4f2.m21 + paramMatrix4f1.m30 * paramMatrix4f2.m31);
            this.m02 = (paramMatrix4f1.m00 * paramMatrix4f2.m02 + paramMatrix4f1.m10 * paramMatrix4f2.m12 + paramMatrix4f1.m20 * paramMatrix4f2.m22 + paramMatrix4f1.m30 * paramMatrix4f2.m32);
            this.m03 = (paramMatrix4f1.m00 * paramMatrix4f2.m03 + paramMatrix4f1.m10 * paramMatrix4f2.m13 + paramMatrix4f1.m20 * paramMatrix4f2.m23 + paramMatrix4f1.m30 * paramMatrix4f2.m33);
            this.m10 = (paramMatrix4f1.m01 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m10 + paramMatrix4f1.m21 * paramMatrix4f2.m20 + paramMatrix4f1.m31 * paramMatrix4f2.m30);
            this.m11 = (paramMatrix4f1.m01 * paramMatrix4f2.m01 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m21 * paramMatrix4f2.m21 + paramMatrix4f1.m31 * paramMatrix4f2.m31);
            this.m12 = (paramMatrix4f1.m01 * paramMatrix4f2.m02 + paramMatrix4f1.m11 * paramMatrix4f2.m12 + paramMatrix4f1.m21 * paramMatrix4f2.m22 + paramMatrix4f1.m31 * paramMatrix4f2.m32);
            this.m13 = (paramMatrix4f1.m01 * paramMatrix4f2.m03 + paramMatrix4f1.m11 * paramMatrix4f2.m13 + paramMatrix4f1.m21 * paramMatrix4f2.m23 + paramMatrix4f1.m31 * paramMatrix4f2.m33);
            this.m20 = (paramMatrix4f1.m02 * paramMatrix4f2.m00 + paramMatrix4f1.m12 * paramMatrix4f2.m10 + paramMatrix4f1.m22 * paramMatrix4f2.m20 + paramMatrix4f1.m32 * paramMatrix4f2.m30);
            this.m21 = (paramMatrix4f1.m02 * paramMatrix4f2.m01 + paramMatrix4f1.m12 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m21 + paramMatrix4f1.m32 * paramMatrix4f2.m31);
            this.m22 = (paramMatrix4f1.m02 * paramMatrix4f2.m02 + paramMatrix4f1.m12 * paramMatrix4f2.m12 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m32 * paramMatrix4f2.m32);
            this.m23 = (paramMatrix4f1.m02 * paramMatrix4f2.m03 + paramMatrix4f1.m12 * paramMatrix4f2.m13 + paramMatrix4f1.m22 * paramMatrix4f2.m23 + paramMatrix4f1.m32 * paramMatrix4f2.m33);
            this.m30 = (paramMatrix4f1.m03 * paramMatrix4f2.m00 + paramMatrix4f1.m13 * paramMatrix4f2.m10 + paramMatrix4f1.m23 * paramMatrix4f2.m20 + paramMatrix4f1.m33 * paramMatrix4f2.m30);
            this.m31 = (paramMatrix4f1.m03 * paramMatrix4f2.m01 + paramMatrix4f1.m13 * paramMatrix4f2.m11 + paramMatrix4f1.m23 * paramMatrix4f2.m21 + paramMatrix4f1.m33 * paramMatrix4f2.m31);
            this.m32 = (paramMatrix4f1.m03 * paramMatrix4f2.m02 + paramMatrix4f1.m13 * paramMatrix4f2.m12 + paramMatrix4f1.m23 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m32);
            this.m33 = (paramMatrix4f1.m03 * paramMatrix4f2.m03 + paramMatrix4f1.m13 * paramMatrix4f2.m13 + paramMatrix4f1.m23 * paramMatrix4f2.m23 + paramMatrix4f1.m33 * paramMatrix4f2.m33);
        } else {
            float f1 = paramMatrix4f1.m00 * paramMatrix4f2.m00 + paramMatrix4f1.m10 * paramMatrix4f2.m10 + paramMatrix4f1.m20 * paramMatrix4f2.m20 + paramMatrix4f1.m30 * paramMatrix4f2.m30;
            float f2 = paramMatrix4f1.m00 * paramMatrix4f2.m01 + paramMatrix4f1.m10 * paramMatrix4f2.m11 + paramMatrix4f1.m20 * paramMatrix4f2.m21 + paramMatrix4f1.m30 * paramMatrix4f2.m31;
            float f3 = paramMatrix4f1.m00 * paramMatrix4f2.m02 + paramMatrix4f1.m10 * paramMatrix4f2.m12 + paramMatrix4f1.m20 * paramMatrix4f2.m22 + paramMatrix4f1.m30 * paramMatrix4f2.m32;
            float f4 = paramMatrix4f1.m00 * paramMatrix4f2.m03 + paramMatrix4f1.m10 * paramMatrix4f2.m13 + paramMatrix4f1.m20 * paramMatrix4f2.m23 + paramMatrix4f1.m30 * paramMatrix4f2.m33;
            float f5 = paramMatrix4f1.m01 * paramMatrix4f2.m00 + paramMatrix4f1.m11 * paramMatrix4f2.m10 + paramMatrix4f1.m21 * paramMatrix4f2.m20 + paramMatrix4f1.m31 * paramMatrix4f2.m30;
            float f6 = paramMatrix4f1.m01 * paramMatrix4f2.m01 + paramMatrix4f1.m11 * paramMatrix4f2.m11 + paramMatrix4f1.m21 * paramMatrix4f2.m21 + paramMatrix4f1.m31 * paramMatrix4f2.m31;
            float f7 = paramMatrix4f1.m01 * paramMatrix4f2.m02 + paramMatrix4f1.m11 * paramMatrix4f2.m12 + paramMatrix4f1.m21 * paramMatrix4f2.m22 + paramMatrix4f1.m31 * paramMatrix4f2.m32;
            float f8 = paramMatrix4f1.m01 * paramMatrix4f2.m03 + paramMatrix4f1.m11 * paramMatrix4f2.m13 + paramMatrix4f1.m21 * paramMatrix4f2.m23 + paramMatrix4f1.m31 * paramMatrix4f2.m33;
            float f9 = paramMatrix4f1.m02 * paramMatrix4f2.m00 + paramMatrix4f1.m12 * paramMatrix4f2.m10 + paramMatrix4f1.m22 * paramMatrix4f2.m20 + paramMatrix4f1.m32 * paramMatrix4f2.m30;
            float f10 = paramMatrix4f1.m02 * paramMatrix4f2.m01 + paramMatrix4f1.m12 * paramMatrix4f2.m11 + paramMatrix4f1.m22 * paramMatrix4f2.m21 + paramMatrix4f1.m32 * paramMatrix4f2.m31;
            float f11 = paramMatrix4f1.m02 * paramMatrix4f2.m02 + paramMatrix4f1.m12 * paramMatrix4f2.m12 + paramMatrix4f1.m22 * paramMatrix4f2.m22 + paramMatrix4f1.m32 * paramMatrix4f2.m32;
            float f12 = paramMatrix4f1.m02 * paramMatrix4f2.m03 + paramMatrix4f1.m12 * paramMatrix4f2.m13 + paramMatrix4f1.m22 * paramMatrix4f2.m23 + paramMatrix4f1.m32 * paramMatrix4f2.m33;
            float f13 = paramMatrix4f1.m03 * paramMatrix4f2.m00 + paramMatrix4f1.m13 * paramMatrix4f2.m10 + paramMatrix4f1.m23 * paramMatrix4f2.m20 + paramMatrix4f1.m33 * paramMatrix4f2.m30;
            float f14 = paramMatrix4f1.m03 * paramMatrix4f2.m01 + paramMatrix4f1.m13 * paramMatrix4f2.m11 + paramMatrix4f1.m23 * paramMatrix4f2.m21 + paramMatrix4f1.m33 * paramMatrix4f2.m31;
            float f15 = paramMatrix4f1.m03 * paramMatrix4f2.m02 + paramMatrix4f1.m13 * paramMatrix4f2.m12 + paramMatrix4f1.m23 * paramMatrix4f2.m22 + paramMatrix4f1.m33 * paramMatrix4f2.m32;
            float f16 = paramMatrix4f1.m03 * paramMatrix4f2.m03 + paramMatrix4f1.m13 * paramMatrix4f2.m13 + paramMatrix4f1.m23 * paramMatrix4f2.m23 + paramMatrix4f1.m33 * paramMatrix4f2.m33;
            this.m00 = f1;
            this.m01 = f2;
            this.m02 = f3;
            this.m03 = f4;
            this.m10 = f5;
            this.m11 = f6;
            this.m12 = f7;
            this.m13 = f8;
            this.m20 = f9;
            this.m21 = f10;
            this.m22 = f11;
            this.m23 = f12;
            this.m30 = f13;
            this.m31 = f14;
            this.m32 = f15;
            this.m33 = f16;
        }
    }

    public boolean equals(Matrix4f paramMatrix4f) {
        try {
            return (this.m00 == paramMatrix4f.m00) && (this.m01 == paramMatrix4f.m01) && (this.m02 == paramMatrix4f.m02) && (this.m03 == paramMatrix4f.m03) && (this.m10 == paramMatrix4f.m10) && (this.m11 == paramMatrix4f.m11) && (this.m12 == paramMatrix4f.m12) && (this.m13 == paramMatrix4f.m13) && (this.m20 == paramMatrix4f.m20) && (this.m21 == paramMatrix4f.m21) && (this.m22 == paramMatrix4f.m22) && (this.m23 == paramMatrix4f.m23) && (this.m30 == paramMatrix4f.m30) && (this.m31 == paramMatrix4f.m31) && (this.m32 == paramMatrix4f.m32) && (this.m33 == paramMatrix4f.m33);
        } catch (NullPointerException localNullPointerException) {
        }
        return false;
    }

    public boolean equals(Object paramObject) {
        try {
            Matrix4f localMatrix4f = (Matrix4f) paramObject;
            return (this.m00 == localMatrix4f.m00) && (this.m01 == localMatrix4f.m01) && (this.m02 == localMatrix4f.m02) && (this.m03 == localMatrix4f.m03) && (this.m10 == localMatrix4f.m10) && (this.m11 == localMatrix4f.m11) && (this.m12 == localMatrix4f.m12) && (this.m13 == localMatrix4f.m13) && (this.m20 == localMatrix4f.m20) && (this.m21 == localMatrix4f.m21) && (this.m22 == localMatrix4f.m22) && (this.m23 == localMatrix4f.m23) && (this.m30 == localMatrix4f.m30) && (this.m31 == localMatrix4f.m31) && (this.m32 == localMatrix4f.m32) && (this.m33 == localMatrix4f.m33);
        } catch (ClassCastException localClassCastException) {
            return false;
        } catch (NullPointerException localNullPointerException) {
        }
        return false;
    }

    public boolean epsilonEquals(Matrix4f paramMatrix4f, float paramFloat) {
        boolean bool = true;
        if (Math.abs(this.m00 - paramMatrix4f.m00) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m01 - paramMatrix4f.m01) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m02 - paramMatrix4f.m02) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m03 - paramMatrix4f.m03) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m10 - paramMatrix4f.m10) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m11 - paramMatrix4f.m11) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m12 - paramMatrix4f.m12) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m13 - paramMatrix4f.m13) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m20 - paramMatrix4f.m20) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m21 - paramMatrix4f.m21) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m22 - paramMatrix4f.m22) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m23 - paramMatrix4f.m23) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m30 - paramMatrix4f.m30) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m31 - paramMatrix4f.m31) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m32 - paramMatrix4f.m32) > paramFloat) {
            bool = false;
        }
        if (Math.abs(this.m33 - paramMatrix4f.m33) > paramFloat) {
            bool = false;
        }
        return bool;
    }

    public int hashCode() {
        long l = 1L;
        l = 31L * l + VecMathUtil.floatToIntBits(this.m00);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m01);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m02);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m03);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m10);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m11);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m12);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m13);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m20);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m21);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m22);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m23);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m30);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m31);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m32);
        l = 31L * l + VecMathUtil.floatToIntBits(this.m33);
        return (int) (l ^ l >> 32);
    }

    public final void transform(Tuple4f paramTuple4f1, Tuple4f paramTuple4f2) {
        float f1 = this.m00 * paramTuple4f1.x + this.m01 * paramTuple4f1.y + this.m02 * paramTuple4f1.z + this.m03 * paramTuple4f1.w;
        float f2 = this.m10 * paramTuple4f1.x + this.m11 * paramTuple4f1.y + this.m12 * paramTuple4f1.z + this.m13 * paramTuple4f1.w;
        float f3 = this.m20 * paramTuple4f1.x + this.m21 * paramTuple4f1.y + this.m22 * paramTuple4f1.z + this.m23 * paramTuple4f1.w;
        paramTuple4f2.w = (this.m30 * paramTuple4f1.x + this.m31 * paramTuple4f1.y + this.m32 * paramTuple4f1.z + this.m33 * paramTuple4f1.w);
        paramTuple4f2.x = f1;
        paramTuple4f2.y = f2;
        paramTuple4f2.z = f3;
    }

    public final void transform(Tuple4f paramTuple4f) {
        float f1 = this.m00 * paramTuple4f.x + this.m01 * paramTuple4f.y + this.m02 * paramTuple4f.z + this.m03 * paramTuple4f.w;
        float f2 = this.m10 * paramTuple4f.x + this.m11 * paramTuple4f.y + this.m12 * paramTuple4f.z + this.m13 * paramTuple4f.w;
        float f3 = this.m20 * paramTuple4f.x + this.m21 * paramTuple4f.y + this.m22 * paramTuple4f.z + this.m23 * paramTuple4f.w;
        paramTuple4f.w = (this.m30 * paramTuple4f.x + this.m31 * paramTuple4f.y + this.m32 * paramTuple4f.z + this.m33 * paramTuple4f.w);
        paramTuple4f.x = f1;
        paramTuple4f.y = f2;
        paramTuple4f.z = f3;
    }

    public final void transform(Point3f paramPoint3f1, Point3f paramPoint3f2) {
        float f1 = this.m00 * paramPoint3f1.x + this.m01 * paramPoint3f1.y + this.m02 * paramPoint3f1.z + this.m03;
        float f2 = this.m10 * paramPoint3f1.x + this.m11 * paramPoint3f1.y + this.m12 * paramPoint3f1.z + this.m13;
        paramPoint3f2.z = (this.m20 * paramPoint3f1.x + this.m21 * paramPoint3f1.y + this.m22 * paramPoint3f1.z + this.m23);
        paramPoint3f2.x = f1;
        paramPoint3f2.y = f2;
    }

    public final void transform(Point3f paramPoint3f) {
        float f1 = this.m00 * paramPoint3f.x + this.m01 * paramPoint3f.y + this.m02 * paramPoint3f.z + this.m03;
        float f2 = this.m10 * paramPoint3f.x + this.m11 * paramPoint3f.y + this.m12 * paramPoint3f.z + this.m13;
        paramPoint3f.z = (this.m20 * paramPoint3f.x + this.m21 * paramPoint3f.y + this.m22 * paramPoint3f.z + this.m23);
        paramPoint3f.x = f1;
        paramPoint3f.y = f2;
    }

    public final void transform(Vector3f paramVector3f1, Vector3f paramVector3f2) {
        float f1 = this.m00 * paramVector3f1.x + this.m01 * paramVector3f1.y + this.m02 * paramVector3f1.z;
        float f2 = this.m10 * paramVector3f1.x + this.m11 * paramVector3f1.y + this.m12 * paramVector3f1.z;
        paramVector3f2.z = (this.m20 * paramVector3f1.x + this.m21 * paramVector3f1.y + this.m22 * paramVector3f1.z);
        paramVector3f2.x = f1;
        paramVector3f2.y = f2;
    }

    public final void transform(Vector3f paramVector3f) {
        float f1 = this.m00 * paramVector3f.x + this.m01 * paramVector3f.y + this.m02 * paramVector3f.z;
        float f2 = this.m10 * paramVector3f.x + this.m11 * paramVector3f.y + this.m12 * paramVector3f.z;
        paramVector3f.z = (this.m20 * paramVector3f.x + this.m21 * paramVector3f.y + this.m22 * paramVector3f.z);
        paramVector3f.x = f1;
        paramVector3f.y = f2;
    }

    public final void setRotation(Matrix3d paramMatrix3d) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        this.m00 = ((float) (paramMatrix3d.m00 * arrayOfDouble2[0]));
        this.m01 = ((float) (paramMatrix3d.m01 * arrayOfDouble2[1]));
        this.m02 = ((float) (paramMatrix3d.m02 * arrayOfDouble2[2]));
        this.m10 = ((float) (paramMatrix3d.m10 * arrayOfDouble2[0]));
        this.m11 = ((float) (paramMatrix3d.m11 * arrayOfDouble2[1]));
        this.m12 = ((float) (paramMatrix3d.m12 * arrayOfDouble2[2]));
        this.m20 = ((float) (paramMatrix3d.m20 * arrayOfDouble2[0]));
        this.m21 = ((float) (paramMatrix3d.m21 * arrayOfDouble2[1]));
        this.m22 = ((float) (paramMatrix3d.m22 * arrayOfDouble2[2]));
    }

    public final void setRotation(Matrix3f paramMatrix3f) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        this.m00 = ((float) (paramMatrix3f.m00 * arrayOfDouble2[0]));
        this.m01 = ((float) (paramMatrix3f.m01 * arrayOfDouble2[1]));
        this.m02 = ((float) (paramMatrix3f.m02 * arrayOfDouble2[2]));
        this.m10 = ((float) (paramMatrix3f.m10 * arrayOfDouble2[0]));
        this.m11 = ((float) (paramMatrix3f.m11 * arrayOfDouble2[1]));
        this.m12 = ((float) (paramMatrix3f.m12 * arrayOfDouble2[2]));
        this.m20 = ((float) (paramMatrix3f.m20 * arrayOfDouble2[0]));
        this.m21 = ((float) (paramMatrix3f.m21 * arrayOfDouble2[1]));
        this.m22 = ((float) (paramMatrix3f.m22 * arrayOfDouble2[2]));
    }

    public final void setRotation(Quat4f paramQuat4f) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        this.m00 = ((float) ((1.0F - 2.0F * paramQuat4f.y * paramQuat4f.y - 2.0F * paramQuat4f.z * paramQuat4f.z) * arrayOfDouble2[0]));
        this.m10 = ((float) (2.0F * (paramQuat4f.x * paramQuat4f.y + paramQuat4f.w * paramQuat4f.z) * arrayOfDouble2[0]));
        this.m20 = ((float) (2.0F * (paramQuat4f.x * paramQuat4f.z - paramQuat4f.w * paramQuat4f.y) * arrayOfDouble2[0]));
        this.m01 = ((float) (2.0F * (paramQuat4f.x * paramQuat4f.y - paramQuat4f.w * paramQuat4f.z) * arrayOfDouble2[1]));
        this.m11 = ((float) ((1.0F - 2.0F * paramQuat4f.x * paramQuat4f.x - 2.0F * paramQuat4f.z * paramQuat4f.z) * arrayOfDouble2[1]));
        this.m21 = ((float) (2.0F * (paramQuat4f.y * paramQuat4f.z + paramQuat4f.w * paramQuat4f.x) * arrayOfDouble2[1]));
        this.m02 = ((float) (2.0F * (paramQuat4f.x * paramQuat4f.z + paramQuat4f.w * paramQuat4f.y) * arrayOfDouble2[2]));
        this.m12 = ((float) (2.0F * (paramQuat4f.y * paramQuat4f.z - paramQuat4f.w * paramQuat4f.x) * arrayOfDouble2[2]));
        this.m22 = ((float) ((1.0F - 2.0F * paramQuat4f.x * paramQuat4f.x - 2.0F * paramQuat4f.y * paramQuat4f.y) * arrayOfDouble2[2]));
    }

    public final void setRotation(Quat4d paramQuat4d) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        this.m00 = ((float) ((1.0D - 2.0D * paramQuat4d.y * paramQuat4d.y - 2.0D * paramQuat4d.z * paramQuat4d.z) * arrayOfDouble2[0]));
        this.m10 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.y + paramQuat4d.w * paramQuat4d.z) * arrayOfDouble2[0]));
        this.m20 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.z - paramQuat4d.w * paramQuat4d.y) * arrayOfDouble2[0]));
        this.m01 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.y - paramQuat4d.w * paramQuat4d.z) * arrayOfDouble2[1]));
        this.m11 = ((float) ((1.0D - 2.0D * paramQuat4d.x * paramQuat4d.x - 2.0D * paramQuat4d.z * paramQuat4d.z) * arrayOfDouble2[1]));
        this.m21 = ((float) (2.0D * (paramQuat4d.y * paramQuat4d.z + paramQuat4d.w * paramQuat4d.x) * arrayOfDouble2[1]));
        this.m02 = ((float) (2.0D * (paramQuat4d.x * paramQuat4d.z + paramQuat4d.w * paramQuat4d.y) * arrayOfDouble2[2]));
        this.m12 = ((float) (2.0D * (paramQuat4d.y * paramQuat4d.z - paramQuat4d.w * paramQuat4d.x) * arrayOfDouble2[2]));
        this.m22 = ((float) ((1.0D - 2.0D * paramQuat4d.x * paramQuat4d.x - 2.0D * paramQuat4d.y * paramQuat4d.y) * arrayOfDouble2[2]));
    }

    public final void setRotation(AxisAngle4f paramAxisAngle4f) {
        double[] arrayOfDouble1 = new double[9];
        double[] arrayOfDouble2 = new double[3];
        getScaleRotate(arrayOfDouble2, arrayOfDouble1);
        double d1 = Math.sqrt(paramAxisAngle4f.x * paramAxisAngle4f.x + paramAxisAngle4f.y * paramAxisAngle4f.y + paramAxisAngle4f.z * paramAxisAngle4f.z);
        if (d1 < 1.0E-8D) {
            this.m00 = 1.0F;
            this.m01 = 0.0F;
            this.m02 = 0.0F;
            this.m10 = 0.0F;
            this.m11 = 1.0F;
            this.m12 = 0.0F;
            this.m20 = 0.0F;
            this.m21 = 0.0F;
            this.m22 = 1.0F;
        } else {
            d1 = 1.0D / d1;
            double d2 = paramAxisAngle4f.x * d1;
            double d3 = paramAxisAngle4f.y * d1;
            double d4 = paramAxisAngle4f.z * d1;
            double d5 = Math.sin(paramAxisAngle4f.angle);
            double d6 = Math.cos(paramAxisAngle4f.angle);
            double d7 = 1.0D - d6;
            double d8 = paramAxisAngle4f.x * paramAxisAngle4f.z;
            double d9 = paramAxisAngle4f.x * paramAxisAngle4f.y;
            double d10 = paramAxisAngle4f.y * paramAxisAngle4f.z;
            this.m00 = ((float) ((d7 * d2 * d2 + d6) * arrayOfDouble2[0]));
            this.m01 = ((float) ((d7 * d9 - d5 * d4) * arrayOfDouble2[1]));
            this.m02 = ((float) ((d7 * d8 + d5 * d3) * arrayOfDouble2[2]));
            this.m10 = ((float) ((d7 * d9 + d5 * d4) * arrayOfDouble2[0]));
            this.m11 = ((float) ((d7 * d3 * d3 + d6) * arrayOfDouble2[1]));
            this.m12 = ((float) ((d7 * d10 - d5 * d2) * arrayOfDouble2[2]));
            this.m20 = ((float) ((d7 * d8 - d5 * d3) * arrayOfDouble2[0]));
            this.m21 = ((float) ((d7 * d10 + d5 * d2) * arrayOfDouble2[1]));
            this.m22 = ((float) ((d7 * d4 * d4 + d6) * arrayOfDouble2[2]));
        }
    }

    public final void setZero() {
        this.m00 = 0.0F;
        this.m01 = 0.0F;
        this.m02 = 0.0F;
        this.m03 = 0.0F;
        this.m10 = 0.0F;
        this.m11 = 0.0F;
        this.m12 = 0.0F;
        this.m13 = 0.0F;
        this.m20 = 0.0F;
        this.m21 = 0.0F;
        this.m22 = 0.0F;
        this.m23 = 0.0F;
        this.m30 = 0.0F;
        this.m31 = 0.0F;
        this.m32 = 0.0F;
        this.m33 = 0.0F;
    }

    public final void negate() {
        this.m00 = (-this.m00);
        this.m01 = (-this.m01);
        this.m02 = (-this.m02);
        this.m03 = (-this.m03);
        this.m10 = (-this.m10);
        this.m11 = (-this.m11);
        this.m12 = (-this.m12);
        this.m13 = (-this.m13);
        this.m20 = (-this.m20);
        this.m21 = (-this.m21);
        this.m22 = (-this.m22);
        this.m23 = (-this.m23);
        this.m30 = (-this.m30);
        this.m31 = (-this.m31);
        this.m32 = (-this.m32);
        this.m33 = (-this.m33);
    }

    public final void negate(Matrix4f paramMatrix4f) {
        this.m00 = (-paramMatrix4f.m00);
        this.m01 = (-paramMatrix4f.m01);
        this.m02 = (-paramMatrix4f.m02);
        this.m03 = (-paramMatrix4f.m03);
        this.m10 = (-paramMatrix4f.m10);
        this.m11 = (-paramMatrix4f.m11);
        this.m12 = (-paramMatrix4f.m12);
        this.m13 = (-paramMatrix4f.m13);
        this.m20 = (-paramMatrix4f.m20);
        this.m21 = (-paramMatrix4f.m21);
        this.m22 = (-paramMatrix4f.m22);
        this.m23 = (-paramMatrix4f.m23);
        this.m30 = (-paramMatrix4f.m30);
        this.m31 = (-paramMatrix4f.m31);
        this.m32 = (-paramMatrix4f.m32);
        this.m33 = (-paramMatrix4f.m33);
    }

    private final void getScaleRotate(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2) {
        double[] arrayOfDouble = new double[9];
        arrayOfDouble[0] = this.m00;
        arrayOfDouble[1] = this.m01;
        arrayOfDouble[2] = this.m02;
        arrayOfDouble[3] = this.m10;
        arrayOfDouble[4] = this.m11;
        arrayOfDouble[5] = this.m12;
        arrayOfDouble[6] = this.m20;
        arrayOfDouble[7] = this.m21;
        arrayOfDouble[8] = this.m22;
        Matrix3d.compute_svd(arrayOfDouble, paramArrayOfDouble1, paramArrayOfDouble2);
    }

    public Object clone() {
        Matrix4f localMatrix4f = null;
        try {
            localMatrix4f = (Matrix4f) super.clone();
        } catch (CloneNotSupportedException localCloneNotSupportedException) {
            throw new InternalError();
        }
        return localMatrix4f;
    }
}




