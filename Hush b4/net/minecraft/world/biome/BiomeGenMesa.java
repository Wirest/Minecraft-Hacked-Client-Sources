// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Arrays;
import net.minecraft.block.BlockDirt;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.block.state.IBlockState;

public class BiomeGenMesa extends BiomeGenBase
{
    private IBlockState[] field_150621_aC;
    private long field_150622_aD;
    private NoiseGeneratorPerlin field_150623_aE;
    private NoiseGeneratorPerlin field_150624_aF;
    private NoiseGeneratorPerlin field_150625_aG;
    private boolean field_150626_aH;
    private boolean field_150620_aI;
    
    public BiomeGenMesa(final int p_i45380_1_, final boolean p_i45380_2_, final boolean p_i45380_3_) {
        super(p_i45380_1_);
        this.field_150626_aH = p_i45380_2_;
        this.field_150620_aI = p_i45380_3_;
        this.setDisableRain();
        this.setTemperatureRainfall(2.0f, 0.0f);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
        this.fillerBlock = Blocks.stained_hardened_clay.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 20;
        this.theBiomeDecorator.reedsPerChunk = 3;
        this.theBiomeDecorator.cactiPerChunk = 5;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        if (p_i45380_3_) {
            this.theBiomeDecorator.treesPerChunk = 5;
        }
    }
    
