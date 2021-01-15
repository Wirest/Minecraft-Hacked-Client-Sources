/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockStaticLiquid extends BlockLiquid
/*     */ {
/*     */   protected BlockStaticLiquid(Material materialIn)
/*     */   {
/*  15 */     super(materialIn);
/*  16 */     setTickRandomly(false);
/*     */     
/*  18 */     if (materialIn == Material.lava)
/*     */     {
/*  20 */       setTickRandomly(true);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  29 */     if (!checkForMixing(worldIn, pos, state))
/*     */     {
/*  31 */       updateLiquid(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateLiquid(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  37 */     BlockDynamicLiquid blockdynamicliquid = getFlowingBlock(this.blockMaterial);
/*  38 */     worldIn.setBlockState(pos, blockdynamicliquid.getDefaultState().withProperty(LEVEL, (Integer)state.getValue(LEVEL)), 2);
/*  39 */     worldIn.scheduleUpdate(pos, blockdynamicliquid, tickRate(worldIn));
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  44 */     if (this.blockMaterial == Material.lava)
/*     */     {
/*  46 */       if (worldIn.getGameRules().getBoolean("doFireTick"))
/*     */       {
/*  48 */         int i = rand.nextInt(3);
/*     */         
/*  50 */         if (i > 0)
/*     */         {
/*  52 */           BlockPos blockpos = pos;
/*     */           
/*  54 */           for (int j = 0; j < i; j++)
/*     */           {
/*  56 */             blockpos = blockpos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
/*  57 */             Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */             
/*  59 */             if (block.blockMaterial == Material.air)
/*     */             {
/*  61 */               if (isSurroundingBlockFlammable(worldIn, blockpos))
/*     */               {
/*  63 */                 worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*     */               }
/*     */               
/*     */             }
/*  67 */             else if (block.blockMaterial.blocksMovement())
/*     */             {
/*  69 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  75 */           for (int k = 0; k < 3; k++)
/*     */           {
/*  77 */             BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
/*     */             
/*  79 */             if ((worldIn.isAirBlock(blockpos1.up())) && (getCanBlockBurn(worldIn, blockpos1)))
/*     */             {
/*  81 */               worldIn.setBlockState(blockpos1.up(), Blocks.fire.getDefaultState());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isSurroundingBlockFlammable(World worldIn, BlockPos pos) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/*  91 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/*  93 */       if (getCanBlockBurn(worldIn, pos.offset(enumfacing)))
/*     */       {
/*  95 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   private boolean getCanBlockBurn(World worldIn, BlockPos pos)
/*     */   {
/* 104 */     return worldIn.getBlockState(pos).getBlock().getMaterial().getCanBurn();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStaticLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */