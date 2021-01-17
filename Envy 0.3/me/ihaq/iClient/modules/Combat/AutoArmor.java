package me.ihaq.iClient.modules.Combat;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
	public static boolean active;
	private int[] bestArmor;
	int delay;

	public AutoArmor() {
		super("AutoArmor", Keyboard.KEY_NONE, Category.COMBAT, "");
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {

		if (!this.isToggled()) {
			return;
		}
		if (mc.thePlayer.capabilities.isCreativeMode
				|| mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}

		ItemStack itemstack;

		++this.delay;
		this.bestArmor = new int[4];
		int i = 0;
		while (i < this.bestArmor.length) {
			this.bestArmor[i] = -1;
			++i;
		}
		i = 0;
		while (i < 36) {
			itemstack = mc.thePlayer.inventory.getStackInSlot(i);
			if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemstack.getItem();
				double damageReduceAmountNew = armor.damageReduceAmount;

				if (armor.hasEffect(itemstack)) {
					damageReduceAmountNew += 0.1;
				}
				if (armor.damageReduceAmount > this.bestArmor[3 - armor.armorType]) {
					this.bestArmor[3 - armor.armorType] = i;
				}
			}
			++i;
		}
		i = 0;
		while (i < 4) {

			ItemArmor bestArmor;
			itemstack = mc.thePlayer.inventory.armorItemInSlot(i);
			ItemArmor currentArmor = itemstack != null && itemstack.getItem() instanceof ItemArmor
					? (ItemArmor) itemstack.getItem() : null;
			try {
				bestArmor = (ItemArmor) mc.thePlayer.inventory.getStackInSlot(this.bestArmor[i]).getItem();
			} catch (Exception ex) {
				bestArmor = null;
			}
			if (!(bestArmor == null
					|| currentArmor != null && bestArmor.damageReduceAmount <= currentArmor.damageReduceAmount
					|| mc.thePlayer.inventory.getFirstEmptyStack() == -1 && currentArmor != null || this.delay < 4)) {
				mc.playerController.windowClick(0, 8 - i, 0, 1, mc.thePlayer);
				mc.playerController.windowClick(0, this.bestArmor[i] < 9 ? 36 + this.bestArmor[i] : this.bestArmor[i],
						0, 1, Minecraft.getMinecraft().thePlayer);
				this.delay = 0;
				System.out.println(this.bestArmor[i]);
			}
			++i;
		}

	}
}
