package saint.modstuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;
import org.reflections.Reflections;
import saint.utilities.ListManager;
import saint.utilities.Logger;

public final class ModManager extends ListManager {
   public final Module getModuleUsingName(String name) {
      if (this.contents == null) {
         return null;
      } else {
         try {
            Iterator var3 = this.contents.iterator();

            while(var3.hasNext()) {
               Module mod = (Module)var3.next();
               if (mod.getName().equalsIgnoreCase(name)) {
                  return mod;
               }
            }
         } catch (ConcurrentModificationException var4) {
         }

         return null;
      }
   }

   public void setup() {
      Logger.writeConsole("Starting to load up the modules.");
      this.contents = new ArrayList();
      Reflections reflect = new Reflections(new Object[]{Module.class});
      Set mods = reflect.getSubTypesOf(Module.class);
      Iterator var4 = mods.iterator();

      while(var4.hasNext()) {
         Class clazz = (Class)var4.next();

         try {
            Module mod = (Module)clazz.newInstance();
            this.getContentList().add(mod);
            Logger.writeConsole("Loaded mod \"" + mod.getName() + "\"");
         } catch (InstantiationException var6) {
            var6.printStackTrace();
            Logger.writeConsole("Failed to load mod \"" + clazz.getSimpleName() + "\" (InstantiationException)");
         } catch (IllegalAccessException var7) {
            var7.printStackTrace();
            Logger.writeConsole("Failed to load mod \"" + clazz.getSimpleName() + "\" (IllegalAccessException)");
         }
      }

      Collections.sort(this.contents, new Comparator() {
         public int compare(Module mod1, Module mod2) {
            return mod1.getName().compareTo(mod2.getName());
         }
      });
      Logger.writeConsole("Successfully loaded " + this.getContentList().size() + " mods.");
   }

   public static enum Category {
      EXPLOITS("Exploits"),
      MOVEMENT("Movement"),
      PLAYER("Player"),
      WORLD("World"),
      RENDER("Render"),
      COMBAT("Combat"),
      INVISIBLE("Miscellaneous");

      private String name;

      private Category(String name) {
         this.name = name;
      }

      public final String getName() {
         return this.name;
      }
   }
}
