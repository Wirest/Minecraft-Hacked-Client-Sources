package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.RotationUtils;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;




public class AACAura extends Module {

    EntityLivingBase target;

    TimeHelper timer = new TimeHelper();
    public AACAura(){
        super("AACAura", Keyboard.KEY_NONE, Category.COMBAT);
    }

    public static int randomNumber(int max, int min) {
        return Math.round(min + (float) Math.random() * ((max - min)));
    }

    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event){
        if(event.isPre()){
            target = getClosest(3.5);

            if(target == null)
                return;
            mc.thePlayer.setSprinting(false);


            float[] rots = RotationUtils.getRotations(target);
            mc.thePlayer.setYaw(rots[0] + randomNumber(-1, 1));
            mc.thePlayer.setPitch(rots[1] + randomNumber(-1, 1));

            if(timer.hasReached(randomClickDelay(6, 10))){
                mc.playerController.attackEntity(mc.thePlayer, target);
                mc.thePlayer.swingItem();
                timer.reset();
            }
        }


    }
    private EntityLivingBase getClosest(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (canAttack(player)) {
                    double currentDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                    }
                }
            }
        }
        return target;
    }
    public static long randomClickDelay(final double minCPS, final double maxCPS) {
        return (long) ((Math.random() * (1000 / minCPS - 1000 / maxCPS + 1)) + 1000 / maxCPS);
    }



    public boolean canAttack(Entity e){
        if(Execution.instance.moduleManager.getModuleByName("Players").isEnabled && !(e instanceof EntityPlayer))
            return false;
        if(Execution.instance.moduleManager.getModuleByName("Mobs").isEnabled && !(e instanceof EntityLivingBase))
            return false;
        if(Execution.instance.moduleManager.getModuleByName("Invisibles").isEnabled && !e.isInvisible())
            return false;
        if(Execution.instance.moduleManager.getModuleByName("Villagers").isEnabled && !(e instanceof EntityVillager))
            return false;
        if(e == mc.thePlayer)
            return false;
        if(mc.thePlayer.getDistanceToEntity(e) > 3.25)
            return false;


        return true;
    }
}
