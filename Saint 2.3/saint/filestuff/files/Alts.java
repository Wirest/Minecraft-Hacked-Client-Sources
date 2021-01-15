package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.utilities.Alt;

public final class Alts extends BasicFile {
   public Alts() {
      super("alts");
   }

   public void loadFile() {
      try {
         BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));

         while(true) {
            String s;
            while((s = bufferedReader.readLine()) != null) {
               if (s.contains("\t")) {
                  s = s.replace("\t", "    ");
               }

               String[] account;
               if (s.contains("    ")) {
                  account = s.split("    ");
                  String[] account = account[1].split(":");
                  if (account.length == 2) {
                     Saint.getAltManager().getContentList().add(new Alt(account[0], account[1], account[0]));
                  } else {
                     String pw = account[1];

                     for(int i = 2; i < account.length; ++i) {
                        pw = pw + ":" + account[i];
                     }

                     Saint.getAltManager().getContentList().add(new Alt(account[0], pw, account[0]));
                  }
               } else {
                  account = s.split(":");
                  if (account.length == 1) {
                     Saint.getAltManager().getContentList().add(new Alt(account[0], ""));
                  } else if (account.length == 2) {
                     Saint.getAltManager().getContentList().add(new Alt(account[0], account[1]));
                  } else {
                     String pw = account[1];

                     for(int i = 2; i < account.length; ++i) {
                        pw = pw + ":" + account[i];
                     }

                     Saint.getAltManager().getContentList().add(new Alt(account[0], pw));
                  }
               }
            }

            bufferedReader.close();
            break;
         }
      } catch (FileNotFoundException var7) {
         var7.printStackTrace();
      } catch (IOException var8) {
         var8.printStackTrace();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         PrintWriter printWriter = new PrintWriter(this.getFile());
         Iterator var3 = Saint.getAltManager().getContentList().iterator();

         while(var3.hasNext()) {
            Alt alt = (Alt)var3.next();
            if (alt.getMask().equals("")) {
               printWriter.println(alt.getUsername() + ":" + alt.getPassword());
            } else {
               printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
            }
         }

         printWriter.close();
      } catch (FileNotFoundException var4) {
         var4.printStackTrace();
      }

   }
}
