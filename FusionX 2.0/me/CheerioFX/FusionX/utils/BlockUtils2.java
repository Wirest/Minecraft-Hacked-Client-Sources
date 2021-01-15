// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.world.World;
import net.minecraft.util.Vec3i;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.client.Minecraft;

public final class BlockUtils2
{
    private static final Minecraft mc;
    static ItemInWorldManager iIM;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return BlockUtils2.mc.theWorld.getBlockState(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static Material getMaterial(final BlockPos pos) {
        return getState(pos).getBlock().getMaterial();
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static boolean placeBlockLegit(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils2.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        faceVectorPacket(hitVec);
                        BlockUtils2.mc.playerController.func_178890_a(BlockUtils2.mc.thePlayer, BlockUtils2.mc.theWorld, BlockUtils2.mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                        BlockUtils2.mc.thePlayer.swingItem();
                        BlockUtils2.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean rcActionLegit(final BlockPos pos) {
        BlockUtils2.iIM = new ItemInWorldManager(BlockUtils2.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils2.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        faceVectorPacket(hitVec);
                        NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(pos, side2.getIndex(), BlockUtils2.mc.thePlayer.getCurrentEquippedItem(), (float)hitVec.xCoord, (float)hitVec.yCoord, (float)hitVec.zCoord));
                        BlockUtils2.mc.thePlayer.swingItem();
                        BlockUtils2.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static double getDistanceToBlock(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        final EnumFacing[] values;
        if ((values = EnumFacing.values()).length != 0) {
            final EnumFacing side = values[0];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
            return eyesPos.distanceTo(hitVec);
        }
        return 0.0;
    }
    
    public static boolean rcAction(final BlockPos pos, final double reach) {
        BlockUtils2.iIM = new ItemInWorldManager(BlockUtils2.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils2.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= reach * reach) {
                        faceVectorPacket(hitVec);
                        NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(pos, side2.getIndex(), BlockUtils2.mc.thePlayer.getCurrentEquippedItem(), (float)hitVec.xCoord, (float)hitVec.yCoord, (float)hitVec.zCoord));
                        BlockUtils2.mc.thePlayer.swingItem();
                        BlockUtils2.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean breakBlockTap(final BlockPos pos, final double reach, final boolean start) {
        BlockUtils2.iIM = new ItemInWorldManager(BlockUtils2.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils2.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= reach * reach) {
                        faceVectorPacket(hitVec);
                        BlockUtils2.mc.playerController.func_180511_b(pos, side2);
                        BlockUtils2.mc.thePlayer.swingItem();
                        BlockUtils2.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean breakBlockTapLegit(final BlockPos pos) {
        BlockUtils2.iIM = new ItemInWorldManager(BlockUtils2.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils2.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        faceVectorPacket(hitVec);
                        BlockUtils2.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                        BlockUtils2.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                        BlockUtils2.mc.thePlayer.swingItem();
                        BlockUtils2.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean placeBlockLegit(final BlockPos pos, final EventPreMotionUpdates event) {
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils2.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        BlockUtils2.mc.playerController.func_178890_a(BlockUtils2.mc.thePlayer, BlockUtils2.mc.theWorld, BlockUtils2.mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                        BlockUtils2.mc.thePlayer.swingItem();
                        BlockUtils2.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean placeBlockSimple(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils2.mc.thePlayer.posX, BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight(), BlockUtils2.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (getBlock(neighbor).canCollideCheck(BlockUtils2.mc.theWorld.getBlockState(neighbor), false)) {
                final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {
                    BlockUtils2.mc.playerController.func_178890_a(BlockUtils2.mc.thePlayer, BlockUtils2.mc.theWorld, BlockUtils2.mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                    return true;
                }
            }
        }
        return false;
    }
    
    private static void faceVectorPacket(final Vec3 vec) {
        final double diffX = vec.xCoord - BlockUtils2.mc.thePlayer.posX;
        final double diffY = vec.yCoord - (BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - BlockUtils2.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        BlockUtils2.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(BlockUtils2.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils2.mc.thePlayer.rotationYaw), BlockUtils2.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils2.mc.thePlayer.rotationPitch), BlockUtils2.mc.thePlayer.onGround));
    }
    
    private static void faceVectorPacket(final Vec3 vec, final EventPreMotionUpdates event) {
        final double diffX = vec.xCoord - BlockUtils2.mc.thePlayer.posX;
        final double diffY = vec.yCoord - (BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - BlockUtils2.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        event.setYaw(BlockUtils2.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils2.mc.thePlayer.rotationYaw));
        event.setPitch(BlockUtils2.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils2.mc.thePlayer.rotationPitch));
    }
    
    public static void faceBlockClient(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - BlockUtils2.mc.thePlayer.posX;
        final double diffY = blockPos.getY() + 0.5 - (BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - BlockUtils2.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        BlockUtils2.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils2.mc.thePlayer.rotationYaw);
        BlockUtils2.mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - BlockUtils2.mc.thePlayer.rotationPitch);
    }
    
    public static void faceBlockPacket(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - BlockUtils2.mc.thePlayer.posX;
        final double diffY = blockPos.getY() + 0.5 - (BlockUtils2.mc.thePlayer.posY + BlockUtils2.mc.thePlayer.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - BlockUtils2.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        BlockUtils2.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(BlockUtils2.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils2.mc.thePlayer.rotationYaw), BlockUtils2.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils2.mc.thePlayer.rotationPitch), BlockUtils2.mc.thePlayer.onGround));
    }
    
    public static void faceBlockClientHorizontally(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - BlockUtils2.mc.thePlayer.posX;
        final double diffZ = blockPos.getZ() + 0.5 - BlockUtils2.mc.thePlayer.posZ;
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        BlockUtils2.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils2.mc.thePlayer.rotationYaw);
    }
    
    public static float getPlayerBlockDistance(final BlockPos blockPos) {
        return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    public static float getPlayerBlockDistance(final double posX, final double posY, final double posZ) {
        final float xDiff = (float)(BlockUtils2.mc.thePlayer.posX - posX);
        final float yDiff = (float)(BlockUtils2.mc.thePlayer.posY - posY);
        final float zDiff = (float)(BlockUtils2.mc.thePlayer.posZ - posZ);
        return getBlockDistance(xDiff, yDiff, zDiff);
    }
    
    public static float getBlockDistance(final float xDiff, final float yDiff, final float zDiff) {
        return MathHelper.sqrt_float((xDiff - 0.5f) * (xDiff - 0.5f) + (yDiff - 0.5f) * (yDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }
    
    public static float getHorizontalPlayerBlockDistance(final BlockPos blockPos) {
        final float xDiff = (float)(BlockUtils2.mc.thePlayer.posX - blockPos.getX());
        final float zDiff = (float)(BlockUtils2.mc.thePlayer.posZ - blockPos.getZ());
        return MathHelper.sqrt_float((xDiff - 0.5f) * (xDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }
}
