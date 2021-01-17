package me.ihaq.iClient.modules;

import java.util.ArrayList;

import me.ihaq.iClient.event.EventManager;
import me.ihaq.iClient.modules.Combat.AntiBot;
import me.ihaq.iClient.modules.Combat.Aura;
import me.ihaq.iClient.modules.Combat.AutoArmor;
import me.ihaq.iClient.modules.Combat.Criticals;
import me.ihaq.iClient.modules.Combat.Velocity;
import me.ihaq.iClient.modules.Movement.AirJump;
import me.ihaq.iClient.modules.Movement.Flight;
import me.ihaq.iClient.modules.Movement.InventoryMove;
import me.ihaq.iClient.modules.Movement.Jesus;
import me.ihaq.iClient.modules.Movement.LongJump;
import me.ihaq.iClient.modules.Movement.NoSlowDown;
import me.ihaq.iClient.modules.Movement.Speed;
import me.ihaq.iClient.modules.Movement.Spider;
import me.ihaq.iClient.modules.Movement.Sprint;
import me.ihaq.iClient.modules.Movement.Step;
import me.ihaq.iClient.modules.Player.BedBreaker;
import me.ihaq.iClient.modules.Player.ChestSteal;
import me.ihaq.iClient.modules.Player.NoFall;
import me.ihaq.iClient.modules.Player.Respawn;
import me.ihaq.iClient.modules.Player.Scaffold;
import me.ihaq.iClient.modules.Render.ClickGui;
import me.ihaq.iClient.modules.Render.ESP;
import me.ihaq.iClient.modules.Render.Fullbright;
import me.ihaq.iClient.modules.Render.HUD;
import me.ihaq.iClient.modules.Render.NameTags;
import me.ihaq.iClient.modules.Render.NoRender;
import me.ihaq.iClient.modules.Render.Tracers;

public class ModuleManager {

	public static ArrayList<Module> modules = new ArrayList<Module>();

	public ModuleManager() {
	}

	public void loadMods() {
		registerModule(new ESP());
		registerModule(new NoFall());
		registerModule(new Flight());
		registerModule(new Sprint());
		registerModule(new Fullbright());
		registerModule(new Step());
		registerModule(new Jesus());
		registerModule(new Respawn());
		registerModule(new Speed());
		registerModule(new Spider());
		registerModule(new Aura());
		registerModule(new Criticals());
		registerModule(new LongJump());
		registerModule(new AntiBot());
		registerModule(new AutoArmor());
		registerModule(new InventoryMove());
		registerModule(new AirJump());
		registerModule(new NoSlowDown());
		registerModule(new ClickGui());
		registerModule(new Velocity());
		registerModule(new HUD());
		registerModule(new Tracers());
		registerModule(new ChestSteal());
		registerModule(new NoRender());
		registerModule(new BedBreaker());
		registerModule(new Scaffold());
		registerModule(new NameTags());
	}

	public static void registerModule(Module module) {
		modules.add(module);
		EventManager.register(module);
	}

	public static ArrayList<Module> getModules() {
		return modules;
	}

}
