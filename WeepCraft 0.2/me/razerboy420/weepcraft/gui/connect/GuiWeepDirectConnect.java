package me.razerboy420.weepcraft.gui.connect;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;

public class GuiWeepDirectConnect extends GuiScreen {

   public GuiScreen parent;
   public GuiTextField ipEdit;


   public GuiWeepDirectConnect(GuiScreen parent) {
      this.parent = parent;
   }

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
      this.ipEdit = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
      this.ipEdit.setMaxStringLength(128);
      this.ipEdit.setFocused(true);
      this.ipEdit.setText(this.mc.gameSettings.lastServer);
      super.initGui();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.gameSettings.lastServer = this.ipEdit.getText();
      this.mc.gameSettings.saveOptions();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         try {
            String e = this.ipEdit.getText().split(":")[0];
            int ip1 = Integer.parseInt(this.ipEdit.getText().split(":")[1]);
            Wrapper.getWorld().sendQuittingDisconnectingPacket();
            Wrapper.mc().loadWorld((WorldClient)null);
            Wrapper.mc().displayGuiScreen(new GuiConnecting(this.parent, Wrapper.mc(), e, ip1));
         } catch (Exception var5) {
            String ip = this.ipEdit.getText();
            short port = 25565;
            Wrapper.getWorld().sendQuittingDisconnectingPacket();
            Wrapper.mc().loadWorld((WorldClient)null);
            Wrapper.mc().displayGuiScreen(new GuiConnecting(this.parent, Wrapper.mc(), ip, port));
         }
      }

      if(button.id == 1) {
         this.mc.displayGuiScreen(this.parent);
      }

      super.actionPerformed(button);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if(this.ipEdit.textboxKeyTyped(typedChar, keyCode)) {
         ((GuiButton)this.buttonList.get(0)).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
      } else if(keyCode == 28 || keyCode == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      this.ipEdit.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ((GuiButton)this.buttonList.get(0)).enabled = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
      this.drawDefaultBackground();
      Weepcraft.drawWeepString();
      drawCenteredString(this.fontRendererObj, I18n.format("selectServer.direct", new Object[0]), (float)(this.width / 2), 20.0F, 16777215);
      drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), (float)(this.width / 2 - 100), 100.0F, 10526880);
      this.ipEdit.drawTextBox();
      this.ipEdit.updateCursorCounter();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
