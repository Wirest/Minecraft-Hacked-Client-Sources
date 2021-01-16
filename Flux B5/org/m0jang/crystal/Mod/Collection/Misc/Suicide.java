package org.m0jang.crystal.Mod.Collection.Misc;

import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.EntityUtils;

public class Suicide extends Module {
   public Suicide() {
      super("Suicide", Category.Misc, false);
   }

   public void onEnable() {
      EntityUtils.damagePlayer(40);
      this.setEnabled(false);
   }
}
