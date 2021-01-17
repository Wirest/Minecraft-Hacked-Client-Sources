// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.pack200;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;

class TempFileCachingStreamBridge extends StreamBridge
{
    private final File f;
    
    TempFileCachingStreamBridge() throws IOException {
        (this.f = File.createTempFile("commons-compress", "packtemp")).deleteOnExit();
        this.out = new FileOutputStream(this.f);
    }
    
    @Override
    InputStream getInputView() throws IOException {
        this.out.close();
        return new FileInputStream(this.f) {
            @Override
            public void close() throws IOException {
                super.close();
                TempFileCachingStreamBridge.this.f.delete();
            }
        };
    }
}
