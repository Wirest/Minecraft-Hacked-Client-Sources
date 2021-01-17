// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.client.renderer.texture.ITextureObject;

public class CustomTexture
{
    private int textureUnit;
    private String path;
    private ITextureObject texture;
    
    public CustomTexture(final int textureUnit, final String path, final ITextureObject texture) {
        this.textureUnit = -1;
        this.path = null;
        this.texture = null;
        this.textureUnit = textureUnit;
        this.path = path;
        this.texture = texture;
    }
    
    public int getTextureUnit() {
        return this.textureUnit;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public ITextureObject getTexture() {
        return this.texture;
    }
    
    @Override
    public String toString() {
        return "textureUnit: " + this.textureUnit + ", path: " + this.path + ", glTextureId: " + this.texture.getGlTextureId();
    }
}
