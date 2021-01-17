// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.Validate;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S29PacketSoundEffect implements Packet<INetHandlerPlayClient>
{
    private String soundName;
    private int posX;
    private int posY;
    private int posZ;
    private float soundVolume;
    private int soundPitch;
    
    public S29PacketSoundEffect() {
        this.posY = Integer.MAX_VALUE;
    }
    
    public S29PacketSoundEffect(final String soundNameIn, final double soundX, final double soundY, final double soundZ, final float volume, float pitch) {
        this.posY = Integer.MAX_VALUE;
        Validate.notNull(soundNameIn, "name", new Object[0]);
        this.soundName = soundNameIn;
        this.posX = (int)(soundX * 8.0);
        this.posY = (int)(soundY * 8.0);
        this.posZ = (int)(soundZ * 8.0);
        this.soundVolume = volume;
        this.soundPitch = (int)(pitch * 63.0f);
        pitch = MathHelper.clamp_float(pitch, 0.0f, 255.0f);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.soundName = buf.readStringFromBuffer(256);
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
        this.soundVolume = buf.readFloat();
        this.soundPitch = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.soundName);
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
        buf.writeFloat(this.soundVolume);
        buf.writeByte(this.soundPitch);
    }
    
    public String getSoundName() {
        return this.soundName;
    }
    
    public double getX() {
        return this.posX / 8.0f;
    }
    
    public double getY() {
        return this.posY / 8.0f;
    }
    
    public double getZ() {
        return this.posZ / 8.0f;
    }
    
    public float getVolume() {
        return this.soundVolume;
    }
    
    public float getPitch() {
        return this.soundPitch / 63.0f;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSoundEffect(this);
    }
}
