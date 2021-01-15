package me.xatzdevelopments.xatz.client.modules;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.module.Module;

public class NoBreakDelay extends Module {

	public NoBreakDelay() {
		super("NoBreakDelay", 0, Category.PLAYER, "Disables the break delay after breaking a block.");
	}
	
	@Override
	public void onUpdate() {
		mc.playerController.blockHitDelay = 0;
		super.onUpdate();
	}
	
}
