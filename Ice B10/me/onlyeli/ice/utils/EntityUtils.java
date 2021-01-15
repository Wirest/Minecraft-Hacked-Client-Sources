package me.onlyeli.ice.utils;

import me.onlyeli.ice.main.Ice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class EntityUtils {

	public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
		double curX = startPos[0];
		double curY = startPos[1];
		double curZ = startPos[2];
		double endX = endPos.getX();
		double endY = endPos.getY();
		double endZ = endPos.getZ();
		double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
		int count = 0;
		while (distance > slack) {
			distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
			if (count > 120) {
				break;
			}
			boolean next = false;
			double diffX = curX - endX;
			double diffY = curY - endY;
			double diffZ = curZ - endZ;
			double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
			if (diffX < 0.0) {
				if (Math.abs(diffX) > offset) {
					curX += offset;
				} else {
					curX += Math.abs(diffX);
				}
			}
			if (diffX > 0.0) {
				if (Math.abs(diffX) > offset) {
					curX -= offset;
				} else {
					curX -= Math.abs(diffX);
				}
			}
			if (diffY < 0.0) {
				if (Math.abs(diffY) > 0.25) {
					curY += 0.25;
				} else {
					curY += Math.abs(diffY);
				}
			}
			if (diffY > 0.0) {
				if (Math.abs(diffY) > 0.25) {
					curY -= 0.25;
				} else {
					curY -= Math.abs(diffY);
				}
			}
			if (diffZ < 0.0) {
				if (Math.abs(diffZ) > offset) {
					curZ += offset;
				} else {
					curZ += Math.abs(diffZ);
				}
			}
			if (diffZ > 0.0) {
				if (Math.abs(diffZ) > offset) {
					curZ -= offset;
				} else {
					curZ -= Math.abs(diffZ);
				}
			}
			NetUtils.sendPacket(new C04PacketPlayerPosition(curX, curY, curZ, true));
			++count;
		}
	}

	public static void critical() {
		double posY = Minecraft.getMinecraft().thePlayer.posY;
		NetUtils.sendPacket(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY + 0.0625,
				Minecraft.getMinecraft().thePlayer.posZ, true));
		NetUtils.sendPacket(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY,
				Minecraft.getMinecraft().thePlayer.posZ, false));
		Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY + 1.1E-5,
						Minecraft.getMinecraft().thePlayer.posZ, false));
		NetUtils.sendPacket(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY,
				Minecraft.getMinecraft().thePlayer.posZ, false));
	}

	public static void attackEntity(EntityLivingBase entity, boolean crit) {
		Minecraft.getMinecraft().thePlayer.swingItem();
		if (crit) {
			EntityUtils.critical();
		}
		float sharpLevel = EnchantmentHelper.func_152377_a(Minecraft.getMinecraft().thePlayer.getHeldItem(),
				entity.getCreatureAttribute());
		boolean vanillaCrit = Minecraft.getMinecraft().thePlayer.fallDistance > 0.0f
				&& !Minecraft.getMinecraft().thePlayer.onGround && !Minecraft.getMinecraft().thePlayer.isOnLadder()
				&& !Minecraft.getMinecraft().thePlayer.isInWater()
				&& !Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.blindness)
				&& Minecraft.getMinecraft().thePlayer.ridingEntity == null;
		NetUtils.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
		if (crit || vanillaCrit) {
			Minecraft.getMinecraft().thePlayer.onCriticalHit(entity);
		}
		if (sharpLevel > 0.0f) {
			Minecraft.getMinecraft().thePlayer.onEnchantmentCritical(entity);
		}
	}

	public static void damagePlayer(int damage) {
		if (damage < 1) {
			damage = 1;
		}
		if (damage > MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getMaxHealth())) {
			damage = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getMaxHealth());
		}
		double offset = 0.0625;
		if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().getNetHandler() != null
				&& Minecraft.getMinecraft().thePlayer.onGround) {
			for (int i = 0; i <= (3 + damage) / 0.0625; ++i) {
				NetUtils.sendPacket(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX,
						Minecraft.getMinecraft().thePlayer.posY + 0.0625, Minecraft.getMinecraft().thePlayer.posZ,
						false));
				NetUtils.sendPacket(new C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX,
						Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ,
						i == (3 + damage) / 0.0625));
			}
		}
	}

	public static EntityLivingBase getClosestEntity() {
		EntityLivingBase closestEntity = null;
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (o instanceof EntityLivingBase) {
				EntityLivingBase en = (EntityLivingBase) o;
				//if (o instanceof EntityPlayerSP || en.isDead || en.getHealth() <= 0.0f
						//|| en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())
						//|| Euphoria.getEuphoria().friendUtils.isFriend(en.getName()) || !ModeUtils.isValidForAura(en)) {
					continue;
				}
				//if (closestEntity != null && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(
				//		en) >= Minecraft.getMinecraft().thePlayer.getDistanceToEntity(closestEntity)) {
					continue;
				}
				//closestEntity = en;
	//		}
	//	}
		return closestEntity;
	}
}
