/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityMagmaCube;
/*    */ 
/*    */ public class ModelMagmaCube extends ModelBase
/*    */ {
/*  9 */   ModelRenderer[] segments = new ModelRenderer[8];
/*    */   ModelRenderer core;
/*    */   
/*    */   public ModelMagmaCube()
/*    */   {
/* 14 */     for (int i = 0; i < this.segments.length; i++)
/*    */     {
/* 16 */       int j = 0;
/* 17 */       int k = i;
/*    */       
/* 19 */       if (i == 2)
/*    */       {
/* 21 */         j = 24;
/* 22 */         k = 10;
/*    */       }
/* 24 */       else if (i == 3)
/*    */       {
/* 26 */         j = 24;
/* 27 */         k = 19;
/*    */       }
/*    */       
/* 30 */       this.segments[i] = new ModelRenderer(this, j, k);
/* 31 */       this.segments[i].addBox(-4.0F, 16 + i, -4.0F, 8, 1, 8);
/*    */     }
/*    */     
/* 34 */     this.core = new ModelRenderer(this, 0, 16);
/* 35 */     this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*    */   {
/* 44 */     EntityMagmaCube entitymagmacube = (EntityMagmaCube)entitylivingbaseIn;
/* 45 */     float f = entitymagmacube.prevSquishFactor + (entitymagmacube.squishFactor - entitymagmacube.prevSquishFactor) * partialTickTime;
/*    */     
/* 47 */     if (f < 0.0F)
/*    */     {
/* 49 */       f = 0.0F;
/*    */     }
/*    */     
/* 52 */     for (int i = 0; i < this.segments.length; i++)
/*    */     {
/* 54 */       this.segments[i].rotationPointY = (-(4 - i) * f * 1.7F);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 63 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 64 */     this.core.render(scale);
/*    */     
/* 66 */     for (int i = 0; i < this.segments.length; i++)
/*    */     {
/* 68 */       this.segments[i].render(scale);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelMagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */