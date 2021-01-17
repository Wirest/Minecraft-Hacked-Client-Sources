// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenBigMushroom extends WorldGenerator
{
    private Block mushroomType;
    
    public WorldGenBigMushroom(final Block p_i46449_1_) {
        super(true);
        this.mushroomType = p_i46449_1_;
    }
    
    public WorldGenBigMushroom() {
        super(false);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        if (this.mushroomType == null) {
            this.mushroomType = (rand.nextBoolean() ? Blocks.brown_mushroom_block : Blocks.red_mushroom_block);
        }
        final int i = rand.nextInt(3) + 4;
        boolean flag = true;
        if (position.getY() < 1 || position.getY() + i + 1 >= 256) {
            return false;
        }
        for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            int k = 3;
            if (j <= position.getY() + 3) {
                k = 0;
            }
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                for (int i2 = position.getZ() - k; i2 <= position.getZ() + k && flag; ++i2) {
                    if (j >= 0 && j < 256) {
                        final Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l, j, i2)).getBlock();
                        if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
                            flag = false;
                        }
                    }
                    else {
                        flag = false;
                    }
                }
            }
        }
        if (!flag) {
            return false;
        }
        final Block block2 = worldIn.getBlockState(position.down()).getBlock();
        if (block2 != Blocks.dirt && block2 != Blocks.grass && block2 != Blocks.mycelium) {
            return false;
        }
        int k2 = position.getY() + i;
        if (this.mushroomType == Blocks.red_mushroom_block) {
            k2 = position.getY() + i - 3;
        }
        for (int l2 = k2; l2 <= position.getY() + i; ++l2) {
            int j2 = 1;
            if (l2 < position.getY() + i) {
                ++j2;
            }
            if (this.mushroomType == Blocks.brown_mushroom_block) {
                j2 = 3;
            }
            final int k3 = position.getX() - j2;
            final int l3 = position.getX() + j2;
            final int j3 = position.getZ() - j2;
            final int k4 = position.getZ() + j2;
            for (int l4 = k3; l4 <= l3; ++l4) {
                for (int i3 = j3; i3 <= k4; ++i3) {
                    int j4 = 5;
                    if (l4 == k3) {
                        --j4;
                    }
                    else if (l4 == l3) {
                        ++j4;
                    }
                    if (i3 == j3) {
                        j4 -= 3;
                    }
                    else if (i3 == k4) {
                        j4 += 3;
                    }
                    BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.byMetadata(j4);
                    if (this.mushroomType == Blocks.brown_mushroom_block || l2 < position.getY() + i) {
                        if (l4 == k3 || l4 == l3) {
                            if (i3 == j3) {
                                continue;
                            }
                            if (i3 == k4) {
                                continue;
                            }
                        }
                        if (l4 == position.getX() - (j2 - 1) && i3 == j3) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
                        }
                        if (l4 == k3 && i3 == position.getZ() - (j2 - 1)) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
                        }
                        if (l4 == position.getX() + (j2 - 1) && i3 == j3) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
                        }
                        if (l4 == l3 && i3 == position.getZ() - (j2 - 1)) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
                        }
                        if (l4 == position.getX() - (j2 - 1) && i3 == k4) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
                        }
                        if (l4 == k3 && i3 == position.getZ() + (j2 - 1)) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
                        }
                        if (l4 == position.getX() + (j2 - 1) && i3 == k4) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
                        }
                        if (l4 == l3 && i3 == position.getZ() + (j2 - 1)) {
                            blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
                        }
                    }
                    if (blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER && l2 < position.getY() + i) {
                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
                    }
                    if (position.getY() >= position.getY() + i - 1 || blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE) {
                        final BlockPos blockpos = new BlockPos(l4, l2, i3);
                        if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock()) {
                            this.setBlockAndNotifyAdequately(worldIn, blockpos, this.mushroomType.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, blockhugemushroom$enumtype));
                        }
                    }
                }
            }
        }
        for (int i4 = 0; i4 < i; ++i4) {
            final Block block3 = worldIn.getBlockState(position.up(i4)).getBlock();
            if (!block3.isFullBlock()) {
                this.setBlockAndNotifyAdequately(worldIn, position.up(i4), this.mushroomType.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
            }
        }
        return true;
    }
}
