package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0CPacketSpawnPlayer implements Packet {
    private int field_148957_a;
    private UUID field_179820_b;
    private int field_148956_c;
    private int field_148953_d;
    private int field_148954_e;
    private byte field_148951_f;
    private byte field_148952_g;
    private int field_148959_h;
    private DataWatcher field_148960_i;
    private List field_148958_j;
    private static final String __OBFID = "CL_00001281";

    public S0CPacketSpawnPlayer() {
    }

    public S0CPacketSpawnPlayer(EntityPlayer p_i45171_1_) {
        this.field_148957_a = p_i45171_1_.getEntityId();
        this.field_179820_b = p_i45171_1_.getGameProfile().getId();
        this.field_148956_c = MathHelper.floor_double(p_i45171_1_.posX * 32.0D);
        this.field_148953_d = MathHelper.floor_double(p_i45171_1_.posY * 32.0D);
        this.field_148954_e = MathHelper.floor_double(p_i45171_1_.posZ * 32.0D);
        this.field_148951_f = (byte) ((int) (p_i45171_1_.rotationYaw * 256.0F / 360.0F));
        this.field_148952_g = (byte) ((int) (p_i45171_1_.rotationPitch * 256.0F / 360.0F));
        ItemStack var2 = p_i45171_1_.inventory.getCurrentItem();
        this.field_148959_h = var2 == null ? 0 : Item.getIdFromItem(var2.getItem());
        this.field_148960_i = p_i45171_1_.getDataWatcher();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_148957_a = data.readVarIntFromBuffer();
        this.field_179820_b = data.readUuid();
        this.field_148956_c = data.readInt();
        this.field_148953_d = data.readInt();
        this.field_148954_e = data.readInt();
        this.field_148951_f = data.readByte();
        this.field_148952_g = data.readByte();
        this.field_148959_h = data.readShort();
        this.field_148958_j = DataWatcher.readWatchedListFromPacketBuffer(data);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_148957_a);
        data.writeUuid(this.field_179820_b);
        data.writeInt(this.field_148956_c);
        data.writeInt(this.field_148953_d);
        data.writeInt(this.field_148954_e);
        data.writeByte(this.field_148951_f);
        data.writeByte(this.field_148952_g);
        data.writeShort(this.field_148959_h);
        this.field_148960_i.writeTo(data);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSpawnPlayer(this);
    }

    public List func_148944_c() {
        if (this.field_148958_j == null) {
            this.field_148958_j = this.field_148960_i.getAllWatched();
        }

        return this.field_148958_j;
    }

    public int func_148943_d() {
        return this.field_148957_a;
    }

    public UUID func_179819_c() {
        return this.field_179820_b;
    }

    public int func_148942_f() {
        return this.field_148956_c;
    }

    public int func_148949_g() {
        return this.field_148953_d;
    }

    public int func_148946_h() {
        return this.field_148954_e;
    }

    public byte func_148941_i() {
        return this.field_148951_f;
    }

    public byte func_148945_j() {
        return this.field_148952_g;
    }

    public int func_148947_k() {
        return this.field_148959_h;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
