/*

 */
package Blizzard.Mod;

import java.util.ArrayList;

import Blizzard.Mod.mods.fight.AntiBot;
import Blizzard.Mod.mods.fight.KillAura;
import Blizzard.Mod.mods.fight.Velocity;
import Blizzard.Mod.mods.misc.AutoArmor;
import Blizzard.Mod.mods.misc.ChestStealer;
import Blizzard.Mod.mods.misc.InvMove;
import Blizzard.Mod.mods.misc.Notify;
import Blizzard.Mod.mods.movement.Fly;
import Blizzard.Mod.mods.movement.LongJump;
import Blizzard.Mod.mods.movement.NoFall;
import Blizzard.Mod.mods.movement.NoSlowDown;
import Blizzard.Mod.mods.movement.Phase;
import Blizzard.Mod.mods.movement.Speed;
import Blizzard.Mod.mods.movement.Sprint;
import Blizzard.Mod.mods.movement.Step;
import Blizzard.Mod.mods.render.ESP;
import Blizzard.Mod.mods.render.FullBright;
import Blizzard.Mod.mods.render.HUD;

public class ModManager {
	public static ArrayList<Mod> activeMods = new ArrayList();

	public ModManager() {
		activeMods.add(new Fly());
		activeMods.add(new Velocity());
		activeMods.add(new FullBright());
		activeMods.add(new KillAura());
		activeMods.add(new LongJump());
		activeMods.add(new NoFall());
		activeMods.add(new NoSlowDown());
		activeMods.add(new Speed());
		activeMods.add(new Sprint());
		activeMods.add(new ESP());
		activeMods.add(new Notify());
		activeMods.add(new HUD());
		activeMods.add(new ChestStealer());
		activeMods.add(new AutoArmor());
		activeMods.add(new InvMove());
		activeMods.add(new Phase());
		activeMods.add(new Step());
		activeMods.add(new AntiBot());
	}

	public Mod getMod(Class<? extends Mod> clazz) {
		for (Mod mod : ModManager.getMods()) {
			if (mod.getClass() != clazz)
				continue;
			return mod;
		}
		return null;
	}

	public static void arrayListSorter(String order) {
	}

	public static ArrayList<Mod> getMods() {
		return activeMods;
	}

	public static Mod getModbyName(String name) {
		for (Mod mod : ModManager.getMods()) {
			if (!mod.getName().equalsIgnoreCase(name))
				continue;
			return mod;
		}
		return null;
	}

	public ArrayList<Mod> enabledMods() {
		ArrayList<Mod> emods = new ArrayList<Mod>();
		for (Mod m2 : activeMods) {
			if (m2.isToggled()) {
				emods.add(m2);
				continue;
			}
			if (m2.isToggled() || !emods.contains(m2))
				continue;
			emods.remove(m2);
		}
		return emods;
	}
}
