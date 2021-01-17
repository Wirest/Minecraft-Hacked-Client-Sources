// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import shadersmod.client.ShadersTex;
import optifine.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class TextureClock extends TextureAtlasSprite
{
    private double field_94239_h;
    private double field_94240_i;
    private static final String __OBFID = "CL_00001070";
    
    public TextureClock(final String iconName) {
        super(iconName);
    }
    
    @Override
    public void updateAnimation() {
        if (!this.framesTextureData.isEmpty()) {
            final Minecraft minecraft = Minecraft.getMinecraft();
            double d0 = 0.0;
            if (minecraft.theWorld != null && Minecraft.thePlayer != null) {
                d0 = minecraft.theWorld.getCelestialAngle(1.0f);
                if (!minecraft.theWorld.provider.isSurfaceWorld()) {
                    d0 = Math.random();
                }
            }
            double d2;
            for (d2 = d0 - this.field_94239_h; d2 < -0.5; ++d2) {}
            while (d2 >= 0.5) {
                --d2;
            }
            d2 = MathHelper.clamp_double(d2, -1.0, 1.0);
            this.field_94240_i += d2 * 0.1;
            this.field_94240_i *= 0.8;
            this.field_94239_h += this.field_94240_i;
            int i;
            for (i = (int)((this.field_94239_h + 1.0) * this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size()) {}
            if (i != this.frameCounter) {
                this.frameCounter = i;
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSub(this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
                }
                else {
                    TextureUtil.uploadTextureMipmap(this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
                }
            }
        }
    }
}
