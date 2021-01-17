// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S1EPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int effectId;
    
    public S1EPacketRemoveEntityEffect() {
    }
    
    public S1EPacketRemoveEntityEffect(final int entityIdIn, final PotionEffect effect) {
        this.entityId = entityIdIn;
        this.effectId = effect.getPotionID();
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.effectId = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.effectId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleRemoveEntityEffect(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public int getEffectId() {
        return this.effectId;
    }
}
