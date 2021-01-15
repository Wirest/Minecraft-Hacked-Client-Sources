package me.xatzdevelopments.xatz.client.tools;

import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.modules.*;
import me.xatzdevelopments.xatz.client.modules.target.Friends;
import me.xatzdevelopments.xatz.client.modules.target.Invisible;
import me.xatzdevelopments.xatz.client.modules.target.MAC_Bypass;
import me.xatzdevelopments.xatz.client.modules.target.NoArmor;
import me.xatzdevelopments.xatz.client.modules.target.NoArmor2;
import me.xatzdevelopments.xatz.client.modules.target.NonPlayers;
import me.xatzdevelopments.xatz.client.modules.target.Players;
import me.xatzdevelopments.xatz.client.modules.target.Team;
import me.xatzdevelopments.xatz.client.modules.utils.AdventureMode;
import me.xatzdevelopments.xatz.client.modules.utils.CreativeMode;
import me.xatzdevelopments.xatz.client.modules.utils.SpectatorMode;
import me.xatzdevelopments.xatz.client.modules.utils.SurvivalMode;
import me.xatzdevelopments.xatz.client.modules.utils.Toggledownfall;
import me.xatzdevelopments.xatz.gui.GuiXatzAlert;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.client.Minecraft;

public class LoadTools {

	public static boolean showupdatetext = true;
	
	private static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

	public static CopyOnWriteArrayList<Module> addModules() {
		// appendModule(new Admins());
		appendModule(new AreaMine());
		appendModule(new Animations());
		appendModule(new AutoArmor());
		appendModule(new AutoBlock());
		appendModule(new AutoClicker());
		// appendModule(new AutoBuild());
		appendModule(new AutoSprint());
		appendModule(new AutoPotion());
		appendModule(new AutoSoup());
		appendModule(new AutoRod());
		appendModule(new AutoMine());
		appendModule(new AirJump());
		appendModule(new Aimbot());
		// appendModule(new AntiDrown());
		appendModule(new AdventureMode());
		appendModule(new AntiAFK());
		appendModule(new AntiFire());
		appendModule(new AntiHunger());
		appendModule(new AutoEat());
		appendModule(new AutoGapple());
		appendModule(new AutoMLG());
		appendModule(new AntiStacker());
		appendModule(new AntiRotate());
		appendModule(new AntiWeb());
		appendModule(new AntiDesync());
		appendModule(new AutoRespawn());
		appendModule(new AutoJump());
		appendModule(new AutoTools());
		appendModule(new AutoWalk());
		appendModule(new AutoSneak());
		//appendModule(new AutoStacker());
		// appendModule(new AutoParkour());
		appendModule(new ArmorBreaker());
		appendModule(new ArrowDodge());
		appendModule(new ArmorHUD());
		// appendModule(new Bettersprint());
		appendModule(new BedNuker());
		appendModule(new Bleach());
		appendModule(new Blink());
		appendModule(new BunnyHop());
		appendModule(new Breadcrumbs());
		appendModule(new BowAimbot());
		//appendModule(new BPS());
		appendModule(new ChestStealer());
		appendModule(new ClickGUI());
		appendModule(new ClickTeleport());
		// appendModule(new ChatSpam());
		appendModule(new Coords());
		appendModule(new Chams());
		appendModule(new CakeEater());
		appendModule(new CopsNCrims());
		appendModule(new CrashBlock());
		appendModule(new Criticals());
		appendModule(new CreativeMode());
		//appendModule(new CubecraftRevive());

		// appendModule(new DialUpPing());

		appendModule(new Decimator());
		//appendModule(new Dolphin());
		appendModule(new DiscordRCP());
		appendModule(new DJ());
		appendModule(new HUDColor());
		appendModule(new InventoryManager());
		appendModule(new IRC());
		
		appendModule(new ESP());
		appendModule(new Eagle());
		appendModule(new ExtendedReach());

		appendModule(new FastBow());
		appendModule(new FastEat());
		appendModule(new FastSneak());
		appendModule(new Flight());
		appendModule(new Fullbright());
		appendModule(new FPS());
		appendModule(new FastPusher());
		appendModule(new FastFall());
		appendModule(new Freecam());
		appendModule(new FakeHackers());

		// Ghost mode was here
		appendModule(new GhostAura());
		appendModule(new GodMode());

		//appendModule(new HackerDetect());
		//appendModule(new HackerDetectGUI());
		appendModule(new Headless());
		//appendModule(new Haste());
		appendModule(new HighJump());
		appendModule(new ItemPhysics());
		appendModule(new InstantPortal());
		appendModule(new InventoryPlus());
		appendModule(new InventoryMove());
		// appendModule(new I_LIEK_FOOD());
		appendModule(new Jesus());
		appendModule(new KeepSprint());

		appendModule(new KillAura());
		//appendModule(new Killaura2());
		appendModule(new Knockback());

		// <Target>
		appendModule(new MAC_Bypass());
		appendModule(new Friends());
		appendModule(new NonPlayers());
		appendModule(new Players());
		appendModule(new Invisible());
		appendModule(new Team());
		appendModule(new NoArmor());
		appendModule(new NoArmor2());
		// appendModule(new HurtResistant());

		// </Target>

		appendModule(new LongJump());
		
		//appendModule(new Magnet());
		appendModule(new MegaKB());
		appendModule(new MobArena());
		appendModule(new MiddleClickFriends());
		appendModule(new MultiUse());
		appendModule(new ModernHotbar());
		appendModule(new NameProtect());
		appendModule(new KillSults());
		//appendModule(new NameTag());
		appendModule(new NameTags());
		//appendModule(new NameTagsPlus());
		appendModule(new NoBreakDelay());
		appendModule(new NoClip());
		appendModule(new NoFall());
		appendModule(new NoHurtcam());
		appendModule(new NoRightDelay());
		appendModule(new NoSwing());
		appendModule(new NoSlowdown());
		appendModule(new Notifications());
		appendModule(new Nuker());

		//appendModule(new OpenBypasses());
		appendModule(new OPSign());
		//appendModule(new PaperDoll());
		appendModule(new Parkour());
		appendModule(new PerfectHorseJump());
		appendModule(new Phase());
		appendModule(new Lag());
		appendModule(new Ping());
		// appendModule(new RangeSpheres());
		appendModule(new Radar());
		//appendModule(new ReachAura());
		appendModule(new Regen());
		appendModule(new ReverseKnockback());
		appendModule(new RodAura());

		appendModule(new ServerCrasher());
		// appendModule(new Shaky());
		appendModule(new SafeWalk());
		appendModule(new SavePos());
		
		appendModule(new Scaffold());
		//appendModule(new Scaffold3());
		appendModule(new Step());
		
		appendModule(new SkinBlinker());
		appendModule(new SkinProtect());
		appendModule(new SurvivalMode());
		appendModule(new SpectatorMode());
		appendModule(new Speed());
		appendModule(new Spin());
		appendModule(new Spider());


		appendModule(new SneakyAssasins());

		// appendModule(new Teleport());
		// appendModule(new me.xatzdevelopments.xatz.client.modules.TabGui());
		appendModule(new TabGUI());
		appendModule(new TagsPlus());
		appendModule(new Teleport());
		appendModule(new Timer());
		appendModule(new TpEggBreaker());
		appendModule(new TPS());
		appendModule(new Tracers());
		appendModule(new TriggerBot());
		appendModule(new TpAura());
		appendModule(new TpGodMode());
		appendModule(new Toggledownfall());
		appendModule(new Disabler());
		// appendModule(new TypeWars());

		appendModule(new VPhase());

		appendModule(new XRay());

		// appendModule(new WallHack());
		// appendModule(new WatchdogDetect());
		appendModule(new WaterFart());	
		appendModule(new ItemSize());
		//appendModule(new WtfAura());
		// appendModule(new Zoom());
		return modules;
	}

	public static void appendModule(Module module) { // This bitch was an actual pain -GreatZardasht 29/08/2020 13:34 British Time (GreatZardasht#4218 on discord)
		// TODO Module autotoggle
		modules.add(module);
		if ((module.getCategory() == Category.TARGET
				&& module.getName() != "Friends" && module.getName() != "Skip Unarmored Players" && module.getName() != "Skip Unarmored Mobs") || module.getEnableAtStartup()) {
			module.setToggled(true, true);
		}
	}

	

