package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public class WindowButton extends Button {
   public final int targetWindow;

   public WindowButton(Window window, int id, int offX, int offY, String title, String tooltip, int targetWindow) {
      super(window, id, offX, offY, title);
      this.width = Math.max(WolframGui.defaultWidth, window.width);
      this.height = WolframGui.buttonHeight;
      this.targetWindow = targetWindow;
      this.type = "WindowButton";
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
      this.isToggled = Crystal.INSTANCE.guiManager.gui.getWindowByID(this.targetWindow).isEnabled;
   }

   protected void pressed() {
      Crystal.INSTANCE.guiManager.gui.getWindowByID(this.targetWindow).isEnabled = !Crystal.INSTANCE.guiManager.gui.getWindowByID(this.targetWindow).isEnabled;
      if (Crystal.INSTANCE.guiManager.gui.getWindowByID(this.targetWindow).isEnabled) {
         Crystal.INSTANCE.guiManager.gui.getWindowByID(this.targetWindow).bringToFront();
      }

   }
}
