package org.m0jang.crystal.Mod.Collection.Movement;

import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Movement.nofall.Kohi;
import org.m0jang.crystal.Mod.Collection.Movement.nofall.Vanilla;

public class NoFall extends Module {
   public NoFall() {
      super("NoFall", Category.Movement, true, new Vanilla(), new Kohi());
      this.setSubModule("Vanilla");
   }
}
