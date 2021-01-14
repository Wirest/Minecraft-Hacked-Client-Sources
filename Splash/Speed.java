package splash.client.modules.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.potion.Potion;
import net.minecraft.util.Timer;
import splash.Splash;
import splash.Splash.GAMEMODE;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.modules.combat.Aura;
import splash.client.modules.player.Scaffold;
import splash.utilities.math.MathUtils;
import splash.utilities.player.MovementUtils;
import splash.utilities.system.ClientLogger;

import java.util.Random;
import java.util.PrimitiveIterator.OfDouble;

import org.lwjgl.opengl.APPLEVertexArrayRange;

/**
 * Author: Ice Created: 17:41, 30-May-20 Project: Client
 */
public class Speed extends Module {

	private double speed, currentDistance, lastDistance;
	public boolean prevOnGround, usedOnGround;
	public static double waterSpeed;
	public int stage, ticksNeeded;
	public BooleanValue<Boolean> flagCheck = new BooleanValue<Boolean>("Flag Detect", true, this);
	public BooleanValue<Boolean> waterSpeedValue = new BooleanValue<Boolean>("Water Speed", true, this);
	public NumberValue<Double> valueSpeed = new NumberValue<Double>("Speed", 0.5D, 0.1D, 5D, this);
	public ModeValue<Mode> modeValue = new ModeValue<Mode>("Mode", Mode.VANILLA, this);

