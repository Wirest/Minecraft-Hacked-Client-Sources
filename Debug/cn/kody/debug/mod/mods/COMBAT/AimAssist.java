package cn.kody.debug.mod.mods.COMBAT;

import java.util.List;
import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import optifine.Reflector;

public class AimAssist extends Mod {
	private static Minecraft mc = Minecraft.getMinecraft();
	protected Random rand = new Random();
	public static Value<Double> Horizontal = new Value("AimAssist_Horizontal", 4.2d, 0.0d, 10.0d, 0.1d);
	public static Value<Double> Vertical = new Value("AimAssist_Vertical", 2.4d, 0.0d, 10.0d, 0.1d);
	public static Value<Double> Speed = new Value("AimAssist_Speed", 0.2d, 0.0d, 1.0d, 0.01d);
	public static Value<Double> Range = new Value("AimAssist_AARange", 4.2d, 1.0d, 8.1d, 0.1d);
	public static Value<Double> anglemin = new Value("AimAssist_AngleMin", 0.0d, 0.0d, 1.0d, 1.0d);
	public static Value<Double> anglemax = new Value("AimAssist_AngleMax", 100.0d, 20.0d, 360.0d, 1.0d);
	public Value<Boolean> ClickAim = new Value<Boolean>("AimAssist_ClickAim", false);
	public Value<Boolean> Strafe = new Value<Boolean>("AimAssist_StrafeIncrease", false);
	public Value<Boolean> team = new Value<Boolean>("AimAssist_Team", true);
	public Value<Boolean> held = new Value<Boolean>("AimAssist_HeldItem", false);

	private float[] lastRotations = new float[2];
	private int rotationSpeed = 1;

	public AimAssist() {
		super("AimAssist","Aim Assist",  Category.COMBAT);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {

		if (ClickAim.getValueState() && !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
			return;
		}
		if (mc.thePlayer.getHeldItem() == null && held.getValueState()) {
			return;
		}
		Entity entity = null;
		double maxDistance = 360.0;
		double maxAngle = anglemax.getValueState();
		double minAngle = anglemin.getValueState();
		for (Object e : this.mc.theWorld.getLoadedEntityList()) {
			double yawdistance;
			float yaw;
			Entity en = (Entity) e;
			if (en == this.mc.thePlayer || !this.isValid(en)
					|| !(maxDistance > (yawdistance = getDistanceBetweenAngles(yaw = getAngles(en)[1],
							this.mc.thePlayer.rotationYaw))))
				continue;
			entity = en;
			maxDistance = yawdistance;
		}
		if (entity != null) {
			float yaw = getAngles(entity)[1];
			float pitch = getAngles(entity)[0];
			double yawdistance = getDistanceBetweenAngles(yaw, this.mc.thePlayer.rotationYaw);
			double pitchdistance = getDistanceBetweenAngles(pitch, this.mc.thePlayer.rotationPitch);
			if (pitchdistance <= maxAngle && yawdistance >= minAngle && yawdistance <= maxAngle) {
				double horizontalSpeed = Horizontal.getValueState() * 3.0
						+ (Horizontal.getValueState() > 0.0 ? this.rand.nextDouble() : 0.0);
				double verticalSpeed = Vertical.getValueState() * 3.0
						+ (Vertical.getValueState() > 0.0 ? this.rand.nextDouble() : 0.0);
				if (Strafe.getValueState() && this.mc.thePlayer.moveStrafing != 0.0f) {
					horizontalSpeed *= 1.25;
				}
				if (getEntity(24.0) != null && getEntity(24.0).equals(entity)) {
					horizontalSpeed *= Speed.getValueState();
					verticalSpeed *= Speed.getValueState();
				}
				this.faceTarget(entity, 0.0f, (float) verticalSpeed);
				this.faceTarget(entity, (float) horizontalSpeed, 0.0f);
			}
		}
	}

	protected float getRotation(float currentRotation, float targetRotation, float maxIncrement) {
		float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
		if (deltaAngle > maxIncrement) {
			deltaAngle = maxIncrement;
		}
		if (deltaAngle < -maxIncrement) {
			deltaAngle = -maxIncrement;
		}
		return currentRotation + deltaAngle / 2.0f;
	}

	private void faceTarget(Entity target, float yawspeed, float pitchspeed) {
		EntityPlayerSP player = this.mc.thePlayer;
		float yaw = getAngles(target)[1];
		float pitch = getAngles(target)[0];
		player.rotationYaw = this.getRotation(player.rotationYaw, yaw, yawspeed);
		player.rotationPitch = this.getRotation(player.rotationPitch, pitch, pitchspeed);
	}

	public static float[] getAngles(Entity entity) {
		double x = entity.posX - mc.thePlayer.posX;
		double z = entity.posZ - mc.thePlayer.posZ;
		double y = entity instanceof EntityEnderman ? entity.posY - mc.thePlayer.posY
				: entity.posY + ((double) entity.getEyeHeight() - 1.9) - mc.thePlayer.posY
						+ ((double) mc.thePlayer.getEyeHeight() - 1.9);
		double helper = MathHelper.sqrt_double(x * x + z * z);
		float newYaw = (float) Math.toDegrees(-Math.atan(x / z));
		float newPitch = (float) (-Math.toDegrees(Math.atan(y / helper)));
		if (z < 0.0 && x < 0.0) {
			newYaw = (float) (90.0 + Math.toDegrees(Math.atan(z / x)));
		} else if (z < 0.0 && x > 0.0) {
			newYaw = (float) (-90.0 + Math.toDegrees(Math.atan(z / x)));
		}
		return new float[] { newPitch, newYaw };
	}

	public static double getDistanceBetweenAngles(float angle1, float angle2) {
		float distance = Math.abs(angle1 - angle2) % 360.0f;
		if (distance > 180.0f) {
			distance = 360.0f - distance;
		}
		return distance;
	}

	public static Object[] getEntity(double distance, double expand, float partialTicks) {
		Entity var2 = mc.getRenderViewEntity();
		Entity entity = null;
		if (var2 != null && mc.theWorld != null) {
			double var3;
			mc.mcProfiler.startSection("pick");
			double var5 = var3 = distance;
			Vec3 var7 = var2.getPositionEyes(0.0f);
			Vec3 var8 = var2.getLook(0.0f);
			Vec3 var9 = var7.addVector(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3);
			Vec3 var10 = null;
			float var11 = 1.0f;
			List var12 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox()
					.addCoord(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3).expand(var11, var11, var11));
			double var13 = var5;
			for (int var15 = 0; var15 < var12.size(); ++var15) {
				double var20;
				Entity var16 = (Entity) var12.get(var15);
				if (!var16.canBeCollidedWith())
					continue;
				float var17 = var16.getCollisionBorderSize();
				AxisAlignedBB var18 = var16.getEntityBoundingBox().expand(var17, var17, var17);
				var18 = var18.expand(expand, expand, expand);
				MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
				if (var18.isVecInside(var7)) {
					if (!(0.0 < var13) && var13 != 0.0)
						continue;
					entity = var16;
					var10 = var19 == null ? var7 : var19.hitVec;
					var13 = 0.0;
					continue;
				}
				if (var19 == null || !((var20 = var7.distanceTo(var19.hitVec)) < var13) && var13 != 0.0)
					continue;
				boolean canRiderInteract = false;
				if (Reflector.ForgeEntity_canRiderInteract.exists()) {
					canRiderInteract = Reflector.callBoolean(var16, Reflector.ForgeEntity_canRiderInteract,
							new Object[0]);
				}
				if (var16 == var2.ridingEntity && !canRiderInteract) {
					if (var13 != 0.0)
						continue;
					entity = var16;
					var10 = var19.hitVec;
					continue;
				}
				entity = var16;
				var10 = var19.hitVec;
				var13 = var20;
			}
			if (var13 < var5 && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
				entity = null;
			}
			mc.mcProfiler.endSection();
			if (entity == null || var10 == null) {
				return null;
			}
			return new Object[] { entity, var10 };
		}
		return null;
	}

	public static Entity getEntity(double distance) {
		if (getEntity(distance, 0.0, 0.0f) == null) {
			return null;
		}
		return (Entity) getEntity(distance, 0.0, 0.0f)[0];
	}

	public boolean isValid(Entity e) {
		return !(e instanceof EntityLivingBase) ? false
				: (e instanceof EntityArmorStand ? false
						: (e instanceof EntityAnimal ? false
								: (e instanceof EntityMob ? false
										: (e == mc.thePlayer ? false
												: (e instanceof EntityVillager ? false
														: ((double) mc.thePlayer.getDistanceToEntity(e) > (Range
																.getValueState())
																		? false
																		: (e.getName().contains("#") ? false
																				: (team.getValueState() && e
																						.getDisplayName()
																						.getFormattedText().startsWith(
																								"\u00a7" + mc.thePlayer
																										.getDisplayName()
																										.getFormattedText()
																										.charAt(1))
																												? false
																												: !e.getName()
																														.toLowerCase()
																														.contains(
																																"shop")))))))));
	}
}
