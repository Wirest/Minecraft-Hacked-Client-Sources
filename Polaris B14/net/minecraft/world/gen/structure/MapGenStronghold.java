/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
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
/*     */ public class MapGenStronghold
/*     */   extends MapGenStructure
/*     */ {
/*  28 */   private ChunkCoordIntPair[] structureCoords = new ChunkCoordIntPair[3];
/*  29 */   private double field_82671_h = 32.0D;
/*  30 */   private int field_82672_i = 3;
/*  31 */   private List<BiomeGenBase> field_151546_e = Lists.newArrayList();
/*     */   
/*  33 */   public MapGenStronghold() { BiomeGenBase[] arrayOfBiomeGenBase; int j = (arrayOfBiomeGenBase = BiomeGenBase.getBiomeGenArray()).length; for (int i = 0; i < j; i++) { BiomeGenBase biomegenbase = arrayOfBiomeGenBase[i];
/*     */       
/*  35 */       if ((biomegenbase != null) && (biomegenbase.minHeight > 0.0F))
/*     */       {
/*  37 */         this.field_151546_e.add(biomegenbase);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public MapGenStronghold(Map<String, String> p_i2068_1_)
/*     */   {
/*  44 */     this();
/*     */     
/*  46 */     for (Map.Entry<String, String> entry : p_i2068_1_.entrySet())
/*     */     {
/*  48 */       if (((String)entry.getKey()).equals("distance"))
/*     */       {
/*  50 */         this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax((String)entry.getValue(), this.field_82671_h, 1.0D);
/*     */       }
/*  52 */       else if (((String)entry.getKey()).equals("count"))
/*     */       {
/*  54 */         this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.structureCoords.length, 1)];
/*     */       }
/*  56 */       else if (((String)entry.getKey()).equals("spread"))
/*     */       {
/*  58 */         this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_82672_i, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getStructureName()
/*     */   {
/*  65 */     return "Stronghold";
/*     */   }
/*     */   
/*     */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
/*     */     int i;
/*  70 */     if (!this.ranBiomeCheck)
/*     */     {
/*  72 */       Random random = new Random();
/*  73 */       random.setSeed(this.worldObj.getSeed());
/*  74 */       d0 = random.nextDouble() * 3.141592653589793D * 2.0D;
/*  75 */       i = 1;
/*     */       
/*  77 */       for (int j = 0; j < this.structureCoords.length; j++)
/*     */       {
/*  79 */         double d1 = (1.25D * i + random.nextDouble()) * this.field_82671_h * i;
/*  80 */         int k = (int)Math.round(Math.cos(d0) * d1);
/*  81 */         int l = (int)Math.round(Math.sin(d0) * d1);
/*  82 */         BlockPos blockpos = this.worldObj.getWorldChunkManager().findBiomePosition((k << 4) + 8, (l << 4) + 8, 112, this.field_151546_e, random);
/*     */         
/*  84 */         if (blockpos != null)
/*     */         {
/*  86 */           k = blockpos.getX() >> 4;
/*  87 */           l = blockpos.getZ() >> 4;
/*     */         }
/*     */         
/*  90 */         this.structureCoords[j] = new ChunkCoordIntPair(k, l);
/*  91 */         d0 += 6.283185307179586D * i / this.field_82672_i;
/*     */         
/*  93 */         if (j == this.field_82672_i)
/*     */         {
/*  95 */           i += 2 + random.nextInt(5);
/*  96 */           this.field_82672_i += 1 + random.nextInt(2);
/*     */         }
/*     */       }
/*     */       
/* 100 */       this.ranBiomeCheck = true;
/*     */     }
/*     */     
/* 103 */     double d1 = (i = this.structureCoords).length; for (double d0 = 0; d0 < d1; d0++) { ChunkCoordIntPair chunkcoordintpair = i[d0];
/*     */       
/* 105 */       if ((chunkX == chunkcoordintpair.chunkXPos) && (chunkZ == chunkcoordintpair.chunkZPos))
/*     */       {
/* 107 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   protected List<BlockPos> getCoordList()
/*     */   {
/* 116 */     List<BlockPos> list = Lists.newArrayList();
/*     */     ChunkCoordIntPair[] arrayOfChunkCoordIntPair;
/* 118 */     int j = (arrayOfChunkCoordIntPair = this.structureCoords).length; for (int i = 0; i < j; i++) { ChunkCoordIntPair chunkcoordintpair = arrayOfChunkCoordIntPair[i];
/*     */       
/* 120 */       if (chunkcoordintpair != null)
/*     */       {
/* 122 */         list.add(chunkcoordintpair.getCenterBlock(64));
/*     */       }
/*     */     }
/*     */     
/* 126 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean ranBiomeCheck;
/*     */   protected StructureStart getStructureStart(int chunkX, int chunkZ)
/*     */   {
/* 133 */     for (Start mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ); (mapgenstronghold$start.getComponents().isEmpty()) || (((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get(0)).strongholdPortalRoom == null); mapgenstronghold$start = new Start(this.worldObj, this.rand, chunkX, chunkZ)) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 138 */     return mapgenstronghold$start;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Start
/*     */     extends StructureStart
/*     */   {
/*     */     public Start() {}
/*     */     
/*     */     public Start(World worldIn, Random p_i2067_2_, int p_i2067_3_, int p_i2067_4_)
/*     */     {
/* 149 */       super(p_i2067_4_);
/* 150 */       StructureStrongholdPieces.prepareStructurePieces();
/* 151 */       StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
/* 152 */       this.components.add(structurestrongholdpieces$stairs2);
/* 153 */       structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
/* 154 */       List<StructureComponent> list = structurestrongholdpieces$stairs2.field_75026_c;
/*     */       
/* 156 */       while (!list.isEmpty())
/*     */       {
/* 158 */         int i = p_i2067_2_.nextInt(list.size());
/* 159 */         StructureComponent structurecomponent = (StructureComponent)list.remove(i);
/* 160 */         structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, p_i2067_2_);
/*     */       }
/*     */       
/* 163 */       updateBoundingBox();
/* 164 */       markAvailableHeight(worldIn, p_i2067_2_, 10);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\MapGenStronghold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */