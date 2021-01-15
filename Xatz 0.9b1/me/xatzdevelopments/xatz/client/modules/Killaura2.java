package me.xatzdevelopments.xatz.client.modules;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.EventMotion;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.CheckBtnSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.KillauraUtil;
import me.xatzdevelopments.xatz.utils.Timer;

public class Killaura2 extends Module {

    KillauraUtil aurautil = new KillauraUtil();
    Timer time = new Timer();
    Entity finalEntity = null;

    float rotationYaw = 0;
    float rotationPitch = 0;

    int range;
    int delay;
    int delayRandom;
    int yawSpeed;
    int pitchSpeed;
    int fov;

    boolean teams;
    boolean walls;
    boolean players;
    boolean animals;
    boolean stopSprint;
    boolean checkRotations;

    public Killaura2() {
        super("Killaura2", 0, Category.FUN, "Attack entities");
    }

    @Override
    public ModSetting[] getModSettings() {
    	SliderSetting<Number> Killaura2range = new SliderSetting<Number>("Range", ClientSettings.Killaura2range, 3.5, 7, 0.0, ValueFormat.INT);
    	SliderSetting<Number> Killaura2delay = new SliderSetting<Number>("Delay", ClientSettings.Killaura2delay, 1, 150, 0.0, ValueFormat.DECIMAL);
    	SliderSetting<Number> Killaura2delayrandom = new SliderSetting<Number>("Delay Randomness", ClientSettings.Killaura2delayrandom, 0, 150, 0.0, ValueFormat.DECIMAL);
    	SliderSetting<Number> Killaura2yawspeed = new SliderSetting<Number>("Yaw", ClientSettings.Killaura2yawspeed, 1, 180, 0.0, ValueFormat.DECIMAL);
    	SliderSetting<Number> Killaura2pitchspeed = new SliderSetting<Number>("Pitch", ClientSettings.Killaura2pitchspeed, 1, 180, 0.0, ValueFormat.DECIMAL);
    	SliderSetting<Number> Killaura2fov = new SliderSetting<Number>("Pitch", ClientSettings.Killaura2fov, 1, 360, 0.0, ValueFormat.DECIMAL);
      
    	CheckBtnSetting killaura2teams = new CheckBtnSetting("Teams", "killaura2teams");
    	CheckBtnSetting killaura2walls = new CheckBtnSetting("Through walls", "killaura2walls");
    	CheckBtnSetting killaura2players = new CheckBtnSetting("Players", "killaura2players");
    	CheckBtnSetting killaura2animals = new CheckBtnSetting("Animals", "killaura2animals");
    	CheckBtnSetting killaura2keepsprint = new CheckBtnSetting("KeepSprint", "killaura2keepsprint");
    	CheckBtnSetting killaura2stopsprint = new CheckBtnSetting("StopSprint", "killaura2stopsprint");
    	CheckBtnSetting killaura2checkrotations = new CheckBtnSetting("Rotations", "killaura2checkrotations");
       
        
       
       
        
        return new ModSetting[] { Killaura2range, Killaura2delay, Killaura2delayrandom, Killaura2yawspeed, Killaura2pitchspeed, Killaura2fov, killaura2teams, killaura2walls, killaura2players, killaura2animals, killaura2keepsprint, killaura2stopsprint, killaura2checkrotations };
    }

    public void setValues() {
        range = ClientSettings.Killaura2range;
        delay = ClientSettings.Killaura2delay;
        delayRandom = ClientSettings.Killaura2delayrandom;
        yawSpeed = ClientSettings.Killaura2yawspeed;
        pitchSpeed =  ClientSettings.Killaura2pitchspeed;
        fov =  ClientSettings.Killaura2fov;

        teams = ClientSettings.killaura2teams;
        walls = ClientSettings.killaura2walls;
        players = ClientSettings.killaura2players;
        animals = ClientSettings.killaura2animals;
        stopSprint = ClientSettings.killaura2stopsprint;
        checkRotations = ClientSettings.killaura2checkrotations;
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity e = (Entity) o;
            if (e != null && finalEntity == null && isChecked(e)) {
                finalEntity = e;
            }
        }

        if (finalEntity.isDead || mc.thePlayer.getDistanceToEntity(finalEntity) > (float) range) {
            finalEntity = null;
        }

        setRotations();
        if (stopSprint)
            slowdownMovement();
    }

    @Override
    public void onEvent(EventMotion event) {
        if (finalEntity == null)
            return;
        event.setYaw(rotationYaw);
		event.setPitch(rotationPitch);
        mc.thePlayer.rotationYawHead = rotationYaw;
        mc.thePlayer.rotationPitch = rotationPitch;
        attack();
    }

    public void attack() {
        if (finalEntity == null)
            return;
        if (checkRotations && !isOver())
            return;
        if (time.isDelayComplete((long) (delay + aurautil.getRandomDouble(0, delayRandom)))) {
            mc.playerController.attackEntity(mc.thePlayer, aurautil.raycast(range, finalEntity));
            mc.thePlayer.swingItem();
            mc.effectRenderer.emitParticleAtEntity(finalEntity, EnumParticleTypes.CRIT);
            mc.effectRenderer.emitParticleAtEntity(finalEntity, EnumParticleTypes.CRIT_MAGIC);
            finalEntity = null;
            time.setLastMs();
        }
    }

    public void setRotations() {
        if (finalEntity == null)
            return;
        float[] smoothRotations = aurautil.faceEntitySmooth(rotationYaw, rotationPitch, aurautil.rotations(finalEntity)[0], aurautil.rotations(finalEntity)[1], yawSpeed, pitchSpeed);
        rotationYaw = aurautil.updateRotation(mc.thePlayer.rotationYaw, smoothRotations[0], fov);
        rotationPitch = smoothRotations[1];
        if(rotationPitch > 90) {
            rotationPitch = 90;
        } else if (rotationPitch < -90) {
            rotationPitch = -90;
        }
    }

    public boolean isChecked(Entity e) {
        if (e == mc.thePlayer)
            return false;

        if (!aurautil.checkEntityID(e))
            return false;

        if (e.isInvisible())
            return false;

        if (!(e instanceof EntityLivingBase))
            return false;

        if (!players && e instanceof EntityPlayer)
            return false;

        if (!animals && !aurautil.isInTablist(e))
            return false;

        if (!walls && !(mc.thePlayer.canEntityBeSeen(e)))
            return false;

        if (mc.thePlayer.getDistanceToEntity(e) > range)
            return false;

        if (teams && (e.getDisplayName().getFormattedText()
                .startsWith("§" + mc.thePlayer.getDisplayName().getFormattedText().charAt(1))
                || e.getName().equalsIgnoreCase(""))) {
            return false;
        }

        return true;
    }

    private boolean isOver() {
        if (finalEntity == null)
            return false;

        float[] smoothRotations = aurautil.faceEntitySmooth(rotationYaw, rotationPitch, aurautil.rotations(finalEntity)[0], aurautil.rotations(finalEntity)[1], 360, 360);

        if (rotationYaw <= smoothRotations[0] + 10 && rotationYaw >= smoothRotations[0] - 10 && rotationPitch <= smoothRotations[1] + 5 && rotationPitch >= smoothRotations[1] - 5) {
            return true;
        }

        return false;
    }

    private void slowdownMovement() {
        if (finalEntity == null)
            return;
        if (mc.thePlayer.hurtTime < 1) {
            double distance = Math.abs(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) - aurautil.rotations(finalEntity)[0]);
            if (distance >= 360 / fov * 10.0D) {
                mc.thePlayer.motionX /= 1.25;
                mc.thePlayer.motionZ /= 1.25;
            }
        }
    }

    @Override
    public void onEnable() {
        rotationYaw = mc.thePlayer.rotationYaw;
        rotationPitch = mc.thePlayer.rotationPitch;
        finalEntity = null;
        setValues();
        
    }

    @Override
    public void onDisable() {
        finalEntity = null;
        
    }
}