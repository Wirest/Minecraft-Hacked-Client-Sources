package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import saint.comandstuff.commands.Ghost;
import saint.filestuff.BasicFile;

public class GhostModeFile extends BasicFile {
   public GhostModeFile() {
      super("ghostmodefile");
   }

   public void loadFile() {
      try {
         BufferedReader reader;
         String line;
         String[] arguments;
         for(reader = new BufferedReader(new FileReader(this.getFile())); (line = reader.readLine()) != null; Ghost.shouldGhost = Boolean.parseBoolean(arguments[1])) {
            arguments = line.split(":");
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
         writer.write("ghost:" + Ghost.shouldGhost);
         writer.newLine();
         writer.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
