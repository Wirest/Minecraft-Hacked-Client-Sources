package rip.autumn.alt.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import rip.autumn.alt.AltLoginService;

public final class GuiDirectLogin extends GuiScreen {
   private final GuiScreen previousScreen;
   private AltLoginService thread;
   private GuiTextField combined;

   public GuiDirectLogin(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
            String u = this.combined.getText().split(":")[0];
            String p = this.combined.getText().split(":")[1];
            this.thread = new AltLoginService(u.replaceAll(" ", ""), p.replaceAll(" ", ""));
            this.thread.start();
         }
         break;
      case 1:
         this.mc.displayGuiScreen(this.previousScreen);
      }

   }

   public void drawScreen(int x, int y, float z) {
      this.drawDefaultBackground();
      this.combined.drawTextBox();
      this.drawCenteredString(this.mc.fontRendererObj, "Direct Login", this.width / 2, 20, -1);
      this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? EnumChatFormatting.GRAY + "Waiting for user input." : this.thread.getStatus(), this.width / 2, 29, -1);
      if (this.combined.getText().isEmpty()) {
         this.drawString(this.mc.fontRendererObj, "Email:Password", this.width / 2 - 96, 56, -5592406);
      }

      super.drawScreen(x, y, z);
   }

   public void initGui() {
      int var3 = this.height / 4 + 24;
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 20, "Login"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 20 + 24, "Back"));
      this.combined = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 50, 200, 20);
      this.combined.setMaxStringLength(200);
      Keyboard.enableRepeatEvents(true);
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      if (character == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      this.combined.textboxKeyTyped(character, key);
   }

   protected void mouseClicked(int x, int y, int button) {
      try {
         super.mouseClicked(x, y, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.combined.mouseClicked(x, y, button);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
      this.combined.updateCursorCounter();
   }
}
