/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockChest;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntityChest;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.WeightedRandomChestContent;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class WorldGeneratorBonusChest
/*    */   extends WorldGenerator
/*    */ {
/*    */   private final List<WeightedRandomChestContent> chestItems;
/*    */   private final int itemsToGenerateInBonusChest;
/*    */   
/*    */   public WorldGeneratorBonusChest(List<WeightedRandomChestContent> p_i45634_1_, int p_i45634_2_)
/*    */   {
/* 25 */     this.chestItems = p_i45634_1_;
/* 26 */     this.itemsToGenerateInBonusChest = p_i45634_2_;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/*    */     Block block;
/* 33 */     while ((((block = worldIn.getBlockState(position).getBlock()).getMaterial() == Material.air) || (block.getMaterial() == Material.leaves)) && (position.getY() > 1)) {
/*    */       Block block;
/* 35 */       position = position.down();
/*    */     }
/*    */     
/* 38 */     if (position.getY() < 1)
/*    */     {
/* 40 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 44 */     position = position.up();
/*    */     
/* 46 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 48 */       BlockPos blockpos = position.add(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(3) - rand.nextInt(3), rand.nextInt(4) - rand.nextInt(4));
/*    */       
/* 50 */       if ((worldIn.isAirBlock(blockpos)) && (World.doesBlockHaveSolidTopSurface(worldIn, blockpos.down())))
/*    */       {
/* 52 */         worldIn.setBlockState(blockpos, Blocks.chest.getDefaultState(), 2);
/* 53 */         TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*    */         
/* 55 */         if ((tileentity instanceof TileEntityChest))
/*    */         {
/* 57 */           WeightedRandomChestContent.generateChestContents(rand, this.chestItems, (TileEntityChest)tileentity, this.itemsToGenerateInBonusChest);
/*    */         }
/*    */         
/* 60 */         BlockPos blockpos1 = blockpos.east();
/* 61 */         BlockPos blockpos2 = blockpos.west();
/* 62 */         BlockPos blockpos3 = blockpos.north();
/* 63 */         BlockPos blockpos4 = blockpos.south();
/*    */         
/* 65 */         if ((worldIn.isAirBlock(blockpos2)) && (World.doesBlockHaveSolidTopSurface(worldIn, blockpos2.down())))
/*    */         {
/* 67 */           worldIn.setBlockState(blockpos2, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 70 */         if ((worldIn.isAirBlock(blockpos1)) && (World.doesBlockHaveSolidTopSurface(worldIn, blockpos1.down())))
/*    */         {
/* 72 */           worldIn.setBlockState(blockpos1, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 75 */         if ((worldIn.isAirBlock(blockpos3)) && (World.doesBlockHaveSolidTopSurface(worldIn, blockpos3.down())))
/*    */         {
/* 77 */           worldIn.setBlockState(blockpos3, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 80 */         if ((worldIn.isAirBlock(blockpos4)) && (World.doesBlockHaveSolidTopSurface(worldIn, blockpos4.down())))
/*    */         {
/* 82 */           worldIn.setBlockState(blockpos4, Blocks.torch.getDefaultState(), 2);
/*    */         }
/*    */         
/* 85 */         return true;
/*    */       }
/*    */     }
/*    */     
/* 89 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGeneratorBonusChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */