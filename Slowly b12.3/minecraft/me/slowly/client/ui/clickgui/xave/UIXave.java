package me.slowly.client.ui.clickgui.xave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.clickgui.xave.uis.XPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class UIXave extends GuiScreen {
   public static int guiMode;
   public static float alpha;
   protected Minecraft mc = Minecraft.getMinecraft();
   private ArrayList panels = new ArrayList();
   public static Mod currentMod = null;
   public static Mod.Category move;
   public static Mod.Category cats;

   public UIXave() {
      int xAxis = 20;
      Mod.Category[] values;
      int length = (values = Mod.Category.values()).length;

      for(int i = 0; i < length; ++i) {
         Mod.Category c = values[i];
         if (c != Mod.Category.NONE && c != Mod.Category.SETTINGS) {
            XPanel panel = new XPanel(xAxis, 10, c);
            this.panels.add(panel);
            xAxis += panel.getWidth() + 5;
         }
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      currentMod = null;
      drawToggleButton(mouseX, mouseY);
      Iterator var5 = this.panels.iterator();

      while(var5.hasNext()) {
         XPanel p = (XPanel)var5.next();
         p.drawScreen(mouseX, mouseY, partialTicks);
      }

   }

   public static void drawToggleButton(int mouseX, int mouseY) {
      Client.getInstance().getTaskBar().drawScreen(mouseX, mouseY);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      Iterator var5 = this.panels.iterator();

      while(var5.hasNext()) {
         XPanel p = (XPanel)var5.next();
         p.mouseClicked(mouseX, mouseY, mouseButton);
      }

      if (currentMod != null) {
         if (mouseButton == 0) {
            currentMod.set(!currentMod.isEnabled());
         }

         if (mouseButton == 1 && currentMod.hasValues()) {
            currentMod.openValues = !currentMod.openValues;
         }
      }

   }

   public void initGui() {
      if (this.mc.theWorld != null && !this.mc.gameSettings.ofFastRender) {
         this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
      }

      super.initGui();
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      Iterator var5 = this.panels.iterator();

      while(var5.hasNext()) {
         XPanel p = (XPanel)var5.next();
         p.mouseReleased(mouseX, mouseY, state);
      }

      move = null;
   }

   public void onGuiClosed() {
      this.mc.entityRenderer.func_181022_b();
      Client.getInstance().getFileUtil().saveXaveUI();
   }

   public ArrayList getPanels() {
      return this.panels;
   }
}
