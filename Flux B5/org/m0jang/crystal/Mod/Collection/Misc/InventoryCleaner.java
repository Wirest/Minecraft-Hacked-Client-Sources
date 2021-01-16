package org.m0jang.crystal.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;

public class InventoryCleaner extends Module {
   private int currentSlot = 9;
   private TimeHelper timeHelper = new TimeHelper();
   private double handitemAttackValue;

   public InventoryCleaner() {
      super("InventoryCleaner", Category.Misc, false);
   }

   public void onEnable() {
      super.onEnable();
      this.currentSlot = 9;
      this.handitemAttackValue = getAttackDamage(Minecraft.thePlayer.getHeldItem());
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventRender3D event) {
      if (this.isEnabled()) {
         if (this.currentSlot >= 45) {
            this.setEnabled(false);
         } else if (this.timeHelper.hasPassed(130.0D)) {
            this.handitemAttackValue = getAttackDamage(Minecraft.thePlayer.getHeldItem());
            ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(this.currentSlot).getStack();
            if (isShit(itemStack) && getAttackDamage(itemStack) <= this.handitemAttackValue && itemStack != Minecraft.thePlayer.getHeldItem()) {
               this.mc.playerController.windowClick(0, this.currentSlot, 1, 4, Minecraft.thePlayer);
               this.timeHelper.reset();
            }

            ++this.currentSlot;
         }
      }
   }

   public static boolean isShit(ItemStack itemStack) {
      if (itemStack == null) {
         return false;
      } else if (itemStack.getItem().getUnlocalizedName().contains("bow")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("arrow")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("stick")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("egg")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("flower pot")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("stick")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("string")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("flint")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("compass")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("feather")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("bucket")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("chest")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("snow")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("fish")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("enchant")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("exp")) {
         return true;
      } else if (itemStack.getItem().getUnlocalizedName().contains("tnt")) {
         return true;
      } else if (itemStack.getItem() instanceof ItemPickaxe) {
         return true;
      } else if (itemStack.getItem() instanceof ItemTool) {
         return true;
      } else if (itemStack.getItem() instanceof ItemArmor) {
         return true;
      } else if (itemStack.getItem() instanceof ItemSword) {
         return true;
      } else {
         return itemStack.getItem().getUnlocalizedName().contains("potion") && isBadPotion(itemStack);
      }
   }

   public static boolean isBadPotion(ItemStack stack) {
      if (stack != null && stack.getItem() instanceof ItemPotion) {
         ItemPotion potion = (ItemPotion)stack.getItem();
         Iterator var3 = potion.getEffects(stack).iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            PotionEffect effect = (PotionEffect)o;
            if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.harm.getId()) {
               return true;
            }
         }
      }

      return false;
   }

   private static double getAttackDamage(ItemStack itemStack) {
      if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
         ItemSword sword = (ItemSword)itemStack.getItem();
         return (double)((float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) + sword.getAttackDamage());
      } else {
         return 0.0D;
      }
   }
}
