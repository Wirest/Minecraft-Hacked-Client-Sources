package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AntiFlag extends Module {
    public AntiFlag(){
        super("AntiFlag", Keyboard.KEY_NONE, Category.EXPLOIT);
    }
    ArrayList<Packet> packets = new ArrayList<>();
    @EventTarget
    public void onReceivePacket(EventReceivePacket event){
        if(event.getPacket() instanceof S08PacketPlayerPosLook){
            ((S08PacketPlayerPosLook) event.getPacket()).setY(((S08PacketPlayerPosLook) event.getPacket()).getY() - 255);

        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket eventSendPacket){

    }

    @Override
    public void onDisable(){
        super.onDisable();

    }
}
