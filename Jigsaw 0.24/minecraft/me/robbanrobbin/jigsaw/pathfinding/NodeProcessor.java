package me.robbanrobbin.jigsaw.pathfinding;

import java.util.ArrayList;
import java.util.Collections;

import me.robbanrobbin.jigsaw.client.tools.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class NodeProcessor {
	
	public ArrayList<Node> path = new ArrayList<Node>();
	public ArrayList<Node> triedPaths = new ArrayList<Node>();
	
	private ArrayList<Node> getNeighbors(Node node) {
		ArrayList<Node> neighbors = new ArrayList<Node>();
		BlockPos b1 = node.getBlockpos();
		BlockPos b2 = node.getBlockpos();
		b1 = b1.add(1, 1, 1);
		b2 = b2.add(-1, -1, -1);
//		neighbors.add(createNode(b1.up()));
//		neighbors.add(createNode(b1.down()));
//		neighbors.add(createNode(b1.east()));
//		neighbors.add(createNode(b1.west()));
//		neighbors.add(createNode(b1.north()));
//		neighbors.add(createNode(b1.south()));
		for(BlockPos pos : BlockPos.getAllInBox(b1, b2)) {
			if(pos.equals(node.getBlockpos())) {
				//System.out.println("EQUALS");
				continue;
			}
			if(pos.getX() > node.getBlockpos().getX() && pos.getZ() > node.getBlockpos().getZ()) {
				continue;
			}
			if(pos.getX() < node.getBlockpos().getX() && pos.getZ() < node.getBlockpos().getZ()) {
				continue;
			}
			if(pos.getX() < node.getBlockpos().getX() && pos.getZ() > node.getBlockpos().getZ()) {
				continue;
			}
			if(pos.getX() > node.getBlockpos().getX() && pos.getZ() < node.getBlockpos().getZ()) {
				continue;
			}
			neighbors.add(createNode(pos));
		}
		return neighbors;
	}
	
	public void getPath(BlockPos start, BlockPos finish) {
		
		Node startNode = createNode(start);
		Node endNode = createNode(finish);
		
		ArrayList<Node> openNodes = new ArrayList<Node>();
		ArrayList<Node> closedNodes = new ArrayList<Node>();
		
		openNodes.add(startNode);

		int count = 0;
		while(openNodes.size() > 0) {
			Node currentNode = openNodes.get(0);
			//System.out.println(openNodes.size());
			if(count > 2000) {
				return;
			}
			//System.out.println("Size: " + openNodes.size());
			double minFCost = 100000000;
			for(int i = 1; i < openNodes.size(); i++) {
				double FCost = openNodes.get(i).getF_Cost(currentNode, endNode);
				if(FCost < minFCost) {
					minFCost = FCost;
					currentNode = openNodes.get(i);
				}
			}

			openNodes.remove(currentNode);
			closedNodes.add(currentNode);
			
			triedPaths.add(currentNode);
			
			if(currentNode.getBlockpos().equals(endNode.getBlockpos())) { //path has been found
				endNode.parent = currentNode;
				retracePath(startNode, endNode);
				return;
			}
			for(Node neighbor : getNeighbors(currentNode)) {
				if(!neighbor.isWalkable() || isNodeClosed(neighbor, closedNodes)) {
					continue;
				}
				double hCost = currentNode.getH_Cost(endNode);
				if(hCost >= neighbor.getH_Cost(endNode) || !isNodeClosed(neighbor, openNodes)) {
					neighbor.parent = currentNode;
					if(!isNodeClosed(neighbor, openNodes)) {
						openNodes.add(neighbor);
					}
				}
			}
			count++;
		}
		
	}
	
	private boolean isNodeClosed(Node node, ArrayList<Node> nodes) {
		for(Node n : nodes) {
			if(n.getBlockpos().equals(node.getBlockpos())) {
				return true;
			}
		}
		return false;
	}
	
	private void retracePath(Node startNode, Node endNode) {
		ArrayList<Node> path = new ArrayList<Node>();
		Node currentNode = endNode;
		
		while(!currentNode.equals(startNode)) {
			path.add(currentNode);
			currentNode = currentNode.parent;
		}
		Collections.reverse(path);
		
		this.path = path;
	}
	
	public Node createNode(BlockPos pos) {
		return new Node(Utils.getBlock(pos).getMaterial() == Material.air &&  Utils.getBlock(pos.up()).getMaterial() == Material.air, pos);
	}
	
}
