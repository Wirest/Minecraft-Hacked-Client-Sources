package saint.screens;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import saint.Saint;

public class GuiRenameAlt extends GuiScreen {
   private final GuiAltManager manager;
   private GuiTextField nameField;
   private String status = "Waiting...";

   public GuiRenameAlt(GuiAltManager manager) {
      this.manager = manager;
   }

   public void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         this.manager.selectedAlt.setMask(this.nameField.getText());
         this.status = "§aRenamed!";
         Saint.getFileManager().getFileUsingName("alts").saveFile();
         break;
      case 1:
         this.mc.displayGuiScreen(this.manager);
      }

   }

   public void drawScreen(int par1, int par2, float par3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, "Rename Alt", this.width / 2, 10, -1);
      this.drawCenteredString(this.fontRendererObj, this.status, this.width / 2, 20, -1);
      this.nameField.drawTextBox();
      super.drawScreen(par1, par2, par3);
   }

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Rename"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Cancel"));
      this.nameField = new GuiTextField(this.eventButton, this.fontRendererObj, this.width / 2 - 100, 76, 200, 20);
   }

   protected void keyTyped(char par1, int par2) {
      this.nameField.textboxKeyTyped(par1, par2);
      if (par1 == '\t' && this.nameField.isFocused()) {
         this.nameField.setFocused(!this.nameField.isFocused());
      }

      if (par1 == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

   }

   protected void mouseClicked(int par1, int par2, int par3) {
      try {
         super.mouseClicked(par1, par2, par3);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.nameField.mouseClicked(par1, par2, par3);
   }
}
