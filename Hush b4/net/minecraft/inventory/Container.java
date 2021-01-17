// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.item.Item;
import java.util.Iterator;
import net.minecraft.entity.player.InventoryPlayer;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import net.minecraft.item.ItemStack;
import java.util.List;

public abstract class Container
{
    public List<ItemStack> inventoryItemStacks;
    public List<Slot> inventorySlots;
    public int windowId;
    private short transactionID;
    private int dragMode;
    private int dragEvent;
    private final Set<Slot> dragSlots;
    protected List<ICrafting> crafters;
    private Set<EntityPlayer> playerList;
    
    public Container() {
        this.inventoryItemStacks = (List<ItemStack>)Lists.newArrayList();
        this.inventorySlots = (List<Slot>)Lists.newArrayList();
        this.dragMode = -1;
        this.dragSlots = (Set<Slot>)Sets.newHashSet();
        this.crafters = (List<ICrafting>)Lists.newArrayList();
        this.playerList = (Set<EntityPlayer>)Sets.newHashSet();
    }
    
    protected Slot addSlotToContainer(final Slot slotIn) {
        slotIn.slotNumber = this.inventorySlots.size();
        this.inventorySlots.add(slotIn);
        this.inventoryItemStacks.add(null);
        return slotIn;
    }
    
    public void onCraftGuiOpened(final ICrafting listener) {
        if (this.crafters.contains(listener)) {
            throw new IllegalArgumentException("Listener already listening");
        }
        this.crafters.add(listener);
        listener.updateCraftingInventory(this, this.getInventory());
        this.detectAndSendChanges();
    }
    
    public void removeCraftingFromCrafters(final ICrafting listeners) {
        this.crafters.remove(listeners);
    }
    
