/*      */ package net.minecraft.world.gen.structure;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockSandStone.EnumType;
/*      */ import net.minecraft.block.BlockSlab;
/*      */ import net.minecraft.block.BlockStairs;
/*      */ import net.minecraft.block.BlockStaticLiquid;
/*      */ import net.minecraft.block.BlockTorch;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.entity.passive.EntityVillager;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*      */ import net.minecraft.util.WeightedRandomChestContent;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ 
/*      */ public class StructureVillagePieces
/*      */ {
/*      */   public static void registerVillagePieces()
/*      */   {
/*   33 */     MapGenStructureIO.registerStructureComponent(House1.class, "ViBH");
/*   34 */     MapGenStructureIO.registerStructureComponent(Field1.class, "ViDF");
/*   35 */     MapGenStructureIO.registerStructureComponent(Field2.class, "ViF");
/*   36 */     MapGenStructureIO.registerStructureComponent(Torch.class, "ViL");
/*   37 */     MapGenStructureIO.registerStructureComponent(Hall.class, "ViPH");
/*   38 */     MapGenStructureIO.registerStructureComponent(House4Garden.class, "ViSH");
/*   39 */     MapGenStructureIO.registerStructureComponent(WoodHut.class, "ViSmH");
/*   40 */     MapGenStructureIO.registerStructureComponent(Church.class, "ViST");
/*   41 */     MapGenStructureIO.registerStructureComponent(House2.class, "ViS");
/*   42 */     MapGenStructureIO.registerStructureComponent(Start.class, "ViStart");
/*   43 */     MapGenStructureIO.registerStructureComponent(Path.class, "ViSR");
/*   44 */     MapGenStructureIO.registerStructureComponent(House3.class, "ViTRH");
/*   45 */     MapGenStructureIO.registerStructureComponent(Well.class, "ViW");
/*      */   }
/*      */   
/*      */   public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, int p_75084_1_)
/*      */   {
/*   50 */     List<PieceWeight> list = Lists.newArrayList();
/*   51 */     list.add(new PieceWeight(House4Garden.class, 4, MathHelper.getRandomIntegerInRange(random, 2 + p_75084_1_, 4 + p_75084_1_ * 2)));
/*   52 */     list.add(new PieceWeight(Church.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + p_75084_1_, 1 + p_75084_1_)));
/*   53 */     list.add(new PieceWeight(House1.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + p_75084_1_, 2 + p_75084_1_)));
/*   54 */     list.add(new PieceWeight(WoodHut.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + p_75084_1_, 5 + p_75084_1_ * 3)));
/*   55 */     list.add(new PieceWeight(Hall.class, 15, MathHelper.getRandomIntegerInRange(random, 0 + p_75084_1_, 2 + p_75084_1_)));
/*   56 */     list.add(new PieceWeight(Field1.class, 3, MathHelper.getRandomIntegerInRange(random, 1 + p_75084_1_, 4 + p_75084_1_)));
/*   57 */     list.add(new PieceWeight(Field2.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + p_75084_1_, 4 + p_75084_1_ * 2)));
/*   58 */     list.add(new PieceWeight(House2.class, 15, MathHelper.getRandomIntegerInRange(random, 0, 1 + p_75084_1_)));
/*   59 */     list.add(new PieceWeight(House3.class, 8, MathHelper.getRandomIntegerInRange(random, 0 + p_75084_1_, 3 + p_75084_1_ * 2)));
/*   60 */     Iterator<PieceWeight> iterator = list.iterator();
/*      */     
/*   62 */     while (iterator.hasNext())
/*      */     {
/*   64 */       if (((PieceWeight)iterator.next()).villagePiecesLimit == 0)
/*      */       {
/*   66 */         iterator.remove();
/*      */       }
/*      */     }
/*      */     
/*   70 */     return list;
/*      */   }
/*      */   
/*      */   private static int func_75079_a(List<PieceWeight> p_75079_0_)
/*      */   {
/*   75 */     boolean flag = false;
/*   76 */     int i = 0;
/*      */     
/*   78 */     for (PieceWeight structurevillagepieces$pieceweight : p_75079_0_)
/*      */     {
/*   80 */       if ((structurevillagepieces$pieceweight.villagePiecesLimit > 0) && (structurevillagepieces$pieceweight.villagePiecesSpawned < structurevillagepieces$pieceweight.villagePiecesLimit))
/*      */       {
/*   82 */         flag = true;
/*      */       }
/*      */       
/*   85 */       i += structurevillagepieces$pieceweight.villagePieceWeight;
/*      */     }
/*      */     
/*   88 */     return flag ? i : -1;
/*      */   }
/*      */   
/*      */   private static Village func_176065_a(Start start, PieceWeight weight, List<StructureComponent> p_176065_2_, Random rand, int p_176065_4_, int p_176065_5_, int p_176065_6_, EnumFacing facing, int p_176065_8_)
/*      */   {
/*   93 */     Class<? extends Village> oclass = weight.villagePieceClass;
/*   94 */     Village structurevillagepieces$village = null;
/*      */     
/*   96 */     if (oclass == House4Garden.class)
/*      */     {
/*   98 */       structurevillagepieces$village = House4Garden.func_175858_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  100 */     else if (oclass == Church.class)
/*      */     {
/*  102 */       structurevillagepieces$village = Church.func_175854_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  104 */     else if (oclass == House1.class)
/*      */     {
/*  106 */       structurevillagepieces$village = House1.func_175850_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  108 */     else if (oclass == WoodHut.class)
/*      */     {
/*  110 */       structurevillagepieces$village = WoodHut.func_175853_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  112 */     else if (oclass == Hall.class)
/*      */     {
/*  114 */       structurevillagepieces$village = Hall.func_175857_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  116 */     else if (oclass == Field1.class)
/*      */     {
/*  118 */       structurevillagepieces$village = Field1.func_175851_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  120 */     else if (oclass == Field2.class)
/*      */     {
/*  122 */       structurevillagepieces$village = Field2.func_175852_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  124 */     else if (oclass == House2.class)
/*      */     {
/*  126 */       structurevillagepieces$village = House2.func_175855_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*  128 */     else if (oclass == House3.class)
/*      */     {
/*  130 */       structurevillagepieces$village = House3.func_175849_a(start, p_176065_2_, rand, p_176065_4_, p_176065_5_, p_176065_6_, facing, p_176065_8_);
/*      */     }
/*      */     
/*  133 */     return structurevillagepieces$village;
/*      */   }
/*      */   
/*      */   private static Village func_176067_c(Start start, List<StructureComponent> p_176067_1_, Random rand, int p_176067_3_, int p_176067_4_, int p_176067_5_, EnumFacing facing, int p_176067_7_)
/*      */   {
/*  138 */     int i = func_75079_a(start.structureVillageWeightedPieceList);
/*      */     
/*  140 */     if (i <= 0)
/*      */     {
/*  142 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  146 */     int j = 0;
/*      */     Iterator localIterator;
/*  148 */     label183: for (; j < 5; 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  153 */         localIterator.hasNext())
/*      */     {
/*  150 */       j++;
/*  151 */       int k = rand.nextInt(i);
/*      */       
/*  153 */       localIterator = start.structureVillageWeightedPieceList.iterator(); continue;PieceWeight structurevillagepieces$pieceweight = (PieceWeight)localIterator.next();
/*      */       
/*  155 */       k -= structurevillagepieces$pieceweight.villagePieceWeight;
/*      */       
/*  157 */       if (k < 0)
/*      */       {
/*  159 */         if ((!structurevillagepieces$pieceweight.canSpawnMoreVillagePiecesOfType(p_176067_7_)) || ((structurevillagepieces$pieceweight == start.structVillagePieceWeight) && (start.structureVillageWeightedPieceList.size() > 1))) {
/*      */           break label183;
/*      */         }
/*      */         
/*      */ 
/*  164 */         Village structurevillagepieces$village = func_176065_a(start, structurevillagepieces$pieceweight, p_176067_1_, rand, p_176067_3_, p_176067_4_, p_176067_5_, facing, p_176067_7_);
/*      */         
/*  166 */         if (structurevillagepieces$village != null)
/*      */         {
/*  168 */           structurevillagepieces$pieceweight.villagePiecesSpawned += 1;
/*  169 */           start.structVillagePieceWeight = structurevillagepieces$pieceweight;
/*      */           
/*  171 */           if (!structurevillagepieces$pieceweight.canSpawnMoreVillagePieces())
/*      */           {
/*  173 */             start.structureVillageWeightedPieceList.remove(structurevillagepieces$pieceweight);
/*      */           }
/*      */           
/*  176 */           return structurevillagepieces$village;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  182 */     StructureBoundingBox structureboundingbox = Torch.func_175856_a(start, p_176067_1_, rand, p_176067_3_, p_176067_4_, p_176067_5_, facing);
/*      */     
/*  184 */     if (structureboundingbox != null)
/*      */     {
/*  186 */       return new Torch(start, p_176067_7_, rand, structureboundingbox, facing);
/*      */     }
/*      */     
/*      */ 
/*  190 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static StructureComponent func_176066_d(Start start, List<StructureComponent> p_176066_1_, Random rand, int p_176066_3_, int p_176066_4_, int p_176066_5_, EnumFacing facing, int p_176066_7_)
/*      */   {
/*  197 */     if (p_176066_7_ > 50)
/*      */     {
/*  199 */       return null;
/*      */     }
/*  201 */     if ((Math.abs(p_176066_3_ - start.getBoundingBox().minX) <= 112) && (Math.abs(p_176066_5_ - start.getBoundingBox().minZ) <= 112))
/*      */     {
/*  203 */       StructureComponent structurecomponent = func_176067_c(start, p_176066_1_, rand, p_176066_3_, p_176066_4_, p_176066_5_, facing, p_176066_7_ + 1);
/*      */       
/*  205 */       if (structurecomponent != null)
/*      */       {
/*  207 */         int i = (structurecomponent.boundingBox.minX + structurecomponent.boundingBox.maxX) / 2;
/*  208 */         int j = (structurecomponent.boundingBox.minZ + structurecomponent.boundingBox.maxZ) / 2;
/*  209 */         int k = structurecomponent.boundingBox.maxX - structurecomponent.boundingBox.minX;
/*  210 */         int l = structurecomponent.boundingBox.maxZ - structurecomponent.boundingBox.minZ;
/*  211 */         int i1 = k > l ? k : l;
/*      */         
/*  213 */         if (start.getWorldChunkManager().areBiomesViable(i, j, i1 / 2 + 4, MapGenVillage.villageSpawnBiomes))
/*      */         {
/*  215 */           p_176066_1_.add(structurecomponent);
/*  216 */           start.field_74932_i.add(structurecomponent);
/*  217 */           return structurecomponent;
/*      */         }
/*      */       }
/*      */       
/*  221 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  225 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   private static StructureComponent func_176069_e(Start start, List<StructureComponent> p_176069_1_, Random rand, int p_176069_3_, int p_176069_4_, int p_176069_5_, EnumFacing facing, int p_176069_7_)
/*      */   {
/*  231 */     if (p_176069_7_ > 3 + start.terrainType)
/*      */     {
/*  233 */       return null;
/*      */     }
/*  235 */     if ((Math.abs(p_176069_3_ - start.getBoundingBox().minX) <= 112) && (Math.abs(p_176069_5_ - start.getBoundingBox().minZ) <= 112))
/*      */     {
/*  237 */       StructureBoundingBox structureboundingbox = Path.func_175848_a(start, p_176069_1_, rand, p_176069_3_, p_176069_4_, p_176069_5_, facing);
/*      */       
/*  239 */       if ((structureboundingbox != null) && (structureboundingbox.minY > 10))
/*      */       {
/*  241 */         StructureComponent structurecomponent = new Path(start, p_176069_7_, rand, structureboundingbox, facing);
/*  242 */         int i = (structurecomponent.boundingBox.minX + structurecomponent.boundingBox.maxX) / 2;
/*  243 */         int j = (structurecomponent.boundingBox.minZ + structurecomponent.boundingBox.maxZ) / 2;
/*  244 */         int k = structurecomponent.boundingBox.maxX - structurecomponent.boundingBox.minX;
/*  245 */         int l = structurecomponent.boundingBox.maxZ - structurecomponent.boundingBox.minZ;
/*  246 */         int i1 = k > l ? k : l;
/*      */         
/*  248 */         if (start.getWorldChunkManager().areBiomesViable(i, j, i1 / 2 + 4, MapGenVillage.villageSpawnBiomes))
/*      */         {
/*  250 */           p_176069_1_.add(structurecomponent);
/*  251 */           start.field_74930_j.add(structurecomponent);
/*  252 */           return structurecomponent;
/*      */         }
/*      */       }
/*      */       
/*  256 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  260 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Church
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     public Church() {}
/*      */     
/*      */ 
/*      */     public Church(StructureVillagePieces.Start start, int p_i45564_2_, Random rand, StructureBoundingBox p_i45564_4_, EnumFacing facing)
/*      */     {
/*  272 */       super(p_i45564_2_);
/*  273 */       this.coordBaseMode = facing;
/*  274 */       this.boundingBox = p_i45564_4_;
/*      */     }
/*      */     
/*      */     public static Church func_175854_a(StructureVillagePieces.Start start, List<StructureComponent> p_175854_1_, Random rand, int p_175854_3_, int p_175854_4_, int p_175854_5_, EnumFacing facing, int p_175854_7_)
/*      */     {
/*  279 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175854_3_, p_175854_4_, p_175854_5_, 0, 0, 0, 5, 12, 9, facing);
/*  280 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175854_1_, structureboundingbox) == null) ? new Church(start, p_175854_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  285 */       if (this.field_143015_k < 0)
/*      */       {
/*  287 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/*  289 */         if (this.field_143015_k < 0)
/*      */         {
/*  291 */           return true;
/*      */         }
/*      */         
/*  294 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 12 - 1, 0);
/*      */       }
/*      */       
/*  297 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 3, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  298 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 3, 9, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  299 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 3, 0, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  300 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 3, 10, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  301 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  302 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  303 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 0, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  304 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 4, 4, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  305 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 8, 3, 4, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  306 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 3, 10, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  307 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 5, 3, 5, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  308 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 9, 0, 4, 9, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  309 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 4, 4, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  310 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 11, 2, structureBoundingBoxIn);
/*  311 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 11, 2, structureBoundingBoxIn);
/*  312 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 2, 11, 0, structureBoundingBoxIn);
/*  313 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 2, 11, 4, structureBoundingBoxIn);
/*  314 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 1, 1, 6, structureBoundingBoxIn);
/*  315 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 1, 1, 7, structureBoundingBoxIn);
/*  316 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 2, 1, 7, structureBoundingBoxIn);
/*  317 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 3, 1, 6, structureBoundingBoxIn);
/*  318 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 3, 1, 7, structureBoundingBoxIn);
/*  319 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 1, 5, structureBoundingBoxIn);
/*  320 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 1, 6, structureBoundingBoxIn);
/*  321 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 3, 1, 5, structureBoundingBoxIn);
/*  322 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 1)), 1, 2, 7, structureBoundingBoxIn);
/*  323 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 0)), 3, 2, 7, structureBoundingBoxIn);
/*  324 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
/*  325 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
/*  326 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureBoundingBoxIn);
/*  327 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 2, structureBoundingBoxIn);
/*  328 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 6, 2, structureBoundingBoxIn);
/*  329 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 7, 2, structureBoundingBoxIn);
/*  330 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 6, 2, structureBoundingBoxIn);
/*  331 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 7, 2, structureBoundingBoxIn);
/*  332 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 6, 0, structureBoundingBoxIn);
/*  333 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 7, 0, structureBoundingBoxIn);
/*  334 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 6, 4, structureBoundingBoxIn);
/*  335 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 7, 4, structureBoundingBoxIn);
/*  336 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 6, structureBoundingBoxIn);
/*  337 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 6, structureBoundingBoxIn);
/*  338 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 3, 8, structureBoundingBoxIn);
/*  339 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 2, 4, 7, structureBoundingBoxIn);
/*  340 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()), 1, 4, 6, structureBoundingBoxIn);
/*  341 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()), 3, 4, 6, structureBoundingBoxIn);
/*  342 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 4, 5, structureBoundingBoxIn);
/*  343 */       int i = getMetadataWithOffset(Blocks.ladder, 4);
/*      */       
/*  345 */       for (int j = 1; j <= 9; j++)
/*      */       {
/*  347 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, j, 3, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  350 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
/*  351 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
/*  352 */       placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.getHorizontal(getMetadataWithOffset(Blocks.oak_door, 1)));
/*      */       
/*  354 */       if ((getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) && (getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air))
/*      */       {
/*  356 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  359 */       for (int l = 0; l < 9; l++)
/*      */       {
/*  361 */         for (int k = 0; k < 5; k++)
/*      */         {
/*  363 */           clearCurrentPositionBlocksUpwards(worldIn, k, 12, l, structureBoundingBoxIn);
/*  364 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), k, -1, l, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  368 */       spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
/*  369 */       return true;
/*      */     }
/*      */     
/*      */     protected int func_180779_c(int p_180779_1_, int p_180779_2_)
/*      */     {
/*  374 */       return 2;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Field1
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     private Block cropTypeA;
/*      */     private Block cropTypeB;
/*      */     private Block cropTypeC;
/*      */     private Block cropTypeD;
/*      */     
/*      */     public Field1() {}
/*      */     
/*      */     public Field1(StructureVillagePieces.Start start, int p_i45570_2_, Random rand, StructureBoundingBox p_i45570_4_, EnumFacing facing)
/*      */     {
/*  391 */       super(p_i45570_2_);
/*  392 */       this.coordBaseMode = facing;
/*  393 */       this.boundingBox = p_i45570_4_;
/*  394 */       this.cropTypeA = func_151559_a(rand);
/*  395 */       this.cropTypeB = func_151559_a(rand);
/*  396 */       this.cropTypeC = func_151559_a(rand);
/*  397 */       this.cropTypeD = func_151559_a(rand);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  402 */       super.writeStructureToNBT(tagCompound);
/*  403 */       tagCompound.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
/*  404 */       tagCompound.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
/*  405 */       tagCompound.setInteger("CC", Block.blockRegistry.getIDForObject(this.cropTypeC));
/*  406 */       tagCompound.setInteger("CD", Block.blockRegistry.getIDForObject(this.cropTypeD));
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  411 */       super.readStructureFromNBT(tagCompound);
/*  412 */       this.cropTypeA = Block.getBlockById(tagCompound.getInteger("CA"));
/*  413 */       this.cropTypeB = Block.getBlockById(tagCompound.getInteger("CB"));
/*  414 */       this.cropTypeC = Block.getBlockById(tagCompound.getInteger("CC"));
/*  415 */       this.cropTypeD = Block.getBlockById(tagCompound.getInteger("CD"));
/*      */     }
/*      */     
/*      */     private Block func_151559_a(Random rand)
/*      */     {
/*  420 */       switch (rand.nextInt(5))
/*      */       {
/*      */       case 0: 
/*  423 */         return Blocks.carrots;
/*      */       
/*      */       case 1: 
/*  426 */         return Blocks.potatoes;
/*      */       }
/*      */       
/*  429 */       return Blocks.wheat;
/*      */     }
/*      */     
/*      */ 
/*      */     public static Field1 func_175851_a(StructureVillagePieces.Start start, List<StructureComponent> p_175851_1_, Random rand, int p_175851_3_, int p_175851_4_, int p_175851_5_, EnumFacing facing, int p_175851_7_)
/*      */     {
/*  435 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175851_3_, p_175851_4_, p_175851_5_, 0, 0, 0, 13, 4, 9, facing);
/*  436 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175851_1_, structureboundingbox) == null) ? new Field1(start, p_175851_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  441 */       if (this.field_143015_k < 0)
/*      */       {
/*  443 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/*  445 */         if (this.field_143015_k < 0)
/*      */         {
/*  447 */           return true;
/*      */         }
/*      */         
/*  450 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
/*      */       }
/*      */       
/*  453 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 12, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  454 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
/*  455 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
/*  456 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 8, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
/*  457 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 1, 11, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
/*  458 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  459 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  460 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  461 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  462 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  463 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
/*  464 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 1, 9, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
/*      */       
/*  466 */       for (int i = 1; i <= 7; i++)
/*      */       {
/*  468 */         setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 1, 1, i, structureBoundingBoxIn);
/*  469 */         setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 2, 1, i, structureBoundingBoxIn);
/*  470 */         setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 4, 1, i, structureBoundingBoxIn);
/*  471 */         setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 5, 1, i, structureBoundingBoxIn);
/*  472 */         setBlockState(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 7, 1, i, structureBoundingBoxIn);
/*  473 */         setBlockState(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 8, 1, i, structureBoundingBoxIn);
/*  474 */         setBlockState(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 10, 1, i, structureBoundingBoxIn);
/*  475 */         setBlockState(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 11, 1, i, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  478 */       for (int k = 0; k < 9; k++)
/*      */       {
/*  480 */         for (int j = 0; j < 13; j++)
/*      */         {
/*  482 */           clearCurrentPositionBlocksUpwards(worldIn, j, 4, k, structureBoundingBoxIn);
/*  483 */           replaceAirAndLiquidDownwards(worldIn, Blocks.dirt.getDefaultState(), j, -1, k, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  487 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Field2
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     private Block cropTypeA;
/*      */     private Block cropTypeB;
/*      */     
/*      */     public Field2() {}
/*      */     
/*      */     public Field2(StructureVillagePieces.Start start, int p_i45569_2_, Random rand, StructureBoundingBox p_i45569_4_, EnumFacing facing)
/*      */     {
/*  502 */       super(p_i45569_2_);
/*  503 */       this.coordBaseMode = facing;
/*  504 */       this.boundingBox = p_i45569_4_;
/*  505 */       this.cropTypeA = func_151560_a(rand);
/*  506 */       this.cropTypeB = func_151560_a(rand);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  511 */       super.writeStructureToNBT(tagCompound);
/*  512 */       tagCompound.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
/*  513 */       tagCompound.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  518 */       super.readStructureFromNBT(tagCompound);
/*  519 */       this.cropTypeA = Block.getBlockById(tagCompound.getInteger("CA"));
/*  520 */       this.cropTypeB = Block.getBlockById(tagCompound.getInteger("CB"));
/*      */     }
/*      */     
/*      */     private Block func_151560_a(Random rand)
/*      */     {
/*  525 */       switch (rand.nextInt(5))
/*      */       {
/*      */       case 0: 
/*  528 */         return Blocks.carrots;
/*      */       
/*      */       case 1: 
/*  531 */         return Blocks.potatoes;
/*      */       }
/*      */       
/*  534 */       return Blocks.wheat;
/*      */     }
/*      */     
/*      */ 
/*      */     public static Field2 func_175852_a(StructureVillagePieces.Start start, List<StructureComponent> p_175852_1_, Random rand, int p_175852_3_, int p_175852_4_, int p_175852_5_, EnumFacing facing, int p_175852_7_)
/*      */     {
/*  540 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175852_3_, p_175852_4_, p_175852_5_, 0, 0, 0, 7, 4, 9, facing);
/*  541 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175852_1_, structureboundingbox) == null) ? new Field2(start, p_175852_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  546 */       if (this.field_143015_k < 0)
/*      */       {
/*  548 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/*  550 */         if (this.field_143015_k < 0)
/*      */         {
/*  552 */           return true;
/*      */         }
/*      */         
/*  555 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
/*      */       }
/*      */       
/*  558 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 6, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  559 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
/*  560 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
/*  561 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  562 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  563 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 5, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  564 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 5, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  565 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
/*      */       
/*  567 */       for (int i = 1; i <= 7; i++)
/*      */       {
/*  569 */         setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 1, 1, i, structureBoundingBoxIn);
/*  570 */         setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 2, 1, i, structureBoundingBoxIn);
/*  571 */         setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 4, 1, i, structureBoundingBoxIn);
/*  572 */         setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(randomIn, 2, 7)), 5, 1, i, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  575 */       for (int k = 0; k < 9; k++)
/*      */       {
/*  577 */         for (int j = 0; j < 7; j++)
/*      */         {
/*  579 */           clearCurrentPositionBlocksUpwards(worldIn, j, 4, k, structureBoundingBoxIn);
/*  580 */           replaceAirAndLiquidDownwards(worldIn, Blocks.dirt.getDefaultState(), j, -1, k, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  584 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Hall
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     public Hall() {}
/*      */     
/*      */     public Hall(StructureVillagePieces.Start start, int p_i45567_2_, Random rand, StructureBoundingBox p_i45567_4_, EnumFacing facing)
/*      */     {
/*  596 */       super(p_i45567_2_);
/*  597 */       this.coordBaseMode = facing;
/*  598 */       this.boundingBox = p_i45567_4_;
/*      */     }
/*      */     
/*      */     public static Hall func_175857_a(StructureVillagePieces.Start start, List<StructureComponent> p_175857_1_, Random rand, int p_175857_3_, int p_175857_4_, int p_175857_5_, EnumFacing facing, int p_175857_7_)
/*      */     {
/*  603 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175857_3_, p_175857_4_, p_175857_5_, 0, 0, 0, 9, 7, 11, facing);
/*  604 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175857_1_, structureboundingbox) == null) ? new Hall(start, p_175857_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  609 */       if (this.field_143015_k < 0)
/*      */       {
/*  611 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/*  613 */         if (this.field_143015_k < 0)
/*      */         {
/*  615 */           return true;
/*      */         }
/*      */         
/*  618 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
/*      */       }
/*      */       
/*  621 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  622 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  623 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 6, 8, 0, 10, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
/*  624 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 0, 6, structureBoundingBoxIn);
/*  625 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 2, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  626 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 6, 8, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  627 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 10, 7, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  628 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  629 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  630 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 8, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  631 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  632 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 5, 7, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  633 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  634 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 7, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  635 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  636 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  637 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  638 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 0, 4, 2, structureBoundingBoxIn);
/*  639 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 0, 4, 3, structureBoundingBoxIn);
/*  640 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 4, 2, structureBoundingBoxIn);
/*  641 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 4, 3, structureBoundingBoxIn);
/*  642 */       int i = getMetadataWithOffset(Blocks.oak_stairs, 3);
/*  643 */       int j = getMetadataWithOffset(Blocks.oak_stairs, 2);
/*      */       
/*  645 */       for (int k = -1; k <= 2; k++)
/*      */       {
/*  647 */         for (int l = 0; l <= 8; l++)
/*      */         {
/*  649 */           setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(i), l, 4 + k, k, structureBoundingBoxIn);
/*  650 */           setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j), l, 4 + k, 5 - k, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  654 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 0, 2, 1, structureBoundingBoxIn);
/*  655 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 0, 2, 4, structureBoundingBoxIn);
/*  656 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 8, 2, 1, structureBoundingBoxIn);
/*  657 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 8, 2, 4, structureBoundingBoxIn);
/*  658 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
/*  659 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
/*  660 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
/*  661 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
/*  662 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 5, structureBoundingBoxIn);
/*  663 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 3, 2, 5, structureBoundingBoxIn);
/*  664 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
/*  665 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
/*  666 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 1, 3, structureBoundingBoxIn);
/*  667 */       setBlockState(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 3, structureBoundingBoxIn);
/*  668 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 1, 4, structureBoundingBoxIn);
/*  669 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 4, structureBoundingBoxIn);
/*  670 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 3, structureBoundingBoxIn);
/*  671 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 1, 7, 0, 3, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
/*  672 */       setBlockState(worldIn, Blocks.double_stone_slab.getDefaultState(), 6, 1, 1, structureBoundingBoxIn);
/*  673 */       setBlockState(worldIn, Blocks.double_stone_slab.getDefaultState(), 6, 1, 2, structureBoundingBoxIn);
/*  674 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
/*  675 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
/*  676 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, structureBoundingBoxIn);
/*  677 */       placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.getHorizontal(getMetadataWithOffset(Blocks.oak_door, 1)));
/*      */       
/*  679 */       if ((getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) && (getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air))
/*      */       {
/*  681 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  684 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
/*  685 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
/*  686 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 6, 3, 4, structureBoundingBoxIn);
/*  687 */       placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 6, 1, 5, EnumFacing.getHorizontal(getMetadataWithOffset(Blocks.oak_door, 1)));
/*      */       
/*  689 */       for (int i1 = 0; i1 < 5; i1++)
/*      */       {
/*  691 */         for (int j1 = 0; j1 < 9; j1++)
/*      */         {
/*  693 */           clearCurrentPositionBlocksUpwards(worldIn, j1, 7, i1, structureBoundingBoxIn);
/*  694 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), j1, -1, i1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  698 */       spawnVillagers(worldIn, structureBoundingBoxIn, 4, 1, 2, 2);
/*  699 */       return true;
/*      */     }
/*      */     
/*      */     protected int func_180779_c(int p_180779_1_, int p_180779_2_)
/*      */     {
/*  704 */       return p_180779_1_ == 0 ? 4 : super.func_180779_c(p_180779_1_, p_180779_2_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class House1
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     public House1() {}
/*      */     
/*      */     public House1(StructureVillagePieces.Start start, int p_i45571_2_, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing)
/*      */     {
/*  716 */       super(p_i45571_2_);
/*  717 */       this.coordBaseMode = facing;
/*  718 */       this.boundingBox = p_i45571_4_;
/*      */     }
/*      */     
/*      */     public static House1 func_175850_a(StructureVillagePieces.Start start, List<StructureComponent> p_175850_1_, Random rand, int p_175850_3_, int p_175850_4_, int p_175850_5_, EnumFacing facing, int p_175850_7_)
/*      */     {
/*  723 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175850_3_, p_175850_4_, p_175850_5_, 0, 0, 0, 9, 9, 6, facing);
/*  724 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175850_1_, structureboundingbox) == null) ? new House1(start, p_175850_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  729 */       if (this.field_143015_k < 0)
/*      */       {
/*  731 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/*  733 */         if (this.field_143015_k < 0)
/*      */         {
/*  735 */           return true;
/*      */         }
/*      */         
/*  738 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
/*      */       }
/*      */       
/*  741 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  742 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 0, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  743 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 8, 5, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  744 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 1, 8, 6, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  745 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 7, 2, 8, 7, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  746 */       int i = getMetadataWithOffset(Blocks.oak_stairs, 3);
/*  747 */       int j = getMetadataWithOffset(Blocks.oak_stairs, 2);
/*      */       
/*  749 */       for (int k = -1; k <= 2; k++)
/*      */       {
/*  751 */         for (int l = 0; l <= 8; l++)
/*      */         {
/*  753 */           setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(i), l, 6 + k, k, structureBoundingBoxIn);
/*  754 */           setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j), l, 6 + k, 5 - k, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  758 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  759 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 8, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  760 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 0, 8, 1, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  761 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  762 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  763 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  764 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 5, 8, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  765 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 0, 8, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  766 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  767 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 7, 4, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  768 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 1, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  769 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 4, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  770 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
/*  771 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
/*  772 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 0, structureBoundingBoxIn);
/*  773 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 0, structureBoundingBoxIn);
/*  774 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 3, 0, structureBoundingBoxIn);
/*  775 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 6, 3, 0, structureBoundingBoxIn);
/*  776 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
/*  777 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
/*  778 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
/*  779 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 3, structureBoundingBoxIn);
/*  780 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
/*  781 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
/*  782 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 3, 2, structureBoundingBoxIn);
/*  783 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 3, 3, structureBoundingBoxIn);
/*  784 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 5, structureBoundingBoxIn);
/*  785 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 3, 2, 5, structureBoundingBoxIn);
/*  786 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
/*  787 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
/*  788 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 7, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  789 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 4, 7, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  790 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 7, 3, 4, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  791 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 7, 1, 4, structureBoundingBoxIn);
/*  792 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.oak_stairs, 0)), 7, 1, 3, structureBoundingBoxIn);
/*  793 */       int j1 = getMetadataWithOffset(Blocks.oak_stairs, 3);
/*  794 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 6, 1, 4, structureBoundingBoxIn);
/*  795 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 5, 1, 4, structureBoundingBoxIn);
/*  796 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 4, 1, 4, structureBoundingBoxIn);
/*  797 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 3, 1, 4, structureBoundingBoxIn);
/*  798 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 6, 1, 3, structureBoundingBoxIn);
/*  799 */       setBlockState(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 6, 2, 3, structureBoundingBoxIn);
/*  800 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 1, 3, structureBoundingBoxIn);
/*  801 */       setBlockState(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 4, 2, 3, structureBoundingBoxIn);
/*  802 */       setBlockState(worldIn, Blocks.crafting_table.getDefaultState(), 7, 1, 1, structureBoundingBoxIn);
/*  803 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
/*  804 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
/*  805 */       placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 1, 1, 0, EnumFacing.getHorizontal(getMetadataWithOffset(Blocks.oak_door, 1)));
/*      */       
/*  807 */       if ((getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) && (getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air))
/*      */       {
/*  809 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  812 */       for (int k1 = 0; k1 < 6; k1++)
/*      */       {
/*  814 */         for (int i1 = 0; i1 < 9; i1++)
/*      */         {
/*  816 */           clearCurrentPositionBlocksUpwards(worldIn, i1, 9, k1, structureBoundingBoxIn);
/*  817 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), i1, -1, k1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  821 */       spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
/*  822 */       return true;
/*      */     }
/*      */     
/*      */     protected int func_180779_c(int p_180779_1_, int p_180779_2_)
/*      */     {
/*  827 */       return 1;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class House2 extends StructureVillagePieces.Village
/*      */   {
/*  833 */     private static final List<WeightedRandomChestContent> villageBlacksmithChestContents = Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(net.minecraft.item.Item.getItemFromBlock(Blocks.obsidian), 0, 3, 7, 5), new WeightedRandomChestContent(net.minecraft.item.Item.getItemFromBlock(Blocks.sapling), 0, 3, 7, 5), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
/*      */     
/*      */     private boolean hasMadeChest;
/*      */     
/*      */ 
/*      */     public House2() {}
/*      */     
/*      */     public House2(StructureVillagePieces.Start start, int p_i45563_2_, Random rand, StructureBoundingBox p_i45563_4_, EnumFacing facing)
/*      */     {
/*  842 */       super(p_i45563_2_);
/*  843 */       this.coordBaseMode = facing;
/*  844 */       this.boundingBox = p_i45563_4_;
/*      */     }
/*      */     
/*      */     public static House2 func_175855_a(StructureVillagePieces.Start start, List<StructureComponent> p_175855_1_, Random rand, int p_175855_3_, int p_175855_4_, int p_175855_5_, EnumFacing facing, int p_175855_7_)
/*      */     {
/*  849 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175855_3_, p_175855_4_, p_175855_5_, 0, 0, 0, 10, 6, 7, facing);
/*  850 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175855_1_, structureboundingbox) == null) ? new House2(start, p_175855_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  855 */       super.writeStructureToNBT(tagCompound);
/*  856 */       tagCompound.setBoolean("Chest", this.hasMadeChest);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  861 */       super.readStructureFromNBT(tagCompound);
/*  862 */       this.hasMadeChest = tagCompound.getBoolean("Chest");
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  867 */       if (this.field_143015_k < 0)
/*      */       {
/*  869 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/*  871 */         if (this.field_143015_k < 0)
/*      */         {
/*  873 */           return true;
/*      */         }
/*      */         
/*  876 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
/*      */       }
/*      */       
/*  879 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 9, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  880 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 9, 0, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  881 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  882 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 9, 5, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  883 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 8, 5, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  884 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  885 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  886 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 3, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  887 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 6, 0, 4, 6, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*  888 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 3, 1, structureBoundingBoxIn);
/*  889 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 3, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  890 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 3, 5, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  891 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  892 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 6, 5, 3, 6, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  893 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 5, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  894 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 0, 9, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  895 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 4, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  896 */       setBlockState(worldIn, Blocks.flowing_lava.getDefaultState(), 7, 1, 5, structureBoundingBoxIn);
/*  897 */       setBlockState(worldIn, Blocks.flowing_lava.getDefaultState(), 8, 1, 5, structureBoundingBoxIn);
/*  898 */       setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 9, 2, 5, structureBoundingBoxIn);
/*  899 */       setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 9, 2, 4, structureBoundingBoxIn);
/*  900 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 4, 8, 2, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  901 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 1, 3, structureBoundingBoxIn);
/*  902 */       setBlockState(worldIn, Blocks.furnace.getDefaultState(), 6, 2, 3, structureBoundingBoxIn);
/*  903 */       setBlockState(worldIn, Blocks.furnace.getDefaultState(), 6, 3, 3, structureBoundingBoxIn);
/*  904 */       setBlockState(worldIn, Blocks.double_stone_slab.getDefaultState(), 8, 1, 1, structureBoundingBoxIn);
/*  905 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
/*  906 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 4, structureBoundingBoxIn);
/*  907 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 6, structureBoundingBoxIn);
/*  908 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 6, structureBoundingBoxIn);
/*  909 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 1, 4, structureBoundingBoxIn);
/*  910 */       setBlockState(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 4, structureBoundingBoxIn);
/*  911 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 1, 5, structureBoundingBoxIn);
/*  912 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 5, structureBoundingBoxIn);
/*  913 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 4, structureBoundingBoxIn);
/*      */       
/*  915 */       if ((!this.hasMadeChest) && (structureBoundingBoxIn.isVecInside(new BlockPos(getXWithOffset(5, 5), getYWithOffset(1), getZWithOffset(5, 5)))))
/*      */       {
/*  917 */         this.hasMadeChest = true;
/*  918 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 5, 1, 5, villageBlacksmithChestContents, 3 + randomIn.nextInt(6));
/*      */       }
/*      */       
/*  921 */       for (int i = 6; i <= 8; i++)
/*      */       {
/*  923 */         if ((getBlockStateFromPos(worldIn, i, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) && (getBlockStateFromPos(worldIn, i, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air))
/*      */         {
/*  925 */           setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), i, 0, -1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  929 */       for (int k = 0; k < 7; k++)
/*      */       {
/*  931 */         for (int j = 0; j < 10; j++)
/*      */         {
/*  933 */           clearCurrentPositionBlocksUpwards(worldIn, j, 6, k, structureBoundingBoxIn);
/*  934 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), j, -1, k, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  938 */       spawnVillagers(worldIn, structureBoundingBoxIn, 7, 1, 1, 1);
/*  939 */       return true;
/*      */     }
/*      */     
/*      */     protected int func_180779_c(int p_180779_1_, int p_180779_2_)
/*      */     {
/*  944 */       return 3;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class House3
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     public House3() {}
/*      */     
/*      */     public House3(StructureVillagePieces.Start start, int p_i45561_2_, Random rand, StructureBoundingBox p_i45561_4_, EnumFacing facing)
/*      */     {
/*  956 */       super(p_i45561_2_);
/*  957 */       this.coordBaseMode = facing;
/*  958 */       this.boundingBox = p_i45561_4_;
/*      */     }
/*      */     
/*      */     public static House3 func_175849_a(StructureVillagePieces.Start start, List<StructureComponent> p_175849_1_, Random rand, int p_175849_3_, int p_175849_4_, int p_175849_5_, EnumFacing facing, int p_175849_7_)
/*      */     {
/*  963 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175849_3_, p_175849_4_, p_175849_5_, 0, 0, 0, 9, 7, 12, facing);
/*  964 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175849_1_, structureboundingbox) == null) ? new House3(start, p_175849_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  969 */       if (this.field_143015_k < 0)
/*      */       {
/*  971 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/*  973 */         if (this.field_143015_k < 0)
/*      */         {
/*  975 */           return true;
/*      */         }
/*      */         
/*  978 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
/*      */       }
/*      */       
/*  981 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  982 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  983 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 8, 0, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  984 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  985 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  986 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 8, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  987 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 7, 2, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  988 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 5, 2, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  989 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 6, 2, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  990 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 10, 7, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*  991 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  992 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 2, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  993 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  994 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 3, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  995 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  996 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 0, 4, 2, structureBoundingBoxIn);
/*  997 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 0, 4, 3, structureBoundingBoxIn);
/*  998 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 4, 2, structureBoundingBoxIn);
/*  999 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 4, 3, structureBoundingBoxIn);
/* 1000 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 4, 4, structureBoundingBoxIn);
/* 1001 */       int i = getMetadataWithOffset(Blocks.oak_stairs, 3);
/* 1002 */       int j = getMetadataWithOffset(Blocks.oak_stairs, 2);
/*      */       
/* 1004 */       for (int k = -1; k <= 2; k++)
/*      */       {
/* 1006 */         for (int l = 0; l <= 8; l++)
/*      */         {
/* 1008 */           setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(i), l, 4 + k, k, structureBoundingBoxIn);
/*      */           
/* 1010 */           if (((k > -1) || (l <= 1)) && ((k > 0) || (l <= 3)) && ((k > 1) || (l <= 4) || (l >= 6)))
/*      */           {
/* 1012 */             setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j), l, 4 + k, 5 - k, structureBoundingBoxIn);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1017 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 5, 3, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1018 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 2, 7, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1019 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 4, 4, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1020 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 4, 6, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1021 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 6, 3, 5, 6, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1022 */       int k1 = getMetadataWithOffset(Blocks.oak_stairs, 0);
/*      */       
/* 1024 */       for (int l1 = 4; l1 >= 1; l1--)
/*      */       {
/* 1026 */         setBlockState(worldIn, Blocks.planks.getDefaultState(), l1, 2 + l1, 7 - l1, structureBoundingBoxIn);
/*      */         
/* 1028 */         for (int i1 = 8 - l1; i1 <= 10; i1++)
/*      */         {
/* 1030 */           setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(k1), l1, 2 + l1, i1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1034 */       int i2 = getMetadataWithOffset(Blocks.oak_stairs, 1);
/* 1035 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 6, 6, 3, structureBoundingBoxIn);
/* 1036 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 7, 5, 4, structureBoundingBoxIn);
/* 1037 */       setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(i2), 6, 6, 4, structureBoundingBoxIn);
/*      */       
/* 1039 */       for (int j2 = 6; j2 <= 8; j2++)
/*      */       {
/* 1041 */         for (int j1 = 5; j1 <= 10; j1++)
/*      */         {
/* 1043 */           setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(i2), j2, 12 - j2, j1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1047 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 0, 2, 1, structureBoundingBoxIn);
/* 1048 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 0, 2, 4, structureBoundingBoxIn);
/* 1049 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
/* 1050 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
/* 1051 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
/* 1052 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
/* 1053 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 6, 2, 0, structureBoundingBoxIn);
/* 1054 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 8, 2, 1, structureBoundingBoxIn);
/* 1055 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
/* 1056 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
/* 1057 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 8, 2, 4, structureBoundingBoxIn);
/* 1058 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 2, 5, structureBoundingBoxIn);
/* 1059 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 8, 2, 6, structureBoundingBoxIn);
/* 1060 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 7, structureBoundingBoxIn);
/* 1061 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 8, structureBoundingBoxIn);
/* 1062 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 8, 2, 9, structureBoundingBoxIn);
/* 1063 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 2, 2, 6, structureBoundingBoxIn);
/* 1064 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 7, structureBoundingBoxIn);
/* 1065 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 8, structureBoundingBoxIn);
/* 1066 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 2, 2, 9, structureBoundingBoxIn);
/* 1067 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 4, 4, 10, structureBoundingBoxIn);
/* 1068 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 4, 10, structureBoundingBoxIn);
/* 1069 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 6, 4, 10, structureBoundingBoxIn);
/* 1070 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 5, 5, 10, structureBoundingBoxIn);
/* 1071 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
/* 1072 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
/* 1073 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, structureBoundingBoxIn);
/* 1074 */       placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.getHorizontal(getMetadataWithOffset(Blocks.oak_door, 1)));
/* 1075 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, -1, 3, 2, -1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       
/* 1077 */       if ((getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) && (getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air))
/*      */       {
/* 1079 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBoxIn);
/*      */       }
/*      */       
/* 1082 */       for (int k2 = 0; k2 < 5; k2++)
/*      */       {
/* 1084 */         for (int i3 = 0; i3 < 9; i3++)
/*      */         {
/* 1086 */           clearCurrentPositionBlocksUpwards(worldIn, i3, 7, k2, structureBoundingBoxIn);
/* 1087 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), i3, -1, k2, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1091 */       for (int l2 = 5; l2 < 11; l2++)
/*      */       {
/* 1093 */         for (int j3 = 2; j3 < 9; j3++)
/*      */         {
/* 1095 */           clearCurrentPositionBlocksUpwards(worldIn, j3, 7, l2, structureBoundingBoxIn);
/* 1096 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), j3, -1, l2, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1100 */       spawnVillagers(worldIn, structureBoundingBoxIn, 4, 1, 2, 2);
/* 1101 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class House4Garden
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     private boolean isRoofAccessible;
/*      */     
/*      */     public House4Garden() {}
/*      */     
/*      */     public House4Garden(StructureVillagePieces.Start start, int p_i45566_2_, Random rand, StructureBoundingBox p_i45566_4_, EnumFacing facing)
/*      */     {
/* 1115 */       super(p_i45566_2_);
/* 1116 */       this.coordBaseMode = facing;
/* 1117 */       this.boundingBox = p_i45566_4_;
/* 1118 */       this.isRoofAccessible = rand.nextBoolean();
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1123 */       super.writeStructureToNBT(tagCompound);
/* 1124 */       tagCompound.setBoolean("Terrace", this.isRoofAccessible);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1129 */       super.readStructureFromNBT(tagCompound);
/* 1130 */       this.isRoofAccessible = tagCompound.getBoolean("Terrace");
/*      */     }
/*      */     
/*      */     public static House4Garden func_175858_a(StructureVillagePieces.Start start, List<StructureComponent> p_175858_1_, Random rand, int p_175858_3_, int p_175858_4_, int p_175858_5_, EnumFacing facing, int p_175858_7_)
/*      */     {
/* 1135 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175858_3_, p_175858_4_, p_175858_5_, 0, 0, 0, 5, 6, 5, facing);
/* 1136 */       return StructureComponent.findIntersecting(p_175858_1_, structureboundingbox) != null ? null : new House4Garden(start, p_175858_7_, rand, structureboundingbox, facing);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1141 */       if (this.field_143015_k < 0)
/*      */       {
/* 1143 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/* 1145 */         if (this.field_143015_k < 0)
/*      */         {
/* 1147 */           return true;
/*      */         }
/*      */         
/* 1150 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
/*      */       }
/*      */       
/* 1153 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/* 1154 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 4, 4, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 1155 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 3, 4, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1156 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 1, 0, structureBoundingBoxIn);
/* 1157 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 2, 0, structureBoundingBoxIn);
/* 1158 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 3, 0, structureBoundingBoxIn);
/* 1159 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 0, structureBoundingBoxIn);
/* 1160 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
/* 1161 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 0, structureBoundingBoxIn);
/* 1162 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 1, 4, structureBoundingBoxIn);
/* 1163 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 2, 4, structureBoundingBoxIn);
/* 1164 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 3, 4, structureBoundingBoxIn);
/* 1165 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 4, structureBoundingBoxIn);
/* 1166 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 2, 4, structureBoundingBoxIn);
/* 1167 */       setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 4, structureBoundingBoxIn);
/* 1168 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1169 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1170 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 3, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1171 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
/* 1172 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 4, structureBoundingBoxIn);
/* 1173 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureBoundingBoxIn);
/* 1174 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
/* 1175 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
/* 1176 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 1, 3, 0, structureBoundingBoxIn);
/* 1177 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 2, 3, 0, structureBoundingBoxIn);
/* 1178 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 3, 0, structureBoundingBoxIn);
/* 1179 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 2, 0, structureBoundingBoxIn);
/* 1180 */       setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 1, 0, structureBoundingBoxIn);
/*      */       
/* 1182 */       if ((getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) && (getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air))
/*      */       {
/* 1184 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBoxIn);
/*      */       }
/*      */       
/* 1187 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       
/* 1189 */       if (this.isRoofAccessible)
/*      */       {
/* 1191 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 0, structureBoundingBoxIn);
/* 1192 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 5, 0, structureBoundingBoxIn);
/* 1193 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 5, 0, structureBoundingBoxIn);
/* 1194 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 3, 5, 0, structureBoundingBoxIn);
/* 1195 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 0, structureBoundingBoxIn);
/* 1196 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 4, structureBoundingBoxIn);
/* 1197 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 5, 4, structureBoundingBoxIn);
/* 1198 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 5, 4, structureBoundingBoxIn);
/* 1199 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 3, 5, 4, structureBoundingBoxIn);
/* 1200 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 4, structureBoundingBoxIn);
/* 1201 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 1, structureBoundingBoxIn);
/* 1202 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 2, structureBoundingBoxIn);
/* 1203 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 5, 3, structureBoundingBoxIn);
/* 1204 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 1, structureBoundingBoxIn);
/* 1205 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 2, structureBoundingBoxIn);
/* 1206 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 0, 5, 3, structureBoundingBoxIn);
/*      */       }
/*      */       
/* 1209 */       if (this.isRoofAccessible)
/*      */       {
/* 1211 */         int i = getMetadataWithOffset(Blocks.ladder, 3);
/* 1212 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 1, 3, structureBoundingBoxIn);
/* 1213 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 2, 3, structureBoundingBoxIn);
/* 1214 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 3, 3, structureBoundingBoxIn);
/* 1215 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, 4, 3, structureBoundingBoxIn);
/*      */       }
/*      */       
/* 1218 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, structureBoundingBoxIn);
/*      */       
/* 1220 */       for (int k = 0; k < 5; k++)
/*      */       {
/* 1222 */         for (int j = 0; j < 5; j++)
/*      */         {
/* 1224 */           clearCurrentPositionBlocksUpwards(worldIn, j, 6, k, structureBoundingBoxIn);
/* 1225 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), j, -1, k, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1229 */       spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1);
/* 1230 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Path
/*      */     extends StructureVillagePieces.Road
/*      */   {
/*      */     private int length;
/*      */     
/*      */     public Path() {}
/*      */     
/*      */     public Path(StructureVillagePieces.Start start, int p_i45562_2_, Random rand, StructureBoundingBox p_i45562_4_, EnumFacing facing)
/*      */     {
/* 1244 */       super(p_i45562_2_);
/* 1245 */       this.coordBaseMode = facing;
/* 1246 */       this.boundingBox = p_i45562_4_;
/* 1247 */       this.length = Math.max(p_i45562_4_.getXSize(), p_i45562_4_.getZSize());
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1252 */       super.writeStructureToNBT(tagCompound);
/* 1253 */       tagCompound.setInteger("Length", this.length);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1258 */       super.readStructureFromNBT(tagCompound);
/* 1259 */       this.length = tagCompound.getInteger("Length");
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1264 */       boolean flag = false;
/*      */       
/* 1266 */       for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
/*      */       {
/* 1268 */         StructureComponent structurecomponent = getNextComponentNN((StructureVillagePieces.Start)componentIn, listIn, rand, 0, i);
/*      */         
/* 1270 */         if (structurecomponent != null)
/*      */         {
/* 1272 */           i += Math.max(structurecomponent.boundingBox.getXSize(), structurecomponent.boundingBox.getZSize());
/* 1273 */           flag = true;
/*      */         }
/*      */       }
/*      */       
/* 1277 */       for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
/*      */       {
/* 1279 */         StructureComponent structurecomponent1 = getNextComponentPP((StructureVillagePieces.Start)componentIn, listIn, rand, 0, j);
/*      */         
/* 1281 */         if (structurecomponent1 != null)
/*      */         {
/* 1283 */           j += Math.max(structurecomponent1.boundingBox.getXSize(), structurecomponent1.boundingBox.getZSize());
/* 1284 */           flag = true;
/*      */         }
/*      */       }
/*      */       
/* 1288 */       if ((flag) && (rand.nextInt(3) > 0) && (this.coordBaseMode != null))
/*      */       {
/* 1290 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1293 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, getComponentType());
/* 1294 */           break;
/*      */         
/*      */         case SOUTH: 
/* 1297 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, getComponentType());
/* 1298 */           break;
/*      */         
/*      */         case UP: 
/* 1301 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/* 1302 */           break;
/*      */         
/*      */         case WEST: 
/* 1305 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/*      */         }
/*      */         
/*      */       }
/* 1309 */       if ((flag) && (rand.nextInt(3) > 0) && (this.coordBaseMode != null))
/*      */       {
/* 1311 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1314 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, getComponentType());
/* 1315 */           break;
/*      */         
/*      */         case SOUTH: 
/* 1318 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, getComponentType());
/* 1319 */           break;
/*      */         
/*      */         case UP: 
/* 1322 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/* 1323 */           break;
/*      */         
/*      */         case WEST: 
/* 1326 */           StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public static StructureBoundingBox func_175848_a(StructureVillagePieces.Start start, List<StructureComponent> p_175848_1_, Random rand, int p_175848_3_, int p_175848_4_, int p_175848_5_, EnumFacing facing)
/*      */     {
/* 1333 */       for (int i = 7 * MathHelper.getRandomIntegerInRange(rand, 3, 5); i >= 7; i -= 7)
/*      */       {
/* 1335 */         StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175848_3_, p_175848_4_, p_175848_5_, 0, 0, 0, 3, 3, i, facing);
/*      */         
/* 1337 */         if (StructureComponent.findIntersecting(p_175848_1_, structureboundingbox) == null)
/*      */         {
/* 1339 */           return structureboundingbox;
/*      */         }
/*      */       }
/*      */       
/* 1343 */       return null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1348 */       IBlockState iblockstate = func_175847_a(Blocks.gravel.getDefaultState());
/* 1349 */       IBlockState iblockstate1 = func_175847_a(Blocks.cobblestone.getDefaultState());
/*      */       
/* 1351 */       for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; i++)
/*      */       {
/* 1353 */         for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; j++)
/*      */         {
/* 1355 */           BlockPos blockpos = new BlockPos(i, 64, j);
/*      */           
/* 1357 */           if (structureBoundingBoxIn.isVecInside(blockpos))
/*      */           {
/* 1359 */             blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();
/* 1360 */             worldIn.setBlockState(blockpos, iblockstate, 2);
/* 1361 */             worldIn.setBlockState(blockpos.down(), iblockstate1, 2);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1366 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PieceWeight
/*      */   {
/*      */     public Class<? extends StructureVillagePieces.Village> villagePieceClass;
/*      */     public final int villagePieceWeight;
/*      */     public int villagePiecesSpawned;
/*      */     public int villagePiecesLimit;
/*      */     
/*      */     public PieceWeight(Class<? extends StructureVillagePieces.Village> p_i2098_1_, int p_i2098_2_, int p_i2098_3_)
/*      */     {
/* 1379 */       this.villagePieceClass = p_i2098_1_;
/* 1380 */       this.villagePieceWeight = p_i2098_2_;
/* 1381 */       this.villagePiecesLimit = p_i2098_3_;
/*      */     }
/*      */     
/*      */     public boolean canSpawnMoreVillagePiecesOfType(int p_75085_1_)
/*      */     {
/* 1386 */       return (this.villagePiecesLimit == 0) || (this.villagePiecesSpawned < this.villagePiecesLimit);
/*      */     }
/*      */     
/*      */     public boolean canSpawnMoreVillagePieces()
/*      */     {
/* 1391 */       return (this.villagePiecesLimit == 0) || (this.villagePiecesSpawned < this.villagePiecesLimit);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static abstract class Road
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     public Road() {}
/*      */     
/*      */     protected Road(StructureVillagePieces.Start start, int type)
/*      */     {
/* 1403 */       super(type);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Start extends StructureVillagePieces.Well
/*      */   {
/*      */     public WorldChunkManager worldChunkMngr;
/*      */     public boolean inDesert;
/*      */     public int terrainType;
/*      */     public StructureVillagePieces.PieceWeight structVillagePieceWeight;
/*      */     public List<StructureVillagePieces.PieceWeight> structureVillageWeightedPieceList;
/* 1414 */     public List<StructureComponent> field_74932_i = Lists.newArrayList();
/* 1415 */     public List<StructureComponent> field_74930_j = Lists.newArrayList();
/*      */     
/*      */ 
/*      */     public Start() {}
/*      */     
/*      */ 
/*      */     public Start(WorldChunkManager chunkManagerIn, int p_i2104_2_, Random rand, int p_i2104_4_, int p_i2104_5_, List<StructureVillagePieces.PieceWeight> p_i2104_6_, int p_i2104_7_)
/*      */     {
/* 1423 */       super(0, rand, p_i2104_4_, p_i2104_5_);
/* 1424 */       this.worldChunkMngr = chunkManagerIn;
/* 1425 */       this.structureVillageWeightedPieceList = p_i2104_6_;
/* 1426 */       this.terrainType = p_i2104_7_;
/* 1427 */       BiomeGenBase biomegenbase = chunkManagerIn.getBiomeGenerator(new BlockPos(p_i2104_4_, 0, p_i2104_5_), BiomeGenBase.field_180279_ad);
/* 1428 */       this.inDesert = ((biomegenbase == BiomeGenBase.desert) || (biomegenbase == BiomeGenBase.desertHills));
/* 1429 */       func_175846_a(this.inDesert);
/*      */     }
/*      */     
/*      */     public WorldChunkManager getWorldChunkManager()
/*      */     {
/* 1434 */       return this.worldChunkMngr;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Torch
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     public Torch() {}
/*      */     
/*      */     public Torch(StructureVillagePieces.Start start, int p_i45568_2_, Random rand, StructureBoundingBox p_i45568_4_, EnumFacing facing)
/*      */     {
/* 1446 */       super(p_i45568_2_);
/* 1447 */       this.coordBaseMode = facing;
/* 1448 */       this.boundingBox = p_i45568_4_;
/*      */     }
/*      */     
/*      */     public static StructureBoundingBox func_175856_a(StructureVillagePieces.Start start, List<StructureComponent> p_175856_1_, Random rand, int p_175856_3_, int p_175856_4_, int p_175856_5_, EnumFacing facing)
/*      */     {
/* 1453 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 3, 4, 2, facing);
/* 1454 */       return StructureComponent.findIntersecting(p_175856_1_, structureboundingbox) != null ? null : structureboundingbox;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1459 */       if (this.field_143015_k < 0)
/*      */       {
/* 1461 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/* 1463 */         if (this.field_143015_k < 0)
/*      */         {
/* 1465 */           return true;
/*      */         }
/*      */         
/* 1468 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
/*      */       }
/*      */       
/* 1471 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 3, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1472 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 0, 0, structureBoundingBoxIn);
/* 1473 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
/* 1474 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
/* 1475 */       setBlockState(worldIn, Blocks.wool.getStateFromMeta(net.minecraft.item.EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, structureBoundingBoxIn);
/* 1476 */       boolean flag = (this.coordBaseMode == EnumFacing.EAST) || (this.coordBaseMode == EnumFacing.NORTH);
/* 1477 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()), flag ? 2 : 0, 3, 0, structureBoundingBoxIn);
/* 1478 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 1, 3, 1, structureBoundingBoxIn);
/* 1479 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()), flag ? 0 : 2, 3, 0, structureBoundingBoxIn);
/* 1480 */       setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 1, 3, -1, structureBoundingBoxIn);
/* 1481 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Village extends StructureComponent
/*      */   {
/* 1487 */     protected int field_143015_k = -1;
/*      */     
/*      */     private int villagersSpawned;
/*      */     
/*      */     private boolean isDesertVillage;
/*      */     
/*      */     public Village() {}
/*      */     
/*      */     protected Village(StructureVillagePieces.Start start, int type)
/*      */     {
/* 1497 */       super();
/*      */       
/* 1499 */       if (start != null)
/*      */       {
/* 1501 */         this.isDesertVillage = start.inDesert;
/*      */       }
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1507 */       tagCompound.setInteger("HPos", this.field_143015_k);
/* 1508 */       tagCompound.setInteger("VCount", this.villagersSpawned);
/* 1509 */       tagCompound.setBoolean("Desert", this.isDesertVillage);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1514 */       this.field_143015_k = tagCompound.getInteger("HPos");
/* 1515 */       this.villagersSpawned = tagCompound.getInteger("VCount");
/* 1516 */       this.isDesertVillage = tagCompound.getBoolean("Desert");
/*      */     }
/*      */     
/*      */     protected StructureComponent getNextComponentNN(StructureVillagePieces.Start start, List<StructureComponent> p_74891_2_, Random rand, int p_74891_4_, int p_74891_5_)
/*      */     {
/* 1521 */       if (this.coordBaseMode != null)
/*      */       {
/* 1523 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1526 */           return StructureVillagePieces.func_176066_d(start, p_74891_2_, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, getComponentType());
/*      */         
/*      */         case SOUTH: 
/* 1529 */           return StructureVillagePieces.func_176066_d(start, p_74891_2_, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, getComponentType());
/*      */         
/*      */         case UP: 
/* 1532 */           return StructureVillagePieces.func_176066_d(start, p_74891_2_, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/*      */         
/*      */         case WEST: 
/* 1535 */           return StructureVillagePieces.func_176066_d(start, p_74891_2_, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/*      */         }
/*      */         
/*      */       }
/* 1539 */       return null;
/*      */     }
/*      */     
/*      */     protected StructureComponent getNextComponentPP(StructureVillagePieces.Start start, List<StructureComponent> p_74894_2_, Random rand, int p_74894_4_, int p_74894_5_)
/*      */     {
/* 1544 */       if (this.coordBaseMode != null)
/*      */       {
/* 1546 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1549 */           return StructureVillagePieces.func_176066_d(start, p_74894_2_, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, getComponentType());
/*      */         
/*      */         case SOUTH: 
/* 1552 */           return StructureVillagePieces.func_176066_d(start, p_74894_2_, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, getComponentType());
/*      */         
/*      */         case UP: 
/* 1555 */           return StructureVillagePieces.func_176066_d(start, p_74894_2_, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */         
/*      */         case WEST: 
/* 1558 */           return StructureVillagePieces.func_176066_d(start, p_74894_2_, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */         }
/*      */         
/*      */       }
/* 1562 */       return null;
/*      */     }
/*      */     
/*      */     protected int getAverageGroundLevel(World worldIn, StructureBoundingBox p_74889_2_)
/*      */     {
/* 1567 */       int i = 0;
/* 1568 */       int j = 0;
/* 1569 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */       
/* 1571 */       for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; k++)
/*      */       {
/* 1573 */         for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; l++)
/*      */         {
/* 1575 */           blockpos$mutableblockpos.func_181079_c(l, 64, k);
/*      */           
/* 1577 */           if (p_74889_2_.isVecInside(blockpos$mutableblockpos))
/*      */           {
/* 1579 */             i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel());
/* 1580 */             j++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1585 */       if (j == 0)
/*      */       {
/* 1587 */         return -1;
/*      */       }
/*      */       
/*      */ 
/* 1591 */       return i / j;
/*      */     }
/*      */     
/*      */ 
/*      */     protected static boolean canVillageGoDeeper(StructureBoundingBox p_74895_0_)
/*      */     {
/* 1597 */       return (p_74895_0_ != null) && (p_74895_0_.minY > 10);
/*      */     }
/*      */     
/*      */     protected void spawnVillagers(World worldIn, StructureBoundingBox p_74893_2_, int p_74893_3_, int p_74893_4_, int p_74893_5_, int p_74893_6_)
/*      */     {
/* 1602 */       if (this.villagersSpawned < p_74893_6_)
/*      */       {
/* 1604 */         for (int i = this.villagersSpawned; i < p_74893_6_; i++)
/*      */         {
/* 1606 */           int j = getXWithOffset(p_74893_3_ + i, p_74893_5_);
/* 1607 */           int k = getYWithOffset(p_74893_4_);
/* 1608 */           int l = getZWithOffset(p_74893_3_ + i, p_74893_5_);
/*      */           
/* 1610 */           if (!p_74893_2_.isVecInside(new BlockPos(j, k, l))) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/* 1615 */           this.villagersSpawned += 1;
/* 1616 */           EntityVillager entityvillager = new EntityVillager(worldIn);
/* 1617 */           entityvillager.setLocationAndAngles(j + 0.5D, k, l + 0.5D, 0.0F, 0.0F);
/* 1618 */           entityvillager.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)), null);
/* 1619 */           entityvillager.setProfession(func_180779_c(i, entityvillager.getProfession()));
/* 1620 */           worldIn.spawnEntityInWorld(entityvillager);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected int func_180779_c(int p_180779_1_, int p_180779_2_)
/*      */     {
/* 1627 */       return p_180779_2_;
/*      */     }
/*      */     
/*      */     protected IBlockState func_175847_a(IBlockState p_175847_1_)
/*      */     {
/* 1632 */       if (this.isDesertVillage)
/*      */       {
/* 1634 */         if ((p_175847_1_.getBlock() == Blocks.log) || (p_175847_1_.getBlock() == Blocks.log2))
/*      */         {
/* 1636 */           return Blocks.sandstone.getDefaultState();
/*      */         }
/*      */         
/* 1639 */         if (p_175847_1_.getBlock() == Blocks.cobblestone)
/*      */         {
/* 1641 */           return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
/*      */         }
/*      */         
/* 1644 */         if (p_175847_1_.getBlock() == Blocks.planks)
/*      */         {
/* 1646 */           return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
/*      */         }
/*      */         
/* 1649 */         if (p_175847_1_.getBlock() == Blocks.oak_stairs)
/*      */         {
/* 1651 */           return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, (EnumFacing)p_175847_1_.getValue(BlockStairs.FACING));
/*      */         }
/*      */         
/* 1654 */         if (p_175847_1_.getBlock() == Blocks.stone_stairs)
/*      */         {
/* 1656 */           return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, (EnumFacing)p_175847_1_.getValue(BlockStairs.FACING));
/*      */         }
/*      */         
/* 1659 */         if (p_175847_1_.getBlock() == Blocks.gravel)
/*      */         {
/* 1661 */           return Blocks.sandstone.getDefaultState();
/*      */         }
/*      */       }
/*      */       
/* 1665 */       return p_175847_1_;
/*      */     }
/*      */     
/*      */     protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
/*      */     {
/* 1670 */       IBlockState iblockstate = func_175847_a(blockstateIn);
/* 1671 */       super.setBlockState(worldIn, iblockstate, x, y, z, boundingboxIn);
/*      */     }
/*      */     
/*      */     protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState boundaryBlockState, IBlockState insideBlockState, boolean existingOnly)
/*      */     {
/* 1676 */       IBlockState iblockstate = func_175847_a(boundaryBlockState);
/* 1677 */       IBlockState iblockstate1 = func_175847_a(insideBlockState);
/* 1678 */       super.fillWithBlocks(worldIn, boundingboxIn, xMin, yMin, zMin, xMax, yMax, zMax, iblockstate, iblockstate1, existingOnly);
/*      */     }
/*      */     
/*      */     protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
/*      */     {
/* 1683 */       IBlockState iblockstate = func_175847_a(blockstateIn);
/* 1684 */       super.replaceAirAndLiquidDownwards(worldIn, iblockstate, x, y, z, boundingboxIn);
/*      */     }
/*      */     
/*      */     protected void func_175846_a(boolean p_175846_1_)
/*      */     {
/* 1689 */       this.isDesertVillage = p_175846_1_;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Well
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     public Well() {}
/*      */     
/*      */     public Well(StructureVillagePieces.Start start, int p_i2109_2_, Random rand, int p_i2109_4_, int p_i2109_5_)
/*      */     {
/* 1701 */       super(p_i2109_2_);
/* 1702 */       this.coordBaseMode = net.minecraft.util.EnumFacing.Plane.HORIZONTAL.random(rand);
/*      */       
/* 1704 */       switch (this.coordBaseMode)
/*      */       {
/*      */       case NORTH: 
/*      */       case SOUTH: 
/* 1708 */         this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
/* 1709 */         break;
/*      */       
/*      */       default: 
/* 1712 */         this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
/*      */       }
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1718 */       StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, getComponentType());
/* 1719 */       StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, getComponentType());
/* 1720 */       StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/* 1721 */       StructureVillagePieces.func_176069_e((StructureVillagePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1726 */       if (this.field_143015_k < 0)
/*      */       {
/* 1728 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/* 1730 */         if (this.field_143015_k < 0)
/*      */         {
/* 1732 */           return true;
/*      */         }
/*      */         
/* 1735 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
/*      */       }
/*      */       
/* 1738 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 4, 12, 4, Blocks.cobblestone.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
/* 1739 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 12, 2, structureBoundingBoxIn);
/* 1740 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 3, 12, 2, structureBoundingBoxIn);
/* 1741 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 12, 3, structureBoundingBoxIn);
/* 1742 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 3, 12, 3, structureBoundingBoxIn);
/* 1743 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 13, 1, structureBoundingBoxIn);
/* 1744 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 14, 1, structureBoundingBoxIn);
/* 1745 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 13, 1, structureBoundingBoxIn);
/* 1746 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 14, 1, structureBoundingBoxIn);
/* 1747 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 13, 4, structureBoundingBoxIn);
/* 1748 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 14, 4, structureBoundingBoxIn);
/* 1749 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 13, 4, structureBoundingBoxIn);
/* 1750 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 14, 4, structureBoundingBoxIn);
/* 1751 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 15, 1, 4, 15, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/*      */       
/* 1753 */       for (int i = 0; i <= 5; i++)
/*      */       {
/* 1755 */         for (int j = 0; j <= 5; j++)
/*      */         {
/* 1757 */           if ((j == 0) || (j == 5) || (i == 0) || (i == 5))
/*      */           {
/* 1759 */             setBlockState(worldIn, Blocks.gravel.getDefaultState(), j, 11, i, structureBoundingBoxIn);
/* 1760 */             clearCurrentPositionBlocksUpwards(worldIn, j, 12, i, structureBoundingBoxIn);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1765 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class WoodHut
/*      */     extends StructureVillagePieces.Village
/*      */   {
/*      */     private boolean isTallHouse;
/*      */     private int tablePosition;
/*      */     
/*      */     public WoodHut() {}
/*      */     
/*      */     public WoodHut(StructureVillagePieces.Start start, int p_i45565_2_, Random rand, StructureBoundingBox p_i45565_4_, EnumFacing facing)
/*      */     {
/* 1780 */       super(p_i45565_2_);
/* 1781 */       this.coordBaseMode = facing;
/* 1782 */       this.boundingBox = p_i45565_4_;
/* 1783 */       this.isTallHouse = rand.nextBoolean();
/* 1784 */       this.tablePosition = rand.nextInt(3);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1789 */       super.writeStructureToNBT(tagCompound);
/* 1790 */       tagCompound.setInteger("T", this.tablePosition);
/* 1791 */       tagCompound.setBoolean("C", this.isTallHouse);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1796 */       super.readStructureFromNBT(tagCompound);
/* 1797 */       this.tablePosition = tagCompound.getInteger("T");
/* 1798 */       this.isTallHouse = tagCompound.getBoolean("C");
/*      */     }
/*      */     
/*      */     public static WoodHut func_175853_a(StructureVillagePieces.Start start, List<StructureComponent> p_175853_1_, Random rand, int p_175853_3_, int p_175853_4_, int p_175853_5_, EnumFacing facing, int p_175853_7_)
/*      */     {
/* 1803 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175853_3_, p_175853_4_, p_175853_5_, 0, 0, 0, 4, 6, 5, facing);
/* 1804 */       return (canVillageGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175853_1_, structureboundingbox) == null) ? new WoodHut(start, p_175853_7_, rand, structureboundingbox, facing) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1809 */       if (this.field_143015_k < 0)
/*      */       {
/* 1811 */         this.field_143015_k = getAverageGroundLevel(worldIn, structureBoundingBoxIn);
/*      */         
/* 1813 */         if (this.field_143015_k < 0)
/*      */         {
/* 1815 */           return true;
/*      */         }
/*      */         
/* 1818 */         this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
/*      */       }
/*      */       
/* 1821 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1822 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 3, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
/* 1823 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 3, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
/*      */       
/* 1825 */       if (this.isTallHouse)
/*      */       {
/* 1827 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 2, 4, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*      */       }
/*      */       else
/*      */       {
/* 1831 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 2, 5, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/*      */       }
/*      */       
/* 1834 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 1, 4, 0, structureBoundingBoxIn);
/* 1835 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 2, 4, 0, structureBoundingBoxIn);
/* 1836 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 1, 4, 4, structureBoundingBoxIn);
/* 1837 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 2, 4, 4, structureBoundingBoxIn);
/* 1838 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 0, 4, 1, structureBoundingBoxIn);
/* 1839 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 0, 4, 2, structureBoundingBoxIn);
/* 1840 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 0, 4, 3, structureBoundingBoxIn);
/* 1841 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 3, 4, 1, structureBoundingBoxIn);
/* 1842 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 3, 4, 2, structureBoundingBoxIn);
/* 1843 */       setBlockState(worldIn, Blocks.log.getDefaultState(), 3, 4, 3, structureBoundingBoxIn);
/* 1844 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 1845 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 3, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 1846 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 1847 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 4, 3, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 1848 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1849 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 1, 3, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1850 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1851 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 2, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/* 1852 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
/* 1853 */       setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 3, 2, 2, structureBoundingBoxIn);
/*      */       
/* 1855 */       if (this.tablePosition > 0)
/*      */       {
/* 1857 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), this.tablePosition, 1, 3, structureBoundingBoxIn);
/* 1858 */         setBlockState(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), this.tablePosition, 2, 3, structureBoundingBoxIn);
/*      */       }
/*      */       
/* 1861 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
/* 1862 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
/* 1863 */       placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 1, 1, 0, EnumFacing.getHorizontal(getMetadataWithOffset(Blocks.oak_door, 1)));
/*      */       
/* 1865 */       if ((getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air) && (getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air))
/*      */       {
/* 1867 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, structureBoundingBoxIn);
/*      */       }
/*      */       
/* 1870 */       for (int i = 0; i < 5; i++)
/*      */       {
/* 1872 */         for (int j = 0; j < 4; j++)
/*      */         {
/* 1874 */           clearCurrentPositionBlocksUpwards(worldIn, j, 6, i, structureBoundingBoxIn);
/* 1875 */           replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), j, -1, i, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1879 */       spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1);
/* 1880 */       return true;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureVillagePieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */