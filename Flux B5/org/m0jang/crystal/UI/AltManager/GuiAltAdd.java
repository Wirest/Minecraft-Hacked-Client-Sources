package org.m0jang.crystal.UI.AltManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Utils.LoginUtils;

public class GuiAltAdd extends AltEditorScreen {
   public GuiAltAdd(GuiScreen par1GuiScreen) {
      super(par1GuiScreen);
   }

   protected String getDoneButtonText() {
      return "Add";
   }

   protected String getEmailAndPasswordBoxText() {
      return Minecraft.getMinecraft().session.getUsername();
   }

   protected void onDoneButtonClick(GuiButton button) {
      if (this.emailAndPasswordBox.getText().split(":").length <= 1) {
         GuiAltList.alts.add(new Alt(this.emailAndPasswordBox.getText(), (String)null, (String)null));
         this.displayText = null;
      } else {
         String email = this.emailAndPasswordBox.getText().split(":")[0];
         String password = this.emailAndPasswordBox.getText().split(":")[1];
         this.displayText = LoginUtils.login(email, password);
         if (this.displayText == null) {
            GuiAltList.alts.add(new Alt(email, password, this.mc.session.getUsername()));
         }
      }

      if (this.displayText == null) {
         GuiAltList.sortAlts();
         Crystal.INSTANCE.getConfig().saveAlts();
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }

   protected String getTitle() {
      return "Add an Alt";
   }
}
