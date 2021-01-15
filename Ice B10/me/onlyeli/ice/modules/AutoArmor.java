package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.eventapi.EventTarget;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventUpdate;
import me.onlyeli.ice.utils.TimeHelper;
import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {

	public AutoArmor() {
		super("AutoArmor", Keyboard.KEY_NONE, Category.COMBAT);
	}

	private TimeHelper timer = new TimeHelper();
	private int[] bestArmor;

	@Override
	public void onEnable() {
		this.timer = new TimeHelper();
		EventManager.register(this);
	}

	@Override
	public void onDisable() {
		EventManager.unregister(this);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (Wrapper.mc.thePlayer.capabilities.isCreativeMode || (Wrapper.mc.currentScreen instanceof GuiContainer
				&& !(Wrapper.mc.currentScreen instanceof GuiInventory))) {
			return;
		}
		if (this.timer.hasPassed(250.0)) {
			this.bestArmor = new int[4];
			for (int i = 0; i < this.bestArmor.length; ++i) {
				this.bestArmor[i] = -1;
			}
			for (int i = 0; i < 36; ++i) {
				final ItemStack itemstack = Wrapper.mc.thePlayer.inventory.getStackInSlot(i);
				if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
					final ItemArmor armor = (ItemArmor) itemstack.getItem();
					if (armor.damageReduceAmount > this.bestArmor[3 - armor.armorType]) {
						this.bestArmor[3 - armor.armorType] = i;
					}
				}
			}
			for (int i = 0; i < 4; ++i) {
				final ItemStack itemstack = Wrapper.mc.thePlayer.inventory.armorItemInSlot(i);
				ItemArmor currentArmor;
				if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
					currentArmor = (ItemArmor) itemstack.getItem();
				} else {
					currentArmor = null;
				}
				ItemArmor bestArmor;
				try {
					bestArmor = (ItemArmor) Wrapper.mc.thePlayer.inventory.getStackInSlot(this.bestArmor[i]).getItem();
				} catch (Exception e) {
					bestArmor = null;
				}
				if (bestArmor != null
						&& (currentArmor == null || bestArmor.damageReduceAmount > currentArmor.damageReduceAmount)
						&& (Wrapper.mc.thePlayer.inventory.getFirstEmptyStack() != -1 || currentArmor == null)) {
					Wrapper.mc.playerController.windowClick(0, 8 - i, 0, 1, Wrapper.mc.thePlayer);
					Wrapper.mc.playerController.windowClick(0,
							(this.bestArmor[i] < 9) ? (36 + this.bestArmor[i]) : this.bestArmor[i], 0, 1,
							Minecraft.getMinecraft().thePlayer);
				}
			}
			this.timer.reset();
		}
	}

}
