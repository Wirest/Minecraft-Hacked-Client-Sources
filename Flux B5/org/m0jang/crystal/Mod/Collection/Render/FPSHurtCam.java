package org.m0jang.crystal.Mod.Collection.Render;

import assets.minecraft.euphoria.Texture;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Events.EventRender2D;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class FPSHurtCam extends Module {
   public FPSHurtCam() {
      super("FPSHurtCam", Category.Render, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   private void onRender3D(EventRender2D event) {
      this.drawVignette();
   }

   private void drawVignette() {
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
      int healthPersent = (int)(Minecraft.thePlayer.getHealth() / Minecraft.thePlayer.getMaxHealth() * 100.0F);
      if (healthPersent <= 10) {
         GL11.glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
      } else if (healthPersent <= 20) {
         GL11.glColor4f(0.0F, 0.8F, 0.8F, 1.0F);
      } else if (healthPersent <= 50) {
         GL11.glColor4f(0.0F, 0.5F, 0.5F, 1.0F);
      } else {
         GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);
      }

      Minecraft var10002 = Minecraft.getMinecraft();
      Minecraft.getMinecraft();
      int var10003 = Minecraft.displayWidth;
      Minecraft.getMinecraft();
      ScaledResolution scaledResolution = new ScaledResolution(var10002, var10003, Minecraft.displayHeight);
      this.mc.getTextureManager().bindTexture(Texture.fpsHurt);
      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      var10.startDrawingQuads();
      var10.addVertexWithUV(0.0D, (double)scaledResolution.getScaledHeight(), -90.0D, 0.0D, 1.0D);
      var10.addVertexWithUV((double)scaledResolution.getScaledWidth(), (double)scaledResolution.getScaledHeight(), -90.0D, 1.0D, 1.0D);
      var10.addVertexWithUV((double)scaledResolution.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
      var10.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
      var9.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
   }
}
