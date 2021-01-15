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
import saint.modstuff.modules.Macros;
import saint.modstuff.modules.addons.Macro;

public class MacrosFile extends BasicFile {
   public MacrosFile() {
      super("macros");
   }

   public void loadFile() {
      try {
         Macros macros = (Macros)Saint.getModuleManager().getModuleUsingName("macros");
         if (macros == null) {
            return;
         }

         BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

         String line;
         while((line = reader.readLine()) != null) {
            String[] arguments = line.split(":");
            if (arguments.length == 2) {
               String command = arguments[0];
               int key = Keyboard.getKeyIndex(arguments[1]);
               macros.getMacros().add(new Macro(command, key));
            }
         }

         reader.close();
      } catch (FileNotFoundException var7) {
         var7.printStackTrace();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         Macros macros = (Macros)Saint.getModuleManager().getModuleUsingName("macros");
         if (macros == null) {
            return;
         }

         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var4 = macros.getMacros().iterator();

         while(var4.hasNext()) {
            Macro macro = (Macro)var4.next();
            String command = macro.getCommand();
            String key = Keyboard.getKeyName(macro.getKey());
            writer.write(command + ":" + key);
            writer.newLine();
         }

         writer.close();
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }
}
