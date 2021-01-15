/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelBlaze
/*    */   extends ModelBase
/*    */ {
/*  9 */   private ModelRenderer[] blazeSticks = new ModelRenderer[12];
/*    */   private ModelRenderer blazeHead;
/*    */   
/*    */   public ModelBlaze()
/*    */   {
/* 14 */     for (int i = 0; i < this.blazeSticks.length; i++)
/*    */     {
/* 16 */       this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
/* 17 */       this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
/*    */     }
/*    */     
/* 20 */     this.blazeHead = new ModelRenderer(this, 0, 0);
/* 21 */     this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*    */   {
/* 29 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 30 */     this.blazeHead.render(scale);
/*    */     
/* 32 */     for (int i = 0; i < this.blazeSticks.length; i++)
/*    */     {
/* 34 */       this.blazeSticks[i].render(scale);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*    */   {
/* 45 */     float f = p_78087_3_ * 3.1415927F * -0.1F;
/*    */     
/* 47 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 49 */       this.blazeSticks[i].rotationPointY = (-2.0F + MathHelper.cos((i * 2 + p_78087_3_) * 0.25F));
/* 50 */       this.blazeSticks[i].rotationPointX = (MathHelper.cos(f) * 9.0F);
/* 51 */       this.blazeSticks[i].rotationPointZ = (MathHelper.sin(f) * 9.0F);
/* 52 */       f += 1.0F;
/*    */     }
/*    */     
/* 55 */     f = 0.7853982F + p_78087_3_ * 3.1415927F * 0.03F;
/*    */     
/* 57 */     for (int j = 4; j < 8; j++)
/*    */     {
/* 59 */       this.blazeSticks[j].rotationPointY = (2.0F + MathHelper.cos((j * 2 + p_78087_3_) * 0.25F));
/* 60 */       this.blazeSticks[j].rotationPointX = (MathHelper.cos(f) * 7.0F);
/* 61 */       this.blazeSticks[j].rotationPointZ = (MathHelper.sin(f) * 7.0F);
/* 62 */       f += 1.0F;
/*    */     }
/*    */     
/* 65 */     f = 0.47123894F + p_78087_3_ * 3.1415927F * -0.05F;
/*    */     
/* 67 */     for (int k = 8; k < 12; k++)
/*    */     {
/* 69 */       this.blazeSticks[k].rotationPointY = (11.0F + MathHelper.cos((k * 1.5F + p_78087_3_) * 0.5F));
/* 70 */       this.blazeSticks[k].rotationPointX = (MathHelper.cos(f) * 5.0F);
/* 71 */       this.blazeSticks[k].rotationPointZ = (MathHelper.sin(f) * 5.0F);
/* 72 */       f += 1.0F;
/*    */     }
/*    */     
/* 75 */     this.blazeHead.rotateAngleY = (p_78087_4_ / 57.295776F);
/* 76 */     this.blazeHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */