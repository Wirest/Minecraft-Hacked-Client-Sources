/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentData;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemEnchantedBook;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ContainerEnchantment
/*     */   extends Container
/*     */ {
/*     */   public IInventory tableInventory;
/*     */   private World worldPointer;
/*     */   private BlockPos position;
/*     */   private Random rand;
/*     */   public int xpSeed;
/*     */   public int[] enchantLevels;
/*     */   public int[] field_178151_h;
/*     */   
/*     */   public ContainerEnchantment(InventoryPlayer playerInv, World worldIn)
/*     */   {
/*  34 */     this(playerInv, worldIn, BlockPos.ORIGIN);
/*     */   }
/*     */   
/*     */   public ContainerEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos)
/*     */   {
/*  39 */     this.tableInventory = new InventoryBasic("Enchant", true, 2)
/*     */     {
/*     */       public int getInventoryStackLimit()
/*     */       {
/*  43 */         return 64;
/*     */       }
/*     */       
/*     */       public void markDirty() {
/*  47 */         super.markDirty();
/*  48 */         ContainerEnchantment.this.onCraftMatrixChanged(this);
/*     */       }
/*  50 */     };
/*  51 */     this.rand = new Random();
/*  52 */     this.enchantLevels = new int[3];
/*  53 */     this.field_178151_h = new int[] { -1, -1, -1 };
/*  54 */     this.worldPointer = worldIn;
/*  55 */     this.position = pos;
/*  56 */     this.xpSeed = playerInv.player.getXPSeed();
/*  57 */     addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47)
/*     */     {
/*     */       public boolean isItemValid(ItemStack stack)
/*     */       {
/*  61 */         return true;
/*     */       }
/*     */       
/*     */       public int getSlotStackLimit() {
/*  65 */         return 1;
/*     */       }
/*  67 */     });
/*  68 */     addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47)
/*     */     {
/*     */       public boolean isItemValid(ItemStack stack)
/*     */       {
/*  72 */         return (stack.getItem() == Items.dye) && (EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLUE);
/*     */       }
/*     */     });
/*     */     
/*  76 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  78 */       for (int j = 0; j < 9; j++)
/*     */       {
/*  80 */         addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     }
/*     */     
/*  84 */     for (int k = 0; k < 9; k++)
/*     */     {
/*  86 */       addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener)
/*     */   {
/*  92 */     super.onCraftGuiOpened(listener);
/*  93 */     listener.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
/*  94 */     listener.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
/*  95 */     listener.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
/*  96 */     listener.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
/*  97 */     listener.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
/*  98 */     listener.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
/*  99 */     listener.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void detectAndSendChanges()
/*     */   {
/* 107 */     super.detectAndSendChanges();
/*     */     
/* 109 */     for (int i = 0; i < this.crafters.size(); i++)
/*     */     {
/* 111 */       ICrafting icrafting = (ICrafting)this.crafters.get(i);
/* 112 */       icrafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
/* 113 */       icrafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
/* 114 */       icrafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
/* 115 */       icrafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
/* 116 */       icrafting.sendProgressBarUpdate(this, 4, this.field_178151_h[0]);
/* 117 */       icrafting.sendProgressBarUpdate(this, 5, this.field_178151_h[1]);
/* 118 */       icrafting.sendProgressBarUpdate(this, 6, this.field_178151_h[2]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateProgressBar(int id, int data)
/*     */   {
/* 124 */     if ((id >= 0) && (id <= 2))
/*     */     {
/* 126 */       this.enchantLevels[id] = data;
/*     */     }
/* 128 */     else if (id == 3)
/*     */     {
/* 130 */       this.xpSeed = data;
/*     */     }
/* 132 */     else if ((id >= 4) && (id <= 6))
/*     */     {
/* 134 */       this.field_178151_h[(id - 4)] = data;
/*     */     }
/*     */     else
/*     */     {
/* 138 */       super.updateProgressBar(id, data);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn)
/*     */   {
/* 147 */     if (inventoryIn == this.tableInventory)
/*     */     {
/* 149 */       ItemStack itemstack = inventoryIn.getStackInSlot(0);
/*     */       
/* 151 */       if ((itemstack != null) && (itemstack.isItemEnchantable()))
/*     */       {
/* 153 */         if (!this.worldPointer.isRemote)
/*     */         {
/* 155 */           int l = 0;
/*     */           
/* 157 */           for (int j = -1; j <= 1; j++)
/*     */           {
/* 159 */             for (int k = -1; k <= 1; k++)
/*     */             {
/* 161 */               if (((j != 0) || (k != 0)) && (this.worldPointer.isAirBlock(this.position.add(k, 0, j))) && (this.worldPointer.isAirBlock(this.position.add(k, 1, j))))
/*     */               {
/* 163 */                 if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                 {
/* 165 */                   l++;
/*     */                 }
/*     */                 
/* 168 */                 if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                 {
/* 170 */                   l++;
/*     */                 }
/*     */                 
/* 173 */                 if ((k != 0) && (j != 0))
/*     */                 {
/* 175 */                   if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 177 */                     l++;
/*     */                   }
/*     */                   
/* 180 */                   if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 182 */                     l++;
/*     */                   }
/*     */                   
/* 185 */                   if (this.worldPointer.getBlockState(this.position.add(k, 0, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 187 */                     l++;
/*     */                   }
/*     */                   
/* 190 */                   if (this.worldPointer.getBlockState(this.position.add(k, 1, j * 2)).getBlock() == Blocks.bookshelf)
/*     */                   {
/* 192 */                     l++;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 199 */           this.rand.setSeed(this.xpSeed);
/*     */           
/* 201 */           for (int i1 = 0; i1 < 3; i1++)
/*     */           {
/* 203 */             this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, l, itemstack);
/* 204 */             this.field_178151_h[i1] = -1;
/*     */             
/* 206 */             if (this.enchantLevels[i1] < i1 + 1)
/*     */             {
/* 208 */               this.enchantLevels[i1] = 0;
/*     */             }
/*     */           }
/*     */           
/* 212 */           for (int j1 = 0; j1 < 3; j1++)
/*     */           {
/* 214 */             if (this.enchantLevels[j1] > 0)
/*     */             {
/* 216 */               List<EnchantmentData> list = func_178148_a(itemstack, j1, this.enchantLevels[j1]);
/*     */               
/* 218 */               if ((list != null) && (!list.isEmpty()))
/*     */               {
/* 220 */                 EnchantmentData enchantmentdata = (EnchantmentData)list.get(this.rand.nextInt(list.size()));
/* 221 */                 this.field_178151_h[j1] = (enchantmentdata.enchantmentobj.effectId | enchantmentdata.enchantmentLevel << 8);
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 226 */           detectAndSendChanges();
/*     */         }
/*     */         
/*     */       }
/*     */       else {
/* 231 */         for (int i = 0; i < 3; i++)
/*     */         {
/* 233 */           this.enchantLevels[i] = 0;
/* 234 */           this.field_178151_h[i] = -1;
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
/* 245 */     ItemStack itemstack = this.tableInventory.getStackInSlot(0);
/* 246 */     ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
/* 247 */     int i = id + 1;
/*     */     
/* 249 */     if (((itemstack1 == null) || (itemstack1.stackSize < i)) && (!playerIn.capabilities.isCreativeMode))
/*     */     {
/* 251 */       return false;
/*     */     }
/* 253 */     if ((this.enchantLevels[id] > 0) && (itemstack != null) && (((playerIn.experienceLevel >= i) && (playerIn.experienceLevel >= this.enchantLevels[id])) || (playerIn.capabilities.isCreativeMode)))
/*     */     {
/* 255 */       if (!this.worldPointer.isRemote)
/*     */       {
/* 257 */         List<EnchantmentData> list = func_178148_a(itemstack, id, this.enchantLevels[id]);
/* 258 */         boolean flag = itemstack.getItem() == Items.book;
/*     */         
/* 260 */         if (list != null)
/*     */         {
/* 262 */           playerIn.removeExperienceLevel(i);
/*     */           
/* 264 */           if (flag)
/*     */           {
/* 266 */             itemstack.setItem(Items.enchanted_book);
/*     */           }
/*     */           
/* 269 */           for (int j = 0; j < list.size(); j++)
/*     */           {
/* 271 */             EnchantmentData enchantmentdata = (EnchantmentData)list.get(j);
/*     */             
/* 273 */             if (flag)
/*     */             {
/* 275 */               Items.enchanted_book.addEnchantment(itemstack, enchantmentdata);
/*     */             }
/*     */             else
/*     */             {
/* 279 */               itemstack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
/*     */             }
/*     */           }
/*     */           
/* 283 */           if (!playerIn.capabilities.isCreativeMode)
/*     */           {
/* 285 */             itemstack1.stackSize -= i;
/*     */             
/* 287 */             if (itemstack1.stackSize <= 0)
/*     */             {
/* 289 */               this.tableInventory.setInventorySlotContents(1, null);
/*     */             }
/*     */           }
/*     */           
/* 293 */           playerIn.triggerAchievement(StatList.field_181739_W);
/* 294 */           this.tableInventory.markDirty();
/* 295 */           this.xpSeed = playerIn.getXPSeed();
/* 296 */           onCraftMatrixChanged(this.tableInventory);
/*     */         }
/*     */       }
/*     */       
/* 300 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 304 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private List<EnchantmentData> func_178148_a(ItemStack stack, int p_178148_2_, int p_178148_3_)
/*     */   {
/* 310 */     this.rand.setSeed(this.xpSeed + p_178148_2_);
/* 311 */     List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_);
/*     */     
/* 313 */     if ((stack.getItem() == Items.book) && (list != null) && (list.size() > 1))
/*     */     {
/* 315 */       list.remove(this.rand.nextInt(list.size()));
/*     */     }
/*     */     
/* 318 */     return list;
/*     */   }
/*     */   
/*     */   public int getLapisAmount()
/*     */   {
/* 323 */     ItemStack itemstack = this.tableInventory.getStackInSlot(1);
/* 324 */     return itemstack == null ? 0 : itemstack.stackSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/* 332 */     super.onContainerClosed(playerIn);
/*     */     
/* 334 */     if (!this.worldPointer.isRemote)
/*     */     {
/* 336 */       for (int i = 0; i < this.tableInventory.getSizeInventory(); i++)
/*     */       {
/* 338 */         ItemStack itemstack = this.tableInventory.removeStackFromSlot(i);
/*     */         
/* 340 */         if (itemstack != null)
/*     */         {
/* 342 */           playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/* 350 */     return this.worldPointer.getBlockState(this.position).getBlock() == Blocks.enchanting_table;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/* 358 */     ItemStack itemstack = null;
/* 359 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/* 361 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/* 363 */       ItemStack itemstack1 = slot.getStack();
/* 364 */       itemstack = itemstack1.copy();
/*     */       
/* 366 */       if (index == 0)
/*     */       {
/* 368 */         if (!mergeItemStack(itemstack1, 2, 38, true))
/*     */         {
/* 370 */           return null;
/*     */         }
/*     */       }
/* 373 */       else if (index == 1)
/*     */       {
/* 375 */         if (!mergeItemStack(itemstack1, 2, 38, true))
/*     */         {
/* 377 */           return null;
/*     */         }
/*     */       }
/* 380 */       else if ((itemstack1.getItem() == Items.dye) && (EnumDyeColor.byDyeDamage(itemstack1.getMetadata()) == EnumDyeColor.BLUE))
/*     */       {
/* 382 */         if (!mergeItemStack(itemstack1, 1, 2, true))
/*     */         {
/* 384 */           return null;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 389 */         if ((((Slot)this.inventorySlots.get(0)).getHasStack()) || (!((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1)))
/*     */         {
/* 391 */           return null;
/*     */         }
/*     */         
/* 394 */         if ((itemstack1.hasTagCompound()) && (itemstack1.stackSize == 1))
/*     */         {
/* 396 */           ((Slot)this.inventorySlots.get(0)).putStack(itemstack1.copy());
/* 397 */           itemstack1.stackSize = 0;
/*     */         }
/* 399 */         else if (itemstack1.stackSize >= 1)
/*     */         {
/* 401 */           ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
/* 402 */           itemstack1.stackSize -= 1;
/*     */         }
/*     */       }
/*     */       
/* 406 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 408 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 412 */         slot.onSlotChanged();
/*     */       }
/*     */       
/* 415 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 417 */         return null;
/*     */       }
/*     */       
/* 420 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     }
/*     */     
/* 423 */     return itemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */