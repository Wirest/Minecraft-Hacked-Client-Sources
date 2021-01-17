// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S04PacketEntityEquipment implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private int equipmentSlot;
    private ItemStack itemStack;
    
    public S04PacketEntityEquipment() {
    }
    
    public S04PacketEntityEquipment(final int entityIDIn, final int p_i45221_2_, final ItemStack itemStackIn) {
        this.entityID = entityIDIn;
        this.equipmentSlot = p_i45221_2_;
        this.itemStack = ((itemStackIn == null) ? null : itemStackIn.copy());
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.equipmentSlot = buf.readShort();
        this.itemStack = buf.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeShort(this.equipmentSlot);
        buf.writeItemStackToBuffer(this.itemStack);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityEquipment(this);
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public int getEntityID() {
        return this.entityID;
    }
    
    public int getEquipmentSlot() {
        return this.equipmentSlot;
    }
}
