package me.slowly.client.ui;

import me.slowly.client.ui.clickgui.UIClick;
import me.slowly.client.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;

public class UIMenuLoad extends GuiScreen {
   private UIClick uiClick = new UIClick();
   private boolean open = true;

   public void onGuiClosed() {
      this.open = true;
      super.onGuiClosed();
   }

   public UIClick getUiClick() {
      return this.uiClick;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      if (this.uiClick.initialized) {
         this.mc.displayGuiScreen(this.uiClick);
      } else {
         RenderUtil.drawLoadingCircle();
         if (this.open) {
            this.open = false;
            this.uiClick.load();
         }

         super.drawScreen(mouseX, mouseY, partialTicks);
      }
   }
}
