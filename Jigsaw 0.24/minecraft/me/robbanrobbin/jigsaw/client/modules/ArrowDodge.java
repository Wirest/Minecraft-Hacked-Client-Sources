package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class ArrowDodge extends Module {

	EntityArrow arrow;
	WaitTimer timer = new WaitTimer();

	public ArrowDodge() {
		super("ArrowDodge", Keyboard.KEY_NONE, Category.COMBAT,
				"Tries to avoid if " + "an arrow is aimed at you. Note: This works best if the arrow is fired a long distance from you.");
	}

	@Override
	public void onDisable() {
		arrow = null;
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if (!timer.hasTimeElapsed(1000, false)) {
			return;
		}
		for (Entity e : mc.theWorld.loadedEntityList) {
			if (!(e instanceof EntityArrow)) {
				continue;
			}
			arrow = (EntityArrow) e;

			if (arrow.shootingEntity != null && arrow.shootingEntity.isEntityEqual(mc.thePlayer)) {
				continue;
			}
			if (arrow.shootingEntity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) arrow.shootingEntity;
				if (mc.thePlayer.isOnSameTeam(player) && !Jigsaw.getModuleByName("Team").isToggled()) {
					continue;
				}
				if (Jigsaw.getFriendsMananger().isFriend(player) && !Jigsaw.getModuleByName("Friends").isToggled()) {
					continue;
				}
			}
			// MovingObjectPosition rayTrace = arrow.rayTrace(100,
			// mc.timer.elapsedPartialTicks);

			// if(rayTrace == null) {
			// continue;
			// }
			// Atlas.chatMessage(rayTrace.toString());
			// if(rayTrace.getBlockPos() != null) {
			//
			// }
			if (arrow.inGround) {
				continue;
			}
			if (mc.thePlayer.getDistanceSqToEntity(arrow) < 20) {
				doBarrier();
			}
		}
		super.onUpdate();
	}

	@Override
	public void onEnable() {
		arrow = null;
		super.onEnable();
	}

	private void doBarrier() {
		timer.reset();
		Jigsaw.chatMessage("Arrow incoming!!");
		int slot = -1;
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) {
				continue;
			}
			if (stack.getItem() == null) {
				continue;
			}
			if (stack.getItem() instanceof ItemBlock) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		double angleA = Math.toRadians(arrow.rotationYaw);
		Vec3 cVec = new Vec3(mc.thePlayer.posX + Math.cos(angleA) * 0.5, mc.thePlayer.posY,
				mc.thePlayer.posZ - Math.sin(angleA) * 0.7);
		Vec3 cVec2 = new Vec3(mc.thePlayer.posX + Math.cos(angleA) * 1.7, mc.thePlayer.posY,
				mc.thePlayer.posZ - Math.sin(angleA) * 1.7);
		Vec3 vec = new Vec3(mc.thePlayer.posX + Math.cos(angleA) * 1.5, mc.thePlayer.posY,
				mc.thePlayer.posZ - Math.sin(angleA) * 1.5);
		if (!Utils.isBlockPosAir(Utils.getBlockPos(cVec))
				|| !Utils.isBlockPosAir(Utils.getBlockPos(cVec2))) {
			cVec = new Vec3(mc.thePlayer.posX - Math.cos(angleA) * 0.5, mc.thePlayer.posY,
					mc.thePlayer.posZ + Math.sin(angleA) * 0.5);
			cVec2 = new Vec3(mc.thePlayer.posX - Math.cos(angleA) * 1.7, mc.thePlayer.posY,
					mc.thePlayer.posZ + Math.sin(angleA) * 1.7);
			vec = new Vec3(mc.thePlayer.posX - Math.cos(angleA) * 1.5, mc.thePlayer.posY,
					mc.thePlayer.posZ + Math.sin(angleA) * 1.5);
		} else {
			if (Utils.isBlockPosAir(Utils.getBlockPos(vec))) {
				if (Utils.isBlockPosAir(Utils.getBlockPos(vec).down(1))) {
					if (Utils.isBlockPosAir(Utils.getBlockPos(vec).down(2))) {
						vec = new Vec3(mc.thePlayer.posX - Math.cos(angleA) * 1.5, mc.thePlayer.posY,
								mc.thePlayer.posZ + Math.sin(angleA) * 1.5);
					}
				}
			}
		}
		if (!Utils.isBlockPosAir(Utils.getBlockPos(cVec))
				|| !Utils.isBlockPosAir(Utils.getBlockPos(cVec2))) {
			return;
		} else {
			if (Utils.isBlockPosAir(Utils.getBlockPos(vec))) {
				if (Utils.isBlockPosAir(Utils.getBlockPos(vec).down(1))) {
					if (Utils.isBlockPosAir(Utils.getBlockPos(vec).down(2))) {
						return;
					}
				}
			}
		}
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionZ = 0;
		mc.thePlayer.setPosition(vec);
		sendPacketFinal(
				new C03PacketPlayer.C04PacketPlayerPosition(vec.xCoord, vec.yCoord, vec.zCoord, mc.thePlayer.onGround));
	}

}