	public static Runnable serverInfoFetcher = new Runnable() { // This bitch was an ctual pain -GreatZardasht 29/08/2020 13:34 British Time
		@Override
		public void run() {
			try {
				Connection conn = Jsoup.connect("https://xenodochial-yonath-28d341.netlify.app/xatzclient/msg.html");
				Document doc = conn.get();
				Element mouseNamesElmt = doc.getElementById("xatzMouseNames");
				Element tagNamesElmt = doc.getElementById("xatzTagNames");
				Element devTagNamesElmt = doc.getElementById("xatzNames");
				Element versionElmt = doc.getElementById("xatzVersion");
				Element motdElmt = doc.getElementById("xatzMOTD");
				String[] mouseNames = mouseNamesElmt.html().split(";");
				String[] tagNames = tagNamesElmt.html().split(";");
				String[] devTags = devTagNamesElmt.html().split("%");
				//System.out.println(devTagNamesElmt.html());
				for (int i = 0; i < mouseNames.length; i++) {

					Xatz.mouseNames.add(mouseNames[i]);
				}
				for (int i = 0; i < tagNames.length; i++) {

					Xatz.nameTagNames.add(tagNames[i]);
				}
				for (int i = 0; i < devTags.length; i++) {
					//System.out.println(devTags[i].split(":")[0].replaceAll("&amp;", "§"));
					//System.out.println(devTags[i].split(":")[1].replaceAll("&amp;", "§"));
					Xatz.devTagNames.put(devTags[i].split(":")[0].replaceAll("&amp;", "§"), devTags[i].split(":")[1].replaceAll("&amp;", "§"));
				}
				String version = versionElmt.html();
				String motd = motdElmt.html();
				Xatz.motd = motd.replaceAll("&amp;", "§");
				Xatz.serverVersion = version;
				// System.err.println("Server version:" + Xatz.serverVersion);
				// System.err.println("Client version:" +
				// Xatz.getClientVersion());
				if (!Xatz.serverVersion.trim().equals(Xatz.getClientmultiVersion()) && !Xatz.promtedUpdate) {
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						@Override
						public void run() {
							//Xatz.promtedUpdate = true;
							//Minecraft.getMinecraft().displayGuiScreen(new GuiXatzUpdate());
						}
					});
				} else {
					System.err.println("Client version up to date!");
					Xatz.triedConnectToUpdate = true;
					showupdatetext = false;
				}
				conn = Jsoup.connect("https://xenodochial-yonath-28d341.netlify.app/xatzclient/changelog.html");
				try {
					doc = conn.get();
					Element wsite_content = doc.getElementById("wsite-content");
					if (wsite_content != null) {
						for (Element el : wsite_content.children()) {
							//System.out.println(el.text());
						}
						Elements changeLinesElmts = wsite_content.child(0).child(0).child(0).child(0).child(0).child(2).child(0)
								.children();
						//System.out.println("SDADS" + changeLinesElmts.get(0).attributes().toString());
						Xatz.changeLineElmts = changeLinesElmts;
					} else {
						System.err.println("Failed to get changelog from the server! (changeElmt == null)");
						Xatz.changelogFailed = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					Xatz.changelogFailed = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to get information from the server!");
				Xatz.triedConnectToUpdate = true;
			}
		}
	};
	public static Runnable alertInfoFetcher = new Runnable() {
		@Override
		public void run() {
			try {
				Connection conn = Jsoup.connect("https://xenodochial-yonath-28d341.netlify.app/xatzclient/msg.html");
				conn.timeout(3000);
				Document doc = conn.get();
				Element alertElmt = doc.getElementById("xatzAlert");
				final String alert = alertElmt.html();
				if (!alert.trim().isEmpty()) {
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						@Override
						public void run() {
							Xatz.promtedAlert = true;
							Minecraft.getMinecraft().displayGuiScreen(new GuiXatzAlert(alert));
						}
					});
				}
				System.err.println("No alert info available from the server!");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to get alert info from the server!");
			}
		}
	};
	public static void doAntiLeak() {
		try {
			Connection conn = Jsoup.connect("https://xenodochial-yonath-28d341.netlify.app/xatzclient/antileak.html");
			conn.timeout(3000);
			Document doc = conn.get();
			Element leakHtml = doc.getElementById("lolitselax");
			final String alert = leakHtml.html();
			if(alert.trim().isEmpty() || alert.indexOf("=") == -1) {
				return;
			}
			String[] versions = alert.trim().split("=");
			if (versions.length != 0) {
				for(String s : versions) {
					if(!s.equalsIgnoreCase(Xatz.getClientmultiVersion())) {
						continue;
					}
					System.exit(-1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
