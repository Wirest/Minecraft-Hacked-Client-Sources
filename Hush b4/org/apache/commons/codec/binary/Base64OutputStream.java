// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import java.io.OutputStream;

public class Base64OutputStream extends BaseNCodecOutputStream
{
    public Base64OutputStream(final OutputStream out) {
        this(out, true);
    }
    
    public Base64OutputStream(final OutputStream out, final boolean doEncode) {
        super(out, new Base64(false), doEncode);
    }
    
    public Base64OutputStream(final OutputStream out, final boolean doEncode, final int lineLength, final byte[] lineSeparator) {
        super(out, new Base64(lineLength, lineSeparator), doEncode);
    }
}
