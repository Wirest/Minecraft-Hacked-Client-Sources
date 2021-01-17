package me.slowly.client.ui.options;

import me.slowly.client.ui.scriptmenu.UIScriptMenu;
import net.minecraft.client.gui.GuiScreen;

public class UIScript extends GuiScreen {
   private UIScriptMenu uiScriptMenu = new UIScriptMenu();

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.uiScriptMenu.draw(mouseX, mouseY);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      this.uiScriptMenu.mouseClicked(mouseX, mouseY);
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.uiScriptMenu.mouseReleased(mouseX, mouseY);
      super.mouseReleased(mouseX, mouseY, state);
   }
}
