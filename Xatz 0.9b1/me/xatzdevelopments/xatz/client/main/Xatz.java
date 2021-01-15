package me.xatzdevelopments.xatz.client.main;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.select.Elements;

import me.xatzdevelopments.xatz.client.EventPacket;
import me.xatzdevelopments.xatz.alts.AltManager;
import me.xatzdevelopments.xatz.client.bypasses.BypassManager;
import me.xatzdevelopments.xatz.client.chat.ChatMananger;
import me.xatzdevelopments.xatz.client.commands.Command;
import me.xatzdevelopments.xatz.client.commands.CommandManager;
import me.xatzdevelopments.xatz.client.events.BlockPlaceEvent;
import me.xatzdevelopments.xatz.client.events.BoundingBoxEvent;
import me.xatzdevelopments.xatz.client.events.EntityHitEvent;
import me.xatzdevelopments.xatz.client.events.EntityInteractEvent;
import me.xatzdevelopments.xatz.client.events.EventAttack;
import me.xatzdevelopments.xatz.client.events.EventRenderEntity;
import me.xatzdevelopments.xatz.client.events.PreMotionEvent;
import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.gui.tab.TabGui;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.modules.AutoPotion;
import me.xatzdevelopments.xatz.client.modules.BowAimbot;

import me.xatzdevelopments.xatz.client.modules.DJ;
import me.xatzdevelopments.xatz.client.modules.DiscordRCP;
import me.xatzdevelopments.xatz.client.modules.TabGUI;
import me.xatzdevelopments.xatz.client.modules.AutoMLG;
import me.xatzdevelopments.xatz.client.modules.TargetHUD;
import me.xatzdevelopments.xatz.client.modules.IRC;
import me.xatzdevelopments.xatz.client.modules.BedNuker;
import me.xatzdevelopments.xatz.client.modules.AntiDesync;
import me.xatzdevelopments.xatz.client.modules.CakeEater;
import me.xatzdevelopments.xatz.client.modules.GhostMode;
import me.xatzdevelopments.xatz.client.modules.KillAura;
import me.xatzdevelopments.xatz.client.modules.RodAura;
import me.xatzdevelopments.xatz.client.modules.TpAura;
import me.xatzdevelopments.xatz.client.modules.scaffoldevents.InventoryUtil;
import me.xatzdevelopments.xatz.client.modules.scaffoldevents.MoveEvent;
import me.xatzdevelopments.xatz.client.modules.scaffoldevents.MovementUtil;
import me.xatzdevelopments.xatz.client.modules.scaffoldevents.RaycastUtil;
import me.xatzdevelopments.xatz.client.modules.scaffoldevents.RotationUtil;
import me.xatzdevelopments.xatz.client.modules.target.AuraUtils;
import me.xatzdevelopments.xatz.client.presets.PresetMananger;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.client.tools.LoadTools;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.cracker.CrackManager;
import me.xatzdevelopments.xatz.files.FileMananger;
import me.xatzdevelopments.xatz.friends.FriendsMananger;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.gui.NotificationManager;
import me.xatzdevelopments.xatz.gui.UIRenderer;
import me.xatzdevelopments.xatz.gui.custom.SearchBar;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ClickGuiManager;
import me.xatzdevelopments.xatz.gui.custom.clickgui.DisplayClickGui;
import me.xatzdevelopments.xatz.irc.IrcManager;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.module.group.ModuleGroup;
import me.xatzdevelopments.xatz.module.group.ModuleGroupMananger;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

public class Xatz {
	
	public static String loggedInName = null;
	public static boolean java8 = true;
	public static boolean promtedUpdate = false;
	public static boolean promtedAlert = false;
	public static boolean upToDate = true;
	public static boolean triedConnectToUpdate = false;
	public static boolean triedConnectToAlert = false;
	public static boolean changelogFailed = false;
	public static boolean customButtons = false;
	public static Elements changeLineElmts;
	public static String motd = null;
	public static boolean devVersion = false;
	public static String clientName = "Xatz";
	public static String registeredUser = null;
	private static String clientmultiVersion = "1.8x0.9b1";
	private static String clientVersion = "0.9b1";
	public static String serverVersion = null;
	private static String clientAuthor = "Xatz Dev Team";
	private static Minecraft mc = Minecraft.getMinecraft();
	private static ClickGuiManager clickGuiManager;
	public static IrcManager irc;
	private static UIRenderer uiRenderer;
	private static FileMananger fileMananger;
	private static PresetMananger presetMananger;
	private static FriendsMananger friendsMananger;
	private static ChatMananger chatMananger;
	private static ModuleGroupMananger moduleGroupMananger;
	private static CommandManager commandManager;
	private static CrackManager crackManager;
	private static BypassManager bypassManager;
	private static NotificationManager notificationManager;
	private static AltManager altManager;
	private static TabGui tabGui;
	public static final MovementUtil MOVEMENT_UTIL = new MovementUtil();
	public static final InventoryUtil INVENTORY_UTIL = new InventoryUtil();
	public static final RaycastUtil RAYCAST_UTIL = new RaycastUtil();
	public static final RotationUtil ROTATION_UTIL = new RotationUtil();
	public static boolean ghostMode = false;
	private static CopyOnWriteArrayList<Module> modules;
	public static boolean loaded = false;
	public static boolean performanceBoost = true;
	public static boolean debugMode = false;
	// public static ArrayList<Module> toggledList = new ArrayList<Module>();
	private static Robot clickRealRobot;
	public static ArrayList<String> mouseNames = new ArrayList<String>();
	public static ArrayList<String> nameTagNames = new ArrayList<String>();
	public static HashMap<String, String> devTagNames = new HashMap<String, String>();
	public static String[] proxy = null;
	public static boolean welcomed = false;
	public static boolean firstStart = false;

	public static final ResourceLocation xatzTextureMainMenu = new ResourceLocation("xatz/xatzmainmenu.png");
	public static final ResourceLocation xatzTextureTabGui = new ResourceLocation("xatz/xatztabgui.png");
	public static final ResourceLocation xatzTexture1024x512 = new ResourceLocation("xatz/xatz1024x512.png");
	public static final ResourceLocation xatzTexture1024 = new ResourceLocation("xatz/XATZ1024.png");
	public static ResourceLocation[] images = null;
	public static final ResourceLocation xatzImage2 = new ResourceLocation("xatz/MainMenuImage-1Blur.png");
	public static final ResourceLocation xatzImage4 = new ResourceLocation("xatz/MainMenuImage0Blur.png");
	public static final ResourceLocation xatzImage5 = new ResourceLocation("xatz/MainMenuImage-1.png");
	public static final ResourceLocation xatzImage6 = new ResourceLocation("xatz/MainMenuImage0.jpg");
	private static WaitTimer tpsTimer = new WaitTimer();
	public static double lastTps = 20.0;
	
	public static final ArrayList<ResourceLocation> gifLocations = new ArrayList<ResourceLocation>();
	
	public static final WaitTimer saveTimer = new WaitTimer();
	public static File jarFile = null;
	public static boolean gifMenu;
	
	private static Random rand = new Random();

	/**
	 * 
	 * Disables the GUI, the UI, and all Chat Messages
	 * 
	 */
	public static void ghostMode(boolean b) {
		getModuleByName("ClickGUI").setToggled(!b, true);
		getUIRenderer().setEnabled(!b);
		ghostMode = b;
	}

