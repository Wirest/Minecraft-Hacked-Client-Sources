package info.spicyclient.modules.player;

import java.awt.Color;
import java.util.List;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.blockCoding.ui.BlockCodingMainUi;
import info.spicyclient.blockCoding.ui.CreateModule;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventKey;
import info.spicyclient.events.listeners.EventRenderGUI;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.SettingChangeEvent;
import info.spicyclient.ui.HUD;
import info.spicyclient.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;

public class BlockCoding extends Module{
	
	public static BlockCodingMainUi BlockCodeUi = new BlockCodingMainUi("test");
	
	public BlockCoding() {
		super("BlockCode", Keyboard.KEY_NONE, Category.PLAYER);
		
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
		mc.displayGuiScreen(new CreateModule());
		toggle();
	}
	
	public void onDisable() {
		
	}
	
}
