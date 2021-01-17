// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

public interface FileContents
{
    boolean canRead() throws IOException;
    
    boolean canWrite() throws IOException;
    
    InputStream getInputStream() throws IOException;
    
    long getLength() throws IOException;
    
    long getMaxLength() throws IOException;
    
    String getName() throws IOException;
    
    OutputStream getOutputStream(final boolean p0) throws IOException;
    
    JNLPRandomAccessFile getRandomAccessFile(final String p0) throws IOException;
    
    long setMaxLength(final long p0) throws IOException;
}
