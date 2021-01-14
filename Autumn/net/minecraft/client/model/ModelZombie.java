package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelZombie extends ModelBiped {
   public ModelZombie() {
      this(0.0F, false);
   }

   protected ModelZombie(float modelSize, float p_i1167_2_, int textureWidthIn, int textureHeightIn) {
      super(modelSize, p_i1167_2_, textureWidthIn, textureHeightIn);
   }

   public ModelZombie(float modelSize, boolean p_i1168_2_) {
      super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
      super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
      float f = MathHelper.sin(this.swingProgress * 3.1415927F);
      float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
      this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
      this.bipedRightArm.rotateAngleX = -1.5707964F;
      this.bipedLeftArm.rotateAngleX = -1.5707964F;
      ModelRenderer var10000 = this.bipedRightArm;
      var10000.rotateAngleX -= f * 1.2F - f1 * 0.4F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX -= f * 1.2F - f1 * 0.4F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
   }
}
