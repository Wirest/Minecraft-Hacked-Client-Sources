package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class DynamicTexture extends AbstractTexture
{
    private final int[] dynamicTextureData;

    /** width of this icon in pixels */
    private final int width;

    /** height of this icon in pixels */
    private final int height;
    private static final String __OBFID = "CL_00001048";
    private boolean shadersInitialized;

    public DynamicTexture(BufferedImage p_i1270_1_)
    {
        this(p_i1270_1_.getWidth(), p_i1270_1_.getHeight());
        p_i1270_1_.getRGB(0, 0, p_i1270_1_.getWidth(), p_i1270_1_.getHeight(), this.dynamicTextureData, 0, p_i1270_1_.getWidth());
        this.updateDynamicTexture();
    }

    public DynamicTexture(int p_i1271_1_, int p_i1271_2_)
    {
        this.shadersInitialized = false;
        this.width = p_i1271_1_;
        this.height = p_i1271_2_;
        this.dynamicTextureData = new int[p_i1271_1_ * p_i1271_2_ * 3];

        if (Config.isShaders())
        {
            ShadersTex.initDynamicTexture(this.getGlTextureId(), p_i1271_1_, p_i1271_2_, this);
            this.shadersInitialized = true;
        }
        else
        {
            TextureUtil.allocateTexture(this.getGlTextureId(), p_i1271_1_, p_i1271_2_);
        }
    }

    public void loadTexture(IResourceManager p_110551_1_) throws IOException {}

    public void updateDynamicTexture()
    {
        if (Config.isShaders())
        {
            if (!this.shadersInitialized)
            {
                ShadersTex.initDynamicTexture(this.getGlTextureId(), this.width, this.height, this);
                this.shadersInitialized = true;
            }

            ShadersTex.updateDynamicTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height, this);
        }
        else
        {
            TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
        }
    }

    public int[] getTextureData()
    {
        return this.dynamicTextureData;
    }
}
