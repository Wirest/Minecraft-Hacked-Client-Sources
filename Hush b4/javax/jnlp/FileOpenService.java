// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.io.IOException;

public interface FileOpenService
{
    FileContents openFileDialog(final String p0, final String[] p1) throws IOException;
    
    FileContents[] openMultiFileDialog(final String p0, final String[] p1) throws IOException;
}
