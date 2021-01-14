package net.minecraft.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public abstract class Container {
   public List inventoryItemStacks = Lists.newArrayList();
   public List inventorySlots = Lists.newArrayList();
   public int windowId;
   private short transactionID;
   private int dragMode = -1;
   private int dragEvent;
   private final Set dragSlots = Sets.newHashSet();
   protected List crafters = Lists.newArrayList();
   private Set playerList = Sets.newHashSet();

   protected Slot addSlotToContainer(Slot slotIn) {
      slotIn.slotNumber = this.inventorySlots.size();
      this.inventorySlots.add(slotIn);
      this.inventoryItemStacks.add((ItemStack)null);
      return slotIn;
   }

   public void onCraftGuiOpened(ICrafting listener) {
      if (this.crafters.contains(listener)) {
         throw new IllegalArgumentException("Listener already listening");
      } else {
         this.crafters.add(listener);
         listener.updateCraftingInventory(this, this.getInventory());
         this.detectAndSendChanges();
      }
   }

   public void removeCraftingFromCrafters(ICrafting listeners) {
      this.crafters.remove(listeners);
   }

   public List getInventory() {
      List list = Lists.newArrayList();

      for(int i = 0; i < this.inventorySlots.size(); ++i) {
         list.add(((Slot)this.inventorySlots.get(i)).getStack());
      }

      return list;
   }

   public void detectAndSendChanges() {
      for(int i = 0; i < this.inventorySlots.size(); ++i) {
         ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
         ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);
         if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
            itemstack1 = itemstack == null ? null : itemstack.copy();
            this.inventoryItemStacks.set(i, itemstack1);

            for(int j = 0; j < this.crafters.size(); ++j) {
               ((ICrafting)this.crafters.get(j)).sendSlotContents(this, i, itemstack1);
            }
         }
      }

   }

   public boolean enchantItem(EntityPlayer playerIn, int id) {
      return false;
   }

   public Slot getSlotFromInventory(IInventory inv, int slotIn) {
      for(int i = 0; i < this.inventorySlots.size(); ++i) {
         Slot slot = (Slot)this.inventorySlots.get(i);
         if (slot.isHere(inv, slotIn)) {
            return slot;
         }
      }

      return null;
   }

   public Slot getSlot(int slotId) {
      return (Slot)this.inventorySlots.get(slotId);
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      Slot slot = (Slot)this.inventorySlots.get(index);
      return slot != null ? slot.getStack() : null;
   }

   public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
      ItemStack itemstack = null;
      InventoryPlayer inventoryplayer = playerIn.inventory;
      int j;
      ItemStack itemstack9;
      if (mode == 5) {
         int i = this.dragEvent;
         this.dragEvent = getDragEvent(clickedButton);
         if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent) {
            this.resetDrag();
         } else if (inventoryplayer.getItemStack() == null) {
            this.resetDrag();
         } else if (this.dragEvent == 0) {
            this.dragMode = extractDragMode(clickedButton);
            if (isValidDragMode(this.dragMode, playerIn)) {
               this.dragEvent = 1;
               this.dragSlots.clear();
            } else {
               this.resetDrag();
            }
         } else if (this.dragEvent == 1) {
            Slot slot = (Slot)this.inventorySlots.get(slotId);
            if (slot != null && canAddItemToSlot(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(slot)) {
               this.dragSlots.add(slot);
            }
         } else if (this.dragEvent == 2) {
            if (!this.dragSlots.isEmpty()) {
               itemstack9 = inventoryplayer.getItemStack().copy();
               j = inventoryplayer.getItemStack().stackSize;
               Iterator var10 = this.dragSlots.iterator();

               while(var10.hasNext()) {
                  Slot slot1 = (Slot)var10.next();
                  if (slot1 != null && canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.dragSlots.size() && this.canDragIntoSlot(slot1)) {
                     ItemStack itemstack1 = itemstack9.copy();
                     int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                     computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);
                     if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
                        itemstack1.stackSize = itemstack1.getMaxStackSize();
                     }

                     if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1)) {
                        itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
                     }

                     j -= itemstack1.stackSize - k;
                     slot1.putStack(itemstack1);
                  }
               }

               itemstack9.stackSize = j;
               if (itemstack9.stackSize <= 0) {
                  itemstack9 = null;
               }

               inventoryplayer.setItemStack(itemstack9);
            }

            this.resetDrag();
         } else {
            this.resetDrag();
         }
      } else if (this.dragEvent != 0) {
         this.resetDrag();
      } else {
         Slot slot7;
         int i2;
         ItemStack itemstack12;
         if ((mode == 0 || mode == 1) && (clickedButton == 0 || clickedButton == 1)) {
            if (slotId == -999) {
               if (inventoryplayer.getItemStack() != null) {
                  if (clickedButton == 0) {
                     playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
                     inventoryplayer.setItemStack((ItemStack)null);
                  }

                  if (clickedButton == 1) {
                     playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);
                     if (inventoryplayer.getItemStack().stackSize == 0) {
                        inventoryplayer.setItemStack((ItemStack)null);
                     }
                  }
               }
            } else if (mode == 1) {
               if (slotId < 0) {
                  return null;
               }

               slot7 = (Slot)this.inventorySlots.get(slotId);
               if (slot7 != null && slot7.canTakeStack(playerIn)) {
                  itemstack9 = this.transferStackInSlot(playerIn, slotId);
                  if (itemstack9 != null) {
                     Item item = itemstack9.getItem();
                     itemstack = itemstack9.copy();
                     if (slot7.getStack() != null && slot7.getStack().getItem() == item) {
                        this.retrySlotClick(slotId, clickedButton, true, playerIn);
                     }
                  }
               }
            } else {
               if (slotId < 0) {
                  return null;
               }

               slot7 = (Slot)this.inventorySlots.get(slotId);
               if (slot7 != null) {
                  itemstack9 = slot7.getStack();
                  ItemStack itemstack10 = inventoryplayer.getItemStack();
                  if (itemstack9 != null) {
                     itemstack = itemstack9.copy();
                  }

                  if (itemstack9 == null) {
                     if (itemstack10 != null && slot7.isItemValid(itemstack10)) {
                        i2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                        if (i2 > slot7.getItemStackLimit(itemstack10)) {
                           i2 = slot7.getItemStackLimit(itemstack10);
                        }

                        if (itemstack10.stackSize >= i2) {
                           slot7.putStack(itemstack10.splitStack(i2));
                        }

                        if (itemstack10.stackSize == 0) {
                           inventoryplayer.setItemStack((ItemStack)null);
                        }
                     }
                  } else if (slot7.canTakeStack(playerIn)) {
                     if (itemstack10 == null) {
                        i2 = clickedButton == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
                        itemstack12 = slot7.decrStackSize(i2);
                        inventoryplayer.setItemStack(itemstack12);
                        if (itemstack9.stackSize == 0) {
                           slot7.putStack((ItemStack)null);
                        }

                        slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
                     } else if (slot7.isItemValid(itemstack10)) {
                        if (itemstack9.getItem() == itemstack10.getItem() && itemstack9.getMetadata() == itemstack10.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)) {
                           i2 = clickedButton == 0 ? itemstack10.stackSize : 1;
                           if (i2 > slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize) {
                              i2 = slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize;
                           }

                           if (i2 > itemstack10.getMaxStackSize() - itemstack9.stackSize) {
                              i2 = itemstack10.getMaxStackSize() - itemstack9.stackSize;
                           }

                           itemstack10.splitStack(i2);
                           if (itemstack10.stackSize == 0) {
                              inventoryplayer.setItemStack((ItemStack)null);
                           }

                           itemstack9.stackSize += i2;
                        } else if (itemstack10.stackSize <= slot7.getItemStackLimit(itemstack10)) {
                           slot7.putStack(itemstack10);
                           inventoryplayer.setItemStack(itemstack9);
                        }
                     } else if (itemstack9.getItem() == itemstack10.getItem() && itemstack10.getMaxStackSize() > 1 && (!itemstack9.getHasSubtypes() || itemstack9.getMetadata() == itemstack10.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)) {
                        i2 = itemstack9.stackSize;
                        if (i2 > 0 && i2 + itemstack10.stackSize <= itemstack10.getMaxStackSize()) {
                           itemstack10.stackSize += i2;
                           itemstack9 = slot7.decrStackSize(i2);
                           if (itemstack9.stackSize == 0) {
                              slot7.putStack((ItemStack)null);
                           }

                           slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
                        }
                     }
                  }

                  slot7.onSlotChanged();
               }
            }
         } else if (mode == 2 && clickedButton >= 0 && clickedButton < 9) {
            slot7 = (Slot)this.inventorySlots.get(slotId);
            if (slot7.canTakeStack(playerIn)) {
               itemstack9 = inventoryplayer.getStackInSlot(clickedButton);
               boolean flag = itemstack9 == null || slot7.inventory == inventoryplayer && slot7.isItemValid(itemstack9);
               i2 = -1;
               if (!flag) {
                  i2 = inventoryplayer.getFirstEmptyStack();
                  flag |= i2 > -1;
               }

               if (slot7.getHasStack() && flag) {
                  itemstack12 = slot7.getStack();
                  inventoryplayer.setInventorySlotContents(clickedButton, itemstack12.copy());
                  if ((slot7.inventory != inventoryplayer || !slot7.isItemValid(itemstack9)) && itemstack9 != null) {
                     if (i2 > -1) {
                        inventoryplayer.addItemStackToInventory(itemstack9);
                        slot7.decrStackSize(itemstack12.stackSize);
                        slot7.putStack((ItemStack)null);
                        slot7.onPickupFromSlot(playerIn, itemstack12);
                     }
                  } else {
                     slot7.decrStackSize(itemstack12.stackSize);
                     slot7.putStack(itemstack9);
                     slot7.onPickupFromSlot(playerIn, itemstack12);
                  }
               } else if (!slot7.getHasStack() && itemstack9 != null && slot7.isItemValid(itemstack9)) {
                  inventoryplayer.setInventorySlotContents(clickedButton, (ItemStack)null);
                  slot7.putStack(itemstack9);
               }
            }
         } else if (mode == 3 && playerIn.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0) {
            slot7 = (Slot)this.inventorySlots.get(slotId);
            if (slot7 != null && slot7.getHasStack()) {
               itemstack9 = slot7.getStack().copy();
               itemstack9.stackSize = itemstack9.getMaxStackSize();
               inventoryplayer.setItemStack(itemstack9);
            }
         } else if (mode == 4 && inventoryplayer.getItemStack() == null && slotId >= 0) {
            slot7 = (Slot)this.inventorySlots.get(slotId);
            if (slot7 != null && slot7.getHasStack() && slot7.canTakeStack(playerIn)) {
               itemstack9 = slot7.decrStackSize(clickedButton == 0 ? 1 : slot7.getStack().stackSize);
               slot7.onPickupFromSlot(playerIn, itemstack9);
               playerIn.dropPlayerItemWithRandomChoice(itemstack9, true);
            }
         } else if (mode == 6 && slotId >= 0) {
            slot7 = (Slot)this.inventorySlots.get(slotId);
            itemstack9 = inventoryplayer.getItemStack();
            if (itemstack9 != null && (slot7 == null || !slot7.getHasStack() || !slot7.canTakeStack(playerIn))) {
               j = clickedButton == 0 ? 0 : this.inventorySlots.size() - 1;
               i2 = clickedButton == 0 ? 1 : -1;

               for(int l2 = 0; l2 < 2; ++l2) {
                  for(int i3 = j; i3 >= 0 && i3 < this.inventorySlots.size() && itemstack9.stackSize < itemstack9.getMaxStackSize(); i3 += i2) {
                     Slot slot8 = (Slot)this.inventorySlots.get(i3);
                     if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack9, true) && slot8.canTakeStack(playerIn) && this.canMergeSlot(itemstack9, slot8) && (l2 != 0 || slot8.getStack().stackSize != slot8.getStack().getMaxStackSize())) {
                        int l = Math.min(itemstack9.getMaxStackSize() - itemstack9.stackSize, slot8.getStack().stackSize);
                        ItemStack itemstack2 = slot8.decrStackSize(l);
                        itemstack9.stackSize += l;
                        if (itemstack2.stackSize <= 0) {
                           slot8.putStack((ItemStack)null);
                        }

                        slot8.onPickupFromSlot(playerIn, itemstack2);
                     }
                  }
               }
            }

            this.detectAndSendChanges();
         }
      }

      return itemstack;
   }

   public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_) {
      return true;
   }

   protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {
      this.slotClick(slotId, clickedButton, 1, playerIn);
   }

   public void onContainerClosed(EntityPlayer playerIn) {
      InventoryPlayer inventoryplayer = playerIn.inventory;
      if (inventoryplayer.getItemStack() != null) {
         playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
         inventoryplayer.setItemStack((ItemStack)null);
      }

   }

   public void onCraftMatrixChanged(IInventory inventoryIn) {
      this.detectAndSendChanges();
   }

   public void putStackInSlot(int slotID, ItemStack stack) {
      this.getSlot(slotID).putStack(stack);
   }

   public void putStacksInSlots(ItemStack[] p_75131_1_) {
      for(int i = 0; i < p_75131_1_.length; ++i) {
         this.getSlot(i).putStack(p_75131_1_[i]);
      }

   }

   public void updateProgressBar(int id, int data) {
   }

   public short getNextTransactionID(InventoryPlayer p_75136_1_) {
      ++this.transactionID;
      return this.transactionID;
   }

   public boolean getCanCraft(EntityPlayer p_75129_1_) {
      return !this.playerList.contains(p_75129_1_);
   }

   public void setCanCraft(EntityPlayer p_75128_1_, boolean p_75128_2_) {
      if (p_75128_2_) {
         this.playerList.remove(p_75128_1_);
      } else {
         this.playerList.add(p_75128_1_);
      }

   }

   public abstract boolean canInteractWith(EntityPlayer var1);

   protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
      boolean flag = false;
      int i = startIndex;
      if (reverseDirection) {
         i = endIndex - 1;
      }

      Slot slot1;
      ItemStack itemstack1;
      if (stack.isStackable()) {
         while(stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
            slot1 = (Slot)this.inventorySlots.get(i);
            itemstack1 = slot1.getStack();
            if (itemstack1 != null && itemstack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack1.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack1)) {
               int j = itemstack1.stackSize + stack.stackSize;
               if (j <= stack.getMaxStackSize()) {
                  stack.stackSize = 0;
                  itemstack1.stackSize = j;
                  slot1.onSlotChanged();
                  flag = true;
               } else if (itemstack1.stackSize < stack.getMaxStackSize()) {
                  stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
                  itemstack1.stackSize = stack.getMaxStackSize();
                  slot1.onSlotChanged();
                  flag = true;
               }
            }

            if (reverseDirection) {
               --i;
            } else {
               ++i;
            }
         }
      }

      if (stack.stackSize > 0) {
         if (reverseDirection) {
            i = endIndex - 1;
         } else {
            i = startIndex;
         }

         while(!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
            slot1 = (Slot)this.inventorySlots.get(i);
            itemstack1 = slot1.getStack();
            if (itemstack1 == null) {
               slot1.putStack(stack.copy());
               slot1.onSlotChanged();
               stack.stackSize = 0;
               flag = true;
               break;
            }

            if (reverseDirection) {
               --i;
            } else {
               ++i;
            }
         }
      }

      return flag;
   }

   public static int extractDragMode(int p_94529_0_) {
      return p_94529_0_ >> 2 & 3;
   }

   public static int getDragEvent(int p_94532_0_) {
      return p_94532_0_ & 3;
   }

   public static int func_94534_d(int p_94534_0_, int p_94534_1_) {
      return p_94534_0_ & 3 | (p_94534_1_ & 3) << 2;
   }

   public static boolean isValidDragMode(int dragModeIn, EntityPlayer player) {
      return dragModeIn == 0 ? true : (dragModeIn == 1 ? true : dragModeIn == 2 && player.capabilities.isCreativeMode);
   }

   protected void resetDrag() {
      this.dragEvent = 0;
      this.dragSlots.clear();
   }

   public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
      boolean flag = slotIn == null || !slotIn.getHasStack();
      if (slotIn != null && slotIn.getHasStack() && stack != null && stack.isItemEqual(slotIn.getStack()) && ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)) {
         flag |= slotIn.getStack().stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= stack.getMaxStackSize();
      }

      return flag;
   }

   public static void computeStackSize(Set p_94525_0_, int p_94525_1_, ItemStack p_94525_2_, int p_94525_3_) {
      switch(p_94525_1_) {
      case 0:
         p_94525_2_.stackSize = MathHelper.floor_float((float)p_94525_2_.stackSize / (float)p_94525_0_.size());
         break;
      case 1:
         p_94525_2_.stackSize = 1;
         break;
      case 2:
         p_94525_2_.stackSize = p_94525_2_.getItem().getItemStackLimit();
      }

      p_94525_2_.stackSize += p_94525_3_;
   }

   public boolean canDragIntoSlot(Slot p_94531_1_) {
      return true;
   }

   public static int calcRedstone(TileEntity te) {
      return te instanceof IInventory ? calcRedstoneFromInventory((IInventory)te) : 0;
   }

   public static int calcRedstoneFromInventory(IInventory inv) {
      if (inv == null) {
         return 0;
      } else {
         int i = 0;
         float f = 0.0F;

         for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (itemstack != null) {
               f += (float)itemstack.stackSize / (float)Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
               ++i;
            }
         }

         f /= (float)inv.getSizeInventory();
         return MathHelper.floor_float(f * 14.0F) + (i > 0 ? 1 : 0);
      }
   }
}
