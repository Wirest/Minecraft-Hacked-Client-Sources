// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.message;

import org.apache.http.ParseException;
import java.util.NoSuchElementException;
import org.apache.http.util.Args;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.TokenIterator;

@NotThreadSafe
public class BasicTokenIterator implements TokenIterator
{
    public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
    protected final HeaderIterator headerIt;
    protected String currentHeader;
    protected String currentToken;
    protected int searchPos;
    
    public BasicTokenIterator(final HeaderIterator headerIterator) {
        this.headerIt = Args.notNull(headerIterator, "Header iterator");
        this.searchPos = this.findNext(-1);
    }
    
    public boolean hasNext() {
        return this.currentToken != null;
    }
    
    public String nextToken() throws NoSuchElementException, ParseException {
        if (this.currentToken == null) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        final String result = this.currentToken;
        this.searchPos = this.findNext(this.searchPos);
        return result;
    }
    
    public final Object next() throws NoSuchElementException, ParseException {
        return this.nextToken();
    }
    
    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing tokens is not supported.");
    }
    
    protected int findNext(final int pos) throws ParseException {
        int from = pos;
        if (from < 0) {
            if (!this.headerIt.hasNext()) {
                return -1;
            }
            this.currentHeader = this.headerIt.nextHeader().getValue();
            from = 0;
        }
        else {
            from = this.findTokenSeparator(from);
        }
        final int start = this.findTokenStart(from);
        if (start < 0) {
            this.currentToken = null;
            return -1;
        }
        final int end = this.findTokenEnd(start);
        this.currentToken = this.createToken(this.currentHeader, start, end);
        return end;
    }
    
    protected String createToken(final String value, final int start, final int end) {
        return value.substring(start, end);
    }
    
    protected int findTokenStart(final int pos) {
        int from = Args.notNegative(pos, "Search position");
        boolean found = false;
        while (!found && this.currentHeader != null) {
            final int to = this.currentHeader.length();
            while (!found && from < to) {
                final char ch = this.currentHeader.charAt(from);
                if (this.isTokenSeparator(ch) || this.isWhitespace(ch)) {
                    ++from;
                }
                else {
                    if (!this.isTokenChar(this.currentHeader.charAt(from))) {
                        throw new ParseException("Invalid character before token (pos " + from + "): " + this.currentHeader);
                    }
                    found = true;
                }
            }
            if (!found) {
                if (this.headerIt.hasNext()) {
                    this.currentHeader = this.headerIt.nextHeader().getValue();
                    from = 0;
                }
                else {
                    this.currentHeader = null;
                }
            }
        }
        return found ? from : -1;
    }
    
    protected int findTokenSeparator(final int pos) {
        int from = Args.notNegative(pos, "Search position");
        boolean found = false;
        final int to = this.currentHeader.length();
        while (!found && from < to) {
            final char ch = this.currentHeader.charAt(from);
            if (this.isTokenSeparator(ch)) {
                found = true;
            }
            else if (this.isWhitespace(ch)) {
                ++from;
            }
            else {
                if (this.isTokenChar(ch)) {
                    throw new ParseException("Tokens without separator (pos " + from + "): " + this.currentHeader);
                }
                throw new ParseException("Invalid character after token (pos " + from + "): " + this.currentHeader);
            }
        }
        return from;
    }
    
    protected int findTokenEnd(final int from) {
        Args.notNegative(from, "Search position");
        int to;
        int end;
        for (to = this.currentHeader.length(), end = from + 1; end < to && this.isTokenChar(this.currentHeader.charAt(end)); ++end) {}
        return end;
    }
    
    protected boolean isTokenSeparator(final char ch) {
        return ch == ',';
    }
    
    protected boolean isWhitespace(final char ch) {
        return ch == '\t' || Character.isSpaceChar(ch);
    }
    
    protected boolean isTokenChar(final char ch) {
        return Character.isLetterOrDigit(ch) || (!Character.isISOControl(ch) && !this.isHttpSeparator(ch));
    }
    
    protected boolean isHttpSeparator(final char ch) {
        return " ,;=()<>@:\\\"/[]?{}\t".indexOf(ch) >= 0;
    }
}
