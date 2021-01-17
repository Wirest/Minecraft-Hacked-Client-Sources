package me.slowly.client.ui.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class UIMenuSlot {
   public String text;
   private GuiScreen screen;
   public double animationX;

   public UIMenuSlot(String text, GuiScreen screen) {
      this.text = text;
      this.screen = screen;
   }

   public void run() {
      if (this.screen == null) {
         Minecraft.getMinecraft().shutdown();
      } else {
         Minecraft.getMinecraft().displayGuiScreen(this.screen);
      }

   }
}
