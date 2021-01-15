package me.robbanrobbin.jigsaw.client.bypasses;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;

public class AntiGCheat extends Bypass {
	
	@Override
	public void onEnable() {
		ClientSettings.KillauraAPS = 8;
		ClientSettings.KillauraRange = 3.5;
		Jigsaw.getModuleByName("Invisible").setToggled(false, true);
		Jigsaw.getModuleByName("BunnyHop").setMode("SlowHop");
		ClientSettings.chestStealDelay = true;
		ClientSettings.ExtendedReachrange = 4.2;
		ClientSettings.smoothAim = true;
		AuraUtils.setSmoothAimSpeed(4.5);
		super.onEnable();
	}

	@Override
	public void setBypassableMods() {
		super.setBypassableMods();
		this.allowedMods.add(Jigsaw.getModuleByName("AutoSprint"));
		this.allowedMods.add(Jigsaw.getModuleByName("Animations"));
		this.allowedMods.add(Jigsaw.getModuleByName("Aimbot"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoEat"));
		this.allowedMods.add(Jigsaw.getModuleByName("AntiHunger"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoRespawn"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoJump"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoWalk"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoSneak"));
		this.allowedMods.add(Jigsaw.getModuleByName("Blink"));
		this.allowedMods.add(Jigsaw.getModuleByName("BunnyHop"));
		this.allowedMods.add(Jigsaw.getModuleByName("Breadcrumbs"));
		this.allowedMods.add(Jigsaw.getModuleByName("BowAimbot"));
		this.allowedMods.add(Jigsaw.getModuleByName("ClickGUI"));
		this.allowedMods.add(Jigsaw.getModuleByName("Criticals"));
		this.allowedMods.add(Jigsaw.getModuleByName("Coords"));
		this.allowedMods.add(Jigsaw.getModuleByName("CopsNCrims"));
		this.allowedMods.add(Jigsaw.getModuleByName("ChestStealer"));
		this.allowedMods.add(Jigsaw.getModuleByName("ESP"));
		this.allowedMods.add(Jigsaw.getModuleByName("Fullbright"));
		this.allowedMods.add(Jigsaw.getModuleByName("FPS"));
		this.allowedMods.add(Jigsaw.getModuleByName("Freecam"));
		this.allowedMods.add(Jigsaw.getModuleByName("FakeHackers"));
		this.allowedMods.add(Jigsaw.getModuleByName("GhostAura"));
		this.allowedMods.add(Jigsaw.getModuleByName("HackerDetect"));
		this.allowedMods.add(Jigsaw.getModuleByName("HackerDetectGUI"));
		this.allowedMods.add(Jigsaw.getModuleByName("HighJump"));
		this.allowedMods.add(Jigsaw.getModuleByName("Inventory+"));
		this.allowedMods.add(Jigsaw.getModuleByName("InvMove"));
		this.allowedMods.add(Jigsaw.getModuleByName("Friends"));
		this.allowedMods.add(Jigsaw.getModuleByName("NonPlayers"));
		this.allowedMods.add(Jigsaw.getModuleByName("Players"));
		this.allowedMods.add(Jigsaw.getModuleByName("Team"));
		this.allowedMods.add(Jigsaw.getModuleByName("GhostMode"));
		this.allowedMods.add(Jigsaw.getModuleByName("MiddleClickFriends"));
		this.allowedMods.add(Jigsaw.getModuleByName("NameProtect"));
		this.allowedMods.add(Jigsaw.getModuleByName("NameTags"));
		this.allowedMods.add(Jigsaw.getModuleByName("NoHurtcam"));
		this.allowedMods.add(Jigsaw.getModuleByName("Notifications"));
		this.allowedMods.add(Jigsaw.getModuleByName("PerfectHorseJump"));
		this.allowedMods.add(Jigsaw.getModuleByName("Ping"));
		this.allowedMods.add(Jigsaw.getModuleByName("Radar"));
		this.allowedMods.add(Jigsaw.getModuleByName("SafeWalk"));
		this.allowedMods.add(Jigsaw.getModuleByName("SkinBlinker"));
		this.allowedMods.add(Jigsaw.getModuleByName("SkinProtect"));
		this.allowedMods.add(Jigsaw.getModuleByName("Tags+"));
		this.allowedMods.add(Jigsaw.getModuleByName("Tracers"));
		this.allowedMods.add(Jigsaw.getModuleByName("XRay"));
	}
	
	@Override
	public String getName() {
		return "AntiGCheat (Alpha)";
	}
	
}
