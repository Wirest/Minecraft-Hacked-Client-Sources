package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree
{
    private Random rand;
    private World world;
    private BlockPos basePos;
    int heightLimit;
    int height;
    double heightAttenuation;
    double branchSlope;
    double scaleWidth;
    double leafDensity;
    int trunkSize;
    int heightLimitLimit;

    /**
     * Sets the distance limit for how far away the generator will populate leaves from the base leaf node.
     */
    int leafDistanceLimit;
    List field_175948_j;

    public WorldGenBigTree(boolean p_i2008_1_)
    {
        super(p_i2008_1_);
        this.basePos = BlockPos.ORIGIN;
        this.heightAttenuation = 0.618D;
        this.branchSlope = 0.381D;
        this.scaleWidth = 1.0D;
        this.leafDensity = 1.0D;
        this.trunkSize = 1;
        this.heightLimitLimit = 12;
        this.leafDistanceLimit = 4;
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList()
    {
        this.height = (int)(this.heightLimit * this.heightAttenuation);

        if (this.height >= this.heightLimit)
        {
            this.height = this.heightLimit - 1;
        }

        int var1 = (int)(1.382D + Math.pow(this.leafDensity * this.heightLimit / 13.0D, 2.0D));

        if (var1 < 1)
        {
            var1 = 1;
        }

        int var2 = this.basePos.getY() + this.height;
        int var3 = this.heightLimit - this.leafDistanceLimit;
        this.field_175948_j = Lists.newArrayList();
        this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(this.basePos.up(var3), var2));

        for (; var3 >= 0; --var3)
        {
            float var4 = this.layerSize(var3);

            if (var4 >= 0.0F)
            {
                for (int var5 = 0; var5 < var1; ++var5)
                {
                    double var6 = this.scaleWidth * var4 * (this.rand.nextFloat() + 0.328D);
                    double var8 = this.rand.nextFloat() * 2.0F * Math.PI;
                    double var10 = var6 * Math.sin(var8) + 0.5D;
                    double var12 = var6 * Math.cos(var8) + 0.5D;
                    BlockPos var14 = this.basePos.add(var10, var3 - 1, var12);
                    BlockPos var15 = var14.up(this.leafDistanceLimit);

                    if (this.checkBlockLine(var14, var15) == -1)
                    {
                        int var16 = this.basePos.getX() - var14.getX();
                        int var17 = this.basePos.getZ() - var14.getZ();
                        double var18 = var14.getY() - Math.sqrt(var16 * var16 + var17 * var17) * this.branchSlope;
                        int var20 = var18 > var2 ? var2 : (int)var18;
                        BlockPos var21 = new BlockPos(this.basePos.getX(), var20, this.basePos.getZ());

                        if (this.checkBlockLine(var21, var14) == -1)
                        {
                            this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(var14, var21.getY()));
                        }
                    }
                }
            }
        }
    }

    void func_180712_a(BlockPos p_180712_1_, float p_180712_2_, Block p_180712_3_)
    {
        int var4 = (int)(p_180712_2_ + 0.618D);

        for (int var5 = -var4; var5 <= var4; ++var5)
        {
            for (int var6 = -var4; var6 <= var4; ++var6)
            {
                if (Math.pow(Math.abs(var5) + 0.5D, 2.0D) + Math.pow(Math.abs(var6) + 0.5D, 2.0D) <= p_180712_2_ * p_180712_2_)
                {
                    BlockPos var7 = p_180712_1_.add(var5, 0, var6);
                    Material var8 = this.world.getBlockState(var7).getBlock().getMaterial();

                    if (var8 == Material.air || var8 == Material.leaves)
                    {
                        this.func_175905_a(this.world, var7, p_180712_3_, 0);
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    float layerSize(int p_76490_1_)
    {
        if (p_76490_1_ < this.heightLimit * 0.3F)
        {
            return -1.0F;
        }
        else
        {
            float var2 = this.heightLimit / 2.0F;
            float var3 = var2 - p_76490_1_;
            float var4 = MathHelper.sqrt_float(var2 * var2 - var3 * var3);

            if (var3 == 0.0F)
            {
                var4 = var2;
            }
            else if (Math.abs(var3) >= var2)
            {
                return 0.0F;
            }

            return var4 * 0.5F;
        }
    }

    float leafSize(int p_76495_1_)
    {
        return p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit ? (p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    /**
     * Generates the leaves surrounding an individual entry in the leafNodes list.
     */
    void generateLeafNode(BlockPos pos)
    {
        for (int var2 = 0; var2 < this.leafDistanceLimit; ++var2)
        {
            this.func_180712_a(pos.up(var2), this.leafSize(var2), Blocks.leaves);
        }
    }

    void func_175937_a(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_)
    {
        BlockPos var4 = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int var5 = this.getGreatestDistance(var4);
        float var6 = (float)var4.getX() / (float)var5;
        float var7 = (float)var4.getY() / (float)var5;
        float var8 = (float)var4.getZ() / (float)var5;

        for (int var9 = 0; var9 <= var5; ++var9)
        {
            BlockPos var10 = p_175937_1_.add(0.5F + var9 * var6, 0.5F + var9 * var7, 0.5F + var9 * var8);
            BlockLog.EnumAxis var11 = this.func_175938_b(p_175937_1_, var10);
            this.setBlockAndNotifyAdequately(this.world, var10, p_175937_3_.getDefaultState().withProperty(BlockLog.LOG_AXIS, var11));
        }
    }

    /**
     * Returns the absolute greatest distance in the BlockPos object.
     */
    private int getGreatestDistance(BlockPos posIn)
    {
        int var2 = MathHelper.abs_int(posIn.getX());
        int var3 = MathHelper.abs_int(posIn.getY());
        int var4 = MathHelper.abs_int(posIn.getZ());
        return var4 > var2 && var4 > var3 ? var4 : (var3 > var2 ? var3 : var2);
    }

    private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_)
    {
        BlockLog.EnumAxis var3 = BlockLog.EnumAxis.Y;
        int var4 = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int var5 = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
        int var6 = Math.max(var4, var5);

        if (var6 > 0)
        {
            if (var4 == var6)
            {
                var3 = BlockLog.EnumAxis.X;
            }
            else if (var5 == var6)
            {
                var3 = BlockLog.EnumAxis.Z;
            }
        }

        return var3;
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    void generateLeaves()
    {
        Iterator var1 = this.field_175948_j.iterator();

        while (var1.hasNext())
        {
            WorldGenBigTree.FoliageCoordinates var2 = (WorldGenBigTree.FoliageCoordinates)var1.next();
            this.generateLeafNode(var2);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    boolean leafNodeNeedsBase(int p_76493_1_)
    {
        return p_76493_1_ >= this.heightLimit * 0.2D;
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    void generateTrunk()
    {
        BlockPos var1 = this.basePos;
        BlockPos var2 = this.basePos.up(this.height);
        Block var3 = Blocks.log;
        this.func_175937_a(var1, var2, var3);

        if (this.trunkSize == 2)
        {
            this.func_175937_a(var1.east(), var2.east(), var3);
            this.func_175937_a(var1.east().south(), var2.east().south(), var3);
            this.func_175937_a(var1.south(), var2.south(), var3);
        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    void generateLeafNodeBases()
    {
        Iterator var1 = this.field_175948_j.iterator();

        while (var1.hasNext())
        {
            WorldGenBigTree.FoliageCoordinates var2 = (WorldGenBigTree.FoliageCoordinates)var1.next();
            int var3 = var2.func_177999_q();
            BlockPos var4 = new BlockPos(this.basePos.getX(), var3, this.basePos.getZ());

            if (this.leafNodeNeedsBase(var3 - this.basePos.getY()))
            {
                this.func_175937_a(var4, var2, Blocks.log);
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     */
    int checkBlockLine(BlockPos posOne, BlockPos posTwo)
    {
        BlockPos var3 = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
        int var4 = this.getGreatestDistance(var3);
        float var5 = (float)var3.getX() / (float)var4;
        float var6 = (float)var3.getY() / (float)var4;
        float var7 = (float)var3.getZ() / (float)var4;

        if (var4 == 0)
        {
            return -1;
        }
        else
        {
            for (int var8 = 0; var8 <= var4; ++var8)
            {
                BlockPos var9 = posOne.add(0.5F + var8 * var5, 0.5F + var8 * var6, 0.5F + var8 * var7);

                if (!this.func_150523_a(this.world.getBlockState(var9).getBlock()))
                {
                    return var8;
                }
            }

            return -1;
        }
    }

    @Override
	public void func_175904_e()
    {
        this.leafDistanceLimit = 5;
    }

    @Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        this.world = worldIn;
        this.basePos = position;
        this.rand = new Random(rand.nextLong());

        if (this.heightLimit == 0)
        {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
        }

        if (!this.validTreeLocation())
        {
            return false;
        }
        else
        {
            this.generateLeafNodeList();
            this.generateLeaves();
            this.generateTrunk();
            this.generateLeafNodeBases();
            return true;
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    private boolean validTreeLocation()
    {
        Block var1 = this.world.getBlockState(this.basePos.down()).getBlock();

        if (var1 != Blocks.dirt && var1 != Blocks.grass && var1 != Blocks.farmland)
        {
            return false;
        }
        else
        {
            int var2 = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));

            if (var2 == -1)
            {
                return true;
            }
            else if (var2 < 6)
            {
                return false;
            }
            else
            {
                this.heightLimit = var2;
                return true;
            }
        }
    }

    static class FoliageCoordinates extends BlockPos
    {
        private final int field_178000_b;

        public FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_)
        {
            super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
            this.field_178000_b = p_i45635_2_;
        }

        public int func_177999_q()
        {
            return this.field_178000_b;
        }
    }
}
