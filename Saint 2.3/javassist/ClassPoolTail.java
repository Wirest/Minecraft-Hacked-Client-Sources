package javassist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

final class ClassPoolTail {
   protected ClassPathList pathList = null;

   public ClassPoolTail() {
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("[class path: ");

      for(ClassPathList list = this.pathList; list != null; list = list.next) {
         buf.append(list.path.toString());
         buf.append(File.pathSeparatorChar);
      }

      buf.append(']');
      return buf.toString();
   }

   public synchronized ClassPath insertClassPath(ClassPath cp) {
      this.pathList = new ClassPathList(cp, this.pathList);
      return cp;
   }

   public synchronized ClassPath appendClassPath(ClassPath cp) {
      ClassPathList tail = new ClassPathList(cp, (ClassPathList)null);
      ClassPathList list = this.pathList;
      if (list == null) {
         this.pathList = tail;
      } else {
         while(true) {
            if (list.next == null) {
               list.next = tail;
               break;
            }

            list = list.next;
         }
      }

      return cp;
   }

   public synchronized void removeClassPath(ClassPath cp) {
      ClassPathList list = this.pathList;
      if (list != null) {
         if (list.path == cp) {
            this.pathList = list.next;
         } else {
            while(list.next != null) {
               if (list.next.path == cp) {
                  list.next = list.next.next;
               } else {
                  list = list.next;
               }
            }
         }
      }

      cp.close();
   }

   public ClassPath appendSystemPath() {
      return this.appendClassPath((ClassPath)(new ClassClassPath()));
   }

   public ClassPath insertClassPath(String pathname) throws NotFoundException {
      return this.insertClassPath(makePathObject(pathname));
   }

   public ClassPath appendClassPath(String pathname) throws NotFoundException {
      return this.appendClassPath(makePathObject(pathname));
   }

   private static ClassPath makePathObject(String pathname) throws NotFoundException {
      String lower = pathname.toLowerCase();
      if (!lower.endsWith(".jar") && !lower.endsWith(".zip")) {
         int len = pathname.length();
         if (len <= 2 || pathname.charAt(len - 1) != '*' || pathname.charAt(len - 2) != '/' && pathname.charAt(len - 2) != File.separatorChar) {
            return new DirClassPath(pathname);
         } else {
            String dir = pathname.substring(0, len - 2);
            return new JarDirClassPath(dir);
         }
      } else {
         return new JarClassPath(pathname);
      }
   }

   void writeClassfile(String classname, OutputStream out) throws NotFoundException, IOException, CannotCompileException {
      InputStream fin = this.openClassfile(classname);
      if (fin == null) {
         throw new NotFoundException(classname);
      } else {
         try {
            copyStream(fin, out);
         } finally {
            fin.close();
         }

      }
   }

   InputStream openClassfile(String classname) throws NotFoundException {
      ClassPathList list = this.pathList;
      InputStream ins = null;

      NotFoundException error;
      for(error = null; list != null; list = list.next) {
         try {
            ins = list.path.openClassfile(classname);
         } catch (NotFoundException var6) {
            if (error == null) {
               error = var6;
            }
         }

         if (ins != null) {
            return ins;
         }
      }

      if (error != null) {
         throw error;
      } else {
         return null;
      }
   }

   public URL find(String classname) {
      ClassPathList list = this.pathList;

      for(URL url = null; list != null; list = list.next) {
         url = list.path.find(classname);
         if (url != null) {
            return url;
         }
      }

      return null;
   }

   public static byte[] readStream(InputStream fin) throws IOException {
      byte[][] bufs = new byte[8][];
      int bufsize = 4096;

      for(int i = 0; i < 8; ++i) {
         bufs[i] = new byte[bufsize];
         int size = 0;
         boolean var5 = false;

         do {
            int len = fin.read(bufs[i], size, bufsize - size);
            if (len < 0) {
               byte[] result = new byte[bufsize - 4096 + size];
               int s = 0;

               for(int j = 0; j < i; ++j) {
                  System.arraycopy(bufs[j], 0, result, s, s + 4096);
                  s = s + s + 4096;
               }

               System.arraycopy(bufs[i], 0, result, s, size);
               return result;
            }

            size += len;
         } while(size < bufsize);

         bufsize *= 2;
      }

      throw new IOException("too much data");
   }

   public static void copyStream(InputStream fin, OutputStream fout) throws IOException {
      int bufsize = 4096;
      byte[] buf = null;

      for(int i = 0; i < 64; ++i) {
         if (i < 8) {
            bufsize *= 2;
            buf = new byte[bufsize];
         }

         int size = 0;
         boolean var6 = false;

         do {
            int len = fin.read(buf, size, bufsize - size);
            if (len < 0) {
               fout.write(buf, 0, size);
               return;
            }

            size += len;
         } while(size < bufsize);

         fout.write(buf);
      }

      throw new IOException("too much data");
   }
}