	public static void Register() {
		if(rand.nextInt(100) == 50) {
			Xatz.gifMenu = true;
		}
		try {
			jarFile = new File(Xatz.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		for(int i = 0; i < 282; i++) {
			gifLocations.add(new ResourceLocation("Xatz/gif/frame_" + String.valueOf(i) + "_delay-0.04s.png"));
		}
		LoadTools.doAntiLeak();
			
		images = new ResourceLocation[] { xatzImage5, xatzImage2, xatzImage4 , xatzImage6};
		modules = new CopyOnWriteArrayList<Module>();
		fileMananger = new FileMananger();
		try {
			fileMananger.load();
		} catch (IOException e) {
			System.out.println("File error!");
			e.printStackTrace();
			System.exit(-1);
		}

		notificationManager = new NotificationManager();
		getFileMananger().loadSettings();
		if (ClientSettings.lockGuiScale || Minecraft.getMinecraft().gameSettings.guiScale == 0) {
			Minecraft.getMinecraft().gameSettings.guiScale = 2;
		}
		modules = LoadTools.addModules();
		appendModule(new GhostMode());
		uiRenderer = new UIRenderer();

		clickGuiManager = new ClickGuiManager();
		clickGuiManager.setup();
		irc = new IrcManager(Minecraft.getMinecraft().session.getUsername());
		presetMananger = new PresetMananger();
		friendsMananger = new FriendsMananger();
		chatMananger = new ChatMananger();
		commandManager = new CommandManager();
		bypassManager = new BypassManager();
		altManager = new AltManager();
		
		tabGui = new TabGui();

		// Default module groups!
		moduleGroupMananger = new ModuleGroupMananger();
		moduleGroupMananger.addGroup(new ModuleGroup(new Module[] { Xatz.getModuleByName("KillAura"),
				Xatz.getModuleByName("TriggerBot"), Xatz.getModuleByName("MobArena"),
				Xatz.getModuleByName("TpAura"), Xatz.getModuleByName("GhostAura"),
				Xatz.getModuleByName("RodAura"), Xatz.getModuleByName("Aimbot") }, "AuraGroup"));

		moduleGroupMananger.addGroup(new ModuleGroup(
				new Module[] { Xatz.getModuleByName("Regen"), Xatz.getModuleByName("FastPusher") }, "RegenGroup"));

		moduleGroupMananger.addGroup(new ModuleGroup(
				new Module[] { Xatz.getModuleByName("Flight"), Xatz.getModuleByName("AirJump") }, "AirGroup"));
		try {
			clickRealRobot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		getFileMananger().loadModules();
		for (Module m : getModules()) {
			m.onClientLoad();
		}
		
		getFileMananger().loadGUI(clickGuiManager.windows);
		getFileMananger().loadFriends();
		getFileMananger().loadAlts();
		

		if(Xatz.firstStart) {
			System.out.println("Xatz first start!");
		}
		
		loaded = true;
		irc.connect();
		Xatz.getModuleByName("ItemSize").toggle();
	}
	
	public static void saveSettings() {
		getFileMananger().saveModules();
		//TODO fix this shit
		getFileMananger().saveGUI(Xatz.clickGuiManager.windows);
		getFileMananger().saveFriends();
		getFileMananger().saveSettings();
		getFileMananger().saveAlts();
	}

	public static void onMCClose() {
		if (Xatz.loaded) {
			saveSettings();
		}
	}

	public static void setToggledAllModules(boolean enabled) {
		for (Module m : Xatz.getModules()) {
			m.setToggled(enabled, true);
		}
	}

	public static int getModuleCount() {
		return modules.size();
	}

	public static void appendModule(Module module) {
		// TODO Module autotoggle
		modules.add(module);
		if ((module.getCategory() == Category.TARGET) && module.getName() != "Friends") {
			module.setToggled(true, true);
		}
	}

	public static final Category[] defaultCategories = new Category[] { Category.AUTOMATION, Category.COMBAT,
			Category.MOVEMENT, Category.MISC, Category.RENDER, Category.FUN, Category.MINIGAMES, Category.PLAYER,
			Category.EXPLOITS, Category.WORLD };

	public static ArrayList<Module> getModules() {

		ArrayList<Module> mods = new ArrayList<Module>();
		for (Module m : modules) {
			mods.add(m);
		}
		return mods;
	}

	public static ArrayList<Module> getToggledModules() {
		ArrayList<Module> a = new ArrayList<Module>();
		for (Module m : modules) {
			if (m.isToggled()) {
				a.add(m);
			}
		}
		return a;
	}

	public static ArrayList<Object> getToggledModulesObject() {
		ArrayList<Object> a = new ArrayList<Object>();
		for (Module m : modules) {
			if (m.isToggled()) {
				a.add(m);
			}
		}
		return a;
	}

	/**
	 * Gets an Xatz module by its name
	 * 
	 * @param name
	 *            The name
	 * @return The module which name was specified
	 */
	public static Module getModuleByName(String name) {
		Iterator<Module> iter = modules.iterator();
		while (iter.hasNext()) {
			Module mod = iter.next();
			if (mod.getName().equalsIgnoreCase(name)) {
				return mod;
			}
		}
		System.err.println("Module: " + name + " was not found");
		return null;
	}

	public static boolean isModuleName(String name) {
		for (Module m : modules) {
			if (m.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Filters modules
	 * 
	 * @param modules
	 * @param categories
	 *            Modules must be this category
	 * @param r
	 *            ReturnType. The modules name, keyboard, or the module
	 * @return An ArrayList of the filtered modules
	 */
	public static ArrayList<Object> filterModulesByCategory(ArrayList<Module> modules, Category[] categories,
			ReturnType r) {
		ArrayList<Object> filtered = new ArrayList<Object>();
		for (Module m : modules) {
			for (Category c : categories) {
				if (m.getCategory() == c) {
					if (r == ReturnType.MODULE) {
						filtered.add(m);
					}
					if (r == ReturnType.NAME) {
						filtered.add(m.getName());
					}
					if (r == ReturnType.KEYBOARD) {
						filtered.add(m.getKeyboardKey());
					}

					break;
				}
			}
		}
		return filtered;
	}

	public static String getClientName() {
		return clientName;
	}

	public static String getClientmultiVersion() {
		return clientmultiVersion;
	}
	
	public static String getClientVersion() {
		return clientVersion;
	}

	public static String getClientAuthor() {
		return clientAuthor;
	}

	public static UIRenderer getUIRenderer() {
		return uiRenderer;
	}

	public static FileMananger getFileMananger() {
		return fileMananger;
	}

	public static PresetMananger getPresetMananger() {
		return presetMananger;
	}

	public static FriendsMananger getFriendsMananger() {
		return friendsMananger;
	}

	public static ChatMananger getChatMananger() {
		return chatMananger;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static ModuleGroupMananger getModuleGroupMananger() {
		return moduleGroupMananger;
	}

	public static CrackManager getCrackManager() {
		return crackManager;
	}

	public static void setCrackManager(CrackManager crackManager) {
		Xatz.crackManager = crackManager;
	}

	public static void onError(Exception e, ErrorState errorState, Module module) {
		try {
			if (module != null) {
				try {
					System.err.println("Module: " + module.getName() + " ecountered an exception! /n"
							+ "Disabling module without update...");
					module.setToggled(false, false);
					e.printStackTrace();
				} catch (Exception e2) {
					System.err.println("Could not disable module...");

				}

			}
			getNotificationManager().addNotification(new Notification(Level.ERROR,
					e.getMessage() + ", at line: " + e.getStackTrace()[0].getLineNumber() + ", in class: "
							+ e.getStackTrace()[0].getFileName() + ", in method: "
							+ e.getStackTrace()[0].getMethodName()));

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static enum ErrorState {

		onRender("rendering"), onUpdate("updating"), onEnable("enabling"), onDisable("disabling"), onMessage(
				"recieving message"), onLeftClick("recieving leftclick"), onRightClick(
						"recieving rightclick"), onConstruct("contructing"), onPacketRecieved(
								"recieving packet"), onPacketSent("sending packet"), onEntityHit(
										"hitting entity"), onLateUpdate("updating (post)"), onGui(
												"rendering gui"), onPostMotion("updating (postMotion)"), onPreMotion(
														"updating (preMotion)"), onBasicUpdates(
																"updating (basicUpdates)"), onBlockPlace(
																		"placing block"), onEntityInteract(
																				"interacting with entity"), onBoundingBox(
																						"setting boundingbox"), onLivingUpdate(
																								"updating (living)");

		String text;

		ErrorState(String displayText) {
			this.text = displayText;
		}

		public String getDisplayText() {
			return this.text;
		}

	}

	public static void onEntityHit(EntityHitEvent entityHitEvent) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onEntityHit(entityHitEvent);
			} catch (Exception e) {
				onError(e, ErrorState.onEntityHit, module);
			}
		}
	}

	public static void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onBlockPlace(blockPlaceEvent);
			} catch (Exception e) {
				onError(e, ErrorState.onBlockPlace, module);
			}
		}
	}

	public static void onLeftClick() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onLeftClick();
			} catch (Exception e) {
				onError(e, ErrorState.onLeftClick, module);
			}
		}
	}

	public static void onRightClick() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onRightClick();
			} catch (Exception e) {
				onError(e, ErrorState.onRightClick, module);
			}
		}
	}

	public static void onChatMessageSent(C01PacketChatMessage packet) {
		try {
			chatMananger.onMessageSent(packet);
		} catch (Exception e) {
			onError(e, ErrorState.onMessage, null);
		}
	}

	public static void onChatMessageRecived(S02PacketChat packetIn) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onChatMessageRecieved(packetIn);
			} catch (Exception e) {
				onError(e, ErrorState.onMessage, module);
			}
		}
	}

	public static void onKeyPressed(int keyCode) {
		if(getTabGui() != null) {
			getTabGui().onKeyPressed(keyCode);
		}
		for (Module module : getModules()) {
			if (module.getKeyboardKey() == keyCode) {
				module.toggle();
			}
		}
	}

	public static void OnUpdate(UpdateEvent event) {
		Utils.spectator = false;
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if(getTabGui() != null) {
			getTabGui().update();
		}
		if (mc.currentScreen instanceof GuiDownloadTerrain) {
			Utils.blackList.clear();
		}
		Utils.updateLastGroundLocation();
		if (registeredUser == null) {
			mc.shutdown();
		}
		if (getCrackManager() != null) {
			getCrackManager().onUpdate();
		}
		//if (!Xatz.welcomed) {
		//	Xatz.getNotificationManager().addNotification(new Notification(Level.INFO,
		//			"Press left ctrl to open the click gui. Do .commands for a list of all commands!"));
		//	Xatz.welcomed = true;
		//}
		getNotificationManager().update();
		for (Module module : getToggledModules()) {
			try {
				ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
				if (group != null
						&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
						&& AuraUtils.getDisableAura()) {
					continue;
				}
				module.onUpdate(event);
				module.onUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onUpdate, module);
			}
		}
		if(saveTimer.hasTimeElapsed(100000, true)) {
			saveSettings();
		}
	}

	public static void onGui(boolean renderModules) {
		if(mc == null) {
			return;
		}
		if (getCrackManager() != null) {
			getCrackManager().onGui();
		}
//		if(getTabGui() != null && !ghostMode && !mc.gameSettings.showDebugInfo) {
//			getTabGui().render();
//		}
		getNotificationManager().draw();
		if (getUIRenderer() != null && !mc.gameSettings.showDebugInfo) {
			try {
				Xatz.getUIRenderer().render(renderModules);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		for (Module module : getToggledModules()) {
			try {
				module.onGui();
			} catch (Exception e) {
				onError(e, ErrorState.onGui, module);
			}

		}
	}
	
	public static void onPreLateUpdate() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPreLateUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLateUpdate, module);
			}

		}
	}

	public static void OnLateUpdate() {
		onPreLateUpdate();
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onLateUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLateUpdate, module);
			}

		}
	}

	public static void onPostMotion() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPostMotion();
			} catch (Exception e) {
				onError(e, ErrorState.onPostMotion, module);
			}

		}
	}

	public static void onPreMotion(PreMotionEvent event) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPreMotion(event);
			} catch (Exception e) {
				onError(e, ErrorState.onPreMotion, module);
			}

		}
	}
	
	public static void onMotion(MoveEvent event) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onMotion(event);
			} catch (Exception e) {
				onError(e, ErrorState.onPreMotion, module);
			}

		}
	}

	public static void onBasicUpdates() {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onBasicUpdates();
			} catch (Exception e) {
				onError(e, ErrorState.onBasicUpdates, module);
			}

		}
	}

	public static void OnRender() {
		if (!ghostMode) {
			getBypassManager().render();
			for (Module module : getToggledModules()) {
				ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
				if (group != null
						&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
						&& AuraUtils.getDisableAura()) {
					continue;
				}
				try {
					module.onRender();
				} catch (Exception e) {
					onError(e, ErrorState.onRender, module);
				}
			}
		}
	}
	
	private static ArrayList<Long> times = new ArrayList<Long>();
	public static Object eventBus;

	public static void onPacketRecieved(AbstractPacket modPacket) {
		if(modPacket instanceof S03PacketTimeUpdate) {
			times.add(Math.max(1000, tpsTimer.getTime()));
			long timesAdded = 0;
			if(times.size() > 5) {
				times.remove(0);
			}
			for(long l : times) {
				timesAdded += l;
			}
			long roundedTps = timesAdded / times.size();
			lastTps = (20.0 / roundedTps) * 1000.0;
			tpsTimer.reset();
		}
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPacketRecieved(modPacket);
			} catch (Exception e) {
				onError(e, ErrorState.onPacketRecieved, module);
			}
		}
	}

	public static void onPacketSent(AbstractPacket packet) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onPacketSent(packet);
			} catch (Exception e) {
				onError(e, ErrorState.onPacketSent, module);
			}
		}
	}

	public static void onEntityInteract(EntityInteractEvent event) {
		for (Module module : getToggledModules()) {
			ModuleGroup group = getModuleGroupMananger().getModuleGroupForModule(module);
			if (group != null
					&& group.getName().equals(getModuleGroupMananger().getModuleGroupByName("AuraGroup").getName())
					&& AuraUtils.getDisableAura()) {
				continue;
			}
			try {
				module.onEntityInteract(event);
			} catch (Exception e) {
				onError(e, ErrorState.onEntityInteract, module);
			}
		}
	}

	public static final String header = "§8[§c!§8] §7";
	public static final String headerNoBrackets = "§c§l Xa§r§8tz §7";

	public static void chatMessage(float message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(double message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(String message) {
		if (!ghostMode) {
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(header + message));
		}
	}

	public static void chatMessage(int message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(long message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(boolean message) {
		chatMessage(String.valueOf(message));
	}

	public static void chatMessage(String header, String message) {
		if (!ghostMode) {
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(header + message));
		}
	}

	public static void chatMessage(ChatComponentText e) {
		if (!ghostMode) {
			mc.ingameGUI.getChatGUI().printChatMessage(e);
		}
	}

	public static void click() {
		mc.clickMouse();
	}

	public static boolean doDisablePacketSwitch() {
		return KillAura.getShouldChangePackets() || BowAimbot.getShouldChangePackets() || DJ.getIsPlaying()
				|| TpAura.doBlock() || RodAura.getShouldChangePackets();
	}

	public static void clickReal() {
		clickRealRobot.mousePress(InputEvent.BUTTON1_MASK);
		clickRealRobot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public static void sendChatMessage(String message) {
		mc.getNetHandler().getNetworkManager().sendPacket(new C01PacketChatMessage(message));
	}

	public static void sendChatMessageFinal(String message) {
		mc.getNetHandler().getNetworkManager().sendPacketFinal(new C01PacketChatMessage(message));
	}

	public static String playerName() {
		return Minecraft.getMinecraft().thePlayer.getName();
	}

	public static boolean isCommand(String string) {
		for (Command cmd : getCommandManager().commands) {
			if (cmd.getName().equals(string)) {
				return true;
			}
		}
		return false;
	}

	public static Command getCommandByName(String string) {
		for (Command cmd : getCommandManager().commands) {
			if (cmd.getName().equals(string)) {
				return cmd;
			}
		}
		return null;
	}

	public static boolean getIsMouseHead(String name) {
		for (String s : mouseNames) {
			if (s.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean getIsTagName(String name) {
		for (String s : nameTagNames) {
			if (s.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static String getSearch() {
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if (mc.currentScreen instanceof DisplayClickGui) {
			return ((DisplayClickGui) mc.currentScreen).searchBar.typed;
		}
		return null;
	}

	public static SearchBar getSearchBar() {
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if (mc.currentScreen instanceof DisplayClickGui) {
			return ((DisplayClickGui) mc.currentScreen).searchBar;
		}
		return null;
	}

	public static void onBoundingBox(BoundingBoxEvent event) {
		for (Module module : getToggledModules()) {
			try {
				module.onBoundingBox(event);
			} catch (Exception e) {
				onError(e, ErrorState.onBoundingBox, module);
			}
		}
	}

	public static void onLivingUpdate() {
		for (Module module : getToggledModules()) {
			try {
				module.onLivingUpdate();
			} catch (Exception e) {
				onError(e, ErrorState.onLivingUpdate, module);
			}
		}
	}

	public static BypassManager getBypassManager() {
		return bypassManager;
	}

	public static NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public static TabGui getTabGui() {
		return tabGui;
	}

	public static AltManager getAltManager() {
		return altManager;
	}

	public static void onDeath() {
		for (Module module : getToggledModules()) {
			try {
				module.onDeath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ClickGuiManager getClickGuiManager() {
		return clickGuiManager;
	}
	
	private static final Xatz INSTANCE = new Xatz();
	public static final Xatz getInstance() {
		return INSTANCE; 
		}
	
	private DiscordRP discordRP = new DiscordRP();
	
	
	public void init() {
		discordRP.start();
	}
	
	public void shutdown() {
		discordRP.shutdown();
	}
	
	public DiscordRP getDiscordRP() {
		return discordRP;
	}
	
	private Font font;
	public Font getFontRenderer() {
		return font;
	}

	public static void OnUpdate(EventRenderEntity event) {
		// TODO Auto-generated method stub
		
	}

	public static void OnUpdate(EventAttack event) {
		// TODO Auto-generated method stub
		
	}

	public static void OnUpdate(EventPacket event) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