    @Override
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return this.worldGeneratorTrees;
    }
    
    @Override
    public int getFoliageColorAtPos(final BlockPos pos) {
        return 10387789;
    }
    
    @Override
    public int getGrassColorAtPos(final BlockPos pos) {
        return 9470285;
    }
    
    @Override
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        super.decorate(worldIn, rand, pos);
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
        if (this.field_150621_aC == null || this.field_150622_aD != worldIn.getSeed()) {
            this.func_150619_a(worldIn.getSeed());
        }
        if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != worldIn.getSeed()) {
            final Random random = new Random(this.field_150622_aD);
            this.field_150623_aE = new NoiseGeneratorPerlin(random, 4);
            this.field_150624_aF = new NoiseGeneratorPerlin(random, 1);
        }
        this.field_150622_aD = worldIn.getSeed();
        double d4 = 0.0;
        if (this.field_150626_aH) {
            final int i = (p_180622_4_ & 0xFFFFFFF0) + (p_180622_5_ & 0xF);
            final int j = (p_180622_5_ & 0xFFFFFFF0) + (p_180622_4_ & 0xF);
            final double d5 = Math.min(Math.abs(p_180622_6_), this.field_150623_aE.func_151601_a(i * 0.25, j * 0.25));
            if (d5 > 0.0) {
                final double d6 = 0.001953125;
                final double d7 = Math.abs(this.field_150624_aF.func_151601_a(i * d6, j * d6));
                d4 = d5 * d5 * 2.5;
                final double d8 = Math.ceil(d7 * 50.0) + 14.0;
                if (d4 > d8) {
                    d4 = d8;
                }
                d4 += 64.0;
            }
        }
        final int j2 = p_180622_4_ & 0xF;
        final int k1 = p_180622_5_ & 0xF;
        final int l1 = worldIn.func_181545_F();
        IBlockState iblockstate = Blocks.stained_hardened_clay.getDefaultState();
        IBlockState iblockstate2 = this.fillerBlock;
        final int m = (int)(p_180622_6_ / 3.0 + 3.0 + rand.nextDouble() * 0.25);
        final boolean flag = Math.cos(p_180622_6_ / 3.0 * 3.141592653589793) > 0.0;
        int l2 = -1;
        boolean flag2 = false;
        for (int i2 = 255; i2 >= 0; --i2) {
            if (chunkPrimerIn.getBlockState(k1, i2, j2).getBlock().getMaterial() == Material.air && i2 < (int)d4) {
                chunkPrimerIn.setBlockState(k1, i2, j2, Blocks.stone.getDefaultState());
            }
            if (i2 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(k1, i2, j2, Blocks.bedrock.getDefaultState());
            }
            else {
                final IBlockState iblockstate3 = chunkPrimerIn.getBlockState(k1, i2, j2);
                if (iblockstate3.getBlock().getMaterial() == Material.air) {
                    l2 = -1;
                }
                else if (iblockstate3.getBlock() == Blocks.stone) {
                    if (l2 == -1) {
                        flag2 = false;
                        if (m <= 0) {
                            iblockstate = null;
                            iblockstate2 = Blocks.stone.getDefaultState();
                        }
                        else if (i2 >= l1 - 4 && i2 <= l1 + 1) {
                            iblockstate = Blocks.stained_hardened_clay.getDefaultState();
                            iblockstate2 = this.fillerBlock;
                        }
                        if (i2 < l1 && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air)) {
                            iblockstate = Blocks.water.getDefaultState();
                        }
                        l2 = m + Math.max(0, i2 - l1);
                        if (i2 < l1 - 1) {
                            chunkPrimerIn.setBlockState(k1, i2, j2, iblockstate2);
                            if (iblockstate2.getBlock() == Blocks.stained_hardened_clay) {
                                chunkPrimerIn.setBlockState(k1, i2, j2, iblockstate2.getBlock().getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                            }
                        }
                        else if (this.field_150620_aI && i2 > 86 + m * 2) {
                            if (flag) {
                                chunkPrimerIn.setBlockState(k1, i2, j2, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
                            }
                            else {
                                chunkPrimerIn.setBlockState(k1, i2, j2, Blocks.grass.getDefaultState());
                            }
                        }
                        else if (i2 <= l1 + 3 + m) {
                            chunkPrimerIn.setBlockState(k1, i2, j2, this.topBlock);
                            flag2 = true;
                        }
                        else {
                            IBlockState iblockstate4;
                            if (i2 >= 64 && i2 <= 127) {
                                if (flag) {
                                    iblockstate4 = Blocks.hardened_clay.getDefaultState();
                                }
                                else {
                                    iblockstate4 = this.func_180629_a(p_180622_4_, i2, p_180622_5_);
                                }
                            }
                            else {
                                iblockstate4 = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
                            }
                            chunkPrimerIn.setBlockState(k1, i2, j2, iblockstate4);
                        }
                    }
                    else if (l2 > 0) {
                        --l2;
                        if (flag2) {
                            chunkPrimerIn.setBlockState(k1, i2, j2, Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                        }
                        else {
                            final IBlockState iblockstate5 = this.func_180629_a(p_180622_4_, i2, p_180622_5_);
                            chunkPrimerIn.setBlockState(k1, i2, j2, iblockstate5);
                        }
                    }
                }
            }
        }
    }
    
    private void func_150619_a(final long p_150619_1_) {
        Arrays.fill(this.field_150621_aC = new IBlockState[64], Blocks.hardened_clay.getDefaultState());
        final Random random = new Random(p_150619_1_);
        this.field_150625_aG = new NoiseGeneratorPerlin(random, 1);
        for (int l1 = 0; l1 < 64; ++l1) {
            l1 += random.nextInt(5) + 1;
            if (l1 < 64) {
                this.field_150621_aC[l1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
            }
        }
        for (int i2 = random.nextInt(4) + 2, j = 0; j < i2; ++j) {
            for (int k = random.nextInt(3) + 1, m = random.nextInt(64), l2 = 0; m + l2 < 64 && l2 < k; ++l2) {
                this.field_150621_aC[m + l2] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
            }
        }
        for (int j2 = random.nextInt(4) + 2, k2 = 0; k2 < j2; ++k2) {
            for (int i3 = random.nextInt(3) + 2, l3 = random.nextInt(64), i4 = 0; l3 + i4 < 64 && i4 < i3; ++i4) {
                this.field_150621_aC[l3 + i4] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
            }
        }
        for (int l4 = random.nextInt(4) + 2, j3 = 0; j3 < l4; ++j3) {
            for (int i5 = random.nextInt(3) + 1, k3 = random.nextInt(64), j4 = 0; k3 + j4 < 64 && j4 < i5; ++j4) {
                this.field_150621_aC[k3 + j4] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
            }
        }
        final int k4 = random.nextInt(3) + 3;
        int j5 = 0;
        for (int l5 = 0; l5 < k4; ++l5) {
            final int i6 = 1;
            j5 += random.nextInt(16) + 4;
            for (int k5 = 0; j5 + k5 < 64 && k5 < i6; ++k5) {
                this.field_150621_aC[j5 + k5] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
                if (j5 + k5 > 1 && random.nextBoolean()) {
                    this.field_150621_aC[j5 + k5 - 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
                if (j5 + k5 < 63 && random.nextBoolean()) {
                    this.field_150621_aC[j5 + k5 + 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
            }
        }
    }
    
    private IBlockState func_180629_a(final int p_180629_1_, final int p_180629_2_, final int p_180629_3_) {
        final int i = (int)Math.round(this.field_150625_aG.func_151601_a(p_180629_1_ * 1.0 / 512.0, p_180629_1_ * 1.0 / 512.0) * 2.0);
        return this.field_150621_aC[(p_180629_2_ + i + 64) % 64];
    }
    
    @Override
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        final boolean flag = this.biomeID == BiomeGenBase.mesa.biomeID;
        final BiomeGenMesa biomegenmesa = new BiomeGenMesa(p_180277_1_, flag, this.field_150620_aI);
        if (!flag) {
            biomegenmesa.setHeight(BiomeGenMesa.height_LowHills);
            biomegenmesa.setBiomeName(String.valueOf(this.biomeName) + " M");
        }
        else {
            biomegenmesa.setBiomeName(String.valueOf(this.biomeName) + " (Bryce)");
        }
        biomegenmesa.func_150557_a(this.color, true);
        return biomegenmesa;
    }
}
