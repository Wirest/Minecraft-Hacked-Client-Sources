// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import java.io.OutputStream;

public class Base32OutputStream extends BaseNCodecOutputStream
{
    public Base32OutputStream(final OutputStream out) {
        this(out, true);
    }
    
    public Base32OutputStream(final OutputStream out, final boolean doEncode) {
        super(out, new Base32(false), doEncode);
    }
    
    public Base32OutputStream(final OutputStream out, final boolean doEncode, final int lineLength, final byte[] lineSeparator) {
        super(out, new Base32(lineLength, lineSeparator), doEncode);
    }
}
