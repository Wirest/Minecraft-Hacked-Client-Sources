package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class StructureMineshaftPieces {
    private static final List field_175893_a = Lists.newArrayList(new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.dye, EnumDyeColor.BLUE.getDyeColorDamage(), 4, 9, 5), new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1), new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1)});
    private static final String __OBFID = "CL_00000444";

    public static void registerStructurePieces() {
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Corridor.class, "MSCorridor");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Cross.class, "MSCrossing");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Room.class, "MSRoom");
        MapGenStructureIO.registerStructureComponent(StructureMineshaftPieces.Stairs.class, "MSStairs");
    }

    private static StructureComponent func_175892_a(List p_175892_0_, Random p_175892_1_, int p_175892_2_, int p_175892_3_, int p_175892_4_, EnumFacing p_175892_5_, int p_175892_6_) {
        int var7 = p_175892_1_.nextInt(100);
        StructureBoundingBox var8;

        if (var7 >= 80) {
            var8 = StructureMineshaftPieces.Cross.func_175813_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);

            if (var8 != null) {
                return new StructureMineshaftPieces.Cross(p_175892_6_, p_175892_1_, var8, p_175892_5_);
            }
        } else if (var7 >= 70) {
            var8 = StructureMineshaftPieces.Stairs.func_175812_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);

            if (var8 != null) {
                return new StructureMineshaftPieces.Stairs(p_175892_6_, p_175892_1_, var8, p_175892_5_);
            }
        } else {
            var8 = StructureMineshaftPieces.Corridor.func_175814_a(p_175892_0_, p_175892_1_, p_175892_2_, p_175892_3_, p_175892_4_, p_175892_5_);

            if (var8 != null) {
                return new StructureMineshaftPieces.Corridor(p_175892_6_, p_175892_1_, var8, p_175892_5_);
            }
        }

        return null;
    }

    private static StructureComponent func_175890_b(StructureComponent p_175890_0_, List p_175890_1_, Random p_175890_2_, int p_175890_3_, int p_175890_4_, int p_175890_5_, EnumFacing p_175890_6_, int p_175890_7_) {
        if (p_175890_7_ > 8) {
            return null;
        } else if (Math.abs(p_175890_3_ - p_175890_0_.getBoundingBox().minX) <= 80 && Math.abs(p_175890_5_ - p_175890_0_.getBoundingBox().minZ) <= 80) {
            StructureComponent var8 = func_175892_a(p_175890_1_, p_175890_2_, p_175890_3_, p_175890_4_, p_175890_5_, p_175890_6_, p_175890_7_ + 1);

            if (var8 != null) {
                p_175890_1_.add(var8);
                var8.buildComponent(p_175890_0_, p_175890_1_, p_175890_2_);
            }

            return var8;
        } else {
            return null;
        }
    }

    public static class Corridor extends StructureComponent {
        private boolean hasRails;
        private boolean hasSpiders;
        private boolean spawnerPlaced;
        private int sectionCount;
        private static final String __OBFID = "CL_00000445";

        public Corridor() {
        }

        protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
            p_143012_1_.setBoolean("hr", this.hasRails);
            p_143012_1_.setBoolean("sc", this.hasSpiders);
            p_143012_1_.setBoolean("hps", this.spawnerPlaced);
            p_143012_1_.setInteger("Num", this.sectionCount);
        }

        protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {
            this.hasRails = p_143011_1_.getBoolean("hr");
            this.hasSpiders = p_143011_1_.getBoolean("sc");
            this.spawnerPlaced = p_143011_1_.getBoolean("hps");
            this.sectionCount = p_143011_1_.getInteger("Num");
        }

        public Corridor(int p_i45625_1_, Random p_i45625_2_, StructureBoundingBox p_i45625_3_, EnumFacing p_i45625_4_) {
            super(p_i45625_1_);
            this.coordBaseMode = p_i45625_4_;
            this.boundingBox = p_i45625_3_;
            this.hasRails = p_i45625_2_.nextInt(3) == 0;
            this.hasSpiders = !this.hasRails && p_i45625_2_.nextInt(23) == 0;

            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                this.sectionCount = p_i45625_3_.getXSize() / 5;
            } else {
                this.sectionCount = p_i45625_3_.getZSize() / 5;
            }
        }

        public static StructureBoundingBox func_175814_a(List p_175814_0_, Random p_175814_1_, int p_175814_2_, int p_175814_3_, int p_175814_4_, EnumFacing p_175814_5_) {
            StructureBoundingBox var6 = new StructureBoundingBox(p_175814_2_, p_175814_3_, p_175814_4_, p_175814_2_, p_175814_3_ + 2, p_175814_4_);
            int var7;

            for (var7 = p_175814_1_.nextInt(3) + 2; var7 > 0; --var7) {
                int var8 = var7 * 5;

                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175814_5_.ordinal()]) {
                    case 1:
                        var6.maxX = p_175814_2_ + 2;
                        var6.minZ = p_175814_4_ - (var8 - 1);
                        break;

                    case 2:
                        var6.maxX = p_175814_2_ + 2;
                        var6.maxZ = p_175814_4_ + (var8 - 1);
                        break;

                    case 3:
                        var6.minX = p_175814_2_ - (var8 - 1);
                        var6.maxZ = p_175814_4_ + 2;
                        break;

                    case 4:
                        var6.maxX = p_175814_2_ + (var8 - 1);
                        var6.maxZ = p_175814_4_ + 2;
                }

                if (StructureComponent.findIntersecting(p_175814_0_, var6) == null) {
                    break;
                }
            }

            return var7 > 0 ? var6 : null;
        }

        public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
            int var4 = this.getComponentType();
            int var5 = p_74861_3_.nextInt(4);

            if (this.coordBaseMode != null) {
                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.coordBaseMode.ordinal()]) {
                    case 1:
                        if (var5 <= 1) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, this.coordBaseMode, var4);
                        } else if (var5 == 2) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.WEST, var4);
                        } else {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, EnumFacing.EAST, var4);
                        }

                        break;

                    case 2:
                        if (var5 <= 1) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, this.coordBaseMode, var4);
                        } else if (var5 == 2) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.WEST, var4);
                        } else {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ - 3, EnumFacing.EAST, var4);
                        }

                        break;

                    case 3:
                        if (var5 <= 1) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, var4);
                        } else if (var5 == 2) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                        } else {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                        }

                        break;

                    case 4:
                        if (var5 <= 1) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ, this.coordBaseMode, var4);
                        } else if (var5 == 2) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                        } else {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + p_74861_3_.nextInt(3), this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                        }
                }
            }

            if (var4 < 8) {
                int var6;
                int var7;

                if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                    for (var6 = this.boundingBox.minX + 3; var6 + 3 <= this.boundingBox.maxX; var6 += 5) {
                        var7 = p_74861_3_.nextInt(5);

                        if (var7 == 0) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, var6, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4 + 1);
                        } else if (var7 == 1) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, var6, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4 + 1);
                        }
                    }
                } else {
                    for (var6 = this.boundingBox.minZ + 3; var6 + 3 <= this.boundingBox.maxZ; var6 += 5) {
                        var7 = p_74861_3_.nextInt(5);

                        if (var7 == 0) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, var6, EnumFacing.WEST, var4 + 1);
                        } else if (var7 == 1) {
                            StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, var6, EnumFacing.EAST, var4 + 1);
                        }
                    }
                }
            }
        }

        protected boolean func_180778_a(World worldIn, StructureBoundingBox p_180778_2_, Random p_180778_3_, int p_180778_4_, int p_180778_5_, int p_180778_6_, List p_180778_7_, int p_180778_8_) {
            BlockPos var9 = new BlockPos(this.getXWithOffset(p_180778_4_, p_180778_6_), this.getYWithOffset(p_180778_5_), this.getZWithOffset(p_180778_4_, p_180778_6_));

            if (p_180778_2_.func_175898_b(var9) && worldIn.getBlockState(var9).getBlock().getMaterial() == Material.air) {
                int var10 = p_180778_3_.nextBoolean() ? 1 : 0;
                worldIn.setBlockState(var9, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, var10)), 2);
                EntityMinecartChest var11 = new EntityMinecartChest(worldIn, (double) ((float) var9.getX() + 0.5F), (double) ((float) var9.getY() + 0.5F), (double) ((float) var9.getZ() + 0.5F));
                WeightedRandomChestContent.generateChestContents(p_180778_3_, p_180778_7_, var11, p_180778_8_);
                worldIn.spawnEntityInWorld(var11);
                return true;
            } else {
                return false;
            }
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            } else {
                boolean var4 = false;
                boolean var5 = true;
                boolean var6 = false;
                boolean var7 = true;
                int var8 = this.sectionCount * 5 - 1;
                this.func_175804_a(worldIn, p_74875_3_, 0, 0, 0, 2, 1, var8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175805_a(worldIn, p_74875_3_, p_74875_2_, 0.8F, 0, 2, 0, 2, 2, var8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);

                if (this.hasSpiders) {
                    this.func_175805_a(worldIn, p_74875_3_, p_74875_2_, 0.6F, 0, 0, 0, 2, 1, var8, Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
                }

                int var9;
                int var10;

                for (var9 = 0; var9 < this.sectionCount; ++var9) {
                    var10 = 2 + var9 * 5;
                    this.func_175804_a(worldIn, p_74875_3_, 0, 0, var10, 0, 1, var10, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, 2, 0, var10, 2, 1, var10, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);

                    if (p_74875_2_.nextInt(4) == 0) {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 2, var10, 0, 2, var10, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                        this.func_175804_a(worldIn, p_74875_3_, 2, 2, var10, 2, 2, var10, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    } else {
                        this.func_175804_a(worldIn, p_74875_3_, 0, 2, var10, 2, 2, var10, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                    }

                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 0, 2, var10 - 1, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 2, 2, var10 - 1, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 0, 2, var10 + 1, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.1F, 2, 2, var10 + 1, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 0, 2, var10 - 2, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 2, 2, var10 - 2, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 0, 2, var10 + 2, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 2, 2, var10 + 2, Blocks.web.getDefaultState());
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 1, 2, var10 - 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
                    this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.05F, 1, 2, var10 + 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));

                    if (p_74875_2_.nextInt(100) == 0) {
                        this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 2, 0, var10 - 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.field_175893_a, new WeightedRandomChestContent[]{Items.enchanted_book.getRandomEnchantedBook(p_74875_2_)}), 3 + p_74875_2_.nextInt(4));
                    }

                    if (p_74875_2_.nextInt(100) == 0) {
                        this.func_180778_a(worldIn, p_74875_3_, p_74875_2_, 0, 0, var10 + 1, WeightedRandomChestContent.func_177629_a(StructureMineshaftPieces.field_175893_a, new WeightedRandomChestContent[]{Items.enchanted_book.getRandomEnchantedBook(p_74875_2_)}), 3 + p_74875_2_.nextInt(4));
                    }

                    if (this.hasSpiders && !this.spawnerPlaced) {
                        int var11 = this.getYWithOffset(0);
                        int var12 = var10 - 1 + p_74875_2_.nextInt(3);
                        int var13 = this.getXWithOffset(1, var12);
                        var12 = this.getZWithOffset(1, var12);
                        BlockPos var14 = new BlockPos(var13, var11, var12);

                        if (p_74875_3_.func_175898_b(var14)) {
                            this.spawnerPlaced = true;
                            worldIn.setBlockState(var14, Blocks.mob_spawner.getDefaultState(), 2);
                            TileEntity var15 = worldIn.getTileEntity(var14);

                            if (var15 instanceof TileEntityMobSpawner) {
                                ((TileEntityMobSpawner) var15).getSpawnerBaseLogic().setEntityName("CaveSpider");
                            }
                        }
                    }
                }

                for (var9 = 0; var9 <= 2; ++var9) {
                    for (var10 = 0; var10 <= var8; ++var10) {
                        byte var17 = -1;
                        IBlockState var18 = this.func_175807_a(worldIn, var9, var17, var10, p_74875_3_);

                        if (var18.getBlock().getMaterial() == Material.air) {
                            byte var19 = -1;
                            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), var9, var19, var10, p_74875_3_);
                        }
                    }
                }

                if (this.hasRails) {
                    for (var9 = 0; var9 <= var8; ++var9) {
                        IBlockState var16 = this.func_175807_a(worldIn, 1, -1, var9, p_74875_3_);

                        if (var16.getBlock().getMaterial() != Material.air && var16.getBlock().isFullBlock()) {
                            this.func_175809_a(worldIn, p_74875_3_, p_74875_2_, 0.7F, 1, 0, var9, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, 0)));
                        }
                    }
                }

                return true;
            }
        }
    }

    public static class Cross extends StructureComponent {
        private EnumFacing corridorDirection;
        private boolean isMultipleFloors;
        private static final String __OBFID = "CL_00000446";

        public Cross() {
        }

        protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
            p_143012_1_.setBoolean("tf", this.isMultipleFloors);
            p_143012_1_.setInteger("D", this.corridorDirection.getHorizontalIndex());
        }

        protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {
            this.isMultipleFloors = p_143011_1_.getBoolean("tf");
            this.corridorDirection = EnumFacing.getHorizontal(p_143011_1_.getInteger("D"));
        }

        public Cross(int p_i45624_1_, Random p_i45624_2_, StructureBoundingBox p_i45624_3_, EnumFacing p_i45624_4_) {
            super(p_i45624_1_);
            this.corridorDirection = p_i45624_4_;
            this.boundingBox = p_i45624_3_;
            this.isMultipleFloors = p_i45624_3_.getYSize() > 3;
        }

        public static StructureBoundingBox func_175813_a(List p_175813_0_, Random p_175813_1_, int p_175813_2_, int p_175813_3_, int p_175813_4_, EnumFacing p_175813_5_) {
            StructureBoundingBox var6 = new StructureBoundingBox(p_175813_2_, p_175813_3_, p_175813_4_, p_175813_2_, p_175813_3_ + 2, p_175813_4_);

            if (p_175813_1_.nextInt(4) == 0) {
                var6.maxY += 4;
            }

            switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175813_5_.ordinal()]) {
                case 1:
                    var6.minX = p_175813_2_ - 1;
                    var6.maxX = p_175813_2_ + 3;
                    var6.minZ = p_175813_4_ - 4;
                    break;

                case 2:
                    var6.minX = p_175813_2_ - 1;
                    var6.maxX = p_175813_2_ + 3;
                    var6.maxZ = p_175813_4_ + 4;
                    break;

                case 3:
                    var6.minX = p_175813_2_ - 4;
                    var6.minZ = p_175813_4_ - 1;
                    var6.maxZ = p_175813_4_ + 3;
                    break;

                case 4:
                    var6.maxX = p_175813_2_ + 4;
                    var6.minZ = p_175813_4_ - 1;
                    var6.maxZ = p_175813_4_ + 3;
            }

            return StructureComponent.findIntersecting(p_175813_0_, var6) != null ? null : var6;
        }

        public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
            int var4 = this.getComponentType();

            switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.corridorDirection.ordinal()]) {
                case 1:
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
                    break;

                case 2:
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
                    break;

                case 3:
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                    break;

                case 4:
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
            }

            if (this.isMultipleFloors) {
                if (p_74861_3_.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                }

                if (p_74861_3_.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.WEST, var4);
                }

                if (p_74861_3_.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, EnumFacing.EAST, var4);
                }

                if (p_74861_3_.nextBoolean()) {
                    StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                }
            }
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            } else {
                if (this.isMultipleFloors) {
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                } else {
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                    this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                }

                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.minX + 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.minZ + 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ - 1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);

                for (int var4 = this.boundingBox.minX; var4 <= this.boundingBox.maxX; ++var4) {
                    for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5) {
                        if (this.func_175807_a(worldIn, var4, this.boundingBox.minY - 1, var5, p_74875_3_).getBlock().getMaterial() == Material.air) {
                            this.func_175811_a(worldIn, Blocks.planks.getDefaultState(), var4, this.boundingBox.minY - 1, var5, p_74875_3_);
                        }
                    }
                }

                return true;
            }
        }
    }

    public static class Room extends StructureComponent {
        private List roomsLinkedToTheRoom = Lists.newLinkedList();
        private static final String __OBFID = "CL_00000447";

        public Room() {
        }

        public Room(int p_i2037_1_, Random p_i2037_2_, int p_i2037_3_, int p_i2037_4_) {
            super(p_i2037_1_);
            this.boundingBox = new StructureBoundingBox(p_i2037_3_, 50, p_i2037_4_, p_i2037_3_ + 7 + p_i2037_2_.nextInt(6), 54 + p_i2037_2_.nextInt(6), p_i2037_4_ + 7 + p_i2037_2_.nextInt(6));
        }

        public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
            int var4 = this.getComponentType();
            int var6 = this.boundingBox.getYSize() - 3 - 1;

            if (var6 <= 0) {
                var6 = 1;
            }

            int var5;
            StructureComponent var7;
            StructureBoundingBox var8;

            for (var5 = 0; var5 < this.boundingBox.getXSize(); var5 += 4) {
                var5 += p_74861_3_.nextInt(this.boundingBox.getXSize());

                if (var5 + 3 > this.boundingBox.getXSize()) {
                    break;
                }

                var7 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + var5, this.boundingBox.minY + p_74861_3_.nextInt(var6) + 1, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);

                if (var7 != null) {
                    var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(var8.minX, var8.minY, this.boundingBox.minZ, var8.maxX, var8.maxY, this.boundingBox.minZ + 1));
                }
            }

            for (var5 = 0; var5 < this.boundingBox.getXSize(); var5 += 4) {
                var5 += p_74861_3_.nextInt(this.boundingBox.getXSize());

                if (var5 + 3 > this.boundingBox.getXSize()) {
                    break;
                }

                var7 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX + var5, this.boundingBox.minY + p_74861_3_.nextInt(var6) + 1, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);

                if (var7 != null) {
                    var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(var8.minX, var8.minY, this.boundingBox.maxZ - 1, var8.maxX, var8.maxY, this.boundingBox.maxZ));
                }
            }

            for (var5 = 0; var5 < this.boundingBox.getZSize(); var5 += 4) {
                var5 += p_74861_3_.nextInt(this.boundingBox.getZSize());

                if (var5 + 3 > this.boundingBox.getZSize()) {
                    break;
                }

                var7 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74861_3_.nextInt(var6) + 1, this.boundingBox.minZ + var5, EnumFacing.WEST, var4);

                if (var7 != null) {
                    var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.minX, var8.minY, var8.minZ, this.boundingBox.minX + 1, var8.maxY, var8.maxZ));
                }
            }

            for (var5 = 0; var5 < this.boundingBox.getZSize(); var5 += 4) {
                var5 += p_74861_3_.nextInt(this.boundingBox.getZSize());

                if (var5 + 3 > this.boundingBox.getZSize()) {
                    break;
                }

                var7 = StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74861_3_.nextInt(var6) + 1, this.boundingBox.minZ + var5, EnumFacing.EAST, var4);

                if (var7 != null) {
                    var8 = var7.getBoundingBox();
                    this.roomsLinkedToTheRoom.add(new StructureBoundingBox(this.boundingBox.maxX - 1, var8.minY, var8.minZ, this.boundingBox.maxX, var8.maxY, var8.maxZ));
                }
            }
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            } else {
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
                this.func_175804_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                Iterator var4 = this.roomsLinkedToTheRoom.iterator();

                while (var4.hasNext()) {
                    StructureBoundingBox var5 = (StructureBoundingBox) var4.next();
                    this.func_175804_a(worldIn, p_74875_3_, var5.minX, var5.maxY - 2, var5.minZ, var5.maxX, var5.maxY, var5.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                }

                this.func_180777_a(worldIn, p_74875_3_, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
                return true;
            }
        }

        protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
            NBTTagList var2 = new NBTTagList();
            Iterator var3 = this.roomsLinkedToTheRoom.iterator();

            while (var3.hasNext()) {
                StructureBoundingBox var4 = (StructureBoundingBox) var3.next();
                var2.appendTag(var4.func_151535_h());
            }

            p_143012_1_.setTag("Entrances", var2);
        }

        protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {
            NBTTagList var2 = p_143011_1_.getTagList("Entrances", 11);

            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                this.roomsLinkedToTheRoom.add(new StructureBoundingBox(var2.getIntArray(var3)));
            }
        }
    }

    public static class Stairs extends StructureComponent {
        private static final String __OBFID = "CL_00000449";

        public Stairs() {
        }

        public Stairs(int p_i45623_1_, Random p_i45623_2_, StructureBoundingBox p_i45623_3_, EnumFacing p_i45623_4_) {
            super(p_i45623_1_);
            this.coordBaseMode = p_i45623_4_;
            this.boundingBox = p_i45623_3_;
        }

        protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
        }

        protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {
        }

        public static StructureBoundingBox func_175812_a(List p_175812_0_, Random p_175812_1_, int p_175812_2_, int p_175812_3_, int p_175812_4_, EnumFacing p_175812_5_) {
            StructureBoundingBox var6 = new StructureBoundingBox(p_175812_2_, p_175812_3_ - 5, p_175812_4_, p_175812_2_, p_175812_3_ + 2, p_175812_4_);

            switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[p_175812_5_.ordinal()]) {
                case 1:
                    var6.maxX = p_175812_2_ + 2;
                    var6.minZ = p_175812_4_ - 8;
                    break;

                case 2:
                    var6.maxX = p_175812_2_ + 2;
                    var6.maxZ = p_175812_4_ + 8;
                    break;

                case 3:
                    var6.minX = p_175812_2_ - 8;
                    var6.maxZ = p_175812_4_ + 2;
                    break;

                case 4:
                    var6.maxX = p_175812_2_ + 8;
                    var6.maxZ = p_175812_4_ + 2;
            }

            return StructureComponent.findIntersecting(p_175812_0_, var6) != null ? null : var6;
        }

        public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
            int var4 = this.getComponentType();

            if (this.coordBaseMode != null) {
                switch (StructureMineshaftPieces.SwitchEnumFacing.field_175894_a[this.coordBaseMode.ordinal()]) {
                    case 1:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, var4);
                        break;

                    case 2:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, var4);
                        break;

                    case 3:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, var4);
                        break;

                    case 4:
                        StructureMineshaftPieces.func_175890_b(p_74861_1_, p_74861_2_, p_74861_3_, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, var4);
                }
            }
        }

        public boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
            if (this.isLiquidInStructureBoundingBox(worldIn, p_74875_3_)) {
                return false;
            } else {
                this.func_175804_a(worldIn, p_74875_3_, 0, 5, 0, 2, 7, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.func_175804_a(worldIn, p_74875_3_, 0, 0, 7, 2, 2, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);

                for (int var4 = 0; var4 < 5; ++var4) {
                    this.func_175804_a(worldIn, p_74875_3_, 0, 5 - var4 - (var4 < 4 ? 1 : 0), 2 + var4, 2, 7 - var4, 2 + var4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
                }

                return true;
            }
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_175894_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00001998";

        static {
            try {
                field_175894_a[EnumFacing.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_175894_a[EnumFacing.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_175894_a[EnumFacing.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_175894_a[EnumFacing.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
