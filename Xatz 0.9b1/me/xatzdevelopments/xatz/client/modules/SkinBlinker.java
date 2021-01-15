package me.xatzdevelopments.xatz.client.modules;

import java.util.Set;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinBlinker extends Module {
	WaitTimer timer = new WaitTimer();

	public SkinBlinker() {
		super("SkinBlinker", Keyboard.KEY_NONE, Category.FUN, "Makes your skin blink");
	}

	@Override
	public void onDisable() {
		for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
			mc.gameSettings.setModelPartEnabled(part, true);
		}
		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (!timer.hasTimeElapsed(100, true)) {
			return;
		}
		Set activeParts = mc.gameSettings.getModelParts();
		for (EnumPlayerModelParts part : EnumPlayerModelParts.values()) {
			mc.gameSettings.setModelPartEnabled(part, !activeParts.contains(part));
		}
		super.onUpdate();
	}

}
