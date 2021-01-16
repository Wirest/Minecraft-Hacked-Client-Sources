package me.existdev.exist.utils;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class PictureUtils {
   // $FF: synthetic field
   private ResourceLocation location;
   // $FF: synthetic field
   private int x;
   // $FF: synthetic field
   private int y;
   // $FF: synthetic field
   private int width;
   // $FF: synthetic field
   private int height;
   // $FF: synthetic field
   private Color color;
   // $FF: synthetic field
   private Minecraft mc = Minecraft.getMinecraft();

   // $FF: synthetic method
   public PictureUtils(ResourceLocation location, int x, int y, int width, int height) {
      this.location = location;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = new Color(255, 255, 255, 255);
   }

   // $FF: synthetic method
   public PictureUtils(ResourceLocation location, int x, int y, int width, int height, Color color) {
      this.location = location;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }

   // $FF: synthetic method
   public void draw() {
      new ScaledResolution(this.mc, this.width, this.height);
      this.mc.getTextureManager().bindTexture(this.location);
      Gui.drawScaledCustomSizeModalRect(this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height, (float)this.width, (float)this.height);
   }
}
