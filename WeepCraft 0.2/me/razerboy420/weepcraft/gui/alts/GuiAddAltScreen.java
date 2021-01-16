package me.razerboy420.weepcraft.gui.alts;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.alts.Alt;
import me.razerboy420.weepcraft.files.AltsFile;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiAddAltScreen extends GuiScreen {

   GuiScreen parent;
   GuiTextField username = new GuiTextField(0, Wrapper.fr(), RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100, RenderUtils2D.newScaledResolution().getScaledHeight() / 2 - 140, 200, 20);
   GuiTextField email = new GuiTextField(0, Wrapper.fr(), RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100, RenderUtils2D.newScaledResolution().getScaledHeight() / 2 - 119 + 20, 200, 20);
   GuiPasswordField password = new GuiPasswordField(Wrapper.fr(), RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100, RenderUtils2D.newScaledResolution().getScaledHeight() / 2 - 98 + 40, 200, 20);


   public GuiAddAltScreen(GuiScreen parent) {
      this.parent = parent;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.username.setFocused(true);
      this.username.setMaxStringLength(16);
      this.buttonList.clear();
      this.username.setCanLoseFocus(true);
      this.email.setCanLoseFocus(true);
      this.buttonList.add(new GuiButton(0, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 60, RenderUtils2D.newScaledResolution().getScaledHeight() - 54, 120, 20, "Cancel"));
      this.buttonList.add(new GuiButton(1, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 60, RenderUtils2D.newScaledResolution().getScaledHeight() - 75, 120, 20, "Add"));
      super.initGui();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if(this.username.isFocused()) {
         this.username.textboxKeyTyped(typedChar, keyCode);
      }

      if(this.email.isFocused()) {
         this.email.textboxKeyTyped(typedChar, keyCode);
      }

      if(this.password.isFocused()) {
         this.password.textboxKeyTyped(typedChar, keyCode);
      }

      if(keyCode == 28 || keyCode == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(1));
      }

      super.keyTyped(typedChar, keyCode);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      Weepcraft.drawWeepString();
      this.username.drawTextBox();
      this.username.updateCursorCounter();
      this.email.drawTextBox();
      this.email.updateCursorCounter();
      this.password.drawTextBox();
      this.password.updateCursorCounter();
      Gui.drawString(Wrapper.fr(), "Username", (float)this.username.xPosition, (float)(this.username.yPosition - 10), -9868951);
      Gui.drawString(Wrapper.fr(), "Email", (float)this.email.xPosition, (float)(this.email.yPosition - 10), -9868951);
      Gui.drawString(Wrapper.fr(), "Password", (float)this.email.xPosition, (float)(this.email.yPosition + 30), -9868951);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         Wrapper.mc().displayGuiScreen(this.parent);
      }

      if(button.id == 1) {
         if(this.email.getText().equalsIgnoreCase("")) {
            this.email.setText("[NULL]");
         }

         if(this.password.getText().equalsIgnoreCase("")) {
            this.password.setText("[NULL]");
         }

         Alt alt = new Alt(this.username.getText(), this.email.getText(), this.password.getText());
         Alt.alts.add(alt);
         AltsFile.save();
         Wrapper.mc().displayGuiScreen(this.parent);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.username.mouseClicked(mouseX, mouseY, mouseButton);
      this.email.mouseClicked(mouseX, mouseY, mouseButton);
      this.password.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }
}
