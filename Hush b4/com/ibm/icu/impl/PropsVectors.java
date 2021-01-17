// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Arrays;
import java.util.Comparator;

public class PropsVectors
{
    private int[] v;
    private int columns;
    private int maxRows;
    private int rows;
    private int prevRow;
    private boolean isCompacted;
    public static final int FIRST_SPECIAL_CP = 1114112;
    public static final int INITIAL_VALUE_CP = 1114112;
    public static final int ERROR_VALUE_CP = 1114113;
    public static final int MAX_CP = 1114113;
    public static final int INITIAL_ROWS = 4096;
    public static final int MEDIUM_ROWS = 65536;
    public static final int MAX_ROWS = 1114114;
    
    private boolean areElementsSame(final int index1, final int[] target, final int index2, final int length) {
        for (int i = 0; i < length; ++i) {
            if (this.v[index1 + i] != target[index2 + i]) {
                return false;
            }
        }
        return true;
    }
    
    private int findRow(final int rangeStart) {
        int index = 0;
        index = this.prevRow * this.columns;
        if (rangeStart >= this.v[index]) {
            if (rangeStart < this.v[index + 1]) {
                return index;
            }
            index += this.columns;
            if (rangeStart < this.v[index + 1]) {
                ++this.prevRow;
                return index;
            }
            index += this.columns;
            if (rangeStart < this.v[index + 1]) {
                this.prevRow += 2;
                return index;
            }
            if (rangeStart - this.v[index + 1] < 10) {
                this.prevRow += 2;
                do {
                    ++this.prevRow;
                    index += this.columns;
                } while (rangeStart >= this.v[index + 1]);
                return index;
            }
        }
        else if (rangeStart < this.v[1]) {
            return this.prevRow = 0;
        }
        int start = 0;
        int mid = 0;
        int limit = this.rows;
        while (start < limit - 1) {
            mid = (start + limit) / 2;
            index = this.columns * mid;
            if (rangeStart < this.v[index]) {
                limit = mid;
            }
            else {
                if (rangeStart < this.v[index + 1]) {
                    this.prevRow = mid;
                    return index;
                }
                start = mid;
            }
        }
        this.prevRow = start;
        index = start * this.columns;
        return index;
    }
    
    public PropsVectors(final int numOfColumns) {
        if (numOfColumns < 1) {
            throw new IllegalArgumentException("numOfColumns need to be no less than 1; but it is " + numOfColumns);
        }
        this.columns = numOfColumns + 2;
        this.v = new int[4096 * this.columns];
        this.maxRows = 4096;
        this.rows = 3;
        this.prevRow = 0;
        this.isCompacted = false;
        this.v[0] = 0;
        this.v[1] = 1114112;
        int index = this.columns;
        for (int cp = 1114112; cp <= 1114113; ++cp) {
            this.v[index] = cp;
            this.v[index + 1] = cp + 1;
            index += this.columns;
        }
    }
    
    public void setValue(final int start, final int end, int column, int value, int mask) {
        if (start < 0 || start > end || end > 1114113 || column < 0 || column >= this.columns - 2) {
            throw new IllegalArgumentException();
        }
        if (this.isCompacted) {
            throw new IllegalStateException("Shouldn't be called aftercompact()!");
        }
        final int limit = end + 1;
        column += 2;
        value &= mask;
        int firstRow = this.findRow(start);
        int lastRow = this.findRow(end);
        final boolean splitFirstRow = start != this.v[firstRow] && value != (this.v[firstRow + column] & mask);
        final boolean splitLastRow = limit != this.v[lastRow + 1] && value != (this.v[lastRow + column] & mask);
        if (splitFirstRow || splitLastRow) {
            int rowsToExpand = 0;
            if (splitFirstRow) {
                ++rowsToExpand;
            }
            if (splitLastRow) {
                ++rowsToExpand;
            }
            int newMaxRows = 0;
            if (this.rows + rowsToExpand > this.maxRows) {
                if (this.maxRows < 65536) {
                    newMaxRows = 65536;
                }
                else {
                    if (this.maxRows >= 1114114) {
                        throw new IndexOutOfBoundsException("MAX_ROWS exceeded! Increase it to a higher valuein the implementation");
                    }
                    newMaxRows = 1114114;
                }
                final int[] temp = new int[newMaxRows * this.columns];
                System.arraycopy(this.v, 0, temp, 0, this.rows * this.columns);
                this.v = temp;
                this.maxRows = newMaxRows;
            }
            int count = this.rows * this.columns - (lastRow + this.columns);
            if (count > 0) {
                System.arraycopy(this.v, lastRow + this.columns, this.v, lastRow + (1 + rowsToExpand) * this.columns, count);
            }
            this.rows += rowsToExpand;
            if (splitFirstRow) {
                count = lastRow - firstRow + this.columns;
                System.arraycopy(this.v, firstRow, this.v, firstRow + this.columns, count);
                lastRow += this.columns;
                this.v[firstRow + 1] = (this.v[firstRow + this.columns] = start);
                firstRow += this.columns;
            }
            if (splitLastRow) {
                System.arraycopy(this.v, lastRow, this.v, lastRow + this.columns, this.columns);
                this.v[lastRow + 1] = (this.v[lastRow + this.columns] = limit);
            }
        }
        this.prevRow = lastRow / this.columns;
        firstRow += column;
        lastRow += column;
        mask ^= -1;
        while (true) {
            this.v[firstRow] = ((this.v[firstRow] & mask) | value);
            if (firstRow == lastRow) {
                break;
            }
            firstRow += this.columns;
        }
    }
    
