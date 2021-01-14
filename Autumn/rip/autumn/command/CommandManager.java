package rip.autumn.command;

import java.util.ArrayList;
import java.util.List;
import rip.autumn.command.impl.BindCommand;
import rip.autumn.command.impl.ClientNameCommand;
import rip.autumn.command.impl.ConfigCommand;
import rip.autumn.command.impl.HelpCommand;
import rip.autumn.command.impl.TeleportCommand;
import rip.autumn.command.impl.ToggleCommand;
import rip.autumn.command.impl.VClipCommand;
import rip.autumn.core.Autumn;

public final class CommandManager {
   public static final String PREFIX = ".";
   private final List commands = new ArrayList();

   public CommandManager() {
      Autumn.EVENT_BUS_REGISTRY.eventBus.subscribe(new CommandHandler(this));
      this.commands.add(new HelpCommand());
      this.commands.add(new BindCommand());
      this.commands.add(new VClipCommand());
      this.commands.add(new TeleportCommand());
      this.commands.add(new ToggleCommand());
      this.commands.add(new ConfigCommand());
      this.commands.add(new ClientNameCommand());
   }

   public List getCommands() {
      return this.commands;
   }

   public final boolean execute(String args) {
      String noPrefix = args.substring(1);
      String[] split = noPrefix.split(" ");
      if (split.length > 0) {
         List commands = this.commands;
         int i = 0;

         for(int commandsSize = commands.size(); i < commandsSize; ++i) {
            AbstractCommand command = (AbstractCommand)commands.get(i);
            String[] var8 = command.getAliases();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String alias = var8[var10];
               if (split[0].equalsIgnoreCase(alias)) {
                  command.execute(split);
                  return true;
               }
            }
         }
      }

      return false;
   }
}
