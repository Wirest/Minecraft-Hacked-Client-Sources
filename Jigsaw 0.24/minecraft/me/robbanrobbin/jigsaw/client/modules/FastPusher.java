package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastPusher extends Module {

	public FastPusher() {
		super("FastPusher", Keyboard.KEY_NONE, Category.FUN, "Pushes mobs very far.");
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
		Entity en = Utils.getClosestEntitySkipValidCheck(5);
		if (en == null) {
			return;
		}
		if (!mc.thePlayer.boundingBox.intersectsWith(en.boundingBox)) {
			return;
		}
		for (int i = 0; i < 1000; i++) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
		}
		super.onUpdate();
	}
}
