package rip.autumn.command.impl;

import java.util.Arrays;
import java.util.List;
import rip.autumn.command.AbstractCommand;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.utils.Logger;

public final class ClientNameCommand extends AbstractCommand {
   private final List chars = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'k', 'm', 'o', 'l', 'n', 'r');

   public ClientNameCommand() {
      super("clientname", "Change text displayed on watermark.", "clientname <name>", "clientname", "name", "rename");
   }

   public void execute(String... arguments) {
      if (arguments.length >= 2) {
         StringBuilder string = new StringBuilder();

         for(int i = 1; i < arguments.length; ++i) {
            String tempString = arguments[i];
            tempString = tempString.replace('&', '§');
            string.append(tempString).append(" ");
         }

         Logger.log(String.format("Changed client name to '%s§7' was '%s§7'.", string.toString().trim(), HUDMod.clientName));
         HUDMod.clientName = string.toString().trim();
      } else {
         this.usage();
      }

   }
}
