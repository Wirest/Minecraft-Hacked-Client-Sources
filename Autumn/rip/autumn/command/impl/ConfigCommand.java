package rip.autumn.command.impl;

import java.util.List;
import rip.autumn.command.AbstractCommand;
import rip.autumn.config.ConfigManager;
import rip.autumn.core.Autumn;
import rip.autumn.utils.Logger;

public final class ConfigCommand extends AbstractCommand {
   public ConfigCommand() {
      super("Config", "Save, load and delete configs.", "config <save/load/delete/list/refresh> <name(optional)>", "config", "c");
   }

   public void execute(String... arguments) {
      if (arguments.length > 1) {
         String operator = arguments[1].toUpperCase();
         if (arguments.length == 3) {
            byte var4 = -1;
            switch(operator.hashCode()) {
            case 2342118:
               if (operator.equals("LOAD")) {
                  var4 = 0;
               }
               break;
            case 2537853:
               if (operator.equals("SAVE")) {
                  var4 = 1;
               }
               break;
            case 2012838315:
               if (operator.equals("DELETE")) {
                  var4 = 2;
               }
            }

            switch(var4) {
            case 0:
               if (Autumn.MANAGER_REGISTRY.configManager.load(arguments[2])) {
                  Logger.log("Loaded config named " + arguments[2]);
               }
               break;
            case 1:
               if (Autumn.MANAGER_REGISTRY.configManager.save(arguments[2])) {
                  Logger.log("Saved config as " + arguments[2]);
               }
               break;
            case 2:
               if (Autumn.MANAGER_REGISTRY.configManager.delete(arguments[2])) {
                  Logger.log("Deleted config named " + arguments[2]);
               }
            }
         } else if (arguments.length == 2) {
            if (operator.equalsIgnoreCase("LIST")) {
               Logger.log("---------------- Configs ----------------");
               List configs = Autumn.MANAGER_REGISTRY.configManager.getConfigs();
               int i = 0;

               for(int configsSize = configs.size(); i < configsSize; ++i) {
                  ConfigManager.Config config = (ConfigManager.Config)configs.get(i);
                  Logger.log(config.getName() + String.format("§F (%s)", i));
               }
            } else if (operator.equalsIgnoreCase("REFRESH")) {
               Autumn.MANAGER_REGISTRY.configManager.refresh();
            }
         } else {
            this.usage();
         }
      } else {
         this.usage();
      }

   }
}
