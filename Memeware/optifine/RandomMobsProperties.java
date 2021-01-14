package optifine;

import java.util.ArrayList;
import java.util.Properties;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

public class RandomMobsProperties {
    public String name = null;
    public String basePath = null;
    public ResourceLocation[] resourceLocations = null;
    public RandomMobsRule[] rules = null;

    public RandomMobsProperties(String path, ResourceLocation[] variants) {
        ConnectedParser cp = new ConnectedParser("RandomMobs");
        this.name = cp.parseName(path);
        this.basePath = cp.parseBasePath(path);
        this.resourceLocations = variants;
    }

    public RandomMobsProperties(Properties props, String path, ResourceLocation baseResLoc) {
        ConnectedParser cp = new ConnectedParser("RandomMobs");
        this.name = cp.parseName(path);
        this.basePath = cp.parseBasePath(path);
        this.rules = this.parseRules(props, baseResLoc, cp);
    }

    public ResourceLocation getTextureLocation(ResourceLocation loc, EntityLiving el) {
        int randomId;

        if (this.rules != null) {
            for (randomId = 0; randomId < this.rules.length; ++randomId) {
                RandomMobsRule index = this.rules[randomId];

                if (index.matches(el)) {
                    return index.getTextureLocation(loc, el.randomMobsId);
                }
            }
        }

        if (this.resourceLocations != null) {
            randomId = el.randomMobsId;
            int var5 = randomId % this.resourceLocations.length;
            return this.resourceLocations[var5];
        } else {
            return loc;
        }
    }

    private RandomMobsRule[] parseRules(Properties props, ResourceLocation baseResLoc, ConnectedParser cp) {
        ArrayList list = new ArrayList();
        int count = props.size();

        for (int rules = 0; rules < count; ++rules) {
            int index = rules + 1;
            String valSkins = props.getProperty("skins." + index);

            if (valSkins != null) {
                int[] skins = cp.parseIntList(valSkins);
                int[] weights = cp.parseIntList(props.getProperty("weights." + index));
                BiomeGenBase[] biomes = cp.parseBiomes(props.getProperty("biomes." + index));
                RangeListInt heights = cp.parseRangeListInt(props.getProperty("heights." + index));

                if (heights == null) {
                    heights = this.parseMinMaxHeight(props, index);
                }

                RandomMobsRule rule = new RandomMobsRule(baseResLoc, skins, weights, biomes, heights);
                list.add(rule);
            }
        }

        RandomMobsRule[] var14 = (RandomMobsRule[]) ((RandomMobsRule[]) list.toArray(new RandomMobsRule[list.size()]));
        return var14;
    }

    private RangeListInt parseMinMaxHeight(Properties props, int index) {
        String minHeightStr = props.getProperty("minHeight." + index);
        String maxHeightStr = props.getProperty("maxHeight." + index);

        if (minHeightStr == null && maxHeightStr == null) {
            return null;
        } else {
            int minHeight = 0;

            if (minHeightStr != null) {
                minHeight = Config.parseInt(minHeightStr, -1);

                if (minHeight < 0) {
                    Config.warn("Invalid minHeight: " + minHeightStr);
                    return null;
                }
            }

            int maxHeight = 256;

            if (maxHeightStr != null) {
                maxHeight = Config.parseInt(maxHeightStr, -1);

                if (maxHeight < 0) {
                    Config.warn("Invalid maxHeight: " + maxHeightStr);
                    return null;
                }
            }

            if (maxHeight < 0) {
                Config.warn("Invalid minHeight, maxHeight: " + minHeightStr + ", " + maxHeightStr);
                return null;
            } else {
                RangeListInt list = new RangeListInt();
                list.addRange(new RangeInt(minHeight, maxHeight));
                return list;
            }
        }
    }

    public boolean isValid(String path) {
        if (this.resourceLocations == null && this.rules == null) {
            Config.warn("No skins specified: " + path);
            return false;
        } else {
            int i;

            if (this.rules != null) {
                for (i = 0; i < this.rules.length; ++i) {
                    RandomMobsRule loc = this.rules[i];

                    if (!loc.isValid(path)) {
                        return false;
                    }
                }
            }

            if (this.resourceLocations != null) {
                for (i = 0; i < this.resourceLocations.length; ++i) {
                    ResourceLocation var4 = this.resourceLocations[i];

                    if (!Config.hasResource(var4)) {
                        Config.warn("Texture not found: " + var4.getResourcePath());
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
