package org.reflections.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import org.reflections.ReflectionsException;

public class JarInputFile implements Vfs.File {
   private final ZipEntry entry;
   private final JarInputDir jarInputDir;
   private final long fromIndex;
   private final long endIndex;

   public JarInputFile(ZipEntry entry, JarInputDir jarInputDir, long cursor, long nextCursor) {
      this.entry = entry;
      this.jarInputDir = jarInputDir;
      this.fromIndex = cursor;
      this.endIndex = nextCursor;
   }

   public String getName() {
      String name = this.entry.getName();
      return name.substring(name.lastIndexOf("/") + 1);
   }

   public String getRelativePath() {
      return this.entry.getName();
   }

   public InputStream openInputStream() throws IOException {
      return new InputStream() {
         public int read() throws IOException {
            if (JarInputFile.this.jarInputDir.cursor >= JarInputFile.this.fromIndex && JarInputFile.this.jarInputDir.cursor <= JarInputFile.this.endIndex) {
               int read = JarInputFile.this.jarInputDir.jarInputStream.read();
               ++JarInputFile.this.jarInputDir.cursor;
               return read;
            } else {
               throw new ReflectionsException("could not read input stream, cursor beyond endInputStream");
            }
         }
      };
   }
}
