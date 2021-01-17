// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;

public class TileEntityFlowerPot extends TileEntity
{
    private Item flowerPotItem;
    private int flowerPotData;
    
    public TileEntityFlowerPot() {
    }
    
    public TileEntityFlowerPot(final Item potItem, final int potData) {
        this.flowerPotItem = potItem;
        this.flowerPotData = potData;
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        final ResourceLocation resourcelocation = Item.itemRegistry.getNameForObject(this.flowerPotItem);
        compound.setString("Item", (resourcelocation == null) ? "" : resourcelocation.toString());
        compound.setInteger("Data", this.flowerPotData);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Item", 8)) {
            this.flowerPotItem = Item.getByNameOrId(compound.getString("Item"));
        }
        else {
            this.flowerPotItem = Item.getItemById(compound.getInteger("Item"));
        }
        this.flowerPotData = compound.getInteger("Data");
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        nbttagcompound.removeTag("Item");
        nbttagcompound.setInteger("Item", Item.getIdFromItem(this.flowerPotItem));
        return new S35PacketUpdateTileEntity(this.pos, 5, nbttagcompound);
    }
    
    public void setFlowerPotData(final Item potItem, final int potData) {
        this.flowerPotItem = potItem;
        this.flowerPotData = potData;
    }
    
    public Item getFlowerPotItem() {
        return this.flowerPotItem;
    }
    
    public int getFlowerPotData() {
        return this.flowerPotData;
    }
}