	public Speed() {
		super("Speed", "Lets you go faster.", ModuleCategory.MOVEMENT);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		speed = ticksNeeded = 0;
		prevOnGround = true;
		lastDistance = 0;
		waterSpeed = 0.1;
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1f;
		mc.thePlayer.jumpMovementFactor = 0.0F;
		MovementUtils.setMoveSpeed(0);
		if (modeValue.getValue() == Mode.SENTINEL) {
			mc.thePlayer.motionY = 0F;
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2664444, mc.thePlayer.posZ);
		}

	}

	@Collect
	public void packetEvent(EventPlayerUpdate e) {

		if (modeValue.getValue() == Mode.WATCHDOG && e.getStage().equals(Stage.PRE)) {
			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
			if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
				e.setY(mc.thePlayer.posY + 1.8995E-35D);
			}

		}
	}

	@Collect
	public void onMove(EventMove e) {
		if (waterSpeedValue.getValue()) {
			if (mc.thePlayer.isInWater()) {
				e.setY(mc.thePlayer.motionY = 0.42F);
				waterSpeed = 0.5D;
				MovementUtils.setMoveSpeed(e, waterSpeed);
			} else {
				if (waterSpeed > 0.3) {
					MovementUtils.setMoveSpeed(e, waterSpeed *= 0.99);
				}
			}
		}

		if (modeValue.getValue() == Mode.SENTINEL) {
			if (mc.thePlayer.isMoving()) {
				if (mc.thePlayer.onGround) {
					e.setY(.24f);
					e.setMoveSpeed(valueSpeed.getValue() * 2);
				} else {
					e.setMoveSpeed(.15);
				}
			} else {
				e.setX(0);
				e.setZ(0);
			}
		}
		if (modeValue.getValue() == Mode.VANILLA) {
			if (mc.thePlayer.isMovingOnGround()) {
			 mc.thePlayer.motionY += 0.42;
			}
			MovementUtils.setMoveSpeed(e, valueSpeed.getValue());
		}

		if (modeValue.getValue() == Mode.PVPTEMPLE && mc.thePlayer.isMoving()) {
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				if (usedOnGround) {
					MovementUtils.setMoveSpeed(e, speed = .1);
					if (mc.thePlayer.onGround) {
						usedOnGround = false;
					}
				} else {
					if (mc.thePlayer.onGround) {
						speed = e.getMovementSpeed() * 1.1;
						e.setY(mc.thePlayer.motionY = 0.42F);
						prevOnGround = true;
					} else {
						speed -= (double) (mc.thePlayer.fallDistance / 10.0F);
						if (speed < e.getMovementSpeed()) {
							speed = e.getMovementSpeed();
						}
					}
				}
				MovementUtils.setMoveSpeed(speed);
			} else {
				usedOnGround = true;
				if (mc.thePlayer.onGround) {
					if (mc.thePlayer.ticksExisted % 4 == 0) {
						MovementUtils.setMoveSpeed(valueSpeed.getValue());
					} else {
						MovementUtils.setMoveSpeed(.15);
					}
				} else {
					MovementUtils.setMoveSpeed(.1);
				}
			}
		}

		if (modeValue.getValue() == Mode.GWEN) {
			mc.timer.timerSpeed = 1.0f;
			if (this.mc.thePlayer.onGround && this.mc.thePlayer.isMoving()) {
				speed = 0.8;
				final Timer timer = this.mc.timer;
				timer.timerSpeed *= (float) 2.14999;
				e.setY(this.mc.thePlayer.motionY = 0.42);
				e.setX(0.0);
				e.setZ(0.0);
				return;
			}
			if (mc.thePlayer.ticksExisted % 2 == 0) {
				speed -= 0.01;
			} else {
				speed -= 0.005;

			}
			MovementUtils.setMoveSpeed(speed);

		}

		if (modeValue.getValue() == Mode.WATCHDOG) {

			if (Splash.getInstance().getGameMode().equals(GAMEMODE.DUELS)) {
				if (!mc.thePlayer.onGround) {
					if (Splash.getInstance().getModuleManager().getModuleByClass(Aura.class).isModuleActive() && Aura.currentEntity != null) {
						mc.timer.timerSpeed = 1.2016f;
					} else {
						mc.timer.timerSpeed = 1.00f;
						}
				}
				if (mc.thePlayer.isMovingOnGround() && !mc.thePlayer.isInWater()) {
					mc.timer.timerSpeed = 1F;
					e.setY(mc.thePlayer.motionY = 0.419999999F);
					speed = MovementUtils.getBaseMoveSpeed() * 2.1499924999912287832;
					prevOnGround = true;
				} else if (!mc.thePlayer.isInWater()) {
					if (prevOnGround) {
						if (currentDistance > 0) {
							speed -= 0.63000222233332211 * (speed - MovementUtils.getBaseMoveSpeed());
							currentDistance = 0;
						} else {
							speed -= 0.6499996000222233332211 * (speed - MovementUtils.getBaseMoveSpeed());
							currentDistance++;
						}
						prevOnGround = false;
					} else {
						speed -= speed / 122;
					}
				}
				if (!mc.thePlayer.isInWater())
					MovementUtils.setMoveSpeed(e, speed);
			} else {
				if (!mc.thePlayer.isMoving()) return;
				if (stage  == 1) {
					speed = currentDistance > 1 ? e.getMovementSpeed() / 1.5 : e.getMovementSpeed();
				}
				if (stage == 2) {
					if (mc.thePlayer.isPotionActive(Potion.jump)) {
						e.setY((mc.thePlayer.motionY = 0.41999998688697815D + (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1));
					} else {
						e.setY((mc.thePlayer.motionY = 0.41999998688697815D));
					}
					speed = e.getMovementSpeed() * 2.1499924999912287832;
					prevOnGround = true;
				} else if(stage == 3) { 
					if (currentDistance > 1) {
						speed -= 0.6444444444444444 * (speed - e.getMovementSpeed());
						currentDistance = 0;
					} else {
						speed -= 0.65555555555555555 * (speed - e.getMovementSpeed());
						currentDistance++;
					}
				} else { 
	                e.setY(mc.thePlayer.motionY - (0.000999999999999999999999999F));
					
					if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0 || mc.thePlayer.onGround) && stage > 0) {
						stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
					}
					speed -= speed / 159.999996656434;
				}
				e.setMoveSpeed(Math.max(speed, e.getMovementSpeed()));
				stage++;
			
			}
			
			

		}

	}

	public enum Mode {
		VANILLA, PVPTEMPLE, GWEN, WATCHDOG, SENTINEL, NCP, GAY
	}
}
