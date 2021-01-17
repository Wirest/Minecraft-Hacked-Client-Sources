// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S33PacketUpdateSign implements Packet<INetHandlerPlayClient>
{
    private World world;
    private BlockPos blockPos;
    private IChatComponent[] lines;
    
    public S33PacketUpdateSign() {
    }
    
    public S33PacketUpdateSign(final World worldIn, final BlockPos blockPosIn, final IChatComponent[] linesIn) {
        this.world = worldIn;
        this.blockPos = blockPosIn;
        this.lines = new IChatComponent[] { linesIn[0], linesIn[1], linesIn[2], linesIn[3] };
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.blockPos = buf.readBlockPos();
        this.lines = new IChatComponent[4];
        for (int i = 0; i < 4; ++i) {
            this.lines[i] = buf.readChatComponent();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.blockPos);
        for (int i = 0; i < 4; ++i) {
            buf.writeChatComponent(this.lines[i]);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUpdateSign(this);
    }
    
    public BlockPos getPos() {
        return this.blockPos;
    }
    
    public IChatComponent[] getLines() {
        return this.lines;
    }
}
