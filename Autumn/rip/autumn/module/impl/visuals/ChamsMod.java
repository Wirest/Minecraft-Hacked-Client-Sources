package rip.autumn.module.impl.visuals;

import rip.autumn.annotations.Label;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.render.Palette;

@Label("Chams")
@Category(ModuleCategory.VISUALS)
public final class ChamsMod extends Module {
   public final EnumOption mode;
   public final EnumOption player;
   public final EnumOption playerBehindWalls;
   public static final int HANDCOL = -1253464587;

   public ChamsMod() {
      this.mode = new EnumOption("Mode", ChamsMod.Mode.COLOR);
      this.player = new EnumOption("Player", Palette.PURPLE);
      this.playerBehindWalls = new EnumOption("Player behind walls", Palette.PURPLE);
      this.addOptions(new Option[]{this.mode, this.player, this.playerBehindWalls});
   }

   public static enum Mode {
      COLOR,
      TEXTURED;
   }
}
