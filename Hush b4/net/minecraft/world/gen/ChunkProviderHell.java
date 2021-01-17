// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenFire;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderHell implements IChunkProvider
{
    private final World worldObj;
    private final boolean field_177466_i;
    private final Random hellRNG;
    private double[] slowsandNoise;
    private double[] gravelNoise;
    private double[] netherrackExclusivityNoise;
    private double[] noiseField;
    private final NoiseGeneratorOctaves netherNoiseGen1;
    private final NoiseGeneratorOctaves netherNoiseGen2;
    private final NoiseGeneratorOctaves netherNoiseGen3;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public final NoiseGeneratorOctaves netherNoiseGen6;
    public final NoiseGeneratorOctaves netherNoiseGen7;
    private final WorldGenFire field_177470_t;
    private final WorldGenGlowStone1 field_177469_u;
    private final WorldGenGlowStone2 field_177468_v;
    private final WorldGenerator field_177467_w;
    private final WorldGenHellLava field_177473_x;
    private final WorldGenHellLava field_177472_y;
    private final GeneratorBushFeature field_177471_z;
    private final GeneratorBushFeature field_177465_A;
    private final MapGenNetherBridge genNetherBridge;
    private final MapGenBase netherCaveGenerator;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    
    public ChunkProviderHell(final World worldIn, final boolean p_i45637_2_, final long p_i45637_3_) {
        this.slowsandNoise = new double[256];
        this.gravelNoise = new double[256];
        this.netherrackExclusivityNoise = new double[256];
        this.field_177470_t = new WorldGenFire();
        this.field_177469_u = new WorldGenGlowStone1();
        this.field_177468_v = new WorldGenGlowStone2();
        this.field_177467_w = new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14, BlockHelper.forBlock(Blocks.netherrack));
        this.field_177473_x = new WorldGenHellLava(Blocks.flowing_lava, true);
        this.field_177472_y = new WorldGenHellLava(Blocks.flowing_lava, false);
        this.field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
        this.genNetherBridge = new MapGenNetherBridge();
        this.netherCaveGenerator = new MapGenCavesHell();
        this.worldObj = worldIn;
        this.field_177466_i = p_i45637_2_;
        this.hellRNG = new Random(p_i45637_3_);
        this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
        this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        worldIn.func_181544_b(63);
    }
    
    public void func_180515_a(final int p_180515_1_, final int p_180515_2_, final ChunkPrimer p_180515_3_) {
        final int i = 4;
        final int j = this.worldObj.func_181545_F() / 2 + 1;
        final int k = i + 1;
        final int l = 17;
        final int i2 = i + 1;
        this.noiseField = this.initializeNoiseField(this.noiseField, p_180515_1_ * i, 0, p_180515_2_ * i, k, l, i2);
        for (int j2 = 0; j2 < i; ++j2) {
            for (int k2 = 0; k2 < i; ++k2) {
                for (int l2 = 0; l2 < 16; ++l2) {
                    final double d0 = 0.125;
                    double d2 = this.noiseField[((j2 + 0) * i2 + k2 + 0) * l + l2 + 0];
                    double d3 = this.noiseField[((j2 + 0) * i2 + k2 + 1) * l + l2 + 0];
                    double d4 = this.noiseField[((j2 + 1) * i2 + k2 + 0) * l + l2 + 0];
                    double d5 = this.noiseField[((j2 + 1) * i2 + k2 + 1) * l + l2 + 0];
                    final double d6 = (this.noiseField[((j2 + 0) * i2 + k2 + 0) * l + l2 + 1] - d2) * d0;
                    final double d7 = (this.noiseField[((j2 + 0) * i2 + k2 + 1) * l + l2 + 1] - d3) * d0;
                    final double d8 = (this.noiseField[((j2 + 1) * i2 + k2 + 0) * l + l2 + 1] - d4) * d0;
                    final double d9 = (this.noiseField[((j2 + 1) * i2 + k2 + 1) * l + l2 + 1] - d5) * d0;
                    for (int i3 = 0; i3 < 8; ++i3) {
                        final double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        final double d13 = (d4 - d2) * d10;
                        final double d14 = (d5 - d3) * d10;
                        for (int j3 = 0; j3 < 4; ++j3) {
                            final double d15 = 0.25;
                            double d16 = d11;
                            final double d17 = (d12 - d11) * d15;
                            for (int k3 = 0; k3 < 4; ++k3) {
                                IBlockState iblockstate = null;
                                if (l2 * 8 + i3 < j) {
                                    iblockstate = Blocks.lava.getDefaultState();
                                }
                                if (d16 > 0.0) {
                                    iblockstate = Blocks.netherrack.getDefaultState();
                                }
                                final int l3 = j3 + j2 * 4;
                                final int i4 = i3 + l2 * 8;
                                final int j4 = k3 + k2 * 4;
                                p_180515_3_.setBlockState(l3, i4, j4, iblockstate);
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
    
    public void func_180516_b(final int p_180516_1_, final int p_180516_2_, final ChunkPrimer p_180516_3_) {
        final int i = this.worldObj.func_181545_F() + 1;
        final double d0 = 0.03125;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0, d0, 1.0);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_180516_1_ * 16, 109, p_180516_2_ * 16, 16, 1, 16, d0, 1.0, d0);
        this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0 * 2.0, d0 * 2.0, d0 * 2.0);
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                final boolean flag = this.slowsandNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                final boolean flag2 = this.gravelNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                final int l = (int)(this.netherrackExclusivityNoise[j + k * 16] / 3.0 + 3.0 + this.hellRNG.nextDouble() * 0.25);
                int i2 = -1;
                IBlockState iblockstate = Blocks.netherrack.getDefaultState();
                IBlockState iblockstate2 = Blocks.netherrack.getDefaultState();
                for (int j2 = 127; j2 >= 0; --j2) {
                    if (j2 < 127 - this.hellRNG.nextInt(5) && j2 > this.hellRNG.nextInt(5)) {
                        final IBlockState iblockstate3 = p_180516_3_.getBlockState(k, j2, j);
                        if (iblockstate3.getBlock() != null && iblockstate3.getBlock().getMaterial() != Material.air) {
                            if (iblockstate3.getBlock() == Blocks.netherrack) {
                                if (i2 == -1) {
                                    if (l <= 0) {
                                        iblockstate = null;
                                        iblockstate2 = Blocks.netherrack.getDefaultState();
                                    }
                                    else if (j2 >= i - 4 && j2 <= i + 1) {
                                        iblockstate = Blocks.netherrack.getDefaultState();
                                        iblockstate2 = Blocks.netherrack.getDefaultState();
                                        if (flag2) {
                                            iblockstate = Blocks.gravel.getDefaultState();
                                            iblockstate2 = Blocks.netherrack.getDefaultState();
                                        }
                                        if (flag) {
                                            iblockstate = Blocks.soul_sand.getDefaultState();
                                            iblockstate2 = Blocks.soul_sand.getDefaultState();
                                        }
                                    }
                                    if (j2 < i && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air)) {
                                        iblockstate = Blocks.lava.getDefaultState();
                                    }
                                    i2 = l;
                                    if (j2 >= i - 1) {
                                        p_180516_3_.setBlockState(k, j2, j, iblockstate);
                                    }
                                    else {
                                        p_180516_3_.setBlockState(k, j2, j, iblockstate2);
                                    }
                                }
                                else if (i2 > 0) {
                                    --i2;
                                    p_180516_3_.setBlockState(k, j2, j, iblockstate2);
                                }
                            }
                        }
                        else {
                            i2 = -1;
                        }
                    }
                    else {
                        p_180516_3_.setBlockState(k, j2, j, Blocks.bedrock.getDefaultState());
                    }
                }
            }
        }
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        this.hellRNG.setSeed(x * 341873128712L + z * 132897987541L);
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        this.func_180515_a(x, z, chunkprimer);
        this.func_180516_b(x, z, chunkprimer);
        this.netherCaveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        if (this.field_177466_i) {
            this.genNetherBridge.generate(this, this.worldObj, x, z, chunkprimer);
        }
        final Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        final BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)abiomegenbase[i].biomeID;
        }
        chunk.resetRelightChecks();
        return chunk;
    }
    
    private double[] initializeNoiseField(double[] p_73164_1_, final int p_73164_2_, final int p_73164_3_, final int p_73164_4_, final int p_73164_5_, final int p_73164_6_, final int p_73164_7_) {
        if (p_73164_1_ == null) {
            p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
        }
        final double d0 = 684.412;
        final double d2 = 2053.236;
        this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 1.0, 0.0, 1.0);
        this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 100.0, 0.0, 100.0);
        this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0 / 80.0, d2 / 60.0, d0 / 80.0);
        this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d2, d0);
        this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d2, d0);
        int i = 0;
        final double[] adouble = new double[p_73164_6_];
        for (int j = 0; j < p_73164_6_; ++j) {
            adouble[j] = Math.cos(j * 3.141592653589793 * 6.0 / p_73164_6_) * 2.0;
            double d3 = j;
            if (j > p_73164_6_ / 2) {
                d3 = p_73164_6_ - 1 - j;
            }
            if (d3 < 4.0) {
                d3 = 4.0 - d3;
                final double[] array = adouble;
                final int n = j;
                array[n] -= d3 * d3 * d3 * 10.0;
            }
        }
        for (int l = 0; l < p_73164_5_; ++l) {
            for (int i2 = 0; i2 < p_73164_7_; ++i2) {
                final double d4 = 0.0;
                for (int k = 0; k < p_73164_6_; ++k) {
                    double d5 = 0.0;
                    final double d6 = adouble[k];
                    final double d7 = this.noiseData2[i] / 512.0;
                    final double d8 = this.noiseData3[i] / 512.0;
                    final double d9 = (this.noiseData1[i] / 10.0 + 1.0) / 2.0;
                    if (d9 < 0.0) {
                        d5 = d7;
                    }
                    else if (d9 > 1.0) {
                        d5 = d8;
                    }
                    else {
                        d5 = d7 + (d8 - d7) * d9;
                    }
                    d5 -= d6;
                    if (k > p_73164_6_ - 4) {
                        final double d10 = (k - (p_73164_6_ - 4)) / 3.0f;
                        d5 = d5 * (1.0 - d10) + -10.0 * d10;
                    }
                    if (k < d4) {
                        double d11 = (d4 - k) / 4.0;
                        d11 = MathHelper.clamp_double(d11, 0.0, 1.0);
                        d5 = d5 * (1.0 - d11) + -10.0 * d11;
                    }
                    p_73164_1_[i] = d5;
                    ++i;
                }
            }
        }
        return p_73164_1_;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        final BlockPos blockpos = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        final ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        this.genNetherBridge.generateStructure(this.worldObj, this.hellRNG, chunkcoordintpair);
        for (int i = 0; i < 8; ++i) {
            this.field_177472_y.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int j = 0; j < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1; ++j) {
            this.field_177470_t.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int k = 0; k < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1); ++k) {
            this.field_177469_u.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int l = 0; l < 10; ++l) {
            this.field_177468_v.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177471_z.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177465_A.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        for (int i2 = 0; i2 < 16; ++i2) {
            this.field_177467_w.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
        }
        for (int j2 = 0; j2 < 16; ++j2) {
            this.field_177473_x.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
        }
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
        return "HellRandomLevelSource";
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.func_175795_b(pos)) {
                return this.genNetherBridge.getSpawnList();
            }
            if (this.genNetherBridge.func_175796_a(this.worldObj, pos) && this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.nether_brick) {
                return this.genNetherBridge.getSpawnList();
            }
        }
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
        return biomegenbase.getSpawnableList(creatureType);
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
        this.genNetherBridge.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
