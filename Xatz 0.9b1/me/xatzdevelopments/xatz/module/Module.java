package me.xatzdevelopments.xatz.module;

import java.util.Random;

import org.darkstorm.minecraft.gui.component.Component;
import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.BlockPlaceEvent;
import me.xatzdevelopments.xatz.client.events.BoundingBoxEvent;
import me.xatzdevelopments.xatz.client.events.EntityHitEvent;
import me.xatzdevelopments.xatz.client.events.EntityInteractEvent;
import me.xatzdevelopments.xatz.client.events.Event;
import me.xatzdevelopments.xatz.client.events.EventMotion;
import me.xatzdevelopments.xatz.client.events.EventRenderEntity;
import me.xatzdevelopments.xatz.client.events.PreMotionEvent;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.main.Xatz.ErrorState;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.module.state.ModuleState;
import me.xatzdevelopments.xatz.client.modules.scaffoldevents.MoveEvent;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S02PacketChat;

public class Module {

	protected static Minecraft mc = Minecraft.getMinecraft();
	protected static Random rand = new Random();

	private String name;
	private int keyCode;
	private int defaultKeyCode;
	private boolean toggled;
	private Category category;
	protected String currentMode;
	protected String currentModeName;
	public String description = "";
	public boolean settingsDisplayed = false;
	public int toggleTime = 0;

	public Module(String name, int defaultKeyCode, Category cat) {
		this.name = name;
		this.defaultKeyCode = defaultKeyCode;
		this.keyCode = defaultKeyCode;
		this.toggled = false;
		this.category = cat;
		if (this.getModes().length == 0) {
			this.currentMode = "default";
			this.currentModeName = "Mode";
		} else {
			this.currentMode = this.getModes()[0];
			this.currentMode = this.getModeName();
		}
	}

	public Module(String name, int defaultKeyCode, Category cat, String description) {
		this.name = name;
		this.defaultKeyCode = defaultKeyCode;
		this.keyCode = defaultKeyCode;
		this.toggled = false;
		this.category = cat;
		if (this.getModes().length == 0) {
			this.currentMode = "default";
		} else {
			this.currentMode = this.getModes()[0];
		}
		this.description = description;
	}

	public void toggle() {
		this.setToggled(!this.toggled, true);
	}

	public Category getCategory() {
		return this.category;
	}

	public void onToggle() {
		toggleTime = 0;
		if (this.toggled) {
			try {
				onEnable();
			} catch (Exception e) {
				Xatz.onError(e, ErrorState.onEnable, this);
			}
		} else {
			try {
				onDisable();
			} catch (Exception e) {
				Xatz.onError(e, ErrorState.onDisable, this);
			}

		}
	}

	public void onLeftClick() {

	}

	public void onRightClick() {

	}

	public void onEnable() {
		if (Xatz.loaded) {
			if(this.category != Category.HIDDEN && this.category != Category.UTILS && ClientSettings.notificationModulesEnable 
					&& Xatz.getModuleByName("Notifications").isToggled()) {
				Xatz.getNotificationManager().addNotification(new Notification(Level.INFO, this.getName() + " enabled!"));
			}
			Xatz.getModuleGroupMananger().disableGroupModsForModule(this);
			
		}
	}

	public void onDisable() {
		if (Xatz.loaded) {
			if(this.category != Category.HIDDEN && this.category != Category.UTILS && ClientSettings.notificationModulesDisable 
					&& Xatz.getModuleByName("Notifications").isToggled()) {
				Xatz.getNotificationManager().addNotification(new Notification(Level.INFO, this.getName() + " disabled!"));
			}
		}
	}

	public void onUpdate() {
		toggleTime++;
	}
	
	public void onUpdate(UpdateEvent event) {
		
	}

	public void onLateUpdate() {

	}

	public void onRender() {

	}

	public void onDeath() {

	}

	public void onGui() {

	}

	protected void onModeChanged(String modeBefore, String newMode) {
		
	}

	public void onChatMessageRecieved(S02PacketChat packetIn) {

	}

	public void onPreMotion(PreMotionEvent event) {

	}

	public void onPostMotion() {

	}

	public void onBasicUpdates() {

	}

	public void onPacketRecieved(AbstractPacket packetIn) {

	}

	public void onPacketSent(AbstractPacket packet) {

	}

	public void sendPacketFinal(AbstractPacket packet) {
		mc.getNetHandler().getNetworkManager().sendPacketFinal(packet);
	}

	public void sendPacket(AbstractPacket packet) {
		mc.getNetHandler().getNetworkManager().sendPacket(packet);
	}

	public void onClientLoad() {

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKeyboardKey() {
		return this.keyCode;
	}

	public int getDefaultKeyboardKey() {
		return this.defaultKeyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
		if(Xatz.loaded) {
			Xatz.getNotificationManager().addNotification(new Notification(Level.INFO, 
					this.getName() + " was bound to key: " + Keyboard.getKeyName(keyCode)));
		}
	}
	
	


	public void setToggled(boolean toggled, boolean update) {
		this.toggled = toggled;
		if(toggled) {
			if (Xatz.loaded) {
				boolean allowed = !Xatz.getBypassManager().disableBadMod(this);
				if(!allowed) {
					this.toggled = false;
					return;
				}
			}
		}
		if (update) {
			try {
				onToggle();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			if (toggled) {
				// Xatz.toggledList.add(this);
			} else {
				// Xatz.toggledList.remove(this);
			}
		}
	}

	public boolean isToggled() {
		return this.toggled;
	}

	public void setMode(String string) {
		for (String mode : getModes()) {
			if (string.equals(mode)) {
				this.currentMode = mode;
				return;
			}
		}
		System.out.println("Mode: §6" + string + "§7 does not exist switching to default mode...");
		if (this.getModes().length == 0) {
			this.currentMode = "default";
		} else {
			this.currentMode = this.getModes()[0];
		}
		if(Xatz.loaded) {
			this.onModeChanged(this.currentMode, string);
		}
		
	}

	public String getCurrentMode() {
		return this.currentMode;
	}
	
	public String getModeName() {
		return this.currentModeName;
	}

	@Deprecated
	public void setAddonText(String string) {
	}

	public String getAddonText() {
		if (this.currentMode.equals("default")) {
			return null;
		} else {
			return this.currentMode;
		}
	}

	public String[] getModes() {
		return new String[] { "default" };
	}

	public static Module getInstance(Class s) {
		return Xatz.getModuleByName(s.getSimpleName());
	}

	public void onEntityHit(EntityHitEvent entityHitEvent) {

	}

	public int getLengthForSort() {
		if (this.getModes().length == 1) {
			return this.getName().length();
		} else {
			return (this.getName() + this.getAddonText()).length() + 3;
		}
	}

	public void setState(ModuleState state) {
		this.toggled = state.getToggled();
		this.setMode(state.getMode());
		this.keyCode = state.getKeyBind();
	}

	public ModuleState createState() {
		return new ModuleState(this);
	}

	public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {

	}

	public boolean dontToggleOnLoadModules() {
		return false;
	}

	public boolean getEnableAtStartup() {
		return false;
	}

	public void onEntityInteract(EntityInteractEvent event) {

	}

	public boolean mode(String mode) {
		return this.currentMode.equals(mode);
	}

	public ModSetting[] getModSettings() {
		return null;
	}

	public void onBoundingBox(BoundingBoxEvent event) {

	}

	public boolean isCheckbox() {
		return false;
	}

	public void onLivingUpdate() {
		
	}

	public void onPreLateUpdate() {
		
	}

	public void onEvent(EventMotion event) {
		// TODO Auto-generated method stub
		
	}

	public void onUpdate(Event event) {
		// TODO Auto-generated method stub
		
	}

	public void onEvent(Event e) {
		// TODO Auto-generated method stub
		
	}

	public void onUpdate(EventRenderEntity event) {
		// TODO Auto-generated method stub
		
	}

	public void onEvent(EventRenderEntity event) {
		// TODO Auto-generated method stub
		
	}

	public void onMotion(MoveEvent event) {
		// TODO Auto-generated method stub
		
	}

	
}
