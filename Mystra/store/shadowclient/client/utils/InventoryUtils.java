package store.shadowclient.client.utils;

import java.util.Iterator;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InventoryUtils {
	
	public static float getDamageLevel(ItemStack stack) {
		if (stack.getItem() instanceof ItemSword) {
			ItemSword sword = (ItemSword)stack.getItem();
	        float sharpness = (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F;
	        float fireAspect = (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1.5F;
	        return sword.getDamageVsEntity() + sharpness + fireAspect;
	    } else {
	        return 0.0F;
	    }
	}
	
	public static boolean isValidItem(ItemStack itemStack) {
		if (itemStack.getDisplayName().startsWith("§a")) {
			return true;
	    } else {
	        return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion && !isBadPotion(itemStack) || itemStack.getItem() instanceof ItemBlock || itemStack.getDisplayName().contains("Play") || itemStack.getDisplayName().contains("Game") || itemStack.getDisplayName().contains("Right Click");
	    }
	}
	
	public static boolean isBadPotion(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			ItemPotion potion = (ItemPotion)stack.getItem();
			if (ItemPotion.isSplash(stack.getItemDamage())) {
	        	Iterator var2 = potion.getEffects(stack).iterator();

	            while(var2.hasNext()) {
	               Object o = var2.next();
	               PotionEffect effect = (PotionEffect)o;
	               if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
	            	   return true;
	               }
	            }
	         }
		}
		return false;
	}
}
