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

public class S36PacketSignEditorOpen implements Packet<INetHandlerPlayClient>
{
    private BlockPos signPosition;
    
    public S36PacketSignEditorOpen() {
    }
    
    public S36PacketSignEditorOpen(final BlockPos signPositionIn) {
        this.signPosition = signPositionIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSignEditorOpen(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.signPosition = buf.readBlockPos();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.signPosition);
    }
    
    public BlockPos getSignPosition() {
        return this.signPosition;
    }
}
