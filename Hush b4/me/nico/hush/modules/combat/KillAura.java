// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.combat;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Iterator;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import me.nico.hush.utils.PlayerUtils;
import me.nico.hush.utils.RandomUtil;
import me.nico.hush.utils.KillAuraUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.PreUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.RayCast;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class KillAura extends Module
{
    public TimeHelper hitdelay;
    public TimeHelper blockdelay;
    public TimeHelper time;
    private RayCast rayCast;
    private static double blockrange;
    private static double wallrange;
    private float yaw;
    private float pitch;
    
    static {
        KillAura.blockrange = 3.35;
        KillAura.wallrange = 1.75;
    }
    
    public KillAura() {
        super("KillAura", "KillAura", 1048575, 37, Category.COMBAT);
        this.hitdelay = new TimeHelper();
        this.blockdelay = new TimeHelper();
        this.time = new TimeHelper();
        this.rayCast = new RayCast();
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("CubeCraft");
        mode.add("Normal");
        mode.add("Tick");
        mode.add("AAC");
        Client.instance.settingManager.rSetting(new Setting("Mode", "KillAuraMode", this, "Normal", mode));
        Client.instance.settingManager.rSetting(new Setting("Animals", this, false));
        Client.instance.settingManager.rSetting(new Setting("Mobs", this, true));
        Client.instance.settingManager.rSetting(new Setting("Villager", this, false));
        Client.instance.settingManager.rSetting(new Setting("Shop", this, false));
        Client.instance.settingManager.rSetting(new Setting("Dealer", this, false));
        Client.instance.settingManager.rSetting(new Setting("AntiBots", this, false));
        Client.instance.settingManager.rSetting(new Setting("AutoBlock", this, true));
        Client.instance.settingManager.rSetting(new Setting("Walls", this, false));
        Client.instance.settingManager.rSetting(new Setting("Range §l\u2192", "KillAuraRange", this, 4.2, 1.0, 6.0, false));
        Client.instance.settingManager.rSetting(new Setting("Delay §l\u2192", "KillAuraDelay", this, 12.0, 1.0, 180.0, true));
        Client.instance.settingManager.rSetting(new Setting("Ticks §l\u2192", "KillAuraTicks", this, 3.0, 1.0, 20.0, true));
    }
    
    @EventTarget
    public void onUpdate(final PreUpdate event) {
        if (Client.instance.settingManager.getSettingByName("KillAuraMode").getValString().equalsIgnoreCase("Normal")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("KillAura[Normal]");
            }
            else {
                this.setDisplayname("KillAura");
            }
            this.Normal(event);
        }
        else if (Client.instance.settingManager.getSettingByName("KillAuraMode").getValString().equalsIgnoreCase("CubeCraft")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("KillAura[CC]");
            }
            else {
                this.setDisplayname("KillAura");
            }
            this.CC(event);
        }
        else if (Client.instance.settingManager.getSettingByName("KillAuraMode").getValString().equalsIgnoreCase("AAC")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("KillAura[AAC]");
            }
            else {
                this.setDisplayname("KillAura");
            }
            this.AAC(event);
        }
        else if (Client.instance.settingManager.getSettingByName("KillAuraMode").getValString().equalsIgnoreCase("Tick")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("KillAura[Tick]");
            }
            else {
                this.setDisplayname("KillAura");
            }
            this.Tick(event);
        }
    }
    
    private void Tick(final PreUpdate update) {
        for (final Entity e : KillAura.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                final Entity entity = e;
                final Minecraft mc = KillAura.mc;
                if (entity == Minecraft.thePlayer || !isValid(e)) {
                    continue;
                }
                final Minecraft mc2 = KillAura.mc;
                if (Minecraft.thePlayer.getDistanceToEntity(e) > Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble()) {
                    continue;
                }
                final Entity p = this.rayCast.raycast(e);
                final Minecraft mc3 = KillAura.mc;
                if (Minecraft.thePlayer.ticksExisted > Client.instance.settingManager.getSettingByName("KillAuraTicks").getValDouble()) {
                    final Entity entity2 = e;
                    final Minecraft mc4 = KillAura.mc;
                    if (entity2.getDistanceToEntity(Minecraft.thePlayer) <= Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble() && KillAuraUtils.INSTANCE.canAttack(e)) {
                        final float addRandom = RandomUtil.randomAngle(1.0f, 15.0f);
                        final float[] rotations = KillAuraUtils.INSTANCE.getRotations(KillAuraUtils.INSTANCE.getCenter(e.getEntityBoundingBox()));
                        final float[] smooth = KillAuraUtils.INSTANCE.faceEntitySmooth(this.yaw, this.pitch, rotations[0], rotations[1], 53.0, 53.0);
                        final KillAuraUtils instance = KillAuraUtils.INSTANCE;
                        final Minecraft mc5 = KillAura.mc;
                        this.yaw = instance.updateRotation(Minecraft.thePlayer.rotationYaw, smooth[0], 360.0f);
                        this.pitch = smooth[1];
                        if (this.pitch > 90.0f) {
                            this.pitch = 90.0f;
                        }
                        else if (this.pitch < -90.0f) {
                            this.pitch = -90.0f;
                        }
                        update.setYaw(this.yaw);
                        update.setPitch(this.pitch);
                        final Minecraft mc6 = KillAura.mc;
                        Minecraft.thePlayer.renderYawOffset = this.yaw + addRandom;
                        final Minecraft mc7 = KillAura.mc;
                        Minecraft.thePlayer.rotationYawHead = this.yaw + addRandom;
                        KillAuraUtils.setPitching(true);
                        KillAuraUtils.INSTANCE.setPitch(this.pitch);
                    }
                    if (PlayerUtils.playeriswalking()) {
                        final Minecraft mc8 = KillAura.mc;
                        final float rotationYaw = Minecraft.thePlayer.rotationYaw;
                        final Minecraft mc9 = KillAura.mc;
                        final double distance = Math.abs(MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw) - rotationYaw);
                        if (distance >= 10.0) {
                            final Minecraft mc10 = KillAura.mc;
                            Minecraft.thePlayer.setSprinting(false);
                            final Minecraft mc11 = KillAura.mc;
                            final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                            thePlayer.motionX /= 1.125;
                            final Minecraft mc12 = KillAura.mc;
                            final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                            thePlayer2.motionZ /= 1.125;
                        }
                    }
                    final Minecraft mc13 = KillAura.mc;
                    Minecraft.thePlayer.swingItem();
                    final PlayerControllerMP playerController = KillAura.mc.playerController;
                    final Minecraft mc14 = KillAura.mc;
                    playerController.attackEntity(Minecraft.thePlayer, this.rayCast.raycast(e));
                    final Minecraft mc15 = KillAura.mc;
                    Minecraft.thePlayer.ticksExisted = 0;
                    final NetHandlerPlayClient netHandler = KillAura.mc.getNetHandler();
                    final Minecraft mc16 = KillAura.mc;
                    netHandler.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
                if (!Client.instance.settingManager.getSettingByName("AutoBlock").getValBoolean()) {
                    continue;
                }
                final Minecraft mc17 = KillAura.mc;
                if (Minecraft.thePlayer.onGround) {
                    final Minecraft mc18 = KillAura.mc;
                    if (Minecraft.thePlayer.getHeldItem() != null) {
                        final Minecraft mc19 = KillAura.mc;
                        if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            final Minecraft mc20 = KillAura.mc;
                            if (Minecraft.thePlayer.getDistanceToEntity(e) < 2.25) {
                                final Minecraft mc21 = KillAura.mc;
                                final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
                                final Minecraft mc22 = KillAura.mc;
                                thePlayer3.setItemInUse(Minecraft.thePlayer.getHeldItem(), 200);
                                if (((EntityLivingBase)e).deathTime != 0) {
                                    continue;
                                }
                                final EntityLivingBase entityLivingBase = (EntityLivingBase)e;
                                final Minecraft mc23 = KillAura.mc;
                                Minecraft.thePlayer.swingItem();
                                final Minecraft mc24 = KillAura.mc;
                                Minecraft.thePlayer.ticksExisted = 0;
                                TimeHelper.reset();
                            }
                            else {
                                final Minecraft mc25 = KillAura.mc;
                                Minecraft.thePlayer.swingItem();
                                final Minecraft mc26 = KillAura.mc;
                                Minecraft.thePlayer.ticksExisted = 0;
                            }
                        }
                        else {
                            final Minecraft mc27 = KillAura.mc;
                            Minecraft.thePlayer.swingItem();
                            final Minecraft mc28 = KillAura.mc;
                            Minecraft.thePlayer.ticksExisted = 0;
                        }
                    }
                    else {
                        final Minecraft mc29 = KillAura.mc;
                        Minecraft.thePlayer.swingItem();
                        final Minecraft mc30 = KillAura.mc;
                        Minecraft.thePlayer.ticksExisted = 0;
                    }
                }
                else {
                    final Minecraft mc31 = KillAura.mc;
                    Minecraft.thePlayer.swingItem();
                    final Minecraft mc32 = KillAura.mc;
                    Minecraft.thePlayer.ticksExisted = 0;
                }
            }
        }
    }
    
    private void Normal(final PreUpdate update) {
        for (final Entity e : KillAura.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                final Entity entity = e;
                final Minecraft mc = KillAura.mc;
                if (entity == Minecraft.thePlayer || !isValid(e)) {
                    continue;
                }
                final Minecraft mc2 = KillAura.mc;
                if (Minecraft.thePlayer.getDistanceToEntity(e) > Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble()) {
                    continue;
                }
                final Entity p = this.rayCast.raycast(e);
                if (!TimeHelper.hasReached((long)Client.instance.settingManager.getSettingByName("KillAuraDelay").getValDouble())) {
                    continue;
                }
                final Entity entity2 = e;
                final Minecraft mc3 = KillAura.mc;
                if (entity2.getDistanceToEntity(Minecraft.thePlayer) <= Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble() && KillAuraUtils.INSTANCE.canAttack(e)) {
                    final float addRandom = RandomUtil.randomAngle(1.0f, 15.0f);
                    final float[] rotations = KillAuraUtils.INSTANCE.getRotations(KillAuraUtils.INSTANCE.getCenter(e.getEntityBoundingBox()));
                    final float[] smooth = KillAuraUtils.INSTANCE.faceEntitySmooth(this.yaw, this.pitch, rotations[0], rotations[1], 50.0, 50.0);
                    final KillAuraUtils instance = KillAuraUtils.INSTANCE;
                    final Minecraft mc4 = KillAura.mc;
                    this.yaw = instance.updateRotation(Minecraft.thePlayer.rotationYaw, smooth[0], 360.0f);
                    this.pitch = smooth[1];
                    if (this.pitch > 90.0f) {
                        this.pitch = 90.0f;
                    }
                    else if (this.pitch < -90.0f) {
                        this.pitch = -90.0f;
                    }
                    update.setYaw(this.yaw);
                    update.setPitch(this.pitch);
                    final Minecraft mc5 = KillAura.mc;
                    Minecraft.thePlayer.renderYawOffset = this.yaw + addRandom;
                    final Minecraft mc6 = KillAura.mc;
                    Minecraft.thePlayer.rotationYawHead = this.yaw + addRandom;
                    KillAuraUtils.setPitching(true);
                    KillAuraUtils.INSTANCE.setPitch(this.pitch);
                }
                final Minecraft mc7 = KillAura.mc;
                Minecraft.thePlayer.swingItem();
                final Minecraft mc8 = KillAura.mc;
                Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                TimeHelper.reset();
                if (Client.instance.settingManager.getSettingByName("AutoBlock").getValBoolean()) {
                    final Minecraft mc9 = KillAura.mc;
                    if (Minecraft.thePlayer.getHeldItem() != null) {
                        final Minecraft mc10 = KillAura.mc;
                        if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            final Minecraft mc11 = KillAura.mc;
                            if (Minecraft.thePlayer.getDistanceToEntity(e) < KillAura.blockrange) {
                                final Minecraft mc12 = KillAura.mc;
                                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                                final Minecraft mc13 = KillAura.mc;
                                thePlayer.setItemInUse(Minecraft.thePlayer.getHeldItem(), 200);
                                if (((EntityLivingBase)e).deathTime == 0) {
                                    final EntityLivingBase entityLivingBase = (EntityLivingBase)e;
                                    final Minecraft mc14 = KillAura.mc;
                                    Minecraft.thePlayer.swingItem();
                                    TimeHelper.reset();
                                    TimeHelper.reset();
                                }
                            }
                            else {
                                final Minecraft mc15 = KillAura.mc;
                                Minecraft.thePlayer.swingItem();
                                TimeHelper.reset();
                            }
                        }
                        else {
                            final Minecraft mc16 = KillAura.mc;
                            Minecraft.thePlayer.swingItem();
                            TimeHelper.reset();
                        }
                    }
                    else {
                        final Minecraft mc17 = KillAura.mc;
                        Minecraft.thePlayer.swingItem();
                        TimeHelper.reset();
                    }
                }
                else {
                    final Minecraft mc18 = KillAura.mc;
                    Minecraft.thePlayer.swingItem();
                    TimeHelper.reset();
                }
                if (Client.instance.moduleManager.getModuleName("Criticals").isEnabled() && Client.instance.settingManager.getSettingByName("CriticalsMode").getValString().equalsIgnoreCase("Jump")) {
                    final Minecraft mc19 = KillAura.mc;
                    if (!Minecraft.thePlayer.onGround) {
                        continue;
                    }
                    final Minecraft mc20 = KillAura.mc;
                    Minecraft.thePlayer.motionY = 0.1;
                }
                else {
                    if (!Client.instance.moduleManager.getModuleName("Criticals").isEnabled() || !Client.instance.settingManager.getSettingByName("CriticalsMode").getValString().equalsIgnoreCase("Packet")) {
                        continue;
                    }
                    final Minecraft mc21 = KillAura.mc;
                    final NetHandlerPlayClient sendQueue = Minecraft.thePlayer.sendQueue;
                    final Minecraft mc22 = KillAura.mc;
                    final double posX = Minecraft.thePlayer.posX;
                    final Minecraft mc23 = KillAura.mc;
                    final double posY = Minecraft.thePlayer.posY + 0.01;
                    final Minecraft mc24 = KillAura.mc;
                    sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, Minecraft.thePlayer.posZ, false));
                    final Minecraft mc25 = KillAura.mc;
                    final NetHandlerPlayClient sendQueue2 = Minecraft.thePlayer.sendQueue;
                    final Minecraft mc26 = KillAura.mc;
                    final double posX2 = Minecraft.thePlayer.posX;
                    final Minecraft mc27 = KillAura.mc;
                    final double posY2 = Minecraft.thePlayer.posY + 0.01;
                    final Minecraft mc28 = KillAura.mc;
                    sendQueue2.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX2, posY2, Minecraft.thePlayer.posZ, false));
                }
            }
        }
    }
    
    private void AAC(final PreUpdate update) {
        for (final Entity e : KillAura.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                final Entity entity = e;
                final Minecraft mc = KillAura.mc;
                if (entity == Minecraft.thePlayer || !((EntityLivingBase)e).isEntityAlive() || !isValid(e)) {
                    continue;
                }
                final Minecraft mc2 = KillAura.mc;
                if (Minecraft.thePlayer.getDistanceToEntity(e) > Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble()) {
                    continue;
                }
                final Entity p = this.rayCast.raycast(e);
                final Minecraft mc3 = KillAura.mc;
                if (Minecraft.thePlayer.ticksExisted <= Client.instance.settingManager.getSettingByName("KillAuraTicks").getValDouble()) {
                    continue;
                }
                final Entity entity2 = e;
                final Minecraft mc4 = KillAura.mc;
                if (entity2.getDistanceToEntity(Minecraft.thePlayer) <= Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble() && KillAuraUtils.INSTANCE.canAttack(e)) {
                    final float addRandom = RandomUtil.randomAngle(1.0f, 15.0f);
                    final float[] rotations = KillAuraUtils.INSTANCE.getRotations(KillAuraUtils.INSTANCE.getCenter(e.getEntityBoundingBox()));
                    final float[] smooth = KillAuraUtils.INSTANCE.faceEntitySmooth(this.yaw, this.pitch, rotations[0], rotations[1], 40.0, 40.0);
                    final KillAuraUtils instance = KillAuraUtils.INSTANCE;
                    final Minecraft mc5 = KillAura.mc;
                    this.yaw = instance.updateRotation(Minecraft.thePlayer.rotationYaw, smooth[0], 360.0f);
                    this.pitch = smooth[1];
                    if (this.pitch > 90.0f) {
                        this.pitch = 90.0f;
                    }
                    else if (this.pitch < -90.0f) {
                        this.pitch = -90.0f;
                    }
                    update.setYaw(this.yaw);
                    update.setPitch(this.pitch);
                    final Minecraft mc6 = KillAura.mc;
                    Minecraft.thePlayer.renderYawOffset = this.yaw + addRandom;
                    final Minecraft mc7 = KillAura.mc;
                    Minecraft.thePlayer.rotationYawHead = this.yaw + addRandom;
                    KillAuraUtils.setPitching(true);
                    KillAuraUtils.INSTANCE.setPitch(this.pitch);
                }
                final Minecraft mc8 = KillAura.mc;
                Minecraft.thePlayer.swingItem();
                final PlayerControllerMP playerController = KillAura.mc.playerController;
                final Minecraft mc9 = KillAura.mc;
                playerController.attackEntity(Minecraft.thePlayer, this.rayCast.raycast(e));
                TimeHelper.reset();
                if (Client.instance.settingManager.getSettingByName("AutoBlock").getValBoolean()) {
                    final Minecraft mc10 = KillAura.mc;
                    if (Minecraft.thePlayer.getHeldItem() != null) {
                        final Minecraft mc11 = KillAura.mc;
                        if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            final Minecraft mc12 = KillAura.mc;
                            if (Minecraft.thePlayer.getDistanceToEntity(e) < 2.25) {
                                final Minecraft mc13 = KillAura.mc;
                                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                                final Minecraft mc14 = KillAura.mc;
                                thePlayer.setItemInUse(Minecraft.thePlayer.getHeldItem(), 200);
                                if (((EntityLivingBase)e).deathTime != 0) {
                                    continue;
                                }
                                final EntityLivingBase entityLivingBase = (EntityLivingBase)e;
                                final Minecraft mc15 = KillAura.mc;
                                Minecraft.thePlayer.swingItem();
                                TimeHelper.reset();
                                TimeHelper.reset();
                            }
                            else {
                                final Minecraft mc16 = KillAura.mc;
                                Minecraft.thePlayer.swingItem();
                                TimeHelper.reset();
                            }
                        }
                        else {
                            final Minecraft mc17 = KillAura.mc;
                            Minecraft.thePlayer.swingItem();
                            TimeHelper.reset();
                        }
                    }
                    else {
                        final Minecraft mc18 = KillAura.mc;
                        Minecraft.thePlayer.swingItem();
                        TimeHelper.reset();
                    }
                }
                else {
                    final Minecraft mc19 = KillAura.mc;
                    Minecraft.thePlayer.swingItem();
                    TimeHelper.reset();
                }
            }
        }
    }
    
    public void CC(final PreUpdate update) {
        for (final Entity e : KillAura.mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                final Entity entity = e;
                final Minecraft mc = KillAura.mc;
                if (entity == Minecraft.thePlayer || !((EntityLivingBase)e).isEntityAlive() || !isValid(e) || !PlayerUtils.playeriswalking()) {
                    continue;
                }
                final Minecraft mc2 = KillAura.mc;
                if (Minecraft.thePlayer.getDistanceToEntity(e) > Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble()) {
                    continue;
                }
                final Entity p = this.rayCast.raycast(e);
                final Minecraft mc3 = KillAura.mc;
                if (Minecraft.thePlayer.ticksExisted <= Client.instance.settingManager.getSettingByName("KillAuraDelay").getValDouble()) {
                    continue;
                }
                final Entity entity2 = e;
                final Minecraft mc4 = KillAura.mc;
                if (entity2.getDistanceToEntity(Minecraft.thePlayer) <= Client.instance.settingManager.getSettingByName("KillAuraRange").getValDouble() && KillAuraUtils.INSTANCE.canAttack(e)) {
                    final float addRandom = RandomUtil.randomAngle(1.0f, 15.0f);
                    final float[] rotations = KillAuraUtils.INSTANCE.getRotations(KillAuraUtils.INSTANCE.getCenter(e.getEntityBoundingBox()));
                    final float[] smooth = KillAuraUtils.INSTANCE.faceEntitySmooth(this.yaw, this.pitch, rotations[0], rotations[1], 60.0, 60.0);
                    final KillAuraUtils instance = KillAuraUtils.INSTANCE;
                    final Minecraft mc5 = KillAura.mc;
                    this.yaw = instance.updateRotation(Minecraft.thePlayer.rotationYaw, smooth[0], 360.0f);
                    this.pitch = smooth[1];
                    if (this.pitch > 90.0f) {
                        this.pitch = 90.0f;
                    }
                    else if (this.pitch < -90.0f) {
                        this.pitch = -90.0f;
                    }
                    update.setYaw(this.yaw);
                    update.setPitch(this.pitch);
                    final Minecraft mc6 = KillAura.mc;
                    Minecraft.thePlayer.renderYawOffset = this.yaw + addRandom;
                    final Minecraft mc7 = KillAura.mc;
                    Minecraft.thePlayer.rotationYawHead = this.yaw + addRandom;
                    KillAuraUtils.setPitching(true);
                    KillAuraUtils.INSTANCE.setPitch(this.pitch);
                }
                final Minecraft mc8 = KillAura.mc;
                Minecraft.thePlayer.swingItem();
                final PlayerControllerMP playerController = KillAura.mc.playerController;
                final Minecraft mc9 = KillAura.mc;
                playerController.attackEntity(Minecraft.thePlayer, this.rayCast.raycast(e));
                TimeHelper.reset();
                if (Client.instance.settingManager.getSettingByName("AutoBlock").getValBoolean()) {
                    final Minecraft mc10 = KillAura.mc;
                    if (Minecraft.thePlayer.getHeldItem() != null) {
                        final Minecraft mc11 = KillAura.mc;
                        if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            final Minecraft mc12 = KillAura.mc;
                            if (Minecraft.thePlayer.getDistanceToEntity(e) < KillAura.blockrange) {
                                final Minecraft mc13 = KillAura.mc;
                                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                                final Minecraft mc14 = KillAura.mc;
                                thePlayer.setItemInUse(Minecraft.thePlayer.getHeldItem(), 200);
                                if (!TimeHelper.hasReached(1000L) || ((EntityLivingBase)e).deathTime != 0) {
                                    continue;
                                }
                                final EntityLivingBase entityLivingBase = (EntityLivingBase)e;
                                final Minecraft mc15 = KillAura.mc;
                                Minecraft.thePlayer.swingItem();
                                TimeHelper.reset();
                                TimeHelper.reset();
                            }
                            else {
                                final Minecraft mc16 = KillAura.mc;
                                if (Minecraft.thePlayer.hurtTime > 0.8) {
                                    KillAura.blockrange = 2.75;
                                    final Minecraft mc17 = KillAura.mc;
                                    Minecraft.thePlayer.swingItem();
                                }
                                else {
                                    KillAura.blockrange = 2.5;
                                    final Minecraft mc18 = KillAura.mc;
                                    Minecraft.thePlayer.swingItem();
                                }
                            }
                        }
                        else {
                            final Minecraft mc19 = KillAura.mc;
                            Minecraft.thePlayer.swingItem();
                            TimeHelper.reset();
                        }
                    }
                    else {
                        final Minecraft mc20 = KillAura.mc;
                        Minecraft.thePlayer.swingItem();
                        TimeHelper.reset();
                    }
                }
                else {
                    final Minecraft mc21 = KillAura.mc;
                    Minecraft.thePlayer.swingItem();
                    TimeHelper.reset();
                }
            }
        }
    }
    
    public static boolean isValid(final Entity e) {
        if (!(e instanceof EntityLivingBase)) {
            return false;
        }
        if (e.isDead) {
            return false;
        }
        final Minecraft mc = KillAura.mc;
        if (e == Minecraft.thePlayer) {
            return false;
        }
        if (!Client.instance.settingManager.getSettingByName("Animals").getValBoolean() && e instanceof EntityAnimal) {
            return false;
        }
        if (!Client.instance.settingManager.getSettingByName("Mobs").getValBoolean() && e instanceof EntityMob) {
            return false;
        }
        if (!Client.instance.settingManager.getSettingByName("Villager").getValBoolean() && e instanceof EntityVillager) {
            return false;
        }
        if (!Client.instance.settingManager.getSettingByName("Walls").getValBoolean()) {
            final Minecraft mc2 = KillAura.mc;
            if (!Minecraft.thePlayer.canEntityBeSeen(e)) {
                return false;
            }
            if (Client.instance.settingManager.getSettingByName("Walls").getValBoolean()) {
                final Minecraft mc3 = KillAura.mc;
                if (Minecraft.thePlayer.getDistanceToEntity(e) < KillAura.wallrange) {
                    return true;
                }
            }
        }
        if (!Client.instance.settingManager.getSettingByName("Shop").getValBoolean() && e.getDisplayName().getUnformattedText().contains("Shop")) {
            return false;
        }
        if (!Client.instance.settingManager.getSettingByName("Dealer").getValBoolean() && e.getDisplayName().getUnformattedText().contains("Dealer")) {
            return false;
        }
        if (Client.instance.settingManager.getSettingByName("AntiBots").getValBoolean() && ((EntityLivingBase)e).getHealth() > 0.0f) {
            return false;
        }
        Client.instance.moduleManager.getModuleName("Teams").isEnabled();
        if (e instanceof EntityPlayer && Teams.teams.contains(e)) {
            return false;
        }
        if (e instanceof EntityArmorStand) {
            return false;
        }
        final EntityLivingBase p = (EntityLivingBase)e;
        return true;
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        TimeHelper.reset();
    }
    
    @Override
    public void onDisable() {
        TimeHelper.reset();
        TimeHelper.reset();
        KillAuraUtils.setPitching(false);
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        KillAura.mc.gameSettings.keyBindUseItem.pressed = false;
        EventManager.unregister(this);
    }
}
