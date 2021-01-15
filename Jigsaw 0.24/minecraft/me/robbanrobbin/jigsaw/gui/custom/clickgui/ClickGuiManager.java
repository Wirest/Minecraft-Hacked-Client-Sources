package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ClickGuiManager {
	
	public ArrayList<ModuleWindow> windows = new ArrayList<ModuleWindow>();
	final Map<Category, ModuleWindow> catWindows = new HashMap<Category, ModuleWindow>();
	public float globalAlphaMinus = 0f;
	
	public int mouseX;
	
	public int mouseY;
	
	public float getAlphaDec() {
		return 0.5f;
	}
	
	public float getAlpha() {
		if(globalAlphaMinus == 0) {
			return 0;
		}
		return globalAlphaMinus - GuiUtils.partialTicks * getAlphaDec();
	}
	
	public void setup() {
		for(Category cat : Category.values()) {
			if (cat == Category.HIDDEN) {
				continue;
			}
			ModuleWindow wind = new ModuleWindow();
			wind.setExtended(false);
			wind.setX(10);
			wind.setY(10);
			wind.setCategory(cat);
			String name = cat.name().toLowerCase();
			name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
			wind.setTitle(name);
			catWindows.put(cat, wind);
		}
		for(Module m : Jigsaw.getModules()) {
			if (m.getCategory() == Category.HIDDEN) {
				continue;
			}
			if(m.isCheckbox()) {
				ModCheckBtn btn = new ModCheckBtn();
				btn.setMod(m);
				btn.setHeight(12);
				if (m.getKeyboardKey() == Keyboard.KEY_NONE) {
					btn.setTitle(m.getName());
				} else {
					btn.setTitle(m.getName() + "[" + Keyboard.getKeyName(m.getKeyboardKey()) + "]");
				}
				catWindows.get(m.getCategory()).addChild(btn);
			}
			else {
				ModuleButton btn = new ModuleButton();
				btn.setMod(m);
				btn.setHeight(12);
				if (m.getKeyboardKey() == Keyboard.KEY_NONE) {
					btn.setTitle(m.getName());
				} else {
					btn.setTitle(m.getName() + "[" + Keyboard.getKeyName(m.getKeyboardKey()) + "]");
				}
				SettingContainer con = new SettingContainer();
				con.extended = false;
				con.addChild(new KeybindButton(m));
				btn.setSettingContainer(con);
				if(m.getModes().length > 1) {
					ModeButton modeBtn = new ModeButton(m);
					con.addChild(modeBtn);
				}
				if(m.getModSettings() != null) {
					for(ModSetting setting : m.getModSettings()) {
						if(setting == null) {
							continue;
						}
						con.addChild(setting.createComponent());
					}
				}
				catWindows.get(m.getCategory()).addChild(btn);
				catWindows.get(m.getCategory()).addChild(con);
			}
			
		}
		for(ModuleWindow wind : catWindows.values()) {
			if(wind.getChildren().isEmpty()) {
				continue;
			}
			windows.add(wind);
		}
		{
			CheckBtnSetting setting = new CheckBtnSetting("Smooth Aiming", "smoothAim");
			catWindows.get(Category.SETTINGS).addChild(setting.createComponent());
		}
		{
			SliderSetting setting = new SliderSetting<Number>("Smooth Aim Factor", ClientSettings.smoothAimSpeed, 1.5, 5.0, 0.0, ValueFormat.PERCENT);
			catWindows.get(Category.SETTINGS).addChild(setting.createComponent());
		}
		int x = 0 + 10;
		int y = 0 + 10;
		for(ModuleWindow wind : windows) {
			wind.layoutChildren();
			wind.setX(x);
			wind.setY(y);
			x += wind.getWidth() + 10;
			if(x + wind.getWidth() > Minecraft.getMinecraft().displayWidth / 2) {
				y += 20;
				x = 10;
			}
		}
	}
	
	public void draw() {
		GlStateManager.scale(2d / Minecraft.getMinecraft().gameSettings.guiScale, 2d / Minecraft.getMinecraft().gameSettings.guiScale, 1);
		for(ModuleWindow wind : windows) {
			wind.draw();
		}
	}
	
	public void update() {
		for(ModuleWindow wind : windows) {
			wind.update();
		}
	}
	
	public void reload() {
		windows.clear();
		catWindows.clear();
		setup();
	}
	
}
