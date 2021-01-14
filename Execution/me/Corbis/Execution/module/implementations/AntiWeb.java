package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AntiWeb extends Module {
    public Setting mode;
    public AntiWeb(){
        super("AntiWeb", Keyboard.KEY_NONE, Category.MOVEMENT);
        ArrayList<String> options = new ArrayList<>();
        options.add("NCP");
        options.add("Vanilla");
        Execution.instance.settingsManager.rSetting(mode = new Setting("Antiweb Mode", this, "Vanilla", options));
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event){
        if(event.isPre()){
            if(mode.getValString().equalsIgnoreCase("Vanilla")){
                mc.thePlayer.isInWeb = false;
            }else {
                if(mc.thePlayer.isInWeb){
                    if(mc.thePlayer.onGround){
                        mc.thePlayer.jump();
                        MoveUtils.setMotion(0.2);
                    }else {
                        mc.thePlayer.motionY = -0.1;
                    }
                } 
            }
        }
    }
}
