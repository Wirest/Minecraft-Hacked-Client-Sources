package skyline.specc.managers;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import com.google.common.reflect.ClassPath;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.managers.File.Manager;
import skyline.specc.mods.combat.*;
import skyline.specc.mods.move.Fly;
import skyline.specc.mods.move.Jesus;
import skyline.specc.mods.move.LongJump;
import skyline.specc.mods.move.NoFall;
import skyline.specc.mods.move.NoSlowMod;
import skyline.specc.mods.move.Scaffold;
import skyline.specc.mods.move.Speed;
import skyline.specc.mods.move.Sprint;
import skyline.specc.mods.other.AntiFreeze;
import skyline.specc.mods.other.FreecamMod;
import skyline.specc.mods.other.InvMove;
import skyline.specc.mods.other.NoRotateMod;
import skyline.specc.mods.player.ChestStealer;
import skyline.specc.mods.player.MCF;
import skyline.specc.mods.player.NameProtect;
import skyline.specc.mods.player.NoPing;
import skyline.specc.mods.player.Regen;
import skyline.specc.mods.world.Phase;
import skyline.specc.render.modules.*;

public class ModuleManager extends Manager<Module> {

	public ModType c;

	public ModuleManager() {

		// COMBAT
		addContent(new AntiBot(new ModData("AntiBot", 0, new Color(0,0,0)), ModType.COMBAT));
		addContent(new BowAim(new ModData("BowAim", 0, new Color(0,0,0)), ModType.COMBAT));
		addContent(new AutoArmorMod());
		addContent(new AimAssist());
		addContent(new Autoclicker());
		addContent(new AutoPotion());
		addContent(new FastBow());
		addContent(new KillAuraMod());
		addContent(new Criticals());
		addContent(new Velocity());
		addContent(new NoSlowMod());
		addContent(new Regen());
		addContent(new LongJump());
		addContent(new Speed());
		addContent(new NoFall());
		addContent(new Sprint());
		addContent(new Fly());
		addContent(new Jesus());
		addContent(new MCF());
		addContent(new NoPing());
		addContent(new NoRotateMod());
		addContent(new ChestStealer());
		addContent(new AntiFreeze());
		addContent(new Scaffold());
		addContent(new FreecamMod());
		addContent(new InvMove());
		addContent(new FullbrightMod());
		addContent(new NametagsMod());
		addContent(new Tracers());
		addContent(new Chams());
		addContent(new XrayMod());
		addContent(new NameProtect());
		addContent(new HUD(new ModData("HUD", 0, new Color(0, 0, 0)), ModType.RENDER));
		addContent(new ESP(new ModData("ESP", 0, new Color(0, 0, 0)), ModType.RENDER));
		addContent(new Phase());

		
	}

	public Module getModuleFromName(String name) {
		for (Module m : getContents()) {
			if (m.getName().equalsIgnoreCase(name))
				return m;
		}

		return null;
	}

	public Module getcat(ModType c) {
		for (Module m : getContents()) {
			if (m.getModCategory2(c))
				return m;
		}

		return null;
	}

	public Module getModuleFromClass(Class clas) {
		for (Module m : getContents()) {
			if (m.getClass() == clas) {
				return m;
			}
		}
		return null;
	}

	private ArrayList<Class<?>> getClasses(final String packageName) {
		final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		try {
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			for (final ClassPath.ClassInfo info : ClassPath.from(loader).getAllClasses()) {
				if (info.getName().startsWith(packageName)) {
					final Class<?> clazz = info.load();
					classes.add(clazz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	public boolean hasMod(Module m) {

		for (Module module : getContents()) {
			if (module == m)
				return true;
		}

		return false;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PACKAGE)
	public @interface RegisterModulePackage {

	}

	public boolean getModCategory2(ModType cat) {
		return c == cat;
	}

}
