package saint.comandstuff.commands;

import java.util.Iterator;
import saint.Saint;
import saint.comandstuff.Command;
import saint.modstuff.Module;

public class AllOff extends Command {
   public AllOff() {
      super("alloff", "none", "aoff", "ao");
   }

   public void run(String message) {
      Iterator var3 = Saint.getModuleManager().getContentList().iterator();

      while(var3.hasNext()) {
         Module mod = (Module)var3.next();
         if (mod.isEnabled()) {
            mod.toggle();
         }
      }

   }
}
