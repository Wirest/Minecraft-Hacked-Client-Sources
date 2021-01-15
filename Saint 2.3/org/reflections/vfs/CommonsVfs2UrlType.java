package org.reflections.vfs;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;

public interface CommonsVfs2UrlType {
   public static class File implements Vfs.File {
      private final FileObject root;
      private final FileObject file;

      public File(FileObject root, FileObject file) {
         this.root = root;
         this.file = file;
      }

      public String getName() {
         return this.file.getName().getBaseName();
      }

      public String getRelativePath() {
         String filepath = this.file.getName().getPath().replace("\\", "/");
         return filepath.startsWith(this.root.getName().getPath()) ? filepath.substring(this.root.getName().getPath().length() + 1) : null;
      }

      public InputStream openInputStream() throws IOException {
         return this.file.getContent().getInputStream();
      }
   }

   public static class Dir implements Vfs.Dir {
      private final FileObject file;

      public Dir(FileObject file) {
         this.file = file;
      }

      public String getPath() {
         try {
            return this.file.getURL().getPath();
         } catch (FileSystemException var2) {
            throw new RuntimeException(var2);
         }
      }

      public Iterable getFiles() {
         return new Iterable() {
            public Iterator iterator() {
               return Dir.this.new FileAbstractIterator();
            }
         };
      }

      public void close() {
         try {
            this.file.close();
         } catch (FileSystemException var2) {
         }

      }

      private class FileAbstractIterator extends AbstractIterator {
         final Stack stack;

         private FileAbstractIterator() {
            this.stack = new Stack();
            this.listDir(Dir.this.file);
         }

         protected Vfs.File computeNext() {
            while(true) {
               if (!this.stack.isEmpty()) {
                  FileObject file = (FileObject)this.stack.pop();

                  try {
                     if (this.isDir(file)) {
                        this.listDir(file);
                        continue;
                     }

                     return this.getFile(file);
                  } catch (FileSystemException var3) {
                     throw new RuntimeException(var3);
                  }
               }

               return (Vfs.File)this.endOfData();
            }
         }

         private CommonsVfs2UrlType.File getFile(FileObject file) {
            return new CommonsVfs2UrlType.File(Dir.this.file, file);
         }

         private boolean listDir(FileObject file) {
            return this.stack.addAll(this.listFiles(file));
         }

         private boolean isDir(FileObject file) throws FileSystemException {
            return file.getType() == FileType.FOLDER;
         }

         protected List listFiles(FileObject file) {
            try {
               FileObject[] files = file.getChildren();
               return files != null ? Lists.newArrayList(files) : new ArrayList();
            } catch (FileSystemException var3) {
               throw new RuntimeException(var3);
            }
         }

         // $FF: synthetic method
         FileAbstractIterator(Object x1) {
            this();
         }
      }
   }
}
