package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventStep;
import me.Corbis.Execution.event.events.StepPostEvent;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.MoveUtils2;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Step extends Module {
    int stage = 0;

    public Setting mode;
    public Setting timer;
    public Setting smooth;
    double stepY = 0, stepX = 0, stepZ = 0;
    boolean isStep = false;
    double jumpGround = 0;
    public Step(){
        super("Step", Keyboard.KEY_I, Category.MOVEMENT);
        ArrayList<String> options = new ArrayList<>();
        options.add("Spider");
        options.add("NCP");
        Execution.instance.settingsManager.rSetting(mode = new Setting("Step Mode", this, "Spider", options));

        Execution.instance.settingsManager.rSetting(smooth = new Setting("Step Smooth", this, false));
        Execution.instance.settingsManager.rSetting(timer = new Setting("Step timer", this, 0.5, 0.0, 1, false));
    }

    @EventTarget
    public void onStep(EventMotion event){

        if(mode.getValString().equalsIgnoreCase("Spider")){
            if(mc.thePlayer.isCollidedHorizontally){
                if(mc.thePlayer.onGround && shouldStep()){
                    fakeJump();
                    event.y = 0.41999998688698;
                    stage = 1;
                    return;
                }
                if(stage == 1){
                    event.y = 0.7531999805212 - 0.41999998688698;

                    stage = 2;
                    return;
                }

                if(stage == 2){
                    double yaw = MoveUtils.getDirection();
                    event.y = 1.001335979112147 - 0.7531999805212;

                    event.x = -sin(yaw) * 0.7;
                    event.z = cos(yaw) * 0.7;
                    stage = 4;
                }
            }else if(stage == 4){
                MoveUtils.setMotion(event, MoveUtils.getBaseMoveSpeed());
                stage = 0;
            }


        }
    }
    public void fakeJump(){
        mc.thePlayer.isAirBorne = true;
        mc.thePlayer.triggerAchievement(StatList.jumpStat);

    }
    int ticks = 2;
    @EventTarget
    public void onStep(EventStep event){
        if(!Execution.instance.moduleManager.getModule(Speed.class).isEnabled && mode.getValString().equalsIgnoreCase("NCP")) {
            mc.thePlayer.stepHeight = 1;
            event.setStepHeight(1);
            if (ticks == 1) {
                mc.timer.timerSpeed = 1.0f;
                ticks = 2;
            }
            if (event.getStepHeight() > 0.625) {
                isStep = true;
                stepY = mc.thePlayer.posY;
                stepX = mc.thePlayer.posX;
                stepZ = mc.thePlayer.posZ;
            }
        }else {
            mc.thePlayer.stepHeight = 0.625f;
        }
    }

    @EventTarget
    public void onStepConfirm(StepPostEvent event){
        if(mode.getValString().equalsIgnoreCase("NCP")){
            if(mc.thePlayer.getEntityBoundingBox().minY - stepY > 0.625 && !Execution.instance.moduleManager.getModule(Speed.class).isEnabled){
                fakeJump();
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(stepX, stepY + 0.41999998688698, stepZ,false));
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(stepX, stepY + 0.7531999805212, stepZ, false));
                isStep = false;
                mc.timer.timerSpeed = (float) this.timer.getValDouble();
                ticks = 1;
            }
        }
    }

    public boolean shouldStep(){
        double yaw = MoveUtils.getDirection();
        double x = -sin(yaw) * 0.4;
        double z = cos(yaw) * 0.4;
        return mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,mc.thePlayer.getEntityBoundingBox().offset(x, 1.001335979112147, z)).isEmpty();
    }
    @Override
    public void onDisable(){
        super.onDisable();
        mc.thePlayer.stepHeight = 0.625f;
        stage = 0;

    }
}
