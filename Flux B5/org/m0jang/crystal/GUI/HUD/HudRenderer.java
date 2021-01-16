package org.m0jang.crystal.GUI.HUD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.GUI.HUD.Themes.Flux;
import org.m0jang.crystal.GUI.HUD.Themes.HudTheme;
import org.m0jang.crystal.GUI.HUD.Themes.OldFlux;
import org.m0jang.crystal.GUI.click.WolframGui;

public class HudRenderer extends GuiIngame {
   List HudThemes = new ArrayList();

   public HudRenderer(Minecraft mcIn) {
      super(mcIn);
      this.HudThemes.add(new Flux());
      this.HudThemes.add(new OldFlux());
   }

   public void renderUI(float particalTicks) {
      super.renderUI(particalTicks);
      String theme = Crystal.HudTheme.getSelectedOption();
      Iterator var4 = this.HudThemes.iterator();

      while(var4.hasNext()) {
         HudTheme item = (HudTheme)var4.next();
         if (item.getClass().getSimpleName().equalsIgnoreCase(theme)) {
            item.render();
            if (!(Minecraft.getMinecraft().currentScreen instanceof WolframGui)) {
               item.renderTab();
            }
            break;
         }
      }

   }
}
