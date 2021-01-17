package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.util.*;

public class FastSwim extends Module implements MovementHelper, CombatHelper
{
    public int ticks;
    
    public FastSwim() {
        this.setName("FastSwim");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void moveEntity(final EventMove event) {
        if (this.isToggled() && FastSwim.mc.thePlayer.isInWater()) {
            ++this.ticks;
            if (this.ticks == 4) {
                setMoveSpeed(event, 0.4000000059604645);
            }
            if (this.ticks >= 5) {
                setMoveSpeed(event, 0.30000001192092896);
                this.ticks = 0;
            }
        }
    }
    
    public static void setMoveSpeed(final EventMove event, final double speed) {
        final MovementInput movementInput = FastSwim.mc.thePlayer.movementInput;
        double forward = movementInput.moveForward;
        double strafe = movementInput.moveStrafe;
        float yaw = FastSwim.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setMotionX(0.0);
            event.setMotionZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setMotionX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setMotionZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
}
