package net.minecraft.world.gen;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.util.JsonUtils;
import net.minecraft.world.biome.BiomeGenBase;

public class ChunkProviderSettings
{
    public final float coordinateScale;
    public final float heightScale;
    public final float upperLimitScale;
    public final float lowerLimitScale;
    public final float depthNoiseScaleX;
    public final float depthNoiseScaleZ;
    public final float depthNoiseScaleExponent;
    public final float mainNoiseScaleX;
    public final float mainNoiseScaleY;
    public final float mainNoiseScaleZ;
    public final float baseSize;
    public final float stretchY;
    public final float biomeDepthWeight;
    public final float biomeDepthOffSet;
    public final float biomeScaleWeight;
    public final float biomeScaleOffset;
    public final int seaLevel;
    public final boolean useCaves;
    public final boolean useDungeons;
    public final int dungeonChance;
    public final boolean useStrongholds;
    public final boolean useVillages;
    public final boolean useMineShafts;
    public final boolean useTemples;
    public final boolean useMonuments;
    public final boolean useRavines;
    public final boolean useWaterLakes;
    public final int waterLakeChance;
    public final boolean useLavaLakes;
    public final int lavaLakeChance;
    public final boolean useLavaOceans;
    public final int fixedBiome;
    public final int biomeSize;
    public final int riverSize;
    public final int dirtSize;
    public final int dirtCount;
    public final int dirtMinHeight;
    public final int dirtMaxHeight;
    public final int gravelSize;
    public final int gravelCount;
    public final int gravelMinHeight;
    public final int gravelMaxHeight;
    public final int graniteSize;
    public final int graniteCount;
    public final int graniteMinHeight;
    public final int graniteMaxHeight;
    public final int dioriteSize;
    public final int dioriteCount;
    public final int dioriteMinHeight;
    public final int dioriteMaxHeight;
    public final int andesiteSize;
    public final int andesiteCount;
    public final int andesiteMinHeight;
    public final int andesiteMaxHeight;
    public final int coalSize;
    public final int coalCount;
    public final int coalMinHeight;
    public final int coalMaxHeight;
    public final int ironSize;
    public final int ironCount;
    public final int ironMinHeight;
    public final int ironMaxHeight;
    public final int goldSize;
    public final int goldCount;
    public final int goldMinHeight;
    public final int goldMaxHeight;
    public final int redstoneSize;
    public final int redstoneCount;
    public final int redstoneMinHeight;
    public final int redstoneMaxHeight;
    public final int diamondSize;
    public final int diamondCount;
    public final int diamondMinHeight;
    public final int diamondMaxHeight;
    public final int lapisSize;
    public final int lapisCount;
    public final int lapisCenterHeight;
    public final int lapisSpread;

