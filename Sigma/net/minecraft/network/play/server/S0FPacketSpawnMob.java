package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0FPacketSpawnMob implements Packet {
    private int field_149042_a;
    private int field_149040_b;
    private int field_149041_c;
    private int field_149038_d;
    private int field_149039_e;
    private int field_149036_f;
    private int field_149037_g;
    private int field_149047_h;
    private byte field_149048_i;
    private byte field_149045_j;
    private byte field_149046_k;
    private DataWatcher field_149043_l;
    private List field_149044_m;
    private static final String __OBFID = "CL_00001279";

    public S0FPacketSpawnMob() {
    }

    public S0FPacketSpawnMob(EntityLivingBase p_i45192_1_) {
        this.field_149042_a = p_i45192_1_.getEntityId();
        this.field_149040_b = (byte) EntityList.getEntityID(p_i45192_1_);
        this.field_149041_c = MathHelper.floor_double(p_i45192_1_.posX * 32.0D);
        this.field_149038_d = MathHelper.floor_double(p_i45192_1_.posY * 32.0D);
        this.field_149039_e = MathHelper.floor_double(p_i45192_1_.posZ * 32.0D);
        this.field_149048_i = (byte) ((int) (p_i45192_1_.rotationYaw * 256.0F / 360.0F));
        this.field_149045_j = (byte) ((int) (p_i45192_1_.rotationPitch * 256.0F / 360.0F));
        this.field_149046_k = (byte) ((int) (p_i45192_1_.rotationYawHead * 256.0F / 360.0F));
        double var2 = 3.9D;
        double var4 = p_i45192_1_.motionX;
        double var6 = p_i45192_1_.motionY;
        double var8 = p_i45192_1_.motionZ;

        if (var4 < -var2) {
            var4 = -var2;
        }

        if (var6 < -var2) {
            var6 = -var2;
        }

        if (var8 < -var2) {
            var8 = -var2;
        }

        if (var4 > var2) {
            var4 = var2;
        }

        if (var6 > var2) {
            var6 = var2;
        }

        if (var8 > var2) {
            var8 = var2;
        }

        this.field_149036_f = (int) (var4 * 8000.0D);
        this.field_149037_g = (int) (var6 * 8000.0D);
        this.field_149047_h = (int) (var8 * 8000.0D);
        this.field_149043_l = p_i45192_1_.getDataWatcher();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149042_a = data.readVarIntFromBuffer();
        this.field_149040_b = data.readByte() & 255;
        this.field_149041_c = data.readInt();
        this.field_149038_d = data.readInt();
        this.field_149039_e = data.readInt();
        this.field_149048_i = data.readByte();
        this.field_149045_j = data.readByte();
        this.field_149046_k = data.readByte();
        this.field_149036_f = data.readShort();
        this.field_149037_g = data.readShort();
        this.field_149047_h = data.readShort();
        this.field_149044_m = DataWatcher.readWatchedListFromPacketBuffer(data);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149042_a);
        data.writeByte(this.field_149040_b & 255);
        data.writeInt(this.field_149041_c);
        data.writeInt(this.field_149038_d);
        data.writeInt(this.field_149039_e);
        data.writeByte(this.field_149048_i);
        data.writeByte(this.field_149045_j);
        data.writeByte(this.field_149046_k);
        data.writeShort(this.field_149036_f);
        data.writeShort(this.field_149037_g);
        data.writeShort(this.field_149047_h);
        this.field_149043_l.writeTo(data);
    }

    public void func_180721_a(INetHandlerPlayClient p_180721_1_) {
        p_180721_1_.handleSpawnMob(this);
    }

    public List func_149027_c() {
        if (this.field_149044_m == null) {
            this.field_149044_m = this.field_149043_l.getAllWatched();
        }

        return this.field_149044_m;
    }

    public int func_149024_d() {
        return this.field_149042_a;
    }

    public int func_149025_e() {
        return this.field_149040_b;
    }

    public int func_149023_f() {
        return this.field_149041_c;
    }

    public int func_149034_g() {
        return this.field_149038_d;
    }

    public int func_149029_h() {
        return this.field_149039_e;
    }

    public int func_149026_i() {
        return this.field_149036_f;
    }

    public int func_149033_j() {
        return this.field_149037_g;
    }

    public int func_149031_k() {
        return this.field_149047_h;
    }

    public byte func_149028_l() {
        return this.field_149048_i;
    }

    public byte func_149030_m() {
        return this.field_149045_j;
    }

    public byte func_149032_n() {
        return this.field_149046_k;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_180721_a((INetHandlerPlayClient) handler);
    }
}
