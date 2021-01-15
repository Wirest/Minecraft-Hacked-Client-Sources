/*     */ package net.minecraft.block.state;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockPistonBase;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPistonStructureHelper
/*     */ {
/*     */   private final World world;
/*     */   private final BlockPos pistonPos;
/*     */   private final BlockPos blockToMove;
/*     */   private final EnumFacing moveDirection;
/*  19 */   private final List<BlockPos> toMove = Lists.newArrayList();
/*  20 */   private final List<BlockPos> toDestroy = Lists.newArrayList();
/*     */   
/*     */   public BlockPistonStructureHelper(World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending)
/*     */   {
/*  24 */     this.world = worldIn;
/*  25 */     this.pistonPos = posIn;
/*     */     
/*  27 */     if (extending)
/*     */     {
/*  29 */       this.moveDirection = pistonFacing;
/*  30 */       this.blockToMove = posIn.offset(pistonFacing);
/*     */     }
/*     */     else
/*     */     {
/*  34 */       this.moveDirection = pistonFacing.getOpposite();
/*  35 */       this.blockToMove = posIn.offset(pistonFacing, 2);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canMove()
/*     */   {
/*  41 */     this.toMove.clear();
/*  42 */     this.toDestroy.clear();
/*  43 */     Block block = this.world.getBlockState(this.blockToMove).getBlock();
/*     */     
/*  45 */     if (!BlockPistonBase.canPush(block, this.world, this.blockToMove, this.moveDirection, false))
/*     */     {
/*  47 */       if (block.getMobilityFlag() != 1)
/*     */       {
/*  49 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  53 */       this.toDestroy.add(this.blockToMove);
/*  54 */       return true;
/*     */     }
/*     */     
/*  57 */     if (!func_177251_a(this.blockToMove))
/*     */     {
/*  59 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  63 */     for (int i = 0; i < this.toMove.size(); i++)
/*     */     {
/*  65 */       BlockPos blockpos = (BlockPos)this.toMove.get(i);
/*     */       
/*  67 */       if ((this.world.getBlockState(blockpos).getBlock() == Blocks.slime_block) && (!func_177250_b(blockpos)))
/*     */       {
/*  69 */         return false;
/*     */       }
/*     */     }
/*     */     
/*  73 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean func_177251_a(BlockPos origin)
/*     */   {
/*  79 */     Block block = this.world.getBlockState(origin).getBlock();
/*     */     
/*  81 */     if (block.getMaterial() == Material.air)
/*     */     {
/*  83 */       return true;
/*     */     }
/*  85 */     if (!BlockPistonBase.canPush(block, this.world, origin, this.moveDirection, false))
/*     */     {
/*  87 */       return true;
/*     */     }
/*  89 */     if (origin.equals(this.pistonPos))
/*     */     {
/*  91 */       return true;
/*     */     }
/*  93 */     if (this.toMove.contains(origin))
/*     */     {
/*  95 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  99 */     int i = 1;
/*     */     
/* 101 */     if (i + this.toMove.size() > 12)
/*     */     {
/* 103 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 107 */     while (block == Blocks.slime_block)
/*     */     {
/* 109 */       BlockPos blockpos = origin.offset(this.moveDirection.getOpposite(), i);
/* 110 */       block = this.world.getBlockState(blockpos).getBlock();
/*     */       
/* 112 */       if ((block.getMaterial() == Material.air) || (!BlockPistonBase.canPush(block, this.world, blockpos, this.moveDirection, false)) || (blockpos.equals(this.pistonPos))) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 117 */       i++;
/*     */       
/* 119 */       if (i + this.toMove.size() > 12)
/*     */       {
/* 121 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 125 */     int i1 = 0;
/*     */     
/* 127 */     for (int j = i - 1; j >= 0; j--)
/*     */     {
/* 129 */       this.toMove.add(origin.offset(this.moveDirection.getOpposite(), j));
/* 130 */       i1++;
/*     */     }
/*     */     
/* 133 */     int j1 = 1;
/*     */     
/*     */     for (;;)
/*     */     {
/* 137 */       BlockPos blockpos1 = origin.offset(this.moveDirection, j1);
/* 138 */       int k = this.toMove.indexOf(blockpos1);
/*     */       
/* 140 */       if (k > -1)
/*     */       {
/* 142 */         func_177255_a(i1, k);
/*     */         
/* 144 */         for (int l = 0; l <= k + i1; l++)
/*     */         {
/* 146 */           BlockPos blockpos2 = (BlockPos)this.toMove.get(l);
/*     */           
/* 148 */           if ((this.world.getBlockState(blockpos2).getBlock() == Blocks.slime_block) && (!func_177250_b(blockpos2)))
/*     */           {
/* 150 */             return false;
/*     */           }
/*     */         }
/*     */         
/* 154 */         return true;
/*     */       }
/*     */       
/* 157 */       block = this.world.getBlockState(blockpos1).getBlock();
/*     */       
/* 159 */       if (block.getMaterial() == Material.air)
/*     */       {
/* 161 */         return true;
/*     */       }
/*     */       
/* 164 */       if ((!BlockPistonBase.canPush(block, this.world, blockpos1, this.moveDirection, true)) || (blockpos1.equals(this.pistonPos)))
/*     */       {
/* 166 */         return false;
/*     */       }
/*     */       
/* 169 */       if (block.getMobilityFlag() == 1)
/*     */       {
/* 171 */         this.toDestroy.add(blockpos1);
/* 172 */         return true;
/*     */       }
/*     */       
/* 175 */       if (this.toMove.size() >= 12)
/*     */       {
/* 177 */         return false;
/*     */       }
/*     */       
/* 180 */       this.toMove.add(blockpos1);
/* 181 */       i1++;
/* 182 */       j1++;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void func_177255_a(int p_177255_1_, int p_177255_2_)
/*     */   {
/* 190 */     List<BlockPos> list = Lists.newArrayList();
/* 191 */     List<BlockPos> list1 = Lists.newArrayList();
/* 192 */     List<BlockPos> list2 = Lists.newArrayList();
/* 193 */     list.addAll(this.toMove.subList(0, p_177255_2_));
/* 194 */     list1.addAll(this.toMove.subList(this.toMove.size() - p_177255_1_, this.toMove.size()));
/* 195 */     list2.addAll(this.toMove.subList(p_177255_2_, this.toMove.size() - p_177255_1_));
/* 196 */     this.toMove.clear();
/* 197 */     this.toMove.addAll(list);
/* 198 */     this.toMove.addAll(list1);
/* 199 */     this.toMove.addAll(list2);
/*     */   }
/*     */   
/*     */   private boolean func_177250_b(BlockPos p_177250_1_) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/* 204 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/* 206 */       if ((enumfacing.getAxis() != this.moveDirection.getAxis()) && (!func_177251_a(p_177250_1_.offset(enumfacing))))
/*     */       {
/* 208 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 212 */     return true;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getBlocksToMove()
/*     */   {
/* 217 */     return this.toMove;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getBlocksToDestroy()
/*     */   {
/* 222 */     return this.toDestroy;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\state\BlockPistonStructureHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */