package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import rip.autumn.menu.AutumnMainMenu;

public class GuiGameOver extends GuiScreen implements GuiYesNoCallback {
   private int enableButtonsTimer;
   private boolean field_146346_f = false;

   public void initGui() {
      this.buttonList.clear();
      if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
         if (this.mc.isIntegratedServerRunning()) {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld")));
         } else {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer")));
         }
      } else {
         this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
         this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));
         if (this.mc.getSession() == null) {
            ((GuiButton)this.buttonList.get(1)).enabled = false;
         }
      }

      GuiButton guibutton;
      for(Iterator var1 = this.buttonList.iterator(); var1.hasNext(); guibutton.enabled = false) {
         guibutton = (GuiButton)var1.next();
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         this.mc.thePlayer.respawnPlayer();
         this.mc.displayGuiScreen((GuiScreen)null);
         break;
      case 1:
         if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            this.mc.displayGuiScreen(new AutumnMainMenu());
         } else {
            GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
            this.mc.displayGuiScreen(guiyesno);
            guiyesno.setButtonDelay(20);
         }
      }

   }

   public void confirmClicked(boolean result, int id) {
      if (result) {
         this.mc.theWorld.sendQuittingDisconnectingPacket();
         this.mc.loadWorld((WorldClient)null);
         this.mc.displayGuiScreen(new AutumnMainMenu());
      } else {
         this.mc.thePlayer.respawnPlayer();
         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
      GlStateManager.pushMatrix();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      boolean flag = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
      String s = flag ? I18n.format("deathScreen.title.hardcore") : I18n.format("deathScreen.title");
      this.drawCenteredString(this.fontRendererObj, s, this.width / 2 / 2, 30, 16777215);
      GlStateManager.popMatrix();
      if (flag) {
         this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
      }

      this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void updateScreen() {
      super.updateScreen();
      ++this.enableButtonsTimer;
      GuiButton guibutton;
      if (this.enableButtonsTimer == 20) {
         for(Iterator var1 = this.buttonList.iterator(); var1.hasNext(); guibutton.enabled = true) {
            guibutton = (GuiButton)var1.next();
         }
      }

   }
}
