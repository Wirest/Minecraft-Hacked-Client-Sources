package org.m0jang.crystal.UI.AltGenerator;

import java.io.IOException;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;
import org.m0jang.crystal.Crystal;

public class AltGeneratorLogin extends GuiScreen {
   public static HashMap Idents = new HashMap();
   private GuiScreen prevMenu;
   private GuiTextField fieldUsername_FA;
   private GuiTextField fieldPassword_FA;
   private int altIndex;
   private String errorMessage;

   public AltGeneratorLogin(GuiScreen parent) {
      this.prevMenu = parent;
   }

   public void initGui() {
      this.buttonList.clear();
      int var10005 = this.width / 2 - 100;
      this.fieldUsername_FA = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, var10005, this.height / 4 + 20, 200, 20);
      this.fieldUsername_FA.setMaxStringLength(250);
      var10005 = this.width / 2 - 100;
      this.fieldPassword_FA = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, var10005, this.height / 4 + 40, 200, 20);
      this.fieldPassword_FA.setMaxStringLength(250);
      if (Idents.containsKey(GeneratorType.FASTALTS)) {
         this.fieldUsername_FA.setText(((String[])Idents.get(GeneratorType.FASTALTS))[0]);
         this.fieldPassword_FA.setText(((String[])Idents.get(GeneratorType.FASTALTS))[1]);
      }

      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 115, "Done"));
      Keyboard.enableRepeatEvents(true);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         if (!this.fieldUsername_FA.getText().isEmpty() && !this.fieldPassword_FA.getText().isEmpty()) {
            if (Idents.containsKey(GeneratorType.FASTALTS)) {
               Idents.remove(GeneratorType.FASTALTS);
            }

            Idents.put(GeneratorType.FASTALTS, new String[]{this.fieldUsername_FA.getText(), this.fieldPassword_FA.getText()});
         }

         this.mc.displayGuiScreen(this.prevMenu);
         Crystal.INSTANCE.getConfig().saveIdentities();
      default:
      }
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      this.fieldUsername_FA.textboxKeyTyped(typedChar, keyCode);
      this.fieldPassword_FA.textboxKeyTyped(typedChar, keyCode);
      if (keyCode == 1) {
         this.mc.displayGuiScreen(this.prevMenu);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.fieldUsername_FA.mouseClicked(mouseX, mouseY, mouseButton);
      this.fieldPassword_FA.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void updateScreen() {
      this.fieldUsername_FA.updateCursorCounter();
      this.fieldPassword_FA.updateCursorCounter();
      super.updateScreen();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      int var10003 = this.width / 2;
      this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Set account information of alt generators.", var10003, 48, -1);
      this.fieldUsername_FA.drawTextBox();
      this.fieldPassword_FA.drawTextBox();
      var10003 = this.width / 2 - 100;
      this.drawString(Minecraft.getMinecraft().fontRendererObj, "FastAlts (Username, Password)", var10003, this.height / 4 + 8, -1);
      if (this.errorMessage != null) {
         var10003 = this.width / 2;
         this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.errorMessage, var10003, 24, -1);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
