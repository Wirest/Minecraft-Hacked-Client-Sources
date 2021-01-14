package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S08PacketPlayerPosLook implements Packet {
    private double x;
    private double y;
    private double z;
    public float yaw;
    public float pitch;
    private Set set;
    private static final String __OBFID = "CL_00001273";

    public S08PacketPlayerPosLook() {
    }

    public S08PacketPlayerPosLook(double x, double y, double z, float yaw, float pitch, Set set) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.set = set;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.x = data.readDouble();
        this.y = data.readDouble();
        this.z = data.readDouble();
        this.yaw = data.readFloat();
        this.pitch = data.readFloat();
        this.set = S08PacketPlayerPosLook.EnumFlags.func_180053_a(data.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeDouble(this.x);
        data.writeDouble(this.y);
        data.writeDouble(this.z);
        data.writeFloat(this.yaw);
        data.writeFloat(this.pitch);
        data.writeByte(S08PacketPlayerPosLook.EnumFlags.func_180056_a(this.set));
    }

    public void func_180718_a(INetHandlerPlayClient p_180718_1_) {
        p_180718_1_.handlePlayerPosLook(this);
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

    public Set getSet() {
        return this.set;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180718_a((INetHandlerPlayClient) handler);
    }

    public static enum EnumFlags {
        X("X", 0, 0),
        Y("Y", 1, 1),
        Z("Z", 2, 2),
        Y_ROT("Y_ROT", 3, 3),
        X_ROT("X_ROT", 4, 4);
        private int field_180058_f;

        private static final S08PacketPlayerPosLook.EnumFlags[] $VALUES = new S08PacketPlayerPosLook.EnumFlags[]{X, Y, Z, Y_ROT, X_ROT};
        private static final String __OBFID = "CL_00002304";

        private EnumFlags(String p_i45992_1_, int p_i45992_2_, int p_i45992_3_) {
            this.field_180058_f = p_i45992_3_;
        }

        private int func_180055_a() {
            return 1 << this.field_180058_f;
        }

        private boolean func_180054_b(int p_180054_1_) {
            return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
        }

        public static Set func_180053_a(int p_180053_0_) {
            EnumSet var1 = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
            S08PacketPlayerPosLook.EnumFlags[] var2 = values();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                S08PacketPlayerPosLook.EnumFlags var5 = var2[var4];

                if (var5.func_180054_b(p_180053_0_)) {
                    var1.add(var5);
                }
            }

            return var1;
        }

        public static int func_180056_a(Set p_180056_0_) {
            int var1 = 0;
            S08PacketPlayerPosLook.EnumFlags var3;

            for (Iterator var2 = p_180056_0_.iterator(); var2.hasNext(); var1 |= var3.func_180055_a()) {
                var3 = (S08PacketPlayerPosLook.EnumFlags) var2.next();
            }

            return var1;
        }
    }
}
