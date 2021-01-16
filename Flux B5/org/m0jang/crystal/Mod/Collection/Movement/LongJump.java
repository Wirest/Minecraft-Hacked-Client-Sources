package org.m0jang.crystal.Mod.Collection.Movement;

import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Movement.longjump.Kohi;
import org.m0jang.crystal.Mod.Collection.Movement.longjump.NCP;
import org.m0jang.crystal.Mod.Collection.Movement.longjump.OldNCP;

public class LongJump extends Module {
   public LongJump() {
      super("LongJump", Category.Movement, true, new OldNCP(), new NCP(), new Kohi());
      this.setSubModule("NCP");
   }
}
