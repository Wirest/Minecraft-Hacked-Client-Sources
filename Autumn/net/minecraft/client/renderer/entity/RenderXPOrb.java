package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderXPOrb extends Render {
   private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");

   public RenderXPOrb(RenderManager renderManagerIn) {
      super(renderManagerIn);
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)x, (float)y, (float)z);
      this.bindEntityTexture(entity);
      int i = entity.getTextureByXP();
      float f = (float)(i % 4 * 16) / 64.0F;
      float f1 = (float)(i % 4 * 16 + 16) / 64.0F;
      float f2 = (float)(i / 4 * 16) / 64.0F;
      float f3 = (float)(i / 4 * 16 + 16) / 64.0F;
      float f4 = 1.0F;
      float f5 = 0.5F;
      float f6 = 0.25F;
      int j = entity.getBrightnessForRender(partialTicks);
      int k = j % 65536;
      int l = j / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float f8 = 255.0F;
      float f9 = ((float)entity.xpColor + partialTicks) / 2.0F;
      l = (int)((MathHelper.sin(f9 + 0.0F) + 1.0F) * 0.5F * 255.0F);
      int i1 = true;
      int j1 = (int)((MathHelper.sin(f9 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
      float playerViewY = this.renderManager.playerViewY;
      GlStateManager.rotate(180.0F - playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-playerViewY, 1.0F, 0.0F, 0.0F);
      float f7 = 0.3F;
      GlStateManager.scale(0.3F, 0.3F, 0.3F);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
      worldrenderer.pos(-0.5D, -0.25D, 0.0D).tex((double)f, (double)f3).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      worldrenderer.pos(0.5D, -0.25D, 0.0D).tex((double)f1, (double)f3).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      worldrenderer.pos(0.5D, 0.75D, 0.0D).tex((double)f1, (double)f2).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      worldrenderer.pos(-0.5D, 0.75D, 0.0D).tex((double)f, (double)f2).color(l, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      tessellator.draw();
      GlStateManager.disableBlend();
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(entity, x, y, z, entityYaw, partialTicks);
   }

   protected ResourceLocation getEntityTexture(EntityXPOrb entity) {
      return experienceOrbTextures;
   }
}
