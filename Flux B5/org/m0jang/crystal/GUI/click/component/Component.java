package org.m0jang.crystal.GUI.click.component;

import org.lwjgl.input.Mouse;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;
import org.m0jang.crystal.Utils.MathUtils;

public abstract class Component {
   public int x;
   public int y;
   public int width;
   public int height;
   public int offX;
   public int offY;
   public int id;
   public String title;
   protected boolean isHovered;
   public boolean isEnabled = true;
   protected boolean wasMousePressed;
   public Window window;
   public String type = "Component";
   public boolean editable = true;

   public Component(Window window, int id, int offX, int offY, String title) {
      this.window = window;
      this.offX = offX;
      this.offY = offY;
      this.title = title;
      this.id = id;
   }

   protected void reposition() {
      this.x = this.window.x + this.offX;
      this.y = this.window.y + this.offY - this.window.scrollY;
   }

   public boolean contains(int mouseX, int mouseY) {
      return MathUtils.contains((float)mouseX, (float)mouseY, (float)this.x, (float)this.y, (float)(this.x + this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)(this.y + this.height));
   }

   public void noMouseUpdates() {
      this.isHovered = false;
      this.wasMousePressed = Mouse.isButtonDown(0);
   }

   public void update(int mouseX, int mouseY) {
      this.reposition();
   }

   public abstract void render(int var1, int var2);

   public abstract void mouseUpdates(int var1, int var2, boolean var3);
}
