/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.item.EntityMinecartTNT;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class RenderTntMinecart extends RenderMinecart<EntityMinecartTNT>
/*    */ {
/*    */   public RenderTntMinecart(RenderManager renderManagerIn)
/*    */   {
/* 15 */     super(renderManagerIn);
/*    */   }
/*    */   
/*    */   protected void func_180560_a(EntityMinecartTNT minecart, float partialTicks, IBlockState state)
/*    */   {
/* 20 */     int i = minecart.getFuseTicks();
/*    */     
/* 22 */     if ((i > -1) && (i - partialTicks + 1.0F < 10.0F))
/*    */     {
/* 24 */       float f = 1.0F - (i - partialTicks + 1.0F) / 10.0F;
/* 25 */       f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 26 */       f *= f;
/* 27 */       f *= f;
/* 28 */       float f1 = 1.0F + f * 0.3F;
/* 29 */       GlStateManager.scale(f1, f1, f1);
/*    */     }
/*    */     
/* 32 */     super.func_180560_a(minecart, partialTicks, state);
/*    */     
/* 34 */     if ((i > -1) && (i / 5 % 2 == 0))
/*    */     {
/* 36 */       BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 37 */       GlStateManager.disableTexture2D();
/* 38 */       GlStateManager.disableLighting();
/* 39 */       GlStateManager.enableBlend();
/* 40 */       GlStateManager.blendFunc(770, 772);
/* 41 */       GlStateManager.color(1.0F, 1.0F, 1.0F, (1.0F - (i - partialTicks + 1.0F) / 100.0F) * 0.8F);
/* 42 */       GlStateManager.pushMatrix();
/* 43 */       blockrendererdispatcher.renderBlockBrightness(net.minecraft.init.Blocks.tnt.getDefaultState(), 1.0F);
/* 44 */       GlStateManager.popMatrix();
/* 45 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 46 */       GlStateManager.disableBlend();
/* 47 */       GlStateManager.enableLighting();
/* 48 */       GlStateManager.enableTexture2D();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderTntMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */