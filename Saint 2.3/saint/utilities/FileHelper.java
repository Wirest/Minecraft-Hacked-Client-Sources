package saint.utilities;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileHelper {
   public static String percentage = "";

   public static void download(String remotePath, String localPath) {
      BufferedInputStream in = null;
      FileOutputStream out = null;

      try {
         URL url = new URL(remotePath);
         URLConnection conn = url.openConnection();
         int size = conn.getContentLength();
         if (size < 0) {
            System.out.println("Could not get the file size");
         } else {
            System.out.println("File size: " + size);
         }

         in = new BufferedInputStream(url.openStream());
         out = new FileOutputStream(localPath);
         byte[] data = new byte[1024];
         double sumCount = 0.0D;

         int count;
         while((count = in.read(data, 0, 1024)) != -1) {
            out.write(data, 0, count);
            sumCount += (double)count;
            if (size > 0) {
               percentage = sumCount / (double)size * 100.0D + "%";

               try {
                  Thread.sleep(1L);
               } catch (InterruptedException var28) {
                  var28.printStackTrace();
               }
            }
         }
      } catch (MalformedURLException var29) {
         var29.printStackTrace();
      } catch (IOException var30) {
         var30.printStackTrace();
      } finally {
         if (in != null) {
            try {
               in.close();
            } catch (IOException var27) {
               var27.printStackTrace();
            }
         }

         if (out != null) {
            try {
               out.close();
            } catch (IOException var26) {
               var26.printStackTrace();
            }
         }

      }

   }
}
