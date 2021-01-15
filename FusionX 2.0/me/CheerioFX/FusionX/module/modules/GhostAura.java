// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import me.CheerioFX.FusionX.utils.EntityUtils;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiScreen;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class GhostAura extends Module
{
    float distance;
    float auraDelay;
    int delay;
    
    public GhostAura() {
        super("GhostAura", 41, Category.COMBAT);
        this.distance = 3.7f;
        this.auraDelay = 1.0f;
    }
    
    @EventTarget
    public void onPreMotionUpdate(final EventPreMotionUpdates event) {
        if (GhostAura.mc.thePlayer.isDead || GhostAura.mc.thePlayer.getHealth() <= 0.0f) {
            this.setState(false);
        }
        if (GhostAura.mc.currentScreen instanceof GuiScreen) {
            return;
        }
        this.killaura();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState() && (GhostAura.mc.thePlayer.isDead || GhostAura.mc.thePlayer.getHealth() <= 0.0f)) {
            this.setState(false);
        }
    }
    
    private void killaura() {
        this.distance = 4.0f;
        ++this.delay;
        final EntityLivingBase entityplayer = EntityUtils.getClosestEntity();
        final float f = GhostAura.mc.thePlayer.getDistanceToEntity(entityplayer);
        if (f < this.distance && GhostAura.mc.thePlayer.canEntityBeSeen(entityplayer) && entityplayer.getHealth() > 0.0f && !entityplayer.isDead) {
            this.faceEntity(entityplayer);
        }
        this.auraDelay = 1.0f;
        final double bypassdelay = this.auraDelay + (int)(Math.random() * (this.auraDelay + 2.0f - this.auraDelay + 1.0f));
        if (f < this.distance && GhostAura.mc.thePlayer.canEntityBeSeen(entityplayer) && this.delay > bypassdelay && entityplayer.getHealth() > 0.0f && !entityplayer.isDead) {
            for (int i = 0; i < 1; ++i) {
                GhostAura.mc.clickMouse();
            }
            this.delay = 1;
        }
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Jitter", this, false));
        FusionX.theClient.setmgr.rSetting(new Setting("AimAssist", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("AimAssist Slowness", this, 3.0, 1.0, 10.0, true));
    }
    
    private boolean isJitter() {
        return FusionX.theClient.setmgr.getSetting(this, "Jitter").getValBoolean();
    }
    
    private boolean isAimAssist() {
        return FusionX.theClient.setmgr.getSetting(this, "AimAssist").getValBoolean();
    }
    
    private double getAimAssistValue() {
        return FusionX.theClient.setmgr.getSetting(this, "AimAssist Slowness").getValInt();
    }
    
    private void faceEntity(final EntityLivingBase entity) {
        final float[] rotations = this.getRotationsNeeded(entity);
        final float jitterYaw = rotations[0] + 5.0f + (int)(Math.random() * (rotations[0] - 20.0f - rotations[0] + 5.0f + 1.0f)) - 1.0f;
        final float jitterPitch = rotations[1] + 5.0f + (int)(Math.random() * (rotations[1] - 20.0f - rotations[1] + 7.0f + 1.0f)) + 7.0f;
        if (rotations != null) {
            if (this.isJitter()) {
                if (this.isAimAssist()) {
                    final float math4Yaw = (float)((GhostAura.mc.thePlayer.rotationYaw - rotations[0]) / (this.getAimAssistValue() * 1.5));
                    final float math4Pitch = (float)((GhostAura.mc.thePlayer.rotationPitch - (rotations[1] + 15.0f)) / (this.getAimAssistValue() * 1.5));
                    final float JRY = 5 + (int)(Math.random() * -9.0);
                    final float JRP = 5 + (int)(Math.random() * -9.0);
                    final EntityPlayerSP thePlayer = GhostAura.mc.thePlayer;
                    thePlayer.rotationYaw -= math4Yaw + JRY;
                    final EntityPlayerSP thePlayer2 = GhostAura.mc.thePlayer;
                    thePlayer2.rotationPitch -= math4Pitch + JRP;
                }
                else {
                    GhostAura.mc.thePlayer.rotationYaw = jitterYaw;
                    GhostAura.mc.thePlayer.rotationPitch = jitterPitch + 1.0f;
                }
            }
            else if (this.isAimAssist()) {
                final EntityPlayerSP thePlayer3 = GhostAura.mc.thePlayer;
                thePlayer3.rotationYaw -= (float)((GhostAura.mc.thePlayer.rotationYaw - rotations[0]) / (this.getAimAssistValue() * 1.5));
                final EntityPlayerSP thePlayer4 = GhostAura.mc.thePlayer;
                thePlayer4.rotationPitch -= (float)((GhostAura.mc.thePlayer.rotationPitch - (rotations[1] + 10.0f)) / (this.getAimAssistValue() * 1.5));
            }
            else {
                final EntityPlayerSP thePlayer5 = GhostAura.mc.thePlayer;
                thePlayer5.rotationYaw -= (GhostAura.mc.thePlayer.rotationYaw - rotations[0]) / 1.0f;
                final EntityPlayerSP thePlayer6 = GhostAura.mc.thePlayer;
                thePlayer6.rotationPitch -= (GhostAura.mc.thePlayer.rotationPitch - (rotations[1] + 10.0f)) / 1.0f;
            }
        }
    }
    
    private float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