    private ChunkProviderSettings(ChunkProviderSettings.Factory settingsFactory)
    {
        this.coordinateScale = settingsFactory.coordinateScale;
        this.heightScale = settingsFactory.heightScale;
        this.upperLimitScale = settingsFactory.upperLimitScale;
        this.lowerLimitScale = settingsFactory.lowerLimitScale;
        this.depthNoiseScaleX = settingsFactory.depthNoiseScaleX;
        this.depthNoiseScaleZ = settingsFactory.depthNoiseScaleZ;
        this.depthNoiseScaleExponent = settingsFactory.depthNoiseScaleExponent;
        this.mainNoiseScaleX = settingsFactory.mainNoiseScaleX;
        this.mainNoiseScaleY = settingsFactory.mainNoiseScaleY;
        this.mainNoiseScaleZ = settingsFactory.mainNoiseScaleZ;
        this.baseSize = settingsFactory.baseSize;
        this.stretchY = settingsFactory.stretchY;
        this.biomeDepthWeight = settingsFactory.biomeDepthWeight;
        this.biomeDepthOffSet = settingsFactory.biomeDepthOffset;
        this.biomeScaleWeight = settingsFactory.biomeScaleWeight;
        this.biomeScaleOffset = settingsFactory.biomeScaleOffset;
        this.seaLevel = settingsFactory.seaLevel;
        this.useCaves = settingsFactory.useCaves;
        this.useDungeons = settingsFactory.useDungeons;
        this.dungeonChance = settingsFactory.dungeonChance;
        this.useStrongholds = settingsFactory.useStrongholds;
        this.useVillages = settingsFactory.useVillages;
        this.useMineShafts = settingsFactory.useMineShafts;
        this.useTemples = settingsFactory.useTemples;
        this.useMonuments = settingsFactory.useMonuments;
        this.useRavines = settingsFactory.useRavines;
        this.useWaterLakes = settingsFactory.useWaterLakes;
        this.waterLakeChance = settingsFactory.waterLakeChance;
        this.useLavaLakes = settingsFactory.useLavaLakes;
        this.lavaLakeChance = settingsFactory.lavaLakeChance;
        this.useLavaOceans = settingsFactory.useLavaOceans;
        this.fixedBiome = settingsFactory.fixedBiome;
        this.biomeSize = settingsFactory.biomeSize;
        this.riverSize = settingsFactory.riverSize;
        this.dirtSize = settingsFactory.dirtSize;
        this.dirtCount = settingsFactory.dirtCount;
        this.dirtMinHeight = settingsFactory.dirtMinHeight;
        this.dirtMaxHeight = settingsFactory.dirtMaxHeight;
        this.gravelSize = settingsFactory.gravelSize;
        this.gravelCount = settingsFactory.gravelCount;
        this.gravelMinHeight = settingsFactory.gravelMinHeight;
        this.gravelMaxHeight = settingsFactory.gravelMaxHeight;
        this.graniteSize = settingsFactory.graniteSize;
        this.graniteCount = settingsFactory.graniteCount;
        this.graniteMinHeight = settingsFactory.graniteMinHeight;
        this.graniteMaxHeight = settingsFactory.graniteMaxHeight;
        this.dioriteSize = settingsFactory.dioriteSize;
        this.dioriteCount = settingsFactory.dioriteCount;
        this.dioriteMinHeight = settingsFactory.dioriteMinHeight;
        this.dioriteMaxHeight = settingsFactory.dioriteMaxHeight;
        this.andesiteSize = settingsFactory.andesiteSize;
        this.andesiteCount = settingsFactory.andesiteCount;
        this.andesiteMinHeight = settingsFactory.andesiteMinHeight;
        this.andesiteMaxHeight = settingsFactory.andesiteMaxHeight;
        this.coalSize = settingsFactory.coalSize;
        this.coalCount = settingsFactory.coalCount;
        this.coalMinHeight = settingsFactory.coalMinHeight;
        this.coalMaxHeight = settingsFactory.coalMaxHeight;
        this.ironSize = settingsFactory.ironSize;
        this.ironCount = settingsFactory.ironCount;
        this.ironMinHeight = settingsFactory.ironMinHeight;
        this.ironMaxHeight = settingsFactory.ironMaxHeight;
        this.goldSize = settingsFactory.goldSize;
        this.goldCount = settingsFactory.goldCount;
        this.goldMinHeight = settingsFactory.goldMinHeight;
        this.goldMaxHeight = settingsFactory.goldMaxHeight;
        this.redstoneSize = settingsFactory.redstoneSize;
        this.redstoneCount = settingsFactory.redstoneCount;
        this.redstoneMinHeight = settingsFactory.redstoneMinHeight;
        this.redstoneMaxHeight = settingsFactory.redstoneMaxHeight;
        this.diamondSize = settingsFactory.diamondSize;
        this.diamondCount = settingsFactory.diamondCount;
        this.diamondMinHeight = settingsFactory.diamondMinHeight;
        this.diamondMaxHeight = settingsFactory.diamondMaxHeight;
        this.lapisSize = settingsFactory.lapisSize;
        this.lapisCount = settingsFactory.lapisCount;
        this.lapisCenterHeight = settingsFactory.lapisCenterHeight;
        this.lapisSpread = settingsFactory.lapisSpread;
    }

    ChunkProviderSettings(ChunkProviderSettings.Factory p_i45640_1_, Object p_i45640_2_)
    {
        this(p_i45640_1_);
    }

    public static class Factory
    {
        static final Gson JSON_ADAPTER = (new GsonBuilder()).registerTypeAdapter(ChunkProviderSettings.Factory.class, new ChunkProviderSettings.Serializer()).create();
        public float coordinateScale = 684.412F;
        public float heightScale = 684.412F;
        public float upperLimitScale = 512.0F;
        public float lowerLimitScale = 512.0F;
        public float depthNoiseScaleX = 200.0F;
        public float depthNoiseScaleZ = 200.0F;
        public float depthNoiseScaleExponent = 0.5F;
        public float mainNoiseScaleX = 80.0F;
        public float mainNoiseScaleY = 160.0F;
        public float mainNoiseScaleZ = 80.0F;
        public float baseSize = 8.5F;
        public float stretchY = 12.0F;
        public float biomeDepthWeight = 1.0F;
        public float biomeDepthOffset = 0.0F;
        public float biomeScaleWeight = 1.0F;
        public float biomeScaleOffset = 0.0F;
        public int seaLevel = 63;
        public boolean useCaves = true;
        public boolean useDungeons = true;
        public int dungeonChance = 8;
        public boolean useStrongholds = true;
        public boolean useVillages = true;
        public boolean useMineShafts = true;
        public boolean useTemples = true;
        public boolean useMonuments = true;
        public boolean useRavines = true;
        public boolean useWaterLakes = true;
        public int waterLakeChance = 4;
        public boolean useLavaLakes = true;
        public int lavaLakeChance = 80;
        public boolean useLavaOceans = false;
        public int fixedBiome = -1;
        public int biomeSize = 4;
        public int riverSize = 4;
        public int dirtSize = 33;
        public int dirtCount = 10;
        public int dirtMinHeight = 0;
        public int dirtMaxHeight = 256;
        public int gravelSize = 33;
        public int gravelCount = 8;
        public int gravelMinHeight = 0;
        public int gravelMaxHeight = 256;
        public int graniteSize = 33;
        public int graniteCount = 10;
        public int graniteMinHeight = 0;
        public int graniteMaxHeight = 80;
        public int dioriteSize = 33;
        public int dioriteCount = 10;
        public int dioriteMinHeight = 0;
        public int dioriteMaxHeight = 80;
        public int andesiteSize = 33;
        public int andesiteCount = 10;
        public int andesiteMinHeight = 0;
        public int andesiteMaxHeight = 80;
        public int coalSize = 17;
        public int coalCount = 20;
        public int coalMinHeight = 0;
        public int coalMaxHeight = 128;
        public int ironSize = 9;
        public int ironCount = 20;
        public int ironMinHeight = 0;
        public int ironMaxHeight = 64;
        public int goldSize = 9;
        public int goldCount = 2;
        public int goldMinHeight = 0;
        public int goldMaxHeight = 32;
        public int redstoneSize = 8;
        public int redstoneCount = 8;
        public int redstoneMinHeight = 0;
        public int redstoneMaxHeight = 16;
        public int diamondSize = 8;
        public int diamondCount = 1;
        public int diamondMinHeight = 0;
        public int diamondMaxHeight = 16;
        public int lapisSize = 7;
        public int lapisCount = 1;
        public int lapisCenterHeight = 16;
        public int lapisSpread = 16;

