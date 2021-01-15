package me.aristhena.lucid.modules.player;

import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.value.Val;

@Mod
public class SpeedMine
  extends Module
{
  @Val(min=1.0D, max=10.0D, increment=1.0D)
  public static double amplifier = 1.0D;
}
