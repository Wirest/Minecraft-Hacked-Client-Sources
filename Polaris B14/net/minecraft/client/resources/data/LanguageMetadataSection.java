/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import net.minecraft.client.resources.Language;
/*    */ 
/*    */ public class LanguageMetadataSection implements IMetadataSection
/*    */ {
/*    */   private final Collection<Language> languages;
/*    */   
/*    */   public LanguageMetadataSection(Collection<Language> p_i1311_1_)
/*    */   {
/* 12 */     this.languages = p_i1311_1_;
/*    */   }
/*    */   
/*    */   public Collection<Language> getLanguages()
/*    */   {
/* 17 */     return this.languages;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\LanguageMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */