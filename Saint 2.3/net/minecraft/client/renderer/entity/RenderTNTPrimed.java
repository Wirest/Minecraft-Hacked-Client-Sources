package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderTNTPrimed extends Render {
   private static final String __OBFID = "CL_00001030";

   public RenderTNTPrimed(RenderManager p_i46134_1_) {
      super(p_i46134_1_);
      this.shadowSize = 0.5F;
   }

   public void doRender(EntityTNTPrimed p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      BlockRendererDispatcher var10 = Minecraft.getMinecraft().getBlockRendererDispatcher();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_ + 0.5F, (float)p_76986_6_);
      float var11;
      if ((float)p_76986_1_.fuse - p_76986_9_ + 1.0F < 10.0F) {
         var11 = 1.0F - ((float)p_76986_1_.fuse - p_76986_9_ + 1.0F) / 10.0F;
         var11 = MathHelper.clamp_float(var11, 0.0F, 1.0F);
         var11 *= var11;
         var11 *= var11;
         float var12 = 1.0F + var11 * 0.3F;
         GlStateManager.scale(var12, var12, var12);
      }

      var11 = (1.0F - ((float)p_76986_1_.fuse - p_76986_9_ + 1.0F) / 100.0F) * 0.8F;
      this.bindEntityTexture(p_76986_1_);
      GlStateManager.translate(-0.5F, -0.5F, 0.5F);
      var10.func_175016_a(Blocks.tnt.getDefaultState(), p_76986_1_.getBrightness(p_76986_9_));
      GlStateManager.translate(0.0F, 0.0F, 1.0F);
      if (p_76986_1_.fuse / 5 % 2 == 0) {
         GlStateManager.func_179090_x();
         GlStateManager.disableLighting();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 772);
         GlStateManager.color(1.0F, 1.0F, 1.0F, var11);
         GlStateManager.doPolygonOffset(-3.0F, -3.0F);
         GlStateManager.enablePolygonOffset();
         var10.func_175016_a(Blocks.tnt.getDefaultState(), 1.0F);
         GlStateManager.doPolygonOffset(0.0F, 0.0F);
         GlStateManager.disablePolygonOffset();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.enableLighting();
         GlStateManager.func_179098_w();
      }

      GlStateManager.popMatrix();
      super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   protected ResourceLocation func_180563_a(EntityTNTPrimed p_180563_1_) {
      return TextureMap.locationBlocksTexture;
   }

   protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
      return this.func_180563_a((EntityTNTPrimed)p_110775_1_);
   }

   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityTNTPrimed)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }
}
