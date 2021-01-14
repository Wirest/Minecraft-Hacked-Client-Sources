package me.Corbis.Execution.module.implementations;


import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.Notifications.Notification;
import me.Corbis.Execution.ui.Notifications.NotificationType;
import me.Corbis.Execution.utils.MoveUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;

public class HackerDetect extends Module {

    public HackerDetect(){
        super("HackerDetector", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        for(Entity e : mc.theWorld.loadedEntityList){
            if(e instanceof EntityOtherPlayerMP){
                EntityOtherPlayerMP player = (EntityOtherPlayerMP)e;

                if(player.rotationPitch > 90 || player.rotationPitch < -90){
                    Detect(player, "Scaffold/Aura V1: Bad Rotations");
                }

                if(Math.sqrt(player.posX * player.posX + player.posZ * player.posZ) - Math.sqrt(player.lastTickPosX * player.lastTickPosX + player.lastTickPosZ * player.lastTickPosZ) > 10){
                    Detect(player, "Flight V2: Blink Watchdog");
                }

                if(player.motionY == 0 && !player.isOnGround() && player.airTicks > 20 && player.fallDistance == 0){
                   // Detect(player, "Flight V1: Non Blink, Motion");
                }

                if(MoveUtils.getSpeed(player) > MoveUtils.getBaseMoveSpeed() + 0.85 && !player.isOnGround()){
                    Detect(player, "Speed V1: Motion, Moving too fast.");
                }
            }
        }
    }

    public void Detect(EntityOtherPlayerMP player, String text){
        if(mc.thePlayer.ticksExisted % 5 == 0) {
            Execution.instance.notificationManager.show(new Notification(player.getName() + " Failed " + text, 2, NotificationType.INFO));
        }
    }

}
