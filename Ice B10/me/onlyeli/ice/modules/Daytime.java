package me.onlyeli.ice.modules;

import java.awt.Color;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.events.EventListener;
import me.onlyeli.ice.events.EventSetWorldTime;
import me.onlyeli.ice.events.ModData;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

public class Daytime extends Module {

	protected static Minecraft mc = Minecraft.getMinecraft();
	
	private long time = 0;
	
	public Daytime() {
		super("Daytime", Keyboard.KEY_NONE, Category.WORLD);
	}
	
	
	public void onEnable() {
		this.time = mc.theWorld.getWorldTime();
		mc.theWorld.setWorldTime(6000l);
	}
	
	@Override
	public void onDisable() {
		mc.theWorld.setWorldTime(time);
	}
	
	@EventListener
	public void onTimeSet(EventSetWorldTime event){
		time = event.getTime();
		event.setTime(6000l);
	}
	
}
