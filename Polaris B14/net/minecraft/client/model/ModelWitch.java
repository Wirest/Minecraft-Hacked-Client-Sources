/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelWitch extends ModelVillager
/*    */ {
/*    */   public boolean field_82900_g;
/*  9 */   private ModelRenderer field_82901_h = new ModelRenderer(this).setTextureSize(64, 128);
/*    */   private ModelRenderer witchHat;
/*    */   
/*    */   public ModelWitch(float p_i46361_1_)
/*    */   {
/* 14 */     super(p_i46361_1_, 0.0F, 64, 128);
/* 15 */     this.field_82901_h.setRotationPoint(0.0F, -2.0F, 0.0F);
/* 16 */     this.field_82901_h.setTextureOffset(0, 0).addBox(0.0F, 3.0F, -6.75F, 1, 1, 1, -0.25F);
/* 17 */     this.villagerNose.addChild(this.field_82901_h);
/* 18 */     this.witchHat = new ModelRenderer(this).setTextureSize(64, 128);
/* 19 */     this.witchHat.setRotationPoint(-5.0F, -10.03125F, -5.0F);
/* 20 */     this.witchHat.setTextureOffset(0, 64).addBox(0.0F, 0.0F, 0.0F, 10, 2, 10);
/* 21 */     this.villagerHead.addChild(this.witchHat);
/* 22 */     ModelRenderer modelrenderer = new ModelRenderer(this).setTextureSize(64, 128);
/* 23 */     modelrenderer.setRotationPoint(1.75F, -4.0F, 2.0F);
/* 24 */     modelrenderer.setTextureOffset(0, 76).addBox(0.0F, 0.0F, 0.0F, 7, 4, 7);
/* 25 */     modelrenderer.rotateAngleX = -0.05235988F;
/* 26 */     modelrenderer.rotateAngleZ = 0.02617994F;
/* 27 */     this.witchHat.addChild(modelrenderer);
/* 28 */     ModelRenderer modelrenderer1 = new ModelRenderer(this).setTextureSize(64, 128);
/* 29 */     modelrenderer1.setRotationPoint(1.75F, -4.0F, 2.0F);
/* 30 */     modelrenderer1.setTextureOffset(0, 87).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
/* 31 */     modelrenderer1.rotateAngleX = -0.10471976F;
/* 32 */     modelrenderer1.rotateAngleZ = 0.05235988F;
/* 33 */     modelrenderer.addChild(modelrenderer1);
/* 34 */     ModelRenderer modelrenderer2 = new ModelRenderer(this).setTextureSize(64, 128);
/* 35 */     modelrenderer2.setRotationPoint(1.75F, -2.0F, 2.0F);
/* 36 */     modelrenderer2.setTextureOffset(0, 95).addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
/* 37 */     modelrenderer2.rotateAngleX = -0.20943952F;
/* 38 */     modelrenderer2.rotateAngleZ = 0.10471976F;
/* 39 */     modelrenderer1.addChild(modelrenderer2);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 49 */     super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
/* 50 */     this.villagerNose.offsetX = (this.villagerNose.offsetY = this.villagerNose.offsetZ = 0.0F);
/* 51 */     float f = 0.01F * (entityIn.getEntityId() % 10);
/* 52 */     this.villagerNose.rotateAngleX = (MathHelper.sin(entityIn.ticksExisted * f) * 4.5F * 3.1415927F / 180.0F);
/* 53 */     this.villagerNose.rotateAngleY = 0.0F;
/* 54 */     this.villagerNose.rotateAngleZ = (MathHelper.cos(entityIn.ticksExisted * f) * 2.5F * 3.1415927F / 180.0F);
/*    */     
/* 56 */     if (this.field_82900_g)
/*    */     {
/* 58 */       this.villagerNose.rotateAngleX = -0.9F;
/* 59 */       this.villagerNose.offsetZ = -0.09375F;
/* 60 */       this.villagerNose.offsetY = 0.1875F;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */