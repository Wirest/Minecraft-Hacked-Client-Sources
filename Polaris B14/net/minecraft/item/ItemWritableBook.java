/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemWritableBook
/*    */   extends Item
/*    */ {
/*    */   public ItemWritableBook()
/*    */   {
/* 13 */     setMaxStackSize(1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 21 */     playerIn.displayGUIBook(itemStackIn);
/* 22 */     playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 23 */     return itemStackIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isNBTValid(NBTTagCompound nbt)
/*    */   {
/* 31 */     if (nbt == null)
/*    */     {
/* 33 */       return false;
/*    */     }
/* 35 */     if (!nbt.hasKey("pages", 9))
/*    */     {
/* 37 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 41 */     NBTTagList nbttaglist = nbt.getTagList("pages", 8);
/*    */     
/* 43 */     for (int i = 0; i < nbttaglist.tagCount(); i++)
/*    */     {
/* 45 */       String s = nbttaglist.getStringTagAt(i);
/*    */       
/* 47 */       if (s == null)
/*    */       {
/* 49 */         return false;
/*    */       }
/*    */       
/* 52 */       if (s.length() > 32767)
/*    */       {
/* 54 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemWritableBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */