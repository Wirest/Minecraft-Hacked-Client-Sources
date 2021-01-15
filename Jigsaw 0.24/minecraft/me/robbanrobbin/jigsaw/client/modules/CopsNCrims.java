package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.Vec3;

public class CopsNCrims extends Module {

	public static EntityLivingBase en = null;
	public static boolean firstShot = true;

	public CopsNCrims() {
		super("CopsNCrims", Keyboard.KEY_NONE, Category.MINIGAMES, "Aims for you in cops 'n crims on Hypixel.");
	}

	@Override
	public void onDisable() {
		firstShot = true;
		en = null;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		firstShot = true;
		en = null;
		super.onEnable();
	}

	@Override
	public void onLeftClick() {
		firstShot = true;
		super.onLeftClick();
	}

	@Override
	public void onUpdate() {
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
			return;
		}
		if (mc.theWorld.rayTraceBlocks(
				new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
				new Vec3(en.posX, en.posY + en.getEyeHeight(), en.posZ), false, true, false) != null) {
			en = null;
		}
		super.onUpdate();
	}

	@Override
	public void onRender() {
		if (en == null) {
			return;
		}
		EntityPlayer e = mc.thePlayer;
		double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks);
		double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks);
		double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks);
		double ENxPos = (en.lastTickPosX + (en.posX - en.lastTickPosX) * mc.timer.renderPartialTicks);
		double ENyPos = (en.lastTickPosY + (en.posY - en.lastTickPosY) * mc.timer.renderPartialTicks);
		double ENzPos = (en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * mc.timer.renderPartialTicks);

		RenderTools.draw3DLine(xPos, yPos + e.getEyeHeight(), zPos, ENxPos, ENyPos + en.getEyeHeight(), ENzPos, 1f,
				0.3f, 0.3f, 0.9f, 2);
		super.onRender();
	}

	private float[] getRot(EntityLivingBase en) {
		double xAim = (en.posX - 0.5) + (en.posX - en.lastTickPosX) * 5.5;
		double yAim = en.posY + (en.getEyeHeight());
		double zAim = (en.posZ - 0.5) + (en.posZ - en.lastTickPosZ) * 5.5;
		if (firstShot || this.getCurrentMode().equals("Headshot")) {
			return Utils.getFacePos(new Vec3(xAim, yAim - 0.35, zAim));
		}
		float[] rots = Utils.getFacePos(new Vec3(xAim, yAim - 0.35, zAim));
		Item heldItem = mc.thePlayer.getCurrentEquippedItem().getItem();
		if (heldItem != null) {
			if (heldItem instanceof ItemSpade) {
				rots[1] += 4.2;
			}
			if (heldItem instanceof ItemHoe) {
				rots[1] += 6.5;
			}
		}
		return rots;
	}

	public AbstractPacket createPosLookPacket(C03PacketPlayer.C06PacketPlayerPosLook posLookIn) {
		float[] rots = getRot(en);
		return new C03PacketPlayer.C06PacketPlayerPosLook(posLookIn.getPositionX(), posLookIn.getPositionY(),
				posLookIn.getPositionZ(), rots[0], rots[1], mc.thePlayer.onGround);
	}

	public AbstractPacket createPacket() {
		float[] rots = getRot(en);
		return new C03PacketPlayer.C05PacketPlayerLook(rots[0], rots[1], mc.thePlayer.onGround);
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (en != null) {
			if (packet instanceof C08PacketPlayerBlockPlacement) {
				firstShot = false;
			}
			if (packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
				packet.cancel();
				sendPacketFinal(this.createPacket());
			}
			if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
				packet.cancel();
				sendPacketFinal(this.createPosLookPacket((C06PacketPlayerPosLook) packet));
			}
		}
		super.onPacketRecieved(packet);
	}

	@Override
	public String[] getModes() {
		return new String[] { "Dynamic", "Headshot" };
	}
}
