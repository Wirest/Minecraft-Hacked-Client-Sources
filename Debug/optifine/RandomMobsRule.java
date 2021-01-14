package optifine;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

public class RandomMobsRule
{
    private ResourceLocation baseResLoc = null;
    private int index;
    private int[] skins = null;
    private ResourceLocation[] resourceLocations = null;
    private int[] weights = null;
    private BiomeGenBase[] biomes = null;
    private RangeListInt heights = null;
    private NbtTagValue nbtName = null;
    public int[] sumWeights = null;
    public int sumAllWeights = 1;
    private VillagerProfession[] professions = null;

    public RandomMobsRule(ResourceLocation p_i89_1_, int p_i89_2_, int[] p_i89_3_, int[] p_i89_4_, BiomeGenBase[] p_i89_5_, RangeListInt p_i89_6_, NbtTagValue p_i89_7_, VillagerProfession[] p_i89_8_)
    {
        this.baseResLoc = p_i89_1_;
        this.index = p_i89_2_;
        this.skins = p_i89_3_;
        this.weights = p_i89_4_;
        this.biomes = p_i89_5_;
        this.heights = p_i89_6_;
        this.nbtName = p_i89_7_;
        this.professions = p_i89_8_;
    }

    public boolean isValid(String p_isValid_1_)
    {
        if (this.skins != null && this.skins.length != 0)
        {
            if (this.resourceLocations != null)
            {
                return true;
            }
            else
            {
                this.resourceLocations = new ResourceLocation[this.skins.length];
                ResourceLocation resourcelocation = RandomMobs.getMcpatcherLocation(this.baseResLoc);

                if (resourcelocation == null)
                {
                    Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
                    return false;
                }
                else
                {
                    for (int i = 0; i < this.resourceLocations.length; ++i)
                    {
                        int j = this.skins[i];

                        if (j <= 1)
                        {
                            this.resourceLocations[i] = this.baseResLoc;
                        }
                        else
                        {
                            ResourceLocation resourcelocation1 = RandomMobs.getLocationIndexed(resourcelocation, j);

                            if (resourcelocation1 == null)
                            {
                                Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
                                return false;
                            }

                            if (!Config.hasResource(resourcelocation1))
                            {
                                Config.warn("Texture not found: " + resourcelocation1.getResourcePath());
                                return false;
                            }

                            this.resourceLocations[i] = resourcelocation1;
                        }
                    }

                    if (this.weights != null)
                    {
                        if (this.weights.length > this.resourceLocations.length)
                        {
                            Config.warn("More weights defined than skins, trimming weights: " + p_isValid_1_);
                            int[] aint = new int[this.resourceLocations.length];
                            System.arraycopy(this.weights, 0, aint, 0, aint.length);
                            this.weights = aint;
                        }

                        if (this.weights.length < this.resourceLocations.length)
                        {
                            Config.warn("Less weights defined than skins, expanding weights: " + p_isValid_1_);
                            int[] aint1 = new int[this.resourceLocations.length];
                            System.arraycopy(this.weights, 0, aint1, 0, this.weights.length);
                            int l = MathUtils.getAverage(this.weights);

                            for (int j1 = this.weights.length; j1 < aint1.length; ++j1)
                            {
                                aint1[j1] = l;
                            }

                            this.weights = aint1;
                        }

                        this.sumWeights = new int[this.weights.length];
                        int k = 0;

                        for (int i1 = 0; i1 < this.weights.length; ++i1)
                        {
                            if (this.weights[i1] < 0)
                            {
                                Config.warn("Invalid weight: " + this.weights[i1]);
                                return false;
                            }

                            k += this.weights[i1];
                            this.sumWeights[i1] = k;
                        }

                        this.sumAllWeights = k;

                        if (this.sumAllWeights <= 0)
                        {
                            Config.warn("Invalid sum of all weights: " + k);
                            this.sumAllWeights = 1;
                        }
                    }

                    if (this.professions == ConnectedParser.PROFESSIONS_INVALID)
                    {
                        Config.warn("Invalid professions or careers: " + p_isValid_1_);
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            Config.warn("Invalid skins for rule: " + this.index);
            return false;
        }
    }

    public boolean matches(EntityLiving p_matches_1_)
    {
        if (!Matches.biome(p_matches_1_.spawnBiome, this.biomes))
        {
            return false;
        }
        else if (this.heights != null && p_matches_1_.spawnPosition != null)
        {
            return this.heights.isInRange(p_matches_1_.spawnPosition.getY());
        }
        else
        {
            if (this.nbtName != null)
            {
                String s = p_matches_1_.hasCustomName() ? p_matches_1_.getCustomNameTag() : null;

                if (!this.nbtName.matchesValue(s))
                {
                    return false;
                }
            }

            if (this.professions != null && p_matches_1_ instanceof EntityVillager)
            {
                EntityVillager entityvillager = (EntityVillager)p_matches_1_;
                int i = entityvillager.getProfession();
                int j = Reflector.getFieldValueInt(entityvillager, Reflector.EntityVillager_careerId, -1);

                if (i < 0 || j < 0)
                {
                    return false;
                }

                boolean flag = false;

                for (int k = 0; k < this.professions.length; ++k)
                {
                    VillagerProfession villagerprofession = this.professions[k];

                    if (villagerprofession.matches(i, j))
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_1_, int p_getTextureLocation_2_)
    {
        if (this.resourceLocations != null && this.resourceLocations.length != 0)
        {
            int i = 0;

            if (this.weights == null)
            {
                i = p_getTextureLocation_2_ % this.resourceLocations.length;
            }
            else
            {
                int j = p_getTextureLocation_2_ % this.sumAllWeights;

                for (int k = 0; k < this.sumWeights.length; ++k)
                {
                    if (this.sumWeights[k] > j)
                    {
                        i = k;
                        break;
                    }
                }
            }

            return this.resourceLocations[i];
        }
        else
        {
            return p_getTextureLocation_1_;
        }
    }
}
