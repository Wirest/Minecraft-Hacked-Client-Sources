package javassist.tools.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;

public class Viewer extends ClassLoader {
   private String server;
   private int port;

   public static void main(String[] args) throws Throwable {
      if (args.length >= 3) {
         Viewer cl = new Viewer(args[0], Integer.parseInt(args[1]));
         String[] args2 = new String[args.length - 3];
         System.arraycopy(args, 3, args2, 0, args.length - 3);
         cl.run(args[2], args2);
      } else {
         System.err.println("Usage: java javassist.tools.web.Viewer <host> <port> class [args ...]");
      }

   }

   public Viewer(String host, int p) {
      this.server = host;
      this.port = p;
   }

   public String getServer() {
      return this.server;
   }

   public int getPort() {
      return this.port;
   }

   public void run(String classname, String[] args) throws Throwable {
      Class c = this.loadClass(classname);

      try {
         c.getDeclaredMethod("main", String[].class).invoke((Object)null, args);
      } catch (InvocationTargetException var5) {
         throw var5.getTargetException();
      }
   }

   protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class c = this.findLoadedClass(name);
      if (c == null) {
         c = this.findClass(name);
      }

      if (c == null) {
         throw new ClassNotFoundException(name);
      } else {
         if (resolve) {
            this.resolveClass(c);
         }

         return c;
      }
   }

   protected Class findClass(String name) throws ClassNotFoundException {
      Class c = null;
      if (name.startsWith("java.") || name.startsWith("javax.") || name.equals("javassist.tools.web.Viewer")) {
         c = this.findSystemClass(name);
      }

      if (c == null) {
         try {
            byte[] b = this.fetchClass(name);
            if (b != null) {
               c = this.defineClass(name, b, 0, b.length);
            }
         } catch (Exception var4) {
         }
      }

      return c;
   }

   protected byte[] fetchClass(String classname) throws Exception {
      URL url = new URL("http", this.server, this.port, "/" + classname.replace('.', '/') + ".class");
      URLConnection con = url.openConnection();
      con.connect();
      int size = con.getContentLength();
      InputStream s = con.getInputStream();
      byte[] b;
      if (size <= 0) {
         b = this.readStream(s);
      } else {
         b = new byte[size];
         int len = 0;

         do {
            int n = s.read(b, len, size - len);
            if (n < 0) {
               s.close();
               throw new IOException("the stream was closed: " + classname);
            }

            len += n;
         } while(len < size);
      }

      s.close();
      return b;
   }

   private byte[] readStream(InputStream fin) throws IOException {
      byte[] buf = new byte[4096];
      int size = 0;
      int len = 0;

      byte[] result;
      do {
         size += len;
         if (buf.length - size <= 0) {
            result = new byte[buf.length * 2];
            System.arraycopy(buf, 0, result, 0, size);
            buf = result;
         }

         len = fin.read(buf, size, buf.length - size);
      } while(len >= 0);

      result = new byte[size];
      System.arraycopy(buf, 0, result, 0, size);
      return result;
   }
}
