package rip.autumn.menu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;
import rip.autumn.alt.gui.GuiAltManager;
import rip.autumn.menu.buttons.SimpleButton;
import rip.autumn.utils.render.RenderUtils;

public final class AutumnMainMenu extends GuiScreen {
   private ResourceLocation background = new ResourceLocation("autumn/menu/background.png");

   public void initGui() {
      super.initGui();
      this.buttonList.add(new SimpleButton(0, this.width / 2, this.height / 2 - 40, "Singleplayer"));
      this.buttonList.add(new SimpleButton(1, this.width / 2, this.height / 2 - 20, "Multiplayer"));
      this.buttonList.add(new SimpleButton(2, this.width / 2, this.height / 2, "Alt Manager"));
      this.buttonList.add(new SimpleButton(3, this.width / 2, this.height / 2 + 20, "Settings"));
      this.buttonList.add(new SimpleButton(4, this.width / 2, this.height / 2 + 40, "Exit"));
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
         break;
      case 1:
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
         break;
      case 2:
         this.mc.displayGuiScreen(new GuiAltManager(this));
         break;
      case 3:
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
         break;
      case 4:
         System.exit(0);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      RenderUtils.drawImg(this.background, 0.0D, 0.0D, (double)this.width, (double)this.height);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
