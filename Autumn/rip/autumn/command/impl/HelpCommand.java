package rip.autumn.command.impl;

import java.util.Iterator;
import rip.autumn.command.AbstractCommand;
import rip.autumn.core.Autumn;
import rip.autumn.utils.Logger;

public final class HelpCommand extends AbstractCommand {
   public HelpCommand() {
      super("Help", "Lists all commands.", "help", "help", "h");
   }

   public void execute(String... arguments) {
      if (arguments.length == 1) {
         Logger.log("---------------- Help ----------------");
         Iterator var2 = Autumn.MANAGER_REGISTRY.commandManager.getCommands().iterator();

         while(var2.hasNext()) {
            AbstractCommand command = (AbstractCommand)var2.next();
            Logger.log(command.getName() + ": §F" + command.getDescription());
         }
      } else {
         this.usage();
      }

   }
}
