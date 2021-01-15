// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Movement;

import cf.euphoria.euphorical.Events.EventUpdate;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import cf.euphoria.euphorical.Utils.MathUtils;
import cf.euphoria.euphorical.Events.EventMove;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Utils.ModeUtils;
import cf.euphoria.euphorical.Events.EventTick;
import net.minecraft.util.Timer;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.potion.Potion;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Speed extends Mod
{
    private int stage;
    private double moveSpeed;
    private double lastDist;
    
    public Speed() {
        super("Speed", Category.MOVEMENT);
        this.setBind(47);
        this.stage = 1;
        this.moveSpeed = 0.2873;
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        Timer.timerSpeed = 1.0f;
        this.moveSpeed = this.getBaseMoveSpeed();
        this.lastDist = 0.0;
        this.stage = 4;
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onTick(final EventTick event) {
        if (ModeUtils.speedMode.equalsIgnoreCase("yport")) {
            this.setRenderName(String.format("%s §7yPort", this.getModName()));
        }
        else if (ModeUtils.speedMode.equalsIgnoreCase("vhop")) {
            this.setRenderName(String.format("%s §7VHop", this.getModName()));
        }
    }
    
    @EventTarget
    public void onMove(final EventMove event) {
        Timer.timerSpeed = 1.0f;
        if (MathUtils.round(this.mc.thePlayer.posY - (int)this.mc.thePlayer.posY, 3) == MathUtils.round(0.138, 3)) {
            final EntityPlayerSP thePlayer = this.mc.thePlayer;
            --thePlayer.motionY;
            event.y -= 0.0931;
            final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
            thePlayer2.posY -= 0.0931;
        }
        if (ModeUtils.speedMode.equalsIgnoreCase("yport") && this.mc.thePlayer.onGround && (this.mc.thePlayer.motionX != 0.0 || this.mc.thePlayer.motionZ != 0.0)) {
            this.stage = 2;
        }
        if (this.stage == 2 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
            final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
            final double n = 0.39936;
            thePlayer3.motionY = n;
            event.y = n;
            this.moveSpeed *= 2.3499999;
        }
        else if (this.stage == 3) {
            final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
            this.moveSpeed = this.lastDist - difference;
        }
        else {
            if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
                this.stage = 1;
            }
            this.moveSpeed = this.lastDist - this.lastDist / 159.0;
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        float forward = this.mc.thePlayer.movementInput.moveForward;
        float strafe = this.mc.thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
        event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
        this.mc.thePlayer.stepHeight = 0.6f;
        if (forward == 0.0f && strafe == 0.0f) {
            event.x = 0.0;
            event.z = 0.0;
        }
        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        ++this.stage;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
        final double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        if (ModeUtils.speedMode.equalsIgnoreCase("yport")) {
            this.mc.thePlayer.motionY = -0.41;
        }
    }
}
