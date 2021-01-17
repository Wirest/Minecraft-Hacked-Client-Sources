// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.IOException;

abstract class LineBuffer
{
    private StringBuilder line;
    private boolean sawReturn;
    
    LineBuffer() {
        this.line = new StringBuilder();
    }
    
    protected void add(final char[] cbuf, final int off, final int len) throws IOException {
        int pos = off;
        if (this.sawReturn && len > 0 && this.finishLine(cbuf[pos] == '\n')) {
            ++pos;
        }
        int start = pos;
        for (int end = off + len; pos < end; ++pos) {
            switch (cbuf[pos]) {
                case '\r': {
                    this.line.append(cbuf, start, pos - start);
                    this.sawReturn = true;
                    if (pos + 1 < end && this.finishLine(cbuf[pos + 1] == '\n')) {
                        ++pos;
                    }
                    start = pos + 1;
                    break;
                }
                case '\n': {
                    this.line.append(cbuf, start, pos - start);
                    this.finishLine(true);
                    start = pos + 1;
                    break;
                }
            }
        }
        this.line.append(cbuf, start, off + len - start);
    }
    
    private boolean finishLine(final boolean sawNewline) throws IOException {
        this.handleLine(this.line.toString(), this.sawReturn ? (sawNewline ? "\r\n" : "\r") : (sawNewline ? "\n" : ""));
        this.line = new StringBuilder();
        this.sawReturn = false;
        return sawNewline;
    }
    
    protected void finish() throws IOException {
        if (this.sawReturn || this.line.length() > 0) {
            this.finishLine(false);
        }
    }
    
    protected abstract void handleLine(final String p0, final String p1) throws IOException;
}
