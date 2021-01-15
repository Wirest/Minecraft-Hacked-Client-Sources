package info.spicyclient.modules.render;

import java.awt.Color;
import java.util.List;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventKey;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.SettingChangeEvent;
import info.spicyclient.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGui extends Module{
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static info.spicyclient.ClickGUI.ClickGUI clickGui = new info.spicyclient.ClickGUI.ClickGUI(null);
	
	public BooleanSetting sound = new BooleanSetting("Sound", true);
	public NumberSetting volume = new NumberSetting("Volume", 0.5, 0.1, 1.0, 0.1);
	public ModeSetting mode = new ModeSetting("Separator Mode", " | ", " | ", " OwO ", " UwU ", " |OwO| ", " |UwU| ", "Switch between OwO and UwU", "Switch between :OwO: and :UwU:", " - ");
	public NumberSetting colorSettingRed = new NumberSetting("Red", 255, 0, 255, 1);
	public NumberSetting colorSettingGreen = new NumberSetting("Green", 255, 0, 255, 1);
	public NumberSetting colorSettingBlue = new NumberSetting("Blue", 255, 0, 255, 1);
	
	public ClickGui() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
	}
	
	@Override
	public void toggle() {
		toggled = !toggled;
		if (toggled) {
			onEnable();
		}else {
			onDisable();
		}
	}
	
	public void onEnable() {
		info.spicyclient.ClickGUI.ClickGUI clickGui = new info.spicyclient.ClickGUI.ClickGUI(null);
		mc.displayGuiScreen(clickGui);
	}
	
	public void onDisable() {
		mc.displayGuiScreen(clickGui.last);
	}
	
	@Override
	public void onSettingChange(SettingChangeEvent e) {
		
	}
	
	private Timer timer = new Timer();
	private boolean OwO = true;
	
	public void onEvent(Event e) {
		
	}
	
}
