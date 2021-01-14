package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.event.Event;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class TestModule extends Module {
    int ticks;
    double y;
    public TestModule(){
        super("TestModule", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onMotion(EventMotionUpdate event){
        mc.thePlayer.cameraYaw = 0.105f;

        mc.thePlayer.motionY = 0;
        if(event.isPre()){

            if(mc.thePlayer.ticksExisted % 2 == 0){
                MoveUtils.setPosPlus(0, 1.0e-5, 0);
            }
        }
        MoveUtils.setMotion(MoveUtils.getBaseMoveSpeed() - 0.01);
    }


    @EventTarget
    public void onUpdate(EventUpdate event){
        PlayerCapabilities pc = new PlayerCapabilities();
        pc.setFlySpeed((float) nigger(5,4));
        pc.setPlayerWalkSpeed((float) nigger(5,4));
        if(mc.thePlayer.ticksExisted % 2 == 0){
            mc.getNetHandler().addToSendQueueSilent(new C13PacketPlayerAbilities(pc));
        }
    }
    public static double nigger(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }


    @Override
    public void onEnable(){
        super.onEnable();
        C13PacketPlayerAbilities packet = new C13PacketPlayerAbilities();
        packet.setCreativeMode(true);
        mc.getNetHandler().addToSendQueueSilent(packet);
        ticks = 0;
        y = 0;
    }
}
