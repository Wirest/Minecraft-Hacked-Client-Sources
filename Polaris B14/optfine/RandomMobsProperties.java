/*     */ package optfine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class RandomMobsProperties
/*     */ {
/*  12 */   public String name = null;
/*  13 */   public String basePath = null;
/*  14 */   public ResourceLocation[] resourceLocations = null;
/*  15 */   public RandomMobsRule[] rules = null;
/*     */   
/*     */   public RandomMobsProperties(String p_i51_1_, ResourceLocation[] p_i51_2_)
/*     */   {
/*  19 */     ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
/*  20 */     this.name = connectedparser.parseName(p_i51_1_);
/*  21 */     this.basePath = connectedparser.parseBasePath(p_i51_1_);
/*  22 */     this.resourceLocations = p_i51_2_;
/*     */   }
/*     */   
/*     */   public RandomMobsProperties(Properties p_i52_1_, String p_i52_2_, ResourceLocation p_i52_3_)
/*     */   {
/*  27 */     ConnectedParser connectedparser = new ConnectedParser("RandomMobs");
/*  28 */     this.name = connectedparser.parseName(p_i52_2_);
/*  29 */     this.basePath = connectedparser.parseBasePath(p_i52_2_);
/*  30 */     this.rules = parseRules(p_i52_1_, p_i52_3_, connectedparser);
/*     */   }
/*     */   
/*     */   public ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_1_, EntityLiving p_getTextureLocation_2_)
/*     */   {
/*  35 */     if (this.rules != null)
/*     */     {
/*  37 */       for (int i = 0; i < this.rules.length; i++)
/*     */       {
/*  39 */         RandomMobsRule randommobsrule = this.rules[i];
/*     */         
/*  41 */         if (randommobsrule.matches(p_getTextureLocation_2_))
/*     */         {
/*  43 */           return randommobsrule.getTextureLocation(p_getTextureLocation_1_, p_getTextureLocation_2_.randomMobsId);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  48 */     if (this.resourceLocations != null)
/*     */     {
/*  50 */       int j = p_getTextureLocation_2_.randomMobsId;
/*  51 */       int k = j % this.resourceLocations.length;
/*  52 */       return this.resourceLocations[k];
/*     */     }
/*     */     
/*     */ 
/*  56 */     return p_getTextureLocation_1_;
/*     */   }
/*     */   
/*     */ 
/*     */   private RandomMobsRule[] parseRules(Properties p_parseRules_1_, ResourceLocation p_parseRules_2_, ConnectedParser p_parseRules_3_)
/*     */   {
/*  62 */     List list = new ArrayList();
/*  63 */     int i = p_parseRules_1_.size();
/*     */     
/*  65 */     for (int j = 0; j < i; j++)
/*     */     {
/*  67 */       int k = j + 1;
/*  68 */       String s = p_parseRules_1_.getProperty("skins." + k);
/*     */       
/*  70 */       if (s != null)
/*     */       {
/*  72 */         int[] aint = p_parseRules_3_.parseIntList(s);
/*  73 */         int[] aint1 = p_parseRules_3_.parseIntList(p_parseRules_1_.getProperty("weights." + k));
/*  74 */         BiomeGenBase[] abiomegenbase = p_parseRules_3_.parseBiomes(p_parseRules_1_.getProperty("biomes." + k));
/*  75 */         RangeListInt rangelistint = p_parseRules_3_.parseRangeListInt(p_parseRules_1_.getProperty("heights." + k));
/*     */         
/*  77 */         if (rangelistint == null)
/*     */         {
/*  79 */           rangelistint = parseMinMaxHeight(p_parseRules_1_, k);
/*     */         }
/*     */         
/*  82 */         RandomMobsRule randommobsrule = new RandomMobsRule(p_parseRules_2_, aint, aint1, abiomegenbase, rangelistint);
/*  83 */         list.add(randommobsrule);
/*     */       }
/*     */     }
/*     */     
/*  87 */     RandomMobsRule[] arandommobsrule = (RandomMobsRule[])list.toArray(new RandomMobsRule[list.size()]);
/*  88 */     return arandommobsrule;
/*     */   }
/*     */   
/*     */   private RangeListInt parseMinMaxHeight(Properties p_parseMinMaxHeight_1_, int p_parseMinMaxHeight_2_)
/*     */   {
/*  93 */     String s = p_parseMinMaxHeight_1_.getProperty("minHeight." + p_parseMinMaxHeight_2_);
/*  94 */     String s1 = p_parseMinMaxHeight_1_.getProperty("maxHeight." + p_parseMinMaxHeight_2_);
/*     */     
/*  96 */     if ((s == null) && (s1 == null))
/*     */     {
/*  98 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 102 */     int i = 0;
/*     */     
/* 104 */     if (s != null)
/*     */     {
/* 106 */       i = Config.parseInt(s, -1);
/*     */       
/* 108 */       if (i < 0)
/*     */       {
/* 110 */         Config.warn("Invalid minHeight: " + s);
/* 111 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 115 */     int j = 256;
/*     */     
/* 117 */     if (s1 != null)
/*     */     {
/* 119 */       j = Config.parseInt(s1, -1);
/*     */       
/* 121 */       if (j < 0)
/*     */       {
/* 123 */         Config.warn("Invalid maxHeight: " + s1);
/* 124 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 128 */     if (j < 0)
/*     */     {
/* 130 */       Config.warn("Invalid minHeight, maxHeight: " + s + ", " + s1);
/* 131 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 135 */     RangeListInt rangelistint = new RangeListInt();
/* 136 */     rangelistint.addRange(new RangeInt(i, j));
/* 137 */     return rangelistint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isValid(String p_isValid_1_)
/*     */   {
/* 144 */     if ((this.resourceLocations == null) && (this.rules == null))
/*     */     {
/* 146 */       Config.warn("No skins specified: " + p_isValid_1_);
/* 147 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 151 */     if (this.rules != null)
/*     */     {
/* 153 */       for (int i = 0; i < this.rules.length; i++)
/*     */       {
/* 155 */         RandomMobsRule randommobsrule = this.rules[i];
/*     */         
/* 157 */         if (!randommobsrule.isValid(p_isValid_1_))
/*     */         {
/* 159 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 164 */     if (this.resourceLocations != null)
/*     */     {
/* 166 */       for (int j = 0; j < this.resourceLocations.length; j++)
/*     */       {
/* 168 */         ResourceLocation resourcelocation = this.resourceLocations[j];
/*     */         
/* 170 */         if (!Config.hasResource(resourcelocation))
/*     */         {
/* 172 */           Config.warn("Texture not found: " + resourcelocation.getResourcePath());
/* 173 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 178 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\RandomMobsProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */