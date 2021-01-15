package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.modstuff.Module;

public final class ModuleConfiguration extends BasicFile {
   public ModuleConfiguration() {
      super("moduleconfiguration");
   }

   public void saveFile() {
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var3 = Saint.getModuleManager().getContentList().iterator();

         while(var3.hasNext()) {
            Module mod = (Module)var3.next();
            writer.write(mod.getName().toLowerCase() + ":" + mod.isEnabled() + ":" + Keyboard.getKeyName(mod.getKeybind()) + ":" + mod.isVisible());
            writer.newLine();
         }

         writer.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public void loadFile() {
      try {
         BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

         String line;
         while((line = reader.readLine()) != null) {
            String[] arguments = line.split(":");
            if (arguments.length == 4) {
               Module mod = Saint.getModuleManager().getModuleUsingName(arguments[0]);
               if (mod != null) {
                  mod.setEnabled(Boolean.parseBoolean(arguments[1]));
                  mod.setKeybind(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                  mod.setVisible(Boolean.parseBoolean(arguments[3]));
               }
            }
         }

         reader.close();
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }
}
