// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.util.Args;
import org.apache.http.Header;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.HeaderIterator;

@NotThreadSafe
public class BasicHeaderIterator implements HeaderIterator
{
    protected final Header[] allHeaders;
    protected int currentIndex;
    protected String headerName;
    
    public BasicHeaderIterator(final Header[] headers, final String name) {
        this.allHeaders = Args.notNull(headers, "Header array");
        this.headerName = name;
        this.currentIndex = this.findNext(-1);
    }
    
    protected int findNext(final int pos) {
        int from = pos;
        if (from < -1) {
            return -1;
        }
        int to;
        boolean found;
        for (to = this.allHeaders.length - 1, found = false; !found && from < to; ++from, found = this.filterHeader(from)) {}
        return found ? from : -1;
    }
    
    protected boolean filterHeader(final int index) {
        return this.headerName == null || this.headerName.equalsIgnoreCase(this.allHeaders[index].getName());
    }
    
    public boolean hasNext() {
        return this.currentIndex >= 0;
    }
    
    public Header nextHeader() throws NoSuchElementException {
        final int current = this.currentIndex;
        if (current < 0) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        this.currentIndex = this.findNext(current);
        return this.allHeaders[current];
    }
    
    public final Object next() throws NoSuchElementException {
        return this.nextHeader();
    }
    
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing headers is not supported.");
    }
}
