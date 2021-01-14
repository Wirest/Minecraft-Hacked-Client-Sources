
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.GVector;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;

public class GMatrix
        implements Serializable {
    private double[] elementData;
    private int nRow;
    private int nCol;

    public GMatrix(int nRow, int nCol) {
        if (nRow < 0) {
            throw new NegativeArraySizeException(String.valueOf(String.valueOf(nRow)) + " < 0");
        }
        if (nCol < 0) {
            throw new NegativeArraySizeException(String.valueOf(String.valueOf(nCol)) + " < 0");
        }
        this.nRow = nRow;
        this.nCol = nCol;
        this.elementData = new double[nRow * nCol];
        this.setIdentity();
    }

    public GMatrix(int nRow, int nCol, double[] matrix) {
        if (nRow < 0) {
            throw new NegativeArraySizeException(String.valueOf(String.valueOf(nRow)) + " < 0");
        }
        if (nCol < 0) {
            throw new NegativeArraySizeException(String.valueOf(String.valueOf(nCol)) + " < 0");
        }
        this.nRow = nRow;
        this.nCol = nCol;
        this.elementData = new double[nRow * nCol];
        this.set(matrix);
    }

    public GMatrix(GMatrix matrix) {
        this.nRow = matrix.nRow;
        this.nCol = matrix.nCol;
        int newSize = this.nRow * this.nCol;
        this.elementData = new double[newSize];
        System.arraycopy(matrix.elementData, 0, this.elementData, 0, newSize);
    }

    public final void mul(GMatrix m1) {
        this.mul(this, m1);
    }

    public final void mul(GMatrix m1, GMatrix m2) {
        if (this.nRow != m1.nRow) {
            throw new ArrayIndexOutOfBoundsException("nRow:" + this.nRow + " != m1.nRow:" + m1.nRow);
        }
        if (this.nCol != m2.nCol) {
            throw new ArrayIndexOutOfBoundsException("nCol:" + this.nCol + " != m2.nCol:" + m2.nCol);
        }
        if (m1.nCol != m2.nRow) {
            throw new ArrayIndexOutOfBoundsException("m1.nCol:" + m1.nCol + " != m2.nRow:" + m2.nRow);
        }
        double[] newData = new double[this.nCol * this.nRow];
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                double sum = 0.0;
                for (int k = 0; k < m1.nCol; ++k) {
                    sum += m1.elementData[i * m1.nCol + k] * m2.elementData[k * m2.nCol + j];
                }
                newData[i * this.nCol + j] = sum;
            }
        }
        this.elementData = newData;
    }

    public final void mul(GVector v1, GVector v2) {
        if (this.nRow < v1.getSize()) {
            throw new IllegalArgumentException("nRow:" + this.nRow + " < v1.getSize():" + v1.getSize());
        }
        if (this.nCol < v2.getSize()) {
            throw new IllegalArgumentException("nCol:" + this.nCol + " < v2.getSize():" + v2.getSize());
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                this.elementData[i * this.nCol + j] = v1.getElement(i) * v2.getElement(j);
            }
        }
    }

    public final void add(GMatrix m1) {
        if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
            throw new IllegalArgumentException("this:(" + this.nRow + "x" + this.nCol + ") != m1:(" + m1.nRow + "x" + m1.nCol + ").");
        }
        for (int i = 0; i < this.nRow * this.nCol; ++i) {
            double[] elementData = this.elementData;
            int n = i;
            double[] arrd = elementData;
            int n2 = n;
            arrd[n2] = arrd[n2] + m1.elementData[i];
        }
    }

    public final void add(GMatrix m1, GMatrix m2) {
        if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
            throw new IllegalArgumentException("this:(" + this.nRow + "x" + this.nCol + ") != m1:(" + m1.nRow + "x" + m1.nCol + ").");
        }
        if (this.nRow != m2.nRow || this.nCol != m2.nCol) {
            throw new IllegalArgumentException("this:(" + this.nRow + "x" + this.nCol + ") != m2:(" + m2.nRow + "x" + m2.nCol + ").");
        }
        for (int i = 0; i < this.nRow * this.nCol; ++i) {
            this.elementData[i] = m1.elementData[i] + m2.elementData[i];
        }
    }

    public final void sub(GMatrix m1) {
        if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
            throw new IllegalArgumentException("this:(" + this.nRow + "x" + this.nCol + ") != m1:(" + m1.nRow + "x" + m1.nCol + ").");
        }
        for (int i = 0; i < this.nRow * this.nCol; ++i) {
            double[] elementData = this.elementData;
            int n = i;
            double[] arrd = elementData;
            int n2 = n;
            arrd[n2] = arrd[n2] - m1.elementData[i];
        }
    }

    public final void sub(GMatrix m1, GMatrix m2) {
        if (this.nRow != m1.nRow || this.nCol != m1.nCol) {
            throw new IllegalArgumentException("this:(" + this.nRow + "x" + this.nCol + ") != m1:(" + m1.nRow + "x" + m1.nCol + ").");
        }
        if (this.nRow != m2.nRow || this.nCol != m2.nCol) {
            throw new IllegalArgumentException("this:(" + this.nRow + "x" + this.nCol + ") != m2:(" + m2.nRow + "x" + m2.nCol + ").");
        }
        for (int i = 0; i < this.nRow * this.nCol; ++i) {
            this.elementData[i] = m1.elementData[i] - m2.elementData[i];
        }
    }

    public final void negate() {
        for (int i = 0; i < this.nRow * this.nCol; ++i) {
            this.elementData[i] = -this.elementData[i];
        }
    }

    public final void negate(GMatrix m1) {
        this.set(m1);
        this.negate();
    }

    public final void setIdentity() {
        this.setZero();
        int min = this.nRow < this.nCol ? this.nRow : this.nCol;
        for (int i = 0; i < min; ++i) {
            this.elementData[i * this.nCol + i] = 1.0;
        }
    }

    public final void setZero() {
        for (int i = 0; i < this.nRow * this.nCol; ++i) {
            this.elementData[i] = 0.0;
        }
    }

    public final void identityMinus() {
        this.negate();
        int min = this.nRow < this.nCol ? this.nRow : this.nCol;
        for (int i = 0; i < min; ++i) {
            double[] elementData = this.elementData;
            int n = i * this.nCol + i;
            double[] arrd = elementData;
            int n2 = n;
            arrd[n2] = arrd[n2] + 1.0;
        }
    }

    public final void invert() {
        if (this.nRow != this.nCol) {
            throw new ArrayIndexOutOfBoundsException("not a square matrix");
        }
        int n = this.nRow;
        GMatrix LU = new GMatrix(n, n);
        GVector permutation = new GVector(n);
        GVector column = new GVector(n);
        GVector unit = new GVector(n);
        this.LUD(LU, permutation);
        for (int j = 0; j < n; ++j) {
            unit.zero();
            unit.setElement(j, 1.0);
            column.LUDBackSolve(LU, unit, permutation);
            this.setColumn(j, column);
        }
    }

    public final void invert(GMatrix m1) {
        this.set(m1);
        this.invert();
    }

    public final void copySubMatrix(int rowSource, int colSource, int numRow, int numCol, int rowDest, int colDest, GMatrix target) {
        if (rowSource < 0 || colSource < 0 || rowDest < 0 || colDest < 0) {
            throw new ArrayIndexOutOfBoundsException("rowSource,colSource,rowDest,colDest < 0.");
        }
        if (this.nRow < numRow + rowSource || this.nCol < numCol + colSource) {
            throw new ArrayIndexOutOfBoundsException("Source GMatrix too small.");
        }
        if (target.nRow < numRow + rowDest || target.nCol < numCol + colDest) {
            throw new ArrayIndexOutOfBoundsException("Target GMatrix too small.");
        }
        for (int i = 0; i < numRow; ++i) {
            for (int j = 0; j < numCol; ++j) {
                target.elementData[(i + rowDest) * this.nCol + (j + colDest)] = this.elementData[(i + rowSource) * this.nCol + (j + colSource)];
            }
        }
    }

    public final void setSize(int nRow, int nCol) {
        if (nRow < 0 || nCol < 0) {
            throw new NegativeArraySizeException("nRow or nCol < 0");
        }
        int oldnRow = this.nRow;
        int oldnCol = this.nCol;
        int oldSize = this.nRow * this.nCol;
        this.nRow = nRow;
        this.nCol = nCol;
        int newSize = nRow * nCol;
        double[] oldData = this.elementData;
        if (oldnCol == nCol) {
            if (nRow <= oldnRow) {
                return;
            }
            this.elementData = new double[newSize];
            System.arraycopy(oldData, 0, this.elementData, 0, oldSize);
        } else {
            this.elementData = new double[newSize];
            this.setZero();
            for (int i = 0; i < oldnRow; ++i) {
                System.arraycopy(oldData, i * oldnCol, this.elementData, i * nCol, oldnCol);
            }
        }
    }

    public final void set(double[] matrix) {
        int size = this.nRow * this.nCol;
        System.arraycopy(matrix, 0, this.elementData, 0, size);
    }

    public final void set(Matrix3f m1) {
        this.elementData[0] = m1.m00;
        this.elementData[1] = m1.m01;
        this.elementData[2] = m1.m02;
        this.elementData[this.nCol] = m1.m10;
        this.elementData[this.nCol + 1] = m1.m11;
        this.elementData[this.nCol + 2] = m1.m12;
        this.elementData[2 * this.nCol] = m1.m20;
        this.elementData[2 * this.nCol + 1] = m1.m21;
        this.elementData[2 * this.nCol + 2] = m1.m22;
    }

    public final void set(Matrix3d m1) {
        this.elementData[0] = m1.m00;
        this.elementData[1] = m1.m01;
        this.elementData[2] = m1.m02;
        this.elementData[this.nCol] = m1.m10;
        this.elementData[this.nCol + 1] = m1.m11;
        this.elementData[this.nCol + 2] = m1.m12;
        this.elementData[2 * this.nCol] = m1.m20;
        this.elementData[2 * this.nCol + 1] = m1.m21;
        this.elementData[2 * this.nCol + 2] = m1.m22;
    }

    public final void set(Matrix4f m1) {
        this.elementData[0] = m1.m00;
        this.elementData[1] = m1.m01;
        this.elementData[2] = m1.m02;
        this.elementData[3] = m1.m03;
        this.elementData[this.nCol] = m1.m10;
        this.elementData[this.nCol + 1] = m1.m11;
        this.elementData[this.nCol + 2] = m1.m12;
        this.elementData[this.nCol + 3] = m1.m13;
        this.elementData[2 * this.nCol] = m1.m20;
        this.elementData[2 * this.nCol + 1] = m1.m21;
        this.elementData[2 * this.nCol + 2] = m1.m22;
        this.elementData[2 * this.nCol + 3] = m1.m23;
        this.elementData[3 * this.nCol] = m1.m30;
        this.elementData[3 * this.nCol + 1] = m1.m31;
        this.elementData[3 * this.nCol + 2] = m1.m32;
        this.elementData[3 * this.nCol + 3] = m1.m33;
    }

    public final void set(Matrix4d m1) {
        this.elementData[0] = m1.m00;
        this.elementData[1] = m1.m01;
        this.elementData[2] = m1.m02;
        this.elementData[3] = m1.m03;
        this.elementData[this.nCol] = m1.m10;
        this.elementData[this.nCol + 1] = m1.m11;
        this.elementData[this.nCol + 2] = m1.m12;
        this.elementData[this.nCol + 3] = m1.m13;
        this.elementData[2 * this.nCol] = m1.m20;
        this.elementData[2 * this.nCol + 1] = m1.m21;
        this.elementData[2 * this.nCol + 2] = m1.m22;
        this.elementData[2 * this.nCol + 3] = m1.m23;
        this.elementData[3 * this.nCol] = m1.m30;
        this.elementData[3 * this.nCol + 1] = m1.m31;
        this.elementData[3 * this.nCol + 2] = m1.m32;
        this.elementData[3 * this.nCol + 3] = m1.m33;
    }

    public final void set(GMatrix m1) {
        if (m1.nRow < this.nRow || m1.nCol < this.nCol) {
            throw new ArrayIndexOutOfBoundsException("m1 smaller than this matrix");
        }
        System.arraycopy(m1.elementData, 0, this.elementData, 0, this.nRow * this.nCol);
    }

    public final int getNumRow() {
        return this.nRow;
    }

    public final int getNumCol() {
        return this.nCol;
    }

    public final double getElement(int row, int column) {
        if (this.nRow <= row) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " > matrix's nRow:" + this.nRow);
        }
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " < 0");
        }
        if (this.nCol <= column) {
            throw new ArrayIndexOutOfBoundsException("column:" + column + " > matrix's nCol:" + this.nCol);
        }
        if (column < 0) {
            throw new ArrayIndexOutOfBoundsException("column:" + column + " < 0");
        }
        return this.elementData[row * this.nCol + column];
    }

    public final void setElement(int row, int column, double value) {
        if (this.nRow <= row) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " > matrix's nRow:" + this.nRow);
        }
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " < 0");
        }
        if (this.nCol <= column) {
            throw new ArrayIndexOutOfBoundsException("column:" + column + " > matrix's nCol:" + this.nCol);
        }
        if (column < 0) {
            throw new ArrayIndexOutOfBoundsException("column:" + column + " < 0");
        }
        this.elementData[row * this.nCol + column] = value;
    }

    public final void getRow(int row, double[] array) {
        if (this.nRow <= row) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " > matrix's nRow:" + this.nRow);
        }
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " < 0");
        }
        if (array.length < this.nCol) {
            throw new ArrayIndexOutOfBoundsException("array length:" + array.length + " smaller than matrix's nCol:" + this.nCol);
        }
        System.arraycopy(this.elementData, row * this.nCol, array, 0, this.nCol);
    }

    public final void getRow(int row, GVector vector) {
        if (this.nRow <= row) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " > matrix's nRow:" + this.nRow);
        }
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " < 0");
        }
        if (vector.getSize() < this.nCol) {
            throw new ArrayIndexOutOfBoundsException("vector size:" + vector.getSize() + " smaller than matrix's nCol:" + this.nCol);
        }
        for (int i = 0; i < this.nCol; ++i) {
            vector.setElement(i, this.elementData[row * this.nCol + i]);
        }
    }

    public final void getColumn(int col, double[] array) {
        if (this.nCol <= col) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " > matrix's nCol:" + this.nCol);
        }
        if (col < 0) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " < 0");
        }
        if (array.length < this.nRow) {
            throw new ArrayIndexOutOfBoundsException("array.length:" + array.length + " < matrix's nRow=" + this.nRow);
        }
        for (int i = 0; i < this.nRow; ++i) {
            array[i] = this.elementData[i * this.nCol + col];
        }
    }

    public final void getColumn(int col, GVector vector) {
        if (this.nCol <= col) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " > matrix's nCol:" + this.nCol);
        }
        if (col < 0) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " < 0");
        }
        if (vector.getSize() < this.nRow) {
            throw new ArrayIndexOutOfBoundsException("vector size:" + vector.getSize() + " < matrix's nRow:" + this.nRow);
        }
        for (int i = 0; i < this.nRow; ++i) {
            vector.setElement(i, this.elementData[i * this.nCol + col]);
        }
    }

    public final void get(Matrix3d m1) {
        m1.m00 = this.elementData[0];
        m1.m01 = this.elementData[1];
        m1.m02 = this.elementData[2];
        m1.m10 = this.elementData[this.nCol];
        m1.m11 = this.elementData[this.nCol + 1];
        m1.m12 = this.elementData[this.nCol + 2];
        m1.m20 = this.elementData[2 * this.nCol];
        m1.m21 = this.elementData[2 * this.nCol + 1];
        m1.m22 = this.elementData[2 * this.nCol + 2];
    }

    public final void get(Matrix3f m1) {
        m1.m00 = (float) this.elementData[0];
        m1.m01 = (float) this.elementData[1];
        m1.m02 = (float) this.elementData[2];
        m1.m10 = (float) this.elementData[this.nCol];
        m1.m11 = (float) this.elementData[this.nCol + 1];
        m1.m12 = (float) this.elementData[this.nCol + 2];
        m1.m20 = (float) this.elementData[2 * this.nCol];
        m1.m21 = (float) this.elementData[2 * this.nCol + 1];
        m1.m22 = (float) this.elementData[2 * this.nCol + 2];
    }

    public final void get(Matrix4d m1) {
        m1.m00 = this.elementData[0];
        m1.m01 = this.elementData[1];
        m1.m02 = this.elementData[2];
        m1.m03 = this.elementData[3];
        m1.m10 = this.elementData[this.nCol];
        m1.m11 = this.elementData[this.nCol + 1];
        m1.m12 = this.elementData[this.nCol + 2];
        m1.m13 = this.elementData[this.nCol + 3];
        m1.m20 = this.elementData[2 * this.nCol];
        m1.m21 = this.elementData[2 * this.nCol + 1];
        m1.m22 = this.elementData[2 * this.nCol + 2];
        m1.m23 = this.elementData[2 * this.nCol + 3];
        m1.m30 = this.elementData[3 * this.nCol];
        m1.m31 = this.elementData[3 * this.nCol + 1];
        m1.m32 = this.elementData[3 * this.nCol + 2];
        m1.m33 = this.elementData[3 * this.nCol + 3];
    }

    public final void get(Matrix4f m1) {
        m1.m00 = (float) this.elementData[0];
        m1.m01 = (float) this.elementData[1];
        m1.m02 = (float) this.elementData[2];
        m1.m03 = (float) this.elementData[3];
        m1.m10 = (float) this.elementData[this.nCol];
        m1.m11 = (float) this.elementData[this.nCol + 1];
        m1.m12 = (float) this.elementData[this.nCol + 2];
        m1.m13 = (float) this.elementData[this.nCol + 3];
        m1.m20 = (float) this.elementData[2 * this.nCol];
        m1.m21 = (float) this.elementData[2 * this.nCol + 1];
        m1.m22 = (float) this.elementData[2 * this.nCol + 2];
        m1.m23 = (float) this.elementData[2 * this.nCol + 3];
        m1.m30 = (float) this.elementData[3 * this.nCol];
        m1.m31 = (float) this.elementData[3 * this.nCol + 1];
        m1.m32 = (float) this.elementData[3 * this.nCol + 2];
        m1.m33 = (float) this.elementData[3 * this.nCol + 3];
    }

    public final void get(GMatrix m1) {
        if (m1.nRow < this.nRow || m1.nCol < this.nCol) {
            throw new IllegalArgumentException("m1 matrix is smaller than this matrix.");
        }
        if (m1.nCol == this.nCol) {
            System.arraycopy(this.elementData, 0, m1.elementData, 0, this.nRow * this.nCol);
        } else {
            for (int i = 0; i < this.nRow; ++i) {
                System.arraycopy(this.elementData, i * this.nCol, m1.elementData, i * m1.nCol, this.nCol);
            }
        }
    }

    public final void setRow(int row, double[] array) {
        if (this.nRow <= row) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " > matrix's nRow:" + this.nRow);
        }
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " < 0");
        }
        if (array.length < this.nCol) {
            throw new ArrayIndexOutOfBoundsException("array length:" + array.length + " < matrix's nCol=" + this.nCol);
        }
        System.arraycopy(array, 0, this.elementData, row * this.nCol, this.nCol);
    }

    public final void setRow(int row, GVector vector) {
        if (this.nRow <= row) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " > matrix's nRow:" + this.nRow);
        }
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("row:" + row + " < 0");
        }
        int vecSize = vector.getSize();
        if (vecSize < this.nCol) {
            throw new ArrayIndexOutOfBoundsException("vector's size:" + vecSize + " < matrix's nCol=" + this.nCol);
        }
        for (int i = 0; i < this.nCol; ++i) {
            this.elementData[row * this.nCol + i] = vector.getElement(i);
        }
    }

    public final void setColumn(int col, double[] array) {
        if (this.nCol <= col) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " > matrix's nCol=" + this.nCol);
        }
        if (col < 0) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " < 0");
        }
        if (array.length < this.nRow) {
            throw new ArrayIndexOutOfBoundsException("array length:" + array.length + " < matrix's nRow:" + this.nRow);
        }
        for (int i = 0; i < this.nRow; ++i) {
            this.elementData[i * this.nCol + col] = array[i];
        }
    }

    public final void setColumn(int col, GVector vector) {
        if (this.nCol <= col) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " > matrix's nCol=" + this.nCol);
        }
        if (col < 0) {
            throw new ArrayIndexOutOfBoundsException("col:" + col + " < 0");
        }
        int vecSize = vector.getSize();
        if (vecSize < this.nRow) {
            throw new ArrayIndexOutOfBoundsException("vector size:" + vecSize + " < matrix's nRow=" + this.nRow);
        }
        for (int i = 0; i < this.nRow; ++i) {
            this.elementData[i * this.nCol + col] = vector.getElement(i);
        }
    }

    public final void mulTransposeBoth(GMatrix m1, GMatrix m2) {
        this.mul(m2, m1);
        this.transpose();
    }

    public final void mulTransposeRight(GMatrix m1, GMatrix m2) {
        if (m1.nCol != m2.nCol || this.nRow != m1.nRow || this.nCol != m2.nRow) {
            throw new ArrayIndexOutOfBoundsException("matrices mismatch");
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                double sum = 0.0;
                for (int k = 0; k < m1.nCol; ++k) {
                    sum += m1.elementData[i * m1.nCol + k] * m2.elementData[j * m2.nCol + k];
                }
                this.elementData[i * this.nCol + j] = sum;
            }
        }
    }

    public final void mulTransposeLeft(GMatrix m1, GMatrix m2) {
        this.transpose(m1);
        this.mul(m2);
    }

    public final void transpose() {
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = i + 1; j < this.nCol; ++j) {
                double tmp = this.elementData[i * this.nCol + j];
                this.elementData[i * this.nCol + j] = this.elementData[j * this.nCol + i];
                this.elementData[j * this.nCol + i] = tmp;
            }
        }
    }

    public final void transpose(GMatrix m1) {
        this.set(m1);
        this.transpose();
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuffer out = new StringBuffer("[");
        out.append(nl);
        for (int i = 0; i < this.nRow; ++i) {
            out.append("  [");
            for (int j = 0; j < this.nCol; ++j) {
                if (j > 0) {
                    out.append("\t");
                }
                out.append(this.elementData[i * this.nCol + j]);
            }
            if (i + 1 < this.nRow) {
                out.append("]");
                out.append(nl);
                continue;
            }
            out.append("] ]");
        }
        return out.toString();
    }

    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.nRow * this.nCol; ++i) {
            long bits = Double.doubleToLongBits(this.elementData[i]);
            hash ^= (int) (bits ^ bits >> 32);
        }
        return hash;
    }

    public boolean equals(GMatrix m1) {
        if (m1 == null) {
            return false;
        }
        if (m1.nRow != this.nRow) {
            return false;
        }
        if (m1.nCol != this.nCol) {
            return false;
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                if (this.elementData[i * this.nCol + j] == m1.elementData[i * this.nCol + j]) continue;
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object o1) {
        return o1 != null && o1 instanceof GMatrix && this.equals((GMatrix) o1);
    }

    @Deprecated
    public boolean epsilonEquals(GMatrix m1, float epsilon) {
        if (m1.nRow != this.nRow) {
            return false;
        }
        if (m1.nCol != this.nCol) {
            return false;
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                if (!((double) epsilon < Math.abs(this.elementData[i * this.nCol + j] - m1.elementData[i * this.nCol + j])))
                    continue;
                return false;
            }
        }
        return true;
    }

    public boolean epsilonEquals(GMatrix m1, double epsilon) {
        if (m1.nRow != this.nRow) {
            return false;
        }
        if (m1.nCol != this.nCol) {
            return false;
        }
        for (int i = 0; i < this.nRow; ++i) {
            for (int j = 0; j < this.nCol; ++j) {
                if (!(epsilon < Math.abs(this.elementData[i * this.nCol + j] - m1.elementData[i * this.nCol + j])))
                    continue;
                return false;
            }
        }
        return true;
    }

    public final double trace() {
        int min = this.nRow < this.nCol ? this.nRow : this.nCol;
        double trace = 0.0;
        for (int i = 0; i < min; ++i) {
            trace += this.elementData[i * this.nCol + i];
        }
        return trace;
    }

    public final void setScale(double scale) {
        this.setZero();
        int min = this.nRow < this.nCol ? this.nRow : this.nCol;
        for (int i = 0; i < min; ++i) {
            this.elementData[i * this.nCol + i] = scale;
        }
    }

    private void setDiag(int i, double value) {
        this.elementData[i * this.nCol + i] = value;
    }

    private double getDiag(int i) {
        return this.elementData[i * this.nCol + i];
    }

    private double dpythag(double a2, double b) {
        double absb;
        double absa = Math.abs(a2);
        if (absa > (absb = Math.abs(b))) {
            if (absa == 0.0) {
                return 0.0;
            }
            double term = absb / absa;
            if (Math.abs(term) <= Double.MIN_VALUE) {
                return absa;
            }
            return absa * Math.sqrt(1.0 + term * term);
        }
        if (absb == 0.0) {
            return 0.0;
        }
        double term = absa / absb;
        if (Math.abs(term) <= Double.MIN_VALUE) {
            return absb;
        }
        return absb * Math.sqrt(1.0 + term * term);
    }

    public final int SVD(GMatrix u, GMatrix w, GMatrix v) {
        int k;
        int i;
        if (u.nRow != this.nRow || u.nCol != this.nRow) {
            throw new ArrayIndexOutOfBoundsException("The U Matrix invalid size");
        }
        if (v.nRow != this.nCol || v.nCol != this.nCol) {
            throw new ArrayIndexOutOfBoundsException("The V Matrix invalid size");
        }
        if (w.nCol != this.nCol || w.nRow != this.nRow) {
            throw new ArrayIndexOutOfBoundsException("The W Matrix invalid size");
        }
        int m = this.nRow;
        int n = this.nCol;
        int imax = m > n ? m : n;
        double[] A = u.elementData;
        double[] V = v.elementData;
        int l = 0;
        int nm = 0;
        double[] rv1 = new double[n];
        this.get(u);
        for (int i2 = m; i2 < imax; ++i2) {
            for (int j = 0; j < imax; ++j) {
                A[i2 * m + j] = 0.0;
            }
        }
        for (int j = n; j < imax; ++j) {
            for (int i3 = 0; i3 < imax; ++i3) {
                A[i3 * m + j] = 0.0;
            }
        }
        w.setZero();
        double anorm = 0.0;
        double g = 0.0;
        double scale = 0.0;
        for (i = 0; i < n; ++i) {
            int j;
            double a1;
            int k2;
            int k3;
            l = i + 1;
            rv1[i] = scale * g;
            scale = 0.0;
            g = 0.0;
            double s = 0.0;
            if (i < m) {
                for (k2 = i; k2 < m; ++k2) {
                    scale += Math.abs(A[k2 * m + i]);
                }
                if (scale != 0.0) {
                    for (k2 = i; k2 < m; ++k2) {
                        double[] array = A;
                        int n2 = k2 * m + i;
                        double[] arrd = array;
                        int n3 = n2;
                        arrd[n3] = arrd[n3] / scale;
                        s += A[k2 * m + i] * A[k2 * m + i];
                    }
                    double f = A[i * m + i];
                    g = f < 0.0 ? Math.sqrt(s) : -Math.sqrt(s);
                    double h = f * g - s;
                    A[i * m + i] = f - g;
                    for (j = l; j < n; ++j) {
                        s = 0.0;
                        for (k3 = i; k3 < m; ++k3) {
                            s += A[k3 * m + i] * A[k3 * m + j];
                        }
                        f = s / h;
                        for (k3 = i; k3 < m; ++k3) {
                            double[] array2 = A;
                            int n3 = k3 * m + j;
                            double[] arrd = array2;
                            int n4 = n3;
                            arrd[n4] = arrd[n4] + f * A[k3 * m + i];
                        }
                    }
                    for (k = i; k < m; ++k) {
                        double[] array3 = A;
                        int n4 = k * m + i;
                        double[] arrd = array3;
                        int n5 = n4;
                        arrd[n5] = arrd[n5] * scale;
                    }
                }
            }
            w.setDiag(i, scale * g);
            scale = 0.0;
            g = 0.0;
            s = 0.0;
            if (i < m && i != n - 1) {
                for (k2 = l; k2 < n; ++k2) {
                    scale += Math.abs(A[i * m + k2]);
                }
                if (scale != 0.0) {
                    for (k2 = l; k2 < n; ++k2) {
                        double[] array4 = A;
                        int n5 = i * m + k2;
                        double[] arrd = array4;
                        int n6 = n5;
                        arrd[n6] = arrd[n6] / scale;
                        s += A[i * m + k2] * A[i * m + k2];
                    }
                    double f = A[i * m + l];
                    g = f < 0.0 ? Math.sqrt(s) : -Math.sqrt(s);
                    double h = f * g - s;
                    A[i * m + l] = f - g;
                    for (k = l; k < n; ++k) {
                        rv1[k] = A[i * m + k] / h;
                    }
                    for (j = l; j < m; ++j) {
                        s = 0.0;
                        for (k3 = l; k3 < n; ++k3) {
                            s += A[j * m + k3] * A[i * m + k3];
                        }
                        for (k3 = l; k3 < n; ++k3) {
                            double[] array5 = A;
                            int n6 = j * m + k3;
                            double[] arrd = array5;
                            int n7 = n6;
                            arrd[n7] = arrd[n7] + s * rv1[k3];
                        }
                    }
                    for (k = l; k < n; ++k) {
                        double[] array6 = A;
                        int n7 = i * m + k;
                        double[] arrd = array6;
                        int n8 = n7;
                        arrd[n8] = arrd[n8] * scale;
                    }
                }
            }
            if (!((a1 = Math.abs(w.getDiag(i)) + Math.abs(rv1[i])) > anorm)) continue;
            anorm = a1;
        }
        i = n - 1;
        while (i >= 0) {
            if (i < n - 1) {
                if (g != 0.0) {
                    int j;
                    for (j = l; j < n; ++j) {
                        V[j * n + i] = A[i * m + j] / A[i * m + l] / g;
                    }
                    for (j = l; j < n; ++j) {
                        int k4;
                        double s = 0.0;
                        for (k4 = l; k4 < n; ++k4) {
                            s += A[i * m + k4] * V[k4 * n + j];
                        }
                        for (k4 = l; k4 < n; ++k4) {
                            double[] array7 = V;
                            int n8 = k4 * n + j;
                            double[] arrd = array7;
                            int n9 = n8;
                            arrd[n9] = arrd[n9] + s * V[k4 * n + i];
                        }
                    }
                }
                for (int j = l; j < n; ++j) {
                    V[j * n + i] = 0.0;
                    V[i * n + j] = 0.0;
                }
            }
            V[i * n + i] = 1.0;
            g = rv1[i];
            l = i--;
        }
        int imin = m < n ? m : n;
        for (int i4 = imin - 1; i4 >= 0; --i4) {
            int j;
            l = i4 + 1;
            g = w.getDiag(i4);
            for (j = l; j < n; ++j) {
                A[i4 * m + j] = 0.0;
            }
            if (g != 0.0) {
                g = 1.0 / g;
                for (j = l; j < n; ++j) {
                    double s = 0.0;
                    for (int k5 = l; k5 < m; ++k5) {
                        s += A[k5 * m + i4] * A[k5 * m + j];
                    }
                    double f = s / A[i4 * m + i4] * g;
                    for (k = i4; k < m; ++k) {
                        double[] array8 = A;
                        int n9 = k * m + j;
                        double[] arrd = array8;
                        int n10 = n9;
                        arrd[n10] = arrd[n10] + f * A[k * m + i4];
                    }
                }
                for (j = i4; j < m; ++j) {
                    double[] array9 = A;
                    int n10 = j * m + i4;
                    double[] arrd = array9;
                    int n11 = n10;
                    arrd[n11] = arrd[n11] * g;
                }
            } else {
                for (j = i4; j < m; ++j) {
                    A[j * m + i4] = 0.0;
                }
            }
            double[] array10 = A;
            int n11 = i4 * m + i4;
            double[] arrd = array10;
            int n12 = n11;
            arrd[n12] = arrd[n12] + 1.0;
        }
        block31:
        for (int k6 = n - 1; k6 >= 0; --k6) {
            for (int its = 1; its <= 30; ++its) {
                boolean flag = true;
                for (l = k6; l >= 0; --l) {
                    nm = l - 1;
                    if (Math.abs(rv1[l]) + anorm == anorm) {
                        flag = false;
                        break;
                    }
                    if (Math.abs(w.getDiag(nm)) + anorm == anorm) break;
                }
                if (flag) {
                    double c2 = 0.0;
                    double s = 1.0;
                    for (int i5 = l; i5 <= k6; ++i5) {
                        double f = s * rv1[i5];
                        double[] arrd = rv1;
                        int n13 = i5;
                        arrd[n13] = arrd[n13] * c2;
                        if (Math.abs(f) + anorm == anorm) break;
                        g = w.getDiag(i5);
                        double h = this.dpythag(f, g);
                        w.setDiag(i5, h);
                        h = 1.0 / h;
                        c2 = g * h;
                        s = -f * h;
                        for (int j = 0; j < m; ++j) {
                            double y2 = A[j * m + nm];
                            double z = A[j * m + i5];
                            A[j * m + nm] = y2 * c2 + z * s;
                            A[j * m + i5] = z * c2 - y2 * s;
                        }
                    }
                }
                double z = w.getDiag(k6);
                if (l == k6) {
                    if (!(z < 0.0)) continue block31;
                    w.setDiag(k6, -z);
                    for (int j = 0; j < n; ++j) {
                        V[j * n + k6] = -V[j * n + k6];
                    }
                    continue block31;
                }
                if (its == 30) {
                    return 0;
                }
                double x = w.getDiag(l);
                nm = k6 - 1;
                double y3 = w.getDiag(nm);
                g = rv1[nm];
                double h = rv1[k6];
                double f = ((y3 - z) * (y3 + z) + (g - h) * (g + h)) / (2.0 * h * y3);
                g = this.dpythag(f, 1.0);
                f = ((x - z) * (x + z) + h * (y3 / (f + (f >= 0.0 ? Math.abs(g) : -Math.abs(g))) - h)) / x;
                double c3 = 1.0;
                double s = 1.0;
                for (int j = l; j <= nm; ++j) {
                    int jj;
                    int i6 = j + 1;
                    g = rv1[i6];
                    y3 = w.getDiag(i6);
                    h = s * g;
                    rv1[j] = z = this.dpythag(f, h);
                    c3 = f / z;
                    s = h / z;
                    f = x * c3 + (g *= c3) * s;
                    g = g * c3 - x * s;
                    h = y3 * s;
                    y3 *= c3;
                    for (jj = 0; jj < n; ++jj) {
                        x = V[jj * n + j];
                        z = V[jj * n + i6];
                        V[jj * n + j] = x * c3 + z * s;
                        V[jj * n + i6] = z * c3 - x * s;
                    }
                    z = this.dpythag(f, h);
                    w.setDiag(j, z);
                    if (z != 0.0) {
                        z = 1.0 / z;
                        c3 = f * z;
                        s = h * z;
                    }
                    f = c3 * g + s * y3;
                    x = c3 * y3 - s * g;
                    for (jj = 0; jj < m; ++jj) {
                        y3 = A[jj * m + j];
                        z = A[jj * m + i6];
                        A[jj * m + j] = y3 * c3 + z * s;
                        A[jj * m + i6] = z * c3 - y3 * s;
                    }
                }
                rv1[l] = 0.0;
                rv1[k6] = f;
                w.setDiag(k6, x);
            }
        }
        int rank = 0;
        for (int i7 = 0; i7 < n; ++i7) {
            if (!(w.getDiag(i7) > 0.0)) continue;
            ++rank;
        }
        return rank;
    }

    private void swapRows(int i, int j) {
        for (int k = 0; k < this.nCol; ++k) {
            double tmp = this.elementData[i * this.nCol + k];
            this.elementData[i * this.nCol + k] = this.elementData[j * this.nCol + k];
            this.elementData[j * this.nCol + k] = tmp;
        }
    }

    public final int LUD(GMatrix LU, GVector permutation) {
        if (this.nRow != this.nCol) {
            throw new ArrayIndexOutOfBoundsException("not a square matrix");
        }
        int n = this.nRow;
        if (n != LU.nRow) {
            throw new ArrayIndexOutOfBoundsException("this.nRow:" + n + " != LU.nRow:" + LU.nRow);
        }
        if (n != LU.nCol) {
            throw new ArrayIndexOutOfBoundsException("this.nCol:" + n + " != LU.nCol:" + LU.nCol);
        }
        if (permutation.getSize() < n) {
            throw new ArrayIndexOutOfBoundsException("permutation.size:" + permutation.getSize() + " < this.nCol:" + n);
        }
        if (this != LU) {
            LU.set(this);
        }
        int even = 1;
        double[] a2 = LU.elementData;
        for (int i = 0; i < n; ++i) {
            permutation.setElement(i, i);
        }
        for (int j = 0; j < n; ++j) {
            for (int k = 0; k < j; ++k) {
                double sum = a2[k * n + j];
                for (int l = 0; l < k; ++l) {
                    if (a2[k * n + l] == 0.0 || a2[l * n + j] == 0.0) continue;
                    sum -= a2[k * n + l] * a2[l * n + j];
                }
                a2[k * n + j] = sum;
            }
            double big = 0.0;
            int imax = j;
            for (int k = j; k < n; ++k) {
                double sum = a2[k * n + j];
                for (int l = 0; l < j; ++l) {
                    if (a2[k * n + l] == 0.0 || a2[l * n + j] == 0.0) continue;
                    sum -= a2[k * n + l] * a2[l * n + j];
                }
                a2[k * n + j] = sum;
                double dum = Math.abs(sum);
                if (!(dum >= big)) continue;
                big = dum;
                imax = k;
            }
            if (j != imax) {
                LU.swapRows(imax, j);
                double tmp = permutation.getElement(imax);
                permutation.setElement(imax, permutation.getElement(j));
                permutation.setElement(j, tmp);
                even = -even;
            }
            if (j == n - 1) continue;
            double dum = 1.0 / a2[j * n + j];
            for (int k = j + 1; k < n; ++k) {
                double[] array = a2;
                int n2 = k * n + j;
                double[] arrd = array;
                int n3 = n2;
                arrd[n3] = arrd[n3] * dum;
            }
        }
        return even;
    }
}

