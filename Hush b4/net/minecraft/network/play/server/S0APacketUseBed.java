// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S0APacketUseBed implements Packet<INetHandlerPlayClient>
{
    private int playerID;
    private BlockPos bedPos;
    
    public S0APacketUseBed() {
    }
    
    public S0APacketUseBed(final EntityPlayer player, final BlockPos bedPosIn) {
        this.playerID = player.getEntityId();
        this.bedPos = bedPosIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.playerID = buf.readVarIntFromBuffer();
        this.bedPos = buf.readBlockPos();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.playerID);
        buf.writeBlockPos(this.bedPos);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUseBed(this);
    }
    
    public EntityPlayer getPlayer(final World worldIn) {
        return (EntityPlayer)worldIn.getEntityByID(this.playerID);
    }
    
    public BlockPos getBedPosition() {
        return this.bedPos;
    }
}
