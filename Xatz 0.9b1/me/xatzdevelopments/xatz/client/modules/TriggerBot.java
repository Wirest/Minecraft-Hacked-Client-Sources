package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.modules.target.AuraUtils;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;
import net.minecraft.entity.EntityLivingBase;

public class TriggerBot extends Module {

	private WaitTimer timer = new WaitTimer();

	public TriggerBot() {
		super("TriggerBot", Keyboard.KEY_NONE, Category.COMBAT, "Attacks the entity you are looking at");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (!timer.hasTimeElapsed(1000 / AuraUtils.getAPS(), true)) {
			return;
		}
		if (mc.objectMouseOver == null) {
			return;
		}
		if (mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
			EntityLivingBase entityHit = (EntityLivingBase) mc.objectMouseOver.entityHit;
			if (!Utils.validEntity(entityHit)
					|| mc.thePlayer.getDistanceToEntity(entityHit) > AuraUtils.getRange()) {
				return;
			}
			AutoBlock.stopBlock();
			Criticals.crit();
			mc.thePlayer.swingItem();
			mc.playerController.attackEntity(mc.thePlayer, entityHit);

			AutoBlock.startBlock();
		}

		super.onUpdate();
	}

}
