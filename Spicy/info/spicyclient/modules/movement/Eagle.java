package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class Eagle extends Module {

	public Eagle() {
		super("Eagle", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.keyBindSneak.pressed = false;
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			
			ItemStack i = mc.thePlayer.getCurrentEquippedItem();
			BlockPos Bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1D, mc.thePlayer.posZ);
			if(i != null) {
				if(i.getItem() instanceof ItemBlock) {
					mc.gameSettings.keyBindSneak.pressed = false;
					if(mc.theWorld.getBlockState(Bp).getBlock() == Blocks.air) {
						mc.gameSettings.keyBindSneak.pressed = true;
					}
					
				}
	        }
			
		}
		
	}
	
}
