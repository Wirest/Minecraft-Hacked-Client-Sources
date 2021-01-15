package info.spicyclient.files;

import java.io.IOException;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.Module.Category;
import info.spicyclient.modules.beta.TestModuleOne;
import info.spicyclient.modules.combat.*;
import info.spicyclient.modules.memes.*;
import info.spicyclient.modules.movement.*;
import info.spicyclient.modules.player.*;
import info.spicyclient.modules.render.*;
import info.spicyclient.modules.render.Snow.Snowflake;
import info.spicyclient.modules.world.*;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import net.minecraft.client.Minecraft;

public class Config {
	
	public String name;
	public String version;
	
	public TabGUI tabgui = new TabGUI();
	public ClickGui clickgui = new ClickGui();
	public Killaura killaura = new Killaura();
	public Fly fly = new Fly();
	public Sprint sprint = new Sprint();
	public Bhop bhop = new Bhop();
	public RainbowGUI rainbowgui = new RainbowGUI();
	public Fullbright fullbright = new Fullbright();
	public NoFall nofall = new NoFall();
	public Keystrokes keystrokes = new Keystrokes();
	public FastPlace fastplace = new FastPlace();
	public Step step = new Step();
	public NoHead noHead = new NoHead();
	public OldHitting oldHitting = new OldHitting();
	public NoSlow noSlow = new NoSlow();
	public OwOifier owoifier = new OwOifier();
	public ChatBypass chatBypass = new ChatBypass();
	public Safewalk safewalk = new Safewalk();
	public BlockFly blockFly = new BlockFly();
	public PlayerESP playerESP = new PlayerESP();
	public AntiVoid antiVoid = new AntiVoid();
	public LongJump longJump = new LongJump();
	public Spider spider = new Spider();
	public AltManager altManager = new AltManager();
	public Timer timer = new Timer();
	public AntiKnockback antiKnockback = new AntiKnockback();
	public Back back = new Back();
	public NoClip noClip = new NoClip();
	public Blink blink = new Blink();
	public AutoClicker autoClicker = new AutoClicker();
	public FastBreak fastBreak = new FastBreak();
	public InventoryManager inventoryManager = new InventoryManager();
	public Tophats tophat = new Tophats();
	public WorldTime worldTime = new WorldTime();
	public ChestStealer chestStealer = new ChestStealer();
	public NoRotate noRotate = new NoRotate();
	public SkyColor skyColor = new SkyColor();
	public Reach reach = new Reach();
	public CsgoSpinbot csgoSpinbot = new CsgoSpinbot();
	public YawAndPitchSpoof yawAndPitchSpoof = new YawAndPitchSpoof();
	public Antibot antibot = new Antibot();
	public PingSpoof pingSpoof = new PingSpoof();
	public KillSults killSults = new KillSults();
	public AutoLog autoLog = new AutoLog();
	public FloofyFoxes floofyFoxes = new FloofyFoxes();
	public Jesus jesus = new Jesus();
	public Phase phase = new Phase();
	public DougDimmadome dougDimmadome = new DougDimmadome();
	public Criticals criticals = new Criticals();
	public Wtap wtap = new Wtap();
	public TriggerBot triggerBot = new TriggerBot();
	public Trail trail = new Trail();
	public ReachNotify reachNotify = new ReachNotify();
	public HideName hideName = new HideName();
	public DiscordRichPresence discordRichPresence = new DiscordRichPresence();
	public AutoArmor autoArmor = new AutoArmor();
	public AntiLava antiLava = new AntiLava();
	public InvWalk invWalk = new InvWalk();
	public Mike mike = new Mike();
	public Disabler disabler = new Disabler();
	public SmallItems smallItems = new SmallItems();
	public LSD lsd = new LSD();
	public Tracers tracers = new Tracers();
	public BlockCoding blockCoding = new BlockCoding();
	public TestModuleOne testModuleOne = new TestModuleOne();
	public Hypixel5SecDisabler hypixel5SecDisabler = new Hypixel5SecDisabler();
	public Hud hud = new Hud();
	public Snow snow = new Snow();
	public TargetStrafe targetStrafe = new TargetStrafe();
	public Eagle eagle = new Eagle();
	public Parkour parkour = new Parkour();
	public Furries furries = new Furries();
	
	public String clientName = "SpicyClient ", clientVersion = "B3 Beta";
	
	public Config(String name) {
		this.name = name;
		this.version = clientVersion;
	}
	
	public void saveConfig() {
		
	}
	
