package org.m0jang.crystal.Mod.Collection.Misc;

import java.util.Iterator;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;

public class Panic extends Module {
   public Panic() {
      super("Panic", Category.Misc, false);
   }

   public void onEnable() {
      Iterator var2 = Crystal.INSTANCE.getMods().getModList().iterator();

      while(var2.hasNext()) {
         Module mod = (Module)var2.next();
         if (mod.isEnabled()) {
            mod.setEnabled(false);
         }
      }

   }
}
