package me.ihaq.iClient.modules.Render;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventUpdate;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.modules.Module.Category;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

public class NoRender extends Module {

	public static int droppedformassclear = 40;
	public static int ticksforclear = 140;

	public NoRender() {
		super("NoRender", Keyboard.KEY_NONE, Category.RENDER, "");
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		Iterator var3 = this.mc.theWorld.loadedEntityList.iterator();
		while (var3.hasNext()) {
			Object o = var3.next();
			Entity entity = (Entity) o;
			if ((entity instanceof EntityItem)) {
				EntityItem item = (EntityItem) entity;
				item.renderDistanceWeight = 1.0D;
			}
		}
	}

	@EventTarget
	public void onTick(EventUpdate e) {
		if (!this.isToggled()) {
			return;
		}

		int items = 0;
		for (Object o : mc.theWorld.loadedEntityList) {
			Entity entity = (Entity) o;
			if ((entity instanceof EntityItem)) {
				items++;
				if (entity.ticksExisted >= ticksforclear) {
					mc.theWorld.removeEntity(entity);
				}
			}
		}
		if (items >= droppedformassclear) {
			for (Object o2 : mc.theWorld.loadedEntityList) {
				Entity entity2 = (Entity) o2;
				if ((entity2 instanceof EntityItem)) {
					mc.theWorld.removeEntity(entity2);
				}
			}
		}

	}

}
