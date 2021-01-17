// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderEnd implements IChunkProvider
{
    private Random endRNG;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    public NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    private World endWorld;
    private double[] densities;
    private BiomeGenBase[] biomesForGeneration;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    
    public ChunkProviderEnd(final World worldIn, final long p_i2007_2_) {
        this.endWorld = worldIn;
        this.endRNG = new Random(p_i2007_2_);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
    }
    
    public void func_180520_a(final int p_180520_1_, final int p_180520_2_, final ChunkPrimer p_180520_3_) {
        final int i = 2;
        final int j = i + 1;
        final int k = 33;
        final int l = i + 1;
        this.densities = this.initializeNoiseField(this.densities, p_180520_1_ * i, 0, p_180520_2_ * i, j, k, l);
        for (int i2 = 0; i2 < i; ++i2) {
            for (int j2 = 0; j2 < i; ++j2) {
                for (int k2 = 0; k2 < 32; ++k2) {
                    final double d0 = 0.25;
                    double d2 = this.densities[((i2 + 0) * l + j2 + 0) * k + k2 + 0];
                    double d3 = this.densities[((i2 + 0) * l + j2 + 1) * k + k2 + 0];
                    double d4 = this.densities[((i2 + 1) * l + j2 + 0) * k + k2 + 0];
                    double d5 = this.densities[((i2 + 1) * l + j2 + 1) * k + k2 + 0];
                    final double d6 = (this.densities[((i2 + 0) * l + j2 + 0) * k + k2 + 1] - d2) * d0;
                    final double d7 = (this.densities[((i2 + 0) * l + j2 + 1) * k + k2 + 1] - d3) * d0;
                    final double d8 = (this.densities[((i2 + 1) * l + j2 + 0) * k + k2 + 1] - d4) * d0;
                    final double d9 = (this.densities[((i2 + 1) * l + j2 + 1) * k + k2 + 1] - d5) * d0;
                    for (int l2 = 0; l2 < 4; ++l2) {
                        final double d10 = 0.125;
                        double d11 = d2;
                        double d12 = d3;
                        final double d13 = (d4 - d2) * d10;
                        final double d14 = (d5 - d3) * d10;
                        for (int i3 = 0; i3 < 8; ++i3) {
                            final double d15 = 0.125;
                            double d16 = d11;
                            final double d17 = (d12 - d11) * d15;
                            for (int j3 = 0; j3 < 8; ++j3) {
                                IBlockState iblockstate = null;
                                if (d16 > 0.0) {
                                    iblockstate = Blocks.end_stone.getDefaultState();
                                }
                                final int k3 = i3 + i2 * 8;
                                final int l3 = l2 + k2 * 4;
                                final int i4 = j3 + j2 * 8;
                                p_180520_3_.setBlockState(k3, l3, i4, iblockstate);
                                d16 += d17;
                            }
                            d11 += d13;
                            d12 += d14;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                    }
                }
            }
        }
    }
    
    public void func_180519_a(final ChunkPrimer p_180519_1_) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                final int k = 1;
                int l = -1;
                IBlockState iblockstate = Blocks.end_stone.getDefaultState();
                IBlockState iblockstate2 = Blocks.end_stone.getDefaultState();
                for (int i2 = 127; i2 >= 0; --i2) {
                    final IBlockState iblockstate3 = p_180519_1_.getBlockState(i, i2, j);
                    if (iblockstate3.getBlock().getMaterial() == Material.air) {
                        l = -1;
                    }
                    else if (iblockstate3.getBlock() == Blocks.stone) {
                        if (l == -1) {
                            if (k <= 0) {
                                iblockstate = Blocks.air.getDefaultState();
                                iblockstate2 = Blocks.end_stone.getDefaultState();
                            }
                            l = k;
                            if (i2 >= 0) {
                                p_180519_1_.setBlockState(i, i2, j, iblockstate);
                            }
                            else {
                                p_180519_1_.setBlockState(i, i2, j, iblockstate2);
                            }
                        }
                        else if (l > 0) {
                            --l;
                            p_180519_1_.setBlockState(i, i2, j, iblockstate2);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        this.endRNG.setSeed(x * 341873128712L + z * 132897987541L);
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.func_180520_a(x, z, chunkprimer);
        this.func_180519_a(chunkprimer);
        final Chunk chunk = new Chunk(this.endWorld, chunkprimer, x, z);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)this.biomesForGeneration[i].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    private double[] initializeNoiseField(double[] p_73187_1_, final int p_73187_2_, final int p_73187_3_, final int p_73187_4_, final int p_73187_5_, final int p_73187_6_, final int p_73187_7_) {
        if (p_73187_1_ == null) {
            p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
        }
        double d0 = 684.412;
        final double d2 = 684.412;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 1.121, 1.121, 0.5);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 200.0, 200.0, 0.5);
        d0 *= 2.0;
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0 / 80.0, d2 / 160.0, d0 / 80.0);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d2, d0);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d2, d0);
        int i = 0;
        for (int j = 0; j < p_73187_5_; ++j) {
            for (int k = 0; k < p_73187_7_; ++k) {
                final float f = (j + p_73187_2_) / 1.0f;
                final float f2 = (k + p_73187_4_) / 1.0f;
                float f3 = 100.0f - MathHelper.sqrt_float(f * f + f2 * f2) * 8.0f;
                if (f3 > 80.0f) {
                    f3 = 80.0f;
                }
                if (f3 < -100.0f) {
                    f3 = -100.0f;
                }
                for (int l = 0; l < p_73187_6_; ++l) {
                    double d3 = 0.0;
                    final double d4 = this.noiseData2[i] / 512.0;
                    final double d5 = this.noiseData3[i] / 512.0;
                    final double d6 = (this.noiseData1[i] / 10.0 + 1.0) / 2.0;
                    if (d6 < 0.0) {
                        d3 = d4;
                    }
                    else if (d6 > 1.0) {
                        d3 = d5;
                    }
                    else {
                        d3 = d4 + (d5 - d4) * d6;
                    }
                    d3 -= 8.0;
                    d3 += f3;
                    int i2 = 2;
                    if (l > p_73187_6_ / 2 - i2) {
                        double d7 = (l - (p_73187_6_ / 2 - i2)) / 64.0f;
                        d7 = MathHelper.clamp_double(d7, 0.0, 1.0);
                        d3 = d3 * (1.0 - d7) + -3000.0 * d7;
                    }
                    i2 = 8;
                    if (l < i2) {
                        final double d8 = (i2 - l) / (i2 - 1.0f);
                        d3 = d3 * (1.0 - d8) + -30.0 * d8;
                    }
                    p_73187_1_[i] = d3;
                    ++i;
                }
            }
        }
        return p_73187_1_;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        final BlockPos blockpos = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        this.endWorld.getBiomeGenForCoords(blockpos.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand, blockpos);
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate progressCallback) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    @Override
    public boolean canSave() {
        return true;
    }
    
    @Override
    public String makeString() {
        return "RandomLevelSource";
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return this.endWorld.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void recreateStructures(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
