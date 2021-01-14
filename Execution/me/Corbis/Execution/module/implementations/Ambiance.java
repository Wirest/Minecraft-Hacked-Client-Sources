package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.lwjgl.input.Keyboard;

public class Ambiance extends Module {
    public Setting ambiance;
    public Setting loop;
    TimeHelper timer = new TimeHelper();
    public Ambiance(){
        super("Ambiance", Keyboard.KEY_NONE, Category.WORLD);
        Execution.instance.settingsManager.rSetting(ambiance = new Setting("Ambiance", this, 0 ,0, 15000, true));

        Execution.instance.settingsManager.rSetting(loop = new Setting("Loop", this,false));
    }

    @EventTarget
    public void onRecieve(EventReceivePacket event){
        if(event.getPacket() instanceof S03PacketTimeUpdate){
            event.setCancelled(true);
        }

        if(!loop.getValBoolean()) {
            mc.theWorld.setWorldTime((long) ambiance.getValDouble());
        }else {
            mc.theWorld.setWorldTime(timer.getCurrentTime());
            if(timer.getCurrentTime() > 30000){
                timer.reset();
            }
        }
    }

    @Override
    public void onEnable(){
        super.onEnable();
        timer.reset();
    }
}
