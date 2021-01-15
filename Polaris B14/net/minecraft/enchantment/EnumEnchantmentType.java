/*    */ package net.minecraft.enchantment;
/*    */ 
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum EnumEnchantmentType
/*    */ {
/* 12 */   ALL, 
/* 13 */   ARMOR, 
/* 14 */   ARMOR_FEET, 
/* 15 */   ARMOR_LEGS, 
/* 16 */   ARMOR_TORSO, 
/* 17 */   ARMOR_HEAD, 
/* 18 */   WEAPON, 
/* 19 */   DIGGER, 
/* 20 */   FISHING_ROD, 
/* 21 */   BREAKABLE, 
/* 22 */   BOW;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canEnchantItem(Item p_77557_1_)
/*    */   {
/* 29 */     if (this == ALL)
/*    */     {
/* 31 */       return true;
/*    */     }
/* 33 */     if ((this == BREAKABLE) && (p_77557_1_.isDamageable()))
/*    */     {
/* 35 */       return true;
/*    */     }
/* 37 */     if ((p_77557_1_ instanceof ItemArmor))
/*    */     {
/* 39 */       if (this == ARMOR)
/*    */       {
/* 41 */         return true;
/*    */       }
/*    */       
/*    */ 
/* 45 */       ItemArmor itemarmor = (ItemArmor)p_77557_1_;
/* 46 */       return this == ARMOR_HEAD;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 51 */     return this == WEAPON;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\enchantment\EnumEnchantmentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */