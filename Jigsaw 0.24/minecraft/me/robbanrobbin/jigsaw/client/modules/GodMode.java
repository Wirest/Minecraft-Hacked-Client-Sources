package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.gui.GuiGameOver;

public class GodMode extends Module {

	public static boolean enabled = false;

	public GodMode() {
		super("GodMode", 0, Category.EXPLOITS, "Makes you invisible and invincible when you die on a vanilla server.");
		enabled = false;
	}
	
	@Override
	public void onToggle() {
		enabled = false;
		super.onToggle();
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if(mc.thePlayer.getHealth() <= 0 || enabled) {
			enabled = true;
			mc.thePlayer.setHealth(mc.thePlayer.getMaxHealth());
			if(mc.currentScreen instanceof GuiGameOver) {
				mc.currentScreen = null;
			}
		}
		super.onUpdate(event);
	}
	
}