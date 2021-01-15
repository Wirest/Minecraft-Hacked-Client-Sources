package javassist;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLClassPath implements ClassPath {
   protected String hostname;
   protected int port;
   protected String directory;
   protected String packageName;

   public URLClassPath(String host, int port, String directory, String packageName) {
      this.hostname = host;
      this.port = port;
      this.directory = directory;
      this.packageName = packageName;
   }

   public String toString() {
      return this.hostname + ":" + this.port + this.directory;
   }

   public InputStream openClassfile(String classname) {
      try {
         URLConnection con = this.openClassfile0(classname);
         if (con != null) {
            return con.getInputStream();
         }
      } catch (IOException var3) {
      }

      return null;
   }

   private URLConnection openClassfile0(String classname) throws IOException {
      if (this.packageName != null && !classname.startsWith(this.packageName)) {
         return null;
      } else {
         String jarname = this.directory + classname.replace('.', '/') + ".class";
         return fetchClass0(this.hostname, this.port, jarname);
      }
   }

   public URL find(String classname) {
      try {
         URLConnection con = this.openClassfile0(classname);
         InputStream is = con.getInputStream();
         if (is != null) {
            is.close();
            return con.getURL();
         }
      } catch (IOException var4) {
      }

      return null;
   }

   public void close() {
   }

   public static byte[] fetchClass(String host, int port, String directory, String classname) throws IOException {
      URLConnection con = fetchClass0(host, port, directory + classname.replace('.', '/') + ".class");
      int size = con.getContentLength();
      InputStream s = con.getInputStream();

      byte[] b;
      try {
         if (size <= 0) {
            b = ClassPoolTail.readStream(s);
         } else {
            b = new byte[size];
            int len = 0;

            do {
               int n = s.read(b, len, size - len);
               if (n < 0) {
                  throw new IOException("the stream was closed: " + classname);
               }

               len += n;
            } while(len < size);
         }
      } finally {
         s.close();
      }

      return b;
   }

   private static URLConnection fetchClass0(String host, int port, String filename) throws IOException {
      URL url;
      try {
         url = new URL("http", host, port, filename);
      } catch (MalformedURLException var5) {
         throw new IOException("invalid URL?");
      }

      URLConnection con = url.openConnection();
      con.connect();
      return con;
   }
}
