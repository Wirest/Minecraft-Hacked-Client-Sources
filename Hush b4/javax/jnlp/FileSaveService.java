// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.io.InputStream;
import java.io.IOException;

public interface FileSaveService
{
    FileContents saveAsFileDialog(final String p0, final String[] p1, final FileContents p2) throws IOException;
    
    FileContents saveFileDialog(final String p0, final String[] p1, final InputStream p2, final String p3) throws IOException;
}
