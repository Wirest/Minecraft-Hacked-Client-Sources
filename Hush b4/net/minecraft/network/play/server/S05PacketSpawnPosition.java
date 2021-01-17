// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S05PacketSpawnPosition implements Packet<INetHandlerPlayClient>
{
    private BlockPos spawnBlockPos;
    
    public S05PacketSpawnPosition() {
    }
    
    public S05PacketSpawnPosition(final BlockPos spawnBlockPosIn) {
        this.spawnBlockPos = spawnBlockPosIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.spawnBlockPos = buf.readBlockPos();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.spawnBlockPos);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnPosition(this);
    }
    
    public BlockPos getSpawnPos() {
        return this.spawnBlockPos;
    }
}
