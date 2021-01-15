/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.ItemModelMesher;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.projectile.EntityFireball;
/*    */ import net.minecraft.init.Items;
/*    */ 
/*    */ public class RenderFireball extends Render<EntityFireball>
/*    */ {
/*    */   private float scale;
/*    */   
/*    */   public RenderFireball(RenderManager renderManagerIn, float scaleIn)
/*    */   {
/* 20 */     super(renderManagerIn);
/* 21 */     this.scale = scaleIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 32 */     GlStateManager.pushMatrix();
/* 33 */     bindEntityTexture(entity);
/* 34 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 35 */     GlStateManager.enableRescaleNormal();
/* 36 */     GlStateManager.scale(this.scale, this.scale, this.scale);
/* 37 */     TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
/* 38 */     Tessellator tessellator = Tessellator.getInstance();
/* 39 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 40 */     float f = textureatlassprite.getMinU();
/* 41 */     float f1 = textureatlassprite.getMaxU();
/* 42 */     float f2 = textureatlassprite.getMinV();
/* 43 */     float f3 = textureatlassprite.getMaxV();
/* 44 */     float f4 = 1.0F;
/* 45 */     float f5 = 0.5F;
/* 46 */     float f6 = 0.25F;
/* 47 */     GlStateManager.rotate(180.0F - RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 48 */     GlStateManager.rotate(-RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 49 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
/* 50 */     worldrenderer.pos(-0.5D, -0.25D, 0.0D).tex(f, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 51 */     worldrenderer.pos(0.5D, -0.25D, 0.0D).tex(f1, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 52 */     worldrenderer.pos(0.5D, 0.75D, 0.0D).tex(f1, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 53 */     worldrenderer.pos(-0.5D, 0.75D, 0.0D).tex(f, f2).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 54 */     tessellator.draw();
/* 55 */     GlStateManager.disableRescaleNormal();
/* 56 */     GlStateManager.popMatrix();
/* 57 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected net.minecraft.util.ResourceLocation getEntityTexture(EntityFireball entity)
/*    */   {
/* 65 */     return TextureMap.locationBlocksTexture;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */