package de.iotacb.client.utilities.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class AStarProcessor {
    private Vec3 startPos;
    private Vec3 endPos;
    private ArrayList<Vec3> path = new ArrayList<Vec3>();
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ArrayList<Node> activeNodes = new ArrayList<Node>();
    private boolean nearest = true;

    private static Vec3[] coords = {
            new Vec3(1, 0, 0),
            new Vec3(-1, 0, 0),
            new Vec3(0, 0, 1),
            new Vec3(0, 0, -1),
            new Vec3(0, 1, 0),
            new Vec3(0, -1, 0)
    };

    public AStarProcessor(Vec3 startPos, Vec3 endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public ArrayList<Vec3> getPath() {
        return path;
    }

    public void calculatePath(int iterations) {
        path.clear();
        activeNodes.clear();
        final ArrayList<Vec3> initPath = new ArrayList<Vec3>();
        initPath.add(startPos);
        activeNodes.add(new Node(startPos, null, initPath, startPos.squareDistanceTo(endPos), 0, 0));
        search:
		for (int i = 0; i < iterations; i++) {
			Collections.sort(activeNodes, new CompareNode());
			for (Node node : new ArrayList<Node>(activeNodes)) {
				activeNodes.remove(node);
				nodes.add(node);
				for (Vec3 direction : coords) {
					final Vec3 loc = node.getLoc().add(direction);
					if (checkPositionValidity(loc, false)) {
						if (addHub(node, loc, 0)) {
							break search;
						}
					}
				}
				break;
			}
		}
        Collections.sort(nodes, new CompareNode());
        path = nodes.get(0).getPath();
    }

    public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
        return checkPositionValidity((int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
    	final BlockPos block1 = new BlockPos(x, y, z);
    	final BlockPos block2 = new BlockPos(x, y + 1, z);
    	final BlockPos block3 = new BlockPos(x, y - 1, z);
        return !isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3) && block1.getBlock() == Blocks.air;
    }

    private static boolean isBlockSolid(BlockPos block) {
        return block.getBlock().isFullBlock() ||
        		(block.getBlock() instanceof BlockSlab) ||
        		(block.getBlock() instanceof BlockStairs) ||
        		(block.getBlock() instanceof BlockCactus) ||
        		(block.getBlock() instanceof BlockChest) ||
        		(block.getBlock() instanceof BlockEnderChest) ||
        		(block.getBlock() instanceof BlockSkull) ||
        		(block.getBlock() instanceof BlockPane) ||
        		(block.getBlock() instanceof BlockFence) ||
        		(block.getBlock() instanceof BlockWall) ||
        		(block.getBlock() instanceof BlockGlass) ||
        		(block.getBlock() instanceof BlockPistonBase) ||
        		(block.getBlock() instanceof BlockPistonExtension) ||
        		(block.getBlock() instanceof BlockPistonMoving) ||
        		(block.getBlock() instanceof BlockStainedGlass) ||
        		(block.getBlock() instanceof BlockTrapDoor) ||
        		(!(block.getBlock() instanceof BlockAir));
    }

    private static boolean isSafeToWalkOn(BlockPos block) {
        return !(Minecraft.getMinecraft().theWorld.getBlockState(block) instanceof BlockFence) && 
        		!(Minecraft.getMinecraft().theWorld.getBlockState(block) instanceof BlockWall);
    }

    public Node isHubExisting(Vec3 loc) {
        for (Node hub : nodes) {
            if (hub.getLoc().getX() == loc.getX() && hub.getLoc().getY() == loc.getY() && hub.getLoc().getZ() == loc.getZ()) {
                return hub;
            }
        }
        for (Node hub : activeNodes) {
            if (hub.getLoc().getX() == loc.getX() && hub.getLoc().getY() == loc.getY() && hub.getLoc().getZ() == loc.getZ()) {
                return hub;
            }
        }
        return null;
    }

    public boolean addHub(Node parent, Vec3 loc, double cost) {
    	final Node existingNode = isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getFCost();
        }
        if (existingNode == null) {
            if ((loc.getX() == endPos.getX() && loc.getY() == endPos.getY() && loc.getZ() == endPos.getZ()) || (loc.squareDistanceTo(endPos) <= 3)) {
                path.clear();
                path = parent.getPath();
                path.add(loc);
                return true;
            } else {
            	final ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
                path.add(loc);
                activeNodes.add(new Node(loc, parent, path, loc.squareDistanceTo(endPos), cost, totalCost));
            }
        }
        return false;
    }

    class Node {
        Vec3 loc = null;
        Node parent = null;
        ArrayList<Vec3> path;
        double squareDistanceToFromTarget;
        double hCost;
        double fCost;

        public Node(Vec3 loc, Node parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.hCost = cost;
            this.fCost = totalCost;
        }

        public Vec3 getLoc() {
            return loc;
        }

        public Node getParent() {
            return parent;
        }

        public ArrayList<Vec3> getPath() {
            return path;
        }

        public double getSquareDistanceToFromTarget() {
            return squareDistanceToFromTarget;
        }

        public double getHCost() {
            return hCost;
        }

        public void setLoc(Vec3 loc) {
            this.loc = loc;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void setPath(ArrayList<Vec3> path) {
            this.path = path;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }

        public void setHCost(double hCost) {
            this.hCost = hCost;
        }

        public double getFCost() {
            return fCost;
        }

        public void setFCost(double fCost) {
            this.fCost = fCost;
        }
    }

    public class CompareNode implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (int) (
                    (o1.getSquareDistanceToFromTarget() + o1.getFCost()) - (o2.getSquareDistanceToFromTarget() + o2.getFCost())
            );
        }
    }
}
