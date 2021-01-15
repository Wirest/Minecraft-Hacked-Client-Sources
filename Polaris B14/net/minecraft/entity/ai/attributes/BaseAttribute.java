/*    */ package net.minecraft.entity.ai.attributes;
/*    */ 
/*    */ public abstract class BaseAttribute implements IAttribute
/*    */ {
/*    */   private final IAttribute field_180373_a;
/*    */   private final String unlocalizedName;
/*    */   private final double defaultValue;
/*    */   private boolean shouldWatch;
/*    */   
/*    */   protected BaseAttribute(IAttribute p_i45892_1_, String unlocalizedNameIn, double defaultValueIn)
/*    */   {
/* 12 */     this.field_180373_a = p_i45892_1_;
/* 13 */     this.unlocalizedName = unlocalizedNameIn;
/* 14 */     this.defaultValue = defaultValueIn;
/*    */     
/* 16 */     if (unlocalizedNameIn == null)
/*    */     {
/* 18 */       throw new IllegalArgumentException("Name cannot be null!");
/*    */     }
/*    */   }
/*    */   
/*    */   public String getAttributeUnlocalizedName()
/*    */   {
/* 24 */     return this.unlocalizedName;
/*    */   }
/*    */   
/*    */   public double getDefaultValue()
/*    */   {
/* 29 */     return this.defaultValue;
/*    */   }
/*    */   
/*    */   public boolean getShouldWatch()
/*    */   {
/* 34 */     return this.shouldWatch;
/*    */   }
/*    */   
/*    */   public BaseAttribute setShouldWatch(boolean shouldWatchIn)
/*    */   {
/* 39 */     this.shouldWatch = shouldWatchIn;
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public IAttribute func_180372_d()
/*    */   {
/* 45 */     return this.field_180373_a;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 50 */     return this.unlocalizedName.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object p_equals_1_)
/*    */   {
/* 55 */     return ((p_equals_1_ instanceof IAttribute)) && (this.unlocalizedName.equals(((IAttribute)p_equals_1_).getAttributeUnlocalizedName()));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\attributes\BaseAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */