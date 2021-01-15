package me.xatzdevelopments.xatz.utils;

import net.minecraft.entity.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.material.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;
import java.util.*;

public class EntityUtils
{
    public static boolean lookChanged;
    public static float yaw;
    public static float pitch;
    static Minecraft mc;

    public static synchronized void faceEntityClient(final EntityLivingBase entity) {
        final float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            
            Minecraft.getMinecraft();
            
            Minecraft.getMinecraft();
            mc.thePlayer.rotationYaw = limitAngleChange(mc.thePlayer.prevRotationYaw, rotations[0], 55.0f);
            
            Minecraft.getMinecraft();
            mc.thePlayer.rotationPitch = rotations[1];
        }
    }

    public static void attackEntity(final EntityLivingBase entity) {
        
        Minecraft.getMinecraft();
        mc.thePlayer.swingItem();
        
        Minecraft.getMinecraft();
        mc.playerController.attackEntity(mc.thePlayer, entity);
    }

    public static void doCritical() {
        
        Minecraft.getMinecraft();
        if (mc.thePlayer.isInWater()) {
            
            Minecraft.getMinecraft();
            if (mc.thePlayer.isInsideOfMaterial(Material.lava)) {
                
                Minecraft.getMinecraft();
                if (mc.thePlayer.onGround) {
                    
                    Minecraft.getMinecraft();
                    mc.thePlayer.motionY = 0.10000000149011612;
                    
                    Minecraft.getMinecraft();
                    mc.thePlayer.fallDistance = 0.1f;
                    
                    Minecraft.getMinecraft();
                    mc.thePlayer.onGround = false;
                }
            }
        }
    }

    public static synchronized float getDistanceToEntity(final EntityLivingBase entity) {
        return mc.thePlayer.getDistanceToEntity(entity);
    }

    public static synchronized void faceEntityPacket(final EntityLivingBase entity) {
        final float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            
            Minecraft.getMinecraft();
            EntityUtils.yaw = limitAngleChange(mc.thePlayer.prevRotationYaw, rotations[0], 55.0f);
            EntityUtils.pitch = rotations[1];
            EntityUtils.lookChanged = true;
        }
    }

    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        
        Minecraft.getMinecraft();
        final double diffX = entity.posX - mc.thePlayer.posX;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            
            Minecraft.getMinecraft();
            
            Minecraft.getMinecraft();
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        else {
            
            Minecraft.getMinecraft();
            
            Minecraft.getMinecraft();
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        
        Minecraft.getMinecraft();
        final double diffZ = entity.posZ - mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        final float[] tmp194_192 = new float[2];
        
        Minecraft.getMinecraft();
        
        Minecraft.getMinecraft();
        tmp194_192[0] = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(tmp194_192[0] - mc.thePlayer.rotationYaw);
        final float[] tmp224_194 = tmp194_192;
        
        Minecraft.getMinecraft();
        
        Minecraft.getMinecraft();
        tmp224_194[1] = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(tmp224_194[1] - mc.thePlayer.rotationPitch);
        return tmp224_194;
    }

    private static final float limitAngleChange(final float current, final float intended, final float maxChange) {
        float change = intended - current;
        if (change > maxChange) {
            change = maxChange;
        }
        else if (change < -maxChange) {
            change = -maxChange;
        }
        return current + change;
    }

    public static int getDistanceFromMouse(final Entity entity) {
        final float[] neededRotations = getRotationsNeeded(entity);
        if (neededRotations != null) {
            
            Minecraft.getMinecraft();
            final float neededYaw = mc.thePlayer.rotationYaw - neededRotations[0];
            
            Minecraft.getMinecraft();
            final float neededPitch = mc.thePlayer.rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
            return (int)distanceFromMouse;
        }
        return -1;
    }

    private static boolean checkName(final String name) {
        final String[] colors = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        boolean unknownColor = true;
        for (int i2 = 0; i2 < 16; ++i2) {
            if (name.contains("?" + colors[i2])) {
                return true;
            }
            unknownColor = false;
        }
        return unknownColor || !name.contains("?");
    }

    public static EntityLivingBase getClosestEntity(final boolean ignoreFriends, final boolean useFOV) {
        EntityLivingBase closestEntity = null;
        
        Minecraft.getMinecraft();
        for (final Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof EntityLivingBase && getDistanceFromMouse((Entity)o) <= 180) {
                final EntityLivingBase en = (EntityLivingBase)o;
                if (o instanceof EntityPlayerSP || en.isDead || en.getHealth() <= 0.0f) {
                    continue;
                }
                
                Minecraft.getMinecraft();
                if (mc.thePlayer.canEntityBeSeen(en)) {
                    continue;
                }
                
                Minecraft.getMinecraft();
                if (en.getName().equals(mc.thePlayer.getName())) {
                    continue;
                }
                if (closestEntity != null) {
                    
                    Minecraft.getMinecraft();
                    
                    Minecraft.getMinecraft();
                    mc.thePlayer.getDistanceToEntity(en);
                    mc.thePlayer.getDistanceToEntity(closestEntity);
                }
                else {
                    closestEntity = en;
                }
            }
        }
        return closestEntity;
    }

    public static ArrayList<EntityLivingBase> getCloseEntities(final float range) {
        final ArrayList<EntityLivingBase> closeEntities = new ArrayList<EntityLivingBase>();
        
        Minecraft.getMinecraft();
        for (final Object o : mc.theWorld.loadedEntityList) {
            final EntityLivingBase en2 = (EntityLivingBase)o;
            if (!(o instanceof EntityPlayerSP) && !en2.isDead && en2.getHealth() > 0.0f) {
                
                Minecraft.getMinecraft();
                if (mc.thePlayer.canEntityBeSeen(en2)) {
                    continue;
                }
                
                Minecraft.getMinecraft();
                if (en2.getName().equals(mc.thePlayer.getName())) {
                    continue;
                }
                
                Minecraft.getMinecraft();
                if (mc.thePlayer.getDistanceToEntity(en2) > range) {
                    continue;
                }
                closeEntities.add(en2);
            }
        }
        return closeEntities;
    }

    public static EntityLivingBase getClosestEntityRaw() {
        EntityLivingBase closestEntity = null;
        
        Minecraft.getMinecraft();
        for (final Object o : mc.theWorld.loadedEntityList) {
            final EntityLivingBase en2 = (EntityLivingBase)o;
            if (!(o instanceof EntityPlayerSP) && !en2.isDead && en2.getHealth() > 0.0f) {
                if (closestEntity != null) {
                    
                    Minecraft.getMinecraft();
                    
                    Minecraft.getMinecraft();
                    mc.thePlayer.getDistanceToEntity(en2);
                    mc.thePlayer.getDistanceToEntity(closestEntity);
                }
                else {
                    closestEntity = en2;
                }
            }
        }
        return closestEntity;
    }

    public static EntityLivingBase getClosestEnemy(final EntityLivingBase friend) {
        EntityLivingBase closestEnemy = null;
        
        Minecraft.getMinecraft();
        for (final Object o : mc.theWorld.loadedEntityList) {
            final EntityLivingBase en2 = (EntityLivingBase)o;
            if (!(o instanceof EntityPlayerSP) && o != friend && !en2.isDead && en2.getHealth() > 0.0f) {
                
                Minecraft.getMinecraft();
                if (mc.thePlayer.canEntityBeSeen(en2)) {
                    continue;
                }
                if (closestEnemy != null) {
                    
                    Minecraft.getMinecraft();
                    
                    Minecraft.getMinecraft();
                    mc.thePlayer.getDistanceToEntity(en2);
                    mc.thePlayer.getDistanceToEntity(closestEnemy);
                }
                else {
                    closestEnemy = en2;
                }
            }
        }
        return closestEnemy;
    }

    public static EntityLivingBase searchEntityByIdRaw(final UUID ID) {
        EntityLivingBase newEntity = null;
        Minecraft.getMinecraft();
        for (final Object o : mc.theWorld.loadedEntityList) {
            final EntityLivingBase en2 = (EntityLivingBase)o;
            if (!(o instanceof EntityPlayerSP) && !en2.isDead && newEntity == null && en2.getUniqueID().equals(ID)) {
                newEntity = en2;
            }
        }
        return newEntity;
    }

    public static EntityLivingBase searchEntityByName(final String name) {
        EntityLivingBase newEntity = null;
        Minecraft.getMinecraft();
        for (final Object o : mc.theWorld.loadedEntityList) {
            final EntityLivingBase en2 = (EntityLivingBase)o;
            if (!(o instanceof EntityPlayerSP) && !en2.isDead) {
                Minecraft.getMinecraft();
                if (mc.thePlayer.canEntityBeSeen(en2) || newEntity != null || !en2.getName().equals(name)) {
                    continue;
                }
                newEntity = en2;
            }
        }
        return newEntity;
    }

    public static EntityLivingBase searchEntityByNameRaw(final String name) {
        EntityLivingBase newEntity = null;
        
        Minecraft.getMinecraft();
        for (final Object o : mc.theWorld.loadedEntityList) {
            final EntityLivingBase en2 = (EntityLivingBase)o;
            if (!(o instanceof EntityPlayerSP) && !en2.isDead && newEntity == null && en2.getName().equals(name)) {
                newEntity = en2;
            }
        }
        return newEntity;
    }
}
