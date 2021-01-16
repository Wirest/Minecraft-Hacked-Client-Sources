package me.razerboy420.weepcraft.gui.clientsettings.extras;

import org.lwjgl.input.Keyboard;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.keybinds.Keybind;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;

public class KeybindItem {

   public Keybind keybind;
   public float x;
   public float y;
   public float width;
   public float height;
   public boolean selected;


   public KeybindItem(Keybind keybind, float x, float y, float width, float height) {
      this.keybind = keybind;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void draw() {
      if(this.selected) {
         Wrapper.drawBorderRect(this.x, this.y, this.x + this.width, this.y + this.height, -10000537, -16777216, 1.0F);
      }

      Gui.drawString(Wrapper.fr(), Keyboard.getKeyName(this.keybind.getKey()), this.x + 2.0F, this.y + 2.0F, -1);
      Gui.drawString(Wrapper.fr(), this.keybind.mod.getName(), this.x + 2.0F, this.y + 12.0F, -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Key: ", this.x - (float)Wrapper.fr().getStringWidth("Key:"), this.y + 2.0F, -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Bind: ", this.x - (float)Wrapper.fr().getStringWidth("Bind:"), this.y + 12.0F, -1);
   }

   public boolean isHovered() {
      return this.x < (float)MouseUtils.getMouseX() && this.x + this.width > (float)MouseUtils.getMouseX() && this.y < (float)MouseUtils.getMouseY() && this.y + this.height > (float)MouseUtils.getMouseY();
   }
}
