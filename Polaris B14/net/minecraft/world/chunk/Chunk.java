/*      */ package net.minecraft.world.chunk;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Maps;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.ITileEntityProvider;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*      */ import net.minecraft.util.ClassInheritanceMultiMap;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumFacing.Plane;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.world.ChunkCoordIntPair;
/*      */ import net.minecraft.world.EnumSkyBlock;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class Chunk
/*      */ {
/*   40 */   private static final Logger logger = ;
/*      */   
/*      */ 
/*      */   private final ExtendedBlockStorage[] storageArrays;
/*      */   
/*      */ 
/*      */   private final byte[] blockBiomeArray;
/*      */   
/*      */ 
/*      */   private final int[] precipitationHeightMap;
/*      */   
/*      */ 
/*      */   private final boolean[] updateSkylightColumns;
/*      */   
/*      */ 
/*      */   private boolean isChunkLoaded;
/*      */   
/*      */ 
/*      */   private final World worldObj;
/*      */   
/*      */ 
/*      */   private final int[] heightMap;
/*      */   
/*      */ 
/*      */   public final int xPosition;
/*      */   
/*      */ 
/*      */   public final int zPosition;
/*      */   
/*      */ 
/*      */   private boolean isGapLightingUpdated;
/*      */   
/*      */ 
/*      */   private final Map<BlockPos, TileEntity> chunkTileEntityMap;
/*      */   
/*      */ 
/*      */   private final ClassInheritanceMultiMap<Entity>[] entityLists;
/*      */   
/*      */ 
/*      */   private boolean isTerrainPopulated;
/*      */   
/*      */ 
/*      */   private boolean isLightPopulated;
/*      */   
/*      */ 
/*      */   private boolean field_150815_m;
/*      */   
/*      */ 
/*      */   private boolean isModified;
/*      */   
/*      */ 
/*      */   private boolean hasEntities;
/*      */   
/*      */ 
/*      */   private long lastSaveTime;
/*      */   
/*      */ 
/*      */   private int heightMapMinimum;
/*      */   
/*      */ 
/*      */   private long inhabitedTime;
/*      */   
/*      */   private int queuedLightChecks;
/*      */   
/*      */   private ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue;
/*      */   
/*      */ 
/*      */   public Chunk(World worldIn, int x, int z)
/*      */   {
/*  109 */     this.storageArrays = new ExtendedBlockStorage[16];
/*  110 */     this.blockBiomeArray = new byte['Ā'];
/*  111 */     this.precipitationHeightMap = new int['Ā'];
/*  112 */     this.updateSkylightColumns = new boolean['Ā'];
/*  113 */     this.chunkTileEntityMap = Maps.newHashMap();
/*  114 */     this.queuedLightChecks = 4096;
/*  115 */     this.tileEntityPosQueue = com.google.common.collect.Queues.newConcurrentLinkedQueue();
/*  116 */     this.entityLists = new ClassInheritanceMultiMap[16];
/*  117 */     this.worldObj = worldIn;
/*  118 */     this.xPosition = x;
/*  119 */     this.zPosition = z;
/*  120 */     this.heightMap = new int['Ā'];
/*      */     
/*  122 */     for (int i = 0; i < this.entityLists.length; i++)
/*      */     {
/*  124 */       this.entityLists[i] = new ClassInheritanceMultiMap(Entity.class);
/*      */     }
/*      */     
/*  127 */     Arrays.fill(this.precipitationHeightMap, 64537);
/*  128 */     Arrays.fill(this.blockBiomeArray, (byte)-1);
/*      */   }
/*      */   
/*      */   public Chunk(World worldIn, ChunkPrimer primer, int x, int z)
/*      */   {
/*  133 */     this(worldIn, x, z);
/*  134 */     int i = 256;
/*  135 */     boolean flag = !worldIn.provider.getHasNoSky();
/*      */     
/*  137 */     for (int j = 0; j < 16; j++)
/*      */     {
/*  139 */       for (int k = 0; k < 16; k++)
/*      */       {
/*  141 */         for (int l = 0; l < i; l++)
/*      */         {
/*  143 */           int i1 = j * i * 16 | k * i | l;
/*  144 */           IBlockState iblockstate = primer.getBlockState(i1);
/*      */           
/*  146 */           if (iblockstate.getBlock().getMaterial() != Material.air)
/*      */           {
/*  148 */             int j1 = l >> 4;
/*      */             
/*  150 */             if (this.storageArrays[j1] == null)
/*      */             {
/*  152 */               this.storageArrays[j1] = new ExtendedBlockStorage(j1 << 4, flag);
/*      */             }
/*      */             
/*  155 */             this.storageArrays[j1].set(j, l & 0xF, k, iblockstate);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAtLocation(int x, int z)
/*      */   {
/*  167 */     return (x == this.xPosition) && (z == this.zPosition);
/*      */   }
/*      */   
/*      */   public int getHeight(BlockPos pos)
/*      */   {
/*  172 */     return getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getHeightValue(int x, int z)
/*      */   {
/*  180 */     return this.heightMap[(z << 4 | x)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTopFilledSegment()
/*      */   {
/*  188 */     for (int i = this.storageArrays.length - 1; i >= 0; i--)
/*      */     {
/*  190 */       if (this.storageArrays[i] != null)
/*      */       {
/*  192 */         return this.storageArrays[i].getYLocation();
/*      */       }
/*      */     }
/*      */     
/*  196 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ExtendedBlockStorage[] getBlockStorageArray()
/*      */   {
/*  204 */     return this.storageArrays;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void generateHeightMap()
/*      */   {
/*  212 */     int i = getTopFilledSegment();
/*  213 */     this.heightMapMinimum = Integer.MAX_VALUE;
/*      */     
/*  215 */     for (int j = 0; j < 16; j++)
/*      */     {
/*  217 */       for (int k = 0; k < 16; k++)
/*      */       {
/*  219 */         this.precipitationHeightMap[(j + (k << 4))] = 64537;
/*      */         
/*  221 */         for (int l = i + 16; l > 0; l--)
/*      */         {
/*  223 */           Block block = getBlock0(j, l - 1, k);
/*      */           
/*  225 */           if (block.getLightOpacity() != 0)
/*      */           {
/*  227 */             this.heightMap[(k << 4 | j)] = l;
/*      */             
/*  229 */             if (l >= this.heightMapMinimum)
/*      */               break;
/*  231 */             this.heightMapMinimum = l;
/*      */             
/*      */ 
/*  234 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  240 */     this.isModified = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void generateSkylightMap()
/*      */   {
/*  248 */     int i = getTopFilledSegment();
/*  249 */     this.heightMapMinimum = Integer.MAX_VALUE;
/*      */     
/*  251 */     for (int j = 0; j < 16; j++)
/*      */     {
/*  253 */       for (int k = 0; k < 16; k++)
/*      */       {
/*  255 */         this.precipitationHeightMap[(j + (k << 4))] = 64537;
/*      */         
/*  257 */         for (int l = i + 16; l > 0; l--)
/*      */         {
/*  259 */           if (getBlockLightOpacity(j, l - 1, k) != 0)
/*      */           {
/*  261 */             this.heightMap[(k << 4 | j)] = l;
/*      */             
/*  263 */             if (l >= this.heightMapMinimum)
/*      */               break;
/*  265 */             this.heightMapMinimum = l;
/*      */             
/*      */ 
/*  268 */             break;
/*      */           }
/*      */         }
/*      */         
/*  272 */         if (!this.worldObj.provider.getHasNoSky())
/*      */         {
/*  274 */           int k1 = 15;
/*  275 */           int i1 = i + 16 - 1;
/*      */           
/*      */           do
/*      */           {
/*  279 */             int j1 = getBlockLightOpacity(j, i1, k);
/*      */             
/*  281 */             if ((j1 == 0) && (k1 != 15))
/*      */             {
/*  283 */               j1 = 1;
/*      */             }
/*      */             
/*  286 */             k1 -= j1;
/*      */             
/*  288 */             if (k1 > 0)
/*      */             {
/*  290 */               ExtendedBlockStorage extendedblockstorage = this.storageArrays[(i1 >> 4)];
/*      */               
/*  292 */               if (extendedblockstorage != null)
/*      */               {
/*  294 */                 extendedblockstorage.setExtSkylightValue(j, i1 & 0xF, k, k1);
/*  295 */                 this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + j, i1, (this.zPosition << 4) + k));
/*      */               }
/*      */             }
/*      */             
/*  299 */             i1--;
/*      */           }
/*  301 */           while ((i1 > 0) && (k1 > 0));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  310 */     this.isModified = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void propagateSkylightOcclusion(int x, int z)
/*      */   {
/*  318 */     this.updateSkylightColumns[(x + z * 16)] = true;
/*  319 */     this.isGapLightingUpdated = true;
/*      */   }
/*      */   
/*      */   private void recheckGaps(boolean p_150803_1_)
/*      */   {
/*  324 */     this.worldObj.theProfiler.startSection("recheckGaps");
/*      */     
/*  326 */     if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16))
/*      */     {
/*  328 */       for (int i = 0; i < 16; i++)
/*      */       {
/*  330 */         for (int j = 0; j < 16; j++)
/*      */         {
/*  332 */           if (this.updateSkylightColumns[(i + j * 16)] != 0)
/*      */           {
/*  334 */             this.updateSkylightColumns[(i + j * 16)] = false;
/*  335 */             int k = getHeightValue(i, j);
/*  336 */             int l = this.xPosition * 16 + i;
/*  337 */             int i1 = this.zPosition * 16 + j;
/*  338 */             int j1 = Integer.MAX_VALUE;
/*      */             
/*  340 */             for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*      */             {
/*  342 */               EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*  343 */               j1 = Math.min(j1, this.worldObj.getChunksLowestHorizon(l + enumfacing.getFrontOffsetX(), i1 + enumfacing.getFrontOffsetZ()));
/*      */             }
/*      */             
/*  346 */             checkSkylightNeighborHeight(l, i1, j1);
/*      */             
/*  348 */             for (Object enumfacing10 : EnumFacing.Plane.HORIZONTAL)
/*      */             {
/*  350 */               EnumFacing enumfacing1 = (EnumFacing)enumfacing10;
/*  351 */               checkSkylightNeighborHeight(l + enumfacing1.getFrontOffsetX(), i1 + enumfacing1.getFrontOffsetZ(), k);
/*      */             }
/*      */             
/*  354 */             if (p_150803_1_)
/*      */             {
/*  356 */               this.worldObj.theProfiler.endSection();
/*  357 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  363 */       this.isGapLightingUpdated = false;
/*      */     }
/*      */     
/*  366 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkSkylightNeighborHeight(int x, int z, int maxValue)
/*      */   {
/*  374 */     int i = this.worldObj.getHeight(new BlockPos(x, 0, z)).getY();
/*      */     
/*  376 */     if (i > maxValue)
/*      */     {
/*  378 */       updateSkylightNeighborHeight(x, z, maxValue, i + 1);
/*      */     }
/*  380 */     else if (i < maxValue)
/*      */     {
/*  382 */       updateSkylightNeighborHeight(x, z, i, maxValue + 1);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateSkylightNeighborHeight(int x, int z, int startY, int endY)
/*      */   {
/*  388 */     if ((endY > startY) && (this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)))
/*      */     {
/*  390 */       for (int i = startY; i < endY; i++)
/*      */       {
/*  392 */         this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i, z));
/*      */       }
/*      */       
/*  395 */       this.isModified = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void relightBlock(int x, int y, int z)
/*      */   {
/*  404 */     int i = this.heightMap[(z << 4 | x)] & 0xFF;
/*  405 */     int j = i;
/*      */     
/*  407 */     if (y > i)
/*      */     {
/*  409 */       j = y;
/*      */     }
/*      */     
/*  412 */     while ((j > 0) && (getBlockLightOpacity(x, j - 1, z) == 0))
/*      */     {
/*  414 */       j--;
/*      */     }
/*      */     
/*  417 */     if (j != i)
/*      */     {
/*  419 */       this.worldObj.markBlocksDirtyVertical(x + this.xPosition * 16, z + this.zPosition * 16, j, i);
/*  420 */       this.heightMap[(z << 4 | x)] = j;
/*  421 */       int k = this.xPosition * 16 + x;
/*  422 */       int l = this.zPosition * 16 + z;
/*      */       
/*  424 */       if (!this.worldObj.provider.getHasNoSky())
/*      */       {
/*  426 */         if (j < i)
/*      */         {
/*  428 */           for (int j1 = j; j1 < i; j1++)
/*      */           {
/*  430 */             ExtendedBlockStorage extendedblockstorage2 = this.storageArrays[(j1 >> 4)];
/*      */             
/*  432 */             if (extendedblockstorage2 != null)
/*      */             {
/*  434 */               extendedblockstorage2.setExtSkylightValue(x, j1 & 0xF, z, 15);
/*  435 */               this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, j1, (this.zPosition << 4) + z));
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         } else {
/*  441 */           for (int i1 = i; i1 < j; i1++)
/*      */           {
/*  443 */             ExtendedBlockStorage extendedblockstorage = this.storageArrays[(i1 >> 4)];
/*      */             
/*  445 */             if (extendedblockstorage != null)
/*      */             {
/*  447 */               extendedblockstorage.setExtSkylightValue(x, i1 & 0xF, z, 0);
/*  448 */               this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, i1, (this.zPosition << 4) + z));
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  453 */         int k1 = 15;
/*      */         
/*  455 */         while ((j > 0) && (k1 > 0))
/*      */         {
/*  457 */           j--;
/*  458 */           int i2 = getBlockLightOpacity(x, j, z);
/*      */           
/*  460 */           if (i2 == 0)
/*      */           {
/*  462 */             i2 = 1;
/*      */           }
/*      */           
/*  465 */           k1 -= i2;
/*      */           
/*  467 */           if (k1 < 0)
/*      */           {
/*  469 */             k1 = 0;
/*      */           }
/*      */           
/*  472 */           ExtendedBlockStorage extendedblockstorage1 = this.storageArrays[(j >> 4)];
/*      */           
/*  474 */           if (extendedblockstorage1 != null)
/*      */           {
/*  476 */             extendedblockstorage1.setExtSkylightValue(x, j & 0xF, z, k1);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  481 */       int l1 = this.heightMap[(z << 4 | x)];
/*  482 */       int j2 = i;
/*  483 */       int k2 = l1;
/*      */       
/*  485 */       if (l1 < i)
/*      */       {
/*  487 */         j2 = l1;
/*  488 */         k2 = i;
/*      */       }
/*      */       
/*  491 */       if (l1 < this.heightMapMinimum)
/*      */       {
/*  493 */         this.heightMapMinimum = l1;
/*      */       }
/*      */       
/*  496 */       if (!this.worldObj.provider.getHasNoSky())
/*      */       {
/*  498 */         for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*      */         {
/*  500 */           EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*  501 */           updateSkylightNeighborHeight(k + enumfacing.getFrontOffsetX(), l + enumfacing.getFrontOffsetZ(), j2, k2);
/*      */         }
/*      */         
/*  504 */         updateSkylightNeighborHeight(k, l, j2, k2);
/*      */       }
/*      */       
/*  507 */       this.isModified = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public int getBlockLightOpacity(BlockPos pos)
/*      */   {
/*  513 */     return getBlock(pos).getLightOpacity();
/*      */   }
/*      */   
/*      */   private int getBlockLightOpacity(int x, int y, int z)
/*      */   {
/*  518 */     return getBlock0(x, y, z).getLightOpacity();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Block getBlock0(int x, int y, int z)
/*      */   {
/*  526 */     Block block = Blocks.air;
/*      */     
/*  528 */     if ((y >= 0) && (y >> 4 < this.storageArrays.length))
/*      */     {
/*  530 */       ExtendedBlockStorage extendedblockstorage = this.storageArrays[(y >> 4)];
/*      */       
/*  532 */       if (extendedblockstorage != null)
/*      */       {
/*      */         try
/*      */         {
/*  536 */           block = extendedblockstorage.getBlockByExtId(x, y & 0xF, z);
/*      */         }
/*      */         catch (Throwable throwable)
/*      */         {
/*  540 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block");
/*  541 */           throw new ReportedException(crashreport);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  546 */     return block;
/*      */   }
/*      */   
/*      */   public Block getBlock(final int x, final int y, final int z)
/*      */   {
/*      */     try
/*      */     {
/*  553 */       return getBlock0(x & 0xF, y, z & 0xF);
/*      */     }
/*      */     catch (ReportedException reportedexception)
/*      */     {
/*  557 */       CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
/*  558 */       crashreportcategory.addCrashSectionCallable("Location", new Callable()
/*      */       {
/*      */         public String call() throws Exception
/*      */         {
/*  562 */           return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + x, y, Chunk.this.zPosition * 16 + z));
/*      */         }
/*  564 */       });
/*  565 */       throw reportedexception;
/*      */     }
/*      */   }
/*      */   
/*      */   public Block getBlock(final BlockPos pos)
/*      */   {
/*      */     try
/*      */     {
/*  573 */       return getBlock0(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
/*      */     }
/*      */     catch (ReportedException reportedexception)
/*      */     {
/*  577 */       CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
/*  578 */       crashreportcategory.addCrashSectionCallable("Location", new Callable()
/*      */       {
/*      */         public String call() throws Exception
/*      */         {
/*  582 */           return CrashReportCategory.getCoordinateInfo(pos);
/*      */         }
/*  584 */       });
/*  585 */       throw reportedexception;
/*      */     }
/*      */   }
/*      */   
/*      */   public IBlockState getBlockState(final BlockPos pos)
/*      */   {
/*  591 */     if (this.worldObj.getWorldType() == net.minecraft.world.WorldType.DEBUG_WORLD)
/*      */     {
/*  593 */       IBlockState iblockstate = null;
/*      */       
/*  595 */       if (pos.getY() == 60)
/*      */       {
/*  597 */         iblockstate = Blocks.barrier.getDefaultState();
/*      */       }
/*      */       
/*  600 */       if (pos.getY() == 70)
/*      */       {
/*  602 */         iblockstate = net.minecraft.world.gen.ChunkProviderDebug.func_177461_b(pos.getX(), pos.getZ());
/*      */       }
/*      */       
/*  605 */       return iblockstate == null ? Blocks.air.getDefaultState() : iblockstate;
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  611 */       if ((pos.getY() >= 0) && (pos.getY() >> 4 < this.storageArrays.length))
/*      */       {
/*  613 */         ExtendedBlockStorage extendedblockstorage = this.storageArrays[(pos.getY() >> 4)];
/*      */         
/*  615 */         if (extendedblockstorage != null)
/*      */         {
/*  617 */           int j = pos.getX() & 0xF;
/*  618 */           int k = pos.getY() & 0xF;
/*  619 */           int i = pos.getZ() & 0xF;
/*  620 */           return extendedblockstorage.get(j, k, i);
/*      */         }
/*      */       }
/*      */       
/*  624 */       return Blocks.air.getDefaultState();
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/*  628 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
/*  629 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
/*  630 */       crashreportcategory.addCrashSectionCallable("Location", new Callable()
/*      */       {
/*      */         public String call() throws Exception
/*      */         {
/*  634 */           return CrashReportCategory.getCoordinateInfo(pos);
/*      */         }
/*  636 */       });
/*  637 */       throw new ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getBlockMetadata(int x, int y, int z)
/*      */   {
/*  647 */     if (y >> 4 >= this.storageArrays.length)
/*      */     {
/*  649 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  653 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[(y >> 4)];
/*  654 */     return extendedblockstorage != null ? extendedblockstorage.getExtBlockMetadata(x, y & 0xF, z) : 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getBlockMetadata(BlockPos pos)
/*      */   {
/*  660 */     return getBlockMetadata(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
/*      */   }
/*      */   
/*      */   public IBlockState setBlockState(BlockPos pos, IBlockState state)
/*      */   {
/*  665 */     int i = pos.getX() & 0xF;
/*  666 */     int j = pos.getY();
/*  667 */     int k = pos.getZ() & 0xF;
/*  668 */     int l = k << 4 | i;
/*      */     
/*  670 */     if (j >= this.precipitationHeightMap[l] - 1)
/*      */     {
/*  672 */       this.precipitationHeightMap[l] = 64537;
/*      */     }
/*      */     
/*  675 */     int i1 = this.heightMap[l];
/*  676 */     IBlockState iblockstate = getBlockState(pos);
/*      */     
/*  678 */     if (iblockstate == state)
/*      */     {
/*  680 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  684 */     Block block = state.getBlock();
/*  685 */     Block block1 = iblockstate.getBlock();
/*  686 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[(j >> 4)];
/*  687 */     boolean flag = false;
/*      */     
/*  689 */     if (extendedblockstorage == null)
/*      */     {
/*  691 */       if (block == Blocks.air)
/*      */       {
/*  693 */         return null;
/*      */       }
/*      */       
/*  696 */       extendedblockstorage = this.storageArrays[(j >> 4)] = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
/*  697 */       flag = j >= i1;
/*      */     }
/*      */     
/*  700 */     extendedblockstorage.set(i, j & 0xF, k, state);
/*      */     
/*  702 */     if (block1 != block)
/*      */     {
/*  704 */       if (!this.worldObj.isRemote)
/*      */       {
/*  706 */         block1.breakBlock(this.worldObj, pos, iblockstate);
/*      */       }
/*  708 */       else if ((block1 instanceof ITileEntityProvider))
/*      */       {
/*  710 */         this.worldObj.removeTileEntity(pos);
/*      */       }
/*      */     }
/*      */     
/*  714 */     if (extendedblockstorage.getBlockByExtId(i, j & 0xF, k) != block)
/*      */     {
/*  716 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  720 */     if (flag)
/*      */     {
/*  722 */       generateSkylightMap();
/*      */     }
/*      */     else
/*      */     {
/*  726 */       int j1 = block.getLightOpacity();
/*  727 */       int k1 = block1.getLightOpacity();
/*      */       
/*  729 */       if (j1 > 0)
/*      */       {
/*  731 */         if (j >= i1)
/*      */         {
/*  733 */           relightBlock(i, j + 1, k);
/*      */         }
/*      */       }
/*  736 */       else if (j == i1 - 1)
/*      */       {
/*  738 */         relightBlock(i, j, k);
/*      */       }
/*      */       
/*  741 */       if ((j1 != k1) && ((j1 < k1) || (getLightFor(EnumSkyBlock.SKY, pos) > 0) || (getLightFor(EnumSkyBlock.BLOCK, pos) > 0)))
/*      */       {
/*  743 */         propagateSkylightOcclusion(i, k);
/*      */       }
/*      */     }
/*      */     
/*  747 */     if ((block1 instanceof ITileEntityProvider))
/*      */     {
/*  749 */       TileEntity tileentity = getTileEntity(pos, EnumCreateEntityType.CHECK);
/*      */       
/*  751 */       if (tileentity != null)
/*      */       {
/*  753 */         tileentity.updateContainingBlockInfo();
/*      */       }
/*      */     }
/*      */     
/*  757 */     if ((!this.worldObj.isRemote) && (block1 != block))
/*      */     {
/*  759 */       block.onBlockAdded(this.worldObj, pos, state);
/*      */     }
/*      */     
/*  762 */     if ((block instanceof ITileEntityProvider))
/*      */     {
/*  764 */       TileEntity tileentity1 = getTileEntity(pos, EnumCreateEntityType.CHECK);
/*      */       
/*  766 */       if (tileentity1 == null)
/*      */       {
/*  768 */         tileentity1 = ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, block.getMetaFromState(state));
/*  769 */         this.worldObj.setTileEntity(pos, tileentity1);
/*      */       }
/*      */       
/*  772 */       if (tileentity1 != null)
/*      */       {
/*  774 */         tileentity1.updateContainingBlockInfo();
/*      */       }
/*      */     }
/*      */     
/*  778 */     this.isModified = true;
/*  779 */     return iblockstate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos)
/*      */   {
/*  786 */     int i = pos.getX() & 0xF;
/*  787 */     int j = pos.getY();
/*  788 */     int k = pos.getZ() & 0xF;
/*  789 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[(j >> 4)];
/*  790 */     return p_177413_1_ == EnumSkyBlock.BLOCK ? extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k) : p_177413_1_ == EnumSkyBlock.SKY ? extendedblockstorage.getExtSkylightValue(i, j & 0xF, k) : this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage == null ? 0 : canSeeSky(pos) ? p_177413_1_.defaultLightValue : p_177413_1_.defaultLightValue;
/*      */   }
/*      */   
/*      */   public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value)
/*      */   {
/*  795 */     int i = pos.getX() & 0xF;
/*  796 */     int j = pos.getY();
/*  797 */     int k = pos.getZ() & 0xF;
/*  798 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[(j >> 4)];
/*      */     
/*  800 */     if (extendedblockstorage == null)
/*      */     {
/*  802 */       extendedblockstorage = this.storageArrays[(j >> 4)] = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
/*  803 */       generateSkylightMap();
/*      */     }
/*      */     
/*  806 */     this.isModified = true;
/*      */     
/*  808 */     if (p_177431_1_ == EnumSkyBlock.SKY)
/*      */     {
/*  810 */       if (!this.worldObj.provider.getHasNoSky())
/*      */       {
/*  812 */         extendedblockstorage.setExtSkylightValue(i, j & 0xF, k, value);
/*      */       }
/*      */     }
/*  815 */     else if (p_177431_1_ == EnumSkyBlock.BLOCK)
/*      */     {
/*  817 */       extendedblockstorage.setExtBlocklightValue(i, j & 0xF, k, value);
/*      */     }
/*      */   }
/*      */   
/*      */   public int getLightSubtracted(BlockPos pos, int amount)
/*      */   {
/*  823 */     int i = pos.getX() & 0xF;
/*  824 */     int j = pos.getY();
/*  825 */     int k = pos.getZ() & 0xF;
/*  826 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[(j >> 4)];
/*      */     
/*  828 */     if (extendedblockstorage == null)
/*      */     {
/*  830 */       return (!this.worldObj.provider.getHasNoSky()) && (amount < EnumSkyBlock.SKY.defaultLightValue) ? EnumSkyBlock.SKY.defaultLightValue - amount : 0;
/*      */     }
/*      */     
/*      */ 
/*  834 */     int l = this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k);
/*  835 */     l -= amount;
/*  836 */     int i1 = extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k);
/*      */     
/*  838 */     if (i1 > l)
/*      */     {
/*  840 */       l = i1;
/*      */     }
/*      */     
/*  843 */     return l;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addEntity(Entity entityIn)
/*      */   {
/*  852 */     this.hasEntities = true;
/*  853 */     int i = MathHelper.floor_double(entityIn.posX / 16.0D);
/*  854 */     int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
/*      */     
/*  856 */     if ((i != this.xPosition) || (j != this.zPosition))
/*      */     {
/*  858 */       logger.warn("Wrong location! (" + i + ", " + j + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, new Object[] { entityIn });
/*  859 */       entityIn.setDead();
/*      */     }
/*      */     
/*  862 */     int k = MathHelper.floor_double(entityIn.posY / 16.0D);
/*      */     
/*  864 */     if (k < 0)
/*      */     {
/*  866 */       k = 0;
/*      */     }
/*      */     
/*  869 */     if (k >= this.entityLists.length)
/*      */     {
/*  871 */       k = this.entityLists.length - 1;
/*      */     }
/*      */     
/*  874 */     entityIn.addedToChunk = true;
/*  875 */     entityIn.chunkCoordX = this.xPosition;
/*  876 */     entityIn.chunkCoordY = k;
/*  877 */     entityIn.chunkCoordZ = this.zPosition;
/*  878 */     this.entityLists[k].add(entityIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeEntity(Entity entityIn)
/*      */   {
/*  886 */     removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeEntityAtIndex(Entity entityIn, int p_76608_2_)
/*      */   {
/*  894 */     if (p_76608_2_ < 0)
/*      */     {
/*  896 */       p_76608_2_ = 0;
/*      */     }
/*      */     
/*  899 */     if (p_76608_2_ >= this.entityLists.length)
/*      */     {
/*  901 */       p_76608_2_ = this.entityLists.length - 1;
/*      */     }
/*      */     
/*  904 */     this.entityLists[p_76608_2_].remove(entityIn);
/*      */   }
/*      */   
/*      */   public boolean canSeeSky(BlockPos pos)
/*      */   {
/*  909 */     int i = pos.getX() & 0xF;
/*  910 */     int j = pos.getY();
/*  911 */     int k = pos.getZ() & 0xF;
/*  912 */     return j >= this.heightMap[(k << 4 | i)];
/*      */   }
/*      */   
/*      */   private TileEntity createNewTileEntity(BlockPos pos)
/*      */   {
/*  917 */     Block block = getBlock(pos);
/*  918 */     return !block.hasTileEntity() ? null : ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, getBlockMetadata(pos));
/*      */   }
/*      */   
/*      */   public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_177424_2_)
/*      */   {
/*  923 */     TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.get(pos);
/*      */     
/*  925 */     if (tileentity == null)
/*      */     {
/*  927 */       if (p_177424_2_ == EnumCreateEntityType.IMMEDIATE)
/*      */       {
/*  929 */         tileentity = createNewTileEntity(pos);
/*  930 */         this.worldObj.setTileEntity(pos, tileentity);
/*      */       }
/*  932 */       else if (p_177424_2_ == EnumCreateEntityType.QUEUED)
/*      */       {
/*  934 */         this.tileEntityPosQueue.add(pos);
/*      */       }
/*      */     }
/*  937 */     else if (tileentity.isInvalid())
/*      */     {
/*  939 */       this.chunkTileEntityMap.remove(pos);
/*  940 */       return null;
/*      */     }
/*      */     
/*  943 */     return tileentity;
/*      */   }
/*      */   
/*      */   public void addTileEntity(TileEntity tileEntityIn)
/*      */   {
/*  948 */     addTileEntity(tileEntityIn.getPos(), tileEntityIn);
/*      */     
/*  950 */     if (this.isChunkLoaded)
/*      */     {
/*  952 */       this.worldObj.addTileEntity(tileEntityIn);
/*      */     }
/*      */   }
/*      */   
/*      */   public void addTileEntity(BlockPos pos, TileEntity tileEntityIn)
/*      */   {
/*  958 */     tileEntityIn.setWorldObj(this.worldObj);
/*  959 */     tileEntityIn.setPos(pos);
/*      */     
/*  961 */     if ((getBlock(pos) instanceof ITileEntityProvider))
/*      */     {
/*  963 */       if (this.chunkTileEntityMap.containsKey(pos))
/*      */       {
/*  965 */         ((TileEntity)this.chunkTileEntityMap.get(pos)).invalidate();
/*      */       }
/*      */       
/*  968 */       tileEntityIn.validate();
/*  969 */       this.chunkTileEntityMap.put(pos, tileEntityIn);
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeTileEntity(BlockPos pos)
/*      */   {
/*  975 */     if (this.isChunkLoaded)
/*      */     {
/*  977 */       TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.remove(pos);
/*      */       
/*  979 */       if (tileentity != null)
/*      */       {
/*  981 */         tileentity.invalidate();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onChunkLoad()
/*      */   {
/*  991 */     this.isChunkLoaded = true;
/*  992 */     this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
/*      */     
/*  994 */     for (int i = 0; i < this.entityLists.length; i++)
/*      */     {
/*  996 */       for (Entity entity : this.entityLists[i])
/*      */       {
/*  998 */         entity.onChunkLoad();
/*      */       }
/*      */       
/* 1001 */       this.worldObj.loadEntities(this.entityLists[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onChunkUnload()
/*      */   {
/* 1010 */     this.isChunkLoaded = false;
/*      */     
/* 1012 */     for (TileEntity tileentity : this.chunkTileEntityMap.values())
/*      */     {
/* 1014 */       this.worldObj.markTileEntityForRemoval(tileentity);
/*      */     }
/*      */     
/* 1017 */     for (int i = 0; i < this.entityLists.length; i++)
/*      */     {
/* 1019 */       this.worldObj.unloadEntities(this.entityLists[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setChunkModified()
/*      */   {
/* 1028 */     this.isModified = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> p_177414_4_)
/*      */   {
/* 1036 */     int i = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
/* 1037 */     int j = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
/* 1038 */     i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
/* 1039 */     j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
/*      */     
/* 1041 */     for (int k = i; k <= j; k++)
/*      */     {
/* 1043 */       if (!this.entityLists[k].isEmpty())
/*      */       {
/* 1045 */         for (Entity entity : this.entityLists[k])
/*      */         {
/* 1047 */           if ((entity.getEntityBoundingBox().intersectsWith(aabb)) && (entity != entityIn))
/*      */           {
/* 1049 */             if ((p_177414_4_ == null) || (p_177414_4_.apply(entity)))
/*      */             {
/* 1051 */               listToFill.add(entity);
/*      */             }
/*      */             
/* 1054 */             Entity[] aentity = entity.getParts();
/*      */             
/* 1056 */             if (aentity != null)
/*      */             {
/* 1058 */               for (int l = 0; l < aentity.length; l++)
/*      */               {
/* 1060 */                 entity = aentity[l];
/*      */                 
/* 1062 */                 if ((entity != entityIn) && (entity.getEntityBoundingBox().intersectsWith(aabb)) && ((p_177414_4_ == null) || (p_177414_4_.apply(entity))))
/*      */                 {
/* 1064 */                   listToFill.add(entity);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> p_177430_4_)
/*      */   {
/* 1076 */     int i = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
/* 1077 */     int j = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
/* 1078 */     i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
/* 1079 */     j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
/*      */     
/* 1081 */     for (int k = i; k <= j; k++)
/*      */     {
/* 1083 */       for (T t : this.entityLists[k].getByClass(entityClass))
/*      */       {
/* 1085 */         if ((t.getEntityBoundingBox().intersectsWith(aabb)) && ((p_177430_4_ == null) || (p_177430_4_.apply(t))))
/*      */         {
/* 1087 */           listToFill.add(t);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean needsSaving(boolean p_76601_1_)
/*      */   {
/* 1098 */     if (p_76601_1_)
/*      */     {
/* 1100 */       if (((this.hasEntities) && (this.worldObj.getTotalWorldTime() != this.lastSaveTime)) || (this.isModified))
/*      */       {
/* 1102 */         return true;
/*      */       }
/*      */     }
/* 1105 */     else if ((this.hasEntities) && (this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L))
/*      */     {
/* 1107 */       return true;
/*      */     }
/*      */     
/* 1110 */     return this.isModified;
/*      */   }
/*      */   
/*      */   public Random getRandomWithSeed(long seed)
/*      */   {
/* 1115 */     return new Random(this.worldObj.getSeed() + this.xPosition * this.xPosition * 4987142 + this.xPosition * 5947611 + this.zPosition * this.zPosition * 4392871L + this.zPosition * 389711 ^ seed);
/*      */   }
/*      */   
/*      */   public boolean isEmpty()
/*      */   {
/* 1120 */     return false;
/*      */   }
/*      */   
/*      */   public void populateChunk(IChunkProvider p_76624_1_, IChunkProvider p_76624_2_, int p_76624_3_, int p_76624_4_)
/*      */   {
/* 1125 */     boolean flag = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1);
/* 1126 */     boolean flag1 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_);
/* 1127 */     boolean flag2 = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1);
/* 1128 */     boolean flag3 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_);
/* 1129 */     boolean flag4 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ - 1);
/* 1130 */     boolean flag5 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ + 1);
/* 1131 */     boolean flag6 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1);
/* 1132 */     boolean flag7 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1);
/*      */     
/* 1134 */     if ((flag1) && (flag2) && (flag5))
/*      */     {
/* 1136 */       if (!this.isTerrainPopulated)
/*      */       {
/* 1138 */         p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_);
/*      */       }
/*      */       else
/*      */       {
/* 1142 */         p_76624_1_.func_177460_a(p_76624_2_, this, p_76624_3_, p_76624_4_);
/*      */       }
/*      */     }
/*      */     
/* 1146 */     if ((flag3) && (flag2) && (flag6))
/*      */     {
/* 1148 */       Chunk chunk = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_);
/*      */       
/* 1150 */       if (!chunk.isTerrainPopulated)
/*      */       {
/* 1152 */         p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_);
/*      */       }
/*      */       else
/*      */       {
/* 1156 */         p_76624_1_.func_177460_a(p_76624_2_, chunk, p_76624_3_ - 1, p_76624_4_);
/*      */       }
/*      */     }
/*      */     
/* 1160 */     if ((flag) && (flag1) && (flag7))
/*      */     {
/* 1162 */       Chunk chunk1 = p_76624_1_.provideChunk(p_76624_3_, p_76624_4_ - 1);
/*      */       
/* 1164 */       if (!chunk1.isTerrainPopulated)
/*      */       {
/* 1166 */         p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_ - 1);
/*      */       }
/*      */       else
/*      */       {
/* 1170 */         p_76624_1_.func_177460_a(p_76624_2_, chunk1, p_76624_3_, p_76624_4_ - 1);
/*      */       }
/*      */     }
/*      */     
/* 1174 */     if ((flag4) && (flag) && (flag3))
/*      */     {
/* 1176 */       Chunk chunk2 = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1);
/*      */       
/* 1178 */       if (!chunk2.isTerrainPopulated)
/*      */       {
/* 1180 */         p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
/*      */       }
/*      */       else
/*      */       {
/* 1184 */         p_76624_1_.func_177460_a(p_76624_2_, chunk2, p_76624_3_ - 1, p_76624_4_ - 1);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public BlockPos getPrecipitationHeight(BlockPos pos)
/*      */   {
/* 1191 */     int i = pos.getX() & 0xF;
/* 1192 */     int j = pos.getZ() & 0xF;
/* 1193 */     int k = i | j << 4;
/* 1194 */     BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
/*      */     
/* 1196 */     if (blockpos.getY() == 64537)
/*      */     {
/* 1198 */       int l = getTopFilledSegment() + 15;
/* 1199 */       blockpos = new BlockPos(pos.getX(), l, pos.getZ());
/* 1200 */       int i1 = -1;
/*      */       
/* 1202 */       while ((blockpos.getY() > 0) && (i1 == -1))
/*      */       {
/* 1204 */         Block block = getBlock(blockpos);
/* 1205 */         Material material = block.getMaterial();
/*      */         
/* 1207 */         if ((!material.blocksMovement()) && (!material.isLiquid()))
/*      */         {
/* 1209 */           blockpos = blockpos.down();
/*      */         }
/*      */         else
/*      */         {
/* 1213 */           i1 = blockpos.getY() + 1;
/*      */         }
/*      */       }
/*      */       
/* 1217 */       this.precipitationHeightMap[k] = i1;
/*      */     }
/*      */     
/* 1220 */     return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
/*      */   }
/*      */   
/*      */   public void func_150804_b(boolean p_150804_1_)
/*      */   {
/* 1225 */     if ((this.isGapLightingUpdated) && (!this.worldObj.provider.getHasNoSky()) && (!p_150804_1_))
/*      */     {
/* 1227 */       recheckGaps(this.worldObj.isRemote);
/*      */     }
/*      */     
/* 1230 */     this.field_150815_m = true;
/*      */     
/* 1232 */     if ((!this.isLightPopulated) && (this.isTerrainPopulated))
/*      */     {
/* 1234 */       func_150809_p();
/*      */     }
/*      */     
/* 1237 */     while (!this.tileEntityPosQueue.isEmpty())
/*      */     {
/* 1239 */       BlockPos blockpos = (BlockPos)this.tileEntityPosQueue.poll();
/*      */       
/* 1241 */       if ((getTileEntity(blockpos, EnumCreateEntityType.CHECK) == null) && (getBlock(blockpos).hasTileEntity()))
/*      */       {
/* 1243 */         TileEntity tileentity = createNewTileEntity(blockpos);
/* 1244 */         this.worldObj.setTileEntity(blockpos, tileentity);
/* 1245 */         this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isPopulated()
/*      */   {
/* 1252 */     return (this.field_150815_m) && (this.isTerrainPopulated) && (this.isLightPopulated);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChunkCoordIntPair getChunkCoordIntPair()
/*      */   {
/* 1260 */     return new ChunkCoordIntPair(this.xPosition, this.zPosition);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAreLevelsEmpty(int startY, int endY)
/*      */   {
/* 1269 */     if (startY < 0)
/*      */     {
/* 1271 */       startY = 0;
/*      */     }
/*      */     
/* 1274 */     if (endY >= 256)
/*      */     {
/* 1276 */       endY = 255;
/*      */     }
/*      */     
/* 1279 */     for (int i = startY; i <= endY; i += 16)
/*      */     {
/* 1281 */       ExtendedBlockStorage extendedblockstorage = this.storageArrays[(i >> 4)];
/*      */       
/* 1283 */       if ((extendedblockstorage != null) && (!extendedblockstorage.isEmpty()))
/*      */       {
/* 1285 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1289 */     return true;
/*      */   }
/*      */   
/*      */   public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays)
/*      */   {
/* 1294 */     if (this.storageArrays.length != newStorageArrays.length)
/*      */     {
/* 1296 */       logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length + " instead of " + this.storageArrays.length);
/*      */     }
/*      */     else
/*      */     {
/* 1300 */       for (int i = 0; i < this.storageArrays.length; i++)
/*      */       {
/* 1302 */         this.storageArrays[i] = newStorageArrays[i];
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_)
/*      */   {
/* 1312 */     int i = 0;
/* 1313 */     boolean flag = !this.worldObj.provider.getHasNoSky();
/*      */     
/* 1315 */     for (int j = 0; j < this.storageArrays.length; j++)
/*      */     {
/* 1317 */       if ((p_177439_2_ & 1 << j) != 0)
/*      */       {
/* 1319 */         if (this.storageArrays[j] == null)
/*      */         {
/* 1321 */           this.storageArrays[j] = new ExtendedBlockStorage(j << 4, flag);
/*      */         }
/*      */         
/* 1324 */         char[] achar = this.storageArrays[j].getData();
/*      */         
/* 1326 */         for (int k = 0; k < achar.length; k++)
/*      */         {
/* 1328 */           achar[k] = ((char)((p_177439_1_[(i + 1)] & 0xFF) << 8 | p_177439_1_[i] & 0xFF));
/* 1329 */           i += 2;
/*      */         }
/*      */       }
/* 1332 */       else if ((p_177439_3_) && (this.storageArrays[j] != null))
/*      */       {
/* 1334 */         this.storageArrays[j] = null;
/*      */       }
/*      */     }
/*      */     
/* 1338 */     for (int l = 0; l < this.storageArrays.length; l++)
/*      */     {
/* 1340 */       if (((p_177439_2_ & 1 << l) != 0) && (this.storageArrays[l] != null))
/*      */       {
/* 1342 */         NibbleArray nibblearray = this.storageArrays[l].getBlocklightArray();
/* 1343 */         System.arraycopy(p_177439_1_, i, nibblearray.getData(), 0, nibblearray.getData().length);
/* 1344 */         i += nibblearray.getData().length;
/*      */       } }
/*      */     int i1;
/*      */     NibbleArray nibblearray1;
/* 1348 */     if (flag)
/*      */     {
/* 1350 */       for (i1 = 0; i1 < this.storageArrays.length; i1++)
/*      */       {
/* 1352 */         if (((p_177439_2_ & 1 << i1) != 0) && (this.storageArrays[i1] != null))
/*      */         {
/* 1354 */           nibblearray1 = this.storageArrays[i1].getSkylightArray();
/* 1355 */           System.arraycopy(p_177439_1_, i, nibblearray1.getData(), 0, nibblearray1.getData().length);
/* 1356 */           i += nibblearray1.getData().length;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1361 */     if (p_177439_3_)
/*      */     {
/* 1363 */       System.arraycopy(p_177439_1_, i, this.blockBiomeArray, 0, this.blockBiomeArray.length);
/* 1364 */       i1 = i + this.blockBiomeArray.length;
/*      */     }
/*      */     
/* 1367 */     for (int j1 = 0; j1 < this.storageArrays.length; j1++)
/*      */     {
/* 1369 */       if ((this.storageArrays[j1] != null) && ((p_177439_2_ & 1 << j1) != 0))
/*      */       {
/* 1371 */         this.storageArrays[j1].removeInvalidBlocks();
/*      */       }
/*      */     }
/*      */     
/* 1375 */     this.isLightPopulated = true;
/* 1376 */     this.isTerrainPopulated = true;
/* 1377 */     generateHeightMap();
/*      */     
/* 1379 */     for (TileEntity tileentity : this.chunkTileEntityMap.values())
/*      */     {
/* 1381 */       tileentity.updateContainingBlockInfo();
/*      */     }
/*      */   }
/*      */   
/*      */   public BiomeGenBase getBiome(BlockPos pos, WorldChunkManager chunkManager)
/*      */   {
/* 1387 */     int i = pos.getX() & 0xF;
/* 1388 */     int j = pos.getZ() & 0xF;
/* 1389 */     int k = this.blockBiomeArray[(j << 4 | i)] & 0xFF;
/*      */     
/* 1391 */     if (k == 255)
/*      */     {
/* 1393 */       BiomeGenBase biomegenbase = chunkManager.getBiomeGenerator(pos, BiomeGenBase.plains);
/* 1394 */       k = biomegenbase.biomeID;
/* 1395 */       this.blockBiomeArray[(j << 4 | i)] = ((byte)(k & 0xFF));
/*      */     }
/*      */     
/* 1398 */     BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(k);
/* 1399 */     return biomegenbase1 == null ? BiomeGenBase.plains : biomegenbase1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] getBiomeArray()
/*      */   {
/* 1407 */     return this.blockBiomeArray;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBiomeArray(byte[] biomeArray)
/*      */   {
/* 1416 */     if (this.blockBiomeArray.length != biomeArray.length)
/*      */     {
/* 1418 */       logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of " + this.blockBiomeArray.length);
/*      */     }
/*      */     else
/*      */     {
/* 1422 */       for (int i = 0; i < this.blockBiomeArray.length; i++)
/*      */       {
/* 1424 */         this.blockBiomeArray[i] = biomeArray[i];
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetRelightChecks()
/*      */   {
/* 1434 */     this.queuedLightChecks = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enqueueRelightChecks()
/*      */   {
/* 1444 */     BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
/*      */     
/* 1446 */     for (int i = 0; i < 8; i++)
/*      */     {
/* 1448 */       if (this.queuedLightChecks >= 4096)
/*      */       {
/* 1450 */         return;
/*      */       }
/*      */       
/* 1453 */       int j = this.queuedLightChecks % 16;
/* 1454 */       int k = this.queuedLightChecks / 16 % 16;
/* 1455 */       int l = this.queuedLightChecks / 256;
/* 1456 */       this.queuedLightChecks += 1;
/*      */       
/* 1458 */       for (int i1 = 0; i1 < 16; i1++)
/*      */       {
/* 1460 */         BlockPos blockpos1 = blockpos.add(k, (j << 4) + i1, l);
/* 1461 */         boolean flag = (i1 == 0) || (i1 == 15) || (k == 0) || (k == 15) || (l == 0) || (l == 15);
/*      */         
/* 1463 */         if (((this.storageArrays[j] == null) && (flag)) || ((this.storageArrays[j] != null) && (this.storageArrays[j].getBlockByExtId(k, i1, l).getMaterial() == Material.air))) {
/*      */           EnumFacing[] arrayOfEnumFacing;
/* 1465 */           int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*      */             
/* 1467 */             BlockPos blockpos2 = blockpos1.offset(enumfacing);
/*      */             
/* 1469 */             if (this.worldObj.getBlockState(blockpos2).getBlock().getLightValue() > 0)
/*      */             {
/* 1471 */               this.worldObj.checkLight(blockpos2);
/*      */             }
/*      */           }
/*      */           
/* 1475 */           this.worldObj.checkLight(blockpos1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void func_150809_p()
/*      */   {
/* 1483 */     this.isTerrainPopulated = true;
/* 1484 */     this.isLightPopulated = true;
/* 1485 */     BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
/*      */     
/* 1487 */     if (!this.worldObj.provider.getHasNoSky())
/*      */     {
/* 1489 */       if (this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.worldObj.func_181545_F(), 16)))
/*      */       {
/*      */         int j;
/*      */         
/* 1493 */         for (int i = 0; i < 16; i++)
/*      */         {
/* 1495 */           for (j = 0; j < 16; j++)
/*      */           {
/* 1497 */             if (!func_150811_f(i, j))
/*      */             {
/* 1499 */               this.isLightPopulated = false;
/* 1500 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1505 */         if (this.isLightPopulated)
/*      */         {
/* 1507 */           for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*      */           {
/* 1509 */             EnumFacing enumfacing = (EnumFacing)enumfacing0;
/* 1510 */             int k = enumfacing.getAxisDirection() == net.minecraft.util.EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
/* 1511 */             this.worldObj.getChunkFromBlockCoords(blockpos.offset(enumfacing, k)).func_180700_a(enumfacing.getOpposite());
/*      */           }
/*      */           
/* 1514 */           func_177441_y();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1519 */         this.isLightPopulated = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void func_177441_y()
/*      */   {
/* 1526 */     for (int i = 0; i < this.updateSkylightColumns.length; i++)
/*      */     {
/* 1528 */       this.updateSkylightColumns[i] = true;
/*      */     }
/*      */     
/* 1531 */     recheckGaps(false);
/*      */   }
/*      */   
/*      */   private void func_180700_a(EnumFacing p_180700_1_)
/*      */   {
/* 1536 */     if (this.isTerrainPopulated)
/*      */     {
/* 1538 */       if (p_180700_1_ == EnumFacing.EAST)
/*      */       {
/* 1540 */         for (int i = 0; i < 16; i++)
/*      */         {
/* 1542 */           func_150811_f(15, i);
/*      */         }
/*      */         
/* 1545 */       } else if (p_180700_1_ == EnumFacing.WEST)
/*      */       {
/* 1547 */         for (int j = 0; j < 16; j++)
/*      */         {
/* 1549 */           func_150811_f(0, j);
/*      */         }
/*      */         
/* 1552 */       } else if (p_180700_1_ == EnumFacing.SOUTH)
/*      */       {
/* 1554 */         for (int k = 0; k < 16; k++)
/*      */         {
/* 1556 */           func_150811_f(k, 15);
/*      */         }
/*      */         
/* 1559 */       } else if (p_180700_1_ == EnumFacing.NORTH)
/*      */       {
/* 1561 */         for (int l = 0; l < 16; l++)
/*      */         {
/* 1563 */           func_150811_f(l, 0);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean func_150811_f(int x, int z)
/*      */   {
/* 1571 */     int i = getTopFilledSegment();
/* 1572 */     boolean flag = false;
/* 1573 */     boolean flag1 = false;
/* 1574 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.xPosition << 4) + x, 0, (this.zPosition << 4) + z);
/*      */     
/* 1576 */     for (int j = i + 16 - 1; (j > this.worldObj.func_181545_F()) || ((j > 0) && (!flag1)); j--)
/*      */     {
/* 1578 */       blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), j, blockpos$mutableblockpos.getZ());
/* 1579 */       int k = getBlockLightOpacity(blockpos$mutableblockpos);
/*      */       
/* 1581 */       if ((k == 255) && (blockpos$mutableblockpos.getY() < this.worldObj.func_181545_F()))
/*      */       {
/* 1583 */         flag1 = true;
/*      */       }
/*      */       
/* 1586 */       if ((!flag) && (k > 0))
/*      */       {
/* 1588 */         flag = true;
/*      */       }
/* 1590 */       else if ((flag) && (k == 0) && (!this.worldObj.checkLight(blockpos$mutableblockpos)))
/*      */       {
/* 1592 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1596 */     for (int l = blockpos$mutableblockpos.getY(); l > 0; l--)
/*      */     {
/* 1598 */       blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());
/*      */       
/* 1600 */       if (getBlock(blockpos$mutableblockpos).getLightValue() > 0)
/*      */       {
/* 1602 */         this.worldObj.checkLight(blockpos$mutableblockpos);
/*      */       }
/*      */     }
/*      */     
/* 1606 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isLoaded()
/*      */   {
/* 1611 */     return this.isChunkLoaded;
/*      */   }
/*      */   
/*      */   public void setChunkLoaded(boolean loaded)
/*      */   {
/* 1616 */     this.isChunkLoaded = loaded;
/*      */   }
/*      */   
/*      */   public World getWorld()
/*      */   {
/* 1621 */     return this.worldObj;
/*      */   }
/*      */   
/*      */   public int[] getHeightMap()
/*      */   {
/* 1626 */     return this.heightMap;
/*      */   }
/*      */   
/*      */   public void setHeightMap(int[] newHeightMap)
/*      */   {
/* 1631 */     if (this.heightMap.length != newHeightMap.length)
/*      */     {
/* 1633 */       logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of " + this.heightMap.length);
/*      */     }
/*      */     else
/*      */     {
/* 1637 */       for (int i = 0; i < this.heightMap.length; i++)
/*      */       {
/* 1639 */         this.heightMap[i] = newHeightMap[i];
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Map<BlockPos, TileEntity> getTileEntityMap()
/*      */   {
/* 1646 */     return this.chunkTileEntityMap;
/*      */   }
/*      */   
/*      */   public ClassInheritanceMultiMap<Entity>[] getEntityLists()
/*      */   {
/* 1651 */     return this.entityLists;
/*      */   }
/*      */   
/*      */   public boolean isTerrainPopulated()
/*      */   {
/* 1656 */     return this.isTerrainPopulated;
/*      */   }
/*      */   
/*      */   public void setTerrainPopulated(boolean terrainPopulated)
/*      */   {
/* 1661 */     this.isTerrainPopulated = terrainPopulated;
/*      */   }
/*      */   
/*      */   public boolean isLightPopulated()
/*      */   {
/* 1666 */     return this.isLightPopulated;
/*      */   }
/*      */   
/*      */   public void setLightPopulated(boolean lightPopulated)
/*      */   {
/* 1671 */     this.isLightPopulated = lightPopulated;
/*      */   }
/*      */   
/*      */   public void setModified(boolean modified)
/*      */   {
/* 1676 */     this.isModified = modified;
/*      */   }
/*      */   
/*      */   public void setHasEntities(boolean hasEntitiesIn)
/*      */   {
/* 1681 */     this.hasEntities = hasEntitiesIn;
/*      */   }
/*      */   
/*      */   public void setLastSaveTime(long saveTime)
/*      */   {
/* 1686 */     this.lastSaveTime = saveTime;
/*      */   }
/*      */   
/*      */   public int getLowestHeight()
/*      */   {
/* 1691 */     return this.heightMapMinimum;
/*      */   }
/*      */   
/*      */   public long getInhabitedTime()
/*      */   {
/* 1696 */     return this.inhabitedTime;
/*      */   }
/*      */   
/*      */   public void setInhabitedTime(long newInhabitedTime)
/*      */   {
/* 1701 */     this.inhabitedTime = newInhabitedTime;
/*      */   }
/*      */   
/*      */   public static enum EnumCreateEntityType
/*      */   {
/* 1706 */     IMMEDIATE, 
/* 1707 */     QUEUED, 
/* 1708 */     CHECK;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\chunk\Chunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */