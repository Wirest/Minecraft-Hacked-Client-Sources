package javassist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

final class DirClassPath implements ClassPath {
   String directory;

   DirClassPath(String dirName) {
      this.directory = dirName;
   }

   public InputStream openClassfile(String classname) {
      try {
         char sep = File.separatorChar;
         String filename = this.directory + sep + classname.replace('.', sep) + ".class";
         return new FileInputStream(filename.toString());
      } catch (FileNotFoundException var4) {
      } catch (SecurityException var5) {
      }

      return null;
   }

   public URL find(String classname) {
      char sep = File.separatorChar;
      String filename = this.directory + sep + classname.replace('.', sep) + ".class";
      File f = new File(filename);
      if (f.exists()) {
         try {
            return f.getCanonicalFile().toURI().toURL();
         } catch (MalformedURLException var6) {
         } catch (IOException var7) {
         }
      }

      return null;
   }

   public void close() {
   }

   public String toString() {
      return this.directory;
   }
}
