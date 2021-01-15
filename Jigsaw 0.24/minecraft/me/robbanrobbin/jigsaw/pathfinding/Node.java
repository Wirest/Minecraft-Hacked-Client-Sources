package me.robbanrobbin.jigsaw.pathfinding;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Node {
	
	private boolean walkable;
	private BlockPos blockPos;
	public Node parent;
	
	public Node(boolean walkable, BlockPos blockPos) {
		this.walkable = walkable;
		this.blockPos = blockPos;
	}
	
	/**
	 * Distance from start node
	 * @return
	 */
	public double getG_Cost(Node startNode) {
		return distance(blockPos, startNode.getBlockpos());
	}
	
	/**
	 * Distance from end node
	 * @return
	 */
	public double getH_Cost(Node endNode) {
		return distance(blockPos, endNode.getBlockpos());
	}
	
	/**
	 * g cost and h cost added together
	 * @return
	 */
	public double getF_Cost(Node startNode, Node endNode) {
		return getG_Cost(startNode) + getH_Cost(endNode);
	}
	
	public BlockPos getBlockpos() {
		return blockPos;
	}
	
	public boolean isWalkable() {
		return walkable;
	}
	
	public double distance(BlockPos b1, BlockPos b2) {
		float f = (float) (b1.getX() - b2.getX());
		float f1 = (float) (b1.getY() - b2.getY());
		float f2 = (float) (b1.getZ() - b2.getZ());
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}
	
}
