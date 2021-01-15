/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.item.EntityFallingBlock;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockFalling extends Block
/*    */ {
/*    */   public static boolean fallInstantly;
/*    */   
/*    */   public BlockFalling()
/*    */   {
/* 18 */     super(Material.sand);
/* 19 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */   
/*    */   public BlockFalling(Material materialIn)
/*    */   {
/* 24 */     super(materialIn);
/*    */   }
/*    */   
/*    */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 29 */     worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*    */   {
/* 37 */     worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*    */   }
/*    */   
/*    */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*    */   {
/* 42 */     if (!worldIn.isRemote)
/*    */     {
/* 44 */       checkFallable(worldIn, pos);
/*    */     }
/*    */   }
/*    */   
/*    */   private void checkFallable(World worldIn, BlockPos pos)
/*    */   {
/* 50 */     if ((canFallInto(worldIn, pos.down())) && (pos.getY() >= 0))
/*    */     {
/* 52 */       int i = 32;
/*    */       
/* 54 */       if ((!fallInstantly) && (worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))))
/*    */       {
/* 56 */         if (!worldIn.isRemote)
/*    */         {
/* 58 */           EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, worldIn.getBlockState(pos));
/* 59 */           onStartFalling(entityfallingblock);
/* 60 */           worldIn.spawnEntityInWorld(entityfallingblock);
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 65 */         worldIn.setBlockToAir(pos);
/*    */         
/*    */ 
/* 68 */         for (BlockPos blockpos = pos.down(); (canFallInto(worldIn, blockpos)) && (blockpos.getY() > 0); blockpos = blockpos.down()) {}
/*    */         
/*    */ 
/*    */ 
/*    */ 
/* 73 */         if (blockpos.getY() > 0)
/*    */         {
/* 75 */           worldIn.setBlockState(blockpos.up(), getDefaultState());
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void onStartFalling(EntityFallingBlock fallingEntity) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public int tickRate(World worldIn)
/*    */   {
/* 90 */     return 2;
/*    */   }
/*    */   
/*    */   public static boolean canFallInto(World worldIn, BlockPos pos)
/*    */   {
/* 95 */     Block block = worldIn.getBlockState(pos).getBlock();
/* 96 */     Material material = block.blockMaterial;
/* 97 */     return (block == Blocks.fire) || (material == Material.air) || (material == Material.water) || (material == Material.lava);
/*    */   }
/*    */   
/*    */   public void onEndFalling(World worldIn, BlockPos pos) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockFalling.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */