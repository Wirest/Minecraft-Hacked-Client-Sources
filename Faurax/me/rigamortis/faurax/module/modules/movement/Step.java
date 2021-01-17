package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Step extends Module implements MovementHelper
{
    private int ticks;
    
    public Step() {
        this.setName("Step");
        this.setKey("H");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Step.mc.thePlayer.stepHeight = 0.5f;
    }
    
    @EventTarget
    public void preStep(final EventStep e) {
        if (this.isToggled()) {
            if (Step.movementUtils.shouldStep() && !Step.mc.gameSettings.keyBindJump.pressed && Step.mc.thePlayer.fallDistance < 0.1f) {
                Step.mc.thePlayer.stepHeight = 1.0f;
            }
            else {
                Step.mc.thePlayer.stepHeight = 0.5f;
            }
        }
    }
    
    @EventTarget
    public void postStep(final EventPostStep e) {
        if (this.isToggled() && !Step.mc.gameSettings.keyBindJump.pressed && Step.mc.thePlayer.fallDistance < 0.1f) {
            Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + 0.42, Step.mc.thePlayer.posZ, Step.mc.thePlayer.onGround));
            Step.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Step.mc.thePlayer.posX, Step.mc.thePlayer.posY + 0.75, Step.mc.thePlayer.posZ, Step.mc.thePlayer.onGround));
        }
    }
}
