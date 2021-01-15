/*     */ package optfine;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class RandomMobs
/*     */ {
/*  22 */   private static Map locationProperties = new HashMap();
/*  23 */   private static RenderGlobal renderGlobal = null;
/*  24 */   private static boolean initialized = false;
/*  25 */   private static Random random = new Random();
/*  26 */   private static boolean working = false;
/*     */   public static final String SUFFIX_PNG = ".png";
/*     */   public static final String SUFFIX_PROPERTIES = ".properties";
/*     */   public static final String PREFIX_TEXTURES_ENTITY = "textures/entity/";
/*     */   public static final String PREFIX_MCPATCHER_MOB = "mcpatcher/mob/";
/*  31 */   private static final String[] DEPENDANT_SUFFIXES = { "_armor", "_eyes", "_exploding", "_shooting", "_fur", "_eyes", "_invulnerable", "_angry", "_tame", "_collar" };
/*     */   
/*     */   public static void entityLoaded(Entity p_entityLoaded_0_)
/*     */   {
/*  35 */     if ((p_entityLoaded_0_ instanceof EntityLiving))
/*     */     {
/*  37 */       EntityLiving entityliving = (EntityLiving)p_entityLoaded_0_;
/*  38 */       WorldServer worldserver = Config.getWorldServer();
/*     */       
/*  40 */       if (worldserver != null)
/*     */       {
/*  42 */         Entity entity = worldserver.getEntityByID(p_entityLoaded_0_.getEntityId());
/*     */         
/*  44 */         if ((entity instanceof EntityLiving))
/*     */         {
/*  46 */           EntityLiving entityliving1 = (EntityLiving)entity;
/*  47 */           UUID uuid = entityliving1.getUniqueID();
/*  48 */           long i = uuid.getLeastSignificantBits();
/*  49 */           int j = (int)(i & 0x7FFFFFFF);
/*  50 */           entityliving.randomMobsId = j;
/*  51 */           entityliving.spawnPosition = entityliving.getPosition();
/*  52 */           entityliving.spawnBiome = worldserver.getBiomeGenForCoords(entityliving.spawnPosition);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void worldChanged(World p_worldChanged_0_, World p_worldChanged_1_)
/*     */   {
/*  60 */     if (p_worldChanged_1_ != null)
/*     */     {
/*  62 */       List list = p_worldChanged_1_.getLoadedEntityList();
/*     */       
/*  64 */       for (int i = 0; i < list.size(); i++)
/*     */       {
/*  66 */         Entity entity = (Entity)list.get(i);
/*  67 */         entityLoaded(entity);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_0_)
/*     */   {
/*  74 */     if (working)
/*     */     {
/*  76 */       return p_getTextureLocation_0_;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  84 */       working = true;
/*     */       
/*  86 */       if (!initialized)
/*     */       {
/*  88 */         initialize();
/*     */       }
/*     */       
/*  91 */       if (renderGlobal != null)
/*     */       {
/*  93 */         Entity entity1 = renderGlobal.renderedEntity;
/*     */         ResourceLocation localResourceLocation1;
/*  95 */         if (!(entity1 instanceof EntityLiving))
/*     */         {
/*  97 */           ResourceLocation resourcelocation2 = p_getTextureLocation_0_;
/*  98 */           return resourcelocation2;
/*     */         }
/*     */         
/* 101 */         EntityLiving entityliving = (EntityLiving)entity1;
/* 102 */         String s = p_getTextureLocation_0_.getResourcePath();
/*     */         
/* 104 */         if (!s.startsWith("textures/entity/"))
/*     */         {
/* 106 */           ResourceLocation resourcelocation3 = p_getTextureLocation_0_;
/* 107 */           return resourcelocation3;
/*     */         }
/*     */         
/* 110 */         RandomMobsProperties randommobsproperties = getProperties(p_getTextureLocation_0_);
/*     */         
/* 112 */         if (randommobsproperties == null)
/*     */         {
/* 114 */           ResourceLocation resourcelocation4 = p_getTextureLocation_0_;
/* 115 */           return resourcelocation4;
/*     */         }
/*     */         
/* 118 */         ResourceLocation resourcelocation1 = randommobsproperties.getTextureLocation(p_getTextureLocation_0_, entityliving);
/* 119 */         return resourcelocation1;
/*     */       }
/*     */       
/* 122 */       entity = p_getTextureLocation_0_;
/*     */     }
/*     */     finally {
/*     */       ResourceLocation entity;
/* 126 */       working = false; } ResourceLocation entity; working = false;
/*     */     
/*     */ 
/* 129 */     return entity;
/*     */   }
/*     */   
/*     */ 
/*     */   private static RandomMobsProperties getProperties(ResourceLocation p_getProperties_0_)
/*     */   {
/* 135 */     String s = p_getProperties_0_.getResourcePath();
/* 136 */     RandomMobsProperties randommobsproperties = (RandomMobsProperties)locationProperties.get(s);
/*     */     
/* 138 */     if (randommobsproperties == null)
/*     */     {
/* 140 */       randommobsproperties = makeProperties(p_getProperties_0_);
/* 141 */       locationProperties.put(s, randommobsproperties);
/*     */     }
/*     */     
/* 144 */     return randommobsproperties;
/*     */   }
/*     */   
/*     */   private static RandomMobsProperties makeProperties(ResourceLocation p_makeProperties_0_)
/*     */   {
/* 149 */     String s = p_makeProperties_0_.getResourcePath();
/* 150 */     ResourceLocation resourcelocation = getPropertyLocation(p_makeProperties_0_);
/*     */     
/* 152 */     if (resourcelocation != null)
/*     */     {
/* 154 */       RandomMobsProperties randommobsproperties = parseProperties(resourcelocation, p_makeProperties_0_);
/*     */       
/* 156 */       if (randommobsproperties != null)
/*     */       {
/* 158 */         return randommobsproperties;
/*     */       }
/*     */     }
/*     */     
/* 162 */     ResourceLocation[] aresourcelocation = getTextureVariants(p_makeProperties_0_);
/* 163 */     return new RandomMobsProperties(s, aresourcelocation);
/*     */   }
/*     */   
/*     */   private static RandomMobsProperties parseProperties(ResourceLocation p_parseProperties_0_, ResourceLocation p_parseProperties_1_)
/*     */   {
/*     */     try
/*     */     {
/* 170 */       String s = p_parseProperties_0_.getResourcePath();
/* 171 */       Config.dbg("RandomMobs: " + p_parseProperties_1_.getResourcePath() + ", variants: " + s);
/* 172 */       InputStream inputstream = Config.getResourceStream(p_parseProperties_0_);
/*     */       
/* 174 */       if (inputstream == null)
/*     */       {
/* 176 */         Config.warn("RandomMobs properties not found: " + s);
/* 177 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 181 */       Properties properties = new Properties();
/* 182 */       properties.load(inputstream);
/* 183 */       RandomMobsProperties randommobsproperties = new RandomMobsProperties(properties, s, p_parseProperties_1_);
/* 184 */       return !randommobsproperties.isValid(s) ? null : randommobsproperties;
/*     */ 
/*     */     }
/*     */     catch (FileNotFoundException var6)
/*     */     {
/* 189 */       Config.warn("RandomMobs file not found: " + p_parseProperties_1_.getResourcePath());
/* 190 */       return null;
/*     */     }
/*     */     catch (IOException ioexception)
/*     */     {
/* 194 */       ioexception.printStackTrace(); }
/* 195 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static ResourceLocation getPropertyLocation(ResourceLocation p_getPropertyLocation_0_)
/*     */   {
/* 201 */     ResourceLocation resourcelocation = getMcpatcherLocation(p_getPropertyLocation_0_);
/*     */     
/* 203 */     if (resourcelocation == null)
/*     */     {
/* 205 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 209 */     String s = resourcelocation.getResourceDomain();
/* 210 */     String s1 = resourcelocation.getResourcePath();
/* 211 */     String s2 = s1;
/*     */     
/* 213 */     if (s1.endsWith(".png"))
/*     */     {
/* 215 */       s2 = s1.substring(0, s1.length() - ".png".length());
/*     */     }
/*     */     
/* 218 */     String s3 = s2 + ".properties";
/* 219 */     ResourceLocation resourcelocation1 = new ResourceLocation(s, s3);
/*     */     
/* 221 */     if (Config.hasResource(resourcelocation1))
/*     */     {
/* 223 */       return resourcelocation1;
/*     */     }
/*     */     
/*     */ 
/* 227 */     String s4 = getParentPath(s2);
/*     */     
/* 229 */     if (s4 == null)
/*     */     {
/* 231 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 235 */     ResourceLocation resourcelocation2 = new ResourceLocation(s, s4 + ".properties");
/* 236 */     return Config.hasResource(resourcelocation2) ? resourcelocation2 : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ResourceLocation getMcpatcherLocation(ResourceLocation p_getMcpatcherLocation_0_)
/*     */   {
/* 244 */     String s = p_getMcpatcherLocation_0_.getResourcePath();
/*     */     
/* 246 */     if (!s.startsWith("textures/entity/"))
/*     */     {
/* 248 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 252 */     String s1 = "mcpatcher/mob/" + s.substring("textures/entity/".length());
/* 253 */     return new ResourceLocation(p_getMcpatcherLocation_0_.getResourceDomain(), s1);
/*     */   }
/*     */   
/*     */ 
/*     */   public static ResourceLocation getLocationIndexed(ResourceLocation p_getLocationIndexed_0_, int p_getLocationIndexed_1_)
/*     */   {
/* 259 */     if (p_getLocationIndexed_0_ == null)
/*     */     {
/* 261 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 265 */     String s = p_getLocationIndexed_0_.getResourcePath();
/* 266 */     int i = s.lastIndexOf('.');
/*     */     
/* 268 */     if (i < 0)
/*     */     {
/* 270 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 274 */     String s1 = s.substring(0, i);
/* 275 */     String s2 = s.substring(i);
/* 276 */     String s3 = s1 + p_getLocationIndexed_1_ + s2;
/* 277 */     ResourceLocation resourcelocation = new ResourceLocation(p_getLocationIndexed_0_.getResourceDomain(), s3);
/* 278 */     return resourcelocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static String getParentPath(String p_getParentPath_0_)
/*     */   {
/* 285 */     for (int i = 0; i < DEPENDANT_SUFFIXES.length; i++)
/*     */     {
/* 287 */       String s = DEPENDANT_SUFFIXES[i];
/*     */       
/* 289 */       if (p_getParentPath_0_.endsWith(s))
/*     */       {
/* 291 */         String s1 = p_getParentPath_0_.substring(0, p_getParentPath_0_.length() - s.length());
/* 292 */         return s1;
/*     */       }
/*     */     }
/*     */     
/* 296 */     return null;
/*     */   }
/*     */   
/*     */   private static ResourceLocation[] getTextureVariants(ResourceLocation p_getTextureVariants_0_)
/*     */   {
/* 301 */     List list = new ArrayList();
/* 302 */     list.add(p_getTextureVariants_0_);
/* 303 */     ResourceLocation resourcelocation = getMcpatcherLocation(p_getTextureVariants_0_);
/*     */     
/* 305 */     if (resourcelocation == null)
/*     */     {
/* 307 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 311 */     for (int i = 1; i < list.size() + 10; i++)
/*     */     {
/* 313 */       int j = i + 1;
/* 314 */       ResourceLocation resourcelocation1 = getLocationIndexed(resourcelocation, j);
/*     */       
/* 316 */       if (Config.hasResource(resourcelocation1))
/*     */       {
/* 318 */         list.add(resourcelocation1);
/*     */       }
/*     */     }
/*     */     
/* 322 */     if (list.size() <= 1)
/*     */     {
/* 324 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 328 */     ResourceLocation[] aresourcelocation = (ResourceLocation[])list.toArray(new ResourceLocation[list.size()]);
/* 329 */     Config.dbg("RandomMobs: " + p_getTextureVariants_0_.getResourcePath() + ", variants: " + aresourcelocation.length);
/* 330 */     return aresourcelocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void resetTextures()
/*     */   {
/* 337 */     locationProperties.clear();
/*     */     
/* 339 */     if (Config.isRandomMobs())
/*     */     {
/* 341 */       initialize();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void initialize()
/*     */   {
/* 347 */     renderGlobal = Config.getRenderGlobal();
/*     */     
/* 349 */     if (renderGlobal != null)
/*     */     {
/* 351 */       initialized = true;
/* 352 */       List list = new ArrayList();
/* 353 */       list.add("bat");
/* 354 */       list.add("blaze");
/* 355 */       list.add("cat/black");
/* 356 */       list.add("cat/ocelot");
/* 357 */       list.add("cat/red");
/* 358 */       list.add("cat/siamese");
/* 359 */       list.add("chicken");
/* 360 */       list.add("cow/cow");
/* 361 */       list.add("cow/mooshroom");
/* 362 */       list.add("creeper/creeper");
/* 363 */       list.add("enderman/enderman");
/* 364 */       list.add("enderman/enderman_eyes");
/* 365 */       list.add("ghast/ghast");
/* 366 */       list.add("ghast/ghast_shooting");
/* 367 */       list.add("iron_golem");
/* 368 */       list.add("pig/pig");
/* 369 */       list.add("sheep/sheep");
/* 370 */       list.add("sheep/sheep_fur");
/* 371 */       list.add("silverfish");
/* 372 */       list.add("skeleton/skeleton");
/* 373 */       list.add("skeleton/wither_skeleton");
/* 374 */       list.add("slime/slime");
/* 375 */       list.add("slime/magmacube");
/* 376 */       list.add("snowman");
/* 377 */       list.add("spider/cave_spider");
/* 378 */       list.add("spider/spider");
/* 379 */       list.add("spider_eyes");
/* 380 */       list.add("squid");
/* 381 */       list.add("villager/villager");
/* 382 */       list.add("villager/butcher");
/* 383 */       list.add("villager/farmer");
/* 384 */       list.add("villager/librarian");
/* 385 */       list.add("villager/priest");
/* 386 */       list.add("villager/smith");
/* 387 */       list.add("wither/wither");
/* 388 */       list.add("wither/wither_armor");
/* 389 */       list.add("wither/wither_invulnerable");
/* 390 */       list.add("wolf/wolf");
/* 391 */       list.add("wolf/wolf_angry");
/* 392 */       list.add("wolf/wolf_collar");
/* 393 */       list.add("wolf/wolf_tame");
/* 394 */       list.add("zombie_pigman");
/* 395 */       list.add("zombie/zombie");
/* 396 */       list.add("zombie/zombie_villager");
/*     */       
/* 398 */       for (int i = 0; i < list.size(); i++)
/*     */       {
/* 400 */         String s = (String)list.get(i);
/* 401 */         String s1 = "textures/entity/" + s + ".png";
/* 402 */         ResourceLocation resourcelocation = new ResourceLocation(s1);
/*     */         
/* 404 */         if (!Config.hasResource(resourcelocation))
/*     */         {
/* 406 */           Config.warn("Not found: " + resourcelocation);
/*     */         }
/*     */         
/* 409 */         getProperties(resourcelocation);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\RandomMobs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */