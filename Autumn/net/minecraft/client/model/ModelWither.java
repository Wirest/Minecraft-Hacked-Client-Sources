package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;

public class ModelWither extends ModelBase {
   private ModelRenderer[] field_82905_a;
   private ModelRenderer[] field_82904_b;

   public ModelWither(float p_i46302_1_) {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.field_82905_a = new ModelRenderer[3];
      this.field_82905_a[0] = new ModelRenderer(this, 0, 16);
      this.field_82905_a[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, p_i46302_1_);
      this.field_82905_a[1] = (new ModelRenderer(this)).setTextureSize(this.textureWidth, this.textureHeight);
      this.field_82905_a[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
      this.field_82905_a[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, p_i46302_1_);
      this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, p_i46302_1_);
      this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, p_i46302_1_);
      this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, p_i46302_1_);
      this.field_82905_a[2] = new ModelRenderer(this, 12, 22);
      this.field_82905_a[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, p_i46302_1_);
      this.field_82904_b = new ModelRenderer[3];
      this.field_82904_b[0] = new ModelRenderer(this, 0, 0);
      this.field_82904_b[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, p_i46302_1_);
      this.field_82904_b[1] = new ModelRenderer(this, 32, 0);
      this.field_82904_b[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
      this.field_82904_b[1].rotationPointX = -8.0F;
      this.field_82904_b[1].rotationPointY = 4.0F;
      this.field_82904_b[2] = new ModelRenderer(this, 32, 0);
      this.field_82904_b[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
      this.field_82904_b[2].rotationPointX = 10.0F;
      this.field_82904_b[2].rotationPointY = 4.0F;
   }

   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
      ModelRenderer[] var8 = this.field_82904_b;
      int var9 = var8.length;

      int var10;
      ModelRenderer modelrenderer1;
      for(var10 = 0; var10 < var9; ++var10) {
         modelrenderer1 = var8[var10];
         modelrenderer1.render(scale);
      }

      var8 = this.field_82905_a;
      var9 = var8.length;

      for(var10 = 0; var10 < var9; ++var10) {
         modelrenderer1 = var8[var10];
         modelrenderer1.render(scale);
      }

   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn) {
      float f = MathHelper.cos(p_78087_3_ * 0.1F);
      this.field_82905_a[1].rotateAngleX = (0.065F + 0.05F * f) * 3.1415927F;
      this.field_82905_a[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(this.field_82905_a[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(this.field_82905_a[1].rotateAngleX) * 10.0F);
      this.field_82905_a[2].rotateAngleX = (0.265F + 0.1F * f) * 3.1415927F;
      this.field_82904_b[0].rotateAngleY = p_78087_4_ / 57.295776F;
      this.field_82904_b[0].rotateAngleX = p_78087_5_ / 57.295776F;
   }

   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
      EntityWither entitywither = (EntityWither)entitylivingbaseIn;

      for(int i = 1; i < 3; ++i) {
         this.field_82904_b[i].rotateAngleY = (entitywither.func_82207_a(i - 1) - entitylivingbaseIn.renderYawOffset) / 57.295776F;
         this.field_82904_b[i].rotateAngleX = entitywither.func_82210_r(i - 1) / 57.295776F;
      }

   }
}
