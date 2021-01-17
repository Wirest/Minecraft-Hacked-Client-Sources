// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerBiomeEdge extends GenLayer
{
    public GenLayerBiomeEdge(final long p_i45475_1_, final GenLayer p_i45475_3_) {
        super(p_i45475_1_);
        this.parent = p_i45475_3_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                final int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                if (!this.replaceBiomeEdgeIfNecessary(aint, aint2, j, i, areaWidth, k, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID) && !this.replaceBiomeEdge(aint, aint2, j, i, areaWidth, k, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(aint, aint2, j, i, areaWidth, k, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(aint, aint2, j, i, areaWidth, k, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID)) {
                    if (k == BiomeGenBase.desert.biomeID) {
                        final int l1 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        final int i2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        final int j2 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        final int k2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                        if (l1 != BiomeGenBase.icePlains.biomeID && i2 != BiomeGenBase.icePlains.biomeID && j2 != BiomeGenBase.icePlains.biomeID && k2 != BiomeGenBase.icePlains.biomeID) {
                            aint2[j + i * areaWidth] = k;
                        }
                        else {
                            aint2[j + i * areaWidth] = BiomeGenBase.extremeHillsPlus.biomeID;
                        }
                    }
                    else if (k == BiomeGenBase.swampland.biomeID) {
                        final int m = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        final int i3 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        final int j3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        final int k3 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
                        if (m != BiomeGenBase.desert.biomeID && i3 != BiomeGenBase.desert.biomeID && j3 != BiomeGenBase.desert.biomeID && k3 != BiomeGenBase.desert.biomeID && m != BiomeGenBase.coldTaiga.biomeID && i3 != BiomeGenBase.coldTaiga.biomeID && j3 != BiomeGenBase.coldTaiga.biomeID && k3 != BiomeGenBase.coldTaiga.biomeID && m != BiomeGenBase.icePlains.biomeID && i3 != BiomeGenBase.icePlains.biomeID && j3 != BiomeGenBase.icePlains.biomeID && k3 != BiomeGenBase.icePlains.biomeID) {
                            if (m != BiomeGenBase.jungle.biomeID && k3 != BiomeGenBase.jungle.biomeID && i3 != BiomeGenBase.jungle.biomeID && j3 != BiomeGenBase.jungle.biomeID) {
                                aint2[j + i * areaWidth] = k;
                            }
                            else {
                                aint2[j + i * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
                            }
                        }
                        else {
                            aint2[j + i * areaWidth] = BiomeGenBase.plains.biomeID;
                        }
                    }
                    else {
                        aint2[j + i * areaWidth] = k;
                    }
                }
            }
        }
        return aint2;
    }
    
    private boolean replaceBiomeEdgeIfNecessary(final int[] p_151636_1_, final int[] p_151636_2_, final int p_151636_3_, final int p_151636_4_, final int p_151636_5_, final int p_151636_6_, final int p_151636_7_, final int p_151636_8_) {
        if (!GenLayer.biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_)) {
            return false;
        }
        final int i = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
        final int j = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        final int k = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
        final int l = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];
        if (this.canBiomesBeNeighbors(i, p_151636_7_) && this.canBiomesBeNeighbors(j, p_151636_7_) && this.canBiomesBeNeighbors(k, p_151636_7_) && this.canBiomesBeNeighbors(l, p_151636_7_)) {
            p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_6_;
        }
        else {
            p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_8_;
        }
        return true;
    }
    
    private boolean replaceBiomeEdge(final int[] p_151635_1_, final int[] p_151635_2_, final int p_151635_3_, final int p_151635_4_, final int p_151635_5_, final int p_151635_6_, final int p_151635_7_, final int p_151635_8_) {
        if (p_151635_6_ != p_151635_7_) {
            return false;
        }
        final int i = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
        final int j = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        final int k = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
        final int l = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];
        if (GenLayer.biomesEqualOrMesaPlateau(i, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(j, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(k, p_151635_7_) && GenLayer.biomesEqualOrMesaPlateau(l, p_151635_7_)) {
            p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_6_;
        }
        else {
            p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_8_;
        }
        return true;
    }
    
    private boolean canBiomesBeNeighbors(final int p_151634_1_, final int p_151634_2_) {
        if (GenLayer.biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_)) {
            return true;
        }
        final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(p_151634_1_);
        final BiomeGenBase biomegenbase2 = BiomeGenBase.getBiome(p_151634_2_);
        if (biomegenbase != null && biomegenbase2 != null) {
            final BiomeGenBase.TempCategory biomegenbase$tempcategory = biomegenbase.getTempCategory();
            final BiomeGenBase.TempCategory biomegenbase$tempcategory2 = biomegenbase2.getTempCategory();
            return biomegenbase$tempcategory == biomegenbase$tempcategory2 || biomegenbase$tempcategory == BiomeGenBase.TempCategory.MEDIUM || biomegenbase$tempcategory2 == BiomeGenBase.TempCategory.MEDIUM;
        }
        return false;
    }
}
