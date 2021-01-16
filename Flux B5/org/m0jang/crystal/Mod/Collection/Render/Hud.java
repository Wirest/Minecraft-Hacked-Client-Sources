package org.m0jang.crystal.Mod.Collection.Render;

import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class Hud extends Module {
   public static Value tabgui;
   public static Value watermark;
   public static Value array;
   public static Value coords;

   static {
      tabgui = new Value("Hud", Boolean.TYPE, "TabGui", true);
      watermark = new Value("Hud", Boolean.TYPE, "Watermark", true);
      array = new Value("Hud", Boolean.TYPE, "Arraylist", true);
      coords = new Value("Hud", Boolean.TYPE, "Coords", true);
   }

   public Hud() {
      super("Hud", Category.Render, false);
   }
}
