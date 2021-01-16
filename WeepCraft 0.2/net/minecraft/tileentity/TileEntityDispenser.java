package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDispenser extends TileEntityLockableLoot
{
    private static final Random RNG = new Random();
    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>func_191197_a(9, ItemStack.field_190927_a);

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 9;
    }

    public boolean func_191420_l()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.func_190926_b())
            {
                return false;
            }
        }

        return true;
    }

    public int getDispenseSlot()
    {
        this.fillWithLoot((EntityPlayer)null);
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.stacks.size(); ++k)
        {
            if (!((ItemStack)this.stacks.get(k)).func_190926_b() && RNG.nextInt(j++) == 0)
            {
                i = k;
            }
        }

        return i;
    }

    /**
     * Add the given ItemStack to this Dispenser. Return the Slot the Item was placed in or -1 if no free slot is
     * available.
     */
    public int addItemStack(ItemStack stack)
    {
        for (int i = 0; i < this.stacks.size(); ++i)
        {
            if (((ItemStack)this.stacks.get(i)).func_190926_b())
            {
                this.setInventorySlotContents(i, stack);
                return i;
            }
        }

        return -1;
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.field_190577_o : "container.dispenser";
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDispenser.class, new String[] {"Items"}));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.stacks = NonNullList.<ItemStack>func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.func_191283_b(compound, this.stacks);
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.field_190577_o = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.func_191282_a(compound, this.stacks);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.field_190577_o);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public String getGuiID()
    {
        return "minecraft:dispenser";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        this.fillWithLoot(playerIn);
        return new ContainerDispenser(playerInventory, this);
    }

    protected NonNullList<ItemStack> func_190576_q()
    {
        return this.stacks;
    }
}
