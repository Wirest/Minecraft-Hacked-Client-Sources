package me.robbanrobbin.jigsaw.client.modules.tpaura;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class BBRayTrace {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	private int highestHitBlockHeight;
	private BlockPos highestBlock = null;
	private boolean hitBlock;
	private ArrayList<BlockPos> hitBlocks = new ArrayList<BlockPos>();
	
	public BBRayTrace(Vec3 pos1, Vec3 pos2, int checks, double bbSize) {
		AxisAlignedBB bb = mc.thePlayer.boundingBox;
		bb.expand(bbSize, 0, bbSize);
		double xDist = pos2.xCoord - pos1.xCoord;
		double yDist = pos2.yCoord - pos1.yCoord;
		double zDist = pos2.zCoord - pos1.zCoord;
		for(int i = 0; i < checks; i++) {
			bb = bb.offset((zDist / checks) * i, ((yDist / checks) * i) + 0.05, (xDist / checks) * i);
			for(BlockPos pos : mc.theWorld.getCollidingBlockPositions(mc.thePlayer, bb)) {
				if(!hitBlocks.contains(pos)) {
					hitBlocks.add(pos);
				}
			}
		}
		if(hitBlocks.isEmpty()) {
			hitBlock = false;
			return;
		}
		hitBlock = true;
		int maxHeight = -1000;
		for(BlockPos pos : hitBlocks) {
			if(pos.getY() > maxHeight) {
				maxHeight = pos.getY();
				highestBlock = pos;
			}
		}
	}
	
	public boolean didHitBlock() {
		return hitBlock;
	}
	
	public ArrayList<BlockPos> getHitBlocks() {
		return hitBlocks;
	}
	
	public int getHighestHitBlockHeight() {
		return highestHitBlockHeight;
	}
	
	public BlockPos getHighestBlock() {
		return highestBlock;
	}
	
}
