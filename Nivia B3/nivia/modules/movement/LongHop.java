package nivia.modules.movement;

import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import nivia.events.EventTarget;
import nivia.events.events.EventMove;
import nivia.events.events.EventPostMotionUpdates;
import nivia.modules.Module;
import nivia.utils.Helper;

public class LongHop extends Module {
	public LongHop() {
		super("LongHop", 0, 0x75FF47, Category.MOVEMENT, "Long Jump stupid noob", new String[] { "lh", "lj" }, true);
	}

	private int level = 1;
	private double moveSpeed;
	private double lastDist;
	private boolean b2;
	private nivia.utils.utils.Timer timerino = new nivia.utils.utils.Timer();
	@Override
	public void onDisable(){
		super.onDisable();
		level = 1;
		moveSpeed = 0.1;
		b2 = false;
		lastDist = 0;
	}
	@EventTarget
	public void onMove(EventMove e) {
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			e.x = 0.0D;
			e.z = 0.0D;
		} else if (forward != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (forward > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (forward > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}
			if (forward > 0.0F)
				forward = 1.0F;
			else if (forward < 0.0F)
				forward = -1.0F;
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		if (b2) {
			if (!Helper.playerUtils().MovementInput()) {
				this.level = 1;
				mc.thePlayer.setSprinting(false);
			}

			if (this.level == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {

				this.level = 2;
				final int amplifier = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
				double boost = 3.5D;
				switch (amplifier) {
					case 1:
						boost = 3.3;
						break;
					case 2:
						boost = 3.1;
						break;
					case 3:
						boost = 2.9;
						break;
					case 4:
						boost = 2.7;
						break;
				}
				this.moveSpeed = (boost) * this.getBaseMoveSpeed() - 0.01;
			} else if (this.level == 2) {
				this.level = 3;
				this.mc.thePlayer.motionY = 0.424;
				e.y = 0.424;
				this.moveSpeed *= (2.149802);
			} else if (this.level == 3) {
				this.level = 4;
				final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
				this.moveSpeed = this.lastDist - difference;

			} else {
				Timer.timerSpeed = 1.0F;
				if (mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
						this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically)
					this.level = 1;
				this.moveSpeed = this.lastDist - this.lastDist / 159.0;
			}
			this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
			e.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
			e.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
			if (forward == 0.0f && strafe == 0.0f) {
				e.x = 0;
				e.z = 0;
			}
		}

		if (this.timerino.hasTimeElapsed(620)) {
			//Wrapper.addChat("End B");
			if(Helper.playerUtils().getDistanceToFall() < 2){
				mc.thePlayer.motionX *= 0.3;
				mc.thePlayer.motionZ *= 0.3;
			}
			b2 = false;
		}

		if (this.timerino.hasTimeElapsed(1000) && mc.thePlayer.isCollidedVertically) {
			this.timerino.reset();
			level = 1;
			this.b2 = true;
		}
	}

	@EventTarget
	public void onPost(EventPostMotionUpdates post) {
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX, zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
	}
	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

}
