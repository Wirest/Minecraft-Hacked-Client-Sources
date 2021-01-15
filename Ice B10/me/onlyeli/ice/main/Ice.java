package me.onlyeli.ice.main;

import java.util.ArrayList;

import me.onlyeli.ice.modules.*;
import me.onlyeli.ice.modules.Timer;
import me.onlyeli.ice.*;
import me.onlyeli.ice.events.BoolOption;
import me.onlyeli.ice.managers.Commands;
import me.onlyeli.ice.ui.*;
import me.onlyeli.ice.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;

public class Ice {
	
	public FriendUtils friendUtils;
	private static FontRenderer clientFont;
	private static Ice theClient = new Ice();
	private static ArrayList<Module> modules;
	private static GUI GUI;
	private String name;
	
	public static FontRenderer clientFont() {
        return Ice.clientFont();
    }
	
	public static Ice getIce() {
		return theClient;
	}
	
	public static void register() {
		modules = new ArrayList<Module>();
		registerModule(new Flight());
		registerModule(new Nuker());
		registerModule(new NoFall());
		registerModule(new Fullbright());
		registerModule(new Aura());
		registerModule(new XRay());
		registerModule(new ESP());
		registerModule(new ChestESP());
		registerModule(new AutoSneak());
		registerModule(new DoubleJump());
		registerModule(new NoClip());
		registerModule(new NoPush());
		registerModule(new Regen());
		registerModule(new AutoMine());
		registerModule(new Tracers());
		registerModule(new Step());
		registerModule(new Sprint());
		registerModule(new Aimbot());
		registerModule(new FastUse());
		registerModule(new Spider());
		registerModule(new Triggerbot());
		registerModule(new Timer());
		registerModule(new Daytime());
		registerModule(new FastEat());
		registerModule(new AntiCactus());
		registerModule(new AntiKnockback());
		registerModule(new TrollPot());
		registerModule(new KillerPot());
		registerModule(new ServerLagger());
		registerModule(new AutoArmor());
		registerModule(new AutoPot());
		registerModule(new FullbrightTest());
		registerModule(new Jesus());
		registerModule(new NoSlow());
		registerModule(new Phase());
		registerModule(new PingSpoof());
		registerModule(new SafeAura());
		registerModule(new Criticals());
		registerModule(new Commands());
			
		 GUI = new GUI();
		 
//		 InGameGUI = new InGameGUI();
	    
	}
		
	public static void registerModule(Module module) {
		modules.add(module);
		
	}
	
	public static ArrayList<Module> getModules() {
		return modules;
	}
	
	public static GUI getGUI() {
		 return GUI;
	 }
	
//	public static InGameGUI getInGameGUI() {
//		return InGameGUI;
//	}
	
	public static void onKeyPressed(int keyCode) {
		for(Module module : getModules()) {
			if(module.getkeyCode() == keyCode) {
				module.toggle();
			}
		}
	}
	 
	public static void onUpdate() {
		for(Module module : getModules()) {
			 module.onUpdate();
		}
	}
		
	public static void onRender() {
		for(Module module : getModules()) {
			 module.onRender();

	 	}
	}
}