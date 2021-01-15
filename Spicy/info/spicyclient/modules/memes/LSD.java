package info.spicyclient.modules.memes;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class LSD extends Module {

	public LSD() {
		super("LSD", Keyboard.KEY_NONE, Category.MEMES);
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventRenderGUI && e.isPre()) {
			
			float hue = System.currentTimeMillis() % (int)(4 * 1000) / (float)(4 * 1000);
    		int primColor = Color.HSBtoRGB(hue, 1, 1);
    		Gui.drawRect(0, 0, new ScaledResolution(mc).getScaledWidth(), new ScaledResolution(mc).getScaledHeight(), 0x6600ff00);
			
		}
		
	}
	
}