    public List<ItemStack> getInventory() {
        final List<ItemStack> list = (List<ItemStack>)Lists.newArrayList();
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            list.add(this.inventorySlots.get(i).getStack());
        }
        return list;
    }
    
    public void detectAndSendChanges() {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            final ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack2 = this.inventoryItemStacks.get(i);
            if (!ItemStack.areItemStacksEqual(itemstack2, itemstack)) {
                itemstack2 = ((itemstack == null) ? null : itemstack.copy());
                this.inventoryItemStacks.set(i, itemstack2);
                for (int j = 0; j < this.crafters.size(); ++j) {
                    this.crafters.get(j).sendSlotContents(this, i, itemstack2);
                }
            }
        }
    }
    
    public boolean enchantItem(final EntityPlayer playerIn, final int id) {
        return false;
    }
    
    public Slot getSlotFromInventory(final IInventory inv, final int slotIn) {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            final Slot slot = this.inventorySlots.get(i);
            if (slot.isHere(inv, slotIn)) {
                return slot;
            }
        }
        return null;
    }
    
    public Slot getSlot(final int slotId) {
        return this.inventorySlots.get(slotId);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        final Slot slot = this.inventorySlots.get(index);
        return (slot != null) ? slot.getStack() : null;
    }
    
    public ItemStack slotClick(final int slotId, final int clickedButton, final int mode, final EntityPlayer playerIn) {
        ItemStack itemstack = null;
        final InventoryPlayer inventoryplayer = playerIn.inventory;
        if (mode == 5) {
            final int i = this.dragEvent;
            this.dragEvent = getDragEvent(clickedButton);
            if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
                this.resetDrag();
            }
            else if (inventoryplayer.getItemStack() == null) {
                this.resetDrag();
            }
            else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(clickedButton);
                if (isValidDragMode(this.dragMode, playerIn)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                }
                else {
                    this.resetDrag();
                }
            }
            else if (this.dragEvent == 1) {
                final Slot slot = this.inventorySlots.get(slotId);
                if (slot != null && canAddItemToSlot(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(slot)) {
                    this.dragSlots.add(slot);
                }
            }
            else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemstack2 = inventoryplayer.getItemStack().copy();
                    int j = inventoryplayer.getItemStack().stackSize;
                    for (final Slot slot2 : this.dragSlots) {
                        if (slot2 != null && canAddItemToSlot(slot2, inventoryplayer.getItemStack(), true) && slot2.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.dragSlots.size() && this.canDragIntoSlot(slot2)) {
                            final ItemStack itemstack3 = itemstack2.copy();
                            final int k = slot2.getHasStack() ? slot2.getStack().stackSize : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack3, k);
                            if (itemstack3.stackSize > itemstack3.getMaxStackSize()) {
                                itemstack3.stackSize = itemstack3.getMaxStackSize();
                            }
                            if (itemstack3.stackSize > slot2.getItemStackLimit(itemstack3)) {
                                itemstack3.stackSize = slot2.getItemStackLimit(itemstack3);
                            }
                            j -= itemstack3.stackSize - k;
                            slot2.putStack(itemstack3);
                        }
                    }
                    itemstack2.stackSize = j;
                    if (itemstack2.stackSize <= 0) {
                        itemstack2 = null;
                    }
                    inventoryplayer.setItemStack(itemstack2);
                }
                this.resetDrag();
            }
            else {
                this.resetDrag();
            }
        }
        else if (this.dragEvent != 0) {
            this.resetDrag();
        }
        else if ((mode == 0 || mode == 1) && (clickedButton == 0 || clickedButton == 1)) {
            if (slotId == -999) {
                if (inventoryplayer.getItemStack() != null) {
                    if (clickedButton == 0) {
                        playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack(null);
                    }
                    if (clickedButton == 1) {
                        playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);
                        if (inventoryplayer.getItemStack().stackSize == 0) {
                            inventoryplayer.setItemStack(null);
                        }
                    }
                }
            }
            else if (mode == 1) {
                if (slotId < 0) {
                    return null;
                }
                final Slot slot3 = this.inventorySlots.get(slotId);
                if (slot3 != null && slot3.canTakeStack(playerIn)) {
                    final ItemStack itemstack4 = this.transferStackInSlot(playerIn, slotId);
                    if (itemstack4 != null) {
                        final Item item = itemstack4.getItem();
                        itemstack = itemstack4.copy();
                        if (slot3.getStack() != null && slot3.getStack().getItem() == item) {
                            this.retrySlotClick(slotId, clickedButton, true, playerIn);
                        }
                    }
                }
            }
            else {
                if (slotId < 0) {
                    return null;
                }
                final Slot slot4 = this.inventorySlots.get(slotId);
                if (slot4 != null) {
                    ItemStack itemstack5 = slot4.getStack();
                    final ItemStack itemstack6 = inventoryplayer.getItemStack();
                    if (itemstack5 != null) {
                        itemstack = itemstack5.copy();
                    }
                    if (itemstack5 == null) {
                        if (itemstack6 != null && slot4.isItemValid(itemstack6)) {
                            int k2 = (clickedButton == 0) ? itemstack6.stackSize : 1;
                            if (k2 > slot4.getItemStackLimit(itemstack6)) {
                                k2 = slot4.getItemStackLimit(itemstack6);
                            }
                            if (itemstack6.stackSize >= k2) {
                                slot4.putStack(itemstack6.splitStack(k2));
                            }
                            if (itemstack6.stackSize == 0) {
                                inventoryplayer.setItemStack(null);
                            }
                        }
                    }
                    else if (slot4.canTakeStack(playerIn)) {
                        if (itemstack6 == null) {
                            final int j2 = (clickedButton == 0) ? itemstack5.stackSize : ((itemstack5.stackSize + 1) / 2);
                            final ItemStack itemstack7 = slot4.decrStackSize(j2);
                            inventoryplayer.setItemStack(itemstack7);
                            if (itemstack5.stackSize == 0) {
                                slot4.putStack(null);
                            }
                            slot4.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
                        }
                        else if (slot4.isItemValid(itemstack6)) {
                            if (itemstack5.getItem() == itemstack6.getItem() && itemstack5.getMetadata() == itemstack6.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack5, itemstack6)) {
                                int i2 = (clickedButton == 0) ? itemstack6.stackSize : 1;
                                if (i2 > slot4.getItemStackLimit(itemstack6) - itemstack5.stackSize) {
                                    i2 = slot4.getItemStackLimit(itemstack6) - itemstack5.stackSize;
                                }
                                if (i2 > itemstack6.getMaxStackSize() - itemstack5.stackSize) {
                                    i2 = itemstack6.getMaxStackSize() - itemstack5.stackSize;
                                }
                                itemstack6.splitStack(i2);
                                if (itemstack6.stackSize == 0) {
                                    inventoryplayer.setItemStack(null);
                                }
                                final ItemStack itemStack = itemstack5;
                                itemStack.stackSize += i2;
                            }
                            else if (itemstack6.stackSize <= slot4.getItemStackLimit(itemstack6)) {
                                slot4.putStack(itemstack6);
                                inventoryplayer.setItemStack(itemstack5);
                            }
                        }
                        else if (itemstack5.getItem() == itemstack6.getItem() && itemstack6.getMaxStackSize() > 1 && (!itemstack5.getHasSubtypes() || itemstack5.getMetadata() == itemstack6.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack5, itemstack6)) {
                            final int l1 = itemstack5.stackSize;
                            if (l1 > 0 && l1 + itemstack6.stackSize <= itemstack6.getMaxStackSize()) {
                                final ItemStack itemStack2 = itemstack6;
                                itemStack2.stackSize += l1;
                                itemstack5 = slot4.decrStackSize(l1);
                                if (itemstack5.stackSize == 0) {
                                    slot4.putStack(null);
                                }
                                slot4.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
                            }
                        }
                    }
                    slot4.onSlotChanged();
                }
            }
        }
        else if (mode == 2 && clickedButton >= 0 && clickedButton < 9) {
            final Slot slot5 = this.inventorySlots.get(slotId);
            if (slot5.canTakeStack(playerIn)) {
                final ItemStack itemstack8 = inventoryplayer.getStackInSlot(clickedButton);
                boolean flag = itemstack8 == null || (slot5.inventory == inventoryplayer && slot5.isItemValid(itemstack8));
                int k3 = -1;
                if (!flag) {
                    k3 = inventoryplayer.getFirstEmptyStack();
                    flag |= (k3 > -1);
                }
                if (slot5.getHasStack() && flag) {
                    final ItemStack itemstack9 = slot5.getStack();
                    inventoryplayer.setInventorySlotContents(clickedButton, itemstack9.copy());
                    if ((slot5.inventory != inventoryplayer || !slot5.isItemValid(itemstack8)) && itemstack8 != null) {
                        if (k3 > -1) {
                            inventoryplayer.addItemStackToInventory(itemstack8);
                            slot5.decrStackSize(itemstack9.stackSize);
                            slot5.putStack(null);
                            slot5.onPickupFromSlot(playerIn, itemstack9);
                        }
                    }
                    else {
                        slot5.decrStackSize(itemstack9.stackSize);
                        slot5.putStack(itemstack8);
                        slot5.onPickupFromSlot(playerIn, itemstack9);
                    }
                }
                else if (!slot5.getHasStack() && itemstack8 != null && slot5.isItemValid(itemstack8)) {
                    inventoryplayer.setInventorySlotContents(clickedButton, null);
                    slot5.putStack(itemstack8);
                }
            }
        }
        else if (mode == 3 && playerIn.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0) {
            final Slot slot6 = this.inventorySlots.get(slotId);
            if (slot6 != null && slot6.getHasStack()) {
                final ItemStack itemstack10 = slot6.getStack().copy();
                itemstack10.stackSize = itemstack10.getMaxStackSize();
                inventoryplayer.setItemStack(itemstack10);
            }
        }
        else if (mode == 4 && inventoryplayer.getItemStack() == null && slotId >= 0) {
            final Slot slot7 = this.inventorySlots.get(slotId);
            if (slot7 != null && slot7.getHasStack() && slot7.canTakeStack(playerIn)) {
                final ItemStack itemstack11 = slot7.decrStackSize((clickedButton == 0) ? 1 : slot7.getStack().stackSize);
                slot7.onPickupFromSlot(playerIn, itemstack11);
                playerIn.dropPlayerItemWithRandomChoice(itemstack11, true);
            }
        }
        else if (mode == 6 && slotId >= 0) {
            final Slot slot8 = this.inventorySlots.get(slotId);
            final ItemStack itemstack12 = inventoryplayer.getItemStack();
            if (itemstack12 != null && (slot8 == null || !slot8.getHasStack() || !slot8.canTakeStack(playerIn))) {
                final int i3 = (clickedButton == 0) ? 0 : (this.inventorySlots.size() - 1);
                final int j3 = (clickedButton == 0) ? 1 : -1;
                for (int l2 = 0; l2 < 2; ++l2) {
                    for (int i4 = i3; i4 >= 0 && i4 < this.inventorySlots.size() && itemstack12.stackSize < itemstack12.getMaxStackSize(); i4 += j3) {
                        final Slot slot9 = this.inventorySlots.get(i4);
                        if (slot9.getHasStack() && canAddItemToSlot(slot9, itemstack12, true) && slot9.canTakeStack(playerIn) && this.canMergeSlot(itemstack12, slot9) && (l2 != 0 || slot9.getStack().stackSize != slot9.getStack().getMaxStackSize())) {
                            final int m = Math.min(itemstack12.getMaxStackSize() - itemstack12.stackSize, slot9.getStack().stackSize);
                            final ItemStack itemstack13 = slot9.decrStackSize(m);
                            final ItemStack itemStack3 = itemstack12;
                            itemStack3.stackSize += m;
                            if (itemstack13.stackSize <= 0) {
                                slot9.putStack(null);
                            }
                            slot9.onPickupFromSlot(playerIn, itemstack13);
                        }
                    }
                }
            }
            this.detectAndSendChanges();
        }
        return itemstack;
    }
    
    public boolean canMergeSlot(final ItemStack stack, final Slot p_94530_2_) {
        return true;
    }
    
    protected void retrySlotClick(final int slotId, final int clickedButton, final boolean mode, final EntityPlayer playerIn) {
        this.slotClick(slotId, clickedButton, 1, playerIn);
    }
    
    public void onContainerClosed(final EntityPlayer playerIn) {
        final InventoryPlayer inventoryplayer = playerIn.inventory;
        if (inventoryplayer.getItemStack() != null) {
            playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
            inventoryplayer.setItemStack(null);
        }
    }
    
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        this.detectAndSendChanges();
    }
    
    public void putStackInSlot(final int slotID, final ItemStack stack) {
        this.getSlot(slotID).putStack(stack);
    }
    
    public void putStacksInSlots(final ItemStack[] p_75131_1_) {
        for (int i = 0; i < p_75131_1_.length; ++i) {
            this.getSlot(i).putStack(p_75131_1_[i]);
        }
    }
    
    public void updateProgressBar(final int id, final int data) {
    }
    
    public short getNextTransactionID(final InventoryPlayer p_75136_1_) {
        return (short)(++this.transactionID);
    }
    
    public boolean getCanCraft(final EntityPlayer p_75129_1_) {
        return !this.playerList.contains(p_75129_1_);
    }
    
    public void setCanCraft(final EntityPlayer p_75128_1_, final boolean p_75128_2_) {
        if (p_75128_2_) {
            this.playerList.remove(p_75128_1_);
        }
        else {
            this.playerList.add(p_75128_1_);
        }
    }
    
    public abstract boolean canInteractWith(final EntityPlayer p0);
    
    protected boolean mergeItemStack(final ItemStack stack, final int startIndex, final int endIndex, final boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }
        if (stack.isStackable()) {
            while (stack.stackSize > 0 && ((!reverseDirection && i < endIndex) || (reverseDirection && i >= startIndex))) {
                final Slot slot = this.inventorySlots.get(i);
                final ItemStack itemstack = slot.getStack();
                if (itemstack != null && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                    final int j = itemstack.stackSize + stack.stackSize;
                    if (j <= stack.getMaxStackSize()) {
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                        flag = true;
                    }
                    else if (itemstack.stackSize < stack.getMaxStackSize()) {
                        stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
                        itemstack.stackSize = stack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag = true;
                    }
                }
                if (reverseDirection) {
                    --i;
                }
                else {
                    ++i;
                }
            }
        }
        if (stack.stackSize > 0) {
            if (reverseDirection) {
                i = endIndex - 1;
            }
            else {
                i = startIndex;
            }
            while ((!reverseDirection && i < endIndex) || (reverseDirection && i >= startIndex)) {
                final Slot slot2 = this.inventorySlots.get(i);
                final ItemStack itemstack2 = slot2.getStack();
                if (itemstack2 == null) {
                    slot2.putStack(stack.copy());
                    slot2.onSlotChanged();
                    stack.stackSize = 0;
                    flag = true;
                    break;
                }
                if (reverseDirection) {
                    --i;
                }
                else {
                    ++i;
                }
            }
        }
        return flag;
    }
    
    public static int extractDragMode(final int p_94529_0_) {
        return p_94529_0_ >> 2 & 0x3;
    }
    
    public static int getDragEvent(final int p_94532_0_) {
        return p_94532_0_ & 0x3;
    }
    
    public static int func_94534_d(final int p_94534_0_, final int p_94534_1_) {
        return (p_94534_0_ & 0x3) | (p_94534_1_ & 0x3) << 2;
    }
    
    public static boolean isValidDragMode(final int dragModeIn, final EntityPlayer player) {
        return dragModeIn == 0 || dragModeIn == 1 || (dragModeIn == 2 && player.capabilities.isCreativeMode);
    }
    
    protected void resetDrag() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }
    
    public static boolean canAddItemToSlot(final Slot slotIn, final ItemStack stack, final boolean stackSizeMatters) {
        boolean flag = slotIn == null || !slotIn.getHasStack();
        if (slotIn != null && slotIn.getHasStack() && stack != null && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)) {
            flag |= (slotIn.getStack().stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= stack.getMaxStackSize());
        }
        return flag;
    }
    
    public static void computeStackSize(final Set<Slot> p_94525_0_, final int p_94525_1_, final ItemStack p_94525_2_, final int p_94525_3_) {
        switch (p_94525_1_) {
            case 0: {
                p_94525_2_.stackSize = MathHelper.floor_float(p_94525_2_.stackSize / (float)p_94525_0_.size());
                break;
            }
            case 1: {
                p_94525_2_.stackSize = 1;
                break;
            }
            case 2: {
                p_94525_2_.stackSize = p_94525_2_.getItem().getItemStackLimit();
                break;
            }
        }
        p_94525_2_.stackSize += p_94525_3_;
    }
    
    public boolean canDragIntoSlot(final Slot p_94531_1_) {
        return true;
    }
    
    public static int calcRedstone(final TileEntity te) {
        return (te instanceof IInventory) ? calcRedstoneFromInventory((IInventory)te) : 0;
    }
    
    public static int calcRedstoneFromInventory(final IInventory inv) {
        if (inv == null) {
            return 0;
        }
        int i = 0;
        float f = 0.0f;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            final ItemStack itemstack = inv.getStackInSlot(j);
            if (itemstack != null) {
                f += itemstack.stackSize / (float)Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
                ++i;
            }
        }
        f /= inv.getSizeInventory();
        return MathHelper.floor_float(f * 14.0f) + ((i > 0) ? 1 : 0);
    }
}