        public static ChunkProviderSettings.Factory func_177865_a(String p_177865_0_)
        {
            if (p_177865_0_.length() == 0)
            {
                return new ChunkProviderSettings.Factory();
            }
            else
            {
                try
                {
                    return JSON_ADAPTER.fromJson(p_177865_0_, ChunkProviderSettings.Factory.class);
                }
                catch (Exception var2)
                {
                    return new ChunkProviderSettings.Factory();
                }
            }
        }

        @Override
		public String toString()
        {
            return JSON_ADAPTER.toJson(this);
        }

        public Factory()
        {
            this.func_177863_a();
        }

        public void func_177863_a()
        {
            this.coordinateScale = 684.412F;
            this.heightScale = 684.412F;
            this.upperLimitScale = 512.0F;
            this.lowerLimitScale = 512.0F;
            this.depthNoiseScaleX = 200.0F;
            this.depthNoiseScaleZ = 200.0F;
            this.depthNoiseScaleExponent = 0.5F;
            this.mainNoiseScaleX = 80.0F;
            this.mainNoiseScaleY = 160.0F;
            this.mainNoiseScaleZ = 80.0F;
            this.baseSize = 8.5F;
            this.stretchY = 12.0F;
            this.biomeDepthWeight = 1.0F;
            this.biomeDepthOffset = 0.0F;
            this.biomeScaleWeight = 1.0F;
            this.biomeScaleOffset = 0.0F;
            this.seaLevel = 63;
            this.useCaves = true;
            this.useDungeons = true;
            this.dungeonChance = 8;
            this.useStrongholds = true;
            this.useVillages = true;
            this.useMineShafts = true;
            this.useTemples = true;
            this.useMonuments = true;
            this.useRavines = true;
            this.useWaterLakes = true;
            this.waterLakeChance = 4;
            this.useLavaLakes = true;
            this.lavaLakeChance = 80;
            this.useLavaOceans = false;
            this.fixedBiome = -1;
            this.biomeSize = 4;
            this.riverSize = 4;
            this.dirtSize = 33;
            this.dirtCount = 10;
            this.dirtMinHeight = 0;
            this.dirtMaxHeight = 256;
            this.gravelSize = 33;
            this.gravelCount = 8;
            this.gravelMinHeight = 0;
            this.gravelMaxHeight = 256;
            this.graniteSize = 33;
            this.graniteCount = 10;
            this.graniteMinHeight = 0;
            this.graniteMaxHeight = 80;
            this.dioriteSize = 33;
            this.dioriteCount = 10;
            this.dioriteMinHeight = 0;
            this.dioriteMaxHeight = 80;
            this.andesiteSize = 33;
            this.andesiteCount = 10;
            this.andesiteMinHeight = 0;
            this.andesiteMaxHeight = 80;
            this.coalSize = 17;
            this.coalCount = 20;
            this.coalMinHeight = 0;
            this.coalMaxHeight = 128;
            this.ironSize = 9;
            this.ironCount = 20;
            this.ironMinHeight = 0;
            this.ironMaxHeight = 64;
            this.goldSize = 9;
            this.goldCount = 2;
            this.goldMinHeight = 0;
            this.goldMaxHeight = 32;
            this.redstoneSize = 8;
            this.redstoneCount = 8;
            this.redstoneMinHeight = 0;
            this.redstoneMaxHeight = 16;
            this.diamondSize = 8;
            this.diamondCount = 1;
            this.diamondMinHeight = 0;
            this.diamondMaxHeight = 16;
            this.lapisSize = 7;
            this.lapisCount = 1;
            this.lapisCenterHeight = 16;
            this.lapisSpread = 16;
        }

        @Override
		public boolean equals(Object p_equals_1_)
        {
            if (this == p_equals_1_)
            {
                return true;
            }
            else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
            {
                ChunkProviderSettings.Factory var2 = (ChunkProviderSettings.Factory)p_equals_1_;
                return this.andesiteCount != var2.andesiteCount ? false : (this.andesiteMaxHeight != var2.andesiteMaxHeight ? false : (this.andesiteMinHeight != var2.andesiteMinHeight ? false : (this.andesiteSize != var2.andesiteSize ? false : (Float.compare(var2.baseSize, this.baseSize) != 0 ? false : (Float.compare(var2.biomeDepthOffset, this.biomeDepthOffset) != 0 ? false : (Float.compare(var2.biomeDepthWeight, this.biomeDepthWeight) != 0 ? false : (Float.compare(var2.biomeScaleOffset, this.biomeScaleOffset) != 0 ? false : (Float.compare(var2.biomeScaleWeight, this.biomeScaleWeight) != 0 ? false : (this.biomeSize != var2.biomeSize ? false : (this.coalCount != var2.coalCount ? false : (this.coalMaxHeight != var2.coalMaxHeight ? false : (this.coalMinHeight != var2.coalMinHeight ? false : (this.coalSize != var2.coalSize ? false : (Float.compare(var2.coordinateScale, this.coordinateScale) != 0 ? false : (Float.compare(var2.depthNoiseScaleExponent, this.depthNoiseScaleExponent) != 0 ? false : (Float.compare(var2.depthNoiseScaleX, this.depthNoiseScaleX) != 0 ? false : (Float.compare(var2.depthNoiseScaleZ, this.depthNoiseScaleZ) != 0 ? false : (this.diamondCount != var2.diamondCount ? false : (this.diamondMaxHeight != var2.diamondMaxHeight ? false : (this.diamondMinHeight != var2.diamondMinHeight ? false : (this.diamondSize != var2.diamondSize ? false : (this.dioriteCount != var2.dioriteCount ? false : (this.dioriteMaxHeight != var2.dioriteMaxHeight ? false : (this.dioriteMinHeight != var2.dioriteMinHeight ? false : (this.dioriteSize != var2.dioriteSize ? false : (this.dirtCount != var2.dirtCount ? false : (this.dirtMaxHeight != var2.dirtMaxHeight ? false : (this.dirtMinHeight != var2.dirtMinHeight ? false : (this.dirtSize != var2.dirtSize ? false : (this.dungeonChance != var2.dungeonChance ? false : (this.fixedBiome != var2.fixedBiome ? false : (this.goldCount != var2.goldCount ? false : (this.goldMaxHeight != var2.goldMaxHeight ? false : (this.goldMinHeight != var2.goldMinHeight ? false : (this.goldSize != var2.goldSize ? false : (this.graniteCount != var2.graniteCount ? false : (this.graniteMaxHeight != var2.graniteMaxHeight ? false : (this.graniteMinHeight != var2.graniteMinHeight ? false : (this.graniteSize != var2.graniteSize ? false : (this.gravelCount != var2.gravelCount ? false : (this.gravelMaxHeight != var2.gravelMaxHeight ? false : (this.gravelMinHeight != var2.gravelMinHeight ? false : (this.gravelSize != var2.gravelSize ? false : (Float.compare(var2.heightScale, this.heightScale) != 0 ? false : (this.ironCount != var2.ironCount ? false : (this.ironMaxHeight != var2.ironMaxHeight ? false : (this.ironMinHeight != var2.ironMinHeight ? false : (this.ironSize != var2.ironSize ? false : (this.lapisCenterHeight != var2.lapisCenterHeight ? false : (this.lapisCount != var2.lapisCount ? false : (this.lapisSize != var2.lapisSize ? false : (this.lapisSpread != var2.lapisSpread ? false : (this.lavaLakeChance != var2.lavaLakeChance ? false : (Float.compare(var2.lowerLimitScale, this.lowerLimitScale) != 0 ? false : (Float.compare(var2.mainNoiseScaleX, this.mainNoiseScaleX) != 0 ? false : (Float.compare(var2.mainNoiseScaleY, this.mainNoiseScaleY) != 0 ? false : (Float.compare(var2.mainNoiseScaleZ, this.mainNoiseScaleZ) != 0 ? false : (this.redstoneCount != var2.redstoneCount ? false : (this.redstoneMaxHeight != var2.redstoneMaxHeight ? false : (this.redstoneMinHeight != var2.redstoneMinHeight ? false : (this.redstoneSize != var2.redstoneSize ? false : (this.riverSize != var2.riverSize ? false : (this.seaLevel != var2.seaLevel ? false : (Float.compare(var2.stretchY, this.stretchY) != 0 ? false : (Float.compare(var2.upperLimitScale, this.upperLimitScale) != 0 ? false : (this.useCaves != var2.useCaves ? false : (this.useDungeons != var2.useDungeons ? false : (this.useLavaLakes != var2.useLavaLakes ? false : (this.useLavaOceans != var2.useLavaOceans ? false : (this.useMineShafts != var2.useMineShafts ? false : (this.useRavines != var2.useRavines ? false : (this.useStrongholds != var2.useStrongholds ? false : (this.useTemples != var2.useTemples ? false : (this.useMonuments != var2.useMonuments ? false : (this.useVillages != var2.useVillages ? false : (this.useWaterLakes != var2.useWaterLakes ? false : this.waterLakeChance == var2.waterLakeChance))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))));
            }
            else
            {
                return false;
            }
        }

        @Override
		public int hashCode()
        {
            int var1 = this.coordinateScale != 0.0F ? Float.floatToIntBits(this.coordinateScale) : 0;
            var1 = 31 * var1 + (this.heightScale != 0.0F ? Float.floatToIntBits(this.heightScale) : 0);
            var1 = 31 * var1 + (this.upperLimitScale != 0.0F ? Float.floatToIntBits(this.upperLimitScale) : 0);
            var1 = 31 * var1 + (this.lowerLimitScale != 0.0F ? Float.floatToIntBits(this.lowerLimitScale) : 0);
            var1 = 31 * var1 + (this.depthNoiseScaleX != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleX) : 0);
            var1 = 31 * var1 + (this.depthNoiseScaleZ != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleZ) : 0);
            var1 = 31 * var1 + (this.depthNoiseScaleExponent != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleExponent) : 0);
            var1 = 31 * var1 + (this.mainNoiseScaleX != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleX) : 0);
            var1 = 31 * var1 + (this.mainNoiseScaleY != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleY) : 0);
            var1 = 31 * var1 + (this.mainNoiseScaleZ != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleZ) : 0);
            var1 = 31 * var1 + (this.baseSize != 0.0F ? Float.floatToIntBits(this.baseSize) : 0);
            var1 = 31 * var1 + (this.stretchY != 0.0F ? Float.floatToIntBits(this.stretchY) : 0);
            var1 = 31 * var1 + (this.biomeDepthWeight != 0.0F ? Float.floatToIntBits(this.biomeDepthWeight) : 0);
            var1 = 31 * var1 + (this.biomeDepthOffset != 0.0F ? Float.floatToIntBits(this.biomeDepthOffset) : 0);
            var1 = 31 * var1 + (this.biomeScaleWeight != 0.0F ? Float.floatToIntBits(this.biomeScaleWeight) : 0);
            var1 = 31 * var1 + (this.biomeScaleOffset != 0.0F ? Float.floatToIntBits(this.biomeScaleOffset) : 0);
            var1 = 31 * var1 + this.seaLevel;
            var1 = 31 * var1 + (this.useCaves ? 1 : 0);
            var1 = 31 * var1 + (this.useDungeons ? 1 : 0);
            var1 = 31 * var1 + this.dungeonChance;
            var1 = 31 * var1 + (this.useStrongholds ? 1 : 0);
            var1 = 31 * var1 + (this.useVillages ? 1 : 0);
            var1 = 31 * var1 + (this.useMineShafts ? 1 : 0);
            var1 = 31 * var1 + (this.useTemples ? 1 : 0);
            var1 = 31 * var1 + (this.useMonuments ? 1 : 0);
            var1 = 31 * var1 + (this.useRavines ? 1 : 0);
            var1 = 31 * var1 + (this.useWaterLakes ? 1 : 0);
            var1 = 31 * var1 + this.waterLakeChance;
            var1 = 31 * var1 + (this.useLavaLakes ? 1 : 0);
            var1 = 31 * var1 + this.lavaLakeChance;
            var1 = 31 * var1 + (this.useLavaOceans ? 1 : 0);
            var1 = 31 * var1 + this.fixedBiome;
            var1 = 31 * var1 + this.biomeSize;
            var1 = 31 * var1 + this.riverSize;
            var1 = 31 * var1 + this.dirtSize;
            var1 = 31 * var1 + this.dirtCount;
            var1 = 31 * var1 + this.dirtMinHeight;
            var1 = 31 * var1 + this.dirtMaxHeight;
            var1 = 31 * var1 + this.gravelSize;
            var1 = 31 * var1 + this.gravelCount;
            var1 = 31 * var1 + this.gravelMinHeight;
            var1 = 31 * var1 + this.gravelMaxHeight;
            var1 = 31 * var1 + this.graniteSize;
            var1 = 31 * var1 + this.graniteCount;
            var1 = 31 * var1 + this.graniteMinHeight;
            var1 = 31 * var1 + this.graniteMaxHeight;
            var1 = 31 * var1 + this.dioriteSize;
            var1 = 31 * var1 + this.dioriteCount;
            var1 = 31 * var1 + this.dioriteMinHeight;
            var1 = 31 * var1 + this.dioriteMaxHeight;
            var1 = 31 * var1 + this.andesiteSize;
            var1 = 31 * var1 + this.andesiteCount;
            var1 = 31 * var1 + this.andesiteMinHeight;
            var1 = 31 * var1 + this.andesiteMaxHeight;
            var1 = 31 * var1 + this.coalSize;
            var1 = 31 * var1 + this.coalCount;
            var1 = 31 * var1 + this.coalMinHeight;
            var1 = 31 * var1 + this.coalMaxHeight;
            var1 = 31 * var1 + this.ironSize;
            var1 = 31 * var1 + this.ironCount;
            var1 = 31 * var1 + this.ironMinHeight;
            var1 = 31 * var1 + this.ironMaxHeight;
            var1 = 31 * var1 + this.goldSize;
            var1 = 31 * var1 + this.goldCount;
            var1 = 31 * var1 + this.goldMinHeight;
            var1 = 31 * var1 + this.goldMaxHeight;
            var1 = 31 * var1 + this.redstoneSize;
            var1 = 31 * var1 + this.redstoneCount;
            var1 = 31 * var1 + this.redstoneMinHeight;
            var1 = 31 * var1 + this.redstoneMaxHeight;
            var1 = 31 * var1 + this.diamondSize;
            var1 = 31 * var1 + this.diamondCount;
            var1 = 31 * var1 + this.diamondMinHeight;
            var1 = 31 * var1 + this.diamondMaxHeight;
            var1 = 31 * var1 + this.lapisSize;
            var1 = 31 * var1 + this.lapisCount;
            var1 = 31 * var1 + this.lapisCenterHeight;
            var1 = 31 * var1 + this.lapisSpread;
            return var1;
        }

        public ChunkProviderSettings func_177864_b()
        {
            return new ChunkProviderSettings(this, null);
        }
    }

    public static class Serializer implements JsonDeserializer, JsonSerializer
    {

        public ChunkProviderSettings.Factory func_177861_a(JsonElement p_177861_1_, Type p_177861_2_, JsonDeserializationContext p_177861_3_)
        {
            JsonObject var4 = p_177861_1_.getAsJsonObject();
            ChunkProviderSettings.Factory var5 = new ChunkProviderSettings.Factory();

            try
            {
                var5.coordinateScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "coordinateScale", var5.coordinateScale);
                var5.heightScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "heightScale", var5.heightScale);
                var5.lowerLimitScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "lowerLimitScale", var5.lowerLimitScale);
                var5.upperLimitScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "upperLimitScale", var5.upperLimitScale);
                var5.depthNoiseScaleX = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "depthNoiseScaleX", var5.depthNoiseScaleX);
                var5.depthNoiseScaleZ = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "depthNoiseScaleZ", var5.depthNoiseScaleZ);
                var5.depthNoiseScaleExponent = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "depthNoiseScaleExponent", var5.depthNoiseScaleExponent);
                var5.mainNoiseScaleX = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "mainNoiseScaleX", var5.mainNoiseScaleX);
                var5.mainNoiseScaleY = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "mainNoiseScaleY", var5.mainNoiseScaleY);
                var5.mainNoiseScaleZ = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "mainNoiseScaleZ", var5.mainNoiseScaleZ);
                var5.baseSize = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "baseSize", var5.baseSize);
                var5.stretchY = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "stretchY", var5.stretchY);
                var5.biomeDepthWeight = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "biomeDepthWeight", var5.biomeDepthWeight);
                var5.biomeDepthOffset = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "biomeDepthOffset", var5.biomeDepthOffset);
                var5.biomeScaleWeight = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "biomeScaleWeight", var5.biomeScaleWeight);
                var5.biomeScaleOffset = JsonUtils.getJsonObjectFloatFieldValueOrDefault(var4, "biomeScaleOffset", var5.biomeScaleOffset);
                var5.seaLevel = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "seaLevel", var5.seaLevel);
                var5.useCaves = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useCaves", var5.useCaves);
                var5.useDungeons = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useDungeons", var5.useDungeons);
                var5.dungeonChance = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dungeonChance", var5.dungeonChance);
                var5.useStrongholds = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useStrongholds", var5.useStrongholds);
                var5.useVillages = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useVillages", var5.useVillages);
                var5.useMineShafts = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useMineShafts", var5.useMineShafts);
                var5.useTemples = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useTemples", var5.useTemples);
                var5.useMonuments = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useMonuments", var5.useMonuments);
                var5.useRavines = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useRavines", var5.useRavines);
                var5.useWaterLakes = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useWaterLakes", var5.useWaterLakes);
                var5.waterLakeChance = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "waterLakeChance", var5.waterLakeChance);
                var5.useLavaLakes = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useLavaLakes", var5.useLavaLakes);
                var5.lavaLakeChance = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "lavaLakeChance", var5.lavaLakeChance);
                var5.useLavaOceans = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "useLavaOceans", var5.useLavaOceans);
                var5.fixedBiome = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "fixedBiome", var5.fixedBiome);

                if (var5.fixedBiome < 38 && var5.fixedBiome >= -1)
                {
                    if (var5.fixedBiome >= BiomeGenBase.hell.biomeID)
                    {
                        var5.fixedBiome += 2;
                    }
                }
                else
                {
                    var5.fixedBiome = -1;
                }

                var5.biomeSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "biomeSize", var5.biomeSize);
                var5.riverSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "riverSize", var5.riverSize);
                var5.dirtSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dirtSize", var5.dirtSize);
                var5.dirtCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dirtCount", var5.dirtCount);
                var5.dirtMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dirtMinHeight", var5.dirtMinHeight);
                var5.dirtMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dirtMaxHeight", var5.dirtMaxHeight);
                var5.gravelSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "gravelSize", var5.gravelSize);
                var5.gravelCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "gravelCount", var5.gravelCount);
                var5.gravelMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "gravelMinHeight", var5.gravelMinHeight);
                var5.gravelMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "gravelMaxHeight", var5.gravelMaxHeight);
                var5.graniteSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "graniteSize", var5.graniteSize);
                var5.graniteCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "graniteCount", var5.graniteCount);
                var5.graniteMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "graniteMinHeight", var5.graniteMinHeight);
                var5.graniteMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "graniteMaxHeight", var5.graniteMaxHeight);
                var5.dioriteSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dioriteSize", var5.dioriteSize);
                var5.dioriteCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dioriteCount", var5.dioriteCount);
                var5.dioriteMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dioriteMinHeight", var5.dioriteMinHeight);
                var5.dioriteMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "dioriteMaxHeight", var5.dioriteMaxHeight);
                var5.andesiteSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "andesiteSize", var5.andesiteSize);
                var5.andesiteCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "andesiteCount", var5.andesiteCount);
                var5.andesiteMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "andesiteMinHeight", var5.andesiteMinHeight);
                var5.andesiteMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "andesiteMaxHeight", var5.andesiteMaxHeight);
                var5.coalSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "coalSize", var5.coalSize);
                var5.coalCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "coalCount", var5.coalCount);
                var5.coalMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "coalMinHeight", var5.coalMinHeight);
                var5.coalMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "coalMaxHeight", var5.coalMaxHeight);
                var5.ironSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "ironSize", var5.ironSize);
                var5.ironCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "ironCount", var5.ironCount);
                var5.ironMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "ironMinHeight", var5.ironMinHeight);
                var5.ironMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "ironMaxHeight", var5.ironMaxHeight);
                var5.goldSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "goldSize", var5.goldSize);
                var5.goldCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "goldCount", var5.goldCount);
                var5.goldMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "goldMinHeight", var5.goldMinHeight);
                var5.goldMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "goldMaxHeight", var5.goldMaxHeight);
                var5.redstoneSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "redstoneSize", var5.redstoneSize);
                var5.redstoneCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "redstoneCount", var5.redstoneCount);
                var5.redstoneMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "redstoneMinHeight", var5.redstoneMinHeight);
                var5.redstoneMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "redstoneMaxHeight", var5.redstoneMaxHeight);
                var5.diamondSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "diamondSize", var5.diamondSize);
                var5.diamondCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "diamondCount", var5.diamondCount);
                var5.diamondMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "diamondMinHeight", var5.diamondMinHeight);
                var5.diamondMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "diamondMaxHeight", var5.diamondMaxHeight);
                var5.lapisSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "lapisSize", var5.lapisSize);
                var5.lapisCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "lapisCount", var5.lapisCount);
                var5.lapisCenterHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "lapisCenterHeight", var5.lapisCenterHeight);
                var5.lapisSpread = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var4, "lapisSpread", var5.lapisSpread);
            }
            catch (Exception var7)
            {
                ;
            }

            return var5;
        }

        public JsonElement func_177862_a(ChunkProviderSettings.Factory p_177862_1_, Type p_177862_2_, JsonSerializationContext p_177862_3_)
        {
            JsonObject var4 = new JsonObject();
            var4.addProperty("coordinateScale", Float.valueOf(p_177862_1_.coordinateScale));
            var4.addProperty("heightScale", Float.valueOf(p_177862_1_.heightScale));
            var4.addProperty("lowerLimitScale", Float.valueOf(p_177862_1_.lowerLimitScale));
            var4.addProperty("upperLimitScale", Float.valueOf(p_177862_1_.upperLimitScale));
            var4.addProperty("depthNoiseScaleX", Float.valueOf(p_177862_1_.depthNoiseScaleX));
            var4.addProperty("depthNoiseScaleZ", Float.valueOf(p_177862_1_.depthNoiseScaleZ));
            var4.addProperty("depthNoiseScaleExponent", Float.valueOf(p_177862_1_.depthNoiseScaleExponent));
            var4.addProperty("mainNoiseScaleX", Float.valueOf(p_177862_1_.mainNoiseScaleX));
            var4.addProperty("mainNoiseScaleY", Float.valueOf(p_177862_1_.mainNoiseScaleY));
            var4.addProperty("mainNoiseScaleZ", Float.valueOf(p_177862_1_.mainNoiseScaleZ));
            var4.addProperty("baseSize", Float.valueOf(p_177862_1_.baseSize));
            var4.addProperty("stretchY", Float.valueOf(p_177862_1_.stretchY));
            var4.addProperty("biomeDepthWeight", Float.valueOf(p_177862_1_.biomeDepthWeight));
            var4.addProperty("biomeDepthOffset", Float.valueOf(p_177862_1_.biomeDepthOffset));
            var4.addProperty("biomeScaleWeight", Float.valueOf(p_177862_1_.biomeScaleWeight));
            var4.addProperty("biomeScaleOffset", Float.valueOf(p_177862_1_.biomeScaleOffset));
            var4.addProperty("seaLevel", Integer.valueOf(p_177862_1_.seaLevel));
            var4.addProperty("useCaves", Boolean.valueOf(p_177862_1_.useCaves));
            var4.addProperty("useDungeons", Boolean.valueOf(p_177862_1_.useDungeons));
            var4.addProperty("dungeonChance", Integer.valueOf(p_177862_1_.dungeonChance));
            var4.addProperty("useStrongholds", Boolean.valueOf(p_177862_1_.useStrongholds));
            var4.addProperty("useVillages", Boolean.valueOf(p_177862_1_.useVillages));
            var4.addProperty("useMineShafts", Boolean.valueOf(p_177862_1_.useMineShafts));
            var4.addProperty("useTemples", Boolean.valueOf(p_177862_1_.useTemples));
            var4.addProperty("useMonuments", Boolean.valueOf(p_177862_1_.useMonuments));
            var4.addProperty("useRavines", Boolean.valueOf(p_177862_1_.useRavines));
            var4.addProperty("useWaterLakes", Boolean.valueOf(p_177862_1_.useWaterLakes));
            var4.addProperty("waterLakeChance", Integer.valueOf(p_177862_1_.waterLakeChance));
            var4.addProperty("useLavaLakes", Boolean.valueOf(p_177862_1_.useLavaLakes));
            var4.addProperty("lavaLakeChance", Integer.valueOf(p_177862_1_.lavaLakeChance));
            var4.addProperty("useLavaOceans", Boolean.valueOf(p_177862_1_.useLavaOceans));
            var4.addProperty("fixedBiome", Integer.valueOf(p_177862_1_.fixedBiome));
            var4.addProperty("biomeSize", Integer.valueOf(p_177862_1_.biomeSize));
            var4.addProperty("riverSize", Integer.valueOf(p_177862_1_.riverSize));
            var4.addProperty("dirtSize", Integer.valueOf(p_177862_1_.dirtSize));
            var4.addProperty("dirtCount", Integer.valueOf(p_177862_1_.dirtCount));
            var4.addProperty("dirtMinHeight", Integer.valueOf(p_177862_1_.dirtMinHeight));
            var4.addProperty("dirtMaxHeight", Integer.valueOf(p_177862_1_.dirtMaxHeight));
            var4.addProperty("gravelSize", Integer.valueOf(p_177862_1_.gravelSize));
            var4.addProperty("gravelCount", Integer.valueOf(p_177862_1_.gravelCount));
            var4.addProperty("gravelMinHeight", Integer.valueOf(p_177862_1_.gravelMinHeight));
            var4.addProperty("gravelMaxHeight", Integer.valueOf(p_177862_1_.gravelMaxHeight));
            var4.addProperty("graniteSize", Integer.valueOf(p_177862_1_.graniteSize));
            var4.addProperty("graniteCount", Integer.valueOf(p_177862_1_.graniteCount));
            var4.addProperty("graniteMinHeight", Integer.valueOf(p_177862_1_.graniteMinHeight));
            var4.addProperty("graniteMaxHeight", Integer.valueOf(p_177862_1_.graniteMaxHeight));
            var4.addProperty("dioriteSize", Integer.valueOf(p_177862_1_.dioriteSize));
            var4.addProperty("dioriteCount", Integer.valueOf(p_177862_1_.dioriteCount));
            var4.addProperty("dioriteMinHeight", Integer.valueOf(p_177862_1_.dioriteMinHeight));
            var4.addProperty("dioriteMaxHeight", Integer.valueOf(p_177862_1_.dioriteMaxHeight));
            var4.addProperty("andesiteSize", Integer.valueOf(p_177862_1_.andesiteSize));
            var4.addProperty("andesiteCount", Integer.valueOf(p_177862_1_.andesiteCount));
            var4.addProperty("andesiteMinHeight", Integer.valueOf(p_177862_1_.andesiteMinHeight));
            var4.addProperty("andesiteMaxHeight", Integer.valueOf(p_177862_1_.andesiteMaxHeight));
            var4.addProperty("coalSize", Integer.valueOf(p_177862_1_.coalSize));
            var4.addProperty("coalCount", Integer.valueOf(p_177862_1_.coalCount));
            var4.addProperty("coalMinHeight", Integer.valueOf(p_177862_1_.coalMinHeight));
            var4.addProperty("coalMaxHeight", Integer.valueOf(p_177862_1_.coalMaxHeight));
            var4.addProperty("ironSize", Integer.valueOf(p_177862_1_.ironSize));
            var4.addProperty("ironCount", Integer.valueOf(p_177862_1_.ironCount));
            var4.addProperty("ironMinHeight", Integer.valueOf(p_177862_1_.ironMinHeight));
            var4.addProperty("ironMaxHeight", Integer.valueOf(p_177862_1_.ironMaxHeight));
            var4.addProperty("goldSize", Integer.valueOf(p_177862_1_.goldSize));
            var4.addProperty("goldCount", Integer.valueOf(p_177862_1_.goldCount));
            var4.addProperty("goldMinHeight", Integer.valueOf(p_177862_1_.goldMinHeight));
            var4.addProperty("goldMaxHeight", Integer.valueOf(p_177862_1_.goldMaxHeight));
            var4.addProperty("redstoneSize", Integer.valueOf(p_177862_1_.redstoneSize));
            var4.addProperty("redstoneCount", Integer.valueOf(p_177862_1_.redstoneCount));
            var4.addProperty("redstoneMinHeight", Integer.valueOf(p_177862_1_.redstoneMinHeight));
            var4.addProperty("redstoneMaxHeight", Integer.valueOf(p_177862_1_.redstoneMaxHeight));
            var4.addProperty("diamondSize", Integer.valueOf(p_177862_1_.diamondSize));
            var4.addProperty("diamondCount", Integer.valueOf(p_177862_1_.diamondCount));
            var4.addProperty("diamondMinHeight", Integer.valueOf(p_177862_1_.diamondMinHeight));
            var4.addProperty("diamondMaxHeight", Integer.valueOf(p_177862_1_.diamondMaxHeight));
            var4.addProperty("lapisSize", Integer.valueOf(p_177862_1_.lapisSize));
            var4.addProperty("lapisCount", Integer.valueOf(p_177862_1_.lapisCount));
            var4.addProperty("lapisCenterHeight", Integer.valueOf(p_177862_1_.lapisCenterHeight));
            var4.addProperty("lapisSpread", Integer.valueOf(p_177862_1_.lapisSpread));
            return var4;
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.func_177861_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }

        @Override
		public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return this.func_177862_a((ChunkProviderSettings.Factory)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
    }
}
