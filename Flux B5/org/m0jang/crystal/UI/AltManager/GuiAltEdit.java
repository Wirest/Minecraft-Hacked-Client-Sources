package org.m0jang.crystal.UI.AltManager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Utils.LoginUtils;

public class GuiAltEdit extends AltEditorScreen {
   private Alt editedAlt;

   public GuiAltEdit(GuiScreen par1GuiScreen, Alt editedAlt) {
      super(par1GuiScreen);
      this.editedAlt = editedAlt;
   }

   protected String getDoneButtonText() {
      return "Save";
   }

   protected String getEmailAndPasswordBoxText() {
      return this.editedAlt.getEmail() + ":" + this.editedAlt.getPassword();
   }

   protected void onDoneButtonClick(GuiButton button) {
      if (this.emailAndPasswordBox.getText().split(":").length <= 1) {
         GuiAltList.alts.set(GuiAltList.alts.indexOf(this.editedAlt), new Alt(this.emailAndPasswordBox.getText(), (String)null, (String)null, this.editedAlt.isStarred()));
         this.displayText = null;
      } else {
         String email = this.emailAndPasswordBox.getText().split(":")[0];
         String password = this.emailAndPasswordBox.getText().split(":")[1];
         this.displayText = LoginUtils.login(email, password);
         if (this.displayText == null) {
            GuiAltList.alts.set(GuiAltList.alts.indexOf(this.editedAlt), new Alt(email, password, this.mc.session.getUsername(), this.editedAlt.isStarred()));
         }
      }

      if (this.displayText == null) {
         GuiAltList.sortAlts();
         Crystal.INSTANCE.getConfig().saveAlts();
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }

   protected String getTitle() {
      return "Edit this Alt";
   }
}
