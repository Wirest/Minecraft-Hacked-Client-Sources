package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S08PacketPlayerPosLook implements Packet
{
    private double field_148940_a;
    private double field_148938_b;
    private double field_148939_c;
    private float field_148936_d;
    private float field_148937_e;
    private Set field_179835_f;
    private static final String __OBFID = "CL_00001273";

    public S08PacketPlayerPosLook() {}

    public S08PacketPlayerPosLook(double p_i45993_1_, double p_i45993_3_, double p_i45993_5_, float p_i45993_7_, float p_i45993_8_, Set p_i45993_9_)
    {
        this.field_148940_a = p_i45993_1_;
        this.field_148938_b = p_i45993_3_;
        this.field_148939_c = p_i45993_5_;
        this.field_148936_d = p_i45993_7_;
        this.field_148937_e = p_i45993_8_;
        this.field_179835_f = p_i45993_9_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148940_a = data.readDouble();
        this.field_148938_b = data.readDouble();
        this.field_148939_c = data.readDouble();
        this.field_148936_d = data.readFloat();
        this.field_148937_e = data.readFloat();
        this.field_179835_f = S08PacketPlayerPosLook.EnumFlags.func_180053_a(data.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeDouble(this.field_148940_a);
        data.writeDouble(this.field_148938_b);
        data.writeDouble(this.field_148939_c);
        data.writeFloat(this.field_148936_d);
        data.writeFloat(this.field_148937_e);
        data.writeByte(S08PacketPlayerPosLook.EnumFlags.func_180056_a(this.field_179835_f));
    }

    public void func_180718_a(INetHandlerPlayClient p_180718_1_)
    {
        p_180718_1_.handlePlayerPosLook(this);
    }

    public double func_148932_c()
    {
        return this.field_148940_a;
    }

    public double func_148928_d()
    {
        return this.field_148938_b;
    }

    public double func_148933_e()
    {
        return this.field_148939_c;
    }

    public float func_148931_f()
    {
        return this.field_148936_d;
    }

    public float func_148930_g()
    {
        return this.field_148937_e;
    }

    public Set func_179834_f()
    {
        return this.field_179835_f;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180718_a((INetHandlerPlayClient)handler);
    }

    public static enum EnumFlags
    {
        X("X", 0, 0),
        Y("Y", 1, 1),
        Z("Z", 2, 2),
        Y_ROT("Y_ROT", 3, 3),
        X_ROT("X_ROT", 4, 4);
        private int field_180058_f;

        private static final S08PacketPlayerPosLook.EnumFlags[] $VALUES = new S08PacketPlayerPosLook.EnumFlags[]{X, Y, Z, Y_ROT, X_ROT};
        private static final String __OBFID = "CL_00002304";

        private EnumFlags(String p_i45992_1_, int p_i45992_2_, int p_i45992_3_)
        {
            this.field_180058_f = p_i45992_3_;
        }

        private int func_180055_a()
        {
            return 1 << this.field_180058_f;
        }

        private boolean func_180054_b(int p_180054_1_)
        {
            return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
        }

        public static Set func_180053_a(int p_180053_0_)
        {
            EnumSet var1 = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
            S08PacketPlayerPosLook.EnumFlags[] var2 = values();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                S08PacketPlayerPosLook.EnumFlags var5 = var2[var4];

                if (var5.func_180054_b(p_180053_0_))
                {
                    var1.add(var5);
                }
            }

            return var1;
        }

        public static int func_180056_a(Set p_180056_0_)
        {
            int var1 = 0;
            S08PacketPlayerPosLook.EnumFlags var3;

            for (Iterator var2 = p_180056_0_.iterator(); var2.hasNext(); var1 |= var3.func_180055_a())
            {
                var3 = (S08PacketPlayerPosLook.EnumFlags)var2.next();
            }

            return var1;
        }
    }
}
