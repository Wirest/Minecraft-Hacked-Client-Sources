package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.MathHelper;

public class ModelOcelot extends ModelBase {
   ModelRenderer ocelotBackLeftLeg;
   ModelRenderer ocelotBackRightLeg;
   ModelRenderer ocelotFrontLeftLeg;
   ModelRenderer ocelotFrontRightLeg;
   ModelRenderer ocelotTail;
   ModelRenderer ocelotTail2;
   ModelRenderer ocelotHead;
   ModelRenderer ocelotBody;
   int field_78163_i = 1;
   private static final String __OBFID = "CL_00000848";

   public ModelOcelot() {
      this.setTextureOffset("head.main", 0, 0);
      this.setTextureOffset("head.nose", 0, 24);
      this.setTextureOffset("head.ear1", 0, 10);
      this.setTextureOffset("head.ear2", 6, 10);
      this.ocelotHead = new ModelRenderer(this, "head");
      this.ocelotHead.addBox("main", -2.5F, -2.0F, -3.0F, 5, 4, 5);
      this.ocelotHead.addBox("nose", -1.5F, 0.0F, -4.0F, 3, 2, 2);
      this.ocelotHead.addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2);
      this.ocelotHead.addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2);
      this.ocelotHead.setRotationPoint(0.0F, 15.0F, -9.0F);
      this.ocelotBody = new ModelRenderer(this, 20, 0);
      this.ocelotBody.addBox(-2.0F, 3.0F, -8.0F, 4, 16, 6, 0.0F);
      this.ocelotBody.setRotationPoint(0.0F, 12.0F, -10.0F);
      this.ocelotTail = new ModelRenderer(this, 0, 15);
      this.ocelotTail.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
      this.ocelotTail.rotateAngleX = 0.9F;
      this.ocelotTail.setRotationPoint(0.0F, 15.0F, 8.0F);
      this.ocelotTail2 = new ModelRenderer(this, 4, 15);
      this.ocelotTail2.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
      this.ocelotTail2.setRotationPoint(0.0F, 20.0F, 14.0F);
      this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13);
      this.ocelotBackLeftLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
      this.ocelotBackLeftLeg.setRotationPoint(1.1F, 18.0F, 5.0F);
      this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13);
      this.ocelotBackRightLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
      this.ocelotBackRightLeg.setRotationPoint(-1.1F, 18.0F, 5.0F);
      this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0);
      this.ocelotFrontLeftLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
      this.ocelotFrontLeftLeg.setRotationPoint(1.2F, 13.8F, -5.0F);
      this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0);
      this.ocelotFrontRightLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
      this.ocelotFrontRightLeg.setRotationPoint(-1.2F, 13.8F, -5.0F);
   }

   public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
      if (this.isChild) {
         float var8 = 2.0F;
         GlStateManager.pushMatrix();
         GlStateManager.scale(1.5F / var8, 1.5F / var8, 1.5F / var8);
         GlStateManager.translate(0.0F, 10.0F * p_78088_7_, 4.0F * p_78088_7_);
         this.ocelotHead.render(p_78088_7_);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
         GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
         this.ocelotBody.render(p_78088_7_);
         this.ocelotBackLeftLeg.render(p_78088_7_);
         this.ocelotBackRightLeg.render(p_78088_7_);
         this.ocelotFrontLeftLeg.render(p_78088_7_);
         this.ocelotFrontRightLeg.render(p_78088_7_);
         this.ocelotTail.render(p_78088_7_);
         this.ocelotTail2.render(p_78088_7_);
         GlStateManager.popMatrix();
      } else {
         this.ocelotHead.render(p_78088_7_);
         this.ocelotBody.render(p_78088_7_);
         this.ocelotTail.render(p_78088_7_);
         this.ocelotTail2.render(p_78088_7_);
         this.ocelotBackLeftLeg.render(p_78088_7_);
         this.ocelotBackRightLeg.render(p_78088_7_);
         this.ocelotFrontLeftLeg.render(p_78088_7_);
         this.ocelotFrontRightLeg.render(p_78088_7_);
      }

   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      this.ocelotHead.rotateAngleX = p_78087_5_ / 57.295776F;
      this.ocelotHead.rotateAngleY = p_78087_4_ / 57.295776F;
      if (this.field_78163_i != 3) {
         this.ocelotBody.rotateAngleX = 1.5707964F;
         if (this.field_78163_i == 2) {
            this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.0F * p_78087_2_;
            this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 0.3F) * 1.0F * p_78087_2_;
            this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F + 0.3F) * 1.0F * p_78087_2_;
            this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.0F * p_78087_2_;
            this.ocelotTail2.rotateAngleX = 1.7278761F + 0.31415927F * MathHelper.cos(p_78087_1_) * p_78087_2_;
         } else {
            this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.0F * p_78087_2_;
            this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.0F * p_78087_2_;
            this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.0F * p_78087_2_;
            this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.0F * p_78087_2_;
            if (this.field_78163_i == 1) {
               this.ocelotTail2.rotateAngleX = 1.7278761F + 0.7853982F * MathHelper.cos(p_78087_1_) * p_78087_2_;
            } else {
               this.ocelotTail2.rotateAngleX = 1.7278761F + 0.47123894F * MathHelper.cos(p_78087_1_) * p_78087_2_;
            }
         }
      }

   }

   public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
      EntityOcelot var5 = (EntityOcelot)p_78086_1_;
      this.ocelotBody.rotationPointY = 12.0F;
      this.ocelotBody.rotationPointZ = -10.0F;
      this.ocelotHead.rotationPointY = 15.0F;
      this.ocelotHead.rotationPointZ = -9.0F;
      this.ocelotTail.rotationPointY = 15.0F;
      this.ocelotTail.rotationPointZ = 8.0F;
      this.ocelotTail2.rotationPointY = 20.0F;
      this.ocelotTail2.rotationPointZ = 14.0F;
      this.ocelotFrontLeftLeg.rotationPointY = this.ocelotFrontRightLeg.rotationPointY = 13.8F;
      this.ocelotFrontLeftLeg.rotationPointZ = this.ocelotFrontRightLeg.rotationPointZ = -5.0F;
      this.ocelotBackLeftLeg.rotationPointY = this.ocelotBackRightLeg.rotationPointY = 18.0F;
      this.ocelotBackLeftLeg.rotationPointZ = this.ocelotBackRightLeg.rotationPointZ = 5.0F;
      this.ocelotTail.rotateAngleX = 0.9F;
      ModelRenderer var10000;
      if (var5.isSneaking()) {
         ++this.ocelotBody.rotationPointY;
         var10000 = this.ocelotHead;
         var10000.rotationPointY += 2.0F;
         ++this.ocelotTail.rotationPointY;
         var10000 = this.ocelotTail2;
         var10000.rotationPointY += -4.0F;
         var10000 = this.ocelotTail2;
         var10000.rotationPointZ += 2.0F;
         this.ocelotTail.rotateAngleX = 1.5707964F;
         this.ocelotTail2.rotateAngleX = 1.5707964F;
         this.field_78163_i = 0;
      } else if (var5.isSprinting()) {
         this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
         var10000 = this.ocelotTail2;
         var10000.rotationPointZ += 2.0F;
         this.ocelotTail.rotateAngleX = 1.5707964F;
         this.ocelotTail2.rotateAngleX = 1.5707964F;
         this.field_78163_i = 2;
      } else if (var5.isSitting()) {
         this.ocelotBody.rotateAngleX = 0.7853982F;
         var10000 = this.ocelotBody;
         var10000.rotationPointY += -4.0F;
         var10000 = this.ocelotBody;
         var10000.rotationPointZ += 5.0F;
         var10000 = this.ocelotHead;
         var10000.rotationPointY += -3.3F;
         ++this.ocelotHead.rotationPointZ;
         var10000 = this.ocelotTail;
         var10000.rotationPointY += 8.0F;
         var10000 = this.ocelotTail;
         var10000.rotationPointZ += -2.0F;
         var10000 = this.ocelotTail2;
         var10000.rotationPointY += 2.0F;
         var10000 = this.ocelotTail2;
         var10000.rotationPointZ += -0.8F;
         this.ocelotTail.rotateAngleX = 1.7278761F;
         this.ocelotTail2.rotateAngleX = 2.670354F;
         this.ocelotFrontLeftLeg.rotateAngleX = this.ocelotFrontRightLeg.rotateAngleX = -0.15707964F;
         this.ocelotFrontLeftLeg.rotationPointY = this.ocelotFrontRightLeg.rotationPointY = 15.8F;
         this.ocelotFrontLeftLeg.rotationPointZ = this.ocelotFrontRightLeg.rotationPointZ = -7.0F;
         this.ocelotBackLeftLeg.rotateAngleX = this.ocelotBackRightLeg.rotateAngleX = -1.5707964F;
         this.ocelotBackLeftLeg.rotationPointY = this.ocelotBackRightLeg.rotationPointY = 21.0F;
         this.ocelotBackLeftLeg.rotationPointZ = this.ocelotBackRightLeg.rotationPointZ = 1.0F;
         this.field_78163_i = 3;
      } else {
         this.field_78163_i = 1;
      }

   }
}
