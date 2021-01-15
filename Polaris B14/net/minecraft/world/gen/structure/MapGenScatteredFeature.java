/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ 
/*     */ public class MapGenScatteredFeature extends MapGenStructure
/*     */ {
/*  17 */   private static final List<BiomeGenBase> biomelist = java.util.Arrays.asList(new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland });
/*     */   
/*     */   private List<BiomeGenBase.SpawnListEntry> scatteredFeatureSpawnList;
/*     */   
/*     */   private int maxDistanceBetweenScatteredFeatures;
/*     */   
/*     */   private int minDistanceBetweenScatteredFeatures;
/*     */   
/*     */ 
/*     */   public MapGenScatteredFeature()
/*     */   {
/*  28 */     this.scatteredFeatureSpawnList = com.google.common.collect.Lists.newArrayList();
/*  29 */     this.maxDistanceBetweenScatteredFeatures = 32;
/*  30 */     this.minDistanceBetweenScatteredFeatures = 8;
/*  31 */     this.scatteredFeatureSpawnList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 1, 1, 1));
/*     */   }
/*     */   
/*     */   public MapGenScatteredFeature(Map<String, String> p_i2061_1_)
/*     */   {
/*  36 */     this();
/*     */     
/*  38 */     for (Map.Entry<String, String> entry : p_i2061_1_.entrySet())
/*     */     {
/*  40 */       if (((String)entry.getKey()).equals("distance"))
/*     */       {
/*  42 */         this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.maxDistanceBetweenScatteredFeatures, this.minDistanceBetweenScatteredFeatures + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getStructureName()
/*     */   {
/*  49 */     return "Temple";
/*     */   }
/*     */   
/*     */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
/*     */   {
/*  54 */     int i = chunkX;
/*  55 */     int j = chunkZ;
/*     */     
/*  57 */     if (chunkX < 0)
/*     */     {
/*  59 */       chunkX -= this.maxDistanceBetweenScatteredFeatures - 1;
/*     */     }
/*     */     
/*  62 */     if (chunkZ < 0)
/*     */     {
/*  64 */       chunkZ -= this.maxDistanceBetweenScatteredFeatures - 1;
/*     */     }
/*     */     
/*  67 */     int k = chunkX / this.maxDistanceBetweenScatteredFeatures;
/*  68 */     int l = chunkZ / this.maxDistanceBetweenScatteredFeatures;
/*  69 */     Random random = this.worldObj.setRandomSeed(k, l, 14357617);
/*  70 */     k *= this.maxDistanceBetweenScatteredFeatures;
/*  71 */     l *= this.maxDistanceBetweenScatteredFeatures;
/*  72 */     k += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
/*  73 */     l += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
/*     */     
/*  75 */     if ((i == k) && (j == l))
/*     */     {
/*  77 */       BiomeGenBase biomegenbase = this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(i * 16 + 8, 0, j * 16 + 8));
/*     */       
/*  79 */       if (biomegenbase == null)
/*     */       {
/*  81 */         return false;
/*     */       }
/*     */       
/*  84 */       for (BiomeGenBase biomegenbase1 : biomelist)
/*     */       {
/*  86 */         if (biomegenbase == biomegenbase1)
/*     */         {
/*  88 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   protected StructureStart getStructureStart(int chunkX, int chunkZ)
/*     */   {
/*  98 */     return new Start(this.worldObj, this.rand, chunkX, chunkZ);
/*     */   }
/*     */   
/*     */   public boolean func_175798_a(BlockPos p_175798_1_)
/*     */   {
/* 103 */     StructureStart structurestart = func_175797_c(p_175798_1_);
/*     */     
/* 105 */     if ((structurestart != null) && ((structurestart instanceof Start)) && (!structurestart.components.isEmpty()))
/*     */     {
/* 107 */       StructureComponent structurecomponent = (StructureComponent)structurestart.components.getFirst();
/* 108 */       return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
/*     */     }
/*     */     
/*     */ 
/* 112 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<BiomeGenBase.SpawnListEntry> getScatteredFeatureSpawnList()
/*     */   {
/* 118 */     return this.scatteredFeatureSpawnList;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Start
/*     */     extends StructureStart
/*     */   {
/*     */     public Start() {}
/*     */     
/*     */     public Start(World worldIn, Random p_i2060_2_, int p_i2060_3_, int p_i2060_4_)
/*     */     {
/* 129 */       super(p_i2060_4_);
/* 130 */       BiomeGenBase biomegenbase = worldIn.getBiomeGenForCoords(new BlockPos(p_i2060_3_ * 16 + 8, 0, p_i2060_4_ * 16 + 8));
/*     */       
/* 132 */       if ((biomegenbase != BiomeGenBase.jungle) && (biomegenbase != BiomeGenBase.jungleHills))
/*     */       {
/* 134 */         if (biomegenbase == BiomeGenBase.swampland)
/*     */         {
/* 136 */           ComponentScatteredFeaturePieces.SwampHut componentscatteredfeaturepieces$swamphut = new ComponentScatteredFeaturePieces.SwampHut(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
/* 137 */           this.components.add(componentscatteredfeaturepieces$swamphut);
/*     */         }
/* 139 */         else if ((biomegenbase == BiomeGenBase.desert) || (biomegenbase == BiomeGenBase.desertHills))
/*     */         {
/* 141 */           ComponentScatteredFeaturePieces.DesertPyramid componentscatteredfeaturepieces$desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
/* 142 */           this.components.add(componentscatteredfeaturepieces$desertpyramid);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 147 */         ComponentScatteredFeaturePieces.JunglePyramid componentscatteredfeaturepieces$junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(p_i2060_2_, p_i2060_3_ * 16, p_i2060_4_ * 16);
/* 148 */         this.components.add(componentscatteredfeaturepieces$junglepyramid);
/*     */       }
/*     */       
/* 151 */       updateBoundingBox();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\MapGenScatteredFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */