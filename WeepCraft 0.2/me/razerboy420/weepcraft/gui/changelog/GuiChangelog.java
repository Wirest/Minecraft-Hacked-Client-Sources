package me.razerboy420.weepcraft.gui.changelog;

import java.util.ArrayList;
import java.util.Iterator;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class GuiChangelog extends GuiScreen {

   public ArrayList log = new ArrayList();


   public void initGui() {
      this.log.add("[NEWV] wepcraft crak\'d\'d\'d");
      this.log.add("+ no changelogerino");
      this.log.add("- couldnt get my weeb cape working im truley upset");
      this.log.add("= hi peoples");
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      String weepcraftString = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
      drawCenteredString(Wrapper.fr(), weepcraftString + " " + ColorUtil.getColor(Weepcraft.normalColor) + Weepcraft.version + "v", (float)(this.width / 2), 2.0F, -1);
      int count = 0;

      for(Iterator var7 = this.log.iterator(); var7.hasNext(); ++count) {
         String s = (String)var7.next();
         int y = 25 + count * 10;
         if(s.contains("[NEWV]")) {
            drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "§l" + s.replace("[NEWV]", ""), (float)(this.width / 2), (float)y, -1);
         } else {
            Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + s, 2.0F, (float)y, -1);
            if(s.contains("+")) {
               Gui.drawRect(1.0F, (float)y, 10.0F, (float)(y + 9), -16744448);
            }

            if(s.contains("-")) {
               Gui.drawRect(1.0F, (float)y, 10.0F, (float)(y + 9), -8388608);
            }

            if(s.contains("=")) {
               Gui.drawRect(1.0F, (float)y, 10.0F, (float)(y + 9), -16777088);
            }

            if(s.contains("[]")) {
               Gui.drawRect(1.0F, (float)y, 10.0F, (float)(y + 9), -16777088);
            }
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
