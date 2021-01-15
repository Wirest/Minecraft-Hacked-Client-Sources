/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.MapGenBase;
/*     */ import net.minecraft.world.storage.MapStorage;
/*     */ import optfine.Reflector;
/*     */ import optfine.ReflectorMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MapGenStructure
/*     */   extends MapGenBase
/*     */ {
/*     */   private MapGenStructureData structureData;
/*  33 */   protected Map structureMap = Maps.newHashMap();
/*     */   private static final String __OBFID = "CL_00000505";
/*  35 */   private LongHashMap structureLongMap = new LongHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract String getStructureName();
/*     */   
/*     */ 
/*     */   protected final void recursiveGenerate(World worldIn, final int chunkX, final int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn)
/*     */   {
/*  44 */     func_143027_a(worldIn);
/*     */     
/*  46 */     if (!this.structureLongMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ)))
/*     */     {
/*  48 */       this.rand.nextInt();
/*     */       
/*     */       try
/*     */       {
/*  52 */         if (canSpawnStructureAtCoords(chunkX, chunkZ))
/*     */         {
/*  54 */           StructureStart structurestart = getStructureStart(chunkX, chunkZ);
/*  55 */           this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ)), structurestart);
/*  56 */           this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), structurestart);
/*  57 */           func_143026_a(chunkX, chunkZ, structurestart);
/*     */         }
/*     */       }
/*     */       catch (Throwable throwable)
/*     */       {
/*  62 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
/*  63 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
/*  64 */         crashreportcategory.addCrashSectionCallable("Is feature chunk", new Callable()
/*     */         {
/*     */           private static final String __OBFID = "CL_00000506";
/*     */           
/*     */           public String call() throws Exception {
/*  69 */             return MapGenStructure.this.canSpawnStructureAtCoords(chunkX, chunkZ) ? "True" : "False";
/*     */           }
/*  71 */         });
/*  72 */         crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d", new Object[] { Integer.valueOf(chunkX), Integer.valueOf(chunkZ) }));
/*  73 */         crashreportcategory.addCrashSectionCallable("Chunk pos hash", new Callable()
/*     */         {
/*     */           private static final String __OBFID = "CL_00000507";
/*     */           
/*     */           public String call() throws Exception {
/*  78 */             return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
/*     */           }
/*  80 */         });
/*  81 */         crashreportcategory.addCrashSectionCallable("Structure type", new Callable()
/*     */         {
/*     */           private static final String __OBFID = "CL_00000508";
/*     */           
/*     */           public String call() throws Exception {
/*  86 */             return MapGenStructure.this.getClass().getCanonicalName();
/*     */           }
/*  88 */         });
/*  89 */         throw new ReportedException(crashreport);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean generateStructure(World worldIn, Random randomIn, ChunkCoordIntPair chunkCoord)
/*     */   {
/*  96 */     func_143027_a(worldIn);
/*  97 */     int i = (chunkCoord.chunkXPos << 4) + 8;
/*  98 */     int j = (chunkCoord.chunkZPos << 4) + 8;
/*  99 */     boolean flag = false;
/*     */     
/* 101 */     for (Object structurestart0 : this.structureMap.values())
/*     */     {
/* 103 */       StructureStart structurestart = (StructureStart)structurestart0;
/*     */       
/* 105 */       if ((structurestart.isSizeableStructure()) && (structurestart.func_175788_a(chunkCoord)) && (structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)))
/*     */       {
/* 107 */         structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
/* 108 */         structurestart.func_175787_b(chunkCoord);
/* 109 */         flag = true;
/* 110 */         func_143026_a(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
/*     */       }
/*     */     }
/*     */     
/* 114 */     return flag;
/*     */   }
/*     */   
/*     */   public boolean func_175795_b(BlockPos pos)
/*     */   {
/* 119 */     func_143027_a(this.worldObj);
/* 120 */     return func_175797_c(pos) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected StructureStart func_175797_c(BlockPos pos)
/*     */   {
/* 127 */     for (Object structurestart0 : this.structureMap.values())
/*     */     {
/* 129 */       StructureStart structurestart = (StructureStart)structurestart0;
/*     */       
/* 131 */       if ((structurestart.isSizeableStructure()) && (structurestart.getBoundingBox().isVecInside(pos)))
/*     */       {
/* 133 */         Iterator iterator = structurestart.getComponents().iterator();
/*     */         
/*     */ 
/*     */ 
/* 137 */         while (iterator.hasNext())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 142 */           StructureComponent structurecomponent = (StructureComponent)iterator.next();
/*     */           
/* 144 */           if (structurecomponent.getBoundingBox().isVecInside(pos))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */             return structurestart; }
/*     */         }
/*     */       }
/*     */     }
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   public boolean func_175796_a(World worldIn, BlockPos pos)
/*     */   {
/* 159 */     func_143027_a(worldIn);
/*     */     
/* 161 */     for (Object structurestart0 : this.structureMap.values())
/*     */     {
/* 163 */       StructureStart structurestart = (StructureStart)structurestart0;
/*     */       
/* 165 */       if ((structurestart.isSizeableStructure()) && (structurestart.getBoundingBox().isVecInside(pos)))
/*     */       {
/* 167 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos)
/*     */   {
/* 176 */     this.worldObj = worldIn;
/* 177 */     func_143027_a(worldIn);
/* 178 */     this.rand.setSeed(worldIn.getSeed());
/* 179 */     long i = this.rand.nextLong();
/* 180 */     long j = this.rand.nextLong();
/* 181 */     long k = (pos.getX() >> 4) * i;
/* 182 */     long l = (pos.getZ() >> 4) * j;
/* 183 */     this.rand.setSeed(k ^ l ^ worldIn.getSeed());
/* 184 */     recursiveGenerate(worldIn, pos.getX() >> 4, pos.getZ() >> 4, 0, 0, null);
/* 185 */     double d0 = Double.MAX_VALUE;
/* 186 */     BlockPos blockpos = null;
/*     */     StructureComponent structurecomponent;
/* 188 */     for (Object structurestart0 : this.structureMap.values())
/*     */     {
/* 190 */       StructureStart structurestart = (StructureStart)structurestart0;
/*     */       
/* 192 */       if (structurestart.isSizeableStructure())
/*     */       {
/* 194 */         structurecomponent = (StructureComponent)structurestart.getComponents().get(0);
/* 195 */         BlockPos blockpos1 = structurecomponent.getBoundingBoxCenter();
/* 196 */         double d1 = blockpos1.distanceSq(pos);
/*     */         
/* 198 */         if (d1 < d0)
/*     */         {
/* 200 */           d0 = d1;
/* 201 */           blockpos = blockpos1;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 206 */     if (blockpos != null)
/*     */     {
/* 208 */       return blockpos;
/*     */     }
/*     */     
/*     */ 
/* 212 */     List list = getCoordList();
/*     */     
/* 214 */     if (list != null)
/*     */     {
/* 216 */       BlockPos blockpos3 = null;
/*     */       
/* 218 */       for (Object blockpos2 : list)
/*     */       {
/* 220 */         double d2 = ((BlockPos)blockpos2).distanceSq(pos);
/*     */         
/* 222 */         if (d2 < d0)
/*     */         {
/* 224 */           d0 = d2;
/* 225 */           blockpos3 = (BlockPos)blockpos2;
/*     */         }
/*     */       }
/*     */       
/* 229 */       return blockpos3;
/*     */     }
/*     */     
/*     */ 
/* 233 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List getCoordList()
/*     */   {
/* 244 */     return null;
/*     */   }
/*     */   
/*     */   private void func_143027_a(World worldIn)
/*     */   {
/* 249 */     if (this.structureData == null)
/*     */     {
/* 251 */       if (Reflector.ForgeWorld_getPerWorldStorage.exists())
/*     */       {
/* 253 */         MapStorage mapstorage = (MapStorage)Reflector.call(worldIn, Reflector.ForgeWorld_getPerWorldStorage, new Object[0]);
/* 254 */         this.structureData = ((MapGenStructureData)mapstorage.loadData(MapGenStructureData.class, getStructureName()));
/*     */       }
/*     */       else
/*     */       {
/* 258 */         this.structureData = ((MapGenStructureData)worldIn.loadItemData(MapGenStructureData.class, getStructureName()));
/*     */       }
/*     */       
/* 261 */       if (this.structureData == null)
/*     */       {
/* 263 */         this.structureData = new MapGenStructureData(getStructureName());
/*     */         
/* 265 */         if (Reflector.ForgeWorld_getPerWorldStorage.exists())
/*     */         {
/* 267 */           MapStorage mapstorage1 = (MapStorage)Reflector.call(worldIn, Reflector.ForgeWorld_getPerWorldStorage, new Object[0]);
/* 268 */           mapstorage1.setData(getStructureName(), this.structureData);
/*     */         }
/*     */         else
/*     */         {
/* 272 */           worldIn.setItemData(getStructureName(), this.structureData);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 277 */         NBTTagCompound nbttagcompound1 = this.structureData.getTagCompound();
/*     */         
/* 279 */         for (String s : nbttagcompound1.getKeySet())
/*     */         {
/* 281 */           NBTBase nbtbase = nbttagcompound1.getTag(s);
/*     */           
/* 283 */           if (nbtbase.getId() == 10)
/*     */           {
/* 285 */             NBTTagCompound nbttagcompound = (NBTTagCompound)nbtbase;
/*     */             
/* 287 */             if ((nbttagcompound.hasKey("ChunkX")) && (nbttagcompound.hasKey("ChunkZ")))
/*     */             {
/* 289 */               int i = nbttagcompound.getInteger("ChunkX");
/* 290 */               int j = nbttagcompound.getInteger("ChunkZ");
/* 291 */               StructureStart structurestart = MapGenStructureIO.getStructureStart(nbttagcompound, worldIn);
/*     */               
/* 293 */               if (structurestart != null)
/*     */               {
/* 295 */                 this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)), structurestart);
/* 296 */                 this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_143026_a(int p_143026_1_, int p_143026_2_, StructureStart start)
/*     */   {
/* 307 */     this.structureData.writeInstance(start.writeStructureComponentsToNBT(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
/* 308 */     this.structureData.markDirty();
/*     */   }
/*     */   
/*     */   protected abstract boolean canSpawnStructureAtCoords(int paramInt1, int paramInt2);
/*     */   
/*     */   protected abstract StructureStart getStructureStart(int paramInt1, int paramInt2);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\MapGenStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */