package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.RenderUtils;
import me.onlyeli.ice.utils.Wrapper;

public class Nuker extends Module {
	
	private int xPos;
	private int yPos;
	private int zPos;
	private static int radius = 4;
	
	public Nuker() {
		super("Nuker", Keyboard.KEY_NONE, Category.WORLD);
	}
	
	
	public void onUpdate() {
		
		if(!this.isToggled())
			return;
		
		for(int x = -radius; x < radius; x++) {
			for(int y = radius; y > -radius; y++) {
				for(int z = -radius; z < radius; z++) {
					this.xPos = (int)Wrapper.mc.thePlayer.posX + x;
					this.yPos = (int)Wrapper.mc.thePlayer.posY + y;
					this.zPos = (int)Wrapper.mc.thePlayer.posZ + z;
					
					BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
					Block block = Wrapper.mc.theWorld.getBlockState(blockPos).getBlock();
					
					if(block.getMaterial() == Material.air)
						continue;
					
					Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
					Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
				}
			}
		}
		
		super.onUpdate();
	}
	
	
	@Override
	public void onRender() {
		
		if(!this.isToggled())
			return;
		
		for(int x = -radius; x < radius; x++) {
			for(int y = radius; y > -radius; y++) {
				for(int z = -radius; z < radius; z++) {
					this.xPos = (int)Wrapper.mc.thePlayer.posX + x;
					this.yPos = (int)Wrapper.mc.thePlayer.posY + y;
					this.zPos = (int)Wrapper.mc.thePlayer.posZ + z;
					
					BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
					Block block = Wrapper.mc.theWorld.getBlockState(blockPos).getBlock();
					
					if(block.getMaterial() == Material.air)
						continue;
					
					double renderX = this.xPos - Wrapper.mc.getRenderManager().renderPosX;
					double renderY = this.yPos - Wrapper.mc.getRenderManager().renderPosY;
					double renderZ = this.zPos - Wrapper.mc.getRenderManager().renderPosZ;
					
					RenderUtils.drawOutlinedBlockESP(renderX, renderY, renderZ, 0F, 2.5F, 2.5F, 1F, 1.5F);
				}
			}
		}
		
		super.onRender();
	}
	
}
