package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.RandomUtils;
import me.Corbis.Execution.utils.TimeHelper;
import org.lwjgl.input.Keyboard;

public class Spammer extends Module {
    public Setting delay;
    TimeHelper timer = new TimeHelper();


    public Spammer(){
        super("Spammer", Keyboard.KEY_NONE, Category.MISC);
        Execution.instance.settingsManager.rSetting(delay = new Setting("Spammer Delay", this, 3000, 1, 10000, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        if(timer.hasReached( delay.getValInt())){
            mc.thePlayer.sendChatMessage("sub to Corbis?#2410");
            timer.reset();
        }

    }
}
