package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoEat extends Module {

	int slotBefore;
	int bestSlot;
	int eating;

	public AutoEat() {
		super("AutoEat", Keyboard.KEY_NONE, Category.PLAYER, "Automatically eats when you can't regenerate anymore.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		slotBefore = -1;
		bestSlot = -1;
		eating = 41;

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		if (eating < 41) {
			eating++;
			if (eating <= 1) {
				mc.thePlayer.inventory.currentItem = bestSlot;
			}
			mc.gameSettings.keyBindUseItem.pressed = true;
			if (eating >= 38) {
				mc.gameSettings.keyBindUseItem.unpressKey();
				if (slotBefore != -1) {
					mc.thePlayer.inventory.currentItem = slotBefore;
				}
				slotBefore = -1;
			}
			return;
		}

		float bestRestoration = 0;
		bestSlot = -1;
		// Loop through hotbar
		for (int i = 0; i < 9; i++) {
			ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
			if (item == null) {
				continue;
			}
			float restoration = 0;

			if (item.getItem() instanceof ItemFood) {
				restoration = ((ItemFood) item.getItem()).getSaturationModifier(item);
			}

			if (restoration > bestRestoration) {
				bestRestoration = restoration;
				bestSlot = i;
			}
		}
		if (bestSlot == -1) {
			return;
		}
		if ((bestSlot == -1) || !(this.mc.thePlayer.getFoodStats().getFoodLevel() < 18)) {
			return;
		}
		slotBefore = mc.thePlayer.inventory.currentItem;
		if (slotBefore == -1) {
			return;
		}
		if (Jigsaw.getModuleByName("FastEat").isToggled()
				&& !Jigsaw.getModuleByName("FastEat").getCurrentMode().equals("NCP")) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bestSlot));
			this.mc.thePlayer.sendQueue.addToSendQueue(
					new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.mainInventory[bestSlot]));
			for (int i = 0; i < 32; i++) {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			}
			this.mc.thePlayer.stopUsingItem();
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
			return;
		}
		eating = 0;

		super.onUpdate();
	}

}
