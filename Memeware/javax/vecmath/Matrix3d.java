
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Matrix3d
        implements Serializable {
    public double m00;
    public double m01;
    public double m02;
    public double m10;
    public double m11;
    public double m12;
    public double m20;
    public double m21;
    public double m22;

    public Matrix3d(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        this.set(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    public Matrix3d(double[] v) {
        this.set(v);
    }

    public Matrix3d(Matrix3d m1) {
        this.set(m1);
    }

    public Matrix3d(Matrix3f m1) {
        this.set(m1);
    }

    public Matrix3d() {
        this.setZero();
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        return "[" + nl + "  [" + this.m00 + "\t" + this.m01 + "\t" + this.m02 + "]" + nl + "  [" + this.m10 + "\t" + this.m11 + "\t" + this.m12 + "]" + nl + "  [" + this.m20 + "\t" + this.m21 + "\t" + this.m22 + "] ]";
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

    public final void setScale(double scale) {
        this.SVD(this);
        this.m00 *= scale;
        this.m11 *= scale;
        this.m22 *= scale;
    }

    public final void setElement(int row, int column, double value) {
        if (row == 0) {
            if (column == 0) {
                this.m00 = value;
            } else if (column == 1) {
                this.m01 = value;
            } else {
                if (column != 2) {
                    throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
                }
                this.m02 = value;
            }
        } else if (row == 1) {
            if (column == 0) {
                this.m10 = value;
            } else if (column == 1) {
                this.m11 = value;
            } else {
                if (column != 2) {
                    throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
                }
                this.m12 = value;
            }
        } else {
            if (row != 2) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
            }
            if (column == 0) {
                this.m20 = value;
            } else if (column == 1) {
                this.m21 = value;
            } else {
                if (column != 2) {
                    throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
                }
                this.m22 = value;
            }
        }
    }

    public final double getElement(int row, int column) {
        if (row == 0) {
            if (column == 0) {
                return this.m00;
            }
            if (column == 1) {
                return this.m01;
            }
            if (column == 2) {
                return this.m02;
            }
            throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
        }
        if (row == 1) {
            if (column == 0) {
                return this.m10;
            }
            if (column == 1) {
                return this.m11;
            }
            if (column == 2) {
                return this.m12;
            }
            throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
        }
        if (row != 2) {
            throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
        }
        if (column == 0) {
            return this.m20;
        }
        if (column == 1) {
            return this.m21;
        }
        if (column == 2) {
            return this.m22;
        }
        throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
    }

    public final void setRow(int row, double x, double y2, double z) {
        if (row == 0) {
            this.m00 = x;
            this.m01 = y2;
            this.m02 = z;
        } else if (row == 1) {
            this.m10 = x;
            this.m11 = y2;
            this.m12 = z;
        } else {
            if (row != 2) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
            }
            this.m20 = x;
            this.m21 = y2;
            this.m22 = z;
        }
    }

    public final void setRow(int row, Vector3d v) {
        if (row == 0) {
            this.m00 = v.x;
            this.m01 = v.y;
            this.m02 = v.z;
        } else if (row == 1) {
            this.m10 = v.x;
            this.m11 = v.y;
            this.m12 = v.z;
        } else {
            if (row != 2) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
            }
            this.m20 = v.x;
            this.m21 = v.y;
            this.m22 = v.z;
        }
    }

    public final void setRow(int row, double[] v) {
        if (row == 0) {
            this.m00 = v[0];
            this.m01 = v[1];
            this.m02 = v[2];
        } else if (row == 1) {
            this.m10 = v[0];
            this.m11 = v[1];
            this.m12 = v[2];
        } else {
            if (row != 2) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
            }
            this.m20 = v[0];
            this.m21 = v[1];
            this.m22 = v[2];
        }
    }

    public final void getRow(int row, double[] v) {
        if (row == 0) {
            v[0] = this.m00;
            v[1] = this.m01;
            v[2] = this.m02;
        } else if (row == 1) {
            v[0] = this.m10;
            v[1] = this.m11;
            v[2] = this.m12;
        } else {
            if (row != 2) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
            }
            v[0] = this.m20;
            v[1] = this.m21;
            v[2] = this.m22;
        }
    }

    public final void getRow(int row, Vector3d v) {
        if (row == 0) {
            v.x = this.m00;
            v.y = this.m01;
            v.z = this.m02;
        } else if (row == 1) {
            v.x = this.m10;
            v.y = this.m11;
            v.z = this.m12;
        } else {
            if (row != 2) {
                throw new ArrayIndexOutOfBoundsException("row must be 0 to 2 and is " + row);
            }
            v.x = this.m20;
            v.y = this.m21;
            v.z = this.m22;
        }
    }

    public final void setColumn(int column, double x, double y2, double z) {
        if (column == 0) {
            this.m00 = x;
            this.m10 = y2;
            this.m20 = z;
        } else if (column == 1) {
            this.m01 = x;
            this.m11 = y2;
            this.m21 = z;
        } else {
            if (column != 2) {
                throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
            }
            this.m02 = x;
            this.m12 = y2;
            this.m22 = z;
        }
    }

    public final void setColumn(int column, Vector3d v) {
        if (column == 0) {
            this.m00 = v.x;
            this.m10 = v.y;
            this.m20 = v.z;
        } else if (column == 1) {
            this.m01 = v.x;
            this.m11 = v.y;
            this.m21 = v.z;
        } else {
            if (column != 2) {
                throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
            }
            this.m02 = v.x;
            this.m12 = v.y;
            this.m22 = v.z;
        }
    }

    public final void setColumn(int column, double[] v) {
        if (column == 0) {
            this.m00 = v[0];
            this.m10 = v[1];
            this.m20 = v[2];
        } else if (column == 1) {
            this.m01 = v[0];
            this.m11 = v[1];
            this.m21 = v[2];
        } else {
            if (column != 2) {
                throw new ArrayIndexOutOfBoundsException("col must be 0 to 2 and is " + column);
            }
            this.m02 = v[0];
            this.m12 = v[1];
            this.m22 = v[2];
        }
    }

    public final void getColumn(int column, Vector3d v) {
        if (column == 0) {
            v.x = this.m00;
            v.y = this.m10;
            v.z = this.m20;
        } else if (column == 1) {
            v.x = this.m01;
            v.y = this.m11;
            v.z = this.m21;
        } else {
            if (column != 2) {
                throw new ArrayIndexOutOfBoundsException("column must be 0 to 2 and is " + column);
            }
            v.x = this.m02;
            v.y = this.m12;
            v.z = this.m22;
        }
    }

    public final void getColumn(int column, double[] v) {
        if (column == 0) {
            v[0] = this.m00;
            v[1] = this.m10;
            v[2] = this.m20;
        } else if (column == 1) {
            v[0] = this.m01;
            v[1] = this.m11;
            v[2] = this.m21;
        } else {
            if (column != 2) {
                throw new ArrayIndexOutOfBoundsException("column must be 0 to 2 and is " + column);
            }
            v[0] = this.m02;
            v[1] = this.m12;
            v[2] = this.m22;
        }
    }

    public final double getScale() {
        return this.SVD(null);
    }

    public final void add(double scalar) {
        this.m00 += scalar;
        this.m01 += scalar;
        this.m02 += scalar;
        this.m10 += scalar;
        this.m11 += scalar;
        this.m12 += scalar;
        this.m20 += scalar;
        this.m21 += scalar;
        this.m22 += scalar;
    }

    public final void add(double scalar, Matrix3d m1) {
        this.set(m1);
        this.add(scalar);
    }

    public final void add(Matrix3d m1, Matrix3d m2) {
        this.set(m1.m00 + m2.m00, m1.m01 + m2.m01, m1.m02 + m2.m02, m1.m10 + m2.m10, m1.m11 + m2.m11, m1.m12 + m2.m12, m1.m20 + m2.m20, m1.m21 + m2.m21, m1.m22 + m2.m22);
    }

    public final void add(Matrix3d m1) {
        this.m00 += m1.m00;
        this.m01 += m1.m01;
        this.m02 += m1.m02;
        this.m10 += m1.m10;
        this.m11 += m1.m11;
        this.m12 += m1.m12;
        this.m20 += m1.m20;
        this.m21 += m1.m21;
        this.m22 += m1.m22;
    }

    public final void sub(Matrix3d m1, Matrix3d m2) {
        this.set(m1.m00 - m2.m00, m1.m01 - m2.m01, m1.m02 - m2.m02, m1.m10 - m2.m10, m1.m11 - m2.m11, m1.m12 - m2.m12, m1.m20 - m2.m20, m1.m21 - m2.m21, m1.m22 - m2.m22);
    }

    public final void sub(Matrix3d m1) {
        this.m00 -= m1.m00;
        this.m01 -= m1.m01;
        this.m02 -= m1.m02;
        this.m10 -= m1.m10;
        this.m11 -= m1.m11;
        this.m12 -= m1.m12;
        this.m20 -= m1.m20;
        this.m21 -= m1.m21;
        this.m22 -= m1.m22;
    }

    public final void transpose() {
        double tmp = this.m01;
        this.m01 = this.m10;
        this.m10 = tmp;
        tmp = this.m02;
        this.m02 = this.m20;
        this.m20 = tmp;
        tmp = this.m12;
        this.m12 = this.m21;
        this.m21 = tmp;
    }

    public final void transpose(Matrix3d m1) {
        this.set(m1);
        this.transpose();
    }

    public final void set(Quat4d q1) {
        this.setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }

    public final void set(AxisAngle4d a1) {
        this.setFromAxisAngle(a1.x, a1.y, a1.z, a1.angle);
    }

    public final void set(Quat4f q1) {
        this.setFromQuat(q1.x, q1.y, q1.z, q1.w);
    }

    public final void set(AxisAngle4f a1) {
        this.setFromAxisAngle(a1.x, a1.y, a1.z, a1.angle);
    }

    public final void set(Matrix3d m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
    }

    public final void set(Matrix3f m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
    }

    public final void set(double[] m) {
        this.m00 = m[0];
        this.m01 = m[1];
        this.m02 = m[2];
        this.m10 = m[3];
        this.m11 = m[4];
        this.m12 = m[5];
        this.m20 = m[6];
        this.m21 = m[7];
        this.m22 = m[8];
    }

    public final void invert(Matrix3d m1) {
        this.set(m1);
        this.invert();
    }

    public final void invert() {
        double s = this.determinant();
        if (s == 0.0) {
            return;
        }
        s = 1.0 / s;
        this.set(this.m11 * this.m22 - this.m12 * this.m21, this.m02 * this.m21 - this.m01 * this.m22, this.m01 * this.m12 - this.m02 * this.m11, this.m12 * this.m20 - this.m10 * this.m22, this.m00 * this.m22 - this.m02 * this.m20, this.m02 * this.m10 - this.m00 * this.m12, this.m10 * this.m21 - this.m11 * this.m20, this.m01 * this.m20 - this.m00 * this.m21, this.m00 * this.m11 - this.m01 * this.m10);
        this.mul(s);
    }

    public final double determinant() {
        return this.m00 * (this.m11 * this.m22 - this.m21 * this.m12) - this.m01 * (this.m10 * this.m22 - this.m20 * this.m12) + this.m02 * (this.m10 * this.m21 - this.m20 * this.m11);
    }

    public final void set(double scale) {
        this.m00 = scale;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = scale;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = scale;
    }

    public final void rotX(double angle) {
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = c2;
        this.m12 = -s;
        this.m20 = 0.0;
        this.m21 = s;
        this.m22 = c2;
    }

    public final void rotY(double angle) {
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        this.m00 = c2;
        this.m01 = 0.0;
        this.m02 = s;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m20 = -s;
        this.m21 = 0.0;
        this.m22 = c2;
    }

    public final void rotZ(double angle) {
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        this.m00 = c2;
        this.m01 = -s;
        this.m02 = 0.0;
        this.m10 = s;
        this.m11 = c2;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
    }

    public final void mul(double scalar) {
        this.m00 *= scalar;
        this.m01 *= scalar;
        this.m02 *= scalar;
        this.m10 *= scalar;
        this.m11 *= scalar;
        this.m12 *= scalar;
        this.m20 *= scalar;
        this.m21 *= scalar;
        this.m22 *= scalar;
    }

    public final void mul(double scalar, Matrix3d m1) {
        this.set(m1);
        this.mul(scalar);
    }

    public final void mul(Matrix3d m1) {
        this.mul(this, m1);
    }

    public final void mul(Matrix3d m1, Matrix3d m2) {
        this.set(m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20, m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21, m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22, m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20, m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21, m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22, m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20, m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21, m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22);
    }

    public final void mulNormalize(Matrix3d m1) {
        this.mul(m1);
        this.SVD(this);
    }

    public final void mulNormalize(Matrix3d m1, Matrix3d m2) {
        this.mul(m1, m2);
        this.SVD(this);
    }

    public final void mulTransposeBoth(Matrix3d m1, Matrix3d m2) {
        this.mul(m2, m1);
        this.transpose();
    }

    public final void mulTransposeRight(Matrix3d m1, Matrix3d m2) {
        this.set(m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02, m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12, m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22, m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02, m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12, m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22, m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02, m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12, m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22);
    }

    public final void mulTransposeLeft(Matrix3d m1, Matrix3d m2) {
        this.set(m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20, m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21, m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22, m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20, m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21, m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22, m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20, m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21, m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22);
    }

    public final void normalize() {
        this.SVD(this);
    }

    public final void normalize(Matrix3d m1) {
        this.set(m1);
        this.SVD(this);
    }

    public final void normalizeCP() {
        double s = Math.pow(Math.abs(this.determinant()), -0.3333333333333333);
        this.mul(s);
    }

    public final void normalizeCP(Matrix3d m1) {
        this.set(m1);
        this.normalizeCP();
    }

    public boolean equals(Matrix3d m1) {
        return m1 != null && this.m00 == m1.m00 && this.m01 == m1.m01 && this.m02 == m1.m02 && this.m10 == m1.m10 && this.m11 == m1.m11 && this.m12 == m1.m12 && this.m20 == m1.m20 && this.m21 == m1.m21 && this.m22 == m1.m22;
    }

    public boolean equals(Object o1) {
        return o1 != null && o1 instanceof Matrix3d && this.equals((Matrix3d) o1);
    }

    public boolean epsilonEquals(Matrix3d m1, double epsilon) {
        return Math.abs(this.m00 - m1.m00) <= epsilon && Math.abs(this.m01 - m1.m01) <= epsilon && Math.abs(this.m02 - m1.m02) <= epsilon && Math.abs(this.m10 - m1.m10) <= epsilon && Math.abs(this.m11 - m1.m11) <= epsilon && Math.abs(this.m12 - m1.m12) <= epsilon && Math.abs(this.m20 - m1.m20) <= epsilon && Math.abs(this.m21 - m1.m21) <= epsilon && Math.abs(this.m22 - m1.m22) <= epsilon;
    }

    public int hashCode() {
        long bits = Double.doubleToLongBits(this.m00);
        int hash = (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m01);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m02);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m10);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m11);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m12);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m20);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m21);
        hash ^= (int) (bits ^ bits >> 32);
        bits = Double.doubleToLongBits(this.m22);
        return hash ^= (int) (bits ^ bits >> 32);
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

    public final void negate(Matrix3d m1) {
        this.set(m1);
        this.negate();
    }

    public final void transform(Tuple3d t) {
        this.transform(t, t);
    }

    public final void transform(Tuple3d t, Tuple3d result) {
        result.set(this.m00 * t.x + this.m01 * t.y + this.m02 * t.z, this.m10 * t.x + this.m11 * t.y + this.m12 * t.z, this.m20 * t.x + this.m21 * t.y + this.m22 * t.z);
    }

    private void set(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    private double SVD(Matrix3d rot) {
        double s = Math.sqrt((this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20 + this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21 + this.m02 * this.m02 + this.m12 * this.m12 + this.m22 * this.m22) / 3.0);
        if (rot != null) {
            double n = 1.0 / Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20);
            rot.m00 = this.m00 * n;
            rot.m10 = this.m10 * n;
            rot.m20 = this.m20 * n;
            n = 1.0 / Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
            rot.m01 = this.m01 * n;
            rot.m11 = this.m11 * n;
            rot.m21 = this.m21 * n;
            n = 1.0 / Math.sqrt(this.m02 * this.m02 + this.m12 * this.m12 + this.m22 * this.m22);
            rot.m02 = this.m02 * n;
            rot.m12 = this.m12 * n;
            rot.m22 = this.m22 * n;
        }
        return s;
    }

    private void setFromQuat(double x, double y2, double z, double w) {
        double n = x * x + y2 * y2 + z * z + w * w;
        double s = n > 0.0 ? 2.0 / n : 0.0;
        double xs = x * s;
        double ys = y2 * s;
        double zs = z * s;
        double wx = w * xs;
        double wy = w * ys;
        double wz = w * zs;
        double xx = x * xs;
        double xy = x * ys;
        double xz = x * zs;
        double yy = y2 * ys;
        double yz = y2 * zs;
        double zz = z * zs;
        this.m00 = 1.0 - (yy + zz);
        this.m01 = xy - wz;
        this.m02 = xz + wy;
        this.m10 = xy + wz;
        this.m11 = 1.0 - (xx + zz);
        this.m12 = yz - wx;
        this.m20 = xz - wy;
        this.m21 = yz + wx;
        this.m22 = 1.0 - (xx + yy);
    }

    private void setFromAxisAngle(double x, double y2, double z, double angle) {
        double n = Math.sqrt(x * x + y2 * y2 + z * z);
        n = 1.0 / n;
        double c2 = Math.cos(angle);
        double s = Math.sin(angle);
        double omc = 1.0 - c2;
        this.m00 = c2 + (x *= n) * x * omc;
        this.m11 = c2 + (y2 *= n) * y2 * omc;
        this.m22 = c2 + (z *= n) * z * omc;
        double tmp1 = x * y2 * omc;
        double tmp2 = z * s;
        this.m01 = tmp1 - tmp2;
        this.m10 = tmp1 + tmp2;
        tmp1 = x * z * omc;
        tmp2 = y2 * s;
        this.m02 = tmp1 + tmp2;
        this.m20 = tmp1 - tmp2;
        tmp1 = y2 * z * omc;
        tmp2 = x * s;
        this.m12 = tmp1 - tmp2;
        this.m21 = tmp1 + tmp2;
    }
}

