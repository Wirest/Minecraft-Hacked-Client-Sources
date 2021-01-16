package me.razerboy420.weepcraft.gui.click.window;

import java.util.Iterator;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Window;
import me.razerboy420.weepcraft.module.Module;
public class WindowMisc extends Window {

   public WindowMisc() {
      super("Misc", 2, 50);
   }

   public Window init() {
      Iterator var2 = Weepcraft.getMods().iterator();

      while(var2.hasNext()) {
         Module m = (Module)var2.next();
         if(m.getCategory() == Module.Category.MISC) {
            this.addButton(m);
         }
      }

      return this;
   }
}
