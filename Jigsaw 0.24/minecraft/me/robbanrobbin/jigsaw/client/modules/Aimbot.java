package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class Aimbot extends Module {
	WaitTimer timer = new WaitTimer();
	int leftClickTimer = 0;
	EntityLivingBase en = null;

	public Aimbot() {
		super("Aimbot", Keyboard.KEY_NONE, Category.COMBAT, "Aims for the closest entity. Tick the \"Smooth Aim\" box to make it a smooth aimbot.");
	}

	@Override
	public void onToggle() {
		en = null;
		leftClickTimer = 0;
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		en = Utils.getClosestEntity(10);
		if (en == null) {
			return;
		}
		if (this.currentMode.equals("Click")) {
			if (leftClickTimer > 0) {
				this.onLeftClick();
			}
			return;
		}
		if (currentMode.equals("Click")) {
			return;
		}
		if (mc.currentScreen != null) {
			return;
		}
		if (en != null && mc.thePlayer.getDistanceToEntity(en) <= 10) {
			if (ClientSettings.smoothAim) {
				double xAim = (en.posX - 0.5) + (en.posX - en.lastTickPosX) * 2.5;
				double yAim = en.posY + (en.getEyeHeight() - en.height / 1.5) - (en.posY - en.lastTickPosY) * 1.5;
				double zAim = (en.posZ - 0.5) + (en.posZ - en.lastTickPosZ) * 2.5;
				Utils.facePos(new Vec3(xAim, yAim, zAim));
			} else {
				Utils.faceEntity(en);
			}
		}
		super.onUpdate();
	}

	@Override
	public void onLeftClick() {
		if (!currentMode.equalsIgnoreCase("Click")) {
			return;
		}
		if (mc.currentScreen != null) {
			return;
		}
		if (leftClickTimer > 0) {
			leftClickTimer--;
		} else {
			leftClickTimer = 5;
		}
		en = Utils.getClosestEntity(10);
		if (en == null) {
			return;
		}
		if (en != null) {
			Utils.smoothFacePos(new Vec3((en.posX - 0.5) + (en.posX - en.lastTickPosX) * 3,
					en.posY + (en.getEyeHeight() - en.height / 1.5) - (en.posY - en.lastTickPosY) * 2,
					(en.posZ - 0.5) + (en.posZ - en.lastTickPosZ) * 3));
		}
		super.onLeftClick();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Always", "Click" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}
}
