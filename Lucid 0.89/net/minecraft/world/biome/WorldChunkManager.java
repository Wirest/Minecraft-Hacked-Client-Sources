package net.minecraft.world.biome;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class WorldChunkManager
{
    private GenLayer genBiomes;

    /** A GenLayer containing the indices into BiomeGenBase.biomeList[] */
    private GenLayer biomeIndexLayer;

    /** The biome list. */
    private BiomeCache biomeCache;

    /** A list of biomes that the player can spawn in. */
    private List biomesToSpawnIn;
    protected WorldChunkManager()
    {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = Lists.newArrayList();
        this.biomesToSpawnIn.add(BiomeGenBase.forest);
        this.biomesToSpawnIn.add(BiomeGenBase.plains);
        this.biomesToSpawnIn.add(BiomeGenBase.taiga);
        this.biomesToSpawnIn.add(BiomeGenBase.taigaHills);
        this.biomesToSpawnIn.add(BiomeGenBase.forestHills);
        this.biomesToSpawnIn.add(BiomeGenBase.jungle);
        this.biomesToSpawnIn.add(BiomeGenBase.jungleHills);
    }

    public WorldChunkManager(long p_i45744_1_, WorldType p_i45744_3_, String p_i45744_4_)
    {
        this();
        GenLayer[] var5 = GenLayer.initializeAllBiomeGenerators(p_i45744_1_, p_i45744_3_, p_i45744_4_);
        this.genBiomes = var5[0];
        this.biomeIndexLayer = var5[1];
    }

    public WorldChunkManager(World worldIn)
    {
        this(worldIn.getSeed(), worldIn.getWorldInfo().getTerrainType(), worldIn.getWorldInfo().getGeneratorOptions());
    }

    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
    public List getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

    /**
     * Returns the biome generator
     */
    public BiomeGenBase getBiomeGenerator(BlockPos p_180631_1_)
    {
        return this.func_180300_a(p_180631_1_, (BiomeGenBase)null);
    }

    public BiomeGenBase func_180300_a(BlockPos p_180300_1_, BiomeGenBase p_180300_2_)
    {
        return this.biomeCache.func_180284_a(p_180300_1_.getX(), p_180300_1_.getZ(), p_180300_2_);
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length)
    {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new float[width * length];
        }

        int[] var6 = this.biomeIndexLayer.getInts(x, z, width, length);

        for (int var7 = 0; var7 < width * length; ++var7)
        {
            try
            {
                float var8 = BiomeGenBase.getBiomeFromBiomeList(var6[var7], BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0F;

                if (var8 > 1.0F)
                {
                    var8 = 1.0F;
                }

                listToReuse[var7] = var8;
            }
            catch (Throwable var11)
            {
                CrashReport var9 = CrashReport.makeCrashReport(var11, "Invalid Biome id");
                CrashReportCategory var10 = var9.makeCategory("DownfallBlock");
                var10.addCrashSection("biome id", Integer.valueOf(var7));
                var10.addCrashSection("downfalls[] size", Integer.valueOf(listToReuse.length));
                var10.addCrashSection("x", Integer.valueOf(x));
                var10.addCrashSection("z", Integer.valueOf(z));
                var10.addCrashSection("w", Integer.valueOf(width));
                var10.addCrashSection("h", Integer.valueOf(length));
                throw new ReportedException(var9);
            }
        }

        return listToReuse;
    }

    /**
     * Return an adjusted version of a given temperature based on the y height
     */
    public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_)
    {
        return p_76939_1_;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] p_76937_1_, int p_76937_2_, int p_76937_3_, int p_76937_4_, int p_76937_5_)
    {
        IntCache.resetIntCache();

        if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_)
        {
            p_76937_1_ = new BiomeGenBase[p_76937_4_ * p_76937_5_];
        }

        int[] var6 = this.genBiomes.getInts(p_76937_2_, p_76937_3_, p_76937_4_, p_76937_5_);

        try
        {
            for (int var7 = 0; var7 < p_76937_4_ * p_76937_5_; ++var7)
            {
                p_76937_1_[var7] = BiomeGenBase.getBiomeFromBiomeList(var6[var7], BiomeGenBase.field_180279_ad);
            }

            return p_76937_1_;
        }
        catch (Throwable var10)
        {
            CrashReport var8 = CrashReport.makeCrashReport(var10, "Invalid Biome id");
            CrashReportCategory var9 = var8.makeCategory("RawBiomeBlock");
            var9.addCrashSection("biomes[] size", Integer.valueOf(p_76937_1_.length));
            var9.addCrashSection("x", Integer.valueOf(p_76937_2_));
            var9.addCrashSection("z", Integer.valueOf(p_76937_3_));
            var9.addCrashSection("w", Integer.valueOf(p_76937_4_));
            var9.addCrashSection("h", Integer.valueOf(p_76937_5_));
            throw new ReportedException(var8);
        }
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth)
    {
        return this.getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     *  
     * @param cacheFlag If false, don't check biomeCache to avoid infinite loop in BiomeCacheBlock
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new BiomeGenBase[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
        {
            BiomeGenBase[] var9 = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(var9, 0, listToReuse, 0, width * length);
            return listToReuse;
        }
        else
        {
            int[] var7 = this.biomeIndexLayer.getInts(x, z, width, length);

            for (int var8 = 0; var8 < width * length; ++var8)
            {
                listToReuse[var8] = BiomeGenBase.getBiomeFromBiomeList(var7[var8], BiomeGenBase.field_180279_ad);
            }

            return listToReuse;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int p_76940_1_, int p_76940_2_, int p_76940_3_, List p_76940_4_)
    {
        IntCache.resetIntCache();
        int var5 = p_76940_1_ - p_76940_3_ >> 2;
        int var6 = p_76940_2_ - p_76940_3_ >> 2;
        int var7 = p_76940_1_ + p_76940_3_ >> 2;
        int var8 = p_76940_2_ + p_76940_3_ >> 2;
        int var9 = var7 - var5 + 1;
        int var10 = var8 - var6 + 1;
        int[] var11 = this.genBiomes.getInts(var5, var6, var9, var10);

        try
        {
            for (int var12 = 0; var12 < var9 * var10; ++var12)
            {
                BiomeGenBase var16 = BiomeGenBase.getBiome(var11[var12]);

                if (!p_76940_4_.contains(var16))
                {
                    return false;
                }
            }

            return true;
        }
        catch (Throwable var15)
        {
            CrashReport var13 = CrashReport.makeCrashReport(var15, "Invalid Biome id");
            CrashReportCategory var14 = var13.makeCategory("Layer");
            var14.addCrashSection("Layer", this.genBiomes.toString());
            var14.addCrashSection("x", Integer.valueOf(p_76940_1_));
            var14.addCrashSection("z", Integer.valueOf(p_76940_2_));
            var14.addCrashSection("radius", Integer.valueOf(p_76940_3_));
            var14.addCrashSection("allowed", p_76940_4_);
            throw new ReportedException(var13);
        }
    }

    public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random)
    {
        IntCache.resetIntCache();
        int var6 = x - range >> 2;
        int var7 = z - range >> 2;
        int var8 = x + range >> 2;
        int var9 = z + range >> 2;
        int var10 = var8 - var6 + 1;
        int var11 = var9 - var7 + 1;
        int[] var12 = this.genBiomes.getInts(var6, var7, var10, var11);
        BlockPos var13 = null;
        int var14 = 0;

        for (int var15 = 0; var15 < var10 * var11; ++var15)
        {
            int var16 = var6 + var15 % var10 << 2;
            int var17 = var7 + var15 / var10 << 2;
            BiomeGenBase var18 = BiomeGenBase.getBiome(var12[var15]);

            if (biomes.contains(var18) && (var13 == null || random.nextInt(var14 + 1) == 0))
            {
                var13 = new BlockPos(var16, 0, var17);
                ++var14;
            }
        }

        return var13;
    }

    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
    public void cleanupCache()
    {
        this.biomeCache.cleanupCache();
    }
}
