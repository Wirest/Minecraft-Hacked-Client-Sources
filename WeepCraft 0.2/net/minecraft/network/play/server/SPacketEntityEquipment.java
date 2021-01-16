package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityEquipment implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private EntityEquipmentSlot equipmentSlot;
    private ItemStack itemStack = ItemStack.field_190927_a;

    public SPacketEntityEquipment()
    {
    }

    public SPacketEntityEquipment(int entityIdIn, EntityEquipmentSlot equipmentSlotIn, ItemStack itemStackIn)
    {
        this.entityID = entityIdIn;
        this.equipmentSlot = equipmentSlotIn;
        this.itemStack = itemStackIn.copy();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarIntFromBuffer();
        this.equipmentSlot = (EntityEquipmentSlot)buf.readEnumValue(EntityEquipmentSlot.class);
        this.itemStack = buf.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeEnumValue(this.equipmentSlot);
        buf.writeItemStackToBuffer(this.itemStack);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityEquipment(this);
    }

    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    public int getEntityID()
    {
        return this.entityID;
    }

    public EntityEquipmentSlot getEquipmentSlot()
    {
        return this.equipmentSlot;
    }
}
