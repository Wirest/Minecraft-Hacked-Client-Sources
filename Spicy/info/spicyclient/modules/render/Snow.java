package info.spicyclient.modules.render;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.chatCommands.Command;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.modules.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class Snow extends Module {

	public Snow() {
		super("Snow", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	public static transient ArrayList<Snowflake> snowflakes = new ArrayList<Snow.Snowflake>();
	public static transient float lastYaw = 0, lastPitch = 0;
	public static transient int snowflakeAmount = 1000;
	
	@Override
	public void onEnable() {
		
		snowflakes.clear();
		
		ScaledResolution sr = new ScaledResolution(mc);
		
		for (int i = snowflakes.size(); i < 500; i++) {
			
			snowflakes.add(new Snowflake(new Random().nextInt(sr.getScaledWidth()), new Random().nextInt(sr.getScaledHeight())));
			
		}
		
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventRenderGUI && e.isPre()) {
			
			ScaledResolution sr = new ScaledResolution(mc);
			
			double pixelXMove = 360 * ((lastYaw - mc.thePlayer.rotationYaw) / (mc.gameSettings.fovSetting));
			if (Double.isInfinite(pixelXMove)) {
				pixelXMove = 0;
			}
			
			double pixelYMove = 360 * ((lastPitch - mc.thePlayer.rotationPitch) / (mc.gameSettings.fovSetting));
			if (Double.isInfinite(pixelYMove)) {
				pixelYMove = 0;
			}
			
			ArrayList<Snowflake> snowflakesToRemove = new ArrayList<Snow.Snowflake>();
			for (Snowflake s : snowflakes) {
				
				s.x += pixelXMove;
				s.y += new Random().nextInt(5) + pixelYMove;
				
				if (s.x > 0 && s.x < sr.getScaledWidth_double() && s.y > 0 && s.y < sr.getScaledHeight_double()) {
					
					Gui.drawRect(s.x, s.y, s.x + 1 + new Random().nextDouble() + new Random().nextInt(1), s.y + 1 + new Random().nextDouble() + new Random().nextInt(1), -1);
					
				}
				
				if (s.x < -200 || s.x > sr.getScaledWidth_double() + 200 || s.y < -300 || s.y > sr.getScaledHeight_double()) {
					
					snowflakesToRemove.add(s);
					
				}
				
			}
			
			for (Snowflake s : snowflakesToRemove) {
				snowflakes.remove(s);
			}
			
			if (snowflakes.size() < snowflakeAmount) {
				
				for (int i = snowflakes.size(); i < snowflakeAmount; i++) {
					
					snowflakes.add(new Snowflake(new Random().nextInt(sr.getScaledWidth() + 400) - 200, new Random().nextInt(300) * -1));
					
				}
				
			}
			
			lastYaw = mc.thePlayer.rotationYaw;
			lastPitch = mc.thePlayer.rotationPitch;
			
		}
		
	}
	
	public class Snowflake {
		
		public Snowflake(double x, double y) {
			
			this.x = x;
			this.y = y;
			
		}
		
		public double x = 0, y = 0;
		
	}
	
}
