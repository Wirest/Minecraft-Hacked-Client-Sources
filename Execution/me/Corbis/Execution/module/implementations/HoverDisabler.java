package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.Notifications.Notification;
import me.Corbis.Execution.ui.Notifications.NotificationType;
import me.Corbis.Execution.utils.MoveUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;

public class HoverDisabler extends Module {

    public HoverDisabler(){
        super("HoverDisabler", Keyboard.KEY_U, Category.EXPLOIT);
    }

    @EventTarget
    public void onMotion(EventMotion event){
        if(mc.thePlayer.onGround){
            event.setY(event.getLegitMotion());
        }else  {
            event.setY(0);
          //  if (mc.thePlayer.ticksExisted % 3 == 0) {
               // mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0e-5, mc.thePlayer.posZ, false));
               // mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 8e-7, mc.thePlayer.posZ, true));
                mc.thePlayer.capabilities.isFlying = true;
                mc.thePlayer.capabilities.allowFlying = true;
                mc.thePlayer.capabilities.isCreativeMode = true;
            //}

            MoveUtils.setMotion(event, 0);
        }
    }

    @EventTarget
    public void onReceive(EventReceivePacket event){
        if(event.getPacket() instanceof S08PacketPlayerPosLook){
            Execution.instance.notificationManager.show(new Notification("You can do anything for 5 seconds.", 2, NotificationType.INFO));
            mc.thePlayer.capabilities.isFlying = false;
            mc.thePlayer.capabilities.allowFlying = false;
            mc.thePlayer.capabilities.isCreativeMode = false;
            this.toggle();
        }
    }

    @Override
    public void onEnable(){
        super.onEnable();
        MoveUtils.setMotion(0);

        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.28, mc.thePlayer.posZ, false));
    }
}