    public int getValue(final int c, final int column) {
        if (this.isCompacted || c < 0 || c > 1114113 || column < 0 || column >= this.columns - 2) {
            return 0;
        }
        final int index = this.findRow(c);
        return this.v[index + 2 + column];
    }
    
    public int[] getRow(final int rowIndex) {
        if (this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method after compact()");
        }
        if (rowIndex < 0 || rowIndex > this.rows) {
            throw new IllegalArgumentException("rowIndex out of bound!");
        }
        final int[] rowToReturn = new int[this.columns - 2];
        System.arraycopy(this.v, rowIndex * this.columns + 2, rowToReturn, 0, this.columns - 2);
        return rowToReturn;
    }
    
    public int getRowStart(final int rowIndex) {
        if (this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method after compact()");
        }
        if (rowIndex < 0 || rowIndex > this.rows) {
            throw new IllegalArgumentException("rowIndex out of bound!");
        }
        return this.v[rowIndex * this.columns];
    }
    
    public int getRowEnd(final int rowIndex) {
        if (this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method after compact()");
        }
        if (rowIndex < 0 || rowIndex > this.rows) {
            throw new IllegalArgumentException("rowIndex out of bound!");
        }
        return this.v[rowIndex * this.columns + 1] - 1;
    }
    
    public void compact(final CompactHandler compactor) {
        if (this.isCompacted) {
            return;
        }
        this.isCompacted = true;
        final int valueColumns = this.columns - 2;
        final Integer[] indexArray = new Integer[this.rows];
        for (int i = 0; i < this.rows; ++i) {
            indexArray[i] = this.columns * i;
        }
        Arrays.sort(indexArray, new Comparator<Integer>() {
            public int compare(final Integer o1, final Integer o2) {
                final int indexOfRow1 = o1;
                final int indexOfRow2 = o2;
                int count = PropsVectors.this.columns;
                int index = 2;
                while (PropsVectors.this.v[indexOfRow1 + index] == PropsVectors.this.v[indexOfRow2 + index]) {
                    if (++index == PropsVectors.this.columns) {
                        index = 0;
                    }
                    if (--count <= 0) {
                        return 0;
                    }
                }
                return (PropsVectors.this.v[indexOfRow1 + index] < PropsVectors.this.v[indexOfRow2 + index]) ? -1 : 1;
            }
        });
        int count = -valueColumns;
        for (int j = 0; j < this.rows; ++j) {
            final int start = this.v[indexArray[j]];
            if (count < 0 || !this.areElementsSame(indexArray[j] + 2, this.v, indexArray[j - 1] + 2, valueColumns)) {
                count += valueColumns;
            }
            if (start == 1114112) {
                compactor.setRowIndexForInitialValue(count);
            }
            else if (start == 1114113) {
                compactor.setRowIndexForErrorValue(count);
            }
        }
        count += valueColumns;
        compactor.startRealValues(count);
        final int[] temp = new int[count];
        count = -valueColumns;
        for (int k = 0; k < this.rows; ++k) {
            final int start2 = this.v[indexArray[k]];
            final int limit = this.v[indexArray[k] + 1];
            if (count < 0 || !this.areElementsSame(indexArray[k] + 2, temp, count, valueColumns)) {
                count += valueColumns;
                System.arraycopy(this.v, indexArray[k] + 2, temp, count, valueColumns);
            }
            if (start2 < 1114112) {
                compactor.setRowIndexForRange(start2, limit - 1, count);
            }
        }
        this.v = temp;
        this.rows = count / valueColumns + 1;
    }
    
    public int[] getCompactedArray() {
        if (!this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method before compact()");
        }
        return this.v;
    }
    
    public int getCompactedRows() {
        if (!this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method before compact()");
        }
        return this.rows;
    }
    
    public int getCompactedColumns() {
        if (!this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method before compact()");
        }
        return this.columns - 2;
    }
    
    public IntTrie compactToTrieWithRowIndexes() {
        final PVecToTrieCompactHandler compactor = new PVecToTrieCompactHandler();
        this.compact(compactor);
        return compactor.builder.serialize(new DefaultGetFoldedValue(compactor.builder), new DefaultGetFoldingOffset());
    }
    
    private static class DefaultGetFoldingOffset implements Trie.DataManipulate
    {
        public int getFoldingOffset(final int value) {
            return value;
        }
    }
    
    private static class DefaultGetFoldedValue implements TrieBuilder.DataManipulate
    {
        private IntTrieBuilder builder;
        
        public DefaultGetFoldedValue(final IntTrieBuilder inBuilder) {
            this.builder = inBuilder;
        }
        
        public int getFoldedValue(int start, final int offset) {
            final int initialValue = this.builder.m_initialValue_;
            final int limit = start + 1024;
            while (start < limit) {
                final boolean[] inBlockZero = { false };
                final int value = this.builder.getValue(start, inBlockZero);
                if (inBlockZero[0]) {
                    start += 32;
                }
                else {
                    if (value != initialValue) {
                        return offset;
                    }
                    ++start;
                }
            }
            return 0;
        }
    }
    
    public interface CompactHandler
    {
        void setRowIndexForRange(final int p0, final int p1, final int p2);
        
        void setRowIndexForInitialValue(final int p0);
        
        void setRowIndexForErrorValue(final int p0);
        
        void startRealValues(final int p0);
    }
}
