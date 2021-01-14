package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventJump;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Longjump extends Module {
    boolean boost = false;
    public Setting boostspeed;
    public Setting damage;
    int stage = 0;
    public Setting mode;
    private float air, groundTicks;
    double motionY;
    int count;
    boolean collided, half;
    double lastDist, moveSpeed;
    int level;
    boolean wasOnGround;
    public Longjump(){
        super("LongJump", Keyboard.KEY_B, Category.MOVEMENT);
        Execution.instance.settingsManager.rSetting(boostspeed = new Setting("Longjump Speed", this, 1, 0, 5, false));
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Cubecraft");
        options.add("Mineplex");
        options.add("Mineplex2");


        Execution.instance.settingsManager.rSetting(mode = new Setting("Longjump Mode", this, "Hypixel", options));
        Execution.instance.settingsManager.rSetting(damage = new Setting("Longjump Damage", this, false));
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event){
        if(!event.isPre()){
            return;
        }
        if(mode.getValString().equalsIgnoreCase("Hypixel")) {


            
        }else if(mode.getValString().equalsIgnoreCase("Cubecraft")){
            stage++;


            switch(stage){
                case 0:
                    mc.thePlayer.motionY = 0.42;
                    MoveUtils.strafe(this.boostspeed.getValDouble());
                    break;
                case 1:
                    mc.thePlayer.motionY = 0.33;
                    MoveUtils.strafe(this.boostspeed.getValDouble());
                    break;
                case 2:
                    mc.thePlayer.motionY = 0.25;
                    MoveUtils.strafe(this.boostspeed.getValDouble());
                    break;
                case 3:
                    mc.thePlayer.motionY = 0.16;
                    break;
            }
            if(mc.thePlayer.fallDistance > 0){
                this.toggle();
                mc.timer.timerSpeed = 1f;
                MoveUtils.setMotion(0);
            }else {
                //   mc.timer.timerSpeed = 0.3f;
            }
        }else if(mode.getValString().equalsIgnoreCase("Mineplex")){
            if (!mc.thePlayer.onGround && !MoveUtils.isOnGround(0.01) && air > 0) {
                air++;
                if(mc.thePlayer.isCollidedVertically){
                    air = 0;
                }
                if(mc.thePlayer.isCollidedHorizontally && !collided){
                    collided = !collided;
                }
                double speed = half?0.5- air / 100 : 0.658 - air / 100;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                motionY -= 0.04000000000001;
                if(air > 24){
                    motionY -= 0.02;
                }
                if(air == 12){
                    motionY = -0.005;
                }
                if(speed < 0.3)
                    speed = 0.3;
                if(collided)
                    speed = 0.2873;
                mc.thePlayer.motionY = motionY;
                MoveUtils.setMotion(speed);
            } else {
                if (air > 0) {
                    air = 0;
                }
            }

            if (mc.thePlayer.onGround && MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {

                double groundspeed = 0;
                collided = mc.thePlayer.isCollidedHorizontally;
                groundTicks ++;

                mc.thePlayer.motionX *= groundspeed;
                mc.thePlayer.motionZ *= groundspeed;

                half = mc.thePlayer.posY != (int)mc.thePlayer.posY;
                mc.thePlayer.motionY = 0.4299999;
                air = 1;
                motionY = mc.thePlayer.motionY;
            }
        }


    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate){
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        if(mode.getValString().equalsIgnoreCase("Hypixel")) {
            final EntityPlayerSP player = Wrapper.getPlayer();

            if (MoveUtils.isOnGround()) {
                if (++groundTicks >= 1) toggle();
            }


        }
    }
    @EventTarget
    public void onMove(EventMotion event){
        if(mode.getValString().equalsIgnoreCase("Hypixel")){
            if (MoveUtils.isMoving() && !half) {
                if(MoveUtils.isOnGround()){
                    moveSpeed = 0.635;
                    event.setY(event.getLegitMotion());
                }else {
                    if(mc.thePlayer.fallDistance == 0) {
                        event.setY(event.getY() * 1.0419999688);
                    }else {
                        event.setY(event.getY() * MoveUtils.WATCHDOG_BUNNY_SLOPE);
                    }
                    moveSpeed = moveSpeed - lastDist / 159;
                }



                MoveUtils.setMotion(event, moveSpeed);
            }
        }

        if(stage > 5){
            event.setY(event.getY() - 0.032);
        }

        if(mc.thePlayer.hurtTime > 0){
            half = true;
        }

        stage++;
    }

    public double getBaseMoveSpeed(){
        return MoveUtils.getBaseMoveSpeed();
    }

    public void damagePlayer (int mode) {
        if (mc.thePlayer.onGround) {
            final double offset = 0.4122222218322211111111F * mode;
            final NetHandlerPlayClient netHandler = mc.getNetHandler();
            final EntityPlayerSP player = mc.thePlayer;
            final double x = player.posX;
            final double y = player.posY;
            final double z = player.posZ;
            for (int i = 0; i < 9; i++) {
                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + offset, z, false));
                netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.000002737272, z, false));
                netHandler.addToSendQueueSilent(new C03PacketPlayer(false));
            }
            netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
        }

    }

    @Override
    public void onEnable(){
        super.onEnable();
        this.boost = true;
        lastDist = 0;
        stage = 0;
        motionY = mc.thePlayer.motionY;
        air = 0;
        count = 0;
        half = mc.thePlayer.posY != (int)mc.thePlayer.posY;
        collided = mc.thePlayer.isCollidedHorizontally;
        groundTicks = 0;
        level = 0;
        if(mode.getValString().equalsIgnoreCase("Hypixel")) {
         //   mc.thePlayer.jump();
            air = (float) mc.thePlayer.posY;
            half = false;
        }
    }



    @Override
    public void onDisable(){
        super.onDisable();
        mc.timer.timerSpeed = 1f;
        Wrapper.getPlayer().motionX = 0.0;
        Wrapper.getPlayer().motionZ = 0.0;
    }

}
