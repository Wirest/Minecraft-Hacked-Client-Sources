package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPotion extends Module {

	private static boolean doThrow;
	int slot;
	private WaitTimer timer = new WaitTimer();

	public AutoPotion() {
		super("AutoPotion", Keyboard.KEY_NONE, Category.COMBAT, "Throws potions automatically. Note: "
				+ "Use AntiKnockback with this so that you don't get pushed away from the potion.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (!(mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() / 2.7)
				|| mc.thePlayer.capabilities.isCreativeMode) {
			return;
		}
		if (!timer.hasTimeElapsed(500, false)) {
			return;
		}
		if((KillAura.timer.hasTimeElapsed(1000 / (ClientSettings.KillauraAPS), false) && Jigsaw.getModuleByName("KillAura").isToggled())) {
			return;
		}
		slot = -1;
		boolean hasPots = false;
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			if (stack.getItem() == null) {
				continue;
			}
			if (stack.getItem() instanceof ItemPotion) {
				ItemPotion potion = (ItemPotion) stack.getItem();
				if (isValidPotion(potion, stack)) {
					hasPots = true;
				}
			}
		}
		if (!hasPots) {
			for (int i = 9; i < 4 * 9; i++) {
				ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (stack == null || stack.getItem() == null) {
					continue;
				}
				if (stack.getItem() instanceof ItemPotion && (isValidPotion(stack))) {
					mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 1, 2, mc.thePlayer);
					break;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			if (stack.getItem() == null) {
				continue;
			}
			if (stack.getItem() instanceof ItemPotion) {
				ItemPotion potion = (ItemPotion) stack.getItem();
				if (isValidPotion(potion, stack)) {
					slot = i;
					break;
				}
			}
		}
		if (slot == -1) {
			return;
		}
		timer.reset();
		sendPacket(new C09PacketHeldItemChange(slot));
		event.pitch = 85f;
		event.autopot = true;
		doThrow = true;
		super.onUpdate();
	}

	@Override
	public void onLateUpdate() {
		if (doThrow) {
			sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(slot)));
			sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
			doThrow = false;
		}
		super.onLateUpdate();
	}

	public boolean isValidPotion(ItemPotion potion, ItemStack stack) {
		if (ItemPotion.isSplash(stack.getItemDamage())) {
			for (PotionEffect effect : potion.getEffects(stack)) {
				if (effect.getPotionID() == Potion.heal.id) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidPotion(ItemStack stack) {
		return isValidPotion((ItemPotion) stack.getItem(), stack);
	}

	public static boolean isPotting() {
		return doThrow;
	}

	@Override
	public String[] getModes() {
		return new String[] { "Ground", "Head" };
	}

}
