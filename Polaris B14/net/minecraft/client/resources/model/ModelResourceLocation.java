/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class ModelResourceLocation extends ResourceLocation
/*    */ {
/*    */   private final String variant;
/*    */   
/*    */   protected ModelResourceLocation(int p_i46078_1_, String... p_i46078_2_)
/*    */   {
/* 12 */     super(0, new String[] { p_i46078_2_[0], p_i46078_2_[1] });
/* 13 */     this.variant = (StringUtils.isEmpty(p_i46078_2_[2]) ? "normal" : p_i46078_2_[2].toLowerCase());
/*    */   }
/*    */   
/*    */   public ModelResourceLocation(String p_i46079_1_)
/*    */   {
/* 18 */     this(0, parsePathString(p_i46079_1_));
/*    */   }
/*    */   
/*    */   public ModelResourceLocation(ResourceLocation p_i46080_1_, String p_i46080_2_)
/*    */   {
/* 23 */     this(p_i46080_1_.toString(), p_i46080_2_);
/*    */   }
/*    */   
/*    */   public ModelResourceLocation(String p_i46081_1_, String p_i46081_2_)
/*    */   {
/* 28 */     this(0, parsePathString(p_i46081_1_ + '#' + (p_i46081_2_ == null ? "normal" : p_i46081_2_)));
/*    */   }
/*    */   
/*    */   protected static String[] parsePathString(String p_177517_0_)
/*    */   {
/* 33 */     String[] astring = { 0, p_177517_0_ };
/* 34 */     int i = p_177517_0_.indexOf('#');
/* 35 */     String s = p_177517_0_;
/*    */     
/* 37 */     if (i >= 0)
/*    */     {
/* 39 */       astring[2] = p_177517_0_.substring(i + 1, p_177517_0_.length());
/*    */       
/* 41 */       if (i > 1)
/*    */       {
/* 43 */         s = p_177517_0_.substring(0, i);
/*    */       }
/*    */     }
/*    */     
/* 47 */     System.arraycopy(ResourceLocation.splitObjectName(s), 0, astring, 0, 2);
/* 48 */     return astring;
/*    */   }
/*    */   
/*    */   public String getVariant()
/*    */   {
/* 53 */     return this.variant;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 58 */     if (this == p_equals_1_)
/*    */     {
/* 60 */       return true;
/*    */     }
/* 62 */     if (((p_equals_1_ instanceof ModelResourceLocation)) && (super.equals(p_equals_1_)))
/*    */     {
/* 64 */       ModelResourceLocation modelresourcelocation = (ModelResourceLocation)p_equals_1_;
/* 65 */       return this.variant.equals(modelresourcelocation.variant);
/*    */     }
/*    */     
/*    */ 
/* 69 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 75 */     return 31 * super.hashCode() + this.variant.hashCode();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 80 */     return super.toString() + '#' + this.variant;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\model\ModelResourceLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */