package me.robbanrobbin.jigsaw.client.bypasses;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;

public class AntiGwen extends Bypass {
	
	@Override
	public void onEnable() {
		ClientSettings.KillauraAPS = 10;
		ClientSettings.KillauraRange = 4;
		Jigsaw.getModuleByName("Invisible").setToggled(false, true);
		Jigsaw.getModuleByName("Flight").setMode("Glide");
		Jigsaw.getModuleByName("BunnyHop").setMode("SlowHop");
		ClientSettings.TpAuraAPS = 1;
		ClientSettings.TpAuramaxTargets = 1;
		ClientSettings.TpAurarange = 40;
		super.onEnable();
	}

	@Override
	public void setBypassableMods() {
		super.setBypassableMods();
		this.allowedMods.add(Jigsaw.getModuleByName("AutoSoup"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoSprint"));
		this.allowedMods.add(Jigsaw.getModuleByName("Animations"));
		this.allowedMods.add(Jigsaw.getModuleByName("Aimbot"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoEat"));
		this.allowedMods.add(Jigsaw.getModuleByName("AntiHunger"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoRespawn"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoJump"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoWalk"));
		this.allowedMods.add(Jigsaw.getModuleByName("AutoSneak"));
		this.allowedMods.add(Jigsaw.getModuleByName("Bleach"));
		this.allowedMods.add(Jigsaw.getModuleByName("Blink"));
		this.allowedMods.add(Jigsaw.getModuleByName("BunnyHop"));
		this.allowedMods.add(Jigsaw.getModuleByName("Breadcrumbs"));
		this.allowedMods.add(Jigsaw.getModuleByName("BowAimbot"));
		this.allowedMods.add(Jigsaw.getModuleByName("ClickGUI"));
		this.allowedMods.add(Jigsaw.getModuleByName("Criticals"));
		this.allowedMods.add(Jigsaw.getModuleByName("Coords"));
		this.allowedMods.add(Jigsaw.getModuleByName("CopsNCrims"));
		this.allowedMods.add(Jigsaw.getModuleByName("ESP"));
		this.allowedMods.add(Jigsaw.getModuleByName("ExtendedReach"));
		this.allowedMods.add(Jigsaw.getModuleByName("FastEat"));
		this.allowedMods.add(Jigsaw.getModuleByName("Flight"));
		this.allowedMods.add(Jigsaw.getModuleByName("Fullbright"));
		this.allowedMods.add(Jigsaw.getModuleByName("FPS"));
		this.allowedMods.add(Jigsaw.getModuleByName("Freecam"));
		this.allowedMods.add(Jigsaw.getModuleByName("FakeHackers"));
		this.allowedMods.add(Jigsaw.getModuleByName("GhostAura"));
		this.allowedMods.add(Jigsaw.getModuleByName("HackerDetect"));
		this.allowedMods.add(Jigsaw.getModuleByName("HackerDetectGUI"));
		this.allowedMods.add(Jigsaw.getModuleByName("Headless"));
		this.allowedMods.add(Jigsaw.getModuleByName("HighJump"));
		this.allowedMods.add(Jigsaw.getModuleByName("Inventory+"));
		this.allowedMods.add(Jigsaw.getModuleByName("InvMove"));
		this.allowedMods.add(Jigsaw.getModuleByName("KeepSprint"));
		this.allowedMods.add(Jigsaw.getModuleByName("KillAura"));
		this.allowedMods.add(Jigsaw.getModuleByName("Knockback"));
		this.allowedMods.add(Jigsaw.getModuleByName("Friends"));
		this.allowedMods.add(Jigsaw.getModuleByName("NonPlayers"));
		this.allowedMods.add(Jigsaw.getModuleByName("Players"));
		this.allowedMods.add(Jigsaw.getModuleByName("Team"));
		this.allowedMods.add(Jigsaw.getModuleByName("GWEN Bypass"));
		this.allowedMods.add(Jigsaw.getModuleByName("GhostMode"));
		this.allowedMods.add(Jigsaw.getModuleByName("LongJump"));
		this.allowedMods.add(Jigsaw.getModuleByName("MegaKnockback"));
		this.allowedMods.add(Jigsaw.getModuleByName("MobArena"));
		this.allowedMods.add(Jigsaw.getModuleByName("MiddleClickFriends"));
		this.allowedMods.add(Jigsaw.getModuleByName("NameProtect"));
		this.allowedMods.add(Jigsaw.getModuleByName("NameTags"));
		this.allowedMods.add(Jigsaw.getModuleByName("NoHurtcam"));
		this.allowedMods.add(Jigsaw.getModuleByName("NoSlowdown"));
		this.allowedMods.add(Jigsaw.getModuleByName("Notifications"));
		this.allowedMods.add(Jigsaw.getModuleByName("PerfectHorseJump"));
		this.allowedMods.add(Jigsaw.getModuleByName("Phase"));
		this.allowedMods.add(Jigsaw.getModuleByName("Ping"));
		this.allowedMods.add(Jigsaw.getModuleByName("Radar"));
		this.allowedMods.add(Jigsaw.getModuleByName("Regen"));
		this.allowedMods.add(Jigsaw.getModuleByName("ReverseKnockback"));
		this.allowedMods.add(Jigsaw.getModuleByName("RodAura"));
		this.allowedMods.add(Jigsaw.getModuleByName("SafeWalk"));
		this.allowedMods.add(Jigsaw.getModuleByName("Speed"));
		this.allowedMods.add(Jigsaw.getModuleByName("SkinBlinker"));
		this.allowedMods.add(Jigsaw.getModuleByName("SkinProtect"));
		this.allowedMods.add(Jigsaw.getModuleByName("SneakyAssasins"));
		this.allowedMods.add(Jigsaw.getModuleByName("Spin"));
		this.allowedMods.add(Jigsaw.getModuleByName("SolidLiquids"));
		this.allowedMods.add(Jigsaw.getModuleByName("Tags+"));
		this.allowedMods.add(Jigsaw.getModuleByName("Teleport"));
		this.allowedMods.add(Jigsaw.getModuleByName("Timer"));
		this.allowedMods.add(Jigsaw.getModuleByName("Tracers"));
		this.allowedMods.add(Jigsaw.getModuleByName("TpAura"));
		this.allowedMods.add(Jigsaw.getModuleByName("TriggerBot"));
		this.allowedMods.add(Jigsaw.getModuleByName("VPhase"));
		this.allowedMods.add(Jigsaw.getModuleByName("XRay"));
		this.allowedMods.add(Jigsaw.getModuleByName("WaterFart"));
	}
	
	@Override
	public String getName() {
		return "AntiGwen";
	}
	
}
