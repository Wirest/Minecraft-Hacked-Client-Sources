package info.spicyclient.modules.render;

import java.awt.Color;
import java.util.Comparator;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.SettingChangeEvent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class SkyColor extends Module {
	
	public BooleanSetting Rainbow = new BooleanSetting("Rainbow", false);
	
	// Settings for when rgb is enabled
	public NumberSetting RgbSpeed = new NumberSetting("Speed", 95, 1, 100, 0.2);
	public NumberSetting Saturation = new NumberSetting("Saturation", 0.5, 0, 1, 0.05);
	public NumberSetting Brightness = new NumberSetting("Brightness", 0.3, 0, 1, 0.05);
	
	// Settings for when rgb is disabled
	public NumberSetting red = new NumberSetting("red", 1, 1, 255, 1);
	public NumberSetting green = new NumberSetting("green", 1, 1, 255, 1);
	public NumberSetting blue = new NumberSetting("blue", 1, 1, 255, 1);
	public NumberSetting RgbBrightness = new NumberSetting("Brightness", 1, 0, 1, 0.01);
	
	public SkyColor() {
		super("SkyColor", Keyboard.KEY_NONE, Category.RENDER);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(Rainbow, RgbSpeed, Saturation, Brightness, red, green, blue, RgbBrightness);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		
		if (Rainbow == null) {
			
		}
		else if (e.setting.equals(Rainbow)) {
			
			World.spicySkyColorSet = false;
			
			if (Rainbow.enabled) {
				
				if (!settings.contains(RgbSpeed)) {
					settings.add(RgbSpeed);
				}
				if (!settings.contains(Saturation)) {
					settings.add(Saturation);
				}
				if (!settings.contains(Brightness)) {
					settings.add(Brightness);
				}
				if (settings.contains(red)) {
					settings.remove(red);
				}
				if (settings.contains(green)) {
					settings.remove(green);
				}
				if (settings.contains(blue)) {
					settings.remove(blue);
				}
				if (settings.contains(RgbBrightness)) {
					settings.remove(RgbBrightness);
				}
				
			}
			else if (!Rainbow.enabled) {
				
				if (settings.contains(RgbSpeed)) {
					settings.remove(RgbSpeed);
				}
				if (settings.contains(Saturation)) {
					settings.remove(Saturation);
				}
				if (settings.contains(Brightness)) {
					settings.remove(Brightness);
				}
				if (!settings.contains(red)) {
					settings.add(red);
				}
				if (!settings.contains(green)) {
					settings.add(green);
				}
				if (!settings.contains(blue)) {
					settings.add(blue);
				}
				if (!settings.contains(RgbBrightness)) {
					settings.add(RgbBrightness);
				}
				
			}
			
			this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
			
		}
		
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		World.spicySkyColorSet = false;
	}
	
	private Random rand = new Random();
	private double counter = 0;
	private int count = 0;
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate) {
			
			if (e.isPre()) {
				
				if (Rainbow.enabled) {
					float hue = System.currentTimeMillis() % (int)((100.5f - RgbSpeed.getValue()) * 1000) / (float)((100.5f - RgbSpeed.getValue()) * 1000);
					int tempColor = Color.HSBtoRGB(hue, (float) Saturation.getValue(), (float) Brightness.getValue());
					
					String hexColor = String.format("#%06X", (0xFFFFFF & tempColor));
					
					Color finalColor = Color.decode(hexColor);
					World.spicySkyColor = new Vec3 ((finalColor.getRed() * (1f/255f)), (finalColor.getGreen() * (1d/255d)), (finalColor.getBlue() * (1d/255d)));
					World.spicySkyColorSet = true;
				}
				else if (!Rainbow.enabled) {
					
					World.spicySkyColor = new Vec3 ((red.getValue() * (1f/255f)) * RgbBrightness.getValue(), (green.getValue() * (1d/255d)) * RgbBrightness.getValue(), (blue.getValue() * (1d/255d)) * RgbBrightness.getValue());
					World.spicySkyColorSet = true;
					
				}
				
			}
			
		}
		
	}
	
}