// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.hero.settings.Setting;

import com.darkmagician6.eventapi.EventTarget;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.events.MoveEvent;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.utils.MathUtils;
import me.CheerioFX.FusionX.utils.TimeHelper;
import me.CheerioFX.FusionX.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Speed extends Module {
	TimeHelper time;
	ArrayList<String> Modes;
	private double moveSpeed;
	private double lastDist;
	private int stage;
	int counter;
	private int timeState;
	public int state;
	public double moveSpeed3;
	private double lastDist3;
	private int cooldownHops;
	TimeHelper timer;
	private boolean wasOnWater;
	private boolean doTime;
	private double speed;
	private int level;
	private boolean disabling;
	private boolean stopMotionUntilNext;
	private double moveSpeed2;
	private boolean spedUp;
	public static boolean canStep;
	private double lastDist2;
	public static double yOffset;
	private boolean cancel;
	private boolean speedTick;
	private float speedTimer;
	private int timerDelay;

	public Speed() {
		super("Speed", 35, Category.MOVEMENT);
		this.timer = new TimeHelper();
		this.wasOnWater = false;
		this.doTime = true;
		(this.Modes = new ArrayList<String>()).add("Mineplex");
		this.Modes.add("Test");
		this.Modes.add("Hop");
		this.Modes.add("NCP");
		this.Modes.add("BlockAbove");
		this.Modes.add("Hypixel");
		this.Modes.add("Cubecraft");
		this.Modes.add("Mineplex-Old");
		this.time = new TimeHelper();
		this.speed = 6.0;
		this.level = 1;
		this.moveSpeed2 = 0.2873;
		this.speedTimer = 1.3f;
		FusionX.theClient.setmgr.rSetting(new Setting("Mode", this, "Mineplex", this.Modes));
	}

	public String getMode() {
		return FusionX.theClient.setmgr.getSetting(this, "Mode").getValString();
	}

	@Override
	public void onUpdate() {
		if (!this.getState()) {
			return;
		}
		final String options = this.getMode();
		if (Speed.mc.gameSettings.keyBindJump.pressed) {
			return;
		}
		if (options.equalsIgnoreCase("Mineplex-Old")) {
			this.MineplexOld();
		} else if (options.equalsIgnoreCase("Test")) {
			this.GommeHD();
		} else if (options.equalsIgnoreCase("Mineplex")) {
			this.mineplex();
		} else if (options.equalsIgnoreCase("NCP")) {
			if (Speed.mc.thePlayer.moveForward <= 0.0f || Wrapper.isInLiquid()) {
				return;
			}
			if (Speed.mc.thePlayer.onGround) {
				Speed.mc.thePlayer.jump();
			} else {
				Speed.mc.thePlayer.motionY = -0.6;
			}
		} else if (options.equalsIgnoreCase("BlockAbove")) {
			if (Speed.mc.thePlayer.moveForward <= 0.0f || Wrapper.isInLiquid()) {
				return;
			}
			if (Speed.mc.thePlayer.onGround) {
				Speed.mc.thePlayer.jump();
				Speed.mc.thePlayer.motionY = -0.00211;
			}
		}
		super.onUpdate();
	}

	public static double getSpeed() {
		return Math.sqrt(Speed.mc.thePlayer.motionX * Speed.mc.thePlayer.motionX
				+ Speed.mc.thePlayer.motionZ * Speed.mc.thePlayer.motionZ);
	}

	public static float getDirection() {
		float yaw = Speed.mc.thePlayer.rotationYaw;
		final float forward = Speed.mc.thePlayer.moveForward;
		final float strafe = Speed.mc.thePlayer.moveStrafing;
		yaw += ((forward < 0.0f) ? 180 : 0);
		if (strafe < 0.0f) {
			yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
		}
		if (strafe > 0.0f) {
			yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
		}
		return yaw * 0.017453292f;
	}

	public static void setSpeed2(final double speed) {
		Speed.mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
		Speed.mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
	}

	public void mineplex() {
		setSpeed2(getSpeed());
		if (Speed.mc.thePlayer.movementInput.moveStrafe != 0.0f
				|| Speed.mc.thePlayer.movementInput.moveForward > 0.0f) {
			setSpeed2(getSpeed() + 0.0105);
			if (Speed.mc.thePlayer.onGround) {
				Speed.mc.thePlayer.jump();
				Speed.mc.thePlayer.motionY = 0.42;
			}
		}
	}

	private void GommeHD() {
		final BlockPos bp = new BlockPos(Speed.mc.thePlayer.posX, Speed.mc.thePlayer.posY - 1.0,
				Speed.mc.thePlayer.posZ);
		if (!Wrapper.isMoving()) {
			return;
		}
		if (Speed.mc.thePlayer.movementInput.jump) {
			return;
		}
		if (Speed.mc.thePlayer.isCollidedHorizontally) {
			return;
		}
		if (Speed.mc.theWorld.getBlockState(bp).getBlock() == Blocks.air) {
			Speed.mc.thePlayer.motionY = -9.899999618530273;
		}
		if (Speed.mc.thePlayer.onGround) {
			Speed.mc.thePlayer.capabilities.allowFlying = true;
			Speed.mc.thePlayer.capabilities.isFlying = true;
		}
	}

	private void MineplexOld() {
		if (!Wrapper.isMoving()) {
			return;
		}
		if (Speed.mc.gameSettings.keyBindJump.pressed) {
			return;
		}
		if (Speed.mc.thePlayer.onGround) {
			Speed.mc.thePlayer.motionY = 0.4000000059604645;
		}
		Speed.mc.thePlayer.setSprinting(true);
		this.setSpeed(0.8199999868869782);
	}

	public void setSpeed(final double speed) {
		final EntityPlayerSP player = Speed.mc.thePlayer;
		double yaw = player.rotationYaw;
		final boolean isMoving = player.moveForward != 0.0f || player.moveStrafing != 0.0f;
		final boolean isMovingForward = player.moveForward > 0.0f;
		final boolean isMovingBackward = player.moveForward < 0.0f;
		final boolean isMovingRight = player.moveStrafing > 0.0f;
		final boolean isMovingLeft = player.moveStrafing < 0.0f;
		final boolean isMovingSideways = isMovingLeft || isMovingRight;
		final boolean bl;
		final boolean isMovingStraight = bl = (isMovingForward || isMovingBackward);
		if (isMoving) {
			if (isMovingForward && !isMovingSideways) {
				yaw += 0.0;
			} else if (isMovingBackward && !isMovingSideways) {
				yaw += 180.0;
			} else if (isMovingForward && isMovingLeft) {
				yaw += 45.0;
			} else if (isMovingForward) {
				yaw -= 45.0;
			} else if (!isMovingStraight && isMovingLeft) {
				yaw += 90.0;
			} else if (!isMovingStraight && isMovingRight) {
				yaw -= 90.0;
			} else if (isMovingBackward && isMovingLeft) {
				yaw += 135.0;
			} else if (isMovingBackward) {
				yaw -= 135.0;
			}
			yaw = Math.toRadians(yaw);
			player.motionX = -Math.sin(yaw) * speed;
			player.motionZ = Math.cos(yaw) * speed;
		}
	}

	@EventTarget
	public void onMove(final MoveEvent event) {
		if (this.getMode() == "Mineplex-Old" || this.getMode() == "Test") {
			if (Speed.mc.thePlayer.onGround) {
				final Timer timer = Speed.mc.timer;
				Timer.timerSpeed = ((Speed.mc.thePlayer.ticksExisted % 2 == 0) ? 2.0f : 1.0f);
				if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
					event.setX(event.getX() * 1.5);
					event.setZ(event.getZ() * 1.5);
				}
			} else {
				final Timer timer2 = Speed.mc.timer;
				Timer.timerSpeed = 1.0f;
			}
		} else if (this.getMode() == "Hop") {
			this.speedTick = !this.speedTick;
			++this.timerDelay;
			this.timerDelay %= 5;
			if (this.timerDelay != 0) {
				Timer.timerSpeed = 1.0f;
			} else {
				if (Wrapper.isMoving(Wrapper.getPlayer())) {
					Timer.timerSpeed = 32767.0f;
				}
				if (Wrapper.isMoving(Wrapper.getPlayer())) {
					Timer.timerSpeed = this.speedTimer;
					final EntityPlayerSP player = Wrapper.getPlayer();
					player.motionX *= 1.0199999809265137;
					final EntityPlayerSP player2 = Wrapper.getPlayer();
					player2.motionZ *= 1.0199999809265137;
				}
			}
			if (Wrapper.mc.thePlayer.onGround && Wrapper.isMoving(Wrapper.getPlayer())) {
				this.level = 2;
			}
			if (MathUtils.round(Wrapper.mc.thePlayer.posY - (int) Wrapper.mc.thePlayer.posY, 3) == MathUtils
					.round(0.138, 3)) {
				final EntityPlayerSP thePlayer3;
				final EntityPlayerSP thePlayer = thePlayer3 = Wrapper.mc.thePlayer;
				thePlayer3.motionY -= 0.08;
				event.y -= 0.09316090325960147;
				final EntityPlayerSP thePlayer4;
				final EntityPlayerSP thePlayer2 = thePlayer4 = Wrapper.mc.thePlayer;
				thePlayer4.posY -= 0.09316090325960147;
			}
			if (this.level == 1
					&& (Wrapper.mc.thePlayer.moveForward != 0.0f || Wrapper.mc.thePlayer.moveStrafing != 0.0f)) {
				this.level = 2;
				this.moveSpeed2 = 1.35 * this.getBasemoveSpeed2() - 0.01;
			} else if (this.level == 2) {
				this.level = 3;
				Wrapper.mc.thePlayer.motionY = 0.399399995803833;
				event.y = 0.399399995803833;
				this.moveSpeed2 *= 2.149;
			} else if (this.level == 3) {
				this.level = 4;
				final double difference = 0.66 * (this.lastDist2 - this.getBasemoveSpeed2());
				this.moveSpeed2 = this.lastDist2 - difference;
			} else {
				if (Wrapper.mc.theWorld
						.getCollidingBoundingBoxes(Wrapper.mc.thePlayer,
								Wrapper.mc.thePlayer.boundingBox.offset(0.0, Wrapper.mc.thePlayer.motionY, 0.0))
						.size() > 0 || Wrapper.mc.thePlayer.isCollidedVertically) {
					this.level = 1;
				}
				this.moveSpeed2 = this.lastDist2 - this.lastDist2 / 159.0;
			}
			this.moveSpeed2 = Math.max(this.moveSpeed2, this.getBasemoveSpeed2());
			final MovementInput movementInput = Wrapper.mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if (forward == 0.0f && strafe == 0.0f) {
				event.x = 0.0;
				event.z = 0.0;
			} else if (forward != 0.0f) {
				if (strafe >= 1.0f) {
					yaw += ((forward > 0.0f) ? -45 : 45);
					strafe = 0.0f;
				} else if (strafe <= -1.0f) {
					yaw += ((forward > 0.0f) ? 45 : -45);
					strafe = 0.0f;
				}
				if (forward > 0.0f) {
					forward = 1.0f;
				} else if (forward < 0.0f) {
					forward = -1.0f;
				}
			}
			final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
			final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
			final double motionX = forward * this.moveSpeed2 * mx + strafe * this.moveSpeed2 * mz;
			final double motionZ = forward * this.moveSpeed2 * mz - strafe * this.moveSpeed2 * mx;
			event.x = forward * this.moveSpeed2 * mx + strafe * this.moveSpeed2 * mz;
			event.z = forward * this.moveSpeed2 * mz - strafe * this.moveSpeed2 * mx;
			Speed.canStep = true;
			Wrapper.mc.thePlayer.stepHeight = 0.6f;
			if (forward == 0.0f && strafe == 0.0f) {
				event.x = 0.0;
				event.z = 0.0;
			} else {
				boolean collideCheck = false;
				if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Wrapper.mc.thePlayer,
						Wrapper.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
					collideCheck = true;
				}
				if (forward != 0.0f) {
					if (strafe >= 1.0f) {
						yaw += ((forward > 0.0f) ? -45 : 45);
						strafe = 0.0f;
					} else if (strafe <= -1.0f) {
						yaw += ((forward > 0.0f) ? 45 : -45);
						strafe = 0.0f;
					}
					if (forward > 0.0f) {
						forward = 1.0f;
					} else if (forward < 0.0f) {
						forward = -1.0f;
					}
				}
			}
		} else if (this.getMode() == "Hypixel") {
			if (Speed.mc.thePlayer.isInWater()) {
				return;
			}
			if (Speed.mc.thePlayer.moveForward == 0.0f && Speed.mc.thePlayer.moveStrafing == 0.0f
					&& Speed.mc.thePlayer.onGround) {
				this.cooldownHops = 4;
				this.moveSpeed3 *= 1.0600000429153442;
				this.state = 2;
			}
			final double xDist = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
			final double zDist = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
			this.lastDist3 = Math.sqrt(xDist * xDist + zDist * zDist);
			Speed.mc.thePlayer.motionY = -0.1599999964237213;
			if (Speed.mc.thePlayer.isInWater()) {
				this.cooldownHops = 2;
				return;
			}
			if (Speed.mc.thePlayer.isOnLadder() || Speed.mc.thePlayer.isEntityInsideOpaqueBlock()) {
				this.moveSpeed3 = 0.0;
				this.wasOnWater = true;
				return;
			}
			if (this.wasOnWater) {
				this.moveSpeed3 = 0.0;
				this.wasOnWater = false;
				return;
			}
			if (Speed.mc.thePlayer.moveForward == 0.0f && Speed.mc.thePlayer.moveStrafing == 0.0f) {
				if (!Wrapper.isMoving()) {
					this.moveSpeed3 = getSpeed() * 0.064;
				} else {
					this.moveSpeed3 = getSpeed() * 0.04;
				}
				return;
			}
			if (Speed.mc.thePlayer.onGround) {
				Speed.mc.thePlayer.motionY = -0.10000000149011612;
				this.state = 2;
				final Timer timer3 = Speed.mc.timer;
				Timer.timerSpeed = 1.0f;
				++this.timeState;
				if (this.timeState > 4) {
					this.timeState = 0;
				}
				if (this.timer.hasReached(3000L)) {
					this.doTime = !this.doTime;
					this.timer.reset();
				}
				final EntityPlayerSP thePlayer5 = Speed.mc.thePlayer;
				thePlayer5.motionY *= 1.0499999523162842;
			}
			if (this.round(Speed.mc.thePlayer.posY - (int) Speed.mc.thePlayer.posY, 3) == this.round(0.138, 3)) {
				event.y -= 0.09316090325960147;
				final EntityPlayerSP thePlayer6 = Speed.mc.thePlayer;
				thePlayer6.posY -= 0.09316090325960147;
			}
			if (this.state == 1
					&& (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
				this.state = 2;
				this.moveSpeed3 = 1.35 * getSpeed() - 0.01;
			} else if (this.state == 2) {
				this.state = 3;
				if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
					Speed.mc.thePlayer.motionY = 0.4;
					event.y = 0.4;
					if (this.cooldownHops > 0) {
						--this.cooldownHops;
					}
					this.moveSpeed3 *= 2.149;
				}
			} else if (this.state == 3) {
				this.state = 4;
				final double difference2 = 0.66 * (this.lastDist3 - getSpeed());
				this.moveSpeed3 = this.lastDist3 - difference2;
			} else {
				if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer,
						Speed.mc.thePlayer.boundingBox.offset(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() > 0
						|| Speed.mc.thePlayer.isCollidedVertically) {
					this.state = 1;
				}
				this.moveSpeed3 = this.lastDist3 - this.lastDist3 / 159.0;
			}
			this.moveSpeed3 = Math.max(this.moveSpeed3, getSpeed());
			final MovementInput movementInput2 = Speed.mc.thePlayer.movementInput;
			float forward2 = movementInput2.moveForward;
			float strafe2 = movementInput2.moveStrafe;
			float yaw2 = Speed.mc.thePlayer.rotationYaw;
			if (forward2 == 0.0f && strafe2 == 0.0f) {
				event.x = 0.0;
				event.z = 0.0;
			} else if (forward2 != 0.0f) {
				if (strafe2 >= 1.0f) {
					yaw2 += ((forward2 > 0.0f) ? -45 : 45);
					strafe2 = 0.0f;
				} else if (strafe2 <= -1.0f) {
					yaw2 += ((forward2 > 0.0f) ? 45 : -45);
					strafe2 = 0.0f;
				}
				if (forward2 > 0.0f) {
					forward2 = 1.0f;
				} else if (forward2 < 0.0f) {
					forward2 = -1.0f;
				}
			}
			final double mx2 = Math.cos(Math.toRadians(yaw2 + 90.0f));
			final double mz2 = Math.sin(Math.toRadians(yaw2 + 90.0f));
			double motionX2 = forward2 * this.moveSpeed3 * mx2 + strafe2 * this.moveSpeed3 * mz2;
			double motionZ2 = forward2 * this.moveSpeed3 * mz2 - strafe2 * this.moveSpeed3 * mx2;
			if ((Speed.mc.thePlayer.isUsingItem() || Speed.mc.thePlayer.isBlocking())
					&& FusionX.theClient.moduleManager.getModule(NoSlowDown.class).getState()) {
				motionX2 *= 0.4000000059604645;
				motionZ2 *= 0.4000000059604645;
			}
			event.x = forward2 * this.moveSpeed3 * mx2 + strafe2 * this.moveSpeed3 * mz2;
			event.z = forward2 * this.moveSpeed3 * mz2 - strafe2 * this.moveSpeed3 * mx2;
			Speed.mc.thePlayer.stepHeight = 0.6f;
			if (forward2 == 0.0f && strafe2 == 0.0f) {
				event.x = 0.0;
				event.z = 0.0;
			} else {
				boolean collideCheck2 = false;
				if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer,
						Speed.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
					collideCheck2 = true;
				}
				if (forward2 != 0.0f) {
					if (strafe2 >= 1.0f) {
						yaw2 += ((forward2 > 0.0f) ? -45 : 45);
						strafe2 = 0.0f;
					} else if (strafe2 <= -1.0f) {
						yaw2 += ((forward2 > 0.0f) ? 45 : -45);
						strafe2 = 0.0f;
					}
					if (forward2 > 0.0f) {
						forward2 = 1.0f;
					} else if (forward2 < 0.0f) {
						forward2 = -1.0f;
					}
				}
			}
		}
	}

	@EventTarget
	public void onPreUpdate(final EventPreMotionUpdates event) {
		if (this.getMode() == "Mineplex-Old" || this.getMode() == "Test") {
			final double xDist = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
			final double zDist = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
			this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		} else if (this.getMode() == "Hop") {
			final double xDist = Wrapper.mc.thePlayer.posX - Wrapper.mc.thePlayer.prevPosX;
			final double zDist = Wrapper.mc.thePlayer.posZ - Wrapper.mc.thePlayer.prevPosZ;
			this.lastDist2 = Math.sqrt(xDist * xDist + zDist * zDist);
		}
	}

	public double round(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void onEnable() {
		this.cooldownHops = 3;
		this.state = 0;
		this.counter = 0;
		this.cancel = false;
		this.level = 1;
		this.moveSpeed2 = ((Wrapper.mc.thePlayer == null) ? 0.2873 : this.getBasemoveSpeed2());
		if (Speed.mc.thePlayer != null) {
			this.moveSpeed = 0.2873;
		}
		this.lastDist = 0.0;
		this.stage = 2;
		final Timer timer = Speed.mc.timer;
		Timer.timerSpeed = 1.0f;
		this.time.reset();
		super.onEnable();
	}

	public double round2(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private double getBasemoveSpeed2() {
		double baseSpeed = 0.2873;
		if (Wrapper.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int amplifier = Wrapper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	@Override
	public void onDisable() {
		this.moveSpeed2 = this.getBasemoveSpeed2();
		Speed.yOffset = 0.0;
		this.level = 0;
		this.disabling = false;
		final Timer timer = Speed.mc.timer;
		Timer.timerSpeed = 1.0f;
		Speed.mc.thePlayer.capabilities.allowFlying = false;
		Speed.mc.thePlayer.capabilities.isFlying = false;
		this.time.reset();
		super.onDisable();
	}
}
