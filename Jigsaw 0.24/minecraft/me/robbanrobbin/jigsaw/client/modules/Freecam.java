package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class Freecam extends Module {

	private static C03PacketPlayer position;

	private BlockPos oldBlockPos;

	private EntityOtherPlayerMP oldPlayer;

	public Freecam() {
		super("Freecam", Keyboard.KEY_NONE, Category.WORLD, "Makes you fly out of your body.");
	}

	@Override
	public void onDisable() {
		mc.thePlayer.setPosition(oldBlockPos.getX(), oldBlockPos.getY(), oldBlockPos.getZ());
		mc.thePlayer.noClip = false;

		mc.thePlayer.rotationYawHead = oldPlayer.rotationYawHead;
		mc.thePlayer.rotationPitch = oldPlayer.rotationPitch;

		mc.theWorld.removeEntityFromWorld(-123);
		oldPlayer = null;

		super.onDisable();

	}

	@Override
	public void onEnable() {
		oldBlockPos = new BlockPos(mc.thePlayer.getPosition());

		position = new C03PacketPlayer();

		oldPlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());

		oldPlayer.setPosition(oldBlockPos.getX(), oldBlockPos.getY(), oldBlockPos.getZ());

		oldPlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
		oldPlayer.clonePlayer(mc.thePlayer, true);
		oldPlayer.copyLocationAndAnglesFrom(mc.thePlayer);
		oldPlayer.rotationYaw = mc.thePlayer.rotationYaw;

		mc.theWorld.addEntityToWorld(-123, oldPlayer);

		super.onEnable();

	}

	@Override
	public void onUpdate() {
		Utils.spectator = true;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.jumpMovementFactor = 0.1f;
		mc.thePlayer.onGround = false;
		oldPlayer.setSneaking(mc.thePlayer.isSneaking());
		oldPlayer.setSprinting(mc.thePlayer.isSprinting());
		oldPlayer.isSwingInProgress = mc.thePlayer.isSwingInProgress;
		oldPlayer.swingProgress = mc.thePlayer.swingProgress;
		oldPlayer.swingProgressInt = mc.thePlayer.swingProgressInt;
		if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.thePlayer.motionY += 0.4;
		}
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.thePlayer.motionY += -0.4;
		}
		super.onUpdate();
	}
	
	@Override
	public void onLivingUpdate() {
		mc.thePlayer.noClip = true;
		super.onLivingUpdate();
	}

	public static C03PacketPlayer getPacket() {
		return position;
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C03PacketPlayer) {
			packet.cancel();
			sendPacketFinal(Freecam.getPacket());
		}
		super.onPacketSent(packet);
	}

}
