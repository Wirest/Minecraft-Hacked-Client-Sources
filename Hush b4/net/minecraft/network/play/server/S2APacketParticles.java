// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S2APacketParticles implements Packet<INetHandlerPlayClient>
{
    private EnumParticleTypes particleType;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private boolean longDistance;
    private int[] particleArguments;
    
    public S2APacketParticles() {
    }
    
    public S2APacketParticles(final EnumParticleTypes particleTypeIn, final boolean longDistanceIn, final float x, final float y, final float z, final float xOffsetIn, final float yOffset, final float zOffset, final float particleSpeedIn, final int particleCountIn, final int... particleArgumentsIn) {
        this.particleType = particleTypeIn;
        this.longDistance = longDistanceIn;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.xOffset = xOffsetIn;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.particleSpeed = particleSpeedIn;
        this.particleCount = particleCountIn;
        this.particleArguments = particleArgumentsIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.particleType = EnumParticleTypes.getParticleFromId(buf.readInt());
        if (this.particleType == null) {
            this.particleType = EnumParticleTypes.BARRIER;
        }
        this.longDistance = buf.readBoolean();
        this.xCoord = buf.readFloat();
        this.yCoord = buf.readFloat();
        this.zCoord = buf.readFloat();
        this.xOffset = buf.readFloat();
        this.yOffset = buf.readFloat();
        this.zOffset = buf.readFloat();
        this.particleSpeed = buf.readFloat();
        this.particleCount = buf.readInt();
        final int i = this.particleType.getArgumentCount();
        this.particleArguments = new int[i];
        for (int j = 0; j < i; ++j) {
            this.particleArguments[j] = buf.readVarIntFromBuffer();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.particleType.getParticleID());
        buf.writeBoolean(this.longDistance);
        buf.writeFloat(this.xCoord);
        buf.writeFloat(this.yCoord);
        buf.writeFloat(this.zCoord);
        buf.writeFloat(this.xOffset);
        buf.writeFloat(this.yOffset);
        buf.writeFloat(this.zOffset);
        buf.writeFloat(this.particleSpeed);
        buf.writeInt(this.particleCount);
        for (int i = this.particleType.getArgumentCount(), j = 0; j < i; ++j) {
            buf.writeVarIntToBuffer(this.particleArguments[j]);
        }
    }
    
    public EnumParticleTypes getParticleType() {
        return this.particleType;
    }
    
    public boolean isLongDistance() {
        return this.longDistance;
    }
    
    public double getXCoordinate() {
        return this.xCoord;
    }
    
    public double getYCoordinate() {
        return this.yCoord;
    }
    
    public double getZCoordinate() {
        return this.zCoord;
    }
    
    public float getXOffset() {
        return this.xOffset;
    }
    
    public float getYOffset() {
        return this.yOffset;
    }
    
    public float getZOffset() {
        return this.zOffset;
    }
    
    public float getParticleSpeed() {
        return this.particleSpeed;
    }
    
    public int getParticleCount() {
        return this.particleCount;
    }
    
    public int[] getParticleArgs() {
        return this.particleArguments;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleParticles(this);
    }
}
