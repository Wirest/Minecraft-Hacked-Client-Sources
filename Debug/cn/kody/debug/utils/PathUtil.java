package cn.kody.debug.utils;

import java.util.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraft.block.*;

public class PathUtil
{
    private Path startVec3;
    private Path endVec3;
    private ArrayList<Path> path;
    private double minDistanceSquared;
    private boolean nearest;
    private static Path[] flatCardinalDirections;
    
    public PathUtil(final Path class148, final Path class149) {
        this.path = new ArrayList<Path>();
        this.minDistanceSquared = 9.0;
        this.nearest = true;
        this.startVec3 = class148.addVector(0.0, 0.0, 0.0).floor();
        this.endVec3 = class149.addVector(0.0, 0.0, 0.0).floor();
    }
    
    public ArrayList<Path> getPath() {
        return this.path;
    }
    
    
    public static boolean checkPositionValidity(final Path class148, final boolean b) {
        return checkPositionValidity((int)class148.getX(), (int)class148.getY(), (int)class148.getZ(), b);
    }
    
    public static boolean checkPositionValidity(final int n, final int n2, final int n3, final boolean b) {
        final BlockPos blockPos = new BlockPos(n, n2, n3);
        final BlockPos blockPos2 = new BlockPos(n, n2 + 1, n3);
        final BlockPos blockPos3 = new BlockPos(n, n2 - 1, n3);
        return !isBlockSolid(blockPos) && !isBlockSolid(blockPos2) && (isBlockSolid(blockPos3) || !b) && isSafeToWalkOn(blockPos3);
    }
    
    private static boolean isBlockSolid(final BlockPos blockPos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock().isFullBlock() || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockSlab || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockStairs || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockCactus || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockChest || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockEnderChest || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockSkull || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockPane || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockFence || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockWall || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockGlass || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockPistonBase || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockPistonExtension || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockPistonMoving || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockStainedGlass || Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockTrapDoor;
    }
    
    private static boolean isSafeToWalkOn(final BlockPos blockPos) {
        return !(Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockFence) && !(Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock() instanceof BlockWall);
    }
    
    
    
    static {
        PathUtil.flatCardinalDirections = new Path[] { new Path(1.0, 0.0, 0.0), new Path(-1.0, 0.0, 0.0), new Path(0.0, 0.0, 1.0), new Path(0.0, 0.0, -1.0) };
    }
    
    
}
