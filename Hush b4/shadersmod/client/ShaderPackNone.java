// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.io.InputStream;

public class ShaderPackNone implements IShaderPack
{
    @Override
    public void close() {
    }
    
    @Override
    public InputStream getResourceAsStream(final String resName) {
        return null;
    }
    
    @Override
    public boolean hasDirectory(final String name) {
        return false;
    }
    
    @Override
    public String getName() {
        return Shaders.packNameNone;
    }
}
