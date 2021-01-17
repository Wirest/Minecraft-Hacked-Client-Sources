// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.ResourceLocation;

public class RandomMobsRule
{
    private ResourceLocation baseResLoc;
    private int[] skins;
    private ResourceLocation[] resourceLocations;
    private int[] weights;
    private BiomeGenBase[] biomes;
    private RangeListInt heights;
    public int[] sumWeights;
    public int sumAllWeights;
    
    public RandomMobsRule(final ResourceLocation p_i79_1_, final int[] p_i79_2_, final int[] p_i79_3_, final BiomeGenBase[] p_i79_4_, final RangeListInt p_i79_5_) {
        this.baseResLoc = null;
        this.skins = null;
        this.resourceLocations = null;
        this.weights = null;
        this.biomes = null;
        this.heights = null;
        this.sumWeights = null;
        this.sumAllWeights = 1;
        this.baseResLoc = p_i79_1_;
        this.skins = p_i79_2_;
        this.weights = p_i79_3_;
        this.biomes = p_i79_4_;
        this.heights = p_i79_5_;
    }
    
    public boolean isValid(final String p_isValid_1_) {
        this.resourceLocations = new ResourceLocation[this.skins.length];
        final ResourceLocation resourcelocation = RandomMobs.getMcpatcherLocation(this.baseResLoc);
        if (resourcelocation == null) {
            Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
            return false;
        }
        for (int i = 0; i < this.resourceLocations.length; ++i) {
            final int j = this.skins[i];
            if (j <= 1) {
                this.resourceLocations[i] = this.baseResLoc;
            }
            else {
                final ResourceLocation resourcelocation2 = RandomMobs.getLocationIndexed(resourcelocation, j);
                if (resourcelocation2 == null) {
                    Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
                    return false;
                }
                if (!Config.hasResource(resourcelocation2)) {
                    Config.warn("Texture not found: " + resourcelocation2.getResourcePath());
                    return false;
                }
                this.resourceLocations[i] = resourcelocation2;
            }
        }
        if (this.weights != null) {
            if (this.weights.length > this.resourceLocations.length) {
                Config.warn("More weights defined than skins, trimming weights: " + p_isValid_1_);
                final int[] aint = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, aint, 0, aint.length);
                this.weights = aint;
            }
            if (this.weights.length < this.resourceLocations.length) {
                Config.warn("Less weights defined than skins, expanding weights: " + p_isValid_1_);
                final int[] aint2 = new int[this.resourceLocations.length];
                System.arraycopy(this.weights, 0, aint2, 0, this.weights.length);
                final int l = MathUtils.getAverage(this.weights);
                for (int j2 = this.weights.length; j2 < aint2.length; ++j2) {
                    aint2[j2] = l;
                }
                this.weights = aint2;
            }
            this.sumWeights = new int[this.weights.length];
            int k = 0;
            for (int i2 = 0; i2 < this.weights.length; ++i2) {
                if (this.weights[i2] < 0) {
                    Config.warn("Invalid weight: " + this.weights[i2]);
                    return false;
                }
                k += this.weights[i2];
                this.sumWeights[i2] = k;
            }
            this.sumAllWeights = k;
            if (this.sumAllWeights <= 0) {
                Config.warn("Invalid sum of all weights: " + k);
                this.sumAllWeights = 1;
            }
        }
        return true;
    }
    
    public boolean matches(final EntityLiving p_matches_1_) {
        return Matches.biome(p_matches_1_.spawnBiome, this.biomes) && (this.heights == null || p_matches_1_.spawnPosition == null || this.heights.isInRange(p_matches_1_.spawnPosition.getY()));
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation p_getTextureLocation_1_, final int p_getTextureLocation_2_) {
        int i = 0;
        if (this.weights == null) {
            i = p_getTextureLocation_2_ % this.resourceLocations.length;
        }
        else {
            final int j = p_getTextureLocation_2_ % this.sumAllWeights;
            for (int k = 0; k < this.sumWeights.length; ++k) {
                if (this.sumWeights[k] > j) {
                    i = k;
                    break;
                }
            }
        }
        return this.resourceLocations[i];
    }
}
