package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.util.MovementUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Parkour extends Module {

	public Parkour() {
		super("Parkour", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			
			if (mc.thePlayer.ticksExisted > 0 && MovementUtils.isOnGround(0.000000001) && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() != null && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() == Blocks.air && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY - 1, mc.thePlayer.lastTickPosZ)).getBlock() != null && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY - 1, mc.thePlayer.lastTickPosZ)).getBlock() != Blocks.air) {
				
				mc.thePlayer.jump();
				
			}
			
		}
		
	}
	
}
