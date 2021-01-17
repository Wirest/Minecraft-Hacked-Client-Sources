// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenMesa;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerShore extends GenLayer
{
    public GenLayerShore(final long p_i2130_1_, final GenLayer p_i2130_3_) {
        super(p_i2130_1_);
        this.parent = p_i2130_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                final int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(k);
                if (k == BiomeGenBase.mushroomIsland.biomeID) {
                    final int j2 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                    final int i2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                    final int l3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                    final int k2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                    if (j2 != BiomeGenBase.ocean.biomeID && i2 != BiomeGenBase.ocean.biomeID && l3 != BiomeGenBase.ocean.biomeID && k2 != BiomeGenBase.ocean.biomeID) {
                        aint2[j + i * areaWidth] = k;
                    }
                    else {
                        aint2[j + i * areaWidth] = BiomeGenBase.mushroomIslandShore.biomeID;
                    }
                }
                else if (biomegenbase != null && biomegenbase.getBiomeClass() == BiomeGenJungle.class) {
                    final int i3 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                    final int l4 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                    final int k3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                    final int j3 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                    if (this.func_151631_c(i3) && this.func_151631_c(l4) && this.func_151631_c(k3) && this.func_151631_c(j3)) {
                        if (!GenLayer.isBiomeOceanic(i3) && !GenLayer.isBiomeOceanic(l4) && !GenLayer.isBiomeOceanic(k3) && !GenLayer.isBiomeOceanic(j3)) {
                            aint2[j + i * areaWidth] = k;
                        }
                        else {
                            aint2[j + i * areaWidth] = BiomeGenBase.beach.biomeID;
                        }
                    }
                    else {
                        aint2[j + i * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
                    }
                }
                else if (k != BiomeGenBase.extremeHills.biomeID && k != BiomeGenBase.extremeHillsPlus.biomeID && k != BiomeGenBase.extremeHillsEdge.biomeID) {
                    if (biomegenbase != null && biomegenbase.isSnowyBiome()) {
                        this.func_151632_a(aint, aint2, j, i, areaWidth, k, BiomeGenBase.coldBeach.biomeID);
                    }
                    else if (k != BiomeGenBase.mesa.biomeID && k != BiomeGenBase.mesaPlateau_F.biomeID) {
                        if (k != BiomeGenBase.ocean.biomeID && k != BiomeGenBase.deepOcean.biomeID && k != BiomeGenBase.river.biomeID && k != BiomeGenBase.swampland.biomeID) {
                            final int l5 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                            final int k4 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                            final int j4 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                            final int i4 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                            if (!GenLayer.isBiomeOceanic(l5) && !GenLayer.isBiomeOceanic(k4) && !GenLayer.isBiomeOceanic(j4) && !GenLayer.isBiomeOceanic(i4)) {
                                aint2[j + i * areaWidth] = k;
                            }
                            else {
                                aint2[j + i * areaWidth] = BiomeGenBase.beach.biomeID;
                            }
                        }
                        else {
                            aint2[j + i * areaWidth] = k;
                        }
                    }
                    else {
                        final int m = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        final int i5 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        final int j5 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        final int k5 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                        if (!GenLayer.isBiomeOceanic(m) && !GenLayer.isBiomeOceanic(i5) && !GenLayer.isBiomeOceanic(j5) && !GenLayer.isBiomeOceanic(k5)) {
                            if (this.func_151633_d(m) && this.func_151633_d(i5) && this.func_151633_d(j5) && this.func_151633_d(k5)) {
                                aint2[j + i * areaWidth] = k;
                            }
                            else {
                                aint2[j + i * areaWidth] = BiomeGenBase.desert.biomeID;
                            }
                        }
                        else {
                            aint2[j + i * areaWidth] = k;
                        }
                    }
                }
                else {
                    this.func_151632_a(aint, aint2, j, i, areaWidth, k, BiomeGenBase.stoneBeach.biomeID);
                }
            }
        }
        return aint2;
    }
    
    private void func_151632_a(final int[] p_151632_1_, final int[] p_151632_2_, final int p_151632_3_, final int p_151632_4_, final int p_151632_5_, final int p_151632_6_, final int p_151632_7_) {
        if (GenLayer.isBiomeOceanic(p_151632_6_)) {
            p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
        }
        else {
            final int i = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
            final int j = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            final int k = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
            final int l = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];
            if (!GenLayer.isBiomeOceanic(i) && !GenLayer.isBiomeOceanic(j) && !GenLayer.isBiomeOceanic(k) && !GenLayer.isBiomeOceanic(l)) {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
            }
            else {
                p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_7_;
            }
        }
    }
    
    private boolean func_151631_c(final int p_151631_1_) {
        return (BiomeGenBase.getBiome(p_151631_1_) != null && BiomeGenBase.getBiome(p_151631_1_).getBiomeClass() == BiomeGenJungle.class) || p_151631_1_ == BiomeGenBase.jungleEdge.biomeID || p_151631_1_ == BiomeGenBase.jungle.biomeID || p_151631_1_ == BiomeGenBase.jungleHills.biomeID || p_151631_1_ == BiomeGenBase.forest.biomeID || p_151631_1_ == BiomeGenBase.taiga.biomeID || GenLayer.isBiomeOceanic(p_151631_1_);
    }
    
    private boolean func_151633_d(final int p_151633_1_) {
        return BiomeGenBase.getBiome(p_151633_1_) instanceof BiomeGenMesa;
    }
}
