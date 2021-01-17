// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.util.WeightedRandom;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import java.util.Collections;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.MathHelper;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import java.util.Random;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public abstract class BiomeGenBase
{
    private static final Logger logger;
    protected static final Height height_Default;
    protected static final Height height_ShallowWaters;
    protected static final Height height_Oceans;
    protected static final Height height_DeepOceans;
    protected static final Height height_LowPlains;
    protected static final Height height_MidPlains;
    protected static final Height height_LowHills;
    protected static final Height height_HighPlateaus;
    protected static final Height height_MidHills;
    protected static final Height height_Shores;
    protected static final Height height_RockyWaters;
    protected static final Height height_LowIslands;
    protected static final Height height_PartiallySubmerged;
    private static final BiomeGenBase[] biomeList;
    public static final Set<BiomeGenBase> explorationBiomesList;
    public static final Map<String, BiomeGenBase> BIOME_ID_MAP;
    public static final BiomeGenBase ocean;
    public static final BiomeGenBase plains;
    public static final BiomeGenBase desert;
    public static final BiomeGenBase extremeHills;
    public static final BiomeGenBase forest;
    public static final BiomeGenBase taiga;
    public static final BiomeGenBase swampland;
    public static final BiomeGenBase river;
    public static final BiomeGenBase hell;
    public static final BiomeGenBase sky;
    public static final BiomeGenBase frozenOcean;
    public static final BiomeGenBase frozenRiver;
    public static final BiomeGenBase icePlains;
    public static final BiomeGenBase iceMountains;
    public static final BiomeGenBase mushroomIsland;
    public static final BiomeGenBase mushroomIslandShore;
    public static final BiomeGenBase beach;
    public static final BiomeGenBase desertHills;
    public static final BiomeGenBase forestHills;
    public static final BiomeGenBase taigaHills;
    public static final BiomeGenBase extremeHillsEdge;
    public static final BiomeGenBase jungle;
    public static final BiomeGenBase jungleHills;
    public static final BiomeGenBase jungleEdge;
    public static final BiomeGenBase deepOcean;
    public static final BiomeGenBase stoneBeach;
    public static final BiomeGenBase coldBeach;
    public static final BiomeGenBase birchForest;
    public static final BiomeGenBase birchForestHills;
    public static final BiomeGenBase roofedForest;
    public static final BiomeGenBase coldTaiga;
    public static final BiomeGenBase coldTaigaHills;
    public static final BiomeGenBase megaTaiga;
    public static final BiomeGenBase megaTaigaHills;
    public static final BiomeGenBase extremeHillsPlus;
    public static final BiomeGenBase savanna;
    public static final BiomeGenBase savannaPlateau;
    public static final BiomeGenBase mesa;
    public static final BiomeGenBase mesaPlateau_F;
    public static final BiomeGenBase mesaPlateau;
    public static final BiomeGenBase field_180279_ad;
    protected static final NoiseGeneratorPerlin temperatureNoise;
    protected static final NoiseGeneratorPerlin GRASS_COLOR_NOISE;
    protected static final WorldGenDoublePlant DOUBLE_PLANT_GENERATOR;
    public String biomeName;
    public int color;
    public int field_150609_ah;
    public IBlockState topBlock;
    public IBlockState fillerBlock;
    public int fillerBlockMetadata;
    public float minHeight;
    public float maxHeight;
    public float temperature;
    public float rainfall;
    public int waterColorMultiplier;
    public BiomeDecorator theBiomeDecorator;
    protected List<SpawnListEntry> spawnableMonsterList;
    protected List<SpawnListEntry> spawnableCreatureList;
    protected List<SpawnListEntry> spawnableWaterCreatureList;
    protected List<SpawnListEntry> spawnableCaveCreatureList;
    protected boolean enableSnow;
    protected boolean enableRain;
    public final int biomeID;
    protected WorldGenTrees worldGeneratorTrees;
    protected WorldGenBigTree worldGeneratorBigTree;
    protected WorldGenSwamp worldGeneratorSwamp;
    
    static {
        logger = LogManager.getLogger();
        height_Default = new Height(0.1f, 0.2f);
        height_ShallowWaters = new Height(-0.5f, 0.0f);
        height_Oceans = new Height(-1.0f, 0.1f);
        height_DeepOceans = new Height(-1.8f, 0.1f);
        height_LowPlains = new Height(0.125f, 0.05f);
        height_MidPlains = new Height(0.2f, 0.2f);
        height_LowHills = new Height(0.45f, 0.3f);
        height_HighPlateaus = new Height(1.5f, 0.025f);
        height_MidHills = new Height(1.0f, 0.5f);
        height_Shores = new Height(0.0f, 0.025f);
        height_RockyWaters = new Height(0.1f, 0.8f);
        height_LowIslands = new Height(0.2f, 0.3f);
        height_PartiallySubmerged = new Height(-0.2f, 0.1f);
        biomeList = new BiomeGenBase[256];
        explorationBiomesList = Sets.newHashSet();
        BIOME_ID_MAP = Maps.newHashMap();
        ocean = new BiomeGenOcean(0).setColor(112).setBiomeName("Ocean").setHeight(BiomeGenBase.height_Oceans);
        plains = new BiomeGenPlains(1).setColor(9286496).setBiomeName("Plains");
        desert = new BiomeGenDesert(2).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0f, 0.0f).setHeight(BiomeGenBase.height_LowPlains);
        extremeHills = new BiomeGenHills(3, false).setColor(6316128).setBiomeName("Extreme Hills").setHeight(BiomeGenBase.height_MidHills).setTemperatureRainfall(0.2f, 0.3f);
        forest = new BiomeGenForest(4, 0).setColor(353825).setBiomeName("Forest");
        taiga = new BiomeGenTaiga(5, 0).setColor(747097).setBiomeName("Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(BiomeGenBase.height_MidPlains);
        swampland = new BiomeGenSwamp(6).setColor(522674).setBiomeName("Swampland").setFillerBlockMetadata(9154376).setHeight(BiomeGenBase.height_PartiallySubmerged).setTemperatureRainfall(0.8f, 0.9f);
        river = new BiomeGenRiver(7).setColor(255).setBiomeName("River").setHeight(BiomeGenBase.height_ShallowWaters);
        hell = new BiomeGenHell(8).setColor(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0f, 0.0f);
        sky = new BiomeGenEnd(9).setColor(8421631).setBiomeName("The End").setDisableRain();
        frozenOcean = new BiomeGenOcean(10).setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow().setHeight(BiomeGenBase.height_Oceans).setTemperatureRainfall(0.0f, 0.5f);
        frozenRiver = new BiomeGenRiver(11).setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow().setHeight(BiomeGenBase.height_ShallowWaters).setTemperatureRainfall(0.0f, 0.5f);
        icePlains = new BiomeGenSnow(12, false).setColor(16777215).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0f, 0.5f).setHeight(BiomeGenBase.height_LowPlains);
        iceMountains = new BiomeGenSnow(13, false).setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow().setHeight(BiomeGenBase.height_LowHills).setTemperatureRainfall(0.0f, 0.5f);
        mushroomIsland = new BiomeGenMushroomIsland(14).setColor(16711935).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9f, 1.0f).setHeight(BiomeGenBase.height_LowIslands);
        mushroomIslandShore = new BiomeGenMushroomIsland(15).setColor(10486015).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9f, 1.0f).setHeight(BiomeGenBase.height_Shores);
        beach = new BiomeGenBeach(16).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8f, 0.4f).setHeight(BiomeGenBase.height_Shores);
        desertHills = new BiomeGenDesert(17).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0f, 0.0f).setHeight(BiomeGenBase.height_LowHills);
        forestHills = new BiomeGenForest(18, 0).setColor(2250012).setBiomeName("ForestHills").setHeight(BiomeGenBase.height_LowHills);
        taigaHills = new BiomeGenTaiga(19, 0).setColor(1456435).setBiomeName("TaigaHills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25f, 0.8f).setHeight(BiomeGenBase.height_LowHills);
        extremeHillsEdge = new BiomeGenHills(20, true).setColor(7501978).setBiomeName("Extreme Hills Edge").setHeight(BiomeGenBase.height_MidHills.attenuate()).setTemperatureRainfall(0.2f, 0.3f);
        jungle = new BiomeGenJungle(21, false).setColor(5470985).setBiomeName("Jungle").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.9f);
        jungleHills = new BiomeGenJungle(22, false).setColor(2900485).setBiomeName("JungleHills").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.9f).setHeight(BiomeGenBase.height_LowHills);
        jungleEdge = new BiomeGenJungle(23, true).setColor(6458135).setBiomeName("JungleEdge").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95f, 0.8f);
        deepOcean = new BiomeGenOcean(24).setColor(48).setBiomeName("Deep Ocean").setHeight(BiomeGenBase.height_DeepOceans);
        stoneBeach = new BiomeGenStoneBeach(25).setColor(10658436).setBiomeName("Stone Beach").setTemperatureRainfall(0.2f, 0.3f).setHeight(BiomeGenBase.height_RockyWaters);
        coldBeach = new BiomeGenBeach(26).setColor(16445632).setBiomeName("Cold Beach").setTemperatureRainfall(0.05f, 0.3f).setHeight(BiomeGenBase.height_Shores).setEnableSnow();
        birchForest = new BiomeGenForest(27, 2).setBiomeName("Birch Forest").setColor(3175492);
        birchForestHills = new BiomeGenForest(28, 2).setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(BiomeGenBase.height_LowHills);
        roofedForest = new BiomeGenForest(29, 3).setColor(4215066).setBiomeName("Roofed Forest");
        coldTaiga = new BiomeGenTaiga(30, 0).setColor(3233098).setBiomeName("Cold Taiga").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5f, 0.4f).setHeight(BiomeGenBase.height_MidPlains).func_150563_c(16777215);
        coldTaigaHills = new BiomeGenTaiga(31, 0).setColor(2375478).setBiomeName("Cold Taiga Hills").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5f, 0.4f).setHeight(BiomeGenBase.height_LowHills).func_150563_c(16777215);
        megaTaiga = new BiomeGenTaiga(32, 1).setColor(5858897).setBiomeName("Mega Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3f, 0.8f).setHeight(BiomeGenBase.height_MidPlains);
        megaTaigaHills = new BiomeGenTaiga(33, 1).setColor(4542270).setBiomeName("Mega Taiga Hills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3f, 0.8f).setHeight(BiomeGenBase.height_LowHills);
        extremeHillsPlus = new BiomeGenHills(34, true).setColor(5271632).setBiomeName("Extreme Hills+").setHeight(BiomeGenBase.height_MidHills).setTemperatureRainfall(0.2f, 0.3f);
        savanna = new BiomeGenSavanna(35).setColor(12431967).setBiomeName("Savanna").setTemperatureRainfall(1.2f, 0.0f).setDisableRain().setHeight(BiomeGenBase.height_LowPlains);
        savannaPlateau = new BiomeGenSavanna(36).setColor(10984804).setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0f, 0.0f).setDisableRain().setHeight(BiomeGenBase.height_HighPlateaus);
        mesa = new BiomeGenMesa(37, false, false).setColor(14238997).setBiomeName("Mesa");
        mesaPlateau_F = new BiomeGenMesa(38, false, true).setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(BiomeGenBase.height_HighPlateaus);
        mesaPlateau = new BiomeGenMesa(39, false, false).setColor(13274213).setBiomeName("Mesa Plateau").setHeight(BiomeGenBase.height_HighPlateaus);
        field_180279_ad = BiomeGenBase.ocean;
        BiomeGenBase.plains.createMutation();
        BiomeGenBase.desert.createMutation();
        BiomeGenBase.forest.createMutation();
        BiomeGenBase.taiga.createMutation();
        BiomeGenBase.swampland.createMutation();
        BiomeGenBase.icePlains.createMutation();
        BiomeGenBase.jungle.createMutation();
        BiomeGenBase.jungleEdge.createMutation();
        BiomeGenBase.coldTaiga.createMutation();
        BiomeGenBase.savanna.createMutation();
        BiomeGenBase.savannaPlateau.createMutation();
        BiomeGenBase.mesa.createMutation();
        BiomeGenBase.mesaPlateau_F.createMutation();
        BiomeGenBase.mesaPlateau.createMutation();
        BiomeGenBase.birchForest.createMutation();
        BiomeGenBase.birchForestHills.createMutation();
        BiomeGenBase.roofedForest.createMutation();
        BiomeGenBase.megaTaiga.createMutation();
        BiomeGenBase.extremeHills.createMutation();
        BiomeGenBase.extremeHillsPlus.createMutation();
        BiomeGenBase.megaTaiga.createMutatedBiome(BiomeGenBase.megaTaigaHills.biomeID + 128).setBiomeName("Redwood Taiga Hills M");
        BiomeGenBase[] biomeList2;
        for (int length = (biomeList2 = BiomeGenBase.biomeList).length, i = 0; i < length; ++i) {
            final BiomeGenBase biomegenbase = biomeList2[i];
            if (biomegenbase != null) {
                if (BiomeGenBase.BIOME_ID_MAP.containsKey(biomegenbase.biomeName)) {
                    throw new Error("Biome \"" + biomegenbase.biomeName + "\" is defined as both ID " + BiomeGenBase.BIOME_ID_MAP.get(biomegenbase.biomeName).biomeID + " and " + biomegenbase.biomeID);
                }
                BiomeGenBase.BIOME_ID_MAP.put(biomegenbase.biomeName, biomegenbase);
                if (biomegenbase.biomeID < 128) {
                    BiomeGenBase.explorationBiomesList.add(biomegenbase);
                }
            }
        }
        BiomeGenBase.explorationBiomesList.remove(BiomeGenBase.hell);
        BiomeGenBase.explorationBiomesList.remove(BiomeGenBase.sky);
        BiomeGenBase.explorationBiomesList.remove(BiomeGenBase.frozenOcean);
        BiomeGenBase.explorationBiomesList.remove(BiomeGenBase.extremeHillsEdge);
        temperatureNoise = new NoiseGeneratorPerlin(new Random(1234L), 1);
        GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);
        DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant();
    }
    
    protected BiomeGenBase(final int id) {
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        this.fillerBlockMetadata = 5169201;
        this.minHeight = BiomeGenBase.height_Default.rootHeight;
        this.maxHeight = BiomeGenBase.height_Default.variation;
        this.temperature = 0.5f;
        this.rainfall = 0.5f;
        this.waterColorMultiplier = 16777215;
        this.spawnableMonsterList = (List<SpawnListEntry>)Lists.newArrayList();
        this.spawnableCreatureList = (List<SpawnListEntry>)Lists.newArrayList();
        this.spawnableWaterCreatureList = (List<SpawnListEntry>)Lists.newArrayList();
        this.spawnableCaveCreatureList = (List<SpawnListEntry>)Lists.newArrayList();
        this.enableRain = true;
        this.worldGeneratorTrees = new WorldGenTrees(false);
        this.worldGeneratorBigTree = new WorldGenBigTree(false);
        this.worldGeneratorSwamp = new WorldGenSwamp();
        this.biomeID = id;
        BiomeGenBase.biomeList[id] = this;
        this.theBiomeDecorator = this.createBiomeDecorator();
        this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityRabbit.class, 10, 3, 3));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityWitch.class, 5, 1, 1));
        this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
        this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityBat.class, 10, 8, 8));
    }
    
    protected BiomeDecorator createBiomeDecorator() {
        return new BiomeDecorator();
    }
    
    protected BiomeGenBase setTemperatureRainfall(final float temperatureIn, final float rainfallIn) {
        if (temperatureIn > 0.1f && temperatureIn < 0.2f) {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        this.temperature = temperatureIn;
        this.rainfall = rainfallIn;
        return this;
    }
    
    protected final BiomeGenBase setHeight(final Height heights) {
        this.minHeight = heights.rootHeight;
        this.maxHeight = heights.variation;
        return this;
    }
    
    protected BiomeGenBase setDisableRain() {
        this.enableRain = false;
        return this;
    }
    
    public WorldGenAbstractTree genBigTreeChance(final Random rand) {
        return (rand.nextInt(10) == 0) ? this.worldGeneratorBigTree : this.worldGeneratorTrees;
    }
    
    public WorldGenerator getRandomWorldGenForGrass(final Random rand) {
        return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    
    public BlockFlower.EnumFlowerType pickRandomFlower(final Random rand, final BlockPos pos) {
        return (rand.nextInt(3) > 0) ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
    }
    
    protected BiomeGenBase setEnableSnow() {
        this.enableSnow = true;
        return this;
    }
    
    protected BiomeGenBase setBiomeName(final String name) {
        this.biomeName = name;
        return this;
    }
    
    protected BiomeGenBase setFillerBlockMetadata(final int meta) {
        this.fillerBlockMetadata = meta;
        return this;
    }
    
    protected BiomeGenBase setColor(final int colorIn) {
        this.func_150557_a(colorIn, false);
        return this;
    }
    
    protected BiomeGenBase func_150563_c(final int p_150563_1_) {
        this.field_150609_ah = p_150563_1_;
        return this;
    }
    
    protected BiomeGenBase func_150557_a(final int p_150557_1_, final boolean p_150557_2_) {
        this.color = p_150557_1_;
        if (p_150557_2_) {
            this.field_150609_ah = (p_150557_1_ & 0xFEFEFE) >> 1;
        }
        else {
            this.field_150609_ah = p_150557_1_;
        }
        return this;
    }
    
    public int getSkyColorByTemp(float p_76731_1_) {
        p_76731_1_ /= 3.0f;
        p_76731_1_ = MathHelper.clamp_float(p_76731_1_, -1.0f, 1.0f);
        return MathHelper.func_181758_c(0.62222224f - p_76731_1_ * 0.05f, 0.5f + p_76731_1_ * 0.1f, 1.0f);
    }
    
    public List<SpawnListEntry> getSpawnableList(final EnumCreatureType creatureType) {
        switch (creatureType) {
            case MONSTER: {
                return this.spawnableMonsterList;
            }
            case CREATURE: {
                return this.spawnableCreatureList;
            }
            case WATER_CREATURE: {
                return this.spawnableWaterCreatureList;
            }
            case AMBIENT: {
                return this.spawnableCaveCreatureList;
            }
            default: {
                return Collections.emptyList();
            }
        }
    }
    
    public boolean getEnableSnow() {
        return this.isSnowyBiome();
    }
    
    public boolean canSpawnLightningBolt() {
        return !this.isSnowyBiome() && this.enableRain;
    }
    
    public boolean isHighHumidity() {
        return this.rainfall > 0.85f;
    }
    
    public float getSpawningChance() {
        return 0.1f;
    }
    
    public final int getIntRainfall() {
        return (int)(this.rainfall * 65536.0f);
    }
    
    public final float getFloatRainfall() {
        return this.rainfall;
    }
    
    public final float getFloatTemperature(final BlockPos pos) {
        if (pos.getY() > 64) {
            final float f = (float)(BiomeGenBase.temperatureNoise.func_151601_a(pos.getX() * 1.0 / 8.0, pos.getZ() * 1.0 / 8.0) * 4.0);
            return this.temperature - (f + pos.getY() - 64.0f) * 0.05f / 30.0f;
        }
        return this.temperature;
    }
    
    public void decorate(final World worldIn, final Random rand, final BlockPos pos) {
        this.theBiomeDecorator.decorate(worldIn, rand, this, pos);
    }
    
    public int getGrassColorAtPos(final BlockPos pos) {
        final double d0 = MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0f, 1.0f);
        final double d2 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0f, 1.0f);
        return ColorizerGrass.getGrassColor(d0, d2);
    }
    
    public int getFoliageColorAtPos(final BlockPos pos) {
        final double d0 = MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0f, 1.0f);
        final double d2 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0f, 1.0f);
        return ColorizerFoliage.getFoliageColor(d0, d2);
    }
    
    public boolean isSnowyBiome() {
        return this.enableSnow;
    }
    
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int p_180622_4_, final int p_180622_5_, final double p_180622_6_) {
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
    }
    
    public final void generateBiomeTerrain(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int p_180628_4_, final int p_180628_5_, final double p_180628_6_) {
        final int i = worldIn.func_181545_F();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate2 = this.fillerBlock;
        int j = -1;
        final int k = (int)(p_180628_6_ / 3.0 + 3.0 + rand.nextDouble() * 0.25);
        final int l = p_180628_4_ & 0xF;
        final int i2 = p_180628_5_ & 0xF;
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j2 = 255; j2 >= 0; --j2) {
            if (j2 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i2, j2, l, Blocks.bedrock.getDefaultState());
            }
            else {
                final IBlockState iblockstate3 = chunkPrimerIn.getBlockState(i2, j2, l);
                if (iblockstate3.getBlock().getMaterial() == Material.air) {
                    j = -1;
                }
                else if (iblockstate3.getBlock() == Blocks.stone) {
                    if (j == -1) {
                        if (k <= 0) {
                            iblockstate = null;
                            iblockstate2 = Blocks.stone.getDefaultState();
                        }
                        else if (j2 >= i - 4 && j2 <= i + 1) {
                            iblockstate = this.topBlock;
                            iblockstate2 = this.fillerBlock;
                        }
                        if (j2 < i && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air)) {
                            if (this.getFloatTemperature(blockpos$mutableblockpos.func_181079_c(p_180628_4_, j2, p_180628_5_)) < 0.15f) {
                                iblockstate = Blocks.ice.getDefaultState();
                            }
                            else {
                                iblockstate = Blocks.water.getDefaultState();
                            }
                        }
                        j = k;
                        if (j2 >= i - 1) {
                            chunkPrimerIn.setBlockState(i2, j2, l, iblockstate);
                        }
                        else if (j2 < i - 7 - k) {
                            iblockstate = null;
                            iblockstate2 = Blocks.stone.getDefaultState();
                            chunkPrimerIn.setBlockState(i2, j2, l, Blocks.gravel.getDefaultState());
                        }
                        else {
                            chunkPrimerIn.setBlockState(i2, j2, l, iblockstate2);
                        }
                    }
                    else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i2, j2, l, iblockstate2);
                        if (j == 0 && iblockstate2.getBlock() == Blocks.sand) {
                            j = rand.nextInt(4) + Math.max(0, j2 - 63);
                            iblockstate2 = ((iblockstate2.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND) ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
                        }
                    }
                }
            }
        }
    }
    
    protected BiomeGenBase createMutation() {
        return this.createMutatedBiome(this.biomeID + 128);
    }
    
    protected BiomeGenBase createMutatedBiome(final int p_180277_1_) {
        return new BiomeGenMutated(p_180277_1_, this);
    }
    
    public Class<? extends BiomeGenBase> getBiomeClass() {
        return this.getClass();
    }
    
    public boolean isEqualTo(final BiomeGenBase biome) {
        return biome == this || (biome != null && this.getBiomeClass() == biome.getBiomeClass());
    }
    
    public TempCategory getTempCategory() {
        return (this.temperature < 0.2) ? TempCategory.COLD : ((this.temperature < 1.0) ? TempCategory.MEDIUM : TempCategory.WARM);
    }
    
    public static BiomeGenBase[] getBiomeGenArray() {
        return BiomeGenBase.biomeList;
    }
    
    public static BiomeGenBase getBiome(final int id) {
        return getBiomeFromBiomeList(id, null);
    }
    
    public static BiomeGenBase getBiomeFromBiomeList(final int biomeId, final BiomeGenBase biome) {
        if (biomeId >= 0 && biomeId <= BiomeGenBase.biomeList.length) {
            final BiomeGenBase biomegenbase = BiomeGenBase.biomeList[biomeId];
            return (biomegenbase == null) ? biome : biomegenbase;
        }
        BiomeGenBase.logger.warn("Biome ID is out of bounds: " + biomeId + ", defaulting to 0 (Ocean)");
        return BiomeGenBase.ocean;
    }
    
    public enum TempCategory
    {
        OCEAN("OCEAN", 0), 
        COLD("COLD", 1), 
        MEDIUM("MEDIUM", 2), 
        WARM("WARM", 3);
        
        private TempCategory(final String name, final int ordinal) {
        }
    }
    
    public static class Height
    {
        public float rootHeight;
        public float variation;
        
        public Height(final float rootHeightIn, final float variationIn) {
            this.rootHeight = rootHeightIn;
            this.variation = variationIn;
        }
        
        public Height attenuate() {
            return new Height(this.rootHeight * 0.8f, this.variation * 0.6f);
        }
    }
    
    public static class SpawnListEntry extends WeightedRandom.Item
    {
        public Class<? extends EntityLiving> entityClass;
        public int minGroupCount;
        public int maxGroupCount;
        
        public SpawnListEntry(final Class<? extends EntityLiving> entityclassIn, final int weight, final int groupCountMin, final int groupCountMax) {
            super(weight);
            this.entityClass = entityclassIn;
            this.minGroupCount = groupCountMin;
            this.maxGroupCount = groupCountMax;
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.entityClass.getSimpleName()) + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }
    }
}
