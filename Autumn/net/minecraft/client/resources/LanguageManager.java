package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.util.StringTranslate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener {
   private static final Logger logger = LogManager.getLogger();
   private final IMetadataSerializer theMetadataSerializer;
   private String currentLanguage;
   protected static final Locale currentLocale = new Locale();
   private Map languageMap = Maps.newHashMap();

   public LanguageManager(IMetadataSerializer theMetadataSerializerIn, String currentLanguageIn) {
      this.theMetadataSerializer = theMetadataSerializerIn;
      this.currentLanguage = currentLanguageIn;
      I18n.setLocale(currentLocale);
   }

   public void parseLanguageMetadata(List p_135043_1_) {
      this.languageMap.clear();
      Iterator var2 = p_135043_1_.iterator();

      while(var2.hasNext()) {
         IResourcePack iresourcepack = (IResourcePack)var2.next();

         try {
            LanguageMetadataSection languagemetadatasection = (LanguageMetadataSection)iresourcepack.getPackMetadata(this.theMetadataSerializer, "language");
            if (languagemetadatasection != null) {
               Iterator var5 = languagemetadatasection.getLanguages().iterator();

               while(var5.hasNext()) {
                  Language language = (Language)var5.next();
                  if (!this.languageMap.containsKey(language.getLanguageCode())) {
                     this.languageMap.put(language.getLanguageCode(), language);
                  }
               }
            }
         } catch (RuntimeException var7) {
            logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), var7);
         } catch (IOException var8) {
            logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), var8);
         }
      }

   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      List list = Lists.newArrayList(new String[]{"en_US"});
      if (!"en_US".equals(this.currentLanguage)) {
         list.add(this.currentLanguage);
      }

      currentLocale.loadLocaleDataFiles(resourceManager, list);
      StringTranslate.replaceWith(currentLocale.properties);
   }

   public boolean isCurrentLocaleUnicode() {
      return currentLocale.isUnicode();
   }

   public boolean isCurrentLanguageBidirectional() {
      return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
   }

   public void setCurrentLanguage(Language currentLanguageIn) {
      this.currentLanguage = currentLanguageIn.getLanguageCode();
   }

   public Language getCurrentLanguage() {
      return this.languageMap.containsKey(this.currentLanguage) ? (Language)this.languageMap.get(this.currentLanguage) : (Language)this.languageMap.get("en_US");
   }

   public SortedSet getLanguages() {
      return Sets.newTreeSet(this.languageMap.values());
   }
}
