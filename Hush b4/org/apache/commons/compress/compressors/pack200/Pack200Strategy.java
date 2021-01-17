// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.pack200;

import java.io.IOException;

public enum Pack200Strategy
{
    IN_MEMORY {
        @Override
        StreamBridge newStreamBridge() {
            return new InMemoryCachingStreamBridge();
        }
    }, 
    TEMP_FILE {
        @Override
        StreamBridge newStreamBridge() throws IOException {
            return new TempFileCachingStreamBridge();
        }
    };
    
    abstract StreamBridge newStreamBridge() throws IOException;
}
