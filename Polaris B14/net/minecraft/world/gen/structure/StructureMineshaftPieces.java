/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.item.EntityMinecartChest;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemEnchantedBook;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.WeightedRandomChestContent;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class StructureMineshaftPieces
/*     */ {
/*  25 */   private static final List<WeightedRandomChestContent> CHEST_CONTENT_WEIGHT_LIST = Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), 4, 9, 5), new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1), new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1) });
/*     */   
/*     */   public static void registerStructurePieces()
/*     */   {
/*  29 */     MapGenStructureIO.registerStructureComponent(Corridor.class, "MSCorridor");
/*  30 */     MapGenStructureIO.registerStructureComponent(Cross.class, "MSCrossing");
/*  31 */     MapGenStructureIO.registerStructureComponent(Room.class, "MSRoom");
/*  32 */     MapGenStructureIO.registerStructureComponent(Stairs.class, "MSStairs");
/*     */   }
/*     */   
/*     */   private static StructureComponent func_175892_a(List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing, int type)
/*     */   {
/*  37 */     int i = rand.nextInt(100);
/*     */     
/*  39 */     if (i >= 80)
/*     */     {
/*  41 */       StructureBoundingBox structureboundingbox = Cross.func_175813_a(listIn, rand, x, y, z, facing);
/*     */       
/*  43 */       if (structureboundingbox != null)
/*     */       {
/*  45 */         return new Cross(type, rand, structureboundingbox, facing);
/*     */       }
/*     */     }
/*  48 */     else if (i >= 70)
/*     */     {
/*  50 */       StructureBoundingBox structureboundingbox1 = Stairs.func_175812_a(listIn, rand, x, y, z, facing);
/*     */       
/*  52 */       if (structureboundingbox1 != null)
/*     */       {
/*  54 */         return new Stairs(type, rand, structureboundingbox1, facing);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  59 */       StructureBoundingBox structureboundingbox2 = Corridor.func_175814_a(listIn, rand, x, y, z, facing);
/*     */       
/*  61 */       if (structureboundingbox2 != null)
/*     */       {
/*  63 */         return new Corridor(type, rand, structureboundingbox2, facing);
/*     */       }
/*     */     }
/*     */     
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   private static StructureComponent func_175890_b(StructureComponent componentIn, List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing, int type)
/*     */   {
/*  72 */     if (type > 8)
/*     */     {
/*  74 */       return null;
/*     */     }
/*  76 */     if ((Math.abs(x - componentIn.getBoundingBox().minX) <= 80) && (Math.abs(z - componentIn.getBoundingBox().minZ) <= 80))
/*     */     {
/*  78 */       StructureComponent structurecomponent = func_175892_a(listIn, rand, x, y, z, facing, type + 1);
/*     */       
/*  80 */       if (structurecomponent != null)
/*     */       {
/*  82 */         listIn.add(structurecomponent);
/*  83 */         structurecomponent.buildComponent(componentIn, listIn, rand);
/*     */       }
/*     */       
/*  86 */       return structurecomponent;
/*     */     }
/*     */     
/*     */ 
/*  90 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Corridor
/*     */     extends StructureComponent
/*     */   {
/*     */     private boolean hasRails;
/*     */     
/*     */     private boolean hasSpiders;
/*     */     private boolean spawnerPlaced;
/*     */     private int sectionCount;
/*     */     
/*     */     public Corridor() {}
/*     */     
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 107 */       tagCompound.setBoolean("hr", this.hasRails);
/* 108 */       tagCompound.setBoolean("sc", this.hasSpiders);
/* 109 */       tagCompound.setBoolean("hps", this.spawnerPlaced);
/* 110 */       tagCompound.setInteger("Num", this.sectionCount);
/*     */     }
/*     */     
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 115 */       this.hasRails = tagCompound.getBoolean("hr");
/* 116 */       this.hasSpiders = tagCompound.getBoolean("sc");
/* 117 */       this.spawnerPlaced = tagCompound.getBoolean("hps");
/* 118 */       this.sectionCount = tagCompound.getInteger("Num");
/*     */     }
/*     */     
/*     */     public Corridor(int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing)
/*     */     {
/* 123 */       super();
/* 124 */       this.coordBaseMode = facing;
/* 125 */       this.boundingBox = structurebb;
/* 126 */       this.hasRails = (rand.nextInt(3) == 0);
/* 127 */       this.hasSpiders = ((!this.hasRails) && (rand.nextInt(23) == 0));
/*     */       
/* 129 */       if ((this.coordBaseMode != EnumFacing.NORTH) && (this.coordBaseMode != EnumFacing.SOUTH))
/*     */       {
/* 131 */         this.sectionCount = (structurebb.getXSize() / 5);
/*     */       }
/*     */       else
/*     */       {
/* 135 */         this.sectionCount = (structurebb.getZSize() / 5);
/*     */       }
/*     */     }
/*     */     
/*     */     public static StructureBoundingBox func_175814_a(List<StructureComponent> p_175814_0_, Random rand, int x, int y, int z, EnumFacing facing)
/*     */     {
/* 141 */       StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
/*     */       
/*     */ 
/* 144 */       for (int i = rand.nextInt(3) + 2; i > 0; i--)
/*     */       {
/* 146 */         int j = i * 5;
/*     */         
/* 148 */         switch (facing)
/*     */         {
/*     */         case NORTH: 
/* 151 */           structureboundingbox.maxX = (x + 2);
/* 152 */           structureboundingbox.minZ = (z - (j - 1));
/* 153 */           break;
/*     */         
/*     */         case SOUTH: 
/* 156 */           structureboundingbox.maxX = (x + 2);
/* 157 */           structureboundingbox.maxZ = (z + (j - 1));
/* 158 */           break;
/*     */         
/*     */         case UP: 
/* 161 */           structureboundingbox.minX = (x - (j - 1));
/* 162 */           structureboundingbox.maxZ = (z + 2);
/* 163 */           break;
/*     */         
/*     */         case WEST: 
/* 166 */           structureboundingbox.maxX = (x + (j - 1));
/* 167 */           structureboundingbox.maxZ = (z + 2);
/*     */         }
/*     */         
/* 170 */         if (StructureComponent.findIntersecting(p_175814_0_, structureboundingbox) == null) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 176 */       return i > 0 ? structureboundingbox : null;
/*     */     }
/*     */     
/*     */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*     */     {
/* 181 */       int i = getComponentType();
/* 182 */       int j = rand.nextInt(4);
/*     */       
/* 184 */       if (this.coordBaseMode != null)
/*     */       {
/* 186 */         switch (this.coordBaseMode)
/*     */         {
/*     */         case NORTH: 
/* 189 */           if (j <= 1)
/*     */           {
/* 191 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, this.coordBaseMode, i);
/*     */           }
/* 193 */           else if (j == 2)
/*     */           {
/* 195 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, i);
/*     */           }
/*     */           else
/*     */           {
/* 199 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, i);
/*     */           }
/*     */           
/* 202 */           break;
/*     */         
/*     */         case SOUTH: 
/* 205 */           if (j <= 1)
/*     */           {
/* 207 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, this.coordBaseMode, i);
/*     */           }
/* 209 */           else if (j == 2)
/*     */           {
/* 211 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, i);
/*     */           }
/*     */           else
/*     */           {
/* 215 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, i);
/*     */           }
/*     */           
/* 218 */           break;
/*     */         
/*     */         case UP: 
/* 221 */           if (j <= 1)
/*     */           {
/* 223 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
/*     */           }
/* 225 */           else if (j == 2)
/*     */           {
/* 227 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/*     */           }
/*     */           else
/*     */           {
/* 231 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/*     */           }
/*     */           
/* 234 */           break;
/*     */         
/*     */         case WEST: 
/* 237 */           if (j <= 1)
/*     */           {
/* 239 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, i);
/*     */           }
/* 241 */           else if (j == 2)
/*     */           {
/* 243 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/*     */           }
/*     */           else
/*     */           {
/* 247 */             StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + rand.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/*     */           }
/*     */           break;
/*     */         }
/*     */       }
/* 252 */       if (i < 8)
/*     */       {
/* 254 */         if ((this.coordBaseMode != EnumFacing.NORTH) && (this.coordBaseMode != EnumFacing.SOUTH))
/*     */         {
/* 256 */           for (int i1 = this.boundingBox.minX + 3; i1 + 3 <= this.boundingBox.maxX; i1 += 5)
/*     */           {
/* 258 */             int j1 = rand.nextInt(5);
/*     */             
/* 260 */             if (j1 == 0)
/*     */             {
/* 262 */               StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, i1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i + 1);
/*     */             }
/* 264 */             else if (j1 == 1)
/*     */             {
/* 266 */               StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, i1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i + 1);
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         } else {
/* 272 */           for (int k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5)
/*     */           {
/* 274 */             int l = rand.nextInt(5);
/*     */             
/* 276 */             if (l == 0)
/*     */             {
/* 278 */               StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, k, EnumFacing.WEST, i + 1);
/*     */             }
/* 280 */             else if (l == 1)
/*     */             {
/* 282 */               StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, k, EnumFacing.EAST, i + 1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, List<WeightedRandomChestContent> listIn, int max)
/*     */     {
/* 291 */       BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */       
/* 293 */       if ((boundingBoxIn.isVecInside(blockpos)) && (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air))
/*     */       {
/* 295 */         int i = rand.nextBoolean() ? 1 : 0;
/* 296 */         worldIn.setBlockState(blockpos, Blocks.rail.getStateFromMeta(getMetadataWithOffset(Blocks.rail, i)), 2);
/* 297 */         EntityMinecartChest entityminecartchest = new EntityMinecartChest(worldIn, blockpos.getX() + 0.5F, blockpos.getY() + 0.5F, blockpos.getZ() + 0.5F);
/* 298 */         WeightedRandomChestContent.generateChestContents(rand, listIn, entityminecartchest, max);
/* 299 */         worldIn.spawnEntityInWorld(entityminecartchest);
/* 300 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 304 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*     */     {
/* 310 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*     */       {
/* 312 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 316 */       int i = 0;
/* 317 */       int j = 2;
/* 318 */       int k = 0;
/* 319 */       int l = 2;
/* 320 */       int i1 = this.sectionCount * 5 - 1;
/* 321 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 1, i1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 322 */       func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.8F, 0, 2, 0, 2, 2, i1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       
/* 324 */       if (this.hasSpiders)
/*     */       {
/* 326 */         func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.6F, 0, 0, 0, 2, 1, i1, Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       }
/*     */       
/* 329 */       for (int j1 = 0; j1 < this.sectionCount; j1++)
/*     */       {
/* 331 */         int k1 = 2 + j1 * 5;
/* 332 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, k1, 0, 1, k1, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 333 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, k1, 2, 1, k1, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */         
/* 335 */         if (randomIn.nextInt(4) == 0)
/*     */         {
/* 337 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k1, 0, 2, k1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 338 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, k1, 2, 2, k1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */         }
/*     */         else
/*     */         {
/* 342 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k1, 2, 2, k1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */         }
/*     */         
/* 345 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 0, 2, k1 - 1, Blocks.web.getDefaultState());
/* 346 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 2, 2, k1 - 1, Blocks.web.getDefaultState());
/* 347 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 0, 2, k1 + 1, Blocks.web.getDefaultState());
/* 348 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 2, 2, k1 + 1, Blocks.web.getDefaultState());
/* 349 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 0, 2, k1 - 2, Blocks.web.getDefaultState());
/* 350 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 2, 2, k1 - 2, Blocks.web.getDefaultState());
/* 351 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 0, 2, k1 + 2, Blocks.web.getDefaultState());
/* 352 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 2, 2, k1 + 2, Blocks.web.getDefaultState());
/* 353 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 1, 2, k1 - 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
/* 354 */         randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05F, 1, 2, k1 + 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
/*     */         
/* 356 */         if (randomIn.nextInt(100) == 0)
/*     */         {
/* 358 */           generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 2, 0, k1 - 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.CHEST_CONTENT_WEIGHT_LIST, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 3 + randomIn.nextInt(4));
/*     */         }
/*     */         
/* 361 */         if (randomIn.nextInt(100) == 0)
/*     */         {
/* 363 */           generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 0, 0, k1 + 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.CHEST_CONTENT_WEIGHT_LIST, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 3 + randomIn.nextInt(4));
/*     */         }
/*     */         
/* 366 */         if ((this.hasSpiders) && (!this.spawnerPlaced))
/*     */         {
/* 368 */           int l1 = getYWithOffset(0);
/* 369 */           int i2 = k1 - 1 + randomIn.nextInt(3);
/* 370 */           int j2 = getXWithOffset(1, i2);
/* 371 */           i2 = getZWithOffset(1, i2);
/* 372 */           BlockPos blockpos = new BlockPos(j2, l1, i2);
/*     */           
/* 374 */           if (structureBoundingBoxIn.isVecInside(blockpos))
/*     */           {
/* 376 */             this.spawnerPlaced = true;
/* 377 */             worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
/* 378 */             net.minecraft.tileentity.TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */             
/* 380 */             if ((tileentity instanceof TileEntityMobSpawner))
/*     */             {
/* 382 */               ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("CaveSpider");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 388 */       for (int k2 = 0; k2 <= 2; k2++)
/*     */       {
/* 390 */         for (int i3 = 0; i3 <= i1; i3++)
/*     */         {
/* 392 */           int j3 = -1;
/* 393 */           IBlockState iblockstate1 = getBlockStateFromPos(worldIn, k2, j3, i3, structureBoundingBoxIn);
/*     */           
/* 395 */           if (iblockstate1.getBlock().getMaterial() == Material.air)
/*     */           {
/* 397 */             int k3 = -1;
/* 398 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), k2, k3, i3, structureBoundingBoxIn);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 403 */       if (this.hasRails)
/*     */       {
/* 405 */         for (int l2 = 0; l2 <= i1; l2++)
/*     */         {
/* 407 */           IBlockState iblockstate = getBlockStateFromPos(worldIn, 1, -1, l2, structureBoundingBoxIn);
/*     */           
/* 409 */           if ((iblockstate.getBlock().getMaterial() != Material.air) && (iblockstate.getBlock().isFullBlock()))
/*     */           {
/* 411 */             randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.7F, 1, 0, l2, Blocks.rail.getStateFromMeta(getMetadataWithOffset(Blocks.rail, 0)));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 416 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Cross
/*     */     extends StructureComponent
/*     */   {
/*     */     private EnumFacing corridorDirection;
/*     */     
/*     */     private boolean isMultipleFloors;
/*     */     
/*     */     public Cross() {}
/*     */     
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 432 */       tagCompound.setBoolean("tf", this.isMultipleFloors);
/* 433 */       tagCompound.setInteger("D", this.corridorDirection.getHorizontalIndex());
/*     */     }
/*     */     
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 438 */       this.isMultipleFloors = tagCompound.getBoolean("tf");
/* 439 */       this.corridorDirection = EnumFacing.getHorizontal(tagCompound.getInteger("D"));
/*     */     }
/*     */     
/*     */     public Cross(int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing)
/*     */     {
/* 444 */       super();
/* 445 */       this.corridorDirection = facing;
/* 446 */       this.boundingBox = structurebb;
/* 447 */       this.isMultipleFloors = (structurebb.getYSize() > 3);
/*     */     }
/*     */     
/*     */     public static StructureBoundingBox func_175813_a(List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing)
/*     */     {
/* 452 */       StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
/*     */       
/* 454 */       if (rand.nextInt(4) == 0)
/*     */       {
/* 456 */         structureboundingbox.maxY += 4;
/*     */       }
/*     */       
/* 459 */       switch (facing)
/*     */       {
/*     */       case NORTH: 
/* 462 */         structureboundingbox.minX = (x - 1);
/* 463 */         structureboundingbox.maxX = (x + 3);
/* 464 */         structureboundingbox.minZ = (z - 4);
/* 465 */         break;
/*     */       
/*     */       case SOUTH: 
/* 468 */         structureboundingbox.minX = (x - 1);
/* 469 */         structureboundingbox.maxX = (x + 3);
/* 470 */         structureboundingbox.maxZ = (z + 4);
/* 471 */         break;
/*     */       
/*     */       case UP: 
/* 474 */         structureboundingbox.minX = (x - 4);
/* 475 */         structureboundingbox.minZ = (z - 1);
/* 476 */         structureboundingbox.maxZ = (z + 3);
/* 477 */         break;
/*     */       
/*     */       case WEST: 
/* 480 */         structureboundingbox.maxX = (x + 4);
/* 481 */         structureboundingbox.minZ = (z - 1);
/* 482 */         structureboundingbox.maxZ = (z + 3);
/*     */       }
/*     */       
/* 485 */       return StructureComponent.findIntersecting(listIn, structureboundingbox) != null ? null : structureboundingbox;
/*     */     }
/*     */     
/*     */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*     */     {
/* 490 */       int i = getComponentType();
/*     */       
/* 492 */       switch (this.corridorDirection)
/*     */       {
/*     */       case NORTH: 
/* 495 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/* 496 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
/* 497 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
/* 498 */         break;
/*     */       
/*     */       case SOUTH: 
/* 501 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/* 502 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
/* 503 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
/* 504 */         break;
/*     */       
/*     */       case UP: 
/* 507 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/* 508 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/* 509 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
/* 510 */         break;
/*     */       
/*     */       case WEST: 
/* 513 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/* 514 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/* 515 */         StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
/*     */       }
/*     */       
/* 518 */       if (this.isMultipleFloors)
/*     */       {
/* 520 */         if (rand.nextBoolean())
/*     */         {
/* 522 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/*     */         }
/*     */         
/* 525 */         if (rand.nextBoolean())
/*     */         {
/* 527 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, i);
/*     */         }
/*     */         
/* 530 */         if (rand.nextBoolean())
/*     */         {
/* 532 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, i);
/*     */         }
/*     */         
/* 535 */         if (rand.nextBoolean())
/*     */         {
/* 537 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*     */     {
/* 544 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*     */       {
/* 546 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 550 */       if (this.isMultipleFloors)
/*     */       {
/* 552 */         fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 553 */         fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 554 */         fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 555 */         fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 556 */         fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       }
/*     */       else
/*     */       {
/* 560 */         fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 561 */         fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       }
/*     */       
/* 564 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 565 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 566 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 567 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       
/* 569 */       for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; i++)
/*     */       {
/* 571 */         for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; j++)
/*     */         {
/* 573 */           if (getBlockStateFromPos(worldIn, i, this.boundingBox.minY - 1, j, structureBoundingBoxIn).getBlock().getMaterial() == Material.air)
/*     */           {
/* 575 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), i, this.boundingBox.minY - 1, j, structureBoundingBoxIn);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 580 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Room
/*     */     extends StructureComponent
/*     */   {
/* 587 */     private List<StructureBoundingBox> roomsLinkedToTheRoom = Lists.newLinkedList();
/*     */     
/*     */ 
/*     */     public Room() {}
/*     */     
/*     */ 
/*     */     public Room(int type, Random rand, int x, int z)
/*     */     {
/* 595 */       super();
/* 596 */       this.boundingBox = new StructureBoundingBox(x, 50, z, x + 7 + rand.nextInt(6), 54 + rand.nextInt(6), z + 7 + rand.nextInt(6));
/*     */     }
/*     */     
/*     */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*     */     {
/* 601 */       int i = getComponentType();
/* 602 */       int j = this.boundingBox.getYSize() - 3 - 1;
/*     */       
/* 604 */       if (j <= 0)
/*     */       {
/* 606 */         j = 1;
/*     */       }
/*     */       
/* 609 */       for (int k = 0; k < this.boundingBox.getXSize(); k += 4)
/*     */       {
/* 611 */         k += rand.nextInt(this.boundingBox.getXSize());
/*     */         
/* 613 */         if (k + 3 > this.boundingBox.getXSize()) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 618 */         StructureComponent structurecomponent = StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/*     */         
/* 620 */         if (structurecomponent != null)
/*     */         {
/* 622 */           StructureBoundingBox structureboundingbox = structurecomponent.getBoundingBox();
/* 623 */           this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
/*     */         }
/*     */       }
/*     */       
/* 627 */       for (int k = 0; k < this.boundingBox.getXSize(); k += 4)
/*     */       {
/* 629 */         k += rand.nextInt(this.boundingBox.getXSize());
/*     */         
/* 631 */         if (k + 3 > this.boundingBox.getXSize()) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 636 */         StructureComponent structurecomponent1 = StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX + k, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/*     */         
/* 638 */         if (structurecomponent1 != null)
/*     */         {
/* 640 */           StructureBoundingBox structureboundingbox1 = structurecomponent1.getBoundingBox();
/* 641 */           this.roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox1.minX, structureboundingbox1.minY, this.boundingBox.maxZ - 1, structureboundingbox1.maxX, structureboundingbox1.maxY, this.boundingBox.maxZ));
/*     */         }
/*     */       }
/*     */       
/* 645 */       for (int k = 0; k < this.boundingBox.getZSize(); k += 4)
/*     */       {
/* 647 */         k += rand.nextInt(this.boundingBox.getZSize());
/*     */         
/* 649 */         if (k + 3 > this.boundingBox.getZSize()) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 654 */         StructureComponent structurecomponent2 = StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.WEST, i);
/*     */         
/* 656 */         if (structurecomponent2 != null)
/*     */         {
/* 658 */           StructureBoundingBox structureboundingbox2 = structurecomponent2.getBoundingBox();
/* 659 */           this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, structureboundingbox2.minY, structureboundingbox2.minZ, this.boundingBox.minX + 1, structureboundingbox2.maxY, structureboundingbox2.maxZ));
/*     */         }
/*     */       }
/*     */       
/* 663 */       for (int k = 0; k < this.boundingBox.getZSize(); k += 4)
/*     */       {
/* 665 */         k += rand.nextInt(this.boundingBox.getZSize());
/*     */         
/* 667 */         if (k + 3 > this.boundingBox.getZSize()) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 672 */         StructureComponent structurecomponent3 = StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + rand.nextInt(j) + 1, this.boundingBox.minZ + k, EnumFacing.EAST, i);
/*     */         
/* 674 */         if (structurecomponent3 != null)
/*     */         {
/* 676 */           StructureBoundingBox structureboundingbox3 = structurecomponent3.getBoundingBox();
/* 677 */           this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox3.minY, structureboundingbox3.minZ, this.boundingBox.maxX, structureboundingbox3.maxY, structureboundingbox3.maxZ));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*     */     {
/* 684 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*     */       {
/* 686 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 690 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
/* 691 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       
/* 693 */       for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom)
/*     */       {
/* 695 */         fillWithBlocks(worldIn, structureBoundingBoxIn, structureboundingbox.minX, structureboundingbox.maxY - 2, structureboundingbox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       }
/*     */       
/* 698 */       randomlyRareFillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
/* 699 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_181138_a(int p_181138_1_, int p_181138_2_, int p_181138_3_)
/*     */     {
/* 705 */       super.func_181138_a(p_181138_1_, p_181138_2_, p_181138_3_);
/*     */       
/* 707 */       for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom)
/*     */       {
/* 709 */         structureboundingbox.offset(p_181138_1_, p_181138_2_, p_181138_3_);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 715 */       NBTTagList nbttaglist = new NBTTagList();
/*     */       
/* 717 */       for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom)
/*     */       {
/* 719 */         nbttaglist.appendTag(structureboundingbox.toNBTTagIntArray());
/*     */       }
/*     */       
/* 722 */       tagCompound.setTag("Entrances", nbttaglist);
/*     */     }
/*     */     
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 727 */       NBTTagList nbttaglist = tagCompound.getTagList("Entrances", 11);
/*     */       
/* 729 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/* 731 */         this.roomsLinkedToTheRoom.add(new StructureBoundingBox(nbttaglist.getIntArrayAt(i)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Stairs
/*     */     extends StructureComponent
/*     */   {
/*     */     public Stairs() {}
/*     */     
/*     */     public Stairs(int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing)
/*     */     {
/* 744 */       super();
/* 745 */       this.coordBaseMode = facing;
/* 746 */       this.boundingBox = structurebb;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {}
/*     */     
/*     */ 
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {}
/*     */     
/*     */ 
/*     */     public static StructureBoundingBox func_175812_a(List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing)
/*     */     {
/* 759 */       StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y - 5, z, x, y + 2, z);
/*     */       
/* 761 */       switch (facing)
/*     */       {
/*     */       case NORTH: 
/* 764 */         structureboundingbox.maxX = (x + 2);
/* 765 */         structureboundingbox.minZ = (z - 8);
/* 766 */         break;
/*     */       
/*     */       case SOUTH: 
/* 769 */         structureboundingbox.maxX = (x + 2);
/* 770 */         structureboundingbox.maxZ = (z + 8);
/* 771 */         break;
/*     */       
/*     */       case UP: 
/* 774 */         structureboundingbox.minX = (x - 8);
/* 775 */         structureboundingbox.maxZ = (z + 2);
/* 776 */         break;
/*     */       
/*     */       case WEST: 
/* 779 */         structureboundingbox.maxX = (x + 8);
/* 780 */         structureboundingbox.maxZ = (z + 2);
/*     */       }
/*     */       
/* 783 */       return StructureComponent.findIntersecting(listIn, structureboundingbox) != null ? null : structureboundingbox;
/*     */     }
/*     */     
/*     */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*     */     {
/* 788 */       int i = getComponentType();
/*     */       
/* 790 */       if (this.coordBaseMode != null)
/*     */       {
/* 792 */         switch (this.coordBaseMode)
/*     */         {
/*     */         case NORTH: 
/* 795 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, i);
/* 796 */           break;
/*     */         
/*     */         case SOUTH: 
/* 799 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, i);
/* 800 */           break;
/*     */         
/*     */         case UP: 
/* 803 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, i);
/* 804 */           break;
/*     */         
/*     */         case WEST: 
/* 807 */           StructureMineshaftPieces.func_175890_b(componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, i);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*     */     {
/* 814 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*     */       {
/* 816 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 820 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 821 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       
/* 823 */       for (int i = 0; i < 5; i++)
/*     */       {
/* 825 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       }
/*     */       
/* 828 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureMineshaftPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */