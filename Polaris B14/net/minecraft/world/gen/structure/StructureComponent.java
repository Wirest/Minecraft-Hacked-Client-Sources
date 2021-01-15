/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.BlockDirectional;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemDoor;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityDispenser;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.WeightedRandomChestContent;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StructureComponent
/*     */ {
/*     */   protected StructureBoundingBox boundingBox;
/*     */   protected EnumFacing coordBaseMode;
/*     */   protected int componentType;
/*     */   
/*     */   public StructureComponent() {}
/*     */   
/*     */   protected StructureComponent(int type)
/*     */   {
/*  37 */     this.componentType = type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NBTTagCompound createStructureBaseNBT()
/*     */   {
/*  48 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  49 */     nbttagcompound.setString("id", MapGenStructureIO.getStructureComponentName(this));
/*  50 */     nbttagcompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
/*  51 */     nbttagcompound.setInteger("O", this.coordBaseMode == null ? -1 : this.coordBaseMode.getHorizontalIndex());
/*  52 */     nbttagcompound.setInteger("GD", this.componentType);
/*  53 */     writeStructureToNBT(nbttagcompound);
/*  54 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void writeStructureToNBT(NBTTagCompound paramNBTTagCompound);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readStructureBaseNBT(World worldIn, NBTTagCompound tagCompound)
/*     */   {
/*  69 */     if (tagCompound.hasKey("BB"))
/*     */     {
/*  71 */       this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
/*     */     }
/*     */     
/*  74 */     int i = tagCompound.getInteger("O");
/*  75 */     this.coordBaseMode = (i == -1 ? null : EnumFacing.getHorizontal(i));
/*  76 */     this.componentType = tagCompound.getInteger("GD");
/*  77 */     readStructureFromNBT(tagCompound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void readStructureFromNBT(NBTTagCompound paramNBTTagCompound);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean addComponentParts(World paramWorld, Random paramRandom, StructureBoundingBox paramStructureBoundingBox);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StructureBoundingBox getBoundingBox()
/*     */   {
/* 100 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getComponentType()
/*     */   {
/* 108 */     return this.componentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StructureComponent findIntersecting(List<StructureComponent> listIn, StructureBoundingBox boundingboxIn)
/*     */   {
/* 116 */     for (StructureComponent structurecomponent : listIn)
/*     */     {
/* 118 */       if ((structurecomponent.getBoundingBox() != null) && (structurecomponent.getBoundingBox().intersectsWith(boundingboxIn)))
/*     */       {
/* 120 */         return structurecomponent;
/*     */       }
/*     */     }
/*     */     
/* 124 */     return null;
/*     */   }
/*     */   
/*     */   public BlockPos getBoundingBoxCenter()
/*     */   {
/* 129 */     return new BlockPos(this.boundingBox.getCenter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isLiquidInStructureBoundingBox(World worldIn, StructureBoundingBox boundingboxIn)
/*     */   {
/* 137 */     int i = Math.max(this.boundingBox.minX - 1, boundingboxIn.minX);
/* 138 */     int j = Math.max(this.boundingBox.minY - 1, boundingboxIn.minY);
/* 139 */     int k = Math.max(this.boundingBox.minZ - 1, boundingboxIn.minZ);
/* 140 */     int l = Math.min(this.boundingBox.maxX + 1, boundingboxIn.maxX);
/* 141 */     int i1 = Math.min(this.boundingBox.maxY + 1, boundingboxIn.maxY);
/* 142 */     int j1 = Math.min(this.boundingBox.maxZ + 1, boundingboxIn.maxZ);
/* 143 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 145 */     for (int k1 = i; k1 <= l; k1++)
/*     */     {
/* 147 */       for (int l1 = k; l1 <= j1; l1++)
/*     */       {
/* 149 */         if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, j, l1)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 151 */           return true;
/*     */         }
/*     */         
/* 154 */         if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 156 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 161 */     for (int i2 = i; i2 <= l; i2++)
/*     */     {
/* 163 */       for (int k2 = j; k2 <= i1; k2++)
/*     */       {
/* 165 */         if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i2, k2, k)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 167 */           return true;
/*     */         }
/*     */         
/* 170 */         if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i2, k2, j1)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 172 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 177 */     for (int j2 = k; j2 <= j1; j2++)
/*     */     {
/* 179 */       for (int l2 = j; l2 <= i1; l2++)
/*     */       {
/* 181 */         if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(i, l2, j2)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 183 */           return true;
/*     */         }
/*     */         
/* 186 */         if (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, l2, j2)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 188 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   protected int getXWithOffset(int x, int z)
/*     */   {
/* 198 */     if (this.coordBaseMode == null)
/*     */     {
/* 200 */       return x;
/*     */     }
/*     */     
/*     */ 
/* 204 */     switch (this.coordBaseMode)
/*     */     {
/*     */     case NORTH: 
/*     */     case SOUTH: 
/* 208 */       return this.boundingBox.minX + x;
/*     */     
/*     */     case UP: 
/* 211 */       return this.boundingBox.maxX - z;
/*     */     
/*     */     case WEST: 
/* 214 */       return this.boundingBox.minX + z;
/*     */     }
/*     */     
/* 217 */     return x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int getYWithOffset(int y)
/*     */   {
/* 224 */     return this.coordBaseMode == null ? y : y + this.boundingBox.minY;
/*     */   }
/*     */   
/*     */   protected int getZWithOffset(int x, int z)
/*     */   {
/* 229 */     if (this.coordBaseMode == null)
/*     */     {
/* 231 */       return z;
/*     */     }
/*     */     
/*     */ 
/* 235 */     switch (this.coordBaseMode)
/*     */     {
/*     */     case NORTH: 
/* 238 */       return this.boundingBox.maxZ - z;
/*     */     
/*     */     case SOUTH: 
/* 241 */       return this.boundingBox.minZ + z;
/*     */     
/*     */     case UP: 
/*     */     case WEST: 
/* 245 */       return this.boundingBox.minZ + x;
/*     */     }
/*     */     
/* 248 */     return z;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getMetadataWithOffset(Block blockIn, int meta)
/*     */   {
/* 258 */     if (blockIn == Blocks.rail)
/*     */     {
/* 260 */       if ((this.coordBaseMode == EnumFacing.WEST) || (this.coordBaseMode == EnumFacing.EAST))
/*     */       {
/* 262 */         if (meta == 1)
/*     */         {
/* 264 */           return 0;
/*     */         }
/*     */         
/* 267 */         return 1;
/*     */       }
/*     */     }
/* 270 */     else if ((blockIn instanceof BlockDoor))
/*     */     {
/* 272 */       if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */       {
/* 274 */         if (meta == 0)
/*     */         {
/* 276 */           return 2;
/*     */         }
/*     */         
/* 279 */         if (meta == 2)
/*     */         {
/* 281 */           return 0;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 286 */         if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 288 */           return meta + 1 & 0x3;
/*     */         }
/*     */         
/* 291 */         if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 293 */           return meta + 3 & 0x3;
/*     */         }
/*     */       }
/*     */     }
/* 297 */     else if ((blockIn != Blocks.stone_stairs) && (blockIn != Blocks.oak_stairs) && (blockIn != Blocks.nether_brick_stairs) && (blockIn != Blocks.stone_brick_stairs) && (blockIn != Blocks.sandstone_stairs))
/*     */     {
/* 299 */       if (blockIn == Blocks.ladder)
/*     */       {
/* 301 */         if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */         {
/* 303 */           if (meta == EnumFacing.NORTH.getIndex())
/*     */           {
/* 305 */             return EnumFacing.SOUTH.getIndex();
/*     */           }
/*     */           
/* 308 */           if (meta == EnumFacing.SOUTH.getIndex())
/*     */           {
/* 310 */             return EnumFacing.NORTH.getIndex();
/*     */           }
/*     */         }
/* 313 */         else if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 315 */           if (meta == EnumFacing.NORTH.getIndex())
/*     */           {
/* 317 */             return EnumFacing.WEST.getIndex();
/*     */           }
/*     */           
/* 320 */           if (meta == EnumFacing.SOUTH.getIndex())
/*     */           {
/* 322 */             return EnumFacing.EAST.getIndex();
/*     */           }
/*     */           
/* 325 */           if (meta == EnumFacing.WEST.getIndex())
/*     */           {
/* 327 */             return EnumFacing.NORTH.getIndex();
/*     */           }
/*     */           
/* 330 */           if (meta == EnumFacing.EAST.getIndex())
/*     */           {
/* 332 */             return EnumFacing.SOUTH.getIndex();
/*     */           }
/*     */         }
/* 335 */         else if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 337 */           if (meta == EnumFacing.NORTH.getIndex())
/*     */           {
/* 339 */             return EnumFacing.EAST.getIndex();
/*     */           }
/*     */           
/* 342 */           if (meta == EnumFacing.SOUTH.getIndex())
/*     */           {
/* 344 */             return EnumFacing.WEST.getIndex();
/*     */           }
/*     */           
/* 347 */           if (meta == EnumFacing.WEST.getIndex())
/*     */           {
/* 349 */             return EnumFacing.NORTH.getIndex();
/*     */           }
/*     */           
/* 352 */           if (meta == EnumFacing.EAST.getIndex())
/*     */           {
/* 354 */             return EnumFacing.SOUTH.getIndex();
/*     */           }
/*     */         }
/*     */       }
/* 358 */       else if (blockIn == Blocks.stone_button)
/*     */       {
/* 360 */         if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */         {
/* 362 */           if (meta == 3)
/*     */           {
/* 364 */             return 4;
/*     */           }
/*     */           
/* 367 */           if (meta == 4)
/*     */           {
/* 369 */             return 3;
/*     */           }
/*     */         }
/* 372 */         else if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 374 */           if (meta == 3)
/*     */           {
/* 376 */             return 1;
/*     */           }
/*     */           
/* 379 */           if (meta == 4)
/*     */           {
/* 381 */             return 2;
/*     */           }
/*     */           
/* 384 */           if (meta == 2)
/*     */           {
/* 386 */             return 3;
/*     */           }
/*     */           
/* 389 */           if (meta == 1)
/*     */           {
/* 391 */             return 4;
/*     */           }
/*     */         }
/* 394 */         else if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 396 */           if (meta == 3)
/*     */           {
/* 398 */             return 2;
/*     */           }
/*     */           
/* 401 */           if (meta == 4)
/*     */           {
/* 403 */             return 1;
/*     */           }
/*     */           
/* 406 */           if (meta == 2)
/*     */           {
/* 408 */             return 3;
/*     */           }
/*     */           
/* 411 */           if (meta == 1)
/*     */           {
/* 413 */             return 4;
/*     */           }
/*     */         }
/*     */       }
/* 417 */       else if ((blockIn != Blocks.tripwire_hook) && (!(blockIn instanceof BlockDirectional)))
/*     */       {
/* 419 */         if ((blockIn == Blocks.piston) || (blockIn == Blocks.sticky_piston) || (blockIn == Blocks.lever) || (blockIn == Blocks.dispenser))
/*     */         {
/* 421 */           if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */           {
/* 423 */             if ((meta == EnumFacing.NORTH.getIndex()) || (meta == EnumFacing.SOUTH.getIndex()))
/*     */             {
/* 425 */               return EnumFacing.getFront(meta).getOpposite().getIndex();
/*     */             }
/*     */           }
/* 428 */           else if (this.coordBaseMode == EnumFacing.WEST)
/*     */           {
/* 430 */             if (meta == EnumFacing.NORTH.getIndex())
/*     */             {
/* 432 */               return EnumFacing.WEST.getIndex();
/*     */             }
/*     */             
/* 435 */             if (meta == EnumFacing.SOUTH.getIndex())
/*     */             {
/* 437 */               return EnumFacing.EAST.getIndex();
/*     */             }
/*     */             
/* 440 */             if (meta == EnumFacing.WEST.getIndex())
/*     */             {
/* 442 */               return EnumFacing.NORTH.getIndex();
/*     */             }
/*     */             
/* 445 */             if (meta == EnumFacing.EAST.getIndex())
/*     */             {
/* 447 */               return EnumFacing.SOUTH.getIndex();
/*     */             }
/*     */           }
/* 450 */           else if (this.coordBaseMode == EnumFacing.EAST)
/*     */           {
/* 452 */             if (meta == EnumFacing.NORTH.getIndex())
/*     */             {
/* 454 */               return EnumFacing.EAST.getIndex();
/*     */             }
/*     */             
/* 457 */             if (meta == EnumFacing.SOUTH.getIndex())
/*     */             {
/* 459 */               return EnumFacing.WEST.getIndex();
/*     */             }
/*     */             
/* 462 */             if (meta == EnumFacing.WEST.getIndex())
/*     */             {
/* 464 */               return EnumFacing.NORTH.getIndex();
/*     */             }
/*     */             
/* 467 */             if (meta == EnumFacing.EAST.getIndex())
/*     */             {
/* 469 */               return EnumFacing.SOUTH.getIndex();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 476 */         EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
/*     */         
/* 478 */         if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */         {
/* 480 */           if ((enumfacing == EnumFacing.SOUTH) || (enumfacing == EnumFacing.NORTH))
/*     */           {
/* 482 */             return enumfacing.getOpposite().getHorizontalIndex();
/*     */           }
/*     */         }
/* 485 */         else if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 487 */           if (enumfacing == EnumFacing.NORTH)
/*     */           {
/* 489 */             return EnumFacing.WEST.getHorizontalIndex();
/*     */           }
/*     */           
/* 492 */           if (enumfacing == EnumFacing.SOUTH)
/*     */           {
/* 494 */             return EnumFacing.EAST.getHorizontalIndex();
/*     */           }
/*     */           
/* 497 */           if (enumfacing == EnumFacing.WEST)
/*     */           {
/* 499 */             return EnumFacing.NORTH.getHorizontalIndex();
/*     */           }
/*     */           
/* 502 */           if (enumfacing == EnumFacing.EAST)
/*     */           {
/* 504 */             return EnumFacing.SOUTH.getHorizontalIndex();
/*     */           }
/*     */         }
/* 507 */         else if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 509 */           if (enumfacing == EnumFacing.NORTH)
/*     */           {
/* 511 */             return EnumFacing.EAST.getHorizontalIndex();
/*     */           }
/*     */           
/* 514 */           if (enumfacing == EnumFacing.SOUTH)
/*     */           {
/* 516 */             return EnumFacing.WEST.getHorizontalIndex();
/*     */           }
/*     */           
/* 519 */           if (enumfacing == EnumFacing.WEST)
/*     */           {
/* 521 */             return EnumFacing.NORTH.getHorizontalIndex();
/*     */           }
/*     */           
/* 524 */           if (enumfacing == EnumFacing.EAST)
/*     */           {
/* 526 */             return EnumFacing.SOUTH.getHorizontalIndex();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 531 */     else if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */     {
/* 533 */       if (meta == 2)
/*     */       {
/* 535 */         return 3;
/*     */       }
/*     */       
/* 538 */       if (meta == 3)
/*     */       {
/* 540 */         return 2;
/*     */       }
/*     */     }
/* 543 */     else if (this.coordBaseMode == EnumFacing.WEST)
/*     */     {
/* 545 */       if (meta == 0)
/*     */       {
/* 547 */         return 2;
/*     */       }
/*     */       
/* 550 */       if (meta == 1)
/*     */       {
/* 552 */         return 3;
/*     */       }
/*     */       
/* 555 */       if (meta == 2)
/*     */       {
/* 557 */         return 0;
/*     */       }
/*     */       
/* 560 */       if (meta == 3)
/*     */       {
/* 562 */         return 1;
/*     */       }
/*     */     }
/* 565 */     else if (this.coordBaseMode == EnumFacing.EAST)
/*     */     {
/* 567 */       if (meta == 0)
/*     */       {
/* 569 */         return 2;
/*     */       }
/*     */       
/* 572 */       if (meta == 1)
/*     */       {
/* 574 */         return 3;
/*     */       }
/*     */       
/* 577 */       if (meta == 2)
/*     */       {
/* 579 */         return 1;
/*     */       }
/*     */       
/* 582 */       if (meta == 3)
/*     */       {
/* 584 */         return 0;
/*     */       }
/*     */     }
/*     */     
/* 588 */     return meta;
/*     */   }
/*     */   
/*     */   protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
/*     */   {
/* 593 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 595 */     if (boundingboxIn.isVecInside(blockpos))
/*     */     {
/* 597 */       worldIn.setBlockState(blockpos, blockstateIn, 2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected IBlockState getBlockStateFromPos(World worldIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
/*     */   {
/* 603 */     int i = getXWithOffset(x, z);
/* 604 */     int j = getYWithOffset(y);
/* 605 */     int k = getZWithOffset(x, z);
/* 606 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 607 */     return !boundingboxIn.isVecInside(blockpos) ? Blocks.air.getDefaultState() : worldIn.getBlockState(blockpos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fillWithAir(World worldIn, StructureBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
/*     */   {
/* 616 */     for (int i = minY; i <= maxY; i++)
/*     */     {
/* 618 */       for (int j = minX; j <= maxX; j++)
/*     */       {
/* 620 */         for (int k = minZ; k <= maxZ; k++)
/*     */         {
/* 622 */           setBlockState(worldIn, Blocks.air.getDefaultState(), j, i, k, structurebb);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState boundaryBlockState, IBlockState insideBlockState, boolean existingOnly)
/*     */   {
/* 633 */     for (int i = yMin; i <= yMax; i++)
/*     */     {
/* 635 */       for (int j = xMin; j <= xMax; j++)
/*     */       {
/* 637 */         for (int k = zMin; k <= zMax; k++)
/*     */         {
/* 639 */           if ((!existingOnly) || (getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air))
/*     */           {
/* 641 */             if ((i != yMin) && (i != yMax) && (j != xMin) && (j != xMax) && (k != zMin) && (k != zMax))
/*     */             {
/* 643 */               setBlockState(worldIn, insideBlockState, j, i, k, boundingboxIn);
/*     */             }
/*     */             else
/*     */             {
/* 647 */               setBlockState(worldIn, boundaryBlockState, j, i, k, boundingboxIn);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fillWithRandomizedBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean alwaysReplace, Random rand, BlockSelector blockselector)
/*     */   {
/* 661 */     for (int i = minY; i <= maxY; i++)
/*     */     {
/* 663 */       for (int j = minX; j <= maxX; j++)
/*     */       {
/* 665 */         for (int k = minZ; k <= maxZ; k++)
/*     */         {
/* 667 */           if ((!alwaysReplace) || (getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air))
/*     */           {
/* 669 */             blockselector.selectBlocks(rand, j, i, k, (i == minY) || (i == maxY) || (j == minX) || (j == maxX) || (k == minZ) || (k == maxZ));
/* 670 */             setBlockState(worldIn, blockselector.getBlockState(), j, i, k, boundingboxIn);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_175805_a(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstate1, IBlockState blockstate2, boolean p_175805_13_)
/*     */   {
/* 679 */     for (int i = minY; i <= maxY; i++)
/*     */     {
/* 681 */       for (int j = minX; j <= maxX; j++)
/*     */       {
/* 683 */         for (int k = minZ; k <= maxZ; k++)
/*     */         {
/* 685 */           if ((rand.nextFloat() <= chance) && ((!p_175805_13_) || (getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air)))
/*     */           {
/* 687 */             if ((i != minY) && (i != maxY) && (j != minX) && (j != maxX) && (k != minZ) && (k != maxZ))
/*     */             {
/* 689 */               setBlockState(worldIn, blockstate2, j, i, k, boundingboxIn);
/*     */             }
/*     */             else
/*     */             {
/* 693 */               setBlockState(worldIn, blockstate1, j, i, k, boundingboxIn);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void randomlyPlaceBlock(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int x, int y, int z, IBlockState blockstateIn)
/*     */   {
/* 703 */     if (rand.nextFloat() < chance)
/*     */     {
/* 705 */       setBlockState(worldIn, blockstateIn, x, y, z, boundingboxIn);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void randomlyRareFillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstateIn, boolean p_180777_10_)
/*     */   {
/* 711 */     float f = maxX - minX + 1;
/* 712 */     float f1 = maxY - minY + 1;
/* 713 */     float f2 = maxZ - minZ + 1;
/* 714 */     float f3 = minX + f / 2.0F;
/* 715 */     float f4 = minZ + f2 / 2.0F;
/*     */     
/* 717 */     for (int i = minY; i <= maxY; i++)
/*     */     {
/* 719 */       float f5 = (i - minY) / f1;
/*     */       
/* 721 */       for (int j = minX; j <= maxX; j++)
/*     */       {
/* 723 */         float f6 = (j - f3) / (f * 0.5F);
/*     */         
/* 725 */         for (int k = minZ; k <= maxZ; k++)
/*     */         {
/* 727 */           float f7 = (k - f4) / (f2 * 0.5F);
/*     */           
/* 729 */           if ((!p_180777_10_) || (getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air))
/*     */           {
/* 731 */             float f8 = f6 * f6 + f5 * f5 + f7 * f7;
/*     */             
/* 733 */             if (f8 <= 1.05F)
/*     */             {
/* 735 */               setBlockState(worldIn, blockstateIn, j, i, k, boundingboxIn);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void clearCurrentPositionBlocksUpwards(World worldIn, int x, int y, int z, StructureBoundingBox structurebb)
/*     */   {
/* 748 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 750 */     if (structurebb.isVecInside(blockpos))
/*     */     {
/* 752 */       while ((!worldIn.isAirBlock(blockpos)) && (blockpos.getY() < 255))
/*     */       {
/* 754 */         worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
/* 755 */         blockpos = blockpos.up();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
/*     */   {
/* 765 */     int i = getXWithOffset(x, z);
/* 766 */     int j = getYWithOffset(y);
/* 767 */     int k = getZWithOffset(x, z);
/*     */     
/* 769 */     if (boundingboxIn.isVecInside(new BlockPos(i, j, k)))
/*     */     {
/* 771 */       while (((worldIn.isAirBlock(new BlockPos(i, j, k))) || (worldIn.getBlockState(new BlockPos(i, j, k)).getBlock().getMaterial().isLiquid())) && (j > 1))
/*     */       {
/* 773 */         worldIn.setBlockState(new BlockPos(i, j, k), blockstateIn, 2);
/* 774 */         j--;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, List<WeightedRandomChestContent> listIn, int max)
/*     */   {
/* 781 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 783 */     if ((boundingBoxIn.isVecInside(blockpos)) && (worldIn.getBlockState(blockpos).getBlock() != Blocks.chest))
/*     */     {
/* 785 */       IBlockState iblockstate = Blocks.chest.getDefaultState();
/* 786 */       worldIn.setBlockState(blockpos, Blocks.chest.correctFacing(worldIn, blockpos, iblockstate), 2);
/* 787 */       TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */       
/* 789 */       if ((tileentity instanceof TileEntityChest))
/*     */       {
/* 791 */         WeightedRandomChestContent.generateChestContents(rand, listIn, (TileEntityChest)tileentity, max);
/*     */       }
/*     */       
/* 794 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 798 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean generateDispenserContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, int meta, List<WeightedRandomChestContent> listIn, int max)
/*     */   {
/* 804 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 806 */     if ((boundingBoxIn.isVecInside(blockpos)) && (worldIn.getBlockState(blockpos).getBlock() != Blocks.dispenser))
/*     */     {
/* 808 */       worldIn.setBlockState(blockpos, Blocks.dispenser.getStateFromMeta(getMetadataWithOffset(Blocks.dispenser, meta)), 2);
/* 809 */       TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */       
/* 811 */       if ((tileentity instanceof TileEntityDispenser))
/*     */       {
/* 813 */         WeightedRandomChestContent.generateDispenserContents(rand, listIn, (TileEntityDispenser)tileentity, max);
/*     */       }
/*     */       
/* 816 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 820 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void placeDoorCurrentPosition(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, EnumFacing facing)
/*     */   {
/* 829 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 831 */     if (boundingBoxIn.isVecInside(blockpos))
/*     */     {
/* 833 */       ItemDoor.placeDoor(worldIn, blockpos, facing.rotateYCCW(), Blocks.oak_door);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_181138_a(int p_181138_1_, int p_181138_2_, int p_181138_3_)
/*     */   {
/* 839 */     this.boundingBox.offset(p_181138_1_, p_181138_2_, p_181138_3_);
/*     */   }
/*     */   
/*     */   public static abstract class BlockSelector
/*     */   {
/* 844 */     protected IBlockState blockstate = Blocks.air.getDefaultState();
/*     */     
/*     */     public abstract void selectBlocks(Random paramRandom, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
/*     */     
/*     */     public IBlockState getBlockState()
/*     */     {
/* 850 */       return this.blockstate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */