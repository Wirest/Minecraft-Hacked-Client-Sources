/*      */ package net.minecraft.world;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockLiquid;
/*      */ import net.minecraft.block.BlockRedstoneComparator;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*      */ import net.minecraft.util.EntitySelectors;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumFacing.Plane;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.ITickable;
/*      */ import net.minecraft.util.IntHashMap;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.MovingObjectPosition.MovingObjectType;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.village.VillageCollection;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.MapStorage;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ 
/*      */ public abstract class World implements IBlockAccess
/*      */ {
/*   58 */   private int field_181546_a = 63;
/*      */   
/*      */ 
/*      */   protected boolean scheduledUpdatesAreImmediate;
/*      */   
/*      */ 
/*   64 */   public final List<Entity> loadedEntityList = Lists.newArrayList();
/*   65 */   protected final List<Entity> unloadedEntityList = Lists.newArrayList();
/*   66 */   public final List<TileEntity> loadedTileEntityList = Lists.newArrayList();
/*   67 */   public final List<TileEntity> tickableTileEntities = Lists.newArrayList();
/*   68 */   private final List<TileEntity> addedTileEntityList = Lists.newArrayList();
/*   69 */   private final List<TileEntity> tileEntitiesToBeRemoved = Lists.newArrayList();
/*   70 */   public final List<EntityPlayer> playerEntities = Lists.newArrayList();
/*   71 */   public final List<Entity> weatherEffects = Lists.newArrayList();
/*   72 */   protected final IntHashMap<Entity> entitiesById = new IntHashMap();
/*   73 */   private long cloudColour = 16777215L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int skylightSubtracted;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   83 */   protected int updateLCG = new Random().nextInt();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   88 */   protected final int DIST_HASH_MAGIC = 1013904223;
/*      */   
/*      */ 
/*      */   protected float prevRainingStrength;
/*      */   
/*      */   protected float rainingStrength;
/*      */   
/*      */   protected float prevThunderingStrength;
/*      */   
/*      */   protected float thunderingStrength;
/*      */   
/*      */   private int lastLightningBolt;
/*      */   
/*  101 */   public final Random rand = new Random();
/*      */   
/*      */   public final WorldProvider provider;
/*      */   
/*  105 */   protected List<IWorldAccess> worldAccesses = Lists.newArrayList();
/*      */   
/*      */ 
/*      */   protected IChunkProvider chunkProvider;
/*      */   
/*      */ 
/*      */   protected final ISaveHandler saveHandler;
/*      */   
/*      */ 
/*      */   protected WorldInfo worldInfo;
/*      */   
/*      */   protected boolean findingSpawnPoint;
/*      */   
/*      */   protected MapStorage mapStorage;
/*      */   
/*      */   protected VillageCollection villageCollectionObj;
/*      */   
/*      */   public final Profiler theProfiler;
/*      */   
/*  124 */   private final Calendar theCalendar = Calendar.getInstance();
/*  125 */   protected Scoreboard worldScoreboard = new Scoreboard();
/*      */   
/*      */ 
/*      */ 
/*      */   public final boolean isRemote;
/*      */   
/*      */ 
/*  132 */   protected Set<ChunkCoordIntPair> activeChunkSet = com.google.common.collect.Sets.newHashSet();
/*      */   
/*      */ 
/*      */   private int ambientTickCountdown;
/*      */   
/*      */ 
/*      */   protected boolean spawnHostileMobs;
/*      */   
/*      */ 
/*      */   protected boolean spawnPeacefulMobs;
/*      */   
/*      */ 
/*      */   private boolean processingLoadedTiles;
/*      */   
/*      */ 
/*      */   private final WorldBorder worldBorder;
/*      */   
/*      */ 
/*      */   int[] lightUpdateBlockList;
/*      */   
/*      */ 
/*      */   protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client)
/*      */   {
/*  155 */     this.ambientTickCountdown = this.rand.nextInt(12000);
/*  156 */     this.spawnHostileMobs = true;
/*  157 */     this.spawnPeacefulMobs = true;
/*  158 */     this.lightUpdateBlockList = new int[32768];
/*  159 */     this.saveHandler = saveHandlerIn;
/*  160 */     this.theProfiler = profilerIn;
/*  161 */     this.worldInfo = info;
/*  162 */     this.provider = providerIn;
/*  163 */     this.isRemote = client;
/*  164 */     this.worldBorder = providerIn.getWorldBorder();
/*      */   }
/*      */   
/*      */   public World init()
/*      */   {
/*  169 */     return this;
/*      */   }
/*      */   
/*      */   public BiomeGenBase getBiomeGenForCoords(final BlockPos pos)
/*      */   {
/*  174 */     if (isBlockLoaded(pos))
/*      */     {
/*  176 */       Chunk chunk = getChunkFromBlockCoords(pos);
/*      */       
/*      */       try
/*      */       {
/*  180 */         return chunk.getBiome(pos, this.provider.getWorldChunkManager());
/*      */       }
/*      */       catch (Throwable throwable)
/*      */       {
/*  184 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
/*  185 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
/*  186 */         crashreportcategory.addCrashSectionCallable("Location", new Callable()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/*  190 */             return CrashReportCategory.getCoordinateInfo(pos);
/*      */           }
/*  192 */         });
/*  193 */         throw new ReportedException(crashreport);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  198 */     return this.provider.getWorldChunkManager().getBiomeGenerator(pos, BiomeGenBase.plains);
/*      */   }
/*      */   
/*      */ 
/*      */   public WorldChunkManager getWorldChunkManager()
/*      */   {
/*  204 */     return this.provider.getWorldChunkManager();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected abstract IChunkProvider createChunkProvider();
/*      */   
/*      */ 
/*      */   public void initialize(WorldSettings settings)
/*      */   {
/*  214 */     this.worldInfo.setServerInitialized(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInitialSpawnLocation()
/*      */   {
/*  222 */     setSpawnPoint(new BlockPos(8, 64, 8));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Block getGroundAboveSeaLevel(BlockPos pos)
/*      */   {
/*  229 */     for (BlockPos blockpos = new BlockPos(pos.getX(), func_181545_F(), pos.getZ()); !isAirBlock(blockpos.up()); blockpos = blockpos.up()) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  234 */     return getBlockState(blockpos).getBlock();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isValid(BlockPos pos)
/*      */   {
/*  242 */     return (pos.getX() >= -30000000) && (pos.getZ() >= -30000000) && (pos.getX() < 30000000) && (pos.getZ() < 30000000) && (pos.getY() >= 0) && (pos.getY() < 256);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAirBlock(BlockPos pos)
/*      */   {
/*  251 */     return getBlockState(pos).getBlock().getMaterial() == Material.air;
/*      */   }
/*      */   
/*      */   public boolean isBlockLoaded(BlockPos pos)
/*      */   {
/*  256 */     return isBlockLoaded(pos, true);
/*      */   }
/*      */   
/*      */   public boolean isBlockLoaded(BlockPos pos, boolean allowEmpty)
/*      */   {
/*  261 */     return !isValid(pos) ? false : isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
/*      */   }
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos center, int radius)
/*      */   {
/*  266 */     return isAreaLoaded(center, radius, true);
/*      */   }
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty)
/*      */   {
/*  271 */     return isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
/*      */   }
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos from, BlockPos to)
/*      */   {
/*  276 */     return isAreaLoaded(from, to, true);
/*      */   }
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos from, BlockPos to, boolean allowEmpty)
/*      */   {
/*  281 */     return isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
/*      */   }
/*      */   
/*      */   public boolean isAreaLoaded(StructureBoundingBox box)
/*      */   {
/*  286 */     return isAreaLoaded(box, true);
/*      */   }
/*      */   
/*      */   public boolean isAreaLoaded(StructureBoundingBox box, boolean allowEmpty)
/*      */   {
/*  291 */     return isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
/*      */   }
/*      */   
/*      */   private boolean isAreaLoaded(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, boolean allowEmpty)
/*      */   {
/*  296 */     if ((yEnd >= 0) && (yStart < 256))
/*      */     {
/*  298 */       xStart >>= 4;
/*  299 */       zStart >>= 4;
/*  300 */       xEnd >>= 4;
/*  301 */       zEnd >>= 4;
/*      */       
/*  303 */       for (int i = xStart; i <= xEnd; i++)
/*      */       {
/*  305 */         for (int j = zStart; j <= zEnd; j++)
/*      */         {
/*  307 */           if (!isChunkLoaded(i, j, allowEmpty))
/*      */           {
/*  309 */             return false;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  314 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  318 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean isChunkLoaded(int x, int z, boolean allowEmpty)
/*      */   {
/*  324 */     return (this.chunkProvider.chunkExists(x, z)) && ((allowEmpty) || (!this.chunkProvider.provideChunk(x, z).isEmpty()));
/*      */   }
/*      */   
/*      */   public Chunk getChunkFromBlockCoords(BlockPos pos)
/*      */   {
/*  329 */     return getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ)
/*      */   {
/*  337 */     return this.chunkProvider.provideChunk(chunkX, chunkZ);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setBlockState(BlockPos pos, IBlockState newState, int flags)
/*      */   {
/*  347 */     if (!isValid(pos))
/*      */     {
/*  349 */       return false;
/*      */     }
/*  351 */     if ((!this.isRemote) && (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD))
/*      */     {
/*  353 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  357 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  358 */     Block block = newState.getBlock();
/*  359 */     IBlockState iblockstate = chunk.setBlockState(pos, newState);
/*      */     
/*  361 */     if (iblockstate == null)
/*      */     {
/*  363 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  367 */     Block block1 = iblockstate.getBlock();
/*      */     
/*  369 */     if ((block.getLightOpacity() != block1.getLightOpacity()) || (block.getLightValue() != block1.getLightValue()))
/*      */     {
/*  371 */       this.theProfiler.startSection("checkLight");
/*  372 */       checkLight(pos);
/*  373 */       this.theProfiler.endSection();
/*      */     }
/*      */     
/*  376 */     if (((flags & 0x2) != 0) && ((!this.isRemote) || ((flags & 0x4) == 0)) && (chunk.isPopulated()))
/*      */     {
/*  378 */       markBlockForUpdate(pos);
/*      */     }
/*      */     
/*  381 */     if ((!this.isRemote) && ((flags & 0x1) != 0))
/*      */     {
/*  383 */       notifyNeighborsRespectDebug(pos, iblockstate.getBlock());
/*      */       
/*  385 */       if (block.hasComparatorInputOverride())
/*      */       {
/*  387 */         updateComparatorOutputLevel(pos, block);
/*      */       }
/*      */     }
/*      */     
/*  391 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean setBlockToAir(BlockPos pos)
/*      */   {
/*  398 */     return setBlockState(pos, Blocks.air.getDefaultState(), 3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean destroyBlock(BlockPos pos, boolean dropBlock)
/*      */   {
/*  406 */     IBlockState iblockstate = getBlockState(pos);
/*  407 */     Block block = iblockstate.getBlock();
/*      */     
/*  409 */     if (block.getMaterial() == Material.air)
/*      */     {
/*  411 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  415 */     playAuxSFX(2001, pos, Block.getStateId(iblockstate));
/*      */     
/*  417 */     if (dropBlock)
/*      */     {
/*  419 */       block.dropBlockAsItem(this, pos, iblockstate, 0);
/*      */     }
/*      */     
/*  422 */     return setBlockState(pos, Blocks.air.getDefaultState(), 3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setBlockState(BlockPos pos, IBlockState state)
/*      */   {
/*  431 */     return setBlockState(pos, state, 3);
/*      */   }
/*      */   
/*      */   public void markBlockForUpdate(BlockPos pos)
/*      */   {
/*  436 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  438 */       ((IWorldAccess)this.worldAccesses.get(i)).markBlockForUpdate(pos);
/*      */     }
/*      */   }
/*      */   
/*      */   public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType)
/*      */   {
/*  444 */     if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
/*      */     {
/*  446 */       notifyNeighborsOfStateChange(pos, blockType);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void markBlocksDirtyVertical(int x1, int z1, int x2, int z2)
/*      */   {
/*  455 */     if (x2 > z2)
/*      */     {
/*  457 */       int i = z2;
/*  458 */       z2 = x2;
/*  459 */       x2 = i;
/*      */     }
/*      */     
/*  462 */     if (!this.provider.getHasNoSky())
/*      */     {
/*  464 */       for (int j = x2; j <= z2; j++)
/*      */       {
/*  466 */         checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, j, z1));
/*      */       }
/*      */     }
/*      */     
/*  470 */     markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
/*      */   }
/*      */   
/*      */   public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax)
/*      */   {
/*  475 */     markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
/*      */   }
/*      */   
/*      */   public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
/*      */   {
/*  480 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  482 */       ((IWorldAccess)this.worldAccesses.get(i)).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
/*      */     }
/*      */   }
/*      */   
/*      */   public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType)
/*      */   {
/*  488 */     notifyBlockOfStateChange(pos.west(), blockType);
/*  489 */     notifyBlockOfStateChange(pos.east(), blockType);
/*  490 */     notifyBlockOfStateChange(pos.down(), blockType);
/*  491 */     notifyBlockOfStateChange(pos.up(), blockType);
/*  492 */     notifyBlockOfStateChange(pos.north(), blockType);
/*  493 */     notifyBlockOfStateChange(pos.south(), blockType);
/*      */   }
/*      */   
/*      */   public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide)
/*      */   {
/*  498 */     if (skipSide != EnumFacing.WEST)
/*      */     {
/*  500 */       notifyBlockOfStateChange(pos.west(), blockType);
/*      */     }
/*      */     
/*  503 */     if (skipSide != EnumFacing.EAST)
/*      */     {
/*  505 */       notifyBlockOfStateChange(pos.east(), blockType);
/*      */     }
/*      */     
/*  508 */     if (skipSide != EnumFacing.DOWN)
/*      */     {
/*  510 */       notifyBlockOfStateChange(pos.down(), blockType);
/*      */     }
/*      */     
/*  513 */     if (skipSide != EnumFacing.UP)
/*      */     {
/*  515 */       notifyBlockOfStateChange(pos.up(), blockType);
/*      */     }
/*      */     
/*  518 */     if (skipSide != EnumFacing.NORTH)
/*      */     {
/*  520 */       notifyBlockOfStateChange(pos.north(), blockType);
/*      */     }
/*      */     
/*  523 */     if (skipSide != EnumFacing.SOUTH)
/*      */     {
/*  525 */       notifyBlockOfStateChange(pos.south(), blockType);
/*      */     }
/*      */   }
/*      */   
/*      */   public void notifyBlockOfStateChange(BlockPos pos, final Block blockIn)
/*      */   {
/*  531 */     if (!this.isRemote)
/*      */     {
/*  533 */       IBlockState iblockstate = getBlockState(pos);
/*      */       
/*      */       try
/*      */       {
/*  537 */         iblockstate.getBlock().onNeighborBlockChange(this, pos, iblockstate, blockIn);
/*      */       }
/*      */       catch (Throwable throwable)
/*      */       {
/*  541 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
/*  542 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
/*  543 */         crashreportcategory.addCrashSectionCallable("Source block type", new Callable()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/*      */             try
/*      */             {
/*  549 */               return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.getIdFromBlock(blockIn)), blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName() });
/*      */             }
/*      */             catch (Throwable var2) {}
/*      */             
/*  553 */             return "ID #" + Block.getIdFromBlock(blockIn);
/*      */           }
/*      */           
/*  556 */         });
/*  557 */         CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
/*  558 */         throw new ReportedException(crashreport);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isBlockTickPending(BlockPos pos, Block blockType)
/*      */   {
/*  565 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canSeeSky(BlockPos pos)
/*      */   {
/*  570 */     return getChunkFromBlockCoords(pos).canSeeSky(pos);
/*      */   }
/*      */   
/*      */   public boolean canBlockSeeSky(BlockPos pos)
/*      */   {
/*  575 */     if (pos.getY() >= func_181545_F())
/*      */     {
/*  577 */       return canSeeSky(pos);
/*      */     }
/*      */     
/*      */ 
/*  581 */     BlockPos blockpos = new BlockPos(pos.getX(), func_181545_F(), pos.getZ());
/*      */     
/*  583 */     if (!canSeeSky(blockpos))
/*      */     {
/*  585 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  589 */     for (blockpos = blockpos.down(); blockpos.getY() > pos.getY(); blockpos = blockpos.down())
/*      */     {
/*  591 */       Block block = getBlockState(blockpos).getBlock();
/*      */       
/*  593 */       if ((block.getLightOpacity() > 0) && (!block.getMaterial().isLiquid()))
/*      */       {
/*  595 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  599 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getLight(BlockPos pos)
/*      */   {
/*  606 */     if (pos.getY() < 0)
/*      */     {
/*  608 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  612 */     if (pos.getY() >= 256)
/*      */     {
/*  614 */       pos = new BlockPos(pos.getX(), 255, pos.getZ());
/*      */     }
/*      */     
/*  617 */     return getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getLightFromNeighbors(BlockPos pos)
/*      */   {
/*  623 */     return getLight(pos, true);
/*      */   }
/*      */   
/*      */   public int getLight(BlockPos pos, boolean checkNeighbors)
/*      */   {
/*  628 */     if ((pos.getX() >= -30000000) && (pos.getZ() >= -30000000) && (pos.getX() < 30000000) && (pos.getZ() < 30000000))
/*      */     {
/*  630 */       if ((checkNeighbors) && (getBlockState(pos).getBlock().getUseNeighborBrightness()))
/*      */       {
/*  632 */         int i1 = getLight(pos.up(), false);
/*  633 */         int i = getLight(pos.east(), false);
/*  634 */         int j = getLight(pos.west(), false);
/*  635 */         int k = getLight(pos.south(), false);
/*  636 */         int l = getLight(pos.north(), false);
/*      */         
/*  638 */         if (i > i1)
/*      */         {
/*  640 */           i1 = i;
/*      */         }
/*      */         
/*  643 */         if (j > i1)
/*      */         {
/*  645 */           i1 = j;
/*      */         }
/*      */         
/*  648 */         if (k > i1)
/*      */         {
/*  650 */           i1 = k;
/*      */         }
/*      */         
/*  653 */         if (l > i1)
/*      */         {
/*  655 */           i1 = l;
/*      */         }
/*      */         
/*  658 */         return i1;
/*      */       }
/*  660 */       if (pos.getY() < 0)
/*      */       {
/*  662 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*  666 */       if (pos.getY() >= 256)
/*      */       {
/*  668 */         pos = new BlockPos(pos.getX(), 255, pos.getZ());
/*      */       }
/*      */       
/*  671 */       Chunk chunk = getChunkFromBlockCoords(pos);
/*  672 */       return chunk.getLightSubtracted(pos, this.skylightSubtracted);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  677 */     return 15;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public BlockPos getHeight(BlockPos pos)
/*      */   {
/*      */     int i;
/*      */     
/*      */     int i;
/*      */     
/*  688 */     if ((pos.getX() >= -30000000) && (pos.getZ() >= -30000000) && (pos.getX() < 30000000) && (pos.getZ() < 30000000)) {
/*      */       int i;
/*  690 */       if (isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, true))
/*      */       {
/*  692 */         i = getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
/*      */       }
/*      */       else
/*      */       {
/*  696 */         i = 0;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  701 */       i = func_181545_F() + 1;
/*      */     }
/*      */     
/*  704 */     return new BlockPos(pos.getX(), i, pos.getZ());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getChunksLowestHorizon(int x, int z)
/*      */   {
/*  712 */     if ((x >= -30000000) && (z >= -30000000) && (x < 30000000) && (z < 30000000))
/*      */     {
/*  714 */       if (!isChunkLoaded(x >> 4, z >> 4, true))
/*      */       {
/*  716 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*  720 */       Chunk chunk = getChunkFromChunkCoords(x >> 4, z >> 4);
/*  721 */       return chunk.getLowestHeight();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  726 */     return func_181545_F() + 1;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos)
/*      */   {
/*  732 */     if ((this.provider.getHasNoSky()) && (type == EnumSkyBlock.SKY))
/*      */     {
/*  734 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  738 */     if (pos.getY() < 0)
/*      */     {
/*  740 */       pos = new BlockPos(pos.getX(), 0, pos.getZ());
/*      */     }
/*      */     
/*  743 */     if (!isValid(pos))
/*      */     {
/*  745 */       return type.defaultLightValue;
/*      */     }
/*  747 */     if (!isBlockLoaded(pos))
/*      */     {
/*  749 */       return type.defaultLightValue;
/*      */     }
/*  751 */     if (getBlockState(pos).getBlock().getUseNeighborBrightness())
/*      */     {
/*  753 */       int i1 = getLightFor(type, pos.up());
/*  754 */       int i = getLightFor(type, pos.east());
/*  755 */       int j = getLightFor(type, pos.west());
/*  756 */       int k = getLightFor(type, pos.south());
/*  757 */       int l = getLightFor(type, pos.north());
/*      */       
/*  759 */       if (i > i1)
/*      */       {
/*  761 */         i1 = i;
/*      */       }
/*      */       
/*  764 */       if (j > i1)
/*      */       {
/*  766 */         i1 = j;
/*      */       }
/*      */       
/*  769 */       if (k > i1)
/*      */       {
/*  771 */         i1 = k;
/*      */       }
/*      */       
/*  774 */       if (l > i1)
/*      */       {
/*  776 */         i1 = l;
/*      */       }
/*      */       
/*  779 */       return i1;
/*      */     }
/*      */     
/*      */ 
/*  783 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  784 */     return chunk.getLightFor(type, pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getLightFor(EnumSkyBlock type, BlockPos pos)
/*      */   {
/*  791 */     if (pos.getY() < 0)
/*      */     {
/*  793 */       pos = new BlockPos(pos.getX(), 0, pos.getZ());
/*      */     }
/*      */     
/*  796 */     if (!isValid(pos))
/*      */     {
/*  798 */       return type.defaultLightValue;
/*      */     }
/*  800 */     if (!isBlockLoaded(pos))
/*      */     {
/*  802 */       return type.defaultLightValue;
/*      */     }
/*      */     
/*      */ 
/*  806 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  807 */     return chunk.getLightFor(type, pos);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue)
/*      */   {
/*  813 */     if (isValid(pos))
/*      */     {
/*  815 */       if (isBlockLoaded(pos))
/*      */       {
/*  817 */         Chunk chunk = getChunkFromBlockCoords(pos);
/*  818 */         chunk.setLightFor(type, pos, lightValue);
/*  819 */         notifyLightSet(pos);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void notifyLightSet(BlockPos pos)
/*      */   {
/*  826 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  828 */       ((IWorldAccess)this.worldAccesses.get(i)).notifyLightSet(pos);
/*      */     }
/*      */   }
/*      */   
/*      */   public int getCombinedLight(BlockPos pos, int lightValue)
/*      */   {
/*  834 */     int i = getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
/*  835 */     int j = getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);
/*      */     
/*  837 */     if (j < lightValue)
/*      */     {
/*  839 */       j = lightValue;
/*      */     }
/*      */     
/*  842 */     return i << 20 | j << 4;
/*      */   }
/*      */   
/*      */   public float getLightBrightness(BlockPos pos)
/*      */   {
/*  847 */     return this.provider.getLightBrightnessTable()[getLightFromNeighbors(pos)];
/*      */   }
/*      */   
/*      */   public IBlockState getBlockState(BlockPos pos)
/*      */   {
/*  852 */     if (!isValid(pos))
/*      */     {
/*  854 */       return Blocks.air.getDefaultState();
/*      */     }
/*      */     
/*      */ 
/*  858 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  859 */     return chunk.getBlockState(pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isDaytime()
/*      */   {
/*  868 */     return this.skylightSubtracted < 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public MovingObjectPosition rayTraceBlocks(Vec3 p_72933_1_, Vec3 p_72933_2_)
/*      */   {
/*  876 */     return rayTraceBlocks(p_72933_1_, p_72933_2_, false, false, false);
/*      */   }
/*      */   
/*      */   public MovingObjectPosition rayTraceBlocks(Vec3 start, Vec3 end, boolean stopOnLiquid)
/*      */   {
/*  881 */     return rayTraceBlocks(start, end, stopOnLiquid, false, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MovingObjectPosition rayTraceBlocks(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
/*      */   {
/*  890 */     if ((!Double.isNaN(vec31.xCoord)) && (!Double.isNaN(vec31.yCoord)) && (!Double.isNaN(vec31.zCoord)))
/*      */     {
/*  892 */       if ((!Double.isNaN(vec32.xCoord)) && (!Double.isNaN(vec32.yCoord)) && (!Double.isNaN(vec32.zCoord)))
/*      */       {
/*  894 */         int i = MathHelper.floor_double(vec32.xCoord);
/*  895 */         int j = MathHelper.floor_double(vec32.yCoord);
/*  896 */         int k = MathHelper.floor_double(vec32.zCoord);
/*  897 */         int l = MathHelper.floor_double(vec31.xCoord);
/*  898 */         int i1 = MathHelper.floor_double(vec31.yCoord);
/*  899 */         int j1 = MathHelper.floor_double(vec31.zCoord);
/*  900 */         BlockPos blockpos = new BlockPos(l, i1, j1);
/*  901 */         IBlockState iblockstate = getBlockState(blockpos);
/*  902 */         Block block = iblockstate.getBlock();
/*      */         
/*  904 */         if (((!ignoreBlockWithoutBoundingBox) || (block.getCollisionBoundingBox(this, blockpos, iblockstate) != null)) && (block.canCollideCheck(iblockstate, stopOnLiquid)))
/*      */         {
/*  906 */           MovingObjectPosition movingobjectposition = block.collisionRayTrace(this, blockpos, vec31, vec32);
/*      */           
/*  908 */           if (movingobjectposition != null)
/*      */           {
/*  910 */             return movingobjectposition;
/*      */           }
/*      */         }
/*      */         
/*  914 */         MovingObjectPosition movingobjectposition2 = null;
/*  915 */         int k1 = 200;
/*      */         
/*  917 */         while (k1-- >= 0)
/*      */         {
/*  919 */           if ((Double.isNaN(vec31.xCoord)) || (Double.isNaN(vec31.yCoord)) || (Double.isNaN(vec31.zCoord)))
/*      */           {
/*  921 */             return null;
/*      */           }
/*      */           
/*  924 */           if ((l == i) && (i1 == j) && (j1 == k))
/*      */           {
/*  926 */             return returnLastUncollidableBlock ? movingobjectposition2 : null;
/*      */           }
/*      */           
/*  929 */           boolean flag2 = true;
/*  930 */           boolean flag = true;
/*  931 */           boolean flag1 = true;
/*  932 */           double d0 = 999.0D;
/*  933 */           double d1 = 999.0D;
/*  934 */           double d2 = 999.0D;
/*      */           
/*  936 */           if (i > l)
/*      */           {
/*  938 */             d0 = l + 1.0D;
/*      */           }
/*  940 */           else if (i < l)
/*      */           {
/*  942 */             d0 = l + 0.0D;
/*      */           }
/*      */           else
/*      */           {
/*  946 */             flag2 = false;
/*      */           }
/*      */           
/*  949 */           if (j > i1)
/*      */           {
/*  951 */             d1 = i1 + 1.0D;
/*      */           }
/*  953 */           else if (j < i1)
/*      */           {
/*  955 */             d1 = i1 + 0.0D;
/*      */           }
/*      */           else
/*      */           {
/*  959 */             flag = false;
/*      */           }
/*      */           
/*  962 */           if (k > j1)
/*      */           {
/*  964 */             d2 = j1 + 1.0D;
/*      */           }
/*  966 */           else if (k < j1)
/*      */           {
/*  968 */             d2 = j1 + 0.0D;
/*      */           }
/*      */           else
/*      */           {
/*  972 */             flag1 = false;
/*      */           }
/*      */           
/*  975 */           double d3 = 999.0D;
/*  976 */           double d4 = 999.0D;
/*  977 */           double d5 = 999.0D;
/*  978 */           double d6 = vec32.xCoord - vec31.xCoord;
/*  979 */           double d7 = vec32.yCoord - vec31.yCoord;
/*  980 */           double d8 = vec32.zCoord - vec31.zCoord;
/*      */           
/*  982 */           if (flag2)
/*      */           {
/*  984 */             d3 = (d0 - vec31.xCoord) / d6;
/*      */           }
/*      */           
/*  987 */           if (flag)
/*      */           {
/*  989 */             d4 = (d1 - vec31.yCoord) / d7;
/*      */           }
/*      */           
/*  992 */           if (flag1)
/*      */           {
/*  994 */             d5 = (d2 - vec31.zCoord) / d8;
/*      */           }
/*      */           
/*  997 */           if (d3 == -0.0D)
/*      */           {
/*  999 */             d3 = -1.0E-4D;
/*      */           }
/*      */           
/* 1002 */           if (d4 == -0.0D)
/*      */           {
/* 1004 */             d4 = -1.0E-4D;
/*      */           }
/*      */           
/* 1007 */           if (d5 == -0.0D)
/*      */           {
/* 1009 */             d5 = -1.0E-4D;
/*      */           }
/*      */           
/*      */           EnumFacing enumfacing;
/*      */           
/* 1014 */           if ((d3 < d4) && (d3 < d5))
/*      */           {
/* 1016 */             EnumFacing enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
/* 1017 */             vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
/*      */           }
/* 1019 */           else if (d4 < d5)
/*      */           {
/* 1021 */             EnumFacing enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
/* 1022 */             vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
/*      */           }
/*      */           else
/*      */           {
/* 1026 */             enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
/* 1027 */             vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
/*      */           }
/*      */           
/* 1030 */           l = MathHelper.floor_double(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
/* 1031 */           i1 = MathHelper.floor_double(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
/* 1032 */           j1 = MathHelper.floor_double(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
/* 1033 */           blockpos = new BlockPos(l, i1, j1);
/* 1034 */           IBlockState iblockstate1 = getBlockState(blockpos);
/* 1035 */           Block block1 = iblockstate1.getBlock();
/*      */           
/* 1037 */           if ((!ignoreBlockWithoutBoundingBox) || (block1.getCollisionBoundingBox(this, blockpos, iblockstate1) != null))
/*      */           {
/* 1039 */             if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
/*      */             {
/* 1041 */               MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(this, blockpos, vec31, vec32);
/*      */               
/* 1043 */               if (movingobjectposition1 != null)
/*      */               {
/* 1045 */                 return movingobjectposition1;
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 1050 */               movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec31, enumfacing, blockpos);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1055 */         return returnLastUncollidableBlock ? movingobjectposition2 : null;
/*      */       }
/*      */       
/*      */ 
/* 1059 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1064 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playSoundAtEntity(Entity entityIn, String name, float volume, float pitch)
/*      */   {
/* 1074 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1076 */       ((IWorldAccess)this.worldAccesses.get(i)).playSound(name, entityIn.posX, entityIn.posY, entityIn.posZ, volume, pitch);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playSoundToNearExcept(EntityPlayer player, String name, float volume, float pitch)
/*      */   {
/* 1085 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1087 */       ((IWorldAccess)this.worldAccesses.get(i)).playSoundToNearExcept(player, name, player.posX, player.posY, player.posZ, volume, pitch);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch)
/*      */   {
/* 1098 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1100 */       ((IWorldAccess)this.worldAccesses.get(i)).playSound(soundName, x, y, z, volume, pitch);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void playRecord(BlockPos pos, String name)
/*      */   {
/* 1113 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1115 */       ((IWorldAccess)this.worldAccesses.get(i)).playRecord(name, pos);
/*      */     }
/*      */   }
/*      */   
/*      */   public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175688_14_)
/*      */   {
/* 1121 */     spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175688_14_);
/*      */   }
/*      */   
/*      */   public void spawnParticle(EnumParticleTypes particleType, boolean p_175682_2_, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175682_15_)
/*      */   {
/* 1126 */     spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() | p_175682_2_, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175682_15_);
/*      */   }
/*      */   
/*      */   private void spawnParticle(int particleID, boolean p_175720_2_, double xCood, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175720_15_)
/*      */   {
/* 1131 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1133 */       ((IWorldAccess)this.worldAccesses.get(i)).spawnParticle(particleID, p_175720_2_, xCood, yCoord, zCoord, xOffset, yOffset, zOffset, p_175720_15_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean addWeatherEffect(Entity entityIn)
/*      */   {
/* 1142 */     this.weatherEffects.add(entityIn);
/* 1143 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean spawnEntityInWorld(Entity entityIn)
/*      */   {
/* 1151 */     int i = MathHelper.floor_double(entityIn.posX / 16.0D);
/* 1152 */     int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
/* 1153 */     boolean flag = entityIn.forceSpawn;
/*      */     
/* 1155 */     if ((entityIn instanceof EntityPlayer))
/*      */     {
/* 1157 */       flag = true;
/*      */     }
/*      */     
/* 1160 */     if ((!flag) && (!isChunkLoaded(i, j, true)))
/*      */     {
/* 1162 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1166 */     if ((entityIn instanceof EntityPlayer))
/*      */     {
/* 1168 */       EntityPlayer entityplayer = (EntityPlayer)entityIn;
/* 1169 */       this.playerEntities.add(entityplayer);
/* 1170 */       updateAllPlayersSleepingFlag();
/*      */     }
/*      */     
/* 1173 */     getChunkFromChunkCoords(i, j).addEntity(entityIn);
/* 1174 */     this.loadedEntityList.add(entityIn);
/* 1175 */     onEntityAdded(entityIn);
/* 1176 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void onEntityAdded(Entity entityIn)
/*      */   {
/* 1182 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1184 */       ((IWorldAccess)this.worldAccesses.get(i)).onEntityAdded(entityIn);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void onEntityRemoved(Entity entityIn)
/*      */   {
/* 1190 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1192 */       ((IWorldAccess)this.worldAccesses.get(i)).onEntityRemoved(entityIn);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeEntity(Entity entityIn)
/*      */   {
/* 1201 */     if (entityIn.riddenByEntity != null)
/*      */     {
/* 1203 */       entityIn.riddenByEntity.mountEntity(null);
/*      */     }
/*      */     
/* 1206 */     if (entityIn.ridingEntity != null)
/*      */     {
/* 1208 */       entityIn.mountEntity(null);
/*      */     }
/*      */     
/* 1211 */     entityIn.setDead();
/*      */     
/* 1213 */     if ((entityIn instanceof EntityPlayer))
/*      */     {
/* 1215 */       this.playerEntities.remove(entityIn);
/* 1216 */       updateAllPlayersSleepingFlag();
/* 1217 */       onEntityRemoved(entityIn);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removePlayerEntityDangerously(Entity entityIn)
/*      */   {
/* 1226 */     entityIn.setDead();
/*      */     
/* 1228 */     if ((entityIn instanceof EntityPlayer))
/*      */     {
/* 1230 */       this.playerEntities.remove(entityIn);
/* 1231 */       updateAllPlayersSleepingFlag();
/*      */     }
/*      */     
/* 1234 */     int i = entityIn.chunkCoordX;
/* 1235 */     int j = entityIn.chunkCoordZ;
/*      */     
/* 1237 */     if ((entityIn.addedToChunk) && (isChunkLoaded(i, j, true)))
/*      */     {
/* 1239 */       getChunkFromChunkCoords(i, j).removeEntity(entityIn);
/*      */     }
/*      */     
/* 1242 */     this.loadedEntityList.remove(entityIn);
/* 1243 */     onEntityRemoved(entityIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addWorldAccess(IWorldAccess worldAccess)
/*      */   {
/* 1251 */     this.worldAccesses.add(worldAccess);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeWorldAccess(IWorldAccess worldAccess)
/*      */   {
/* 1259 */     this.worldAccesses.remove(worldAccess);
/*      */   }
/*      */   
/*      */   public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entityIn, AxisAlignedBB bb)
/*      */   {
/* 1264 */     List<AxisAlignedBB> list = Lists.newArrayList();
/* 1265 */     int i = MathHelper.floor_double(bb.minX);
/* 1266 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1267 */     int k = MathHelper.floor_double(bb.minY);
/* 1268 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1269 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1270 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 1271 */     WorldBorder worldborder = getWorldBorder();
/* 1272 */     boolean flag = entityIn.isOutsideBorder();
/* 1273 */     boolean flag1 = isInsideBorder(worldborder, entityIn);
/* 1274 */     IBlockState iblockstate = Blocks.stone.getDefaultState();
/* 1275 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1277 */     for (int k1 = i; k1 < j; k1++)
/*      */     {
/* 1279 */       for (int l1 = i1; l1 < j1; l1++)
/*      */       {
/* 1281 */         if (isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k1, 64, l1)))
/*      */         {
/* 1283 */           for (int i2 = k - 1; i2 < l; i2++)
/*      */           {
/* 1285 */             blockpos$mutableblockpos.func_181079_c(k1, i2, l1);
/*      */             
/* 1287 */             if ((flag) && (flag1))
/*      */             {
/* 1289 */               entityIn.setOutsideBorder(false);
/*      */             }
/* 1291 */             else if ((!flag) && (!flag1))
/*      */             {
/* 1293 */               entityIn.setOutsideBorder(true);
/*      */             }
/*      */             
/* 1296 */             IBlockState iblockstate1 = iblockstate;
/*      */             
/* 1298 */             if ((worldborder.contains(blockpos$mutableblockpos)) || (!flag1))
/*      */             {
/* 1300 */               iblockstate1 = getBlockState(blockpos$mutableblockpos);
/*      */             }
/*      */             
/* 1303 */             iblockstate1.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate1, bb, list, entityIn);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1309 */     double d0 = 0.25D;
/* 1310 */     List<Entity> list1 = getEntitiesWithinAABBExcludingEntity(entityIn, bb.expand(d0, d0, d0));
/*      */     
/* 1312 */     for (int j2 = 0; j2 < list1.size(); j2++)
/*      */     {
/* 1314 */       if ((entityIn.riddenByEntity != list1) && (entityIn.ridingEntity != list1))
/*      */       {
/* 1316 */         AxisAlignedBB axisalignedbb = ((Entity)list1.get(j2)).getCollisionBoundingBox();
/*      */         
/* 1318 */         if ((axisalignedbb != null) && (axisalignedbb.intersectsWith(bb)))
/*      */         {
/* 1320 */           list.add(axisalignedbb);
/*      */         }
/*      */         
/* 1323 */         axisalignedbb = entityIn.getCollisionBox((Entity)list1.get(j2));
/*      */         
/* 1325 */         if ((axisalignedbb != null) && (axisalignedbb.intersectsWith(bb)))
/*      */         {
/* 1327 */           list.add(axisalignedbb);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1332 */     return list;
/*      */   }
/*      */   
/*      */   public boolean isInsideBorder(WorldBorder worldBorderIn, Entity entityIn)
/*      */   {
/* 1337 */     double d0 = worldBorderIn.minX();
/* 1338 */     double d1 = worldBorderIn.minZ();
/* 1339 */     double d2 = worldBorderIn.maxX();
/* 1340 */     double d3 = worldBorderIn.maxZ();
/*      */     
/* 1342 */     if (entityIn.isOutsideBorder())
/*      */     {
/* 1344 */       d0 += 1.0D;
/* 1345 */       d1 += 1.0D;
/* 1346 */       d2 -= 1.0D;
/* 1347 */       d3 -= 1.0D;
/*      */     }
/*      */     else
/*      */     {
/* 1351 */       d0 -= 1.0D;
/* 1352 */       d1 -= 1.0D;
/* 1353 */       d2 += 1.0D;
/* 1354 */       d3 += 1.0D;
/*      */     }
/*      */     
/* 1357 */     return (entityIn.posX > d0) && (entityIn.posX < d2) && (entityIn.posZ > d1) && (entityIn.posZ < d3);
/*      */   }
/*      */   
/*      */   public List<AxisAlignedBB> func_147461_a(AxisAlignedBB bb)
/*      */   {
/* 1362 */     List<AxisAlignedBB> list = Lists.newArrayList();
/* 1363 */     int i = MathHelper.floor_double(bb.minX);
/* 1364 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1365 */     int k = MathHelper.floor_double(bb.minY);
/* 1366 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1367 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1368 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 1369 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1371 */     for (int k1 = i; k1 < j; k1++)
/*      */     {
/* 1373 */       for (int l1 = i1; l1 < j1; l1++)
/*      */       {
/* 1375 */         if (isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k1, 64, l1)))
/*      */         {
/* 1377 */           for (int i2 = k - 1; i2 < l; i2++)
/*      */           {
/* 1379 */             blockpos$mutableblockpos.func_181079_c(k1, i2, l1);
/*      */             IBlockState iblockstate;
/*      */             IBlockState iblockstate;
/* 1382 */             if ((k1 >= -30000000) && (k1 < 30000000) && (l1 >= -30000000) && (l1 < 30000000))
/*      */             {
/* 1384 */               iblockstate = getBlockState(blockpos$mutableblockpos);
/*      */             }
/*      */             else
/*      */             {
/* 1388 */               iblockstate = Blocks.bedrock.getDefaultState();
/*      */             }
/*      */             
/* 1391 */             iblockstate.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate, bb, list, null);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1397 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int calculateSkylightSubtracted(float p_72967_1_)
/*      */   {
/* 1405 */     float f = getCelestialAngle(p_72967_1_);
/* 1406 */     float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F);
/* 1407 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1408 */     f1 = 1.0F - f1;
/* 1409 */     f1 = (float)(f1 * (1.0D - getRainStrength(p_72967_1_) * 5.0F / 16.0D));
/* 1410 */     f1 = (float)(f1 * (1.0D - getThunderStrength(p_72967_1_) * 5.0F / 16.0D));
/* 1411 */     f1 = 1.0F - f1;
/* 1412 */     return (int)(f1 * 11.0F);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getSunBrightness(float p_72971_1_)
/*      */   {
/* 1420 */     float f = getCelestialAngle(p_72971_1_);
/* 1421 */     float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.2F);
/* 1422 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1423 */     f1 = 1.0F - f1;
/* 1424 */     f1 = (float)(f1 * (1.0D - getRainStrength(p_72971_1_) * 5.0F / 16.0D));
/* 1425 */     f1 = (float)(f1 * (1.0D - getThunderStrength(p_72971_1_) * 5.0F / 16.0D));
/* 1426 */     return f1 * 0.8F + 0.2F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getSkyColor(Entity entityIn, float partialTicks)
/*      */   {
/* 1434 */     float f = getCelestialAngle(partialTicks);
/* 1435 */     float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 1436 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1437 */     int i = MathHelper.floor_double(entityIn.posX);
/* 1438 */     int j = MathHelper.floor_double(entityIn.posY);
/* 1439 */     int k = MathHelper.floor_double(entityIn.posZ);
/* 1440 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 1441 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(blockpos);
/* 1442 */     float f2 = biomegenbase.getFloatTemperature(blockpos);
/* 1443 */     int l = biomegenbase.getSkyColorByTemp(f2);
/* 1444 */     float f3 = (l >> 16 & 0xFF) / 255.0F;
/* 1445 */     float f4 = (l >> 8 & 0xFF) / 255.0F;
/* 1446 */     float f5 = (l & 0xFF) / 255.0F;
/* 1447 */     f3 *= f1;
/* 1448 */     f4 *= f1;
/* 1449 */     f5 *= f1;
/* 1450 */     float f6 = getRainStrength(partialTicks);
/*      */     
/* 1452 */     if (f6 > 0.0F)
/*      */     {
/* 1454 */       float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
/* 1455 */       float f8 = 1.0F - f6 * 0.75F;
/* 1456 */       f3 = f3 * f8 + f7 * (1.0F - f8);
/* 1457 */       f4 = f4 * f8 + f7 * (1.0F - f8);
/* 1458 */       f5 = f5 * f8 + f7 * (1.0F - f8);
/*      */     }
/*      */     
/* 1461 */     float f10 = getThunderStrength(partialTicks);
/*      */     
/* 1463 */     if (f10 > 0.0F)
/*      */     {
/* 1465 */       float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
/* 1466 */       float f9 = 1.0F - f10 * 0.75F;
/* 1467 */       f3 = f3 * f9 + f11 * (1.0F - f9);
/* 1468 */       f4 = f4 * f9 + f11 * (1.0F - f9);
/* 1469 */       f5 = f5 * f9 + f11 * (1.0F - f9);
/*      */     }
/*      */     
/* 1472 */     if (this.lastLightningBolt > 0)
/*      */     {
/* 1474 */       float f12 = this.lastLightningBolt - partialTicks;
/*      */       
/* 1476 */       if (f12 > 1.0F)
/*      */       {
/* 1478 */         f12 = 1.0F;
/*      */       }
/*      */       
/* 1481 */       f12 *= 0.45F;
/* 1482 */       f3 = f3 * (1.0F - f12) + 0.8F * f12;
/* 1483 */       f4 = f4 * (1.0F - f12) + 0.8F * f12;
/* 1484 */       f5 = f5 * (1.0F - f12) + 1.0F * f12;
/*      */     }
/*      */     
/* 1487 */     return new Vec3(f3, f4, f5);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getCelestialAngle(float partialTicks)
/*      */   {
/* 1495 */     return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
/*      */   }
/*      */   
/*      */   public int getMoonPhase()
/*      */   {
/* 1500 */     return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getCurrentMoonPhaseFactor()
/*      */   {
/* 1508 */     return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getCelestialAngleRadians(float partialTicks)
/*      */   {
/* 1516 */     float f = getCelestialAngle(partialTicks);
/* 1517 */     return f * 3.1415927F * 2.0F;
/*      */   }
/*      */   
/*      */   public Vec3 getCloudColour(float partialTicks)
/*      */   {
/* 1522 */     float f = getCelestialAngle(partialTicks);
/* 1523 */     float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 1524 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1525 */     float f2 = (float)(this.cloudColour >> 16 & 0xFF) / 255.0F;
/* 1526 */     float f3 = (float)(this.cloudColour >> 8 & 0xFF) / 255.0F;
/* 1527 */     float f4 = (float)(this.cloudColour & 0xFF) / 255.0F;
/* 1528 */     float f5 = getRainStrength(partialTicks);
/*      */     
/* 1530 */     if (f5 > 0.0F)
/*      */     {
/* 1532 */       float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
/* 1533 */       float f7 = 1.0F - f5 * 0.95F;
/* 1534 */       f2 = f2 * f7 + f6 * (1.0F - f7);
/* 1535 */       f3 = f3 * f7 + f6 * (1.0F - f7);
/* 1536 */       f4 = f4 * f7 + f6 * (1.0F - f7);
/*      */     }
/*      */     
/* 1539 */     f2 *= (f1 * 0.9F + 0.1F);
/* 1540 */     f3 *= (f1 * 0.9F + 0.1F);
/* 1541 */     f4 *= (f1 * 0.85F + 0.15F);
/* 1542 */     float f9 = getThunderStrength(partialTicks);
/*      */     
/* 1544 */     if (f9 > 0.0F)
/*      */     {
/* 1546 */       float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
/* 1547 */       float f8 = 1.0F - f9 * 0.95F;
/* 1548 */       f2 = f2 * f8 + f10 * (1.0F - f8);
/* 1549 */       f3 = f3 * f8 + f10 * (1.0F - f8);
/* 1550 */       f4 = f4 * f8 + f10 * (1.0F - f8);
/*      */     }
/*      */     
/* 1553 */     return new Vec3(f2, f3, f4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Vec3 getFogColor(float partialTicks)
/*      */   {
/* 1561 */     float f = getCelestialAngle(partialTicks);
/* 1562 */     return this.provider.getFogColor(f, partialTicks);
/*      */   }
/*      */   
/*      */   public BlockPos getPrecipitationHeight(BlockPos pos)
/*      */   {
/* 1567 */     return getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public BlockPos getTopSolidOrLiquidBlock(BlockPos pos)
/*      */   {
/* 1575 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*      */     
/*      */     BlockPos blockpos1;
/*      */     
/* 1579 */     for (BlockPos blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1)
/*      */     {
/* 1581 */       blockpos1 = blockpos.down();
/* 1582 */       Material material = chunk.getBlock(blockpos1).getMaterial();
/*      */       
/* 1584 */       if ((material.blocksMovement()) && (material != Material.leaves)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1590 */     return blockpos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getStarBrightness(float partialTicks)
/*      */   {
/* 1598 */     float f = getCelestialAngle(partialTicks);
/* 1599 */     float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.25F);
/* 1600 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1601 */     return f1 * f1 * 0.5F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateEntities()
/*      */   {
/* 1621 */     this.theProfiler.startSection("entities");
/* 1622 */     this.theProfiler.startSection("global");
/*      */     
/* 1624 */     for (int i = 0; i < this.weatherEffects.size(); i++)
/*      */     {
/* 1626 */       Entity entity = (Entity)this.weatherEffects.get(i);
/*      */       
/*      */       try
/*      */       {
/* 1630 */         entity.ticksExisted += 1;
/* 1631 */         entity.onUpdate();
/*      */       }
/*      */       catch (Throwable throwable2)
/*      */       {
/* 1635 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
/* 1636 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");
/*      */         
/* 1638 */         if (entity == null)
/*      */         {
/* 1640 */           crashreportcategory.addCrashSection("Entity", "~~NULL~~");
/*      */         }
/*      */         else
/*      */         {
/* 1644 */           entity.addEntityCrashInfo(crashreportcategory);
/*      */         }
/*      */         
/* 1647 */         throw new ReportedException(crashreport);
/*      */       }
/*      */       
/* 1650 */       if (entity.isDead)
/*      */       {
/* 1652 */         this.weatherEffects.remove(i--);
/*      */       }
/*      */     }
/*      */     
/* 1656 */     this.theProfiler.endStartSection("remove");
/* 1657 */     this.loadedEntityList.removeAll(this.unloadedEntityList);
/*      */     
/* 1659 */     for (int k = 0; k < this.unloadedEntityList.size(); k++)
/*      */     {
/* 1661 */       Entity entity1 = (Entity)this.unloadedEntityList.get(k);
/* 1662 */       int j = entity1.chunkCoordX;
/* 1663 */       int l1 = entity1.chunkCoordZ;
/*      */       
/* 1665 */       if ((entity1.addedToChunk) && (isChunkLoaded(j, l1, true)))
/*      */       {
/* 1667 */         getChunkFromChunkCoords(j, l1).removeEntity(entity1);
/*      */       }
/*      */     }
/*      */     
/* 1671 */     for (int l = 0; l < this.unloadedEntityList.size(); l++)
/*      */     {
/* 1673 */       onEntityRemoved((Entity)this.unloadedEntityList.get(l));
/*      */     }
/*      */     
/* 1676 */     this.unloadedEntityList.clear();
/* 1677 */     this.theProfiler.endStartSection("regular");
/*      */     
/* 1679 */     for (int i1 = 0; i1 < this.loadedEntityList.size(); i1++)
/*      */     {
/* 1681 */       Entity entity2 = (Entity)this.loadedEntityList.get(i1);
/*      */       
/* 1683 */       if (entity2.ridingEntity != null)
/*      */       {
/* 1685 */         if ((entity2.ridingEntity.isDead) || (entity2.ridingEntity.riddenByEntity != entity2))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1690 */           entity2.ridingEntity.riddenByEntity = null;
/* 1691 */           entity2.ridingEntity = null;
/*      */         }
/*      */       } else {
/* 1694 */         this.theProfiler.startSection("tick");
/*      */         
/* 1696 */         if (!entity2.isDead)
/*      */         {
/*      */           try
/*      */           {
/* 1700 */             updateEntity(entity2);
/*      */           }
/*      */           catch (Throwable throwable1)
/*      */           {
/* 1704 */             CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
/* 1705 */             CrashReportCategory crashreportcategory2 = crashreport1.makeCategory("Entity being ticked");
/* 1706 */             entity2.addEntityCrashInfo(crashreportcategory2);
/* 1707 */             throw new ReportedException(crashreport1);
/*      */           }
/*      */         }
/*      */         
/* 1711 */         this.theProfiler.endSection();
/* 1712 */         this.theProfiler.startSection("remove");
/*      */         
/* 1714 */         if (entity2.isDead)
/*      */         {
/* 1716 */           int k1 = entity2.chunkCoordX;
/* 1717 */           int i2 = entity2.chunkCoordZ;
/*      */           
/* 1719 */           if ((entity2.addedToChunk) && (isChunkLoaded(k1, i2, true)))
/*      */           {
/* 1721 */             getChunkFromChunkCoords(k1, i2).removeEntity(entity2);
/*      */           }
/*      */           
/* 1724 */           this.loadedEntityList.remove(i1--);
/* 1725 */           onEntityRemoved(entity2);
/*      */         }
/*      */         
/* 1728 */         this.theProfiler.endSection();
/*      */       }
/*      */     }
/* 1731 */     this.theProfiler.endStartSection("blockEntities");
/* 1732 */     this.processingLoadedTiles = true;
/* 1733 */     Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();
/*      */     
/* 1735 */     while (iterator.hasNext())
/*      */     {
/* 1737 */       TileEntity tileentity = (TileEntity)iterator.next();
/*      */       
/* 1739 */       if ((!tileentity.isInvalid()) && (tileentity.hasWorldObj()))
/*      */       {
/* 1741 */         BlockPos blockpos = tileentity.getPos();
/*      */         
/* 1743 */         if ((isBlockLoaded(blockpos)) && (this.worldBorder.contains(blockpos)))
/*      */         {
/*      */           try
/*      */           {
/* 1747 */             ((ITickable)tileentity).update();
/*      */           }
/*      */           catch (Throwable throwable)
/*      */           {
/* 1751 */             CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
/* 1752 */             CrashReportCategory crashreportcategory1 = crashreport2.makeCategory("Block entity being ticked");
/* 1753 */             tileentity.addInfoToCrashReport(crashreportcategory1);
/* 1754 */             throw new ReportedException(crashreport2);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1759 */       if (tileentity.isInvalid())
/*      */       {
/* 1761 */         iterator.remove();
/* 1762 */         this.loadedTileEntityList.remove(tileentity);
/*      */         
/* 1764 */         if (isBlockLoaded(tileentity.getPos()))
/*      */         {
/* 1766 */           getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1771 */     this.processingLoadedTiles = false;
/*      */     
/* 1773 */     if (!this.tileEntitiesToBeRemoved.isEmpty())
/*      */     {
/* 1775 */       this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
/* 1776 */       this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
/* 1777 */       this.tileEntitiesToBeRemoved.clear();
/*      */     }
/*      */     
/* 1780 */     this.theProfiler.endStartSection("pendingBlockEntities");
/*      */     
/* 1782 */     if (!this.addedTileEntityList.isEmpty())
/*      */     {
/* 1784 */       for (int j1 = 0; j1 < this.addedTileEntityList.size(); j1++)
/*      */       {
/* 1786 */         TileEntity tileentity1 = (TileEntity)this.addedTileEntityList.get(j1);
/*      */         
/* 1788 */         if (!tileentity1.isInvalid())
/*      */         {
/* 1790 */           if (!this.loadedTileEntityList.contains(tileentity1))
/*      */           {
/* 1792 */             addTileEntity(tileentity1);
/*      */           }
/*      */           
/* 1795 */           if (isBlockLoaded(tileentity1.getPos()))
/*      */           {
/* 1797 */             getChunkFromBlockCoords(tileentity1.getPos()).addTileEntity(tileentity1.getPos(), tileentity1);
/*      */           }
/*      */           
/* 1800 */           markBlockForUpdate(tileentity1.getPos());
/*      */         }
/*      */       }
/*      */       
/* 1804 */       this.addedTileEntityList.clear();
/*      */     }
/*      */     
/* 1807 */     this.theProfiler.endSection();
/* 1808 */     this.theProfiler.endSection();
/*      */   }
/*      */   
/*      */   public boolean addTileEntity(TileEntity tile)
/*      */   {
/* 1813 */     boolean flag = this.loadedTileEntityList.add(tile);
/*      */     
/* 1815 */     if ((flag) && ((tile instanceof ITickable)))
/*      */     {
/* 1817 */       this.tickableTileEntities.add(tile);
/*      */     }
/*      */     
/* 1820 */     return flag;
/*      */   }
/*      */   
/*      */   public void addTileEntities(Collection<TileEntity> tileEntityCollection)
/*      */   {
/* 1825 */     if (this.processingLoadedTiles)
/*      */     {
/* 1827 */       this.addedTileEntityList.addAll(tileEntityCollection);
/*      */     }
/*      */     else
/*      */     {
/* 1831 */       for (TileEntity tileentity : tileEntityCollection)
/*      */       {
/* 1833 */         this.loadedTileEntityList.add(tileentity);
/*      */         
/* 1835 */         if ((tileentity instanceof ITickable))
/*      */         {
/* 1837 */           this.tickableTileEntities.add(tileentity);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateEntity(Entity ent)
/*      */   {
/* 1848 */     updateEntityWithOptionalForce(ent, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate)
/*      */   {
/* 1857 */     int i = MathHelper.floor_double(entityIn.posX);
/* 1858 */     int j = MathHelper.floor_double(entityIn.posZ);
/* 1859 */     int k = 32;
/*      */     
/* 1861 */     if ((!forceUpdate) || (isAreaLoaded(i - k, 0, j - k, i + k, 0, j + k, true)))
/*      */     {
/* 1863 */       entityIn.lastTickPosX = entityIn.posX;
/* 1864 */       entityIn.lastTickPosY = entityIn.posY;
/* 1865 */       entityIn.lastTickPosZ = entityIn.posZ;
/* 1866 */       entityIn.prevRotationYaw = entityIn.rotationYaw;
/* 1867 */       entityIn.prevRotationPitch = entityIn.rotationPitch;
/*      */       
/* 1869 */       if ((forceUpdate) && (entityIn.addedToChunk))
/*      */       {
/* 1871 */         entityIn.ticksExisted += 1;
/*      */         
/* 1873 */         if (entityIn.ridingEntity != null)
/*      */         {
/* 1875 */           entityIn.updateRidden();
/*      */         }
/*      */         else
/*      */         {
/* 1879 */           entityIn.onUpdate();
/*      */         }
/*      */       }
/*      */       
/* 1883 */       this.theProfiler.startSection("chunkCheck");
/*      */       
/* 1885 */       if ((Double.isNaN(entityIn.posX)) || (Double.isInfinite(entityIn.posX)))
/*      */       {
/* 1887 */         entityIn.posX = entityIn.lastTickPosX;
/*      */       }
/*      */       
/* 1890 */       if ((Double.isNaN(entityIn.posY)) || (Double.isInfinite(entityIn.posY)))
/*      */       {
/* 1892 */         entityIn.posY = entityIn.lastTickPosY;
/*      */       }
/*      */       
/* 1895 */       if ((Double.isNaN(entityIn.posZ)) || (Double.isInfinite(entityIn.posZ)))
/*      */       {
/* 1897 */         entityIn.posZ = entityIn.lastTickPosZ;
/*      */       }
/*      */       
/* 1900 */       if ((Double.isNaN(entityIn.rotationPitch)) || (Double.isInfinite(entityIn.rotationPitch)))
/*      */       {
/* 1902 */         entityIn.rotationPitch = entityIn.prevRotationPitch;
/*      */       }
/*      */       
/* 1905 */       if ((Double.isNaN(entityIn.rotationYaw)) || (Double.isInfinite(entityIn.rotationYaw)))
/*      */       {
/* 1907 */         entityIn.rotationYaw = entityIn.prevRotationYaw;
/*      */       }
/*      */       
/* 1910 */       int l = MathHelper.floor_double(entityIn.posX / 16.0D);
/* 1911 */       int i1 = MathHelper.floor_double(entityIn.posY / 16.0D);
/* 1912 */       int j1 = MathHelper.floor_double(entityIn.posZ / 16.0D);
/*      */       
/* 1914 */       if ((!entityIn.addedToChunk) || (entityIn.chunkCoordX != l) || (entityIn.chunkCoordY != i1) || (entityIn.chunkCoordZ != j1))
/*      */       {
/* 1916 */         if ((entityIn.addedToChunk) && (isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true)))
/*      */         {
/* 1918 */           getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
/*      */         }
/*      */         
/* 1921 */         if (isChunkLoaded(l, j1, true))
/*      */         {
/* 1923 */           entityIn.addedToChunk = true;
/* 1924 */           getChunkFromChunkCoords(l, j1).addEntity(entityIn);
/*      */         }
/*      */         else
/*      */         {
/* 1928 */           entityIn.addedToChunk = false;
/*      */         }
/*      */       }
/*      */       
/* 1932 */       this.theProfiler.endSection();
/*      */       
/* 1934 */       if ((forceUpdate) && (entityIn.addedToChunk) && (entityIn.riddenByEntity != null))
/*      */       {
/* 1936 */         if ((!entityIn.riddenByEntity.isDead) && (entityIn.riddenByEntity.ridingEntity == entityIn))
/*      */         {
/* 1938 */           updateEntity(entityIn.riddenByEntity);
/*      */         }
/*      */         else
/*      */         {
/* 1942 */           entityIn.riddenByEntity.ridingEntity = null;
/* 1943 */           entityIn.riddenByEntity = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkNoEntityCollision(AxisAlignedBB bb)
/*      */   {
/* 1954 */     return checkNoEntityCollision(bb, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkNoEntityCollision(AxisAlignedBB bb, Entity entityIn)
/*      */   {
/* 1962 */     List<Entity> list = getEntitiesWithinAABBExcludingEntity(null, bb);
/*      */     
/* 1964 */     for (int i = 0; i < list.size(); i++)
/*      */     {
/* 1966 */       Entity entity = (Entity)list.get(i);
/*      */       
/* 1968 */       if ((!entity.isDead) && (entity.preventEntitySpawning) && (entity != entityIn) && ((entityIn == null) || ((entityIn.ridingEntity != entity) && (entityIn.riddenByEntity != entity))))
/*      */       {
/* 1970 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 1974 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean checkBlockCollision(AxisAlignedBB bb)
/*      */   {
/* 1982 */     int i = MathHelper.floor_double(bb.minX);
/* 1983 */     int j = MathHelper.floor_double(bb.maxX);
/* 1984 */     int k = MathHelper.floor_double(bb.minY);
/* 1985 */     int l = MathHelper.floor_double(bb.maxY);
/* 1986 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1987 */     int j1 = MathHelper.floor_double(bb.maxZ);
/* 1988 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1990 */     for (int k1 = i; k1 <= j; k1++)
/*      */     {
/* 1992 */       for (int l1 = k; l1 <= l; l1++)
/*      */       {
/* 1994 */         for (int i2 = i1; i2 <= j1; i2++)
/*      */         {
/* 1996 */           Block block = getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
/*      */           
/* 1998 */           if (block.getMaterial() != Material.air)
/*      */           {
/* 2000 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2006 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyLiquid(AxisAlignedBB bb)
/*      */   {
/* 2014 */     int i = MathHelper.floor_double(bb.minX);
/* 2015 */     int j = MathHelper.floor_double(bb.maxX);
/* 2016 */     int k = MathHelper.floor_double(bb.minY);
/* 2017 */     int l = MathHelper.floor_double(bb.maxY);
/* 2018 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 2019 */     int j1 = MathHelper.floor_double(bb.maxZ);
/* 2020 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 2022 */     for (int k1 = i; k1 <= j; k1++)
/*      */     {
/* 2024 */       for (int l1 = k; l1 <= l; l1++)
/*      */       {
/* 2026 */         for (int i2 = i1; i2 <= j1; i2++)
/*      */         {
/* 2028 */           Block block = getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
/*      */           
/* 2030 */           if (block.getMaterial().isLiquid())
/*      */           {
/* 2032 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2038 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isFlammableWithin(AxisAlignedBB bb)
/*      */   {
/* 2043 */     int i = MathHelper.floor_double(bb.minX);
/* 2044 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 2045 */     int k = MathHelper.floor_double(bb.minY);
/* 2046 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 2047 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 2048 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/*      */     
/* 2050 */     if (isAreaLoaded(i, k, i1, j, l, j1, true))
/*      */     {
/* 2052 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */       
/* 2054 */       for (int k1 = i; k1 < j; k1++)
/*      */       {
/* 2056 */         for (int l1 = k; l1 < l; l1++)
/*      */         {
/* 2058 */           for (int i2 = i1; i2 < j1; i2++)
/*      */           {
/* 2060 */             Block block = getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
/*      */             
/* 2062 */             if ((block == Blocks.fire) || (block == Blocks.flowing_lava) || (block == Blocks.lava))
/*      */             {
/* 2064 */               return true;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2071 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn)
/*      */   {
/* 2079 */     int i = MathHelper.floor_double(bb.minX);
/* 2080 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 2081 */     int k = MathHelper.floor_double(bb.minY);
/* 2082 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 2083 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 2084 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/*      */     
/* 2086 */     if (!isAreaLoaded(i, k, i1, j, l, j1, true))
/*      */     {
/* 2088 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2092 */     boolean flag = false;
/* 2093 */     Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);
/* 2094 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 2096 */     for (int k1 = i; k1 < j; k1++)
/*      */     {
/* 2098 */       for (int l1 = k; l1 < l; l1++)
/*      */       {
/* 2100 */         for (int i2 = i1; i2 < j1; i2++)
/*      */         {
/* 2102 */           blockpos$mutableblockpos.func_181079_c(k1, l1, i2);
/* 2103 */           IBlockState iblockstate = getBlockState(blockpos$mutableblockpos);
/* 2104 */           Block block = iblockstate.getBlock();
/*      */           
/* 2106 */           if (block.getMaterial() == materialIn)
/*      */           {
/* 2108 */             double d0 = l1 + 1 - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue());
/*      */             
/* 2110 */             if (l >= d0)
/*      */             {
/* 2112 */               flag = true;
/* 2113 */               vec3 = block.modifyAcceleration(this, blockpos$mutableblockpos, entityIn, vec3);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2120 */     if ((vec3.lengthVector() > 0.0D) && (entityIn.isPushedByWater()))
/*      */     {
/* 2122 */       vec3 = vec3.normalize();
/* 2123 */       double d1 = 0.014D;
/* 2124 */       entityIn.motionX += vec3.xCoord * d1;
/* 2125 */       entityIn.motionY += vec3.yCoord * d1;
/* 2126 */       entityIn.motionZ += vec3.zCoord * d1;
/*      */     }
/*      */     
/* 2129 */     return flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn)
/*      */   {
/* 2138 */     int i = MathHelper.floor_double(bb.minX);
/* 2139 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 2140 */     int k = MathHelper.floor_double(bb.minY);
/* 2141 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 2142 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 2143 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 2144 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 2146 */     for (int k1 = i; k1 < j; k1++)
/*      */     {
/* 2148 */       for (int l1 = k; l1 < l; l1++)
/*      */       {
/* 2150 */         for (int i2 = i1; i2 < j1; i2++)
/*      */         {
/* 2152 */           if (getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock().getMaterial() == materialIn)
/*      */           {
/* 2154 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2160 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAABBInMaterial(AxisAlignedBB bb, Material materialIn)
/*      */   {
/* 2168 */     int i = MathHelper.floor_double(bb.minX);
/* 2169 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 2170 */     int k = MathHelper.floor_double(bb.minY);
/* 2171 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 2172 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 2173 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 2174 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 2176 */     for (int k1 = i; k1 < j; k1++)
/*      */     {
/* 2178 */       for (int l1 = k; l1 < l; l1++)
/*      */       {
/* 2180 */         for (int i2 = i1; i2 < j1; i2++)
/*      */         {
/* 2182 */           IBlockState iblockstate = getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2));
/* 2183 */           Block block = iblockstate.getBlock();
/*      */           
/* 2185 */           if (block.getMaterial() == materialIn)
/*      */           {
/* 2187 */             int j2 = ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue();
/* 2188 */             double d0 = l1 + 1;
/*      */             
/* 2190 */             if (j2 < 8)
/*      */             {
/* 2192 */               d0 = l1 + 1 - j2 / 8.0D;
/*      */             }
/*      */             
/* 2195 */             if (d0 >= bb.minY)
/*      */             {
/* 2197 */               return true;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2204 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Explosion createExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isSmoking)
/*      */   {
/* 2212 */     return newExplosion(entityIn, x, y, z, strength, false, isSmoking);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Explosion newExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
/*      */   {
/* 2220 */     Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
/* 2221 */     explosion.doExplosionA();
/* 2222 */     explosion.doExplosionB(true);
/* 2223 */     return explosion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getBlockDensity(Vec3 vec, AxisAlignedBB bb)
/*      */   {
/* 2231 */     double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
/* 2232 */     double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
/* 2233 */     double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
/* 2234 */     double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
/* 2235 */     double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
/*      */     
/* 2237 */     if ((d0 >= 0.0D) && (d1 >= 0.0D) && (d2 >= 0.0D))
/*      */     {
/* 2239 */       int i = 0;
/* 2240 */       int j = 0;
/*      */       
/* 2242 */       for (float f = 0.0F; f <= 1.0F; f = (float)(f + d0))
/*      */       {
/* 2244 */         for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)(f1 + d1))
/*      */         {
/* 2246 */           for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)(f2 + d2))
/*      */           {
/* 2248 */             double d5 = bb.minX + (bb.maxX - bb.minX) * f;
/* 2249 */             double d6 = bb.minY + (bb.maxY - bb.minY) * f1;
/* 2250 */             double d7 = bb.minZ + (bb.maxZ - bb.minZ) * f2;
/*      */             
/* 2252 */             if (rayTraceBlocks(new Vec3(d5 + d3, d6, d7 + d4), vec) == null)
/*      */             {
/* 2254 */               i++;
/*      */             }
/*      */             
/* 2257 */             j++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2262 */       return i / j;
/*      */     }
/*      */     
/*      */ 
/* 2266 */     return 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean extinguishFire(EntityPlayer player, BlockPos pos, EnumFacing side)
/*      */   {
/* 2275 */     pos = pos.offset(side);
/*      */     
/* 2277 */     if (getBlockState(pos).getBlock() == Blocks.fire)
/*      */     {
/* 2279 */       playAuxSFXAtEntity(player, 1004, pos, 0);
/* 2280 */       setBlockToAir(pos);
/* 2281 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2285 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDebugLoadedEntities()
/*      */   {
/* 2294 */     return "All: " + this.loadedEntityList.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProviderName()
/*      */   {
/* 2302 */     return this.chunkProvider.makeString();
/*      */   }
/*      */   
/*      */   public TileEntity getTileEntity(BlockPos pos)
/*      */   {
/* 2307 */     if (!isValid(pos))
/*      */     {
/* 2309 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 2313 */     TileEntity tileentity = null;
/*      */     
/* 2315 */     if (this.processingLoadedTiles)
/*      */     {
/* 2317 */       for (int i = 0; i < this.addedTileEntityList.size(); i++)
/*      */       {
/* 2319 */         TileEntity tileentity1 = (TileEntity)this.addedTileEntityList.get(i);
/*      */         
/* 2321 */         if ((!tileentity1.isInvalid()) && (tileentity1.getPos().equals(pos)))
/*      */         {
/* 2323 */           tileentity = tileentity1;
/* 2324 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2329 */     if (tileentity == null)
/*      */     {
/* 2331 */       tileentity = getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
/*      */     }
/*      */     
/* 2334 */     if (tileentity == null)
/*      */     {
/* 2336 */       for (int j = 0; j < this.addedTileEntityList.size(); j++)
/*      */       {
/* 2338 */         TileEntity tileentity2 = (TileEntity)this.addedTileEntityList.get(j);
/*      */         
/* 2340 */         if ((!tileentity2.isInvalid()) && (tileentity2.getPos().equals(pos)))
/*      */         {
/* 2342 */           tileentity = tileentity2;
/* 2343 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2348 */     return tileentity;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setTileEntity(BlockPos pos, TileEntity tileEntityIn)
/*      */   {
/* 2354 */     if ((tileEntityIn != null) && (!tileEntityIn.isInvalid()))
/*      */     {
/* 2356 */       if (this.processingLoadedTiles)
/*      */       {
/* 2358 */         tileEntityIn.setPos(pos);
/* 2359 */         Iterator<TileEntity> iterator = this.addedTileEntityList.iterator();
/*      */         
/* 2361 */         while (iterator.hasNext())
/*      */         {
/* 2363 */           TileEntity tileentity = (TileEntity)iterator.next();
/*      */           
/* 2365 */           if (tileentity.getPos().equals(pos))
/*      */           {
/* 2367 */             tileentity.invalidate();
/* 2368 */             iterator.remove();
/*      */           }
/*      */         }
/*      */         
/* 2372 */         this.addedTileEntityList.add(tileEntityIn);
/*      */       }
/*      */       else
/*      */       {
/* 2376 */         addTileEntity(tileEntityIn);
/* 2377 */         getChunkFromBlockCoords(pos).addTileEntity(pos, tileEntityIn);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeTileEntity(BlockPos pos)
/*      */   {
/* 2384 */     TileEntity tileentity = getTileEntity(pos);
/*      */     
/* 2386 */     if ((tileentity != null) && (this.processingLoadedTiles))
/*      */     {
/* 2388 */       tileentity.invalidate();
/* 2389 */       this.addedTileEntityList.remove(tileentity);
/*      */     }
/*      */     else
/*      */     {
/* 2393 */       if (tileentity != null)
/*      */       {
/* 2395 */         this.addedTileEntityList.remove(tileentity);
/* 2396 */         this.loadedTileEntityList.remove(tileentity);
/* 2397 */         this.tickableTileEntities.remove(tileentity);
/*      */       }
/*      */       
/* 2400 */       getChunkFromBlockCoords(pos).removeTileEntity(pos);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void markTileEntityForRemoval(TileEntity tileEntityIn)
/*      */   {
/* 2409 */     this.tileEntitiesToBeRemoved.add(tileEntityIn);
/*      */   }
/*      */   
/*      */   public boolean isBlockFullCube(BlockPos pos)
/*      */   {
/* 2414 */     IBlockState iblockstate = getBlockState(pos);
/* 2415 */     AxisAlignedBB axisalignedbb = iblockstate.getBlock().getCollisionBoundingBox(this, pos, iblockstate);
/* 2416 */     return (axisalignedbb != null) && (axisalignedbb.getAverageEdgeLength() >= 1.0D);
/*      */   }
/*      */   
/*      */   public static boolean doesBlockHaveSolidTopSurface(IBlockAccess blockAccess, BlockPos pos)
/*      */   {
/* 2421 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 2422 */     Block block = iblockstate.getBlock();
/* 2423 */     return (block.getMaterial().isOpaque()) && (block.isFullCube());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBlockNormalCube(BlockPos pos, boolean _default)
/*      */   {
/* 2431 */     if (!isValid(pos))
/*      */     {
/* 2433 */       return _default;
/*      */     }
/*      */     
/*      */ 
/* 2437 */     Chunk chunk = this.chunkProvider.provideChunk(pos);
/*      */     
/* 2439 */     if (chunk.isEmpty())
/*      */     {
/* 2441 */       return _default;
/*      */     }
/*      */     
/*      */ 
/* 2445 */     Block block = getBlockState(pos).getBlock();
/* 2446 */     return (block.getMaterial().isOpaque()) && (block.isFullCube());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void calculateInitialSkylight()
/*      */   {
/* 2456 */     int i = calculateSkylightSubtracted(1.0F);
/*      */     
/* 2458 */     if (i != this.skylightSubtracted)
/*      */     {
/* 2460 */       this.skylightSubtracted = i;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowedSpawnTypes(boolean hostile, boolean peaceful)
/*      */   {
/* 2469 */     this.spawnHostileMobs = hostile;
/* 2470 */     this.spawnPeacefulMobs = peaceful;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void tick()
/*      */   {
/* 2478 */     updateWeather();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void calculateInitialWeather()
/*      */   {
/* 2486 */     if (this.worldInfo.isRaining())
/*      */     {
/* 2488 */       this.rainingStrength = 1.0F;
/*      */       
/* 2490 */       if (this.worldInfo.isThundering())
/*      */       {
/* 2492 */         this.thunderingStrength = 1.0F;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateWeather()
/*      */   {
/* 2502 */     if (!this.provider.getHasNoSky())
/*      */     {
/* 2504 */       if (!this.isRemote)
/*      */       {
/* 2506 */         int i = this.worldInfo.getCleanWeatherTime();
/*      */         
/* 2508 */         if (i > 0)
/*      */         {
/* 2510 */           i--;
/* 2511 */           this.worldInfo.setCleanWeatherTime(i);
/* 2512 */           this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
/* 2513 */           this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
/*      */         }
/*      */         
/* 2516 */         int j = this.worldInfo.getThunderTime();
/*      */         
/* 2518 */         if (j <= 0)
/*      */         {
/* 2520 */           if (this.worldInfo.isThundering())
/*      */           {
/* 2522 */             this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
/*      */           }
/*      */           else
/*      */           {
/* 2526 */             this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2531 */           j--;
/* 2532 */           this.worldInfo.setThunderTime(j);
/*      */           
/* 2534 */           if (j <= 0)
/*      */           {
/* 2536 */             this.worldInfo.setThundering(!this.worldInfo.isThundering());
/*      */           }
/*      */         }
/*      */         
/* 2540 */         this.prevThunderingStrength = this.thunderingStrength;
/*      */         
/* 2542 */         if (this.worldInfo.isThundering())
/*      */         {
/* 2544 */           this.thunderingStrength = ((float)(this.thunderingStrength + 0.01D));
/*      */         }
/*      */         else
/*      */         {
/* 2548 */           this.thunderingStrength = ((float)(this.thunderingStrength - 0.01D));
/*      */         }
/*      */         
/* 2551 */         this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0F, 1.0F);
/* 2552 */         int k = this.worldInfo.getRainTime();
/*      */         
/* 2554 */         if (k <= 0)
/*      */         {
/* 2556 */           if (this.worldInfo.isRaining())
/*      */           {
/* 2558 */             this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
/*      */           }
/*      */           else
/*      */           {
/* 2562 */             this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2567 */           k--;
/* 2568 */           this.worldInfo.setRainTime(k);
/*      */           
/* 2570 */           if (k <= 0)
/*      */           {
/* 2572 */             this.worldInfo.setRaining(!this.worldInfo.isRaining());
/*      */           }
/*      */         }
/*      */         
/* 2576 */         this.prevRainingStrength = this.rainingStrength;
/*      */         
/* 2578 */         if (this.worldInfo.isRaining())
/*      */         {
/* 2580 */           this.rainingStrength = ((float)(this.rainingStrength + 0.01D));
/*      */         }
/*      */         else
/*      */         {
/* 2584 */           this.rainingStrength = ((float)(this.rainingStrength - 0.01D));
/*      */         }
/*      */         
/* 2587 */         this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0F, 1.0F);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setActivePlayerChunksAndCheckLight()
/*      */   {
/* 2594 */     this.activeChunkSet.clear();
/* 2595 */     this.theProfiler.startSection("buildList");
/*      */     
/* 2597 */     for (int i = 0; i < this.playerEntities.size(); i++)
/*      */     {
/* 2599 */       EntityPlayer entityplayer = (EntityPlayer)this.playerEntities.get(i);
/* 2600 */       int j = MathHelper.floor_double(entityplayer.posX / 16.0D);
/* 2601 */       int k = MathHelper.floor_double(entityplayer.posZ / 16.0D);
/* 2602 */       int l = getRenderDistanceChunks();
/*      */       
/* 2604 */       for (int i1 = -l; i1 <= l; i1++)
/*      */       {
/* 2606 */         for (int j1 = -l; j1 <= l; j1++)
/*      */         {
/* 2608 */           this.activeChunkSet.add(new ChunkCoordIntPair(i1 + j, j1 + k));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2613 */     this.theProfiler.endSection();
/*      */     
/* 2615 */     if (this.ambientTickCountdown > 0)
/*      */     {
/* 2617 */       this.ambientTickCountdown -= 1;
/*      */     }
/*      */     
/* 2620 */     this.theProfiler.startSection("playerCheckLight");
/*      */     
/* 2622 */     if (!this.playerEntities.isEmpty())
/*      */     {
/* 2624 */       int k1 = this.rand.nextInt(this.playerEntities.size());
/* 2625 */       EntityPlayer entityplayer1 = (EntityPlayer)this.playerEntities.get(k1);
/* 2626 */       int l1 = MathHelper.floor_double(entityplayer1.posX) + this.rand.nextInt(11) - 5;
/* 2627 */       int i2 = MathHelper.floor_double(entityplayer1.posY) + this.rand.nextInt(11) - 5;
/* 2628 */       int j2 = MathHelper.floor_double(entityplayer1.posZ) + this.rand.nextInt(11) - 5;
/* 2629 */       checkLight(new BlockPos(l1, i2, j2));
/*      */     }
/*      */     
/* 2632 */     this.theProfiler.endSection();
/*      */   }
/*      */   
/*      */   protected abstract int getRenderDistanceChunks();
/*      */   
/*      */   protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn)
/*      */   {
/* 2639 */     this.theProfiler.endStartSection("moodSound");
/*      */     
/* 2641 */     if ((this.ambientTickCountdown == 0) && (!this.isRemote))
/*      */     {
/* 2643 */       this.updateLCG = (this.updateLCG * 3 + 1013904223);
/* 2644 */       int i = this.updateLCG >> 2;
/* 2645 */       int j = i & 0xF;
/* 2646 */       int k = i >> 8 & 0xF;
/* 2647 */       int l = i >> 16 & 0xFF;
/* 2648 */       BlockPos blockpos = new BlockPos(j, l, k);
/* 2649 */       Block block = chunkIn.getBlock(blockpos);
/* 2650 */       j += p_147467_1_;
/* 2651 */       k += p_147467_2_;
/*      */       
/* 2653 */       if ((block.getMaterial() == Material.air) && (getLight(blockpos) <= this.rand.nextInt(8)) && (getLightFor(EnumSkyBlock.SKY, blockpos) <= 0))
/*      */       {
/* 2655 */         EntityPlayer entityplayer = getClosestPlayer(j + 0.5D, l + 0.5D, k + 0.5D, 8.0D);
/*      */         
/* 2657 */         if ((entityplayer != null) && (entityplayer.getDistanceSq(j + 0.5D, l + 0.5D, k + 0.5D) > 4.0D))
/*      */         {
/* 2659 */           playSoundEffect(j + 0.5D, l + 0.5D, k + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
/* 2660 */           this.ambientTickCountdown = (this.rand.nextInt(12000) + 6000);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2665 */     this.theProfiler.endStartSection("checkLight");
/* 2666 */     chunkIn.enqueueRelightChecks();
/*      */   }
/*      */   
/*      */   protected void updateBlocks()
/*      */   {
/* 2671 */     setActivePlayerChunksAndCheckLight();
/*      */   }
/*      */   
/*      */   public void forceBlockUpdateTick(Block blockType, BlockPos pos, Random random)
/*      */   {
/* 2676 */     this.scheduledUpdatesAreImmediate = true;
/* 2677 */     blockType.updateTick(this, pos, getBlockState(pos), random);
/* 2678 */     this.scheduledUpdatesAreImmediate = false;
/*      */   }
/*      */   
/*      */   public boolean canBlockFreezeWater(BlockPos pos)
/*      */   {
/* 2683 */     return canBlockFreeze(pos, false);
/*      */   }
/*      */   
/*      */   public boolean canBlockFreezeNoWater(BlockPos pos)
/*      */   {
/* 2688 */     return canBlockFreeze(pos, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj)
/*      */   {
/* 2696 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(pos);
/* 2697 */     float f = biomegenbase.getFloatTemperature(pos);
/*      */     
/* 2699 */     if (f > 0.15F)
/*      */     {
/* 2701 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2705 */     if ((pos.getY() >= 0) && (pos.getY() < 256) && (getLightFor(EnumSkyBlock.BLOCK, pos) < 10))
/*      */     {
/* 2707 */       IBlockState iblockstate = getBlockState(pos);
/* 2708 */       Block block = iblockstate.getBlock();
/*      */       
/* 2710 */       if (((block == Blocks.water) || (block == Blocks.flowing_water)) && (((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0))
/*      */       {
/* 2712 */         if (!noWaterAdj)
/*      */         {
/* 2714 */           return true;
/*      */         }
/*      */         
/* 2717 */         boolean flag = (isWater(pos.west())) && (isWater(pos.east())) && (isWater(pos.north())) && (isWater(pos.south()));
/*      */         
/* 2719 */         if (!flag)
/*      */         {
/* 2721 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2726 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean isWater(BlockPos pos)
/*      */   {
/* 2732 */     return getBlockState(pos).getBlock().getMaterial() == Material.water;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canSnowAt(BlockPos pos, boolean checkLight)
/*      */   {
/* 2740 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(pos);
/* 2741 */     float f = biomegenbase.getFloatTemperature(pos);
/*      */     
/* 2743 */     if (f > 0.15F)
/*      */     {
/* 2745 */       return false;
/*      */     }
/* 2747 */     if (!checkLight)
/*      */     {
/* 2749 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 2753 */     if ((pos.getY() >= 0) && (pos.getY() < 256) && (getLightFor(EnumSkyBlock.BLOCK, pos) < 10))
/*      */     {
/* 2755 */       Block block = getBlockState(pos).getBlock();
/*      */       
/* 2757 */       if ((block.getMaterial() == Material.air) && (Blocks.snow_layer.canPlaceBlockAt(this, pos)))
/*      */       {
/* 2759 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2763 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean checkLight(BlockPos pos)
/*      */   {
/* 2769 */     boolean flag = false;
/*      */     
/* 2771 */     if (!this.provider.getHasNoSky())
/*      */     {
/* 2773 */       flag |= checkLightFor(EnumSkyBlock.SKY, pos);
/*      */     }
/*      */     
/* 2776 */     flag |= checkLightFor(EnumSkyBlock.BLOCK, pos);
/* 2777 */     return flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getRawLight(BlockPos pos, EnumSkyBlock lightType)
/*      */   {
/* 2785 */     if ((lightType == EnumSkyBlock.SKY) && (canSeeSky(pos)))
/*      */     {
/* 2787 */       return 15;
/*      */     }
/*      */     
/*      */ 
/* 2791 */     Block block = getBlockState(pos).getBlock();
/* 2792 */     int i = lightType == EnumSkyBlock.SKY ? 0 : block.getLightValue();
/* 2793 */     int j = block.getLightOpacity();
/*      */     
/* 2795 */     if ((j >= 15) && (block.getLightValue() > 0))
/*      */     {
/* 2797 */       j = 1;
/*      */     }
/*      */     
/* 2800 */     if (j < 1)
/*      */     {
/* 2802 */       j = 1;
/*      */     }
/*      */     
/* 2805 */     if (j >= 15)
/*      */     {
/* 2807 */       return 0;
/*      */     }
/* 2809 */     if (i >= 14)
/*      */     {
/* 2811 */       return i;
/*      */     }
/*      */     
/*      */     EnumFacing[] arrayOfEnumFacing;
/* 2815 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*      */       
/* 2817 */       BlockPos blockpos = pos.offset(enumfacing);
/* 2818 */       int k = getLightFor(lightType, blockpos) - j;
/*      */       
/* 2820 */       if (k > i)
/*      */       {
/* 2822 */         i = k;
/*      */       }
/*      */       
/* 2825 */       if (i >= 14)
/*      */       {
/* 2827 */         return i;
/*      */       }
/*      */     }
/*      */     
/* 2831 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean checkLightFor(EnumSkyBlock lightType, BlockPos pos)
/*      */   {
/* 2838 */     if (!isAreaLoaded(pos, 17, false))
/*      */     {
/* 2840 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 2844 */     int i = 0;
/* 2845 */     int j = 0;
/* 2846 */     this.theProfiler.startSection("getBrightness");
/* 2847 */     int k = getLightFor(lightType, pos);
/* 2848 */     int l = getRawLight(pos, lightType);
/* 2849 */     int i1 = pos.getX();
/* 2850 */     int j1 = pos.getY();
/* 2851 */     int k1 = pos.getZ();
/*      */     
/* 2853 */     if (l > k)
/*      */     {
/* 2855 */       this.lightUpdateBlockList[(j++)] = 133152;
/*      */     }
/* 2857 */     else if (l < k)
/*      */     {
/* 2859 */       this.lightUpdateBlockList[(j++)] = (0x20820 | k << 18);
/*      */       
/* 2861 */       while (i < j)
/*      */       {
/* 2863 */         int l1 = this.lightUpdateBlockList[(i++)];
/* 2864 */         int i2 = (l1 & 0x3F) - 32 + i1;
/* 2865 */         int j2 = (l1 >> 6 & 0x3F) - 32 + j1;
/* 2866 */         int k2 = (l1 >> 12 & 0x3F) - 32 + k1;
/* 2867 */         int l2 = l1 >> 18 & 0xF;
/* 2868 */         BlockPos blockpos = new BlockPos(i2, j2, k2);
/* 2869 */         int i3 = getLightFor(lightType, blockpos);
/*      */         
/* 2871 */         if (i3 == l2)
/*      */         {
/* 2873 */           setLightFor(lightType, blockpos, 0);
/*      */           
/* 2875 */           if (l2 > 0)
/*      */           {
/* 2877 */             int j3 = MathHelper.abs_int(i2 - i1);
/* 2878 */             int k3 = MathHelper.abs_int(j2 - j1);
/* 2879 */             int l3 = MathHelper.abs_int(k2 - k1);
/*      */             
/* 2881 */             if (j3 + k3 + l3 < 17)
/*      */             {
/* 2883 */               BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */               EnumFacing[] arrayOfEnumFacing;
/* 2885 */               int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*      */                 
/* 2887 */                 int i4 = i2 + enumfacing.getFrontOffsetX();
/* 2888 */                 int j4 = j2 + enumfacing.getFrontOffsetY();
/* 2889 */                 int k4 = k2 + enumfacing.getFrontOffsetZ();
/* 2890 */                 blockpos$mutableblockpos.func_181079_c(i4, j4, k4);
/* 2891 */                 int l4 = Math.max(1, getBlockState(blockpos$mutableblockpos).getBlock().getLightOpacity());
/* 2892 */                 i3 = getLightFor(lightType, blockpos$mutableblockpos);
/*      */                 
/* 2894 */                 if ((i3 == l2 - l4) && (j < this.lightUpdateBlockList.length))
/*      */                 {
/* 2896 */                   this.lightUpdateBlockList[(j++)] = (i4 - i1 + 32 | j4 - j1 + 32 << 6 | k4 - k1 + 32 << 12 | l2 - l4 << 18);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2904 */       i = 0;
/*      */     }
/*      */     
/* 2907 */     this.theProfiler.endSection();
/* 2908 */     this.theProfiler.startSection("checkedPosition < toCheckCount");
/*      */     
/* 2910 */     while (i < j)
/*      */     {
/* 2912 */       int i5 = this.lightUpdateBlockList[(i++)];
/* 2913 */       int j5 = (i5 & 0x3F) - 32 + i1;
/* 2914 */       int k5 = (i5 >> 6 & 0x3F) - 32 + j1;
/* 2915 */       int l5 = (i5 >> 12 & 0x3F) - 32 + k1;
/* 2916 */       BlockPos blockpos1 = new BlockPos(j5, k5, l5);
/* 2917 */       int i6 = getLightFor(lightType, blockpos1);
/* 2918 */       int j6 = getRawLight(blockpos1, lightType);
/*      */       
/* 2920 */       if (j6 != i6)
/*      */       {
/* 2922 */         setLightFor(lightType, blockpos1, j6);
/*      */         
/* 2924 */         if (j6 > i6)
/*      */         {
/* 2926 */           int k6 = Math.abs(j5 - i1);
/* 2927 */           int l6 = Math.abs(k5 - j1);
/* 2928 */           int i7 = Math.abs(l5 - k1);
/* 2929 */           boolean flag = j < this.lightUpdateBlockList.length - 6;
/*      */           
/* 2931 */           if ((k6 + l6 + i7 < 17) && (flag))
/*      */           {
/* 2933 */             if (getLightFor(lightType, blockpos1.west()) < j6)
/*      */             {
/* 2935 */               this.lightUpdateBlockList[(j++)] = (j5 - 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12));
/*      */             }
/*      */             
/* 2938 */             if (getLightFor(lightType, blockpos1.east()) < j6)
/*      */             {
/* 2940 */               this.lightUpdateBlockList[(j++)] = (j5 + 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12));
/*      */             }
/*      */             
/* 2943 */             if (getLightFor(lightType, blockpos1.down()) < j6)
/*      */             {
/* 2945 */               this.lightUpdateBlockList[(j++)] = (j5 - i1 + 32 + (k5 - 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12));
/*      */             }
/*      */             
/* 2948 */             if (getLightFor(lightType, blockpos1.up()) < j6)
/*      */             {
/* 2950 */               this.lightUpdateBlockList[(j++)] = (j5 - i1 + 32 + (k5 + 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12));
/*      */             }
/*      */             
/* 2953 */             if (getLightFor(lightType, blockpos1.north()) < j6)
/*      */             {
/* 2955 */               this.lightUpdateBlockList[(j++)] = (j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - 1 - k1 + 32 << 12));
/*      */             }
/*      */             
/* 2958 */             if (getLightFor(lightType, blockpos1.south()) < j6)
/*      */             {
/* 2960 */               this.lightUpdateBlockList[(j++)] = (j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 + 1 - k1 + 32 << 12));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2967 */     this.theProfiler.endSection();
/* 2968 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean tickUpdates(boolean p_72955_1_)
/*      */   {
/* 2977 */     return false;
/*      */   }
/*      */   
/*      */   public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_)
/*      */   {
/* 2982 */     return null;
/*      */   }
/*      */   
/*      */   public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBB, boolean p_175712_2_)
/*      */   {
/* 2987 */     return null;
/*      */   }
/*      */   
/*      */   public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entityIn, AxisAlignedBB bb)
/*      */   {
/* 2992 */     return getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
/*      */   }
/*      */   
/*      */   public List<Entity> getEntitiesInAABBexcluding(Entity entityIn, AxisAlignedBB boundingBox, Predicate<? super Entity> predicate)
/*      */   {
/* 2997 */     List<Entity> list = Lists.newArrayList();
/* 2998 */     int i = MathHelper.floor_double((boundingBox.minX - 2.0D) / 16.0D);
/* 2999 */     int j = MathHelper.floor_double((boundingBox.maxX + 2.0D) / 16.0D);
/* 3000 */     int k = MathHelper.floor_double((boundingBox.minZ - 2.0D) / 16.0D);
/* 3001 */     int l = MathHelper.floor_double((boundingBox.maxZ + 2.0D) / 16.0D);
/*      */     
/* 3003 */     for (int i1 = i; i1 <= j; i1++)
/*      */     {
/* 3005 */       for (int j1 = k; j1 <= l; j1++)
/*      */       {
/* 3007 */         if (isChunkLoaded(i1, j1, true))
/*      */         {
/* 3009 */           getChunkFromChunkCoords(i1, j1).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3014 */     return list;
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntities(Class<? extends T> entityType, Predicate<? super T> filter)
/*      */   {
/* 3019 */     List<T> list = Lists.newArrayList();
/*      */     
/* 3021 */     for (Entity entity : this.loadedEntityList)
/*      */     {
/* 3023 */       if ((entityType.isAssignableFrom(entity.getClass())) && (filter.apply(entity)))
/*      */       {
/* 3025 */         list.add(entity);
/*      */       }
/*      */     }
/*      */     
/* 3029 */     return list;
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getPlayers(Class<? extends T> playerType, Predicate<? super T> filter)
/*      */   {
/* 3034 */     List<T> list = Lists.newArrayList();
/*      */     
/* 3036 */     for (Entity entity : this.playerEntities)
/*      */     {
/* 3038 */       if ((playerType.isAssignableFrom(entity.getClass())) && (filter.apply(entity)))
/*      */       {
/* 3040 */         list.add(entity);
/*      */       }
/*      */     }
/*      */     
/* 3044 */     return list;
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> classEntity, AxisAlignedBB bb)
/*      */   {
/* 3049 */     return getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, Predicate<? super T> filter)
/*      */   {
/* 3054 */     int i = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
/* 3055 */     int j = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
/* 3056 */     int k = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
/* 3057 */     int l = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
/* 3058 */     List<T> list = Lists.newArrayList();
/*      */     
/* 3060 */     for (int i1 = i; i1 <= j; i1++)
/*      */     {
/* 3062 */       for (int j1 = k; j1 <= l; j1++)
/*      */       {
/* 3064 */         if (isChunkLoaded(i1, j1, true))
/*      */         {
/* 3066 */           getChunkFromChunkCoords(i1, j1).getEntitiesOfTypeWithinAAAB(clazz, aabb, list, filter);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3071 */     return list;
/*      */   }
/*      */   
/*      */   public <T extends Entity> T findNearestEntityWithinAABB(Class<? extends T> entityType, AxisAlignedBB aabb, T closestTo)
/*      */   {
/* 3076 */     List<T> list = getEntitiesWithinAABB(entityType, aabb);
/* 3077 */     T t = null;
/* 3078 */     double d0 = Double.MAX_VALUE;
/*      */     
/* 3080 */     for (int i = 0; i < list.size(); i++)
/*      */     {
/* 3082 */       T t1 = (Entity)list.get(i);
/*      */       
/* 3084 */       if ((t1 != closestTo) && (EntitySelectors.NOT_SPECTATING.apply(t1)))
/*      */       {
/* 3086 */         double d1 = closestTo.getDistanceSqToEntity(t1);
/*      */         
/* 3088 */         if (d1 <= d0)
/*      */         {
/* 3090 */           t = t1;
/* 3091 */           d0 = d1;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3096 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Entity getEntityByID(int id)
/*      */   {
/* 3104 */     return (Entity)this.entitiesById.lookup(id);
/*      */   }
/*      */   
/*      */   public List<Entity> getLoadedEntityList()
/*      */   {
/* 3109 */     return this.loadedEntityList;
/*      */   }
/*      */   
/*      */   public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity)
/*      */   {
/* 3114 */     if (isBlockLoaded(pos))
/*      */     {
/* 3116 */       getChunkFromBlockCoords(pos).setChunkModified();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int countEntities(Class<?> entityType)
/*      */   {
/* 3125 */     int i = 0;
/*      */     
/* 3127 */     for (Entity entity : this.loadedEntityList)
/*      */     {
/* 3129 */       if (((!(entity instanceof EntityLiving)) || (!((EntityLiving)entity).isNoDespawnRequired())) && (entityType.isAssignableFrom(entity.getClass())))
/*      */       {
/* 3131 */         i++;
/*      */       }
/*      */     }
/*      */     
/* 3135 */     return i;
/*      */   }
/*      */   
/*      */   public void loadEntities(Collection<Entity> entityCollection)
/*      */   {
/* 3140 */     this.loadedEntityList.addAll(entityCollection);
/*      */     
/* 3142 */     for (Entity entity : entityCollection)
/*      */     {
/* 3144 */       onEntityAdded(entity);
/*      */     }
/*      */   }
/*      */   
/*      */   public void unloadEntities(Collection<Entity> entityCollection)
/*      */   {
/* 3150 */     this.unloadedEntityList.addAll(entityCollection);
/*      */   }
/*      */   
/*      */   public boolean canBlockBePlaced(Block blockIn, BlockPos pos, boolean p_175716_3_, EnumFacing side, Entity entityIn, ItemStack itemStackIn)
/*      */   {
/* 3155 */     Block block = getBlockState(pos).getBlock();
/* 3156 */     AxisAlignedBB axisalignedbb = p_175716_3_ ? null : blockIn.getCollisionBoundingBox(this, pos, blockIn.getDefaultState());
/* 3157 */     return (axisalignedbb == null) || (checkNoEntityCollision(axisalignedbb, entityIn));
/*      */   }
/*      */   
/*      */   public int func_181545_F()
/*      */   {
/* 3162 */     return this.field_181546_a;
/*      */   }
/*      */   
/*      */   public void func_181544_b(int p_181544_1_)
/*      */   {
/* 3167 */     this.field_181546_a = p_181544_1_;
/*      */   }
/*      */   
/*      */   public int getStrongPower(BlockPos pos, EnumFacing direction)
/*      */   {
/* 3172 */     IBlockState iblockstate = getBlockState(pos);
/* 3173 */     return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
/*      */   }
/*      */   
/*      */   public WorldType getWorldType()
/*      */   {
/* 3178 */     return this.worldInfo.getTerrainType();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getStrongPower(BlockPos pos)
/*      */   {
/* 3186 */     int i = 0;
/* 3187 */     i = Math.max(i, getStrongPower(pos.down(), EnumFacing.DOWN));
/*      */     
/* 3189 */     if (i >= 15)
/*      */     {
/* 3191 */       return i;
/*      */     }
/*      */     
/*      */ 
/* 3195 */     i = Math.max(i, getStrongPower(pos.up(), EnumFacing.UP));
/*      */     
/* 3197 */     if (i >= 15)
/*      */     {
/* 3199 */       return i;
/*      */     }
/*      */     
/*      */ 
/* 3203 */     i = Math.max(i, getStrongPower(pos.north(), EnumFacing.NORTH));
/*      */     
/* 3205 */     if (i >= 15)
/*      */     {
/* 3207 */       return i;
/*      */     }
/*      */     
/*      */ 
/* 3211 */     i = Math.max(i, getStrongPower(pos.south(), EnumFacing.SOUTH));
/*      */     
/* 3213 */     if (i >= 15)
/*      */     {
/* 3215 */       return i;
/*      */     }
/*      */     
/*      */ 
/* 3219 */     i = Math.max(i, getStrongPower(pos.west(), EnumFacing.WEST));
/*      */     
/* 3221 */     if (i >= 15)
/*      */     {
/* 3223 */       return i;
/*      */     }
/*      */     
/*      */ 
/* 3227 */     i = Math.max(i, getStrongPower(pos.east(), EnumFacing.EAST));
/* 3228 */     return i >= 15 ? i : i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSidePowered(BlockPos pos, EnumFacing side)
/*      */   {
/* 3238 */     return getRedstonePower(pos, side) > 0;
/*      */   }
/*      */   
/*      */   public int getRedstonePower(BlockPos pos, EnumFacing facing)
/*      */   {
/* 3243 */     IBlockState iblockstate = getBlockState(pos);
/* 3244 */     Block block = iblockstate.getBlock();
/* 3245 */     return block.isNormalCube() ? getStrongPower(pos) : block.getWeakPower(this, pos, iblockstate, facing);
/*      */   }
/*      */   
/*      */   public boolean isBlockPowered(BlockPos pos)
/*      */   {
/* 3250 */     return getRedstonePower(pos.down(), EnumFacing.DOWN) > 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int isBlockIndirectlyGettingPowered(BlockPos pos)
/*      */   {
/* 3259 */     int i = 0;
/*      */     EnumFacing[] arrayOfEnumFacing;
/* 3261 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*      */       
/* 3263 */       int j = getRedstonePower(pos.offset(enumfacing), enumfacing);
/*      */       
/* 3265 */       if (j >= 15)
/*      */       {
/* 3267 */         return 15;
/*      */       }
/*      */       
/* 3270 */       if (j > i)
/*      */       {
/* 3272 */         i = j;
/*      */       }
/*      */     }
/*      */     
/* 3276 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance)
/*      */   {
/* 3285 */     return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityPlayer getClosestPlayer(double x, double y, double z, double distance)
/*      */   {
/* 3294 */     double d0 = -1.0D;
/* 3295 */     EntityPlayer entityplayer = null;
/*      */     
/* 3297 */     for (int i = 0; i < this.playerEntities.size(); i++)
/*      */     {
/* 3299 */       EntityPlayer entityplayer1 = (EntityPlayer)this.playerEntities.get(i);
/*      */       
/* 3301 */       if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1))
/*      */       {
/* 3303 */         double d1 = entityplayer1.getDistanceSq(x, y, z);
/*      */         
/* 3305 */         if (((distance < 0.0D) || (d1 < distance * distance)) && ((d0 == -1.0D) || (d1 < d0)))
/*      */         {
/* 3307 */           d0 = d1;
/* 3308 */           entityplayer = entityplayer1;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3313 */     return entityplayer;
/*      */   }
/*      */   
/*      */   public boolean isAnyPlayerWithinRangeAt(double x, double y, double z, double range)
/*      */   {
/* 3318 */     for (int i = 0; i < this.playerEntities.size(); i++)
/*      */     {
/* 3320 */       EntityPlayer entityplayer = (EntityPlayer)this.playerEntities.get(i);
/*      */       
/* 3322 */       if (EntitySelectors.NOT_SPECTATING.apply(entityplayer))
/*      */       {
/* 3324 */         double d0 = entityplayer.getDistanceSq(x, y, z);
/*      */         
/* 3326 */         if ((range < 0.0D) || (d0 < range * range))
/*      */         {
/* 3328 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3333 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityPlayer getPlayerEntityByName(String name)
/*      */   {
/* 3341 */     for (int i = 0; i < this.playerEntities.size(); i++)
/*      */     {
/* 3343 */       EntityPlayer entityplayer = (EntityPlayer)this.playerEntities.get(i);
/*      */       
/* 3345 */       if (name.equals(entityplayer.getName()))
/*      */       {
/* 3347 */         return entityplayer;
/*      */       }
/*      */     }
/*      */     
/* 3351 */     return null;
/*      */   }
/*      */   
/*      */   public EntityPlayer getPlayerEntityByUUID(UUID uuid)
/*      */   {
/* 3356 */     for (int i = 0; i < this.playerEntities.size(); i++)
/*      */     {
/* 3358 */       EntityPlayer entityplayer = (EntityPlayer)this.playerEntities.get(i);
/*      */       
/* 3360 */       if (uuid.equals(entityplayer.getUniqueID()))
/*      */       {
/* 3362 */         return entityplayer;
/*      */       }
/*      */     }
/*      */     
/* 3366 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendQuittingDisconnectingPacket() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void checkSessionLock()
/*      */     throws MinecraftException
/*      */   {
/* 3381 */     this.saveHandler.checkSessionLock();
/*      */   }
/*      */   
/*      */   public void setTotalWorldTime(long worldTime)
/*      */   {
/* 3386 */     this.worldInfo.setWorldTotalTime(worldTime);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getSeed()
/*      */   {
/* 3394 */     return this.worldInfo.getSeed();
/*      */   }
/*      */   
/*      */   public long getTotalWorldTime()
/*      */   {
/* 3399 */     return this.worldInfo.getWorldTotalTime();
/*      */   }
/*      */   
/*      */   public long getWorldTime()
/*      */   {
/* 3404 */     return this.worldInfo.getWorldTime();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWorldTime(long time)
/*      */   {
/* 3412 */     this.worldInfo.setWorldTime(time);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public BlockPos getSpawnPoint()
/*      */   {
/* 3420 */     BlockPos blockpos = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
/*      */     
/* 3422 */     if (!getWorldBorder().contains(blockpos))
/*      */     {
/* 3424 */       blockpos = getHeight(new BlockPos(getWorldBorder().getCenterX(), 0.0D, getWorldBorder().getCenterZ()));
/*      */     }
/*      */     
/* 3427 */     return blockpos;
/*      */   }
/*      */   
/*      */   public void setSpawnPoint(BlockPos pos)
/*      */   {
/* 3432 */     this.worldInfo.setSpawn(pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void joinEntityInSurroundings(Entity entityIn)
/*      */   {
/* 3440 */     int i = MathHelper.floor_double(entityIn.posX / 16.0D);
/* 3441 */     int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
/* 3442 */     int k = 2;
/*      */     
/* 3444 */     for (int l = i - k; l <= i + k; l++)
/*      */     {
/* 3446 */       for (int i1 = j - k; i1 <= j + k; i1++)
/*      */       {
/* 3448 */         getChunkFromChunkCoords(l, i1);
/*      */       }
/*      */     }
/*      */     
/* 3452 */     if (!this.loadedEntityList.contains(entityIn))
/*      */     {
/* 3454 */       this.loadedEntityList.add(entityIn);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
/*      */   {
/* 3460 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEntityState(Entity entityIn, byte state) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IChunkProvider getChunkProvider()
/*      */   {
/* 3475 */     return this.chunkProvider;
/*      */   }
/*      */   
/*      */   public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
/*      */   {
/* 3480 */     blockIn.onBlockEventReceived(this, pos, getBlockState(pos), eventID, eventParam);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ISaveHandler getSaveHandler()
/*      */   {
/* 3488 */     return this.saveHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public WorldInfo getWorldInfo()
/*      */   {
/* 3496 */     return this.worldInfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public GameRules getGameRules()
/*      */   {
/* 3504 */     return this.worldInfo.getGameRulesInstance();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateAllPlayersSleepingFlag() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public float getThunderStrength(float delta)
/*      */   {
/* 3516 */     return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * getRainStrength(delta);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setThunderStrength(float strength)
/*      */   {
/* 3524 */     this.prevThunderingStrength = strength;
/* 3525 */     this.thunderingStrength = strength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getRainStrength(float delta)
/*      */   {
/* 3533 */     return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRainStrength(float strength)
/*      */   {
/* 3541 */     this.prevRainingStrength = strength;
/* 3542 */     this.rainingStrength = strength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isThundering()
/*      */   {
/* 3550 */     return getThunderStrength(1.0F) > 0.9D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRaining()
/*      */   {
/* 3558 */     return getRainStrength(1.0F) > 0.2D;
/*      */   }
/*      */   
/*      */   public boolean canLightningStrike(BlockPos strikePosition)
/*      */   {
/* 3563 */     if (!isRaining())
/*      */     {
/* 3565 */       return false;
/*      */     }
/* 3567 */     if (!canSeeSky(strikePosition))
/*      */     {
/* 3569 */       return false;
/*      */     }
/* 3571 */     if (getPrecipitationHeight(strikePosition).getY() > strikePosition.getY())
/*      */     {
/* 3573 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 3577 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(strikePosition);
/* 3578 */     return canSnowAt(strikePosition, false) ? false : biomegenbase.getEnableSnow() ? false : biomegenbase.canSpawnLightningBolt();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isBlockinHighHumidity(BlockPos pos)
/*      */   {
/* 3584 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(pos);
/* 3585 */     return biomegenbase.isHighHumidity();
/*      */   }
/*      */   
/*      */   public MapStorage getMapStorage()
/*      */   {
/* 3590 */     return this.mapStorage;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setItemData(String dataID, WorldSavedData worldSavedDataIn)
/*      */   {
/* 3599 */     this.mapStorage.setData(dataID, worldSavedDataIn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public WorldSavedData loadItemData(Class<? extends WorldSavedData> clazz, String dataID)
/*      */   {
/* 3608 */     return this.mapStorage.loadData(clazz, dataID);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getUniqueDataId(String key)
/*      */   {
/* 3617 */     return this.mapStorage.getUniqueDataId(key);
/*      */   }
/*      */   
/*      */   public void playBroadcastSound(int p_175669_1_, BlockPos pos, int p_175669_3_)
/*      */   {
/* 3622 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 3624 */       ((IWorldAccess)this.worldAccesses.get(i)).broadcastSound(p_175669_1_, pos, p_175669_3_);
/*      */     }
/*      */   }
/*      */   
/*      */   public void playAuxSFX(int p_175718_1_, BlockPos pos, int p_175718_3_)
/*      */   {
/* 3630 */     playAuxSFXAtEntity(null, p_175718_1_, pos, p_175718_3_);
/*      */   }
/*      */   
/*      */   public void playAuxSFXAtEntity(EntityPlayer player, int sfxType, BlockPos pos, int p_180498_4_)
/*      */   {
/*      */     try
/*      */     {
/* 3637 */       for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */       {
/* 3639 */         ((IWorldAccess)this.worldAccesses.get(i)).playAuxSFX(player, sfxType, pos, p_180498_4_);
/*      */       }
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/* 3644 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Playing level event");
/* 3645 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Level event being played");
/* 3646 */       crashreportcategory.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
/* 3647 */       crashreportcategory.addCrashSection("Event source", player);
/* 3648 */       crashreportcategory.addCrashSection("Event type", Integer.valueOf(sfxType));
/* 3649 */       crashreportcategory.addCrashSection("Event data", Integer.valueOf(p_180498_4_));
/* 3650 */       throw new ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getHeight()
/*      */   {
/* 3659 */     return 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getActualHeight()
/*      */   {
/* 3667 */     return this.provider.getHasNoSky() ? 128 : 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Random setRandomSeed(int p_72843_1_, int p_72843_2_, int p_72843_3_)
/*      */   {
/* 3675 */     long i = p_72843_1_ * 341873128712L + p_72843_2_ * 132897987541L + getWorldInfo().getSeed() + p_72843_3_;
/* 3676 */     this.rand.setSeed(i);
/* 3677 */     return this.rand;
/*      */   }
/*      */   
/*      */   public BlockPos getStrongholdPos(String name, BlockPos pos)
/*      */   {
/* 3682 */     return getChunkProvider().getStrongholdGen(this, name, pos);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean extendedLevelsInChunkCache()
/*      */   {
/* 3690 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getHorizon()
/*      */   {
/* 3698 */     return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public CrashReportCategory addWorldInfoToCrashReport(CrashReport report)
/*      */   {
/* 3706 */     CrashReportCategory crashreportcategory = report.makeCategoryDepth("Affected level", 1);
/* 3707 */     crashreportcategory.addCrashSection("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
/* 3708 */     crashreportcategory.addCrashSectionCallable("All players", new Callable()
/*      */     {
/*      */       public String call()
/*      */       {
/* 3712 */         return World.this.playerEntities.size() + " total; " + World.this.playerEntities.toString();
/*      */       }
/* 3714 */     });
/* 3715 */     crashreportcategory.addCrashSectionCallable("Chunk stats", new Callable()
/*      */     {
/*      */       public String call()
/*      */       {
/* 3719 */         return World.this.chunkProvider.makeString();
/*      */       }
/*      */     });
/*      */     
/*      */     try
/*      */     {
/* 3725 */       this.worldInfo.addToCrashReport(crashreportcategory);
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/* 3729 */       crashreportcategory.addCrashSectionThrowable("Level Data Unobtainable", throwable);
/*      */     }
/*      */     
/* 3732 */     return crashreportcategory;
/*      */   }
/*      */   
/*      */   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
/*      */   {
/* 3737 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 3739 */       IWorldAccess iworldaccess = (IWorldAccess)this.worldAccesses.get(i);
/* 3740 */       iworldaccess.sendBlockBreakProgress(breakerId, pos, progress);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Calendar getCurrentDate()
/*      */   {
/* 3749 */     if (getTotalWorldTime() % 600L == 0L)
/*      */     {
/* 3751 */       this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
/*      */     }
/*      */     
/* 3754 */     return this.theCalendar;
/*      */   }
/*      */   
/*      */ 
/*      */   public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund) {}
/*      */   
/*      */ 
/*      */   public Scoreboard getScoreboard()
/*      */   {
/* 3763 */     return this.worldScoreboard;
/*      */   }
/*      */   
/*      */   public void updateComparatorOutputLevel(BlockPos pos, Block blockIn)
/*      */   {
/* 3768 */     for (Object enumfacing : EnumFacing.Plane.HORIZONTAL)
/*      */     {
/* 3770 */       BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
/*      */       
/* 3772 */       if (isBlockLoaded(blockpos))
/*      */       {
/* 3774 */         IBlockState iblockstate = getBlockState(blockpos);
/*      */         
/* 3776 */         if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock()))
/*      */         {
/* 3778 */           iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
/*      */         }
/* 3780 */         else if (iblockstate.getBlock().isNormalCube())
/*      */         {
/* 3782 */           blockpos = blockpos.offset((EnumFacing)enumfacing);
/* 3783 */           iblockstate = getBlockState(blockpos);
/*      */           
/* 3785 */           if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock()))
/*      */           {
/* 3787 */             iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public DifficultyInstance getDifficultyForLocation(BlockPos pos)
/*      */   {
/* 3796 */     long i = 0L;
/* 3797 */     float f = 0.0F;
/*      */     
/* 3799 */     if (isBlockLoaded(pos))
/*      */     {
/* 3801 */       f = getCurrentMoonPhaseFactor();
/* 3802 */       i = getChunkFromBlockCoords(pos).getInhabitedTime();
/*      */     }
/*      */     
/* 3805 */     return new DifficultyInstance(getDifficulty(), getWorldTime(), i, f);
/*      */   }
/*      */   
/*      */   public EnumDifficulty getDifficulty()
/*      */   {
/* 3810 */     return getWorldInfo().getDifficulty();
/*      */   }
/*      */   
/*      */   public int getSkylightSubtracted()
/*      */   {
/* 3815 */     return this.skylightSubtracted;
/*      */   }
/*      */   
/*      */   public void setSkylightSubtracted(int newSkylightSubtracted)
/*      */   {
/* 3820 */     this.skylightSubtracted = newSkylightSubtracted;
/*      */   }
/*      */   
/*      */   public int getLastLightningBolt()
/*      */   {
/* 3825 */     return this.lastLightningBolt;
/*      */   }
/*      */   
/*      */   public void setLastLightningBolt(int lastLightningBoltIn)
/*      */   {
/* 3830 */     this.lastLightningBolt = lastLightningBoltIn;
/*      */   }
/*      */   
/*      */   public boolean isFindingSpawnPoint()
/*      */   {
/* 3835 */     return this.findingSpawnPoint;
/*      */   }
/*      */   
/*      */   public VillageCollection getVillageCollection()
/*      */   {
/* 3840 */     return this.villageCollectionObj;
/*      */   }
/*      */   
/*      */   public WorldBorder getWorldBorder()
/*      */   {
/* 3845 */     return this.worldBorder;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSpawnChunk(int x, int z)
/*      */   {
/* 3853 */     BlockPos blockpos = getSpawnPoint();
/* 3854 */     int i = x * 16 + 8 - blockpos.getX();
/* 3855 */     int j = z * 16 + 8 - blockpos.getZ();
/* 3856 */     int k = 128;
/* 3857 */     return (i >= -k) && (i <= k) && (j >= -k) && (j <= k);
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\World.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */