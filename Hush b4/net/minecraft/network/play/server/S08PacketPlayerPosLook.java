// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import java.util.Iterator;
import java.util.EnumSet;
import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.Set;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S08PacketPlayerPosLook implements Packet<INetHandlerPlayClient>
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Set<EnumFlags> field_179835_f;
    
    public S08PacketPlayerPosLook() {
    }
    
    public S08PacketPlayerPosLook(final double xIn, final double yIn, final double zIn, final float yawIn, final float pitchIn, final Set<EnumFlags> p_i45993_9_) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        this.yaw = yawIn;
        this.pitch = pitchIn;
        this.field_179835_f = p_i45993_9_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.field_179835_f = EnumFlags.func_180053_a(buf.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeByte(EnumFlags.func_180056_a(this.field_179835_f));
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handlePlayerPosLook(this);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public Set<EnumFlags> func_179834_f() {
        return this.field_179835_f;
    }
    
    public enum EnumFlags
    {
        X("X", 0, 0), 
        Y("Y", 1, 1), 
        Z("Z", 2, 2), 
        Y_ROT("Y_ROT", 3, 3), 
        X_ROT("X_ROT", 4, 4);
        
        private int field_180058_f;
        
        private EnumFlags(final String name, final int ordinal, final int p_i45992_3_) {
            this.field_180058_f = p_i45992_3_;
        }
        
        private int func_180055_a() {
            return 1 << this.field_180058_f;
        }
        
        private boolean func_180054_b(final int p_180054_1_) {
            return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
        }
        
        public static Set<EnumFlags> func_180053_a(final int p_180053_0_) {
            final Set<EnumFlags> set = EnumSet.noneOf(EnumFlags.class);
            EnumFlags[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumFlags s08packetplayerposlook$enumflags = values[i];
                if (s08packetplayerposlook$enumflags.func_180054_b(p_180053_0_)) {
                    set.add(s08packetplayerposlook$enumflags);
                }
            }
            return set;
        }
        
        public static int func_180056_a(final Set<EnumFlags> p_180056_0_) {
            int i = 0;
            for (final EnumFlags s08packetplayerposlook$enumflags : p_180056_0_) {
                i |= s08packetplayerposlook$enumflags.func_180055_a();
            }
            return i;
        }
    }
}
