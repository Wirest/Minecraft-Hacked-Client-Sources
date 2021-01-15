package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class MegaKB extends Module {

	public MegaKB() {
		super("MegaKnockback", Keyboard.KEY_NONE, Category.EXPLOITS,
				"Makes other players take a lot of knockback. You have to be touching the other player and hit him for it to work.");
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
	public void onUpdate() {

		super.onUpdate();
	}

	@Override
	public void onLeftClick() {
		Entity en = Utils.getClosestEntity(5);
		if (en == null) {
			return;
		}
		if (!mc.thePlayer.boundingBox.intersectsWith(en.boundingBox)) {
			return;
		}
		for (int i = 0; i < 250; i++) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
		}
		super.onLeftClick();
	}

}
