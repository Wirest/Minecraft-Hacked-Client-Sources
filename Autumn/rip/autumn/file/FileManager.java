package rip.autumn.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public final class FileManager {
   public final List files = new ArrayList();
   public static final File HOME_DIRECTORY = new File("Autumn");
   private final List directoryList;

   public FileManager() {
      this.files.add(new File(HOME_DIRECTORY, "ModuleData.json"));
      this.directoryList = new ArrayList(Arrays.asList(new File(HOME_DIRECTORY, "Configs")));
      HOME_DIRECTORY.mkdirs();
      Iterator var1 = this.directoryList.iterator();

      File f;
      while(var1.hasNext()) {
         f = (File)var1.next();
         if (!f.exists() && !f.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + f.getName());
         }
      }

      var1 = this.files.iterator();

      while(var1.hasNext()) {
         f = (File)var1.next();

         try {
            if (!f.exists() && !f.createNewFile()) {
               throw new IOException("Failed to create file: " + f.getName());
            }
         } catch (IOException var4) {
         }
      }

   }

   public final String read(File file) {
      StringBuilder sb = new StringBuilder();
      if (file.exists()) {
         try {
            Stream lines = Files.lines(file.toPath(), StandardCharsets.UTF_8);
            Throwable var4 = null;

            try {
               Iterator it = lines.iterator();

               while(it.hasNext()) {
                  sb.append((String)it.next()).append(System.lineSeparator());
               }
            } catch (Throwable var14) {
               var4 = var14;
               throw var14;
            } finally {
               if (lines != null) {
                  if (var4 != null) {
                     try {
                        lines.close();
                     } catch (Throwable var13) {
                        var4.addSuppressed(var13);
                     }
                  } else {
                     lines.close();
                  }
               }

            }
         } catch (IOException var16) {
         }
      }

      return sb.toString();
   }

   public final void write(File file, String content) {
      if (file.exists()) {
         try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
         } catch (IOException var4) {
         }

      } else {
         throw new NullPointerException("File " + file.getName() + " does not exist!");
      }
   }

   public File getFile(String name) {
      Iterator var2 = this.files.iterator();

      File f;
      do {
         if (!var2.hasNext()) {
            throw new NullPointerException("Cannot find file!");
         }

         f = (File)var2.next();
      } while(!f.getName().equalsIgnoreCase(name));

      return f;
   }

   public File getDirectory(String name) {
      Iterator var2 = this.directoryList.iterator();

      File f;
      do {
         if (!var2.hasNext()) {
            throw new NullPointerException("Cannot find directory!");
         }

         f = (File)var2.next();
      } while(!f.getName().equalsIgnoreCase(name));

      return f;
   }
}
