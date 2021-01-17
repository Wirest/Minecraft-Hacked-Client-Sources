// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.world.biome.BiomeGenBase;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.entity.EntityLiving;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;

public class RandomMobsProperties
{
    public String name;
    public String basePath;
    public ResourceLocation[] resourceLocations;
    public RandomMobsRule[] rules;
    
    public RandomMobsProperties(final String p_i77_1_, final ResourceLocation[] p_i77_2_) {
        this.name = null;
        this.basePath = null;
        this.resourceLocations = null;
        this.rules = null;
        final ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
        this.name = connectedparser.parseName(p_i77_1_);
        this.basePath = connectedparser.parseBasePath(p_i77_1_);
        this.resourceLocations = p_i77_2_;
    }
    
    public RandomMobsProperties(final Properties p_i78_1_, final String p_i78_2_, final ResourceLocation p_i78_3_) {
        this.name = null;
        this.basePath = null;
        this.resourceLocations = null;
        this.rules = null;
        final ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
        this.name = connectedparser.parseName(p_i78_2_);
        this.basePath = connectedparser.parseBasePath(p_i78_2_);
        this.rules = this.parseRules(p_i78_1_, p_i78_3_, connectedparser);
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation p_getTextureLocation_1_, final EntityLiving p_getTextureLocation_2_) {
        if (this.rules != null) {
            for (int i = 0; i < this.rules.length; ++i) {
                final RandomMobsRule randommobsrule = this.rules[i];
                if (randommobsrule.matches(p_getTextureLocation_2_)) {
                    return randommobsrule.getTextureLocation(p_getTextureLocation_1_, p_getTextureLocation_2_.randomMobsId);
                }
            }
        }
        if (this.resourceLocations != null) {
            final int j = p_getTextureLocation_2_.randomMobsId;
            final int k = j % this.resourceLocations.length;
            return this.resourceLocations[k];
        }
        return p_getTextureLocation_1_;
    }
    
    private RandomMobsRule[] parseRules(final Properties p_parseRules_1_, final ResourceLocation p_parseRules_2_, final ConnectedParser p_parseRules_3_) {
        final List list = new ArrayList();
        for (int i = p_parseRules_1_.size(), j = 0; j < i; ++j) {
            final int k = j + 1;
            final String s = p_parseRules_1_.getProperty("skins." + k);
            if (s != null) {
                final int[] aint = p_parseRules_3_.parseIntList(s);
                final int[] aint2 = p_parseRules_3_.parseIntList(p_parseRules_1_.getProperty("weights." + k));
                final BiomeGenBase[] abiomegenbase = p_parseRules_3_.parseBiomes(p_parseRules_1_.getProperty("biomes." + k));
                RangeListInt rangelistint = p_parseRules_3_.parseRangeListInt(p_parseRules_1_.getProperty("heights." + k));
                if (rangelistint == null) {
                    rangelistint = this.parseMinMaxHeight(p_parseRules_1_, k);
                }
                final RandomMobsRule randommobsrule = new RandomMobsRule(p_parseRules_2_, aint, aint2, abiomegenbase, rangelistint);
                list.add(randommobsrule);
            }
        }
        final RandomMobsRule[] arandommobsrule = list.toArray(new RandomMobsRule[list.size()]);
        return arandommobsrule;
    }
    
    private RangeListInt parseMinMaxHeight(final Properties p_parseMinMaxHeight_1_, final int p_parseMinMaxHeight_2_) {
        final String s = p_parseMinMaxHeight_1_.getProperty("minHeight." + p_parseMinMaxHeight_2_);
        final String s2 = p_parseMinMaxHeight_1_.getProperty("maxHeight." + p_parseMinMaxHeight_2_);
        if (s == null && s2 == null) {
            return null;
        }
        int i = 0;
        if (s != null) {
            i = Config.parseInt(s, -1);
            if (i < 0) {
                Config.warn("Invalid minHeight: " + s);
                return null;
            }
        }
        int j = 256;
        if (s2 != null) {
            j = Config.parseInt(s2, -1);
            if (j < 0) {
                Config.warn("Invalid maxHeight: " + s2);
                return null;
            }
        }
        if (j < 0) {
            Config.warn("Invalid minHeight, maxHeight: " + s + ", " + s2);
            return null;
        }
        final RangeListInt rangelistint = new RangeListInt();
        rangelistint.addRange(new RangeInt(i, j));
        return rangelistint;
    }
    
    public boolean isValid(final String p_isValid_1_) {
        if (this.resourceLocations == null && this.rules == null) {
            Config.warn("No skins specified: " + p_isValid_1_);
            return false;
        }
        if (this.rules != null) {
            for (int i = 0; i < this.rules.length; ++i) {
                final RandomMobsRule randommobsrule = this.rules[i];
                if (!randommobsrule.isValid(p_isValid_1_)) {
                    return false;
                }
            }
        }
        if (this.resourceLocations != null) {
            for (int j = 0; j < this.resourceLocations.length; ++j) {
                final ResourceLocation resourcelocation = this.resourceLocations[j];
                if (!Config.hasResource(resourcelocation)) {
                    Config.warn("Texture not found: " + resourcelocation.getResourcePath());
                    return false;
                }
            }
        }
        return true;
    }
}
