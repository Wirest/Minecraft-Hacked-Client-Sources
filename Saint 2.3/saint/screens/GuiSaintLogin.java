package saint.screens;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import saint.protection.Account;
import saint.threads.SaintLogin;
import saint.utilities.GuiPasswordField;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class GuiSaintLogin extends GuiScreen {
   private GuiPasswordField password;
   private SaintLogin thread;
   private GuiTextField username;

   public GuiSaintLogin(Account account) {
      if (account != null) {
         this.thread = new SaintLogin(account);
         this.thread.start();
      }

   }

   protected void actionPerformed(GuiButton button) {
      if (button.id == 0) {
         this.thread = new SaintLogin(this.username.getText(), this.password.getText());
         this.thread.start();
      }

      if (button.id == 1) {
         this.mc.shutdown();
      }

   }

   public void drawScreen(int x, int y, float z) {
      if (this.thread != null && this.thread.getStatee().equals("§aLogged in.")) {
         this.mc.displayGuiScreen(new GuiMainMenu());
      }

      this.drawDefaultBackground();
      this.username.drawTextBox();
      this.password.drawTextBox();
      RenderHelper.getNahrFont().drawString("Saint Login", (float)(this.width / 2 - 25), 20.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      RenderHelper.getNahrFont().drawString(this.thread == null ? "§cYou need to login in order to use Saint." : this.thread.getStatee(), (float)(this.thread == null ? this.width / 2 - 85 : this.width / 2 - 27), 35.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      if (this.username.getText().isEmpty()) {
         RenderHelper.getNahrFont().drawString("Username", (float)(this.width / 2 - 96), 62.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      }

      if (this.password.getText().isEmpty()) {
         RenderHelper.getNahrFont().drawString("Password", (float)(this.width / 2 - 96), 102.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      }

      super.drawScreen(x, y, z);
   }

   public void initGui() {
      int var3 = this.height / 4 + 24;
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 36, "Quit"));
      this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
      this.password = new GuiPasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
      this.username.setFocused(true);
      Keyboard.enableRepeatEvents(true);
   }

   protected void keyTyped(char character, int key) {
      if (character == '\t') {
         if (!this.username.isFocused() && !this.password.isFocused()) {
            this.username.setFocused(true);
         } else {
            this.username.setFocused(this.password.isFocused());
            this.password.setFocused(!this.username.isFocused());
         }
      }

      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.username.textboxKeyTyped(character, key);
      this.password.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x, int y, int button) {
      try {
         super.mouseClicked(x, y, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.username.mouseClicked(x, y, button);
      this.password.mouseClicked(x, y, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.username.updateCursorCounter();
      this.password.updateCursorCounter();
   }
}
