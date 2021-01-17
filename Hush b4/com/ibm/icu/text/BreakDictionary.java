// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import com.ibm.icu.util.CompactByteArray;

class BreakDictionary
{
    private char[] reverseColumnMap;
    private CompactByteArray columnMap;
    private int numCols;
    private short[] table;
    private short[] rowIndex;
    private int[] rowIndexFlags;
    private short[] rowIndexFlagsIndex;
    private byte[] rowIndexShifts;
    
    static void writeToFile(final String inFile, final String outFile) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        final BreakDictionary dictionary = new BreakDictionary(new FileInputStream(inFile));
        PrintWriter out = null;
        if (outFile != null) {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UnicodeLittle"));
        }
        dictionary.printWordList("", 0, out);
        if (out != null) {
            out.close();
        }
    }
    
    void printWordList(final String partialWord, final int state, final PrintWriter out) throws IOException {
        if (state == 65535) {
            System.out.println(partialWord);
            if (out != null) {
                out.println(partialWord);
            }
        }
        else {
            for (int i = 0; i < this.numCols; ++i) {
                final int newState = this.at(state, i) & 0xFFFF;
                if (newState != 0) {
                    final char newChar = this.reverseColumnMap[i];
                    String newPartialWord = partialWord;
                    if (newChar != '\0') {
                        newPartialWord += newChar;
                    }
                    this.printWordList(newPartialWord, newState, out);
                }
            }
        }
    }
    
    BreakDictionary(final InputStream dictionaryStream) throws IOException {
        this.reverseColumnMap = null;
        this.columnMap = null;
        this.table = null;
        this.rowIndex = null;
        this.rowIndexFlags = null;
        this.rowIndexFlagsIndex = null;
        this.rowIndexShifts = null;
        this.readDictionaryFile(new DataInputStream(dictionaryStream));
    }
    
    void readDictionaryFile(final DataInputStream in) throws IOException {
        in.readInt();
        int l = in.readInt();
        final char[] temp = new char[l];
        for (int i = 0; i < temp.length; ++i) {
            temp[i] = (char)in.readShort();
        }
        l = in.readInt();
        final byte[] temp2 = new byte[l];
        for (int j = 0; j < temp2.length; ++j) {
            temp2[j] = in.readByte();
        }
        this.columnMap = new CompactByteArray(temp, temp2);
        this.numCols = in.readInt();
        in.readInt();
        l = in.readInt();
        this.rowIndex = new short[l];
        for (int j = 0; j < this.rowIndex.length; ++j) {
            this.rowIndex[j] = in.readShort();
        }
        l = in.readInt();
        this.rowIndexFlagsIndex = new short[l];
        for (int j = 0; j < this.rowIndexFlagsIndex.length; ++j) {
            this.rowIndexFlagsIndex[j] = in.readShort();
        }
        l = in.readInt();
        this.rowIndexFlags = new int[l];
        for (int j = 0; j < this.rowIndexFlags.length; ++j) {
            this.rowIndexFlags[j] = in.readInt();
        }
        l = in.readInt();
        this.rowIndexShifts = new byte[l];
        for (int j = 0; j < this.rowIndexShifts.length; ++j) {
            this.rowIndexShifts[j] = in.readByte();
        }
        l = in.readInt();
        this.table = new short[l];
        for (int j = 0; j < this.table.length; ++j) {
            this.table[j] = in.readShort();
        }
        this.reverseColumnMap = new char[this.numCols];
        for (char c = '\0'; c < '\uffff'; ++c) {
            final int col = this.columnMap.elementAt(c);
            if (col != 0) {
                this.reverseColumnMap[col] = c;
            }
        }
        in.close();
    }
    
    final short at(final int row, final char ch) {
        final int col = this.columnMap.elementAt(ch);
        return this.at(row, col);
    }
    
    final short at(final int row, final int col) {
        if (this.cellIsPopulated(row, col)) {
            return this.internalAt(this.rowIndex[row], col + this.rowIndexShifts[row]);
        }
        return 0;
    }
    
    private final boolean cellIsPopulated(final int row, final int col) {
        if (this.rowIndexFlagsIndex[row] < 0) {
            return col == -this.rowIndexFlagsIndex[row];
        }
        final int flags = this.rowIndexFlags[this.rowIndexFlagsIndex[row] + (col >> 5)];
        return (flags & 1 << (col & 0x1F)) != 0x0;
    }
    
    private final short internalAt(final int row, final int col) {
        return this.table[row * this.numCols + col];
    }
}
