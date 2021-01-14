package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.resources.AbstractResourcePack;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class FolderResourcePack extends AbstractResourcePack {

   private static final String __OBFID = "CL_00001076";


   public FolderResourcePack(File p_i1291_1_) {
      super(p_i1291_1_);
   }

   protected InputStream func_110591_a(String p_110591_1_) throws IOException {
      return new BufferedInputStream(new FileInputStream(new File(this.field_110597_b, p_110591_1_)));
   }

   protected boolean func_110593_b(String p_110593_1_) {
      return (new File(this.field_110597_b, p_110593_1_)).isFile();
   }

   public Set func_110587_b() {
      HashSet var1 = Sets.newHashSet();
      File var2 = new File(this.field_110597_b, "assets/");
      if(var2.isDirectory()) {
         File[] var3 = var2.listFiles(DirectoryFileFilter.DIRECTORY);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            String var7 = func_110595_a(var2, var6);
            if(!var7.equals(var7.toLowerCase())) {
               this.func_110594_c(var7);
            } else {
               var1.add(var7.substring(0, var7.length() - 1));
            }
         }
      }

      return var1;
   }
}
