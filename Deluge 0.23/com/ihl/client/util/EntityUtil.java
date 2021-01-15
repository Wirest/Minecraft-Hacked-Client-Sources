package com.ihl.client.util;

import com.ihl.client.Helper;
import com.ihl.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityUtil {

    public static Map<Entity, List<Vec3>> entityData = new HashMap();

    public static void captureEntities() {
        entityData.clear();

        if (!Module.get("esp").active && !Module.get("nametags").active) {
            return;
        }

        for (Object object : Helper.world().getLoadedEntityList()) {
            if (object instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) object;

                Vec3 position = getEntityRenderPosition(entity);

                AxisAlignedBB bounding = entity.getEntityBoundingBox();
                bounding = bounding.offset(-Helper.mc().getRenderManager().viewerPosX, -Helper.mc().getRenderManager().viewerPosY, -Helper.mc().getRenderManager().viewerPosZ);

                double ax = (bounding.maxX - bounding.minX) / 2;
                double ay = (bounding.maxY - bounding.minY);
                double az = (bounding.maxZ - bounding.minZ) / 2;

                List<Vec3> bounds = new ArrayList();
                bounds.add(position);
                bounds.add(position.addVector(0, entity.height/2, 0));
                bounds.add(position.addVector(0, entity.getEyeHeight(), 0));
                bounds.add(position.addVector(0, entity.height, 0));
                bounds.add(position.addVector(0, entity.height+0.2, 0));

                bounds.add(position.addVector(ax, ay, az));
                bounds.add(position.addVector(ax, ay, -az));
                bounds.add(position.addVector(-ax, ay, az));
                bounds.add(position.addVector(-ax, ay, -az));

                bounds.add(position.addVector(ax, 0, az));
                bounds.add(position.addVector(ax, 0, -az));
                bounds.add(position.addVector(-ax, 0, az));
                bounds.add(position.addVector(-ax, 0, -az));

                List<Vec3> data = new ArrayList();
                for(int i = 0; i < bounds.size(); i++) {
                    Vec3 coords = MathUtil.to2D(bounds.get(i));
                    if (coords != null) {
                        data.add(coords);
                    }
                }

                entityData.put(entity, data);
            }
        }
    }

    public static Vec3 getEntityWorldPosition(Entity entity) {
        double partial = Helper.mc().timer.elapsedPartialTicks;

        double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partial);
        double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partial);
        double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partial);

        return new Vec3(x, y, z);
    }

    public static Vec3 getEntityRenderPosition(Entity entity) {
        double partial = Helper.mc().timer.renderPartialTicks;

        double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partial) - Helper.mc().getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partial) - Helper.mc().getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partial) - Helper.mc().getRenderManager().viewerPosZ;

        return new Vec3(x, y, z);
    }

    public static Vec3 getTileEntityRenderPosition(TileEntity entity) {
        double x = entity.getPos().getX() - Helper.mc().getRenderManager().viewerPosX;
        double y = entity.getPos().getY() - Helper.mc().getRenderManager().viewerPosY;
        double z = entity.getPos().getZ() - Helper.mc().getRenderManager().viewerPosZ;

        return new Vec3(x, y, z);
    }

    public static double[] getRotationToEntity(Entity entity) {
        double pX = Helper.player().posX;
        double pY = Helper.player().posY + (Helper.player().getEyeHeight());
        double pZ = Helper.player().posZ;

        double eX = entity.posX;
        double eY = entity.posY + (entity.height/2);
        double eZ = entity.posZ;

        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2) + Math.pow(dZ, 2));

        double yaw = (Math.toDegrees(Math.atan2(dZ, dX)) + 90);
        double pitch = (Math.toDegrees(Math.atan2(dH, dY)));

        return new double[]{yaw, 90 - pitch};
    }

    public static double getRotationDifference(double[] rotation) {
        return Math.sqrt(Math.pow(Math.abs(MathUtil.angleDifference(Helper.player().rotationYaw % 360, rotation[0])), 2) + Math.pow(Math.abs(MathUtil.angleDifference(Helper.player().rotationPitch, rotation[1])), 2));
    }

    public static boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal;
    }

    public static boolean isMonster(Entity entity) {
        return entity instanceof IMob ||
                entity instanceof EntityDragon ||
                entity instanceof EntityGolem;
    }

    public static boolean isNeutral(Entity entity) {
        return entity instanceof EntityBat ||
                entity instanceof EntitySquid ||
                entity instanceof EntityVillager;
    }

    public static boolean isProjectile(Entity entity) {
        return entity instanceof IProjectile ||
                entity instanceof EntityFishHook;
    }

    public static String getEntityTypeName(Entity entity) {
        String type = null;
        if (isAnimal(entity)) {
            type = "animals";
        } else if (isMonster(entity)) {
            type = "monsters";
        } else if (isNeutral(entity)) {
            type = "neutrals";
        } else if (isProjectile(entity)) {
            type = "projectile";
        } else if (entity instanceof EntityPlayer) {
            type = "players";
        }
        return type;
    }

    public static String getTileEntityTypeName(TileEntity entity) {
        String type = null;
        Block block = entity.getBlockType();
        if (block == Blocks.chest || block == Blocks.trapped_chest) {
            type = "chests";
        } else if (block == Blocks.ender_chest) {
            type = "enderchests";
        } else if (block == Blocks.mob_spawner) {
            type = "mobspawners";
        } else if (block == Blocks.furnace || block == Blocks.lit_furnace || block == Blocks.dispenser || block == Blocks.dropper || block == Blocks.hopper) {
            type = "other";
        }
        return type;
    }

}
