package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.Vec3;

public class RodAura extends Module {

	private boolean changeItem = false;
	private boolean stop = false;
	private ItemStack stack = null;
	private static double sideMultiplier;
	private static double upMultiplier;
	private static double upPredict;
	WaitTimer timer = new WaitTimer();
	private int index = -1;
	private static EntityLivingBase en;
	private static Vec3 toFace;

	public RodAura() {
		super("RodAura", Keyboard.KEY_NONE, Category.COMBAT, "Tries to hit close players with the fishing rod.");
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
		if (!stop) {
			en = Utils.getClosestEntity(7);
			if (en == null || en.hurtResistantTime > 12) {
				return;
			}
			searchInvetory();
		}
		if (stop && timer.hasTimeElapsed(250, true)) {
			sendPacket(new C08PacketPlayerBlockPlacement(this.stack));
			sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
			stop = false;
		}
		if (changeItem) {
			generateToFace();
			sendPacket(new C09PacketHeldItemChange(index));
			sendPacket(new C08PacketPlayerBlockPlacement(stack));
			changeItem = false;
			stop = true;
		}
		super.onUpdate();
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

	@Override
	public void onLateUpdate() {

		super.onLateUpdate();
	}

	public static boolean getShouldChangePackets() {
		return en != null;
	}

	private static void generateToFace() {
		toFace = new Vec3((en.posX - 0.5) + (en.posX - en.lastTickPosX) * 10, en.posY + (en.getEyeHeight() / 4),
				(en.posZ - 0.5) + (en.posZ - en.lastTickPosZ) * 10);
	}

	public void searchInvetory() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			Item item = stack.getItem();
			if (item == null) {
				continue;
			}
			if (item instanceof ItemFishingRod) {
				changeItem = true;
				this.stack = stack;
				index = i;
				timer.reset();
				break;
			}
		}
	}
}
