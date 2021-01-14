package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import rip.autumn.menu.AutumnMainMenu;

public class GuiIngameMenu extends GuiScreen {
   private int field_146445_a;
   private int field_146444_f;

   public void initGui() {
      this.field_146445_a = 0;
      this.buttonList.clear();
      int i = -16;
      int j = true;
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu")));
      if (!this.mc.isIntegratedServerRunning()) {
         ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
      }

      this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame")));
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options")));
      GuiButton guibutton;
      this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan")));
      this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements")));
      this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats")));
      guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
         break;
      case 1:
         boolean flag = this.mc.isIntegratedServerRunning();
         boolean flag1 = this.mc.func_181540_al();
         button.enabled = false;
         this.mc.theWorld.sendQuittingDisconnectingPacket();
         this.mc.loadWorld((WorldClient)null);
         if (flag) {
            this.mc.displayGuiScreen(new AutumnMainMenu());
         } else if (flag1) {
            RealmsBridge realmsbridge = new RealmsBridge();
            realmsbridge.switchToRealms(new AutumnMainMenu());
         } else {
            this.mc.displayGuiScreen(new GuiMultiplayer(new AutumnMainMenu()));
         }
      case 2:
      case 3:
      default:
         break;
      case 4:
         this.mc.displayGuiScreen((GuiScreen)null);
         this.mc.setIngameFocus();
         break;
      case 5:
         this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
         break;
      case 6:
         this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
         break;
      case 7:
         this.mc.displayGuiScreen(new GuiShareToLan(this));
      }

   }

   public void updateScreen() {
      super.updateScreen();
      ++this.field_146444_f;
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game"), this.width / 2, 40, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
