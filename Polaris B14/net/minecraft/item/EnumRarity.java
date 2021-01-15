/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ 
/*    */ public enum EnumRarity
/*    */ {
/*  7 */   COMMON(EnumChatFormatting.WHITE, "Common"), 
/*  8 */   UNCOMMON(EnumChatFormatting.YELLOW, "Uncommon"), 
/*  9 */   RARE(EnumChatFormatting.AQUA, "Rare"), 
/* 10 */   EPIC(EnumChatFormatting.LIGHT_PURPLE, "Epic");
/*    */   
/*    */ 
/*    */ 
/*    */   public final EnumChatFormatting rarityColor;
/*    */   
/*    */ 
/*    */   public final String rarityName;
/*    */   
/*    */ 
/*    */ 
/*    */   private EnumRarity(EnumChatFormatting color, String name)
/*    */   {
/* 23 */     this.rarityColor = color;
/* 24 */     this.rarityName = name;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\EnumRarity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */