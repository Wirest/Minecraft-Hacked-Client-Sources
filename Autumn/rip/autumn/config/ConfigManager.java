package rip.autumn.config;

import com.google.common.collect.UnmodifiableIterator;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import rip.autumn.core.Autumn;
import rip.autumn.file.FileManager;
import rip.autumn.module.Module;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.Logger;

public final class ConfigManager {
   private final List configs = new ArrayList();
   private final File directory;

   public ConfigManager() {
      this.directory = new File(FileManager.HOME_DIRECTORY, "Configs");
      this.refresh();
   }

   public List getConfigs() {
      return this.configs;
   }

   public final void refresh() {
      if (this.directory.listFiles() != null) {
         File[] var1 = this.directory.listFiles();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            File file = var1[var3];
            if (this.get(file.getName()) == null) {
               this.add(FilenameUtils.removeExtension(file.getName()));
            }
         }
      }

   }

   public final void add(String name) {
      this.configs.add(new ConfigManager.Config(name, new File(this.directory, name + ".json")));
   }

   public final boolean create(ConfigManager.Config config) {
      if (!config.getFile().exists()) {
         try {
            if (config.getFile().createNewFile()) {
               this.add(config.getName());
               config.save();
               return true;
            }
         } catch (IOException var3) {
            return false;
         }
      }

      return false;
   }

   public final boolean load(String name) {
      Iterator var2 = this.configs.iterator();

      ConfigManager.Config config;
      do {
         if (!var2.hasNext()) {
            Logger.log("Failed to load config " + name);
            return false;
         }

         config = (ConfigManager.Config)var2.next();
      } while(!config.getName().equalsIgnoreCase(name));

      config.load();
      return true;
   }

   public final boolean save(String name) {
      if (this.configs.contains(this.get(name))) {
         Iterator var2 = this.configs.iterator();

         ConfigManager.Config config;
         do {
            if (!var2.hasNext()) {
               Logger.log("Failed to save config " + name);
               return false;
            }

            config = (ConfigManager.Config)var2.next();
         } while(!config.getName().equalsIgnoreCase(name));

         config.save();
         return true;
      } else {
         return this.create(new ConfigManager.Config(name, new File(this.directory, name + ".json")));
      }
   }

   public final boolean delete(String name) {
      if (this.configs.contains(this.get(name))) {
         Iterator var2 = this.configs.iterator();

         while(var2.hasNext()) {
            ConfigManager.Config config = (ConfigManager.Config)var2.next();
            if (config.getName().equalsIgnoreCase(name) && config.getFile().exists()) {
               this.configs.remove(config);
               return config.getFile().delete();
            }
         }
      }

      Logger.log("Failed to delete config " + name);
      return false;
   }

   public final ConfigManager.Config get(String name) {
      Iterator var2 = this.configs.iterator();

      ConfigManager.Config config;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         config = (ConfigManager.Config)var2.next();
      } while(!config.getName().equalsIgnoreCase(name));

      return config;
   }

   public static class Config {
      private final String name;
      private final File file;

      public Config(String name, File file) {
         this.name = name;
         this.file = file;
      }

      public String getName() {
         return this.name;
      }

      public File getFile() {
         return this.file;
      }

      public void save() {
         JsonObject js = new JsonObject();

         Module module;
         JsonObject jsf;
         for(UnmodifiableIterator var2 = Autumn.MANAGER_REGISTRY.moduleManager.getModules().iterator(); var2.hasNext(); js.add(module.getLabel(), jsf)) {
            module = (Module)var2.next();
            jsf = new JsonObject();
            jsf.addProperty("Enabled", module.isEnabled());
            jsf.addProperty("Hidden", module.isHidden());
            if (module.getOptions() != null) {
               JsonObject optionsObject = new JsonObject();
               Iterator var6 = module.getOptions().iterator();

               while(var6.hasNext()) {
                  Option option = (Option)var6.next();
                  if (option instanceof DoubleOption) {
                     optionsObject.addProperty(option.getLabel(), (Number)option.getValue());
                  } else if (option instanceof BoolOption) {
                     optionsObject.addProperty(option.getLabel(), ((BoolOption)option).getValue());
                  } else if (option instanceof EnumOption) {
                     optionsObject.addProperty(option.getLabel(), ((Enum)option.getValue()).name());
                  }
               }

               jsf.add("Options", optionsObject);
            }
         }

         Autumn.MANAGER_REGISTRY.fileManager.write(this.getFile(), (new GsonBuilder()).setPrettyPrinting().create().toJson(js));
      }

      public void load() {
         File file = this.getFile();

         try {
            Reader reader = new FileReader(file.toPath().toFile());
            Throwable var3 = null;

            try {
               JsonObject object = (new JsonParser()).parse(reader).getAsJsonObject();
               UnmodifiableIterator var5 = Autumn.MANAGER_REGISTRY.moduleManager.getModules().iterator();

               while(var5.hasNext()) {
                  Module module = (Module)var5.next();
                  if (object.has(module.getLabel())) {
                     JsonObject featureObject = object.get(module.getLabel()).getAsJsonObject();
                     if (featureObject.has("Enabled")) {
                        module.setEnabled(featureObject.get("Enabled").getAsBoolean());
                     }

                     if (featureObject.has("Hidden")) {
                        module.setHidden(featureObject.get("Hidden").getAsBoolean());
                     }

                     if (featureObject.has("Options")) {
                        featureObject.get("Options").getAsJsonObject().entrySet().forEach((entry) -> {
                           Option option = module.getOptionByLabel((String)entry.getKey());
                           if (option instanceof BoolOption) {
                              option.setValue(((JsonElement)entry.getValue()).getAsBoolean());
                           } else if (option instanceof EnumOption) {
                              option.setValue(((EnumOption)option).getValueOrNull(((JsonElement)entry.getValue()).getAsString()));
                           } else if (option instanceof DoubleOption) {
                              try {
                                 option.setValue(Double.parseDouble(((JsonElement)entry.getValue()).getAsString()));
                              } catch (NumberFormatException var4) {
                                 Logger.log("Failed to load config!");
                              }
                           }

                        });
                     }
                  }
               }
            } catch (Throwable var16) {
               var3 = var16;
               throw var16;
            } finally {
               if (reader != null) {
                  if (var3 != null) {
                     try {
                        reader.close();
                     } catch (Throwable var15) {
                        var3.addSuppressed(var15);
                     }
                  } else {
                     reader.close();
                  }
               }

            }
         } catch (IOException var18) {
         }

      }
   }
}
