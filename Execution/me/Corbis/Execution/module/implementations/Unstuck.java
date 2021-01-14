package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class Unstuck extends Module {
    TimeHelper timer = new TimeHelper();
    int flags = 0;
    public Unstuck(){
        super("Unstuck", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onEvent(EventReceivePacket event){
        if(timer.hasReached(2000)){
            flags = 0;
            timer.reset();;
        }
        if(event.getPacket() instanceof S08PacketPlayerPosLook){
            flags++;
            if(flags > 6){
                event.setCancelled(true);

                MoveUtils.setMotion(0);
            }
        }

    }

    @EventTarget
    public void onUpdate(EventMotion event){
        if(flags > 8){
            event.setY(0);
            MoveUtils.setMotion(event, 0);
        }
    }




    @EventTarget
    public void onSend(EventSendPacket event){
        if(event.getPacket() instanceof C03PacketPlayer && flags > 8){
            event.setCancelled(true);
        }
    }
}
