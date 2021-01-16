package me.razerboy420.weepcraft.gui.newclick.extras;

import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.value.Value;

public class ValueButton {

   public Value value;
   public float x;
   public float y;
   public float width;
   public float height;


   public ValueButton(Value value, float x, float y, float width, float height) {
      this.value = value;
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
