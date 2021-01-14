package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class HypixelVertFly extends Module {

    public HypixelVertFly(){
        super("HypixelVertFly", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event){
        if(event.getPacket() instanceof S08PacketPlayerPosLook){
            ((S08PacketPlayerPosLook) event.getPacket()).setY((mc.thePlayer.posY - 2 - (mc.thePlayer.posY - ((S08PacketPlayerPosLook) event.getPacket()).getY())));
        }
    }

    @EventTarget
    public void onMotion(EventMotion event){
        if(mc.thePlayer.movementInput.jump){
            event.setY(event.getLegitMotion());
        }
    }



}
