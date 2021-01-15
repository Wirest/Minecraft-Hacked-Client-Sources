/*      */ package net.minecraft.world;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockEventData;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.EntityTracker;
/*      */ import net.minecraft.entity.EnumCreatureType;
/*      */ import net.minecraft.entity.INpc;
/*      */ import net.minecraft.entity.effect.EntityLightningBolt;
/*      */ import net.minecraft.entity.passive.EntityAnimal;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.network.NetHandlerPlayServer;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.server.S27PacketExplosion;
/*      */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*      */ import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.scoreboard.ScoreboardSaveData;
/*      */ import net.minecraft.scoreboard.ServerScoreboard;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.management.PlayerManager;
/*      */ import net.minecraft.server.management.ServerConfigurationManager;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.IProgressUpdate;
/*      */ import net.minecraft.util.IThreadListener;
/*      */ import net.minecraft.util.IntHashMap;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.util.WeightedRandom;
/*      */ import net.minecraft.util.WeightedRandomChestContent;
/*      */ import net.minecraft.village.VillageCollection;
/*      */ import net.minecraft.village.VillageSiege;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
/*      */ import net.minecraft.world.chunk.storage.IChunkLoader;
/*      */ import net.minecraft.world.gen.ChunkProviderServer;
/*      */ import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
/*      */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.MapStorage;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class WorldServer extends World implements IThreadListener
/*      */ {
/*   76 */   private static final Logger logger = ;
/*      */   private final MinecraftServer mcServer;
/*      */   private final EntityTracker theEntityTracker;
/*      */   private final PlayerManager thePlayerManager;
/*   80 */   private final Set<NextTickListEntry> pendingTickListEntriesHashSet = com.google.common.collect.Sets.newHashSet();
/*   81 */   private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet = new TreeSet();
/*   82 */   private final Map<UUID, Entity> entitiesByUuid = Maps.newHashMap();
/*      */   
/*      */ 
/*      */   public ChunkProviderServer theChunkProviderServer;
/*      */   
/*      */ 
/*      */   public boolean disableLevelSaving;
/*      */   
/*      */   private boolean allPlayersSleeping;
/*      */   
/*      */   private int updateEntityTick;
/*      */   
/*      */   private final Teleporter worldTeleporter;
/*      */   
/*   96 */   private final SpawnerAnimals mobSpawner = new SpawnerAnimals();
/*   97 */   protected final VillageSiege villageSiege = new VillageSiege(this);
/*   98 */   private ServerBlockEventList[] field_147490_S = { new ServerBlockEventList(null), new ServerBlockEventList(null) };
/*      */   private int blockEventCacheIndex;
/*  100 */   private static final List<WeightedRandomChestContent> bonusChestContent = Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10), new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10) });
/*  101 */   private List<NextTickListEntry> pendingTickListEntriesThisTick = Lists.newArrayList();
/*      */   
/*      */   public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn)
/*      */   {
/*  105 */     super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), profilerIn, false);
/*  106 */     this.mcServer = server;
/*  107 */     this.theEntityTracker = new EntityTracker(this);
/*  108 */     this.thePlayerManager = new PlayerManager(this);
/*  109 */     this.provider.registerWorld(this);
/*  110 */     this.chunkProvider = createChunkProvider();
/*  111 */     this.worldTeleporter = new Teleporter(this);
/*  112 */     calculateInitialSkylight();
/*  113 */     calculateInitialWeather();
/*  114 */     getWorldBorder().setSize(server.getMaxWorldSize());
/*      */   }
/*      */   
/*      */   public World init()
/*      */   {
/*  119 */     this.mapStorage = new MapStorage(this.saveHandler);
/*  120 */     String s = VillageCollection.fileNameForProvider(this.provider);
/*  121 */     VillageCollection villagecollection = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, s);
/*      */     
/*  123 */     if (villagecollection == null)
/*      */     {
/*  125 */       this.villageCollectionObj = new VillageCollection(this);
/*  126 */       this.mapStorage.setData(s, this.villageCollectionObj);
/*      */     }
/*      */     else
/*      */     {
/*  130 */       this.villageCollectionObj = villagecollection;
/*  131 */       this.villageCollectionObj.setWorldsForAll(this);
/*      */     }
/*      */     
/*  134 */     this.worldScoreboard = new ServerScoreboard(this.mcServer);
/*  135 */     ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData)this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");
/*      */     
/*  137 */     if (scoreboardsavedata == null)
/*      */     {
/*  139 */       scoreboardsavedata = new ScoreboardSaveData();
/*  140 */       this.mapStorage.setData("scoreboard", scoreboardsavedata);
/*      */     }
/*      */     
/*  143 */     scoreboardsavedata.setScoreboard(this.worldScoreboard);
/*  144 */     ((ServerScoreboard)this.worldScoreboard).func_96547_a(scoreboardsavedata);
/*  145 */     getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
/*  146 */     getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
/*  147 */     getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
/*  148 */     getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
/*  149 */     getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
/*      */     
/*  151 */     if (this.worldInfo.getBorderLerpTime() > 0L)
/*      */     {
/*  153 */       getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
/*      */     }
/*      */     else
/*      */     {
/*  157 */       getWorldBorder().setTransition(this.worldInfo.getBorderSize());
/*      */     }
/*      */     
/*  160 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void tick()
/*      */   {
/*  168 */     super.tick();
/*      */     
/*  170 */     if ((getWorldInfo().isHardcoreModeEnabled()) && (getDifficulty() != EnumDifficulty.HARD))
/*      */     {
/*  172 */       getWorldInfo().setDifficulty(EnumDifficulty.HARD);
/*      */     }
/*      */     
/*  175 */     this.provider.getWorldChunkManager().cleanupCache();
/*      */     
/*  177 */     if (areAllPlayersAsleep())
/*      */     {
/*  179 */       if (getGameRules().getBoolean("doDaylightCycle"))
/*      */       {
/*  181 */         long i = this.worldInfo.getWorldTime() + 24000L;
/*  182 */         this.worldInfo.setWorldTime(i - i % 24000L);
/*      */       }
/*      */       
/*  185 */       wakeAllPlayers();
/*      */     }
/*      */     
/*  188 */     this.theProfiler.startSection("mobSpawner");
/*      */     
/*  190 */     if ((getGameRules().getBoolean("doMobSpawning")) && (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD))
/*      */     {
/*  192 */       this.mobSpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
/*      */     }
/*      */     
/*  195 */     this.theProfiler.endStartSection("chunkSource");
/*  196 */     this.chunkProvider.unloadQueuedChunks();
/*  197 */     int j = calculateSkylightSubtracted(1.0F);
/*      */     
/*  199 */     if (j != getSkylightSubtracted())
/*      */     {
/*  201 */       setSkylightSubtracted(j);
/*      */     }
/*      */     
/*  204 */     this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
/*      */     
/*  206 */     if (getGameRules().getBoolean("doDaylightCycle"))
/*      */     {
/*  208 */       this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
/*      */     }
/*      */     
/*  211 */     this.theProfiler.endStartSection("tickPending");
/*  212 */     tickUpdates(false);
/*  213 */     this.theProfiler.endStartSection("tickBlocks");
/*  214 */     updateBlocks();
/*  215 */     this.theProfiler.endStartSection("chunkMap");
/*  216 */     this.thePlayerManager.updatePlayerInstances();
/*  217 */     this.theProfiler.endStartSection("village");
/*  218 */     this.villageCollectionObj.tick();
/*  219 */     this.villageSiege.tick();
/*  220 */     this.theProfiler.endStartSection("portalForcer");
/*  221 */     this.worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
/*  222 */     this.theProfiler.endSection();
/*  223 */     sendQueuedBlockEvents();
/*      */   }
/*      */   
/*      */   public BiomeGenBase.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType creatureType, BlockPos pos)
/*      */   {
/*  228 */     List<BiomeGenBase.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
/*  229 */     return (list != null) && (!list.isEmpty()) ? (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(this.rand, list) : null;
/*      */   }
/*      */   
/*      */   public boolean canCreatureTypeSpawnHere(EnumCreatureType creatureType, BiomeGenBase.SpawnListEntry spawnListEntry, BlockPos pos)
/*      */   {
/*  234 */     List<BiomeGenBase.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
/*  235 */     return (list != null) && (!list.isEmpty()) ? list.contains(spawnListEntry) : false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateAllPlayersSleepingFlag()
/*      */   {
/*  243 */     this.allPlayersSleeping = false;
/*      */     
/*  245 */     if (!this.playerEntities.isEmpty())
/*      */     {
/*  247 */       int i = 0;
/*  248 */       int j = 0;
/*      */       
/*  250 */       for (EntityPlayer entityplayer : this.playerEntities)
/*      */       {
/*  252 */         if (entityplayer.isSpectator())
/*      */         {
/*  254 */           i++;
/*      */         }
/*  256 */         else if (entityplayer.isPlayerSleeping())
/*      */         {
/*  258 */           j++;
/*      */         }
/*      */       }
/*      */       
/*  262 */       this.allPlayersSleeping = ((j > 0) && (j >= this.playerEntities.size() - i));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void wakeAllPlayers()
/*      */   {
/*  268 */     this.allPlayersSleeping = false;
/*      */     
/*  270 */     for (EntityPlayer entityplayer : this.playerEntities)
/*      */     {
/*  272 */       if (entityplayer.isPlayerSleeping())
/*      */       {
/*  274 */         entityplayer.wakeUpPlayer(false, false, true);
/*      */       }
/*      */     }
/*      */     
/*  278 */     resetRainAndThunder();
/*      */   }
/*      */   
/*      */   private void resetRainAndThunder()
/*      */   {
/*  283 */     this.worldInfo.setRainTime(0);
/*  284 */     this.worldInfo.setRaining(false);
/*  285 */     this.worldInfo.setThunderTime(0);
/*  286 */     this.worldInfo.setThundering(false);
/*      */   }
/*      */   
/*      */   public boolean areAllPlayersAsleep()
/*      */   {
/*  291 */     if ((this.allPlayersSleeping) && (!this.isRemote))
/*      */     {
/*  293 */       for (EntityPlayer entityplayer : this.playerEntities)
/*      */       {
/*  295 */         if ((entityplayer.isSpectator()) || (!entityplayer.isPlayerFullyAsleep()))
/*      */         {
/*  297 */           return false;
/*      */         }
/*      */       }
/*      */       
/*  301 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  305 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInitialSpawnLocation()
/*      */   {
/*  314 */     if (this.worldInfo.getSpawnY() <= 0)
/*      */     {
/*  316 */       this.worldInfo.setSpawnY(func_181545_F() + 1);
/*      */     }
/*      */     
/*  319 */     int i = this.worldInfo.getSpawnX();
/*  320 */     int j = this.worldInfo.getSpawnZ();
/*  321 */     int k = 0;
/*      */     
/*  323 */     while (getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.air)
/*      */     {
/*  325 */       i += this.rand.nextInt(8) - this.rand.nextInt(8);
/*  326 */       j += this.rand.nextInt(8) - this.rand.nextInt(8);
/*  327 */       k++;
/*      */       
/*  329 */       if (k == 10000) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  335 */     this.worldInfo.setSpawnX(i);
/*  336 */     this.worldInfo.setSpawnZ(j);
/*      */   }
/*      */   
/*      */   protected void updateBlocks()
/*      */   {
/*  341 */     super.updateBlocks();
/*      */     
/*  343 */     if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
/*      */     {
/*  345 */       for (ChunkCoordIntPair chunkcoordintpair1 : this.activeChunkSet)
/*      */       {
/*  347 */         getChunkFromChunkCoords(chunkcoordintpair1.chunkXPos, chunkcoordintpair1.chunkZPos).func_150804_b(false);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  352 */       int i = 0;
/*  353 */       int j = 0;
/*      */       
/*  355 */       for (ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet)
/*      */       {
/*  357 */         int k = chunkcoordintpair.chunkXPos * 16;
/*  358 */         int l = chunkcoordintpair.chunkZPos * 16;
/*  359 */         this.theProfiler.startSection("getChunk");
/*  360 */         Chunk chunk = getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
/*  361 */         playMoodSoundAndCheckLight(k, l, chunk);
/*  362 */         this.theProfiler.endStartSection("tickChunk");
/*  363 */         chunk.func_150804_b(false);
/*  364 */         this.theProfiler.endStartSection("thunder");
/*      */         
/*  366 */         if ((this.rand.nextInt(100000) == 0) && (isRaining()) && (isThundering()))
/*      */         {
/*  368 */           this.updateLCG = (this.updateLCG * 3 + 1013904223);
/*  369 */           int i1 = this.updateLCG >> 2;
/*  370 */           BlockPos blockpos = adjustPosToNearbyEntity(new BlockPos(k + (i1 & 0xF), 0, l + (i1 >> 8 & 0xF)));
/*      */           
/*  372 */           if (canLightningStrike(blockpos))
/*      */           {
/*  374 */             addWeatherEffect(new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ()));
/*      */           }
/*      */         }
/*      */         
/*  378 */         this.theProfiler.endStartSection("iceandsnow");
/*      */         BlockPos blockpos1;
/*  380 */         if (this.rand.nextInt(16) == 0)
/*      */         {
/*  382 */           this.updateLCG = (this.updateLCG * 3 + 1013904223);
/*  383 */           int k2 = this.updateLCG >> 2;
/*  384 */           BlockPos blockpos2 = getPrecipitationHeight(new BlockPos(k + (k2 & 0xF), 0, l + (k2 >> 8 & 0xF)));
/*  385 */           blockpos1 = blockpos2.down();
/*      */           
/*  387 */           if (canBlockFreezeNoWater(blockpos1))
/*      */           {
/*  389 */             setBlockState(blockpos1, Blocks.ice.getDefaultState());
/*      */           }
/*      */           
/*  392 */           if ((isRaining()) && (canSnowAt(blockpos2, true)))
/*      */           {
/*  394 */             setBlockState(blockpos2, Blocks.snow_layer.getDefaultState());
/*      */           }
/*      */           
/*  397 */           if ((isRaining()) && (getBiomeGenForCoords(blockpos1).canSpawnLightningBolt()))
/*      */           {
/*  399 */             getBlockState(blockpos1).getBlock().fillWithRain(this, blockpos1);
/*      */           }
/*      */         }
/*      */         
/*  403 */         this.theProfiler.endStartSection("tickBlocks");
/*  404 */         int l2 = getGameRules().getInt("randomTickSpeed");
/*      */         
/*  406 */         if (l2 > 0) {
/*      */           ExtendedBlockStorage[] arrayOfExtendedBlockStorage;
/*  408 */           BlockPos localBlockPos1 = (arrayOfExtendedBlockStorage = chunk.getBlockStorageArray()).length; for (blockpos1 = 0; blockpos1 < localBlockPos1; blockpos1++) { ExtendedBlockStorage extendedblockstorage = arrayOfExtendedBlockStorage[blockpos1];
/*      */             
/*  410 */             if ((extendedblockstorage != null) && (extendedblockstorage.getNeedsRandomTick()))
/*      */             {
/*  412 */               for (int j1 = 0; j1 < l2; j1++)
/*      */               {
/*  414 */                 this.updateLCG = (this.updateLCG * 3 + 1013904223);
/*  415 */                 int k1 = this.updateLCG >> 2;
/*  416 */                 int l1 = k1 & 0xF;
/*  417 */                 int i2 = k1 >> 8 & 0xF;
/*  418 */                 int j2 = k1 >> 16 & 0xF;
/*  419 */                 j++;
/*  420 */                 IBlockState iblockstate = extendedblockstorage.get(l1, j2, i2);
/*  421 */                 Block block = iblockstate.getBlock();
/*      */                 
/*  423 */                 if (block.getTickRandomly())
/*      */                 {
/*  425 */                   i++;
/*  426 */                   block.randomTick(this, new BlockPos(l1 + k, j2 + extendedblockstorage.getYLocation(), i2 + l), iblockstate, this.rand);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  433 */         this.theProfiler.endSection();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected BlockPos adjustPosToNearbyEntity(BlockPos pos)
/*      */   {
/*  440 */     BlockPos blockpos = getPrecipitationHeight(pos);
/*  441 */     AxisAlignedBB axisalignedbb = new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), getHeight(), blockpos.getZ())).expand(3.0D, 3.0D, 3.0D);
/*  442 */     List<EntityLivingBase> list = getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new Predicate()
/*      */     {
/*      */       public boolean apply(EntityLivingBase p_apply_1_)
/*      */       {
/*  446 */         return (p_apply_1_ != null) && (p_apply_1_.isEntityAlive()) && (WorldServer.this.canSeeSky(p_apply_1_.getPosition()));
/*      */       }
/*  448 */     });
/*  449 */     return !list.isEmpty() ? ((EntityLivingBase)list.get(this.rand.nextInt(list.size()))).getPosition() : blockpos;
/*      */   }
/*      */   
/*      */   public boolean isBlockTickPending(BlockPos pos, Block blockType)
/*      */   {
/*  454 */     NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockType);
/*  455 */     return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
/*      */   }
/*      */   
/*      */   public void scheduleUpdate(BlockPos pos, Block blockIn, int delay)
/*      */   {
/*  460 */     updateBlockTick(pos, blockIn, delay, 0);
/*      */   }
/*      */   
/*      */   public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority)
/*      */   {
/*  465 */     NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
/*  466 */     int i = 0;
/*      */     
/*  468 */     if ((this.scheduledUpdatesAreImmediate) && (blockIn.getMaterial() != Material.air))
/*      */     {
/*  470 */       if (blockIn.requiresUpdates())
/*      */       {
/*  472 */         i = 8;
/*      */         
/*  474 */         if (isAreaLoaded(nextticklistentry.position.add(-i, -i, -i), nextticklistentry.position.add(i, i, i)))
/*      */         {
/*  476 */           IBlockState iblockstate = getBlockState(nextticklistentry.position);
/*      */           
/*  478 */           if ((iblockstate.getBlock().getMaterial() != Material.air) && (iblockstate.getBlock() == nextticklistentry.getBlock()))
/*      */           {
/*  480 */             iblockstate.getBlock().updateTick(this, nextticklistentry.position, iblockstate, this.rand);
/*      */           }
/*      */         }
/*      */         
/*  484 */         return;
/*      */       }
/*      */       
/*  487 */       delay = 1;
/*      */     }
/*      */     
/*  490 */     if (isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i)))
/*      */     {
/*  492 */       if (blockIn.getMaterial() != Material.air)
/*      */       {
/*  494 */         nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
/*  495 */         nextticklistentry.setPriority(priority);
/*      */       }
/*      */       
/*  498 */       if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry))
/*      */       {
/*  500 */         this.pendingTickListEntriesHashSet.add(nextticklistentry);
/*  501 */         this.pendingTickListEntriesTreeSet.add(nextticklistentry);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority)
/*      */   {
/*  508 */     NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
/*  509 */     nextticklistentry.setPriority(priority);
/*      */     
/*  511 */     if (blockIn.getMaterial() != Material.air)
/*      */     {
/*  513 */       nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
/*      */     }
/*      */     
/*  516 */     if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry))
/*      */     {
/*  518 */       this.pendingTickListEntriesHashSet.add(nextticklistentry);
/*  519 */       this.pendingTickListEntriesTreeSet.add(nextticklistentry);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateEntities()
/*      */   {
/*  528 */     if (this.playerEntities.isEmpty())
/*      */     {
/*  530 */       if (this.updateEntityTick++ < 1200) {}
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  537 */       resetUpdateEntityTick();
/*      */     }
/*      */     
/*  540 */     super.updateEntities();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetUpdateEntityTick()
/*      */   {
/*  548 */     this.updateEntityTick = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean tickUpdates(boolean p_72955_1_)
/*      */   {
/*  556 */     if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
/*      */     {
/*  558 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  562 */     int i = this.pendingTickListEntriesTreeSet.size();
/*      */     
/*  564 */     if (i != this.pendingTickListEntriesHashSet.size())
/*      */     {
/*  566 */       throw new IllegalStateException("TickNextTick list out of synch");
/*      */     }
/*      */     
/*      */ 
/*  570 */     if (i > 1000)
/*      */     {
/*  572 */       i = 1000;
/*      */     }
/*      */     
/*  575 */     this.theProfiler.startSection("cleaning");
/*      */     
/*  577 */     for (int j = 0; j < i; j++)
/*      */     {
/*  579 */       NextTickListEntry nextticklistentry = (NextTickListEntry)this.pendingTickListEntriesTreeSet.first();
/*      */       
/*  581 */       if ((!p_72955_1_) && (nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime())) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  586 */       this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
/*  587 */       this.pendingTickListEntriesHashSet.remove(nextticklistentry);
/*  588 */       this.pendingTickListEntriesThisTick.add(nextticklistentry);
/*      */     }
/*      */     
/*  591 */     this.theProfiler.endSection();
/*  592 */     this.theProfiler.startSection("ticking");
/*  593 */     Iterator<NextTickListEntry> iterator = this.pendingTickListEntriesThisTick.iterator();
/*      */     
/*  595 */     while (iterator.hasNext())
/*      */     {
/*  597 */       NextTickListEntry nextticklistentry1 = (NextTickListEntry)iterator.next();
/*  598 */       iterator.remove();
/*  599 */       int k = 0;
/*      */       
/*  601 */       if (isAreaLoaded(nextticklistentry1.position.add(-k, -k, -k), nextticklistentry1.position.add(k, k, k)))
/*      */       {
/*  603 */         IBlockState iblockstate = getBlockState(nextticklistentry1.position);
/*      */         
/*  605 */         if ((iblockstate.getBlock().getMaterial() != Material.air) && (Block.isEqualTo(iblockstate.getBlock(), nextticklistentry1.getBlock())))
/*      */         {
/*      */           try
/*      */           {
/*  609 */             iblockstate.getBlock().updateTick(this, nextticklistentry1.position, iblockstate, this.rand);
/*      */           }
/*      */           catch (Throwable throwable)
/*      */           {
/*  613 */             CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
/*  614 */             CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
/*  615 */             CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry1.position, iblockstate);
/*  616 */             throw new ReportedException(crashreport);
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  622 */         scheduleUpdate(nextticklistentry1.position, nextticklistentry1.getBlock(), 0);
/*      */       }
/*      */     }
/*      */     
/*  626 */     this.theProfiler.endSection();
/*  627 */     this.pendingTickListEntriesThisTick.clear();
/*  628 */     return !this.pendingTickListEntriesTreeSet.isEmpty();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_)
/*      */   {
/*  635 */     ChunkCoordIntPair chunkcoordintpair = chunkIn.getChunkCoordIntPair();
/*  636 */     int i = (chunkcoordintpair.chunkXPos << 4) - 2;
/*  637 */     int j = i + 16 + 2;
/*  638 */     int k = (chunkcoordintpair.chunkZPos << 4) - 2;
/*  639 */     int l = k + 16 + 2;
/*  640 */     return func_175712_a(new StructureBoundingBox(i, 0, k, j, 256, l), p_72920_2_);
/*      */   }
/*      */   
/*      */   public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBB, boolean p_175712_2_)
/*      */   {
/*  645 */     List<NextTickListEntry> list = null;
/*      */     
/*  647 */     for (int i = 0; i < 2; i++)
/*      */     {
/*      */       Iterator<NextTickListEntry> iterator;
/*      */       Iterator<NextTickListEntry> iterator;
/*  651 */       if (i == 0)
/*      */       {
/*  653 */         iterator = this.pendingTickListEntriesTreeSet.iterator();
/*      */       }
/*      */       else
/*      */       {
/*  657 */         iterator = this.pendingTickListEntriesThisTick.iterator();
/*      */       }
/*      */       
/*  660 */       while (iterator.hasNext())
/*      */       {
/*  662 */         NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();
/*  663 */         BlockPos blockpos = nextticklistentry.position;
/*      */         
/*  665 */         if ((blockpos.getX() >= structureBB.minX) && (blockpos.getX() < structureBB.maxX) && (blockpos.getZ() >= structureBB.minZ) && (blockpos.getZ() < structureBB.maxZ))
/*      */         {
/*  667 */           if (p_175712_2_)
/*      */           {
/*  669 */             this.pendingTickListEntriesHashSet.remove(nextticklistentry);
/*  670 */             iterator.remove();
/*      */           }
/*      */           
/*  673 */           if (list == null)
/*      */           {
/*  675 */             list = Lists.newArrayList();
/*      */           }
/*      */           
/*  678 */           list.add(nextticklistentry);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  683 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate)
/*      */   {
/*  692 */     if ((!canSpawnAnimals()) && (((entityIn instanceof EntityAnimal)) || ((entityIn instanceof net.minecraft.entity.passive.EntityWaterMob))))
/*      */     {
/*  694 */       entityIn.setDead();
/*      */     }
/*      */     
/*  697 */     if ((!canSpawnNPCs()) && ((entityIn instanceof INpc)))
/*      */     {
/*  699 */       entityIn.setDead();
/*      */     }
/*      */     
/*  702 */     super.updateEntityWithOptionalForce(entityIn, forceUpdate);
/*      */   }
/*      */   
/*      */   private boolean canSpawnNPCs()
/*      */   {
/*  707 */     return this.mcServer.getCanSpawnNPCs();
/*      */   }
/*      */   
/*      */   private boolean canSpawnAnimals()
/*      */   {
/*  712 */     return this.mcServer.getCanSpawnAnimals();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected IChunkProvider createChunkProvider()
/*      */   {
/*  720 */     IChunkLoader ichunkloader = this.saveHandler.getChunkLoader(this.provider);
/*  721 */     this.theChunkProviderServer = new ChunkProviderServer(this, ichunkloader, this.provider.createChunkGenerator());
/*  722 */     return this.theChunkProviderServer;
/*      */   }
/*      */   
/*      */   public List<TileEntity> getTileEntitiesIn(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
/*      */   {
/*  727 */     List<TileEntity> list = Lists.newArrayList();
/*      */     
/*  729 */     for (int i = 0; i < this.loadedTileEntityList.size(); i++)
/*      */     {
/*  731 */       TileEntity tileentity = (TileEntity)this.loadedTileEntityList.get(i);
/*  732 */       BlockPos blockpos = tileentity.getPos();
/*      */       
/*  734 */       if ((blockpos.getX() >= minX) && (blockpos.getY() >= minY) && (blockpos.getZ() >= minZ) && (blockpos.getX() < maxX) && (blockpos.getY() < maxY) && (blockpos.getZ() < maxZ))
/*      */       {
/*  736 */         list.add(tileentity);
/*      */       }
/*      */     }
/*      */     
/*  740 */     return list;
/*      */   }
/*      */   
/*      */   public boolean isBlockModifiable(EntityPlayer player, BlockPos pos)
/*      */   {
/*  745 */     return (!this.mcServer.isBlockProtected(this, pos, player)) && (getWorldBorder().contains(pos));
/*      */   }
/*      */   
/*      */   public void initialize(WorldSettings settings)
/*      */   {
/*  750 */     if (!this.worldInfo.isInitialized())
/*      */     {
/*      */       try
/*      */       {
/*  754 */         createSpawnPosition(settings);
/*      */         
/*  756 */         if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
/*      */         {
/*  758 */           setDebugWorldSettings();
/*      */         }
/*      */         
/*  761 */         super.initialize(settings);
/*      */       }
/*      */       catch (Throwable throwable)
/*      */       {
/*  765 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");
/*      */         
/*      */         try
/*      */         {
/*  769 */           addWorldInfoToCrashReport(crashreport);
/*      */         }
/*      */         catch (Throwable localThrowable1) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  776 */         throw new ReportedException(crashreport);
/*      */       }
/*      */       
/*  779 */       this.worldInfo.setServerInitialized(true);
/*      */     }
/*      */   }
/*      */   
/*      */   private void setDebugWorldSettings()
/*      */   {
/*  785 */     this.worldInfo.setMapFeaturesEnabled(false);
/*  786 */     this.worldInfo.setAllowCommands(true);
/*  787 */     this.worldInfo.setRaining(false);
/*  788 */     this.worldInfo.setThundering(false);
/*  789 */     this.worldInfo.setCleanWeatherTime(1000000000);
/*  790 */     this.worldInfo.setWorldTime(6000L);
/*  791 */     this.worldInfo.setGameType(WorldSettings.GameType.SPECTATOR);
/*  792 */     this.worldInfo.setHardcore(false);
/*  793 */     this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
/*  794 */     this.worldInfo.setDifficultyLocked(true);
/*  795 */     getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void createSpawnPosition(WorldSettings p_73052_1_)
/*      */   {
/*  803 */     if (!this.provider.canRespawnHere())
/*      */     {
/*  805 */       this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
/*      */     }
/*  807 */     else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
/*      */     {
/*  809 */       this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
/*      */     }
/*      */     else
/*      */     {
/*  813 */       this.findingSpawnPoint = true;
/*  814 */       WorldChunkManager worldchunkmanager = this.provider.getWorldChunkManager();
/*  815 */       List<BiomeGenBase> list = worldchunkmanager.getBiomesToSpawnIn();
/*  816 */       Random random = new Random(getSeed());
/*  817 */       BlockPos blockpos = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
/*  818 */       int i = 0;
/*  819 */       int j = this.provider.getAverageGroundLevel();
/*  820 */       int k = 0;
/*      */       
/*  822 */       if (blockpos != null)
/*      */       {
/*  824 */         i = blockpos.getX();
/*  825 */         k = blockpos.getZ();
/*      */       }
/*      */       else
/*      */       {
/*  829 */         logger.warn("Unable to find spawn biome");
/*      */       }
/*      */       
/*  832 */       int l = 0;
/*      */       
/*  834 */       while (!this.provider.canCoordinateBeSpawn(i, k))
/*      */       {
/*  836 */         i += random.nextInt(64) - random.nextInt(64);
/*  837 */         k += random.nextInt(64) - random.nextInt(64);
/*  838 */         l++;
/*      */         
/*  840 */         if (l == 1000) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  846 */       this.worldInfo.setSpawn(new BlockPos(i, j, k));
/*  847 */       this.findingSpawnPoint = false;
/*      */       
/*  849 */       if (p_73052_1_.isBonusChestEnabled())
/*      */       {
/*  851 */         createBonusChest();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void createBonusChest()
/*      */   {
/*  861 */     WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest(bonusChestContent, 10);
/*      */     
/*  863 */     for (int i = 0; i < 10; i++)
/*      */     {
/*  865 */       int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
/*  866 */       int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
/*  867 */       BlockPos blockpos = getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();
/*      */       
/*  869 */       if (worldgeneratorbonuschest.generate(this, this.rand, blockpos)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BlockPos getSpawnCoordinate()
/*      */   {
/*  881 */     return this.provider.getSpawnCoordinate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void saveAllChunks(boolean p_73044_1_, IProgressUpdate progressCallback)
/*      */     throws MinecraftException
/*      */   {
/*  889 */     if (this.chunkProvider.canSave())
/*      */     {
/*  891 */       if (progressCallback != null)
/*      */       {
/*  893 */         progressCallback.displaySavingString("Saving level");
/*      */       }
/*      */       
/*  896 */       saveLevel();
/*      */       
/*  898 */       if (progressCallback != null)
/*      */       {
/*  900 */         progressCallback.displayLoadingString("Saving chunks");
/*      */       }
/*      */       
/*  903 */       this.chunkProvider.saveChunks(p_73044_1_, progressCallback);
/*      */       
/*  905 */       for (Chunk chunk : Lists.newArrayList(this.theChunkProviderServer.func_152380_a()))
/*      */       {
/*  907 */         if ((chunk != null) && (!this.thePlayerManager.hasPlayerInstance(chunk.xPosition, chunk.zPosition)))
/*      */         {
/*  909 */           this.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void saveChunkData()
/*      */   {
/*  920 */     if (this.chunkProvider.canSave())
/*      */     {
/*  922 */       this.chunkProvider.saveExtraData();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void saveLevel()
/*      */     throws MinecraftException
/*      */   {
/*  931 */     checkSessionLock();
/*  932 */     this.worldInfo.setBorderSize(getWorldBorder().getDiameter());
/*  933 */     this.worldInfo.getBorderCenterX(getWorldBorder().getCenterX());
/*  934 */     this.worldInfo.getBorderCenterZ(getWorldBorder().getCenterZ());
/*  935 */     this.worldInfo.setBorderSafeZone(getWorldBorder().getDamageBuffer());
/*  936 */     this.worldInfo.setBorderDamagePerBlock(getWorldBorder().getDamageAmount());
/*  937 */     this.worldInfo.setBorderWarningDistance(getWorldBorder().getWarningDistance());
/*  938 */     this.worldInfo.setBorderWarningTime(getWorldBorder().getWarningTime());
/*  939 */     this.worldInfo.setBorderLerpTarget(getWorldBorder().getTargetSize());
/*  940 */     this.worldInfo.setBorderLerpTime(getWorldBorder().getTimeUntilTarget());
/*  941 */     this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getConfigurationManager().getHostPlayerData());
/*  942 */     this.mapStorage.saveAllData();
/*      */   }
/*      */   
/*      */   protected void onEntityAdded(Entity entityIn)
/*      */   {
/*  947 */     super.onEntityAdded(entityIn);
/*  948 */     this.entitiesById.addKey(entityIn.getEntityId(), entityIn);
/*  949 */     this.entitiesByUuid.put(entityIn.getUniqueID(), entityIn);
/*  950 */     Entity[] aentity = entityIn.getParts();
/*      */     
/*  952 */     if (aentity != null)
/*      */     {
/*  954 */       for (int i = 0; i < aentity.length; i++)
/*      */       {
/*  956 */         this.entitiesById.addKey(aentity[i].getEntityId(), aentity[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void onEntityRemoved(Entity entityIn)
/*      */   {
/*  963 */     super.onEntityRemoved(entityIn);
/*  964 */     this.entitiesById.removeObject(entityIn.getEntityId());
/*  965 */     this.entitiesByUuid.remove(entityIn.getUniqueID());
/*  966 */     Entity[] aentity = entityIn.getParts();
/*      */     
/*  968 */     if (aentity != null)
/*      */     {
/*  970 */       for (int i = 0; i < aentity.length; i++)
/*      */       {
/*  972 */         this.entitiesById.removeObject(aentity[i].getEntityId());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean addWeatherEffect(Entity entityIn)
/*      */   {
/*  982 */     if (super.addWeatherEffect(entityIn))
/*      */     {
/*  984 */       this.mcServer.getConfigurationManager().sendToAllNear(entityIn.posX, entityIn.posY, entityIn.posZ, 512.0D, this.provider.getDimensionId(), new S2CPacketSpawnGlobalEntity(entityIn));
/*  985 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  989 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEntityState(Entity entityIn, byte state)
/*      */   {
/*  998 */     getEntityTracker().func_151248_b(entityIn, new net.minecraft.network.play.server.S19PacketEntityStatus(entityIn, state));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Explosion newExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
/*      */   {
/* 1006 */     Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
/* 1007 */     explosion.doExplosionA();
/* 1008 */     explosion.doExplosionB(false);
/*      */     
/* 1010 */     if (!isSmoking)
/*      */     {
/* 1012 */       explosion.func_180342_d();
/*      */     }
/*      */     
/* 1015 */     for (EntityPlayer entityplayer : this.playerEntities)
/*      */     {
/* 1017 */       if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
/*      */       {
/* 1019 */         ((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), (Vec3)explosion.getPlayerKnockbackMap().get(entityplayer)));
/*      */       }
/*      */     }
/*      */     
/* 1023 */     return explosion;
/*      */   }
/*      */   
/*      */   public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam)
/*      */   {
/* 1028 */     BlockEventData blockeventdata = new BlockEventData(pos, blockIn, eventID, eventParam);
/*      */     
/* 1030 */     for (BlockEventData blockeventdata1 : this.field_147490_S[this.blockEventCacheIndex])
/*      */     {
/* 1032 */       if (blockeventdata1.equals(blockeventdata))
/*      */       {
/* 1034 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1038 */     this.field_147490_S[this.blockEventCacheIndex].add(blockeventdata);
/*      */   }
/*      */   
/*      */   private void sendQueuedBlockEvents()
/*      */   {
/* 1043 */     while (!this.field_147490_S[this.blockEventCacheIndex].isEmpty())
/*      */     {
/* 1045 */       int i = this.blockEventCacheIndex;
/* 1046 */       this.blockEventCacheIndex ^= 0x1;
/*      */       
/* 1048 */       for (BlockEventData blockeventdata : this.field_147490_S[i])
/*      */       {
/* 1050 */         if (fireBlockEvent(blockeventdata))
/*      */         {
/* 1052 */           this.mcServer.getConfigurationManager().sendToAllNear(blockeventdata.getPosition().getX(), blockeventdata.getPosition().getY(), blockeventdata.getPosition().getZ(), 64.0D, this.provider.getDimensionId(), new net.minecraft.network.play.server.S24PacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(), blockeventdata.getEventID(), blockeventdata.getEventParameter()));
/*      */         }
/*      */       }
/*      */       
/* 1056 */       this.field_147490_S[i].clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean fireBlockEvent(BlockEventData event)
/*      */   {
/* 1062 */     IBlockState iblockstate = getBlockState(event.getPosition());
/* 1063 */     return iblockstate.getBlock() == event.getBlock() ? iblockstate.getBlock().onBlockEventReceived(this, event.getPosition(), iblockstate, event.getEventID(), event.getEventParameter()) : false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void flush()
/*      */   {
/* 1071 */     this.saveHandler.flush();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateWeather()
/*      */   {
/* 1079 */     boolean flag = isRaining();
/* 1080 */     super.updateWeather();
/*      */     
/* 1082 */     if (this.prevRainingStrength != this.rainingStrength)
/*      */     {
/* 1084 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionId());
/*      */     }
/*      */     
/* 1087 */     if (this.prevThunderingStrength != this.thunderingStrength)
/*      */     {
/* 1089 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionId());
/*      */     }
/*      */     
/* 1092 */     if (flag != isRaining())
/*      */     {
/* 1094 */       if (flag)
/*      */       {
/* 1096 */         this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
/*      */       }
/*      */       else
/*      */       {
/* 1100 */         this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(1, 0.0F));
/*      */       }
/*      */       
/* 1103 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, this.rainingStrength));
/* 1104 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, this.thunderingStrength));
/*      */     }
/*      */   }
/*      */   
/*      */   protected int getRenderDistanceChunks()
/*      */   {
/* 1110 */     return this.mcServer.getConfigurationManager().getViewDistance();
/*      */   }
/*      */   
/*      */   public MinecraftServer getMinecraftServer()
/*      */   {
/* 1115 */     return this.mcServer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityTracker getEntityTracker()
/*      */   {
/* 1123 */     return this.theEntityTracker;
/*      */   }
/*      */   
/*      */   public PlayerManager getPlayerManager()
/*      */   {
/* 1128 */     return this.thePlayerManager;
/*      */   }
/*      */   
/*      */   public Teleporter getDefaultTeleporter()
/*      */   {
/* 1133 */     return this.worldTeleporter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, int numberOfParticles, double p_175739_9_, double p_175739_11_, double p_175739_13_, double p_175739_15_, int... p_175739_17_)
/*      */   {
/* 1141 */     spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, p_175739_9_, p_175739_11_, p_175739_13_, p_175739_15_, p_175739_17_);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void spawnParticle(EnumParticleTypes particleType, boolean longDistance, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... p_180505_18_)
/*      */   {
/* 1149 */     Packet packet = new net.minecraft.network.play.server.S2APacketParticles(particleType, longDistance, (float)xCoord, (float)yCoord, (float)zCoord, (float)xOffset, (float)yOffset, (float)zOffset, (float)particleSpeed, numberOfParticles, p_180505_18_);
/*      */     
/* 1151 */     for (int i = 0; i < this.playerEntities.size(); i++)
/*      */     {
/* 1153 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntities.get(i);
/* 1154 */       BlockPos blockpos = entityplayermp.getPosition();
/* 1155 */       double d0 = blockpos.distanceSq(xCoord, yCoord, zCoord);
/*      */       
/* 1157 */       if ((d0 <= 256.0D) || ((longDistance) && (d0 <= 65536.0D)))
/*      */       {
/* 1159 */         entityplayermp.playerNetServerHandler.sendPacket(packet);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Entity getEntityFromUuid(UUID uuid)
/*      */   {
/* 1166 */     return (Entity)this.entitiesByUuid.get(uuid);
/*      */   }
/*      */   
/*      */   public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
/*      */   {
/* 1171 */     return this.mcServer.addScheduledTask(runnableToSchedule);
/*      */   }
/*      */   
/*      */   public boolean isCallingFromMinecraftThread()
/*      */   {
/* 1176 */     return this.mcServer.isCallingFromMinecraftThread();
/*      */   }
/*      */   
/*      */   static class ServerBlockEventList
/*      */     extends ArrayList<BlockEventData>
/*      */   {}
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */