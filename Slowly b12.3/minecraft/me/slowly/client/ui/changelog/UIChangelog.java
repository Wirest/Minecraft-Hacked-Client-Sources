package me.slowly.client.ui.changelog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import me.slowly.client.Client;
import me.slowly.client.ui.particles.ParticleManager;
import me.slowly.client.ui.particles.particle.ParticleSnow;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UIChangelog extends GuiScreen {
   private GuiScreen parentScreen;
   private Map logs = new LinkedHashMap();

   public UIChangelog(GuiScreen parentScreen) {
      this.parentScreen = parentScreen;
      this.addLogs();
   }

   public void initGui() {
      GuiMainMenu.particleManager = new ParticleManager(new ParticleSnow(), 100);
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution res = new ScaledResolution(this.mc);
      RenderUtil.drawImage(new ResourceLocation("slowly/wallpaper.jpg"), 0, 0, res.getScaledWidth(), res.getScaledHeight());
      this.mc.fontRendererObj.drawStringWithShadow("Back", 2.0F, 1.0F, Colors.WHITE.c);
      int xStart = 10;
      int xStart2 = 190;
      int xStart3 = 340;
      int xStart4 = 475;
      int y = 50;
      int y2 = 50;
      int y3 = 50;
      int y4 = 50;
      UnicodeFontRenderer realTitle = Client.getInstance().getFontManager().consolasbold30;
      realTitle.drawCenteredString("Changelog", (float)(res.getScaledWidth() / 2) + 0.5F, 10.5F, Colors.BLACK.c);
      realTitle.drawCenteredString("Changelog", (float)(res.getScaledWidth() / 2), 10.0F, -1);
      UnicodeFontRenderer normalFont = Client.getInstance().getFontManager().consolasbold15;
      UnicodeFontRenderer titleFont = Client.getInstance().getFontManager().consolasbold20;
      Iterator var17 = this.logs.keySet().iterator();

      while(true) {
         while(var17.hasNext()) {
            String s = (String)var17.next();
            String changeStr;
            Iterator var19;
            if (y < 300) {
               titleFont.drawString(s, (float)xStart + 0.5F, (float)y + 0.5F, Colors.BLACK.c);
               titleFont.drawString(s, (float)xStart, (float)y, -1);
               y += titleFont.getStringHeight(s) + 1;

               for(var19 = ((ArrayList)this.logs.get(s)).iterator(); var19.hasNext(); y += normalFont.getStringHeight(changeStr)) {
                  changeStr = (String)var19.next();
                  normalFont.drawString(changeStr, (float)(xStart + 10) + 0.5F, (float)y + 0.5F, Colors.BLACK.c);
                  normalFont.drawString(changeStr, (float)(xStart + 10), (float)y, -1);
               }
            } else if (y2 < 300) {
               titleFont.drawString(s, (float)xStart2 + 0.5F, (float)y2 + 0.5F, Colors.BLACK.c);
               titleFont.drawString(s, (float)xStart2, (float)y2, -1);
               y2 += titleFont.getStringHeight(s) + 1;

               for(var19 = ((ArrayList)this.logs.get(s)).iterator(); var19.hasNext(); y2 += normalFont.getStringHeight(changeStr)) {
                  changeStr = (String)var19.next();
                  normalFont.drawString(changeStr, (float)xStart2 + 0.5F, (float)y2 + 0.5F, Colors.BLACK.c);
                  normalFont.drawString(changeStr, (float)xStart2, (float)y2, -1);
               }
            } else if (y3 < 300) {
               titleFont.drawString(s, (float)xStart3 + 0.5F, (float)y3 + 0.5F, Colors.BLACK.c);
               titleFont.drawString(s, (float)xStart3, (float)y3, -1);
               y3 += titleFont.getStringHeight(s) + 1;

               for(var19 = ((ArrayList)this.logs.get(s)).iterator(); var19.hasNext(); y3 += normalFont.getStringHeight(changeStr)) {
                  changeStr = (String)var19.next();
                  normalFont.drawString(changeStr, (float)xStart3 + 0.5F, (float)y3 + 0.5F, Colors.BLACK.c);
                  normalFont.drawString(changeStr, (float)xStart3, (float)y3, -1);
               }
            } else if (y4 < 300) {
               titleFont.drawString(s, (float)xStart4 + 0.5F, (float)y4 + 0.5F, Colors.BLACK.c);
               titleFont.drawString(s, (float)xStart4, (float)y4, -1);
               y4 += titleFont.getStringHeight(s) + 1;

               for(var19 = ((ArrayList)this.logs.get(s)).iterator(); var19.hasNext(); y4 += normalFont.getStringHeight(changeStr)) {
                  changeStr = (String)var19.next();
                  normalFont.drawString(changeStr, (float)xStart4 + 0.5F, (float)y4 + 0.5F, Colors.BLACK.c);
                  normalFont.drawString(changeStr, (float)xStart4, (float)y4, -1);
               }
            }
         }

         super.drawScreen(mouseX, mouseY, partialTicks);
         return;
      }
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseX <= this.mc.fontRendererObj.getStringWidth("Back") && mouseY <= this.mc.fontRendererObj.FONT_HEIGHT) {
         this.mc.displayGuiScreen(this.parentScreen);
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
   }

   private void addLogs() {
      InputStream in = this.getClass().getResourceAsStream("logs/logs");
      BufferedReader input = new BufferedReader(new InputStreamReader(in));

      try {
         String s;
         while((s = input.readLine()) != null) {
            this.addChanges(s);
         }

         input.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   private void addChanges(String version) {
      InputStream in = this.getClass().getResourceAsStream("logs/" + version);
      BufferedReader input = new BufferedReader(new InputStreamReader(in));

      try {
         ArrayList list = new ArrayList();

         String s;
         while((s = input.readLine()) != null) {
            list.add(s);
         }

         input.close();
         this.logs.put(version, list);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }
}
