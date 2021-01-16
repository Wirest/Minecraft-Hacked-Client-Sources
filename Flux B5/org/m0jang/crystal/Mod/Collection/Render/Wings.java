package org.m0jang.crystal.Mod.Collection.Render;

import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Values.Value;

public class Wings extends Module {
   public static Value red;
   public static Value green;
   public static Value blue;
   public static Value size;

   static {
      red = new Value("Wings", Float.TYPE, "Red", 208.0F, 1.0F, 255.0F, 1.0F);
      green = new Value("Wings", Float.TYPE, "Green", 11.0F, 1.0F, 255.0F, 1.0F);
      blue = new Value("Wings", Float.TYPE, "Blue", 110.0F, 1.0F, 255.0F, 1.0F);
      size = new Value("Wings", Float.TYPE, "Size", 60.0F, 1.0F, 1000.0F, 1.0F);
   }

   public Wings() {
      super("Wings", Category.Render, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   public void onUpdate(EventUpdate event) {
   }
}
