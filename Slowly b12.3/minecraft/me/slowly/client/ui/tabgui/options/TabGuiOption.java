package me.slowly.client.ui.tabgui.options;

import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;

public class TabGuiOption {
   public Value value;
   public int x;
   public int y;
   public int width;
   public int height;
   public Minecraft mc = Minecraft.getMinecraft();
   public float animationX;

   public TabGuiOption(Value value, int x, int y, int height) {
      this.value = value;
      this.x = x;
      this.y = y;
      this.height = height;
      this.width = this.mc.fontRendererObj.getStringWidth(this.getName()) + 2;
   }

   public void draw() {
   }

   public String getName() {
      return this.value.getValueName().split("_")[1];
   }
}
