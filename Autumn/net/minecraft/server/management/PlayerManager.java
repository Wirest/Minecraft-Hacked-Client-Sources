package net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerManager {
   private static final Logger pmLogger = LogManager.getLogger();
   private final WorldServer theWorldServer;
   private final List players = Lists.newArrayList();
   private final LongHashMap playerInstances = new LongHashMap();
   private final List playerInstancesToUpdate = Lists.newArrayList();
   private final List playerInstanceList = Lists.newArrayList();
   private int playerViewRadius;
   private long previousTotalWorldTime;
   private final int[][] xzDirectionsConst = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

   public PlayerManager(WorldServer serverWorld) {
      this.theWorldServer = serverWorld;
      this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
   }

   public WorldServer getWorldServer() {
      return this.theWorldServer;
   }

   public void updatePlayerInstances() {
      long i = this.theWorldServer.getTotalWorldTime();
      int j;
      PlayerManager.PlayerInstance playermanager$playerinstance;
      if (i - this.previousTotalWorldTime > 8000L) {
         this.previousTotalWorldTime = i;

         for(j = 0; j < this.playerInstanceList.size(); ++j) {
            playermanager$playerinstance = (PlayerManager.PlayerInstance)this.playerInstanceList.get(j);
            playermanager$playerinstance.onUpdate();
            playermanager$playerinstance.processChunk();
         }
      } else {
         for(j = 0; j < this.playerInstancesToUpdate.size(); ++j) {
            playermanager$playerinstance = (PlayerManager.PlayerInstance)this.playerInstancesToUpdate.get(j);
            playermanager$playerinstance.onUpdate();
         }
      }

      this.playerInstancesToUpdate.clear();
      if (this.players.isEmpty()) {
         WorldProvider worldprovider = this.theWorldServer.provider;
         if (!worldprovider.canRespawnHere()) {
            this.theWorldServer.theChunkProviderServer.unloadAllChunks();
         }
      }

   }

   public boolean hasPlayerInstance(int chunkX, int chunkZ) {
      long i = (long)chunkX + 2147483647L | (long)chunkZ + 2147483647L << 32;
      return this.playerInstances.getValueByKey(i) != null;
   }

   private PlayerManager.PlayerInstance getPlayerInstance(int chunkX, int chunkZ, boolean createIfAbsent) {
      long i = (long)chunkX + 2147483647L | (long)chunkZ + 2147483647L << 32;
      PlayerManager.PlayerInstance playermanager$playerinstance = (PlayerManager.PlayerInstance)this.playerInstances.getValueByKey(i);
      if (playermanager$playerinstance == null && createIfAbsent) {
         playermanager$playerinstance = new PlayerManager.PlayerInstance(chunkX, chunkZ);
         this.playerInstances.add(i, playermanager$playerinstance);
         this.playerInstanceList.add(playermanager$playerinstance);
      }

      return playermanager$playerinstance;
   }

   public void markBlockForUpdate(BlockPos pos) {
      int i = pos.getX() >> 4;
      int j = pos.getZ() >> 4;
      PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(i, j, false);
      if (playermanager$playerinstance != null) {
         playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
      }

   }

   public void addPlayer(EntityPlayerMP player) {
      int i = (int)player.posX >> 4;
      int j = (int)player.posZ >> 4;
      player.managedPosX = player.posX;
      player.managedPosZ = player.posZ;

      for(int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
         for(int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
            this.getPlayerInstance(k, l, true).addPlayer(player);
         }
      }

      this.players.add(player);
      this.filterChunkLoadQueue(player);
   }

   public void filterChunkLoadQueue(EntityPlayerMP player) {
      List list = Lists.newArrayList(player.loadedChunks);
      int i = 0;
      int j = this.playerViewRadius;
      int k = (int)player.posX >> 4;
      int l = (int)player.posZ >> 4;
      int i1 = 0;
      int j1 = 0;
      ChunkCoordIntPair chunkcoordintpair = this.getPlayerInstance(k, l, true).chunkCoords;
      player.loadedChunks.clear();
      if (list.contains(chunkcoordintpair)) {
         player.loadedChunks.add(chunkcoordintpair);
      }

      int k1;
      for(k1 = 1; k1 <= j * 2; ++k1) {
         for(int l1 = 0; l1 < 2; ++l1) {
            int[] aint = this.xzDirectionsConst[i++ % 4];

            for(int i2 = 0; i2 < k1; ++i2) {
               i1 += aint[0];
               j1 += aint[1];
               chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;
               if (list.contains(chunkcoordintpair)) {
                  player.loadedChunks.add(chunkcoordintpair);
               }
            }
         }
      }

      i %= 4;

      for(k1 = 0; k1 < j * 2; ++k1) {
         i1 += this.xzDirectionsConst[i][0];
         j1 += this.xzDirectionsConst[i][1];
         chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;
         if (list.contains(chunkcoordintpair)) {
            player.loadedChunks.add(chunkcoordintpair);
         }
      }

   }

   public void removePlayer(EntityPlayerMP player) {
      int i = (int)player.managedPosX >> 4;
      int j = (int)player.managedPosZ >> 4;

      for(int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
         for(int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
            PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(k, l, false);
            if (playermanager$playerinstance != null) {
               playermanager$playerinstance.removePlayer(player);
            }
         }
      }

      this.players.remove(player);
   }

   private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
      int i = x1 - x2;
      int j = z1 - z2;
      return i >= -radius && i <= radius ? j >= -radius && j <= radius : false;
   }

   public void updateMountedMovingPlayer(EntityPlayerMP player) {
      int i = (int)player.posX >> 4;
      int j = (int)player.posZ >> 4;
      double d0 = player.managedPosX - player.posX;
      double d1 = player.managedPosZ - player.posZ;
      double d2 = d0 * d0 + d1 * d1;
      if (d2 >= 64.0D) {
         int k = (int)player.managedPosX >> 4;
         int l = (int)player.managedPosZ >> 4;
         int i1 = this.playerViewRadius;
         int j1 = i - k;
         int k1 = j - l;
         if (j1 != 0 || k1 != 0) {
            for(int l1 = i - i1; l1 <= i + i1; ++l1) {
               for(int i2 = j - i1; i2 <= j + i1; ++i2) {
                  if (!this.overlaps(l1, i2, k, l, i1)) {
                     this.getPlayerInstance(l1, i2, true).addPlayer(player);
                  }

                  if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1)) {
                     PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l1 - j1, i2 - k1, false);
                     if (playermanager$playerinstance != null) {
                        playermanager$playerinstance.removePlayer(player);
                     }
                  }
               }
            }

            this.filterChunkLoadQueue(player);
            player.managedPosX = player.posX;
            player.managedPosZ = player.posZ;
         }
      }

   }

   public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
      PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkX, chunkZ, false);
      return playermanager$playerinstance != null && playermanager$playerinstance.playersWatchingChunk.contains(player) && !player.loadedChunks.contains(playermanager$playerinstance.chunkCoords);
   }

   public void setPlayerViewRadius(int radius) {
      radius = MathHelper.clamp_int(radius, 3, 32);
      if (radius != this.playerViewRadius) {
         int i = radius - this.playerViewRadius;
         Iterator var3 = Lists.newArrayList(this.players).iterator();

         while(true) {
            while(var3.hasNext()) {
               EntityPlayerMP entityplayermp = (EntityPlayerMP)var3.next();
               int j = (int)entityplayermp.posX >> 4;
               int k = (int)entityplayermp.posZ >> 4;
               int l;
               int i1;
               if (i > 0) {
                  for(l = j - radius; l <= j + radius; ++l) {
                     for(i1 = k - radius; i1 <= k + radius; ++i1) {
                        PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l, i1, true);
                        if (!playermanager$playerinstance.playersWatchingChunk.contains(entityplayermp)) {
                           playermanager$playerinstance.addPlayer(entityplayermp);
                        }
                     }
                  }
               } else {
                  for(l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                     for(i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1) {
                        if (!this.overlaps(l, i1, j, k, radius)) {
                           this.getPlayerInstance(l, i1, true).removePlayer(entityplayermp);
                        }
                     }
                  }
               }
            }

            this.playerViewRadius = radius;
            break;
         }
      }

   }

   public static int getFurthestViewableBlock(int distance) {
      return distance * 16 - 16;
   }

   class PlayerInstance {
      private final List playersWatchingChunk = Lists.newArrayList();
      private final ChunkCoordIntPair chunkCoords;
      private short[] locationOfBlockChange = new short[64];
      private int numBlocksToUpdate;
      private int flagsYAreasToUpdate;
      private long previousWorldTime;

      public PlayerInstance(int chunkX, int chunkZ) {
         this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
         PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ);
      }

      public void addPlayer(EntityPlayerMP player) {
         if (this.playersWatchingChunk.contains(player)) {
            PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", new Object[]{player, this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos});
         } else {
            if (this.playersWatchingChunk.isEmpty()) {
               this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
            }

            this.playersWatchingChunk.add(player);
            player.loadedChunks.add(this.chunkCoords);
         }

      }

      public void removePlayer(EntityPlayerMP player) {
         if (this.playersWatchingChunk.contains(player)) {
            Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            if (chunk.isPopulated()) {
               player.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
            }

            this.playersWatchingChunk.remove(player);
            player.loadedChunks.remove(this.chunkCoords);
            if (this.playersWatchingChunk.isEmpty()) {
               long i = (long)this.chunkCoords.chunkXPos + 2147483647L | (long)this.chunkCoords.chunkZPos + 2147483647L << 32;
               this.increaseInhabitedTime(chunk);
               PlayerManager.this.playerInstances.remove(i);
               PlayerManager.this.playerInstanceList.remove(this);
               if (this.numBlocksToUpdate > 0) {
                  PlayerManager.this.playerInstancesToUpdate.remove(this);
               }

               PlayerManager.this.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            }
         }

      }

      public void processChunk() {
         this.increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
      }

      private void increaseInhabitedTime(Chunk theChunk) {
         theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
         this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
      }

      public void flagChunkForUpdate(int x, int y, int z) {
         if (this.numBlocksToUpdate == 0) {
            PlayerManager.this.playerInstancesToUpdate.add(this);
         }

         this.flagsYAreasToUpdate |= 1 << (y >> 4);
         if (this.numBlocksToUpdate < 64) {
            short short1 = (short)(x << 12 | z << 8 | y);

            for(int i = 0; i < this.numBlocksToUpdate; ++i) {
               if (this.locationOfBlockChange[i] == short1) {
                  return;
               }
            }

            this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
         }

      }

      public void sendToAllPlayersWatchingChunk(Packet thePacket) {
         for(int i = 0; i < this.playersWatchingChunk.size(); ++i) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playersWatchingChunk.get(i);
            if (!entityplayermp.loadedChunks.contains(this.chunkCoords)) {
               entityplayermp.playerNetServerHandler.sendPacket(thePacket);
            }
         }

      }

      public void onUpdate() {
         if (this.numBlocksToUpdate != 0) {
            int i1;
            int k1;
            int i2;
            if (this.numBlocksToUpdate == 1) {
               i1 = (this.locationOfBlockChange[0] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
               k1 = this.locationOfBlockChange[0] & 255;
               i2 = (this.locationOfBlockChange[0] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
               BlockPos blockpos = new BlockPos(i1, k1, i2);
               this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));
               if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity()) {
                  this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
               }
            } else {
               int k2;
               if (this.numBlocksToUpdate != 64) {
                  this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));

                  for(i1 = 0; i1 < this.numBlocksToUpdate; ++i1) {
                     k1 = (this.locationOfBlockChange[i1] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
                     i2 = this.locationOfBlockChange[i1] & 255;
                     k2 = (this.locationOfBlockChange[i1] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
                     BlockPos blockpos1 = new BlockPos(k1, i2, k2);
                     if (PlayerManager.this.theWorldServer.getBlockState(blockpos1).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos1));
                     }
                  }
               } else {
                  i1 = this.chunkCoords.chunkXPos * 16;
                  k1 = this.chunkCoords.chunkZPos * 16;
                  this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));

                  for(i2 = 0; i2 < 16; ++i2) {
                     if ((this.flagsYAreasToUpdate & 1 << i2) != 0) {
                        k2 = i2 << 4;
                        List list = PlayerManager.this.theWorldServer.getTileEntitiesIn(i1, k2, k1, i1 + 16, k2 + 16, k1 + 16);

                        for(int l = 0; l < list.size(); ++l) {
                           this.sendTileToAllPlayersWatchingChunk((TileEntity)list.get(l));
                        }
                     }
                  }
               }
            }

            this.numBlocksToUpdate = 0;
            this.flagsYAreasToUpdate = 0;
         }

      }

      private void sendTileToAllPlayersWatchingChunk(TileEntity theTileEntity) {
         if (theTileEntity != null) {
            Packet packet = theTileEntity.getDescriptionPacket();
            if (packet != null) {
               this.sendToAllPlayersWatchingChunk(packet);
            }
         }

      }
   }
}
