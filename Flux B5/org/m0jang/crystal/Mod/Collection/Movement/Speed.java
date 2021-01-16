package org.m0jang.crystal.Mod.Collection.Movement;

import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Movement.speed.Frames;
import org.m0jang.crystal.Mod.Collection.Movement.speed.KohiGround;
import org.m0jang.crystal.Mod.Collection.Movement.speed.KohiHop;
import org.m0jang.crystal.Mod.Collection.Movement.speed.Latest;
import org.m0jang.crystal.Mod.Collection.Movement.speed.NCPHop;
import org.m0jang.crystal.Mod.Collection.Movement.speed.SlowHop;
import org.m0jang.crystal.Mod.Collection.Movement.speed.Swift;

public class Speed extends Module {
   public Speed() {
      super("Speed", Category.Movement, true, new NCPHop(), new SlowHop(), new Latest(), new Frames(), new Swift(), new KohiHop(), new KohiGround());
      this.setSubModule("SlowHop");
   }
}
