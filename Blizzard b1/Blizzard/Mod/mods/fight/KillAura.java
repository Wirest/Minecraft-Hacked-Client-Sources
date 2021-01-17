
package Blizzard.Mod.mods.fight;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import Blizzard.Category.Category;
import Blizzard.Event.EventTarget;
import Blizzard.Event.events.EventUpdate;
import Blizzard.Mod.Mod;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class KillAura extends Mod {
	private int ticks = 0;
	public static float yaw;
	public static float pitch;
	public static Entity entity;
	public static double cps;
	public static double reach;

	static {
		cps = 18.0;
		reach = 4.0;
	}

	public KillAura() {
		super("KillAura", "KillAura", Keyboard.KEY_R, Category.COMBAT);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {

		if (!this.isToggled())
			return;

		for (Iterator<Object> entities = mc.theWorld.loadedEntityList.iterator(); entities.hasNext();) {
			Object theObject = entities.next();
			if (theObject instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) theObject;

				if (entity instanceof EntityPlayerSP)
					continue;

				if (mc.thePlayer.getDistanceToEntity(entity) <= 4.2173613F) {
					if (entity.isEntityAlive()) {
						mc.playerController.attackEntity(mc.thePlayer, entity);
						mc.thePlayer.swingItem();
						continue;
					}
				}
			}
		}
	}
}
