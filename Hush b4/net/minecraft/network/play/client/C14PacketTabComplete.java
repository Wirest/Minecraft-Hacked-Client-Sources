// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C14PacketTabComplete implements Packet<INetHandlerPlayServer>
{
    private String message;
    private BlockPos targetBlock;
    
    public C14PacketTabComplete() {
    }
    
    public C14PacketTabComplete(final String msg) {
        this(msg, null);
    }
    
    public C14PacketTabComplete(final String msg, final BlockPos target) {
        this.message = msg;
        this.targetBlock = target;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.message = buf.readStringFromBuffer(32767);
        final boolean flag = buf.readBoolean();
        if (flag) {
            this.targetBlock = buf.readBlockPos();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(StringUtils.substring(this.message, 0, 32767));
        final boolean flag = this.targetBlock != null;
        buf.writeBoolean(flag);
        if (flag) {
            buf.writeBlockPos(this.targetBlock);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processTabComplete(this);
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public BlockPos getTargetBlock() {
        return this.targetBlock;
    }
}
