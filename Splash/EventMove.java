package splash.client.events.player;


import me.hippo.systems.lwjeb.event.Cancelable;
import me.hippo.systems.lwjeb.event.MultiStage;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import splash.Splash;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.TargetStrafe;
import splash.utilities.player.RotationUtils;

/**
 * Author: Ice
 * Created: 17:35, 30-May-20
 * Project: Client
 */
public class EventMove extends Cancelable {

	private double x;
	private double y;
	private double z;
    private boolean onGround;

    public EventMove(double x, double y, double z, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    public double getMovementSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward, strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe,
            yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;

    public void setMovementSpeed(double movementSpeed) {
        setX(-(Math.sin((double) Minecraft.getMinecraft().thePlayer.getDirection()) * Math.max(movementSpeed, getMovementSpeed())));
        setZ(Math.cos((double) Minecraft.getMinecraft().thePlayer.getDirection()) * Math.max(movementSpeed, getMovementSpeed()));
    }

    public void setMoveSpeed(double moveSpeed) {
    	Minecraft mc = Minecraft.getMinecraft();
		double range = ((Aura) Splash.getInstance().getModuleManager().getModuleByClass(Aura.class)).range.getValue();
		MovementInput movementInput = mc.thePlayer.movementInput;
		double moveForward = movementInput.getForward();
		boolean targetStrafe = TargetStrafe.canStrafe();
		if (targetStrafe) {
			if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= range - 2.5) {
				moveForward = 0;
			} else {
				moveForward = 1;
			}
		}
		double moveStrafe = targetStrafe ? TargetStrafe.direction : movementInput.getStrafe();
		double yaw = targetStrafe ? RotationUtils.getNeededRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
		if (moveForward == 0.0D && moveStrafe == 0.0D) {
			setX(0.0D);
			setZ(0.0D);
		} else {
			if (moveStrafe > 0) {
				moveStrafe = 1;
			} else if (moveStrafe < 0) {
				moveStrafe = -1;
			}
			if (moveForward != 0.0D) {
				if (moveStrafe > 0.0D) {
					yaw += (moveForward > 0.0D ? -45 : 45);
				} else if (moveStrafe < 0.0D) {
					yaw += (moveForward > 0.0D ? 45 : -45);
				}
				moveStrafe = 0.0D;
				if (moveForward > 0.0D) {
					moveForward = 1.0D;
				} else if (moveForward < 0.0D) {
					moveForward = -1.0D;
				}
			}
			double cos = Math.cos(Math.toRadians(yaw + 90.0F));
			double sin = Math.sin(Math.toRadians(yaw + 90.0F));
			setX(moveForward * moveSpeed * cos + moveStrafe * moveSpeed * sin);
			setZ(moveForward * moveSpeed * sin - moveStrafe * moveSpeed * cos);
		}
    }
}
