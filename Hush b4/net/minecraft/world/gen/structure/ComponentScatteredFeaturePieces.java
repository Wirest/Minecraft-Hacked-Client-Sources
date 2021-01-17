// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.Block;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockSandStone;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import java.util.List;

public class ComponentScatteredFeaturePieces
{
    public static void registerScatteredFeaturePieces() {
        MapGenStructureIO.registerStructureComponent(DesertPyramid.class, "TeDP");
        MapGenStructureIO.registerStructureComponent(JunglePyramid.class, "TeJP");
        MapGenStructureIO.registerStructureComponent(SwampHut.class, "TeSH");
    }
    
    public static class DesertPyramid extends Feature
    {
        private boolean[] field_74940_h;
        private static final List<WeightedRandomChestContent> itemsToGenerateInTemple;
        
        static {
            itemsToGenerateInTemple = Lists.newArrayList(new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1));
        }
        
        public DesertPyramid() {
            this.field_74940_h = new boolean[4];
        }
        
        public DesertPyramid(final Random p_i2062_1_, final int p_i2062_2_, final int p_i2062_3_) {
            super(p_i2062_1_, p_i2062_2_, 64, p_i2062_3_, 21, 15, 21);
            this.field_74940_h = new boolean[4];
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("hasPlacedChest0", this.field_74940_h[0]);
            tagCompound.setBoolean("hasPlacedChest1", this.field_74940_h[1]);
            tagCompound.setBoolean("hasPlacedChest2", this.field_74940_h[2]);
            tagCompound.setBoolean("hasPlacedChest3", this.field_74940_h[3]);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
            super.readStructureFromNBT(tagCompound);
            this.field_74940_h[0] = tagCompound.getBoolean("hasPlacedChest0");
            this.field_74940_h[1] = tagCompound.getBoolean("hasPlacedChest1");
            this.field_74940_h[2] = tagCompound.getBoolean("hasPlacedChest2");
            this.field_74940_h[3] = tagCompound.getBoolean("hasPlacedChest3");
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            for (int i = 1; i <= 9; ++i) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, i, i, this.scatteredFeatureSizeX - 1 - i, i, this.scatteredFeatureSizeZ - 1 - i, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, i + 1, i, i + 1, this.scatteredFeatureSizeX - 2 - i, i, this.scatteredFeatureSizeZ - 2 - i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            for (int j2 = 0; j2 < this.scatteredFeatureSizeX; ++j2) {
                for (int k = 0; k < this.scatteredFeatureSizeZ; ++k) {
                    final int l = -5;
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.sandstone.getDefaultState(), j2, l, k, structureBoundingBoxIn);
                }
            }
            final int k2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 3);
            final int l2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 2);
            final int i2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 0);
            final int m = this.getMetadataWithOffset(Blocks.sandstone_stairs, 1);
            final int i3 = ~EnumDyeColor.ORANGE.getDyeDamage() & 0xF;
            final int j3 = ~EnumDyeColor.BLUE.getDyeDamage() & 0xF;
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 10, 1, 3, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 10, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l2), 2, 10, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i2), 0, 10, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(m), 4, 10, 2, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 0, 0, this.scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 4, 10, 1, this.scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 10, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l2), this.scatteredFeatureSizeX - 3, 10, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i2), this.scatteredFeatureSizeX - 5, 10, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(m), this.scatteredFeatureSizeX - 1, 10, 2, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 12, 4, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 0, 11, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 8, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 2, 8, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 1, 16, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 2, 16, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, this.scatteredFeatureSizeX - 6, 4, this.scatteredFeatureSizeZ - 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 4, 9, 11, 4, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 8, 8, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 8, 12, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 12, 8, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 12, 12, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 4, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 1, 5, this.scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 7, 9, 6, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 7, 7, 9, this.scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 9, 5, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 6, 5, 9, this.scatteredFeatureSizeX - 6, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 5, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 6, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 6, 6, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 5, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 6, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 7, 6, 10, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 4, 2, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 3, 4, 4, this.scatteredFeatureSizeX - 3, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 4, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 3, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 4, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 3, 4, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 3, 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 3, 1, 3, this.scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getDefaultState(), 1, 1, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getDefaultState(), this.scatteredFeatureSizeX - 2, 1, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), this.scatteredFeatureSizeX - 2, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(m), 2, 1, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i2), this.scatteredFeatureSizeX - 3, 1, 2, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 5, 4, 3, 18, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 3, 5, this.scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 5, 4, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 6, 1, 5, this.scatteredFeatureSizeX - 5, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            for (int k3 = 5; k3 <= 17; k3 += 2) {
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, k3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, k3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), this.scatteredFeatureSizeX - 5, 1, k3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), this.scatteredFeatureSizeX - 5, 2, k3, structureBoundingBoxIn);
            }
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 10, 0, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 10, 0, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 9, 0, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 11, 0, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 8, 0, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 12, 0, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 7, 0, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 13, 0, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 9, 0, 11, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 11, 0, 11, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 10, 0, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 10, 0, 13, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(j3), 10, 0, 10, structureBoundingBoxIn);
            for (int j4 = 0; j4 <= this.scatteredFeatureSizeX - 1; j4 += this.scatteredFeatureSizeX - 1) {
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 2, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 2, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 2, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 3, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 3, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 3, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 4, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j4, 4, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 4, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 5, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 5, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 5, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 6, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j4, 6, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 6, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 7, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 7, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), j4, 7, 3, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 8, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 8, 2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j4, 8, 3, structureBoundingBoxIn);
            }
            for (int k4 = 2; k4 <= this.scatteredFeatureSizeX - 3; k4 += this.scatteredFeatureSizeX - 3 - 2) {
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 - 1, 2, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4, 2, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 + 1, 2, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 - 1, 3, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4, 3, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 + 1, 3, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4 - 1, 4, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k4, 4, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4 + 1, 4, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 - 1, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 + 1, 5, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4 - 1, 6, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k4, 6, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4 + 1, 6, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4 - 1, 7, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4, 7, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), k4 + 1, 7, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 - 1, 8, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4, 8, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k4 + 1, 8, 0, structureBoundingBoxIn);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 0, 12, 6, 0, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 8, 6, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 12, 6, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 9, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i3), 11, 5, 0, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -14, 8, 12, -11, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -10, 8, 12, -10, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -9, 8, 12, -9, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -8, 8, 12, -1, 12, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -11, 9, 11, -1, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.stone_pressure_plate.getDefaultState(), 10, -11, 10, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -13, 9, 11, -13, 11, Blocks.tnt.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 8, -11, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 8, -10, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 12, -11, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 12, -10, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -11, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -10, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -11, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -10, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, structureBoundingBoxIn);
            for (final Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
                final EnumFacing enumfacing2 = (EnumFacing)enumfacing0;
                if (!this.field_74940_h[enumfacing2.getHorizontalIndex()]) {
                    final int l3 = enumfacing2.getFrontOffsetX() * 2;
                    final int i4 = enumfacing2.getFrontOffsetZ() * 2;
                    this.field_74940_h[enumfacing2.getHorizontalIndex()] = this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 10 + l3, -11, 10 + i4, WeightedRandomChestContent.func_177629_a(DesertPyramid.itemsToGenerateInTemple, Items.enchanted_book.getRandom(randomIn)), 2 + randomIn.nextInt(5));
                }
            }
            return true;
        }
    }
    
    abstract static class Feature extends StructureComponent
    {
        protected int scatteredFeatureSizeX;
        protected int scatteredFeatureSizeY;
        protected int scatteredFeatureSizeZ;
        protected int field_74936_d;
        
        public Feature() {
            this.field_74936_d = -1;
        }
        
        protected Feature(final Random p_i2065_1_, final int p_i2065_2_, final int p_i2065_3_, final int p_i2065_4_, final int p_i2065_5_, final int p_i2065_6_, final int p_i2065_7_) {
            super(0);
            this.field_74936_d = -1;
            this.scatteredFeatureSizeX = p_i2065_5_;
            this.scatteredFeatureSizeY = p_i2065_6_;
            this.scatteredFeatureSizeZ = p_i2065_7_;
            this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2065_1_);
            switch (this.coordBaseMode) {
                case NORTH:
                case SOUTH: {
                    this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_5_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_7_ - 1);
                    break;
                }
                default: {
                    this.boundingBox = new StructureBoundingBox(p_i2065_2_, p_i2065_3_, p_i2065_4_, p_i2065_2_ + p_i2065_7_ - 1, p_i2065_3_ + p_i2065_6_ - 1, p_i2065_4_ + p_i2065_5_ - 1);
                    break;
                }
            }
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            tagCompound.setInteger("Width", this.scatteredFeatureSizeX);
            tagCompound.setInteger("Height", this.scatteredFeatureSizeY);
            tagCompound.setInteger("Depth", this.scatteredFeatureSizeZ);
            tagCompound.setInteger("HPos", this.field_74936_d);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
            this.scatteredFeatureSizeX = tagCompound.getInteger("Width");
            this.scatteredFeatureSizeY = tagCompound.getInteger("Height");
            this.scatteredFeatureSizeZ = tagCompound.getInteger("Depth");
            this.field_74936_d = tagCompound.getInteger("HPos");
        }
        
        protected boolean func_74935_a(final World worldIn, final StructureBoundingBox p_74935_2_, final int p_74935_3_) {
            if (this.field_74936_d >= 0) {
                return true;
            }
            int i = 0;
            int j = 0;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                    blockpos$mutableblockpos.func_181079_c(l, 64, k);
                    if (p_74935_2_.isVecInside(blockpos$mutableblockpos)) {
                        i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel());
                        ++j;
                    }
                }
            }
            if (j == 0) {
                return false;
            }
            this.field_74936_d = i / j;
            this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + p_74935_3_, 0);
            return true;
        }
    }
    
    public static class JunglePyramid extends Feature
    {
        private boolean field_74947_h;
        private boolean field_74948_i;
        private boolean field_74945_j;
        private boolean field_74946_k;
        private static final List<WeightedRandomChestContent> field_175816_i;
        private static final List<WeightedRandomChestContent> field_175815_j;
        private static Stones junglePyramidsRandomScatteredStones;
        
        static {
            field_175816_i = Lists.newArrayList(new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1));
            field_175815_j = Lists.newArrayList(new WeightedRandomChestContent(Items.arrow, 0, 2, 7, 30));
            JunglePyramid.junglePyramidsRandomScatteredStones = new Stones(null);
        }
        
        public JunglePyramid() {
        }
        
        public JunglePyramid(final Random p_i2064_1_, final int p_i2064_2_, final int p_i2064_3_) {
            super(p_i2064_1_, p_i2064_2_, 64, p_i2064_3_, 12, 10, 15);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("placedMainChest", this.field_74947_h);
            tagCompound.setBoolean("placedHiddenChest", this.field_74948_i);
            tagCompound.setBoolean("placedTrap1", this.field_74945_j);
            tagCompound.setBoolean("placedTrap2", this.field_74946_k);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
            super.readStructureFromNBT(tagCompound);
            this.field_74947_h = tagCompound.getBoolean("placedMainChest");
            this.field_74948_i = tagCompound.getBoolean("placedHiddenChest");
            this.field_74945_j = tagCompound.getBoolean("placedTrap1");
            this.field_74946_k = tagCompound.getBoolean("placedTrap2");
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (!this.func_74935_a(worldIn, structureBoundingBoxIn, 0)) {
                return false;
            }
            final int i = this.getMetadataWithOffset(Blocks.stone_stairs, 3);
            final int j = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
            final int k = this.getMetadataWithOffset(Blocks.stone_stairs, 0);
            final int l = this.getMetadataWithOffset(Blocks.stone_stairs, 1);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 9, 2, 2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 12, 9, 2, 12, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 2, 11, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 1, 3, 9, 2, 11, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 10, 6, 1, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 13, 10, 6, 13, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 2, 1, 6, 12, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, 3, 2, 10, 6, 12, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 3, 2, 9, 3, 12, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 6, 2, 9, 6, 12, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 3, 7, 3, 8, 7, 11, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 8, 4, 7, 8, 10, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 3, 8, 2, 11);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 4, 3, 6, 7, 3, 9);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 4, 2, 9, 5, 12);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 4, 6, 5, 7, 6, 9);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 7, 6, 6, 7, 8);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 1, 2, 6, 2, 2);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 2, 12, 6, 2, 12);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 1, 6, 5, 1);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 13, 6, 5, 13);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 5, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, 5, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 5, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, 5, 9, structureBoundingBoxIn);
            for (int i2 = 0; i2 <= 14; i2 += 14) {
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 4, i2, 2, 5, i2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 4, i2, 4, 5, i2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 4, i2, 7, 5, i2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 4, i2, 9, 5, i2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            }
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 6, 0, 6, 6, 0, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            for (int k2 = 0; k2 <= 11; k2 += 11) {
                for (int j2 = 2; j2 <= 12; j2 += 2) {
                    this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k2, 4, j2, k2, 5, j2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
                }
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k2, 6, 5, k2, 6, 5, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k2, 6, 9, k2, 6, 9, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            }
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 2, 2, 9, 2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 2, 9, 9, 2, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 12, 2, 9, 12, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 12, 9, 9, 12, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 4, 4, 9, 4, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 4, 7, 9, 4, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 10, 4, 9, 10, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 10, 7, 9, 10, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 9, 7, 6, 9, 7, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 9, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 9, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 9, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 9, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 0, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 0, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 0, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 0, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 1, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 2, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 3, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 1, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 2, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 3, 10, structureBoundingBoxIn);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 4, 1, 9, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 1, 9, 7, 1, 9, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 10, 7, 2, 10, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, 6, 4, 5, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(k), 4, 4, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(l), 7, 4, 5, structureBoundingBoxIn);
            for (int l2 = 0; l2 < 4; ++l2) {
                this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 0 - l2, 6 + l2, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 0 - l2, 6 + l2, structureBoundingBoxIn);
                this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 0 - l2, 7 + l2, 6, 0 - l2, 9 + l2);
            }
            this.fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 12, 10, -1, 13);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 3, -1, 13);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 9, -1, 5);
            for (int i3 = 1; i3 <= 13; i3 += 2) {
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -3, i3, 1, -2, i3, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            }
            for (int j3 = 2; j3 <= 12; j3 += 2) {
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -1, j3, 3, -1, j3, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            }
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, -2, 1, 5, -2, 1, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, -2, 1, 9, -2, 1, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -3, 1, 6, -3, 1, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -1, 1, 6, -1, 1, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.EAST.getHorizontalIndex())).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, true), 1, -3, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.WEST.getHorizontalIndex())).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, true), 4, -3, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, true), 2, -3, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, true), 3, -3, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 4, -3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 3, -3, 1, structureBoundingBoxIn);
            if (!this.field_74945_j) {
                this.field_74945_j = this.generateDispenserContents(worldIn, structureBoundingBoxIn, randomIn, 3, -2, 1, EnumFacing.NORTH.getIndex(), JunglePyramid.field_175815_j, 2);
            }
            this.setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 3, -2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.NORTH.getHorizontalIndex())).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, true), 7, -3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset(Blocks.tripwire_hook, EnumFacing.SOUTH.getHorizontalIndex())).withProperty((IProperty<Comparable>)BlockTripWireHook.ATTACHED, true), 7, -3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, true), 7, -3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, true), 7, -3, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty<Comparable>)BlockTripWire.ATTACHED, true), 7, -3, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -3, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -2, 4, structureBoundingBoxIn);
            if (!this.field_74946_k) {
                this.field_74946_k = this.generateDispenserContents(worldIn, structureBoundingBoxIn, randomIn, 9, -2, 3, EnumFacing.WEST.getIndex(), JunglePyramid.field_175815_j, 2);
            }
            this.setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 8, -1, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 8, -2, 3, structureBoundingBoxIn);
            if (!this.field_74947_h) {
                this.field_74947_h = this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 8, -3, 3, WeightedRandomChestContent.func_177629_a(JunglePyramid.field_175816_i, Items.enchanted_book.getRandom(randomIn)), 2 + randomIn.nextInt(5));
            }
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 4, -3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 6, -3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -1, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 5, structureBoundingBoxIn);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, -1, 1, 9, -1, 5, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 8, -3, 8, 10, -1, 10);
            this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 8, -2, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 9, -2, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing(EnumFacing.getFront(this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 10, -2, 12, structureBoundingBoxIn);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, -3, 8, 8, -3, 10, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, -3, 8, 10, -3, 10, false, randomIn, JunglePyramid.junglePyramidsRandomScatteredStones);
            this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 10, -2, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 10, -1, 9, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(EnumFacing.UP.getIndex()), 9, -2, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -2, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset(Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -1, 8, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.unpowered_repeater.getStateFromMeta(this.getMetadataWithOffset(Blocks.unpowered_repeater, EnumFacing.NORTH.getHorizontalIndex())), 10, -2, 10, structureBoundingBoxIn);
            if (!this.field_74948_i) {
                this.field_74948_i = this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 9, -3, 10, WeightedRandomChestContent.func_177629_a(JunglePyramid.field_175816_i, Items.enchanted_book.getRandom(randomIn)), 2 + randomIn.nextInt(5));
            }
            return true;
        }
        
        static class Stones extends BlockSelector
        {
            private Stones() {
            }
            
            @Override
            public void selectBlocks(final Random rand, final int x, final int y, final int z, final boolean p_75062_5_) {
                if (rand.nextFloat() < 0.4f) {
                    this.blockstate = Blocks.cobblestone.getDefaultState();
                }
                else {
                    this.blockstate = Blocks.mossy_cobblestone.getDefaultState();
                }
            }
        }
    }
    
    public static class SwampHut extends Feature
    {
        private boolean hasWitch;
        
        public SwampHut() {
        }
        
        public SwampHut(final Random p_i2066_1_, final int p_i2066_2_, final int p_i2066_3_) {
            super(p_i2066_1_, p_i2066_2_, 64, p_i2066_3_, 7, 7, 9);
        }
        
        @Override
        protected void writeStructureToNBT(final NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setBoolean("Witch", this.hasWitch);
        }
        
        @Override
        protected void readStructureFromNBT(final NBTTagCompound tagCompound) {
            super.readStructureFromNBT(tagCompound);
            this.hasWitch = tagCompound.getBoolean("Witch");
        }
        
        @Override
        public boolean addComponentParts(final World worldIn, final Random randomIn, final StructureBoundingBox structureBoundingBoxIn) {
            if (!this.func_74935_a(worldIn, structureBoundingBoxIn, 0)) {
                return false;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 5, 1, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 5, 4, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 4, 1, 0, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 2, 3, 3, 2, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 3, 1, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 3, 5, 3, 6, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 7, 4, 3, 7, Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), Blocks.planks.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 3, 2, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 7, 5, 3, 7, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 2, 3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 3, 3, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 3, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.flower_pot.getDefaultState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.crafting_table.getDefaultState(), 3, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.cauldron.getDefaultState(), 4, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 5, 2, 1, structureBoundingBoxIn);
            final int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
            final int j = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
            final int k = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
            final int l = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 6, 4, 1, Blocks.spruce_stairs.getStateFromMeta(i), Blocks.spruce_stairs.getStateFromMeta(i), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 2, 0, 4, 7, Blocks.spruce_stairs.getStateFromMeta(k), Blocks.spruce_stairs.getStateFromMeta(k), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 6, 4, 7, Blocks.spruce_stairs.getStateFromMeta(j), Blocks.spruce_stairs.getStateFromMeta(j), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 8, 6, 4, 8, Blocks.spruce_stairs.getStateFromMeta(l), Blocks.spruce_stairs.getStateFromMeta(l), false);
            for (int i2 = 2; i2 <= 7; i2 += 5) {
                for (int j2 = 1; j2 <= 5; j2 += 4) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.log.getDefaultState(), j2, -1, i2, structureBoundingBoxIn);
                }
            }
            if (!this.hasWitch) {
                final int l2 = this.getXWithOffset(2, 5);
                final int i3 = this.getYWithOffset(2);
                final int k2 = this.getZWithOffset(2, 5);
                if (structureBoundingBoxIn.isVecInside(new BlockPos(l2, i3, k2))) {
                    this.hasWitch = true;
                    final EntityWitch entitywitch = new EntityWitch(worldIn);
                    entitywitch.setLocationAndAngles(l2 + 0.5, i3, k2 + 0.5, 0.0f, 0.0f);
                    entitywitch.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(l2, i3, k2)), null);
                    worldIn.spawnEntityInWorld(entitywitch);
                }
            }
            return true;
        }
    }
}
