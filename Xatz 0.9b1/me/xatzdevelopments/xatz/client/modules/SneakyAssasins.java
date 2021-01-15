package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.entity.passive.EntityVillager;

public class SneakyAssasins extends Module {

	ArrayList<EntityVillager> render = new ArrayList<EntityVillager>();

	public SneakyAssasins() {
		super("SneakyAssasins", Keyboard.KEY_NONE, Category.MINIGAMES,
				"Allows you to see all players in the 'Sneaky Assasins'");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		render.clear();
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		render.clear();
		for (Object o : mc.theWorld.loadedEntityList) {
			if (!(o instanceof EntityVillager)) {
				continue;
			}
			EntityVillager villager = (EntityVillager) o;

			if (villager.rotationPitch != 0 || villager.isSprinting() || villager.moveStrafing > 0
					|| villager.moveStrafing < 0) {
				render.add(villager);
			}

		}

		super.onUpdate();
	}

	@Override
	public void onRender() {

		if (render == null || render.isEmpty()) {

			return;
		}
		for (EntityVillager toRender : render) {
			((ESP) Xatz.getModuleByName("ESP")).drawESP(0.5f, 1f, 0.5f, 0.5f, toRender);
		}

		super.onRender();
	}

}
