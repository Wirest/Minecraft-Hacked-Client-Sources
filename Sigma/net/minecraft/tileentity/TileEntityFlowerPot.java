package net.minecraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityFlowerPot extends TileEntity {
    private Item flowerPotItem;
    private int flowerPotData;
    private static final String __OBFID = "CL_00000356";

    public TileEntityFlowerPot() {
    }

    public TileEntityFlowerPot(Item p_i45442_1_, int p_i45442_2_) {
        flowerPotItem = p_i45442_1_;
        flowerPotData = p_i45442_2_;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ResourceLocation var2 = (ResourceLocation) Item.itemRegistry.getNameForObject(flowerPotItem);
        compound.setString("Item", var2 == null ? "" : var2.toString());
        compound.setInteger("Data", flowerPotData);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("Item", 8)) {
            flowerPotItem = Item.getByNameOrId(compound.getString("Item"));
        } else {
            flowerPotItem = Item.getItemById(compound.getInteger("Item"));
        }

        flowerPotData = compound.getInteger("Data");
    }

    /**
     * Overriden in a sign to provide the text.
     */
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToNBT(var1);
        var1.removeTag("Item");
        var1.setInteger("Item", Item.getIdFromItem(flowerPotItem));
        return new S35PacketUpdateTileEntity(pos, 5, var1);
    }

    public void func_145964_a(Item p_145964_1_, int p_145964_2_) {
        flowerPotItem = p_145964_1_;
        flowerPotData = p_145964_2_;
    }

    public Item getFlowerPotItem() {
        return flowerPotItem;
    }

    public int getFlowerPotData() {
        return flowerPotData;
    }
}
