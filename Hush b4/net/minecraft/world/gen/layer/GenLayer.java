// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.WorldType;

public abstract class GenLayer
{
    private long worldGenSeed;
    protected GenLayer parent;
    private long chunkSeed;
    protected long baseSeed;
    
    public static GenLayer[] initializeAllBiomeGenerators(final long seed, final WorldType p_180781_2_, final String p_180781_3_) {
        GenLayer genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        final GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
        final GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        GenLayerAddIsland genlayeraddisland2 = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland2 = new GenLayerAddIsland(50L, genlayeraddisland2);
        genlayeraddisland2 = new GenLayerAddIsland(70L, genlayeraddisland2);
        final GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland2);
        final GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        final GenLayerAddIsland genlayeraddisland3 = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland3, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayerZoom genlayerzoom2 = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom2 = new GenLayerZoom(2003L, genlayerzoom2);
        final GenLayerAddIsland genlayeraddisland4 = new GenLayerAddIsland(4L, genlayerzoom2);
        final GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland4);
        final GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        final GenLayer genlayer2 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
        ChunkProviderSettings chunkprovidersettings = null;
        int j;
        int i = j = 4;
        if (p_180781_2_ == WorldType.CUSTOMIZED && p_180781_3_.length() > 0) {
            chunkprovidersettings = ChunkProviderSettings.Factory.jsonToFactory(p_180781_3_).func_177864_b();
            i = chunkprovidersettings.biomeSize;
            j = chunkprovidersettings.riverSize;
        }
        if (p_180781_2_ == WorldType.LARGE_BIOMES) {
            i = 6;
        }
        final GenLayer lvt_8_1_ = GenLayerZoom.magnify(1000L, genlayer2, 0);
        final GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, lvt_8_1_);
        final GenLayerBiome lvt_9_1_ = new GenLayerBiome(200L, genlayer2, p_180781_2_, p_180781_3_);
        final GenLayer genlayer3 = GenLayerZoom.magnify(1000L, lvt_9_1_, 2);
        final GenLayerBiomeEdge genlayerbiomeedge = new GenLayerBiomeEdge(1000L, genlayer3);
        final GenLayer lvt_10_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_10_1_);
        GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer4 = GenLayerZoom.magnify(1000L, genlayer4, j);
        final GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer4);
        final GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);
        for (int k = 0; k < i; ++k) {
            genlayerhills = new GenLayerZoom(1000 + k, genlayerhills);
            if (k == 0) {
                genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
            }
            if (k == 1 || i == 1) {
                genlayerhills = new GenLayerShore(1000L, genlayerhills);
            }
        }
        final GenLayerSmooth genlayersmooth2 = new GenLayerSmooth(1000L, genlayerhills);
        final GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth2, genlayersmooth);
        final GenLayer genlayer5 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(seed);
        genlayer5.initWorldGenSeed(seed);
        return new GenLayer[] { genlayerrivermix, genlayer5, genlayerrivermix };
    }
    
    public GenLayer(final long p_i2125_1_) {
        this.baseSeed = p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
    }
    
    public void initWorldGenSeed(final long seed) {
        this.worldGenSeed = seed;
        if (this.parent != null) {
            this.parent.initWorldGenSeed(seed);
        }
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }
    
    public void initChunkSeed(final long p_75903_1_, final long p_75903_3_) {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
    }
    
    protected int nextInt(final int p_75902_1_) {
        int i = (int)((this.chunkSeed >> 24) % p_75902_1_);
        if (i < 0) {
            i += p_75902_1_;
        }
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return i;
    }
    
    public abstract int[] getInts(final int p0, final int p1, final int p2, final int p3);
    
    protected static boolean biomesEqualOrMesaPlateau(final int biomeIDA, final int biomeIDB) {
        if (biomeIDA == biomeIDB) {
            return true;
        }
        if (biomeIDA != BiomeGenBase.mesaPlateau_F.biomeID && biomeIDA != BiomeGenBase.mesaPlateau.biomeID) {
            final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(biomeIDA);
            final BiomeGenBase biomegenbase2 = BiomeGenBase.getBiome(biomeIDB);
            try {
                return biomegenbase != null && biomegenbase2 != null && biomegenbase.isEqualTo(biomegenbase2);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
                crashreportcategory.addCrashSection("Biome A ID", biomeIDA);
                crashreportcategory.addCrashSection("Biome B ID", biomeIDB);
                crashreportcategory.addCrashSectionCallable("Biome A", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(biomegenbase);
                    }
                });
                crashreportcategory.addCrashSectionCallable("Biome B", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(biomegenbase2);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        return biomeIDB == BiomeGenBase.mesaPlateau_F.biomeID || biomeIDB == BiomeGenBase.mesaPlateau.biomeID;
    }
    
    protected static boolean isBiomeOceanic(final int p_151618_0_) {
        return p_151618_0_ == BiomeGenBase.ocean.biomeID || p_151618_0_ == BiomeGenBase.deepOcean.biomeID || p_151618_0_ == BiomeGenBase.frozenOcean.biomeID;
    }
    
    protected int selectRandom(final int... p_151619_1_) {
        return p_151619_1_[this.nextInt(p_151619_1_.length)];
    }
    
    protected int selectModeOrRandom(final int p_151617_1_, final int p_151617_2_, final int p_151617_3_, final int p_151617_4_) {
        return (p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_) ? p_151617_2_ : ((p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_) ? p_151617_1_ : ((p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_) ? p_151617_1_ : ((p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_) ? p_151617_2_ : ((p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_) ? p_151617_2_ : ((p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_) ? p_151617_3_ : this.selectRandom(p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_))))))))));
    }
}
