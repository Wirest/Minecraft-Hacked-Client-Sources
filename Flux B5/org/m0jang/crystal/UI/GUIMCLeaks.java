package org.m0jang.crystal.UI;

import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

public class GUIMCLeaks extends GuiScreen {
   private GuiScreen prevMenu;
   private GuiTextField theName;
   private int altIndex;
   private String errorMessage;

   public GUIMCLeaks(GuiScreen parent) {
      this.prevMenu = parent;
   }

   public void initGui() {
      this.buttonList.clear();
      int var10005 = this.width / 2 - 100;
      this.theName = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, var10005, this.height / 4 + 20, 200, 20);
      this.theName.setMaxStringLength(250);
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 45, "Redeem Token"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 90, "Get Token"));
      this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 115, "Done"));
      Keyboard.enableRepeatEvents(true);
   }

   protected void actionPerformed(final GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         if (!StringUtils.isNullOrEmpty(this.theName.getText())) {
            if (this.theName.getText().length() != 16) {
               this.errorMessage = "Error! The token has to be 16 characters long!";
               return;
            }

            button.enabled = false;
            button.displayString = "Loading...";

            try {
               ModApi.redeem(this.theName.getText(), new Callback() {
                  public void done(Object o) {
                     if (o instanceof String) {
                        GUIMCLeaks.this.errorMessage = (String)o;
                     } else {
                        if (MCLeaks.savedSession == null) {
                           MCLeaks.savedSession = Minecraft.getMinecraft().getSession();
                        }

                        RedeemResponse response = (RedeemResponse)o;
                        MCLeaks.refresh(response.getSession(), response.getMcName());
                        GUIMCLeaks.this.errorMessage = "Your token was redeemed successfully!";
                        button.enabled = true;
                        button.displayString = "Redeem Token";
                     }

                  }
               });
               ModApi.loginMCLeaks(MCLeaks.getMCName());
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }
         break;
      case 1:
         try {
            Class throwable = Class.forName("java.awt.Desktop");
            Object object = throwable.getMethod("getDesktop").invoke((Object)null);
            throwable.getMethod("browse", URI.class).invoke(object, new URI("https://mcleaks.net/"));
         } catch (Throwable var4) {
            var4.printStackTrace();
         }
      case 2:
      default:
         break;
      case 3:
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.theName.textboxKeyTyped(typedChar, keyCode);
      if (keyCode == 1) {
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.theName.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void updateScreen() {
      this.theName.updateCursorCounter();
      super.updateScreen();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      int var10003 = this.width / 2;
      this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Login with MCLeaks Token.", var10003, 48, -1);
      this.theName.drawTextBox();
      var10003 = this.width / 2 - 100;
      this.drawString(Minecraft.getMinecraft().fontRendererObj, "Token", var10003, this.height / 4 + 8, -1);
      this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Current name: \247a" + Minecraft.getMinecraft().getSession().getUsername(), this.width / 2, 12, -1);
      if (this.errorMessage != null) {
         var10003 = this.width / 2;
         this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.errorMessage, var10003, 24, -1);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
