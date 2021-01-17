package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.utils.MathUtils;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;


public class Speed extends Module {
	public static double speed = 0.07999999821186066;
    public int stage;
    public boolean disabling;
    public boolean stopMotionUntilNext;
    public double moveSpeed;
    public boolean spedUp;
    public static boolean canStep;
    public double lastDist;
    public static double yOffset;
    public boolean cancel;
	private static String mode;
 
    public Speed(){
		super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT, mode);
	}
 
    @EventTarget
    public void onUpdate(EventUpdate e) {       
        if(!this.isToggled()){
            return;
        }
        bhop();                         
    }
    
    public void bhop(){
		setMode("\u00A7f[BHOP]");
    	double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        if (!canSpeed())
            return;
        if (mc.thePlayer.isInWater()) {
            return;
        }
        mc.timer.timerSpeed = 1.0f;
        if (MathUtils.round(mc.thePlayer.posY - (double) ((int) mc.thePlayer.posY), 3) == MathUtils.round(0.138, 3)) {
            mc.thePlayer.motionY -= 0.08;
            mc.thePlayer.motionY -= 0.09316090325960147;
            mc.thePlayer.posY -= 0.09316090325960147;
        }
        if (this.stage == 1 && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
            this.stage = 2;
            this.moveSpeed = 1.38 * this.getBaseMoveSpeed() - 0.01;
        } else if (this.stage == 2) {
            this.stage = 3;
            mc.thePlayer.motionY = 0.399399995803833;
            mc.thePlayer.motionY = 0.399399995803833;
            this.moveSpeed *= 2.149;
        } else if (this.stage == 3) {
            this.stage = 4;
            double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        } else {
            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
                    mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0
                    || mc.thePlayer.isCollidedVertically) {
                this.stage = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        MovementInput movementInput = mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0f && strafe == 0.0f) {
        	mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        } else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double mx2 = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz2 = Math.sin(Math.toRadians(yaw + 90.0f));
        double motionX = (double) forward * this.moveSpeed * mx2 + (double) strafe * this.moveSpeed * mz2;
        double motionZ = (double) forward * this.moveSpeed * mz2 - (double) strafe * this.moveSpeed * mx2;
        mc.thePlayer.motionX = (double) forward * this.moveSpeed * mx2 + (double) strafe * this.moveSpeed * mz2;
        mc.thePlayer.motionZ = (double) forward * this.moveSpeed * mz2 - (double) strafe * this.moveSpeed * mx2;
        
    }
 
    public boolean canSpeed() {
        if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.movementInput.jump) {
            return false;
        } else
            return true;
    }
 
 
    public double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
        }
        return baseSpeed;
    }
 
 
    @Override
    public void onEnable() {
        mc.timer.timerSpeed = 1.0f;
        this.cancel = false;
        this.stage = 1;
        this.moveSpeed = mc.thePlayer == null ? 0.2873 : this.getBaseMoveSpeed();
        double d2 = this.moveSpeed;
    }
 
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        this.moveSpeed = this.getBaseMoveSpeed();
        yOffset = 0.0;
        this.stage = 0;
        this.disabling = false;
    }
}