package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import saint.Saint;
import saint.filestuff.BasicFile;

public class FriendFile extends BasicFile {
   public FriendFile() {
      super("friendfile");
   }

   public void loadFile() {
      try {
         BufferedReader reader = new BufferedReader(new FileReader(this.getFile()));

         String line;
         while((line = reader.readLine()) != null) {
            String[] arguments = line.split(":");
            Saint.getFriendManager().addFriend(arguments[0], arguments[1]);
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
         Iterator var3 = Saint.getFriendManager().getContents().keySet().iterator();

         while(var3.hasNext()) {
            String name = (String)var3.next();
            writer.write(name + ":" + (String)Saint.getFriendManager().getContents().get(name));
            writer.newLine();
         }

         writer.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }
}
