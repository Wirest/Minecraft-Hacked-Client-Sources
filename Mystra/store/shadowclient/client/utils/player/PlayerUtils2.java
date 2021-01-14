package store.shadowclient.client.utils.player;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerUtils2 {
    public static final double SPRINT_WALK_SPEED = 0.153210663;
    
    public static float getInitialBowVelocity(final EntityPlayer entityPlayer) {
        final float n = (entityPlayer.getHeldItem().getItem().getMaxItemUseDuration(entityPlayer.getHeldItem()) - entityPlayer.getItemInUseCount()) / 20.0f;
        float n2;
        if (entityPlayer.isUsingItem()) {
            n2 = (n * n + n * 2.0f) / 3.0f;
        }
        else {
            n2 = 1.0f;
        }
        return n2;
    }
    
    public static ArrayList<Entity> getClosestEntities(final double n, final boolean b) {
        final ArrayList<Entity> list = new ArrayList<Entity>();
        for (final Entity entity3 : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity3 != Minecraft.getMinecraft().thePlayer) {
                if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity3) > n) {
                    continue;
                }
                if (b && !(entity3 instanceof EntityLivingBase)) {
                    continue;
                }
                list.add(entity3);
            }
        }
        list.sort((entity, entity2) -> {
            if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) > Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity2)) {
                return 1;
            }
            else if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) < Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity2)) {
                return -1;
            }
            else {
                return 0;
            }
        });
        return list;
    }
    
    public static boolean shouldSprint() {
        return isPlayerMovingIntentionally() && !Minecraft.getMinecraft().thePlayer.isSneaking() && Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() > 6;
    }
    
    public static boolean isPlayerMovingIntentionally() {
        return Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() || Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown() || Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown() || Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown() || Minecraft.getMinecraft().thePlayer.movementInput.moveForward != 0.0f || Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe != 0.0f;
    }
    
    public static boolean isPlayerMoving() {
        return isPlayerMovingIntentionally() || Minecraft.getMinecraft().thePlayer.moveForward != 0.0f || Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0f || Minecraft.getMinecraft().thePlayer.motionX != 0.0 || Minecraft.getMinecraft().thePlayer.motionZ != 0.0 || Minecraft.getMinecraft().thePlayer.motionY != 0.0;
    }
    
    
    
    public static int getBlocksDownwards(final EntityPlayer entityPlayer) {
        for (int i = 0; i < 100; ++i) {
            if (Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY - i, entityPlayer.posZ)).getBlock().getMaterial() != Material.air) {
                return i - 1;
            }
        }
        return 0;
    }
    
    public static boolean canWalkThrough(final BlockPos blockPos) {
        return getBlock(blockPos).getCollisionBoundingBox((World)Minecraft.getMinecraft().theWorld, blockPos, getBlockState(blockPos)) == null || getBlock(blockPos).getCollisionBoundingBox((World)Minecraft.getMinecraft().theWorld, blockPos, getBlockState(blockPos)).getAverageEdgeLength() == 0.0;
    }
    
    public static IBlockState getBlockState(final BlockPos blockPos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
    }
    
    public static Block getBlock(final BlockPos blockPos) {
        final Block block = getBlockState(blockPos).getBlock();
        return (block == null) ? Blocks.air : block;
    }
    
    public static float getYawByMovement() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
        final float moveForward = movementInput.moveForward;
        final float moveStrafe = movementInput.moveStrafe;
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
            }
            else if (moveStrafe <= -1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
            }
        }
        return rotationYaw;
    }
    
    public static float getRandomFloat(final float n, final float n2) {
        return n + new Random().nextFloat() * (n2 - n);
    }
    
    public static double[] getMotionXZ(final double n) {
        final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
        float moveForward = movementInput.moveForward;
        float moveStrafe = movementInput.moveStrafe;
        final float yawByMovement = getYawByMovement();
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                moveStrafe = 0.0f;
            }
            else if (moveStrafe <= -1.0f) {
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double cos = Math.cos(Math.toRadians(yawByMovement + 90.0f));
        final double sin = Math.sin(Math.toRadians(yawByMovement + 90.0f));
        return new double[] { moveForward * n * cos + moveStrafe * n * sin, moveForward * n * sin - moveStrafe * n * cos };
    }
    
    public static void setSpeed(final double n) {
        final double[] motionXZ = getMotionXZ(n);
        final double motionX = motionXZ[0];
        final double motionZ = motionXZ[1];
        Minecraft.getMinecraft().thePlayer.motionX = motionX;
        Minecraft.getMinecraft().thePlayer.motionZ = motionZ;
    }
    
    /*public static MovingObjectPosition rayTrace(final Entity entity, final float n, final float n2, final double n3, final float n4) {
        final Vec3 positionEyes = entity.getPositionEyes(n4);
        final Vec3 vectorForRotation = entity.getVectorForRotation(n2, n);
        return entity.worldObj.rayTraceBlocks(positionEyes, positionEyes.addVector(vectorForRotation.xCoord * n3, vectorForRotation.yCoord * n3, vectorForRotation.zCoord * n3), false, false, true);
    }
    
    public static MovingObjectPosition rayTraceEntity(final Entity entity, final double n, final float n2, final float n3) {
        final Vec3 positionEyes = entity.getPositionEyes(Minecraft.getMinecraft().timer.renderPartialTicks);
        final Vec3 look = entity.getLook(n2);
        return rayTraceEntity(entity, positionEyes.addVector(look.xCoord * 0.25, look.yCoord * 0.25, look.zCoord * 0.25), positionEyes.addVector(look.xCoord * n, look.yCoord * n, look.zCoord * n));
    }
    
    /*public static MovingObjectPosition rayTraceEntity(final Entity entity, final Vec3 vec3, final Vec3 vec4) {
        final MovingObjectPosition rayTraceBlocks = Minecraft.getMinecraft().theWorld.rayTraceBlocks(vec3, vec4, false, true, true);
        Entity entity2 = null;
        final List entitiesWithinAABB = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB((Class)EntityLivingBase.class, new AxisAlignedBB(vec3.xCoord - 0.5, vec3.yCoord - 0.5, vec3.zCoord - 0.5, vec4.xCoord + 0.5, vec4.yCoord + 0.5, vec4.zCoord + 0.5).expand(1.0, 1.0, 1.0));
        double n = 0.0;
        for (final EntityLivingBase entityLivingBase : entitiesWithinAABB) {
            if (entityLivingBase.canBeCollidedWith() && entityLivingBase != entity) {
                final double n2 = 0.2;
                final MovingObjectPosition calculateIntercept = entityLivingBase.getEntityBoundingBox().expand(n2, n2, n2).calculateIntercept(vec3, vec4);
                if (calculateIntercept == null) {
                    continue;
                }
                final double squareDistanceTo = vec4.squareDistanceTo(calculateIntercept.hitVec);
                if (squareDistanceTo >= n && n != 0.0) {
                    continue;
                }
                entity2 = (Entity)entityLivingBase;
                n = squareDistanceTo;
            }
        }
        return (entity2 != null) ? new MovingObjectPosition(entity2) : rayTraceBlocks;
    }*/
    
    public static Vec3 getAABBCenter(final AxisAlignedBB axisAlignedBB) {
        return getAABBCenter(axisAlignedBB, 0.0, 0.0, 0.0);
    }
    
    public static Vec3 getAABBCenter(final AxisAlignedBB axisAlignedBB, final double n, final double n2, final double n3) {
        final double n4 = ThreadLocalRandom.current().nextBoolean() ? (Math.random() * 0.1) : (-(Math.random() * 0.1));
        final double n5 = ThreadLocalRandom.current().nextBoolean() ? (Math.random() * 0.1) : (-(Math.random() * 0.1));
        final double n6 = ThreadLocalRandom.current().nextBoolean() ? (Math.random() * 0.1) : (-(Math.random() * 0.1));
        axisAlignedBB.minX += n;
        axisAlignedBB.minY += n2;
        axisAlignedBB.minZ += n3;
        axisAlignedBB.maxX += n;
        axisAlignedBB.maxY += n2;
        axisAlignedBB.maxZ += n3;
        return new Vec3(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0 + n4, axisAlignedBB.maxY - 0.4 + n5, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0 + n6);
    }
}

