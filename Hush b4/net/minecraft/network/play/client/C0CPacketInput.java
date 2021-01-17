// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C0CPacketInput implements Packet<INetHandlerPlayServer>
{
    private float strafeSpeed;
    private float forwardSpeed;
    private boolean jumping;
    private boolean sneaking;
    
    public C0CPacketInput() {
    }
    
    public C0CPacketInput(final float strafeSpeed, final float forwardSpeed, final boolean jumping, final boolean sneaking) {
        this.strafeSpeed = strafeSpeed;
        this.forwardSpeed = forwardSpeed;
        this.jumping = jumping;
        this.sneaking = sneaking;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.strafeSpeed = buf.readFloat();
        this.forwardSpeed = buf.readFloat();
        final byte b0 = buf.readByte();
        this.jumping = ((b0 & 0x1) > 0);
        this.sneaking = ((b0 & 0x2) > 0);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeFloat(this.strafeSpeed);
        buf.writeFloat(this.forwardSpeed);
        byte b0 = 0;
        if (this.jumping) {
            b0 |= 0x1;
        }
        if (this.sneaking) {
            b0 |= 0x2;
        }
        buf.writeByte(b0);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processInput(this);
    }
    
    public float getStrafeSpeed() {
        return this.strafeSpeed;
    }
    
    public float getForwardSpeed() {
        return this.forwardSpeed;
    }
    
    public boolean isJumping() {
        return this.jumping;
    }
    
    public boolean isSneaking() {
        return this.sneaking;
    }
}
