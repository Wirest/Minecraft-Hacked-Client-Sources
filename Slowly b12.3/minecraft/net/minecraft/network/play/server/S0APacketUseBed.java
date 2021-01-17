package net.minecraft.network.play.server;

import net.minecraft.network.play.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import java.io.*;
import net.minecraft.world.*;
import net.minecraft.network.*;

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
