package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Misc.InventoryCleaner;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class AutoArmor extends Module {
   TimeHelper timer = new TimeHelper();
   public static Value openinv;
   public static Value delay;

   static {
      openinv = new Value("AutoArmor", Boolean.TYPE, "Open Inv", false);
      delay = new Value("AutoArmor", Float.TYPE, "Delay", 350.0F, 1.0F, 1000.0F, 50.0F);
   }

   public AutoArmor() {
      super("AutoArmor", Category.Combat, false);
   }

   @EventTarget
   public void onTick(EventTick event) {
      if (this.timer.hasPassed((double)delay.getFloatValue()) && !Minecraft.thePlayer.capabilities.isCreativeMode && (this.mc.currentScreen != null || !openinv.getBooleanValue()) && !(this.mc.currentScreen instanceof GuiChat) && !Crystal.INSTANCE.getMods().get(InventoryCleaner.class).isEnabled()) {
         for(int b = 5; b <= 8; ++b) {
            if (this.equipArmor(b)) {
               this.timer.reset();
               break;
            }
         }

      }
   }

   private boolean equipArmor(int b) {
      int currentProtection = -1;
      byte slot = -1;
      ItemArmor current = null;
      if (Minecraft.thePlayer.inventoryContainer.getSlot(b).getStack() != null && Minecraft.thePlayer.inventoryContainer.getSlot(b).getStack().getItem() instanceof ItemArmor) {
         current = (ItemArmor)Minecraft.thePlayer.inventoryContainer.getSlot(b).getStack().getItem();
         currentProtection = current.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, Minecraft.thePlayer.inventoryContainer.getSlot(b).getStack());
      }

      for(byte i = 9; i <= 44; ++i) {
         ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            int armorProtection = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            if (this.checkArmor(armor, b) && (current == null || currentProtection < armorProtection)) {
               currentProtection = armorProtection;
               current = armor;
               slot = i;
            }
         }
      }

      if (slot != -1) {
         boolean isNull = Minecraft.thePlayer.inventoryContainer.getSlot(b).getStack() == null;
         if (!isNull) {
            this.dropSlot(b);
            return true;
         } else {
            this.clickSlot(slot, 0, true);
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean checkArmor(ItemArmor item, int b) {
      return b == 5 && item.getUnlocalizedName().startsWith("item.helmet") || b == 6 && item.getUnlocalizedName().startsWith("item.chestplate") || b == 7 && item.getUnlocalizedName().startsWith("item.leggings") || b == 8 && item.getUnlocalizedName().startsWith("item.boots");
   }

   private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
      this.mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, Minecraft.thePlayer);
   }

   private void dropSlot(int slot) {
      this.mc.playerController.windowClick(0, slot, 1, 4, Minecraft.thePlayer);
   }
}
