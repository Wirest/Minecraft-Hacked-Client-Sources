package saint.comandstuff.commands;

import java.util.Arrays;
import java.util.Iterator;
import saint.Saint;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class Help extends Command {
   public Help() {
      super("help", "none", "cmds");
   }

   public void run(String message) {
      String[] arguments = message.split(" ");
      if (arguments.length == 1) {
         StringBuilder commands = new StringBuilder("Commands: ");
         Iterator var5 = Saint.getCommandManager().getContentList().iterator();

         while(var5.hasNext()) {
            Command command = (Command)var5.next();
            commands.append(command.getCommand() + ", ");
         }

         Logger.writeChat(commands.substring(0, commands.length() - 2).toString());
      } else {
         Command command = Saint.getCommandManager().getCommandUsingName(arguments[1]);
         if (command == null) {
            Logger.writeChat("Command \"." + arguments[1] + "\" was not found!");
         } else {
            Logger.writeChat("Command: " + command.getCommand());
            if (command.getAliases().length != 0) {
               Logger.writeChat("Aliases: " + Arrays.toString(command.getAliases()));
            }

            if (!command.getArguments().equals("none")) {
               Logger.writeChat("Args: " + command.getArguments());
            }
         }
      }

   }
}
