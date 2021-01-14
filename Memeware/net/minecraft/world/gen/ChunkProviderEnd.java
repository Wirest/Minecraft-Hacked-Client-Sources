package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderEnd implements IChunkProvider {
    private Random endRNG;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    public NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    private World endWorld;
    private double[] densities;

    /**
     * The biomes that are used to generate the chunk
     */
    private BiomeGenBase[] biomesForGeneration;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    private static final String __OBFID = "CL_00000397";

    public ChunkProviderEnd(World worldIn, long p_i2007_2_) {
        this.endWorld = worldIn;
        this.endRNG = new Random(p_i2007_2_);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
    }

    public void func_180520_a(int p_180520_1_, int p_180520_2_, ChunkPrimer p_180520_3_) {
        byte var4 = 2;
        int var5 = var4 + 1;
        byte var6 = 33;
        int var7 = var4 + 1;
        this.densities = this.initializeNoiseField(this.densities, p_180520_1_ * var4, 0, p_180520_2_ * var4, var5, var6, var7);

        for (int var8 = 0; var8 < var4; ++var8) {
            for (int var9 = 0; var9 < var4; ++var9) {
                for (int var10 = 0; var10 < 32; ++var10) {
                    double var11 = 0.25D;
                    double var13 = this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var15 = this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 0];
                    double var17 = this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var19 = this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 0];
                    double var21 = (this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 1] - var13) * var11;
                    double var23 = (this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 1] - var15) * var11;
                    double var25 = (this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 1] - var17) * var11;
                    double var27 = (this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 1] - var19) * var11;

                    for (int var29 = 0; var29 < 4; ++var29) {
                        double var30 = 0.125D;
                        double var32 = var13;
                        double var34 = var15;
                        double var36 = (var17 - var13) * var30;
                        double var38 = (var19 - var15) * var30;

                        for (int var40 = 0; var40 < 8; ++var40) {
                            double var41 = 0.125D;
                            double var43 = var32;
                            double var45 = (var34 - var32) * var41;

                            for (int var47 = 0; var47 < 8; ++var47) {
                                IBlockState var48 = null;

                                if (var43 > 0.0D) {
                                    var48 = Blocks.end_stone.getDefaultState();
                                }

                                int var49 = var40 + var8 * 8;
                                int var50 = var29 + var10 * 4;
                                int var51 = var47 + var9 * 8;
                                p_180520_3_.setBlockState(var49, var50, var51, var48);
                                var43 += var45;
                            }

                            var32 += var36;
                            var34 += var38;
                        }

                        var13 += var21;
                        var15 += var23;
                        var17 += var25;
                        var19 += var27;
                    }
                }
            }
        }
    }

    public void func_180519_a(ChunkPrimer p_180519_1_) {
        for (int var2 = 0; var2 < 16; ++var2) {
            for (int var3 = 0; var3 < 16; ++var3) {
                byte var4 = 1;
                int var5 = -1;
                IBlockState var6 = Blocks.end_stone.getDefaultState();
                IBlockState var7 = Blocks.end_stone.getDefaultState();

                for (int var8 = 127; var8 >= 0; --var8) {
                    IBlockState var9 = p_180519_1_.getBlockState(var2, var8, var3);

                    if (var9.getBlock().getMaterial() == Material.air) {
                        var5 = -1;
                    } else if (var9.getBlock() == Blocks.stone) {
                        if (var5 == -1) {
                            if (var4 <= 0) {
                                var6 = Blocks.air.getDefaultState();
                                var7 = Blocks.end_stone.getDefaultState();
                            }

                            var5 = var4;

                            if (var8 >= 0) {
                                p_180519_1_.setBlockState(var2, var8, var3, var6);
                            } else {
                                p_180519_1_.setBlockState(var2, var8, var3, var7);
                            }
                        } else if (var5 > 0) {
                            --var5;
                            p_180519_1_.setBlockState(var2, var8, var3, var7);
                        }
                    }
                }
            }
        }
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        this.endRNG.setSeed((long) p_73154_1_ * 341873128712L + (long) p_73154_2_ * 132897987541L);
        ChunkPrimer var3 = new ChunkPrimer();
        this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        this.func_180520_a(p_73154_1_, p_73154_2_, var3);
        this.func_180519_a(var3);
        Chunk var4 = new Chunk(this.endWorld, var3, p_73154_1_, p_73154_2_);
        byte[] var5 = var4.getBiomeArray();

        for (int var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = (byte) this.biomesForGeneration[var6].biomeID;
        }

        var4.generateSkylightMap();
        return var4;
    }

    /**
     * generates a subset of the level's terrain data. Takes 7 arguments: the [empty] noise array, the position, and the
     * size.
     */
    private double[] initializeNoiseField(double[] p_73187_1_, int p_73187_2_, int p_73187_3_, int p_73187_4_, int p_73187_5_, int p_73187_6_, int p_73187_7_) {
        if (p_73187_1_ == null) {
            p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
        }

        double var8 = 684.412D;
        double var10 = 684.412D;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 1.121D, 1.121D, 0.5D);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 200.0D, 200.0D, 0.5D);
        var8 *= 2.0D;
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8, var10, var8);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8, var10, var8);
        int var12 = 0;

        for (int var13 = 0; var13 < p_73187_5_; ++var13) {
            for (int var14 = 0; var14 < p_73187_7_; ++var14) {
                float var15 = (float) (var13 + p_73187_2_) / 1.0F;
                float var16 = (float) (var14 + p_73187_4_) / 1.0F;
                float var17 = 100.0F - MathHelper.sqrt_float(var15 * var15 + var16 * var16) * 8.0F;

                if (var17 > 80.0F) {
                    var17 = 80.0F;
                }

                if (var17 < -100.0F) {
                    var17 = -100.0F;
                }

                for (int var18 = 0; var18 < p_73187_6_; ++var18) {
                    double var19 = 0.0D;
                    double var21 = this.noiseData2[var12] / 512.0D;
                    double var23 = this.noiseData3[var12] / 512.0D;
                    double var25 = (this.noiseData1[var12] / 10.0D + 1.0D) / 2.0D;

                    if (var25 < 0.0D) {
                        var19 = var21;
                    } else if (var25 > 1.0D) {
                        var19 = var23;
                    } else {
                        var19 = var21 + (var23 - var21) * var25;
                    }

                    var19 -= 8.0D;
                    var19 += (double) var17;
                    byte var27 = 2;
                    double var28;

                    if (var18 > p_73187_6_ / 2 - var27) {
                        var28 = (double) ((float) (var18 - (p_73187_6_ / 2 - var27)) / 64.0F);
                        var28 = MathHelper.clamp_double(var28, 0.0D, 1.0D);
                        var19 = var19 * (1.0D - var28) + -3000.0D * var28;
                    }

                    var27 = 8;

                    if (var18 < var27) {
                        var28 = (double) ((float) (var27 - var18) / ((float) var27 - 1.0F));
                        var19 = var19 * (1.0D - var28) + -30.0D * var28;
                    }

                    p_73187_1_[var12] = var19;
                    ++var12;
                }
            }
        }

        return p_73187_1_;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        BlockPos var4 = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        this.endWorld.getBiomeGenForCoords(var4.add(16, 0, 16)).func_180624_a(this.endWorld, this.endWorld.rand, var4);
        BlockFalling.fallInstantly = false;
    }

    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        return false;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData() {
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks() {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave() {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString() {
        return "RandomLevelSource";
    }

    public List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
        return this.endWorld.getBiomeGenForCoords(p_177458_2_).getSpawnableList(p_177458_1_);
    }

    public BlockPos func_180513_a(World worldIn, String p_180513_2_, BlockPos p_180513_3_) {
        return null;
    }

    public int getLoadedChunkCount() {
        return 0;
    }

    public void func_180514_a(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
    }

    public Chunk func_177459_a(BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}
