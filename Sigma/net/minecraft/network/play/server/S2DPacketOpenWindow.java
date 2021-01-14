package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S2DPacketOpenWindow implements Packet {
    private int windowId;
    private String inventoryType;
    private IChatComponent windowTitle;
    private int slotCount;
    private int entityId;
    private static final String __OBFID = "CL_00001293";

    public S2DPacketOpenWindow() {
    }

    public S2DPacketOpenWindow(int p_i45981_1_, String p_i45981_2_, IChatComponent p_i45981_3_) {
        this(p_i45981_1_, p_i45981_2_, p_i45981_3_, 0);
    }

    public S2DPacketOpenWindow(int p_i45982_1_, String p_i45982_2_, IChatComponent p_i45982_3_, int p_i45982_4_) {
        this.windowId = p_i45982_1_;
        this.inventoryType = p_i45982_2_;
        this.windowTitle = p_i45982_3_;
        this.slotCount = p_i45982_4_;
    }

    public S2DPacketOpenWindow(int p_i45983_1_, String p_i45983_2_, IChatComponent p_i45983_3_, int p_i45983_4_, int p_i45983_5_) {
        this(p_i45983_1_, p_i45983_2_, p_i45983_3_, p_i45983_4_);
        this.entityId = p_i45983_5_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleOpenWindow(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.windowId = data.readUnsignedByte();
        this.inventoryType = data.readStringFromBuffer(32);
        this.windowTitle = data.readChatComponent();
        this.slotCount = data.readUnsignedByte();

        if (this.inventoryType.equals("EntityHorse")) {
            this.entityId = data.readInt();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeByte(this.windowId);
        data.writeString(this.inventoryType);
        data.writeChatComponent(this.windowTitle);
        data.writeByte(this.slotCount);

        if (this.inventoryType.equals("EntityHorse")) {
            data.writeInt(this.entityId);
        }
    }

    public int func_148901_c() {
        return this.windowId;
    }

    public String func_148902_e() {
        return this.inventoryType;
    }

    public IChatComponent func_179840_c() {
        return this.windowTitle;
    }

    public int func_148898_f() {
        return this.slotCount;
    }

    public int func_148897_h() {
        return this.entityId;
    }

    public boolean func_148900_g() {
        return this.slotCount > 0;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}
