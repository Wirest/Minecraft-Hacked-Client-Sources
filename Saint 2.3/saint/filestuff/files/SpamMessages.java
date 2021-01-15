package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.modstuff.modules.Spammer;

public class SpamMessages extends BasicFile {
   public SpamMessages() {
      super("spam");
   }

   public void loadFile() {
      try {
         Spammer spammer = (Spammer)Saint.getModuleManager().getModuleUsingName("spammer");
         if (spammer == null) {
            return;
         }

         BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

         String line;
         while((line = reader.readLine()) != null) {
            if (!line.equals("")) {
               line.replaceAll("\r", "");
               line.replaceAll("\n", "");
               spammer.getMessages().add(line);
            }
         }

         reader.close();
      } catch (FileNotFoundException var4) {
         var4.printStackTrace();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         writer.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
