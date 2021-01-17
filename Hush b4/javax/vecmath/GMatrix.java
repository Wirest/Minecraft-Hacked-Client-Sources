// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class GMatrix implements Serializable, Cloneable
{
    static final long serialVersionUID = 2777097312029690941L;
    private static final boolean debug = false;
    int nRow;
    int nCol;
    double[][] values;
    private static final double EPS = 1.0E-10;
    
    public GMatrix(final int nRow, final int nCol) {
        this.values = new double[nRow][nCol];
        this.nRow = nRow;
        this.nCol = nCol;
        for (int i = 0; i < nRow; ++i) {
            for (int j = 0; j < nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
        int n;
        if (nRow < nCol) {
            n = nRow;
        }
        else {
            n = nCol;
        }
        for (int k = 0; k < n; ++k) {
            this.values[k][k] = 1.0;
        }
    }
    
    public GMatrix(final int nRow, final int nCol, final double[] array) {
        this.values = new double[nRow][nCol];
        this.nRow = nRow;
        this.nCol = nCol;
        for (int i = 0; i < nRow; ++i) {
            for (int j = 0; j < nCol; ++j) {
                this.values[i][j] = array[i * nCol + j];
            }
        }
    }
    
    public GMatrix(final GMatrix gMatrix) {
        this.nRow = gMatrix.nRow;
        this.nCol = gMatrix.nCol;
        this.values = new double[this.nRow][this.nCol];
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = gMatrix.values[i][j];
            }
        }
    }
    
    public final void mul(final GMatrix gMatrix) {
        if (this.nCol != gMatrix.nRow || this.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix0"));
        }
        final double[][] values = new double[this.nRow][this.nCol];
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                values[i][j] = 0.0;
                for (int k = 0; k < this.nCol; ++k) {
                    final double[] array = values[i];
                    final int n = j;
                    array[n] += this.values[i][k] * gMatrix.values[k][j];
                }
            }
        }
        this.values = values;
    }
    
    public final void mul(final GMatrix gMatrix, final GMatrix gMatrix2) {
        if (gMatrix.nCol != gMatrix2.nRow || this.nRow != gMatrix.nRow || this.nCol != gMatrix2.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix1"));
        }
        final double[][] values = new double[this.nRow][this.nCol];
        for (int i = 0; i < gMatrix.nRow; ++i) {
            for (int j = 0; j < gMatrix2.nCol; ++j) {
                values[i][j] = 0.0;
                for (int k = 0; k < gMatrix.nCol; ++k) {
                    final double[] array = values[i];
                    final int n = j;
                    array[n] += gMatrix.values[i][k] * gMatrix2.values[k][j];
                }
            }
        }
        this.values = values;
    }
    
    public final void mul(final GVector gVector, final GVector gVector2) {
        if (this.nRow < gVector.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix2"));
        }
        if (this.nCol < gVector2.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix3"));
        }
        for (int i = 0; i < gVector.getSize(); ++i) {
            for (int j = 0; j < gVector2.getSize(); ++j) {
                this.values[i][j] = gVector.values[i] * gVector2.values[j];
            }
        }
    }
    
    public final void add(final GMatrix gMatrix) {
        if (this.nRow != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix4"));
        }
        if (this.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix5"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] += gMatrix.values[i][j];
            }
        }
    }
    
    public final void add(final GMatrix gMatrix, final GMatrix gMatrix2) {
        if (gMatrix2.nRow != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix6"));
        }
        if (gMatrix2.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix7"));
        }
        if (this.nCol != gMatrix.nCol || this.nRow != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix8"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = gMatrix.values[i][j] + gMatrix2.values[i][j];
            }
        }
    }
    
    public final void sub(final GMatrix gMatrix) {
        if (this.nRow != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix9"));
        }
        if (this.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix28"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] -= gMatrix.values[i][j];
            }
        }
    }
    
    public final void sub(final GMatrix gMatrix, final GMatrix gMatrix2) {
        if (gMatrix2.nRow != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix10"));
        }
        if (gMatrix2.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix11"));
        }
        if (this.nRow != gMatrix.nRow || this.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix12"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = gMatrix.values[i][j] - gMatrix2.values[i][j];
            }
        }
    }
    
    public final void negate() {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = -this.values[i][j];
            }
        }
    }
    
    public final void negate(final GMatrix gMatrix) {
        if (this.nRow != gMatrix.nRow || this.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix13"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = -gMatrix.values[i][j];
            }
        }
    }
    
    public final void setIdentity() {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
        int n;
        if (this.nRow < this.nCol) {
            n = this.nRow;
        }
        else {
            n = this.nCol;
        }
        for (int k = 0; k < n; ++k) {
            this.values[k][k] = 1.0;
        }
    }
    
    public final void setZero() {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }
    
    public final void identityMinus() {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = -this.values[i][j];
            }
        }
        int n;
        if (this.nRow < this.nCol) {
            n = this.nRow;
        }
        else {
            n = this.nCol;
        }
        for (int k = 0; k < n; ++k) {
            final double[] array = this.values[k];
            final int n2 = k;
            ++array[n2];
        }
    }
    
    public final void invert() {
        this.invertGeneral(this);
    }
    
    public final void invert(final GMatrix gMatrix) {
        this.invertGeneral(gMatrix);
    }
    
    public final void copySubMatrix(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final GMatrix gMatrix) {
        if (this != gMatrix) {
            for (int i = 0; i < n3; ++i) {
                for (int j = 0; j < n4; ++j) {
                    gMatrix.values[n5 + i][n6 + j] = this.values[n + i][n2 + j];
                }
            }
        }
        else {
            final double[][] array = new double[n3][n4];
            for (int k = 0; k < n3; ++k) {
                for (int l = 0; l < n4; ++l) {
                    array[k][l] = this.values[n + k][n2 + l];
                }
            }
            for (int n7 = 0; n7 < n3; ++n7) {
                for (int n8 = 0; n8 < n4; ++n8) {
                    gMatrix.values[n5 + n7][n6 + n8] = array[n7][n8];
                }
            }
        }
    }
    
    public final void setSize(final int nRow, final int nCol) {
        final double[][] values = new double[nRow][nCol];
        int nRow2;
        if (this.nRow < nRow) {
            nRow2 = this.nRow;
        }
        else {
            nRow2 = nRow;
        }
        int nCol2;
        if (this.nCol < nCol) {
            nCol2 = this.nCol;
        }
        else {
            nCol2 = nCol;
        }
        for (int i = 0; i < nRow2; ++i) {
            for (int j = 0; j < nCol2; ++j) {
                values[i][j] = this.values[i][j];
            }
        }
        this.nRow = nRow;
        this.nCol = nCol;
        this.values = values;
    }
    
    public final void set(final double[] array) {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = array[this.nCol * i + j];
            }
        }
    }
    
    public final void set(final Matrix3f matrix3f) {
        if (this.nCol < 3 || this.nRow < 3) {
            this.nCol = 3;
            this.nRow = 3;
            this.values = new double[this.nRow][this.nCol];
        }
        this.values[0][0] = matrix3f.m00;
        this.values[0][1] = matrix3f.m01;
        this.values[0][2] = matrix3f.m02;
        this.values[1][0] = matrix3f.m10;
        this.values[1][1] = matrix3f.m11;
        this.values[1][2] = matrix3f.m12;
        this.values[2][0] = matrix3f.m20;
        this.values[2][1] = matrix3f.m21;
        this.values[2][2] = matrix3f.m22;
        for (int i = 3; i < this.nRow; ++i) {
            for (int j = 3; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }
    
    public final void set(final Matrix3d matrix3d) {
        if (this.nRow < 3 || this.nCol < 3) {
            this.values = new double[3][3];
            this.nRow = 3;
            this.nCol = 3;
        }
        this.values[0][0] = matrix3d.m00;
        this.values[0][1] = matrix3d.m01;
        this.values[0][2] = matrix3d.m02;
        this.values[1][0] = matrix3d.m10;
        this.values[1][1] = matrix3d.m11;
        this.values[1][2] = matrix3d.m12;
        this.values[2][0] = matrix3d.m20;
        this.values[2][1] = matrix3d.m21;
        this.values[2][2] = matrix3d.m22;
        for (int i = 3; i < this.nRow; ++i) {
            for (int j = 3; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }
    
    public final void set(final Matrix4f matrix4f) {
        if (this.nRow < 4 || this.nCol < 4) {
            this.values = new double[4][4];
            this.nRow = 4;
            this.nCol = 4;
        }
        this.values[0][0] = matrix4f.m00;
        this.values[0][1] = matrix4f.m01;
        this.values[0][2] = matrix4f.m02;
        this.values[0][3] = matrix4f.m03;
        this.values[1][0] = matrix4f.m10;
        this.values[1][1] = matrix4f.m11;
        this.values[1][2] = matrix4f.m12;
        this.values[1][3] = matrix4f.m13;
        this.values[2][0] = matrix4f.m20;
        this.values[2][1] = matrix4f.m21;
        this.values[2][2] = matrix4f.m22;
        this.values[2][3] = matrix4f.m23;
        this.values[3][0] = matrix4f.m30;
        this.values[3][1] = matrix4f.m31;
        this.values[3][2] = matrix4f.m32;
        this.values[3][3] = matrix4f.m33;
        for (int i = 4; i < this.nRow; ++i) {
            for (int j = 4; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }
    
    public final void set(final Matrix4d matrix4d) {
        if (this.nRow < 4 || this.nCol < 4) {
            this.values = new double[4][4];
            this.nRow = 4;
            this.nCol = 4;
        }
        this.values[0][0] = matrix4d.m00;
        this.values[0][1] = matrix4d.m01;
        this.values[0][2] = matrix4d.m02;
        this.values[0][3] = matrix4d.m03;
        this.values[1][0] = matrix4d.m10;
        this.values[1][1] = matrix4d.m11;
        this.values[1][2] = matrix4d.m12;
        this.values[1][3] = matrix4d.m13;
        this.values[2][0] = matrix4d.m20;
        this.values[2][1] = matrix4d.m21;
        this.values[2][2] = matrix4d.m22;
        this.values[2][3] = matrix4d.m23;
        this.values[3][0] = matrix4d.m30;
        this.values[3][1] = matrix4d.m31;
        this.values[3][2] = matrix4d.m32;
        this.values[3][3] = matrix4d.m33;
        for (int i = 4; i < this.nRow; ++i) {
            for (int j = 4; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
    }
    
    public final void set(final GMatrix gMatrix) {
        if (this.nRow < gMatrix.nRow || this.nCol < gMatrix.nCol) {
            this.nRow = gMatrix.nRow;
            this.nCol = gMatrix.nCol;
            this.values = new double[this.nRow][this.nCol];
        }
        for (int i = 0; i < Math.min(this.nRow, gMatrix.nRow); ++i) {
            for (int j = 0; j < Math.min(this.nCol, gMatrix.nCol); ++j) {
                this.values[i][j] = gMatrix.values[i][j];
            }
        }
        for (int k = gMatrix.nRow; k < this.nRow; ++k) {
            for (int l = gMatrix.nCol; l < this.nCol; ++l) {
                this.values[k][l] = 0.0;
            }
        }
    }
    
    public final int getNumRow() {
        return this.nRow;
    }
    
    public final int getNumCol() {
        return this.nCol;
    }
    
    public final double getElement(final int n, final int n2) {
        return this.values[n][n2];
    }
    
    public final void setElement(final int n, final int n2, final double n3) {
        this.values[n][n2] = n3;
    }
    
    public final void getRow(final int n, final double[] array) {
        for (int i = 0; i < this.nCol; ++i) {
            array[i] = this.values[n][i];
        }
    }
    
    public final void getRow(final int n, final GVector gVector) {
        if (gVector.getSize() < this.nCol) {
            gVector.setSize(this.nCol);
        }
        for (int i = 0; i < this.nCol; ++i) {
            gVector.values[i] = this.values[n][i];
        }
    }
    
    public final void getColumn(final int n, final double[] array) {
        for (int i = 0; i < this.nRow; ++i) {
            array[i] = this.values[i][n];
        }
    }
    
    public final void getColumn(final int n, final GVector gVector) {
        if (gVector.getSize() < this.nRow) {
            gVector.setSize(this.nRow);
        }
        for (int i = 0; i < this.nRow; ++i) {
            gVector.values[i] = this.values[i][n];
        }
    }
    
    public final void get(final Matrix3d matrix3d) {
        if (this.nRow < 3 || this.nCol < 3) {
            matrix3d.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    matrix3d.m00 = this.values[0][0];
                    if (this.nRow > 1) {
                        matrix3d.m10 = this.values[1][0];
                        if (this.nRow > 2) {
                            matrix3d.m20 = this.values[2][0];
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        matrix3d.m01 = this.values[0][1];
                        if (this.nRow > 1) {
                            matrix3d.m11 = this.values[1][1];
                            if (this.nRow > 2) {
                                matrix3d.m21 = this.values[2][1];
                            }
                        }
                    }
                    if (this.nCol > 2 && this.nRow > 0) {
                        matrix3d.m02 = this.values[0][2];
                        if (this.nRow > 1) {
                            matrix3d.m12 = this.values[1][2];
                            if (this.nRow > 2) {
                                matrix3d.m22 = this.values[2][2];
                            }
                        }
                    }
                }
            }
        }
        else {
            matrix3d.m00 = this.values[0][0];
            matrix3d.m01 = this.values[0][1];
            matrix3d.m02 = this.values[0][2];
            matrix3d.m10 = this.values[1][0];
            matrix3d.m11 = this.values[1][1];
            matrix3d.m12 = this.values[1][2];
            matrix3d.m20 = this.values[2][0];
            matrix3d.m21 = this.values[2][1];
            matrix3d.m22 = this.values[2][2];
        }
    }
    
    public final void get(final Matrix3f matrix3f) {
        if (this.nRow < 3 || this.nCol < 3) {
            matrix3f.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    matrix3f.m00 = (float)this.values[0][0];
                    if (this.nRow > 1) {
                        matrix3f.m10 = (float)this.values[1][0];
                        if (this.nRow > 2) {
                            matrix3f.m20 = (float)this.values[2][0];
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        matrix3f.m01 = (float)this.values[0][1];
                        if (this.nRow > 1) {
                            matrix3f.m11 = (float)this.values[1][1];
                            if (this.nRow > 2) {
                                matrix3f.m21 = (float)this.values[2][1];
                            }
                        }
                    }
                    if (this.nCol > 2 && this.nRow > 0) {
                        matrix3f.m02 = (float)this.values[0][2];
                        if (this.nRow > 1) {
                            matrix3f.m12 = (float)this.values[1][2];
                            if (this.nRow > 2) {
                                matrix3f.m22 = (float)this.values[2][2];
                            }
                        }
                    }
                }
            }
        }
        else {
            matrix3f.m00 = (float)this.values[0][0];
            matrix3f.m01 = (float)this.values[0][1];
            matrix3f.m02 = (float)this.values[0][2];
            matrix3f.m10 = (float)this.values[1][0];
            matrix3f.m11 = (float)this.values[1][1];
            matrix3f.m12 = (float)this.values[1][2];
            matrix3f.m20 = (float)this.values[2][0];
            matrix3f.m21 = (float)this.values[2][1];
            matrix3f.m22 = (float)this.values[2][2];
        }
    }
    
    public final void get(final Matrix4d matrix4d) {
        if (this.nRow < 4 || this.nCol < 4) {
            matrix4d.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    matrix4d.m00 = this.values[0][0];
                    if (this.nRow > 1) {
                        matrix4d.m10 = this.values[1][0];
                        if (this.nRow > 2) {
                            matrix4d.m20 = this.values[2][0];
                            if (this.nRow > 3) {
                                matrix4d.m30 = this.values[3][0];
                            }
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        matrix4d.m01 = this.values[0][1];
                        if (this.nRow > 1) {
                            matrix4d.m11 = this.values[1][1];
                            if (this.nRow > 2) {
                                matrix4d.m21 = this.values[2][1];
                                if (this.nRow > 3) {
                                    matrix4d.m31 = this.values[3][1];
                                }
                            }
                        }
                    }
                    if (this.nCol > 2) {
                        if (this.nRow > 0) {
                            matrix4d.m02 = this.values[0][2];
                            if (this.nRow > 1) {
                                matrix4d.m12 = this.values[1][2];
                                if (this.nRow > 2) {
                                    matrix4d.m22 = this.values[2][2];
                                    if (this.nRow > 3) {
                                        matrix4d.m32 = this.values[3][2];
                                    }
                                }
                            }
                        }
                        if (this.nCol > 3 && this.nRow > 0) {
                            matrix4d.m03 = this.values[0][3];
                            if (this.nRow > 1) {
                                matrix4d.m13 = this.values[1][3];
                                if (this.nRow > 2) {
                                    matrix4d.m23 = this.values[2][3];
                                    if (this.nRow > 3) {
                                        matrix4d.m33 = this.values[3][3];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            matrix4d.m00 = this.values[0][0];
            matrix4d.m01 = this.values[0][1];
            matrix4d.m02 = this.values[0][2];
            matrix4d.m03 = this.values[0][3];
            matrix4d.m10 = this.values[1][0];
            matrix4d.m11 = this.values[1][1];
            matrix4d.m12 = this.values[1][2];
            matrix4d.m13 = this.values[1][3];
            matrix4d.m20 = this.values[2][0];
            matrix4d.m21 = this.values[2][1];
            matrix4d.m22 = this.values[2][2];
            matrix4d.m23 = this.values[2][3];
            matrix4d.m30 = this.values[3][0];
            matrix4d.m31 = this.values[3][1];
            matrix4d.m32 = this.values[3][2];
            matrix4d.m33 = this.values[3][3];
        }
    }
    
    public final void get(final Matrix4f matrix4f) {
        if (this.nRow < 4 || this.nCol < 4) {
            matrix4f.setZero();
            if (this.nCol > 0) {
                if (this.nRow > 0) {
                    matrix4f.m00 = (float)this.values[0][0];
                    if (this.nRow > 1) {
                        matrix4f.m10 = (float)this.values[1][0];
                        if (this.nRow > 2) {
                            matrix4f.m20 = (float)this.values[2][0];
                            if (this.nRow > 3) {
                                matrix4f.m30 = (float)this.values[3][0];
                            }
                        }
                    }
                }
                if (this.nCol > 1) {
                    if (this.nRow > 0) {
                        matrix4f.m01 = (float)this.values[0][1];
                        if (this.nRow > 1) {
                            matrix4f.m11 = (float)this.values[1][1];
                            if (this.nRow > 2) {
                                matrix4f.m21 = (float)this.values[2][1];
                                if (this.nRow > 3) {
                                    matrix4f.m31 = (float)this.values[3][1];
                                }
                            }
                        }
                    }
                    if (this.nCol > 2) {
                        if (this.nRow > 0) {
                            matrix4f.m02 = (float)this.values[0][2];
                            if (this.nRow > 1) {
                                matrix4f.m12 = (float)this.values[1][2];
                                if (this.nRow > 2) {
                                    matrix4f.m22 = (float)this.values[2][2];
                                    if (this.nRow > 3) {
                                        matrix4f.m32 = (float)this.values[3][2];
                                    }
                                }
                            }
                        }
                        if (this.nCol > 3 && this.nRow > 0) {
                            matrix4f.m03 = (float)this.values[0][3];
                            if (this.nRow > 1) {
                                matrix4f.m13 = (float)this.values[1][3];
                                if (this.nRow > 2) {
                                    matrix4f.m23 = (float)this.values[2][3];
                                    if (this.nRow > 3) {
                                        matrix4f.m33 = (float)this.values[3][3];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            matrix4f.m00 = (float)this.values[0][0];
            matrix4f.m01 = (float)this.values[0][1];
            matrix4f.m02 = (float)this.values[0][2];
            matrix4f.m03 = (float)this.values[0][3];
            matrix4f.m10 = (float)this.values[1][0];
            matrix4f.m11 = (float)this.values[1][1];
            matrix4f.m12 = (float)this.values[1][2];
            matrix4f.m13 = (float)this.values[1][3];
            matrix4f.m20 = (float)this.values[2][0];
            matrix4f.m21 = (float)this.values[2][1];
            matrix4f.m22 = (float)this.values[2][2];
            matrix4f.m23 = (float)this.values[2][3];
            matrix4f.m30 = (float)this.values[3][0];
            matrix4f.m31 = (float)this.values[3][1];
            matrix4f.m32 = (float)this.values[3][2];
            matrix4f.m33 = (float)this.values[3][3];
        }
    }
    
    public final void get(final GMatrix gMatrix) {
        int n;
        if (this.nCol < gMatrix.nCol) {
            n = this.nCol;
        }
        else {
            n = gMatrix.nCol;
        }
        int n2;
        if (this.nRow < gMatrix.nRow) {
            n2 = this.nRow;
        }
        else {
            n2 = gMatrix.nRow;
        }
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                gMatrix.values[i][j] = this.values[i][j];
            }
        }
        for (int k = n2; k < gMatrix.nRow; ++k) {
            for (int l = 0; l < gMatrix.nCol; ++l) {
                gMatrix.values[k][l] = 0.0;
            }
        }
        for (int n3 = n; n3 < gMatrix.nCol; ++n3) {
            for (int n4 = 0; n4 < n2; ++n4) {
                gMatrix.values[n4][n3] = 0.0;
            }
        }
    }
    
    public final void setRow(final int n, final double[] array) {
        for (int i = 0; i < this.nCol; ++i) {
            this.values[n][i] = array[i];
        }
    }
    
    public final void setRow(final int n, final GVector gVector) {
        for (int i = 0; i < this.nCol; ++i) {
            this.values[n][i] = gVector.values[i];
        }
    }
    
    public final void setColumn(final int n, final double[] array) {
        for (int i = 0; i < this.nRow; ++i) {
            this.values[i][n] = array[i];
        }
    }
    
    public final void setColumn(final int n, final GVector gVector) {
        for (int i = 0; i < this.nRow; ++i) {
            this.values[i][n] = gVector.values[i];
        }
    }
    
    public final void mulTransposeBoth(final GMatrix gMatrix, final GMatrix gMatrix2) {
        if (gMatrix.nRow != gMatrix2.nCol || this.nRow != gMatrix.nCol || this.nCol != gMatrix2.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix14"));
        }
        if (gMatrix == this || gMatrix2 == this) {
            final double[][] values = new double[this.nRow][this.nCol];
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    values[i][j] = 0.0;
                    for (int k = 0; k < gMatrix.nRow; ++k) {
                        final double[] array = values[i];
                        final int n = j;
                        array[n] += gMatrix.values[k][i] * gMatrix2.values[j][k];
                    }
                }
            }
            this.values = values;
        }
        else {
            for (int l = 0; l < this.nRow; ++l) {
                for (int n2 = 0; n2 < this.nCol; ++n2) {
                    this.values[l][n2] = 0.0;
                    for (int n3 = 0; n3 < gMatrix.nRow; ++n3) {
                        final double[] array2 = this.values[l];
                        final int n4 = n2;
                        array2[n4] += gMatrix.values[n3][l] * gMatrix2.values[n2][n3];
                    }
                }
            }
        }
    }
    
    public final void mulTransposeRight(final GMatrix gMatrix, final GMatrix gMatrix2) {
        if (gMatrix.nCol != gMatrix2.nCol || this.nCol != gMatrix2.nRow || this.nRow != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix15"));
        }
        if (gMatrix == this || gMatrix2 == this) {
            final double[][] values = new double[this.nRow][this.nCol];
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    values[i][j] = 0.0;
                    for (int k = 0; k < gMatrix.nCol; ++k) {
                        final double[] array = values[i];
                        final int n = j;
                        array[n] += gMatrix.values[i][k] * gMatrix2.values[j][k];
                    }
                }
            }
            this.values = values;
        }
        else {
            for (int l = 0; l < this.nRow; ++l) {
                for (int n2 = 0; n2 < this.nCol; ++n2) {
                    this.values[l][n2] = 0.0;
                    for (int n3 = 0; n3 < gMatrix.nCol; ++n3) {
                        final double[] array2 = this.values[l];
                        final int n4 = n2;
                        array2[n4] += gMatrix.values[l][n3] * gMatrix2.values[n2][n3];
                    }
                }
            }
        }
    }
    
    public final void mulTransposeLeft(final GMatrix gMatrix, final GMatrix gMatrix2) {
        if (gMatrix.nRow != gMatrix2.nRow || this.nCol != gMatrix2.nCol || this.nRow != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix16"));
        }
        if (gMatrix == this || gMatrix2 == this) {
            final double[][] values = new double[this.nRow][this.nCol];
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    values[i][j] = 0.0;
                    for (int k = 0; k < gMatrix.nRow; ++k) {
                        final double[] array = values[i];
                        final int n = j;
                        array[n] += gMatrix.values[k][i] * gMatrix2.values[k][j];
                    }
                }
            }
            this.values = values;
        }
        else {
            for (int l = 0; l < this.nRow; ++l) {
                for (int n2 = 0; n2 < this.nCol; ++n2) {
                    this.values[l][n2] = 0.0;
                    for (int n3 = 0; n3 < gMatrix.nRow; ++n3) {
                        final double[] array2 = this.values[l];
                        final int n4 = n2;
                        array2[n4] += gMatrix.values[n3][l] * gMatrix2.values[n3][n2];
                    }
                }
            }
        }
    }
    
    public final void transpose() {
        if (this.nRow != this.nCol) {
            final int nRow = this.nRow;
            this.nRow = this.nCol;
            this.nCol = nRow;
            final double[][] values = new double[this.nRow][this.nCol];
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    values[i][j] = this.values[j][i];
                }
            }
            this.values = values;
        }
        else {
            for (int k = 0; k < this.nRow; ++k) {
                for (int l = 0; l < k; ++l) {
                    final double n = this.values[k][l];
                    this.values[k][l] = this.values[l][k];
                    this.values[l][k] = n;
                }
            }
        }
    }
    
    public final void transpose(final GMatrix gMatrix) {
        if (this.nRow != gMatrix.nCol || this.nCol != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix17"));
        }
        if (gMatrix != this) {
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    this.values[i][j] = gMatrix.values[j][i];
                }
            }
        }
        else {
            this.transpose();
        }
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.nRow * this.nCol * 8);
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                sb.append(this.values[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private static void checkMatrix(final GMatrix gMatrix) {
        for (int i = 0; i < gMatrix.nRow; ++i) {
            for (int j = 0; j < gMatrix.nCol; ++j) {
                if (Math.abs(gMatrix.values[i][j]) < 1.0E-10) {
                    System.out.print(" 0.0     ");
                }
                else {
                    System.out.print(" " + gMatrix.values[i][j]);
                }
            }
            System.out.print("\n");
        }
    }
    
    public int hashCode() {
        long n = 31L * (31L * 1L + this.nRow) + this.nCol;
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                n = 31L * n + VecMathUtil.doubleToLongBits(this.values[i][j]);
            }
        }
        return (int)(n ^ n >> 32);
    }
    
    public boolean equals(final GMatrix gMatrix) {
        try {
            if (this.nRow != gMatrix.nRow || this.nCol != gMatrix.nCol) {
                return false;
            }
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    if (this.values[i][j] != gMatrix.values[i][j]) {
                        return false;
                    }
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
            final GMatrix gMatrix = (GMatrix)o;
            if (this.nRow != gMatrix.nRow || this.nCol != gMatrix.nCol) {
                return false;
            }
            for (int i = 0; i < this.nRow; ++i) {
                for (int j = 0; j < this.nCol; ++j) {
                    if (this.values[i][j] != gMatrix.values[i][j]) {
                        return false;
                    }
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
    
    public boolean epsilonEquals(final GMatrix gMatrix, final float n) {
        return this.epsilonEquals(gMatrix, (double)n);
    }
    
    public boolean epsilonEquals(final GMatrix gMatrix, final double n) {
        if (this.nRow != gMatrix.nRow || this.nCol != gMatrix.nCol) {
            return false;
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                final double n2 = this.values[i][j] - gMatrix.values[i][j];
                if (((n2 < 0.0) ? (-n2) : n2) > n) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public final double trace() {
        int n;
        if (this.nRow < this.nCol) {
            n = this.nRow;
        }
        else {
            n = this.nCol;
        }
        double n2 = 0.0;
        for (int i = 0; i < n; ++i) {
            n2 += this.values[i][i];
        }
        return n2;
    }
    
    public final int SVD(final GMatrix gMatrix, final GMatrix gMatrix2, final GMatrix gMatrix3) {
        if (this.nCol != gMatrix3.nCol || this.nCol != gMatrix3.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix18"));
        }
        if (this.nRow != gMatrix.nRow || this.nRow != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix25"));
        }
        if (this.nRow != gMatrix2.nRow || this.nCol != gMatrix2.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix26"));
        }
        if (this.nRow != 2 || this.nCol != 2 || this.values[1][0] != 0.0) {
            return computeSVD(this, gMatrix, gMatrix2, gMatrix3);
        }
        gMatrix.setIdentity();
        gMatrix3.setIdentity();
        if (this.values[0][1] == 0.0) {
            return 2;
        }
        final double[] array = { 0.0 };
        final double[] array2 = { 0.0 };
        final double[] array3 = { 0.0 };
        final double[] array4 = { 0.0 };
        compute_2X2(this.values[0][0], this.values[0][1], this.values[1][1], new double[] { this.values[0][0], this.values[1][1] }, array, array3, array2, array4, 0);
        update_u(0, gMatrix, array3, array);
        update_v(0, gMatrix3, array4, array2);
        return 2;
    }
    
    public final int LUD(final GMatrix gMatrix, final GVector gVector) {
        final double[] array = new double[gMatrix.nRow * gMatrix.nCol];
        final int[] array2 = { 0 };
        final int[] array3 = new int[gMatrix.nRow];
        if (this.nRow != this.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix19"));
        }
        if (this.nRow != gMatrix.nRow) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix27"));
        }
        if (this.nCol != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix27"));
        }
        if (gMatrix.nRow != gVector.getSize()) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix20"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                array[i * this.nCol + j] = this.values[i][j];
            }
        }
        if (!luDecomposition(gMatrix.nRow, array, array3, array2)) {
            throw new SingularMatrixException(VecMathI18N.getString("GMatrix21"));
        }
        for (int k = 0; k < this.nRow; ++k) {
            for (int l = 0; l < this.nCol; ++l) {
                gMatrix.values[k][l] = array[k * this.nCol + l];
            }
        }
        for (int n = 0; n < gMatrix.nRow; ++n) {
            gVector.values[n] = array3[n];
        }
        return array2[0];
    }
    
    public final void setScale(final double n) {
        int n2;
        if (this.nRow < this.nCol) {
            n2 = this.nRow;
        }
        else {
            n2 = this.nCol;
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.values[i][j] = 0.0;
            }
        }
        for (int k = 0; k < n2; ++k) {
            this.values[k][k] = n;
        }
    }
    
    final void invertGeneral(final GMatrix gMatrix) {
        final int n = gMatrix.nRow * gMatrix.nCol;
        final double[] array = new double[n];
        final double[] array2 = new double[n];
        final int[] array3 = new int[gMatrix.nRow];
        final int[] array4 = { 0 };
        if (gMatrix.nRow != gMatrix.nCol) {
            throw new MismatchedSizeException(VecMathI18N.getString("GMatrix22"));
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                array[i * this.nCol + j] = gMatrix.values[i][j];
            }
        }
        if (!luDecomposition(gMatrix.nRow, array, array3, array4)) {
            throw new SingularMatrixException(VecMathI18N.getString("GMatrix21"));
        }
        for (int k = 0; k < n; ++k) {
            array2[k] = 0.0;
        }
        for (int l = 0; l < this.nCol; ++l) {
            array2[l + l * this.nCol] = 1.0;
        }
        luBacksubstitution(gMatrix.nRow, array, array3, array2);
        for (int n2 = 0; n2 < this.nRow; ++n2) {
            for (int n3 = 0; n3 < this.nCol; ++n3) {
                this.values[n2][n3] = array2[n2 * this.nCol + n3];
            }
        }
    }
    
    static boolean luDecomposition(final int n, final double[] array, final int[] array2, final int[] array3) {
        final double[] array4 = new double[n];
        int n2 = 0;
        int n3 = 0;
        array3[0] = 1;
        int n4 = n;
        while (n4-- != 0) {
            double n5 = 0.0;
            int n6 = n;
            while (n6-- != 0) {
                final double abs = Math.abs(array[n2++]);
                if (abs > n5) {
                    n5 = abs;
                }
            }
            if (n5 == 0.0) {
                return false;
            }
            array4[n3++] = 1.0 / n5;
        }
        final int n7 = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                final int n8 = n7 + n * j + i;
                double n9 = array[n8];
                int n10 = j;
                int n11 = n7 + n * j;
                int n12 = n7 + i;
                while (n10-- != 0) {
                    n9 -= array[n11] * array[n12];
                    ++n11;
                    n12 += n;
                }
                array[n8] = n9;
            }
            double n13 = 0.0;
            int n14 = -1;
            for (int k = i; k < n; ++k) {
                final int n15 = n7 + n * k + i;
                double a = array[n15];
                int n16 = i;
                int n17 = n7 + n * k;
                int n18 = n7 + i;
                while (n16-- != 0) {
                    a -= array[n17] * array[n18];
                    ++n17;
                    n18 += n;
                }
                array[n15] = a;
                final double n19;
                if ((n19 = array4[k] * Math.abs(a)) >= n13) {
                    n13 = n19;
                    n14 = k;
                }
            }
            if (n14 < 0) {
                throw new RuntimeException(VecMathI18N.getString("GMatrix24"));
            }
            if (i != n14) {
                int n20 = n;
                int n21 = n7 + n * n14;
                int n22 = n7 + n * i;
                while (n20-- != 0) {
                    final double n23 = array[n21];
                    array[n21++] = array[n22];
                    array[n22++] = n23;
                }
                array4[n14] = array4[i];
                array3[0] = -array3[0];
            }
            array2[i] = n14;
            if (array[n7 + n * i + i] == 0.0) {
                return false;
            }
            if (i != n - 1) {
                final double n24 = 1.0 / array[n7 + n * i + i];
                int n25 = n7 + n * (i + 1) + i;
                int n26 = n - 1 - i;
                while (n26-- != 0) {
                    final int n27 = n25;
                    array[n27] *= n24;
                    n25 += n;
                }
            }
        }
        return true;
    }
    
    static void luBacksubstitution(final int n, final double[] array, final int[] array2, final double[] array3) {
        final int n2 = 0;
        for (int i = 0; i < n; ++i) {
            final int n3 = i;
            int n4 = -1;
            for (int j = 0; j < n; ++j) {
                final int n5 = array2[n2 + j];
                double n6 = array3[n3 + n * n5];
                array3[n3 + n * n5] = array3[n3 + n * j];
                if (n4 >= 0) {
                    final int n7 = j * n;
                    for (int k = n4; k <= j - 1; ++k) {
                        n6 -= array[n7 + k] * array3[n3 + n * k];
                    }
                }
                else if (n6 != 0.0) {
                    n4 = j;
                }
                array3[n3 + n * j] = n6;
            }
            for (int l = 0; l < n; ++l) {
                final int n8 = n - 1 - l;
                final int n9 = n * n8;
                double n10 = 0.0;
                for (int n11 = 1; n11 <= l; ++n11) {
                    n10 += array[n9 + n - n11] * array3[n3 + n * (n - n11)];
                }
                array3[n3 + n * n8] = (array3[n3 + n * n8] - n10) / array[n9 + n8];
            }
        }
    }
    
    static int computeSVD(final GMatrix gMatrix, final GMatrix gMatrix2, final GMatrix gMatrix3, final GMatrix gMatrix4) {
        final GMatrix gMatrix5 = new GMatrix(gMatrix.nRow, gMatrix.nCol);
        final GMatrix gMatrix6 = new GMatrix(gMatrix.nRow, gMatrix.nCol);
        final GMatrix gMatrix7 = new GMatrix(gMatrix.nRow, gMatrix.nCol);
        final GMatrix gMatrix8 = new GMatrix(gMatrix);
        int n;
        int nRow;
        if (gMatrix8.nRow >= gMatrix8.nCol) {
            n = gMatrix8.nCol;
            nRow = gMatrix8.nCol - 1;
        }
        else {
            n = gMatrix8.nRow;
            nRow = gMatrix8.nRow;
        }
        int n2;
        if (gMatrix8.nRow > gMatrix8.nCol) {
            n2 = gMatrix8.nRow;
        }
        else {
            n2 = gMatrix8.nCol;
        }
        final double[] array = new double[n2];
        final double[] array2 = new double[n];
        final double[] array3 = new double[nRow];
        gMatrix2.setIdentity();
        gMatrix4.setIdentity();
        int nRow2 = gMatrix8.nRow;
        int nCol = gMatrix8.nCol;
        for (int i = 0; i < n; ++i) {
            if (nRow2 > 1) {
                double a = 0.0;
                for (int j = 0; j < nRow2; ++j) {
                    a += gMatrix8.values[j + i][i] * gMatrix8.values[j + i][i];
                }
                final double sqrt = Math.sqrt(a);
                if (gMatrix8.values[i][i] == 0.0) {
                    array[0] = sqrt;
                }
                else {
                    array[0] = gMatrix8.values[i][i] + d_sign(sqrt, gMatrix8.values[i][i]);
                }
                for (int k = 1; k < nRow2; ++k) {
                    array[k] = gMatrix8.values[i + k][i];
                }
                double n3 = 0.0;
                for (int l = 0; l < nRow2; ++l) {
                    n3 += array[l] * array[l];
                }
                final double n4 = 2.0 / n3;
                for (int n5 = i; n5 < gMatrix8.nRow; ++n5) {
                    for (int n6 = i; n6 < gMatrix8.nRow; ++n6) {
                        gMatrix6.values[n5][n6] = -n4 * array[n5 - i] * array[n6 - i];
                    }
                }
                for (int n7 = i; n7 < gMatrix8.nRow; ++n7) {
                    final double[] array4 = gMatrix6.values[n7];
                    final int n8 = n7;
                    ++array4[n8];
                }
                double n9 = 0.0;
                for (int n10 = i; n10 < gMatrix8.nRow; ++n10) {
                    n9 += gMatrix6.values[i][n10] * gMatrix8.values[n10][i];
                }
                gMatrix8.values[i][i] = n9;
                for (int n11 = i; n11 < gMatrix8.nRow; ++n11) {
                    for (int n12 = i + 1; n12 < gMatrix8.nCol; ++n12) {
                        gMatrix5.values[n11][n12] = 0.0;
                        for (int n13 = i; n13 < gMatrix8.nCol; ++n13) {
                            final double[] array5 = gMatrix5.values[n11];
                            final int n14 = n12;
                            array5[n14] += gMatrix6.values[n11][n13] * gMatrix8.values[n13][n12];
                        }
                    }
                }
                for (int n15 = i; n15 < gMatrix8.nRow; ++n15) {
                    for (int n16 = i + 1; n16 < gMatrix8.nCol; ++n16) {
                        gMatrix8.values[n15][n16] = gMatrix5.values[n15][n16];
                    }
                }
                for (int n17 = i; n17 < gMatrix8.nRow; ++n17) {
                    for (int n18 = 0; n18 < gMatrix8.nCol; ++n18) {
                        gMatrix5.values[n17][n18] = 0.0;
                        for (int n19 = i; n19 < gMatrix8.nCol; ++n19) {
                            final double[] array6 = gMatrix5.values[n17];
                            final int n20 = n18;
                            array6[n20] += gMatrix6.values[n17][n19] * gMatrix2.values[n19][n18];
                        }
                    }
                }
                for (int n21 = i; n21 < gMatrix8.nRow; ++n21) {
                    for (int n22 = 0; n22 < gMatrix8.nCol; ++n22) {
                        gMatrix2.values[n21][n22] = gMatrix5.values[n21][n22];
                    }
                }
                --nRow2;
            }
            if (nCol > 2) {
                double a2 = 0.0;
                for (int n23 = 1; n23 < nCol; ++n23) {
                    a2 += gMatrix8.values[i][i + n23] * gMatrix8.values[i][i + n23];
                }
                final double sqrt2 = Math.sqrt(a2);
                if (gMatrix8.values[i][i + 1] == 0.0) {
                    array[0] = sqrt2;
                }
                else {
                    array[0] = gMatrix8.values[i][i + 1] + d_sign(sqrt2, gMatrix8.values[i][i + 1]);
                }
                for (int n24 = 1; n24 < nCol - 1; ++n24) {
                    array[n24] = gMatrix8.values[i][i + n24 + 1];
                }
                double n25 = 0.0;
                for (int n26 = 0; n26 < nCol - 1; ++n26) {
                    n25 += array[n26] * array[n26];
                }
                final double n27 = 2.0 / n25;
                for (int n28 = i + 1; n28 < nCol; ++n28) {
                    for (int n29 = i + 1; n29 < gMatrix8.nCol; ++n29) {
                        gMatrix7.values[n28][n29] = -n27 * array[n28 - i - 1] * array[n29 - i - 1];
                    }
                }
                for (int n30 = i + 1; n30 < gMatrix8.nCol; ++n30) {
                    final double[] array7 = gMatrix7.values[n30];
                    final int n31 = n30;
                    ++array7[n31];
                }
                double n32 = 0.0;
                for (int n33 = i; n33 < gMatrix8.nCol; ++n33) {
                    n32 += gMatrix7.values[n33][i + 1] * gMatrix8.values[i][n33];
                }
                gMatrix8.values[i][i + 1] = n32;
                for (int n34 = i + 1; n34 < gMatrix8.nRow; ++n34) {
                    for (int n35 = i + 1; n35 < gMatrix8.nCol; ++n35) {
                        gMatrix5.values[n34][n35] = 0.0;
                        for (int n36 = i + 1; n36 < gMatrix8.nCol; ++n36) {
                            final double[] array8 = gMatrix5.values[n34];
                            final int n37 = n35;
                            array8[n37] += gMatrix7.values[n36][n35] * gMatrix8.values[n34][n36];
                        }
                    }
                }
                for (int n38 = i + 1; n38 < gMatrix8.nRow; ++n38) {
                    for (int n39 = i + 1; n39 < gMatrix8.nCol; ++n39) {
                        gMatrix8.values[n38][n39] = gMatrix5.values[n38][n39];
                    }
                }
                for (int n40 = 0; n40 < gMatrix8.nRow; ++n40) {
                    for (int n41 = i + 1; n41 < gMatrix8.nCol; ++n41) {
                        gMatrix5.values[n40][n41] = 0.0;
                        for (int n42 = i + 1; n42 < gMatrix8.nCol; ++n42) {
                            final double[] array9 = gMatrix5.values[n40];
                            final int n43 = n41;
                            array9[n43] += gMatrix7.values[n42][n41] * gMatrix4.values[n40][n42];
                        }
                    }
                }
                for (int n44 = 0; n44 < gMatrix8.nRow; ++n44) {
                    for (int n45 = i + 1; n45 < gMatrix8.nCol; ++n45) {
                        gMatrix4.values[n44][n45] = gMatrix5.values[n44][n45];
                    }
                }
                --nCol;
            }
        }
        for (int n46 = 0; n46 < n; ++n46) {
            array2[n46] = gMatrix8.values[n46][n46];
        }
        for (int n47 = 0; n47 < nRow; ++n47) {
            array3[n47] = gMatrix8.values[n47][n47 + 1];
        }
        if (gMatrix8.nRow == 2 && gMatrix8.nCol == 2) {
            final double[] array10 = { 0.0 };
            final double[] array11 = { 0.0 };
            final double[] array12 = { 0.0 };
            final double[] array13 = { 0.0 };
            compute_2X2(array2[0], array3[0], array2[1], array2, array12, array10, array13, array11, 0);
            update_u(0, gMatrix2, array10, array12);
            update_v(0, gMatrix4, array11, array13);
            return 2;
        }
        compute_qr(0, array3.length - 1, array2, array3, gMatrix2, gMatrix4);
        return array2.length;
    }
    
    static void compute_qr(final int n, int n2, final double[] array, final double[] array2, final GMatrix gMatrix, final GMatrix gMatrix2) {
        final double[] array3 = { 0.0 };
        final double[] array4 = { 0.0 };
        final double[] array5 = { 0.0 };
        final double[] array6 = { 0.0 };
        final GMatrix gMatrix3 = new GMatrix(gMatrix.nCol, gMatrix2.nRow);
        final double n3 = 1.0;
        int n4 = 0;
        double n5 = 0.0;
        double n6 = 0.0;
        for (int n7 = 0; n7 < 2 && n4 == 0; ++n7) {
            int i;
            for (i = n; i <= n2; ++i) {
                if (i == n) {
                    int n8;
                    if (array2.length == array.length) {
                        n8 = n2;
                    }
                    else {
                        n8 = n2 + 1;
                    }
                    final double compute_shift = compute_shift(array[n8 - 1], array2[n2], array[n8]);
                    n5 = (Math.abs(array[i]) - compute_shift) * (d_sign(n3, array[i]) + compute_shift / array[i]);
                    n6 = array2[i];
                }
                final double compute_rot = compute_rot(n5, n6, array6, array4);
                if (i != n) {
                    array2[i - 1] = compute_rot;
                }
                final double n9 = array4[0] * array[i] + array6[0] * array2[i];
                array2[i] = array4[0] * array2[i] - array6[0] * array[i];
                n6 = array6[0] * array[i + 1];
                array[i + 1] *= array4[0];
                update_v(i, gMatrix2, array4, array6);
                array[i] = compute_rot(n9, n6, array5, array3);
                n5 = array3[0] * array2[i] + array5[0] * array[i + 1];
                array[i + 1] = array3[0] * array[i + 1] - array5[0] * array2[i];
                if (i < n2) {
                    n6 = array5[0] * array2[i + 1];
                    array2[i + 1] *= array3[0];
                }
                update_u(i, gMatrix, array3, array5);
            }
            if (array.length == array2.length) {
                compute_rot(n5, n6, array6, array4);
                n5 = array4[0] * array[i] + array6[0] * array2[i];
                array2[i] = array4[0] * array2[i] - array6[0] * array[i];
                array[i + 1] *= array4[0];
                update_v(i, gMatrix2, array4, array6);
            }
            while (n2 - n > 1 && Math.abs(array2[n2]) < 4.89E-15) {
                --n2;
            }
            for (int j = n2 - 2; j > n; --j) {
                if (Math.abs(array2[j]) < 4.89E-15) {
                    compute_qr(j + 1, n2, array, array2, gMatrix, gMatrix2);
                    for (n2 = j - 1; n2 - n > 1 && Math.abs(array2[n2]) < 4.89E-15; --n2) {}
                }
            }
            if (n2 - n <= 1 && Math.abs(array2[n + 1]) < 4.89E-15) {
                n4 = 1;
            }
        }
        if (Math.abs(array2[1]) < 4.89E-15) {
            compute_2X2(array[n], array2[n], array[n + 1], array, array5, array3, array6, array4, 0);
            array2[n + 1] = (array2[n] = 0.0);
        }
        update_u(n, gMatrix, array3, array5);
        update_v(n, gMatrix2, array4, array6);
    }
    
    private static void print_se(final double[] array, final double[] array2) {
        System.out.println("\ns =" + array[0] + " " + array[1] + " " + array[2]);
        System.out.println("e =" + array2[0] + " " + array2[1]);
    }
    
    private static void update_v(final int n, final GMatrix gMatrix, final double[] array, final double[] array2) {
        for (int i = 0; i < gMatrix.nRow; ++i) {
            final double n2 = gMatrix.values[i][n];
            gMatrix.values[i][n] = array[0] * n2 + array2[0] * gMatrix.values[i][n + 1];
            gMatrix.values[i][n + 1] = -array2[0] * n2 + array[0] * gMatrix.values[i][n + 1];
        }
    }
    
    private static void chase_up(final double[] array, final double[] array2, final int n, final GMatrix gMatrix) {
        final double[] array3 = { 0.0 };
        final double[] array4 = { 0.0 };
        final GMatrix gMatrix2 = new GMatrix(gMatrix.nRow, gMatrix.nCol);
        final GMatrix gMatrix3 = new GMatrix(gMatrix.nRow, gMatrix.nCol);
        double n2 = array2[n];
        double n3 = array[n];
        int i;
        for (i = n; i > 0; --i) {
            final double compute_rot = compute_rot(n2, n3, array4, array3);
            n2 = -array2[i - 1] * array4[0];
            n3 = array[i - 1];
            array[i] = compute_rot;
            array2[i - 1] *= array3[0];
            update_v_split(i, n + 1, gMatrix, array3, array4, gMatrix2, gMatrix3);
        }
        array[i + 1] = compute_rot(n2, n3, array4, array3);
        update_v_split(i, n + 1, gMatrix, array3, array4, gMatrix2, gMatrix3);
    }
    
    private static void chase_across(final double[] array, final double[] array2, final int n, final GMatrix gMatrix) {
        final double[] array3 = { 0.0 };
        final double[] array4 = { 0.0 };
        final GMatrix gMatrix2 = new GMatrix(gMatrix.nRow, gMatrix.nCol);
        final GMatrix gMatrix3 = new GMatrix(gMatrix.nRow, gMatrix.nCol);
        double n2 = array2[n];
        double n3 = array[n + 1];
        int i;
        for (i = n; i < gMatrix.nCol - 2; ++i) {
            final double compute_rot = compute_rot(n3, n2, array4, array3);
            n2 = -array2[i + 1] * array4[0];
            n3 = array[i + 2];
            array[i + 1] = compute_rot;
            array2[i + 1] *= array3[0];
            update_u_split(n, i + 1, gMatrix, array3, array4, gMatrix2, gMatrix3);
        }
        array[i + 1] = compute_rot(n3, n2, array4, array3);
        update_u_split(n, i + 1, gMatrix, array3, array4, gMatrix2, gMatrix3);
    }
    
    private static void update_v_split(final int i, final int j, final GMatrix gMatrix, final double[] array, final double[] array2, final GMatrix gMatrix2, final GMatrix gMatrix3) {
        for (int k = 0; k < gMatrix.nRow; ++k) {
            final double n = gMatrix.values[k][i];
            gMatrix.values[k][i] = array[0] * n - array2[0] * gMatrix.values[k][j];
            gMatrix.values[k][j] = array2[0] * n + array[0] * gMatrix.values[k][j];
        }
        System.out.println("topr    =" + i);
        System.out.println("bottomr =" + j);
        System.out.println("cosr =" + array[0]);
        System.out.println("sinr =" + array2[0]);
        System.out.println("\nm =");
        checkMatrix(gMatrix3);
        System.out.println("\nv =");
        checkMatrix(gMatrix2);
        gMatrix3.mul(gMatrix3, gMatrix2);
        System.out.println("\nt*m =");
        checkMatrix(gMatrix3);
    }
    
    private static void update_u_split(final int n, final int n2, final GMatrix gMatrix, final double[] array, final double[] array2, final GMatrix gMatrix2, final GMatrix gMatrix3) {
        for (int i = 0; i < gMatrix.nCol; ++i) {
            final double n3 = gMatrix.values[n][i];
            gMatrix.values[n][i] = array[0] * n3 - array2[0] * gMatrix.values[n2][i];
            gMatrix.values[n2][i] = array2[0] * n3 + array[0] * gMatrix.values[n2][i];
        }
        System.out.println("\nm=");
        checkMatrix(gMatrix3);
        System.out.println("\nu=");
        checkMatrix(gMatrix2);
        gMatrix3.mul(gMatrix2, gMatrix3);
        System.out.println("\nt*m=");
        checkMatrix(gMatrix3);
    }
    
    private static void update_u(final int n, final GMatrix gMatrix, final double[] array, final double[] array2) {
        for (int i = 0; i < gMatrix.nCol; ++i) {
            final double n2 = gMatrix.values[n][i];
            gMatrix.values[n][i] = array[0] * n2 + array2[0] * gMatrix.values[n + 1][i];
            gMatrix.values[n + 1][i] = -array2[0] * n2 + array[0] * gMatrix.values[n + 1][i];
        }
    }
    
    private static void print_m(final GMatrix gMatrix, final GMatrix gMatrix2, final GMatrix gMatrix3) {
        final GMatrix gMatrix4 = new GMatrix(gMatrix.nCol, gMatrix.nRow);
        gMatrix4.mul(gMatrix2, gMatrix4);
        gMatrix4.mul(gMatrix4, gMatrix3);
        System.out.println("\n m = \n" + toString(gMatrix4));
    }
    
    private static String toString(final GMatrix gMatrix) {
        final StringBuffer sb = new StringBuffer(gMatrix.nRow * gMatrix.nCol * 8);
        for (int i = 0; i < gMatrix.nRow; ++i) {
            for (int j = 0; j < gMatrix.nCol; ++j) {
                if (Math.abs(gMatrix.values[i][j]) < 1.0E-9) {
                    sb.append("0.0000 ");
                }
                else {
                    sb.append(gMatrix.values[i][j]).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private static void print_svd(final double[] array, final double[] array2, final GMatrix gMatrix, final GMatrix gMatrix2) {
        final GMatrix gMatrix3 = new GMatrix(gMatrix.nCol, gMatrix2.nRow);
        System.out.println(" \ns = ");
        for (int i = 0; i < array.length; ++i) {
            System.out.println(" " + array[i]);
        }
        System.out.println(" \ne = ");
        for (int j = 0; j < array2.length; ++j) {
            System.out.println(" " + array2[j]);
        }
        System.out.println(" \nu  = \n" + gMatrix.toString());
        System.out.println(" \nv  = \n" + gMatrix2.toString());
        gMatrix3.setIdentity();
        for (int k = 0; k < array.length; ++k) {
            gMatrix3.values[k][k] = array[k];
        }
        for (int l = 0; l < array2.length; ++l) {
            gMatrix3.values[l][l + 1] = array2[l];
        }
        System.out.println(" \nm  = \n" + gMatrix3.toString());
        gMatrix3.mulTransposeLeft(gMatrix, gMatrix3);
        gMatrix3.mulTransposeRight(gMatrix3, gMatrix2);
        System.out.println(" \n u.transpose*m*v.transpose  = \n" + gMatrix3.toString());
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
                if (abs / abs3 < 1.0E-10) {
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
                    if (abs / abs3 < 1.0E-10) {
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
    
    static double compute_rot(final double a, final double a2, final double[] array, final double[] array2) {
        double n;
        double n2;
        double n3;
        if (a2 == 0.0) {
            n = 1.0;
            n2 = 0.0;
            n3 = a;
        }
        else if (a == 0.0) {
            n = 0.0;
            n2 = 1.0;
            n3 = a2;
        }
        else {
            double a3 = a;
            double a4 = a2;
            double n4 = max(Math.abs(a3), Math.abs(a4));
            if (n4 >= 4.9947976805055876E145) {
                int n5 = 0;
                while (n4 >= 4.9947976805055876E145) {
                    ++n5;
                    a3 *= 2.002083095183101E-146;
                    a4 *= 2.002083095183101E-146;
                    n4 = max(Math.abs(a3), Math.abs(a4));
                }
                n3 = Math.sqrt(a3 * a3 + a4 * a4);
                n = a3 / n3;
                n2 = a4 / n3;
                for (int i = 1; i <= n5; ++i) {
                    n3 *= 4.9947976805055876E145;
                }
            }
            else if (n4 <= 2.002083095183101E-146) {
                int n6 = 0;
                while (n4 <= 2.002083095183101E-146) {
                    ++n6;
                    a3 *= 4.9947976805055876E145;
                    a4 *= 4.9947976805055876E145;
                    n4 = max(Math.abs(a3), Math.abs(a4));
                }
                n3 = Math.sqrt(a3 * a3 + a4 * a4);
                n = a3 / n3;
                n2 = a4 / n3;
                for (int j = 1; j <= n6; ++j) {
                    n3 *= 2.002083095183101E-146;
                }
            }
            else {
                n3 = Math.sqrt(a3 * a3 + a4 * a4);
                n = a3 / n3;
                n2 = a4 / n3;
            }
            if (Math.abs(a) > Math.abs(a2) && n < 0.0) {
                n = -n;
                n2 = -n2;
                n3 = -n3;
            }
        }
        array[0] = n2;
        array2[0] = n;
        return n3;
    }
    
    static double d_sign(final double n, final double n2) {
        final double n3 = (n >= 0.0) ? n : (-n);
        return (n2 >= 0.0) ? n3 : (-n3);
    }
    
    public Object clone() {
        GMatrix gMatrix;
        try {
            gMatrix = (GMatrix)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        gMatrix.values = new double[this.nRow][this.nCol];
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                gMatrix.values[i][j] = this.values[i][j];
            }
        }
        return gMatrix;
    }
}
