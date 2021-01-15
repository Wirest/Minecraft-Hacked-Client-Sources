package dev.astroclient.client.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;

public class BlockUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }


    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static Block getBlock(double x, double y, double z) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double blocks) {
        blocks += inPlayer.height;
        return getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + blocks, inPlayer.posZ));
    }

    public static Block getBlockAtPos(BlockPos inBlockPos) {
        IBlockState s = mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return getBlockAtPos(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static float getBlockDistance(float xDiff, float yDiff, float zDiff) {
        return MathHelper.sqrt_float(((xDiff - 0.5F) * (xDiff - 0.5F)) + ((yDiff - 0.5F) * (yDiff - 0.5F))
                + ((zDiff - 0.5F) * (zDiff - 0.5F)));
    }

    public static BlockPos getBlockPos(BlockPos inBlockPos) {
        return inBlockPos;
    }

    public static BlockPos getBlockPos(double x, double y, double z) {
        return getBlockPos(new BlockPos(x, y, z));
    }

    public static BlockPos getBlockPosUnderPlayer(EntityPlayer inPlayer) {
        return new BlockPos(inPlayer.posX, (inPlayer.posY + (mc.thePlayer.motionY + 0.1D)) - 1D, inPlayer.posZ);
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return getBlockAtPos(
                new BlockPos(inPlayer.posX, (inPlayer.posY + (mc.thePlayer.motionY + 0.1D)) - 1D, inPlayer.posZ));
    }
    public static float getHorizontalPlayerBlockDistance(BlockPos blockPos) {
        float xDiff = (float) (mc.thePlayer.posX - blockPos.getX());
        float zDiff = (float) (mc.thePlayer.posZ - blockPos.getZ());
        return MathHelper.sqrt_float(((xDiff - 0.5F) * (xDiff - 0.5F)) + ((zDiff - 0.5F) * (zDiff - 0.5F)));
    }

    public static Material getMaterial(BlockPos pos) {
        return getState(pos).getBlock().getMaterial();
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static MovingObjectPosition getMouseOver() {
        return getMinecraft().objectMouseOver;
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().thePlayer;
    }

    public static float getPlayerBlockDistance(BlockPos blockPos) {
        return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static float getPlayerBlockDistance(double posX, double posY, double posZ) {
        float xDiff = (float) (mc.thePlayer.posX - posX);
        float yDiff = (float) (mc.thePlayer.posY - posY);
        float zDiff = (float) (mc.thePlayer.posZ - posZ);
        return getBlockDistance(xDiff, yDiff, zDiff);
    }

    public static IBlockState getState(BlockPos pos) {
        return mc.theWorld.getBlockState(pos);
    }
}
