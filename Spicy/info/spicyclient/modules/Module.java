package info.spicyclient.modules;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.KeybindSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.Setting;
import info.spicyclient.settings.SettingChangeEvent;
import info.spicyclient.settings.SettingChangeEvent.type;
import net.minecraft.client.Minecraft;

public abstract class Module {
	
	public String name;
	public transient String additionalInformation = "";
	public boolean toggled = false;
	public transient boolean expanded = false;
	public transient boolean ClickGuiExpanded = false;
	public transient int index;
	
	public boolean isToggled() {
		return toggled;
	}

	public KeybindSetting keycode = new KeybindSetting(0);
	public Category category;
	public static Minecraft mc = Minecraft.getMinecraft();
	
	// This was changed because it was causing crashes inside of the click gui
	// public List<Setting> settings = new ArrayList<Setting>();
	
	public transient CopyOnWriteArrayList<Setting> settings = new CopyOnWriteArrayList<Setting>();
	
	public Module(String name, int key, Category category) {
		this.name = name;
		keycode.code = key;
		this.category = category;
		this.addSettings(keycode);
	}
	
	public void addSettings(Setting... settings) {
		
		this.settings.addAll(Arrays.asList(settings));
		this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
		
		if (!this.settings.contains(keycode)) {
			this.settings.add(keycode);
			this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
		}
		
		for (Setting s : settings) {
			if (s instanceof BooleanSetting) {
				this.onSettingChange(new SettingChangeEvent(type.BOOLEAN, s));
			}
			else if (s instanceof NumberSetting) {
				this.onSettingChange(new SettingChangeEvent(type.NUMBER, s));
			}
			else if (s instanceof ModeSetting) {
				this.onSettingChange(new SettingChangeEvent(type.MODE, s));
			}
			else if (s instanceof KeybindSetting) {
				this.onSettingChange(new SettingChangeEvent(type.KEYBIND, s));
			}
		}
		
	}
	
	public boolean isEnabled() {
		return toggled;
	}
	
	public int getKey() {
		return keycode.code;
	}
	
	public void onEvent(Event e) {
		
	}
	
	public void onEventWhenDisabled(Event e) {
		
	}
	
	public void onSettingChange(SettingChangeEvent e) {
		
	}
	
	public void toggle() {
		
		toggled = !toggled;
		if (toggled) {
			
			NotificationManager.getNotificationManager().createNotification("Enabled: " + name, "", true, 1500, Type.INFO, Color.GREEN);
			
			onEnable();
			
			if (SpicyClient.config.hud.sound.isEnabled()) {
				Minecraft.getMinecraft().thePlayer.playSound("random.click", (float) SpicyClient.config.hud.volume.getValue(), 0.6f);
			}
			
		}else {
			
			NotificationManager.getNotificationManager().createNotification("Disabled: " + name, "", true, 1500, Type.INFO, Color.RED);
			
			onDisable();
			
			if (SpicyClient.config.hud.sound.isEnabled()) {
				Minecraft.getMinecraft().thePlayer.playSound("random.click", (float) SpicyClient.config.hud.volume.getValue(), 0.4f);
			}
			
		}
		
		if (SpicyClient.discord != null && SpicyClient.discord.running) {
			SpicyClient.discord.refresh();
		}
		
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void reorderSettings() {
		this.settings.sort(Comparator.comparing(s -> s == keycode ? 1 : 0));
	}
	
	public void resetSettings() {
		this.settings.clear();
		this.addSettings();
	}
	
	public static List<Category> CategoryList;
	
	public enum Category{
		COMBAT("Combat"),
		MOVEMENT("Movement"),
		PLAYER("Player"),
		RENDER("Render"),
		MEMES("Memes"),
		BETA("Beta Modules"),
		WORLD("World");
		
		public String name;
		
		Category(String name){
			
			this.name = name;
			
		}
	}
	
}
