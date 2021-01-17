package me.ihaq.iClient.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class BlockUtils {
    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        if (BlockUtils.getBlockAtPosC(Minecraft.getMinecraft().thePlayer, 0.30000001192092896, 0.10000000149011612, 0.30000001192092896).getMaterial().isLiquid() && BlockUtils.getBlockAtPosC(Minecraft.getMinecraft().thePlayer, -0.30000001192092896, 0.10000000149011612, -0.30000001192092896).getMaterial().isLiquid()) {
            onLiquid = true;
        }
        return onLiquid;
    }

    public static float getPlayerBlockDistance(BlockPos blockPos) {
        return BlockUtils.getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static float getPlayerBlockDistance(double posX, double posY, double posZ) {
        float xDiff = (float)(Minecraft.getMinecraft().thePlayer.posX - posX);
        float yDiff = (float)(Minecraft.getMinecraft().thePlayer.posY - posY);
        float zDiff = (float)(Minecraft.getMinecraft().thePlayer.posZ - posZ);
        return BlockUtils.getBlockDistance(xDiff, yDiff, zDiff);
    }

    public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
        EntitySnowball temp = new EntitySnowball(Minecraft.getMinecraft().theWorld);
        temp.posX = (double)x + 0.5;
        temp.posY = (double)y + 0.5;
        temp.posZ = (double)z + 0.5;
        temp.posX += (double)facing.getDirectionVec().getX() * 0.25;
        temp.posY += (double)facing.getDirectionVec().getY() * 0.25;
        temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25;
        return BlockUtils.getAngles(temp);
    }

    public static float[] getAngles(Entity e) {
        return new float[]{BlockUtils.getYawChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationYaw, BlockUtils.getPitchChangeToEntity(e) + Minecraft.getMinecraft().thePlayer.rotationPitch};
    }

    public static float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(- Math.atan(deltaX / deltaZ)));
        return MathHelper.wrapAngleTo180_float(- Minecraft.getMinecraft().thePlayer.rotationYaw - (float)yawToEntity);
    }

    public static float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double deltaY = entity.posY - 1.6 + (double)entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = - Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return - MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float)pitchToEntity);
    }

    public static boolean canSeeBlock(int x, int y, int z) {
        if (BlockUtils.getFacing(new BlockPos(x, y, z)) != null) {
            return true;
        }
        return false;
    }

    public static EnumFacing getFacing(BlockPos pos) {
        EnumFacing[] orderedValues;
        EnumFacing[] arrenumFacing = orderedValues = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN};
        int n = arrenumFacing.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing facing = arrenumFacing[n2];
            EntitySnowball temp = new EntitySnowball(Minecraft.getMinecraft().theWorld);
            temp.posX = (double)pos.getX() + 0.5;
            temp.posY = (double)pos.getY() + 0.5;
            temp.posZ = (double)pos.getZ() + 0.5;
            temp.posX += (double)facing.getDirectionVec().getX() * 0.5;
            temp.posY += (double)facing.getDirectionVec().getY() * 0.5;
            temp.posZ += (double)facing.getDirectionVec().getZ() * 0.5;
            if (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(temp)) {
                return facing;
            }
            ++n2;
        }
        return null;
    }

    public static float getBlockDistance(float xDiff, float yDiff, float zDiff) {
        return MathHelper.sqrt_float((xDiff - 0.5f) * (xDiff - 0.5f) + (yDiff - 0.5f) * (yDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }

    public static boolean isOnLadder() {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return false;
        }
        boolean onLadder = false;
        int y = (int)Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset((double)0.0, (double)1.0, (double)0.0).minY;
        int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX);
        while (x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1) {
            int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ);
            while (z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1) {
                Block block = BlockUtils.getBlock(x, y, z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                        return false;
                    }
                    onLadder = true;
                }
                ++z;
            }
            ++x;
        }
        if (!onLadder && !Minecraft.getMinecraft().thePlayer.isOnLadder()) {
            return false;
        }
        return true;
    }

    public static boolean isOnIce() {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return false;
        }
        boolean onIce = false;
        int y = (int)Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset((double)0.0, (double)-0.01, (double)0.0).minY;
        int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minX);
        while (x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxX) + 1) {
            int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minZ);
            while (z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().maxZ) + 1) {
                Block block = BlockUtils.getBlock(x, y, z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockIce) && !(block instanceof BlockPackedIce)) {
                        return false;
                    }
                    onIce = true;
                }
                ++z;
            }
            ++x;
        }
        return onIce;
    }

    public boolean isInsideBlock() {
        int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX);
        while (x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1) {
            int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY);
            while (y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1) {
                int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ);
                while (z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1) {
                    AxisAlignedBB boundingBox;
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)))) != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)) {
                        return true;
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
        return false;
    }

    public static boolean isBlockUnderPlayer(Material material, float height) {
        if (BlockUtils.getBlockAtPosC(Minecraft.getMinecraft().thePlayer, 0.3100000023841858, height, 0.3100000023841858).getMaterial() == material && BlockUtils.getBlockAtPosC(Minecraft.getMinecraft().thePlayer, -0.3100000023841858, height, -0.3100000023841858).getMaterial() == material && BlockUtils.getBlockAtPosC(Minecraft.getMinecraft().thePlayer, -0.3100000023841858, height, 0.3100000023841858).getMaterial() == material && BlockUtils.getBlockAtPosC(Minecraft.getMinecraft().thePlayer, 0.3100000023841858, height, -0.3100000023841858).getMaterial() == material) {
            return true;
        }
        return false;
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return BlockUtils.getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        return BlockUtils.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
    }

    public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double height) {
        return BlockUtils.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY + (double)inPlayer.height + height, inPlayer.posZ));
    }

    public static Block getBlock(int x, int y, int z) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }
}

