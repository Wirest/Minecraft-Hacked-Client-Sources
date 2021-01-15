/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.SortedSet;
/*    */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*    */ import net.minecraft.client.resources.data.LanguageMetadataSection;
/*    */ import net.minecraft.util.StringTranslate;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LanguageManager implements IResourceManagerReloadListener
/*    */ {
/* 18 */   private static final Logger logger = ;
/*    */   private final IMetadataSerializer theMetadataSerializer;
/*    */   private String currentLanguage;
/* 21 */   protected static final Locale currentLocale = new Locale();
/* 22 */   private Map<String, Language> languageMap = Maps.newHashMap();
/*    */   
/*    */   public LanguageManager(IMetadataSerializer theMetadataSerializerIn, String currentLanguageIn)
/*    */   {
/* 26 */     this.theMetadataSerializer = theMetadataSerializerIn;
/* 27 */     this.currentLanguage = currentLanguageIn;
/* 28 */     I18n.setLocale(currentLocale);
/*    */   }
/*    */   
/*    */   public void parseLanguageMetadata(List<IResourcePack> p_135043_1_)
/*    */   {
/* 33 */     this.languageMap.clear();
/*    */     
/* 35 */     for (IResourcePack iresourcepack : p_135043_1_)
/*    */     {
/*    */       try
/*    */       {
/* 39 */         LanguageMetadataSection languagemetadatasection = (LanguageMetadataSection)iresourcepack.getPackMetadata(this.theMetadataSerializer, "language");
/*    */         
/* 41 */         if (languagemetadatasection != null)
/*    */         {
/* 43 */           for (Language language : languagemetadatasection.getLanguages())
/*    */           {
/* 45 */             if (!this.languageMap.containsKey(language.getLanguageCode()))
/*    */             {
/* 47 */               this.languageMap.put(language.getLanguageCode(), language);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */       catch (RuntimeException runtimeexception)
/*    */       {
/* 54 */         logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), runtimeexception);
/*    */       }
/*    */       catch (IOException ioexception)
/*    */       {
/* 58 */         logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), ioexception);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void onResourceManagerReload(IResourceManager resourceManager)
/*    */   {
/* 65 */     List<String> list = Lists.newArrayList(new String[] { "en_US" });
/*    */     
/* 67 */     if (!"en_US".equals(this.currentLanguage))
/*    */     {
/* 69 */       list.add(this.currentLanguage);
/*    */     }
/*    */     
/* 72 */     currentLocale.loadLocaleDataFiles(resourceManager, list);
/* 73 */     StringTranslate.replaceWith(currentLocale.properties);
/*    */   }
/*    */   
/*    */   public boolean isCurrentLocaleUnicode()
/*    */   {
/* 78 */     return currentLocale.isUnicode();
/*    */   }
/*    */   
/*    */   public boolean isCurrentLanguageBidirectional()
/*    */   {
/* 83 */     return (getCurrentLanguage() != null) && (getCurrentLanguage().isBidirectional());
/*    */   }
/*    */   
/*    */   public void setCurrentLanguage(Language currentLanguageIn)
/*    */   {
/* 88 */     this.currentLanguage = currentLanguageIn.getLanguageCode();
/*    */   }
/*    */   
/*    */   public Language getCurrentLanguage()
/*    */   {
/* 93 */     return this.languageMap.containsKey(this.currentLanguage) ? (Language)this.languageMap.get(this.currentLanguage) : (Language)this.languageMap.get("en_US");
/*    */   }
/*    */   
/*    */   public SortedSet<Language> getLanguages()
/*    */   {
/* 98 */     return Sets.newTreeSet(this.languageMap.values());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\LanguageManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */