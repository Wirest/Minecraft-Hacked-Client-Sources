package store.shadowclient.client.module;

import java.util.List;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.hud.notifications.Notification;
import store.shadowclient.client.hud.notifications.NotificationManager;
import store.shadowclient.client.hud.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;

public class Module {
	private static String suffix;
    private String name, displayName;
    private boolean isMultiBinded;
    private int key, multiBindKey;
    private Category category;
    protected boolean toggled;
    public static Minecraft mc = Minecraft.getMinecraft();
    private static final Minecraft MC = Minecraft.getMinecraft();
    private List<Setting> values;

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
        toggled = false;

        setup();
    }
    
    public void onEnable() {
        Shadow.instance.eventManager.register(this);
    }
    public void onDisable() {
    	Shadow.instance.eventManager.unregister(this);
    }
    
    public void onToggle() {}
    
    public void toggle() {
        toggled = !toggled;
        onToggle();
        if(toggled) {
        	onEnable();
            NotificationManager.show(new Notification(NotificationType.INFO, " " + EnumChatFormatting.WHITE + "You " + EnumChatFormatting.GREEN + "enabled " + EnumChatFormatting.WHITE + getName(), 2));
        } else {
        	onDisable();
            NotificationManager.show(new Notification(NotificationType.INFO, " " + EnumChatFormatting.WHITE + "You " + EnumChatFormatting.RED + "disabled " + EnumChatFormatting.WHITE + getName(), 2));
        }
    }
    
    public void setState(boolean state) {
		this.toggle();
		if (state) {
			this.onEnable();
			NotificationManager.show(new Notification(NotificationType.INFO, " " + EnumChatFormatting.WHITE + "You " + EnumChatFormatting.GREEN + "enabled " + EnumChatFormatting.WHITE + getName(), 2));
			this.toggled = true;
		} else {
			this.onDisable();
			NotificationManager.show(new Notification(NotificationType.INFO, " " + EnumChatFormatting.WHITE + "You " + EnumChatFormatting.RED + "disabled " + EnumChatFormatting.WHITE + getName(), 2));
			this.toggled = false;
		}
	}
    public Minecraft getMc() {
		return MC;
	}
    public boolean getState() {
		return toggled;
	}
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getKey() {
        return key;
    }
    public boolean isMultiBinded() {
		return isMultiBinded;
	}
    public int getMultiBindKey() {
		return multiBindKey;
	}
    public Module setMultiBinded(boolean isMultiBinded) {
		this.isMultiBinded = isMultiBinded;
		return this;
	}
    public void setKey(int key) {
        this.key = key;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public boolean isToggled() {
        return toggled;
    }
    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setup() {}
    
    public void onRender() {
	}
    
    public Setting getValueByName(String valueName) {
		for (final Setting value : this.values) {
			if (value.getName().equalsIgnoreCase(valueName)) {
				return value;
			}
		}
		return null;
	}
    
    public Module module(final String mod) {
        return Shadow.instance.moduleManager.getModuleByName(mod);
    }
    
    public List<Setting> getValues() {
		return values;
	}
    
    public Setting setting(final String set) {
        return Shadow.instance.settingsManager.getSettingByName(set);
    }
    
    public boolean onSendChatMessage(String s) {
		return true;
	}

	public boolean onReceiveChatMessage(S02PacketChat packet) {
		return true;
	}
	public static String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
