
package me.memewaredevs.client.util.pathfinding;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * totally not from a gitlab repo LOL
 */
public class PathFinder {
    private Vec3 startVec3;
    private Vec3 endVec3;
    private ArrayList<Vec3> path = new ArrayList();
    private ArrayList<PathHub> pathHubs = new ArrayList();
    private ArrayList<PathHub> workingPathHubList = new ArrayList();
    private double minDistanceSquared = 9.0;
    private boolean nearest = true;
    private static Vec3[] directions = new Vec3[]{new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0),
            new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0)};

    public PathFinder(Vec3 startVec3, Vec3 endVec3) {
        this.startVec3 = startVec3.addVector(0.0, 0.0, 0.0).floor();
        this.endVec3 = endVec3.addVector(0.0, 0.0, 0.0).floor();
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void compute() {
        this.compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        this.path.clear();
        this.workingPathHubList.clear();
        ArrayList<Vec3> initPath = new ArrayList<>();
        initPath.add(this.startVec3);
        this.workingPathHubList
                .add(new PathHub(this.startVec3, null, initPath, this.startVec3.squareDistanceTo(this.endVec3), 0.0, 0.0));
        block0:
        for (int i = 0; i < loops; ++i) {
            Collections.sort(this.workingPathHubList, new CompareHub());
            int j = 0;
            if (this.workingPathHubList.size() == 0) {
                break;
            }
            for (PathHub pathHub : new ArrayList<>(this.workingPathHubList)) {
                Vec3 loc2;
                if (++j > depth) {
                    continue block0;
                }
                this.workingPathHubList.remove(pathHub);
                this.pathHubs.add(pathHub);
                for (Vec3 direction : directions) {
                    Vec3 loc = pathHub.getLoc().add(direction).floor();
                    if (PathFinder.isValid(loc, false) && this.putHub(pathHub, loc, 0.0)) {
                        break block0;
                    }
                }
                Vec3 loc1 = pathHub.getLoc().addVector(0.0, 1.0, 0.0).floor();
                if (PathFinder.isValid(loc1, false) && this.putHub(pathHub, loc1, 0.0)
                        || PathFinder
                        .isValid(loc2 = pathHub.getLoc().addVector(0.0, -1.0, 0.0).floor(), false)
                        && this.putHub(pathHub, loc2, 0.0)) {
                    break block0;
                }
            }
        }
        if (this.nearest) {
            Collections.sort(this.pathHubs, new CompareHub());
            this.path = this.pathHubs.get(0).getPathway();
        }
    }

    public static boolean isValid(Vec3 loc, boolean checkGround) {
        return PathFinder.isValid((int) loc.getX(), (int) loc.getY(), (int) loc.getZ(),
                checkGround);
    }

    public static boolean isValid(int x, int y, int z, boolean checkGround) {
        BlockPos block1 = new BlockPos(x, y, z);
        BlockPos block2 = new BlockPos(x, y + 1, z);
        BlockPos block3 = new BlockPos(x, y - 1, z);
        return !PathFinder.isNotPassable(block1) && !PathFinder.isNotPassable(block2)
                && (PathFinder.isNotPassable(block3) || !checkGround)
                && PathFinder.canWalkOn(block3);
    }

    private static boolean isNotPassable(BlockPos block) {
        return Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(), block.getZ()).isSolidFullCube()
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockSlab
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockStairs
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockCactus
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockChest
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockEnderChest
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockSkull
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPane
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockFence
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockWall
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockGlass
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPistonBase
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPistonExtension
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockPistonMoving
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockStainedGlass
                || Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockTrapDoor;
    }

    private static boolean canWalkOn(BlockPos block) {
        return !(Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockFence)
                && !(Minecraft.getMinecraft().theWorld.getBlock(block.getX(), block.getY(),
                block.getZ()) instanceof BlockWall);
    }

    public PathHub doesHubExistAt(Vec3 loc) {
        for (PathHub pathHub : this.pathHubs) {
            if (pathHub.getLoc().getX() != loc.getX() || pathHub.getLoc().getY() != loc.getY()
                    || pathHub.getLoc().getZ() != loc.getZ()) {
                continue;
            }
            return pathHub;
        }
        for (PathHub pathHub : this.workingPathHubList) {
            if (pathHub.getLoc().getX() != loc.getX() || pathHub.getLoc().getY() != loc.getY()
                    || pathHub.getLoc().getZ() != loc.getZ()) {
                continue;
            }
            return pathHub;
        }
        return null;
    }

    public boolean putHub(PathHub parent, Vec3 loc, double cost) {
        PathHub existingPathHub = this.doesHubExistAt(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getMaxCost();
        }
        if (existingPathHub == null) {
            if (loc.getX() == this.endVec3.getX() && loc.getY() == this.endVec3.getY()
                    && loc.getZ() == this.endVec3.getZ()
                    || this.minDistanceSquared != 0.0
                    && loc.squareDistanceTo(this.endVec3) <= this.minDistanceSquared) {
                this.path.clear();
                this.path = parent.getPathway();
                this.path.add(loc);
                return true;
            }
            ArrayList<Vec3> path = new ArrayList<>(parent.getPathway());
            path.add(loc);
            this.workingPathHubList.add(new PathHub(loc, parent, path, loc.squareDistanceTo(this.endVec3), cost, totalCost));
        } else if (existingPathHub.getCurrentCost() > cost) {
            ArrayList<Vec3> path = new ArrayList<>(parent.getPathway());
            path.add(loc);
            existingPathHub.setLoc(loc);
            existingPathHub.setParentPathHub(parent);
            existingPathHub.setPathway(path);
            existingPathHub.setSqDist(loc.squareDistanceTo(this.endVec3));
            existingPathHub.setCurrentCost(cost);
            existingPathHub.setMaxCost(totalCost);
        }
        return false;
    }

    public class CompareHub implements Comparator<PathHub> {
        @Override
        public int compare(PathHub o1, PathHub o2) {
            return (int) (o1.getSqDist() + o1.getMaxCost()
                    - (o2.getSqDist() + o2.getMaxCost()));
        }
    }
}
