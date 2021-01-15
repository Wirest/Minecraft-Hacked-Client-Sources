package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Haste extends Module {

	public Haste() {
		super("Haste", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes you mine faster.");
	}

	@Override
	public void onUpdate() {
		if (mc.thePlayer.onGround) {
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 0, 1));
		}
		super.onUpdate();
	}

}
