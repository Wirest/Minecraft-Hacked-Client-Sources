// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

class InMemoryCachingStreamBridge extends StreamBridge
{
    InMemoryCachingStreamBridge() {
        super(new ByteArrayOutputStream());
    }
    
    @Override
    InputStream getInputView() throws IOException {
        return new ByteArrayInputStream(((ByteArrayOutputStream)this.out).toByteArray());
    }
}
