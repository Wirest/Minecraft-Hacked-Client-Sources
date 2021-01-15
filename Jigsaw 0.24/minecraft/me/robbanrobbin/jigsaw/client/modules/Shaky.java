package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class Shaky extends Module {

	public Shaky() {
		super("Shaky", Keyboard.KEY_NONE, Category.FUN, "Makes you shake to other players");
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
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
			packet = new C04PacketPlayerPosition(mc.thePlayer.posX + rand.nextInt(4) - 2, mc.thePlayer.posY,
					mc.thePlayer.posZ + rand.nextInt(4) - 2, mc.thePlayer.onGround);
		}
		super.onPacketSent(packet);
	}

}
