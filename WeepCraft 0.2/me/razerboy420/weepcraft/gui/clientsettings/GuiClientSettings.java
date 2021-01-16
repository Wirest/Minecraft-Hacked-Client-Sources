package me.razerboy420.weepcraft.gui.clientsettings;

import java.io.IOException;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.files.ColorFile;
import me.razerboy420.weepcraft.gui.clientsettings.crosshair.GuiCrosshair;
import me.razerboy420.weepcraft.settings.EnumColor;
import me.razerboy420.weepcraft.spammer.Spammer;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiClientSettings extends GuiScreen {

   public GuiScreen parent;


   public GuiClientSettings(GuiScreen parent) {
      this.parent = parent;
   }

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 - 62, 90, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Primary: " + ColorUtil.getColor(Weepcraft.primaryColor) + Weepcraft.primaryColor.name()));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 62, 112, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Secondary: " + ColorUtil.getColor(Weepcraft.secondaryColor) + Weepcraft.secondaryColor.name()));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 62, 134, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Normal: " + ColorUtil.getColor(Weepcraft.normalColor) + Weepcraft.normalColor.name()));
      this.buttonList.add(new GuiButton(3, this.width / 2 - 62, 156, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Enabled: " + ColorUtil.getColor(Weepcraft.enabledColor) + Weepcraft.enabledColor.name()));
      this.buttonList.add(new GuiButton(4, this.width / 2 - 62, 178, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Disabled: " + ColorUtil.getColor(Weepcraft.disabledColor) + Weepcraft.disabledColor.name()));
      this.buttonList.add(new GuiButton(5, this.width / 2 - 62, 200, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Border: " + ColorUtil.getColor(Weepcraft.borderColor) + Weepcraft.borderColor.name()));
      this.buttonList.add(new GuiButton(6, this.width / 2 - 62, 222, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Panel: " + ColorUtil.getColor(Weepcraft.panlColor) + Weepcraft.panlColor.name()));
      this.buttonList.add(new GuiButton(7, this.width / 2 - 62, 258, 125, 20, "Back"));
      this.buttonList.add(new GuiButton(11, this.width / 2 - 62 - 145, 90, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Modules"));
      this.buttonList.add(new GuiButton(9, this.width / 2 - 62 - 145, 112, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Keybinds"));
      this.buttonList.add(new GuiButton(10, this.width / 2 - 62 - 145, 134, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Gui"));
      this.buttonList.add(new GuiButton(12, this.width / 2 - 62 - 145, 156, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Crosshair"));
      this.buttonList.add(new GuiButton(13, this.width / 2 - 62 + 145, 90, 125, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Spammer"));
      super.initGui();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         Weepcraft.primaryColor = EnumColor.nextColor(Weepcraft.primaryColor);
         this.buttonList.clear();
         this.initGui();
         ColorFile.save();
      }

      if(button.id == 1) {
         Weepcraft.secondaryColor = EnumColor.nextColor(Weepcraft.secondaryColor);
         this.buttonList.clear();
         this.initGui();
         ColorFile.save();
      }

      if(button.id == 2) {
         Weepcraft.normalColor = EnumColor.nextColor(Weepcraft.normalColor);
         this.buttonList.clear();
         this.initGui();
         ColorFile.save();
      }

      if(button.id == 3) {
         Weepcraft.enabledColor = EnumColor.nextColor(Weepcraft.enabledColor);
         this.buttonList.clear();
         this.initGui();
         ColorFile.save();
      }

      if(button.id == 4) {
         Weepcraft.disabledColor = EnumColor.nextColor(Weepcraft.disabledColor);
         this.buttonList.clear();
         this.initGui();
         ColorFile.save();
      }

      if(button.id == 5) {
         Weepcraft.borderColor = EnumColor.nextColor(Weepcraft.borderColor);
         this.buttonList.clear();
         this.initGui();
         ColorFile.save();
      }

      if(button.id == 6) {
         Weepcraft.panlColor = EnumColor.nextColor(Weepcraft.panlColor);
         this.buttonList.clear();
         this.initGui();
         ColorFile.save();
      }

      if(button.id == 7) {
         Wrapper.mc().displayGuiScreen(this.parent);
      }

      int var10000 = button.id;
      if(button.id == 9) {
         Wrapper.mc().displayGuiScreen(new GuiKeybinds(this));
      }

      if(button.id == 10) {
         Wrapper.mc().displayGuiScreen(Weepcraft.getClick());
      }

      if(button.id == 11) {
         Wrapper.mc().displayGuiScreen(new GuiModules(this));
      }

      if(button.id == 12) {
         Wrapper.mc().displayGuiScreen(new GuiCrosshair());
      }

      if(button.id == 13) {
         Spammer.main();
      }

      super.actionPerformed(button);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      String weepcraftString = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
      drawCenteredString(Wrapper.fr(), weepcraftString + " " + ColorUtil.getColor(Weepcraft.normalColor) + Weepcraft.version + "v", (float)(this.width / 2), 2.0F, -1);
      drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Colors", (float)(this.width / 2), 80.0F, -1);
      drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Managers", (float)(this.width / 2 - 145), 80.0F, -1);
      drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Frames", (float)(this.width / 2 + 145), 80.0F, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
