package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

public class Derp extends Module {
    public Setting silent;
    public Derp(){
        super("Derp", Keyboard.KEY_NONE, Category.PLAYER);
        Execution.instance.settingsManager.rSetting(silent = new Setting("Silent Derp", this, true));
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event){
        if(this.silent.getValBoolean()){
            event.setYaw((float) ThreadLocalRandom.current().nextDouble(-359, 359));
            event.setPitch((float) ThreadLocalRandom.current().nextDouble(-89, 89));
            mc.thePlayer.setSneaking(mc.thePlayer.ticksExisted % 2 == 0);
            if(mc.thePlayer.ticksExisted % 3 == 0){
                mc.thePlayer.swingItem();
            }
        }else {
            mc.thePlayer.rotationYaw = ((float) ThreadLocalRandom.current().nextDouble(-359, 359));
            mc.thePlayer.rotationPitch = ((float) ThreadLocalRandom.current().nextDouble(-89, 89));
            mc.thePlayer.setSneaking(mc.thePlayer.ticksExisted % 2 == 0);
            if(mc.thePlayer.ticksExisted % 3 == 0){
                mc.thePlayer.swingItem();
            }
        }
    }

}
