/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDynamicLiquid extends BlockLiquid
/*     */ {
/*     */   int adjacentSourceBlocks;
/*     */   
/*     */   protected BlockDynamicLiquid(Material materialIn)
/*     */   {
/*  19 */     super(materialIn);
/*     */   }
/*     */   
/*     */   private void placeStaticBlock(World worldIn, BlockPos pos, IBlockState currentState)
/*     */   {
/*  24 */     worldIn.setBlockState(pos, getStaticBlock(this.blockMaterial).getDefaultState().withProperty(LEVEL, (Integer)currentState.getValue(LEVEL)), 2);
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  29 */     int i = ((Integer)state.getValue(LEVEL)).intValue();
/*  30 */     int j = 1;
/*     */     
/*  32 */     if ((this.blockMaterial == Material.lava) && (!worldIn.provider.doesWaterVaporize()))
/*     */     {
/*  34 */       j = 2;
/*     */     }
/*     */     
/*  37 */     int k = tickRate(worldIn);
/*     */     
/*  39 */     if (i > 0)
/*     */     {
/*  41 */       int l = -100;
/*  42 */       this.adjacentSourceBlocks = 0;
/*     */       
/*  44 */       for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/*  46 */         EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*  47 */         l = checkAdjacentBlock(worldIn, pos.offset(enumfacing), l);
/*     */       }
/*     */       
/*  50 */       int i1 = l + j;
/*     */       
/*  52 */       if ((i1 >= 8) || (l < 0))
/*     */       {
/*  54 */         i1 = -1;
/*     */       }
/*     */       
/*  57 */       if (getLevel(worldIn, pos.up()) >= 0)
/*     */       {
/*  59 */         int j1 = getLevel(worldIn, pos.up());
/*     */         
/*  61 */         if (j1 >= 8)
/*     */         {
/*  63 */           i1 = j1;
/*     */         }
/*     */         else
/*     */         {
/*  67 */           i1 = j1 + 8;
/*     */         }
/*     */       }
/*     */       
/*  71 */       if ((this.adjacentSourceBlocks >= 2) && (this.blockMaterial == Material.water))
/*     */       {
/*  73 */         IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
/*     */         
/*  75 */         if (iblockstate1.getBlock().getMaterial().isSolid())
/*     */         {
/*  77 */           i1 = 0;
/*     */         }
/*  79 */         else if ((iblockstate1.getBlock().getMaterial() == this.blockMaterial) && (((Integer)iblockstate1.getValue(LEVEL)).intValue() == 0))
/*     */         {
/*  81 */           i1 = 0;
/*     */         }
/*     */       }
/*     */       
/*  85 */       if ((this.blockMaterial == Material.lava) && (i < 8) && (i1 < 8) && (i1 > i) && (rand.nextInt(4) != 0))
/*     */       {
/*  87 */         k *= 4;
/*     */       }
/*     */       
/*  90 */       if (i1 == i)
/*     */       {
/*  92 */         placeStaticBlock(worldIn, pos, state);
/*     */       }
/*     */       else
/*     */       {
/*  96 */         i = i1;
/*     */         
/*  98 */         if (i1 < 0)
/*     */         {
/* 100 */           worldIn.setBlockToAir(pos);
/*     */         }
/*     */         else
/*     */         {
/* 104 */           state = state.withProperty(LEVEL, Integer.valueOf(i1));
/* 105 */           worldIn.setBlockState(pos, state, 2);
/* 106 */           worldIn.scheduleUpdate(pos, this, k);
/* 107 */           worldIn.notifyNeighborsOfStateChange(pos, this);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 113 */       placeStaticBlock(worldIn, pos, state);
/*     */     }
/*     */     
/* 116 */     IBlockState iblockstate = worldIn.getBlockState(pos.down());
/*     */     
/* 118 */     if (canFlowInto(worldIn, pos.down(), iblockstate))
/*     */     {
/* 120 */       if ((this.blockMaterial == Material.lava) && (worldIn.getBlockState(pos.down()).getBlock().getMaterial() == Material.water))
/*     */       {
/* 122 */         worldIn.setBlockState(pos.down(), Blocks.stone.getDefaultState());
/* 123 */         triggerMixEffects(worldIn, pos.down());
/* 124 */         return;
/*     */       }
/*     */       
/* 127 */       if (i >= 8)
/*     */       {
/* 129 */         tryFlowInto(worldIn, pos.down(), iblockstate, i);
/*     */       }
/*     */       else
/*     */       {
/* 133 */         tryFlowInto(worldIn, pos.down(), iblockstate, i + 8);
/*     */       }
/*     */     }
/* 136 */     else if ((i >= 0) && ((i == 0) || (isBlocked(worldIn, pos.down(), iblockstate))))
/*     */     {
/* 138 */       Set<EnumFacing> set = getPossibleFlowDirections(worldIn, pos);
/* 139 */       int k1 = i + j;
/*     */       
/* 141 */       if (i >= 8)
/*     */       {
/* 143 */         k1 = 1;
/*     */       }
/*     */       
/* 146 */       if (k1 >= 8)
/*     */       {
/* 148 */         return;
/*     */       }
/*     */       
/* 151 */       for (EnumFacing enumfacing1 : set)
/*     */       {
/* 153 */         tryFlowInto(worldIn, pos.offset(enumfacing1), worldIn.getBlockState(pos.offset(enumfacing1)), k1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level)
/*     */   {
/* 160 */     if (canFlowInto(worldIn, pos, state))
/*     */     {
/* 162 */       if (state.getBlock() != Blocks.air)
/*     */       {
/* 164 */         if (this.blockMaterial == Material.lava)
/*     */         {
/* 166 */           triggerMixEffects(worldIn, pos);
/*     */         }
/*     */         else
/*     */         {
/* 170 */           state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
/*     */         }
/*     */       }
/*     */       
/* 174 */       worldIn.setBlockState(pos, getDefaultState().withProperty(LEVEL, Integer.valueOf(level)), 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private int func_176374_a(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost)
/*     */   {
/* 180 */     int i = 1000;
/*     */     
/* 182 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 184 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/* 186 */       if (enumfacing != calculateFlowCost)
/*     */       {
/* 188 */         BlockPos blockpos = pos.offset(enumfacing);
/* 189 */         IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */         
/* 191 */         if ((!isBlocked(worldIn, blockpos, iblockstate)) && ((iblockstate.getBlock().getMaterial() != this.blockMaterial) || (((Integer)iblockstate.getValue(LEVEL)).intValue() > 0)))
/*     */         {
/* 193 */           if (!isBlocked(worldIn, blockpos.down(), iblockstate))
/*     */           {
/* 195 */             return distance;
/*     */           }
/*     */           
/* 198 */           if (distance < 4)
/*     */           {
/* 200 */             int j = func_176374_a(worldIn, blockpos, distance + 1, enumfacing.getOpposite());
/*     */             
/* 202 */             if (j < i)
/*     */             {
/* 204 */               i = j;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 211 */     return i;
/*     */   }
/*     */   
/*     */   private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos)
/*     */   {
/* 216 */     int i = 1000;
/* 217 */     Set<EnumFacing> set = java.util.EnumSet.noneOf(EnumFacing.class);
/*     */     
/* 219 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 221 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/* 222 */       BlockPos blockpos = pos.offset(enumfacing);
/* 223 */       IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */       
/* 225 */       if ((!isBlocked(worldIn, blockpos, iblockstate)) && ((iblockstate.getBlock().getMaterial() != this.blockMaterial) || (((Integer)iblockstate.getValue(LEVEL)).intValue() > 0)))
/*     */       {
/*     */         int j;
/*     */         int j;
/* 229 */         if (isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down())))
/*     */         {
/* 231 */           j = func_176374_a(worldIn, blockpos, 1, enumfacing.getOpposite());
/*     */         }
/*     */         else
/*     */         {
/* 235 */           j = 0;
/*     */         }
/*     */         
/* 238 */         if (j < i)
/*     */         {
/* 240 */           set.clear();
/*     */         }
/*     */         
/* 243 */         if (j <= i)
/*     */         {
/* 245 */           set.add(enumfacing);
/* 246 */           i = j;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 251 */     return set;
/*     */   }
/*     */   
/*     */   private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 256 */     Block block = worldIn.getBlockState(pos).getBlock();
/* 257 */     return block.blockMaterial == Material.portal;
/*     */   }
/*     */   
/*     */   protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel)
/*     */   {
/* 262 */     int i = getLevel(worldIn, pos);
/*     */     
/* 264 */     if (i < 0)
/*     */     {
/* 266 */       return currentMinLevel;
/*     */     }
/*     */     
/*     */ 
/* 270 */     if (i == 0)
/*     */     {
/* 272 */       this.adjacentSourceBlocks += 1;
/*     */     }
/*     */     
/* 275 */     if (i >= 8)
/*     */     {
/* 277 */       i = 0;
/*     */     }
/*     */     
/* 280 */     return (currentMinLevel >= 0) && (i >= currentMinLevel) ? currentMinLevel : i;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 286 */     Material material = state.getBlock().getMaterial();
/* 287 */     return (material != this.blockMaterial) && (material != Material.lava) && (!isBlocked(worldIn, pos, state));
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 292 */     if (!checkForMixing(worldIn, pos, state))
/*     */     {
/* 294 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDynamicLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */