package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBlaze extends ModelBase {
   private ModelRenderer[] blazeSticks = new ModelRenderer[12];
   private ModelRenderer blazeHead;

   public ModelBlaze() {
      for(int i = 0; i < this.blazeSticks.length; ++i) {
         this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
         this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
      }

      this.blazeHead = new ModelRenderer(this, 0, 0);
      this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
   }

   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
      this.blazeHead.render(scale);

      for(int i = 0; i < this.blazeSticks.length; ++i) {
         this.blazeSticks[i].render(scale);
      }

   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
      float f = p_78087_3_ * 3.1415927F * -0.1F;

      int k;
      for(k = 0; k < 4; ++k) {
         this.blazeSticks[k].rotationPointY = -2.0F + MathHelper.cos(((float)(k * 2) + p_78087_3_) * 0.25F);
         this.blazeSticks[k].rotationPointX = MathHelper.cos(f) * 9.0F;
         this.blazeSticks[k].rotationPointZ = MathHelper.sin(f) * 9.0F;
         ++f;
      }

      f = 0.7853982F + p_78087_3_ * 3.1415927F * 0.03F;

      for(k = 4; k < 8; ++k) {
         this.blazeSticks[k].rotationPointY = 2.0F + MathHelper.cos(((float)(k * 2) + p_78087_3_) * 0.25F);
         this.blazeSticks[k].rotationPointX = MathHelper.cos(f) * 7.0F;
         this.blazeSticks[k].rotationPointZ = MathHelper.sin(f) * 7.0F;
         ++f;
      }

      f = 0.47123894F + p_78087_3_ * 3.1415927F * -0.05F;

      for(k = 8; k < 12; ++k) {
         this.blazeSticks[k].rotationPointY = 11.0F + MathHelper.cos(((float)k * 1.5F + p_78087_3_) * 0.5F);
         this.blazeSticks[k].rotationPointX = MathHelper.cos(f) * 5.0F;
         this.blazeSticks[k].rotationPointZ = MathHelper.sin(f) * 5.0F;
         ++f;
      }

      this.blazeHead.rotateAngleY = p_78087_4_ / 57.295776F;
      this.blazeHead.rotateAngleX = p_78087_5_ / 57.295776F;
   }
}
