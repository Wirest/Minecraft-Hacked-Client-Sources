// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.util.UUID;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import java.util.ArrayList;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class EntityUtils2
{
    public static boolean lookChanged;
    public static float yaw;
    public static float pitch;
    public static final TargetSettings DEFAULT_SETTINGS;
    
    static {
        DEFAULT_SETTINGS = new TargetSettings();
    }
    
    public static synchronized boolean faceEntityPacket(final Entity entity) {
        final float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            EntityUtils2.yaw = limitAngleChange(EntityUtils2.yaw, rotations[0], 30.0f);
            EntityUtils2.pitch = rotations[1];
            return EntityUtils2.yaw == rotations[0];
        }
        return true;
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
    
    public static ArrayList<BlockPos> func1(final Entity en) {
        final BlockPos pos1 = new BlockPos(en.boundingBox.minX, en.boundingBox.minY - 0.01, en.boundingBox.minZ);
        final BlockPos pos2 = new BlockPos(en.boundingBox.maxX, en.boundingBox.minY - 0.01, en.boundingBox.maxZ);
        final Iterable<BlockPos> collisionBlocks = (Iterable<BlockPos>)BlockPos.getAllInBoxMutable(pos1, pos2);
        final ArrayList<BlockPos> returnList = new ArrayList<BlockPos>();
        for (final BlockPos pos3 : collisionBlocks) {
            returnList.add(pos3);
        }
        return returnList;
    }
    
    public static boolean func2(final Entity en) {
        final ArrayList<BlockPos> poses = func1(en);
        for (final BlockPos pos : poses) {
            final Block block = Wrapper.mc.theWorld.getBlockState(pos).getBlock();
            if (!(block.getMaterial() instanceof MaterialTransparent) && block.getMaterial() != Material.air && !(block instanceof BlockLiquid) && block.isFullCube()) {
                return true;
            }
        }
        return false;
    }
    
    public static int getDistanceFromMouse(final Entity entity) {
        final float[] neededRotations = getRotationsNeeded(entity);
        if (neededRotations != null) {
            final float neededYaw = Minecraft.getMinecraft().thePlayer.rotationYaw - neededRotations[0];
            final float neededPitch = Minecraft.getMinecraft().thePlayer.rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
            return (int)distanceFromMouse;
        }
        return -1;
    }
    
    public static boolean isCorrectEntity(final Entity en, final TargetSettings settings) {
        if (en == null) {
            return false;
        }
        if (en instanceof EntityLivingBase && (((EntityLivingBase)en).isDead || ((EntityLivingBase)en).getHealth() <= 0.0f)) {
            return false;
        }
        if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) > settings.getRange()) {
            return false;
        }
        if (settings.getFOV() < 360.0f && getDistanceFromMouse(en) > settings.getFOV() / 2.0f) {
            return false;
        }
        if (!settings.targetBehindWalls() && !Minecraft.getMinecraft().thePlayer.canEntityBeSeen(en)) {
            return false;
        }
        if (en instanceof EntityPlayer) {
            if (!settings.targetPlayers()) {
                if (!((EntityPlayer)en).isPlayerSleeping() && !((EntityPlayer)en).isInvisible()) {
                    return false;
                }
            }
            else if (!settings.targetSleepingPlayers()) {
                if (((EntityPlayer)en).isPlayerSleeping()) {
                    return false;
                }
            }
            else if (!settings.targetInvisiblePlayers() && ((EntityPlayer)en).isInvisible()) {
                return false;
            }
            if (settings.targetTeams() && !checkName(((EntityPlayer)en).getDisplayName().getFormattedText(), settings.getTeamColors())) {
                return false;
            }
            if (en == Minecraft.getMinecraft().thePlayer) {
                return false;
            }
            if (((EntityPlayer)en).getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
                return false;
            }
        }
        else {
            if (!(en instanceof EntityLiving)) {
                return false;
            }
            if (((EntityLiving)en).isInvisible()) {
                if (!settings.targetInvisibleMobs()) {
                    return false;
                }
            }
            else if (en instanceof EntityAgeable || en instanceof EntityAmbientCreature || en instanceof EntityWaterMob) {
                if (!settings.targetAnimals()) {
                    return false;
                }
            }
            else if (en instanceof EntityMob || en instanceof EntitySlime || en instanceof EntityFlying) {
                if (!settings.targetMonsters()) {
                    return false;
                }
            }
            else {
                if (!(en instanceof EntityGolem)) {
                    return false;
                }
                if (!settings.targetGolems()) {
                    return false;
                }
            }
            if (settings.targetTeams() && ((EntityLiving)en).hasCustomName() && !checkName(((EntityLiving)en).getCustomNameTag(), settings.getTeamColors())) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean checkName(final String name, final boolean[] teamColors) {
        final String[] colors = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        boolean hasKnownColor = false;
        for (int i = 0; i < 16; ++i) {
            if (name.contains("§" + colors[i])) {
                hasKnownColor = true;
            }
        }
        return !hasKnownColor;
    }
    
    public static Entity getEntityWithName(final String name, final TargetSettings settings) {
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            final Entity entity = (Entity)o;
            if (isCorrectEntity(entity, settings) && entity.getName().equalsIgnoreCase(name)) {
                return entity;
            }
        }
        return null;
    }
    
    public static Entity getEntityWithId(final UUID id, final TargetSettings settings) {
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            final Entity entity = (Entity)o;
            if (isCorrectEntity(entity, settings) && entity.getUniqueID().equals(id)) {
                return entity;
            }
        }
        return null;
    }
    
    public static class TargetSettings
    {
        public boolean targetFriends() {
            return false;
        }
        
        public boolean targetBehindWalls() {
            return false;
        }
        
        public float getRange() {
            return Float.POSITIVE_INFINITY;
        }
        
        public float getFOV() {
            return 360.0f;
        }
        
        public boolean targetPlayers() {
            return true;
        }
        
        public boolean targetAnimals() {
            return false;
        }
        
        public boolean targetMonsters() {
            return false;
        }
        
        public boolean targetGolems() {
            return false;
        }
        
        public boolean targetSleepingPlayers() {
            return false;
        }
        
        public boolean targetInvisiblePlayers() {
            return false;
        }
        
        public boolean targetInvisibleMobs() {
            return false;
        }
        
        public boolean targetTeams() {
            return false;
        }
        
        public boolean[] getTeamColors() {
            return null;
        }
    }
}
