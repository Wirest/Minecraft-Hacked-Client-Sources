// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.LongHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;

public class PlayerManager
{
    private static final Logger pmLogger;
    private final WorldServer theWorldServer;
    private final List<EntityPlayerMP> players;
    private final LongHashMap playerInstances;
    private final List<PlayerInstance> playerInstancesToUpdate;
    private final List<PlayerInstance> playerInstanceList;
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst;
    
    static {
        pmLogger = LogManager.getLogger();
    }
    
    public PlayerManager(final WorldServer serverWorld) {
        this.players = (List<EntityPlayerMP>)Lists.newArrayList();
        this.playerInstances = new LongHashMap();
        this.playerInstancesToUpdate = (List<PlayerInstance>)Lists.newArrayList();
        this.playerInstanceList = (List<PlayerInstance>)Lists.newArrayList();
        this.xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
    }
    
    public WorldServer getWorldServer() {
        return this.theWorldServer;
    }
    
    public void updatePlayerInstances() {
        final long i = this.theWorldServer.getTotalWorldTime();
        if (i - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = i;
            for (int j = 0; j < this.playerInstanceList.size(); ++j) {
                final PlayerInstance playermanager$playerinstance = this.playerInstanceList.get(j);
                playermanager$playerinstance.onUpdate();
                playermanager$playerinstance.processChunk();
            }
        }
        else {
            for (int k = 0; k < this.playerInstancesToUpdate.size(); ++k) {
                final PlayerInstance playermanager$playerinstance2 = this.playerInstancesToUpdate.get(k);
                playermanager$playerinstance2.onUpdate();
            }
        }
        this.playerInstancesToUpdate.clear();
        if (this.players.isEmpty()) {
            final WorldProvider worldprovider = this.theWorldServer.provider;
            if (!worldprovider.canRespawnHere()) {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }
    
    public boolean hasPlayerInstance(final int chunkX, final int chunkZ) {
        final long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32;
        return this.playerInstances.getValueByKey(i) != null;
    }
    
    private PlayerInstance getPlayerInstance(final int chunkX, final int chunkZ, final boolean createIfAbsent) {
        final long i = chunkX + 2147483647L | chunkZ + 2147483647L << 32;
        PlayerInstance playermanager$playerinstance = (PlayerInstance)this.playerInstances.getValueByKey(i);
        if (playermanager$playerinstance == null && createIfAbsent) {
            playermanager$playerinstance = new PlayerInstance(chunkX, chunkZ);
            this.playerInstances.add(i, playermanager$playerinstance);
            this.playerInstanceList.add(playermanager$playerinstance);
        }
        return playermanager$playerinstance;
    }
    
    public void markBlockForUpdate(final BlockPos pos) {
        final int i = pos.getX() >> 4;
        final int j = pos.getZ() >> 4;
        final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(i, j, false);
        if (playermanager$playerinstance != null) {
            playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
    }
    
    public void addPlayer(final EntityPlayerMP player) {
        final int i = (int)player.posX >> 4;
        final int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                this.getPlayerInstance(k, l, true).addPlayer(player);
            }
        }
        this.players.add(player);
        this.filterChunkLoadQueue(player);
    }
    
    public void filterChunkLoadQueue(final EntityPlayerMP player) {
        final List<ChunkCoordIntPair> list = (List<ChunkCoordIntPair>)Lists.newArrayList((Iterable<?>)player.loadedChunks);
        int i = 0;
        final int j = this.playerViewRadius;
        final int k = (int)player.posX >> 4;
        final int l = (int)player.posZ >> 4;
        int i2 = 0;
        int j2 = 0;
        ChunkCoordIntPair chunkcoordintpair = this.getPlayerInstance(k, l, true).chunkCoords;
        player.loadedChunks.clear();
        if (list.contains(chunkcoordintpair)) {
            player.loadedChunks.add(chunkcoordintpair);
        }
        for (int k2 = 1; k2 <= j * 2; ++k2) {
            for (int l2 = 0; l2 < 2; ++l2) {
                final int[] aint = this.xzDirectionsConst[i++ % 4];
                for (int i3 = 0; i3 < k2; ++i3) {
                    i2 += aint[0];
                    j2 += aint[1];
                    chunkcoordintpair = this.getPlayerInstance(k + i2, l + j2, true).chunkCoords;
                    if (list.contains(chunkcoordintpair)) {
                        player.loadedChunks.add(chunkcoordintpair);
                    }
                }
            }
        }
        i %= 4;
        for (int j3 = 0; j3 < j * 2; ++j3) {
            i2 += this.xzDirectionsConst[i][0];
            j2 += this.xzDirectionsConst[i][1];
            chunkcoordintpair = this.getPlayerInstance(k + i2, l + j2, true).chunkCoords;
            if (list.contains(chunkcoordintpair)) {
                player.loadedChunks.add(chunkcoordintpair);
            }
        }
    }
    
    public void removePlayer(final EntityPlayerMP player) {
        final int i = (int)player.managedPosX >> 4;
        final int j = (int)player.managedPosZ >> 4;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(k, l, false);
                if (playermanager$playerinstance != null) {
                    playermanager$playerinstance.removePlayer(player);
                }
            }
        }
        this.players.remove(player);
    }
    
    private boolean overlaps(final int x1, final int z1, final int x2, final int z2, final int radius) {
        final int i = x1 - x2;
        final int j = z1 - z2;
        return i >= -radius && i <= radius && (j >= -radius && j <= radius);
    }
    
    public void updateMountedMovingPlayer(final EntityPlayerMP player) {
        final int i = (int)player.posX >> 4;
        final int j = (int)player.posZ >> 4;
        final double d0 = player.managedPosX - player.posX;
        final double d2 = player.managedPosZ - player.posZ;
        final double d3 = d0 * d0 + d2 * d2;
        if (d3 >= 64.0) {
            final int k = (int)player.managedPosX >> 4;
            final int l = (int)player.managedPosZ >> 4;
            final int i2 = this.playerViewRadius;
            final int j2 = i - k;
            final int k2 = j - l;
            if (j2 != 0 || k2 != 0) {
                for (int l2 = i - i2; l2 <= i + i2; ++l2) {
                    for (int i3 = j - i2; i3 <= j + i2; ++i3) {
                        if (!this.overlaps(l2, i3, k, l, i2)) {
                            this.getPlayerInstance(l2, i3, true).addPlayer(player);
                        }
                        if (!this.overlaps(l2 - j2, i3 - k2, i, j, i2)) {
                            final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l2 - j2, i3 - k2, false);
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
    
    public boolean isPlayerWatchingChunk(final EntityPlayerMP player, final int chunkX, final int chunkZ) {
        final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkX, chunkZ, false);
        return playermanager$playerinstance != null && playermanager$playerinstance.playersWatchingChunk.contains(player) && !player.loadedChunks.contains(playermanager$playerinstance.chunkCoords);
    }
    
    public void setPlayerViewRadius(int radius) {
        radius = MathHelper.clamp_int(radius, 3, 32);
        if (radius != this.playerViewRadius) {
            final int i = radius - this.playerViewRadius;
            for (final EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable<? extends EntityPlayerMP>)this.players)) {
                final int j = (int)entityplayermp.posX >> 4;
                final int k = (int)entityplayermp.posZ >> 4;
                if (i > 0) {
                    for (int j2 = j - radius; j2 <= j + radius; ++j2) {
                        for (int k2 = k - radius; k2 <= k + radius; ++k2) {
                            final PlayerInstance playermanager$playerinstance = this.getPlayerInstance(j2, k2, true);
                            if (!playermanager$playerinstance.playersWatchingChunk.contains(entityplayermp)) {
                                playermanager$playerinstance.addPlayer(entityplayermp);
                            }
                        }
                    }
                }
                else {
                    for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                        for (int i2 = k - this.playerViewRadius; i2 <= k + this.playerViewRadius; ++i2) {
                            if (!this.overlaps(l, i2, j, k, radius)) {
                                this.getPlayerInstance(l, i2, true).removePlayer(entityplayermp);
                            }
                        }
                    }
                }
            }
            this.playerViewRadius = radius;
        }
    }
    
    public static int getFurthestViewableBlock(final int distance) {
        return distance * 16 - 16;
    }
    
    class PlayerInstance
    {
        private final List<EntityPlayerMP> playersWatchingChunk;
        private final ChunkCoordIntPair chunkCoords;
        private short[] locationOfBlockChange;
        private int numBlocksToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;
        
        public PlayerInstance(final int chunkX, final int chunkZ) {
            this.playersWatchingChunk = (List<EntityPlayerMP>)Lists.newArrayList();
            this.locationOfBlockChange = new short[64];
            this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
            PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ);
        }
        
        public void addPlayer(final EntityPlayerMP player) {
            if (this.playersWatchingChunk.contains(player)) {
                PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", player, this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
            }
            else {
                if (this.playersWatchingChunk.isEmpty()) {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }
                this.playersWatchingChunk.add(player);
                player.loadedChunks.add(this.chunkCoords);
            }
        }
        
        public void removePlayer(final EntityPlayerMP player) {
            if (this.playersWatchingChunk.contains(player)) {
                final Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                if (chunk.isPopulated()) {
                    player.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
                }
                this.playersWatchingChunk.remove(player);
                player.loadedChunks.remove(this.chunkCoords);
                if (this.playersWatchingChunk.isEmpty()) {
                    final long i = this.chunkCoords.chunkXPos + 2147483647L | this.chunkCoords.chunkZPos + 2147483647L << 32;
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
        
        private void increaseInhabitedTime(final Chunk theChunk) {
            theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }
        
        public void flagChunkForUpdate(final int x, final int y, final int z) {
            if (this.numBlocksToUpdate == 0) {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }
            this.flagsYAreasToUpdate |= 1 << (y >> 4);
            if (this.numBlocksToUpdate < 64) {
                final short short1 = (short)(x << 12 | z << 8 | y);
                for (int i = 0; i < this.numBlocksToUpdate; ++i) {
                    if (this.locationOfBlockChange[i] == short1) {
                        return;
                    }
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
            }
        }
        
        public void sendToAllPlayersWatchingChunk(final Packet thePacket) {
            for (int i = 0; i < this.playersWatchingChunk.size(); ++i) {
                final EntityPlayerMP entityplayermp = this.playersWatchingChunk.get(i);
                if (!entityplayermp.loadedChunks.contains(this.chunkCoords)) {
                    entityplayermp.playerNetServerHandler.sendPacket(thePacket);
                }
            }
        }
        
        public void onUpdate() {
            if (this.numBlocksToUpdate != 0) {
                if (this.numBlocksToUpdate == 1) {
                    final int i = (this.locationOfBlockChange[0] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                    final int j = this.locationOfBlockChange[0] & 0xFF;
                    final int k = (this.locationOfBlockChange[0] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                    final BlockPos blockpos = new BlockPos(i, j, k);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));
                    if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity()) {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
                    }
                }
                else if (this.numBlocksToUpdate == 64) {
                    final int i2 = this.chunkCoords.chunkXPos * 16;
                    final int k2 = this.chunkCoords.chunkZPos * 16;
                    this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));
                    for (int i3 = 0; i3 < 16; ++i3) {
                        if ((this.flagsYAreasToUpdate & 1 << i3) != 0x0) {
                            final int k3 = i3 << 4;
                            final List<TileEntity> list = PlayerManager.this.theWorldServer.getTileEntitiesIn(i2, k3, k2, i2 + 16, k3 + 16, k2 + 16);
                            for (int l = 0; l < list.size(); ++l) {
                                this.sendTileToAllPlayersWatchingChunk(list.get(l));
                            }
                        }
                    }
                }
                else {
                    this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
                    for (int j2 = 0; j2 < this.numBlocksToUpdate; ++j2) {
                        final int l2 = (this.locationOfBlockChange[j2] >> 12 & 0xF) + this.chunkCoords.chunkXPos * 16;
                        final int j3 = this.locationOfBlockChange[j2] & 0xFF;
                        final int l3 = (this.locationOfBlockChange[j2] >> 8 & 0xF) + this.chunkCoords.chunkZPos * 16;
                        final BlockPos blockpos2 = new BlockPos(l2, j3, l3);
                        if (PlayerManager.this.theWorldServer.getBlockState(blockpos2).getBlock().hasTileEntity()) {
                            this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos2));
                        }
                    }
                }
                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }
        
        private void sendTileToAllPlayersWatchingChunk(final TileEntity theTileEntity) {
            if (theTileEntity != null) {
                final Packet packet = theTileEntity.getDescriptionPacket();
                if (packet != null) {
                    this.sendToAllPlayersWatchingChunk(packet);
                }
            }
        }
    }
}
