/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLever;
/*     */ import net.minecraft.block.BlockPlanks.EnumType;
/*     */ import net.minecraft.block.BlockRedstoneWire;
/*     */ import net.minecraft.block.BlockSandStone.EnumType;
/*     */ import net.minecraft.block.BlockStoneBrick;
/*     */ import net.minecraft.block.BlockStoneSlab.EnumType;
/*     */ import net.minecraft.block.BlockTripWire;
/*     */ import net.minecraft.block.BlockTripWireHook;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.WeightedRandomChestContent;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ComponentScatteredFeaturePieces
/*     */ {
/*     */   public static void registerScatteredFeaturePieces()
/*     */   {
/*  29 */     MapGenStructureIO.registerStructureComponent(DesertPyramid.class, "TeDP");
/*  30 */     MapGenStructureIO.registerStructureComponent(JunglePyramid.class, "TeJP");
/*  31 */     MapGenStructureIO.registerStructureComponent(SwampHut.class, "TeSH");
/*     */   }
/*     */   
/*     */   public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature
/*     */   {
/*  36 */     private boolean[] field_74940_h = new boolean[4];
/*  37 */     private static final java.util.List<WeightedRandomChestContent> itemsToGenerateInTemple = com.google.common.collect.Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
/*     */     
/*     */ 
/*     */     public DesertPyramid() {}
/*     */     
/*     */ 
/*     */     public DesertPyramid(Random p_i2062_1_, int p_i2062_2_, int p_i2062_3_)
/*     */     {
/*  45 */       super(p_i2062_2_, 64, p_i2062_3_, 21, 15, 21);
/*     */     }
/*     */     
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*     */     {
/*  50 */       super.writeStructureToNBT(tagCompound);
/*  51 */       tagCompound.setBoolean("hasPlacedChest0", this.field_74940_h[0]);
/*  52 */       tagCompound.setBoolean("hasPlacedChest1", this.field_74940_h[1]);
/*  53 */       tagCompound.setBoolean("hasPlacedChest2", this.field_74940_h[2]);
/*  54 */       tagCompound.setBoolean("hasPlacedChest3", this.field_74940_h[3]);
/*     */     }
/*     */     
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*     */     {
/*  59 */       super.readStructureFromNBT(tagCompound);
/*  60 */       this.field_74940_h[0] = tagCompound.getBoolean("hasPlacedChest0");
/*  61 */       this.field_74940_h[1] = tagCompound.getBoolean("hasPlacedChest1");
/*  62 */       this.field_74940_h[2] = tagCompound.getBoolean("hasPlacedChest2");
/*  63 */       this.field_74940_h[3] = tagCompound.getBoolean("hasPlacedChest3");
/*     */     }
/*     */     
/*     */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*     */     {
/*  68 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/*     */       
/*  70 */       for (int i = 1; i <= 9; i++)
/*     */       {
/*  72 */         fillWithBlocks(worldIn, structureBoundingBoxIn, i, i, i, this.scatteredFeatureSizeX - 1 - i, i, this.scatteredFeatureSizeZ - 1 - i, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/*  73 */         fillWithBlocks(worldIn, structureBoundingBoxIn, i + 1, i, i + 1, this.scatteredFeatureSizeX - 2 - i, i, this.scatteredFeatureSizeZ - 2 - i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       }
/*     */       
/*  76 */       for (int j2 = 0; j2 < this.scatteredFeatureSizeX; j2++)
/*     */       {
/*  78 */         for (int j = 0; j < this.scatteredFeatureSizeZ; j++)
/*     */         {
/*  80 */           int k = -5;
/*  81 */           replaceAirAndLiquidDownwards(worldIn, Blocks.sandstone.getDefaultState(), j2, k, j, structureBoundingBoxIn);
/*     */         }
/*     */       }
/*     */       
/*  85 */       int k2 = getMetadataWithOffset(Blocks.sandstone_stairs, 3);
/*  86 */       int l2 = getMetadataWithOffset(Blocks.sandstone_stairs, 2);
/*  87 */       int i3 = getMetadataWithOffset(Blocks.sandstone_stairs, 0);
/*  88 */       int l = getMetadataWithOffset(Blocks.sandstone_stairs, 1);
/*  89 */       int i1 = (EnumDyeColor.ORANGE.getDyeDamage() ^ 0xFFFFFFFF) & 0xF;
/*  90 */       int j1 = (EnumDyeColor.BLUE.getDyeDamage() ^ 0xFFFFFFFF) & 0xF;
/*  91 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  92 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 10, 1, 3, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/*  93 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 10, 0, structureBoundingBoxIn);
/*  94 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l2), 2, 10, 4, structureBoundingBoxIn);
/*  95 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i3), 0, 10, 2, structureBoundingBoxIn);
/*  96 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l), 4, 10, 2, structureBoundingBoxIn);
/*  97 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 0, 0, this.scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  98 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 4, 10, 1, this.scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/*  99 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 10, 0, structureBoundingBoxIn);
/* 100 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l2), this.scatteredFeatureSizeX - 3, 10, 4, structureBoundingBoxIn);
/* 101 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i3), this.scatteredFeatureSizeX - 5, 10, 2, structureBoundingBoxIn);
/* 102 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l), this.scatteredFeatureSizeX - 1, 10, 2, structureBoundingBoxIn);
/* 103 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 12, 4, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 104 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 0, 11, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 105 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, structureBoundingBoxIn);
/* 106 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, structureBoundingBoxIn);
/* 107 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, structureBoundingBoxIn);
/* 108 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, structureBoundingBoxIn);
/* 109 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, structureBoundingBoxIn);
/* 110 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, structureBoundingBoxIn);
/* 111 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, structureBoundingBoxIn);
/* 112 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 8, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 113 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 2, 8, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 114 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 1, 16, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 115 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 2, 16, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 116 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, this.scatteredFeatureSizeX - 6, 4, this.scatteredFeatureSizeZ - 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 117 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 4, 9, 11, 4, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 118 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 8, 8, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 119 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 8, 12, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 120 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 12, 8, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 121 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 12, 12, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 122 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 4, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 123 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 1, 5, this.scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 124 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 7, 9, 6, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 125 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 7, 7, 9, this.scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 126 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 9, 5, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 127 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 6, 5, 9, this.scatteredFeatureSizeX - 6, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 128 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 5, 10, structureBoundingBoxIn);
/* 129 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 6, 10, structureBoundingBoxIn);
/* 130 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 6, 6, 10, structureBoundingBoxIn);
/* 131 */       setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 5, 10, structureBoundingBoxIn);
/* 132 */       setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 6, 10, structureBoundingBoxIn);
/* 133 */       setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 7, 6, 10, structureBoundingBoxIn);
/* 134 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 4, 2, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 135 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 3, 4, 4, this.scatteredFeatureSizeX - 3, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 136 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 4, 5, structureBoundingBoxIn);
/* 137 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 3, 4, structureBoundingBoxIn);
/* 138 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 4, 5, structureBoundingBoxIn);
/* 139 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 3, 4, structureBoundingBoxIn);
/* 140 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 3, 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 141 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 3, 1, 3, this.scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 142 */       setBlockState(worldIn, Blocks.sandstone_stairs.getDefaultState(), 1, 1, 2, structureBoundingBoxIn);
/* 143 */       setBlockState(worldIn, Blocks.sandstone_stairs.getDefaultState(), this.scatteredFeatureSizeX - 2, 1, 2, structureBoundingBoxIn);
/* 144 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, structureBoundingBoxIn);
/* 145 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), this.scatteredFeatureSizeX - 2, 2, 2, structureBoundingBoxIn);
/* 146 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l), 2, 1, 2, structureBoundingBoxIn);
/* 147 */       setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i3), this.scatteredFeatureSizeX - 3, 1, 2, structureBoundingBoxIn);
/* 148 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 5, 4, 3, 18, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 149 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 3, 5, this.scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 150 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 5, 4, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 151 */       fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 6, 1, 5, this.scatteredFeatureSizeX - 5, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*     */       
/* 153 */       for (int k1 = 5; k1 <= 17; k1 += 2)
/*     */       {
/* 155 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, k1, structureBoundingBoxIn);
/* 156 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, k1, structureBoundingBoxIn);
/* 157 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), this.scatteredFeatureSizeX - 5, 1, k1, structureBoundingBoxIn);
/* 158 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), this.scatteredFeatureSizeX - 5, 2, k1, structureBoundingBoxIn);
/*     */       }
/*     */       
/* 161 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 7, structureBoundingBoxIn);
/* 162 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 8, structureBoundingBoxIn);
/* 163 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 9, 0, 9, structureBoundingBoxIn);
/* 164 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 11, 0, 9, structureBoundingBoxIn);
/* 165 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 8, 0, 10, structureBoundingBoxIn);
/* 166 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 12, 0, 10, structureBoundingBoxIn);
/* 167 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 7, 0, 10, structureBoundingBoxIn);
/* 168 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 13, 0, 10, structureBoundingBoxIn);
/* 169 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 9, 0, 11, structureBoundingBoxIn);
/* 170 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 11, 0, 11, structureBoundingBoxIn);
/* 171 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 12, structureBoundingBoxIn);
/* 172 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 13, structureBoundingBoxIn);
/* 173 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(j1), 10, 0, 10, structureBoundingBoxIn);
/*     */       
/* 175 */       for (int j3 = 0; j3 <= this.scatteredFeatureSizeX - 1; j3 += this.scatteredFeatureSizeX - 1)
/*     */       {
/* 177 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 2, 1, structureBoundingBoxIn);
/* 178 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 2, 2, structureBoundingBoxIn);
/* 179 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 2, 3, structureBoundingBoxIn);
/* 180 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 3, 1, structureBoundingBoxIn);
/* 181 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 3, 2, structureBoundingBoxIn);
/* 182 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 3, 3, structureBoundingBoxIn);
/* 183 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 4, 1, structureBoundingBoxIn);
/* 184 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j3, 4, 2, structureBoundingBoxIn);
/* 185 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 4, 3, structureBoundingBoxIn);
/* 186 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 5, 1, structureBoundingBoxIn);
/* 187 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 5, 2, structureBoundingBoxIn);
/* 188 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 5, 3, structureBoundingBoxIn);
/* 189 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 6, 1, structureBoundingBoxIn);
/* 190 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j3, 6, 2, structureBoundingBoxIn);
/* 191 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 6, 3, structureBoundingBoxIn);
/* 192 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 7, 1, structureBoundingBoxIn);
/* 193 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 7, 2, structureBoundingBoxIn);
/* 194 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 7, 3, structureBoundingBoxIn);
/* 195 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 8, 1, structureBoundingBoxIn);
/* 196 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 8, 2, structureBoundingBoxIn);
/* 197 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 8, 3, structureBoundingBoxIn);
/*     */       }
/*     */       
/* 200 */       for (int k3 = 2; k3 <= this.scatteredFeatureSizeX - 3; k3 += this.scatteredFeatureSizeX - 3 - 2)
/*     */       {
/* 202 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 2, 0, structureBoundingBoxIn);
/* 203 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 2, 0, structureBoundingBoxIn);
/* 204 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 2, 0, structureBoundingBoxIn);
/* 205 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 3, 0, structureBoundingBoxIn);
/* 206 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 3, 0, structureBoundingBoxIn);
/* 207 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 3, 0, structureBoundingBoxIn);
/* 208 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 - 1, 4, 0, structureBoundingBoxIn);
/* 209 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k3, 4, 0, structureBoundingBoxIn);
/* 210 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 + 1, 4, 0, structureBoundingBoxIn);
/* 211 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 5, 0, structureBoundingBoxIn);
/* 212 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 5, 0, structureBoundingBoxIn);
/* 213 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 5, 0, structureBoundingBoxIn);
/* 214 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 - 1, 6, 0, structureBoundingBoxIn);
/* 215 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k3, 6, 0, structureBoundingBoxIn);
/* 216 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 + 1, 6, 0, structureBoundingBoxIn);
/* 217 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 - 1, 7, 0, structureBoundingBoxIn);
/* 218 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 7, 0, structureBoundingBoxIn);
/* 219 */         setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 + 1, 7, 0, structureBoundingBoxIn);
/* 220 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 8, 0, structureBoundingBoxIn);
/* 221 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3, 8, 0, structureBoundingBoxIn);
/* 222 */         setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 8, 0, structureBoundingBoxIn);
/*     */       }
/*     */       
/* 225 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 0, 12, 6, 0, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 226 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 8, 6, 0, structureBoundingBoxIn);
/* 227 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 12, 6, 0, structureBoundingBoxIn);
/* 228 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 9, 5, 0, structureBoundingBoxIn);
/* 229 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, structureBoundingBoxIn);
/* 230 */       setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 11, 5, 0, structureBoundingBoxIn);
/* 231 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -14, 8, 12, -11, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 232 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -10, 8, 12, -10, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
/* 233 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -9, 8, 12, -9, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
/* 234 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -8, 8, 12, -1, 12, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
/* 235 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -11, 9, 11, -1, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 236 */       setBlockState(worldIn, Blocks.stone_pressure_plate.getDefaultState(), 10, -11, 10, structureBoundingBoxIn);
/* 237 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -13, 9, 11, -13, 11, Blocks.tnt.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 238 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 8, -11, 10, structureBoundingBoxIn);
/* 239 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 8, -10, 10, structureBoundingBoxIn);
/* 240 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, structureBoundingBoxIn);
/* 241 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, structureBoundingBoxIn);
/* 242 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 12, -11, 10, structureBoundingBoxIn);
/* 243 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 12, -10, 10, structureBoundingBoxIn);
/* 244 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, structureBoundingBoxIn);
/* 245 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, structureBoundingBoxIn);
/* 246 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -11, 8, structureBoundingBoxIn);
/* 247 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -10, 8, structureBoundingBoxIn);
/* 248 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, structureBoundingBoxIn);
/* 249 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, structureBoundingBoxIn);
/* 250 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -11, 12, structureBoundingBoxIn);
/* 251 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -10, 12, structureBoundingBoxIn);
/* 252 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, structureBoundingBoxIn);
/* 253 */       setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, structureBoundingBoxIn);
/*     */       
/* 255 */       for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 257 */         EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */         
/* 259 */         if (this.field_74940_h[enumfacing.getHorizontalIndex()] == 0)
/*     */         {
/* 261 */           int l1 = enumfacing.getFrontOffsetX() * 2;
/* 262 */           int i2 = enumfacing.getFrontOffsetZ() * 2;
/* 263 */           this.field_74940_h[enumfacing.getHorizontalIndex()] = generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 10 + l1, -11, 10 + i2, WeightedRandomChestContent.func_177629_a(itemsToGenerateInTemple, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 2 + randomIn.nextInt(5));
/*     */         }
/*     */       }
/*     */       
/* 267 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class Feature extends StructureComponent
/*     */   {
/*     */     protected int scatteredFeatureSizeX;
/*     */     protected int scatteredFeatureSizeY;
/*     */     protected int scatteredFeatureSizeZ;
/* 276 */     protected int field_74936_d = -1;
/*     */     
/*     */ 
/*     */     public Feature() {}
/*     */     
/*     */ 
/*     */     protected Feature(Random p_i2065_1_, int p_i2065_2_, int p_i2065_3_, int p_i2065_4_, int p_i2065_5_, int p_i2065_6_, int p_i2065_7_)
/*     */     {
/* 284 */       super();
/* 285 */       this.scatteredFeatureSizeX = p_i2065_5_;
/* 286 */       this.scatteredFeatureSizeY = p_i2065_6_;
/* 287 */       this.scatteredFeatureSizeZ = p_i2065_7_;
/* 288 */       this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2065_1_);
/*     */       
/* 290 */       switch (this.coordBaseMode)
/*     */       {
/*     */       case NORTH: 
/*     */       case SOUTH: 
/* 294 */         this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_5_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_7_ - 1);
/* 295 */         break;
/*     */       
/*     */       default: 
/* 298 */         this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_7_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_5_ - 1);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 304 */       tagCompound.setInteger("Width", this.scatteredFeatureSizeX);
/* 305 */       tagCompound.setInteger("Height", this.scatteredFeatureSizeY);
/* 306 */       tagCompound.setInteger("Depth", this.scatteredFeatureSizeZ);
/* 307 */       tagCompound.setInteger("HPos", this.field_74936_d);
/*     */     }
/*     */     
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 312 */       this.scatteredFeatureSizeX = tagCompound.getInteger("Width");
/* 313 */       this.scatteredFeatureSizeY = tagCompound.getInteger("Height");
/* 314 */       this.scatteredFeatureSizeZ = tagCompound.getInteger("Depth");
/* 315 */       this.field_74936_d = tagCompound.getInteger("HPos");
/*     */     }
/*     */     
/*     */     protected boolean func_74935_a(World worldIn, StructureBoundingBox p_74935_2_, int p_74935_3_)
/*     */     {
/* 320 */       if (this.field_74936_d >= 0)
/*     */       {
/* 322 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 326 */       int i = 0;
/* 327 */       int j = 0;
/* 328 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */       
/* 330 */       for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; k++)
/*     */       {
/* 332 */         for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; l++)
/*     */         {
/* 334 */           blockpos$mutableblockpos.func_181079_c(l, 64, k);
/*     */           
/* 336 */           if (p_74935_2_.isVecInside(blockpos$mutableblockpos))
/*     */           {
/* 338 */             i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel());
/* 339 */             j++;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 344 */       if (j == 0)
/*     */       {
/* 346 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 350 */       this.field_74936_d = (i / j);
/* 351 */       this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + p_74935_3_, 0);
/* 352 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class JunglePyramid
/*     */     extends ComponentScatteredFeaturePieces.Feature
/*     */   {
/*     */     private boolean field_74947_h;
/*     */     private boolean field_74948_i;
/*     */     private boolean field_74945_j;
/*     */     private boolean field_74946_k;
/* 364 */     private static final java.util.List<WeightedRandomChestContent> field_175816_i = com.google.common.collect.Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
/* 365 */     private static final java.util.List<WeightedRandomChestContent> field_175815_j = com.google.common.collect.Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.arrow, 0, 2, 7, 30) });
/* 366 */     private static Stones junglePyramidsRandomScatteredStones = new Stones(null);
/*     */     
/*     */ 
/*     */     public JunglePyramid() {}
/*     */     
/*     */ 
/*     */     public JunglePyramid(Random p_i2064_1_, int p_i2064_2_, int p_i2064_3_)
/*     */     {
/* 374 */       super(p_i2064_2_, 64, p_i2064_3_, 12, 10, 15);
/*     */     }
/*     */     
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 379 */       super.writeStructureToNBT(tagCompound);
/* 380 */       tagCompound.setBoolean("placedMainChest", this.field_74947_h);
/* 381 */       tagCompound.setBoolean("placedHiddenChest", this.field_74948_i);
/* 382 */       tagCompound.setBoolean("placedTrap1", this.field_74945_j);
/* 383 */       tagCompound.setBoolean("placedTrap2", this.field_74946_k);
/*     */     }
/*     */     
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 388 */       super.readStructureFromNBT(tagCompound);
/* 389 */       this.field_74947_h = tagCompound.getBoolean("placedMainChest");
/* 390 */       this.field_74948_i = tagCompound.getBoolean("placedHiddenChest");
/* 391 */       this.field_74945_j = tagCompound.getBoolean("placedTrap1");
/* 392 */       this.field_74946_k = tagCompound.getBoolean("placedTrap2");
/*     */     }
/*     */     
/*     */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*     */     {
/* 397 */       if (!func_74935_a(worldIn, structureBoundingBoxIn, 0))
/*     */       {
/* 399 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 403 */       int i = getMetadataWithOffset(Blocks.stone_stairs, 3);
/* 404 */       int j = getMetadataWithOffset(Blocks.stone_stairs, 2);
/* 405 */       int k = getMetadataWithOffset(Blocks.stone_stairs, 0);
/* 406 */       int l = getMetadataWithOffset(Blocks.stone_stairs, 1);
/* 407 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 408 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 9, 2, 2, false, randomIn, junglePyramidsRandomScatteredStones);
/* 409 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 12, 9, 2, 12, false, randomIn, junglePyramidsRandomScatteredStones);
/* 410 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 2, 11, false, randomIn, junglePyramidsRandomScatteredStones);
/* 411 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 1, 3, 9, 2, 11, false, randomIn, junglePyramidsRandomScatteredStones);
/* 412 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 10, 6, 1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 413 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 13, 10, 6, 13, false, randomIn, junglePyramidsRandomScatteredStones);
/* 414 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 2, 1, 6, 12, false, randomIn, junglePyramidsRandomScatteredStones);
/* 415 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, 3, 2, 10, 6, 12, false, randomIn, junglePyramidsRandomScatteredStones);
/* 416 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 3, 2, 9, 3, 12, false, randomIn, junglePyramidsRandomScatteredStones);
/* 417 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 6, 2, 9, 6, 12, false, randomIn, junglePyramidsRandomScatteredStones);
/* 418 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 3, 7, 3, 8, 7, 11, false, randomIn, junglePyramidsRandomScatteredStones);
/* 419 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 8, 4, 7, 8, 10, false, randomIn, junglePyramidsRandomScatteredStones);
/* 420 */       fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 3, 8, 2, 11);
/* 421 */       fillWithAir(worldIn, structureBoundingBoxIn, 4, 3, 6, 7, 3, 9);
/* 422 */       fillWithAir(worldIn, structureBoundingBoxIn, 2, 4, 2, 9, 5, 12);
/* 423 */       fillWithAir(worldIn, structureBoundingBoxIn, 4, 6, 5, 7, 6, 9);
/* 424 */       fillWithAir(worldIn, structureBoundingBoxIn, 5, 7, 6, 6, 7, 8);
/* 425 */       fillWithAir(worldIn, structureBoundingBoxIn, 5, 1, 2, 6, 2, 2);
/* 426 */       fillWithAir(worldIn, structureBoundingBoxIn, 5, 2, 12, 6, 2, 12);
/* 427 */       fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 1, 6, 5, 1);
/* 428 */       fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 13, 6, 5, 13);
/* 429 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 5, 5, structureBoundingBoxIn);
/* 430 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 10, 5, 5, structureBoundingBoxIn);
/* 431 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 5, 9, structureBoundingBoxIn);
/* 432 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 10, 5, 9, structureBoundingBoxIn);
/*     */       
/* 434 */       for (int i1 = 0; i1 <= 14; i1 += 14)
/*     */       {
/* 436 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 4, i1, 2, 5, i1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 437 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 4, i1, 4, 5, i1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 438 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 4, i1, 7, 5, i1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 439 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 4, i1, 9, 5, i1, false, randomIn, junglePyramidsRandomScatteredStones);
/*     */       }
/*     */       
/* 442 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 6, 0, 6, 6, 0, false, randomIn, junglePyramidsRandomScatteredStones);
/*     */       
/* 444 */       for (int k1 = 0; k1 <= 11; k1 += 11)
/*     */       {
/* 446 */         for (int j1 = 2; j1 <= 12; j1 += 2)
/*     */         {
/* 448 */           fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k1, 4, j1, k1, 5, j1, false, randomIn, junglePyramidsRandomScatteredStones);
/*     */         }
/*     */         
/* 451 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k1, 6, 5, k1, 6, 5, false, randomIn, junglePyramidsRandomScatteredStones);
/* 452 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k1, 6, 9, k1, 6, 9, false, randomIn, junglePyramidsRandomScatteredStones);
/*     */       }
/*     */       
/* 455 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 2, 2, 9, 2, false, randomIn, junglePyramidsRandomScatteredStones);
/* 456 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 2, 9, 9, 2, false, randomIn, junglePyramidsRandomScatteredStones);
/* 457 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 12, 2, 9, 12, false, randomIn, junglePyramidsRandomScatteredStones);
/* 458 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 12, 9, 9, 12, false, randomIn, junglePyramidsRandomScatteredStones);
/* 459 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 4, 4, 9, 4, false, randomIn, junglePyramidsRandomScatteredStones);
/* 460 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 4, 7, 9, 4, false, randomIn, junglePyramidsRandomScatteredStones);
/* 461 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 10, 4, 9, 10, false, randomIn, junglePyramidsRandomScatteredStones);
/* 462 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 10, 7, 9, 10, false, randomIn, junglePyramidsRandomScatteredStones);
/* 463 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 9, 7, 6, 9, 7, false, randomIn, junglePyramidsRandomScatteredStones);
/* 464 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 9, 6, structureBoundingBoxIn);
/* 465 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 9, 6, structureBoundingBoxIn);
/* 466 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 9, 8, structureBoundingBoxIn);
/* 467 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 9, 8, structureBoundingBoxIn);
/* 468 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 0, 0, structureBoundingBoxIn);
/* 469 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 0, 0, structureBoundingBoxIn);
/* 470 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 0, 0, structureBoundingBoxIn);
/* 471 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 0, 0, structureBoundingBoxIn);
/* 472 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 1, 8, structureBoundingBoxIn);
/* 473 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 2, 9, structureBoundingBoxIn);
/* 474 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 3, 10, structureBoundingBoxIn);
/* 475 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 1, 8, structureBoundingBoxIn);
/* 476 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 2, 9, structureBoundingBoxIn);
/* 477 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 3, 10, structureBoundingBoxIn);
/* 478 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 4, 1, 9, false, randomIn, junglePyramidsRandomScatteredStones);
/* 479 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 1, 9, 7, 1, 9, false, randomIn, junglePyramidsRandomScatteredStones);
/* 480 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 10, 7, 2, 10, false, randomIn, junglePyramidsRandomScatteredStones);
/* 481 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, 6, 4, 5, false, randomIn, junglePyramidsRandomScatteredStones);
/* 482 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(k), 4, 4, 5, structureBoundingBoxIn);
/* 483 */       setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(l), 7, 4, 5, structureBoundingBoxIn);
/*     */       
/* 485 */       for (int l1 = 0; l1 < 4; l1++)
/*     */       {
/* 487 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 0 - l1, 6 + l1, structureBoundingBoxIn);
/* 488 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 0 - l1, 6 + l1, structureBoundingBoxIn);
/* 489 */         fillWithAir(worldIn, structureBoundingBoxIn, 5, 0 - l1, 7 + l1, 6, 0 - l1, 9 + l1);
/*     */       }
/*     */       
/* 492 */       fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 12, 10, -1, 13);
/* 493 */       fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 3, -1, 13);
/* 494 */       fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 9, -1, 5);
/*     */       
/* 496 */       for (int i2 = 1; i2 <= 13; i2 += 2)
/*     */       {
/* 498 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -3, i2, 1, -2, i2, false, randomIn, junglePyramidsRandomScatteredStones);
/*     */       }
/*     */       
/* 501 */       for (int j2 = 2; j2 <= 12; j2 += 2)
/*     */       {
/* 503 */         fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -1, j2, 3, -1, j2, false, randomIn, junglePyramidsRandomScatteredStones);
/*     */       }
/*     */       
/* 506 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, -2, 1, 5, -2, 1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 507 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, -2, 1, 9, -2, 1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 508 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -3, 1, 6, -3, 1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 509 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -1, 1, 6, -1, 1, false, randomIn, junglePyramidsRandomScatteredStones);
/* 510 */       setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.EAST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, structureBoundingBoxIn);
/* 511 */       setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.WEST.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, structureBoundingBoxIn);
/* 512 */       setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, structureBoundingBoxIn);
/* 513 */       setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, structureBoundingBoxIn);
/* 514 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 7, structureBoundingBoxIn);
/* 515 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 6, structureBoundingBoxIn);
/* 516 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 5, structureBoundingBoxIn);
/* 517 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 4, structureBoundingBoxIn);
/* 518 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 3, structureBoundingBoxIn);
/* 519 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 2, structureBoundingBoxIn);
/* 520 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 1, structureBoundingBoxIn);
/* 521 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 4, -3, 1, structureBoundingBoxIn);
/* 522 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 3, -3, 1, structureBoundingBoxIn);
/*     */       
/* 524 */       if (!this.field_74945_j)
/*     */       {
/* 526 */         this.field_74945_j = generateDispenserContents(worldIn, structureBoundingBoxIn, randomIn, 3, -2, 1, EnumFacing.NORTH.getIndex(), field_175815_j, 2);
/*     */       }
/*     */       
/* 529 */       setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 3, -2, 2, structureBoundingBoxIn);
/* 530 */       setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.NORTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, structureBoundingBoxIn);
/* 531 */       setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.SOUTH.getHorizontalIndex())).withProperty(BlockTripWireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, structureBoundingBoxIn);
/* 532 */       setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, structureBoundingBoxIn);
/* 533 */       setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, structureBoundingBoxIn);
/* 534 */       setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty(BlockTripWire.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, structureBoundingBoxIn);
/* 535 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -3, 6, structureBoundingBoxIn);
/* 536 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 6, structureBoundingBoxIn);
/* 537 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 5, structureBoundingBoxIn);
/* 538 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 4, structureBoundingBoxIn);
/* 539 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -2, 4, structureBoundingBoxIn);
/*     */       
/* 541 */       if (!this.field_74946_k)
/*     */       {
/* 543 */         this.field_74946_k = generateDispenserContents(worldIn, structureBoundingBoxIn, randomIn, 9, -2, 3, EnumFacing.WEST.getIndex(), field_175815_j, 2);
/*     */       }
/*     */       
/* 546 */       setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 8, -1, 3, structureBoundingBoxIn);
/* 547 */       setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 8, -2, 3, structureBoundingBoxIn);
/*     */       
/* 549 */       if (!this.field_74947_h)
/*     */       {
/* 551 */         this.field_74947_h = generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 8, -3, 3, WeightedRandomChestContent.func_177629_a(field_175816_i, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 2 + randomIn.nextInt(5));
/*     */       }
/*     */       
/* 554 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 2, structureBoundingBoxIn);
/* 555 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 1, structureBoundingBoxIn);
/* 556 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 4, -3, 5, structureBoundingBoxIn);
/* 557 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -2, 5, structureBoundingBoxIn);
/* 558 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -1, 5, structureBoundingBoxIn);
/* 559 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 6, -3, 5, structureBoundingBoxIn);
/* 560 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -2, 5, structureBoundingBoxIn);
/* 561 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -1, 5, structureBoundingBoxIn);
/* 562 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 5, structureBoundingBoxIn);
/* 563 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, -1, 1, 9, -1, 5, false, randomIn, junglePyramidsRandomScatteredStones);
/* 564 */       fillWithAir(worldIn, structureBoundingBoxIn, 8, -3, 8, 10, -1, 10);
/* 565 */       setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, structureBoundingBoxIn);
/* 566 */       setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, structureBoundingBoxIn);
/* 567 */       setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, structureBoundingBoxIn);
/* 568 */       setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 8, -2, 12, structureBoundingBoxIn);
/* 569 */       setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 9, -2, 12, structureBoundingBoxIn);
/* 570 */       setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 10, -2, 12, structureBoundingBoxIn);
/* 571 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, -3, 8, 8, -3, 10, false, randomIn, junglePyramidsRandomScatteredStones);
/* 572 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, -3, 8, 10, -3, 10, false, randomIn, junglePyramidsRandomScatteredStones);
/* 573 */       setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 10, -2, 9, structureBoundingBoxIn);
/* 574 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 9, structureBoundingBoxIn);
/* 575 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 10, structureBoundingBoxIn);
/* 576 */       setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 10, -1, 9, structureBoundingBoxIn);
/* 577 */       setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(EnumFacing.UP.getIndex()), 9, -2, 8, structureBoundingBoxIn);
/* 578 */       setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -2, 8, structureBoundingBoxIn);
/* 579 */       setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -1, 8, structureBoundingBoxIn);
/* 580 */       setBlockState(worldIn, Blocks.unpowered_repeater.getStateFromMeta(getMetadataWithOffset(Blocks.unpowered_repeater, EnumFacing.NORTH.getHorizontalIndex())), 10, -2, 10, structureBoundingBoxIn);
/*     */       
/* 582 */       if (!this.field_74948_i)
/*     */       {
/* 584 */         this.field_74948_i = generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 9, -3, 10, WeightedRandomChestContent.func_177629_a(field_175816_i, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 2 + randomIn.nextInt(5));
/*     */       }
/*     */       
/* 587 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     static class Stones
/*     */       extends StructureComponent.BlockSelector
/*     */     {
/*     */       public void selectBlocks(Random rand, int x, int y, int z, boolean p_75062_5_)
/*     */       {
/* 599 */         if (rand.nextFloat() < 0.4F)
/*     */         {
/* 601 */           this.blockstate = Blocks.cobblestone.getDefaultState();
/*     */         }
/*     */         else
/*     */         {
/* 605 */           this.blockstate = Blocks.mossy_cobblestone.getDefaultState();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class SwampHut
/*     */     extends ComponentScatteredFeaturePieces.Feature
/*     */   {
/*     */     private boolean hasWitch;
/*     */     
/*     */     public SwampHut() {}
/*     */     
/*     */     public SwampHut(Random p_i2066_1_, int p_i2066_2_, int p_i2066_3_)
/*     */     {
/* 621 */       super(p_i2066_2_, 64, p_i2066_3_, 7, 7, 9);
/*     */     }
/*     */     
/*     */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*     */     {
/* 626 */       super.writeStructureToNBT(tagCompound);
/* 627 */       tagCompound.setBoolean("Witch", this.hasWitch);
/*     */     }
/*     */     
/*     */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*     */     {
/* 632 */       super.readStructureFromNBT(tagCompound);
/* 633 */       this.hasWitch = tagCompound.getBoolean("Witch");
/*     */     }
/*     */     
/*     */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*     */     {
/* 638 */       if (!func_74935_a(worldIn, structureBoundingBoxIn, 0))
/*     */       {
/* 640 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 644 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 5, 1, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
/* 645 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 5, 4, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
/* 646 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 4, 1, 0, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
/* 647 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 2, 3, 3, 2, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
/* 648 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 3, 1, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
/* 649 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 3, 5, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
/* 650 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 7, 4, 3, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
/* 651 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 652 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 653 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 654 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 7, 5, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
/* 655 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 3, 2, structureBoundingBoxIn);
/* 656 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 3, 3, 7, structureBoundingBoxIn);
/* 657 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 3, 4, structureBoundingBoxIn);
/* 658 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
/* 659 */       setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
/* 660 */       setBlockState(worldIn, Blocks.flower_pot.getDefaultState().withProperty(net.minecraft.block.BlockFlowerPot.CONTENTS, net.minecraft.block.BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, structureBoundingBoxIn);
/* 661 */       setBlockState(worldIn, Blocks.crafting_table.getDefaultState(), 3, 2, 6, structureBoundingBoxIn);
/* 662 */       setBlockState(worldIn, Blocks.cauldron.getDefaultState(), 4, 2, 6, structureBoundingBoxIn);
/* 663 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 2, 1, structureBoundingBoxIn);
/* 664 */       setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 5, 2, 1, structureBoundingBoxIn);
/* 665 */       int i = getMetadataWithOffset(Blocks.oak_stairs, 3);
/* 666 */       int j = getMetadataWithOffset(Blocks.oak_stairs, 1);
/* 667 */       int k = getMetadataWithOffset(Blocks.oak_stairs, 0);
/* 668 */       int l = getMetadataWithOffset(Blocks.oak_stairs, 2);
/* 669 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 6, 4, 1, Blocks.spruce_stairs.getStateFromMeta(i), Blocks.spruce_stairs.getStateFromMeta(i), false);
/* 670 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 2, 0, 4, 7, Blocks.spruce_stairs.getStateFromMeta(k), Blocks.spruce_stairs.getStateFromMeta(k), false);
/* 671 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 6, 4, 7, Blocks.spruce_stairs.getStateFromMeta(j), Blocks.spruce_stairs.getStateFromMeta(j), false);
/* 672 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 8, 6, 4, 8, Blocks.spruce_stairs.getStateFromMeta(l), Blocks.spruce_stairs.getStateFromMeta(l), false);
/*     */       
/* 674 */       for (int i1 = 2; i1 <= 7; i1 += 5)
/*     */       {
/* 676 */         for (int j1 = 1; j1 <= 5; j1 += 4)
/*     */         {
/* 678 */           replaceAirAndLiquidDownwards(worldIn, Blocks.log.getDefaultState(), j1, -1, i1, structureBoundingBoxIn);
/*     */         }
/*     */       }
/*     */       
/* 682 */       if (!this.hasWitch)
/*     */       {
/* 684 */         int l1 = getXWithOffset(2, 5);
/* 685 */         int i2 = getYWithOffset(2);
/* 686 */         int k1 = getZWithOffset(2, 5);
/*     */         
/* 688 */         if (structureBoundingBoxIn.isVecInside(new net.minecraft.util.BlockPos(l1, i2, k1)))
/*     */         {
/* 690 */           this.hasWitch = true;
/* 691 */           EntityWitch entitywitch = new EntityWitch(worldIn);
/* 692 */           entitywitch.setLocationAndAngles(l1 + 0.5D, i2, k1 + 0.5D, 0.0F, 0.0F);
/* 693 */           entitywitch.onInitialSpawn(worldIn.getDifficultyForLocation(new net.minecraft.util.BlockPos(l1, i2, k1)), null);
/* 694 */           worldIn.spawnEntityInWorld(entitywitch);
/*     */         }
/*     */       }
/*     */       
/* 698 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\ComponentScatteredFeaturePieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */