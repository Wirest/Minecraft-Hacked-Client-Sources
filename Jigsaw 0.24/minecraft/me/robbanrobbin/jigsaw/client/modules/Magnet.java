package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.Vec3;

public class Magnet extends Module {
	WaitTimer timer = new WaitTimer();
	private static Entity item = null;
	boolean perform = false;
	double x;
	double y;
	double z;
	double renderX;
	double renderY;
	double renderZ;
	boolean doVclip = false;
	boolean doTp = false;
	boolean didTp = false;
	boolean overrideTimer = false;

	public Magnet() {
		super("Magnet", Keyboard.KEY_NONE, Category.WORLD,
				"Picks up items and xp up to 9 blocks away." + " The item must be at the same height as you!");
	}

	@Override
	public void onToggle() {
		doVclip = false;
		doTp = false;
		didTp = false;
		item = null;
		AuraUtils.targets.clear();
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		if (!timer.hasTimeElapsed(500, true)) {
			return;
		}
		doVclip = false;
		item = Utils.getClosestItemOrXPOrb((float) 9);
		if (item == null || item.ticksExisted < 10) {
			// Jigsaw.chatMessage("null");
			return;
		}

		double dist = mc.thePlayer.getDistanceToEntity(item);
		if (Math.abs(mc.thePlayer.posY - item.posY) > 0.1) {
			if (!mc.thePlayer.canEntityBeSeen(item)) {
				doTp = false;
				item = null;
				return;
			} else if (dist < 1) {
				doVclip = true;
			}
		} else {
			doTp = true;
			double sub = 0.3;
			double addRange;
			addRange = dist - sub;
			double angleA = Math.toRadians(Utils.normalizeAngle(
					Math.toDegrees(Math.atan2(item.posZ - mc.thePlayer.posZ, item.posX - mc.thePlayer.posX)) - 180));
			double x = mc.thePlayer.posX - Math.cos(angleA) * addRange;
			double y = mc.thePlayer.posY;
			double z = mc.thePlayer.posZ - Math.sin(angleA) * addRange;
			if (mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ),
					new Vec3(x, mc.thePlayer.posY + 0.5, z), false, true, false) != null) {
				doTp = false;
				item = null;
				return;
			}
		}
		perform = true;
		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		boolean changeTp = false;
		if (didTp) {
			if (packet instanceof C03PacketPlayer) {
				packet.cancel();
				C06PacketPlayerPosLook playerPacket = new C06PacketPlayerPosLook();
				playerPacket.rotating = true;
				playerPacket.moving = true;
				x = mc.thePlayer.posX;
				y = mc.thePlayer.posY;
				z = mc.thePlayer.posZ;
				playerPacket.x = x;
				playerPacket.y = y;
				playerPacket.z = z;
				playerPacket.onGround = mc.thePlayer.onGround;
				playerPacket.yaw = mc.thePlayer.rotationYaw;
				playerPacket.pitch = mc.thePlayer.rotationPitch;
				sendPacketFinal(playerPacket);
				changeTp = true;
				// Jigsaw.chatMessage("BACK");
			}
		}
		if (item != null && !didTp && perform && !Jigsaw.doDisablePacketSwitch()) {
			if (packet instanceof C03PacketPlayer) {
				packet.cancel();
				C06PacketPlayerPosLook playerPacket = new C06PacketPlayerPosLook();
				playerPacket.rotating = true;
				playerPacket.moving = true;
				double sub = 0.3;
				double distance = mc.thePlayer.getDistanceToEntity(item);
				if (distance > 1 && doTp) {
					double addRange;
					addRange = distance - sub;
					double angleA = Math.toRadians(Utils.normalizeAngle(
							Math.toDegrees(Math.atan2(item.posZ - mc.thePlayer.posZ, item.posX - mc.thePlayer.posX))
									- 180));
					x = mc.thePlayer.posX - Math.cos(angleA) * addRange;
					if (doVclip) {
						y = item.posY;
					} else {
						y = mc.thePlayer.posY;
					}
					z = mc.thePlayer.posZ - Math.sin(angleA) * addRange;
					renderX = x;
					renderY = y;
					renderZ = z;
					didTp = true;
					overrideTimer = false;
					// Jigsaw.chatMessage("tp");
				} else {
					// Jigsaw.chatMessage("justattack");
					overrideTimer = true; // Just attacking, overriiiiiide timer
											// XD
					x = mc.thePlayer.posX;
					y = mc.thePlayer.posY;
					z = mc.thePlayer.posZ;
					renderX = -1;
					renderY = 1337;
					renderZ = -1;
				}
				playerPacket.x = x;
				playerPacket.y = y;
				playerPacket.z = z;
				playerPacket.onGround = true;
				float[] rots = Utils.getFacePosEntity(item);
				playerPacket.yaw = rots[0];
				playerPacket.pitch = rots[1];
				sendPacketFinal(playerPacket);
			}
		}
		if (changeTp) {
			didTp = false;
		}
		perform = false;
		super.onPacketSent(packet);
	}

	@Override
	public void onRender() {
		if (item != null && renderY != 1337 && Jigsaw.debugMode) {
			drawESP(1, 0.5f, 0.5f, 0.5f, renderX, renderY, renderZ);
		}
		super.onRender();
	}

	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.thePlayer.width / 2, mc.thePlayer.height, red, green,
				blue, alpha);
	}

	public static boolean doBlock() {
		return item != null;
	}
}
