// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.io.InputStream;

public interface IShaderPack
{
    String getName();
    
    InputStream getResourceAsStream(final String p0);
    
    boolean hasDirectory(final String p0);
    
    void close();
}
