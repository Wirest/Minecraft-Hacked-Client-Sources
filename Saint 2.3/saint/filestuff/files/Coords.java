package saint.filestuff.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.modstuff.modules.ClaimFinder;

public class Coords extends BasicFile {
   public Coords() {
      super("coords");
   }

   public void saveFile() {
      try {
         ClaimFinder claimfinder = (ClaimFinder)Saint.getModuleManager().getModuleUsingName("claimfinder");
         if (claimfinder == null) {
            return;
         }

         BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFile()));
         Iterator var4 = claimfinder.getMap().iterator();

         while(var4.hasNext()) {
            String line = (String)var4.next();
            writer.write(line);
            writer.newLine();
         }

         writer.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void loadFile() {
   }
}
