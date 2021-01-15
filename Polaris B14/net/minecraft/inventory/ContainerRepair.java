/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.BlockAnvil;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class ContainerRepair extends Container
/*     */ {
/*  22 */   private static final org.apache.logging.log4j.Logger logger = ;
/*     */   
/*     */ 
/*     */   private IInventory outputSlot;
/*     */   
/*     */ 
/*     */   private IInventory inputSlots;
/*     */   
/*     */ 
/*     */   private World theWorld;
/*     */   
/*     */   private BlockPos selfPosition;
/*     */   
/*     */   public int maximumCost;
/*     */   
/*     */   private int materialCost;
/*     */   
/*     */   private String repairedItemName;
/*     */   
/*     */   private final EntityPlayer thePlayer;
/*     */   
/*     */ 
/*     */   public ContainerRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player)
/*     */   {
/*  46 */     this(playerInventory, worldIn, BlockPos.ORIGIN, player);
/*     */   }
/*     */   
/*     */   public ContainerRepair(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player)
/*     */   {
/*  51 */     this.outputSlot = new InventoryCraftResult();
/*  52 */     this.inputSlots = new InventoryBasic("Repair", true, 2)
/*     */     {
/*     */       public void markDirty()
/*     */       {
/*  56 */         super.markDirty();
/*  57 */         ContainerRepair.this.onCraftMatrixChanged(this);
/*     */       }
/*  59 */     };
/*  60 */     this.selfPosition = blockPosIn;
/*  61 */     this.theWorld = worldIn;
/*  62 */     this.thePlayer = player;
/*  63 */     addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
/*  64 */     addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
/*  65 */     addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47)
/*     */     {
/*     */       public boolean isItemValid(ItemStack stack)
/*     */       {
/*  69 */         return false;
/*     */       }
/*     */       
/*     */       public boolean canTakeStack(EntityPlayer playerIn) {
/*  73 */         return ((playerIn.capabilities.isCreativeMode) || (playerIn.experienceLevel >= ContainerRepair.this.maximumCost)) && (ContainerRepair.this.maximumCost > 0) && (getHasStack());
/*     */       }
/*     */       
/*     */       public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/*  77 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/*  79 */           playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
/*     */         }
/*     */         
/*  82 */         ContainerRepair.this.inputSlots.setInventorySlotContents(0, null);
/*     */         
/*  84 */         if (ContainerRepair.this.materialCost > 0)
/*     */         {
/*  86 */           ItemStack itemstack = ContainerRepair.this.inputSlots.getStackInSlot(1);
/*     */           
/*  88 */           if ((itemstack != null) && (itemstack.stackSize > ContainerRepair.this.materialCost))
/*     */           {
/*  90 */             itemstack.stackSize -= ContainerRepair.this.materialCost;
/*  91 */             ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack);
/*     */           }
/*     */           else
/*     */           {
/*  95 */             ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 100 */           ContainerRepair.this.inputSlots.setInventorySlotContents(1, null);
/*     */         }
/*     */         
/* 103 */         ContainerRepair.this.maximumCost = 0;
/* 104 */         IBlockState iblockstate = worldIn.getBlockState(blockPosIn);
/*     */         
/* 106 */         if ((!playerIn.capabilities.isCreativeMode) && (!worldIn.isRemote) && (iblockstate.getBlock() == Blocks.anvil) && (playerIn.getRNG().nextFloat() < 0.12F))
/*     */         {
/* 108 */           int l = ((Integer)iblockstate.getValue(BlockAnvil.DAMAGE)).intValue();
/* 109 */           l++;
/*     */           
/* 111 */           if (l > 2)
/*     */           {
/* 113 */             worldIn.setBlockToAir(blockPosIn);
/* 114 */             worldIn.playAuxSFX(1020, blockPosIn, 0);
/*     */           }
/*     */           else
/*     */           {
/* 118 */             worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(l)), 2);
/* 119 */             worldIn.playAuxSFX(1021, blockPosIn, 0);
/*     */           }
/*     */         }
/* 122 */         else if (!worldIn.isRemote)
/*     */         {
/* 124 */           worldIn.playAuxSFX(1021, blockPosIn, 0);
/*     */         }
/*     */       }
/*     */     });
/*     */     
/* 129 */     for (int i = 0; i < 3; i++)
/*     */     {
/* 131 */       for (int j = 0; j < 9; j++)
/*     */       {
/* 133 */         addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
/*     */       }
/*     */     }
/*     */     
/* 137 */     for (int k = 0; k < 9; k++)
/*     */     {
/* 139 */       addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCraftMatrixChanged(IInventory inventoryIn)
/*     */   {
/* 148 */     super.onCraftMatrixChanged(inventoryIn);
/*     */     
/* 150 */     if (inventoryIn == this.inputSlots)
/*     */     {
/* 152 */       updateRepairOutput();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateRepairOutput()
/*     */   {
/* 161 */     int i = 0;
/* 162 */     int j = 1;
/* 163 */     int k = 1;
/* 164 */     int l = 1;
/* 165 */     int i1 = 2;
/* 166 */     int j1 = 1;
/* 167 */     int k1 = 1;
/* 168 */     ItemStack itemstack = this.inputSlots.getStackInSlot(0);
/* 169 */     this.maximumCost = 1;
/* 170 */     int l1 = 0;
/* 171 */     int i2 = 0;
/* 172 */     int j2 = 0;
/*     */     
/* 174 */     if (itemstack == null)
/*     */     {
/* 176 */       this.outputSlot.setInventorySlotContents(0, null);
/* 177 */       this.maximumCost = 0;
/*     */     }
/*     */     else
/*     */     {
/* 181 */       ItemStack itemstack1 = itemstack.copy();
/* 182 */       ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
/* 183 */       Map<Integer, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
/* 184 */       boolean flag = false;
/* 185 */       i2 = i2 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
/* 186 */       this.materialCost = 0;
/*     */       
/* 188 */       if (itemstack2 != null)
/*     */       {
/* 190 */         flag = (itemstack2.getItem() == Items.enchanted_book) && (Items.enchanted_book.getEnchantments(itemstack2).tagCount() > 0);
/*     */         
/* 192 */         if ((itemstack1.isItemStackDamageable()) && (itemstack1.getItem().getIsRepairable(itemstack, itemstack2)))
/*     */         {
/* 194 */           int j4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
/*     */           
/* 196 */           if (j4 <= 0)
/*     */           {
/* 198 */             this.outputSlot.setInventorySlotContents(0, null);
/* 199 */             this.maximumCost = 0;
/* 200 */             return;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 205 */           for (int l4 = 0; (j4 > 0) && (l4 < itemstack2.stackSize); l4++)
/*     */           {
/* 207 */             int j5 = itemstack1.getItemDamage() - j4;
/* 208 */             itemstack1.setItemDamage(j5);
/* 209 */             l1++;
/* 210 */             j4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
/*     */           }
/*     */           
/* 213 */           this.materialCost = l4;
/*     */         }
/*     */         else
/*     */         {
/* 217 */           if ((!flag) && ((itemstack1.getItem() != itemstack2.getItem()) || (!itemstack1.isItemStackDamageable())))
/*     */           {
/* 219 */             this.outputSlot.setInventorySlotContents(0, null);
/* 220 */             this.maximumCost = 0;
/* 221 */             return;
/*     */           }
/*     */           
/* 224 */           if ((itemstack1.isItemStackDamageable()) && (!flag))
/*     */           {
/* 226 */             int k2 = itemstack.getMaxDamage() - itemstack.getItemDamage();
/* 227 */             int l2 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
/* 228 */             int i3 = l2 + itemstack1.getMaxDamage() * 12 / 100;
/* 229 */             int j3 = k2 + i3;
/* 230 */             int k3 = itemstack1.getMaxDamage() - j3;
/*     */             
/* 232 */             if (k3 < 0)
/*     */             {
/* 234 */               k3 = 0;
/*     */             }
/*     */             
/* 237 */             if (k3 < itemstack1.getMetadata())
/*     */             {
/* 239 */               itemstack1.setItemDamage(k3);
/* 240 */               l1 += 2;
/*     */             }
/*     */           }
/*     */           
/* 244 */           Map<Integer, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
/* 245 */           Iterator iterator1 = map1.keySet().iterator();
/*     */           
/* 247 */           while (iterator1.hasNext())
/*     */           {
/* 249 */             int i5 = ((Integer)iterator1.next()).intValue();
/* 250 */             Enchantment enchantment = Enchantment.getEnchantmentById(i5);
/*     */             
/* 252 */             if (enchantment != null)
/*     */             {
/* 254 */               int k5 = map.containsKey(Integer.valueOf(i5)) ? ((Integer)map.get(Integer.valueOf(i5))).intValue() : 0;
/* 255 */               int l3 = ((Integer)map1.get(Integer.valueOf(i5))).intValue();
/*     */               int i6;
/*     */               int i6;
/* 258 */               if (k5 == l3)
/*     */               {
/* 260 */                 l3++;
/* 261 */                 i6 = l3;
/*     */               }
/*     */               else
/*     */               {
/* 265 */                 i6 = Math.max(l3, k5);
/*     */               }
/*     */               
/* 268 */               l3 = i6;
/* 269 */               boolean flag1 = enchantment.canApply(itemstack);
/*     */               
/* 271 */               if ((this.thePlayer.capabilities.isCreativeMode) || (itemstack.getItem() == Items.enchanted_book))
/*     */               {
/* 273 */                 flag1 = true;
/*     */               }
/*     */               
/* 276 */               Iterator iterator = map.keySet().iterator();
/*     */               
/* 278 */               while (iterator.hasNext())
/*     */               {
/* 280 */                 int i4 = ((Integer)iterator.next()).intValue();
/*     */                 
/* 282 */                 if ((i4 != i5) && (!enchantment.canApplyTogether(Enchantment.getEnchantmentById(i4))))
/*     */                 {
/* 284 */                   flag1 = false;
/* 285 */                   l1++;
/*     */                 }
/*     */               }
/*     */               
/* 289 */               if (flag1)
/*     */               {
/* 291 */                 if (l3 > enchantment.getMaxLevel())
/*     */                 {
/* 293 */                   l3 = enchantment.getMaxLevel();
/*     */                 }
/*     */                 
/* 296 */                 map.put(Integer.valueOf(i5), Integer.valueOf(l3));
/* 297 */                 int l5 = 0;
/*     */                 
/* 299 */                 switch (enchantment.getWeight())
/*     */                 {
/*     */                 case 1: 
/* 302 */                   l5 = 8;
/* 303 */                   break;
/*     */                 
/*     */                 case 2: 
/* 306 */                   l5 = 4;
/*     */                 
/*     */                 case 3: 
/*     */                 case 4: 
/*     */                 case 6: 
/*     */                 case 7: 
/*     */                 case 8: 
/*     */                 case 9: 
/*     */                 default: 
/*     */                   break;
/*     */                 
/*     */                 case 5: 
/* 318 */                   l5 = 2;
/* 319 */                   break;
/*     */                 
/*     */                 case 10: 
/* 322 */                   l5 = 1;
/*     */                 }
/*     */                 
/* 325 */                 if (flag)
/*     */                 {
/* 327 */                   l5 = Math.max(1, l5 / 2);
/*     */                 }
/*     */                 
/* 330 */                 l1 += l5 * l3;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 337 */       if (StringUtils.isBlank(this.repairedItemName))
/*     */       {
/* 339 */         if (itemstack.hasDisplayName())
/*     */         {
/* 341 */           j2 = 1;
/* 342 */           l1 += j2;
/* 343 */           itemstack1.clearCustomName();
/*     */         }
/*     */       }
/* 346 */       else if (!this.repairedItemName.equals(itemstack.getDisplayName()))
/*     */       {
/* 348 */         j2 = 1;
/* 349 */         l1 += j2;
/* 350 */         itemstack1.setStackDisplayName(this.repairedItemName);
/*     */       }
/*     */       
/* 353 */       this.maximumCost = (i2 + l1);
/*     */       
/* 355 */       if (l1 <= 0)
/*     */       {
/* 357 */         itemstack1 = null;
/*     */       }
/*     */       
/* 360 */       if ((j2 == l1) && (j2 > 0) && (this.maximumCost >= 40))
/*     */       {
/* 362 */         this.maximumCost = 39;
/*     */       }
/*     */       
/* 365 */       if ((this.maximumCost >= 40) && (!this.thePlayer.capabilities.isCreativeMode))
/*     */       {
/* 367 */         itemstack1 = null;
/*     */       }
/*     */       
/* 370 */       if (itemstack1 != null)
/*     */       {
/* 372 */         int k4 = itemstack1.getRepairCost();
/*     */         
/* 374 */         if ((itemstack2 != null) && (k4 < itemstack2.getRepairCost()))
/*     */         {
/* 376 */           k4 = itemstack2.getRepairCost();
/*     */         }
/*     */         
/* 379 */         k4 = k4 * 2 + 1;
/* 380 */         itemstack1.setRepairCost(k4);
/* 381 */         EnchantmentHelper.setEnchantments(map, itemstack1);
/*     */       }
/*     */       
/* 384 */       this.outputSlot.setInventorySlotContents(0, itemstack1);
/* 385 */       detectAndSendChanges();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener)
/*     */   {
/* 391 */     super.onCraftGuiOpened(listener);
/* 392 */     listener.sendProgressBarUpdate(this, 0, this.maximumCost);
/*     */   }
/*     */   
/*     */   public void updateProgressBar(int id, int data)
/*     */   {
/* 397 */     if (id == 0)
/*     */     {
/* 399 */       this.maximumCost = data;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onContainerClosed(EntityPlayer playerIn)
/*     */   {
/* 408 */     super.onContainerClosed(playerIn);
/*     */     
/* 410 */     if (!this.theWorld.isRemote)
/*     */     {
/* 412 */       for (int i = 0; i < this.inputSlots.getSizeInventory(); i++)
/*     */       {
/* 414 */         ItemStack itemstack = this.inputSlots.removeStackFromSlot(i);
/*     */         
/* 416 */         if (itemstack != null)
/*     */         {
/* 418 */           playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn)
/*     */   {
/* 426 */     return this.theWorld.getBlockState(this.selfPosition).getBlock() == Blocks.anvil;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
/*     */   {
/* 434 */     ItemStack itemstack = null;
/* 435 */     Slot slot = (Slot)this.inventorySlots.get(index);
/*     */     
/* 437 */     if ((slot != null) && (slot.getHasStack()))
/*     */     {
/* 439 */       ItemStack itemstack1 = slot.getStack();
/* 440 */       itemstack = itemstack1.copy();
/*     */       
/* 442 */       if (index == 2)
/*     */       {
/* 444 */         if (!mergeItemStack(itemstack1, 3, 39, true))
/*     */         {
/* 446 */           return null;
/*     */         }
/*     */         
/* 449 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/* 451 */       else if ((index != 0) && (index != 1))
/*     */       {
/* 453 */         if ((index >= 3) && (index < 39) && (!mergeItemStack(itemstack1, 0, 2, false)))
/*     */         {
/* 455 */           return null;
/*     */         }
/*     */       }
/* 458 */       else if (!mergeItemStack(itemstack1, 3, 39, false))
/*     */       {
/* 460 */         return null;
/*     */       }
/*     */       
/* 463 */       if (itemstack1.stackSize == 0)
/*     */       {
/* 465 */         slot.putStack(null);
/*     */       }
/*     */       else
/*     */       {
/* 469 */         slot.onSlotChanged();
/*     */       }
/*     */       
/* 472 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 474 */         return null;
/*     */       }
/*     */       
/* 477 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     }
/*     */     
/* 480 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateItemName(String newName)
/*     */   {
/* 488 */     this.repairedItemName = newName;
/*     */     
/* 490 */     if (getSlot(2).getHasStack())
/*     */     {
/* 492 */       ItemStack itemstack = getSlot(2).getStack();
/*     */       
/* 494 */       if (StringUtils.isBlank(newName))
/*     */       {
/* 496 */         itemstack.clearCustomName();
/*     */       }
/*     */       else
/*     */       {
/* 500 */         itemstack.setStackDisplayName(this.repairedItemName);
/*     */       }
/*     */     }
/*     */     
/* 504 */     updateRepairOutput();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ContainerRepair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */