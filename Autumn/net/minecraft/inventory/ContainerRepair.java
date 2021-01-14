package net.minecraft.inventory;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerRepair extends Container {
   private static final Logger logger = LogManager.getLogger();
   private IInventory outputSlot;
   private IInventory inputSlots;
   private World theWorld;
   private BlockPos selfPosition;
   public int maximumCost;
   private int materialCost;
   private String repairedItemName;
   private final EntityPlayer thePlayer;

   public ContainerRepair(InventoryPlayer playerInventory, World worldIn, EntityPlayer player) {
      this(playerInventory, worldIn, BlockPos.ORIGIN, player);
   }

   public ContainerRepair(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player) {
      this.outputSlot = new InventoryCraftResult();
      this.inputSlots = new InventoryBasic("Repair", true, 2) {
         public void markDirty() {
            super.markDirty();
            ContainerRepair.this.onCraftMatrixChanged(this);
         }
      };
      this.selfPosition = blockPosIn;
      this.theWorld = worldIn;
      this.thePlayer = player;
      this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
      this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
      this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47) {
         public boolean isItemValid(ItemStack stack) {
            return false;
         }

         public boolean canTakeStack(EntityPlayer playerIn) {
            return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerRepair.this.maximumCost) && ContainerRepair.this.maximumCost > 0 && this.getHasStack();
         }

         public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
            if (!playerIn.capabilities.isCreativeMode) {
               playerIn.addExperienceLevel(-ContainerRepair.this.maximumCost);
            }

            ContainerRepair.this.inputSlots.setInventorySlotContents(0, (ItemStack)null);
            if (ContainerRepair.this.materialCost > 0) {
               ItemStack itemstack = ContainerRepair.this.inputSlots.getStackInSlot(1);
               if (itemstack != null && itemstack.stackSize > ContainerRepair.this.materialCost) {
                  itemstack.stackSize -= ContainerRepair.this.materialCost;
                  ContainerRepair.this.inputSlots.setInventorySlotContents(1, itemstack);
               } else {
                  ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
               }
            } else {
               ContainerRepair.this.inputSlots.setInventorySlotContents(1, (ItemStack)null);
            }

            ContainerRepair.this.maximumCost = 0;
            IBlockState iblockstate = worldIn.getBlockState(blockPosIn);
            if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.anvil && playerIn.getRNG().nextFloat() < 0.12F) {
               int l = (Integer)iblockstate.getValue(BlockAnvil.DAMAGE);
               ++l;
               if (l > 2) {
                  worldIn.setBlockToAir(blockPosIn);
                  worldIn.playAuxSFX(1020, blockPosIn, 0);
               } else {
                  worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, l), 2);
                  worldIn.playAuxSFX(1021, blockPosIn, 0);
               }
            } else if (!worldIn.isRemote) {
               worldIn.playAuxSFX(1021, blockPosIn, 0);
            }

         }
      });

      int k;
      for(k = 0; k < 3; ++k) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
         }
      }

      for(k = 0; k < 9; ++k) {
         this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
      }

   }

   public void onCraftMatrixChanged(IInventory inventoryIn) {
      super.onCraftMatrixChanged(inventoryIn);
      if (inventoryIn == this.inputSlots) {
         this.updateRepairOutput();
      }

   }

   public void updateRepairOutput() {
      int i = false;
      int j = true;
      int k = true;
      int l = true;
      int i1 = true;
      int j1 = true;
      int k1 = true;
      ItemStack itemstack = this.inputSlots.getStackInSlot(0);
      this.maximumCost = 1;
      int l1 = 0;
      int i2 = 0;
      int j2 = 0;
      if (itemstack == null) {
         this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
         this.maximumCost = 0;
      } else {
         ItemStack itemstack1 = itemstack.copy();
         ItemStack itemstack2 = this.inputSlots.getStackInSlot(1);
         Map map = EnchantmentHelper.getEnchantments(itemstack1);
         boolean flag = false;
         int i2 = i2 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
         this.materialCost = 0;
         int k4;
         if (itemstack2 != null) {
            flag = itemstack2.getItem() == Items.enchanted_book && Items.enchanted_book.getEnchantments(itemstack2).tagCount() > 0;
            int l2;
            int i5;
            if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
               k4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
               if (k4 <= 0) {
                  this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                  this.maximumCost = 0;
                  return;
               }

               for(l2 = 0; k4 > 0 && l2 < itemstack2.stackSize; ++l2) {
                  i5 = itemstack1.getItemDamage() - k4;
                  itemstack1.setItemDamage(i5);
                  ++l1;
                  k4 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
               }

               this.materialCost = l2;
            } else {
               if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
                  this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
                  this.maximumCost = 0;
                  return;
               }

               int k5;
               if (itemstack1.isItemStackDamageable() && !flag) {
                  k4 = itemstack.getMaxDamage() - itemstack.getItemDamage();
                  l2 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
                  i5 = l2 + itemstack1.getMaxDamage() * 12 / 100;
                  int j3 = k4 + i5;
                  k5 = itemstack1.getMaxDamage() - j3;
                  if (k5 < 0) {
                     k5 = 0;
                  }

                  if (k5 < itemstack1.getMetadata()) {
                     itemstack1.setItemDamage(k5);
                     l1 += 2;
                  }
               }

               Map map1 = EnchantmentHelper.getEnchantments(itemstack2);
               Iterator iterator1 = map1.keySet().iterator();

               label147:
               while(true) {
                  Enchantment enchantment;
                  do {
                     if (!iterator1.hasNext()) {
                        break label147;
                     }

                     i5 = (Integer)iterator1.next();
                     enchantment = Enchantment.getEnchantmentById(i5);
                  } while(enchantment == null);

                  k5 = map.containsKey(i5) ? (Integer)map.get(i5) : 0;
                  int l3 = (Integer)map1.get(i5);
                  int i6;
                  if (k5 == l3) {
                     ++l3;
                     i6 = l3;
                  } else {
                     i6 = Math.max(l3, k5);
                  }

                  l3 = i6;
                  boolean flag1 = enchantment.canApply(itemstack);
                  if (this.thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.enchanted_book) {
                     flag1 = true;
                  }

                  Iterator iterator = map.keySet().iterator();

                  int l5;
                  while(iterator.hasNext()) {
                     l5 = (Integer)iterator.next();
                     if (l5 != i5 && !enchantment.canApplyTogether(Enchantment.getEnchantmentById(l5))) {
                        flag1 = false;
                        ++l1;
                     }
                  }

                  if (flag1) {
                     if (i6 > enchantment.getMaxLevel()) {
                        l3 = enchantment.getMaxLevel();
                     }

                     map.put(i5, l3);
                     l5 = 0;
                     switch(enchantment.getWeight()) {
                     case 1:
                        l5 = 8;
                        break;
                     case 2:
                        l5 = 4;
                     case 3:
                     case 4:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     default:
                        break;
                     case 5:
                        l5 = 2;
                        break;
                     case 10:
                        l5 = 1;
                     }

                     if (flag) {
                        l5 = Math.max(1, l5 / 2);
                     }

                     l1 += l5 * l3;
                  }
               }
            }
         }

         if (StringUtils.isBlank(this.repairedItemName)) {
            if (itemstack.hasDisplayName()) {
               j2 = 1;
               l1 += j2;
               itemstack1.clearCustomName();
            }
         } else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
            j2 = 1;
            l1 += j2;
            itemstack1.setStackDisplayName(this.repairedItemName);
         }

         this.maximumCost = i2 + l1;
         if (l1 <= 0) {
            itemstack1 = null;
         }

         if (j2 == l1 && j2 > 0 && this.maximumCost >= 40) {
            this.maximumCost = 39;
         }

         if (this.maximumCost >= 40 && !this.thePlayer.capabilities.isCreativeMode) {
            itemstack1 = null;
         }

         if (itemstack1 != null) {
            k4 = itemstack1.getRepairCost();
            if (itemstack2 != null && k4 < itemstack2.getRepairCost()) {
               k4 = itemstack2.getRepairCost();
            }

            k4 = k4 * 2 + 1;
            itemstack1.setRepairCost(k4);
            EnchantmentHelper.setEnchantments(map, itemstack1);
         }

         this.outputSlot.setInventorySlotContents(0, itemstack1);
         this.detectAndSendChanges();
      }

   }

   public void onCraftGuiOpened(ICrafting listener) {
      super.onCraftGuiOpened(listener);
      listener.sendProgressBarUpdate(this, 0, this.maximumCost);
   }

   public void updateProgressBar(int id, int data) {
      if (id == 0) {
         this.maximumCost = data;
      }

   }

   public void onContainerClosed(EntityPlayer playerIn) {
      super.onContainerClosed(playerIn);
      if (!this.theWorld.isRemote) {
         for(int i = 0; i < this.inputSlots.getSizeInventory(); ++i) {
            ItemStack itemstack = this.inputSlots.removeStackFromSlot(i);
            if (itemstack != null) {
               playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
         }
      }

   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return this.theWorld.getBlockState(this.selfPosition).getBlock() != Blocks.anvil ? false : playerIn.getDistanceSq((double)this.selfPosition.getX() + 0.5D, (double)this.selfPosition.getY() + 0.5D, (double)this.selfPosition.getZ() + 0.5D) <= 64.0D;
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (index == 2) {
            if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
               return null;
            }

            slot.onSlotChange(itemstack1, itemstack);
         } else if (index != 0 && index != 1) {
            if (index >= 3 && index < 39 && !this.mergeItemStack(itemstack1, 0, 2, false)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
            return null;
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }

         if (itemstack1.stackSize == itemstack.stackSize) {
            return null;
         }

         slot.onPickupFromSlot(playerIn, itemstack1);
      }

      return itemstack;
   }

   public void updateItemName(String newName) {
      this.repairedItemName = newName;
      if (this.getSlot(2).getHasStack()) {
         ItemStack itemstack = this.getSlot(2).getStack();
         if (StringUtils.isBlank(newName)) {
            itemstack.clearCustomName();
         } else {
            itemstack.setStackDisplayName(this.repairedItemName);
         }
      }

      this.updateRepairOutput();
   }
}
