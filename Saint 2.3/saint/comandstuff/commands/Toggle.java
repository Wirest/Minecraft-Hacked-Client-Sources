package saint.comandstuff.commands;

import saint.Saint;
import saint.comandstuff.Command;
import saint.modstuff.Module;
import saint.utilities.Logger;

public class Toggle extends Command {
   public Toggle() {
      super("toggle", "<mod name>", "t");
   }

   public void run(String message) {
      String[] arguments = message.split(" ");
      Module mod = Saint.getModuleManager().getModuleUsingName(arguments[1]);
      if (mod == null) {
         Logger.writeChat("Module \"" + arguments[1] + "\" is not valid!");
      } else {
         mod.toggle();
         Logger.writeChat("Module \"" + mod.getName() + "\" toggled " + (mod.isEnabled() ? "§2on" : "§4off") + "§f.");
      }

   }
}
