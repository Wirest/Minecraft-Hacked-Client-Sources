package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class TextureClock extends TextureAtlasSprite
{
    private double field_94239_h;
    private double field_94240_i;
    private static final String __OBFID = "CL_00001070";

    public TextureClock(String p_i1285_1_)
    {
        super(p_i1285_1_);
    }

    public void updateAnimation()
    {
        if (!this.framesTextureData.isEmpty())
        {
            Minecraft var1 = Minecraft.getMinecraft();
            double var2 = 0.0D;

            if (var1.theWorld != null && var1.thePlayer != null)
            {
                float var7 = var1.theWorld.getCelestialAngle(1.0F);
                var2 = (double)var7;

                if (!var1.theWorld.provider.isSurfaceWorld())
                {
                    var2 = Math.random();
                }
            }

            double var71;

            for (var71 = var2 - this.field_94239_h; var71 < -0.5D; ++var71)
            {
                ;
            }

            while (var71 >= 0.5D)
            {
                --var71;
            }

            var71 = MathHelper.clamp_double(var71, -1.0D, 1.0D);
            this.field_94240_i += var71 * 0.1D;
            this.field_94240_i *= 0.8D;
            this.field_94239_h += this.field_94240_i;
            int var6;

            for (var6 = (int)((this.field_94239_h + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); var6 < 0; var6 = (var6 + this.framesTextureData.size()) % this.framesTextureData.size())
            {
                ;
            }

            if (var6 != this.frameCounter)
            {
                this.frameCounter = var6;

                if (Config.isShaders())
                {
                    ShadersTex.uploadTexSub((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), this.width, this.height, this.originX, this.originY, false, false);
                }
                else
                {
                    TextureUtil.uploadTextureMipmap((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), this.width, this.height, this.originX, this.originY, false, false);
                }
            }
        }
    }
}