	public boolean updateConfig() {
		
		Config temp = new Config("temp");
		
		if (this.version.equalsIgnoreCase(temp.version)) {
			return false;
		}
		
		Command.sendPrivateChatMessage("Outdated config detected!");
		Command.sendPrivateChatMessage("This config is from the version " + this.version);
		Command.sendPrivateChatMessage("Updating the config to the version " + temp.version + "...");
		
		if (this.version.equalsIgnoreCase("B1") || this.version.equalsIgnoreCase("B2 Beta")) {
			
			Command.sendPrivateChatMessage("Legacy configs are not supported, legacy configs are configs from the versions B1 and B2 Beta");
			return true;
			
		}
		else if (this.version.equalsIgnoreCase("B2")) {
			
			this.noClip = new NoClip();
			this.autoLog = new AutoLog();
			this.floofyFoxes = new FloofyFoxes();
			this.jesus = new Jesus();
			this.phase = new Phase();
			this.dougDimmadome = new DougDimmadome();
			this.playerESP = new PlayerESP();
			this.criticals = new Criticals();
			this.wtap = new Wtap();
			this.triggerBot = new TriggerBot();
			this.chatBypass = new ChatBypass();
			this.trail = new Trail();
			this.reachNotify = new ReachNotify();
			this.oldHitting = new OldHitting();
			this.hideName = new HideName();
			this.killSults = new KillSults();
			this.discordRichPresence = new DiscordRichPresence();
			this.autoArmor = new AutoArmor();
			this.antiLava = new AntiLava();
			this.invWalk = new InvWalk();
			this.mike = new Mike();
			this.disabler = new Disabler();
			this.smallItems = new SmallItems();
			this.lsd = new LSD();
			this.tracers = new Tracers();
			this.blockCoding = new BlockCoding();
			this.testModuleOne = new TestModuleOne();
			this.hypixel5SecDisabler = new Hypixel5SecDisabler();
			this.hud = new Hud();
			this.snow = new Snow();
			this.targetStrafe = new TargetStrafe();
			this.eagle = new Eagle();
			this.parkour = new Parkour();
			this.furries = new Furries();
			
			this.killaura.dontHitDeadEntitys = new BooleanSetting("Don't hit dead entitys", false);
			this.killaura.newAutoblock = new ModeSetting("Autoblock mode", "None", "None", "Vanilla", "Hypixel");
			this.killaura.newAutoblock.cycle(false);
			this.killaura.newAutoblock.cycle(false);
			this.killaura.targetingMode = new ModeSetting("Targeting mode", "Single", "Single", "Switch");
			this.killaura.switchTime = new NumberSetting("Switch Time", 2, 0.1, 10, 0.1);
			this.killaura.targetsSetting.index = this.killaura.targetModeSetting.index;
			this.killaura.targetsSetting = this.killaura.targetModeSetting;
			this.killaura.rotationSetting = new ModeSetting("Rotation setting", "lock", "lock", "smooth");
			this.killaura.hitOnHurtTime = new BooleanSetting("Hit on hurt time", false);
			
			this.tabgui.mode = new ModeSetting("Mode", "original", "compressed", "original");
			
			this.nofall.noFallMode = new ModeSetting("NoFall Mode", "Vanilla", "Vanilla", "Packet");
			this.nofall.noFallMode.cycle(false);
			
			this.clickgui.colorSettingRed = new NumberSetting("Red", 255, 0, 255, 1);
			this.clickgui.colorSettingGreen = new NumberSetting("Red", 255, 0, 255, 1);
			this.clickgui.colorSettingBlue = new NumberSetting("Red", 255, 0, 255, 1);
			
			this.fly.mode = new ModeSetting("Mode", this.fly.mode.getMode(), "Vanilla", "Hypixel", "HypixelFast1");
			this.fly.hypixelBlink = new BooleanSetting("Blink", true);
			this.fly.hypixelTimerBoost = new BooleanSetting("Hypixel timer boost", true);
			this.fly.hypixelSpeed = new NumberSetting("Speed", 0.18, 0.05, 0.2, 0.005);
			this.fly.hypixelBoostSpeed = new NumberSetting("Fall speed boost", 2.2, 1.0, 10, 0.1);
			this.fly.hypixelFastFly1Speed = new NumberSetting("Speed", 0.2675, 0.01, 1.0, 0.0025);
			this.fly.hypixelFastFly1StopOnDisable = new BooleanSetting("Stop on disable", true);
			this.fly.hypixelFastFly1Blink = new BooleanSetting("Blink", false);
			this.fly.hypixelFastFly1Decay = new NumberSetting("Decay", 18, 2, 35, 1);
			
			this.bhop.hypixelSpeed = new NumberSetting("Speed", 0.01, 0.0001, 0.03, 0.0001);
			
			this.antiKnockback.horizontalKnockback = new NumberSetting("Horizontal Knockback", 0, 0, 100, 1);
			this.antiKnockback.verticalKnockback = new NumberSetting("Vertical Knockback", 0, 0, 100, 1);
			
			this.killSults.messageMode = new ModeSetting("Message Type", this.killSults.messageMode.getMode(), "Furry", "Retarded Furry", "Annoying", "SpicyClient Ads", "SpicyFacts");
			this.killSults.hypixelShout = new BooleanSetting("/Shout", false);
			
			this.hud.sound = clickgui.sound;
			this.hud.volume = clickgui.volume;
			this.hud.mode = clickgui.mode;
			this.hud.colorSettingRed = clickgui.colorSettingRed;
			this.hud.colorSettingGreen = clickgui.colorSettingGreen;
			this.hud.colorSettingBlue = clickgui.colorSettingBlue;
			this.hud.name = "HUD";
			this.hud.toggled = true;
			
			this.oldHitting.animationSetting.modes.add("Astolfo");
			
			this.antiVoid.jumpFirst = new BooleanSetting("Jump first", false);
			
			this.inventoryManager.category = Category.PLAYER;
			
		}
		
		this.version = temp.version;
		System.out.println(this.clientVersion);
		if (this.clientVersion != "") {
			this.clientVersion = temp.clientVersion;
		}
		
		Command.sendPrivateChatMessage("Config updated :)");
		
		return false;
		
	}
	
	public void loadConfig() {
		
		if (updateConfig()) {
			Command.sendPrivateChatMessage("Config load canceled");
			return;
		}
		
		// This does not work because the gson lib is shit
		
		if (this.clientVersion.toLowerCase().replace(this.version.toLowerCase(), "").length() == 0) {
			Config temp = new Config("temp");
			this.clientName = temp.clientName;
			this.clientVersion = this.version;
		}
		
		SpicyClient.loadConfig(this);
		
		Minecraft.getMinecraft().timer.ticksPerSecond = 20;
		
		for (Module m : SpicyClient.modules) {
			m.resetSettings();
			m.toggle();
			m.toggle();
		}
		
		SpicyClient.config = this;
		
		NotificationManager.getNotificationManager().notificationQueue.clear();
		
		try {
			FileManager.save_config(this.name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
