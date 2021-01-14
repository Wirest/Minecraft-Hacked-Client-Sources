package store.shadowclient.client.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.InventoryUtils;
import store.shadowclient.client.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.DamageSource;

public final class InventoryManager extends Module {
	
   public InventoryManager() {
		super("InvManager", 0, Category.PLAYER);
		
		Shadow.instance.settingsManager.rSetting(new Setting("Sword Slot", this, 1.0D, 1.0D, 10.0D, true));
		 Shadow.instance.settingsManager.rSetting(new Setting("InvManager Delay", this, 250.0D, 0.0D, 1000.0D, true));
	}

public static final TimeHelper INV_STOPWATCH = new TimeHelper();
   private List allSwords = new ArrayList();
   private List[] allArmors = new List[4];
   private List trash = new ArrayList();
   private boolean cleaning;
   private int[] bestArmorSlot;
   private int bestSwordSlot;


   	@EventTarget
	public void onPreMotion(EventUpdate event) {
      if ((mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
         this.collectItems();
         this.collectBestArmor();
         this.collectTrash();
         int trashSize = this.trash.size();
         boolean trashPresent = trashSize > 0;
         EntityPlayerSP player = mc.thePlayer;
         int windowId = player.openContainer.windowId;
         int bestSwordSlot = this.bestSwordSlot;
         if (trashPresent) {
            if (!this.cleaning) {
               this.cleaning = true;
               player.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }

            for(int i = 0; i < trashSize; ++i) {
               int slot = (Integer)this.trash.get(i);
               if (this.checkDelay()) {
                  break;
               }

               mc.playerController.windowClick(windowId, slot < 9 ? slot + 36 : slot, 1, 4, player);
               INV_STOPWATCH.reset();
            }

            if (this.cleaning) {
               player.sendQueue.addToSendQueue(new C0DPacketCloseWindow(windowId));
               this.cleaning = false;
            }
         }

         if (bestSwordSlot != -1 && !this.checkDelay()) {
            mc.playerController.windowClick(windowId, bestSwordSlot < 9 ? bestSwordSlot + 36 : bestSwordSlot, (int) ((Shadow.instance.settingsManager.getSettingByName("Sword Slot").getValDouble()) - 1), 2, player);
            INV_STOPWATCH.reset();
         }
      }

   }

   private boolean checkDelay() {
      return !INV_STOPWATCH.hasReached((long) Shadow.instance.settingsManager.getSettingByName("InvManager Delay").getValDouble());
   }

   public void collectItems() {
      this.bestSwordSlot = -1;
      this.allSwords.clear();
      float bestSwordDamage = -1.0F;

      for(int i = 0; i < 36; ++i) {
         ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
         if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemSword) {
            float damageLevel = InventoryUtils.getDamageLevel(itemStack);
            this.allSwords.add(i);
            if (bestSwordDamage < damageLevel) {
               bestSwordDamage = damageLevel;
               this.bestSwordSlot = i;
            }
         }
      }

   }

   private void collectBestArmor() {
      int[] bestArmorDamageReducement = new int[4];
      this.bestArmorSlot = new int[4];
      Arrays.fill(bestArmorDamageReducement, -1);
      Arrays.fill(this.bestArmorSlot, -1);

      int i;
      ItemStack itemStack;
      ItemArmor armor;
      int armorType;
      for(i = 0; i < this.bestArmorSlot.length; ++i) {
         itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
         this.allArmors[i] = new ArrayList();
         if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
            armor = (ItemArmor)itemStack.getItem();
            armorType = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
            bestArmorDamageReducement[i] = armorType;
         }
      }

      for(i = 0; i < 36; ++i) {
         itemStack = mc.thePlayer.inventory.getStackInSlot(i);
         if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
            armor = (ItemArmor)itemStack.getItem();
            armorType = 3 - armor.armorType;
            this.allArmors[armorType].add(i);
            int slotProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
            if (bestArmorDamageReducement[armorType] < slotProtectionLevel) {
               bestArmorDamageReducement[armorType] = slotProtectionLevel;
               this.bestArmorSlot[armorType] = i;
            }
         }
      }

   }

   private void collectTrash() {
      this.trash.clear();

      int i;
      for(i = 0; i < 36; ++i) {
         ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
         if (itemStack != null && itemStack.getItem() != null && !InventoryUtils.isValidItem(itemStack)) {
            this.trash.add(i);
         }
      }

      for(i = 0; i < this.allArmors.length; ++i) {
         List armorItem = this.allArmors[i];
         if (armorItem != null) {
            List integers = this.trash;
            int i1 = 0;

            for(int armorItemSize = armorItem.size(); i1 < armorItemSize; ++i1) {
               Integer slot = (Integer)armorItem.get(i1);
               if (slot != this.bestArmorSlot[i]) {
                  integers.add(slot);
               }
            }
         }
      }

      List integers = this.trash;
      int i1 = 0;

      for(int allSwordsSize = this.allSwords.size(); i1 < allSwordsSize; ++i1) {
         Integer slot = (Integer)this.allSwords.get(i1);
         if (slot != this.bestSwordSlot) {
            integers.add(slot);
         }
      }
   }
   
   @Override
	public void onEnable() {
		super.onEnable();
	}
   
   @Override
	public void onDisable() {
		super.onDisable();
	}
}
