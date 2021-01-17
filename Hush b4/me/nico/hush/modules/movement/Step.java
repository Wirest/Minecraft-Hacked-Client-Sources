// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class Step extends Module
{
    TimeHelper delay;
    
    public Step() {
        super("Step", "Step", 5663904, 50, Category.MOVEMENT);
        this.delay = new TimeHelper();
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Intave");
        mode.add("CC");
        Client.instance.settingManager.rSetting(new Setting("Mode", "StepMode", this, "Intave", mode));
        Client.instance.settingManager.rSetting(new Setting("Height §l\u2192", "StepHeight", this, 1.0, 1.0, 5.0, false));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("StepMode").getValString().equalsIgnoreCase("CC")) {
            this.CC();
            this.setDisplayname("Step[" + Client.instance.settingManager.getSettingByName("StepMode").getValString() + ", " + Client.instance.settingManager.getSettingByName("StepHeight").getValDouble() + "]");
        }
        else if (Client.instance.settingManager.getSettingByName("StepMode").getValString().equalsIgnoreCase("Intave")) {
            this.Intave();
            this.setDisplayname("Step[Intave]");
        }
    }
    
    public void Intave() {
        if (Step.mc.gameSettings.keyBindForward.pressed || Step.mc.gameSettings.keyBindBack.pressed || Step.mc.gameSettings.keyBindLeft.pressed || Step.mc.gameSettings.keyBindRight.pressed) {
            final Minecraft mc = Step.mc;
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                final Minecraft mc2 = Step.mc;
                if (Minecraft.thePlayer.onGround) {
                    final Minecraft mc3 = Step.mc;
                    Minecraft.thePlayer.jump();
                    final Minecraft mc4 = Step.mc;
                    final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                    thePlayer.motionX *= 3.0;
                    final Minecraft mc5 = Step.mc;
                    final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                    thePlayer2.motionZ *= 3.0;
                }
            }
        }
    }
    
    public void CC() {
        final Minecraft mc = Step.mc;
        if (Minecraft.thePlayer.isCollidedHorizontally && (Step.mc.gameSettings.keyBindForward.pressed || Step.mc.gameSettings.keyBindBack.pressed || Step.mc.gameSettings.keyBindLeft.pressed || Step.mc.gameSettings.keyBindRight.pressed)) {
            final Minecraft mc2 = Step.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = Step.mc;
                if (!Minecraft.thePlayer.isOnLadder()) {
                    final Minecraft mc4 = Step.mc;
                    if (!Minecraft.thePlayer.isInWater()) {
                        final Minecraft mc5 = Step.mc;
                        if (!Minecraft.thePlayer.isInLava()) {
                            final Minecraft mc6 = Step.mc;
                            Minecraft.thePlayer.stepHeight = (float)Client.instance.settingManager.getSettingByName("StepHeight").getValDouble();
                            return;
                        }
                    }
                }
            }
        }
        final Minecraft mc7 = Step.mc;
        Minecraft.thePlayer.stepHeight = 0.5f;
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        final Minecraft mc = Step.mc;
        Minecraft.thePlayer.stepHeight = 0.5f;
        Step.mc.timer.timerSpeed = 1.0f;
    }
}
