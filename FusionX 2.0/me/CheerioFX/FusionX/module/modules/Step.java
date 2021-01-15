// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.events.StepConfirmEvent;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.events.StepEvent;
import me.CheerioFX.FusionX.utils.Wrapper;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Step extends Module
{
    private boolean reverse;
    private int groundTicks;
    private int recentStepTicks;
    private int stepStage;
    public static boolean stepping;
    
    public Step() {
        super("Step", 45, Category.MOVEMENT);
    }
    
    public boolean isAirStep() {
        return FusionX.theClient.setmgr.getSetting(this, "AirStep").getValBoolean();
    }
    
    public int getSh() {
        return FusionX.theClient.setmgr.getSetting(this, "Height").getValInt();
    }
    
    public boolean isBypass() {
        return FusionX.theClient.setmgr.getSetting(this, "Packet").getValBoolean();
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Height", this, 1.0, 1.0, 10.0, true));
        FusionX.theClient.setmgr.rSetting(new Setting("AirStep", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("Packet", this, true));
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.stepStage = 0;
        Step.mc.thePlayer.stepHeight = 0.6f;
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            if (!this.isBypass()) {
                Wrapper.mc.thePlayer.stepHeight = this.getSh();
            }
            if (this.isAirStep() && Step.mc.thePlayer.isCollidedHorizontally) {
                Step.mc.thePlayer.onGround = true;
            }
        }
    }
    
    @EventTarget
    private void onStep(final StepEvent event) {
        if (this.getSh() > 1.0) {
            Wrapper.getMinecraft().thePlayer.stepHeight = this.getSh();
            event.stepHeight = this.getSh();
        }
        else {
            Wrapper.getMinecraft().thePlayer.stepHeight = 0.6f;
            if (Wrapper.getMinecraft().thePlayer.movementInput != null && !Wrapper.getMinecraft().thePlayer.movementInput.jump && Wrapper.getMinecraft().thePlayer.isCollidedVertically) {
                event.stepHeight = 1.0;
                event.bypass = true;
            }
        }
    }
    
    @EventTarget
    private void onStepConfirmed(final StepConfirmEvent event) {
        this.stepStage = 0;
        Wrapper.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getMinecraft().thePlayer.posX, Wrapper.getMinecraft().thePlayer.posY + 0.42, Wrapper.getMinecraft().thePlayer.posZ, Wrapper.getMinecraft().thePlayer.onGround));
        Wrapper.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getMinecraft().thePlayer.posX, Wrapper.getMinecraft().thePlayer.posY + 0.75, Wrapper.getMinecraft().thePlayer.posZ, Wrapper.getMinecraft().thePlayer.onGround));
    }
}
