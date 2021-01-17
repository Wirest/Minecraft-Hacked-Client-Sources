// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import java.io.InputStream;

public class Base32InputStream extends BaseNCodecInputStream
{
    public Base32InputStream(final InputStream in) {
        this(in, false);
    }
    
    public Base32InputStream(final InputStream in, final boolean doEncode) {
        super(in, new Base32(false), doEncode);
    }
    
    public Base32InputStream(final InputStream in, final boolean doEncode, final int lineLength, final byte[] lineSeparator) {
        super(in, new Base32(lineLength, lineSeparator), doEncode);
    }
}
