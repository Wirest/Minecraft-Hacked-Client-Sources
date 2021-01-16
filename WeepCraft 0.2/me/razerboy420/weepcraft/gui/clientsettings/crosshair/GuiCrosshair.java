package me.razerboy420.weepcraft.gui.clientsettings.crosshair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.razerboy420.weepcraft.files.ValuesFile;
import me.razerboy420.weepcraft.gui.clientsettings.GuiClientSettings;
import me.razerboy420.weepcraft.gui.clientsettings.crosshair.pieces.ColorSlider;
import me.razerboy420.weepcraft.gui.clientsettings.crosshair.pieces.Slider;
import me.razerboy420.weepcraft.module.modules.graphics.Crosshair;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiCrosshair extends GuiScreen {

   public ColorSlider cslider;
   public ArrayList sliders;
   ResourceLocation map;


   public GuiCrosshair() {
      this.cslider = new ColorSlider(this.width / 2 - 135, 2, 270, 22);
      this.sliders = new ArrayList();
      this.map = new ResourceLocation("weepcraft/crosshairmap.png");
   }

   public void initGui() {
      this.buttonList.add(new GuiButton(0, this.width / 2 - 40, this.height - 22, 80, 20, "Back"));
      this.cslider = new ColorSlider(this.width / 2 - 135, 2, 270, 22);
      int count = 0;
      Iterator var3 = Value.modes.iterator();

      while(var3.hasNext()) {
         Value v = (Value)var3.next();
         if(v.name.startsWith("crosshair_") && v != Crosshair.color) {
            int y = 25 + 23 * count;
            this.sliders.add(new Slider(this.width - 122, y, 120, 20, v));
            ++count;
         }
      }

      super.initGui();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         Wrapper.mc().displayGuiScreen(new GuiClientSettings((GuiScreen)(Wrapper.getPlayer() == null?new GuiMainMenu():new GuiIngameMenu())));
      }

      super.actionPerformed(button);
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.cslider.draw();
      Iterator y = this.sliders.iterator();

      while(y.hasNext()) {
         Slider x = (Slider)y.next();
         x.draw();
      }

      Gui.drawString(Wrapper.fr(), "Inspired by: http://tools.dathost.net/", 2.0F, (float)(this.height - 10), -1);
      this.mc.getTextureManager().bindTexture(this.map);
      int x1 = this.width / 2;
      int y1 = this.height / 2;
      GL11.glPushMatrix();
      x1 = (int)((float)x1 / 0.65F);
      y1 = (int)((float)y1 / 0.65F);
      GL11.glScaled(0.4D, 0.4D, 0.8D);
      Gui.drawModalRectWithCustomSizedTexture(x1 - 120, y1 - 60, 0.0F, 0.0F, 854, 480, 854.0F, 480.0F);
      GL11.glPopMatrix();
      x1 = this.width / 2;
      y1 = this.height / 2;
      if(mouseX <= 490 && mouseY >= 85 && mouseX >= 150 && mouseY <= 280) {
         Wrapper.drawCrosshair((float)mouseX, (float)mouseY);
      } else {
         Wrapper.drawCrosshair((float)x1, (float)y1);
      }

      if(Mouse.isButtonDown(0)) {
         ValuesFile.save();
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
