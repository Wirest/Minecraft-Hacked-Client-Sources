/*    */ package net.minecraft.util;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class ResourceLocation
/*    */ {
/*    */   protected final String resourceDomain;
/*    */   protected final String resourcePath;
/*    */   
/*    */   protected ResourceLocation(int p_i45928_1_, String... resourceName)
/*    */   {
/* 12 */     this.resourceDomain = (StringUtils.isEmpty(resourceName[0]) ? "minecraft" : resourceName[0].toLowerCase());
/* 13 */     this.resourcePath = resourceName[1];
/* 14 */     org.apache.commons.lang3.Validate.notNull(this.resourcePath);
/*    */   }
/*    */   
/*    */   public ResourceLocation(String resourceName)
/*    */   {
/* 19 */     this(0, splitObjectName(resourceName));
/*    */   }
/*    */   
/*    */   public ResourceLocation(String resourceDomainIn, String resourcePathIn)
/*    */   {
/* 24 */     this(0, new String[] { resourceDomainIn, resourcePathIn });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected static String[] splitObjectName(String toSplit)
/*    */   {
/* 33 */     String[] astring = { 0, toSplit };
/* 34 */     int i = toSplit.indexOf(':');
/*    */     
/* 36 */     if (i >= 0)
/*    */     {
/* 38 */       astring[1] = toSplit.substring(i + 1, toSplit.length());
/*    */       
/* 40 */       if (i > 1)
/*    */       {
/* 42 */         astring[0] = toSplit.substring(0, i);
/*    */       }
/*    */     }
/*    */     
/* 46 */     return astring;
/*    */   }
/*    */   
/*    */   public String getResourcePath()
/*    */   {
/* 51 */     return this.resourcePath;
/*    */   }
/*    */   
/*    */   public String getResourceDomain()
/*    */   {
/* 56 */     return this.resourceDomain;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 61 */     return this.resourceDomain + ':' + this.resourcePath;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 66 */     if (this == p_equals_1_)
/*    */     {
/* 68 */       return true;
/*    */     }
/* 70 */     if (!(p_equals_1_ instanceof ResourceLocation))
/*    */     {
/* 72 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 76 */     ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
/* 77 */     return (this.resourceDomain.equals(resourcelocation.resourceDomain)) && (this.resourcePath.equals(resourcelocation.resourcePath));
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 83 */     return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\ResourceLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */