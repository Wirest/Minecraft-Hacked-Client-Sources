package rip.autumn.alt.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import rip.autumn.alt.Alt;
import rip.autumn.alt.AltLoginService;

public final class GuiAltManager extends GuiScreen {
   private final GuiScreen previousScreen;
   private AltLoginService thread;
   private Alt selected;

   public GuiAltManager(GuiScreen previousScreen) {
      this.previousScreen = previousScreen;
   }

   protected void actionPerformed(GuiButton button) {
      switch(button.id) {
      case 0:
         this.mc.displayGuiScreen(new GuiDirectLogin(this.previousScreen));
         break;
      case 1:
         this.mc.displayGuiScreen(this.previousScreen);
         break;
      case 2:
         if (this.selected != null) {
         }
      }

   }

   public void drawScreen(int x, int y, float z) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.mc.fontRendererObj, "Alt Manager", this.width / 2, 20, -1);
      this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? EnumChatFormatting.GRAY + "Waiting..." : this.thread.getStatus(), this.width / 2, 29, -1);
      super.drawScreen(x, y, z);
   }

   public void initGui() {
      int var3 = this.height - 24;
      this.buttonList.add(new GuiButton(0, this.width / 2 + 102, var3, 100, 20, "Direct Login"));
      this.buttonList.add(new GuiButton(2, this.width / 2, var3, 100, 20, "Login"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 102, var3, 100, 20, "Back"));
      Keyboard.enableRepeatEvents(true);
   }

   protected void keyTyped(char character, int key) {
      try {
         super.keyTyped(character, key);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   protected void mouseClicked(int x, int y, int button) {
      try {
         super.mouseClicked(x, y, button);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void updateScreen() {
   }
}
