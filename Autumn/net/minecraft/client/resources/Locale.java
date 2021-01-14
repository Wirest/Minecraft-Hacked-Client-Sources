package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Locale {
   private static final Splitter splitter = Splitter.on('=').limit(2);
   private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
   Map properties = Maps.newHashMap();
   private boolean unicode;

   public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List p_135022_2_) {
      this.properties.clear();
      Iterator var3 = p_135022_2_.iterator();

      while(var3.hasNext()) {
         String s = (String)var3.next();
         String s1 = String.format("lang/%s.lang", s);
         Iterator var6 = resourceManager.getResourceDomains().iterator();

         while(var6.hasNext()) {
            String s2 = (String)var6.next();

            try {
               this.loadLocaleData(resourceManager.getAllResources(new ResourceLocation(s2, s1)));
            } catch (IOException var9) {
            }
         }
      }

      this.checkUnicode();
   }

   public boolean isUnicode() {
      return this.unicode;
   }

   private void checkUnicode() {
      this.unicode = false;
      int i = 0;
      int j = 0;
      Iterator var3 = this.properties.values().iterator();

      while(var3.hasNext()) {
         String s = (String)var3.next();
         int k = s.length();
         j += k;

         for(int l = 0; l < k; ++l) {
            if (s.charAt(l) >= 256) {
               ++i;
            }
         }
      }

      float f = (float)i / (float)j;
      this.unicode = (double)f > 0.1D;
   }

   private void loadLocaleData(List p_135028_1_) throws IOException {
      Iterator var2 = p_135028_1_.iterator();

      while(var2.hasNext()) {
         IResource iresource = (IResource)var2.next();
         InputStream inputstream = iresource.getInputStream();

         try {
            this.loadLocaleData(inputstream);
         } finally {
            IOUtils.closeQuietly(inputstream);
         }
      }

   }

   private void loadLocaleData(InputStream p_135021_1_) throws IOException {
      Iterator var2 = IOUtils.readLines(p_135021_1_, Charsets.UTF_8).iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         if (!s.isEmpty() && s.charAt(0) != '#') {
            String[] astring = (String[])((String[])Iterables.toArray(splitter.split(s), String.class));
            if (astring != null && astring.length == 2) {
               String s1 = astring[0];
               String s2 = pattern.matcher(astring[1]).replaceAll("%$1s");
               this.properties.put(s1, s2);
            }
         }
      }

   }

   private String translateKeyPrivate(String p_135026_1_) {
      String s = (String)this.properties.get(p_135026_1_);
      return s == null ? p_135026_1_ : s;
   }

   public String formatMessage(String translateKey, Object[] parameters) {
      String s = this.translateKeyPrivate(translateKey);

      try {
         return String.format(s, parameters);
      } catch (IllegalFormatException var5) {
         return "Format error: " + s;
      }
   }
}
