package me.razerboy420.weepcraft.gui.login;

import java.io.IOException;
import java.sql.SQLException;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.login.Login;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiLogin extends GuiScreen {

   public GuiTextField user;
   public GuiTextField pass;
   public GuiButton login;
   public GuiButton register;
   String text = "";


   public void initGui() {
      this.user = new GuiTextField(0, Wrapper.fr(), this.width / 2 - 70, this.height / 2 - 61, 140, 20);
      this.pass = new GuiTextField(1, Wrapper.fr(), this.width / 2 - 70, this.height / 2 - 25, 140, 20);
      this.login = new GuiButton(0, this.width / 2 - 30, this.height / 2 + 15, 60, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Login");
      this.register = new GuiButton(1, this.width / 2 - 30, this.height / 2 + 40, 60, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Register");
      this.buttonList.add(this.login);
      this.buttonList.add(this.register);
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.user.drawTextBox();
      this.pass.drawTextBox();
      this.user.updateCursorCounter();
      this.pass.updateCursorCounter();
      this.login.enabled = !this.user.getText().isEmpty() && !this.pass.getText().isEmpty() && this.user.getText().contains("@") && this.pass.getText().length() > 8 && this.user.getText().contains(".");
      this.register.enabled = !this.user.getText().isEmpty() && !this.pass.getText().isEmpty() && this.user.getText().contains("@") && this.pass.getText().length() > 8 && this.user.getText().contains(".");
      Weepcraft.drawWeepString();
      Gui.drawCenteredString(Wrapper.fr(), this.text, (float)(this.width / 2), (float)(this.height / 2 + 65), -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Password (8 characters or more)", (float)this.pass.xPosition, (float)(this.height / 2 - 35), -1);
      Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Email", (float)this.pass.xPosition, (float)(this.height / 2 - 71), -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      boolean b;
      if(button.id == 0) {
         this.text = "";
         b = false;

         try {
            b = Login.login(this.user.getText(), this.pass.getText());
         } catch (ClassNotFoundException var4) {
            var4.printStackTrace();
         } catch (SQLException var5) {
            var5.printStackTrace();
         }

         b = true;
         if(b) {
            Wrapper.mc().displayGuiScreen(new GuiMainMenu());
         } else {
            this.text = "Invalid credentials.";
         }
      }

      if(button.id == 1) {
         b = false;

         try {
            b = Login.create(this.user.getText(), this.pass.getText());
         } catch (Exception var6) {
            if(b) {
               this.text = "User created!";
            } else {
               this.text = "User already registered.";
            }
         }
      }

      super.actionPerformed(button);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if(this.user.isFocused()) {
         this.user.textboxKeyTyped(typedChar, keyCode);
      }

      if(this.pass.isFocused()) {
         this.pass.textboxKeyTyped(typedChar, keyCode);
      }

      super.keyTyped(typedChar, keyCode);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.user.mouseClicked(mouseX, mouseY, mouseButton);
      this.pass.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }
}
