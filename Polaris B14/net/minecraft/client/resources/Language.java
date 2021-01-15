/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ public class Language implements Comparable<Language>
/*    */ {
/*    */   private final String languageCode;
/*    */   private final String region;
/*    */   private final String name;
/*    */   private final boolean bidirectional;
/*    */   
/*    */   public Language(String languageCodeIn, String regionIn, String nameIn, boolean bidirectionalIn)
/*    */   {
/* 12 */     this.languageCode = languageCodeIn;
/* 13 */     this.region = regionIn;
/* 14 */     this.name = nameIn;
/* 15 */     this.bidirectional = bidirectionalIn;
/*    */   }
/*    */   
/*    */   public String getLanguageCode()
/*    */   {
/* 20 */     return this.languageCode;
/*    */   }
/*    */   
/*    */   public boolean isBidirectional()
/*    */   {
/* 25 */     return this.bidirectional;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 30 */     return String.format("%s (%s)", new Object[] { this.name, this.region });
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 35 */     return !(p_equals_1_ instanceof Language) ? false : this == p_equals_1_ ? true : this.languageCode.equals(((Language)p_equals_1_).languageCode);
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 40 */     return this.languageCode.hashCode();
/*    */   }
/*    */   
/*    */   public int compareTo(Language p_compareTo_1_)
/*    */   {
/* 45 */     return this.languageCode.compareTo(p_compareTo_1_.languageCode);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\Language.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */