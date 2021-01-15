package saint.filestuff;

import java.io.File;
import saint.Saint;

public abstract class BasicFile {
   private final File file;
   private final String name;

   public BasicFile(String name) {
      this.name = name;
      this.file = new File(Saint.getDirectory(), name + ".saint");
      if (!this.file.exists()) {
         this.saveFile();
      }

   }

   public abstract void saveFile();

   public abstract void loadFile();

   public final String getName() {
      return this.name;
   }

   public final File getFile() {
      return this.file;
   }
}
