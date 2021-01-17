// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.util.Iterator;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess
{
    private MinecraftServer mcServer;
    private WorldServer theWorldServer;
    
    public WorldManager(final MinecraftServer p_i1517_1_, final WorldServer p_i1517_2_) {
        this.mcServer = p_i1517_1_;
        this.theWorldServer = p_i1517_2_;
    }
    
    @Override
    public void spawnParticle(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xOffset, final double yOffset, final double zOffset, final int... p_180442_15_) {
    }
    
    @Override
    public void onEntityAdded(final Entity entityIn) {
        this.theWorldServer.getEntityTracker().trackEntity(entityIn);
    }
    
    @Override
    public void onEntityRemoved(final Entity entityIn) {
        this.theWorldServer.getEntityTracker().untrackEntity(entityIn);
        this.theWorldServer.getScoreboard().func_181140_a(entityIn);
    }
    
    @Override
    public void playSound(final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
        this.mcServer.getConfigurationManager().sendToAllNear(x, y, z, (volume > 1.0f) ? ((double)(16.0f * volume)) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }
    
    @Override
    public void playSoundToNearExcept(final EntityPlayer except, final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(except, x, y, z, (volume > 1.0f) ? ((double)(16.0f * volume)) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }
    
    @Override
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    }
    
    @Override
    public void markBlockForUpdate(final BlockPos pos) {
        this.theWorldServer.getPlayerManager().markBlockForUpdate(pos);
    }
    
    @Override
    public void notifyLightSet(final BlockPos pos) {
    }
    
    @Override
    public void playRecord(final String recordName, final BlockPos blockPosIn) {
    }
    
    @Override
    public void playAuxSFX(final EntityPlayer player, final int sfxType, final BlockPos blockPosIn, final int p_180439_4_) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(player, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ(), 64.0, this.theWorldServer.provider.getDimensionId(), new S28PacketEffect(sfxType, blockPosIn, p_180439_4_, false));
    }
    
    @Override
    public void broadcastSound(final int p_180440_1_, final BlockPos p_180440_2_, final int p_180440_3_) {
        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(p_180440_1_, p_180440_2_, p_180440_3_, true));
    }
    
    @Override
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        for (final EntityPlayerMP entityplayermp : this.mcServer.getConfigurationManager().func_181057_v()) {
            if (entityplayermp != null && entityplayermp.worldObj == this.theWorldServer && entityplayermp.getEntityId() != breakerId) {
                final double d0 = pos.getX() - entityplayermp.posX;
                final double d2 = pos.getY() - entityplayermp.posY;
                final double d3 = pos.getZ() - entityplayermp.posZ;
                if (d0 * d0 + d2 * d2 + d3 * d3 >= 1024.0) {
                    continue;
                }
                entityplayermp.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(breakerId, pos, progress));
            }
        }
    }
}
