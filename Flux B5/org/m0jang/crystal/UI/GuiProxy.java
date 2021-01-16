package org.m0jang.crystal.UI;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

public class GuiProxy extends GuiScreen {
   private GuiScreen prevMenu;
   private GuiTextField textbox1;
   private String errorMsg;

   public GuiProxy(GuiScreen parent) {
      this.prevMenu = parent;
   }

   public void initGui() {
      this.buttonList.clear();
      int var10005 = this.width / 2 - 100;
      (this.textbox1 = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, var10005, this.height / 4 + 20, 200, 20)).setMaxStringLength(250);
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 70, "Set"));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 95, I18n.format("menu.returnToMenu")));
      Keyboard.enableRepeatEvents(true);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         if (!StringUtils.isNullOrEmpty(this.textbox1.getText())) {
            try {
               String ip = this.textbox1.getText().split(":")[0];
               String portSring = this.textbox1.getText().split(":")[1];
               System.setProperty("socksProxyHost", ip);
               System.setProperty("socksProxyPort", portSring);
               this.errorMsg = null;
            } catch (Exception var4) {
               this.errorMsg = var4.getMessage();
            }
         } else {
            System.clearProperty("socksProxyHost");
            System.clearProperty("socksProxyPort");
         }
      case 1:
      default:
         break;
      case 2:
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.textbox1.textboxKeyTyped(typedChar, keyCode);
      if (keyCode == 1) {
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.textbox1.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void updateScreen() {
      this.textbox1.updateCursorCounter();
      super.updateScreen();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      int var10003 = this.width / 2;
      this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Setting SOCKS proxy server", var10003, 48, -1);
      this.textbox1.drawTextBox();
      var10003 = this.width / 2 - 100;
      this.drawString(Minecraft.getMinecraft().fontRendererObj, "Address:Port", var10003, this.height / 4 + 8, -1);
      String currentProxy = System.getProperty("socksProxyHost") == null ? "none" : System.getProperty("socksProxyHost") + ":" + System.getProperty("socksProxyPort");
      this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Current proxy: Â\247a" + currentProxy, this.width / 2, 12, -1);
      if (!StringUtils.isNullOrEmpty(this.errorMsg)) {
         var10003 = this.width / 2;
         this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.errorMsg, var10003, 24, 16711680);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
