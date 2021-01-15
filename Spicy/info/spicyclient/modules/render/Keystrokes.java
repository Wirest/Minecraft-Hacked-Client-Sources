package info.spicyclient.modules.render;

import java.util.List;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventKey;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class Keystrokes extends Module {

	public Keystrokes() {
		super("Keystrokes", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public boolean tabgui_toggled;
	public boolean w_toggled = false;
	public boolean a_toggled = false;
	public boolean s_toggled = false;
	public boolean d_toggled = false;
	public boolean space_toggled = false;
	
	public void onEvent(Event e) {
		
		if (e instanceof EventRenderGUI) {
			
			if (e.isPre()) {
				
				
				FontRenderer fr = mc.fontRendererObj;
				
				// this variable is in reverse because i don't want too rewrite code
				TabGUI t = SpicyClient.config.tabgui;
				if (t.isEnabled()) {
					tabgui_toggled = false;
				}else {
					tabgui_toggled = true;
				}
				
				//Gui.drawRect(left, down, right, top, color);
				
				Gui.drawRect(5, tabgui_toggled ? 115 : 115 + 130 + SpicyClient.modules.size(), 45, tabgui_toggled ? 75 : 75 + 130 + SpicyClient.modules.size(), a_toggled ? 0x90ffffff : 0x90000000);
				
				Gui.drawRect(50, tabgui_toggled ? 115 : 115 + 130 + SpicyClient.modules.size(), 90, tabgui_toggled ? 75 : 75 + 130 + SpicyClient.modules.size(), s_toggled ? 0x90ffffff : 0x90000000);
				
				Gui.drawRect(95, tabgui_toggled ? 115 : 115 + 130 + SpicyClient.modules.size(), 135, tabgui_toggled ? 75 : 75 + 130 + SpicyClient.modules.size(), d_toggled ? 0x90ffffff : 0x90000000);
				
				Gui.drawRect(50, tabgui_toggled ? 70 : 70 + 130 + SpicyClient.modules.size(), 90, tabgui_toggled ? 30 : 30 + 130 + SpicyClient.modules.size(), w_toggled ? 0x90ffffff : 0x90000000);
				
				Gui.drawRect(5, tabgui_toggled ? 60 + 80 : 60 + 207 + SpicyClient.modules.size(), 135, tabgui_toggled ? 40 + 80 : 40 + 210 + SpicyClient.modules.size(), space_toggled ? 0x90ffffff : 0x90000000);
				
				
			}
		}
		
		if (e instanceof EventUpdate) {
			
			w_toggled = mc.gameSettings.keyBindForward.isKeyDown();
			a_toggled = mc.gameSettings.keyBindLeft.isKeyDown();
			s_toggled = mc.gameSettings.keyBindBack.isKeyDown();
			d_toggled = mc.gameSettings.keyBindRight.isKeyDown();
			space_toggled = mc.gameSettings.keyBindJump.isKeyDown();
			
		}
		
	}
	
}
