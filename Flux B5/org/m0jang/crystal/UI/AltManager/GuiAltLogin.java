package org.m0jang.crystal.UI.AltManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.m0jang.crystal.Utils.LoginUtils;

public class GuiAltLogin extends AltEditorScreen {
   private GuiScreen prevMenu;

   public GuiAltLogin(GuiScreen par1GuiScreen) {
      super(par1GuiScreen);
      this.prevMenu = par1GuiScreen;
   }

   protected String getTitle() {
      return "Direct Login";
   }

   protected String getDoneButtonText() {
      return "Login";
   }

   protected String getEmailAndPasswordBoxText() {
      return Minecraft.getMinecraft().session.getUsername();
   }

   protected void onDoneButtonClick(GuiButton button) {
      if (this.emailAndPasswordBox.getText().split(":").length <= 1) {
         LoginUtils.changeCrackedName(this.emailAndPasswordBox.getText());
         this.displayText = null;
      } else {
         String email = this.emailAndPasswordBox.getText().split(":")[0];
         String password = this.emailAndPasswordBox.getText().split(":")[1];
         this.displayText = LoginUtils.login(email, password);
      }

      if (this.displayText == null) {
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }
}
