// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.util.BlockPos;
import shadersmod.client.ShadersTex;
import optifine.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;

public class TextureCompass extends TextureAtlasSprite
{
    public double currentAngle;
    public double angleDelta;
    public static String field_176608_l;
    private static final String __OBFID = "CL_00001071";
    
    public TextureCompass(final String iconName) {
        super(iconName);
        TextureCompass.field_176608_l = iconName;
    }
    
    @Override
    public void updateAnimation() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.theWorld != null && Minecraft.thePlayer != null) {
            this.updateCompass(minecraft.theWorld, Minecraft.thePlayer.posX, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, false, false);
        }
        else {
            this.updateCompass(null, 0.0, 0.0, 0.0, true, false);
        }
    }
    
    public void updateCompass(final World worldIn, final double p_94241_2_, final double p_94241_4_, double p_94241_6_, final boolean p_94241_8_, final boolean p_94241_9_) {
        if (!this.framesTextureData.isEmpty()) {
            double d0 = 0.0;
            if (worldIn != null && !p_94241_8_) {
                final BlockPos blockpos = worldIn.getSpawnPoint();
                final double d2 = blockpos.getX() - p_94241_2_;
                final double d3 = blockpos.getZ() - p_94241_4_;
                p_94241_6_ %= 360.0;
                d0 = -((p_94241_6_ - 90.0) * 3.141592653589793 / 180.0 - Math.atan2(d3, d2));
                if (!worldIn.provider.isSurfaceWorld()) {
                    d0 = Math.random() * 3.141592653589793 * 2.0;
                }
            }
            if (p_94241_9_) {
                this.currentAngle = d0;
            }
            else {
                double d4;
                for (d4 = d0 - this.currentAngle; d4 < -3.141592653589793; d4 += 6.283185307179586) {}
                while (d4 >= 3.141592653589793) {
                    d4 -= 6.283185307179586;
                }
                d4 = MathHelper.clamp_double(d4, -1.0, 1.0);
                this.angleDelta += d4 * 0.1;
                this.angleDelta *= 0.8;
                this.currentAngle += this.angleDelta;
            }
            int i;
            for (i = (int)((this.currentAngle / 6.283185307179586 + 1.0) * this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size()) {}
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
