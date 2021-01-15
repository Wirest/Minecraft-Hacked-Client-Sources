package saint.comandstuff.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class Resolve extends Command {
   public Resolve() {
      super("resolve", "<skype-name>", "skyperesolve", "res");
   }

   public void run(String message) {
      String username = message.split(" ")[1];

      try {
         URL url = new URL("http://api.predator.wtf/resolver/?arguments=" + username);
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
         connection.setRequestMethod("GET");
         connection.setConnectTimeout(5000);
         connection.connect();
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String line = reader.readLine();
         Logger.writeChat("API Response: " + line);
      } catch (MalformedURLException var7) {
         Logger.writeChat("Unknown error..");
         var7.printStackTrace();
      } catch (IOException var8) {
         Logger.writeChat("Unknown error..");
         var8.printStackTrace();
      }

   }
}
