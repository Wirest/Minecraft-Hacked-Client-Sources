package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree {
    private Random field_175949_k;
    private World field_175946_l;
    private BlockPos field_175947_m;
    int heightLimit;
    int height;
    double heightAttenuation;
    double field_175944_d;
    double field_175945_e;
    double leafDensity;
    int field_175943_g;
    int field_175950_h;

    /**
     * Sets the distance limit for how far away the generator will populate leaves from the base leaf node.
     */
    int leafDistanceLimit;
    List field_175948_j;
    private static final String __OBFID = "CL_00000400";

    public WorldGenBigTree(boolean p_i2008_1_) {
        super(p_i2008_1_);
        this.field_175947_m = BlockPos.ORIGIN;
        this.heightAttenuation = 0.618D;
        this.field_175944_d = 0.381D;
        this.field_175945_e = 1.0D;
        this.leafDensity = 1.0D;
        this.field_175943_g = 1;
        this.field_175950_h = 12;
        this.leafDistanceLimit = 4;
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList() {
        this.height = (int) ((double) this.heightLimit * this.heightAttenuation);

        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }

        int var1 = (int) (1.382D + Math.pow(this.leafDensity * (double) this.heightLimit / 13.0D, 2.0D));

        if (var1 < 1) {
            var1 = 1;
        }

        int var2 = this.field_175947_m.getY() + this.height;
        int var3 = this.heightLimit - this.leafDistanceLimit;
        this.field_175948_j = Lists.newArrayList();
        this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(this.field_175947_m.offsetUp(var3), var2));

        for (; var3 >= 0; --var3) {
            float var4 = this.layerSize(var3);

            if (var4 >= 0.0F) {
                for (int var5 = 0; var5 < var1; ++var5) {
                    double var6 = this.field_175945_e * (double) var4 * ((double) this.field_175949_k.nextFloat() + 0.328D);
                    double var8 = (double) (this.field_175949_k.nextFloat() * 2.0F) * Math.PI;
                    double var10 = var6 * Math.sin(var8) + 0.5D;
                    double var12 = var6 * Math.cos(var8) + 0.5D;
                    BlockPos var14 = this.field_175947_m.add(var10, (double) (var3 - 1), var12);
                    BlockPos var15 = var14.offsetUp(this.leafDistanceLimit);

                    if (this.func_175936_a(var14, var15) == -1) {
                        int var16 = this.field_175947_m.getX() - var14.getX();
                        int var17 = this.field_175947_m.getZ() - var14.getZ();
                        double var18 = (double) var14.getY() - Math.sqrt((double) (var16 * var16 + var17 * var17)) * this.field_175944_d;
                        int var20 = var18 > (double) var2 ? var2 : (int) var18;
                        BlockPos var21 = new BlockPos(this.field_175947_m.getX(), var20, this.field_175947_m.getZ());

                        if (this.func_175936_a(var21, var14) == -1) {
                            this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(var14, var21.getY()));
                        }
                    }
                }
            }
        }
    }

    void func_180712_a(BlockPos p_180712_1_, float p_180712_2_, Block p_180712_3_) {
        int var4 = (int) ((double) p_180712_2_ + 0.618D);

        for (int var5 = -var4; var5 <= var4; ++var5) {
            for (int var6 = -var4; var6 <= var4; ++var6) {
                if (Math.pow((double) Math.abs(var5) + 0.5D, 2.0D) + Math.pow((double) Math.abs(var6) + 0.5D, 2.0D) <= (double) (p_180712_2_ * p_180712_2_)) {
                    BlockPos var7 = p_180712_1_.add(var5, 0, var6);
                    Material var8 = this.field_175946_l.getBlockState(var7).getBlock().getMaterial();

                    if (var8 == Material.air || var8 == Material.leaves) {
                        this.func_175905_a(this.field_175946_l, var7, p_180712_3_, 0);
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    float layerSize(int p_76490_1_) {
        if ((float) p_76490_1_ < (float) this.heightLimit * 0.3F) {
            return -1.0F;
        } else {
            float var2 = (float) this.heightLimit / 2.0F;
            float var3 = var2 - (float) p_76490_1_;
            float var4 = MathHelper.sqrt_float(var2 * var2 - var3 * var3);

            if (var3 == 0.0F) {
                var4 = var2;
            } else if (Math.abs(var3) >= var2) {
                return 0.0F;
            }

            return var4 * 0.5F;
        }
    }

    float leafSize(int p_76495_1_) {
        return p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit ? (p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    void func_175940_a(BlockPos p_175940_1_) {
        for (int var2 = 0; var2 < this.leafDistanceLimit; ++var2) {
            this.func_180712_a(p_175940_1_.offsetUp(var2), this.leafSize(var2), Blocks.leaves);
        }
    }

    void func_175937_a(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_) {
        BlockPos var4 = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int var5 = this.func_175935_b(var4);
        float var6 = (float) var4.getX() / (float) var5;
        float var7 = (float) var4.getY() / (float) var5;
        float var8 = (float) var4.getZ() / (float) var5;

        for (int var9 = 0; var9 <= var5; ++var9) {
            BlockPos var10 = p_175937_1_.add((double) (0.5F + (float) var9 * var6), (double) (0.5F + (float) var9 * var7), (double) (0.5F + (float) var9 * var8));
            BlockLog.EnumAxis var11 = this.func_175938_b(p_175937_1_, var10);
            this.func_175903_a(this.field_175946_l, var10, p_175937_3_.getDefaultState().withProperty(BlockLog.AXIS_PROP, var11));
        }
    }

    private int func_175935_b(BlockPos p_175935_1_) {
        int var2 = MathHelper.abs_int(p_175935_1_.getX());
        int var3 = MathHelper.abs_int(p_175935_1_.getY());
        int var4 = MathHelper.abs_int(p_175935_1_.getZ());
        return var4 > var2 && var4 > var3 ? var4 : (var3 > var2 ? var3 : var2);
    }

    private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_) {
        BlockLog.EnumAxis var3 = BlockLog.EnumAxis.Y;
        int var4 = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int var5 = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
        int var6 = Math.max(var4, var5);

        if (var6 > 0) {
            if (var4 == var6) {
                var3 = BlockLog.EnumAxis.X;
            } else if (var5 == var6) {
                var3 = BlockLog.EnumAxis.Z;
            }
        }

        return var3;
    }

    void func_175941_b() {
        Iterator var1 = this.field_175948_j.iterator();

        while (var1.hasNext()) {
            WorldGenBigTree.FoliageCoordinates var2 = (WorldGenBigTree.FoliageCoordinates) var1.next();
            this.func_175940_a(var2);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    boolean leafNodeNeedsBase(int p_76493_1_) {
        return (double) p_76493_1_ >= (double) this.heightLimit * 0.2D;
    }

    void func_175942_c() {
        BlockPos var1 = this.field_175947_m;
        BlockPos var2 = this.field_175947_m.offsetUp(this.height);
        Block var3 = Blocks.log;
        this.func_175937_a(var1, var2, var3);

        if (this.field_175943_g == 2) {
            this.func_175937_a(var1.offsetEast(), var2.offsetEast(), var3);
            this.func_175937_a(var1.offsetEast().offsetSouth(), var2.offsetEast().offsetSouth(), var3);
            this.func_175937_a(var1.offsetSouth(), var2.offsetSouth(), var3);
        }
    }

    void func_175939_d() {
        Iterator var1 = this.field_175948_j.iterator();

        while (var1.hasNext()) {
            WorldGenBigTree.FoliageCoordinates var2 = (WorldGenBigTree.FoliageCoordinates) var1.next();
            int var3 = var2.func_177999_q();
            BlockPos var4 = new BlockPos(this.field_175947_m.getX(), var3, this.field_175947_m.getZ());

            if (this.leafNodeNeedsBase(var3 - this.field_175947_m.getY())) {
                this.func_175937_a(var4, var2, Blocks.log);
            }
        }
    }

    int func_175936_a(BlockPos p_175936_1_, BlockPos p_175936_2_) {
        BlockPos var3 = p_175936_2_.add(-p_175936_1_.getX(), -p_175936_1_.getY(), -p_175936_1_.getZ());
        int var4 = this.func_175935_b(var3);
        float var5 = (float) var3.getX() / (float) var4;
        float var6 = (float) var3.getY() / (float) var4;
        float var7 = (float) var3.getZ() / (float) var4;

        if (var4 == 0) {
            return -1;
        } else {
            for (int var8 = 0; var8 <= var4; ++var8) {
                BlockPos var9 = p_175936_1_.add((double) (0.5F + (float) var8 * var5), (double) (0.5F + (float) var8 * var6), (double) (0.5F + (float) var8 * var7));

                if (!this.func_150523_a(this.field_175946_l.getBlockState(var9).getBlock())) {
                    return var8;
                }
            }

            return -1;
        }
    }

    public void func_175904_e() {
        this.leafDistanceLimit = 5;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        this.field_175946_l = worldIn;
        this.field_175947_m = p_180709_3_;
        this.field_175949_k = new Random(p_180709_2_.nextLong());

        if (this.heightLimit == 0) {
            this.heightLimit = 5 + this.field_175949_k.nextInt(this.field_175950_h);
        }

        if (!this.validTreeLocation()) {
            return false;
        } else {
            this.generateLeafNodeList();
            this.func_175941_b();
            this.func_175942_c();
            this.func_175939_d();
            return true;
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    private boolean validTreeLocation() {
        Block var1 = this.field_175946_l.getBlockState(this.field_175947_m.offsetDown()).getBlock();

        if (var1 != Blocks.dirt && var1 != Blocks.grass && var1 != Blocks.farmland) {
            return false;
        } else {
            int var2 = this.func_175936_a(this.field_175947_m, this.field_175947_m.offsetUp(this.heightLimit - 1));

            if (var2 == -1) {
                return true;
            } else if (var2 < 6) {
                return false;
            } else {
                this.heightLimit = var2;
                return true;
            }
        }
    }

    static class FoliageCoordinates extends BlockPos {
        private final int field_178000_b;
        private static final String __OBFID = "CL_00002001";

        public FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_) {
            super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
            this.field_178000_b = p_i45635_2_;
        }

        public int func_177999_q() {
            return this.field_178000_b;
        }
    }
}
