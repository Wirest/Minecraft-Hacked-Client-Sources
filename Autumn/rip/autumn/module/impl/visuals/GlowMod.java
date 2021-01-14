package rip.autumn.module.impl.visuals;

import java.awt.Color;
import me.zane.basicbus.api.annotations.Listener;
import rip.autumn.annotations.Label;
import rip.autumn.events.game.TickEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.ColorOption;
import rip.autumn.module.option.impl.DoubleOption;

@Label("Glow")
@Category(ModuleCategory.VISUALS)
public final class GlowMod extends Module {
   public static final DoubleOption lineWidth = new DoubleOption("Line Width", 3.0D, 0.5D, 5.0D, 0.5D);
   public static final ColorOption friendlyColor = new ColorOption("Friendly Color", new Color(127, 226, 72, 255));
   public static final ColorOption enemyColor = new ColorOption("Enemy Color", new Color(255, 50, 50, 120));

   public GlowMod() {
      this.addOptions(new Option[]{lineWidth, friendlyColor, enemyColor});
   }

   @Listener(TickEvent.class)
   public final void onTick() {
      mc.gameSettings.ofFastRender = false;
   }
}
