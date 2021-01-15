package nivia.utils.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import nivia.utils.Helper;

public class InventoryUtils
{ 
	Timer timer = new Timer();
    // Thanks to andrew <3
	
    public static void shiftClick(final Item item) {
        for (int index = 9; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && stack.getItem() == item) {
                Helper.mc().playerController.windowClick(0, index, 0, 1, Helper.mc().thePlayer);
                break;
            }
        }
    }
    
    public static boolean hotbarHas(final Item item) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }
    
    public static void useFirst(final Item item) {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                final int oldItem = Helper.mc().thePlayer.inventory.currentItem;
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index));
                Helper.mc().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Helper.mc().thePlayer.inventory.getCurrentItem()));
                Helper.mc().thePlayer.stopUsingItem();
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldItem));
                break;
            }
        }
    }
    
    
    public static void instantUseFirst(final Item item) {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                final int oldItem = Helper.mc().thePlayer.inventory.currentItem;
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index));
                Helper.mc().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Helper.mc().thePlayer.inventory.getCurrentItem()));
                for (int x = 0; x <= 32; ++x) {
                    Helper.mc().getNetHandler().addToSendQueue(new C03PacketPlayer(Helper.mc().thePlayer.onGround));
                }
                Helper.mc().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                Helper.mc().thePlayer.stopUsingItem();
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldItem));
                break;
            }
        }
    }
    
    public static void UseFirstSoup() {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() instanceof ItemSoup) {
                final int oldItem = Helper.mc().thePlayer.inventory.currentItem;
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index));
                Helper.mc().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Helper.mc().thePlayer.inventory.getCurrentItem()));
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldItem));
                break;
            }
        }
    }
    
    public static void dropFirst(final Item item) {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                Helper.mc().playerController.windowClick(0, 36 + index, 1, 4, Helper.mc().thePlayer);
                break;
            }
        }
    }
    
    public static int getSlotID(final Item item) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                return index;
            }
        }
        return -1;
    }
    
    public static int countItem(final Item item) {
        int counter = 0;
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                counter += stack.stackSize;
            }
        }
        return counter;
    }
    
    public static boolean isPotion(final ItemStack stack, final Potion potion, final boolean splash) {
        if (stack == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemPotion)) {
            return false;
        }
        final ItemPotion potionItem = (ItemPotion)stack.getItem();
        if (splash && !ItemPotion.isSplash(stack.getItemDamage())) {
            return false;
        }
        if (potionItem.getEffects(stack) == null) {
            return potion == null;
        }
        if (potion == null) {
            return false;
        }
        for (final Object o : potionItem.getEffects(stack)) {
            final PotionEffect effect = (PotionEffect)o;
            if (effect.getPotionID() == potion.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSoup(final ItemStack stack) {
        if (stack != null) {
            if (stack.getItem() instanceof ItemSoup)
                return true;
        }
        return false;
    }
    
    public static void shiftClickPotion(final Potion effect, final boolean splash) {
        for (int index = 9; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && isPotion(stack, effect, splash)) {
                Helper.mc().playerController.windowClick(Helper.mc().thePlayer.openContainer.windowId, index, 0, 1, Helper.mc().thePlayer);
                break;
            }
        }
    }
    public static void getPotion(final Potion effect, final boolean splash) {
        for (int index = 9; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && isPotion(stack, effect, splash)) {
                Helper.mc().playerController.windowClick(Helper.mc().thePlayer.openContainer.windowId, index, 1, 2, Helper.mc().thePlayer);
                break;
            }
        }
    }
    
    public static void shiftClickSoup() {
        for (int index = 9; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && stack.getItem() instanceof ItemSoup) {
                Helper.mc().playerController.windowClick(0, index, 0, 1, Helper.mc().thePlayer);
                break;
            }
        }
    }
    
    public static boolean hotbarHasPotion(final Potion effect, final boolean splash) {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && isPotion(stack, effect, splash)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean inventoryHasPotion(final Potion effect, final boolean splash) {
    	 for (int index = 0; index <= 36; ++index) {
             final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
             if (stack != null && isPotion(stack, effect, splash)) {
                 return true;
             }
         }
         return false;
    }
    

    public static void swap(int slot, int hotbarNum) {
      Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.getMinecraft().thePlayer);
    }
    
    
    public static void useFirstPotionSilent(final Potion effect, final boolean splash) {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && isPotion(stack, effect, splash)) {
                final int oldItem = Helper.mc().thePlayer.inventory.currentItem;
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index));
                Helper.mc().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Helper.mc().thePlayer.inventory.getCurrentItem()));
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldItem));
                break;
            }
        }
    }

    
    public static void instantUseFirstPotion(final Potion effect) {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && isPotion(stack, effect, false)) {
                final int oldItem = Helper.mc().thePlayer.inventory.currentItem;
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index));
                Helper.mc().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Helper.mc().thePlayer.inventory.getCurrentItem()));
                for (int x = 0; x <= 32; ++x) {
                    Helper.mc().getNetHandler().addToSendQueue(new C03PacketPlayer(Helper.mc().thePlayer.onGround));
                }
                Helper.mc().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                Helper.mc().thePlayer.stopUsingItem();
                Helper.mc().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(oldItem));
                break;
            }
        }
    }
    
    public static void dropFirstPotion(final Potion effect, final boolean splash) {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && isPotion(stack, effect, splash)) {
                Helper.mc().playerController.windowClick(0, 36 + index, 1, 4, Helper.mc().thePlayer);
                break;
            }
        }
    }
    
    public static int getPotionSlotID(final Potion effect, final boolean splash) {
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && isPotion(stack, effect, splash)) {
                return index;
            }
        }
        return -1;
    }
    
    public static int countPotion(final Potion effect, final boolean splash) {
        int counter = 0;
        for (int index = 0; index <= 36; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack != null && isPotion(stack, effect, splash)) {
                counter += stack.stackSize;
            }
        }
        return counter;
    }
    public static int countInInventory(EntityPlayer player, Item item, int md) {
        int count = 0;
        for (int i = 0; i < player.inventory.mainInventory.length; i++)
            if ((player.inventory.mainInventory[i] != null) && item.equals(player.inventory.mainInventory[i].getItem()) && ((md == -1) || (player.inventory.mainInventory[i].getMetadata() == md)))
                count += player.inventory.mainInventory[i].stackSize;
        return count;
    }
    public static boolean hotbarIsFull() {
        for (int index = 0; index <= 8; ++index) {
            final ItemStack stack = Helper.mc().thePlayer.inventory.getStackInSlot(index);
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
}