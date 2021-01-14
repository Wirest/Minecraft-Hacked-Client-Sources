package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBiped extends ModelBase {
   public ModelRenderer bipedHead;
   public ModelRenderer bipedHeadwear;
   public ModelRenderer bipedBody;
   public ModelRenderer bipedRightArm;
   public ModelRenderer bipedLeftArm;
   public ModelRenderer bipedRightLeg;
   public ModelRenderer bipedLeftLeg;
   public int heldItemLeft;
   public int heldItemRight;
   public boolean isSneak;
   public boolean aimedBow;

   public ModelBiped() {
      this(0.0F);
   }

   public ModelBiped(float modelSize) {
      this(modelSize, 0.0F, 64, 32);
   }

   public ModelBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
      this.textureWidth = textureWidthIn;
      this.textureHeight = textureHeightIn;
      this.bipedHead = new ModelRenderer(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
      this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
      this.bipedHeadwear = new ModelRenderer(this, 32, 0);
      this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
      this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
      this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 16);
      this.bipedLeftArm.mirror = true;
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
      this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.mirror = true;
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
      this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
   }

   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
      GlStateManager.pushMatrix();
      if (this.isChild) {
         float f = 2.0F;
         GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
         GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
         this.bipedHead.render(scale);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
         GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
      } else {
         if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         this.bipedHead.render(scale);
      }

      this.bipedBody.render(scale);
      this.bipedRightArm.render(scale);
      this.bipedLeftArm.render(scale);
      this.bipedRightLeg.render(scale);
      this.bipedLeftLeg.render(scale);
      this.bipedHeadwear.render(scale);
      GlStateManager.popMatrix();
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
      this.bipedHead.rotateAngleY = p_78087_4_ / 57.295776F;
      this.bipedHead.rotateAngleX = p_78087_5_ / 57.295776F;
      this.bipedRightArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 2.0F * p_78087_2_ * 0.5F;
      this.bipedLeftArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F * p_78087_2_ * 0.5F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
      this.bipedLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_;
      this.bipedRightLeg.rotateAngleY = 0.0F;
      this.bipedLeftLeg.rotateAngleY = 0.0F;
      ModelRenderer var10000;
      if (this.isRiding) {
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX += -0.62831855F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX += -0.62831855F;
         this.bipedRightLeg.rotateAngleX = -1.2566371F;
         this.bipedLeftLeg.rotateAngleX = -1.2566371F;
         this.bipedRightLeg.rotateAngleY = 0.31415927F;
         this.bipedLeftLeg.rotateAngleY = -0.31415927F;
      }

      if (this.heldItemLeft != 0) {
         this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemLeft;
      }

      this.bipedRightArm.rotateAngleY = 0.0F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      switch(this.heldItemRight) {
      case 0:
      case 2:
      default:
         break;
      case 1:
         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemRight;
         break;
      case 3:
         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemRight;
         this.bipedRightArm.rotateAngleY = -0.5235988F;
      }

      this.bipedLeftArm.rotateAngleY = 0.0F;
      float f3;
      float f4;
      if (this.swingProgress > -9990.0F) {
         f3 = this.swingProgress;
         this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f3) * 3.1415927F * 2.0F) * 0.2F;
         this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleY += this.bipedBody.rotateAngleY;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleY += this.bipedBody.rotateAngleY;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX += this.bipedBody.rotateAngleY;
         f3 = 1.0F - this.swingProgress;
         f3 *= f3;
         f3 *= f3;
         f3 = 1.0F - f3;
         f4 = MathHelper.sin(f3 * 3.1415927F);
         float f2 = MathHelper.sin(this.swingProgress * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
         this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f4 * 1.2D + (double)f2));
         var10000 = this.bipedRightArm;
         var10000.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927F) * -0.4F;
      }

      if (this.isSneak) {
         this.bipedBody.rotateAngleX = 0.5F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX += 0.4F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX += 0.4F;
         this.bipedRightLeg.rotationPointZ = 4.0F;
         this.bipedLeftLeg.rotationPointZ = 4.0F;
         this.bipedRightLeg.rotationPointY = 9.0F;
         this.bipedLeftLeg.rotationPointY = 9.0F;
         this.bipedHead.rotationPointY = 1.0F;
      } else {
         this.bipedBody.rotateAngleX = 0.0F;
         this.bipedRightLeg.rotationPointZ = 0.1F;
         this.bipedLeftLeg.rotationPointZ = 0.1F;
         this.bipedRightLeg.rotationPointY = 12.0F;
         this.bipedLeftLeg.rotationPointY = 12.0F;
         this.bipedHead.rotationPointY = 0.0F;
      }

      var10000 = this.bipedRightArm;
      var10000.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
      if (this.aimedBow) {
         f3 = 0.0F;
         f4 = 0.0F;
         this.bipedRightArm.rotateAngleZ = 0.0F;
         this.bipedLeftArm.rotateAngleZ = 0.0F;
         this.bipedRightArm.rotateAngleY = -(0.1F - f3 * 0.6F) + this.bipedHead.rotateAngleY;
         this.bipedLeftArm.rotateAngleY = 0.1F - f3 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
         this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
      }

      copyModelAngles(this.bipedHead, this.bipedHeadwear);
   }

   public void setModelAttributes(ModelBase model) {
      super.setModelAttributes(model);
      if (model instanceof ModelBiped) {
         ModelBiped modelbiped = (ModelBiped)model;
         this.heldItemLeft = modelbiped.heldItemLeft;
         this.heldItemRight = modelbiped.heldItemRight;
         this.isSneak = modelbiped.isSneak;
         this.aimedBow = modelbiped.aimedBow;
      }

   }

   public void setInvisible(boolean invisible) {
      this.bipedHead.showModel = invisible;
      this.bipedHeadwear.showModel = invisible;
      this.bipedBody.showModel = invisible;
      this.bipedRightArm.showModel = invisible;
      this.bipedLeftArm.showModel = invisible;
      this.bipedRightLeg.showModel = invisible;
      this.bipedLeftLeg.showModel = invisible;
   }

   public void postRenderArm(float scale) {
      this.bipedRightArm.postRender(scale);
   }
}
