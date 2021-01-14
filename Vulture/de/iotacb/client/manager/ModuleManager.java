package de.iotacb.client.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import de.iotacb.client.module.Module;
import de.iotacb.client.module.modules.combat.Aura;
import de.iotacb.client.module.modules.combat.AutoClicker;
import de.iotacb.client.module.modules.combat.BowAimbot;
import de.iotacb.client.module.modules.combat.InfiniteReach;
import de.iotacb.client.module.modules.combat.Reach;
import de.iotacb.client.module.modules.combat.Velocity;
import de.iotacb.client.module.modules.misc.AntiBot;
import de.iotacb.client.module.modules.misc.AutoPlay;
import de.iotacb.client.module.modules.misc.AutoRegister;
import de.iotacb.client.module.modules.misc.AutoRespawn;
import de.iotacb.client.module.modules.misc.DeathSpawn;
import de.iotacb.client.module.modules.misc.Disabler;
import de.iotacb.client.module.modules.misc.Friends;
import de.iotacb.client.module.modules.misc.IRC;
import de.iotacb.client.module.modules.misc.KillSults;
import de.iotacb.client.module.modules.misc.NameProtect;
import de.iotacb.client.module.modules.misc.Performance;
import de.iotacb.client.module.modules.misc.PotionSaver;
import de.iotacb.client.module.modules.misc.Teams;
import de.iotacb.client.module.modules.movement.Blink;
import de.iotacb.client.module.modules.movement.Fly;
import de.iotacb.client.module.modules.movement.InventoryWalk;
import de.iotacb.client.module.modules.movement.NoSlow;
import de.iotacb.client.module.modules.movement.Safewalk;
import de.iotacb.client.module.modules.movement.Speed;
import de.iotacb.client.module.modules.movement.Sprint;
import de.iotacb.client.module.modules.movement.Step;
import de.iotacb.client.module.modules.movement.Strafe;
import de.iotacb.client.module.modules.movement.TerrainSpeed;
import de.iotacb.client.module.modules.movement.WaterSpeed;
import de.iotacb.client.module.modules.player.AutoArmor;
import de.iotacb.client.module.modules.player.AutoTool;
import de.iotacb.client.module.modules.player.ChestStealer;
import de.iotacb.client.module.modules.player.FastUse;
import de.iotacb.client.module.modules.player.FreeCam;
import de.iotacb.client.module.modules.player.InventoryCleaner;
import de.iotacb.client.module.modules.player.NoFall;
import de.iotacb.client.module.modules.player.Regenerate;
import de.iotacb.client.module.modules.player.Teleport;
import de.iotacb.client.module.modules.render.BlockOverlay;
import de.iotacb.client.module.modules.render.CamClip;
import de.iotacb.client.module.modules.render.Chams;
import de.iotacb.client.module.modules.render.ChestESP;
import de.iotacb.client.module.modules.render.ClickGui;
import de.iotacb.client.module.modules.render.DamageHits;
import de.iotacb.client.module.modules.render.EnemyFinder;
import de.iotacb.client.module.modules.render.EntityESP;
import de.iotacb.client.module.modules.render.FOV;
import de.iotacb.client.module.modules.render.FullBright;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.module.modules.render.ItemPhysics;
import de.iotacb.client.module.modules.render.Nametags;
import de.iotacb.client.module.modules.render.NoBob;
import de.iotacb.client.module.modules.render.NoHurtCam;
import de.iotacb.client.module.modules.render.NoInvisibles;
import de.iotacb.client.module.modules.render.NoPitchLimit;
import de.iotacb.client.module.modules.render.NoScoreboard;
import de.iotacb.client.module.modules.render.RearView;
import de.iotacb.client.module.modules.render.RenderChanger;
import de.iotacb.client.module.modules.render.SkinChanger;
import de.iotacb.client.module.modules.render.Swing;
import de.iotacb.client.module.modules.render.Tracers;
import de.iotacb.client.module.modules.render.Trajectories;
import de.iotacb.client.module.modules.world.ChestAura;
import de.iotacb.client.module.modules.world.Clip;
import de.iotacb.client.module.modules.world.CubeClip;
import de.iotacb.client.module.modules.world.Fucker;
import de.iotacb.client.module.modules.world.GhostHand;
import de.iotacb.client.module.modules.world.Scaffold;
import de.iotacb.client.module.modules.world.Tower;
import de.iotacb.client.module.modules.world.WorldTime;

