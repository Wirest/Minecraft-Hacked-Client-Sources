package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
	
	BlockPos blockUnder = null;
	BlockPos blockBef = null;
	EnumFacing facing = null;
	public static boolean placing = false;

	public Scaffold() {
		super("Scaffold", 0, Category.WORLD, "Automatically builds a bridge under your feet. Really useful in skywars.");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		blockUnder = null;
		blockBef = null;
		if(mc.thePlayer.getCurrentEquippedItem() == null) {
			return;
		}
		if(mc.thePlayer.getCurrentEquippedItem().getItem() == null) {
			return;
		}
		if(!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
			return;
		}
//		if(mc.thePlayer.isSprinting() && mc.thePlayer.onGround) {
//			mc.thePlayer.motionX *= 0.7;
//			mc.thePlayer.motionZ *= 0.7;
//		}
		BlockPos under = new BlockPos(mc.thePlayer.posX + mc.thePlayer.motionX * 2, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ + mc.thePlayer.motionZ * 2); 
		if(Utils.getBlock(under).getMaterial() == Material.air) {
			blockUnder = new BlockPos(mc.thePlayer.posX + mc.thePlayer.motionX * 2, mc.thePlayer.posY - 0.01, mc.thePlayer.posZ + mc.thePlayer.motionZ * 2);
			for(EnumFacing facing : EnumFacing.values()) {
				BlockPos offset = blockUnder.offset(facing);
				if(Utils.getBlock(offset).getMaterial() != Material.air) {
					this.facing = facing;
					blockBef = offset;
					break;
				}
			}
			if(blockBef != null) {
				float[] rots = Utils.getFacePos(Utils.getVec3(blockBef));
//				event.yaw = rots[0];
				event.pitch = 90f;
				
			}
		}
		super.onUpdate(event);
	}
	
	@Override
	public void onLateUpdate() {
		placing = false;
		if(blockUnder == null) {
			return;
		}
		if(blockBef == null) {
			return;
		}
		placing = true;
		MovingObjectPosition pos = mc.theWorld.rayTraceBlocks(Utils.getVec3(blockUnder).addVector(0.5, 0.5, 0.5),
				Utils.getVec3(blockBef).addVector(0.5, 0.5, 0.5));
		if(pos == null) {
			return;
		}
		Vec3 hitVec = pos.hitVec;
		float f = 0;
		float f1 = 0;
		float f2 = 0;
		mc.thePlayer.swingItem();
		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,  mc.thePlayer.getCurrentEquippedItem(), blockBef, pos.sideHit, hitVec);
		//sendPacket(new C08PacketPlayerBlockPlacement(blockBef, pos.sideHit.getIndex(), mc.thePlayer.getCurrentEquippedItem(), f, f1, f2));
		
		super.onLateUpdate();
	}
	
	@Override
	public void onRender() {
//		if(blockUnder == null) {
//			return;
//		}
//		MovingObjectPosition pos = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ),
//				Utils.getVec3(blockBef));
//		
//		double xPos = pos.hitVec.getX();
//		double yPos = pos.hitVec.getY();
//		double zPos = pos.hitVec.getZ();
//		
//		double ENxPos = mc.thePlayer.posX;
//		double ENyPos = mc.thePlayer.posY - 0.5;
//		double ENzPos = mc.thePlayer.posZ;
//		
//		RenderTools.draw3DLine(xPos, yPos, zPos, ENxPos, ENyPos, ENzPos, 1f,
//				0.3f, 0.3f, 0.9f, 2);
//		
		super.onRender();
	}
	
}
