package saint.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;
import saint.Saint;
import saint.utilities.Logger;

public class NewestVersion extends Thread {
   public void run() {
      boolean var1 = false;

      try {
         new CopyOnWriteArrayList();
         URL url = new URL("http://www.andrewthehax0r.xyz/newstuff.html");

         String inputLine;
         for(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); (inputLine = in.readLine()) != null; in.close()) {
            if (inputLine.startsWith(String.valueOf(Saint.getVersion()))) {
               Logger.writeConsole("You are using the latest version of Saint 1.8!");
               Saint.setNewerVersionAvailable(false);
            } else {
               Logger.writeConsole("You are not using the latest version!");
               Saint.setNewerVersionAvailable(true);
            }
         }
      } catch (Exception var6) {
      }

   }
}
