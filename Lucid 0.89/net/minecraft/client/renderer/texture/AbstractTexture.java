package net.minecraft.client.renderer.texture;

import org.lwjgl.opengl.GL11;

public abstract class AbstractTexture implements ITextureObject
{
    protected int glTextureId = -1;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean blurLast;
    protected boolean mipmapLast;

    public void setBlurMipmapDirect(boolean p_174937_1_, boolean p_174937_2_)
    {
        this.blur = p_174937_1_;
        this.mipmap = p_174937_2_;
        int var5;
        short var6;

        if (p_174937_1_)
        {
            var5 = p_174937_2_ ? 9987 : 9729;
            var6 = 9729;
        }
        else
        {
            var5 = p_174937_2_ ? 9986 : 9728;
            var6 = 9728;
        }

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, var5);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, var6);
    }

    @Override
	public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_)
    {
        this.blurLast = this.blur;
        this.mipmapLast = this.mipmap;
        this.setBlurMipmapDirect(p_174936_1_, p_174936_2_);
    }

    @Override
	public void restoreLastBlurMipmap()
    {
        this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
    }

    @Override
	public int getGlTextureId()
    {
        if (this.glTextureId == -1)
        {
            this.glTextureId = TextureUtil.glGenTextures();
        }

        return this.glTextureId;
    }

    public void deleteGlTexture()
    {
        if (this.glTextureId != -1)
        {
            TextureUtil.deleteTexture(this.glTextureId);
            this.glTextureId = -1;
        }
    }
}
