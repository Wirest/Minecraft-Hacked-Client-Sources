/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import java.lang.reflect.Type;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class ChunkProviderSettings
/*     */ {
/*     */   public final float coordinateScale;
/*     */   public final float heightScale;
/*     */   public final float upperLimitScale;
/*     */   public final float lowerLimitScale;
/*     */   public final float depthNoiseScaleX;
/*     */   public final float depthNoiseScaleZ;
/*     */   public final float depthNoiseScaleExponent;
/*     */   public final float mainNoiseScaleX;
/*     */   public final float mainNoiseScaleY;
/*     */   public final float mainNoiseScaleZ;
/*     */   public final float baseSize;
/*     */   public final float stretchY;
/*     */   public final float biomeDepthWeight;
/*     */   public final float biomeDepthOffSet;
/*     */   public final float biomeScaleWeight;
/*     */   public final float biomeScaleOffset;
/*     */   public final int seaLevel;
/*     */   public final boolean useCaves;
/*     */   public final boolean useDungeons;
/*     */   public final int dungeonChance;
/*     */   public final boolean useStrongholds;
/*     */   public final boolean useVillages;
/*     */   public final boolean useMineShafts;
/*     */   public final boolean useTemples;
/*     */   public final boolean useMonuments;
/*     */   public final boolean useRavines;
/*     */   public final boolean useWaterLakes;
/*     */   public final int waterLakeChance;
/*     */   public final boolean useLavaLakes;
/*     */   public final int lavaLakeChance;
/*     */   public final boolean useLavaOceans;
/*     */   public final int fixedBiome;
/*     */   public final int biomeSize;
/*     */   public final int riverSize;
/*     */   public final int dirtSize;
/*     */   public final int dirtCount;
/*     */   public final int dirtMinHeight;
/*     */   public final int dirtMaxHeight;
/*     */   public final int gravelSize;
/*     */   public final int gravelCount;
/*     */   public final int gravelMinHeight;
/*     */   public final int gravelMaxHeight;
/*     */   public final int graniteSize;
/*     */   public final int graniteCount;
/*     */   public final int graniteMinHeight;
/*     */   public final int graniteMaxHeight;
/*     */   public final int dioriteSize;
/*     */   public final int dioriteCount;
/*     */   public final int dioriteMinHeight;
/*     */   public final int dioriteMaxHeight;
/*     */   public final int andesiteSize;
/*     */   public final int andesiteCount;
/*     */   public final int andesiteMinHeight;
/*     */   public final int andesiteMaxHeight;
/*     */   public final int coalSize;
/*     */   public final int coalCount;
/*     */   public final int coalMinHeight;
/*     */   public final int coalMaxHeight;
/*     */   public final int ironSize;
/*     */   public final int ironCount;
/*     */   public final int ironMinHeight;
/*     */   public final int ironMaxHeight;
/*     */   public final int goldSize;
/*     */   public final int goldCount;
/*     */   public final int goldMinHeight;
/*     */   public final int goldMaxHeight;
/*     */   public final int redstoneSize;
/*     */   public final int redstoneCount;
/*     */   public final int redstoneMinHeight;
/*     */   public final int redstoneMaxHeight;
/*     */   public final int diamondSize;
/*     */   public final int diamondCount;
/*     */   public final int diamondMinHeight;
/*     */   public final int diamondMaxHeight;
/*     */   public final int lapisSize;
/*     */   public final int lapisCount;
/*     */   public final int lapisCenterHeight;
/*     */   public final int lapisSpread;
/*     */   
/*     */   private ChunkProviderSettings(Factory settingsFactory)
/*     */   {
/*  99 */     this.coordinateScale = settingsFactory.coordinateScale;
/* 100 */     this.heightScale = settingsFactory.heightScale;
/* 101 */     this.upperLimitScale = settingsFactory.upperLimitScale;
/* 102 */     this.lowerLimitScale = settingsFactory.lowerLimitScale;
/* 103 */     this.depthNoiseScaleX = settingsFactory.depthNoiseScaleX;
/* 104 */     this.depthNoiseScaleZ = settingsFactory.depthNoiseScaleZ;
/* 105 */     this.depthNoiseScaleExponent = settingsFactory.depthNoiseScaleExponent;
/* 106 */     this.mainNoiseScaleX = settingsFactory.mainNoiseScaleX;
/* 107 */     this.mainNoiseScaleY = settingsFactory.mainNoiseScaleY;
/* 108 */     this.mainNoiseScaleZ = settingsFactory.mainNoiseScaleZ;
/* 109 */     this.baseSize = settingsFactory.baseSize;
/* 110 */     this.stretchY = settingsFactory.stretchY;
/* 111 */     this.biomeDepthWeight = settingsFactory.biomeDepthWeight;
/* 112 */     this.biomeDepthOffSet = settingsFactory.biomeDepthOffset;
/* 113 */     this.biomeScaleWeight = settingsFactory.biomeScaleWeight;
/* 114 */     this.biomeScaleOffset = settingsFactory.biomeScaleOffset;
/* 115 */     this.seaLevel = settingsFactory.seaLevel;
/* 116 */     this.useCaves = settingsFactory.useCaves;
/* 117 */     this.useDungeons = settingsFactory.useDungeons;
/* 118 */     this.dungeonChance = settingsFactory.dungeonChance;
/* 119 */     this.useStrongholds = settingsFactory.useStrongholds;
/* 120 */     this.useVillages = settingsFactory.useVillages;
/* 121 */     this.useMineShafts = settingsFactory.useMineShafts;
/* 122 */     this.useTemples = settingsFactory.useTemples;
/* 123 */     this.useMonuments = settingsFactory.useMonuments;
/* 124 */     this.useRavines = settingsFactory.useRavines;
/* 125 */     this.useWaterLakes = settingsFactory.useWaterLakes;
/* 126 */     this.waterLakeChance = settingsFactory.waterLakeChance;
/* 127 */     this.useLavaLakes = settingsFactory.useLavaLakes;
/* 128 */     this.lavaLakeChance = settingsFactory.lavaLakeChance;
/* 129 */     this.useLavaOceans = settingsFactory.useLavaOceans;
/* 130 */     this.fixedBiome = settingsFactory.fixedBiome;
/* 131 */     this.biomeSize = settingsFactory.biomeSize;
/* 132 */     this.riverSize = settingsFactory.riverSize;
/* 133 */     this.dirtSize = settingsFactory.dirtSize;
/* 134 */     this.dirtCount = settingsFactory.dirtCount;
/* 135 */     this.dirtMinHeight = settingsFactory.dirtMinHeight;
/* 136 */     this.dirtMaxHeight = settingsFactory.dirtMaxHeight;
/* 137 */     this.gravelSize = settingsFactory.gravelSize;
/* 138 */     this.gravelCount = settingsFactory.gravelCount;
/* 139 */     this.gravelMinHeight = settingsFactory.gravelMinHeight;
/* 140 */     this.gravelMaxHeight = settingsFactory.gravelMaxHeight;
/* 141 */     this.graniteSize = settingsFactory.graniteSize;
/* 142 */     this.graniteCount = settingsFactory.graniteCount;
/* 143 */     this.graniteMinHeight = settingsFactory.graniteMinHeight;
/* 144 */     this.graniteMaxHeight = settingsFactory.graniteMaxHeight;
/* 145 */     this.dioriteSize = settingsFactory.dioriteSize;
/* 146 */     this.dioriteCount = settingsFactory.dioriteCount;
/* 147 */     this.dioriteMinHeight = settingsFactory.dioriteMinHeight;
/* 148 */     this.dioriteMaxHeight = settingsFactory.dioriteMaxHeight;
/* 149 */     this.andesiteSize = settingsFactory.andesiteSize;
/* 150 */     this.andesiteCount = settingsFactory.andesiteCount;
/* 151 */     this.andesiteMinHeight = settingsFactory.andesiteMinHeight;
/* 152 */     this.andesiteMaxHeight = settingsFactory.andesiteMaxHeight;
/* 153 */     this.coalSize = settingsFactory.coalSize;
/* 154 */     this.coalCount = settingsFactory.coalCount;
/* 155 */     this.coalMinHeight = settingsFactory.coalMinHeight;
/* 156 */     this.coalMaxHeight = settingsFactory.coalMaxHeight;
/* 157 */     this.ironSize = settingsFactory.ironSize;
/* 158 */     this.ironCount = settingsFactory.ironCount;
/* 159 */     this.ironMinHeight = settingsFactory.ironMinHeight;
/* 160 */     this.ironMaxHeight = settingsFactory.ironMaxHeight;
/* 161 */     this.goldSize = settingsFactory.goldSize;
/* 162 */     this.goldCount = settingsFactory.goldCount;
/* 163 */     this.goldMinHeight = settingsFactory.goldMinHeight;
/* 164 */     this.goldMaxHeight = settingsFactory.goldMaxHeight;
/* 165 */     this.redstoneSize = settingsFactory.redstoneSize;
/* 166 */     this.redstoneCount = settingsFactory.redstoneCount;
/* 167 */     this.redstoneMinHeight = settingsFactory.redstoneMinHeight;
/* 168 */     this.redstoneMaxHeight = settingsFactory.redstoneMaxHeight;
/* 169 */     this.diamondSize = settingsFactory.diamondSize;
/* 170 */     this.diamondCount = settingsFactory.diamondCount;
/* 171 */     this.diamondMinHeight = settingsFactory.diamondMinHeight;
/* 172 */     this.diamondMaxHeight = settingsFactory.diamondMaxHeight;
/* 173 */     this.lapisSize = settingsFactory.lapisSize;
/* 174 */     this.lapisCount = settingsFactory.lapisCount;
/* 175 */     this.lapisCenterHeight = settingsFactory.lapisCenterHeight;
/* 176 */     this.lapisSpread = settingsFactory.lapisSpread;
/*     */   }
/*     */   
/*     */   public static class Factory
/*     */   {
/* 181 */     static final Gson JSON_ADAPTER = new GsonBuilder().registerTypeAdapter(Factory.class, new ChunkProviderSettings.Serializer()).create();
/* 182 */     public float coordinateScale = 684.412F;
/* 183 */     public float heightScale = 684.412F;
/* 184 */     public float upperLimitScale = 512.0F;
/* 185 */     public float lowerLimitScale = 512.0F;
/* 186 */     public float depthNoiseScaleX = 200.0F;
/* 187 */     public float depthNoiseScaleZ = 200.0F;
/* 188 */     public float depthNoiseScaleExponent = 0.5F;
/* 189 */     public float mainNoiseScaleX = 80.0F;
/* 190 */     public float mainNoiseScaleY = 160.0F;
/* 191 */     public float mainNoiseScaleZ = 80.0F;
/* 192 */     public float baseSize = 8.5F;
/* 193 */     public float stretchY = 12.0F;
/* 194 */     public float biomeDepthWeight = 1.0F;
/* 195 */     public float biomeDepthOffset = 0.0F;
/* 196 */     public float biomeScaleWeight = 1.0F;
/* 197 */     public float biomeScaleOffset = 0.0F;
/* 198 */     public int seaLevel = 63;
/* 199 */     public boolean useCaves = true;
/* 200 */     public boolean useDungeons = true;
/* 201 */     public int dungeonChance = 8;
/* 202 */     public boolean useStrongholds = true;
/* 203 */     public boolean useVillages = true;
/* 204 */     public boolean useMineShafts = true;
/* 205 */     public boolean useTemples = true;
/* 206 */     public boolean useMonuments = true;
/* 207 */     public boolean useRavines = true;
/* 208 */     public boolean useWaterLakes = true;
/* 209 */     public int waterLakeChance = 4;
/* 210 */     public boolean useLavaLakes = true;
/* 211 */     public int lavaLakeChance = 80;
/* 212 */     public boolean useLavaOceans = false;
/* 213 */     public int fixedBiome = -1;
/* 214 */     public int biomeSize = 4;
/* 215 */     public int riverSize = 4;
/* 216 */     public int dirtSize = 33;
/* 217 */     public int dirtCount = 10;
/* 218 */     public int dirtMinHeight = 0;
/* 219 */     public int dirtMaxHeight = 256;
/* 220 */     public int gravelSize = 33;
/* 221 */     public int gravelCount = 8;
/* 222 */     public int gravelMinHeight = 0;
/* 223 */     public int gravelMaxHeight = 256;
/* 224 */     public int graniteSize = 33;
/* 225 */     public int graniteCount = 10;
/* 226 */     public int graniteMinHeight = 0;
/* 227 */     public int graniteMaxHeight = 80;
/* 228 */     public int dioriteSize = 33;
/* 229 */     public int dioriteCount = 10;
/* 230 */     public int dioriteMinHeight = 0;
/* 231 */     public int dioriteMaxHeight = 80;
/* 232 */     public int andesiteSize = 33;
/* 233 */     public int andesiteCount = 10;
/* 234 */     public int andesiteMinHeight = 0;
/* 235 */     public int andesiteMaxHeight = 80;
/* 236 */     public int coalSize = 17;
/* 237 */     public int coalCount = 20;
/* 238 */     public int coalMinHeight = 0;
/* 239 */     public int coalMaxHeight = 128;
/* 240 */     public int ironSize = 9;
/* 241 */     public int ironCount = 20;
/* 242 */     public int ironMinHeight = 0;
/* 243 */     public int ironMaxHeight = 64;
/* 244 */     public int goldSize = 9;
/* 245 */     public int goldCount = 2;
/* 246 */     public int goldMinHeight = 0;
/* 247 */     public int goldMaxHeight = 32;
/* 248 */     public int redstoneSize = 8;
/* 249 */     public int redstoneCount = 8;
/* 250 */     public int redstoneMinHeight = 0;
/* 251 */     public int redstoneMaxHeight = 16;
/* 252 */     public int diamondSize = 8;
/* 253 */     public int diamondCount = 1;
/* 254 */     public int diamondMinHeight = 0;
/* 255 */     public int diamondMaxHeight = 16;
/* 256 */     public int lapisSize = 7;
/* 257 */     public int lapisCount = 1;
/* 258 */     public int lapisCenterHeight = 16;
/* 259 */     public int lapisSpread = 16;
/*     */     
/*     */     public static Factory jsonToFactory(String p_177865_0_)
/*     */     {
/* 263 */       if (p_177865_0_.length() == 0)
/*     */       {
/* 265 */         return new Factory();
/*     */       }
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 271 */         return (Factory)JSON_ADAPTER.fromJson(p_177865_0_, Factory.class);
/*     */       }
/*     */       catch (Exception var2) {}
/*     */       
/* 275 */       return new Factory();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 282 */       return JSON_ADAPTER.toJson(this);
/*     */     }
/*     */     
/*     */     public Factory()
/*     */     {
/* 287 */       func_177863_a();
/*     */     }
/*     */     
/*     */     public void func_177863_a()
/*     */     {
/* 292 */       this.coordinateScale = 684.412F;
/* 293 */       this.heightScale = 684.412F;
/* 294 */       this.upperLimitScale = 512.0F;
/* 295 */       this.lowerLimitScale = 512.0F;
/* 296 */       this.depthNoiseScaleX = 200.0F;
/* 297 */       this.depthNoiseScaleZ = 200.0F;
/* 298 */       this.depthNoiseScaleExponent = 0.5F;
/* 299 */       this.mainNoiseScaleX = 80.0F;
/* 300 */       this.mainNoiseScaleY = 160.0F;
/* 301 */       this.mainNoiseScaleZ = 80.0F;
/* 302 */       this.baseSize = 8.5F;
/* 303 */       this.stretchY = 12.0F;
/* 304 */       this.biomeDepthWeight = 1.0F;
/* 305 */       this.biomeDepthOffset = 0.0F;
/* 306 */       this.biomeScaleWeight = 1.0F;
/* 307 */       this.biomeScaleOffset = 0.0F;
/* 308 */       this.seaLevel = 63;
/* 309 */       this.useCaves = true;
/* 310 */       this.useDungeons = true;
/* 311 */       this.dungeonChance = 8;
/* 312 */       this.useStrongholds = true;
/* 313 */       this.useVillages = true;
/* 314 */       this.useMineShafts = true;
/* 315 */       this.useTemples = true;
/* 316 */       this.useMonuments = true;
/* 317 */       this.useRavines = true;
/* 318 */       this.useWaterLakes = true;
/* 319 */       this.waterLakeChance = 4;
/* 320 */       this.useLavaLakes = true;
/* 321 */       this.lavaLakeChance = 80;
/* 322 */       this.useLavaOceans = false;
/* 323 */       this.fixedBiome = -1;
/* 324 */       this.biomeSize = 4;
/* 325 */       this.riverSize = 4;
/* 326 */       this.dirtSize = 33;
/* 327 */       this.dirtCount = 10;
/* 328 */       this.dirtMinHeight = 0;
/* 329 */       this.dirtMaxHeight = 256;
/* 330 */       this.gravelSize = 33;
/* 331 */       this.gravelCount = 8;
/* 332 */       this.gravelMinHeight = 0;
/* 333 */       this.gravelMaxHeight = 256;
/* 334 */       this.graniteSize = 33;
/* 335 */       this.graniteCount = 10;
/* 336 */       this.graniteMinHeight = 0;
/* 337 */       this.graniteMaxHeight = 80;
/* 338 */       this.dioriteSize = 33;
/* 339 */       this.dioriteCount = 10;
/* 340 */       this.dioriteMinHeight = 0;
/* 341 */       this.dioriteMaxHeight = 80;
/* 342 */       this.andesiteSize = 33;
/* 343 */       this.andesiteCount = 10;
/* 344 */       this.andesiteMinHeight = 0;
/* 345 */       this.andesiteMaxHeight = 80;
/* 346 */       this.coalSize = 17;
/* 347 */       this.coalCount = 20;
/* 348 */       this.coalMinHeight = 0;
/* 349 */       this.coalMaxHeight = 128;
/* 350 */       this.ironSize = 9;
/* 351 */       this.ironCount = 20;
/* 352 */       this.ironMinHeight = 0;
/* 353 */       this.ironMaxHeight = 64;
/* 354 */       this.goldSize = 9;
/* 355 */       this.goldCount = 2;
/* 356 */       this.goldMinHeight = 0;
/* 357 */       this.goldMaxHeight = 32;
/* 358 */       this.redstoneSize = 8;
/* 359 */       this.redstoneCount = 8;
/* 360 */       this.redstoneMinHeight = 0;
/* 361 */       this.redstoneMaxHeight = 16;
/* 362 */       this.diamondSize = 8;
/* 363 */       this.diamondCount = 1;
/* 364 */       this.diamondMinHeight = 0;
/* 365 */       this.diamondMaxHeight = 16;
/* 366 */       this.lapisSize = 7;
/* 367 */       this.lapisCount = 1;
/* 368 */       this.lapisCenterHeight = 16;
/* 369 */       this.lapisSpread = 16;
/*     */     }
/*     */     
/*     */     public boolean equals(Object p_equals_1_)
/*     */     {
/* 374 */       if (this == p_equals_1_)
/*     */       {
/* 376 */         return true;
/*     */       }
/* 378 */       if ((p_equals_1_ != null) && (getClass() == p_equals_1_.getClass()))
/*     */       {
/* 380 */         Factory chunkprovidersettings$factory = (Factory)p_equals_1_;
/* 381 */         return this.andesiteCount == chunkprovidersettings$factory.andesiteCount;
/*     */       }
/*     */       
/*     */ 
/* 385 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 391 */       int i = this.coordinateScale != 0.0F ? Float.floatToIntBits(this.coordinateScale) : 0;
/* 392 */       i = 31 * i + (this.heightScale != 0.0F ? Float.floatToIntBits(this.heightScale) : 0);
/* 393 */       i = 31 * i + (this.upperLimitScale != 0.0F ? Float.floatToIntBits(this.upperLimitScale) : 0);
/* 394 */       i = 31 * i + (this.lowerLimitScale != 0.0F ? Float.floatToIntBits(this.lowerLimitScale) : 0);
/* 395 */       i = 31 * i + (this.depthNoiseScaleX != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleX) : 0);
/* 396 */       i = 31 * i + (this.depthNoiseScaleZ != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleZ) : 0);
/* 397 */       i = 31 * i + (this.depthNoiseScaleExponent != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleExponent) : 0);
/* 398 */       i = 31 * i + (this.mainNoiseScaleX != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleX) : 0);
/* 399 */       i = 31 * i + (this.mainNoiseScaleY != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleY) : 0);
/* 400 */       i = 31 * i + (this.mainNoiseScaleZ != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleZ) : 0);
/* 401 */       i = 31 * i + (this.baseSize != 0.0F ? Float.floatToIntBits(this.baseSize) : 0);
/* 402 */       i = 31 * i + (this.stretchY != 0.0F ? Float.floatToIntBits(this.stretchY) : 0);
/* 403 */       i = 31 * i + (this.biomeDepthWeight != 0.0F ? Float.floatToIntBits(this.biomeDepthWeight) : 0);
/* 404 */       i = 31 * i + (this.biomeDepthOffset != 0.0F ? Float.floatToIntBits(this.biomeDepthOffset) : 0);
/* 405 */       i = 31 * i + (this.biomeScaleWeight != 0.0F ? Float.floatToIntBits(this.biomeScaleWeight) : 0);
/* 406 */       i = 31 * i + (this.biomeScaleOffset != 0.0F ? Float.floatToIntBits(this.biomeScaleOffset) : 0);
/* 407 */       i = 31 * i + this.seaLevel;
/* 408 */       i = 31 * i + (this.useCaves ? 1 : 0);
/* 409 */       i = 31 * i + (this.useDungeons ? 1 : 0);
/* 410 */       i = 31 * i + this.dungeonChance;
/* 411 */       i = 31 * i + (this.useStrongholds ? 1 : 0);
/* 412 */       i = 31 * i + (this.useVillages ? 1 : 0);
/* 413 */       i = 31 * i + (this.useMineShafts ? 1 : 0);
/* 414 */       i = 31 * i + (this.useTemples ? 1 : 0);
/* 415 */       i = 31 * i + (this.useMonuments ? 1 : 0);
/* 416 */       i = 31 * i + (this.useRavines ? 1 : 0);
/* 417 */       i = 31 * i + (this.useWaterLakes ? 1 : 0);
/* 418 */       i = 31 * i + this.waterLakeChance;
/* 419 */       i = 31 * i + (this.useLavaLakes ? 1 : 0);
/* 420 */       i = 31 * i + this.lavaLakeChance;
/* 421 */       i = 31 * i + (this.useLavaOceans ? 1 : 0);
/* 422 */       i = 31 * i + this.fixedBiome;
/* 423 */       i = 31 * i + this.biomeSize;
/* 424 */       i = 31 * i + this.riverSize;
/* 425 */       i = 31 * i + this.dirtSize;
/* 426 */       i = 31 * i + this.dirtCount;
/* 427 */       i = 31 * i + this.dirtMinHeight;
/* 428 */       i = 31 * i + this.dirtMaxHeight;
/* 429 */       i = 31 * i + this.gravelSize;
/* 430 */       i = 31 * i + this.gravelCount;
/* 431 */       i = 31 * i + this.gravelMinHeight;
/* 432 */       i = 31 * i + this.gravelMaxHeight;
/* 433 */       i = 31 * i + this.graniteSize;
/* 434 */       i = 31 * i + this.graniteCount;
/* 435 */       i = 31 * i + this.graniteMinHeight;
/* 436 */       i = 31 * i + this.graniteMaxHeight;
/* 437 */       i = 31 * i + this.dioriteSize;
/* 438 */       i = 31 * i + this.dioriteCount;
/* 439 */       i = 31 * i + this.dioriteMinHeight;
/* 440 */       i = 31 * i + this.dioriteMaxHeight;
/* 441 */       i = 31 * i + this.andesiteSize;
/* 442 */       i = 31 * i + this.andesiteCount;
/* 443 */       i = 31 * i + this.andesiteMinHeight;
/* 444 */       i = 31 * i + this.andesiteMaxHeight;
/* 445 */       i = 31 * i + this.coalSize;
/* 446 */       i = 31 * i + this.coalCount;
/* 447 */       i = 31 * i + this.coalMinHeight;
/* 448 */       i = 31 * i + this.coalMaxHeight;
/* 449 */       i = 31 * i + this.ironSize;
/* 450 */       i = 31 * i + this.ironCount;
/* 451 */       i = 31 * i + this.ironMinHeight;
/* 452 */       i = 31 * i + this.ironMaxHeight;
/* 453 */       i = 31 * i + this.goldSize;
/* 454 */       i = 31 * i + this.goldCount;
/* 455 */       i = 31 * i + this.goldMinHeight;
/* 456 */       i = 31 * i + this.goldMaxHeight;
/* 457 */       i = 31 * i + this.redstoneSize;
/* 458 */       i = 31 * i + this.redstoneCount;
/* 459 */       i = 31 * i + this.redstoneMinHeight;
/* 460 */       i = 31 * i + this.redstoneMaxHeight;
/* 461 */       i = 31 * i + this.diamondSize;
/* 462 */       i = 31 * i + this.diamondCount;
/* 463 */       i = 31 * i + this.diamondMinHeight;
/* 464 */       i = 31 * i + this.diamondMaxHeight;
/* 465 */       i = 31 * i + this.lapisSize;
/* 466 */       i = 31 * i + this.lapisCount;
/* 467 */       i = 31 * i + this.lapisCenterHeight;
/* 468 */       i = 31 * i + this.lapisSpread;
/* 469 */       return i;
/*     */     }
/*     */     
/*     */     public ChunkProviderSettings func_177864_b()
/*     */     {
/* 474 */       return new ChunkProviderSettings(this, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Serializer implements JsonDeserializer<ChunkProviderSettings.Factory>, JsonSerializer<ChunkProviderSettings.Factory>
/*     */   {
/*     */     public ChunkProviderSettings.Factory deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/* 482 */       JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 483 */       ChunkProviderSettings.Factory chunkprovidersettings$factory = new ChunkProviderSettings.Factory();
/*     */       
/*     */       try
/*     */       {
/* 487 */         chunkprovidersettings$factory.coordinateScale = JsonUtils.getFloat(jsonobject, "coordinateScale", chunkprovidersettings$factory.coordinateScale);
/* 488 */         chunkprovidersettings$factory.heightScale = JsonUtils.getFloat(jsonobject, "heightScale", chunkprovidersettings$factory.heightScale);
/* 489 */         chunkprovidersettings$factory.lowerLimitScale = JsonUtils.getFloat(jsonobject, "lowerLimitScale", chunkprovidersettings$factory.lowerLimitScale);
/* 490 */         chunkprovidersettings$factory.upperLimitScale = JsonUtils.getFloat(jsonobject, "upperLimitScale", chunkprovidersettings$factory.upperLimitScale);
/* 491 */         chunkprovidersettings$factory.depthNoiseScaleX = JsonUtils.getFloat(jsonobject, "depthNoiseScaleX", chunkprovidersettings$factory.depthNoiseScaleX);
/* 492 */         chunkprovidersettings$factory.depthNoiseScaleZ = JsonUtils.getFloat(jsonobject, "depthNoiseScaleZ", chunkprovidersettings$factory.depthNoiseScaleZ);
/* 493 */         chunkprovidersettings$factory.depthNoiseScaleExponent = JsonUtils.getFloat(jsonobject, "depthNoiseScaleExponent", chunkprovidersettings$factory.depthNoiseScaleExponent);
/* 494 */         chunkprovidersettings$factory.mainNoiseScaleX = JsonUtils.getFloat(jsonobject, "mainNoiseScaleX", chunkprovidersettings$factory.mainNoiseScaleX);
/* 495 */         chunkprovidersettings$factory.mainNoiseScaleY = JsonUtils.getFloat(jsonobject, "mainNoiseScaleY", chunkprovidersettings$factory.mainNoiseScaleY);
/* 496 */         chunkprovidersettings$factory.mainNoiseScaleZ = JsonUtils.getFloat(jsonobject, "mainNoiseScaleZ", chunkprovidersettings$factory.mainNoiseScaleZ);
/* 497 */         chunkprovidersettings$factory.baseSize = JsonUtils.getFloat(jsonobject, "baseSize", chunkprovidersettings$factory.baseSize);
/* 498 */         chunkprovidersettings$factory.stretchY = JsonUtils.getFloat(jsonobject, "stretchY", chunkprovidersettings$factory.stretchY);
/* 499 */         chunkprovidersettings$factory.biomeDepthWeight = JsonUtils.getFloat(jsonobject, "biomeDepthWeight", chunkprovidersettings$factory.biomeDepthWeight);
/* 500 */         chunkprovidersettings$factory.biomeDepthOffset = JsonUtils.getFloat(jsonobject, "biomeDepthOffset", chunkprovidersettings$factory.biomeDepthOffset);
/* 501 */         chunkprovidersettings$factory.biomeScaleWeight = JsonUtils.getFloat(jsonobject, "biomeScaleWeight", chunkprovidersettings$factory.biomeScaleWeight);
/* 502 */         chunkprovidersettings$factory.biomeScaleOffset = JsonUtils.getFloat(jsonobject, "biomeScaleOffset", chunkprovidersettings$factory.biomeScaleOffset);
/* 503 */         chunkprovidersettings$factory.seaLevel = JsonUtils.getInt(jsonobject, "seaLevel", chunkprovidersettings$factory.seaLevel);
/* 504 */         chunkprovidersettings$factory.useCaves = JsonUtils.getBoolean(jsonobject, "useCaves", chunkprovidersettings$factory.useCaves);
/* 505 */         chunkprovidersettings$factory.useDungeons = JsonUtils.getBoolean(jsonobject, "useDungeons", chunkprovidersettings$factory.useDungeons);
/* 506 */         chunkprovidersettings$factory.dungeonChance = JsonUtils.getInt(jsonobject, "dungeonChance", chunkprovidersettings$factory.dungeonChance);
/* 507 */         chunkprovidersettings$factory.useStrongholds = JsonUtils.getBoolean(jsonobject, "useStrongholds", chunkprovidersettings$factory.useStrongholds);
/* 508 */         chunkprovidersettings$factory.useVillages = JsonUtils.getBoolean(jsonobject, "useVillages", chunkprovidersettings$factory.useVillages);
/* 509 */         chunkprovidersettings$factory.useMineShafts = JsonUtils.getBoolean(jsonobject, "useMineShafts", chunkprovidersettings$factory.useMineShafts);
/* 510 */         chunkprovidersettings$factory.useTemples = JsonUtils.getBoolean(jsonobject, "useTemples", chunkprovidersettings$factory.useTemples);
/* 511 */         chunkprovidersettings$factory.useMonuments = JsonUtils.getBoolean(jsonobject, "useMonuments", chunkprovidersettings$factory.useMonuments);
/* 512 */         chunkprovidersettings$factory.useRavines = JsonUtils.getBoolean(jsonobject, "useRavines", chunkprovidersettings$factory.useRavines);
/* 513 */         chunkprovidersettings$factory.useWaterLakes = JsonUtils.getBoolean(jsonobject, "useWaterLakes", chunkprovidersettings$factory.useWaterLakes);
/* 514 */         chunkprovidersettings$factory.waterLakeChance = JsonUtils.getInt(jsonobject, "waterLakeChance", chunkprovidersettings$factory.waterLakeChance);
/* 515 */         chunkprovidersettings$factory.useLavaLakes = JsonUtils.getBoolean(jsonobject, "useLavaLakes", chunkprovidersettings$factory.useLavaLakes);
/* 516 */         chunkprovidersettings$factory.lavaLakeChance = JsonUtils.getInt(jsonobject, "lavaLakeChance", chunkprovidersettings$factory.lavaLakeChance);
/* 517 */         chunkprovidersettings$factory.useLavaOceans = JsonUtils.getBoolean(jsonobject, "useLavaOceans", chunkprovidersettings$factory.useLavaOceans);
/* 518 */         chunkprovidersettings$factory.fixedBiome = JsonUtils.getInt(jsonobject, "fixedBiome", chunkprovidersettings$factory.fixedBiome);
/*     */         
/* 520 */         if ((chunkprovidersettings$factory.fixedBiome < 38) && (chunkprovidersettings$factory.fixedBiome >= -1))
/*     */         {
/* 522 */           if (chunkprovidersettings$factory.fixedBiome >= BiomeGenBase.hell.biomeID)
/*     */           {
/* 524 */             chunkprovidersettings$factory.fixedBiome += 2;
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 529 */           chunkprovidersettings$factory.fixedBiome = -1;
/*     */         }
/*     */         
/* 532 */         chunkprovidersettings$factory.biomeSize = JsonUtils.getInt(jsonobject, "biomeSize", chunkprovidersettings$factory.biomeSize);
/* 533 */         chunkprovidersettings$factory.riverSize = JsonUtils.getInt(jsonobject, "riverSize", chunkprovidersettings$factory.riverSize);
/* 534 */         chunkprovidersettings$factory.dirtSize = JsonUtils.getInt(jsonobject, "dirtSize", chunkprovidersettings$factory.dirtSize);
/* 535 */         chunkprovidersettings$factory.dirtCount = JsonUtils.getInt(jsonobject, "dirtCount", chunkprovidersettings$factory.dirtCount);
/* 536 */         chunkprovidersettings$factory.dirtMinHeight = JsonUtils.getInt(jsonobject, "dirtMinHeight", chunkprovidersettings$factory.dirtMinHeight);
/* 537 */         chunkprovidersettings$factory.dirtMaxHeight = JsonUtils.getInt(jsonobject, "dirtMaxHeight", chunkprovidersettings$factory.dirtMaxHeight);
/* 538 */         chunkprovidersettings$factory.gravelSize = JsonUtils.getInt(jsonobject, "gravelSize", chunkprovidersettings$factory.gravelSize);
/* 539 */         chunkprovidersettings$factory.gravelCount = JsonUtils.getInt(jsonobject, "gravelCount", chunkprovidersettings$factory.gravelCount);
/* 540 */         chunkprovidersettings$factory.gravelMinHeight = JsonUtils.getInt(jsonobject, "gravelMinHeight", chunkprovidersettings$factory.gravelMinHeight);
/* 541 */         chunkprovidersettings$factory.gravelMaxHeight = JsonUtils.getInt(jsonobject, "gravelMaxHeight", chunkprovidersettings$factory.gravelMaxHeight);
/* 542 */         chunkprovidersettings$factory.graniteSize = JsonUtils.getInt(jsonobject, "graniteSize", chunkprovidersettings$factory.graniteSize);
/* 543 */         chunkprovidersettings$factory.graniteCount = JsonUtils.getInt(jsonobject, "graniteCount", chunkprovidersettings$factory.graniteCount);
/* 544 */         chunkprovidersettings$factory.graniteMinHeight = JsonUtils.getInt(jsonobject, "graniteMinHeight", chunkprovidersettings$factory.graniteMinHeight);
/* 545 */         chunkprovidersettings$factory.graniteMaxHeight = JsonUtils.getInt(jsonobject, "graniteMaxHeight", chunkprovidersettings$factory.graniteMaxHeight);
/* 546 */         chunkprovidersettings$factory.dioriteSize = JsonUtils.getInt(jsonobject, "dioriteSize", chunkprovidersettings$factory.dioriteSize);
/* 547 */         chunkprovidersettings$factory.dioriteCount = JsonUtils.getInt(jsonobject, "dioriteCount", chunkprovidersettings$factory.dioriteCount);
/* 548 */         chunkprovidersettings$factory.dioriteMinHeight = JsonUtils.getInt(jsonobject, "dioriteMinHeight", chunkprovidersettings$factory.dioriteMinHeight);
/* 549 */         chunkprovidersettings$factory.dioriteMaxHeight = JsonUtils.getInt(jsonobject, "dioriteMaxHeight", chunkprovidersettings$factory.dioriteMaxHeight);
/* 550 */         chunkprovidersettings$factory.andesiteSize = JsonUtils.getInt(jsonobject, "andesiteSize", chunkprovidersettings$factory.andesiteSize);
/* 551 */         chunkprovidersettings$factory.andesiteCount = JsonUtils.getInt(jsonobject, "andesiteCount", chunkprovidersettings$factory.andesiteCount);
/* 552 */         chunkprovidersettings$factory.andesiteMinHeight = JsonUtils.getInt(jsonobject, "andesiteMinHeight", chunkprovidersettings$factory.andesiteMinHeight);
/* 553 */         chunkprovidersettings$factory.andesiteMaxHeight = JsonUtils.getInt(jsonobject, "andesiteMaxHeight", chunkprovidersettings$factory.andesiteMaxHeight);
/* 554 */         chunkprovidersettings$factory.coalSize = JsonUtils.getInt(jsonobject, "coalSize", chunkprovidersettings$factory.coalSize);
/* 555 */         chunkprovidersettings$factory.coalCount = JsonUtils.getInt(jsonobject, "coalCount", chunkprovidersettings$factory.coalCount);
/* 556 */         chunkprovidersettings$factory.coalMinHeight = JsonUtils.getInt(jsonobject, "coalMinHeight", chunkprovidersettings$factory.coalMinHeight);
/* 557 */         chunkprovidersettings$factory.coalMaxHeight = JsonUtils.getInt(jsonobject, "coalMaxHeight", chunkprovidersettings$factory.coalMaxHeight);
/* 558 */         chunkprovidersettings$factory.ironSize = JsonUtils.getInt(jsonobject, "ironSize", chunkprovidersettings$factory.ironSize);
/* 559 */         chunkprovidersettings$factory.ironCount = JsonUtils.getInt(jsonobject, "ironCount", chunkprovidersettings$factory.ironCount);
/* 560 */         chunkprovidersettings$factory.ironMinHeight = JsonUtils.getInt(jsonobject, "ironMinHeight", chunkprovidersettings$factory.ironMinHeight);
/* 561 */         chunkprovidersettings$factory.ironMaxHeight = JsonUtils.getInt(jsonobject, "ironMaxHeight", chunkprovidersettings$factory.ironMaxHeight);
/* 562 */         chunkprovidersettings$factory.goldSize = JsonUtils.getInt(jsonobject, "goldSize", chunkprovidersettings$factory.goldSize);
/* 563 */         chunkprovidersettings$factory.goldCount = JsonUtils.getInt(jsonobject, "goldCount", chunkprovidersettings$factory.goldCount);
/* 564 */         chunkprovidersettings$factory.goldMinHeight = JsonUtils.getInt(jsonobject, "goldMinHeight", chunkprovidersettings$factory.goldMinHeight);
/* 565 */         chunkprovidersettings$factory.goldMaxHeight = JsonUtils.getInt(jsonobject, "goldMaxHeight", chunkprovidersettings$factory.goldMaxHeight);
/* 566 */         chunkprovidersettings$factory.redstoneSize = JsonUtils.getInt(jsonobject, "redstoneSize", chunkprovidersettings$factory.redstoneSize);
/* 567 */         chunkprovidersettings$factory.redstoneCount = JsonUtils.getInt(jsonobject, "redstoneCount", chunkprovidersettings$factory.redstoneCount);
/* 568 */         chunkprovidersettings$factory.redstoneMinHeight = JsonUtils.getInt(jsonobject, "redstoneMinHeight", chunkprovidersettings$factory.redstoneMinHeight);
/* 569 */         chunkprovidersettings$factory.redstoneMaxHeight = JsonUtils.getInt(jsonobject, "redstoneMaxHeight", chunkprovidersettings$factory.redstoneMaxHeight);
/* 570 */         chunkprovidersettings$factory.diamondSize = JsonUtils.getInt(jsonobject, "diamondSize", chunkprovidersettings$factory.diamondSize);
/* 571 */         chunkprovidersettings$factory.diamondCount = JsonUtils.getInt(jsonobject, "diamondCount", chunkprovidersettings$factory.diamondCount);
/* 572 */         chunkprovidersettings$factory.diamondMinHeight = JsonUtils.getInt(jsonobject, "diamondMinHeight", chunkprovidersettings$factory.diamondMinHeight);
/* 573 */         chunkprovidersettings$factory.diamondMaxHeight = JsonUtils.getInt(jsonobject, "diamondMaxHeight", chunkprovidersettings$factory.diamondMaxHeight);
/* 574 */         chunkprovidersettings$factory.lapisSize = JsonUtils.getInt(jsonobject, "lapisSize", chunkprovidersettings$factory.lapisSize);
/* 575 */         chunkprovidersettings$factory.lapisCount = JsonUtils.getInt(jsonobject, "lapisCount", chunkprovidersettings$factory.lapisCount);
/* 576 */         chunkprovidersettings$factory.lapisCenterHeight = JsonUtils.getInt(jsonobject, "lapisCenterHeight", chunkprovidersettings$factory.lapisCenterHeight);
/* 577 */         chunkprovidersettings$factory.lapisSpread = JsonUtils.getInt(jsonobject, "lapisSpread", chunkprovidersettings$factory.lapisSpread);
/*     */       }
/*     */       catch (Exception localException) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 584 */       return chunkprovidersettings$factory;
/*     */     }
/*     */     
/*     */     public JsonElement serialize(ChunkProviderSettings.Factory p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */     {
/* 589 */       JsonObject jsonobject = new JsonObject();
/* 590 */       jsonobject.addProperty("coordinateScale", Float.valueOf(p_serialize_1_.coordinateScale));
/* 591 */       jsonobject.addProperty("heightScale", Float.valueOf(p_serialize_1_.heightScale));
/* 592 */       jsonobject.addProperty("lowerLimitScale", Float.valueOf(p_serialize_1_.lowerLimitScale));
/* 593 */       jsonobject.addProperty("upperLimitScale", Float.valueOf(p_serialize_1_.upperLimitScale));
/* 594 */       jsonobject.addProperty("depthNoiseScaleX", Float.valueOf(p_serialize_1_.depthNoiseScaleX));
/* 595 */       jsonobject.addProperty("depthNoiseScaleZ", Float.valueOf(p_serialize_1_.depthNoiseScaleZ));
/* 596 */       jsonobject.addProperty("depthNoiseScaleExponent", Float.valueOf(p_serialize_1_.depthNoiseScaleExponent));
/* 597 */       jsonobject.addProperty("mainNoiseScaleX", Float.valueOf(p_serialize_1_.mainNoiseScaleX));
/* 598 */       jsonobject.addProperty("mainNoiseScaleY", Float.valueOf(p_serialize_1_.mainNoiseScaleY));
/* 599 */       jsonobject.addProperty("mainNoiseScaleZ", Float.valueOf(p_serialize_1_.mainNoiseScaleZ));
/* 600 */       jsonobject.addProperty("baseSize", Float.valueOf(p_serialize_1_.baseSize));
/* 601 */       jsonobject.addProperty("stretchY", Float.valueOf(p_serialize_1_.stretchY));
/* 602 */       jsonobject.addProperty("biomeDepthWeight", Float.valueOf(p_serialize_1_.biomeDepthWeight));
/* 603 */       jsonobject.addProperty("biomeDepthOffset", Float.valueOf(p_serialize_1_.biomeDepthOffset));
/* 604 */       jsonobject.addProperty("biomeScaleWeight", Float.valueOf(p_serialize_1_.biomeScaleWeight));
/* 605 */       jsonobject.addProperty("biomeScaleOffset", Float.valueOf(p_serialize_1_.biomeScaleOffset));
/* 606 */       jsonobject.addProperty("seaLevel", Integer.valueOf(p_serialize_1_.seaLevel));
/* 607 */       jsonobject.addProperty("useCaves", Boolean.valueOf(p_serialize_1_.useCaves));
/* 608 */       jsonobject.addProperty("useDungeons", Boolean.valueOf(p_serialize_1_.useDungeons));
/* 609 */       jsonobject.addProperty("dungeonChance", Integer.valueOf(p_serialize_1_.dungeonChance));
/* 610 */       jsonobject.addProperty("useStrongholds", Boolean.valueOf(p_serialize_1_.useStrongholds));
/* 611 */       jsonobject.addProperty("useVillages", Boolean.valueOf(p_serialize_1_.useVillages));
/* 612 */       jsonobject.addProperty("useMineShafts", Boolean.valueOf(p_serialize_1_.useMineShafts));
/* 613 */       jsonobject.addProperty("useTemples", Boolean.valueOf(p_serialize_1_.useTemples));
/* 614 */       jsonobject.addProperty("useMonuments", Boolean.valueOf(p_serialize_1_.useMonuments));
/* 615 */       jsonobject.addProperty("useRavines", Boolean.valueOf(p_serialize_1_.useRavines));
/* 616 */       jsonobject.addProperty("useWaterLakes", Boolean.valueOf(p_serialize_1_.useWaterLakes));
/* 617 */       jsonobject.addProperty("waterLakeChance", Integer.valueOf(p_serialize_1_.waterLakeChance));
/* 618 */       jsonobject.addProperty("useLavaLakes", Boolean.valueOf(p_serialize_1_.useLavaLakes));
/* 619 */       jsonobject.addProperty("lavaLakeChance", Integer.valueOf(p_serialize_1_.lavaLakeChance));
/* 620 */       jsonobject.addProperty("useLavaOceans", Boolean.valueOf(p_serialize_1_.useLavaOceans));
/* 621 */       jsonobject.addProperty("fixedBiome", Integer.valueOf(p_serialize_1_.fixedBiome));
/* 622 */       jsonobject.addProperty("biomeSize", Integer.valueOf(p_serialize_1_.biomeSize));
/* 623 */       jsonobject.addProperty("riverSize", Integer.valueOf(p_serialize_1_.riverSize));
/* 624 */       jsonobject.addProperty("dirtSize", Integer.valueOf(p_serialize_1_.dirtSize));
/* 625 */       jsonobject.addProperty("dirtCount", Integer.valueOf(p_serialize_1_.dirtCount));
/* 626 */       jsonobject.addProperty("dirtMinHeight", Integer.valueOf(p_serialize_1_.dirtMinHeight));
/* 627 */       jsonobject.addProperty("dirtMaxHeight", Integer.valueOf(p_serialize_1_.dirtMaxHeight));
/* 628 */       jsonobject.addProperty("gravelSize", Integer.valueOf(p_serialize_1_.gravelSize));
/* 629 */       jsonobject.addProperty("gravelCount", Integer.valueOf(p_serialize_1_.gravelCount));
/* 630 */       jsonobject.addProperty("gravelMinHeight", Integer.valueOf(p_serialize_1_.gravelMinHeight));
/* 631 */       jsonobject.addProperty("gravelMaxHeight", Integer.valueOf(p_serialize_1_.gravelMaxHeight));
/* 632 */       jsonobject.addProperty("graniteSize", Integer.valueOf(p_serialize_1_.graniteSize));
/* 633 */       jsonobject.addProperty("graniteCount", Integer.valueOf(p_serialize_1_.graniteCount));
/* 634 */       jsonobject.addProperty("graniteMinHeight", Integer.valueOf(p_serialize_1_.graniteMinHeight));
/* 635 */       jsonobject.addProperty("graniteMaxHeight", Integer.valueOf(p_serialize_1_.graniteMaxHeight));
/* 636 */       jsonobject.addProperty("dioriteSize", Integer.valueOf(p_serialize_1_.dioriteSize));
/* 637 */       jsonobject.addProperty("dioriteCount", Integer.valueOf(p_serialize_1_.dioriteCount));
/* 638 */       jsonobject.addProperty("dioriteMinHeight", Integer.valueOf(p_serialize_1_.dioriteMinHeight));
/* 639 */       jsonobject.addProperty("dioriteMaxHeight", Integer.valueOf(p_serialize_1_.dioriteMaxHeight));
/* 640 */       jsonobject.addProperty("andesiteSize", Integer.valueOf(p_serialize_1_.andesiteSize));
/* 641 */       jsonobject.addProperty("andesiteCount", Integer.valueOf(p_serialize_1_.andesiteCount));
/* 642 */       jsonobject.addProperty("andesiteMinHeight", Integer.valueOf(p_serialize_1_.andesiteMinHeight));
/* 643 */       jsonobject.addProperty("andesiteMaxHeight", Integer.valueOf(p_serialize_1_.andesiteMaxHeight));
/* 644 */       jsonobject.addProperty("coalSize", Integer.valueOf(p_serialize_1_.coalSize));
/* 645 */       jsonobject.addProperty("coalCount", Integer.valueOf(p_serialize_1_.coalCount));
/* 646 */       jsonobject.addProperty("coalMinHeight", Integer.valueOf(p_serialize_1_.coalMinHeight));
/* 647 */       jsonobject.addProperty("coalMaxHeight", Integer.valueOf(p_serialize_1_.coalMaxHeight));
/* 648 */       jsonobject.addProperty("ironSize", Integer.valueOf(p_serialize_1_.ironSize));
/* 649 */       jsonobject.addProperty("ironCount", Integer.valueOf(p_serialize_1_.ironCount));
/* 650 */       jsonobject.addProperty("ironMinHeight", Integer.valueOf(p_serialize_1_.ironMinHeight));
/* 651 */       jsonobject.addProperty("ironMaxHeight", Integer.valueOf(p_serialize_1_.ironMaxHeight));
/* 652 */       jsonobject.addProperty("goldSize", Integer.valueOf(p_serialize_1_.goldSize));
/* 653 */       jsonobject.addProperty("goldCount", Integer.valueOf(p_serialize_1_.goldCount));
/* 654 */       jsonobject.addProperty("goldMinHeight", Integer.valueOf(p_serialize_1_.goldMinHeight));
/* 655 */       jsonobject.addProperty("goldMaxHeight", Integer.valueOf(p_serialize_1_.goldMaxHeight));
/* 656 */       jsonobject.addProperty("redstoneSize", Integer.valueOf(p_serialize_1_.redstoneSize));
/* 657 */       jsonobject.addProperty("redstoneCount", Integer.valueOf(p_serialize_1_.redstoneCount));
/* 658 */       jsonobject.addProperty("redstoneMinHeight", Integer.valueOf(p_serialize_1_.redstoneMinHeight));
/* 659 */       jsonobject.addProperty("redstoneMaxHeight", Integer.valueOf(p_serialize_1_.redstoneMaxHeight));
/* 660 */       jsonobject.addProperty("diamondSize", Integer.valueOf(p_serialize_1_.diamondSize));
/* 661 */       jsonobject.addProperty("diamondCount", Integer.valueOf(p_serialize_1_.diamondCount));
/* 662 */       jsonobject.addProperty("diamondMinHeight", Integer.valueOf(p_serialize_1_.diamondMinHeight));
/* 663 */       jsonobject.addProperty("diamondMaxHeight", Integer.valueOf(p_serialize_1_.diamondMaxHeight));
/* 664 */       jsonobject.addProperty("lapisSize", Integer.valueOf(p_serialize_1_.lapisSize));
/* 665 */       jsonobject.addProperty("lapisCount", Integer.valueOf(p_serialize_1_.lapisCount));
/* 666 */       jsonobject.addProperty("lapisCenterHeight", Integer.valueOf(p_serialize_1_.lapisCenterHeight));
/* 667 */       jsonobject.addProperty("lapisSpread", Integer.valueOf(p_serialize_1_.lapisSpread));
/* 668 */       return jsonobject;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\ChunkProviderSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */