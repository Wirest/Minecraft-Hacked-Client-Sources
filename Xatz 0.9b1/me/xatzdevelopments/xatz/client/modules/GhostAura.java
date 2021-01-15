package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.modules.target.AuraUtils;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class GhostAura extends Module {
	WaitTimer timer = new WaitTimer();
	EntityLivingBase en = null;

	public GhostAura() {
		super("GhostAura", Keyboard.KEY_NONE, Category.COMBAT, "An aura that bypasses most Anti-Cheats and looks real.");
	}

	@Override
	public void onToggle() {
		en = null;
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		if (mc.currentScreen != null) {
			return;
		}
		if (en != null && mc.thePlayer.getDistanceToEntity(en) <= AuraUtils.getRange()) {
			double xAim = (en.posX - 0.5) + (en.posX - en.lastTickPosX) * 2.5;
			double yAim = en.posY + (en.getEyeHeight() - en.height / 1.5) - (en.posY - en.lastTickPosY) * 1.5;
			double zAim = (en.posZ - 0.5) + (en.posZ - en.lastTickPosZ) * 2.5;
			Utils.facePos(new Vec3(xAim, yAim, zAim));
		}
		if (!timer.hasTimeElapsed(1000 / AuraUtils.getAPS(), true)) {
			return;
		}
		en = Utils.getClosestEntity((float) AuraUtils.getRange());
		if (en == null) {
			return;
		}
		AutoBlock.stopBlock();
		Criticals.crit();
		Xatz.click();
		AutoBlock.startBlock();
		super.onUpdate();
	}
}
