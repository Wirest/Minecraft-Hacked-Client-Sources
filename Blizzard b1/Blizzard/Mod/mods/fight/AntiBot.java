package Blizzard.Mod.mods.fight;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import net.minecraft.entity.Entity;

public class AntiBot extends Mod {
	public AntiBot() {
		super("AntiBot", "AntiBot", Keyboard.KEY_Q, Category.COMBAT);

	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		for (Object entity : mc.theWorld.loadedEntityList) {
			if (((Entity) entity).isInvisible() && entity != mc.thePlayer) {
				mc.theWorld.removeEntity((Entity) entity);
			}
		}
	}
}