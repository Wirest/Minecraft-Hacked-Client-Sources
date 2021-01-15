package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.Vec3;

public class BowAimbot extends Module {

	private static Entity en;
	private static double sideMultiplier;
	private static double upMultiplier;
	private static double upPredict;
	public static boolean isValid;
	private static Vec3 toFace = null;

	public BowAimbot() {
		super("BowAimbot", Keyboard.KEY_NONE, Category.COMBAT,
				"Tries to aim your bow when you use it. ");
	}

	@Override
	public void onDisable() {

		isValid = false;

		super.onDisable();
	}

	@Override
	public void onEnable() {
		toFace = null;
		isValid = false;

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		en = null;
		if (!(mc.currentScreen == null)) {
			isValid = false;
			return;
		}
		try {
			if (mc.thePlayer.getCurrentEquippedItem() == null) {
				isValid = false;
				return;
			}
			if (mc.thePlayer.getCurrentEquippedItem().getItem() == null) {
				isValid = false;
				return;
			}
			if (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) {
				isValid = false;
				return;
			}
			if (!(mc.thePlayer.isUsingItem())) {
				isValid = false;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isValid = false;
			return;
		}

		ArrayList<EntityLivingBase> ens = Utils.getClosestEntities(100);
		double minDistance = 999;
		for (EntityLivingBase en : ens) {
			if (mc.theWorld.rayTraceBlocks(
					new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
					new Vec3(en.posX, en.posY + en.getEyeHeight(), en.posZ), false, true, false) != null) {
				continue;
			}
			if (mc.thePlayer.getDistanceToEntity(en) < minDistance) {
				minDistance = mc.thePlayer.getDistanceToEntity(en);
				this.en = en;
			}
		}
		if (en == null) {
			isValid = false;
			return;
		}
		if (mc.theWorld.rayTraceBlocks(
				new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
				new Vec3(en.posX, en.posY + en.getEyeHeight(), en.posZ), false, true, false) != null) {
			en = null;
			isValid = false;
		}
		if (en == null) {
			isValid = false;
			return;
		}
		isValid = true;
		sideMultiplier = mc.thePlayer.getDistanceToEntity(en) / ((mc.thePlayer.getDistanceToEntity(en) / 2) / 1) * 5;
		upMultiplier = (mc.thePlayer.getDistanceSqToEntity(en) / 320) * 1.1;
		upPredict = 5;
		generateToFace();
		if (this.currentMode.equalsIgnoreCase("Client")) {
			if (ClientSettings.smoothAim) {
				Utils.smoothFacePos(toFace, 2);
			} else {
				Utils.facePos(toFace);
			}
		}
		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Client", "Packet" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	private static void generateToFace() {
		double xPos = en.posX;
		double yPos = en.posY;
		double zPos = en.posZ;
		toFace = new Vec3((xPos - 0.5) + (xPos - en.lastTickPosX) * sideMultiplier,
				yPos + upMultiplier,
				(zPos - 0.5) + (zPos - en.lastTickPosZ) * sideMultiplier);
	}

	public static boolean getShouldChangePackets() {
		if(!isValid) {
			return false;
		}
		if (!(mc.currentScreen == null)) {
			return false;
		}
		if (mc.thePlayer.getCurrentEquippedItem() == null) {
			return false;
		}
		if (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) {
			return false;
		}
		if (!(mc.thePlayer.isUsingItem())) {
			return false;
		}
		if (Jigsaw.getModuleByName("BowAimbot").getCurrentMode().equals("Client")) {
			return false;
		}
		return true;
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (getShouldChangePackets() && toFace != null) {
			if (packet instanceof C03PacketPlayer) {
				packet.cancel();
				C06PacketPlayerPosLook playerPacket = new C06PacketPlayerPosLook();
				playerPacket.rotating = true;
				playerPacket.moving = true;
				playerPacket.x = mc.thePlayer.posX;
				playerPacket.y = mc.thePlayer.posY;
				playerPacket.z = mc.thePlayer.posZ;
				playerPacket.onGround = mc.thePlayer.onGround;
				float[] rots = Utils.getFacePos(toFace);
				playerPacket.yaw = rots[0];
				playerPacket.pitch = rots[1];
				sendPacketFinal(playerPacket);
			}
		}
		super.onPacketSent(packet);

	}

}
