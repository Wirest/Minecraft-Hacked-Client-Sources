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

public class S1DPacketEntityEffect implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte hideParticles;
    
    public S1DPacketEntityEffect() {
    }
    
    public S1DPacketEntityEffect(final int entityIdIn, final PotionEffect effect) {
        this.entityId = entityIdIn;
        this.effectId = (byte)(effect.getPotionID() & 0xFF);
        this.amplifier = (byte)(effect.getAmplifier() & 0xFF);
        if (effect.getDuration() > 32767) {
            this.duration = 32767;
        }
        else {
            this.duration = effect.getDuration();
        }
        this.hideParticles = (byte)(effect.getIsShowParticles() ? 1 : 0);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.effectId = buf.readByte();
        this.amplifier = buf.readByte();
        this.duration = buf.readVarIntFromBuffer();
        this.hideParticles = buf.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.effectId);
        buf.writeByte(this.amplifier);
        buf.writeVarIntToBuffer(this.duration);
        buf.writeByte(this.hideParticles);
    }
    
    public boolean func_149429_c() {
        return this.duration == 32767;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityEffect(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public byte getEffectId() {
        return this.effectId;
    }
    
    public byte getAmplifier() {
        return this.amplifier;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public boolean func_179707_f() {
        return this.hideParticles != 0;
    }
}
