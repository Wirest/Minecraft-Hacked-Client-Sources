/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.item.EntityXPOrb;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderXPOrb extends Render<EntityXPOrb>
/*    */ {
/* 14 */   private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");
/*    */   
/*    */   public RenderXPOrb(RenderManager renderManagerIn)
/*    */   {
/* 18 */     super(renderManagerIn);
/* 19 */     this.shadowSize = 0.15F;
/* 20 */     this.shadowOpaque = 0.75F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 31 */     GlStateManager.pushMatrix();
/* 32 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 33 */     bindEntityTexture(entity);
/* 34 */     int i = entity.getTextureByXP();
/* 35 */     float f = (i % 4 * 16 + 0) / 64.0F;
/* 36 */     float f1 = (i % 4 * 16 + 16) / 64.0F;
/* 37 */     float f2 = (i / 4 * 16 + 0) / 64.0F;
/* 38 */     float f3 = (i / 4 * 16 + 16) / 64.0F;
/* 39 */     float f4 = 1.0F;
/* 40 */     float f5 = 0.5F;
/* 41 */     float f6 = 0.25F;
/* 42 */     int j = entity.getBrightnessForRender(partialTicks);
/* 43 */     int k = j % 65536;
/* 44 */     int l = j / 65536;
/* 45 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);
/* 46 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 47 */     float f8 = 255.0F;
/* 48 */     float f9 = (entity.xpColor + partialTicks) / 2.0F;
/* 49 */     l = (int)((MathHelper.sin(f9 + 0.0F) + 1.0F) * 0.5F * 255.0F);
/* 50 */     int i1 = 255;
/* 51 */     int j1 = (int)((MathHelper.sin(f9 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
/* 52 */     GlStateManager.rotate(180.0F - RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 53 */     GlStateManager.rotate(-RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 54 */     float f7 = 0.3F;
/* 55 */     GlStateManager.scale(0.3F, 0.3F, 0.3F);
/* 56 */     Tessellator tessellator = Tessellator.getInstance();
/* 57 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 58 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
/* 59 */     worldrenderer.pos(0.0F - f5, 0.0F - f6, 0.0D).tex(f, f3).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 60 */     worldrenderer.pos(f4 - f5, 0.0F - f6, 0.0D).tex(f1, f3).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 61 */     worldrenderer.pos(f4 - f5, 1.0F - f6, 0.0D).tex(f1, f2).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 62 */     worldrenderer.pos(0.0F - f5, 1.0F - f6, 0.0D).tex(f, f2).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 63 */     tessellator.draw();
/* 64 */     GlStateManager.disableBlend();
/* 65 */     GlStateManager.disableRescaleNormal();
/* 66 */     GlStateManager.popMatrix();
/* 67 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityXPOrb entity)
/*    */   {
/* 75 */     return experienceOrbTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderXPOrb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */