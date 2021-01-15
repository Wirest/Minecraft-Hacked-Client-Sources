package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.utilities.Alt;

public class LastAlt extends BasicFile {
   public LastAlt() {
      super("lastalt");
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
                     Saint.getAltManager().setLastAlt(new Alt(account[0], account[1], account[0]));
                  } else {
                     String pw = account[1];

                     for(int i = 2; i < account.length; ++i) {
                        pw = pw + ":" + account[i];
                     }

                     Saint.getAltManager().setLastAlt(new Alt(account[0], pw, account[0]));
                  }
               } else {
                  account = s.split(":");
                  if (account.length == 1) {
                     Saint.getAltManager().setLastAlt(new Alt(account[0], ""));
                  } else if (account.length == 2) {
                     Saint.getAltManager().setLastAlt(new Alt(account[0], account[1]));
                  } else {
                     String pw = account[1];

                     for(int i = 2; i < account.length; ++i) {
                        pw = pw + ":" + account[i];
                     }

                     Saint.getAltManager().setLastAlt(new Alt(account[0], pw));
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
      }

   }

   public void saveFile() {
      try {
         PrintWriter printWriter = new PrintWriter(this.getFile());
         Alt alt = Saint.getAltManager().getLastAlt();
         if (alt != null) {
            if (alt.getMask().equals("")) {
               printWriter.println(alt.getUsername() + ":" + alt.getPassword());
            } else {
               printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
            }
         }

         printWriter.close();
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
      }

   }
}
