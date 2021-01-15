package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.EntityHitEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class ReverseKnockback extends Module {

	public boolean doReverse = false;

	public ReverseKnockback() {
		super("ReverseKnockback", Keyboard.KEY_NONE, Category.HIDDEN, "Reverses knockback stuff");
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
	public void onLateUpdate() {

		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Player", "Others", "Both" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	@Override
	public void onEntityHit(EntityHitEvent entityHitEvent) {
		if (this.currentMode.equals("Others") || this.currentMode.equals("Both")) {
			doReverse = true;
		}
		super.onEntityHit(entityHitEvent);
	}

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		if (this.currentMode.equals("Player") || this.currentMode.equals("Both")) {
			if (packetIn instanceof S12PacketEntityVelocity) {
				S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packetIn;
				Entity entity = mc.getNetHandler().clientWorldController.getEntityByID(velocityPacket.getEntityID());
				if (entity instanceof EntityPlayerSP) {
					entity.setVelocity(-(double) velocityPacket.getMotionX() / 8000.0D,
							-(double) velocityPacket.getMotionY() / 8000.0D,
							-(double) velocityPacket.getMotionZ() / 8000.0D);
					((S12PacketEntityVelocity) packetIn).cancel();
				}
			}
		}

		super.onPacketRecieved(packetIn);
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (doReverse) {
			if (packet instanceof C05PacketPlayerLook) {
				C05PacketPlayerLook playerLook = (C05PacketPlayerLook) packet;
				packet.cancel();
				sendPacketFinal(new C05PacketPlayerLook(playerLook.getYaw() - 180f, playerLook.getPitch(),
						playerLook.isOnGround()));
			}
			if (packet instanceof C06PacketPlayerPosLook) {
				C06PacketPlayerPosLook playerPosLook = (C06PacketPlayerPosLook) packet;
				packet.cancel();
				sendPacketFinal(new C06PacketPlayerPosLook(playerPosLook.getPositionX(), playerPosLook.getPositionY(),
						playerPosLook.getPositionZ(), playerPosLook.getYaw() - 180f, playerPosLook.getPitch(),
						playerPosLook.isOnGround()));
			}
		}
		super.onPacketSent(packet);
	}
}
