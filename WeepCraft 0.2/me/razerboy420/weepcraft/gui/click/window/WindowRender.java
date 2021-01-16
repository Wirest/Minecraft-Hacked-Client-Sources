package me.razerboy420.weepcraft.gui.click.window;

import java.util.Iterator;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Window;
import me.razerboy420.weepcraft.module.Module;

public class WindowRender extends Window {

   public WindowRender() {
      super("Graphics", 2, 26);
   }

   public Window init() {
      Iterator var2 = Weepcraft.getMods().iterator();

      while(var2.hasNext()) {
         Module m = (Module)var2.next();
         if(m.getCategory() == Module.Category.RENDER) {
            this.addButton(m);
         }
      }

      return this;
   }
}
