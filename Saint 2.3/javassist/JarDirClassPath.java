package javassist;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;

final class JarDirClassPath implements ClassPath {
   JarClassPath[] jars;

   JarDirClassPath(String dirName) throws NotFoundException {
      File[] files = (new File(dirName)).listFiles(new FilenameFilter() {
         public boolean accept(File dir, String name) {
            name = name.toLowerCase();
            return name.endsWith(".jar") || name.endsWith(".zip");
         }
      });
      if (files != null) {
         this.jars = new JarClassPath[files.length];

         for(int i = 0; i < files.length; ++i) {
            this.jars[i] = new JarClassPath(files[i].getPath());
         }
      }

   }

   public InputStream openClassfile(String classname) throws NotFoundException {
      if (this.jars != null) {
         for(int i = 0; i < this.jars.length; ++i) {
            InputStream is = this.jars[i].openClassfile(classname);
            if (is != null) {
               return is;
            }
         }
      }

      return null;
   }

   public URL find(String classname) {
      if (this.jars != null) {
         for(int i = 0; i < this.jars.length; ++i) {
            URL url = this.jars[i].find(classname);
            if (url != null) {
               return url;
            }
         }
      }

      return null;
   }

   public void close() {
      if (this.jars != null) {
         for(int i = 0; i < this.jars.length; ++i) {
            this.jars[i].close();
         }
      }

   }
}
