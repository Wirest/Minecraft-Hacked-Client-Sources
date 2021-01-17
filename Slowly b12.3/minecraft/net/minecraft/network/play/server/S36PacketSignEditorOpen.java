package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import net.minecraft.util.*;
import java.io.*;
import net.minecraft.network.*;

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
