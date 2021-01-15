package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.AbstractPacket;

public class Blink extends Module {

	private EntityOtherPlayerMP oldPlayer;
	public static ArrayList<AbstractPacket> blinkList = new ArrayList<AbstractPacket>();
	public static boolean disabling = false;

	public Blink() {
		super("Blink", Keyboard.KEY_NONE, Category.MOVEMENT, "Simulates lag. For other players it looks like you are teleporting.");
	}

	@Override
	public void onDisable() {
		mc.theWorld.removeEntityFromWorld(-123);
		oldPlayer = null;
		disabling = true;
		for (AbstractPacket packet : blinkList) {
			sendPacketFinal(packet);
		}
		blinkList.clear();
		super.onDisable();
		disabling = false;
	}

	@Override
	public void onEnable() {

		oldPlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());

		oldPlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

		oldPlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
		oldPlayer.clonePlayer(mc.thePlayer, true);
		oldPlayer.copyLocationAndAnglesFrom(mc.thePlayer);
		oldPlayer.rotationYaw = mc.thePlayer.rotationYaw;

		mc.theWorld.addEntityToWorld(-123, oldPlayer);

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (oldPlayer == null) {
			return;
		}
//		oldPlayer.setSneaking(mc.thePlayer.isSneaking());
//		oldPlayer.setSprinting(mc.thePlayer.isSprinting());
//		oldPlayer.isSwingInProgress = mc.thePlayer.isSwingInProgress;
//		oldPlayer.swingProgress = mc.thePlayer.swingProgress;
//		oldPlayer.swingProgressInt = mc.thePlayer.swingProgressInt;
		super.onUpdate();
	}

	public static boolean isBlinking() {
		return Jigsaw.getModuleByName("Blink").isToggled();
	}

	public static void packet(AbstractPacket packetIn) {
		blinkList.add(packetIn);
	}

}
