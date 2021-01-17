// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ZipEncoding
{
    boolean canEncode(final String p0);
    
    ByteBuffer encode(final String p0) throws IOException;
    
    String decode(final byte[] p0) throws IOException;
}
