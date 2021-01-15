/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.projectile.EntityArrow;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class RenderArrow extends Render<EntityArrow>
/*    */ {
/* 14 */   private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
/*    */   
/*    */   public RenderArrow(RenderManager renderManagerIn)
/*    */   {
/* 18 */     super(renderManagerIn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 29 */     bindEntityTexture(entity);
/* 30 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 31 */     GlStateManager.pushMatrix();
/* 32 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 33 */     GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
/* 34 */     GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
/* 35 */     Tessellator tessellator = Tessellator.getInstance();
/* 36 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 37 */     int i = 0;
/* 38 */     float f = 0.0F;
/* 39 */     float f1 = 0.5F;
/* 40 */     float f2 = (0 + i * 10) / 32.0F;
/* 41 */     float f3 = (5 + i * 10) / 32.0F;
/* 42 */     float f4 = 0.0F;
/* 43 */     float f5 = 0.15625F;
/* 44 */     float f6 = (5 + i * 10) / 32.0F;
/* 45 */     float f7 = (10 + i * 10) / 32.0F;
/* 46 */     float f8 = 0.05625F;
/* 47 */     GlStateManager.enableRescaleNormal();
/* 48 */     float f9 = entity.arrowShake - partialTicks;
/*    */     
/* 50 */     if (f9 > 0.0F)
/*    */     {
/* 52 */       float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
/* 53 */       GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
/*    */     }
/*    */     
/* 56 */     GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
/* 57 */     GlStateManager.scale(f8, f8, f8);
/* 58 */     GlStateManager.translate(-4.0F, 0.0F, 0.0F);
/* 59 */     GL11.glNormal3f(f8, 0.0F, 0.0F);
/* 60 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 61 */     worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex(f4, f6).endVertex();
/* 62 */     worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex(f5, f6).endVertex();
/* 63 */     worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex(f5, f7).endVertex();
/* 64 */     worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex(f4, f7).endVertex();
/* 65 */     tessellator.draw();
/* 66 */     GL11.glNormal3f(-f8, 0.0F, 0.0F);
/* 67 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 68 */     worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex(f4, f6).endVertex();
/* 69 */     worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex(f5, f6).endVertex();
/* 70 */     worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex(f5, f7).endVertex();
/* 71 */     worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex(f4, f7).endVertex();
/* 72 */     tessellator.draw();
/*    */     
/* 74 */     for (int j = 0; j < 4; j++)
/*    */     {
/* 76 */       GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/* 77 */       GL11.glNormal3f(0.0F, 0.0F, f8);
/* 78 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 79 */       worldrenderer.pos(-8.0D, -2.0D, 0.0D).tex(f, f2).endVertex();
/* 80 */       worldrenderer.pos(8.0D, -2.0D, 0.0D).tex(f1, f2).endVertex();
/* 81 */       worldrenderer.pos(8.0D, 2.0D, 0.0D).tex(f1, f3).endVertex();
/* 82 */       worldrenderer.pos(-8.0D, 2.0D, 0.0D).tex(f, f3).endVertex();
/* 83 */       tessellator.draw();
/*    */     }
/*    */     
/* 86 */     GlStateManager.disableRescaleNormal();
/* 87 */     GlStateManager.popMatrix();
/* 88 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityArrow entity)
/*    */   {
/* 96 */     return arrowTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */