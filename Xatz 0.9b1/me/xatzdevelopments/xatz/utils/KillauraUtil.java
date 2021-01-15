package me.xatzdevelopments.xatz.utils;

import java.util.List;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class KillauraUtil {
	

		private static Minecraft mc = Minecraft.getMinecraft();

		public static float[] faceEntitySmooth(double curYaw, double curPitch, double intendedYaw, double intendedPitch,
				double yawSpeed, double pitchSpeed) {
			float yaw = (float) updateRotation((float) curYaw, (float) intendedYaw, (float) yawSpeed);
			float pitch = (float) updateRotation((float) curPitch, (float) intendedPitch, (float) pitchSpeed);
			return new float[] { yaw, pitch };
		}

		/**
		 * Current: Die jetztige Playerroation
		 * Intended: Wo der Spieler hinaimen soll
		 * Factor: Kann sowohl als speed, als auch als maxFOV genutzt werden. In der
		 * Killaura wird beides ausgenutzt
		 */
		public static float updateRotation(float current, float intended, float factor) {
			float var4 = MathHelper.wrapAngleTo180_float(intended - current);

			if (var4 > factor) {
				var4 = factor;
			}

			if (var4 < -factor) {
				var4 = -factor;
			}

			return current + var4;
		}

		public static float[] rotations(Entity target) {
			double x = target.posX - mc.thePlayer.posX;
			double z = target.posZ - mc.thePlayer.posZ;
			double y = target.posY + target.getEyeHeight() * 0.75D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());

			double distance = MathHelper.sqrt_double(x * x + z * z);

			float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
			float pitch = (float) -((Math.atan2(y, distance) * 180.0D / Math.PI));
			return new float[] { yaw, pitch };
		}

		public static boolean checkEntityID(Entity entity) {
			boolean check;
			if (entity.getEntityId() <= 1070000000 && entity.getEntityId() > -1) {
				check = true;
			} else {
				check = false;
			}
			return check;
		}

		public static boolean isInTablist(Entity entity) {
			if (mc.isSingleplayer()) {
				return false;
			}

			for (Object o : mc.getNetHandler().getPlayerInfoMap()) {
				NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
				if (playerInfo.getGameProfile().getName().equalsIgnoreCase(entity.getName())) {
					return true;
				}
			}
			return false;
		}

		// Skidded btw
		public static Entity raycast(double range, Entity entity) {
			Entity var2 = mc.thePlayer;
			Vec3 var9 = entity.getPositionVector().add(new Vec3(0, entity.getEyeHeight(), 0));
			Vec3 var7 = mc.thePlayer.getPositionVector().add(new Vec3(0, mc.thePlayer.getEyeHeight(), 0));
			Vec3 var10 = null;
			float var11 = 1.0F;
			AxisAlignedBB a = mc.thePlayer.getEntityBoundingBox()
					.addCoord(var9.xCoord - var7.xCoord, var9.yCoord - var7.yCoord, var9.zCoord - var7.zCoord)
					.expand(var11, var11, var11);
			List var12 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, a);
			double var13 = range + 0.5;
			Entity b = null;
			for (int var15 = 0; var15 < var12.size(); ++var15) {
				Entity var16 = (Entity) var12.get(var15);

				if (var16.canBeCollidedWith()) {
					float var17 = var16.getCollisionBorderSize();
					AxisAlignedBB var18 = var16.getEntityBoundingBox().expand((double) var17, (double) var17,
							(double) var17);
					MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);

					if (var18.isVecInside(var7)) {
						if (0.0D < var13 || var13 == 0.0D) {
							b = var16;
							var10 = var19 == null ? var7 : var19.hitVec;
							var13 = 0.0D;
						}
					} else if (var19 != null) {
						double var20 = var7.distanceTo(var19.hitVec);

						if (var20 < var13 || var13 == 0.0D) {
							b = var16;
							var10 = var19.hitVec;
							var13 = var20;
						}
					}
				}
			}
			return b;
		}

		public static double getRandomDouble(double min, double max) {
			return ThreadLocalRandom.current().nextDouble(min, max + 1);
		}
	}

