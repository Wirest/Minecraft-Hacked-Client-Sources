package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class NoRotate extends Module {
    public NoRotate(){
        super("NoRotate", Keyboard.KEY_NONE, Category.MISC);
    }
    @EventTarget
    public void onReceive(EventReceivePacket event){
        if(event.getPacket() instanceof S08PacketPlayerPosLook){
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.thePlayer.rotationYaw;
            packet.pitch = mc.thePlayer.rotationPitch;
        }
    }
}
