package me.xatzdevelopments.xatz.client.bypasses;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.modules.target.AuraUtils;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.module.Module;

public class AntiGCheat extends Bypass {
	
	@Override
	public void onEnable() {
		ClientSettings.KillauraAPS = 8;
		ClientSettings.KillauraRange = 3.5;
		Xatz.getModuleByName("Invisible").setToggled(false, true);
		Xatz.getModuleByName("BunnyHop").setMode("SlowHop");
		ClientSettings.chestStealDelay = true;
		ClientSettings.ExtendedReachrange = 4.2;
		ClientSettings.smoothAim = true;
		AuraUtils.setSmoothAimSpeed(4.5);
		super.onEnable();
	}

	@Override
	public void setBypassableMods() {
		super.setBypassableMods();
		this.allowedMods.add(Xatz.getModuleByName("AutoSprint"));
		this.allowedMods.add(Xatz.getModuleByName("Animations"));
		this.allowedMods.add(Xatz.getModuleByName("Aimbot"));
		this.allowedMods.add(Xatz.getModuleByName("AutoEat"));
		this.allowedMods.add(Xatz.getModuleByName("AntiHunger"));
		this.allowedMods.add(Xatz.getModuleByName("AutoRespawn"));
		this.allowedMods.add(Xatz.getModuleByName("AutoJump"));
		this.allowedMods.add(Xatz.getModuleByName("AutoWalk"));
		this.allowedMods.add(Xatz.getModuleByName("AutoSneak"));
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
		this.allowedMods.add(Xatz.getModuleByName("Fullbright"));
		this.allowedMods.add(Xatz.getModuleByName("FPS"));
		this.allowedMods.add(Xatz.getModuleByName("Freecam"));
		this.allowedMods.add(Xatz.getModuleByName("FakeHackers"));
		this.allowedMods.add(Xatz.getModuleByName("GhostAura"));
		this.allowedMods.add(Xatz.getModuleByName("HackerDetect"));
		this.allowedMods.add(Xatz.getModuleByName("HackerDetectGUI"));
		this.allowedMods.add(Xatz.getModuleByName("HighJump"));
		this.allowedMods.add(Xatz.getModuleByName("Inventory+"));
		this.allowedMods.add(Xatz.getModuleByName("InvMove"));
		this.allowedMods.add(Xatz.getModuleByName("Friends"));
		this.allowedMods.add(Xatz.getModuleByName("NonPlayers"));
		this.allowedMods.add(Xatz.getModuleByName("Players"));
		this.allowedMods.add(Xatz.getModuleByName("Team"));
		this.allowedMods.add(Xatz.getModuleByName("GhostMode"));
		this.allowedMods.add(Xatz.getModuleByName("MiddleClickFriends"));
		this.allowedMods.add(Xatz.getModuleByName("NameProtect"));
		this.allowedMods.add(Xatz.getModuleByName("NameTags"));
		this.allowedMods.add(Xatz.getModuleByName("NoHurtcam"));
		this.allowedMods.add(Xatz.getModuleByName("Notifications"));
		this.allowedMods.add(Xatz.getModuleByName("PerfectHorseJump"));
		this.allowedMods.add(Xatz.getModuleByName("Ping"));
		this.allowedMods.add(Xatz.getModuleByName("Radar"));
		this.allowedMods.add(Xatz.getModuleByName("SafeWalk"));
		this.allowedMods.add(Xatz.getModuleByName("SkinBlinker"));
		this.allowedMods.add(Xatz.getModuleByName("SkinProtect"));
		this.allowedMods.add(Xatz.getModuleByName("Tags+"));
		this.allowedMods.add(Xatz.getModuleByName("Tracers"));
		this.allowedMods.add(Xatz.getModuleByName("XRay"));
	}
	
	@Override
	public String getName() {
		return "AntiGCheat (Alpha)";
	}
	
}
