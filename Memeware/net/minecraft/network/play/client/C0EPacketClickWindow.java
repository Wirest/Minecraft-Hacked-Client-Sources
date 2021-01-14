package net.minecraft.network.play.client;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0EPacketClickWindow implements Packet {
    /**
     * The id of the window which was clicked. 0 for player inventory.
     */
    private int windowId;

    /**
     * Id of the clicked slot
     */
    private int slotId;

    /**
     * Button used
     */
    private int usedButton;

    /**
     * A unique number for the action, used for transaction handling
     */
    private short actionNumber;

    /**
     * The item stack present in the slot
     */
    private ItemStack clickedItem;

    /**
     * Inventory operation mode
     */
    private int mode;
    private static final String __OBFID = "CL_00001353";

    public C0EPacketClickWindow() {
    }

    public C0EPacketClickWindow(int p_i45246_1_, int p_i45246_2_, int p_i45246_3_, int p_i45246_4_, ItemStack p_i45246_5_, short p_i45246_6_) {
        this.windowId = p_i45246_1_;
        this.slotId = p_i45246_2_;
        this.usedButton = p_i45246_3_;
        this.clickedItem = p_i45246_5_ != null ? p_i45246_5_.copy() : null;
        this.actionNumber = p_i45246_6_;
        this.mode = p_i45246_4_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processClickWindow(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.windowId = data.readByte();
        this.slotId = data.readShort();
        this.usedButton = data.readByte();
        this.actionNumber = data.readShort();
        this.mode = data.readByte();
        this.clickedItem = data.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeByte(this.windowId);
        data.writeShort(this.slotId);
        data.writeByte(this.usedButton);
        data.writeShort(this.actionNumber);
        data.writeByte(this.mode);
        data.writeItemStackToBuffer(this.clickedItem);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public int getUsedButton() {
        return this.usedButton;
    }

    public short getActionNumber() {
        return this.actionNumber;
    }

    public ItemStack getClickedItem() {
        return this.clickedItem;
    }

    public int getMode() {
        return this.mode;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }
}
