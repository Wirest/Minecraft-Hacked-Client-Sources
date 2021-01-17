// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.sevenz;

import org.tukaani.xz.UnsupportedOptionsException;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.tukaani.xz.DeltaOptions;
import java.io.InputStream;

class DeltaDecoder extends CoderBase
{
    DeltaDecoder() {
        super((Class<?>[])new Class[] { Number.class });
    }
    
    @Override
    InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
        return new DeltaOptions(this.getOptionsFromCoder(coder)).getInputStream(in);
    }
    
    @Override
    OutputStream encode(final OutputStream out, final Object options) throws IOException {
        final int distance = CoderBase.numberOptionOrDefault(options, 1);
        try {
            return (OutputStream)new DeltaOptions(distance).getOutputStream((FinishableOutputStream)new FinishableWrapperOutputStream(out));
        }
        catch (UnsupportedOptionsException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    
    @Override
    byte[] getOptionsAsProperties(final Object options) {
        return new byte[] { (byte)(CoderBase.numberOptionOrDefault(options, 1) - 1) };
    }
    
    @Override
    Object getOptionsFromCoder(final Coder coder, final InputStream in) {
        return this.getOptionsFromCoder(coder);
    }
    
    private int getOptionsFromCoder(final Coder coder) {
        if (coder.properties == null || coder.properties.length == 0) {
            return 1;
        }
        return (0xFF & coder.properties[0]) + 1;
    }
}
