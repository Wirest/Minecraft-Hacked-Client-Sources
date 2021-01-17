// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import java.util.Iterator;
import net.minecraft.block.BlockLog;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLeaves;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

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
    int leafDistanceLimit;
    List<FoliageCoordinates> field_175948_j;
    
    public WorldGenBigTree(final boolean p_i2008_1_) {
        super(p_i2008_1_);
        this.basePos = BlockPos.ORIGIN;
        this.heightAttenuation = 0.618;
        this.branchSlope = 0.381;
        this.scaleWidth = 1.0;
        this.leafDensity = 1.0;
        this.trunkSize = 1;
        this.heightLimitLimit = 12;
        this.leafDistanceLimit = 4;
    }
    
    void generateLeafNodeList() {
        this.height = (int)(this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }
        int i = (int)(1.382 + Math.pow(this.leafDensity * this.heightLimit / 13.0, 2.0));
        if (i < 1) {
            i = 1;
        }
        final int j = this.basePos.getY() + this.height;
        int k = this.heightLimit - this.leafDistanceLimit;
        (this.field_175948_j = (List<FoliageCoordinates>)Lists.newArrayList()).add(new FoliageCoordinates(this.basePos.up(k), j));
        while (k >= 0) {
            final float f = this.layerSize(k);
            if (f >= 0.0f) {
                for (int l = 0; l < i; ++l) {
                    final double d0 = this.scaleWidth * f * (this.rand.nextFloat() + 0.328);
                    final double d2 = this.rand.nextFloat() * 2.0f * 3.141592653589793;
                    final double d3 = d0 * Math.sin(d2) + 0.5;
                    final double d4 = d0 * Math.cos(d2) + 0.5;
                    final BlockPos blockpos = this.basePos.add(d3, k - 1, d4);
                    final BlockPos blockpos2 = blockpos.up(this.leafDistanceLimit);
                    if (this.checkBlockLine(blockpos, blockpos2) == -1) {
                        final int i2 = this.basePos.getX() - blockpos.getX();
                        final int j2 = this.basePos.getZ() - blockpos.getZ();
                        final double d5 = blockpos.getY() - Math.sqrt(i2 * i2 + j2 * j2) * this.branchSlope;
                        final int k2 = (d5 > j) ? j : ((int)d5);
                        final BlockPos blockpos3 = new BlockPos(this.basePos.getX(), k2, this.basePos.getZ());
                        if (this.checkBlockLine(blockpos3, blockpos) == -1) {
                            this.field_175948_j.add(new FoliageCoordinates(blockpos, blockpos3.getY()));
                        }
                    }
                }
            }
            --k;
        }
    }
    
    void func_181631_a(final BlockPos p_181631_1_, final float p_181631_2_, final IBlockState p_181631_3_) {
        for (int i = (int)(p_181631_2_ + 0.618), j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                if (Math.pow(Math.abs(j) + 0.5, 2.0) + Math.pow(Math.abs(k) + 0.5, 2.0) <= p_181631_2_ * p_181631_2_) {
                    final BlockPos blockpos = p_181631_1_.add(j, 0, k);
                    final Material material = this.world.getBlockState(blockpos).getBlock().getMaterial();
                    if (material == Material.air || material == Material.leaves) {
                        this.setBlockAndNotifyAdequately(this.world, blockpos, p_181631_3_);
                    }
                }
            }
        }
    }
    
    float layerSize(final int p_76490_1_) {
        if (p_76490_1_ < this.heightLimit * 0.3f) {
            return -1.0f;
        }
        final float f = this.heightLimit / 2.0f;
        final float f2 = f - p_76490_1_;
        float f3 = MathHelper.sqrt_float(f * f - f2 * f2);
        if (f2 == 0.0f) {
            f3 = f;
        }
        else if (Math.abs(f2) >= f) {
            return 0.0f;
        }
        return f3 * 0.5f;
    }
    
    float leafSize(final int p_76495_1_) {
        return (p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit) ? ((p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1) ? 3.0f : 2.0f) : -1.0f;
    }
    
    void generateLeafNode(final BlockPos pos) {
        for (int i = 0; i < this.leafDistanceLimit; ++i) {
            this.func_181631_a(pos.up(i), this.leafSize(i), Blocks.leaves.getDefaultState().withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false));
        }
    }
    
    void func_175937_a(final BlockPos p_175937_1_, final BlockPos p_175937_2_, final Block p_175937_3_) {
        final BlockPos blockpos = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        final int i = this.getGreatestDistance(blockpos);
        final float f = blockpos.getX() / (float)i;
        final float f2 = blockpos.getY() / (float)i;
        final float f3 = blockpos.getZ() / (float)i;
        for (int j = 0; j <= i; ++j) {
            final BlockPos blockpos2 = p_175937_1_.add(0.5f + j * f, 0.5f + j * f2, 0.5f + j * f3);
            final BlockLog.EnumAxis blocklog$enumaxis = this.func_175938_b(p_175937_1_, blockpos2);
            this.setBlockAndNotifyAdequately(this.world, blockpos2, p_175937_3_.getDefaultState().withProperty(BlockLog.LOG_AXIS, blocklog$enumaxis));
        }
    }
    
    private int getGreatestDistance(final BlockPos posIn) {
        final int i = MathHelper.abs_int(posIn.getX());
        final int j = MathHelper.abs_int(posIn.getY());
        final int k = MathHelper.abs_int(posIn.getZ());
        return (k > i && k > j) ? k : ((j > i) ? j : i);
    }
    
    private BlockLog.EnumAxis func_175938_b(final BlockPos p_175938_1_, final BlockPos p_175938_2_) {
        BlockLog.EnumAxis blocklog$enumaxis = BlockLog.EnumAxis.Y;
        final int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        final int j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
        final int k = Math.max(i, j);
        if (k > 0) {
            if (i == k) {
                blocklog$enumaxis = BlockLog.EnumAxis.X;
            }
            else if (j == k) {
                blocklog$enumaxis = BlockLog.EnumAxis.Z;
            }
        }
        return blocklog$enumaxis;
    }
    
    void generateLeaves() {
        for (final FoliageCoordinates worldgenbigtree$foliagecoordinates : this.field_175948_j) {
            this.generateLeafNode(worldgenbigtree$foliagecoordinates);
        }
    }
    
    boolean leafNodeNeedsBase(final int p_76493_1_) {
        return p_76493_1_ >= this.heightLimit * 0.2;
    }
    
    void generateTrunk() {
        final BlockPos blockpos = this.basePos;
        final BlockPos blockpos2 = this.basePos.up(this.height);
        final Block block = Blocks.log;
        this.func_175937_a(blockpos, blockpos2, block);
        if (this.trunkSize == 2) {
            this.func_175937_a(blockpos.east(), blockpos2.east(), block);
            this.func_175937_a(blockpos.east().south(), blockpos2.east().south(), block);
            this.func_175937_a(blockpos.south(), blockpos2.south(), block);
        }
    }
    
    void generateLeafNodeBases() {
        for (final FoliageCoordinates worldgenbigtree$foliagecoordinates : this.field_175948_j) {
            final int i = worldgenbigtree$foliagecoordinates.func_177999_q();
            final BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());
            if (!blockpos.equals(worldgenbigtree$foliagecoordinates) && this.leafNodeNeedsBase(i - this.basePos.getY())) {
                this.func_175937_a(blockpos, worldgenbigtree$foliagecoordinates, Blocks.log);
            }
        }
    }
    
    int checkBlockLine(final BlockPos posOne, final BlockPos posTwo) {
        final BlockPos blockpos = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
        final int i = this.getGreatestDistance(blockpos);
        final float f = blockpos.getX() / (float)i;
        final float f2 = blockpos.getY() / (float)i;
        final float f3 = blockpos.getZ() / (float)i;
        if (i == 0) {
            return -1;
        }
        for (int j = 0; j <= i; ++j) {
            final BlockPos blockpos2 = posOne.add(0.5f + j * f, 0.5f + j * f2, 0.5f + j * f3);
            if (!this.func_150523_a(this.world.getBlockState(blockpos2).getBlock())) {
                return j;
            }
        }
        return -1;
    }
    
    @Override
    public void func_175904_e() {
        this.leafDistanceLimit = 5;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        this.world = worldIn;
        this.basePos = position;
        this.rand = new Random(rand.nextLong());
        if (this.heightLimit == 0) {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
        }
        if (!this.validTreeLocation()) {
            return false;
        }
        this.generateLeafNodeList();
        this.generateLeaves();
        this.generateTrunk();
        this.generateLeafNodeBases();
        return true;
    }
    
    private boolean validTreeLocation() {
        final Block block = this.world.getBlockState(this.basePos.down()).getBlock();
        if (block != Blocks.dirt && block != Blocks.grass && block != Blocks.farmland) {
            return false;
        }
        final int i = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));
        if (i == -1) {
            return true;
        }
        if (i < 6) {
            return false;
        }
        this.heightLimit = i;
        return true;
    }
    
    static class FoliageCoordinates extends BlockPos
    {
        private final int field_178000_b;
        
        public FoliageCoordinates(final BlockPos p_i45635_1_, final int p_i45635_2_) {
            super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
            this.field_178000_b = p_i45635_2_;
        }
        
        public int func_177999_q() {
            return this.field_178000_b;
        }
    }
}
