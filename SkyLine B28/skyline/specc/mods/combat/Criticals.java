package skyline.specc.mods.combat;

import java.awt.Color;

import skyline.specc.mods.combat.criticals.MiJump;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.mods.combat.criticals.Ghostly;
import skyline.specc.mods.combat.criticals.Hypixel;
import skyline.specc.mods.combat.criticals.Vanilla;

import java.awt.Color;

public class Criticals extends Module {

	public Criticals() {
		super(new ModData("Criticals", 22, new Color(120, 10, 193)), ModType.COMBAT);
		addMode(new Hypixel(this, "LookPuppy"));
		addMode(new MiJump(this, "MiJump"));
		addMode(new Ghostly(this, "Ghostly"));
		addMode(new Vanilla(this, "Vanilla"));
	}

}