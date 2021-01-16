package org.m0jang.crystal.Mod.Collection.Combat;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Reach extends Module {
   public Reach() {
      super("Reach", Category.Combat, false);
   }

   public static boolean enabledReach() {
      return Crystal.INSTANCE != null && Crystal.INSTANCE.getMods().get(Reach.class) != null && Crystal.INSTANCE.getMods().get(Reach.class).isEnabled();
   }
}
