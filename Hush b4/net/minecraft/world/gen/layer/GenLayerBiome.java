// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerBiome extends GenLayer
{
    private BiomeGenBase[] field_151623_c;
    private BiomeGenBase[] field_151621_d;
    private BiomeGenBase[] field_151622_e;
    private BiomeGenBase[] field_151620_f;
    private final ChunkProviderSettings field_175973_g;
    
    public GenLayerBiome(final long p_i45560_1_, final GenLayer p_i45560_3_, final WorldType p_i45560_4_, final String p_i45560_5_) {
        super(p_i45560_1_);
        this.field_151623_c = new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna, BiomeGenBase.savanna, BiomeGenBase.plains };
        this.field_151621_d = new BiomeGenBase[] { BiomeGenBase.forest, BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.plains, BiomeGenBase.birchForest, BiomeGenBase.swampland };
        this.field_151622_e = new BiomeGenBase[] { BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.taiga, BiomeGenBase.plains };
        this.field_151620_f = new BiomeGenBase[] { BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.coldTaiga };
        this.parent = p_i45560_3_;
        if (p_i45560_4_ == WorldType.DEFAULT_1_1) {
            this.field_151623_c = new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga };
            this.field_175973_g = null;
        }
        else if (p_i45560_4_ == WorldType.CUSTOMIZED) {
            this.field_175973_g = ChunkProviderSettings.Factory.jsonToFactory(p_i45560_5_).func_177864_b();
        }
        else {
            this.field_175973_g = null;
        }
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        final int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        final int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed(j + areaX, i + areaY);
                int k = aint[j + i * areaWidth];
                final int l = (k & 0xF00) >> 8;
                k &= 0xFFFFF0FF;
                if (this.field_175973_g != null && this.field_175973_g.fixedBiome >= 0) {
                    aint2[j + i * areaWidth] = this.field_175973_g.fixedBiome;
                }
                else if (GenLayer.isBiomeOceanic(k)) {
                    aint2[j + i * areaWidth] = k;
                }
                else if (k == BiomeGenBase.mushroomIsland.biomeID) {
                    aint2[j + i * areaWidth] = k;
                }
                else if (k == 1) {
                    if (l > 0) {
                        if (this.nextInt(3) == 0) {
                            aint2[j + i * areaWidth] = BiomeGenBase.mesaPlateau.biomeID;
                        }
                        else {
                            aint2[j + i * areaWidth] = BiomeGenBase.mesaPlateau_F.biomeID;
                        }
                    }
                    else {
                        aint2[j + i * areaWidth] = this.field_151623_c[this.nextInt(this.field_151623_c.length)].biomeID;
                    }
                }
                else if (k == 2) {
                    if (l > 0) {
                        aint2[j + i * areaWidth] = BiomeGenBase.jungle.biomeID;
                    }
                    else {
                        aint2[j + i * areaWidth] = this.field_151621_d[this.nextInt(this.field_151621_d.length)].biomeID;
                    }
                }
                else if (k == 3) {
                    if (l > 0) {
                        aint2[j + i * areaWidth] = BiomeGenBase.megaTaiga.biomeID;
                    }
                    else {
                        aint2[j + i * areaWidth] = this.field_151622_e[this.nextInt(this.field_151622_e.length)].biomeID;
                    }
                }
                else if (k == 4) {
                    aint2[j + i * areaWidth] = this.field_151620_f[this.nextInt(this.field_151620_f.length)].biomeID;
                }
                else {
                    aint2[j + i * areaWidth] = BiomeGenBase.mushroomIsland.biomeID;
                }
            }
        }
        return aint2;
    }
}
