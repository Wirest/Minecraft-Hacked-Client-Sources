/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.item.EntityTNTPrimed;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderTNTPrimed extends Render<EntityTNTPrimed>
/*    */ {
/*    */   public RenderTNTPrimed(RenderManager renderManagerIn)
/*    */   {
/* 16 */     super(renderManagerIn);
/* 17 */     this.shadowSize = 0.5F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 28 */     BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 29 */     GlStateManager.pushMatrix();
/* 30 */     GlStateManager.translate((float)x, (float)y + 0.5F, (float)z);
/*    */     
/* 32 */     if (entity.fuse - partialTicks + 1.0F < 10.0F)
/*    */     {
/* 34 */       float f = 1.0F - (entity.fuse - partialTicks + 1.0F) / 10.0F;
/* 35 */       f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 36 */       f *= f;
/* 37 */       f *= f;
/* 38 */       float f1 = 1.0F + f * 0.3F;
/* 39 */       GlStateManager.scale(f1, f1, f1);
/*    */     }
/*    */     
/* 42 */     float f2 = (1.0F - (entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
/* 43 */     bindEntityTexture(entity);
/* 44 */     GlStateManager.translate(-0.5F, -0.5F, 0.5F);
/* 45 */     blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entity.getBrightness(partialTicks));
/* 46 */     GlStateManager.translate(0.0F, 0.0F, 1.0F);
/*    */     
/* 48 */     if (entity.fuse / 5 % 2 == 0)
/*    */     {
/* 50 */       GlStateManager.disableTexture2D();
/* 51 */       GlStateManager.disableLighting();
/* 52 */       GlStateManager.enableBlend();
/* 53 */       GlStateManager.blendFunc(770, 772);
/* 54 */       GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
/* 55 */       GlStateManager.doPolygonOffset(-3.0F, -3.0F);
/* 56 */       GlStateManager.enablePolygonOffset();
/* 57 */       blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
/* 58 */       GlStateManager.doPolygonOffset(0.0F, 0.0F);
/* 59 */       GlStateManager.disablePolygonOffset();
/* 60 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 61 */       GlStateManager.disableBlend();
/* 62 */       GlStateManager.enableLighting();
/* 63 */       GlStateManager.enableTexture2D();
/*    */     }
/*    */     
/* 66 */     GlStateManager.popMatrix();
/* 67 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(EntityTNTPrimed entity)
/*    */   {
/* 75 */     return net.minecraft.client.renderer.texture.TextureMap.locationBlocksTexture;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderTNTPrimed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */