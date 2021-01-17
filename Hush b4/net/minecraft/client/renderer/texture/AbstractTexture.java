// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import shadersmod.client.ShadersTex;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import shadersmod.client.MultiTexID;

public abstract class AbstractTexture implements ITextureObject
{
    protected int glTextureId;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean blurLast;
    protected boolean mipmapLast;
    private static final String __OBFID = "CL_00001047";
    public MultiTexID multiTex;
    
    public AbstractTexture() {
        this.glTextureId = -1;
    }
    
    public void setBlurMipmapDirect(final boolean p_174937_1_, final boolean p_174937_2_) {
        this.blur = p_174937_1_;
        this.mipmap = p_174937_2_;
        final boolean flag = true;
        final boolean flag2 = true;
        int i;
        short short1;
        if (p_174937_1_) {
            i = (p_174937_2_ ? 9987 : 9729);
            short1 = 9729;
        }
        else {
            i = (p_174937_2_ ? 9986 : 9728);
            short1 = 9728;
        }
        GlStateManager.bindTexture(this.getGlTextureId());
        GL11.glTexParameteri(3553, 10241, i);
        GL11.glTexParameteri(3553, 10240, short1);
    }
    
    @Override
    public void setBlurMipmap(final boolean p_174936_1_, final boolean p_174936_2_) {
        this.blurLast = this.blur;
        this.mipmapLast = this.mipmap;
        this.setBlurMipmapDirect(p_174936_1_, p_174936_2_);
    }
    
    @Override
    public void restoreLastBlurMipmap() {
        this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
    }
    
    @Override
    public int getGlTextureId() {
        if (this.glTextureId == -1) {
            this.glTextureId = TextureUtil.glGenTextures();
        }
        return this.glTextureId;
    }
    
    public void deleteGlTexture() {
        ShadersTex.deleteTextures(this, this.glTextureId);
        if (this.glTextureId != -1) {
            TextureUtil.deleteTexture(this.glTextureId);
            this.glTextureId = -1;
        }
    }
    
    @Override
    public MultiTexID getMultiTexID() {
        return ShadersTex.getMultiTexID(this);
    }
}
