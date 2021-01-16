package org.m0jang.crystal.Mod.Collection.Movement;

import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Movement.fly.AAC;
import org.m0jang.crystal.Mod.Collection.Movement.fly.Cube;
import org.m0jang.crystal.Mod.Collection.Movement.fly.Hypixel;
import org.m0jang.crystal.Mod.Collection.Movement.fly.Mineplex;
import org.m0jang.crystal.Mod.Collection.Movement.fly.Vanilla;

public class Fly extends Module {
   public Fly() {
      super("Fly", Category.Movement, true, new Vanilla(), new Hypixel(), new Mineplex(), new AAC(), new Cube());
      this.setSubModule("Vanilla");
   }
}
