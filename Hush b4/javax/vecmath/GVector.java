// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class GVector implements Serializable, Cloneable
{
    private int length;
    double[] values;
    static final long serialVersionUID = 1398850036893875112L;
    
    public GVector(final int length) {
        this.length = length;
        this.values = new double[length];
        for (int i = 0; i < length; ++i) {
            this.values[i] = 0.0;
        }
    }
    
    public GVector(final double[] array) {
        this.length = array.length;
        this.values = new double[array.length];
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = array[i];
        }
    }
    
    public GVector(final GVector gVector) {
        this.values = new double[gVector.length];
        this.length = gVector.length;
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = gVector.values[i];
        }
    }
    
    public GVector(final Tuple2f tuple2f) {
        (this.values = new double[2])[0] = tuple2f.x;
        this.values[1] = tuple2f.y;
        this.length = 2;
    }
    
    public GVector(final Tuple3f tuple3f) {
        (this.values = new double[3])[0] = tuple3f.x;
        this.values[1] = tuple3f.y;
        this.values[2] = tuple3f.z;
        this.length = 3;
    }
    
    public GVector(final Tuple3d tuple3d) {
        (this.values = new double[3])[0] = tuple3d.x;
        this.values[1] = tuple3d.y;
        this.values[2] = tuple3d.z;
        this.length = 3;
    }
    
    public GVector(final Tuple4f tuple4f) {
        (this.values = new double[4])[0] = tuple4f.x;
        this.values[1] = tuple4f.y;
        this.values[2] = tuple4f.z;
        this.values[3] = tuple4f.w;
        this.length = 4;
    }
    
    public GVector(final Tuple4d tuple4d) {
        (this.values = new double[4])[0] = tuple4d.x;
        this.values[1] = tuple4d.y;
        this.values[2] = tuple4d.z;
        this.values[3] = tuple4d.w;
        this.length = 4;
    }
    
    public GVector(final double[] array, final int length) {
        this.length = length;
        this.values = new double[length];
        for (int i = 0; i < length; ++i) {
            this.values[i] = array[i];
        }
    }
    
    public final double norm() {
        double a = 0.0;
        for (int i = 0; i < this.length; ++i) {
            a += this.values[i] * this.values[i];
        }
        return Math.sqrt(a);
    }
    
    public final double normSquared() {
        double n = 0.0;
        for (int i = 0; i < this.length; ++i) {
            n += this.values[i] * this.values[i];
        }
        return n;
    }
    
    public final void normalize(final GVector gVector) {
        double a = 0.0;
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector0"));
        }
        for (int i = 0; i < this.length; ++i) {
            a += gVector.values[i] * gVector.values[i];
        }
        final double n = 1.0 / Math.sqrt(a);
        for (int j = 0; j < this.length; ++j) {
            this.values[j] = gVector.values[j] * n;
        }
    }
    
    public final void normalize() {
        double a = 0.0;
        for (int i = 0; i < this.length; ++i) {
            a += this.values[i] * this.values[i];
        }
        final double n = 1.0 / Math.sqrt(a);
        for (int j = 0; j < this.length; ++j) {
            this.values[j] *= n;
        }
    }
    
    public final void scale(final double n, final GVector gVector) {
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector1"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = gVector.values[i] * n;
        }
    }
    
    public final void scale(final double n) {
        for (int i = 0; i < this.length; ++i) {
            this.values[i] *= n;
        }
    }
    
    public final void scaleAdd(final double n, final GVector gVector, final GVector gVector2) {
        if (gVector2.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector2"));
        }
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector3"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = gVector.values[i] * n + gVector2.values[i];
        }
    }
    
    public final void add(final GVector gVector) {
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector4"));
        }
        for (int i = 0; i < this.length; ++i) {
            final double[] values = this.values;
            final int n = i;
            values[n] += gVector.values[i];
        }
    }
    
    public final void add(final GVector gVector, final GVector gVector2) {
        if (gVector.length != gVector2.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector5"));
        }
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector6"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = gVector.values[i] + gVector2.values[i];
        }
    }
    
    public final void sub(final GVector gVector) {
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector7"));
        }
        for (int i = 0; i < this.length; ++i) {
            final double[] values = this.values;
            final int n = i;
            values[n] -= gVector.values[i];
        }
    }
    
    public final void sub(final GVector gVector, final GVector gVector2) {
        if (gVector.length != gVector2.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector8"));
        }
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector9"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = gVector.values[i] - gVector2.values[i];
        }
    }
    
    public final void mul(final GMatrix gMatrix, final GVector gVector) {
        if (gMatrix.getNumCol() != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector10"));
        }
        if (this.length != gMatrix.getNumRow()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector11"));
        }
        double[] values;
        if (gVector != this) {
            values = gVector.values;
        }
        else {
            values = this.values.clone();
        }
        for (int i = this.length - 1; i >= 0; --i) {
            this.values[i] = 0.0;
            for (int j = gVector.length - 1; j >= 0; --j) {
                final double[] values2 = this.values;
                final int n = i;
                values2[n] += gMatrix.values[i][j] * values[j];
            }
        }
    }
    
    public final void mul(final GVector gVector, final GMatrix gMatrix) {
        if (gMatrix.getNumRow() != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector12"));
        }
        if (this.length != gMatrix.getNumCol()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector13"));
        }
        double[] values;
        if (gVector != this) {
            values = gVector.values;
        }
        else {
            values = this.values.clone();
        }
        for (int i = this.length - 1; i >= 0; --i) {
            this.values[i] = 0.0;
            for (int j = gVector.length - 1; j >= 0; --j) {
                final double[] values2 = this.values;
                final int n = i;
                values2[n] += gMatrix.values[j][i] * values[j];
            }
        }
    }
    
    public final void negate() {
        for (int i = this.length - 1; i >= 0; --i) {
            final double[] values = this.values;
            final int n = i;
            values[n] *= -1.0;
        }
    }
    
    public final void zero() {
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }
    
    public final void setSize(final int length) {
        final double[] values = new double[length];
        int length2;
        if (this.length < length) {
            length2 = this.length;
        }
        else {
            length2 = length;
        }
        for (int i = 0; i < length2; ++i) {
            values[i] = this.values[i];
        }
        this.length = length;
        this.values = values;
    }
    
    public final void set(final double[] array) {
        for (int i = this.length - 1; i >= 0; --i) {
            this.values[i] = array[i];
        }
    }
    
    public final void set(final GVector gVector) {
        if (this.length < gVector.length) {
            this.length = gVector.length;
            this.values = new double[this.length];
            for (int i = 0; i < this.length; ++i) {
                this.values[i] = gVector.values[i];
            }
        }
        else {
            for (int j = 0; j < gVector.length; ++j) {
                this.values[j] = gVector.values[j];
            }
            for (int k = gVector.length; k < this.length; ++k) {
                this.values[k] = 0.0;
            }
        }
    }
    
    public final void set(final Tuple2f tuple2f) {
        if (this.length < 2) {
            this.length = 2;
            this.values = new double[2];
        }
        this.values[0] = tuple2f.x;
        this.values[1] = tuple2f.y;
        for (int i = 2; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }
    
    public final void set(final Tuple3f tuple3f) {
        if (this.length < 3) {
            this.length = 3;
            this.values = new double[3];
        }
        this.values[0] = tuple3f.x;
        this.values[1] = tuple3f.y;
        this.values[2] = tuple3f.z;
        for (int i = 3; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }
    
    public final void set(final Tuple3d tuple3d) {
        if (this.length < 3) {
            this.length = 3;
            this.values = new double[3];
        }
        this.values[0] = tuple3d.x;
        this.values[1] = tuple3d.y;
        this.values[2] = tuple3d.z;
        for (int i = 3; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }
    
    public final void set(final Tuple4f tuple4f) {
        if (this.length < 4) {
            this.length = 4;
            this.values = new double[4];
        }
        this.values[0] = tuple4f.x;
        this.values[1] = tuple4f.y;
        this.values[2] = tuple4f.z;
        this.values[3] = tuple4f.w;
        for (int i = 4; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }
    
    public final void set(final Tuple4d tuple4d) {
        if (this.length < 4) {
            this.length = 4;
            this.values = new double[4];
        }
        this.values[0] = tuple4d.x;
        this.values[1] = tuple4d.y;
        this.values[2] = tuple4d.z;
        this.values[3] = tuple4d.w;
        for (int i = 4; i < this.length; ++i) {
            this.values[i] = 0.0;
        }
    }
    
    public final int getSize() {
        return this.values.length;
    }
    
    public final double getElement(final int n) {
        return this.values[n];
    }
    
    public final void setElement(final int n, final double n2) {
        this.values[n] = n2;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.length * 8);
        for (int i = 0; i < this.length; ++i) {
            sb.append(this.values[i]).append(" ");
        }
        return sb.toString();
    }
    
    public int hashCode() {
        long n = 1L;
        for (int i = 0; i < this.length; ++i) {
            n = 31L * n + VecMathUtil.doubleToLongBits(this.values[i]);
        }
        return (int)(n ^ n >> 32);
    }
    
    public boolean equals(final GVector gVector) {
        try {
            if (this.length != gVector.length) {
                return false;
            }
            for (int i = 0; i < this.length; ++i) {
                if (this.values[i] != gVector.values[i]) {
                    return false;
                }
            }
            return true;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean equals(final Object o) {
        try {
            final GVector gVector = (GVector)o;
            if (this.length != gVector.length) {
                return false;
            }
            for (int i = 0; i < this.length; ++i) {
                if (this.values[i] != gVector.values[i]) {
                    return false;
                }
            }
            return true;
        }
        catch (ClassCastException ex) {
            return false;
        }
        catch (NullPointerException ex2) {
            return false;
        }
    }
    
    public boolean epsilonEquals(final GVector gVector, final double n) {
        if (this.length != gVector.length) {
            return false;
        }
        for (int i = 0; i < this.length; ++i) {
            final double n2 = this.values[i] - gVector.values[i];
            if (((n2 < 0.0) ? (-n2) : n2) > n) {
                return false;
            }
        }
        return true;
    }
    
    public final double dot(final GVector gVector) {
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector14"));
        }
        double n = 0.0;
        for (int i = 0; i < this.length; ++i) {
            n += this.values[i] * gVector.values[i];
        }
        return n;
    }
    
    public final void SVDBackSolve(final GMatrix gMatrix, final GMatrix gMatrix2, final GMatrix gMatrix3, final GVector gVector) {
        if (gMatrix.nRow != gVector.getSize() || gMatrix.nRow != gMatrix.nCol || gMatrix.nRow != gMatrix2.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector15"));
        }
        if (gMatrix2.nCol != this.values.length || gMatrix2.nCol != gMatrix3.nCol || gMatrix2.nCol != gMatrix3.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector23"));
        }
        final GMatrix gMatrix4 = new GMatrix(gMatrix.nRow, gMatrix2.nCol);
        gMatrix4.mul(gMatrix, gMatrix3);
        gMatrix4.mulTransposeRight(gMatrix, gMatrix2);
        gMatrix4.invert();
        this.mul(gMatrix4, gVector);
    }
    
    public final void LUDBackSolve(final GMatrix gMatrix, final GVector gVector, final GVector gVector2) {
        final int n = gMatrix.nRow * gMatrix.nCol;
        final double[] array = new double[n];
        final double[] array2 = new double[n];
        final int[] array3 = new int[gVector.getSize()];
        if (gMatrix.nRow != gVector.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector16"));
        }
        if (gMatrix.nRow != gVector2.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector24"));
        }
        if (gMatrix.nRow != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector25"));
        }
        for (int i = 0; i < gMatrix.nRow; ++i) {
            for (int j = 0; j < gMatrix.nCol; ++j) {
                array[i * gMatrix.nCol + j] = gMatrix.values[i][j];
            }
        }
        for (int k = 0; k < n; ++k) {
            array2[k] = 0.0;
        }
        for (int l = 0; l < gMatrix.nRow; ++l) {
            array2[l * gMatrix.nCol] = gVector.values[l];
        }
        for (int n2 = 0; n2 < gMatrix.nCol; ++n2) {
            array3[n2] = (int)gVector2.values[n2];
        }
        GMatrix.luBacksubstitution(gMatrix.nRow, array, array3, array2);
        for (int n3 = 0; n3 < gMatrix.nRow; ++n3) {
            this.values[n3] = array2[n3 * gMatrix.nCol];
        }
    }
    
    public final double angle(final GVector gVector) {
        return Math.acos(this.dot(gVector) / (this.norm() * gVector.norm()));
    }
    
    public final void interpolate(final GVector gVector, final GVector gVector2, final float n) {
        this.interpolate(gVector, gVector2, (double)n);
    }
    
    public final void interpolate(final GVector gVector, final float n) {
        this.interpolate(gVector, (double)n);
    }
    
    public final void interpolate(final GVector gVector, final GVector gVector2, final double n) {
        if (gVector2.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector20"));
        }
        if (this.length != gVector.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector21"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = (1.0 - n) * gVector.values[i] + n * gVector2.values[i];
        }
    }
    
    public final void interpolate(final GVector gVector, final double n) {
        if (gVector.length != this.length) {
            throw new MismatchedSizeException(VecMathI18N.getString("GVector22"));
        }
        for (int i = 0; i < this.length; ++i) {
            this.values[i] = (1.0 - n) * this.values[i] + n * gVector.values[i];
        }
    }
    
    public Object clone() {
        GVector gVector;
        try {
            gVector = (GVector)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        gVector.values = new double[this.length];
        for (int i = 0; i < this.length; ++i) {
            gVector.values[i] = this.values[i];
        }
        return gVector;
    }
}
