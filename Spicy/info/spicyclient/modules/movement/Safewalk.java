package info.spicyclient.modules.movement;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventSneaking;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.NumberSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class Safewalk extends Module {

	public Safewalk() {
		super("Safewalk", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventSneaking) {
			
			if (e.isPre()) {
				
				EventSneaking sneak = (EventSneaking) e;
				
				if (sneak.entity.onGround && sneak.entity instanceof EntityPlayer) {
					sneak.sneaking = true;
				}else {
					sneak.sneaking = false;
				}
				sneak.offset = -1D;
				sneak.revertFlagAfter = true;
				
			}
			
		}
		
	}
	
}
