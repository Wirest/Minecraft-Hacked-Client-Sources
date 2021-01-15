/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S21PacketChunkData;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.gen.ChunkProviderServer;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PlayerManager
/*     */ {
/*  23 */   private static final Logger pmLogger = ;
/*     */   private final WorldServer theWorldServer;
/*  25 */   private final List<EntityPlayerMP> players = Lists.newArrayList();
/*  26 */   private final LongHashMap playerInstances = new LongHashMap();
/*  27 */   private final List<PlayerInstance> playerInstancesToUpdate = Lists.newArrayList();
/*  28 */   private final List<PlayerInstance> playerInstanceList = Lists.newArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   private int playerViewRadius;
/*     */   
/*     */ 
/*     */ 
/*     */   private long previousTotalWorldTime;
/*     */   
/*     */ 
/*  39 */   private final int[][] xzDirectionsConst = { { 1 }, { 0, 1 }, { -1 }, { 0, -1 } };
/*     */   
/*     */   public PlayerManager(WorldServer serverWorld)
/*     */   {
/*  43 */     this.theWorldServer = serverWorld;
/*  44 */     setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WorldServer getWorldServer()
/*     */   {
/*  52 */     return this.theWorldServer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updatePlayerInstances()
/*     */   {
/*  60 */     long i = this.theWorldServer.getTotalWorldTime();
/*     */     
/*  62 */     if (i - this.previousTotalWorldTime > 8000L)
/*     */     {
/*  64 */       this.previousTotalWorldTime = i;
/*     */       
/*  66 */       for (int j = 0; j < this.playerInstanceList.size(); j++)
/*     */       {
/*  68 */         PlayerInstance playermanager$playerinstance = (PlayerInstance)this.playerInstanceList.get(j);
/*  69 */         playermanager$playerinstance.onUpdate();
/*  70 */         playermanager$playerinstance.processChunk();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  75 */       for (int k = 0; k < this.playerInstancesToUpdate.size(); k++)
/*     */       {
/*  77 */         PlayerInstance playermanager$playerinstance1 = (PlayerInstance)this.playerInstancesToUpdate.get(k);
/*  78 */         playermanager$playerinstance1.onUpdate();
/*     */       }
/*     */     }
/*     */     
/*  82 */     this.playerInstancesToUpdate.clear();
/*     */     
/*  84 */     if (this.players.isEmpty())
/*     */     {
/*  86 */       WorldProvider worldprovider = this.theWorldServer.provider;
/*     */       
/*  88 */       if (!worldprovider.canRespawnHere())
/*     */       {
/*  90 */         this.theWorldServer.theChunkProviderServer.unloadAllChunks();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasPlayerInstance(int chunkX, int chunkZ)
/*     */   {
/*  97 */     long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32;
/*  98 */     return this.playerInstances.getValueByKey(i) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private PlayerInstance getPlayerInstance(int chunkX, int chunkZ, boolean createIfAbsent)
/*     */   {
/* 106 */     long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32;
/* 107 */     PlayerInstance playermanager$playerinstance = (PlayerInstance)this.playerInstances.getValueByKey(i);
/*     */     
/* 109 */     if ((playermanager$playerinstance == null) && (createIfAbsent))
/*     */     {
/* 111 */       playermanager$playerinstance = new PlayerInstance(chunkX, chunkZ);
/* 112 */       this.playerInstances.add(i, playermanager$playerinstance);
/* 113 */       this.playerInstanceList.add(playermanager$playerinstance);
/*     */     }
/*     */     
/* 116 */     return playermanager$playerinstance;
/*     */   }
/*     */   
/*     */   public void markBlockForUpdate(BlockPos pos)
/*     */   {
/* 121 */     int i = pos.getX() >> 4;
/* 122 */     int j = pos.getZ() >> 4;
/* 123 */     PlayerInstance playermanager$playerinstance = getPlayerInstance(i, j, false);
/*     */     
/* 125 */     if (playermanager$playerinstance != null)
/*     */     {
/* 127 */       playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addPlayer(EntityPlayerMP player)
/*     */   {
/* 136 */     int i = (int)player.posX >> 4;
/* 137 */     int j = (int)player.posZ >> 4;
/* 138 */     player.managedPosX = player.posX;
/* 139 */     player.managedPosZ = player.posZ;
/*     */     
/* 141 */     for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; k++)
/*     */     {
/* 143 */       for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++)
/*     */       {
/* 145 */         getPlayerInstance(k, l, true).addPlayer(player);
/*     */       }
/*     */     }
/*     */     
/* 149 */     this.players.add(player);
/* 150 */     filterChunkLoadQueue(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void filterChunkLoadQueue(EntityPlayerMP player)
/*     */   {
/* 158 */     List<ChunkCoordIntPair> list = Lists.newArrayList(player.loadedChunks);
/* 159 */     int i = 0;
/* 160 */     int j = this.playerViewRadius;
/* 161 */     int k = (int)player.posX >> 4;
/* 162 */     int l = (int)player.posZ >> 4;
/* 163 */     int i1 = 0;
/* 164 */     int j1 = 0;
/* 165 */     ChunkCoordIntPair chunkcoordintpair = getPlayerInstance(k, l, true).chunkCoords;
/* 166 */     player.loadedChunks.clear();
/*     */     
/* 168 */     if (list.contains(chunkcoordintpair))
/*     */     {
/* 170 */       player.loadedChunks.add(chunkcoordintpair);
/*     */     }
/*     */     
/* 173 */     for (int k1 = 1; k1 <= j * 2; k1++)
/*     */     {
/* 175 */       for (int l1 = 0; l1 < 2; l1++)
/*     */       {
/* 177 */         int[] aint = this.xzDirectionsConst[(i++ % 4)];
/*     */         
/* 179 */         for (int i2 = 0; i2 < k1; i2++)
/*     */         {
/* 181 */           i1 += aint[0];
/* 182 */           j1 += aint[1];
/* 183 */           chunkcoordintpair = getPlayerInstance(k + i1, l + j1, true).chunkCoords;
/*     */           
/* 185 */           if (list.contains(chunkcoordintpair))
/*     */           {
/* 187 */             player.loadedChunks.add(chunkcoordintpair);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 193 */     i %= 4;
/*     */     
/* 195 */     for (int j2 = 0; j2 < j * 2; j2++)
/*     */     {
/* 197 */       i1 += this.xzDirectionsConst[i][0];
/* 198 */       j1 += this.xzDirectionsConst[i][1];
/* 199 */       chunkcoordintpair = getPlayerInstance(k + i1, l + j1, true).chunkCoords;
/*     */       
/* 201 */       if (list.contains(chunkcoordintpair))
/*     */       {
/* 203 */         player.loadedChunks.add(chunkcoordintpair);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePlayer(EntityPlayerMP player)
/*     */   {
/* 213 */     int i = (int)player.managedPosX >> 4;
/* 214 */     int j = (int)player.managedPosZ >> 4;
/*     */     
/* 216 */     for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; k++)
/*     */     {
/* 218 */       for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++)
/*     */       {
/* 220 */         PlayerInstance playermanager$playerinstance = getPlayerInstance(k, l, false);
/*     */         
/* 222 */         if (playermanager$playerinstance != null)
/*     */         {
/* 224 */           playermanager$playerinstance.removePlayer(player);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 229 */     this.players.remove(player);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean overlaps(int x1, int z1, int x2, int z2, int radius)
/*     */   {
/* 238 */     int i = x1 - x2;
/* 239 */     int j = z1 - z2;
/* 240 */     return (j >= -radius) && (j <= radius);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateMountedMovingPlayer(EntityPlayerMP player)
/*     */   {
/* 248 */     int i = (int)player.posX >> 4;
/* 249 */     int j = (int)player.posZ >> 4;
/* 250 */     double d0 = player.managedPosX - player.posX;
/* 251 */     double d1 = player.managedPosZ - player.posZ;
/* 252 */     double d2 = d0 * d0 + d1 * d1;
/*     */     
/* 254 */     if (d2 >= 64.0D)
/*     */     {
/* 256 */       int k = (int)player.managedPosX >> 4;
/* 257 */       int l = (int)player.managedPosZ >> 4;
/* 258 */       int i1 = this.playerViewRadius;
/* 259 */       int j1 = i - k;
/* 260 */       int k1 = j - l;
/*     */       
/* 262 */       if ((j1 != 0) || (k1 != 0))
/*     */       {
/* 264 */         for (int l1 = i - i1; l1 <= i + i1; l1++)
/*     */         {
/* 266 */           for (int i2 = j - i1; i2 <= j + i1; i2++)
/*     */           {
/* 268 */             if (!overlaps(l1, i2, k, l, i1))
/*     */             {
/* 270 */               getPlayerInstance(l1, i2, true).addPlayer(player);
/*     */             }
/*     */             
/* 273 */             if (!overlaps(l1 - j1, i2 - k1, i, j, i1))
/*     */             {
/* 275 */               PlayerInstance playermanager$playerinstance = getPlayerInstance(l1 - j1, i2 - k1, false);
/*     */               
/* 277 */               if (playermanager$playerinstance != null)
/*     */               {
/* 279 */                 playermanager$playerinstance.removePlayer(player);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 285 */         filterChunkLoadQueue(player);
/* 286 */         player.managedPosX = player.posX;
/* 287 */         player.managedPosZ = player.posZ;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ)
/*     */   {
/* 294 */     PlayerInstance playermanager$playerinstance = getPlayerInstance(chunkX, chunkZ, false);
/* 295 */     return (playermanager$playerinstance != null) && (playermanager$playerinstance.playersWatchingChunk.contains(player)) && (!player.loadedChunks.contains(playermanager$playerinstance.chunkCoords));
/*     */   }
/*     */   
/*     */   public void setPlayerViewRadius(int radius)
/*     */   {
/* 300 */     radius = net.minecraft.util.MathHelper.clamp_int(radius, 3, 32);
/*     */     
/* 302 */     if (radius != this.playerViewRadius)
/*     */     {
/* 304 */       int i = radius - this.playerViewRadius;
/*     */       
/* 306 */       for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players))
/*     */       {
/* 308 */         int j = (int)entityplayermp.posX >> 4;
/* 309 */         int k = (int)entityplayermp.posZ >> 4;
/*     */         
/* 311 */         if (i > 0)
/*     */         {
/* 313 */           for (int j1 = j - radius; j1 <= j + radius; j1++)
/*     */           {
/* 315 */             for (int k1 = k - radius; k1 <= k + radius; k1++)
/*     */             {
/* 317 */               PlayerInstance playermanager$playerinstance = getPlayerInstance(j1, k1, true);
/*     */               
/* 319 */               if (!playermanager$playerinstance.playersWatchingChunk.contains(entityplayermp))
/*     */               {
/* 321 */                 playermanager$playerinstance.addPlayer(entityplayermp);
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */         } else {
/* 328 */           for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; l++)
/*     */           {
/* 330 */             for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; i1++)
/*     */             {
/* 332 */               if (!overlaps(l, i1, j, k, radius))
/*     */               {
/* 334 */                 getPlayerInstance(l, i1, true).removePlayer(entityplayermp);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 341 */       this.playerViewRadius = radius;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getFurthestViewableBlock(int distance)
/*     */   {
/* 350 */     return distance * 16 - 16;
/*     */   }
/*     */   
/*     */   class PlayerInstance
/*     */   {
/* 355 */     private final List<EntityPlayerMP> playersWatchingChunk = Lists.newArrayList();
/*     */     private final ChunkCoordIntPair chunkCoords;
/* 357 */     private short[] locationOfBlockChange = new short[64];
/*     */     private int numBlocksToUpdate;
/*     */     private int flagsYAreasToUpdate;
/*     */     private long previousWorldTime;
/*     */     
/*     */     public PlayerInstance(int chunkX, int chunkZ)
/*     */     {
/* 364 */       this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
/* 365 */       PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ);
/*     */     }
/*     */     
/*     */     public void addPlayer(EntityPlayerMP player)
/*     */     {
/* 370 */       if (this.playersWatchingChunk.contains(player))
/*     */       {
/* 372 */         PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] { player, Integer.valueOf(this.chunkCoords.chunkXPos), Integer.valueOf(this.chunkCoords.chunkZPos) });
/*     */       }
/*     */       else
/*     */       {
/* 376 */         if (this.playersWatchingChunk.isEmpty())
/*     */         {
/* 378 */           this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
/*     */         }
/*     */         
/* 381 */         this.playersWatchingChunk.add(player);
/* 382 */         player.loadedChunks.add(this.chunkCoords);
/*     */       }
/*     */     }
/*     */     
/*     */     public void removePlayer(EntityPlayerMP player)
/*     */     {
/* 388 */       if (this.playersWatchingChunk.contains(player))
/*     */       {
/* 390 */         Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
/*     */         
/* 392 */         if (chunk.isPopulated())
/*     */         {
/* 394 */           player.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
/*     */         }
/*     */         
/* 397 */         this.playersWatchingChunk.remove(player);
/* 398 */         player.loadedChunks.remove(this.chunkCoords);
/*     */         
/* 400 */         if (this.playersWatchingChunk.isEmpty())
/*     */         {
/* 402 */           long i = this.chunkCoords.chunkXPos + 2147483647L | this.chunkCoords.chunkZPos + 2147483647L << 32;
/* 403 */           increaseInhabitedTime(chunk);
/* 404 */           PlayerManager.this.playerInstances.remove(i);
/* 405 */           PlayerManager.this.playerInstanceList.remove(this);
/*     */           
/* 407 */           if (this.numBlocksToUpdate > 0)
/*     */           {
/* 409 */             PlayerManager.this.playerInstancesToUpdate.remove(this);
/*     */           }
/*     */           
/* 412 */           PlayerManager.this.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void processChunk()
/*     */     {
/* 419 */       increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
/*     */     }
/*     */     
/*     */     private void increaseInhabitedTime(Chunk theChunk)
/*     */     {
/* 424 */       theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
/* 425 */       this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
/*     */     }
/*     */     
/*     */     public void flagChunkForUpdate(int x, int y, int z)
/*     */     {
/* 430 */       if (this.numBlocksToUpdate == 0)
/*     */       {
/* 432 */         PlayerManager.this.playerInstancesToUpdate.add(this);
/*     */       }
/*     */       
/* 435 */       this.flagsYAreasToUpdate |= 1 << (y >> 4);
/*     */       
/* 437 */       if (this.numBlocksToUpdate < 64)
/*     */       {
/* 439 */         short short1 = (short)(x << 12 | z << 8 | y);
/*     */         
/* 441 */         for (int i = 0; i < this.numBlocksToUpdate; i++)
/*     */         {
/* 443 */           if (this.locationOfBlockChange[i] == short1)
/*     */           {
/* 445 */             return;
/*     */           }
/*     */         }
/*     */         
/* 449 */         this.locationOfBlockChange[(this.numBlocksToUpdate++)] = short1;
/*     */       }
/*     */     }
/*     */     
/*     */     public void sendToAllPlayersWatchingChunk(Packet thePacket)
/*     */     {
/* 455 */       for (int i = 0; i < this.playersWatchingChunk.size(); i++)
/*     */       {
/* 457 */         EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playersWatchingChunk.get(i);
/*     */         
/* 459 */         if (!entityplayermp.loadedChunks.contains(this.chunkCoords))
/*     */         {
/* 461 */           entityplayermp.playerNetServerHandler.sendPacket(thePacket);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void onUpdate()
/*     */     {
/* 468 */       if (this.numBlocksToUpdate != 0)
/*     */       {
/* 470 */         if (this.numBlocksToUpdate == 1)
/*     */         {
/* 472 */           int i = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
/* 473 */           int j = this.locationOfBlockChange[0] & 0xFF;
/* 474 */           int k = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
/* 475 */           BlockPos blockpos = new BlockPos(i, j, k);
/* 476 */           sendToAllPlayersWatchingChunk(new net.minecraft.network.play.server.S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));
/*     */           
/* 478 */           if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity())
/*     */           {
/* 480 */             sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
/*     */           }
/*     */         }
/* 483 */         else if (this.numBlocksToUpdate == 64)
/*     */         {
/* 485 */           int i1 = this.chunkCoords.chunkXPos * 16;
/* 486 */           int k1 = this.chunkCoords.chunkZPos * 16;
/* 487 */           sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));
/*     */           
/* 489 */           for (int i2 = 0; i2 < 16; i2++)
/*     */           {
/* 491 */             if ((this.flagsYAreasToUpdate & 1 << i2) != 0)
/*     */             {
/* 493 */               int k2 = i2 << 4;
/* 494 */               List<TileEntity> list = PlayerManager.this.theWorldServer.getTileEntitiesIn(i1, k2, k1, i1 + 16, k2 + 16, k1 + 16);
/*     */               
/* 496 */               for (int l = 0; l < list.size(); l++)
/*     */               {
/* 498 */                 sendTileToAllPlayersWatchingChunk((TileEntity)list.get(l));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 505 */           sendToAllPlayersWatchingChunk(new net.minecraft.network.play.server.S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
/*     */           
/* 507 */           for (int j1 = 0; j1 < this.numBlocksToUpdate; j1++)
/*     */           {
/* 509 */             int l1 = (this.locationOfBlockChange[j1] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
/* 510 */             int j2 = this.locationOfBlockChange[j1] & 0xFF;
/* 511 */             int l2 = (this.locationOfBlockChange[j1] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
/* 512 */             BlockPos blockpos1 = new BlockPos(l1, j2, l2);
/*     */             
/* 514 */             if (PlayerManager.this.theWorldServer.getBlockState(blockpos1).getBlock().hasTileEntity())
/*     */             {
/* 516 */               sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos1));
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 521 */         this.numBlocksToUpdate = 0;
/* 522 */         this.flagsYAreasToUpdate = 0;
/*     */       }
/*     */     }
/*     */     
/*     */     private void sendTileToAllPlayersWatchingChunk(TileEntity theTileEntity)
/*     */     {
/* 528 */       if (theTileEntity != null)
/*     */       {
/* 530 */         Packet packet = theTileEntity.getDescriptionPacket();
/*     */         
/* 532 */         if (packet != null)
/*     */         {
/* 534 */           sendToAllPlayersWatchingChunk(packet);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\PlayerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */