package saint.filestuff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.reflections.Reflections;
import saint.utilities.ListManager;
import saint.utilities.Logger;

public class FileManager extends ListManager {
   public final BasicFile getFileUsingName(String name) {
      if (this.contents == null) {
         return null;
      } else {
         Iterator var3 = this.contents.iterator();

         while(var3.hasNext()) {
            BasicFile file = (BasicFile)var3.next();
            if (file.getName().equalsIgnoreCase(name)) {
               return file;
            }
         }

         return null;
      }
   }

   public void setup() {
      Logger.writeConsole("Started loading up the files.");
      this.contents = new ArrayList();
      Reflections reflect = new Reflections(new Object[]{BasicFile.class});
      Set files = reflect.getSubTypesOf(BasicFile.class);
      Iterator var4 = files.iterator();

      while(var4.hasNext()) {
         Class clazz = (Class)var4.next();

         try {
            BasicFile file = (BasicFile)clazz.newInstance();
            this.getContentList().add(file);
            file.loadFile();
            Logger.writeConsole("Loaded file \"" + file.getName() + "\"");
         } catch (InstantiationException var6) {
            var6.printStackTrace();
            Logger.writeConsole("Failed to load file \"" + clazz.getSimpleName() + "\" (InstantiationException)");
         } catch (IllegalAccessException var7) {
            var7.printStackTrace();
            Logger.writeConsole("Failed to load file \"" + clazz.getSimpleName() + "\" (IllegalAccessException)");
         }
      }

      Logger.writeConsole("Successfully loaded " + this.getContentList().size() + " files.");
   }
}
