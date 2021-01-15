/*    */ package net.minecraft.potion;
/*    */ 
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class PotionAbsorption extends Potion
/*    */ {
/*    */   protected PotionAbsorption(int potionID, ResourceLocation location, boolean badEffect, int potionColor)
/*    */   {
/* 11 */     super(potionID, location, badEffect, potionColor);
/*    */   }
/*    */   
/*    */   public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111187_2_, int amplifier)
/*    */   {
/* 16 */     entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - 4 * (amplifier + 1));
/* 17 */     super.removeAttributesModifiersFromEntity(entityLivingBaseIn, p_111187_2_, amplifier);
/*    */   }
/*    */   
/*    */   public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, BaseAttributeMap p_111185_2_, int amplifier)
/*    */   {
/* 22 */     entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + 4 * (amplifier + 1));
/* 23 */     super.applyAttributesModifiersToEntity(entityLivingBaseIn, p_111185_2_, amplifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\potion\PotionAbsorption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */