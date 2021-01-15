package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.item.ItemFood;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {

	public FastEat() {
		super("FastEat", Keyboard.KEY_NONE, Category.COMBAT, "Eats food faster.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {

		if (currentMode.equals("NCP")) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		if (!(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemFood)) {
			return;
		}
		if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
			for (int i = 0; i < 32; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
			}
		}

		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (!currentMode.equals("NCP")) {
			return;
		}
		if(mc.thePlayer == null) {
			return;
		}
		if(mc.thePlayer.inventory == null) {
			return;
		}
		if (mc.thePlayer.inventory.getCurrentItem() == null) {
			return;
		}
		if (!(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemFood)) {
			return;
		}
		if (mc.gameSettings.keyBindUseItem.isKeyDown() && !KillAura.getShouldChangePackets()) {
			if (packet instanceof C03PacketPlayer) {
				sendPacketFinal(packet);
			}
		}
		super.onPacketSent(packet);
	}

	@Override
	public String[] getModes() {
		return new String[] { "Fast", "NCP" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

}
