/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public abstract class Container
/*     */ {
/*  16 */   public List<ItemStack> inventoryItemStacks = Lists.newArrayList();
/*  17 */   public List<Slot> inventorySlots = Lists.newArrayList();
/*     */   
/*     */ 
/*     */   public int windowId;
/*     */   
/*     */   private short transactionID;
/*     */   
/*  24 */   private int dragMode = -1;
/*     */   
/*     */   private int dragEvent;
/*     */   
/*  28 */   private final Set<Slot> dragSlots = Sets.newHashSet();
/*  29 */   protected List<ICrafting> crafters = Lists.newArrayList();
/*  30 */   private Set<EntityPlayer> playerList = Sets.newHashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Slot addSlotToContainer(Slot slotIn)
/*     */   {
/*  37 */     slotIn.slotNumber = this.inventorySlots.size();
/*  38 */     this.inventorySlots.add(slotIn);
/*  39 */     this.inventoryItemStacks.add(null);
/*  40 */     return slotIn;
/*     */   }
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener)
/*     */   {
/*  45 */     if (this.crafters.contains(listener))
/*     */     {
/*  47 */       throw new IllegalArgumentException("Listener already listening");
/*     */     }
/*     */     
/*     */ 
/*  51 */     this.crafters.add(listener);
/*  52 */     listener.updateCraftingInventory(this, getInventory());
/*  53 */     detectAndSendChanges();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeCraftingFromCrafters(ICrafting listeners)
/*     */   {
/*  62 */     this.crafters.remove(listeners);
/*     */   }
/*     */   
/*     */   public List<ItemStack> getInventory()
/*     */   {
/*  67 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  69 */     for (int i = 0; i < this.inventorySlots.size(); i++)
/*     */     {
/*  71 */       list.add(((Slot)this.inventorySlots.get(i)).getStack());
/*     */     }
/*     */     
/*  74 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void detectAndSendChanges()
/*     */   {
/*  82 */     for (int i = 0; i < this.inventorySlots.size(); i++)
/*     */     {
/*  84 */       ItemStack itemstack = ((Slot)this.inventorySlots.get(i)).getStack();
/*  85 */       ItemStack itemstack1 = (ItemStack)this.inventoryItemStacks.get(i);
/*     */       
/*  87 */       if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
/*     */       {
/*  89 */         itemstack1 = itemstack == null ? null : itemstack.copy();
/*  90 */         this.inventoryItemStacks.set(i, itemstack1);
/*     */         
/*  92 */         for (int j = 0; j < this.crafters.size(); j++)
/*     */         {
/*  94 */           ((ICrafting)this.crafters.get(j)).sendSlotContents(this, i, itemstack1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean enchantItem(EntityPlayer playerIn, int id)
/*     */   {
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public Slot getSlotFromInventory(IInventory inv, int slotIn)
/*     */   {
/* 110 */     for (int i = 0; i < this.inventorySlots.size(); i++)
/*     */     {
/* 112 */       Slot slot = (Slot)this.inventorySlots.get(i);
/*     */       
/* 114 */       if (slot.isHere(inv, slotIn))
/*     */       {
/* 116 */         return slot;
/*     */       }
/*     */     }
/*     */     
/* 120 */     return null;
/*     */   }
/*     */   
/*     */   public Slot getSlot(int slotId)
/*     */   {
/* 125 */     return (Slot)this.inventorySlots.get(slotId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/* 133 */     Slot slot = (Slot)this.inventorySlots.get(index);
/* 134 */     return slot != null ? slot.getStack() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn)
/*     */   {
/* 142 */     ItemStack itemstack = null;
/* 143 */     InventoryPlayer inventoryplayer = playerIn.inventory;
/*     */     
/* 145 */     if (mode == 5)
/*     */     {
/* 147 */       int i = this.dragEvent;
/* 148 */       this.dragEvent = getDragEvent(clickedButton);
/*     */       
/* 150 */       if (((i != 1) || (this.dragEvent != 2)) && (i != this.dragEvent))
/*     */       {
/* 152 */         resetDrag();
/*     */       }
/* 154 */       else if (inventoryplayer.getItemStack() == null)
/*     */       {
/* 156 */         resetDrag();
/*     */       }
/* 158 */       else if (this.dragEvent == 0)
/*     */       {
/* 160 */         this.dragMode = extractDragMode(clickedButton);
/*     */         
/* 162 */         if (isValidDragMode(this.dragMode, playerIn))
/*     */         {
/* 164 */           this.dragEvent = 1;
/* 165 */           this.dragSlots.clear();
/*     */         }
/*     */         else
/*     */         {
/* 169 */           resetDrag();
/*     */         }
/*     */       }
/* 172 */       else if (this.dragEvent == 1)
/*     */       {
/* 174 */         Slot slot = (Slot)this.inventorySlots.get(slotId);
/*     */         
/* 176 */         if ((slot != null) && (canAddItemToSlot(slot, inventoryplayer.getItemStack(), true)) && (slot.isItemValid(inventoryplayer.getItemStack())) && (inventoryplayer.getItemStack().stackSize > this.dragSlots.size()) && (canDragIntoSlot(slot)))
/*     */         {
/* 178 */           this.dragSlots.add(slot);
/*     */         }
/*     */       }
/* 181 */       else if (this.dragEvent == 2)
/*     */       {
/* 183 */         if (!this.dragSlots.isEmpty())
/*     */         {
/* 185 */           ItemStack itemstack3 = inventoryplayer.getItemStack().copy();
/* 186 */           int j = inventoryplayer.getItemStack().stackSize;
/*     */           
/* 188 */           for (Slot slot1 : this.dragSlots)
/*     */           {
/* 190 */             if ((slot1 != null) && (canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true)) && (slot1.isItemValid(inventoryplayer.getItemStack())) && (inventoryplayer.getItemStack().stackSize >= this.dragSlots.size()) && (canDragIntoSlot(slot1)))
/*     */             {
/* 192 */               ItemStack itemstack1 = itemstack3.copy();
/* 193 */               int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
/* 194 */               computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);
/*     */               
/* 196 */               if (itemstack1.stackSize > itemstack1.getMaxStackSize())
/*     */               {
/* 198 */                 itemstack1.stackSize = itemstack1.getMaxStackSize();
/*     */               }
/*     */               
/* 201 */               if (itemstack1.stackSize > slot1.getItemStackLimit(itemstack1))
/*     */               {
/* 203 */                 itemstack1.stackSize = slot1.getItemStackLimit(itemstack1);
/*     */               }
/*     */               
/* 206 */               j -= itemstack1.stackSize - k;
/* 207 */               slot1.putStack(itemstack1);
/*     */             }
/*     */           }
/*     */           
/* 211 */           itemstack3.stackSize = j;
/*     */           
/* 213 */           if (itemstack3.stackSize <= 0)
/*     */           {
/* 215 */             itemstack3 = null;
/*     */           }
/*     */           
/* 218 */           inventoryplayer.setItemStack(itemstack3);
/*     */         }
/*     */         
/* 221 */         resetDrag();
/*     */       }
/*     */       else
/*     */       {
/* 225 */         resetDrag();
/*     */       }
/*     */     }
/* 228 */     else if (this.dragEvent != 0)
/*     */     {
/* 230 */       resetDrag();
/*     */     }
/* 232 */     else if (((mode == 0) || (mode == 1)) && ((clickedButton == 0) || (clickedButton == 1)))
/*     */     {
/* 234 */       if (slotId == 64537)
/*     */       {
/* 236 */         if (inventoryplayer.getItemStack() != null)
/*     */         {
/* 238 */           if (clickedButton == 0)
/*     */           {
/* 240 */             playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
/* 241 */             inventoryplayer.setItemStack(null);
/*     */           }
/*     */           
/* 244 */           if (clickedButton == 1)
/*     */           {
/* 246 */             playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);
/*     */             
/* 248 */             if (inventoryplayer.getItemStack().stackSize == 0)
/*     */             {
/* 250 */               inventoryplayer.setItemStack(null);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 255 */       else if (mode == 1)
/*     */       {
/* 257 */         if (slotId < 0)
/*     */         {
/* 259 */           return null;
/*     */         }
/*     */         
/* 262 */         Slot slot6 = (Slot)this.inventorySlots.get(slotId);
/*     */         
/* 264 */         if ((slot6 != null) && (slot6.canTakeStack(playerIn)))
/*     */         {
/* 266 */           ItemStack itemstack8 = transferStackInSlot(playerIn, slotId);
/*     */           
/* 268 */           if (itemstack8 != null)
/*     */           {
/* 270 */             Item item = itemstack8.getItem();
/* 271 */             itemstack = itemstack8.copy();
/*     */             
/* 273 */             if ((slot6.getStack() != null) && (slot6.getStack().getItem() == item))
/*     */             {
/* 275 */               retrySlotClick(slotId, clickedButton, true, playerIn);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 282 */         if (slotId < 0)
/*     */         {
/* 284 */           return null;
/*     */         }
/*     */         
/* 287 */         Slot slot7 = (Slot)this.inventorySlots.get(slotId);
/*     */         
/* 289 */         if (slot7 != null)
/*     */         {
/* 291 */           ItemStack itemstack9 = slot7.getStack();
/* 292 */           ItemStack itemstack10 = inventoryplayer.getItemStack();
/*     */           
/* 294 */           if (itemstack9 != null)
/*     */           {
/* 296 */             itemstack = itemstack9.copy();
/*     */           }
/*     */           
/* 299 */           if (itemstack9 == null)
/*     */           {
/* 301 */             if ((itemstack10 != null) && (slot7.isItemValid(itemstack10)))
/*     */             {
/* 303 */               int k2 = clickedButton == 0 ? itemstack10.stackSize : 1;
/*     */               
/* 305 */               if (k2 > slot7.getItemStackLimit(itemstack10))
/*     */               {
/* 307 */                 k2 = slot7.getItemStackLimit(itemstack10);
/*     */               }
/*     */               
/* 310 */               if (itemstack10.stackSize >= k2)
/*     */               {
/* 312 */                 slot7.putStack(itemstack10.splitStack(k2));
/*     */               }
/*     */               
/* 315 */               if (itemstack10.stackSize == 0)
/*     */               {
/* 317 */                 inventoryplayer.setItemStack(null);
/*     */               }
/*     */             }
/*     */           }
/* 321 */           else if (slot7.canTakeStack(playerIn))
/*     */           {
/* 323 */             if (itemstack10 == null)
/*     */             {
/* 325 */               int j2 = clickedButton == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
/* 326 */               ItemStack itemstack12 = slot7.decrStackSize(j2);
/* 327 */               inventoryplayer.setItemStack(itemstack12);
/*     */               
/* 329 */               if (itemstack9.stackSize == 0)
/*     */               {
/* 331 */                 slot7.putStack(null);
/*     */               }
/*     */               
/* 334 */               slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
/*     */             }
/* 336 */             else if (slot7.isItemValid(itemstack10))
/*     */             {
/* 338 */               if ((itemstack9.getItem() == itemstack10.getItem()) && (itemstack9.getMetadata() == itemstack10.getMetadata()) && (ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)))
/*     */               {
/* 340 */                 int i2 = clickedButton == 0 ? itemstack10.stackSize : 1;
/*     */                 
/* 342 */                 if (i2 > slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize)
/*     */                 {
/* 344 */                   i2 = slot7.getItemStackLimit(itemstack10) - itemstack9.stackSize;
/*     */                 }
/*     */                 
/* 347 */                 if (i2 > itemstack10.getMaxStackSize() - itemstack9.stackSize)
/*     */                 {
/* 349 */                   i2 = itemstack10.getMaxStackSize() - itemstack9.stackSize;
/*     */                 }
/*     */                 
/* 352 */                 itemstack10.splitStack(i2);
/*     */                 
/* 354 */                 if (itemstack10.stackSize == 0)
/*     */                 {
/* 356 */                   inventoryplayer.setItemStack(null);
/*     */                 }
/*     */                 
/* 359 */                 itemstack9.stackSize += i2;
/*     */               }
/* 361 */               else if (itemstack10.stackSize <= slot7.getItemStackLimit(itemstack10))
/*     */               {
/* 363 */                 slot7.putStack(itemstack10);
/* 364 */                 inventoryplayer.setItemStack(itemstack9);
/*     */               }
/*     */             }
/* 367 */             else if ((itemstack9.getItem() == itemstack10.getItem()) && (itemstack10.getMaxStackSize() > 1) && ((!itemstack9.getHasSubtypes()) || (itemstack9.getMetadata() == itemstack10.getMetadata())) && (ItemStack.areItemStackTagsEqual(itemstack9, itemstack10)))
/*     */             {
/* 369 */               int l1 = itemstack9.stackSize;
/*     */               
/* 371 */               if ((l1 > 0) && (l1 + itemstack10.stackSize <= itemstack10.getMaxStackSize()))
/*     */               {
/* 373 */                 itemstack10.stackSize += l1;
/* 374 */                 itemstack9 = slot7.decrStackSize(l1);
/*     */                 
/* 376 */                 if (itemstack9.stackSize == 0)
/*     */                 {
/* 378 */                   slot7.putStack(null);
/*     */                 }
/*     */                 
/* 381 */                 slot7.onPickupFromSlot(playerIn, inventoryplayer.getItemStack());
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 386 */           slot7.onSlotChanged();
/*     */         }
/*     */       }
/*     */     }
/* 390 */     else if ((mode == 2) && (clickedButton >= 0) && (clickedButton < 9))
/*     */     {
/* 392 */       Slot slot5 = (Slot)this.inventorySlots.get(slotId);
/*     */       
/* 394 */       if (slot5.canTakeStack(playerIn))
/*     */       {
/* 396 */         ItemStack itemstack7 = inventoryplayer.getStackInSlot(clickedButton);
/* 397 */         boolean flag = (itemstack7 == null) || ((slot5.inventory == inventoryplayer) && (slot5.isItemValid(itemstack7)));
/* 398 */         int k1 = -1;
/*     */         
/* 400 */         if (!flag)
/*     */         {
/* 402 */           k1 = inventoryplayer.getFirstEmptyStack();
/* 403 */           flag |= k1 > -1;
/*     */         }
/*     */         
/* 406 */         if ((slot5.getHasStack()) && (flag))
/*     */         {
/* 408 */           ItemStack itemstack11 = slot5.getStack();
/* 409 */           inventoryplayer.setInventorySlotContents(clickedButton, itemstack11.copy());
/*     */           
/* 411 */           if (((slot5.inventory != inventoryplayer) || (!slot5.isItemValid(itemstack7))) && (itemstack7 != null))
/*     */           {
/* 413 */             if (k1 > -1)
/*     */             {
/* 415 */               inventoryplayer.addItemStackToInventory(itemstack7);
/* 416 */               slot5.decrStackSize(itemstack11.stackSize);
/* 417 */               slot5.putStack(null);
/* 418 */               slot5.onPickupFromSlot(playerIn, itemstack11);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 423 */             slot5.decrStackSize(itemstack11.stackSize);
/* 424 */             slot5.putStack(itemstack7);
/* 425 */             slot5.onPickupFromSlot(playerIn, itemstack11);
/*     */           }
/*     */         }
/* 428 */         else if ((!slot5.getHasStack()) && (itemstack7 != null) && (slot5.isItemValid(itemstack7)))
/*     */         {
/* 430 */           inventoryplayer.setInventorySlotContents(clickedButton, null);
/* 431 */           slot5.putStack(itemstack7);
/*     */         }
/*     */       }
/*     */     }
/* 435 */     else if ((mode == 3) && (playerIn.capabilities.isCreativeMode) && (inventoryplayer.getItemStack() == null) && (slotId >= 0))
/*     */     {
/* 437 */       Slot slot4 = (Slot)this.inventorySlots.get(slotId);
/*     */       
/* 439 */       if ((slot4 != null) && (slot4.getHasStack()))
/*     */       {
/* 441 */         ItemStack itemstack6 = slot4.getStack().copy();
/* 442 */         itemstack6.stackSize = itemstack6.getMaxStackSize();
/* 443 */         inventoryplayer.setItemStack(itemstack6);
/*     */       }
/*     */     }
/* 446 */     else if ((mode == 4) && (inventoryplayer.getItemStack() == null) && (slotId >= 0))
/*     */     {
/* 448 */       Slot slot3 = (Slot)this.inventorySlots.get(slotId);
/*     */       
/* 450 */       if ((slot3 != null) && (slot3.getHasStack()) && (slot3.canTakeStack(playerIn)))
/*     */       {
/* 452 */         ItemStack itemstack5 = slot3.decrStackSize(clickedButton == 0 ? 1 : slot3.getStack().stackSize);
/* 453 */         slot3.onPickupFromSlot(playerIn, itemstack5);
/* 454 */         playerIn.dropPlayerItemWithRandomChoice(itemstack5, true);
/*     */       }
/*     */     }
/* 457 */     else if ((mode == 6) && (slotId >= 0))
/*     */     {
/* 459 */       Slot slot2 = (Slot)this.inventorySlots.get(slotId);
/* 460 */       ItemStack itemstack4 = inventoryplayer.getItemStack();
/*     */       
/* 462 */       if ((itemstack4 != null) && ((slot2 == null) || (!slot2.getHasStack()) || (!slot2.canTakeStack(playerIn))))
/*     */       {
/* 464 */         int i1 = clickedButton == 0 ? 0 : this.inventorySlots.size() - 1;
/* 465 */         int j1 = clickedButton == 0 ? 1 : -1;
/*     */         
/* 467 */         for (int l2 = 0; l2 < 2; l2++)
/*     */         {
/* 469 */           for (int i3 = i1; (i3 >= 0) && (i3 < this.inventorySlots.size()) && (itemstack4.stackSize < itemstack4.getMaxStackSize()); i3 += j1)
/*     */           {
/* 471 */             Slot slot8 = (Slot)this.inventorySlots.get(i3);
/*     */             
/* 473 */             if ((slot8.getHasStack()) && (canAddItemToSlot(slot8, itemstack4, true)) && (slot8.canTakeStack(playerIn)) && (canMergeSlot(itemstack4, slot8)) && ((l2 != 0) || (slot8.getStack().stackSize != slot8.getStack().getMaxStackSize())))
/*     */             {
/* 475 */               int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, slot8.getStack().stackSize);
/* 476 */               ItemStack itemstack2 = slot8.decrStackSize(l);
/* 477 */               itemstack4.stackSize += l;
/*     */               
/* 479 */               if (itemstack2.stackSize <= 0)
/*     */               {
/* 481 */                 slot8.putStack(null);
/*     */               }
/*     */               
/* 484 */               slot8.onPickupFromSlot(playerIn, itemstack2);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 490 */       detectAndSendChanges();
/*     */     }
/*     */     
/* 493 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_)
/*     */   {
/* 502 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn)
/*     */   {
/* 510 */     slotClick(slotId, clickedButton, 1, playerIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/* 518 */     InventoryPlayer inventoryplayer = playerIn.inventory;
/*     */     
/* 520 */     if (inventoryplayer.getItemStack() != null)
/*     */     {
/* 522 */       playerIn.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
/* 523 */       inventoryplayer.setItemStack(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn)
/*     */   {
/* 532 */     detectAndSendChanges();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putStackInSlot(int slotID, ItemStack stack)
/*     */   {
/* 540 */     getSlot(slotID).putStack(stack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void putStacksInSlots(ItemStack[] p_75131_1_)
/*     */   {
/* 548 */     for (int i = 0; i < p_75131_1_.length; i++)
/*     */     {
/* 550 */       getSlot(i).putStack(p_75131_1_[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateProgressBar(int id, int data) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public short getNextTransactionID(InventoryPlayer p_75136_1_)
/*     */   {
/* 563 */     this.transactionID = ((short)(this.transactionID + 1));
/* 564 */     return this.transactionID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanCraft(EntityPlayer p_75129_1_)
/*     */   {
/* 572 */     return !this.playerList.contains(p_75129_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCanCraft(EntityPlayer p_75128_1_, boolean p_75128_2_)
/*     */   {
/* 580 */     if (p_75128_2_)
/*     */     {
/* 582 */       this.playerList.remove(p_75128_1_);
/*     */     }
/*     */     else
/*     */     {
/* 586 */       this.playerList.add(p_75128_1_);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean canInteractWith(EntityPlayer paramEntityPlayer);
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
/*     */   {
/* 599 */     boolean flag = false;
/* 600 */     int i = startIndex;
/*     */     
/* 602 */     if (reverseDirection)
/*     */     {
/* 604 */       i = endIndex - 1;
/*     */     }
/*     */     
/* 607 */     if (stack.isStackable())
/*     */     {
/* 609 */       while ((stack.stackSize > 0) && (((!reverseDirection) && (i < endIndex)) || ((reverseDirection) && (i >= startIndex))))
/*     */       {
/* 611 */         Slot slot = (Slot)this.inventorySlots.get(i);
/* 612 */         ItemStack itemstack = slot.getStack();
/*     */         
/* 614 */         if ((itemstack != null) && (itemstack.getItem() == stack.getItem()) && ((!stack.getHasSubtypes()) || (stack.getMetadata() == itemstack.getMetadata())) && (ItemStack.areItemStackTagsEqual(stack, itemstack)))
/*     */         {
/* 616 */           int j = itemstack.stackSize + stack.stackSize;
/*     */           
/* 618 */           if (j <= stack.getMaxStackSize())
/*     */           {
/* 620 */             stack.stackSize = 0;
/* 621 */             itemstack.stackSize = j;
/* 622 */             slot.onSlotChanged();
/* 623 */             flag = true;
/*     */           }
/* 625 */           else if (itemstack.stackSize < stack.getMaxStackSize())
/*     */           {
/* 627 */             stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
/* 628 */             itemstack.stackSize = stack.getMaxStackSize();
/* 629 */             slot.onSlotChanged();
/* 630 */             flag = true;
/*     */           }
/*     */         }
/*     */         
/* 634 */         if (reverseDirection)
/*     */         {
/* 636 */           i--;
/*     */         }
/*     */         else
/*     */         {
/* 640 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 645 */     if (stack.stackSize > 0)
/*     */     {
/* 647 */       if (reverseDirection)
/*     */       {
/* 649 */         i = endIndex - 1;
/*     */       }
/*     */       else
/*     */       {
/* 653 */         i = startIndex;
/*     */       }
/*     */       
/* 656 */       while (((!reverseDirection) && (i < endIndex)) || ((reverseDirection) && (i >= startIndex)))
/*     */       {
/* 658 */         Slot slot1 = (Slot)this.inventorySlots.get(i);
/* 659 */         ItemStack itemstack1 = slot1.getStack();
/*     */         
/* 661 */         if (itemstack1 == null)
/*     */         {
/* 663 */           slot1.putStack(stack.copy());
/* 664 */           slot1.onSlotChanged();
/* 665 */           stack.stackSize = 0;
/* 666 */           flag = true;
/* 667 */           break;
/*     */         }
/*     */         
/* 670 */         if (reverseDirection)
/*     */         {
/* 672 */           i--;
/*     */         }
/*     */         else
/*     */         {
/* 676 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 681 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int extractDragMode(int p_94529_0_)
/*     */   {
/* 689 */     return p_94529_0_ >> 2 & 0x3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getDragEvent(int p_94532_0_)
/*     */   {
/* 697 */     return p_94532_0_ & 0x3;
/*     */   }
/*     */   
/*     */   public static int func_94534_d(int p_94534_0_, int p_94534_1_)
/*     */   {
/* 702 */     return p_94534_0_ & 0x3 | (p_94534_1_ & 0x3) << 2;
/*     */   }
/*     */   
/*     */   public static boolean isValidDragMode(int dragModeIn, EntityPlayer player)
/*     */   {
/* 707 */     return dragModeIn == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void resetDrag()
/*     */   {
/* 715 */     this.dragEvent = 0;
/* 716 */     this.dragSlots.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters)
/*     */   {
/* 724 */     boolean flag = (slotIn == null) || (!slotIn.getHasStack());
/*     */     
/* 726 */     if ((slotIn != null) && (slotIn.getHasStack()) && (stack != null) && (stack.isItemEqual(slotIn.getStack())) && (ItemStack.areItemStackTagsEqual(slotIn.getStack(), stack)))
/*     */     {
/* 728 */       flag |= slotIn.getStack().stackSize + (stackSizeMatters ? 0 : stack.stackSize) <= stack.getMaxStackSize();
/*     */     }
/*     */     
/* 731 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void computeStackSize(Set<Slot> p_94525_0_, int p_94525_1_, ItemStack p_94525_2_, int p_94525_3_)
/*     */   {
/* 740 */     switch (p_94525_1_)
/*     */     {
/*     */     case 0: 
/* 743 */       p_94525_2_.stackSize = MathHelper.floor_float(p_94525_2_.stackSize / p_94525_0_.size());
/* 744 */       break;
/*     */     
/*     */     case 1: 
/* 747 */       p_94525_2_.stackSize = 1;
/* 748 */       break;
/*     */     
/*     */     case 2: 
/* 751 */       p_94525_2_.stackSize = p_94525_2_.getItem().getItemStackLimit(); }
/* 752 */     ItemStack tmp71_70 = p_94525_2_;
/*     */     
/* 754 */     tmp71_70.stackSize = (tmp71_70.stackSize + p_94525_3_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canDragIntoSlot(Slot p_94531_1_)
/*     */   {
/* 763 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int calcRedstone(TileEntity te)
/*     */   {
/* 771 */     return (te instanceof IInventory) ? calcRedstoneFromInventory((IInventory)te) : 0;
/*     */   }
/*     */   
/*     */   public static int calcRedstoneFromInventory(IInventory inv)
/*     */   {
/* 776 */     if (inv == null)
/*     */     {
/* 778 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 782 */     int i = 0;
/* 783 */     float f = 0.0F;
/*     */     
/* 785 */     for (int j = 0; j < inv.getSizeInventory(); j++)
/*     */     {
/* 787 */       ItemStack itemstack = inv.getStackInSlot(j);
/*     */       
/* 789 */       if (itemstack != null)
/*     */       {
/* 791 */         f += itemstack.stackSize / Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
/* 792 */         i++;
/*     */       }
/*     */     }
/*     */     
/* 796 */     f /= inv.getSizeInventory();
/* 797 */     return MathHelper.floor_float(f * 14.0F) + (i > 0 ? 1 : 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\Container.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */