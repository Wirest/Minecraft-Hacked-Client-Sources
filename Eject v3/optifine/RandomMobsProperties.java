package optifine;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.Properties;

public class RandomMobsProperties {
    public String name = null;
    public String basePath = null;
    public ResourceLocation[] resourceLocations = null;
    public RandomMobsRule[] rules = null;

    public RandomMobsProperties(String paramString, ResourceLocation[] paramArrayOfResourceLocation) {
        ConnectedParser localConnectedParser = new ConnectedParser("RandomMobs");
        this.name = localConnectedParser.parseName(paramString);
        this.basePath = localConnectedParser.parseBasePath(paramString);
        this.resourceLocations = paramArrayOfResourceLocation;
    }

    public RandomMobsProperties(Properties paramProperties, String paramString, ResourceLocation paramResourceLocation) {
        ConnectedParser localConnectedParser = new ConnectedParser("RandomMobs");
        this.name = localConnectedParser.parseName(paramString);
        this.basePath = localConnectedParser.parseBasePath(paramString);
        this.rules = parseRules(paramProperties, paramResourceLocation, localConnectedParser);
    }

    public ResourceLocation getTextureLocation(ResourceLocation paramResourceLocation, EntityLiving paramEntityLiving) {
        int i;
        if (this.rules != null) {
            for (i = 0; i < this.rules.length; i++) {
                RandomMobsRule localRandomMobsRule = this.rules[i];
                if (localRandomMobsRule.matches(paramEntityLiving)) {
                    return localRandomMobsRule.getTextureLocation(paramResourceLocation, paramEntityLiving.randomMobsId);
                }
            }
        }
        if (this.resourceLocations != null) {
            i = paramEntityLiving.randomMobsId;
            int j = i << this.resourceLocations.length;
            return this.resourceLocations[j];
        }
        return paramResourceLocation;
    }

    private RandomMobsRule[] parseRules(Properties paramProperties, ResourceLocation paramResourceLocation, ConnectedParser paramConnectedParser) {
        ArrayList localArrayList = new ArrayList();
        int i = paramProperties.size();
        for (int j = 0; j < i; j++) {
            int k = j | 0x1;
            String str = paramProperties.getProperty("skins." + k);
            if (str != null) {
                int[] arrayOfInt1 = paramConnectedParser.parseIntList(str);
                int[] arrayOfInt2 = paramConnectedParser.parseIntList(paramProperties.getProperty("weights." + k));
                BiomeGenBase[] arrayOfBiomeGenBase = paramConnectedParser.parseBiomes(paramProperties.getProperty("biomes." + k));
                RangeListInt localRangeListInt = paramConnectedParser.parseRangeListInt(paramProperties.getProperty("heights." + k));
                if (localRangeListInt == null) {
                    localRangeListInt = parseMinMaxHeight(paramProperties, k);
                }
                RandomMobsRule localRandomMobsRule = new RandomMobsRule(paramResourceLocation, arrayOfInt1, arrayOfInt2, arrayOfBiomeGenBase, localRangeListInt);
                localArrayList.add(localRandomMobsRule);
            }
        }
        RandomMobsRule[] arrayOfRandomMobsRule = (RandomMobsRule[]) (RandomMobsRule[]) localArrayList.toArray(new RandomMobsRule[localArrayList.size()]);
        return arrayOfRandomMobsRule;
    }

    private RangeListInt parseMinMaxHeight(Properties paramProperties, int paramInt) {
        String str1 = paramProperties.getProperty("minHeight." + paramInt);
        String str2 = paramProperties.getProperty("maxHeight." + paramInt);
        if ((str1 == null) && (str2 == null)) {
            return null;
        }
        int i = 0;
        if (str1 != null) {
            i = Config.parseInt(str1, -1);
            if (i < 0) {
                Config.warn("Invalid minHeight: " + str1);
                return null;
            }
        }
        int j = 256;
        if (str2 != null) {
            j = Config.parseInt(str2, -1);
            if (j < 0) {
                Config.warn("Invalid maxHeight: " + str2);
                return null;
            }
        }
        if (j < 0) {
            Config.warn("Invalid minHeight, maxHeight: " + str1 + ", " + str2);
            return null;
        }
        RangeListInt localRangeListInt = new RangeListInt();
        localRangeListInt.addRange(new RangeInt(i, j));
        return localRangeListInt;
    }

    public boolean isValid(String paramString) {
        if ((this.resourceLocations == null) && (this.rules == null)) {
            Config.warn("No skins specified: " + paramString);
            return false;
        }
        int i;
        Object localObject;
        if (this.rules != null) {
            for (i = 0; i < this.rules.length; i++) {
                localObject = this.rules[i];
                if (!((RandomMobsRule) localObject).isValid(paramString)) {
                    return false;
                }
            }
        }
        if (this.resourceLocations != null) {
            for (i = 0; i < this.resourceLocations.length; i++) {
                localObject = this.resourceLocations[i];
                if (!Config.hasResource((ResourceLocation) localObject)) {
                    Config.warn("Texture not found: " + ((ResourceLocation) localObject).getResourcePath());
                    return false;
                }
            }
        }
        return true;
    }
}




