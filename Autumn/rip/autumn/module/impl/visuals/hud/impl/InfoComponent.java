package rip.autumn.module.impl.visuals.hud.impl;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import rip.autumn.module.impl.visuals.hud.Component;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.utils.render.Palette;

public final class InfoComponent extends Component {
   public InfoComponent(HUDMod parent) {
      super(parent);
   }

   public void draw(ScaledResolution sr) {
      HUDMod hud = this.getParent();
      int height = sr.getScaledHeight();
      FontRenderer fr = hud.defaultFont.getValue() ? mc.fontRendererObj : mc.fontRenderer;
      int color = Palette.fade((Color)hud.color.getValue()).getRGB();
      String fps = String.format("FPS§7: %d", Minecraft.getDebugFPS());
      int fontHeight = 9;
      switch((HUDMod.InfoDisplayMode)hud.infoDisplayMode.getValue()) {
      case LEFT:
         if (mc.currentScreen instanceof GuiChat) {
            fontHeight += 15;
         }

         fr.drawStringWithShadow(fps, 2.0F, (float)(height - fontHeight), color);
         break;
      case RIGHT:
         int width = sr.getScaledWidth();
         fr.drawStringWithShadow(fps, (float)(width - fr.getStringWidth(fps)) - 2.0F, (float)(height - 9), color);
      }

   }
}
