// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import java.io.IOException;
import java.util.Collection;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.Reader;
import org.apache.http.annotation.Immutable;

@Immutable
public class PublicSuffixListParser
{
    private static final int MAX_LINE_LEN = 256;
    private final PublicSuffixFilter filter;
    
    PublicSuffixListParser(final PublicSuffixFilter filter) {
        this.filter = filter;
    }
    
    public void parse(final Reader list) throws IOException {
        final Collection<String> rules = new ArrayList<String>();
        final Collection<String> exceptions = new ArrayList<String>();
        final BufferedReader r = new BufferedReader(list);
        final StringBuilder sb = new StringBuilder(256);
        boolean more = true;
        while (more) {
            more = this.readLine(r, sb);
            String line = sb.toString();
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("//")) {
                continue;
            }
            if (line.startsWith(".")) {
                line = line.substring(1);
            }
            final boolean isException = line.startsWith("!");
            if (isException) {
                line = line.substring(1);
            }
            if (isException) {
                exceptions.add(line);
            }
            else {
                rules.add(line);
            }
        }
        this.filter.setPublicSuffixes(rules);
        this.filter.setExceptions(exceptions);
    }
    
    private boolean readLine(final Reader r, final StringBuilder sb) throws IOException {
        sb.setLength(0);
        boolean hitWhitespace = false;
        int b;
        while ((b = r.read()) != -1) {
            final char c = (char)b;
            if (c == '\n') {
                break;
            }
            if (Character.isWhitespace(c)) {
                hitWhitespace = true;
            }
            if (!hitWhitespace) {
                sb.append(c);
            }
            if (sb.length() > 256) {
                throw new IOException("Line too long");
            }
        }
        return b != -1;
    }
}
