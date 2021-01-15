/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemShears extends Item
/*    */ {
/*    */   public ItemShears()
/*    */   {
/* 15 */     setMaxStackSize(1);
/* 16 */     setMaxDamage(238);
/* 17 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
/*    */   {
/* 25 */     if ((blockIn.getMaterial() != Material.leaves) && (blockIn != Blocks.web) && (blockIn != Blocks.tallgrass) && (blockIn != Blocks.vine) && (blockIn != Blocks.tripwire) && (blockIn != Blocks.wool))
/*    */     {
/* 27 */       return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
/*    */     }
/*    */     
/*    */ 
/* 31 */     stack.damageItem(1, playerIn);
/* 32 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canHarvestBlock(Block blockIn)
/*    */   {
/* 41 */     return (blockIn == Blocks.web) || (blockIn == Blocks.redstone_wire) || (blockIn == Blocks.tripwire);
/*    */   }
/*    */   
/*    */   public float getStrVsBlock(ItemStack stack, Block block)
/*    */   {
/* 46 */     return (block != Blocks.web) && (block.getMaterial() != Material.leaves) ? super.getStrVsBlock(stack, block) : block == Blocks.wool ? 5.0F : 15.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemShears.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */