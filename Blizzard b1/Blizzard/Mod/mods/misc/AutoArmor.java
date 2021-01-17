package Blizzard.Mod.mods.misc;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Mod {

	private int[] bestArmor;
	int delay;
	private int[] boots;
	private int[] chestplate;
	private int[] helmet;
	private int[] leggings;

	private int getItem(final int id) {
		for (int index = 9; index < 45; ++index) {
			final ItemStack item = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (item != null && Item.getIdFromItem(item.getItem()) == id) {
				return index;
			}
		}
		return -1;
	}

	private int getProt(ItemStack stack) {
		if ((stack != null) && ((stack.getItem() instanceof ItemArmor))) {
			int normalValue = ((ItemArmor) stack.getItem()).damageReduceAmount;
			int enchantmentValue = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
			return normalValue + enchantmentValue;
		}
		return -1;
	}

	public AutoArmor() {
		super("AutoArmor", "AutoArmor", Keyboard.KEY_X, Category.PLAYER);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		this.boots = new int[] { 313, 309, 317, 305, 301 };
		this.chestplate = new int[] { 311, 307, 315, 303, 299 };
		this.helmet = new int[] { 310, 306, 314, 302, 298 };
		this.leggings = new int[] { 312, 308, 316, 304, 300 };

		this.delay = 0;

		if (mc.thePlayer.capabilities.isCreativeMode
				|| mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}
		if (this.delay <= 0) {
			for (int i = 5; i < 9; i++) {
				ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				int value = getProt(stack);
				int type = i - 5;
				int bestSlot = -1;
				int highestValue = 0;
				for (int inv = 9; inv < 45; inv++) {
					ItemStack invStx = this.mc.thePlayer.inventoryContainer.getSlot(inv).getStack();
					if ((invStx != null) && ((invStx.getItem() instanceof ItemArmor))) {
						ItemArmor armour = (ItemArmor) invStx.getItem();
						int armourProtection = armour.damageReduceAmount
								+ EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, invStx);
						if ((armour.armorType == type) && (armourProtection > value)
								&& (armourProtection > highestValue)) {
							highestValue = armourProtection;
							bestSlot = inv;
						}
					}
				}
				if (bestSlot != -1) {
					if (stack == null) {
						this.mc.playerController.windowClick(0, bestSlot, 0, 1, this.mc.thePlayer);
					} else {
						this.mc.playerController.windowClick(0, i, 1, 4, this.mc.thePlayer);
						this.mc.playerController.windowClick(0, bestSlot, 0, 1, this.mc.thePlayer);
					}
					return;
				}
				this.delay = 12;
			}
		} else {
			--this.delay;
		}
	}
}