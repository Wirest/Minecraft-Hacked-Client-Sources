package rip.autumn.module.impl.visuals.hud.impl;

import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import rip.autumn.module.impl.visuals.hud.Component;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.utils.render.Palette;

public final class Watermark extends Component {
   public Watermark(HUDMod parent) {
      super(parent);
   }

   public void draw(ScaledResolution sr) {
      FontRenderer fr = this.getParent().defaultFont.getValue() ? mc.fontRendererObj : mc.fontRenderer;
      String firstLetter = HUDMod.clientName.substring(0, 1);
      fr.drawStringWithShadow(firstLetter, 2.0F, 2.0F, Palette.fade((Color)this.getParent().color.getValue()).getRGB());
      fr.drawStringWithShadow(HUDMod.clientName.substring(1), (float)(fr.getStringWidth(firstLetter) + 2), 2.0F, -1);
   }
}
