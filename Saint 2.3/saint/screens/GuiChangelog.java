package saint.screens;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import saint.Saint;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class GuiChangelog extends GuiScreen {
   private final List log = new ArrayList();
   private float maxWidth;
   private float maxHeight;

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/saintbg.png"));
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), (float)scaledRes.getScaledWidth(), (float)scaledRes.getScaledHeight());
      RenderHelper.drawBorderedRect((float)(this.width / 2) - this.maxWidth / 2.0F - 6.0F, 38.0F, (float)(this.width / 2) + this.maxWidth / 2.0F + 6.0F, this.maxHeight + 7.0F, 1.5F, -587202560, Integer.MIN_VALUE);
      if (this.log.isEmpty()) {
         RenderHelper.getNahrFont().drawString("Loading...", (float)(this.width / 2) - RenderHelper.getNahrFont().getStringWidth("Loading...") / 2.0F, 40.0F, NahrFont.FontType.EMBOSS_TOP, -1, 1073741824);
      }

      int y = 40;
      Iterator var7 = this.log.iterator();

      String text;
      while(var7.hasNext()) {
         text = (String)var7.next();
         if (Saint.isLatestVersion()) {
            RenderHelper.getNahrFont().drawString(text, (float)(this.width / 2) - RenderHelper.getNahrFont().getStringWidth(text) / 2.0F, (float)y, NahrFont.FontType.EMBOSS_TOP, -1, Integer.MIN_VALUE);
            y = (int)((float)y + RenderHelper.getNahrFont().getStringHeight(text));
            if (this.maxHeight < (float)y) {
               this.maxHeight = (float)y;
            }

            if (this.maxWidth < RenderHelper.getNahrFont().getStringWidth(text)) {
               this.maxWidth = RenderHelper.getNahrFont().getStringWidth(text);
            }
         }
      }

      text = "Saint Changelog - Version " + (Saint.isLatestVersion() ? "§a" : "§c") + Saint.getVersion();
      RenderHelper.getNahrFont().drawString(text, (float)(this.width / 2) - RenderHelper.getNahrFont().getStringWidth(text) / 2.0F, 10.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void initGui() {
      try {
         URL url = new URL("http://andrewthehax0r.xyz/changelog.txt");
         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

         String line;
         while((line = in.readLine()) != null) {
            this.log.add(line);
         }

         in.close();
      } catch (Exception var4) {
      }

      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 40, "Back"));
   }

   protected void actionPerformed(GuiButton p_146284_1_) {
      this.mc.displayGuiScreen(new GuiMainMenu());
   }
}
