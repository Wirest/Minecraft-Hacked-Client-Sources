// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldChunkManager
{
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private BiomeCache biomeCache;
    private List<BiomeGenBase> biomesToSpawnIn;
    private String field_180301_f;
    
    protected WorldChunkManager() {
        this.biomeCache = new BiomeCache(this);
        this.field_180301_f = "";
        (this.biomesToSpawnIn = (List<BiomeGenBase>)Lists.newArrayList()).add(BiomeGenBase.forest);
        this.biomesToSpawnIn.add(BiomeGenBase.plains);
        this.biomesToSpawnIn.add(BiomeGenBase.taiga);
        this.biomesToSpawnIn.add(BiomeGenBase.taigaHills);
        this.biomesToSpawnIn.add(BiomeGenBase.forestHills);
        this.biomesToSpawnIn.add(BiomeGenBase.jungle);
        this.biomesToSpawnIn.add(BiomeGenBase.jungleHills);
    }
    
    public WorldChunkManager(final long seed, final WorldType p_i45744_3_, final String p_i45744_4_) {
        this();
        this.field_180301_f = p_i45744_4_;
        final GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(seed, p_i45744_3_, p_i45744_4_);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
    }
    
    public WorldChunkManager(final World worldIn) {
        this(worldIn.getSeed(), worldIn.getWorldInfo().getTerrainType(), worldIn.getWorldInfo().getGeneratorOptions());
    }
    
    public List<BiomeGenBase> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }
    
    public BiomeGenBase getBiomeGenerator(final BlockPos pos) {
        return this.getBiomeGenerator(pos, null);
    }
    
    public BiomeGenBase getBiomeGenerator(final BlockPos pos, final BiomeGenBase biomeGenBaseIn) {
        return this.biomeCache.func_180284_a(pos.getX(), pos.getZ(), biomeGenBaseIn);
    }
    
    public float[] getRainfall(float[] listToReuse, final int x, final int z, final int width, final int length) {
        IntCache.resetIntCache();
        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new float[width * length];
        }
        final int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);
        for (int i = 0; i < width * length; ++i) {
            try {
                float f = BiomeGenBase.getBiomeFromBiomeList(aint[i], BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0f;
                if (f > 1.0f) {
                    f = 1.0f;
                }
                listToReuse[i] = f;
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                crashreportcategory.addCrashSection("biome id", i);
                crashreportcategory.addCrashSection("downfalls[] size", listToReuse.length);
                crashreportcategory.addCrashSection("x", x);
                crashreportcategory.addCrashSection("z", z);
                crashreportcategory.addCrashSection("w", width);
                crashreportcategory.addCrashSection("h", length);
                throw new ReportedException(crashreport);
            }
        }
        return listToReuse;
    }
    
    public float getTemperatureAtHeight(final float p_76939_1_, final int p_76939_2_) {
        return p_76939_1_;
    }
    
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, final int x, final int z, final int width, final int height) {
        IntCache.resetIntCache();
        if (biomes == null || biomes.length < width * height) {
            biomes = new BiomeGenBase[width * height];
        }
        final int[] aint = this.genBiomes.getInts(x, z, width, height);
        try {
            for (int i = 0; i < width * height; ++i) {
                biomes[i] = BiomeGenBase.getBiomeFromBiomeList(aint[i], BiomeGenBase.field_180279_ad);
            }
            return biomes;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", biomes.length);
            crashreportcategory.addCrashSection("x", x);
            crashreportcategory.addCrashSection("z", z);
            crashreportcategory.addCrashSection("w", width);
            crashreportcategory.addCrashSection("h", height);
            throw new ReportedException(crashreport);
        }
    }
    
    public BiomeGenBase[] loadBlockGeneratorData(final BiomeGenBase[] oldBiomeList, final int x, final int z, final int width, final int depth) {
        return this.getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
    }
    
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, final int x, final int z, final int width, final int length, final boolean cacheFlag) {
        IntCache.resetIntCache();
        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new BiomeGenBase[width * length];
        }
        if (cacheFlag && width == 16 && length == 16 && (x & 0xF) == 0x0 && (z & 0xF) == 0x0) {
            final BiomeGenBase[] abiomegenbase = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiomegenbase, 0, listToReuse, 0, width * length);
            return listToReuse;
        }
        final int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);
        for (int i = 0; i < width * length; ++i) {
            listToReuse[i] = BiomeGenBase.getBiomeFromBiomeList(aint[i], BiomeGenBase.field_180279_ad);
        }
        return listToReuse;
    }
    
    public boolean areBiomesViable(final int p_76940_1_, final int p_76940_2_, final int p_76940_3_, final List<BiomeGenBase> p_76940_4_) {
        IntCache.resetIntCache();
        final int i = p_76940_1_ - p_76940_3_ >> 2;
        final int j = p_76940_2_ - p_76940_3_ >> 2;
        final int k = p_76940_1_ + p_76940_3_ >> 2;
        final int l = p_76940_2_ + p_76940_3_ >> 2;
        final int i2 = k - i + 1;
        final int j2 = l - j + 1;
        final int[] aint = this.genBiomes.getInts(i, j, i2, j2);
        try {
            for (int k2 = 0; k2 < i2 * j2; ++k2) {
                final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k2]);
                if (!p_76940_4_.contains(biomegenbase)) {
                    return false;
                }
            }
            return true;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", p_76940_1_);
            crashreportcategory.addCrashSection("z", p_76940_2_);
            crashreportcategory.addCrashSection("radius", p_76940_3_);
            crashreportcategory.addCrashSection("allowed", p_76940_4_);
            throw new ReportedException(crashreport);
        }
    }
    
    public BlockPos findBiomePosition(final int x, final int z, final int range, final List<BiomeGenBase> biomes, final Random random) {
        IntCache.resetIntCache();
        final int i = x - range >> 2;
        final int j = z - range >> 2;
        final int k = x + range >> 2;
        final int l = z + range >> 2;
        final int i2 = k - i + 1;
        final int j2 = l - j + 1;
        final int[] aint = this.genBiomes.getInts(i, j, i2, j2);
        BlockPos blockpos = null;
        int k2 = 0;
        for (int l2 = 0; l2 < i2 * j2; ++l2) {
            final int i3 = i + l2 % i2 << 2;
            final int j3 = j + l2 / i2 << 2;
            final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[l2]);
            if (biomes.contains(biomegenbase) && (blockpos == null || random.nextInt(k2 + 1) == 0)) {
                blockpos = new BlockPos(i3, 0, j3);
                ++k2;
            }
        }
        return blockpos;
    }
    
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }
}
