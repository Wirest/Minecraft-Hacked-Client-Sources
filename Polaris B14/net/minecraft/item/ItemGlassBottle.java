/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemGlassBottle extends Item
/*    */ {
/*    */   public ItemGlassBottle()
/*    */   {
/* 16 */     setCreativeTab(CreativeTabs.tabBrewing);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
/*    */   {
/* 24 */     MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
/*    */     
/* 26 */     if (movingobjectposition == null)
/*    */     {
/* 28 */       return itemStackIn;
/*    */     }
/*    */     
/*    */ 
/* 32 */     if (movingobjectposition.typeOfHit == net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK)
/*    */     {
/* 34 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*    */       
/* 36 */       if (!worldIn.isBlockModifiable(playerIn, blockpos))
/*    */       {
/* 38 */         return itemStackIn;
/*    */       }
/*    */       
/* 41 */       if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
/*    */       {
/* 43 */         return itemStackIn;
/*    */       }
/*    */       
/* 46 */       if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == net.minecraft.block.material.Material.water)
/*    */       {
/* 48 */         itemStackIn.stackSize -= 1;
/* 49 */         playerIn.triggerAchievement(net.minecraft.stats.StatList.objectUseStats[Item.getIdFromItem(this)]);
/*    */         
/* 51 */         if (itemStackIn.stackSize <= 0)
/*    */         {
/* 53 */           return new ItemStack(Items.potionitem);
/*    */         }
/*    */         
/* 56 */         if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.potionitem)))
/*    */         {
/* 58 */           playerIn.dropPlayerItemWithRandomChoice(new ItemStack(Items.potionitem, 1, 0), false);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 63 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemGlassBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */