/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class ItemColored extends ItemBlock
/*    */ {
/*    */   private final Block coloredBlock;
/*    */   private String[] subtypeNames;
/*    */   
/*    */   public ItemColored(Block block, boolean hasSubtypes)
/*    */   {
/* 12 */     super(block);
/* 13 */     this.coloredBlock = block;
/*    */     
/* 15 */     if (hasSubtypes)
/*    */     {
/* 17 */       setMaxDamage(0);
/* 18 */       setHasSubtypes(true);
/*    */     }
/*    */   }
/*    */   
/*    */   public int getColorFromItemStack(ItemStack stack, int renderPass)
/*    */   {
/* 24 */     return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(stack.getMetadata()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetadata(int damage)
/*    */   {
/* 33 */     return damage;
/*    */   }
/*    */   
/*    */   public ItemColored setSubtypeNames(String[] names)
/*    */   {
/* 38 */     this.subtypeNames = names;
/* 39 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getUnlocalizedName(ItemStack stack)
/*    */   {
/* 48 */     if (this.subtypeNames == null)
/*    */     {
/* 50 */       return super.getUnlocalizedName(stack);
/*    */     }
/*    */     
/*    */ 
/* 54 */     int i = stack.getMetadata();
/* 55 */     return (i >= 0) && (i < this.subtypeNames.length) ? super.getUnlocalizedName(stack) + "." + this.subtypeNames[i] : super.getUnlocalizedName(stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */