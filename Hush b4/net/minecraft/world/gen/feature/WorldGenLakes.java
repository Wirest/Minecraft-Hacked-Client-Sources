// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.material.Material;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class WorldGenLakes extends WorldGenerator
{
    private Block block;
    
    public WorldGenLakes(final Block blockIn) {
        this.block = blockIn;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        for (position = position.add(-8, 0, -8); position.getY() > 5 && worldIn.isAirBlock(position); position = position.down()) {}
        if (position.getY() <= 4) {
            return false;
        }
        position = position.down(4);
        final boolean[] aboolean = new boolean[2048];
        for (int i = rand.nextInt(4) + 4, j = 0; j < i; ++j) {
            final double d0 = rand.nextDouble() * 6.0 + 3.0;
            final double d2 = rand.nextDouble() * 4.0 + 2.0;
            final double d3 = rand.nextDouble() * 6.0 + 3.0;
            final double d4 = rand.nextDouble() * (16.0 - d0 - 2.0) + 1.0 + d0 / 2.0;
            final double d5 = rand.nextDouble() * (8.0 - d2 - 4.0) + 2.0 + d2 / 2.0;
            final double d6 = rand.nextDouble() * (16.0 - d3 - 2.0) + 1.0 + d3 / 2.0;
            for (int l = 1; l < 15; ++l) {
                for (int i2 = 1; i2 < 15; ++i2) {
                    for (int j2 = 1; j2 < 7; ++j2) {
                        final double d7 = (l - d4) / (d0 / 2.0);
                        final double d8 = (j2 - d5) / (d2 / 2.0);
                        final double d9 = (i2 - d6) / (d3 / 2.0);
                        final double d10 = d7 * d7 + d8 * d8 + d9 * d9;
                        if (d10 < 1.0) {
                            aboolean[(l * 16 + i2) * 8 + j2] = true;
                        }
                    }
                }
            }
        }
        for (int k1 = 0; k1 < 16; ++k1) {
            for (int l2 = 0; l2 < 16; ++l2) {
                for (int m = 0; m < 8; ++m) {
                    final boolean flag = !aboolean[(k1 * 16 + l2) * 8 + m] && ((k1 < 15 && aboolean[((k1 + 1) * 16 + l2) * 8 + m]) || (k1 > 0 && aboolean[((k1 - 1) * 16 + l2) * 8 + m]) || (l2 < 15 && aboolean[(k1 * 16 + l2 + 1) * 8 + m]) || (l2 > 0 && aboolean[(k1 * 16 + (l2 - 1)) * 8 + m]) || (m < 7 && aboolean[(k1 * 16 + l2) * 8 + m + 1]) || (m > 0 && aboolean[(k1 * 16 + l2) * 8 + (m - 1)]));
                    if (flag) {
                        final Material material = worldIn.getBlockState(position.add(k1, m, l2)).getBlock().getMaterial();
                        if (m >= 4 && material.isLiquid()) {
                            return false;
                        }
                        if (m < 4 && !material.isSolid() && worldIn.getBlockState(position.add(k1, m, l2)).getBlock() != this.block) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int l3 = 0; l3 < 16; ++l3) {
            for (int i3 = 0; i3 < 16; ++i3) {
                for (int i4 = 0; i4 < 8; ++i4) {
                    if (aboolean[(l3 * 16 + i3) * 8 + i4]) {
                        worldIn.setBlockState(position.add(l3, i4, i3), (i4 >= 4) ? Blocks.air.getDefaultState() : this.block.getDefaultState(), 2);
                    }
                }
            }
        }
        for (int i5 = 0; i5 < 16; ++i5) {
            for (int j3 = 0; j3 < 16; ++j3) {
                for (int j4 = 4; j4 < 8; ++j4) {
                    if (aboolean[(i5 * 16 + j3) * 8 + j4]) {
                        final BlockPos blockpos = position.add(i5, j4 - 1, j3);
                        if (worldIn.getBlockState(blockpos).getBlock() == Blocks.dirt && worldIn.getLightFor(EnumSkyBlock.SKY, position.add(i5, j4, j3)) > 0) {
                            final BiomeGenBase biomegenbase = worldIn.getBiomeGenForCoords(blockpos);
                            if (biomegenbase.topBlock.getBlock() == Blocks.mycelium) {
                                worldIn.setBlockState(blockpos, Blocks.mycelium.getDefaultState(), 2);
                            }
                            else {
                                worldIn.setBlockState(blockpos, Blocks.grass.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
        }
        if (this.block.getMaterial() == Material.lava) {
            for (int j5 = 0; j5 < 16; ++j5) {
                for (int k2 = 0; k2 < 16; ++k2) {
                    for (int k3 = 0; k3 < 8; ++k3) {
                        final boolean flag2 = !aboolean[(j5 * 16 + k2) * 8 + k3] && ((j5 < 15 && aboolean[((j5 + 1) * 16 + k2) * 8 + k3]) || (j5 > 0 && aboolean[((j5 - 1) * 16 + k2) * 8 + k3]) || (k2 < 15 && aboolean[(j5 * 16 + k2 + 1) * 8 + k3]) || (k2 > 0 && aboolean[(j5 * 16 + (k2 - 1)) * 8 + k3]) || (k3 < 7 && aboolean[(j5 * 16 + k2) * 8 + k3 + 1]) || (k3 > 0 && aboolean[(j5 * 16 + k2) * 8 + (k3 - 1)]));
                        if (flag2 && (k3 < 4 || rand.nextInt(2) != 0) && worldIn.getBlockState(position.add(j5, k3, k2)).getBlock().getMaterial().isSolid()) {
                            worldIn.setBlockState(position.add(j5, k3, k2), Blocks.stone.getDefaultState(), 2);
                        }
                    }
                }
            }
        }
        if (this.block.getMaterial() == Material.water) {
            for (int k4 = 0; k4 < 16; ++k4) {
                for (int l4 = 0; l4 < 16; ++l4) {
                    final int l5 = 4;
                    if (worldIn.canBlockFreezeWater(position.add(k4, l5, l4))) {
                        worldIn.setBlockState(position.add(k4, l5, l4), Blocks.ice.getDefaultState(), 2);
                    }
                }
            }
        }
        return true;
    }
}
