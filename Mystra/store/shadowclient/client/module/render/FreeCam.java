package store.shadowclient.client.module.render;

import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.BlockUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class FreeCam extends Module {

	public FreeCam() {
		super("Freecam", 0, Category.RENDER);
	}

	
	private double oldX, oldY, oldZ;
	private float oldYaw, oldPitch;
	private EntityOtherPlayerMP player;

	public void onEnable() {
		this.mc.thePlayer.noClip = true;
		this.oldX = this.mc.thePlayer.posX;
		this.oldY = this.mc.thePlayer.posY;
		this.oldZ = this.mc.thePlayer.posZ;
		this.oldYaw = this.mc.thePlayer.rotationYaw;
		this.oldPitch = this.mc.thePlayer.rotationPitch;
		EventManager.register(this);
		(player = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile()))
				.clonePlayer(this.mc.thePlayer, true);
		player.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
		player.rotationYawHead = this.mc.thePlayer.rotationYaw;
		player.rotationPitch = this.mc.thePlayer.rotationPitch;
		player.setSneaking(this.mc.thePlayer.isSneaking());
		this.mc.theWorld.addEntityToWorld(-1337, player);
	}

	@Override
	public void onDisable() {
		this.mc.thePlayer.noClip = false;
		this.mc.thePlayer.capabilities.isFlying = false;
		EventManager.unregister(this);
		this.mc.thePlayer.setPositionAndRotation(oldX, oldY, oldZ, oldYaw, oldPitch);
		this.mc.theWorld.removeEntity(player);
	}

	@EventTarget
	public void onPacketSend(EventSendPacket event) {
		if (event.packet instanceof C03PacketPlayer || event.packet instanceof C0BPacketEntityAction
				|| event.packet instanceof C0APacketAnimation || event.packet instanceof C02PacketUseEntity
				|| event.packet instanceof C09PacketHeldItemChange || event.packet instanceof C07PacketPlayerDigging) {
			event.setCancelled(true);
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.mc.thePlayer.noClip = true;
		this.mc.thePlayer.motionX = 0;
		this.mc.thePlayer.motionY = 0;
		this.mc.thePlayer.motionZ = 0;
		this.mc.thePlayer.jumpMovementFactor *= 30;
		if (this.mc.gameSettings.keyBindSneak.pressed) {
			this.mc.thePlayer.motionY -= 0.5;
		} else if (this.mc.gameSettings.keyBindJump.pressed) {
			this.mc.thePlayer.motionY += 0.5;
		}
		double mx2 = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
		double mz2 = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
		double x = this.mc.thePlayer.movementInput.moveForward * 0.3 * mx2
				+ this.mc.thePlayer.movementInput.moveStrafe * 0.3 * mz2;
		double z = this.mc.thePlayer.movementInput.moveForward * 0.3 * mz2
				- this.mc.thePlayer.movementInput.moveStrafe * 0.3 * mx2;
		if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder()
				&& !BlockUtils.isInsideBlock()) {
			this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x * 10, this.mc.thePlayer.posY,
					this.mc.thePlayer.posZ + z * 10);
		}
	}
}