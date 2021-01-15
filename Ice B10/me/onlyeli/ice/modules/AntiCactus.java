package me.onlyeli.ice.modules;

import java.awt.Color;

import me.onlyeli.ice.events.EventListener;
import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventBlockBoundingBox;
import me.onlyeli.ice.events.ModData;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.input.Keyboard;

public class AntiCactus extends Module {

	public AntiCactus() {
		super("AntiCactus", Keyboard.KEY_NONE, Category.PLAYER);
	}

	public void onBoundingBox(EventBlockBoundingBox event){
		if(event.getBlock() instanceof BlockCactus){
			event.setBox(new AxisAlignedBB(
					event.getLocation().getX(),
					event.getLocation().getY(),
					event.getLocation().getZ(),
					event.getLocation().getX() + 1,
					event.getBox().maxY,
					event.getLocation().getZ() + 1
					));
		}
	}
	
}
