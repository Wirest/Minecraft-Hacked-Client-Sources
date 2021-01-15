/*    */ package net.minecraft.block.properties;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class PropertyInteger extends PropertyHelper<Integer>
/*    */ {
/*    */   private final ImmutableSet<Integer> allowedValues;
/*    */   
/*    */   protected PropertyInteger(String name, int min, int max)
/*    */   {
/* 14 */     super(name, Integer.class);
/*    */     
/* 16 */     if (min < 0)
/*    */     {
/* 18 */       throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
/*    */     }
/* 20 */     if (max <= min)
/*    */     {
/* 22 */       throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
/*    */     }
/*    */     
/*    */ 
/* 26 */     Set<Integer> set = Sets.newHashSet();
/*    */     
/* 28 */     for (int i = min; i <= max; i++)
/*    */     {
/* 30 */       set.add(Integer.valueOf(i));
/*    */     }
/*    */     
/* 33 */     this.allowedValues = ImmutableSet.copyOf(set);
/*    */   }
/*    */   
/*    */ 
/*    */   public Collection<Integer> getAllowedValues()
/*    */   {
/* 39 */     return this.allowedValues;
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 44 */     if (this == p_equals_1_)
/*    */     {
/* 46 */       return true;
/*    */     }
/* 48 */     if ((p_equals_1_ != null) && (getClass() == p_equals_1_.getClass()))
/*    */     {
/* 50 */       if (!super.equals(p_equals_1_))
/*    */       {
/* 52 */         return false;
/*    */       }
/*    */       
/*    */ 
/* 56 */       PropertyInteger propertyinteger = (PropertyInteger)p_equals_1_;
/* 57 */       return this.allowedValues.equals(propertyinteger.allowedValues);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 62 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 68 */     int i = super.hashCode();
/* 69 */     i = 31 * i + this.allowedValues.hashCode();
/* 70 */     return i;
/*    */   }
/*    */   
/*    */   public static PropertyInteger create(String name, int min, int max)
/*    */   {
/* 75 */     return new PropertyInteger(name, min, max);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName(Integer value)
/*    */   {
/* 83 */     return value.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\properties\PropertyInteger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */