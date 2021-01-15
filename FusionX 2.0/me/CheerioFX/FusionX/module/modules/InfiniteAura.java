// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import me.CheerioFX.FusionX.events.EventPostMotionUpdates;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import java.util.ArrayList;
import me.CheerioFX.FusionX.utils.RotationUtils;
import net.minecraft.entity.Entity;
import me.CheerioFX.FusionX.utils.EntityUtils;
import me.CheerioFX.FusionX.GUI.clickgui.Restrictions;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.TimeHelper2;
import net.minecraft.entity.EntityLivingBase;
import me.CheerioFX.FusionX.utils.PathFinder.GotoAI;
import me.CheerioFX.FusionX.module.Module;

public class InfiniteAura extends Module
{
    private GotoAI ai;
    public EntityLivingBase target;
    private int entitycounter;
    private TimeHelper2 timer;
    private double startPosX;
    private double startPosY;
    private double startPosZ;
    
    public InfiniteAura() {
        super("InfiniteAura", 0, Category.COMBAT);
        this.entitycounter = 0;
    }
    
    private double getReach() {
        return FusionX.theClient.setmgr.getSetting(this, "reach").getValDouble();
    }
    
    private double getCPS() {
        return FusionX.theClient.setmgr.getSetting(this, "cps").getValDouble();
    }
    
    private boolean isbHit() {
        return FusionX.theClient.setmgr.getSetting(this, "blockhit").getValBoolean();
    }
    
    private boolean isLockView() {
        return FusionX.theClient.setmgr.getSetting(this, "lockview").getValBoolean();
    }
    
    private int getMaxTargets() {
        return FusionX.theClient.setmgr.getSetting(this, "max-targets").getValInt();
    }
    
    private boolean isMultiTarget() {
        return FusionX.theClient.setmgr.getSetting(this, "max-targets").getValDouble() != 1.0;
    }
    
    public boolean isTpBack() {
        return FusionX.theClient.setmgr.getSetting(this, "tpback").getValBoolean();
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Reach", this, 100.0, 1.0, 300.0, false));
        FusionX.theClient.setmgr.rSetting(new Setting("CPS", this, 13.0, 1.0, 20.0, false));
        FusionX.theClient.setmgr.rSetting(new Setting("Max-Targets", this, 1.0, 1.0, 50.0, true));
        FusionX.theClient.setmgr.rSetting(new Setting("TPBack", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("BlockHit", this, true));
        FusionX.theClient.setmgr.rSetting(new Setting("LockView", this, false));
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            if (!this.isMultiTarget()) {
                this.setExtraInfo("- Switch");
            }
            else {
                this.setExtraInfo("- Multi");
            }
        }
        super.onUpdate();
    }
    
    @EventTarget
    private void onPreMotionUpdate(final EventPreMotionUpdates event) {
        Restrictions.gcheat();
        if (!this.isMultiTarget()) {
            this.target = EntityUtils.getClosestEntity();
            if (this.target != null && InfiniteAura.mc.thePlayer.getDistanceToEntity(this.target) <= this.getReach()) {
                event.onGround = true;
                if (this.isLockView()) {
                    this.faceTarget(this.target);
                }
                else {
                    event.yaw = RotationUtils.getRotations(this.target)[0];
                    event.pitch = RotationUtils.getRotations(this.target)[1];
                }
            }
            else {
                this.target = null;
            }
        }
        else {
            if (!this.timer.hasPassed(1000.0 / this.getCPS())) {
                return;
            }
            final ArrayList<EntityLivingBase> entities = EntityUtils.getValidEntities();
            for (final EntityLivingBase entity : entities) {
                if (this.entitycounter >= this.getMaxTargets()) {
                    break;
                }
                if (entity != null && InfiniteAura.mc.thePlayer.getDistanceToEntity(entity) <= this.getReach()) {
                    this.target = entity;
                    event.onGround = true;
                    event.yaw = RotationUtils.getRotations(this.target)[0];
                    event.pitch = RotationUtils.getRotations(this.target)[1];
                    this.tpToEntity(entity);
                }
                else {
                    this.target = null;
                }
                ++this.entitycounter;
            }
            this.entitycounter = 0;
            this.timer.reset();
        }
    }
    
    @EventTarget
    public void onPostMotionUpdate(final EventPostMotionUpdates event) {
        if (!this.isMultiTarget() && this.target != null && InfiniteAura.mc.thePlayer.getDistanceToEntity(this.target) < this.getReach() && this.timer.hasPassed(1000.0 / this.getCPS())) {
            this.tpToEntity(this.target);
            this.timer.reset();
        }
        if (!EntityUtils.getValidEntities().isEmpty() && EntityUtils.getValidEntities() != null && InfiniteAura.mc.thePlayer.getHeldItem() != null && this.target != null && InfiniteAura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && this.isbHit()) {
            InfiniteAura.mc.thePlayer.setItemInUse(InfiniteAura.mc.thePlayer.getHeldItem(), InfiniteAura.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
        }
    }
    
    private void faceTarget(final EntityLivingBase entity) {
        final float[] rotations = RotationUtils.getRotations(entity);
        if (rotations != null) {
            Minecraft.getMinecraft().thePlayer.rotationYaw = rotations[0];
            Minecraft.getMinecraft().thePlayer.rotationPitch = rotations[1] + 1.0f;
        }
    }
    
    public void tpToEntity(final EntityLivingBase entity) {
        if (entity != null) {
            final double oldPosX = InfiniteAura.mc.thePlayer.posX;
            final double oldPosY = InfiniteAura.mc.thePlayer.posY;
            final double oldPosZ = InfiniteAura.mc.thePlayer.posZ;
            (this.ai = new GotoAI(entity)).update("infiniteaura");
            if (this.ai.isDone() || this.ai.isFailed()) {
                this.ai.isFailed();
                this.disable();
            }
            InfiniteAura.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(oldPosX, oldPosY, oldPosZ, true));
        }
    }
    
    private void disable() {
        this.ai.stop();
    }
    
    public void i(final String o) {
        final FusionX theClient = FusionX.theClient;
        FusionX.addChatMessage(o);
    }
    
    @Override
    public void onEnable() {
        this.timer = new TimeHelper2();
        this.entitycounter = 0;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        final Timer timer = InfiniteAura.mc.timer;
        Timer.timerSpeed = 1.0f;
        this.entitycounter = 0;
        super.onDisable();
    }
    
    public float[] getRotationsNeeded(final Entity entity) {
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
}
