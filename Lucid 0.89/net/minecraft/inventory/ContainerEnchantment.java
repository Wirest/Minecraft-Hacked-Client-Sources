package net.minecraft.inventory;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container
{
    /** SlotEnchantmentTable object with ItemStack to be enchanted */
    public IInventory tableInventory;

    /** current world (for bookshelf counting) */
    private World worldPointer;
    private BlockPos position;
    private Random rand;
    public int xpSeed;

    /** 3-member array storing the enchantment levels of each slot */
    public int[] enchantLevels;
    public int[] field_178151_h;

    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn)
    {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public ContainerEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos)
    {
        this.tableInventory = new InventoryBasic("Enchant", true, 2)
        {
            @Override
			public int getInventoryStackLimit()
            {
                return 64;
            }
            @Override
			public void markDirty()
            {
                super.markDirty();
                ContainerEnchantment.this.onCraftMatrixChanged(this);
            }
        };
        this.rand = new Random();
        this.enchantLevels = new int[3];
        this.field_178151_h = new int[] { -1, -1, -1};
        this.worldPointer = worldIn;
        this.position = pos;
        this.xpSeed = playerInv.player.getXPSeed();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47)
        {
            @Override
			public boolean isItemValid(ItemStack stack)
            {
                return true;
            }
            @Override
			public int getSlotStackLimit()
            {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47)
        {
            @Override
			public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == Items.dye && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });
        int var4;

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (int var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(playerInv, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(playerInv, var4, 8 + var4 * 18, 142));
        }
    }

    @Override
	public void onCraftGuiOpened(ICrafting listener)
    {
        super.onCraftGuiOpened(listener);
        listener.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
        listener.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
        listener.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
        listener.sendProgressBarUpdate(this, 3, this.xpSeed & -16);
        listener.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
        listener.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
        listener.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);
            var2.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
            var2.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
            var2.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
            var2.sendProgressBarUpdate(this, 3, this.xpSeed & -16);
            var2.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
            var2.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
            var2.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
        }
    }

    @Override
	public void updateProgressBar(int id, int data)
    {
        if (id >= 0 && id <= 2)
        {
            this.enchantLevels[id] = data;
        }
        else if (id == 3)
        {
            this.xpSeed = data;
        }
        else if (id >= 4 && id <= 6)
        {
            this.field_178151_h[id - 4] = data;
        }
        else
        {
            super.updateProgressBar(id, data);
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        if (inventoryIn == this.tableInventory)
        {
            ItemStack var2 = inventoryIn.getStackInSlot(0);
            int var3;

            if (var2 != null && var2.isItemEnchantable())
            {
                if (!this.worldPointer.isRemote)
                {
                    var3 = 0;
                    int var4;

                    for (var4 = -1; var4 <= 1; ++var4)
                    {
                        for (int var5 = -1; var5 <= 1; ++var5)
                        {
                            if ((var4 != 0 || var5 != 0) && this.worldPointer.isAirBlock(this.position.add(var5, 0, var4)) && this.worldPointer.isAirBlock(this.position.add(var5, 1, var4)))
                            {
                                if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 0, var4 * 2)).getBlock() == Blocks.bookshelf)
                                {
                                    ++var3;
                                }

                                if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 1, var4 * 2)).getBlock() == Blocks.bookshelf)
                                {
                                    ++var3;
                                }

                                if (var5 != 0 && var4 != 0)
                                {
                                    if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 0, var4)).getBlock() == Blocks.bookshelf)
                                    {
                                        ++var3;
                                    }

                                    if (this.worldPointer.getBlockState(this.position.add(var5 * 2, 1, var4)).getBlock() == Blocks.bookshelf)
                                    {
                                        ++var3;
                                    }

                                    if (this.worldPointer.getBlockState(this.position.add(var5, 0, var4 * 2)).getBlock() == Blocks.bookshelf)
                                    {
                                        ++var3;
                                    }

                                    if (this.worldPointer.getBlockState(this.position.add(var5, 1, var4 * 2)).getBlock() == Blocks.bookshelf)
                                    {
                                        ++var3;
                                    }
                                }
                            }
                        }
                    }

                    this.rand.setSeed(this.xpSeed);

                    for (var4 = 0; var4 < 3; ++var4)
                    {
                        this.enchantLevels[var4] = EnchantmentHelper.calcItemStackEnchantability(this.rand, var4, var3, var2);
                        this.field_178151_h[var4] = -1;

                        if (this.enchantLevels[var4] < var4 + 1)
                        {
                            this.enchantLevels[var4] = 0;
                        }
                    }

                    for (var4 = 0; var4 < 3; ++var4)
                    {
                        if (this.enchantLevels[var4] > 0)
                        {
                            List var7 = this.func_178148_a(var2, var4, this.enchantLevels[var4]);

                            if (var7 != null && !var7.isEmpty())
                            {
                                EnchantmentData var6 = (EnchantmentData)var7.get(this.rand.nextInt(var7.size()));
                                this.field_178151_h[var4] = var6.enchantmentobj.effectId | var6.enchantmentLevel << 8;
                            }
                        }
                    }

                    this.detectAndSendChanges();
                }
            }
            else
            {
                for (var3 = 0; var3 < 3; ++var3)
                {
                    this.enchantLevels[var3] = 0;
                    this.field_178151_h[var3] = -1;
                }
            }
        }
    }

    /**
     * Handles the given Button-click on the server, currently only used by enchanting. Name is for legacy.
     */
    @Override
	public boolean enchantItem(EntityPlayer playerIn, int id)
    {
        ItemStack var3 = this.tableInventory.getStackInSlot(0);
        ItemStack var4 = this.tableInventory.getStackInSlot(1);
        int var5 = id + 1;

        if ((var4 == null || var4.stackSize < var5) && !playerIn.capabilities.isCreativeMode)
        {
            return false;
        }
        else if (this.enchantLevels[id] > 0 && var3 != null && (playerIn.experienceLevel >= var5 && playerIn.experienceLevel >= this.enchantLevels[id] || playerIn.capabilities.isCreativeMode))
        {
            if (!this.worldPointer.isRemote)
            {
                List var6 = this.func_178148_a(var3, id, this.enchantLevels[id]);
                boolean var7 = var3.getItem() == Items.book;

                if (var6 != null)
                {
                    playerIn.removeExperienceLevel(var5);

                    if (var7)
                    {
                        var3.setItem(Items.enchanted_book);
                    }

                    for (int var8 = 0; var8 < var6.size(); ++var8)
                    {
                        EnchantmentData var9 = (EnchantmentData)var6.get(var8);

                        if (var7)
                        {
                            Items.enchanted_book.addEnchantment(var3, var9);
                        }
                        else
                        {
                            var3.addEnchantment(var9.enchantmentobj, var9.enchantmentLevel);
                        }
                    }

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        var4.stackSize -= var5;

                        if (var4.stackSize <= 0)
                        {
                            this.tableInventory.setInventorySlotContents(1, (ItemStack)null);
                        }
                    }

                    this.tableInventory.markDirty();
                    this.xpSeed = playerIn.getXPSeed();
                    this.onCraftMatrixChanged(this.tableInventory);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private List func_178148_a(ItemStack stack, int p_178148_2_, int p_178148_3_)
    {
        this.rand.setSeed(this.xpSeed + p_178148_2_);
        List var4 = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_);

        if (stack.getItem() == Items.book && var4 != null && var4.size() > 1)
        {
            var4.remove(this.rand.nextInt(var4.size()));
        }

        return var4;
    }

    public int getLapisAmount()
    {
        ItemStack var1 = this.tableInventory.getStackInSlot(1);
        return var1 == null ? 0 : var1.stackSize;
    }

    /**
     * Called when the container is closed.
     */
    @Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.worldPointer.isRemote)
        {
            for (int var2 = 0; var2 < this.tableInventory.getSizeInventory(); ++var2)
            {
                ItemStack var3 = this.tableInventory.getStackInSlotOnClosing(var2);

                if (var3 != null)
                {
                    playerIn.dropPlayerItemWithRandomChoice(var3, false);
                }
            }
        }
    }

    @Override
	public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.worldPointer.getBlockState(this.position).getBlock() != Blocks.enchanting_table ? false : playerIn.getDistanceSq(this.position.getX() + 0.5D, this.position.getY() + 0.5D, this.position.getZ() + 0.5D) <= 64.0D;
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(index);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(var5, 2, 38, true))
                {
                    return null;
                }
            }
            else if (index == 1)
            {
                if (!this.mergeItemStack(var5, 2, 38, true))
                {
                    return null;
                }
            }
            else if (var5.getItem() == Items.dye && EnumDyeColor.byDyeDamage(var5.getMetadata()) == EnumDyeColor.BLUE)
            {
                if (!this.mergeItemStack(var5, 1, 2, true))
                {
                    return null;
                }
            }
            else
            {
                if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(var5))
                {
                    return null;
                }

                if (var5.hasTagCompound() && var5.stackSize == 1)
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(var5.copy());
                    var5.stackSize = 0;
                }
                else if (var5.stackSize >= 1)
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(var5.getItem(), 1, var5.getMetadata()));
                    --var5.stackSize;
                }
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(playerIn, var5);
        }

        return var3;
    }
}
