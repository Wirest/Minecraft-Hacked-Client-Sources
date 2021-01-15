package saint.filestuff.files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import saint.Saint;
import saint.filestuff.BasicFile;
import saint.protection.Account;

public class SaintAccount extends BasicFile {
   public SaintAccount() {
      super("saintaccount");
   }

   public void loadFile() {
      try {
         BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));

         while(true) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
               String[] account = line.split(":");
               if (account.length == 2) {
                  Saint.setAccount(new Account(account[0], account[1]));
               } else {
                  String pw = account[1];

                  for(int i = 2; i < account.length; ++i) {
                     pw = pw + ":" + account[i];
                  }

                  Saint.setAccount(new Account(account[0], pw));
               }
            }

            bufferedReader.close();
            break;
         }
      } catch (FileNotFoundException var6) {
         var6.printStackTrace();
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public void saveFile() {
      try {
         PrintWriter printWriter = new PrintWriter(this.getFile());
         Account account = Saint.getAccount();
         if (account != null) {
            printWriter.println(account.getUsername() + ":" + account.getPassword());
         }

         printWriter.close();
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
      }

   }
}
