package me.existdev.exist.gui.account;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;
import me.existdev.exist.Exist;
import me.existdev.exist.file.files.AltFile;
import me.existdev.exist.gui.account.Alt;
import me.existdev.exist.gui.account.GuiAltManager;
import me.existdev.exist.gui.account.GuiPasswordField;
import me.existdev.exist.ttf.CustomFontManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class GuiAddAlt extends GuiScreen {
   // $FF: synthetic field
   private final GuiAltManager manager;
   // $FF: synthetic field
   private GuiPasswordField password;
   // $FF: synthetic field
   private String status = "§eWaiting...";
   // $FF: synthetic field
   private GuiTextField username;

   // $FF: synthetic method
   public GuiAddAlt(GuiAltManager manager) {
      this.manager = manager;
   }

   // $FF: synthetic method
   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         GuiAddAlt.AddAltThread login = new GuiAddAlt.AddAltThread(this.username.getText(), this.password.getText());
         login.start();
         break;
      case 1:
         this.mc.displayGuiScreen(this.manager);
         break;
      case 2:
         String data = null;

         try {
            data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
         } catch (Exception var5) {
            return;
         }

         if(data.contains(":")) {
            String[] credentials = data.split(":");
            this.username.setText(credentials[0]);
            this.password.setText(credentials[1]);
         }
      }

   }

   // $FF: synthetic method
   public void drawScreen(int i, int j, float f) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, "Add Alt", this.width / 2, 20, -1);
      this.username.drawTextBox();
      this.password.drawTextBox();
      if(this.username.getText().isEmpty()) {
         this.drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
      }

      if(this.password.getText().isEmpty()) {
         this.drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
      }

      this.drawCenteredString(this.fontRendererObj, this.status, this.width / 2, 30, -1);
      super.drawScreen(i, j, f);
   }

   // $FF: synthetic method
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 116 + 36, "Import user:pass"));
      this.username = new GuiTextField(1, CustomFontManager.fontTextField, this.width / 2 - 100, 60, 200, 20);
      this.password = new GuiPasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
   }

   // $FF: synthetic method
   protected void keyTyped(char par1, int par2) {
      this.username.textboxKeyTyped(par1, par2);
      this.password.textboxKeyTyped(par1, par2);
      if(par1 == 9 && (this.username.isFocused() || this.password.isFocused())) {
         this.username.setFocused(!this.username.isFocused());
         this.password.setFocused(!this.password.isFocused());
      }

      if(par1 == 13) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

   }

   // $FF: synthetic method
   protected void mouseClicked(int par1, int par2, int par3) {
      try {
         super.mouseClicked(par1, par2, par3);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(par1, par2, par3);
      this.password.mouseClicked(par1, par2, par3);
   }

   private class AddAltThread extends Thread {
      // $FF: synthetic field
      private final String password;
      // $FF: synthetic field
      private final String username;

      // $FF: synthetic method
      public AddAltThread(String username, String password) {
         this.username = username;
         this.password = password;
         GuiAddAlt.this.status = "§7Waiting...";
      }

      // $FF: synthetic method
      private final void checkAndAddAlt(String username, String password) {
         YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
         YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
         auth.setUsername(username);
         auth.setPassword(password);

         try {
            auth.logIn();
            Exist.altManager.getAlts().add(new Alt(username, password));
            AltFile var10000 = Exist.altFile;
            AltFile.saveAlts();
            GuiAddAlt.this.status = "§aAlt added. (" + username + ")";
         } catch (AuthenticationException var6) {
            GuiAddAlt.this.status = "§4Alt failed!";
            var6.printStackTrace();
         }

      }

      // $FF: synthetic method
      public void run() {
         if(this.password.equals("")) {
            Exist.altManager.getAlts().add(new Alt(this.username, ""));
            AltFile var10000 = Exist.altFile;
            AltFile.saveAlts();
            GuiAddAlt.this.status = "§aAlt added. (" + this.username + " - offline name)";
         } else {
            GuiAddAlt.this.status = "§1Trying alt...";
            this.checkAndAddAlt(this.username, this.password);
         }
      }
   }
}
