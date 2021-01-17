// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.util.Iterator;

public class LineIterator implements Iterator<String>
{
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished;
    
    public LineIterator(final Reader reader) throws IllegalArgumentException {
        this.finished = false;
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        if (reader instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader)reader;
        }
        else {
            this.bufferedReader = new BufferedReader(reader);
        }
    }
    
    @Override
    public boolean hasNext() {
        if (this.cachedLine != null) {
            return true;
        }
        if (this.finished) {
            return false;
        }
        Label_0018: {
            break Label_0018;
            try {
                while (true) {
                    final String line = this.bufferedReader.readLine();
                    if (line == null) {
                        this.finished = true;
                        return false;
                    }
                    if (this.isValidLine(line)) {
                        this.cachedLine = line;
                        return true;
                    }
                }
            }
            catch (IOException ioe) {
                this.close();
                throw new IllegalStateException(ioe);
            }
        }
    }
    
    protected boolean isValidLine(final String line) {
        return true;
    }
    
    @Override
    public String next() {
        return this.nextLine();
    }
    
    public String nextLine() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        final String currentLine = this.cachedLine;
        this.cachedLine = null;
        return currentLine;
    }
    
    public void close() {
        this.finished = true;
        IOUtils.closeQuietly(this.bufferedReader);
        this.cachedLine = null;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }
    
    public static void closeQuietly(final LineIterator iterator) {
        if (iterator != null) {
            iterator.close();
        }
    }
}
