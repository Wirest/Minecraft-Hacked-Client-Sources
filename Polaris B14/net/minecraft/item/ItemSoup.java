/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSoup extends ItemFood
/*    */ {
/*    */   public ItemSoup(int healAmount)
/*    */   {
/* 11 */     super(healAmount, false);
/* 12 */     setMaxStackSize(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 21 */     super.onItemUseFinish(stack, worldIn, playerIn);
/* 22 */     return new ItemStack(Items.bowl);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSoup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */