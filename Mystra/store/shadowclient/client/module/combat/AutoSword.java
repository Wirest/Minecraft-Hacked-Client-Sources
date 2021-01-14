package store.shadowclient.client.module.combat;

import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword extends Module{
	public AutoSword() {
		super("AutoSword", 0, Category.COMBAT);
	}
	public static void getBestWeapon() {
		float damageModifier = 1;
		int newItem = -1;
		for(int slot = 0; slot < 9; slot++) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[slot];
			if(stack == null) {
				continue;
			}
			if(stack.getItem() instanceof ItemSword) {
				ItemSword is = (ItemSword)stack.getItem();
				float damage = is.getDamageVsEntity();
				if(damage > damageModifier) {
					newItem = slot;
					damageModifier = damage;
				}
			}
			if(newItem > -1) {
				Minecraft.getMinecraft().thePlayer.inventory.currentItem = newItem;
			}
		}
	}
}
