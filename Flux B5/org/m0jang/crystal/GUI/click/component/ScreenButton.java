package org.m0jang.crystal.GUI.click.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;

public class ScreenButton extends Button {
   public GuiScreen screen;

   public ScreenButton(Window window, int id, int offX, int offY, String title, String tooltip, GuiScreen screen) {
      super(window, id, offX, offY, title);
      this.width = Math.max(WolframGui.defaultWidth, window.width);
      this.height = WolframGui.buttonHeight;
      this.screen = screen;
      this.type = "ScreenButton";
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
   }

   protected void pressed() {
      Minecraft.getMinecraft().displayGuiScreen(this.screen);
   }
}
