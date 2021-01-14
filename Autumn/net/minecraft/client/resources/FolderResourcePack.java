package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class FolderResourcePack extends AbstractResourcePack {
   public FolderResourcePack(File resourcePackFileIn) {
      super(resourcePackFileIn);
   }

   protected InputStream getInputStreamByName(String name) throws IOException {
      return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, name)));
   }

   protected boolean hasResourceName(String name) {
      return (new File(this.resourcePackFile, name)).isFile();
   }

   public Set getResourceDomains() {
      Set set = Sets.newHashSet();
      File file1 = new File(this.resourcePackFile, "assets/");
      if (file1.isDirectory()) {
         File[] var3 = file1.listFiles(DirectoryFileFilter.DIRECTORY);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File file2 = var3[var5];
            String s = getRelativeName(file1, file2);
            if (!s.equals(s.toLowerCase())) {
               this.logNameNotLowercase(s);
            } else {
               set.add(s.substring(0, s.length() - 1));
            }
         }
      }

      return set;
   }
}
