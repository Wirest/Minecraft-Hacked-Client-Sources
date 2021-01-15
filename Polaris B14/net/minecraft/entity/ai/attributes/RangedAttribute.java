/*    */ package net.minecraft.entity.ai.attributes;
/*    */ 
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class RangedAttribute extends BaseAttribute
/*    */ {
/*    */   private final double minimumValue;
/*    */   private final double maximumValue;
/*    */   private String description;
/*    */   
/*    */   public RangedAttribute(IAttribute p_i45891_1_, String unlocalizedNameIn, double defaultValue, double minimumValueIn, double maximumValueIn)
/*    */   {
/* 13 */     super(p_i45891_1_, unlocalizedNameIn, defaultValue);
/* 14 */     this.minimumValue = minimumValueIn;
/* 15 */     this.maximumValue = maximumValueIn;
/*    */     
/* 17 */     if (minimumValueIn > maximumValueIn)
/*    */     {
/* 19 */       throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
/*    */     }
/* 21 */     if (defaultValue < minimumValueIn)
/*    */     {
/* 23 */       throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
/*    */     }
/* 25 */     if (defaultValue > maximumValueIn)
/*    */     {
/* 27 */       throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
/*    */     }
/*    */   }
/*    */   
/*    */   public RangedAttribute setDescription(String descriptionIn)
/*    */   {
/* 33 */     this.description = descriptionIn;
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public String getDescription()
/*    */   {
/* 39 */     return this.description;
/*    */   }
/*    */   
/*    */   public double clampValue(double p_111109_1_)
/*    */   {
/* 44 */     p_111109_1_ = MathHelper.clamp_double(p_111109_1_, this.minimumValue, this.maximumValue);
/* 45 */     return p_111109_1_;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\attributes\RangedAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */