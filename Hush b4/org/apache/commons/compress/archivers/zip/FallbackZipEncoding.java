// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

class FallbackZipEncoding implements ZipEncoding
{
    private final String charsetName;
    
    public FallbackZipEncoding() {
        this.charsetName = null;
    }
    
    public FallbackZipEncoding(final String charsetName) {
        this.charsetName = charsetName;
    }
    
    public boolean canEncode(final String name) {
        return true;
    }
    
    public ByteBuffer encode(final String name) throws IOException {
        if (this.charsetName == null) {
            return ByteBuffer.wrap(name.getBytes());
        }
        return ByteBuffer.wrap(name.getBytes(this.charsetName));
    }
    
    public String decode(final byte[] data) throws IOException {
        if (this.charsetName == null) {
            return new String(data);
        }
        return new String(data, this.charsetName);
    }
}
