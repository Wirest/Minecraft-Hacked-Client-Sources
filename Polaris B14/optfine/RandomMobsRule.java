/*     */ package optfine;
/*     */ 
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class RandomMobsRule
/*     */ {
/*   9 */   private ResourceLocation baseResLoc = null;
/*  10 */   private int[] skins = null;
/*  11 */   private ResourceLocation[] resourceLocations = null;
/*  12 */   private int[] weights = null;
/*  13 */   private BiomeGenBase[] biomes = null;
/*  14 */   private RangeListInt heights = null;
/*  15 */   public int[] sumWeights = null;
/*  16 */   public int sumAllWeights = 1;
/*     */   
/*     */   public RandomMobsRule(ResourceLocation p_i53_1_, int[] p_i53_2_, int[] p_i53_3_, BiomeGenBase[] p_i53_4_, RangeListInt p_i53_5_)
/*     */   {
/*  20 */     this.baseResLoc = p_i53_1_;
/*  21 */     this.skins = p_i53_2_;
/*  22 */     this.weights = p_i53_3_;
/*  23 */     this.biomes = p_i53_4_;
/*  24 */     this.heights = p_i53_5_;
/*     */   }
/*     */   
/*     */   public boolean isValid(String p_isValid_1_)
/*     */   {
/*  29 */     this.resourceLocations = new ResourceLocation[this.skins.length];
/*  30 */     ResourceLocation resourcelocation = RandomMobs.getMcpatcherLocation(this.baseResLoc);
/*     */     
/*  32 */     if (resourcelocation == null)
/*     */     {
/*  34 */       Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
/*  35 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  39 */     for (int i = 0; i < this.resourceLocations.length; i++)
/*     */     {
/*  41 */       int j = this.skins[i];
/*     */       
/*  43 */       if (j <= 1)
/*     */       {
/*  45 */         this.resourceLocations[i] = this.baseResLoc;
/*     */       }
/*     */       else
/*     */       {
/*  49 */         ResourceLocation resourcelocation1 = RandomMobs.getLocationIndexed(resourcelocation, j);
/*     */         
/*  51 */         if (resourcelocation1 == null)
/*     */         {
/*  53 */           Config.warn("Invalid path: " + this.baseResLoc.getResourcePath());
/*  54 */           return false;
/*     */         }
/*     */         
/*  57 */         if (!Config.hasResource(resourcelocation1))
/*     */         {
/*  59 */           Config.warn("Texture not found: " + resourcelocation1.getResourcePath());
/*  60 */           return false;
/*     */         }
/*     */         
/*  63 */         this.resourceLocations[i] = resourcelocation1;
/*     */       }
/*     */     }
/*     */     
/*  67 */     if (this.weights != null)
/*     */     {
/*  69 */       if (this.weights.length > this.resourceLocations.length)
/*     */       {
/*  71 */         Config.warn("More weights defined than skins, trimming weights: " + p_isValid_1_);
/*  72 */         int[] aint = new int[this.resourceLocations.length];
/*  73 */         System.arraycopy(this.weights, 0, aint, 0, aint.length);
/*  74 */         this.weights = aint;
/*     */       }
/*     */       
/*  77 */       if (this.weights.length < this.resourceLocations.length)
/*     */       {
/*  79 */         Config.warn("Less weights defined than skins, expanding weights: " + p_isValid_1_);
/*  80 */         int[] aint1 = new int[this.resourceLocations.length];
/*  81 */         System.arraycopy(this.weights, 0, aint1, 0, this.weights.length);
/*  82 */         int l = ConnectedUtils.getAverage(this.weights);
/*     */         
/*  84 */         for (int j1 = this.weights.length; j1 < aint1.length; j1++)
/*     */         {
/*  86 */           aint1[j1] = l;
/*     */         }
/*     */         
/*  89 */         this.weights = aint1;
/*     */       }
/*     */       
/*  92 */       this.sumWeights = new int[this.weights.length];
/*  93 */       int k = 0;
/*     */       
/*  95 */       for (int i1 = 0; i1 < this.weights.length; i1++)
/*     */       {
/*  97 */         if (this.weights[i1] < 0)
/*     */         {
/*  99 */           Config.warn("Invalid weight: " + this.weights[i1]);
/* 100 */           return false;
/*     */         }
/*     */         
/* 103 */         k += this.weights[i1];
/* 104 */         this.sumWeights[i1] = k;
/*     */       }
/*     */       
/* 107 */       this.sumAllWeights = k;
/*     */       
/* 109 */       if (this.sumAllWeights <= 0)
/*     */       {
/* 111 */         Config.warn("Invalid sum of all weights: " + k);
/* 112 */         this.sumAllWeights = 1;
/*     */       }
/*     */     }
/*     */     
/* 116 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean matches(EntityLiving p_matches_1_)
/*     */   {
/* 122 */     if (this.biomes != null)
/*     */     {
/* 124 */       BiomeGenBase biomegenbase = p_matches_1_.spawnBiome;
/* 125 */       boolean flag = false;
/*     */       
/* 127 */       for (int i = 0; i < this.biomes.length; i++)
/*     */       {
/* 129 */         BiomeGenBase biomegenbase1 = this.biomes[i];
/*     */         
/* 131 */         if (biomegenbase1 == biomegenbase)
/*     */         {
/* 133 */           flag = true;
/* 134 */           break;
/*     */         }
/*     */       }
/*     */       
/* 138 */       if (!flag)
/*     */       {
/* 140 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 144 */     return (this.heights != null) && (p_matches_1_.spawnPosition != null) ? this.heights.isInRange(p_matches_1_.spawnPosition.getY()) : true;
/*     */   }
/*     */   
/*     */   public ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_1_, int p_getTextureLocation_2_)
/*     */   {
/* 149 */     int i = 0;
/*     */     
/* 151 */     if (this.weights == null)
/*     */     {
/* 153 */       i = p_getTextureLocation_2_ % this.resourceLocations.length;
/*     */     }
/*     */     else
/*     */     {
/* 157 */       int j = p_getTextureLocation_2_ % this.sumAllWeights;
/*     */       
/* 159 */       for (int k = 0; k < this.sumWeights.length; k++)
/*     */       {
/* 161 */         if (this.sumWeights[k] > j)
/*     */         {
/* 163 */           i = k;
/* 164 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 169 */     return this.resourceLocations[i];
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\RandomMobsRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */