
package me.memewaredevs.client.module;

import me.memewaredevs.client.module.Module.Category;
import me.memewaredevs.client.module.combat.*;
import me.memewaredevs.client.module.exploit.AntiStuck;
import me.memewaredevs.client.module.exploit.Disabler;
import me.memewaredevs.client.module.misc.*;
import me.memewaredevs.client.module.movement.*;
import me.memewaredevs.client.module.player.*;
import me.memewaredevs.client.module.render.*;
import me.memewaredevs.client.util.misc.Timer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
	private ArrayList<Module> modules = new ArrayList();

	public ModuleManager() {
		modules.add(new Aura());
		modules.add(new Stealer());
		modules.add(new NoHurtCam());
		modules.add(new Regen());
		modules.add(new AntiBlind());
		modules.add(new AutoPotion());
		modules.add(new AutoArmor());
		modules.add(new FastBow());
		modules.add(new Criticals());
		modules.add(new AntiBot(0, Module.Category.MISC));
		modules.add(new AutoSoup());
		modules.add(new Velocity("Velocity", 0, Module.Category.COMBAT));
		modules.add(new Chams("Chams", 0, Category.RENDER));
		modules.add(new TargetStrafe("Target Strafe", 0, Module.Category.COMBAT));
		modules.add(new FastUse("Fast Use", 0, Module.Category.PLAYER));
		modules.add(new Fly());
		modules.add(new Speed());
		modules.add(new LongJump());
		modules.add(new NoSlow());
		modules.add(new ESP());
		modules.add(new Spammer());
		modules.add(new Step());
		modules.add(new WaterSpeed());
		modules.add(new Phase("Phase", 0, Module.Category.EXPLOIT));
		modules.add(new AntiStuck("Anti Stuck", 0, Module.Category.EXPLOIT));
		modules.add(new HighJump());
		modules.add(new Sprint());
		modules.add(new Tags());
		modules.add(new ClickGui());
		modules.add(new Disabler("Disabler", 0, Module.Category.EXPLOIT));
		modules.add(new Breaker());
		modules.add(new InvMove());
		modules.add(new TPAura());
		modules.add(new Derp("Derp", 0, Module.Category.PLAYER));
		modules.add(new Scaffold());
		modules.add(new NoFall());
		modules.add(new BlockESP());
		modules.add(new Ambiance());
		modules.add(new Fullbright());
		modules.add(new DeathInsult());
		modules.add(new Animations());
		modules.add(new Commands());
		modules.add(new AntiFall());
		modules.add(new FastPlace());
		modules.add(new FastBreak());
		modules.add(new HackerDetect());
		//modules.add(new HitESP());
		modules.add(new CustomSpeed());
		this.getModule("Hacker Detect").toggle();
		this.getModule("Commands").toggle();
		this.getModule("Sprint").toggle();

		try {
			KeyLoader.load(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Timer threadTimer = new Timer();
		new Thread(() -> {
			while (true) {
				if (threadTimer.delay(2500)) {
					KeyLoader.save(modules);
					threadTimer.reset();
				}
			}
		}).start();
	}

	public List<Module> getModules() {
		return modules;
	}

	public Module get(final Class moduleClass) {
		for (final Module m : modules) {
			if (m.getClass() != moduleClass) {
				continue;
			}
			return m;
		}
		return null;
	}

	public Module getModule(final String name) {
		for (final Module m : modules) {
			if (!m.getName().equalsIgnoreCase(name)) {
				continue;
			}
			return m;
		}
		return null;
	}

	public List<Module> getModules(Category category) {
		List<Module> l = new ArrayList();
		for (Module m : modules) {
			if (m.getCategory() == category) {
				l.add(m);
			}
		}
		return l;
	}
}
