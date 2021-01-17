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

public class S28PacketEffect implements Packet<INetHandlerPlayClient>
{
    private int soundType;
    private BlockPos soundPos;
    private int soundData;
    private boolean serverWide;
    
    public S28PacketEffect() {
    }
    
    public S28PacketEffect(final int soundTypeIn, final BlockPos soundPosIn, final int soundDataIn, final boolean serverWideIn) {
        this.soundType = soundTypeIn;
        this.soundPos = soundPosIn;
        this.soundData = soundDataIn;
        this.serverWide = serverWideIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.soundType = buf.readInt();
        this.soundPos = buf.readBlockPos();
        this.soundData = buf.readInt();
        this.serverWide = buf.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.soundType);
        buf.writeBlockPos(this.soundPos);
        buf.writeInt(this.soundData);
        buf.writeBoolean(this.serverWide);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEffect(this);
    }
    
    public boolean isSoundServerwide() {
        return this.serverWide;
    }
    
    public int getSoundType() {
        return this.soundType;
    }
    
    public int getSoundData() {
        return this.soundData;
    }
    
    public BlockPos getSoundPos() {
        return this.soundPos;
    }
}