public class ModuleManager {
	
	private final List<Module> modules = new ArrayList<Module>();
	
	public ModuleManager() {
		addModules(Velocity.class,
				   InfiniteReach.class,
				   Blink.class,
				   InventoryWalk.class,
				   Sprint.class,
				   AutoArmor.class,
				   ChestStealer.class,
				   InventoryCleaner.class,
				   Chams.class,
				   ClickGui.class,
				   EntityESP.class,
				   ChestESP.class,
				   HUD.class,
				   Scaffold.class,
				   Speed.class,
				   NoFall.class,
				   NoSlow.class,
				   Fly.class,
				   Step.class,
				   Strafe.class,
				   NoHurtCam.class,
				   NoBob.class,
				   Swing.class,
				   FOV.class,
				   CubeClip.class,
				   Fucker.class,
				   Aura.class,
				   Performance.class,
				   TerrainSpeed.class,
				   Friends.class,
				   NameProtect.class,
				   EnemyFinder.class,
				   Nametags.class,
				   Teams.class,
				   Clip.class,
				   RenderChanger.class,
				   Regenerate.class,
				   Tower.class,
				   BlockOverlay.class,
				   ChestAura.class,
				   FastUse.class,
				   BowAimbot.class,
				   NoScoreboard.class,
				   Teleport.class,
				   Trajectories.class,
				   FullBright.class,
				   WorldTime.class,
				   Tracers.class,
				   PotionSaver.class,
				   RearView.class,
				   AutoTool.class,
				   DamageHits.class,
				   AutoClicker.class,
				   Reach.class,
				   AutoRegister.class,
				   AutoRespawn.class,
				   FreeCam.class,
				   GhostHand.class,
				   CamClip.class,
				   NoPitchLimit.class,
				   ItemPhysics.class,
				   NoInvisibles.class,
				   Safewalk.class,
				   AutoPlay.class,
				   AntiBot.class,
				   SkinChanger.class,
				   WaterSpeed.class,
				   KillSults.class,
				   Disabler.class,
				   DeathSpawn.class,
				   IRC.class
				   );
		
	}
	
	private void addModule(final Module module) {
		modules.add(module);
	}
	
	private void addModules(final Class <?extends Module>...classes) {
		for (final Class <?extends Module> module : classes) {
			try {
				getModules().add(module.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Module> getModules() {
		return modules;
	}
	
	public void toggleModules(final int key) {
		final List<Module> multiBindedModules = getModulesByBind(key);
		if (multiBindedModules.size() > 0) {
			for (final Module module : getModules()) {
				if (module.isMultiBinded()) {
					if (module.getKey() == key && Keyboard.isKeyDown(module.getMultiBindKey())) {
						module.toggle();
						return;
					}
				}
			}
		}
		getModules().forEach(module -> {
			if (module.isMultiBinded()) return;
			if (module.getKey() == key) {
				module.toggle();
			}
		});
	}
	
	public Module getModuleByClass(final Class<? extends Module> clazz) {
		return getModules().stream().filter(module -> module.getClass() == clazz).findFirst().orElse(null);
	}
	
	public Module getModuleByName(final String name) {
		return getModules().stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
	
	public List<Module> getModulesByCategory(final String categoryName) {
		return getModules().stream().filter(module -> module.getCategory().name().equalsIgnoreCase(categoryName)).collect(Collectors.<Module>toList());
	}
	
	public List<Module> getModulesByBind(final int key) {
		return getModules().stream().filter(module -> module.getKey() == key && module.getMultiBindKey() != -1).collect(Collectors.<Module>toList());
	}

}
