package me.razerboy420.weepcraft.gui.newclick.extras;

import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.MouseUtils;

public class ModuleButton {

   public Module module;
   public float x;
   public float y;
   public float width;
   public float height;


   public ModuleButton(Module module, float x, float y, float width, float height) {
      this.module = module;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void draw() {}

   public boolean isHovered() {
      return this.x < (float)MouseUtils.getMouseX() && this.x + 90.0F > (float)MouseUtils.getMouseX() && this.y < (float)MouseUtils.getMouseY() && this.y + 10.0F > (float)MouseUtils.getMouseY();
   }
}
