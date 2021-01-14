package rip.autumn.module.impl.world;

import rip.autumn.annotations.Label;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.DoubleOption;

@Label("Time Changer")
@Category(ModuleCategory.WORLD)
@Aliases({"timechanger", "worldtime"})
public final class TimeChangerMod extends Module {
   public final DoubleOption time = new DoubleOption("Time", 16000.0D, 1.0D, 24000.0D, 100.0D);

   public TimeChangerMod() {
      this.addOptions(new Option[]{this.time});
   }
}
