package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.potion.*;

public class LongJump extends Module implements MovementHelper
{
    private boolean speedTick;
    
    public LongJump() {
        this.setName("LongJump");
        this.setKey("");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        this.isToggled();
    }
    
    @EventTarget
    public void postTick(final EventPostTick e) {
        if (this.isToggled() && (LongJump.mc.gameSettings.keyBindForward.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindLeft.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindRight.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindBack.getIsKeyPressed()) && LongJump.mc.gameSettings.keyBindJump.pressed) {
            final float direction = LongJump.mc.thePlayer.rotationYaw + ((LongJump.mc.thePlayer.moveForward < 0.0f) ? 180 : 0) + ((LongJump.mc.thePlayer.moveStrafing > 0.0f) ? (-90.0f * ((LongJump.mc.thePlayer.moveForward < 0.0f) ? -0.5f : ((LongJump.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f) - ((LongJump.mc.thePlayer.moveStrafing < 0.0f) ? (-90.0f * ((LongJump.mc.thePlayer.moveForward < 0.0f) ? -0.5f : ((LongJump.mc.thePlayer.moveForward > 0.0f) ? 0.5f : 1.0f))) : 0.0f);
            final float xDir = (float)Math.cos((direction + 90.0f) * 3.141592653589793 / 180.0);
            final float zDir = (float)Math.sin((direction + 90.0f) * 3.141592653589793 / 180.0);
            if (LongJump.mc.thePlayer.isCollidedVertically && (LongJump.mc.gameSettings.keyBindForward.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindLeft.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindRight.getIsKeyPressed() || (LongJump.mc.gameSettings.keyBindBack.getIsKeyPressed() && LongJump.mc.gameSettings.keyBindJump.getIsKeyPressed()))) {
                LongJump.mc.thePlayer.motionX = xDir * 0.29f;
                LongJump.mc.thePlayer.motionZ = zDir * 0.29f;
            }
            if (LongJump.mc.thePlayer.motionY == 0.33319999363422365 && (LongJump.mc.gameSettings.keyBindForward.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindLeft.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindRight.getIsKeyPressed() || LongJump.mc.gameSettings.keyBindBack.getIsKeyPressed())) {
                if (this.speedTick) {
                    if (LongJump.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        LongJump.mc.thePlayer.motionX = xDir * 1.34;
                        LongJump.mc.thePlayer.motionZ = zDir * 1.34;
                    }
                    else {
                        LongJump.mc.thePlayer.motionX = xDir * 1.261;
                        LongJump.mc.thePlayer.motionZ = zDir * 1.261;
                    }
                }
                else if (LongJump.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    LongJump.mc.thePlayer.motionX = xDir * 0.0;
                    LongJump.mc.thePlayer.motionZ = zDir * 0.0;
                }
                else {
                    LongJump.mc.thePlayer.motionX = xDir * 0.0;
                    LongJump.mc.thePlayer.motionZ = zDir * 0.0;
                }
                this.speedTick = !this.speedTick;
            }
        }
    }
    
    private boolean canSpeed() {
        return !LongJump.mc.thePlayer.isOnLadder() && !LongJump.mc.thePlayer.isInWater() && !LongJump.mc.thePlayer.isSneaking() && LongJump.mc.thePlayer.motionX != 0.0 && LongJump.mc.thePlayer.motionZ != 0.0 && !LongJump.movementUtils.isOnLiquid();
    }
}
