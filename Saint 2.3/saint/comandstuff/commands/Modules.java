package saint.comandstuff.commands;

import java.util.Iterator;
import saint.Saint;
import saint.comandstuff.Command;
import saint.modstuff.Module;
import saint.utilities.Logger;

public class Modules extends Command {
   public Modules() {
      super("modules", "none", "mods", "ml");
   }

   public void run(String message) {
      StringBuilder list = new StringBuilder("Modules (" + Saint.getModuleManager().getContentList().size() + "): ");
      Iterator var4 = Saint.getModuleManager().getContentList().iterator();

      while(var4.hasNext()) {
         Module module = (Module)var4.next();
         list.append(module.isEnabled() ? "§a" : "§f").append(module.getName()).append("§f, ");
      }

      Logger.writeChat(list.toString().substring(0, list.toString().length() - 2));
   }
}
