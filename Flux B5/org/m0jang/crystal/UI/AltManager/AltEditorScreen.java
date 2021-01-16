package org.m0jang.crystal.UI.AltManager;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public abstract class AltEditorScreen extends GuiScreen {
   protected GuiScreen prevMenu;
   protected GuiEmailField emailAndPasswordBox;
   protected String displayText;

   public AltEditorScreen(GuiScreen par1GuiScreen) {
      this.prevMenu = par1GuiScreen;
   }

   public void updateScreen() {
      this.emailAndPasswordBox.updateCursorCounter();
   }

   protected abstract String getDoneButtonText();

   protected abstract String getEmailAndPasswordBoxText();

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72 + 12, this.getDoneButtonText()));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 110, "Cancel"));
      this.emailAndPasswordBox = new GuiEmailField(0, this.fontRendererObj, this.width / 2 - 100, 85, 200, 20);
      this.emailAndPasswordBox.setFocused(true);
      this.emailAndPasswordBox.setMaxStringLength(Integer.MAX_VALUE);
      this.emailAndPasswordBox.setText(this.getEmailAndPasswordBoxText());
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected abstract void onDoneButtonClick(GuiButton var1);

   protected void actionPerformed(GuiButton button) {
      if (button.enabled) {
         if (button.id == 1) {
            this.mc.displayGuiScreen(this.prevMenu);
         } else if (button.id == 0) {
            this.onDoneButtonClick(button);
         }
      }

   }

   protected void keyTyped(char par1, int par2) {
      this.emailAndPasswordBox.textboxKeyTyped(par1, par2);
      if (par2 == 28 || par2 == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

   }

   protected void mouseClicked(int par1, int par2, int par3) throws IOException {
      super.mouseClicked(par1, par2, par3);
      this.emailAndPasswordBox.mouseClicked(par1, par2, par3);
   }

   protected abstract String getTitle();

   public void drawScreen(int par1, int par2, float par3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.getTitle(), this.width / 2, 20, 16777215);
      if (this.displayText != null) {
         this.drawCenteredString(this.fontRendererObj, "\247c" + this.displayText, this.width / 2, 45, 16777215);
      }

      this.drawString(this.fontRendererObj, "Name / E-Mail:Password", this.width / 2 - 100, 70, 10526880);
      this.emailAndPasswordBox.drawTextBox();
      super.drawScreen(par1, par2, par3);
   }
}
