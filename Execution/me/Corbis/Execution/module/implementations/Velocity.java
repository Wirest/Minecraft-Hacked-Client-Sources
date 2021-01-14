package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Setting notify;
    public Velocity(){
        super("Velocity", Keyboard.KEY_M, Category.COMBAT);
        Execution.instance.settingsManager.rSetting(notify = new Setting("Notify", this, false));
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event){
        if(event.getPacket() instanceof S27PacketExplosion || event.getPacket() instanceof S12PacketEntityVelocity){
            event.setCancelled(true);
        }
    }

}
