// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

class Simple8BitZipEncoding implements ZipEncoding
{
    private final char[] highChars;
    private final List<Simple8BitChar> reverseMapping;
    
    public Simple8BitZipEncoding(final char[] highChars) {
        this.highChars = highChars.clone();
        final List<Simple8BitChar> temp = new ArrayList<Simple8BitChar>(this.highChars.length);
        byte code = 127;
        for (final char highChar : this.highChars) {
            final List<Simple8BitChar> list = temp;
            ++code;
            list.add(new Simple8BitChar(code, highChar));
        }
        Collections.sort(temp);
        this.reverseMapping = Collections.unmodifiableList((List<? extends Simple8BitChar>)temp);
    }
    
    public char decodeByte(final byte b) {
        if (b >= 0) {
            return (char)b;
        }
        return this.highChars[128 + b];
    }
    
    public boolean canEncodeChar(final char c) {
        if (c >= '\0' && c < '\u0080') {
            return true;
        }
        final Simple8BitChar r = this.encodeHighChar(c);
        return r != null;
    }
    
    public boolean pushEncodedChar(final ByteBuffer bb, final char c) {
        if (c >= '\0' && c < '\u0080') {
            bb.put((byte)c);
            return true;
        }
        final Simple8BitChar r = this.encodeHighChar(c);
        if (r == null) {
            return false;
        }
        bb.put(r.code);
        return true;
    }
    
    private Simple8BitChar encodeHighChar(final char c) {
        int i0 = 0;
        int i2 = this.reverseMapping.size();
        while (i2 > i0) {
            final int j = i0 + (i2 - i0) / 2;
            final Simple8BitChar m = this.reverseMapping.get(j);
            if (m.unicode == c) {
                return m;
            }
            if (m.unicode < c) {
                i0 = j + 1;
            }
            else {
                i2 = j;
            }
        }
        if (i0 >= this.reverseMapping.size()) {
            return null;
        }
        final Simple8BitChar r = this.reverseMapping.get(i0);
        if (r.unicode != c) {
            return null;
        }
        return r;
    }
    
    public boolean canEncode(final String name) {
        for (int i = 0; i < name.length(); ++i) {
            final char c = name.charAt(i);
            if (!this.canEncodeChar(c)) {
                return false;
            }
        }
        return true;
    }
    
    public ByteBuffer encode(final String name) {
        ByteBuffer out = ByteBuffer.allocate(name.length() + 6 + (name.length() + 1) / 2);
        for (int i = 0; i < name.length(); ++i) {
            final char c = name.charAt(i);
            if (out.remaining() < 6) {
                out = ZipEncodingHelper.growBuffer(out, out.position() + 6);
            }
            if (!this.pushEncodedChar(out, c)) {
                ZipEncodingHelper.appendSurrogate(out, c);
            }
        }
        out.limit(out.position());
        out.rewind();
        return out;
    }
    
    public String decode(final byte[] data) throws IOException {
        final char[] ret = new char[data.length];
        for (int i = 0; i < data.length; ++i) {
            ret[i] = this.decodeByte(data[i]);
        }
        return new String(ret);
    }
    
    private static final class Simple8BitChar implements Comparable<Simple8BitChar>
    {
        public final char unicode;
        public final byte code;
        
        Simple8BitChar(final byte code, final char unicode) {
            this.code = code;
            this.unicode = unicode;
        }
        
        public int compareTo(final Simple8BitChar a) {
            return this.unicode - a.unicode;
        }
        
        @Override
        public String toString() {
            return "0x" + Integer.toHexString('\uffff' & this.unicode) + "->0x" + Integer.toHexString(0xFF & this.code);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof Simple8BitChar) {
                final Simple8BitChar other = (Simple8BitChar)o;
                return this.unicode == other.unicode && this.code == other.code;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.unicode;
        }
    }
}
