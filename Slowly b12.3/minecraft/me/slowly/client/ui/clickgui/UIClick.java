package me.slowly.client.ui.clickgui;

import java.io.IOException;
import me.slowly.client.ui.buttons.UIPopUPButton;
import me.slowly.client.util.handler.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UIClick extends GuiScreen {
   private ClientEventHandler mouseClickedPopUpMenu = new ClientEventHandler();
   private UIPopUPButton uiPopUPButton;
   private ScaledResolution res;
   public boolean initialized;

   public void initGui() {
      this.res = new ScaledResolution(this.mc);
      this.mouseClickedPopUpMenu = new ClientEventHandler();
      if (!this.initialized) {
         this.uiPopUPButton = new UIPopUPButton(10.0F, (float)(this.res.getScaledHeight() - 10), 6.0F, 14.0F);
         this.initialized = true;
      }

      if (this.mc.theWorld != null && !this.mc.gameSettings.ofFastRender) {
         this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      }

   }

   public void load() {
      if (!this.initialized) {
         Runnable run = new Runnable() {
            public void run() {
               UIClick.this.uiPopUPButton = new UIPopUPButton(10.0F, (float)(Minecraft.getMinecraft().displayHeight - 10), 6.0F, 14.0F);
               UIClick.this.initialized = true;
            }
         };
         (new Thread(run)).start();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.uiPopUPButton.draw(mouseX, mouseY);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.uiPopUPButton.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      this.uiPopUPButton.mouseReleased(mouseX, mouseY);
   }

   private boolean isHovering(int mouseX, int mouseY, int x, int y, int x2, int y2) {
      return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      super.actionPerformed(button);
   }

   public void onGuiClosed() {
      this.mc.entityRenderer.func_181022_b();
      super.onGuiClosed();
   }
}
