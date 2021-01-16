package me.razerboy420.weepcraft.gui.newclick.extras;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;

public class CategoryButton {

   public Module.Category category;
   public float x;
   public float y;
   public float width;
   public float height;


   public CategoryButton(Module.Category category, float x, float y, float width, float height) {
      this.category = category;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void draw() {
      int color = this.isHovered()?ColorUtil.getHexColor(Weepcraft.enabledColor):ColorUtil.getHexColor(Weepcraft.disabledColor);
      Gui.drawCenteredString(Wrapper.fr(), this.category.name(), this.x + this.width / 2.0F, this.y, color);
   }

   public boolean isHovered() {
      return this.x < (float)MouseUtils.getMouseX() && this.x + 90.0F > (float)MouseUtils.getMouseX() && this.y < (float)MouseUtils.getMouseY() && this.y + 10.0F > (float)MouseUtils.getMouseY();
   }
}
