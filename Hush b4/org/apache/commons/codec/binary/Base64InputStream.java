// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import java.io.InputStream;

public class Base64InputStream extends BaseNCodecInputStream
{
    public Base64InputStream(final InputStream in) {
        this(in, false);
    }
    
    public Base64InputStream(final InputStream in, final boolean doEncode) {
        super(in, new Base64(false), doEncode);
    }
    
    public Base64InputStream(final InputStream in, final boolean doEncode, final int lineLength, final byte[] lineSeparator) {
        super(in, new Base64(lineLength, lineSeparator), doEncode);
    }
}
