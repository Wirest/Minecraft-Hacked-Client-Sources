package me.slowly.client.ui.clickgui;

import me.slowly.client.Client;
import me.slowly.client.ui.clickgui.newclickgui.ClickGui;
import me.slowly.client.ui.options.UIDesign;
import me.slowly.client.ui.options.UIScript;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UITaskBar {
   private int guiMode = 0;
   private boolean show;
   private MouseInputHandler mouseInput1 = new MouseInputHandler(0);
   private MouseInputHandler mouseInput2 = new MouseInputHandler(0);

   public void drawScreen(int mouseX, int mouseY) {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      boolean x = false;
      boolean x2 = true;
      int y = res.getScaledHeight() - 32;
      int y2 = res.getScaledHeight();
      boolean hover = mouseX >= 0 && mouseX <= 32 && mouseY >= y && mouseY <= y2;
      if (hover || this.show) {
         Gui.circle(0.0F, (float)res.getScaledHeight(), 39.0F, ClientUtil.reAlpha(Integer.MAX_VALUE, 1.0F));
      }

      if (hover && this.mouseInput2.canExcecute()) {
         this.show = !this.show;
      }

      Gui.circle(0.0F, (float)res.getScaledHeight(), 40.0F, ClientUtil.reAlpha(Colors.BLACK.c, 0.75F));
      RenderUtil.drawImage(new ResourceLocation("slowly/icon/settings_icon.png"), 0, res.getScaledHeight() - 32, 32, 32);
      if (this.show) {
         this.drawToggleButton(mouseX, mouseY);
      }

      this.end();
   }

   public boolean drawToggleButton(int mouseX, int mouseY) {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      int x = 40;
      int x2 = x + 50;
      int y = res.getScaledHeight() - 20;
      int y2 = y + 15;
      boolean hover1 = mouseX >= x - 40 && mouseX <= x2 - 60 && mouseY >= y - 40 && mouseY <= y2 - 40;
      boolean hover2 = mouseX >= x - 40 && mouseX <= x2 - 60 && mouseY >= y - 60 && mouseY <= y2 - 60;
      boolean hover3 = mouseX >= x - 40 && mouseX <= x2 - 60 && mouseY >= y - 80 && mouseY <= y2 - 80;
      Gui.drawBorderedRect(x - 40, y - 40, x2 - 60, y2 - 40, 1, 0, Integer.MIN_VALUE);
      Gui.drawBorderedRect(x - 40, y - 60, x2 - 60, y2 - 60, 1, 0, Integer.MIN_VALUE);
      Gui.drawBorderedRect(x - 40, y - 80, x2 - 60, y2 - 80, 1, 0, Integer.MIN_VALUE);
      Client.getInstance().getFontManager().arialBold15.drawString("Script", (float)(x - 37), (float)(y - 37), -1);
      Client.getInstance().getFontManager().arialBold15.drawString("Design", (float)(x - 37), (float)(y - 57), -1);
      if (this.mouseInput2.canExcecute() && (hover1 || hover2 || hover3)) {
         Minecraft.getMinecraft().displayGuiScreen((GuiScreen)(hover1 ? new UIScript() : (hover2 ? new UIDesign() : new UIDesign())));
      }

      return false;
   }

   private void end() {
      ClickGui.guiMode = this.guiMode;
   }
}
