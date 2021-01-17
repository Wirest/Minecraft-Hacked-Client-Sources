package me.slowly.client.ui.options;

import java.io.IOException;
import me.slowly.client.Client;
import net.minecraft.client.gui.GuiScreen;

public class UIDesign extends GuiScreen {
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      Client.getInstance();
      Client.uiCustomizer.draw(mouseX, mouseY);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      Client.uiCustomizer.mouseClick(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      Client.uiCustomizer.mouseRelease();
   }
}
