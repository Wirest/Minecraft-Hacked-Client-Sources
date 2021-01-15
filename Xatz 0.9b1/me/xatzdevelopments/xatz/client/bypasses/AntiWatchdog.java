package me.xatzdevelopments.xatz.client.bypasses;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;
import me.xatzdevelopments.xatz.module.Module;

public class AntiWatchdog extends Bypass {
	
	@Override
	public void onEnable() {
		ClientSettings.KillauraAPS = 8;
		ClientSettings.KillauraRange = 4.2;
		Xatz.getModuleByName("Invisible").setToggled(false, true);
		Xatz.getModuleByName("Flight").setMode("AirWalk");
		Xatz.getModuleByName("AutoSneak").setMode("Client");
		Xatz.getModuleByName("FastEat").setMode("NCP");
		Xatz.getModuleByName("BunnyHop").setMode("SlowHop");
		ClientSettings.chestStealDelay = true;
		ClientSettings.ExtendedReachrange = 4.2;
		Xatz.getNotificationManager().addNotification(new Notification(Level.WARNING, "AntiWatchdog does not bypass the fake entity yet!"));
		super.onEnable();
	}

	@Override
	public void setBypassableMods() {
		super.setBypassableMods();
		this.allowedMods.add(Xatz.getModuleByName("AutoSoup"));
		this.allowedMods.add(Xatz.getModuleByName("AutoBlock"));
		this.allowedMods.add(Xatz.getModuleByName("AutoSprint"));
		this.allowedMods.add(Xatz.getModuleByName("Animations"));
		this.allowedMods.add(Xatz.getModuleByName("Aimbot"));
		this.allowedMods.add(Xatz.getModuleByName("AutoEat"));
		this.allowedMods.add(Xatz.getModuleByName("AntiHunger"));
		this.allowedMods.add(Xatz.getModuleByName("AutoRespawn"));
		this.allowedMods.add(Xatz.getModuleByName("AutoJump"));
		this.allowedMods.add(Xatz.getModuleByName("AutoWalk"));
		this.allowedMods.add(Xatz.getModuleByName("AutoSneak"));
		this.allowedMods.add(Xatz.getModuleByName("Bleach"));
		this.allowedMods.add(Xatz.getModuleByName("Blink"));
		this.allowedMods.add(Xatz.getModuleByName("BunnyHop"));
		this.allowedMods.add(Xatz.getModuleByName("Breadcrumbs"));
		this.allowedMods.add(Xatz.getModuleByName("BowAimbot"));
		this.allowedMods.add(Xatz.getModuleByName("ClickGUI"));
		this.allowedMods.add(Xatz.getModuleByName("Criticals"));
		this.allowedMods.add(Xatz.getModuleByName("Coords"));
		this.allowedMods.add(Xatz.getModuleByName("CopsNCrims"));
		this.allowedMods.add(Xatz.getModuleByName("ChestStealer"));
		this.allowedMods.add(Xatz.getModuleByName("ESP"));
		this.allowedMods.add(Xatz.getModuleByName("ExtendedReach"));
		this.allowedMods.add(Xatz.getModuleByName("FastEat"));
		this.allowedMods.add(Xatz.getModuleByName("Flight"));
		this.allowedMods.add(Xatz.getModuleByName("Fullbright"));
		this.allowedMods.add(Xatz.getModuleByName("FPS"));
		this.allowedMods.add(Xatz.getModuleByName("Freecam"));
		this.allowedMods.add(Xatz.getModuleByName("FakeHackers"));
		this.allowedMods.add(Xatz.getModuleByName("GhostAura"));
		this.allowedMods.add(Xatz.getModuleByName("HackerDetect"));
		this.allowedMods.add(Xatz.getModuleByName("HackerDetectGUI"));
		this.allowedMods.add(Xatz.getModuleByName("Headless"));
		this.allowedMods.add(Xatz.getModuleByName("HighJump"));
		this.allowedMods.add(Xatz.getModuleByName("Inventory+"));
		this.allowedMods.add(Xatz.getModuleByName("InvMove"));
		this.allowedMods.add(Xatz.getModuleByName("KeepSprint"));
		this.allowedMods.add(Xatz.getModuleByName("KillAura"));
		this.allowedMods.add(Xatz.getModuleByName("Knockback"));
		this.allowedMods.add(Xatz.getModuleByName("Friends"));
		this.allowedMods.add(Xatz.getModuleByName("NonPlayers"));
		this.allowedMods.add(Xatz.getModuleByName("Players"));
		this.allowedMods.add(Xatz.getModuleByName("Team"));
		this.allowedMods.add(Xatz.getModuleByName("GWEN Bypass"));
		this.allowedMods.add(Xatz.getModuleByName("GhostMode"));
		this.allowedMods.add(Xatz.getModuleByName("LongJump"));
		this.allowedMods.add(Xatz.getModuleByName("MegaKnockback"));
		this.allowedMods.add(Xatz.getModuleByName("MobArena"));
		this.allowedMods.add(Xatz.getModuleByName("MiddleClickFriends"));
		this.allowedMods.add(Xatz.getModuleByName("NameProtect"));
		this.allowedMods.add(Xatz.getModuleByName("NameTags"));
		this.allowedMods.add(Xatz.getModuleByName("NoFall"));
		this.allowedMods.add(Xatz.getModuleByName("NoHurtcam"));
		this.allowedMods.add(Xatz.getModuleByName("NoSlowdown"));
		this.allowedMods.add(Xatz.getModuleByName("Notifications"));
		this.allowedMods.add(Xatz.getModuleByName("PerfectHorseJump"));
		this.allowedMods.add(Xatz.getModuleByName("Phase"));
		this.allowedMods.add(Xatz.getModuleByName("Ping"));
		this.allowedMods.add(Xatz.getModuleByName("Radar"));
		this.allowedMods.add(Xatz.getModuleByName("ReverseKnockback"));
		this.allowedMods.add(Xatz.getModuleByName("RodAura"));
		this.allowedMods.add(Xatz.getModuleByName("SafeWalk"));
		this.allowedMods.add(Xatz.getModuleByName("Scaffold"));
		this.allowedMods.add(Xatz.getModuleByName("SkinBlinker"));
		this.allowedMods.add(Xatz.getModuleByName("SkinProtect"));
		this.allowedMods.add(Xatz.getModuleByName("Spin"));
		this.allowedMods.add(Xatz.getModuleByName("Jesus"));
		this.allowedMods.add(Xatz.getModuleByName("Tags+"));
		this.allowedMods.add(Xatz.getModuleByName("Teleport"));
		this.allowedMods.add(Xatz.getModuleByName("Timer"));
		this.allowedMods.add(Xatz.getModuleByName("Tracers"));
		this.allowedMods.add(Xatz.getModuleByName("TriggerBot"));
		this.allowedMods.add(Xatz.getModuleByName("VPhase"));
		this.allowedMods.add(Xatz.getModuleByName("XRay"));
		this.allowedMods.add(Xatz.getModuleByName("WaterFart"));
	}
	
	@Override
	public String getName() {
		return "AntiWatchdog";
	}
	
}
