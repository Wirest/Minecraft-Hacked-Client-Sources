package rip.autumn.module.impl.visuals;

import rip.autumn.annotations.Label;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;

@Label("Animations")
@Category(ModuleCategory.VISUALS)
public final class AnimationsMod extends Module {
   public final EnumOption mode;
   public static final DoubleOption swingSpeed = new DoubleOption("Swing Speed", 6.0D, 2.0D, 12.0D, 0.5D);
   public final DoubleOption x;
   public final DoubleOption y;
   public final DoubleOption z;

   public AnimationsMod() {
      this.mode = new EnumOption("Mode", AnimationsMod.Mode.EXHIBITION);
      this.x = new DoubleOption("X", 0.0D, -1.0D, 1.0D, 0.05D);
      this.y = new DoubleOption("Y", 0.15D, -1.0D, 1.0D, 0.05D);
      this.z = new DoubleOption("Z", 0.0D, -1.0D, 1.0D, 0.05D);
      this.setMode(this.mode);
      this.addOptions(new Option[]{this.mode, swingSpeed, this.x, this.y, this.z});
      this.setEnabled(true);
      this.setHidden(true);
   }

   public static enum Mode {
      OLD,
      EXHIBITION,
      SLIDE;
   }
}
