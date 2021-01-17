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

public class S25PacketBlockBreakAnim implements Packet<INetHandlerPlayClient>
{
    private int breakerId;
    private BlockPos position;
    private int progress;
    
    public S25PacketBlockBreakAnim() {
    }
    
    public S25PacketBlockBreakAnim(final int breakerId, final BlockPos pos, final int progress) {
        this.breakerId = breakerId;
        this.position = pos;
        this.progress = progress;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.breakerId = buf.readVarIntFromBuffer();
        this.position = buf.readBlockPos();
        this.progress = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.breakerId);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.progress);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleBlockBreakAnim(this);
    }
    
    public int getBreakerId() {
        return this.breakerId;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public int getProgress() {
        return this.progress;
    }
}
