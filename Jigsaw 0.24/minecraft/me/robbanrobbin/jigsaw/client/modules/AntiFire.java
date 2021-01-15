package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiFire extends Module {

	public AntiFire() {
		super("AntiFire", Keyboard.KEY_NONE, Category.PLAYER, "Removes fire instantly.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if (!mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.onGround && mc.thePlayer.isBurning()) {
			for (int i = 0; i < 100; i++) {
				sendPacket(new C03PacketPlayer(true));
			}
		}
		super.onUpdate();
	}

}
