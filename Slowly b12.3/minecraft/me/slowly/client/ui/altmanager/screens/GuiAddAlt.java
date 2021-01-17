package me.slowly.client.ui.altmanager.screens;

import com.google.common.base.Predicate;
import java.io.IOException;
import java.net.IDN;
import me.slowly.client.Client;
import me.slowly.client.ui.altmanager.AltManager;
import me.slowly.client.ui.altmanager.GuiAltSlot;
import me.slowly.client.ui.buttons.UIFlatButton;
import me.slowly.client.ui.textfield.UITextField;
import me.slowly.client.util.Colors;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiAddAlt extends GuiScreen {
   private final GuiScreen parentScreen;
   private UITextField usernameField;
   private UITextField passwordField;
   private UITextField usernamePasswordField;
   private Predicate<String> field_181032_r = new Predicate<String>(){
      public boolean apply(String p_apply_1_) {
         if (p_apply_1_.length() == 0) {
            return true;
         } else {
            String[] astring = p_apply_1_.split(":");
            if (astring.length == 0) {
               return true;
            } else {
               try {
                  String s = IDN.toASCII(astring[0]);
                  return true;
               } catch (IllegalArgumentException var4) {
                  return false;
               }
            }
         }
      }
   };

   public GuiAddAlt(GuiScreen p_i1033_1_) {
      this.parentScreen = p_i1033_1_;
   }

   public void updateScreen() {
      this.usernameField.updateCursorCounter();
      this.passwordField.updateCursorCounter();
      this.usernamePasswordField.updateCursorCounter();
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new UIFlatButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 15, "Add Alt", Colors.DARKBLUE.c));
      this.buttonList.add(new UIFlatButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 15, I18n.format("gui.cancel"), Colors.RED.c));
      this.usernameField = new UITextField(0, this.fontRendererObj, this.width, 66, 200, 20);
      this.usernameField.setFocused(true);
      this.passwordField = new UITextField(1, this.fontRendererObj, 10, 106, 200, 20);
      this.passwordField.setMaxStringLength(128);
      this.passwordField.func_175205_a(this.field_181032_r);
      this.usernamePasswordField = new UITextField(2, this.fontRendererObj, 100, 146, 200, 20);
      this.usernamePasswordField.setMaxStringLength(128);
      this.usernamePasswordField.func_175205_a(this.field_181032_r);
      ((GuiButton)this.buttonList.get(0)).enabled = this.usernameField.getText().length() > 0 && this.passwordField.getText().length() > 0 || this.usernamePasswordField.getText().split(":").length >= 2;
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if (button.enabled && button.id != 2) {
         if (button.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (button.id == 0) {
            if (!this.usernameField.getText().isEmpty() && !this.passwordField.getText().isEmpty()) {
               AltManager.guiSlotList.add(new GuiAltSlot(this.usernameField.getText(), this.passwordField.getText()));
            } else if (!this.usernamePasswordField.getText().isEmpty()) {
               String[] account = this.usernamePasswordField.getText().split(":");
               AltManager.guiSlotList.add(new GuiAltSlot(account[0], account[1]));
            }

            AltManager.saveAltsToFile();
            this.mc.displayGuiScreen(this.parentScreen);
         }
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.usernameField.textboxKeyTyped(typedChar, keyCode);
      this.passwordField.textboxKeyTyped(typedChar, keyCode);
      this.usernamePasswordField.textboxKeyTyped(typedChar, keyCode);
      if (keyCode == 15) {
         this.usernameField.setFocused(!this.usernameField.isFocused());
         this.passwordField.setFocused(!this.passwordField.isFocused());
         this.usernamePasswordField.setFocused(!this.passwordField.isFocused());
      }

      if (keyCode == 28 || keyCode == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      ((GuiButton)this.buttonList.get(0)).enabled = this.usernameField.getText().length() > 0 && this.passwordField.getText().length() > 0 || this.usernamePasswordField.getText().split(":").length >= 2;
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
      this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
      this.usernamePasswordField.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      new ScaledResolution(this.mc);
      int x = this.width / 2 - 150;
      int darkGray = -15658735;
      int lightGray = -15066598;
      Gui.drawBorderedRect(x, 23, x + 300, this.height / 4 + 170, 1, darkGray, lightGray);
      float var10002 = (float)(this.width / 2);
      Client.getInstance().getFontManager().robotobold25.drawCenteredString("Add Alt", var10002, 26.0F, Colors.GREY.c);
      Client.getInstance().getFontManager().VERDANA10.drawString("Email", (float)(this.width / 2 - 100), 53.0F, -1);
      Client.getInstance().getFontManager().VERDANA10.drawString("Password", (float)(this.width / 2 - 100), 94.0F, -1);
      Client.getInstance().getFontManager().VERDANA10.drawString("Email:Password", (float)(this.width / 2 - 100), 135.0F, -1);
      this.usernameField.drawTextBox();
      this.passwordField.drawTextBox();
      this.usernamePasswordField.drawTextBox();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
