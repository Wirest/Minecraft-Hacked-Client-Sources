package org.m0jang.crystal.UI;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.click.RenderUtils;

public class LoadingScreen extends GuiScreen {
   GuiScreen parentScreen;
   public String infoMsg;
   public String statusMsg;
   public boolean endRequested;
   public boolean cancellable;

   public LoadingScreen(GuiScreen parent, boolean cancellable) {
      this.parentScreen = parent;
      this.cancellable = cancellable;
   }

   public void initGui() {
      super.initGui();
      if (this.cancellable) {
         this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 2 + 30, "Cancel"));
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      super.actionPerformed(button);
      if (this.cancellable && button.id == 200) {
         this.endRequested = true;
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      super.drawDefaultBackground();
      Fonts.segoe18.drawCenteredStringXY(this.infoMsg, RenderUtils.getDisplayWidth() / 2, RenderUtils.getDisplayHeight() / 2 - 15, -263693982, true);
      Fonts.segoe18.drawCenteredStringXY(this.statusMsg, RenderUtils.getDisplayWidth() / 2, RenderUtils.getDisplayHeight() / 2, -263693982, true);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }
}
