/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLeashKnot;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemLead extends Item
/*    */ {
/*    */   public ItemLead()
/*    */   {
/* 18 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 26 */     Block block = worldIn.getBlockState(pos).getBlock();
/*    */     
/* 28 */     if ((block instanceof net.minecraft.block.BlockFence))
/*    */     {
/* 30 */       if (worldIn.isRemote)
/*    */       {
/* 32 */         return true;
/*    */       }
/*    */       
/*    */ 
/* 36 */       attachToFence(playerIn, worldIn, pos);
/* 37 */       return true;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 42 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence)
/*    */   {
/* 48 */     EntityLeashKnot entityleashknot = EntityLeashKnot.getKnotForPosition(worldIn, fence);
/* 49 */     boolean flag = false;
/* 50 */     double d0 = 7.0D;
/* 51 */     int i = fence.getX();
/* 52 */     int j = fence.getY();
/* 53 */     int k = fence.getZ();
/*    */     
/* 55 */     for (EntityLiving entityliving : worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0)))
/*    */     {
/* 57 */       if ((entityliving.getLeashed()) && (entityliving.getLeashedToEntity() == player))
/*    */       {
/* 59 */         if (entityleashknot == null)
/*    */         {
/* 61 */           entityleashknot = EntityLeashKnot.createKnot(worldIn, fence);
/*    */         }
/*    */         
/* 64 */         entityliving.setLeashedToEntity(entityleashknot, true);
/* 65 */         flag = true;
/*    */       }
/*    */     }
/*    */     
/* 69 */     return flag;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemLead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */