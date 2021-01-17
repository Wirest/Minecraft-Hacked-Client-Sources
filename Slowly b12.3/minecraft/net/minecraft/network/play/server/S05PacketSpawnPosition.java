package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import net.minecraft.util.*;
import java.io.*;
import net.minecraft.network.*;

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
