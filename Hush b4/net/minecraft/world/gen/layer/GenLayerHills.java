// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerHills extends GenLayer
{
    private static final Logger logger;
    private GenLayer field_151628_d;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public GenLayerHills(final long p_i45479_1_, final GenLayer p_i45479_3_, final GenLayer p_i45479_4_) {
        super(p_i45479_1_);
        this.parent = p_i45479_3_;
        this.field_151628_d = p_i45479_4_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint2 = this.field_151628_d.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint3 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                final int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                final int l = aint2[j + 1 + (i + 1) * (areaWidth + 2)];
                final boolean flag = (l - 2) % 29 == 0;
                if (k > 255) {
                    GenLayerHills.logger.debug("old! " + k);
                }
                if (k != 0 && l >= 2 && (l - 2) % 29 == 1 && k < 128) {
                    if (BiomeGenBase.getBiome(k + 128) != null) {
                        aint3[j + i * areaWidth] = k + 128;
                    }
                    else {
                        aint3[j + i * areaWidth] = k;
                    }
                }
                else if (this.nextInt(3) != 0 && !flag) {
                    aint3[j + i * areaWidth] = k;
                }
                else {
                    int i2;
                    if ((i2 = k) == BiomeGenBase.desert.biomeID) {
                        i2 = BiomeGenBase.desertHills.biomeID;
                    }
                    else if (k == BiomeGenBase.forest.biomeID) {
                        i2 = BiomeGenBase.forestHills.biomeID;
                    }
                    else if (k == BiomeGenBase.birchForest.biomeID) {
                        i2 = BiomeGenBase.birchForestHills.biomeID;
                    }
                    else if (k == BiomeGenBase.roofedForest.biomeID) {
                        i2 = BiomeGenBase.plains.biomeID;
                    }
                    else if (k == BiomeGenBase.taiga.biomeID) {
                        i2 = BiomeGenBase.taigaHills.biomeID;
                    }
                    else if (k == BiomeGenBase.megaTaiga.biomeID) {
                        i2 = BiomeGenBase.megaTaigaHills.biomeID;
                    }
                    else if (k == BiomeGenBase.coldTaiga.biomeID) {
                        i2 = BiomeGenBase.coldTaigaHills.biomeID;
                    }
                    else if (k == BiomeGenBase.plains.biomeID) {
                        if (this.nextInt(3) == 0) {
                            i2 = BiomeGenBase.forestHills.biomeID;
                        }
                        else {
                            i2 = BiomeGenBase.forest.biomeID;
                        }
                    }
                    else if (k == BiomeGenBase.icePlains.biomeID) {
                        i2 = BiomeGenBase.iceMountains.biomeID;
                    }
                    else if (k == BiomeGenBase.jungle.biomeID) {
                        i2 = BiomeGenBase.jungleHills.biomeID;
                    }
                    else if (k == BiomeGenBase.ocean.biomeID) {
                        i2 = BiomeGenBase.deepOcean.biomeID;
                    }
                    else if (k == BiomeGenBase.extremeHills.biomeID) {
                        i2 = BiomeGenBase.extremeHillsPlus.biomeID;
                    }
                    else if (k == BiomeGenBase.savanna.biomeID) {
                        i2 = BiomeGenBase.savannaPlateau.biomeID;
                    }
                    else if (GenLayer.biomesEqualOrMesaPlateau(k, BiomeGenBase.mesaPlateau_F.biomeID)) {
                        i2 = BiomeGenBase.mesa.biomeID;
                    }
                    else if (k == BiomeGenBase.deepOcean.biomeID && this.nextInt(3) == 0) {
                        final int j2 = this.nextInt(2);
                        if (j2 == 0) {
                            i2 = BiomeGenBase.plains.biomeID;
                        }
                        else {
                            i2 = BiomeGenBase.forest.biomeID;
                        }
                    }
                    if (flag && i2 != k) {
                        if (BiomeGenBase.getBiome(i2 + 128) != null) {
                            i2 += 128;
                        }
                        else {
                            i2 = k;
                        }
                    }
                    if (i2 == k) {
                        aint3[j + i * areaWidth] = k;
                    }
                    else {
                        final int k2 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        final int k3 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        final int l2 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        final int i3 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                        int j3 = 0;
                        if (GenLayer.biomesEqualOrMesaPlateau(k2, k)) {
                            ++j3;
                        }
                        if (GenLayer.biomesEqualOrMesaPlateau(k3, k)) {
                            ++j3;
                        }
                        if (GenLayer.biomesEqualOrMesaPlateau(l2, k)) {
                            ++j3;
                        }
                        if (GenLayer.biomesEqualOrMesaPlateau(i3, k)) {
                            ++j3;
                        }
                        if (j3 >= 3) {
                            aint3[j + i * areaWidth] = i2;
                        }
                        else {
                            aint3[j + i * areaWidth] = k;
                        }
                    }
                }
            }
        }
        return aint3;
    }
}
