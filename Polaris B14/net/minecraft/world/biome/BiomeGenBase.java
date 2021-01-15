/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*     */ import net.minecraft.block.BlockGrass;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockSand.EnumType;
/*     */ import net.minecraft.block.BlockStaticLiquid;
/*     */ import net.minecraft.block.BlockTallGrass.EnumType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.entity.monster.EntityCreeper;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntitySlime;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityBat;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.WeightedRandom.Item;
/*     */ import net.minecraft.world.ColorizerGrass;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.NoiseGeneratorPerlin;
/*     */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenBigTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenDoublePlant;
/*     */ import net.minecraft.world.gen.feature.WorldGenSwamp;
/*     */ import net.minecraft.world.gen.feature.WorldGenTallGrass;
/*     */ import net.minecraft.world.gen.feature.WorldGenTrees;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class BiomeGenBase
/*     */ {
/*  53 */   private static final Logger logger = ;
/*  54 */   protected static final Height height_Default = new Height(0.1F, 0.2F);
/*  55 */   protected static final Height height_ShallowWaters = new Height(-0.5F, 0.0F);
/*  56 */   protected static final Height height_Oceans = new Height(-1.0F, 0.1F);
/*  57 */   protected static final Height height_DeepOceans = new Height(-1.8F, 0.1F);
/*  58 */   protected static final Height height_LowPlains = new Height(0.125F, 0.05F);
/*  59 */   protected static final Height height_MidPlains = new Height(0.2F, 0.2F);
/*  60 */   protected static final Height height_LowHills = new Height(0.45F, 0.3F);
/*  61 */   protected static final Height height_HighPlateaus = new Height(1.5F, 0.025F);
/*  62 */   protected static final Height height_MidHills = new Height(1.0F, 0.5F);
/*  63 */   protected static final Height height_Shores = new Height(0.0F, 0.025F);
/*  64 */   protected static final Height height_RockyWaters = new Height(0.1F, 0.8F);
/*  65 */   protected static final Height height_LowIslands = new Height(0.2F, 0.3F);
/*  66 */   protected static final Height height_PartiallySubmerged = new Height(-0.2F, 0.1F);
/*     */   
/*     */ 
/*  69 */   private static final BiomeGenBase[] biomeList = new BiomeGenBase['Ā'];
/*  70 */   public static final Set<BiomeGenBase> explorationBiomesList = com.google.common.collect.Sets.newHashSet();
/*  71 */   public static final Map<String, BiomeGenBase> BIOME_ID_MAP = Maps.newHashMap();
/*  72 */   public static final BiomeGenBase ocean = new BiomeGenOcean(0).setColor(112).setBiomeName("Ocean").setHeight(height_Oceans);
/*  73 */   public static final BiomeGenBase plains = new BiomeGenPlains(1).setColor(9286496).setBiomeName("Plains");
/*  74 */   public static final BiomeGenBase desert = new BiomeGenDesert(2).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowPlains);
/*  75 */   public static final BiomeGenBase extremeHills = new BiomeGenHills(3, false).setColor(6316128).setBiomeName("Extreme Hills").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
/*  76 */   public static final BiomeGenBase forest = new BiomeGenForest(4, 0).setColor(353825).setBiomeName("Forest");
/*  77 */   public static final BiomeGenBase taiga = new BiomeGenTaiga(5, 0).setColor(747097).setBiomeName("Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_MidPlains);
/*  78 */   public static final BiomeGenBase swampland = new BiomeGenSwamp(6).setColor(522674).setBiomeName("Swampland").setFillerBlockMetadata(9154376).setHeight(height_PartiallySubmerged).setTemperatureRainfall(0.8F, 0.9F);
/*  79 */   public static final BiomeGenBase river = new BiomeGenRiver(7).setColor(255).setBiomeName("River").setHeight(height_ShallowWaters);
/*  80 */   public static final BiomeGenBase hell = new BiomeGenHell(8).setColor(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);
/*     */   
/*     */ 
/*  83 */   public static final BiomeGenBase sky = new BiomeGenEnd(9).setColor(8421631).setBiomeName("The End").setDisableRain();
/*  84 */   public static final BiomeGenBase frozenOcean = new BiomeGenOcean(10).setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow().setHeight(height_Oceans).setTemperatureRainfall(0.0F, 0.5F);
/*  85 */   public static final BiomeGenBase frozenRiver = new BiomeGenRiver(11).setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow().setHeight(height_ShallowWaters).setTemperatureRainfall(0.0F, 0.5F);
/*  86 */   public static final BiomeGenBase icePlains = new BiomeGenSnow(12, false).setColor(16777215).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(height_LowPlains);
/*  87 */   public static final BiomeGenBase iceMountains = new BiomeGenSnow(13, false).setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow().setHeight(height_LowHills).setTemperatureRainfall(0.0F, 0.5F);
/*  88 */   public static final BiomeGenBase mushroomIsland = new BiomeGenMushroomIsland(14).setColor(16711935).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_LowIslands);
/*  89 */   public static final BiomeGenBase mushroomIslandShore = new BiomeGenMushroomIsland(15).setColor(10486015).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_Shores);
/*     */   
/*     */ 
/*  92 */   public static final BiomeGenBase beach = new BiomeGenBeach(16).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setHeight(height_Shores);
/*     */   
/*     */ 
/*  95 */   public static final BiomeGenBase desertHills = new BiomeGenDesert(17).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowHills);
/*     */   
/*     */ 
/*  98 */   public static final BiomeGenBase forestHills = new BiomeGenForest(18, 0).setColor(2250012).setBiomeName("ForestHills").setHeight(height_LowHills);
/*     */   
/*     */ 
/* 101 */   public static final BiomeGenBase taigaHills = new BiomeGenTaiga(19, 0).setColor(1456435).setBiomeName("TaigaHills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_LowHills);
/*     */   
/*     */ 
/* 104 */   public static final BiomeGenBase extremeHillsEdge = new BiomeGenHills(20, true).setColor(7501978).setBiomeName("Extreme Hills Edge").setHeight(height_MidHills.attenuate()).setTemperatureRainfall(0.2F, 0.3F);
/*     */   
/*     */ 
/* 107 */   public static final BiomeGenBase jungle = new BiomeGenJungle(21, false).setColor(5470985).setBiomeName("Jungle").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F);
/* 108 */   public static final BiomeGenBase jungleHills = new BiomeGenJungle(22, false).setColor(2900485).setBiomeName("JungleHills").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F).setHeight(height_LowHills);
/* 109 */   public static final BiomeGenBase jungleEdge = new BiomeGenJungle(23, true).setColor(6458135).setBiomeName("JungleEdge").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.8F);
/* 110 */   public static final BiomeGenBase deepOcean = new BiomeGenOcean(24).setColor(48).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
/* 111 */   public static final BiomeGenBase stoneBeach = new BiomeGenStoneBeach(25).setColor(10658436).setBiomeName("Stone Beach").setTemperatureRainfall(0.2F, 0.3F).setHeight(height_RockyWaters);
/* 112 */   public static final BiomeGenBase coldBeach = new BiomeGenBeach(26).setColor(16445632).setBiomeName("Cold Beach").setTemperatureRainfall(0.05F, 0.3F).setHeight(height_Shores).setEnableSnow();
/* 113 */   public static final BiomeGenBase birchForest = new BiomeGenForest(27, 2).setBiomeName("Birch Forest").setColor(3175492);
/* 114 */   public static final BiomeGenBase birchForestHills = new BiomeGenForest(28, 2).setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(height_LowHills);
/* 115 */   public static final BiomeGenBase roofedForest = new BiomeGenForest(29, 3).setColor(4215066).setBiomeName("Roofed Forest");
/* 116 */   public static final BiomeGenBase coldTaiga = new BiomeGenTaiga(30, 0).setColor(3233098).setBiomeName("Cold Taiga").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_MidPlains).func_150563_c(16777215);
/* 117 */   public static final BiomeGenBase coldTaigaHills = new BiomeGenTaiga(31, 0).setColor(2375478).setBiomeName("Cold Taiga Hills").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_LowHills).func_150563_c(16777215);
/* 118 */   public static final BiomeGenBase megaTaiga = new BiomeGenTaiga(32, 1).setColor(5858897).setBiomeName("Mega Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_MidPlains);
/* 119 */   public static final BiomeGenBase megaTaigaHills = new BiomeGenTaiga(33, 1).setColor(4542270).setBiomeName("Mega Taiga Hills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_LowHills);
/* 120 */   public static final BiomeGenBase extremeHillsPlus = new BiomeGenHills(34, true).setColor(5271632).setBiomeName("Extreme Hills+").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
/* 121 */   public static final BiomeGenBase savanna = new BiomeGenSavanna(35).setColor(12431967).setBiomeName("Savanna").setTemperatureRainfall(1.2F, 0.0F).setDisableRain().setHeight(height_LowPlains);
/* 122 */   public static final BiomeGenBase savannaPlateau = new BiomeGenSavanna(36).setColor(10984804).setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0F, 0.0F).setDisableRain().setHeight(height_HighPlateaus);
/* 123 */   public static final BiomeGenBase mesa = new BiomeGenMesa(37, false, false).setColor(14238997).setBiomeName("Mesa");
/* 124 */   public static final BiomeGenBase mesaPlateau_F = new BiomeGenMesa(38, false, true).setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(height_HighPlateaus);
/* 125 */   public static final BiomeGenBase mesaPlateau = new BiomeGenMesa(39, false, false).setColor(13274213).setBiomeName("Mesa Plateau").setHeight(height_HighPlateaus);
/* 126 */   public static final BiomeGenBase field_180279_ad = ocean;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */   public IBlockState topBlock = Blocks.grass.getDefaultState();
/*     */   
/*     */ 
/* 138 */   public IBlockState fillerBlock = Blocks.dirt.getDefaultState();
/* 139 */   public int fillerBlockMetadata = 5169201;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BiomeGenBase(int id)
/*     */   {
/* 185 */     this.minHeight = height_Default.rootHeight;
/* 186 */     this.maxHeight = height_Default.variation;
/* 187 */     this.temperature = 0.5F;
/* 188 */     this.rainfall = 0.5F;
/* 189 */     this.waterColorMultiplier = 16777215;
/* 190 */     this.spawnableMonsterList = Lists.newArrayList();
/* 191 */     this.spawnableCreatureList = Lists.newArrayList();
/* 192 */     this.spawnableWaterCreatureList = Lists.newArrayList();
/* 193 */     this.spawnableCaveCreatureList = Lists.newArrayList();
/* 194 */     this.enableRain = true;
/* 195 */     this.worldGeneratorTrees = new WorldGenTrees(false);
/* 196 */     this.worldGeneratorBigTree = new WorldGenBigTree(false);
/* 197 */     this.worldGeneratorSwamp = new WorldGenSwamp();
/* 198 */     this.biomeID = id;
/* 199 */     biomeList[id] = this;
/* 200 */     this.theBiomeDecorator = createBiomeDecorator();
/* 201 */     this.spawnableCreatureList.add(new SpawnListEntry(net.minecraft.entity.passive.EntitySheep.class, 12, 4, 4));
/* 202 */     this.spawnableCreatureList.add(new SpawnListEntry(net.minecraft.entity.passive.EntityRabbit.class, 10, 3, 3));
/* 203 */     this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
/* 204 */     this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
/* 205 */     this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
/* 206 */     this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 100, 4, 4));
/* 207 */     this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 100, 4, 4));
/* 208 */     this.spawnableMonsterList.add(new SpawnListEntry(net.minecraft.entity.monster.EntitySkeleton.class, 100, 4, 4));
/* 209 */     this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 100, 4, 4));
/* 210 */     this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 100, 4, 4));
/* 211 */     this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 10, 1, 4));
/* 212 */     this.spawnableMonsterList.add(new SpawnListEntry(EntityWitch.class, 5, 1, 1));
/* 213 */     this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
/* 214 */     this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityBat.class, 10, 8, 8));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BiomeDecorator createBiomeDecorator()
/*     */   {
/* 222 */     return new BiomeDecorator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BiomeGenBase setTemperatureRainfall(float temperatureIn, float rainfallIn)
/*     */   {
/* 230 */     if ((temperatureIn > 0.1F) && (temperatureIn < 0.2F))
/*     */     {
/* 232 */       throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
/*     */     }
/*     */     
/*     */ 
/* 236 */     this.temperature = temperatureIn;
/* 237 */     this.rainfall = rainfallIn;
/* 238 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final BiomeGenBase setHeight(Height heights)
/*     */   {
/* 244 */     this.minHeight = heights.rootHeight;
/* 245 */     this.maxHeight = heights.variation;
/* 246 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BiomeGenBase setDisableRain()
/*     */   {
/* 254 */     this.enableRain = false;
/* 255 */     return this;
/*     */   }
/*     */   
/*     */   public WorldGenAbstractTree genBigTreeChance(Random rand)
/*     */   {
/* 260 */     return rand.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WorldGenerator getRandomWorldGenForGrass(Random rand)
/*     */   {
/* 268 */     return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
/*     */   }
/*     */   
/*     */   public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
/*     */   {
/* 273 */     return rand.nextInt(3) > 0 ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BiomeGenBase setEnableSnow()
/*     */   {
/* 281 */     this.enableSnow = true;
/* 282 */     return this;
/*     */   }
/*     */   
/*     */   protected BiomeGenBase setBiomeName(String name)
/*     */   {
/* 287 */     this.biomeName = name;
/* 288 */     return this;
/*     */   }
/*     */   
/*     */   protected BiomeGenBase setFillerBlockMetadata(int meta)
/*     */   {
/* 293 */     this.fillerBlockMetadata = meta;
/* 294 */     return this;
/*     */   }
/*     */   
/*     */   protected BiomeGenBase setColor(int colorIn)
/*     */   {
/* 299 */     func_150557_a(colorIn, false);
/* 300 */     return this;
/*     */   }
/*     */   
/*     */   protected BiomeGenBase func_150563_c(int p_150563_1_)
/*     */   {
/* 305 */     this.field_150609_ah = p_150563_1_;
/* 306 */     return this;
/*     */   }
/*     */   
/*     */   protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_)
/*     */   {
/* 311 */     this.color = p_150557_1_;
/*     */     
/* 313 */     if (p_150557_2_)
/*     */     {
/* 315 */       this.field_150609_ah = ((p_150557_1_ & 0xFEFEFE) >> 1);
/*     */     }
/*     */     else
/*     */     {
/* 319 */       this.field_150609_ah = p_150557_1_;
/*     */     }
/*     */     
/* 322 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSkyColorByTemp(float p_76731_1_)
/*     */   {
/* 330 */     p_76731_1_ /= 3.0F;
/* 331 */     p_76731_1_ = MathHelper.clamp_float(p_76731_1_, -1.0F, 1.0F);
/* 332 */     return MathHelper.func_181758_c(0.62222224F - p_76731_1_ * 0.05F, 0.5F + p_76731_1_ * 0.1F, 1.0F);
/*     */   }
/*     */   
/*     */   public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType)
/*     */   {
/* 337 */     switch (creatureType)
/*     */     {
/*     */     case AMBIENT: 
/* 340 */       return this.spawnableMonsterList;
/*     */     
/*     */     case CREATURE: 
/* 343 */       return this.spawnableCreatureList;
/*     */     
/*     */     case WATER_CREATURE: 
/* 346 */       return this.spawnableWaterCreatureList;
/*     */     
/*     */     case MONSTER: 
/* 349 */       return this.spawnableCaveCreatureList;
/*     */     }
/*     */     
/* 352 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getEnableSnow()
/*     */   {
/* 361 */     return isSnowyBiome();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSpawnLightningBolt()
/*     */   {
/* 369 */     return isSnowyBiome() ? false : this.enableRain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHighHumidity()
/*     */   {
/* 377 */     return this.rainfall > 0.85F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getSpawningChance()
/*     */   {
/* 385 */     return 0.1F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getIntRainfall()
/*     */   {
/* 393 */     return (int)(this.rainfall * 65536.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final float getFloatRainfall()
/*     */   {
/* 401 */     return this.rainfall;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final float getFloatTemperature(BlockPos pos)
/*     */   {
/* 409 */     if (pos.getY() > 64)
/*     */     {
/* 411 */       float f = (float)(temperatureNoise.func_151601_a(pos.getX() * 1.0D / 8.0D, pos.getZ() * 1.0D / 8.0D) * 4.0D);
/* 412 */       return this.temperature - (f + pos.getY() - 64.0F) * 0.05F / 30.0F;
/*     */     }
/*     */     
/*     */ 
/* 416 */     return this.temperature;
/*     */   }
/*     */   
/*     */ 
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*     */   {
/* 422 */     this.theBiomeDecorator.decorate(worldIn, rand, this, pos);
/*     */   }
/*     */   
/*     */   public int getGrassColorAtPos(BlockPos pos)
/*     */   {
/* 427 */     double d0 = MathHelper.clamp_float(getFloatTemperature(pos), 0.0F, 1.0F);
/* 428 */     double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
/* 429 */     return ColorizerGrass.getGrassColor(d0, d1);
/*     */   }
/*     */   
/*     */   public int getFoliageColorAtPos(BlockPos pos)
/*     */   {
/* 434 */     double d0 = MathHelper.clamp_float(getFloatTemperature(pos), 0.0F, 1.0F);
/* 435 */     double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
/* 436 */     return net.minecraft.world.ColorizerFoliage.getFoliageColor(d0, d1);
/*     */   }
/*     */   
/*     */   public boolean isSnowyBiome()
/*     */   {
/* 441 */     return this.enableSnow;
/*     */   }
/*     */   
/*     */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
/*     */   {
/* 446 */     generateBiomeTerrain(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
/*     */   }
/*     */   
/*     */   public final void generateBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180628_4_, int p_180628_5_, double p_180628_6_)
/*     */   {
/* 451 */     int i = worldIn.func_181545_F();
/* 452 */     IBlockState iblockstate = this.topBlock;
/* 453 */     IBlockState iblockstate1 = this.fillerBlock;
/* 454 */     int j = -1;
/* 455 */     int k = (int)(p_180628_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
/* 456 */     int l = p_180628_4_ & 0xF;
/* 457 */     int i1 = p_180628_5_ & 0xF;
/* 458 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 460 */     for (int j1 = 255; j1 >= 0; j1--)
/*     */     {
/* 462 */       if (j1 <= rand.nextInt(5))
/*     */       {
/* 464 */         chunkPrimerIn.setBlockState(i1, j1, l, Blocks.bedrock.getDefaultState());
/*     */       }
/*     */       else
/*     */       {
/* 468 */         IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);
/*     */         
/* 470 */         if (iblockstate2.getBlock().getMaterial() == Material.air)
/*     */         {
/* 472 */           j = -1;
/*     */         }
/* 474 */         else if (iblockstate2.getBlock() == Blocks.stone)
/*     */         {
/* 476 */           if (j == -1)
/*     */           {
/* 478 */             if (k <= 0)
/*     */             {
/* 480 */               iblockstate = null;
/* 481 */               iblockstate1 = Blocks.stone.getDefaultState();
/*     */             }
/* 483 */             else if ((j1 >= i - 4) && (j1 <= i + 1))
/*     */             {
/* 485 */               iblockstate = this.topBlock;
/* 486 */               iblockstate1 = this.fillerBlock;
/*     */             }
/*     */             
/* 489 */             if ((j1 < i) && ((iblockstate == null) || (iblockstate.getBlock().getMaterial() == Material.air)))
/*     */             {
/* 491 */               if (getFloatTemperature(blockpos$mutableblockpos.func_181079_c(p_180628_4_, j1, p_180628_5_)) < 0.15F)
/*     */               {
/* 493 */                 iblockstate = Blocks.ice.getDefaultState();
/*     */               }
/*     */               else
/*     */               {
/* 497 */                 iblockstate = Blocks.water.getDefaultState();
/*     */               }
/*     */             }
/*     */             
/* 501 */             j = k;
/*     */             
/* 503 */             if (j1 >= i - 1)
/*     */             {
/* 505 */               chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
/*     */             }
/* 507 */             else if (j1 < i - 7 - k)
/*     */             {
/* 509 */               iblockstate = null;
/* 510 */               iblockstate1 = Blocks.stone.getDefaultState();
/* 511 */               chunkPrimerIn.setBlockState(i1, j1, l, Blocks.gravel.getDefaultState());
/*     */             }
/*     */             else
/*     */             {
/* 515 */               chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
/*     */             }
/*     */           }
/* 518 */           else if (j > 0)
/*     */           {
/* 520 */             j--;
/* 521 */             chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
/*     */             
/* 523 */             if ((j == 0) && (iblockstate1.getBlock() == Blocks.sand))
/*     */             {
/* 525 */               j = rand.nextInt(4) + Math.max(0, j1 - 63);
/* 526 */               iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BiomeGenBase createMutation()
/*     */   {
/* 540 */     return createMutatedBiome(this.biomeID + 128);
/*     */   }
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_)
/*     */   {
/* 545 */     return new BiomeGenMutated(p_180277_1_, this);
/*     */   }
/*     */   
/*     */   public Class<? extends BiomeGenBase> getBiomeClass()
/*     */   {
/* 550 */     return getClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEqualTo(BiomeGenBase biome)
/*     */   {
/* 558 */     return biome == this;
/*     */   }
/*     */   
/*     */   public TempCategory getTempCategory()
/*     */   {
/* 563 */     return this.temperature < 1.0D ? TempCategory.MEDIUM : this.temperature < 0.2D ? TempCategory.COLD : TempCategory.WARM;
/*     */   }
/*     */   
/*     */   public static BiomeGenBase[] getBiomeGenArray()
/*     */   {
/* 568 */     return biomeList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BiomeGenBase getBiome(int id)
/*     */   {
/* 576 */     return getBiomeFromBiomeList(id, null);
/*     */   }
/*     */   
/*     */   public static BiomeGenBase getBiomeFromBiomeList(int biomeId, BiomeGenBase biome)
/*     */   {
/* 581 */     if ((biomeId >= 0) && (biomeId <= biomeList.length))
/*     */     {
/* 583 */       BiomeGenBase biomegenbase = biomeList[biomeId];
/* 584 */       return biomegenbase == null ? biome : biomegenbase;
/*     */     }
/*     */     
/*     */ 
/* 588 */     logger.warn("Biome ID is out of bounds: " + biomeId + ", defaulting to 0 (Ocean)");
/* 589 */     return ocean;
/*     */   }
/*     */   
/*     */ 
/*     */   static
/*     */   {
/* 595 */     plains.createMutation();
/* 596 */     desert.createMutation();
/* 597 */     forest.createMutation();
/* 598 */     taiga.createMutation();
/* 599 */     swampland.createMutation();
/* 600 */     icePlains.createMutation();
/* 601 */     jungle.createMutation();
/* 602 */     jungleEdge.createMutation();
/* 603 */     coldTaiga.createMutation();
/* 604 */     savanna.createMutation();
/* 605 */     savannaPlateau.createMutation();
/* 606 */     mesa.createMutation();
/* 607 */     mesaPlateau_F.createMutation();
/* 608 */     mesaPlateau.createMutation();
/* 609 */     birchForest.createMutation();
/* 610 */     birchForestHills.createMutation();
/* 611 */     roofedForest.createMutation();
/* 612 */     megaTaiga.createMutation();
/* 613 */     extremeHills.createMutation();
/* 614 */     extremeHillsPlus.createMutation();
/* 615 */     megaTaiga.createMutatedBiome(megaTaigaHills.biomeID + 128).setBiomeName("Redwood Taiga Hills M");
/*     */     BiomeGenBase[] arrayOfBiomeGenBase;
/* 617 */     int j = (arrayOfBiomeGenBase = biomeList).length; for (int i = 0; i < j; i++) { BiomeGenBase biomegenbase = arrayOfBiomeGenBase[i];
/*     */       
/* 619 */       if (biomegenbase != null)
/*     */       {
/* 621 */         if (BIOME_ID_MAP.containsKey(biomegenbase.biomeName))
/*     */         {
/* 623 */           throw new Error("Biome \"" + biomegenbase.biomeName + "\" is defined as both ID " + ((BiomeGenBase)BIOME_ID_MAP.get(biomegenbase.biomeName)).biomeID + " and " + biomegenbase.biomeID);
/*     */         }
/*     */         
/* 626 */         BIOME_ID_MAP.put(biomegenbase.biomeName, biomegenbase);
/*     */         
/* 628 */         if (biomegenbase.biomeID < 128)
/*     */         {
/* 630 */           explorationBiomesList.add(biomegenbase);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 635 */     explorationBiomesList.remove(hell);
/* 636 */     explorationBiomesList.remove(sky);
/* 637 */     explorationBiomesList.remove(frozenOcean);
/* 638 */     explorationBiomesList.remove(extremeHillsEdge); }
/* 639 */   protected static final NoiseGeneratorPerlin temperatureNoise = new NoiseGeneratorPerlin(new Random(1234L), 1);
/* 640 */   protected static final NoiseGeneratorPerlin GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);
/* 641 */   protected static final WorldGenDoublePlant DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant();
/*     */   public String biomeName;
/*     */   public int color;
/*     */   public int field_150609_ah;
/*     */   public float minHeight;
/*     */   
/*     */   public static class Height
/*     */   {
/*     */     public Height(float rootHeightIn, float variationIn)
/*     */     {
/* 651 */       this.rootHeight = rootHeightIn;
/* 652 */       this.variation = variationIn;
/*     */     }
/*     */     
/*     */     public float rootHeight;
/*     */     public float variation;
/* 657 */     public Height attenuate() { return new Height(this.rootHeight * 0.8F, this.variation * 0.6F); }
/*     */   }
/*     */   
/*     */   public float maxHeight;
/*     */   public float temperature;
/*     */   public float rainfall;
/*     */   public int waterColorMultiplier;
/*     */   public BiomeDecorator theBiomeDecorator;
/*     */   protected List<SpawnListEntry> spawnableMonsterList;
/*     */   protected List<SpawnListEntry> spawnableCreatureList;
/*     */   
/*     */   public static class SpawnListEntry extends WeightedRandom.Item {
/* 669 */     public SpawnListEntry(Class<? extends EntityLiving> entityclassIn, int weight, int groupCountMin, int groupCountMax) { super();
/* 670 */       this.entityClass = entityclassIn;
/* 671 */       this.minGroupCount = groupCountMin;
/* 672 */       this.maxGroupCount = groupCountMax; }
/*     */     
/*     */     public Class<? extends EntityLiving> entityClass;
/*     */     public int minGroupCount;
/*     */     public int maxGroupCount;
/* 677 */     public String toString() { return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight; }
/*     */   }
/*     */   
/*     */   protected List<SpawnListEntry> spawnableWaterCreatureList;
/*     */   protected List<SpawnListEntry> spawnableCaveCreatureList;
/*     */   
/* 683 */   public static enum TempCategory { OCEAN, 
/* 684 */     COLD, 
/* 685 */     MEDIUM, 
/* 686 */     WARM;
/*     */   }
/*     */   
/*     */   protected boolean enableSnow;
/*     */   protected boolean enableRain;
/*     */   public final int biomeID;
/*     */   protected WorldGenTrees worldGeneratorTrees;
/*     */   protected WorldGenBigTree worldGeneratorBigTree;
/*     */   protected WorldGenSwamp worldGeneratorSwamp;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */