/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.NetworkManager;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.IntHashMap;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import optfine.BlockPosM;
/*     */ 
/*     */ public class WorldClient extends World
/*     */ {
/*     */   private NetHandlerPlayClient sendQueue;
/*     */   private ChunkProviderClient clientChunkProvider;
/*  48 */   private final Set entityList = Sets.newHashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private final Set entitySpawnQueue = Sets.newHashSet();
/*  55 */   private final Minecraft mc = Minecraft.getMinecraft();
/*  56 */   private final Set previousActiveChunkSet = Sets.newHashSet();
/*     */   private static final String __OBFID = "CL_00000882";
/*  58 */   private BlockPosM randomTickPosM = new BlockPosM(0, 0, 0, 3);
/*     */   
/*     */   public WorldClient(NetHandlerPlayClient p_i45063_1_, WorldSettings p_i45063_2_, int p_i45063_3_, EnumDifficulty p_i45063_4_, Profiler p_i45063_5_)
/*     */   {
/*  62 */     super(new net.minecraft.world.storage.SaveHandlerMP(), new WorldInfo(p_i45063_2_, "MpServer"), WorldProvider.getProviderForDimension(p_i45063_3_), p_i45063_5_, true);
/*  63 */     this.sendQueue = p_i45063_1_;
/*  64 */     getWorldInfo().setDifficulty(p_i45063_4_);
/*  65 */     this.provider.registerWorld(this);
/*  66 */     setSpawnPoint(new BlockPos(8, 64, 8));
/*  67 */     this.chunkProvider = createChunkProvider();
/*  68 */     this.mapStorage = new net.minecraft.world.storage.SaveDataMemoryStorage();
/*  69 */     calculateInitialSkylight();
/*  70 */     calculateInitialWeather();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void tick()
/*     */   {
/*  78 */     super.tick();
/*  79 */     setTotalWorldTime(getTotalWorldTime() + 1L);
/*     */     
/*  81 */     if (getGameRules().getBoolean("doDaylightCycle"))
/*     */     {
/*  83 */       setWorldTime(getWorldTime() + 1L);
/*     */     }
/*     */     
/*  86 */     this.theProfiler.startSection("reEntryProcessing");
/*     */     
/*  88 */     for (int i = 0; (i < 10) && (!this.entitySpawnQueue.isEmpty()); i++)
/*     */     {
/*  90 */       Entity entity = (Entity)this.entitySpawnQueue.iterator().next();
/*  91 */       this.entitySpawnQueue.remove(entity);
/*     */       
/*  93 */       if (!this.loadedEntityList.contains(entity))
/*     */       {
/*  95 */         spawnEntityInWorld(entity);
/*     */       }
/*     */     }
/*     */     
/*  99 */     this.theProfiler.endStartSection("chunkCache");
/* 100 */     this.clientChunkProvider.unloadQueuedChunks();
/* 101 */     this.theProfiler.endStartSection("blocks");
/* 102 */     updateBlocks();
/* 103 */     this.theProfiler.endSection();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invalidateBlockReceiveRegion(int p_73031_1_, int p_73031_2_, int p_73031_3_, int p_73031_4_, int p_73031_5_, int p_73031_6_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected IChunkProvider createChunkProvider()
/*     */   {
/* 119 */     this.clientChunkProvider = new ChunkProviderClient(this);
/* 120 */     return this.clientChunkProvider;
/*     */   }
/*     */   
/*     */   protected void updateBlocks()
/*     */   {
/* 125 */     super.updateBlocks();
/* 126 */     this.previousActiveChunkSet.retainAll(this.activeChunkSet);
/*     */     
/* 128 */     if (this.previousActiveChunkSet.size() == this.activeChunkSet.size())
/*     */     {
/* 130 */       this.previousActiveChunkSet.clear();
/*     */     }
/*     */     
/* 133 */     int i = 0;
/*     */     
/* 135 */     for (ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet)
/*     */     {
/* 137 */       if (!this.previousActiveChunkSet.contains(chunkcoordintpair))
/*     */       {
/* 139 */         int j = chunkcoordintpair.chunkXPos * 16;
/* 140 */         int k = chunkcoordintpair.chunkZPos * 16;
/* 141 */         this.theProfiler.startSection("getChunk");
/* 142 */         Chunk chunk = getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
/* 143 */         playMoodSoundAndCheckLight(j, k, chunk);
/* 144 */         this.theProfiler.endSection();
/* 145 */         this.previousActiveChunkSet.add(chunkcoordintpair);
/* 146 */         i++;
/*     */         
/* 148 */         if (i >= 10)
/*     */         {
/* 150 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void doPreChunk(int p_73025_1_, int p_73025_2_, boolean p_73025_3_)
/*     */   {
/* 158 */     if (p_73025_3_)
/*     */     {
/* 160 */       this.clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
/*     */     }
/*     */     else
/*     */     {
/* 164 */       this.clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
/*     */     }
/*     */     
/* 167 */     if (!p_73025_3_)
/*     */     {
/* 169 */       markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16, p_73025_1_ * 16 + 15, 256, p_73025_2_ * 16 + 15);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean spawnEntityInWorld(Entity entityIn)
/*     */   {
/* 178 */     boolean flag = super.spawnEntityInWorld(entityIn);
/* 179 */     this.entityList.add(entityIn);
/*     */     
/* 181 */     if (!flag)
/*     */     {
/* 183 */       this.entitySpawnQueue.add(entityIn);
/*     */     }
/* 185 */     else if ((entityIn instanceof EntityMinecart))
/*     */     {
/* 187 */       this.mc.getSoundHandler().playSound(new net.minecraft.client.audio.MovingSoundMinecart((EntityMinecart)entityIn));
/*     */     }
/*     */     
/* 190 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeEntity(Entity entityIn)
/*     */   {
/* 198 */     super.removeEntity(entityIn);
/* 199 */     this.entityList.remove(entityIn);
/*     */   }
/*     */   
/*     */   protected void onEntityAdded(Entity entityIn)
/*     */   {
/* 204 */     super.onEntityAdded(entityIn);
/*     */     
/* 206 */     if (this.entitySpawnQueue.contains(entityIn))
/*     */     {
/* 208 */       this.entitySpawnQueue.remove(entityIn);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onEntityRemoved(Entity entityIn)
/*     */   {
/* 214 */     super.onEntityRemoved(entityIn);
/* 215 */     boolean flag = false;
/*     */     
/* 217 */     if (this.entityList.contains(entityIn))
/*     */     {
/* 219 */       if (entityIn.isEntityAlive())
/*     */       {
/* 221 */         this.entitySpawnQueue.add(entityIn);
/* 222 */         flag = true;
/*     */       }
/*     */       else
/*     */       {
/* 226 */         this.entityList.remove(entityIn);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEntityToWorld(int p_73027_1_, Entity p_73027_2_)
/*     */   {
/* 236 */     Entity entity = getEntityByID(p_73027_1_);
/*     */     
/* 238 */     if (entity != null)
/*     */     {
/* 240 */       removeEntity(entity);
/*     */     }
/*     */     
/* 243 */     this.entityList.add(p_73027_2_);
/* 244 */     p_73027_2_.setEntityId(p_73027_1_);
/*     */     
/* 246 */     if (!spawnEntityInWorld(p_73027_2_))
/*     */     {
/* 248 */       this.entitySpawnQueue.add(p_73027_2_);
/*     */     }
/*     */     
/* 251 */     this.entitiesById.addKey(p_73027_1_, p_73027_2_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Entity getEntityByID(int id)
/*     */   {
/* 259 */     return id == this.mc.thePlayer.getEntityId() ? this.mc.thePlayer : super.getEntityByID(id);
/*     */   }
/*     */   
/*     */   public Entity removeEntityFromWorld(int p_73028_1_)
/*     */   {
/* 264 */     Entity entity = (Entity)this.entitiesById.removeObject(p_73028_1_);
/*     */     
/* 266 */     if (entity != null)
/*     */     {
/* 268 */       this.entityList.remove(entity);
/* 269 */       removeEntity(entity);
/*     */     }
/*     */     
/* 272 */     return entity;
/*     */   }
/*     */   
/*     */   public boolean invalidateRegionAndSetBlock(BlockPos p_180503_1_, IBlockState p_180503_2_)
/*     */   {
/* 277 */     int i = p_180503_1_.getX();
/* 278 */     int j = p_180503_1_.getY();
/* 279 */     int k = p_180503_1_.getZ();
/* 280 */     invalidateBlockReceiveRegion(i, j, k, i, j, k);
/* 281 */     return super.setBlockState(p_180503_1_, p_180503_2_, 3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendQuittingDisconnectingPacket()
/*     */   {
/* 289 */     this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateWeather() {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected int getRenderDistanceChunks()
/*     */   {
/* 301 */     return this.mc.gameSettings.renderDistanceChunks;
/*     */   }
/*     */   
/*     */   public void doVoidFogParticles(int p_73029_1_, int p_73029_2_, int p_73029_3_)
/*     */   {
/* 306 */     byte b0 = 16;
/* 307 */     Random random = new Random();
/* 308 */     ItemStack itemstack = this.mc.thePlayer.getHeldItem();
/* 309 */     boolean flag = (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE) && (itemstack != null) && (Block.getBlockFromItem(itemstack.getItem()) == Blocks.barrier);
/* 310 */     BlockPosM blockposm = this.randomTickPosM;
/*     */     
/* 312 */     for (int i = 0; i < 1000; i++)
/*     */     {
/* 314 */       int j = p_73029_1_ + this.rand.nextInt(b0) - this.rand.nextInt(b0);
/* 315 */       int k = p_73029_2_ + this.rand.nextInt(b0) - this.rand.nextInt(b0);
/* 316 */       int l = p_73029_3_ + this.rand.nextInt(b0) - this.rand.nextInt(b0);
/* 317 */       blockposm.setXyz(j, k, l);
/* 318 */       IBlockState iblockstate = getBlockState(blockposm);
/* 319 */       iblockstate.getBlock().randomDisplayTick(this, blockposm, iblockstate, random);
/*     */       
/* 321 */       if ((flag) && (iblockstate.getBlock() == Blocks.barrier))
/*     */       {
/* 323 */         spawnParticle(EnumParticleTypes.BARRIER, j + 0.5F, k + 0.5F, l + 0.5F, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAllEntities()
/*     */   {
/* 333 */     this.loadedEntityList.removeAll(this.unloadedEntityList);
/*     */     
/* 335 */     for (int i = 0; i < this.unloadedEntityList.size(); i++)
/*     */     {
/* 337 */       Entity entity = (Entity)this.unloadedEntityList.get(i);
/* 338 */       int j = entity.chunkCoordX;
/* 339 */       int k = entity.chunkCoordZ;
/*     */       
/* 341 */       if ((entity.addedToChunk) && (isChunkLoaded(j, k, true)))
/*     */       {
/* 343 */         getChunkFromChunkCoords(j, k).removeEntity(entity);
/*     */       }
/*     */     }
/*     */     
/* 347 */     for (int l = 0; l < this.unloadedEntityList.size(); l++)
/*     */     {
/* 349 */       onEntityRemoved((Entity)this.unloadedEntityList.get(l));
/*     */     }
/*     */     
/* 352 */     this.unloadedEntityList.clear();
/*     */     
/* 354 */     for (int i1 = 0; i1 < this.loadedEntityList.size(); i1++)
/*     */     {
/* 356 */       Entity entity1 = (Entity)this.loadedEntityList.get(i1);
/*     */       
/* 358 */       if (entity1.ridingEntity != null)
/*     */       {
/* 360 */         if ((entity1.ridingEntity.isDead) || (entity1.ridingEntity.riddenByEntity != entity1))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 365 */           entity1.ridingEntity.riddenByEntity = null;
/* 366 */           entity1.ridingEntity = null;
/*     */         }
/*     */       }
/* 369 */       else if (entity1.isDead)
/*     */       {
/* 371 */         int j1 = entity1.chunkCoordX;
/* 372 */         int k1 = entity1.chunkCoordZ;
/*     */         
/* 374 */         if ((entity1.addedToChunk) && (isChunkLoaded(j1, k1, true)))
/*     */         {
/* 376 */           getChunkFromChunkCoords(j1, k1).removeEntity(entity1);
/*     */         }
/*     */         
/* 379 */         this.loadedEntityList.remove(i1--);
/* 380 */         onEntityRemoved(entity1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CrashReportCategory addWorldInfoToCrashReport(CrashReport report)
/*     */   {
/* 390 */     CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
/* 391 */     crashreportcategory.addCrashSectionCallable("Forced entities", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00000883";
/*     */       
/*     */       public String call() {
/* 396 */         return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
/*     */       }
/* 398 */     });
/* 399 */     crashreportcategory.addCrashSectionCallable("Retry entities", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00000884";
/*     */       
/*     */       public String call() {
/* 404 */         return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue.toString();
/*     */       }
/* 406 */     });
/* 407 */     crashreportcategory.addCrashSectionCallable("Server brand", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00000885";
/*     */       
/*     */       public String call() throws Exception {
/* 412 */         return WorldClient.this.mc.thePlayer.getClientBrand();
/*     */       }
/* 414 */     });
/* 415 */     crashreportcategory.addCrashSectionCallable("Server type", new Callable()
/*     */     {
/*     */       private static final String __OBFID = "CL_00000886";
/*     */       
/*     */       public String call() throws Exception {
/* 420 */         return WorldClient.this.mc.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
/*     */       }
/* 422 */     });
/* 423 */     return crashreportcategory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playSoundAtPos(BlockPos p_175731_1_, String p_175731_2_, float p_175731_3_, float p_175731_4_, boolean p_175731_5_)
/*     */   {
/* 431 */     playSound(p_175731_1_.getX() + 0.5D, p_175731_1_.getY() + 0.5D, p_175731_1_.getZ() + 0.5D, p_175731_2_, p_175731_3_, p_175731_4_, p_175731_5_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay)
/*     */   {
/* 439 */     double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
/* 440 */     PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float)x, (float)y, (float)z);
/*     */     
/* 442 */     if ((distanceDelay) && (d0 > 100.0D))
/*     */     {
/* 444 */       double d1 = Math.sqrt(d0) / 40.0D;
/* 445 */       this.mc.getSoundHandler().playDelayedSound(positionedsoundrecord, (int)(d1 * 20.0D));
/*     */     }
/*     */     else
/*     */     {
/* 449 */       this.mc.getSoundHandler().playSound(positionedsoundrecord);
/*     */     }
/*     */   }
/*     */   
/*     */   public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund)
/*     */   {
/* 455 */     this.mc.effectRenderer.addEffect(new net.minecraft.client.particle.EntityFirework.StarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
/*     */   }
/*     */   
/*     */   public void setWorldScoreboard(Scoreboard p_96443_1_)
/*     */   {
/* 460 */     this.worldScoreboard = p_96443_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWorldTime(long time)
/*     */   {
/* 468 */     if (time < 0L)
/*     */     {
/* 470 */       time = -time;
/* 471 */       getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
/*     */     }
/*     */     else
/*     */     {
/* 475 */       getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
/*     */     }
/*     */     
/* 478 */     super.setWorldTime(time);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\multiplayer\WorldClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */