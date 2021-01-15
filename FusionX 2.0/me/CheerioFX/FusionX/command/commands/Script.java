// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import java.util.ArrayList;

import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.command.Command;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.module.modules.Flight;
import me.CheerioFX.FusionX.module.modules.KillAura;
import me.CheerioFX.FusionX.module.modules.Speed;
import me.CheerioFX.FusionX.module.modules.Step;

public class Script extends Command {
	private ArrayList<String> mineplex;
	private ArrayList<String> cubecraft;
	private ArrayList<String> hypixel;

	public Script() {
		this.mineplex = new ArrayList<String>();
		this.cubecraft = new ArrayList<String>();
		this.hypixel = new ArrayList<String>();
		this.mineplex.add("criticals");
		this.mineplex.add("aimbot");
		this.mineplex.add("autoarmor");
		this.mineplex.add("Antikb");
		this.mineplex.add("sprint");
		this.mineplex.add("step");
		this.mineplex.add("norightclickdelay");
		this.mineplex.add("nonegitiveeffects");
		this.mineplex.add("extended reach");
		this.mineplex.add("invmove");
		this.mineplex.add("NoSlowDown");
		this.mineplex.add("NoFall");
		this.mineplex.add("fullbright");
		this.mineplex.add("esp");
		this.mineplex.add("tracer");
		this.mineplex.add("nocamerashake");
		this.mineplex.add("nametags");
		this.mineplex.add("chestesp");
		this.mineplex.add("speed");
		this.cubecraft.add("autoarmor");
		this.cubecraft.add("sprint");
		this.cubecraft.add("antikb");
		this.cubecraft.add("invmove");
		this.cubecraft.add("fullbright");
		this.cubecraft.add("esp");
		this.cubecraft.add("tracer");
		this.cubecraft.add("nocamerashake");
		this.cubecraft.add("nametags");
		this.cubecraft.add("chestesp");
		this.hypixel.add("Antikb");
		this.hypixel.add("InvMove");
		this.hypixel.add("Sprint");
		this.hypixel.add("fullbright");
		this.hypixel.add("nocamerashake");
		this.hypixel.add("nametags");
		this.hypixel.add("chestesp");
		this.hypixel.add("esp");
		this.hypixel.add("tracer");
		this.hypixel.add("autoarmor");
	}

	@Override
	public String getAlias() {
		return "script";
	}

	@Override
	public String getDescription() {
		return "(runs a script)";
	}

	@Override
	public String getSyntax() {
		return String.valueOf(FusionX.prefix) + "script <script>";
	}

	@Override
	public void onCommand(final String command, final String[] args) throws Exception {
		if (args[0].equalsIgnoreCase("mineplex")) {
			final ModuleManager moduleManager = FusionX.theClient.moduleManager;
			for (final Module m : ModuleManager.getModules()) {
				if (m.getState()) {
					m.setState(false);
					final FusionX theClient = FusionX.theClient;
					FusionX.addChatMessage(String.valueOf(m.getName()) + " was disabled!");
				}
			}
			final ModuleManager moduleManager2 = FusionX.theClient.moduleManager;
			for (final Module m : ModuleManager.getModules()) {
				for (int i = 0; i < this.mineplex.size(); ++i) {
					if (m.getName().equalsIgnoreCase(this.mineplex.get(i)) && !m.getState()) {
						m.setState(true);
						final FusionX theClient2 = FusionX.theClient;
						FusionX.addChatMessage(String.valueOf(m.getName()) + " was enabled!");
					}
				}
			}
			FusionX.theClient.setmgr.getSetting("Badlion/Cubecraft").setValBoolean(false);
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(Speed.class), "Mode")
					.setValString("Mineplex");
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(Step.class), "Height")
					.setValDouble(10.0);
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(KillAura.class), "Reach")
					.setValDouble(4.756345634563456);
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(KillAura.class), "CPS")
					.setValDouble(12.487243523453413);
			FusionX.theClient.setmgr.getSetting("Antibot").setValBoolean(true);
		} else if (args[0].equalsIgnoreCase("cubecraft")) {
			final ModuleManager moduleManager3 = FusionX.theClient.moduleManager;
			for (final Module m : ModuleManager.getModules()) {
				if (m.getState()) {
					m.setState(false);
					final FusionX theClient3 = FusionX.theClient;
					FusionX.addChatMessage(String.valueOf(m.getName()) + " was disabled!");
				}
			}
			final ModuleManager moduleManager4 = FusionX.theClient.moduleManager;
			for (final Module m : ModuleManager.getModules()) {
				for (int i = 0; i < this.cubecraft.size(); ++i) {
					if (m.getName().equalsIgnoreCase(this.cubecraft.get(i)) && !m.getState()) {
						m.setState(true);
						final FusionX theClient4 = FusionX.theClient;
						FusionX.addChatMessage(String.valueOf(m.getName()) + " was enabled!");
					}
					FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(Speed.class), "Mode")
							.setValString("Hypixel/Cubecraft");
					FusionX.theClient.setmgr.getSetting("Antibot").setValBoolean(false);
					FusionX.theClient.setmgr.getSetting("Badlion/Cubecraft").setValBoolean(true);
					FusionX.theClient.setmgr
							.getSetting(FusionX.theClient.moduleManager.getModule(Flight.class), "Flight Modes")
							.setValString("Cubecraft");
				}
			}
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(KillAura.class), "Reach")
					.setValDouble(4.256345634563456);
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(KillAura.class), "CPS")
					.setValDouble(10.487243523453413);
		} else if (args[0].equalsIgnoreCase("hypixel")) {
			final ModuleManager moduleManager5 = FusionX.theClient.moduleManager;
			for (final Module m : ModuleManager.getModules()) {
				if (m.getState()) {
					m.setState(false);
					final FusionX theClient5 = FusionX.theClient;
					FusionX.addChatMessage(String.valueOf(m.getName()) + " was disabled!");
				}
			}
			final ModuleManager moduleManager6 = FusionX.theClient.moduleManager;
			for (final Module m : ModuleManager.getModules()) {
				for (int i = 0; i < this.hypixel.size(); ++i) {
					if (m.getName().equalsIgnoreCase(this.hypixel.get(i)) && !m.getState()) {
						m.setState(true);
						final FusionX theClient6 = FusionX.theClient;
						FusionX.addChatMessage(String.valueOf(m.getName()) + " was enabled!");
					}
					FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(Speed.class), "Mode")
							.setValString("Hypixel/Cubecraft");
					FusionX.theClient.setmgr.getSetting("Antibot").setValBoolean(true);
					FusionX.theClient.setmgr.getSetting("Badlion/Cubecraft").setValBoolean(false);
					FusionX.theClient.setmgr
							.getSetting(FusionX.theClient.moduleManager.getModule(Flight.class), "Flight Modes")
							.setValString("Hypixel");
				}
			}
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(KillAura.class), "Reach")
					.setValDouble(4.256345634563456);
			FusionX.theClient.setmgr.getSetting(FusionX.theClient.moduleManager.getModule(KillAura.class), "CPS")
					.setValDouble(10.487243523453413);
		} else if (args[0].equalsIgnoreCase("allhacksoff")) {
			final ModuleManager moduleManager7 = FusionX.theClient.moduleManager;
			for (final Module m : ModuleManager.getModules()) {
				if (m.getState()) {
					m.setState(false);
					final FusionX theClient7 = FusionX.theClient;
					FusionX.addChatMessage(String.valueOf(m.getName()) + " was disabled!");
				}
			}
		} else {
			FusionX.addChatMessage("Wrong Command Usage! Correct Usage: " + FusionX.prefix + "script <scriptname>. "
					+ "scripts: mineplex, cubecraft, hypixel, allhacksoff.");
		}
	}
}
