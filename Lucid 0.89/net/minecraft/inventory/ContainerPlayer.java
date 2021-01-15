package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPlayer extends Container
{
    /** The crafting matrix inventory. */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public IInventory craftResult = new InventoryCraftResult();
    
    /** Determines if inventory manipulation should be handled. */
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;
    
    public ContainerPlayer(final InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player)
    {
	this.isLocalWorld = localWorld;
	this.thePlayer = player;
	this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 144, 36));
	int var4;
	int var5;
	
	for (var4 = 0; var4 < 2; ++var4)
	{
	    for (var5 = 0; var5 < 2; ++var5)
	    {
		this.addSlotToContainer(new Slot(this.craftMatrix, var5 + var4 * 2, 88 + var5 * 18, 26 + var4 * 18));
	    }
	}
	
	for (var4 = 0; var4 < 4; ++var4)
	{
	    final int var44 = var4;
	    this.addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 1 - var4, 8, 8 + var4 * 18)
	    {
		@Override
		public int getSlotStackLimit()
		{
		    return 1;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack)
		{
		    return stack == null ? false : (stack.getItem() instanceof ItemArmor ? ((ItemArmor) stack.getItem()).armorType == var44 : (stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull ? false : var44 == 0));
		}
		
		@Override
		public String getSlotTexture()
		{
		    return ItemArmor.EMPTY_SLOT_NAMES[var44];
		}
	    });
	}
	
	for (var4 = 0; var4 < 3; ++var4)
	{
	    for (var5 = 0; var5 < 9; ++var5)
	    {
		this.addSlotToContainer(new Slot(playerInventory, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18));
	    }
	}
	
	for (var4 = 0; var4 < 9; ++var4)
	{
	    this.addSlotToContainer(new Slot(playerInventory, var4, 8 + var4 * 18, 142));
	}
	
	this.onCraftMatrixChanged(this.craftMatrix);
    }
    
    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
	this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
    }
    
    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
	super.onContainerClosed(playerIn);
	
	for (int var2 = 0; var2 < 4; ++var2)
	{
	    ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);
	    
	    if (var3 != null)
	    {
		playerIn.dropPlayerItemWithRandomChoice(var3, false);
	    }
	}
	this.craftResult.setInventorySlotContents(0, (ItemStack) null);
	
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
	return true;
    }
    
    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
	ItemStack var3 = null;
	Slot var4 = (Slot) this.inventorySlots.get(index);
	
	if (var4 != null && var4.getHasStack())
	{
	    ItemStack var5 = var4.getStack();
	    var3 = var5.copy();
	    
	    if (index == 0)
	    {
		if (!this.mergeItemStack(var5, 9, 45, true))
		{
		    return null;
		}
		
		var4.onSlotChange(var5, var3);
	    }
	    else if (index >= 1 && index < 5)
	    {
		if (!this.mergeItemStack(var5, 9, 45, false))
		{
		    return null;
		}
	    }
	    else if (index >= 5 && index < 9)
	    {
		if (!this.mergeItemStack(var5, 9, 45, false))
		{
		    return null;
		}
	    }
	    else if (var3.getItem() instanceof ItemArmor && !((Slot) this.inventorySlots.get(5 + ((ItemArmor) var3.getItem()).armorType)).getHasStack())
	    {
		int var6 = 5 + ((ItemArmor) var3.getItem()).armorType;
		
		if (!this.mergeItemStack(var5, var6, var6 + 1, false))
		{
		    return null;
		}
	    }
	    else if (index >= 9 && index < 36)
	    {
		if (!this.mergeItemStack(var5, 36, 45, false))
		{
		    return null;
		}
	    }
	    else if (index >= 36 && index < 45)
	    {
		if (!this.mergeItemStack(var5, 9, 36, false))
		{
		    return null;
		}
	    }
	    else if (!this.mergeItemStack(var5, 9, 45, false))
	    {
		return null;
	    }
	    
	    if (var5.stackSize == 0)
	    {
		var4.putStack((ItemStack) null);
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
    
    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_)
    {
	return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(stack, p_94530_2_);
    }
}
