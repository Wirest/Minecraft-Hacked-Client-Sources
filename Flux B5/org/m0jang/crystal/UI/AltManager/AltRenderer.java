package org.m0jang.crystal.UI.AltManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;

public class AltRenderer {
   public static void drawAltFace(String name, int x, int y, int w, int h, boolean selected) {
      try {
         AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
         Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
         Tessellator var3 = Tessellator.getInstance();
         WorldRenderer var4 = var3.getWorldRenderer();
         GL11.glEnable(3042);
         if (selected) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         } else {
            GL11.glColor4f(0.9F, 0.9F, 0.9F, 1.0F);
         }

         double fw = 32.0D;
         double fh = 32.0D;
         double u = 32.0D;
         double v = 32.0D;
         var4.startDrawingQuads();
         var4.addVertexWithUV((double)x + 0.0D, (double)y + (double)h, 0.0D, (double)((float)(u + 0.0D) * 0.00390625F), (double)((float)(v + fh) * 0.00390625F));
         var4.addVertexWithUV((double)x + (double)w, (double)y + (double)h, 0.0D, (double)((float)(u + fw) * 0.00390625F), (double)((float)(v + fh) * 0.00390625F));
         var4.addVertexWithUV((double)x + (double)w, (double)y + 0.0D, 0.0D, (double)((float)(u + fw) * 0.00390625F), (double)((float)(v + 0.0D) * 0.00390625F));
         var4.addVertexWithUV((double)x + 0.0D, (double)y + 0.0D, 0.0D, (double)((float)(u + 0.0D) * 0.00390625F), (double)((float)(v + 0.0D) * 0.00390625F));
         var3.draw();
         fw = 32.0D;
         fh = 32.0D;
         u = 160.0D;
         v = 32.0D;
         var4.startDrawingQuads();
         var4.addVertexWithUV((double)x + 0.0D, (double)y + (double)h, 0.0D, (double)((float)(u + 0.0D) * 0.00390625F), (double)((float)(v + fh) * 0.00390625F));
         var4.addVertexWithUV((double)x + (double)w, (double)y + (double)h, 0.0D, (double)((float)(u + fw) * 0.00390625F), (double)((float)(v + fh) * 0.00390625F));
         var4.addVertexWithUV((double)x + (double)w, (double)y + 0.0D, 0.0D, (double)((float)(u + fw) * 0.00390625F), (double)((float)(v + 0.0D) * 0.00390625F));
         var4.addVertexWithUV((double)x + 0.0D, (double)y + 0.0D, 0.0D, (double)((float)(u + 0.0D) * 0.00390625F), (double)((float)(v + 0.0D) * 0.00390625F));
         var3.draw();
         GL11.glDisable(3042);
      } catch (Exception var16) {
         var16.printStackTrace();
      }

   }
}
