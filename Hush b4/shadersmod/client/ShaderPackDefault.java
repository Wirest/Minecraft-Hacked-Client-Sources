// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import java.io.InputStream;

public class ShaderPackDefault implements IShaderPack
{
    @Override
    public void close() {
    }
    
    @Override
    public InputStream getResourceAsStream(final String resName) {
        return ShaderPackDefault.class.getResourceAsStream(resName);
    }
    
    @Override
    public String getName() {
        return Shaders.packNameDefault;
    }
    
    @Override
    public boolean hasDirectory(final String name) {
        return false;
    }
}
