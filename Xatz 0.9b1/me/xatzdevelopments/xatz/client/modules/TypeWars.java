package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class TypeWars extends Module {

	float rangeBefore;
	boolean wasToggled;
	int wait;

	public TypeWars() {
		super("TypeWars", Keyboard.KEY_NONE, Category.MINIGAMES,
				"Automatically types the middle-clicked entity's name");
	}

	@Override
	public void onDisable() {
		Xatz.getModuleByName("ExtendedReach").setToggled(wasToggled, true);
		ClientSettings.ExtendedReachrange = rangeBefore;
		super.onDisable();
	}

	@Override
	public void onEnable() {
		wasToggled = Xatz.getModuleByName("ExtendedReach").isToggled();
		Xatz.getModuleByName("ExtendedReach").setToggled(true, true);
		rangeBefore = (float) ClientSettings.ExtendedReachrange;
		ClientSettings.ExtendedReachrange = 100;
		super.onEnable();
	}

	@Override
	public void onUpdate() {
		if (wait < 5) {
			wait++;
			return;
		}
		MovingObjectPosition rayTrace = mc.objectMouseOver;
		if (rayTrace == null) {
			return;
		}
		if (rayTrace.entityHit == null) {
			return;
		}
		Entity hit = rayTrace.entityHit;
		if (hit instanceof EntityPlayer) {
			return;
		}
		if (mc.gameSettings.keyBindPickBlock.isKeyDown()) {
			Xatz.sendChatMessage(hit.getDisplayName().getUnformattedText());
			wait = 0;
		}
		super.onUpdate();
	}

}
