package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventRenderEntity;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

public class FPSBoost extends Module {

    public Setting adaptiveRenderDistance;
    public Setting removeBots;
    public Setting crappyModels;
    public Setting clearBots;
    public Setting clearFarEntities;

    public FPSBoost(){
        super("FPSBoost", Keyboard.KEY_NONE, Category.RENDER);
        Execution.instance.settingsManager.rSetting(adaptiveRenderDistance = new Setting("Adaptive Render Distance", this, false));
        Execution.instance.settingsManager.rSetting(crappyModels = new Setting("Crappy Models", this, false));
        Execution.instance.settingsManager.rSetting(removeBots = new Setting("Remove Bots", this, false));
        Execution.instance.settingsManager.rSetting(clearBots = new Setting("Clear Bots", this, false));
        Execution.instance.settingsManager.rSetting(clearFarEntities = new Setting("Clear Far Entities", this, false));


    }

    public static boolean clear = false;

    @EventTarget
    public void onUpdate(EventUpdate event){
        clear = crappyModels.getValBoolean();
        if(adaptiveRenderDistance.getValBoolean()) {
            EntityLivingBase entity = getFarthest(16 * 6);
            if (entity == null) {
                mc.gameSettings.renderDistanceChunks = 4;
            } else {
                mc.gameSettings.renderDistanceChunks = mc.thePlayer.getDistanceToEntity(entity) > 16 * 6 ? 6 : (int) (mc.thePlayer.getDistanceToEntity(entity) / 16);
            }
        }
        for(EntityLivingBase e : AntiBot.bots) {
            if(removeBots.getValBoolean()) {
                mc.theWorld.removeEntity(e);
            }
        }
        if(AntiBot.bots.size() > 10 && clearBots.getValBoolean()){
            AntiBot.bots.clear();
        }

    }

    @EventTarget
    public void onRenderPlayer(EventRenderEntity event){
        if(event.getEntity().getDistanceToEntity(mc.thePlayer) > 120 && clearFarEntities.getValBoolean()){
            event.setCancelled(true);
        }
    }

    private EntityLivingBase getFarthest(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                double currentDist = mc.thePlayer.getDistanceToEntity(player);
                if (currentDist >= dist) {
                    dist = currentDist;
                    target = player;
                }

            }
        }
        return target;
    }

}
