// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import net.minecraft.entity.item.EntityArmorStand;
import me.CheerioFX.FusionX.GUI.clickgui.Restrictions;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import me.CheerioFX.FusionX.GUI.clickgui.Targets;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;

public class EntityUtils
{
    public static ArrayList<EntityPlayer> invalid;
    
    static {
        EntityUtils.invalid = new ArrayList<EntityPlayer>();
    }
    
    public static void blinkToPos(final double[] startPos, final BlockPos endPos, final double slack, final double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        final double endX = endPos.getX();
        final double endY = endPos.getY();
        final double endZ = endPos.getZ();
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > slack) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }
            final boolean next = false;
            final double diffX = curX - endX;
            final double diffY = curY - endY;
            final double diffZ = curZ - endZ;
            final double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
            if (diffX < 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                }
                else {
                    curX += Math.abs(diffX);
                }
            }
            if (diffX > 0.0) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                }
                else {
                    curX -= Math.abs(diffX);
                }
            }
            if (diffY < 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY += 0.25;
                }
                else {
                    curY += Math.abs(diffY);
                }
            }
            if (diffY > 0.0) {
                if (Math.abs(diffY) > 0.25) {
                    curY -= 0.25;
                }
                else {
                    curY -= Math.abs(diffY);
                }
            }
            if (diffZ < 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                }
                else {
                    curZ += Math.abs(diffZ);
                }
            }
            if (diffZ > 0.0) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                }
                else {
                    curZ -= Math.abs(diffZ);
                }
            }
            NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }
    
    public static void critical() {
        final double posY = Minecraft.getMinecraft().thePlayer.posY;
        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY + 0.0625, Minecraft.getMinecraft().thePlayer.posZ, true));
        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY, Minecraft.getMinecraft().thePlayer.posZ, false));
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY + 1.1E-5, Minecraft.getMinecraft().thePlayer.posZ, false));
        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, posY, Minecraft.getMinecraft().thePlayer.posZ, false));
    }
    
    public static void criticalAtPos(final double x, final double y, final double z) {
        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, true));
        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5, z, false));
        NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
    }
    
    public static void attackEntity(final EntityLivingBase entity, final boolean crit) {
        Minecraft.getMinecraft().thePlayer.swingItem();
        if (crit) {
            critical();
        }
        final float sharpLevel = EnchantmentHelper.func_152377_a(Minecraft.getMinecraft().thePlayer.getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = Minecraft.getMinecraft().thePlayer.fallDistance > 0.0f && !Minecraft.getMinecraft().thePlayer.onGround && !Minecraft.getMinecraft().thePlayer.isOnLadder() && !Minecraft.getMinecraft().thePlayer.isInWater() && !Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.blindness) && Minecraft.getMinecraft().thePlayer.ridingEntity == null;
        NetUtils.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            Minecraft.getMinecraft().thePlayer.onCriticalHit(entity);
        }
        if (sharpLevel > 0.0f) {
            Minecraft.getMinecraft().thePlayer.onEnchantmentCritical(entity);
        }
    }
    
    public static void attackEntityAtPos(final EntityLivingBase entity, final boolean crit, final double x, final double y, final double z) {
        Minecraft.getMinecraft().thePlayer.swingItem();
        if (crit) {
            criticalAtPos(x, y, z);
        }
        final float sharpLevel = EnchantmentHelper.func_152377_a(Minecraft.getMinecraft().thePlayer.getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = Minecraft.getMinecraft().thePlayer.fallDistance > 0.0f && !Minecraft.getMinecraft().thePlayer.onGround && !Minecraft.getMinecraft().thePlayer.isOnLadder() && !Minecraft.getMinecraft().thePlayer.isInWater() && !Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.blindness) && Minecraft.getMinecraft().thePlayer.ridingEntity == null;
        NetUtils.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            Minecraft.getMinecraft().thePlayer.onCriticalHit(entity);
        }
        if (sharpLevel > 0.0f) {
            Minecraft.getMinecraft().thePlayer.onEnchantmentCritical(entity);
        }
    }
    
    public static void damagePlayer(int damage) {
        if (damage < 1) {
            damage = 1;
        }
        if (damage > MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getMaxHealth())) {
            damage = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getMaxHealth());
        }
        final double offset = 0.0625;
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().thePlayer.onGround) {
            for (int i = 0; i <= (3 + damage) / 0.0625; ++i) {
                NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 0.0625, Minecraft.getMinecraft().thePlayer.posZ, false));
                NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, i == (3 + damage) / 0.0625));
            }
        }
    }
    
    public static ArrayList<EntityLivingBase> getValidEntities() {
        final ArrayList<EntityLivingBase> validEntities = new ArrayList<EntityLivingBase>();
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (o instanceof EntityLivingBase) {
                final EntityLivingBase en = (EntityLivingBase)o;
                boolean notABot;
                if (isAntiBot()) {
                    notABot = (en.isEntityAlive() && en.ticksExisted > 100 && !en.isInvisible() && en.posY - Wrapper.mc.thePlayer.posY != 3.609375);
                    if (en instanceof EntityPlayer && en.posY - Wrapper.mc.thePlayer.posY > 2.78 && en.posY - Wrapper.mc.thePlayer.posY < 4.36) {
                        notABot = false;
                    }
                }
                else {
                    notABot = true;
                }
                boolean notTeammate = true;
                if (Targets.noteams() && en instanceof EntityPlayer && (en.getDisplayName().toString().contains("§a") || !en.getDisplayName().toString().contains("§c"))) {
                    notTeammate = false;
                }
                if (o instanceof EntityPlayerSP || en.isDead || en.getHealth() <= 0.0f || en.getName().equals(Minecraft.getMinecraft().thePlayer.getName()) || !ModeUtils.isValidForAura(en) || !notABot || !notTeammate || (validEntities.contains(o) && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) >= Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en))) {
                    continue;
                }
                validEntities.add(en);
            }
        }
        return validEntities;
    }
    
    private static boolean isAntiBot() {
        return Targets.antibot() && !Restrictions.gcheat();
    }
    
    public static EntityLivingBase getClosestEntity() {
        EntityLivingBase closestEntity = null;
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (o instanceof EntityLivingBase) {
                final EntityLivingBase en = (EntityLivingBase)o;
                if (en instanceof EntityArmorStand && Restrictions.gcheat() && Wrapper.mc.thePlayer.getDistanceToEntity(en) < 4.256345634563456) {
                    attackEntity(en, false);
                    Wrapper.mc.theWorld.removeEntity(en);
                }
                boolean notABot;
                if (isAntiBot()) {
                    if (en instanceof EntityPlayer && en != Wrapper.mc.thePlayer && en.isInvisible() && en.ticksExisted > 205) {
                        en.ticksExisted = -1;
                        en.setInvisible(en.isDead = true);
                    }
                    if (o instanceof EntityPlayer) {
                        final EntityPlayer o2 = (EntityPlayer)o;
                        if (o2.isSwingInProgress && !EntityUtils.invalid.contains(o2)) {
                            EntityUtils.invalid.add(o2);
                        }
                    }
                    notABot = (en.isEntityAlive() && en.ticksExisted > 100 && !en.isInvisible());
                    if (!EntityUtils.invalid.contains(o)) {
                        notABot = false;
                    }
                }
                else {
                    notABot = true;
                }
                boolean notTeammate = true;
                if (Targets.noteams() && en instanceof EntityPlayer && (en.getDisplayName().toString().contains("§a") || !en.getDisplayName().toString().contains("§c"))) {
                    notTeammate = false;
                }
                if (en.isDead || en.getHealth() <= 0.0f || en.getName().equals(Minecraft.getMinecraft().thePlayer.getName()) || !ModeUtils.isValidForAura(en) || !notABot || (closestEntity != null && Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) >= Minecraft.getMinecraft().thePlayer.getDistanceToEntity(closestEntity))) {
                    continue;
                }
                closestEntity = en;
            }
        }
        return closestEntity;
    }
    
    public static synchronized boolean faceEntityClient(final Entity entity) {
        final float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            player.rotationYaw = limitAngleChange(player.prevRotationYaw, rotations[0], 55.0f);
            player.rotationPitch = rotations[1];
            return player.rotationYaw == rotations[0];
        }
        return true;
    }
    
    public static final float limitAngleChange(final float current, final float intended, final float maxChange) {
        float change = intended - current;
        if (change > maxChange) {
            change = maxChange;
        }
        else if (change < -maxChange) {
            change = -maxChange;
        }
        return current + change;
    }
    
    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        final double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
    }
}
