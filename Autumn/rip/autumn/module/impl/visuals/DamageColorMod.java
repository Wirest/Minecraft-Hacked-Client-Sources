package rip.autumn.module.impl.visuals;

import java.awt.Color;
import rip.autumn.annotations.Label;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.ColorOption;

@Label("Damage Color")
@Category(ModuleCategory.VISUALS)
@Aliases({"damagecolor"})
public final class DamageColorMod extends Module {
   public static final ColorOption color = new ColorOption("Color", new Color(255, 0, 0, 76));

   public DamageColorMod() {
      this.addOptions(new Option[]{color});
   }
}
