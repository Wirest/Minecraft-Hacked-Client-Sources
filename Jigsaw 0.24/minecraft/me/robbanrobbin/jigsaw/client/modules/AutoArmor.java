package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {

	private int[] bestArmor;

	public AutoArmor() {
		super("AutoArmor", Keyboard.KEY_NONE, Category.PLAYER, "Equips armor instantly.");
	}

	@Override
	public void onUpdate() {
		if (mc.thePlayer.capabilities.isCreativeMode
				|| mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}
		bestArmor = new int[4];
		for (int i = 0; i < bestArmor.length; i++)
			bestArmor[i] = -1;
		for (int i = 0; i < 36; i++) {
			ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);
			if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) itemstack.getItem();
				if (armor.damageReduceAmount > bestArmor[3 - armor.armorType])
					bestArmor[3 - armor.armorType] = i;
			}
		}
		for (int i = 0; i < 4; i++) {
			ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(i);
			ItemArmor currentArmor;
			if (itemstack != null && itemstack.getItem() instanceof ItemArmor)
				currentArmor = (ItemArmor) itemstack.getItem();
			else
				currentArmor = null;
			ItemArmor bestArmor;
			try {
				bestArmor = (ItemArmor) mc.thePlayer.inventory.getStackInSlot(this.bestArmor[i]).getItem();
			} catch (Exception e) {
				bestArmor = null;
			}
			if (bestArmor != null
					&& (currentArmor == null || bestArmor.damageReduceAmount > currentArmor.damageReduceAmount))
				if (mc.thePlayer.inventory.getFirstEmptyStack() != -1 || currentArmor == null) {
					mc.playerController.windowClick(0, 8 - i, 0, 1, mc.thePlayer);
					mc.playerController.windowClick(0,
							this.bestArmor[i] < 9 ? 36 + this.bestArmor[i] : this.bestArmor[i], 0, 1,
							Minecraft.getMinecraft().thePlayer);
				}
		}
		super.onUpdate();
	}

}
