package rip.autumn.module.impl.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.DamageSource;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.DoubleOption;

@Label("Auto Armor")
@Category(ModuleCategory.PLAYER)
@Aliases({"autoarmor"})
public final class AutoArmorMod extends Module {
   private List[] allArmors = new List[4];
   private boolean equipping;
   private int[] bestArmorSlot;
   private final DoubleOption delay = new DoubleOption("Delay", 250.0D, 0.0D, 1000.0D, 50.0D);

   public AutoArmorMod() {
      this.addOptions(new Option[]{this.delay});
   }

   @Listener(MotionUpdateEvent.class)
   public void onEvent(MotionUpdateEvent event) {
      if ((mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) && event.isPre()) {
         this.collectBestArmor();
         EntityPlayerSP player = mc.thePlayer;
         int inventoryId = player.inventoryContainer.windowId;

         for(int i = 0; i < 4; ++i) {
            if (this.bestArmorSlot[i] != -1) {
               if (!this.equipping) {
                  this.equipping = true;
                  player.sendQueue.addToSendQueueSilent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
               }

               int bestSlot = this.bestArmorSlot[i];
               ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(i);
               if (this.checkDelay()) {
                  return;
               }

               if (oldArmor != null) {
                  mc.playerController.windowClick(inventoryId, 8 - i, 0, 4, player);
                  InventoryManagerMod.INV_STOPWATCH.reset();
               }

               int slot = bestSlot < 9 ? bestSlot + 36 : bestSlot;
               if (this.checkDelay()) {
                  return;
               }

               mc.playerController.windowClick(inventoryId, slot, 0, 1, player);
               InventoryManagerMod.INV_STOPWATCH.reset();
               if (this.equipping) {
                  player.sendQueue.addToSendQueueSilent(new C0DPacketCloseWindow(inventoryId));
                  this.equipping = false;
               }
            }
         }
      }

   }

   private boolean checkDelay() {
      return !InventoryManagerMod.INV_STOPWATCH.elapsed(((Double)this.delay.getValue()).longValue());
   }

   private void collectBestArmor() {
      int[] bestArmorDamageReduction = new int[4];
      this.bestArmorSlot = new int[4];
      Arrays.fill(bestArmorDamageReduction, -1);
      Arrays.fill(this.bestArmorSlot, -1);

      int i;
      ItemStack itemStack;
      ItemArmor armor;
      int armorType;
      for(i = 0; i < this.bestArmorSlot.length; ++i) {
         itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
         this.allArmors[i] = new ArrayList();
         if (itemStack != null && itemStack.getItem() != null) {
            armor = (ItemArmor)itemStack.getItem();
            armorType = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
            bestArmorDamageReduction[i] = armorType;
         }
      }

      for(i = 0; i < 36; ++i) {
         itemStack = mc.thePlayer.inventory.getStackInSlot(i);
         if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
            armor = (ItemArmor)itemStack.getItem();
            armorType = 3 - armor.armorType;
            this.allArmors[armorType].add(i);
            int slotProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack}, DamageSource.generic);
            if (bestArmorDamageReduction[armorType] < slotProtectionLevel) {
               bestArmorDamageReduction[armorType] = slotProtectionLevel;
               this.bestArmorSlot[armorType] = i;
            }
         }
      }

   }
}
