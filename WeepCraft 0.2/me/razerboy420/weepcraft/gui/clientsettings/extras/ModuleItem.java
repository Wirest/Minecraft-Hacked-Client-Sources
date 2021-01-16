package me.razerboy420.weepcraft.gui.clientsettings.extras;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;

public class ModuleItem {

   public Module mod;
   public float x;
   public float y;
   public float width;
   public float height;
   public boolean selected;


   public ModuleItem(Module mod, float x, float y, float width, float height) {
      this.mod = mod;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void draw() {
      if(this.selected) {
         Wrapper.drawBorderRect(this.x, this.y, this.x + this.width, this.y + this.height, -10000537, -16777216, 1.0F);
      }

      Gui.drawString(Wrapper.fr(), this.mod.getName(), this.x + 2.0F, this.y + 2.0F, -1);
      Gui.drawString(Wrapper.fr(), (this.mod.isToggled()?ColorUtil.getColor(Weepcraft.enabledColor):ColorUtil.getColor(Weepcraft.disabledColor)) + this.mod.isToggled(), this.x + 2.0F, this.y + 12.0F, -1);
      Gui.drawString(Wrapper.fr(), (this.mod.isVisible()?ColorUtil.getColor(Weepcraft.enabledColor):ColorUtil.getColor(Weepcraft.disabledColor)) + this.mod.isVisible(), this.x + 2.0F, this.y + 22.0F, -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Mod: ", this.x - (float)Wrapper.fr().getStringWidth("Mod:"), this.y + 2.0F, -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Toggled: ", this.x - (float)Wrapper.fr().getStringWidth("Toggled:"), this.y + 12.0F, -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Shown: ", this.x - (float)Wrapper.fr().getStringWidth("Shown:"), this.y + 22.0F, -1);
   }

   public boolean isHovered() {
      return this.x < (float)MouseUtils.getMouseX() && this.x + this.width > (float)MouseUtils.getMouseX() && this.y < (float)MouseUtils.getMouseY() && this.y + this.height > (float)MouseUtils.getMouseY();
   }
}
