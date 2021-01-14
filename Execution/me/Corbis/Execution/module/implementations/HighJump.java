package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventSendPacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.TimeHelper;
import me.Corbis.Execution.utils.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class HighJump extends Module {
    public Setting mode;
    TimeHelper stopwatch = new TimeHelper();
    public Setting speed;
    boolean fell;
    int stage;
    double moveSpeed;
    double lastDist;
    public HighJump(){
        super("HighJump", Keyboard.KEY_NONE, Category.MOVEMENT);
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Motion");
        Execution.instance.settingsManager.rSetting(mode = new Setting("HighJump mode", this, "Motion", options));

        Execution.instance.settingsManager.rSetting(speed = new Setting("HighJump Speed", this, 3, 0, 10, false));
    }


    @EventTarget
    public void onUpdate(EventMotionUpdate event){
        if(event.isPre()){
            if(mode.getValString().equalsIgnoreCase("Motion")){
                if(mc.thePlayer.motionY > 10){
                    mc.thePlayer.motionY = -20;
                }
            }else {

            }
        }
    }

    @EventTarget
    public void onMove(EventMotion event){
        if(mode.getValString().equalsIgnoreCase("Hypixel")){
            if(this.speed.getValDouble() != 10) {
                if (mc.thePlayer.fallDistance > 8 + this.speed.getValInt() * 4) {
                    fell = true;
                    MoveUtils.setMotion(event, 0);
                    event.setY(mc.thePlayer.motionY = this.speed.getValDouble());
                    mc.thePlayer.fallDistance = 0;
                }
            }else {
                if(mc.thePlayer.posY < 4){
                    fell = true;
                    MoveUtils.setMotion(event, 0);
                    event.setY(mc.thePlayer.motionY = this.speed.getValDouble());
                    mc.thePlayer.fallDistance = 0;
                }
            }
            if(fell){
                double baseMoveSpeed = MoveUtils.getBaseMoveSpeed();
                if(stage == 0) {
                    double difference = MoveUtils.WATCHDOG_BUNNY_SLOPE * (lastDist - baseMoveSpeed);
                    moveSpeed = lastDist - difference;
                    stage++;
                }else {
                    moveSpeed = MoveUtils.calculateFriction(moveSpeed, lastDist, baseMoveSpeed);
                }
            }
        }
    }

    boolean done = false;




    @Override
    public void onEnable(){
        super.onEnable();;
        stage = 0;
        lastDist = 0;
        moveSpeed = 0;
        fell = false;
        stopwatch.reset();;
    }


}
