package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public abstract class Button extends Component {
   public boolean isToggled;

   public Button(Window window, int id, int offX, int offY, String title) {
      super(window, id, offX, offY, title);
      this.width = WolframGui.defaultWidth;
      this.height = WolframGui.buttonHeight;
      this.type = "Button";
   }

   public void render(int mouseX, int mouseY) {
      if (this.isHovered) {
         RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)this.height, 0);
      } else {
         RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)this.height, WolframGui.backgroundColor);
      }

      Fonts.segoe16.drawString(this.title, (float)(this.x + 2), (float)(this.y + this.height / 2 - Fonts.segoe16.getHeight() / 2), this.isToggled ? GuiManager.getHexMainColor() : 16777215, false);
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
   }

   protected abstract void pressed();

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
      this.isHovered = this.contains(mouseX, mouseY) && this.window.mouseOver(mouseX, mouseY);
      if (isPressed && !this.wasMousePressed && this.isHovered) {
         this.pressed();
      }

      this.wasMousePressed = isPressed;
   }
}
