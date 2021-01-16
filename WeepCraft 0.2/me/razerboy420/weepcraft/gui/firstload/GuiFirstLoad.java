package me.razerboy420.weepcraft.gui.firstload;

import java.io.IOException;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.files.Keybinds;
import me.razerboy420.weepcraft.keybinds.Keybind;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class GuiFirstLoad extends GuiScreen {

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 - 60, this.height / 2 + 30, 55, 20, "Yes"));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height / 2 + 30, 55, 20, "No"));
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      Weepcraft.drawWeepString();
      String weepcraftString = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Welcome to " + weepcraftString, (float)(this.width / 2), (float)(this.height / 2 - 30), -1);
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "This is a beta version, not all mods are in yet, and it might have some bugs", (float)(this.width / 2), (float)(this.height / 2 - 20), -1);
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "This screen will only show up once, since it\'s your first time loading", (float)(this.width / 2), (float)(this.height / 2 - 10), -1);
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Since you have no settings yet,", (float)(this.width / 2), (float)(this.height / 2 + 10), -1);
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Would you like WeepCraft to set up some keybinds for you?", (float)(this.width / 2), (float)(this.height / 2 + 20), -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         Weepcraft.keybinds.add(new Keybind(ModuleManager.gui, 54));
         Weepcraft.keybinds.add(new Keybind(ModuleManager.aura, 19));
         Keybinds.save();
         Wrapper.mc().displayGuiScreen(new GuiMainMenu());
      }

      if(button.id == 1) {
         Wrapper.mc().displayGuiScreen(new GuiMainMenu());
      }

      super.actionPerformed(button);
   }
}
