/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.RenderHelper;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.boss.EntityDragon;
/*    */ 
/*    */ public class LayerEnderDragonDeath implements LayerRenderer<EntityDragon>
/*    */ {
/*    */   public void doRenderLayer(EntityDragon entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 15 */     if (entitylivingbaseIn.deathTicks > 0)
/*    */     {
/* 17 */       Tessellator tessellator = Tessellator.getInstance();
/* 18 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 19 */       RenderHelper.disableStandardItemLighting();
/* 20 */       float f = (entitylivingbaseIn.deathTicks + partialTicks) / 200.0F;
/* 21 */       float f1 = 0.0F;
/*    */       
/* 23 */       if (f > 0.8F)
/*    */       {
/* 25 */         f1 = (f - 0.8F) / 0.2F;
/*    */       }
/*    */       
/* 28 */       Random random = new Random(432L);
/* 29 */       GlStateManager.disableTexture2D();
/* 30 */       GlStateManager.shadeModel(7425);
/* 31 */       GlStateManager.enableBlend();
/* 32 */       GlStateManager.blendFunc(770, 1);
/* 33 */       GlStateManager.disableAlpha();
/* 34 */       GlStateManager.enableCull();
/* 35 */       GlStateManager.depthMask(false);
/* 36 */       GlStateManager.pushMatrix();
/* 37 */       GlStateManager.translate(0.0F, -1.0F, -2.0F);
/*    */       
/* 39 */       for (int i = 0; i < (f + f * f) / 2.0F * 60.0F; i++)
/*    */       {
/* 41 */         GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
/* 42 */         GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
/* 43 */         GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
/* 44 */         GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
/* 45 */         GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
/* 46 */         GlStateManager.rotate(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
/* 47 */         float f2 = random.nextFloat() * 20.0F + 5.0F + f1 * 10.0F;
/* 48 */         float f3 = random.nextFloat() * 2.0F + 1.0F + f1 * 2.0F;
/* 49 */         worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
/* 50 */         worldrenderer.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int)(255.0F * (1.0F - f1))).endVertex();
/* 51 */         worldrenderer.pos(-0.866D * f3, f2, -0.5F * f3).color(255, 0, 255, 0).endVertex();
/* 52 */         worldrenderer.pos(0.866D * f3, f2, -0.5F * f3).color(255, 0, 255, 0).endVertex();
/* 53 */         worldrenderer.pos(0.0D, f2, 1.0F * f3).color(255, 0, 255, 0).endVertex();
/* 54 */         worldrenderer.pos(-0.866D * f3, f2, -0.5F * f3).color(255, 0, 255, 0).endVertex();
/* 55 */         tessellator.draw();
/*    */       }
/*    */       
/* 58 */       GlStateManager.popMatrix();
/* 59 */       GlStateManager.depthMask(true);
/* 60 */       GlStateManager.disableCull();
/* 61 */       GlStateManager.disableBlend();
/* 62 */       GlStateManager.shadeModel(7424);
/* 63 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 64 */       GlStateManager.enableTexture2D();
/* 65 */       GlStateManager.enableAlpha();
/* 66 */       RenderHelper.enableStandardItemLighting();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerEnderDragonDeath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */