package me.razerboy420.weepcraft.gui.click.window;

import java.util.Iterator;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Window;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.radar.Radar;
public class WindowRadar extends Window {

   public WindowRadar() {
      super("Radar", 2, 98);
   }

   public Window init() {
      Weepcraft.radar = new Radar();
      return this;
   }
}
