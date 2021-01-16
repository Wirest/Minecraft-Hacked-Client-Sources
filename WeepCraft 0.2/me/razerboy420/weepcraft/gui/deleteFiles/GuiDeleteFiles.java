package me.razerboy420.weepcraft.gui.deleteFiles;

import java.io.IOException;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.FileUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class GuiDeleteFiles extends GuiScreen {

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 - 60, this.height / 2 - 10, 55, 20, "Yes"));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height / 2 - 10, 55, 20, "No"));
      super.initGui();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0 && FileUtils.deleteDir(Weepcraft.weepcraftDir)) {
         Wrapper.mc().shutdown();
      }

      if(button.id == 1) {
         Wrapper.mc().displayGuiScreen(new GuiMainMenu());
      }

      super.actionPerformed(button);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      Weepcraft.drawWeepString();
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Warning: This deletes all WeepCraft files and closes the game. Would you like to do this?", (float)(this.width / 2), (float)(this.height / 2 - 20), -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
