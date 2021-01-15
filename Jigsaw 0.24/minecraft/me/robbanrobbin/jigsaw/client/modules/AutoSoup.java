package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoSoup extends Module {

	int timer;

	public AutoSoup() {
		super("AutoSoup", Keyboard.KEY_NONE, Category.COMBAT, "Eats soup in your inventory when on low health.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		timer = 0;
		super.onEnable();
	}

	@Override
	public void onLateUpdate() {
		if (timer <= 6) {
			timer++;
			return;
		}
		timer = 6;
		int soupSlot = getSoupFromInventory();
		if (mc.thePlayer.getHealth() <= 10 && (soupSlot != -1)) {
			int prevSlot = this.mc.thePlayer.inventory.currentItem;
			if (soupSlot < 9) {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(soupSlot));
				this.mc.thePlayer.sendQueue.addToSendQueue(
						new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
				this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
				this.mc.playerController.syncCurrentPlayItem();

				this.mc.thePlayer.inventory.currentItem = prevSlot;
			} else {
				swap(soupSlot, this.mc.thePlayer.inventory.currentItem
						+ (this.mc.thePlayer.inventory.currentItem < 8 ? 1 : -1));

				this.mc.thePlayer.sendQueue
						.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem
								+ (this.mc.thePlayer.inventory.currentItem < 8 ? 1 : -1)));
				this.mc.thePlayer.sendQueue.addToSendQueue(
						new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
				this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
			}
		}
		super.onLateUpdate();
	}

	protected void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	private int getSoupFromInventory() {
		for (int i = 0; i < 36; i++) {
			if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
				ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
				Item item = is.getItem();
				if (Item.getIdFromItem(item) == 282) {
					return i;
				}
			}
		}
		return -1;
	}
}
