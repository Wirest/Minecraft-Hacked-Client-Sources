package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TextureCompass extends TextureAtlasSprite {
   public double currentAngle;
   public double angleDelta;
   public static String field_176608_l;

   public TextureCompass(String iconName) {
      super(iconName);
      field_176608_l = iconName;
   }

   public void updateAnimation() {
      Minecraft minecraft = Minecraft.getMinecraft();
      if (minecraft.theWorld != null && minecraft.thePlayer != null) {
         this.updateCompass(minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ, (double)minecraft.thePlayer.rotationYaw, false, false);
      } else {
         this.updateCompass((World)null, 0.0D, 0.0D, 0.0D, true, false);
      }

   }

   public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_) {
      if (!this.framesTextureData.isEmpty()) {
         double d0 = 0.0D;
         if (worldIn != null && !p_94241_8_) {
            BlockPos blockpos = worldIn.getSpawnPoint();
            double d1 = (double)blockpos.getX() - p_94241_2_;
            double d2 = (double)blockpos.getZ() - p_94241_4_;
            p_94241_6_ %= 360.0D;
            d0 = -((p_94241_6_ - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(d2, d1));
            if (!worldIn.provider.isSurfaceWorld()) {
               d0 = Math.random() * 3.141592653589793D * 2.0D;
            }
         }

         if (p_94241_9_) {
            this.currentAngle = d0;
         } else {
            double d3;
            for(d3 = d0 - this.currentAngle; d3 < -3.141592653589793D; d3 += 6.283185307179586D) {
            }

            while(d3 >= 3.141592653589793D) {
               d3 -= 6.283185307179586D;
            }

            d3 = MathHelper.clamp_double(d3, -1.0D, 1.0D);
            this.angleDelta += d3 * 0.1D;
            this.angleDelta *= 0.8D;
            this.currentAngle += this.angleDelta;
         }

         int i;
         for(i = (int)((this.currentAngle / 6.283185307179586D + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size()) {
         }

         if (i != this.frameCounter) {
            this.frameCounter = i;
            TextureUtil.uploadTextureMipmap((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), this.width, this.height, this.originX, this.originY, false, false);
         }
      }

   }
}
